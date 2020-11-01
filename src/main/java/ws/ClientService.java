package ws;

import dtos.ClientDTO;
import ejbs.ClientBean;
import entities.Client;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/clients")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClientService {

    //region EJB
    @EJB
    private ClientBean clientBean;
    //endregion

    //region DTOS
    public static ClientDTO toDTO(Client client){
        return new ClientDTO(
                client.getUsername(),
                client.getPassword(),
                client.getName(),
                client.getEmail(),
                client.getContact(),
                client.getAddress()
        );
    }

    public static List<ClientDTO> toDTOs(List<Client> clients){
        return clients.stream().map(ClientService::toDTO).collect(Collectors.toList());
    }


    //endregion

    //region CRUD


    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<ClientDTO> getAllClients() {
        return ClientService.toDTOs(clientBean.getAllClients());
    }

    @POST
    @Path("/")
    public Response createNewClient (ClientDTO clientDTO) throws MyEntityExistsException, MyConstraintViolationException {
        clientBean.create(clientDTO.getUsername(),
                clientDTO.getPassword(),
                clientDTO.getName(),
                clientDTO.getEmail(),
                clientDTO.getContact(),
                clientDTO.getAddress()
                );
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("{username}")
    public Response getClientDetails(@PathParam("username") String username) {
        Client client = clientBean.getClient(username);
        if (client != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTO(client))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_CLIENT")
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateClientDetails(@PathParam("username") String username, ClientDTO clientDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        clientBean.update(clientDTO.getUsername(),
                clientDTO.getPassword(),
                clientDTO.getName(),
                clientDTO.getEmail(),
                clientDTO.getContact(),
                clientDTO.getAddress());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteClient(@PathParam("username") String username) throws MyEntityNotFoundException {
        clientBean.delete(username);
        Client client = clientBean.getClient(username);
        if (client == null) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_DELETING_CLIENT")
                .build();
    }
    //endregion
}

package ws;

import dtos.ClientDTO;
import dtos.ErrorDTO;
import ejbs.ClientBean;
import entities.Client;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
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

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion

    //region DTOS
    public static ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getUsername(),
                client.getPassword(),
                client.getName(),
                client.getEmail(),
                client.getContact(),
                client.getAddress(),
                client.getCreated(),
                client.getUpdated()
        );
    }

    public static List<ClientDTO> toDTOs(List<Client> clients) {
        return clients.stream().map(ClientService::toDTO).collect(Collectors.toList());
    }

    public static ClientDTO toDTOUsernameName(Client client) {
        return new ClientDTO(
                client.getUsername(),
                client.getName()
        );
    }

    public static List<ClientDTO> toDTOsUsernameName(List<Client> clients) {
        return clients.stream().map(ClientService::toDTOUsernameName).collect(Collectors.toList());
    }

    public static ClientDTO toDTONoPassword(Client client) {
        return new ClientDTO(
                client.getUsername(),
                client.getName(),
                client.getEmail(),
                client.getContact(),
                client.getAddress(),
                client.getCreated(),
                client.getUpdated()
        );
    }

    public static List<ClientDTO> toDTOsNoPassword(List<Client> clients) {
        return clients.stream().map(ClientService::toDTONoPassword).collect(Collectors.toList());
    }

    //endregion

    //region CRUD


    @GET
    @Path("/")
    public Response getAllClients() {
        if (!(securityContext.isUserInRole("Admin") ||
                securityContext.isUserInRole("Designer"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(ClientService.toDTOsNoPassword(clientBean.getAllClients())).build();
    }

    @POST
    @Path("/")
    public Response createNewClient(ClientDTO clientDTO) throws MyEntityExistsException, MyConstraintViolationException {
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
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||
                securityContext.isUserInRole("Designer") ||
                securityContext.isUserInRole("Client") &&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Client client = clientBean.getClient(username);
        if (client != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTONoPassword(client))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR_FINDING_CLIENT"))
                .build();
    }
    //TODO put error
    @PUT
    @Path("{username}")
    public Response updateClientDetails(@PathParam("username") String username, ClientDTO clientDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||
                securityContext.isUserInRole("Designer") || //TODO ver
                securityContext.isUserInRole("Client") &&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        clientBean.update(username,
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
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||
                securityContext.isUserInRole("Client") &&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        clientBean.delete(username);
        Client client = clientBean.getClient(username);
        if (client == null) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR_DELETING_CLIENT"))
                .build();
    }
    //endregion




    @GET
    @Path("{username}/projects")
    public Response getClientProjects(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||
                securityContext.isUserInRole("Designer") ||
                securityContext.isUserInRole("Client") &&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Client client = clientBean.getClient(username);
        if (client == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR_FINDING_CLIENT"))
                .build();
        }

        List<Project> projects = clientBean.getAllClientProjects(username);
        return Response.status(Response.Status.ACCEPTED).entity(ProjectService.toDTOs(projects)).build();
    }

    @GET
    @Path("search/{name}")
    public Response searchClientsByName(@PathParam("name") String name) {
        return Response.status(Response.Status.ACCEPTED).entity(ClientService.toDTOsUsernameName(clientBean.searchClientsByName(name))).build();
    }

}

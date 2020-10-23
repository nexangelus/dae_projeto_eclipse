package ws;


import dtos.AdminDTO;
import dtos.ClientDTO;
import ejbs.AdminBean;
import ejbs.ClientBean;
import entities.Admin;
import entities.Client;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/clients") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class ClientService {

    public static ClientDTO toDTO(Client client){
        return new ClientDTO(
                client.getUsername(),
                client.getPassword(),
                client.getName(),
                client.getEmail()
                //TODO acabar
        );
    }

    public static List<ClientDTO> toDTOs(List<Client> clients){
        return clients.stream().map(ClientService::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<ClientDTO> getAllClientsWS() {
        return ClientService.toDTOs(clientBean.getAllClients());
    }

    @EJB
    private ClientBean clientBean;
}

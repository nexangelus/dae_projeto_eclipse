package ws;


import dtos.AdminDTO;
import ejbs.AdminBean;
import entities.Admin;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/admins") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class AdminService {

    public static AdminDTO toDTO(Admin admin){
        return new AdminDTO(
                admin.getUsername(),
                admin.getPassword(),
                admin.getName(),
                admin.getEmail()
        );
    }

    public static List<AdminDTO> toDTOs(List<Admin> admins){
        return admins.stream().map(AdminService::toDTO).collect(Collectors.toList());
    }

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<AdminDTO> getAllAdminsWS() {
        return AdminService.toDTOs(adminBean.getAllAdmins());
    }

    @EJB
    private AdminBean adminBean;
}

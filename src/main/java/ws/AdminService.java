package ws;


import dtos.AdminDTO;
import ejbs.AdminBean;
import entities.Admin;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/admins")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdminService {

    //region EJB
    @EJB
    private AdminBean adminBean;
    //endregion

    //region DTOS
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
    //endregion

    //region CRUD

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/") // means: the relative url path is “/api/students/”
    public List<AdminDTO> getAllAdmins() {
        return AdminService.toDTOs(adminBean.getAllAdmins());
    }

    @POST
    @Path("/")
    public Response createNewAdmin (AdminDTO adminDTO) throws MyEntityExistsException, MyConstraintViolationException {
        adminBean.create(adminDTO.getUsername(),
                adminDTO.getPassword(),
                adminDTO.getName(),
                adminDTO.getEmail());
        return Response.status(Response.Status.CREATED).entity(toDTO(adminBean.getAdmin(adminDTO.getUsername()))).build();
    }

    @GET
    @Path("{username}")
    public Response getAdminDetails(@PathParam("username") String username) {
        Admin admin = adminBean.getAdmin(username);
        if (admin != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTO(admin))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_ADMIN")
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateAdminDetails(@PathParam("username") String username, AdminDTO adminDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        adminBean.update(adminDTO.getUsername(),
                adminDTO.getPassword(),
                adminDTO.getName(),
                adminDTO.getEmail());
        return Response.status(Response.Status.OK).entity(toDTO(adminBean.getAdmin(username))).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAdmin(@PathParam("username") String username) throws MyEntityNotFoundException {
        adminBean.delete(username);
        Admin admin = adminBean.getAdmin(username);
        if (admin == null) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_DELETING_ADMIN")
                .build();
    }

    //endregion

}

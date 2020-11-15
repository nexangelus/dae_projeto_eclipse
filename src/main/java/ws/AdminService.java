package ws;


import dtos.AdminDTO;
import dtos.ErrorDTO;
import ejbs.AdminBean;
import entities.Admin;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion

    //region DTOS
    public static AdminDTO toDTO(Admin admin){
        return new AdminDTO(
                admin.getUsername(),
                admin.getPassword(),
                admin.getName(),
                admin.getEmail(),
                admin.getCreated(),
                admin.getUpdated()
        );
    }

    public static List<AdminDTO> toDTOs(List<Admin> admins){
        return admins.stream().map(AdminService::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region CRUD

    @GET
    @Path("/")
    public Response getAllAdmins() {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(AdminService.toDTOs(adminBean.getAllAdmins())).build();
    }

    @POST
    @Path("/")
    public Response createNewAdmin (AdminDTO adminDTO) throws MyEntityExistsException, MyConstraintViolationException {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        adminBean.create(adminDTO.getUsername(),
                adminDTO.getPassword(),
                adminDTO.getName(),
                adminDTO.getEmail());
        return Response.status(Response.Status.CREATED).entity(toDTO(adminBean.getAdmin(adminDTO.getUsername()))).build();
    }

    @GET
    @Path("{username}")
    public Response getAdminDetails(@PathParam("username") String username) {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Admin admin = adminBean.getAdmin(username);
        if (admin != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTO(admin))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR_FINDING_ADMIN"))
                .build();
    }

    @PUT
    @Path("{username}")
    public Response updateAdminDetails(@PathParam("username") String username, AdminDTO adminDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        adminBean.update(username,
                adminDTO.getPassword(),
                adminDTO.getName(),
                adminDTO.getEmail());
        return Response.status(Response.Status.OK).entity(toDTO(adminBean.getAdmin(username))).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteAdmin(@PathParam("username") String username) throws MyEntityNotFoundException {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        adminBean.delete(username);
        Admin admin = adminBean.getAdmin(username);
        if (admin == null) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR_DELETING_ADMIN"))
                .build();
    }

    //endregion

}

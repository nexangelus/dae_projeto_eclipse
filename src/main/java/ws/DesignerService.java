package ws;

import dtos.DesignerDTO;
import dtos.ErrorDTO;
import ejbs.AccountBean;
import ejbs.DesignerBean;
import entities.Account;
import entities.Client;
import entities.Designer;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/designers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class DesignerService {

    //region EJB
    @EJB
    private DesignerBean designerBean;

    @EJB
    private AccountBean accountBean;
    //endregion

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion

    //region DTOS
    public static DesignerDTO toDTO(Designer designer) {
        return new DesignerDTO(
                designer.getUsername(),
                designer.getName(),
                designer.getEmail(),
                designer.getCreated(),
                designer.getUpdated()
        );
    }

    public static List<DesignerDTO> toDTOs(List<Designer> designers) {
        return designers.stream().map(DesignerService::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region CRUD
    @GET
    @Path("/")
    @RolesAllowed({"Admin"})
    public Response getAll() {
        return Response.status(Response.Status.ACCEPTED).entity(toDTOs(designerBean.getAllDesigners())).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"Admin","Designer"})
    public Response get(@PathParam("username") String username) {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") || securityContext.isUserInRole("Designer") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Designer designer = designerBean.getDesigner(username);
        if (designer != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTO(designer))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR FINDING DESIGNER"))
                .build();
    }

    @POST
    @Path("/{code}")
    public Response create(@PathParam("code") String code, DesignerDTO designer) throws MyEntityExistsException, MyConstraintViolationException {
        Account account =accountBean.getAccountEmail(designer.getEmail());
        if(account==null || !(account.getCode().equals(code)) || !(account.getGroupType().equals("Designer")))
            return Response.status(Response.Status.FORBIDDEN).build();
        designerBean.create(designer.getUsername(), designer.getNewPassword(), designer.getName(), designer.getEmail());
        Designer newDesigner = designerBean.getDesigner(designer.getUsername());
        if (newDesigner == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(newDesigner))
                .build();
    }

    @PUT
    @Path("{username}")
    @RolesAllowed({"Designer"})
    public Response update(@PathParam("username") String username, DesignerDTO designer) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Designer") &&
                principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        designerBean.update(username, designer.getNewPassword(), designer.getOldPassword(), designer.getName(), designer.getEmail());

        return Response.status(Response.Status.OK)
                .entity(toDTO(designerBean.getDesigner(username)))
                .build();
    }

    @DELETE
    @Path("{username}")
    @RolesAllowed({"Admin"})
    public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {
        designerBean.delete(username);
        if (designerBean.getDesigner(username) != null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.error("ERROR_WHILE_DELETING"))
                    .build();
        return Response.status(Response.Status.OK)
                .entity(ErrorDTO.error("SUCCESS"))
                .build();
    }

    //endregion

    @GET
    @Path("{username}/projects")
    @RolesAllowed({"Admin","Designer"})
    public Response getProjects(@PathParam("username") String username){
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||
                securityContext.isUserInRole("Designer") &&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Designer designer = designerBean.getDesigner(username);
        if (designer == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorDTO.error("ERROR_FINDING_CLIENT"))
                    .build();
        }

        List<Project> projects = designerBean.getDesignerProjects(username);
        return Response.status(Response.Status.ACCEPTED).entity(ProjectService.toDTOs(projects)).build();

    }

}

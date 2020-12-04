package ws;

import dtos.ErrorDTO;
import dtos.StructureDTO;
import ejbs.StructureBean;
import entities.Structure;
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

@Path("/projects/{idP}/structures")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StructureService {

    //region EJB
    @EJB
    private StructureBean structureBean;
    //endregion

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion

    //region DTOS
    public static StructureDTO toDTO(Structure structure) {
        return new StructureDTO(
                structure.getName(),
                structure.getParameters(),
                structure.getClientObservations(),
                structure.getProject(),
                structure.isVisibleToClient(),
                structure.isClientAccepted(),
                structure.getCreated(),
                structure.getUpdated()
        );
    }

    public static List<StructureDTO> toDTOs(List<Structure> structures) {
        return structures.stream().map(StructureService::toDTO).collect(Collectors.toList());
    }
    //endregion

    //region CRUD
    @GET
    @Path("/")
    public List<StructureDTO> getAll() {
        return toDTOs(structureBean.getAllStructures());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") long id) {
        Structure structure = structureBean.findStructure(id);
        if (structure != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTO(structure))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR FINDING PROJECT"))
                .build();
    }

    @POST
    @Path("/")
    public Response create(@PathParam("idP") long idP,StructureDTO structure) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        long id = structureBean.create(
                idP,
                structure.getName(),
                structure.getParameters()
        );
        Structure newStructure = structureBean.findStructure(id);
        if (newStructure == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(newStructure))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("idP") long idP, @PathParam("id") long id, StructureDTO structure) throws MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||  securityContext.isUserInRole("Designer") &&
                principal.getName().equals(structure.getProject().getDesigner()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        structureBean.update(
                id,
                structure.getName(),
                structure.getParameters(),
                structure.isVisibleToClient(),
                structure.isVisibleToClient(),
                structure.getClientObservations()
        );
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        structureBean.delete(
                id
        );
        return Response.status(Response.Status.OK).build();

    }

    //endregion

}

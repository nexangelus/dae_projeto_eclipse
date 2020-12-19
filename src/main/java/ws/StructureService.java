package ws;

import dtos.ErrorDTO;
import dtos.MaterialDTO;
import dtos.SimulateDTO;
import dtos.StructureDTO;
import dtos.materials.ProfileDTO;
import ejbs.*;
import entities.Material;
import entities.Profile;
import entities.Project;
import entities.Structure;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/projects/{idP}/structures")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class StructureService {
    //TODO Validar todo o ficheiro
    //region EJB
    @EJB
    private ProjectBean projectBean;

    @EJB
    private StructureBean structureBean;

    @EJB
    private EmailBean emailBean;

    @EJB
    private SimulationBean simulationBean;

    @EJB
    private ProfileBean profileBean;
    //endregion

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion

    //region DTOS
    public static StructureDTO toDTO(Structure structure) {
        return new StructureDTO(
                structure.getId(),
                structure.getName(),
                structure.getNb(),
                structure.getLVao(),
                structure.getQ(),
                structure.getClientObservations(),
                ProjectService.toDTO(structure.getProject()),
                structure.isVisibleToClient(),
                structure.isClientAccepted(),
                structure.getCreated(),
                structure.getUpdated()
        );
    }

    public static List<StructureDTO> toDTOs(List<Structure> structures) {
        return structures.stream().map(StructureService::toDTO).collect(Collectors.toList());
    }

    public static StructureDTO toDTOWithoutProject(Structure structure) {
        return new StructureDTO(
                structure.getId(),
                structure.getName(),
                structure.getNb(),
                structure.getLVao(),
                structure.getQ(),
                structure.getClientObservations(),
                structure.isVisibleToClient(),
                structure.isClientAccepted(),
                structure.getCreated(),
                structure.getUpdated()
        );
    }

    public static List<StructureDTO> toDTOsWithoutProject(List<Structure> structures) {
        return structures.stream().map(StructureService::toDTOWithoutProject).collect(Collectors.toList());
    }

    public static StructureDTO toDTOWithoutProjectWithMaterials(Structure structure) {
        StructureDTO structureDTO =  new StructureDTO(
                structure.getId(),
                structure.getName(),
                structure.getNb(),
                structure.getLVao(),
                structure.getQ(),
                structure.getClientObservations(),
                structure.isVisibleToClient(),
                structure.isClientAccepted(),
                structure.getCreated(),
                structure.getUpdated()
        );
        Set<Material> materialSet =  structure.getMaterials();

        return structureDTO;
    }

    public static List<StructureDTO> toDTOsWithoutProjectWithMaterials(List<Structure> structures) {
        return structures.stream().map(StructureService::toDTOWithoutProject).collect(Collectors.toList());
    }
    //endregion

    //region CRUD
    @GET
    @Path("/")
    //TODO manufacturer list of structs accepted by client
    public List<StructureDTO> getAll() {
        return toDTOs(structureBean.getAllStructures());
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"Designer", "Client"})
    public Response get(@PathParam("idP") long idP, @PathParam("id") long id) {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Designer") && !principal.getName().equals(project.getDesigner().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (securityContext.isUserInRole("Client") && !principal.getName().equals(project.getClient().getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Structure structure = structureBean.findStructure(id);
        if (structure != null) {
            return Response.status(Response.Status.OK)
                    .entity(toDTOWithoutProjectWithMaterials(structure))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorDTO.error("ERROR FINDING PROJECT"))
                .build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Designer"})
    public Response create(@PathParam("idP") long idP,StructureDTO structure) throws MyConstraintViolationException, MyEntityNotFoundException {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Designer") && !principal.getName().equals(project.getDesigner().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        long id = structureBean.create(
                idP,
                structure.getName(),
                structure.getNb(),
                structure.getLVao(),
                structure.getQ()
        );
        Structure newStructure = structureBean.findStructure(id);
        if (newStructure == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        try {

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(newStructure))
                    .build();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"Designer"})
    public Response update(@PathParam("idP") long idP, @PathParam("id") long id, StructureDTO structure) throws MyEntityNotFoundException, MyConstraintViolationException {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Designer") && !principal.getName().equals(project.getDesigner().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        structureBean.update(
                id,
                structure.getName(),
                structure.getNb(),
                structure.getLVao(),
                structure.getQ()
        );
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"Designer"})
    public Response delete(@PathParam("idP") long idP,@PathParam("id") long id) throws MyEntityNotFoundException {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Designer") && !principal.getName().equals(project.getDesigner().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (securityContext.isUserInRole("Client") && !principal.getName().equals(project.getClient().getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        structureBean.delete(
                id
        );
        return Response.status(Response.Status.OK).build();

    }

    @PATCH
    @Path("{id}/visibletoclient")
    @RolesAllowed({"Designer"})
    public Response visibleToClient(@PathParam("idP") long idP,@PathParam("id") long id) throws MyEntityNotFoundException, MyConstraintViolationException {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Designer") && !principal.getName().equals(project.getDesigner().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        structureBean.visibleToClient(id);
        emailBean.send(project.getClient().getEmail(), "Structure finished", "Structure has been finished");

        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Path("{id}/status/{status}")
    @RolesAllowed({"Client"})
    public Response isAccepted(@PathParam("idP") long idP,@PathParam("id") long id, @PathParam("status") boolean status) throws MyEntityNotFoundException, MyConstraintViolationException {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if (securityContext.isUserInRole("Client") && !principal.getName().equals(project.getClient().getUsername())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        structureBean.setStatus(id, status);
        String message;
        if(status){
            message = "accepted";
        }else{
            message = "declined";
        }
        emailBean.send(project.getDesigner().getEmail(), "Structure Updated by client", "Structure of project: "+idP+" has been :" + message);
        return Response.status(Response.Status.OK).build();
    }

    //endregion
    @POST
    @RolesAllowed({"Designer"})
    @Path("{id}/simulate")
    public Response isAccepted(@PathParam("idP") long idP, @PathParam("id") long id, SimulateDTO simulate, MaterialDTO materialDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        Project project = projectBean.getProject(idP);
        Principal principal = securityContext.getUserPrincipal();
        if(securityContext.isUserInRole("Designer") && !principal.getName().equals(project.getDesigner().getUsername())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Structure structure = structureBean.findStructure(id);
        Profile profile = profileBean.getProfile(materialDTO.getId());
        simulationBean.simulaVariante(
                simulate.getNb(),simulate.getLVao(),simulate.getQ(),profile
        );

        return Response.status(Response.Status.OK).build();
    }

}

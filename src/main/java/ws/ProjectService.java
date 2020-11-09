package ws;

import dtos.ManufacturerDTO;
import ejbs.ManufacturerBean;
import ejbs.ProjectBean;
import entities.Manufacturer;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

@Path("/manufacturers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProjectService {

    //region EJB
    @EJB
    private ProjectBean projectBean;
    //endregion

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion

    //region DTOS
    public static ProjectDTO toDTO(Project project) {
        //TODO
    }

    public static List<ProjectDTO> toDTOs(List<Project> projects) {
        //TODO
        return projects.stream().map(ProjectService::toDTO).collect(Collectors.toList());
    }
    //endregion
}

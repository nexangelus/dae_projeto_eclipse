package ws;

import dtos.ErrorDTO;
import dtos.ManufacturerDTO;
import dtos.ProjectDTO;
import ejbs.ClientBean;
import ejbs.DesignerBean;
import ejbs.ProjectBean;
import entities.Client;
import entities.Designer;
import entities.Manufacturer;
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
import java.util.List;
import java.util.stream.Collectors;

@Path("/projects")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProjectService {
	//region EJB
	@EJB
	private ProjectBean projectBean;
	@EJB
	private ClientBean clientBean;
	@EJB
	private DesignerBean designerBean;
	//endregion

	//region Security
	@Context
	private SecurityContext securityContext;
	//endregion

	//region DTOS
	public static ProjectDTO toDTO(Project project) {
		return new ProjectDTO(
				project.getId(),
				project.getClient().getUsername(),
				project.getDesigner().getUsername(),
				project.getTitle(),
				project.getDescription(),
				project.getObservations(),
				project.getFinished(),
				project.getCreated(),
				project.getUpdated()
		);
	}

	public static List<ProjectDTO> toDTOs(List<Project> projects) {
		return projects.stream().map(ProjectService::toDTO).collect(Collectors.toList());
	}
	//endregion

	//region CRUD
	@GET
	@Path("/")
	public List<ProjectDTO> getAll() {
		return toDTOs(projectBean.getAllProjects());
	}

	@GET
	@Path("{id}")
	public Response get(@PathParam("id") long id) {
		Project project = projectBean.getProject(id);
		if (project != null) {
			return Response.status(Response.Status.OK)
					.entity(toDTO(project))
					.build();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(ErrorDTO.error("ERROR FINDING PROJECT"))
				.build();
	}

	@POST
	@Path("/")
	public Response create(ProjectDTO project) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
		long id = projectBean.create(
				project.getClientUsername(),
				project.getDesignerUsername(),
				project.getTitle(),
				project.getDescription()
		);

		Project newProject = projectBean.getProject(id);
		if (newProject == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED)
				.entity(toDTO(newProject))
				.build();
	}

	@PUT
	@Path("{username}")
	public Response update(@PathParam("username") String username, ManufacturerDTO manufacturer) throws MyEntityNotFoundException, MyConstraintViolationException {
/* // TODO
		manufacturerBean.update(username, manufacturer.getPassword(), manufacturer.getName(),
				manufacturer.getEmail(), manufacturer.getAddress(), manufacturer.getContact(), manufacturer.getWebsite());
*/
		return Response.status(Response.Status.OK)
				/*.entity(toDTO(manufacturerBean.getManufacturer(username)))*/
				.build();
	}

	@DELETE
	@Path("{username}")
	public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {
		// TODO
		/*manufacturerBean.delete(username);

		if (manufacturerBean.getManufacturer(username) != null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(ErrorDTO.error("ERROR_WHILE_DELETING"))
					.build();*/

		return Response.status(Response.Status.OK)
				.entity(ErrorDTO.error("SUCCESS"))
				.build();

	}

	//endregion
}

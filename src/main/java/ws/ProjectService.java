package ws;

import dtos.ErrorDTO;
import dtos.ManufacturerDTO;
import dtos.ProjectDTO;
import ejbs.ClientBean;
import ejbs.DesignerBean;
import ejbs.ProjectBean;
import ejbs.UploadBean;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.FileOutputStream;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
	@EJB
	private UploadBean uploadBean;
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

	private String getFilename(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
		System.out.println("Written: " + filename);
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

	@POST
	@Path("{id}/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@PathParam("id") long projectID, MultipartFormDataInput input) throws MyEntityNotFoundException, IOException {
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		Project project = projectBean.getProject(projectID);
		List<InputPart> inputParts = uploadForm.get("file");
		for (InputPart inputPart : inputParts) {
			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String filename = getFilename(header);
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				String path = System.getProperty("user.home") + File.separator + "uploads";
				File customDir = new File(path);
				if (!customDir.exists()) {
					customDir.mkdir();
				}
				String filepath = customDir.getCanonicalPath() + File.separator + filename;
				writeFile(bytes, filepath);
				uploadBean.create(project.getId(), path, filename);
				return Response.status(200).entity("Uploaded file name : " + filename).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

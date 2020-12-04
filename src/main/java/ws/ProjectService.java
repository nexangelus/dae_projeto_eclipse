package ws;

import dtos.ErrorDTO;
import dtos.ProjectDTO;
import dtos.StructureDTO;
import dtos.UploadDTO;
import ejbs.ClientBean;
import ejbs.DesignerBean;
import ejbs.ProjectBean;
import ejbs.UploadBean;
import entities.Project;
import entities.Structure;
import entities.Upload;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	public static UploadDTO toDTOUpload(Upload upload){
		return new UploadDTO(
				upload.getId(), upload.getFilepath(), upload.getFilename()
		);
	}

	public static List<UploadDTO> toDTOsUploads(List<Upload> uploads){
		return uploads.stream().map(ProjectService::toDTOUpload).collect(Collectors.toList());
	}

	public static ProjectDTO toDTOWithUpload(Project project){
		ProjectDTO projectDTO = toDTO(project);
		Set<Upload> uploads = project.getUploads();
		List<UploadDTO> uploadDTO = toDTOsUploads(new ArrayList<>(uploads));
		projectDTO.setUploadDTOS(uploadDTO);
		return projectDTO;
	}

	public static ProjectDTO toDTOWithUploadStructure(Project project){
		ProjectDTO projectDTO = toDTOWithUpload(project);
		Set<Structure> structures = project.getStructures();
		projectDTO.setStructureDTOS(StructureService.toDTOs(new ArrayList<>(structures)));
		return projectDTO;
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
					.entity(toDTOWithUploadStructure(project))
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
	@Path("{id}")
	public Response update(@PathParam("id") long id, ProjectDTO project) throws MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Admin") ||  securityContext.isUserInRole("Designer") &&
				principal.getName().equals(project.getDesignerUsername()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        projectBean.update(
				id,
				project.getTitle(),
				project.getDescription(),
				project.getObservations()
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
		projectBean.delete(
				id
		);
		return Response.status(Response.Status.OK).build();

	}

	//endregion

	@POST
	@Path("{id}/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@PathParam("id") long projectID, MultipartFormDataInput input) throws MyEntityNotFoundException, IOException {
		Principal principal = securityContext.getUserPrincipal();

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		Project project = projectBean.getProject(projectID);
		if (!(securityContext.isUserInRole("Admin") ||
				securityContext.isUserInRole("Client") &&
						principal.getName().equals(project.getClient().getUsername()))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
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

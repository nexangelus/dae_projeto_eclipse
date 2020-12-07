package ws;

import dtos.ErrorDTO;
import dtos.ProjectDTO;
import dtos.DocumentDTO;
import ejbs.ClientBean;
import ejbs.DesignerBean;
import ejbs.ProjectBean;
import ejbs.DocumentBean;
import entities.Document;
import entities.Project;
import entities.Structure;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import org.apache.commons.io.FileUtils;
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
	private DocumentBean documentBean;
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

	public static DocumentDTO toDTODocument(Document document){
		return new DocumentDTO(
				document.getId(), document.getFilepath(), document.getFilename()
		);
	}

	public static List<DocumentDTO> toDTOsUploads(List<Document> documents){
		return documents.stream().map(ProjectService::toDTODocument).collect(Collectors.toList());
	}

	public static ProjectDTO toDTOWithDocument(Project project){
		ProjectDTO projectDTO = toDTO(project);
		Set<Document> documents = project.getDocuments();
		List<DocumentDTO> uploadDTO = toDTOsUploads(new ArrayList<>(documents));
		projectDTO.setUploadDTOS(uploadDTO);
		return projectDTO;
	}

	public static ProjectDTO toDTOWithUploadStructure(Project project){
		ProjectDTO projectDTO = toDTOWithDocument(project);
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
	public Response getAll() {
		Principal principal = securityContext.getUserPrincipal();
		if (!(securityContext.isUserInRole("Admin"))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		return Response.status(Response.Status.OK)
				.entity(toDTOs(projectBean.getAllProjects()))
				.build();
	}

	@GET
	@Path("{id}")
	public Response get(@PathParam("id") long id) {
		//Todo struct visible to client
		Project project = projectBean.getProject(id);
		Principal principal = securityContext.getUserPrincipal();
		if (!(securityContext.isUserInRole("Admin") ||
				(securityContext.isUserInRole("Designer") && principal.getName().equals(project.getDesigner().getUsername())) ||
				(securityContext.isUserInRole("Client") && principal.getName().equals(project.getClient().getUsername())) )) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
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
		if (!(securityContext.isUserInRole("Admin") ||  securityContext.isUserInRole("Designer"))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
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
        if (!(securityContext.isUserInRole("Admin") ||
				(securityContext.isUserInRole("Designer") && principal.getName().equals(project.getDesigner().getUsername()))
		)) {
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
	public Response uploadDocument(@PathParam("id") long projectID, MultipartFormDataInput input) throws MyEntityNotFoundException, IOException {
		Principal principal = securityContext.getUserPrincipal();
		Project project = projectBean.getProject(projectID);
		if (!(securityContext.isUserInRole("Admin") ||
				(securityContext.isUserInRole("Client") && principal.getName().equals(project.getClient().getUsername()))
		)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
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
				documentBean.create(project.getId(), path, filename);
				return Response.status(200).entity("Uploaded file name : " + filename).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@GET
	@Path("{idP}/download/{idD}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadDocument(@PathParam("idP") Integer idP, @PathParam("idD") Integer idD) throws MyEntityNotFoundException
	{
		Project project = projectBean.getProject(idP);
		Principal principal = securityContext.getUserPrincipal();
		if (!(securityContext.isUserInRole("Admin") ||
				(securityContext.isUserInRole("Designer") && principal.getName().equals(project.getDesigner().getUsername())) ||
				(securityContext.isUserInRole("Client") && principal.getName().equals(project.getClient().getUsername())) )) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Document document = documentBean.findDocument(idD);
		File fileDownload = new File(document.getFilepath() + File.separator +
				document.getFilename());
		Response.ResponseBuilder response = Response.ok((Object) fileDownload);
		response.header("Content-Disposition", "attachment;filename=" +
				document.getFilename());
		return response.build();
	}

	@DELETE
	@Path("{idP}/delete/{idD}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response deleteDocument(@PathParam("idP") Integer idP, @PathParam("idD") Integer idD) throws MyEntityNotFoundException, MyConstraintViolationException {
		Project project = projectBean.getProject(idP);
		Principal principal = securityContext.getUserPrincipal();
		if (!(securityContext.isUserInRole("Admin") ||
				securityContext.isUserInRole("Client") &&
						principal.getName().equals(project.getClient().getUsername()))) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
		Document document = documentBean.findDocument(idD);
		File fileDelete = new File(document.getFilepath() + File.separator + document.getFilename());
		FileUtils.deleteQuietly(fileDelete);
		File fileCheck = new File(document.getFilepath() + File.separator +  document.getFilename());
		if (!fileCheck.exists()){
			project.removeDocument(document);
			documentBean.delete(document.getId());
			return Response.status(Response.Status.ACCEPTED).build();
		}
		return Response.status(Response.Status.CONFLICT).build();
	}
}

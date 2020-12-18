package ws;

import dtos.ErrorDTO;
import dtos.ManufacturerDTO;
import ejbs.DocumentBean;
import ejbs.ManufacturerBean;
import entities.Manufacturer;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import utils.Excel;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/manufacturers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed({"Admin", "Manufacturer"})
public class ManufacturerService {

	//region EJB
	@EJB
	private ManufacturerBean manufacturerBean;

	@EJB
	private Excel excel;
	//endregion

	//region Security
	@Context
	private SecurityContext securityContext;
	//endregion

	//region DTOS
	public static ManufacturerDTO toDTO(Manufacturer manufacturer) {
		return new ManufacturerDTO(
				manufacturer.getUsername(),
				manufacturer.getName(),
				manufacturer.getEmail(),
				manufacturer.getAddress(),
				manufacturer.getWebsite(),
				manufacturer.getContact(),
				manufacturer.getCreated(),
				manufacturer.getUpdated()
		);
	}

	public static List<ManufacturerDTO> toDTOs(List<Manufacturer> manufacturers) {
		return manufacturers.stream().map(ManufacturerService::toDTO).collect(Collectors.toList());
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

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed({"Manufacturer"})
	public Response uploadExcel(MultipartFormDataInput input) {
		Principal principal = securityContext.getUserPrincipal();
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		for (InputPart inputPart : inputParts) {
			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				String filename = getFilename(header);
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				String path = System.getProperty("user.home") + File.separator + "uploads" + File.separator + "manufacturer";
				File customDir = new File(path);
				if (!customDir.exists()) {
					customDir.mkdir();
				}
				String filepath = customDir.getCanonicalPath() + File.separator + filename;
				writeFile(bytes, filepath);
				excel.readFromExcel(filepath, principal.getName());
				File file = new File(System.getProperty("user.home") + File.separator + "uploads" + File.separator + "manufacturer");
				FileUtils.cleanDirectory(file);
				return Response.status(200).build();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@GET
	@Path("/")
	@RolesAllowed({"Admin"})
	public List<ManufacturerDTO> getAll() {
		return toDTOs(manufacturerBean.getAllManufactures());
	}

	@GET
	@Path("{username}")
	public Response get(@PathParam("username") String username) {
		Principal principal = securityContext.getUserPrincipal();
		if (securityContext.isUserInRole("Manufacturer") && !principal.getName().equals(username)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		Manufacturer manufacturer = manufacturerBean.getManufacturer(username);
		if (manufacturer != null) {
			return Response.status(Response.Status.OK)
					.entity(toDTO(manufacturer))
					.build();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(ErrorDTO.error("ERROR FINDING MANUFACTURER"))
				.build();
	}

	@POST
	@Path("/")
	@RolesAllowed({"Admin"})
	public Response create(ManufacturerDTO manufacturer) throws MyEntityExistsException, MyConstraintViolationException {
		manufacturerBean.create(manufacturer.getUsername(), manufacturer.getNewPassword(),
				manufacturer.getName(), manufacturer.getEmail(), manufacturer.getAddress(),
				manufacturer.getWebsite(), manufacturer.getContact());

		Manufacturer newManufacturer = manufacturerBean.getManufacturer(manufacturer.getUsername());
		if (newManufacturer == null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

		return Response.status(Response.Status.CREATED)
				.entity(toDTO(newManufacturer))
				.build();
	}

	@PUT
	@Path("{username}")
	public Response update(@PathParam("username") String username, ManufacturerDTO manufacturer) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
		Principal principal = securityContext.getUserPrincipal();
		if (securityContext.isUserInRole("Manufacturer") && !principal.getName().equals(username)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		manufacturerBean.update(username, manufacturer.getNewPassword(), manufacturer.getOldPassword(), manufacturer.getName(),
				manufacturer.getEmail(), manufacturer.getAddress(), manufacturer.getContact(), manufacturer.getWebsite());

		return Response.status(Response.Status.OK)
				.entity(toDTO(manufacturerBean.getManufacturer(username)))
				.build();
	}

	@DELETE
	@Path("{username}")
	@RolesAllowed({"Admin"})
	public Response delete(@PathParam("username") String username) throws MyEntityNotFoundException {

		manufacturerBean.delete(username);

		if (manufacturerBean.getManufacturer(username) != null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(ErrorDTO.error("ERROR_WHILE_DELETING"))
					.build();

		return Response.status(Response.Status.OK)
				.entity(ErrorDTO.error("SUCCESS"))
				.build();

	}
	//endregion

}

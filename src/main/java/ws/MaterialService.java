package ws;


import dtos.DesignerDTO;
import dtos.ErrorDTO;
import dtos.MaterialDTO;
import dtos.materials.ProfileDTO;
import dtos.materials.SheetDTO;
import ejbs.DesignerBean;
import ejbs.MaterialBean;
import entities.*;
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

@Path("/materials")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed({"Admin", "Manufacturer", "Designer"})
public class MaterialService {

	//region EJB
	@EJB
	private MaterialBean materialBean;
	//endregion

	//region Security
	@Context
	private SecurityContext securityContext;
	//endregion

	//region DTOS
	public static MaterialDTO toDTO(Material material) {
		if(material.getClass() == Profile.class) {
			return toDTO((Profile)material);
		} else if (material.getClass() == Sheet.class) {
			return toDTO((Sheet)material);
		}
		return null;
	}

	public static MaterialDTO toDTO(Profile profile) {
		return new MaterialDTO(
				profile.getId(),
				profile.getName(),
				profile.getManufacturer().getName(),
				profile.getManufacturer().getUsername(),
				FamilyService.toDTONoManufacturerMaterialsCount(profile.getFamily()),
				new ProfileDTO(
						profile.getWeff_p(),
						profile.getWeff_n(),
						profile.getAr(),
						profile.getSigmaC(),
						profile.getPp(),
						profile.getMcr_p(),
						profile.getMcr_n()
				),
				profile.getCreated(),
				profile.getUpdated()
		);
	}

	public static MaterialDTO toDTO(Sheet sheet) {
		return new MaterialDTO(
				sheet.getId(),
				sheet.getName(),
				sheet.getManufacturer().getName(),
				sheet.getManufacturer().getUsername(),
				FamilyService.toDTONoManufacturerMaterialsCount(sheet.getFamily()),
				new SheetDTO(
						sheet.getThickness()
				),
				sheet.getCreated(),
				sheet.getUpdated()
		);
	}

	public static List<MaterialDTO> toDTOs(List<Material> materials) {
		return materials.stream().map(MaterialService::toDTO).collect(Collectors.toList());
	}
	//endregion

	//region CRUD
	@GET
	@Path("/")
	public Response getAll() {
		List<Material> allMaterials = materialBean.getAllMaterials();
		return Response.status(Response.Status.ACCEPTED).entity(toDTOs(allMaterials)).build();
	}
	// TODO Devolver apenas os meus materiais (manufacturer) e ver quais os materiais estão em projetos

	@GET
	@Path("{id}")
	public Response get(@PathParam("id") long id) {
		Material material = materialBean.getMaterial(id);
		if (material != null) {
			return Response.status(Response.Status.OK)
					.entity(toDTO(material))
					.build();
		}

		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(ErrorDTO.error("ERROR FINDING MATERIAL"))
				.build();
	}

	@POST
	@Path("/")
	@RolesAllowed({"Admin", "Manufacturer"})
	public Response create(MaterialDTO material) throws MyConstraintViolationException, MyIllegalArgumentException, MyEntityNotFoundException {
		Material created = null;
		if(material.getProfile() != null) { // is creating a profile
			created = materialBean.getMaterial(materialBean.createProfile(material
					.getFamily().getId(), material.getManufacturerUsername(), material.getName(), material.getProfile().getWeff_p(), material.getProfile().getWeff_n(), material.getProfile().getAr(), material.getProfile().getSigmaC(), material.getProfile().getMcr_p(), material.getProfile().getMcr_n()));
		} else if (material.getSheet() != null) { // is creating a sheet
			created = materialBean.getMaterial(materialBean.createSheet(material.getFamily().getId(), material.getManufacturerUsername(), material.getName(), material.getSheet().getThickness()));
		} else {
			throw new MyIllegalArgumentException("No material was selected.");
		}

		if(created == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.CREATED)
				.entity(toDTO(created))
				.build();

	}

	@PUT
	@Path("{id}")
	@RolesAllowed({"Admin", "Manufacturer"})
	public Response update(@PathParam("id") long id, MaterialDTO material) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
		Principal principal = securityContext.getUserPrincipal();
		if (securityContext.isUserInRole("Manufacturer") && !principal.getName().equals(material.getManufacturerUsername())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if(material.getProfile() != null) { // is creating a profile
			materialBean.updateProfile(id, material.getFamily().getId(), material.getName(), material.getProfile().getWeff_p(), material.getProfile().getWeff_n(), material.getProfile().getAr(), material.getProfile().getSigmaC(), material.getProfile().getMcr_p(), material.getProfile().getMcr_n());
		} else if (material.getSheet() != null) { // is creating a sheet
			materialBean.updateSheet(id, material.getFamily().getId(), material.getName(), material.getSheet().getThickness());
		} else {
			throw new MyIllegalArgumentException("No material was selected.");
		}

		return Response.status(Response.Status.OK)
				.entity(toDTO(materialBean.getMaterial(id)))
				.build();
	}

	@DELETE
	@Path("{id}")
	@RolesAllowed({"Admin", "Manufacturer"})
	public Response delete(@PathParam("id") long id) throws MyEntityNotFoundException {
		materialBean.delete(id);
		if (materialBean.getMaterial(id) != null)
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(ErrorDTO.error("ERROR_WHILE_DELETING"))
					.build();
		return Response.status(Response.Status.OK)
				.entity(ErrorDTO.error("SUCCESS"))
				.build();
	}

	//endregion

}

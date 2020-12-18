package ws;

import dtos.ErrorDTO;
import dtos.FamilyDTO;
import ejbs.FamilyBean;
import entities.Family;
import exceptions.MyConstraintViolationException;
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

@Path("/families")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed({"Admin", "Manufacturer"})
public class FamilyService {

	//region EJB
	@EJB
	private FamilyBean familyBean;
	//endregion

	//region Security
	@Context
	private SecurityContext securityContext;
	//endregion

	//region DTOS
	public static FamilyDTO toDTO(Family family){
		return new FamilyDTO(
				family.getId(),
				family.getName(),
				ManufacturerService.toDTO(family.getManufacturer()),
				family.getMaterials().size(),
				family.getCreated(),
				family.getUpdated()
		);
	}

	public static List<FamilyDTO> toDTOs(List<Family> families){
		return families.stream().map(FamilyService::toDTO).collect(Collectors.toList());
	}

	public static FamilyDTO toDTONoManufacturerMaterialsCount(Family family){
		return new FamilyDTO(
				family.getId(),
				family.getName(),
				family.getManufacturer().getUsername(),
				family.getMaterials().size(),
				family.getCreated(),
				family.getUpdated()
		);
	}

	public static List<FamilyDTO> toDTOsNoManufacturerMaterialsCount(List<Family> families){
		return families.stream().map(FamilyService::toDTONoManufacturerMaterialsCount).collect(Collectors.toList());
	}
	//endregion

	//region CRUD

	@GET
	@Path("/")
	@RolesAllowed({"Admin", "Manufacturer", "Designer"})
	public Response getAllFamilies() {
		return Response.status(Response.Status.ACCEPTED).entity(FamilyService.toDTOsNoManufacturerMaterialsCount(familyBean.getAll())).build();
	}

	@POST
	@Path("/")
	public Response createNewFamily (FamilyDTO familyDTO) throws MyConstraintViolationException, MyEntityNotFoundException {
		Principal principal = securityContext.getUserPrincipal();
		if (securityContext.isUserInRole("Manufacturer") && !principal.getName().equals(familyDTO.getManufacturerUsername())) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		long id = familyBean.create(
				familyDTO.getName(),
				familyDTO.getManufacturerUsername()
		);
		return Response.status(Response.Status.CREATED).entity(toDTO(familyBean.getFamily(id))).build();
	}

	@GET
	@Path("{id}")
	public Response getFamilyDetails(@PathParam("id") long id) {
		Family family = familyBean.getFamily(id);
		if (family != null) {
			return Response.status(Response.Status.OK)
					.entity(toDTONoManufacturerMaterialsCount(family))
					.build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(ErrorDTO.error("ERROR_FINDING_FAMILY"))
				.build();
	}

	@PUT
	@Path("{id}")
	public Response updateFamily(@PathParam("id") long id, FamilyDTO family) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
		String name = securityContext.getUserPrincipal().getName();
		boolean admin = securityContext.isUserInRole("Admin");
		familyBean.update(id, family.getName(), name, admin);
		return Response.status(Response.Status.OK).entity(toDTO(familyBean.getFamily(id))).build();
	}

	@DELETE
	@Path("{id}")
	@RolesAllowed({"Admin"})
	public Response deleteAdmin(@PathParam("id") long id) throws MyEntityNotFoundException {
		familyBean.delete(id);
		Family family = familyBean.getFamily(id);
		if (family == null) {
			return Response.status(Response.Status.OK).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(ErrorDTO.error("ERROR_DELETING_FAMILY"))
				.build();
	}
	//endregion

}

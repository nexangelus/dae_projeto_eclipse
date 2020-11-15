package ws;

import dtos.ErrorDTO;
import dtos.ManufacturerDTO;
import ejbs.ManufacturerBean;
import entities.Manufacturer;
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

@Path("/manufacturers")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ManufacturerService {

	//region EJB
	@EJB
	private ManufacturerBean manufacturerBean;
	//endregion

	//region Security
	@Context
	private SecurityContext securityContext;
	//endregion

	//region DTOS
	public static ManufacturerDTO toDTO(Manufacturer manufacturer) {
		return new ManufacturerDTO(
				manufacturer.getUsername(),
				manufacturer.getPassword(),
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
	//endregion

	//region CRUD
	@GET
	@Path("/")
	public List<ManufacturerDTO> getAll() {
		return toDTOs(manufacturerBean.getAllManufactures());
	}

	@GET
	@Path("{username}")
	public Response get(@PathParam("username") String username) {
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
	public Response create(ManufacturerDTO manufacturer) throws MyEntityExistsException, MyConstraintViolationException {
		manufacturerBean.create(manufacturer.getUsername(), manufacturer.getPassword(),
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
	public Response update(@PathParam("username") String username, ManufacturerDTO manufacturer) throws MyEntityNotFoundException, MyConstraintViolationException {

		manufacturerBean.update(username, manufacturer.getPassword(), manufacturer.getName(),
				manufacturer.getEmail(), manufacturer.getAddress(), manufacturer.getContact(), manufacturer.getWebsite());

		return Response.status(Response.Status.OK)
				.entity(toDTO(manufacturerBean.getManufacturer(username)))
				.build();
	}

	@DELETE
	@Path("{username}")
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

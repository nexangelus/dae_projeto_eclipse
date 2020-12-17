package exceptions;

import dtos.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Exception> {
	private static final Logger logger = Logger.getLogger("exceptions.CatchAllExceptionMapper");

	@Override
	public Response toResponse(Exception e) {
		String errorMsg = e.getMessage();
		logger.warning("ERROR: " + errorMsg);
		if (errorMsg.contains("403")){
			return Response.status(Response.Status.FORBIDDEN).entity(ErrorDTO.error(errorMsg)).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.error(errorMsg)).build();
	}
}
package exceptions;

import dtos.ErrorDTO;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class MyWebApplicationExceptionMapper implements ExceptionMapper<MyWebApplicationException> {
    private static final Logger logger = Logger.getLogger("exceptions.MyWebApllicationExceptionMapper");

    @Override
    public Response toResponse(MyWebApplicationException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.BAD_REQUEST).entity(ErrorDTO.error(errorMsg)).build();
    }
}

package ws;

import exceptions.MyWebApplicationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/error")
@Produces(MediaType.APPLICATION_JSON)
public class ErrorService {

    private static final Map<Integer, String> statusMsg;
    static
    {
        statusMsg = new HashMap<Integer, String>();
        statusMsg.put(401, "Resource requires authentication");
        statusMsg.put(403, "Access denied");
        statusMsg.put(404, "Resource not found");
        statusMsg.put(500, "Internal server error");
    }

    @GET
    @Path("{httpStatus}")
    public Response error(@PathParam("httpStatus") Integer httpStatus) throws MyWebApplicationException {

        String msg = statusMsg.get(httpStatus);
        if (msg == null)
            msg = "Unexpected error";

        throw new MyWebApplicationException(msg);
    }

}

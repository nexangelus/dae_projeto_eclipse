package ws;

import dtos.AccountDTO;
import dtos.AdminDTO;
import ejbs.AccountBean;
import ejbs.AdminBean;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/accounts")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AccountService {

    //region EJB
    @EJB
    private AccountBean accountBean;
    //endregion

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion
    @POST
    @Path("/")
    public Response createAccount (AccountDTO accountDTO) throws MyEntityExistsException, MyConstraintViolationException {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        accountBean.create(accountDTO.getEmail(),accountDTO.getGroup());
        return Response.status(Response.Status.CREATED).build();
    }
}

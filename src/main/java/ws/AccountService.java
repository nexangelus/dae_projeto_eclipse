package ws;

import dtos.AccountDTO;
import dtos.AdminDTO;
import ejbs.AccountBean;
import ejbs.AdminBean;
import ejbs.EmailBean;
import entities.Account;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.EJB;
import javax.ws.rs.*;
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

    @EJB
    private EmailBean emailBean;
    //endregion

    //region Security
    @Context
    private SecurityContext securityContext;
    //endregion
    @POST
    @Path("/")
    public Response prepareAccount (AccountDTO accountDTO) throws MyEntityExistsException, MyConstraintViolationException {
        if(!(securityContext.isUserInRole("Admin"))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        String code = accountBean.create(accountDTO.getEmail(),accountDTO.getGroup());
        emailBean.send(accountDTO.getEmail(), "Create account", "Go to link http://http://localhost:3000/registers/"+code);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{code}")
    public Response getAccount (@PathParam("code") String code) throws MyEntityExistsException, MyConstraintViolationException {
        Account account = accountBean.findAccount(code);
        return Response.status(Response.Status.CREATED).entity(account).build();
    }

}

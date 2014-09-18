package basicAuthRW.web.ws;

import javax.naming.AuthenticationException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import basicAuthRW.core.AuthService;
import basicAuthRW.core.User;
import basicAuthRW.core.UserService;
import basicAuthRW.web.api.UserAPI;
import com.sun.jersey.api.core.InjectParam;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;

/**
 * Endpoint for basic user account functionality
 */
@Path("/user")
public class UserEndpoint implements UserAPI {

  static final String EMAIL_PARAM_NAME = "email";
  static final String PASSWORD_PARAM_NAME = "password";
  static final String LASTNAME_PARAM_NAME = "last-name";
  static final String FIRSTNAME_PARAM_NAME = "first-name";

  @InjectParam
  @SuppressWarnings({"UnusedDeclaration"})
  private UserService nameService;

  @InjectParam
  @SuppressWarnings({"UnusedDeclaration"})
  private AuthService authService;

  @Path("/name")
  @GET
  @StatusCodes({
    @ResponseCode(code = 200, condition = "User for user returned"),
    @ResponseCode(code = 403, condition = "User\'s session ID is not correct"),
    @ResponseCode(code = 500, condition = "User has validated and should have a name returned, but no name was returned")
  })
  public Response getNameForUser(@QueryParam(EMAIL_PARAM_NAME) String userEmail,
    @QueryParam(AuthEndpoint.SESSION_ID_PARAM_NAME) String sessionId) {

    if(!sessionIdVerified(userEmail, sessionId)) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    User user = nameService.getUser(userEmail);
    return (user == null) ? Response.serverError().build() : Response.ok().entity("User\'s name: <b>" + user.getFirstName() +
      " " + user.getLastName() + "</b>. <br/>Email: <b>" + userEmail + "</b>").build();
  }

  @Path("/create")
  @GET
  @StatusCodes({
    @ResponseCode(code = 200, condition = "User was created."),
    @ResponseCode(code = 403, condition = "Account already associated with provided email address"),
    @ResponseCode(code = 404, condition = "Email and/or password is/are incorrect")
  })
  public Response createUser(@QueryParam(EMAIL_PARAM_NAME) String email, @QueryParam(PASSWORD_PARAM_NAME) String password,
                             @QueryParam(LASTNAME_PARAM_NAME) String lastName, @QueryParam(FIRSTNAME_PARAM_NAME) String firstName) {
    User newUser = nameService.createUser(email, firstName, lastName);
    if(newUser == null) {
      nameService.removeUser(email);
      return Response.status(Response.Status.BAD_REQUEST).entity("Email <b>" + email + "</b> isn\'t valid").build();
    }

    try {
      authService.createPasswordForEmail(email, password);
    } catch(IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Invalid password").build();
    } catch(RuntimeException e) {
      return Response.status(Response.Status.FORBIDDEN).entity("Email address already exists.").build();
    }

    return Response.ok().entity("User " + firstName + " " + lastName +
      "</b> created with Email <b>" + email + "</b>. Please login as the new user.").build();
  }

  private boolean sessionIdVerified(String userEmail, String sessionId) {
    try {
      authService.verifySessionIdForUser(userEmail, sessionId);
    } catch (AuthenticationException e) {
      return false;
    }

    return true;
  }

}

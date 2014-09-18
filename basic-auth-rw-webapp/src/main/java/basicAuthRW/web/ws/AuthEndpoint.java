package basicAuthRW.web.ws;

import javax.naming.AuthenticationException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import basicAuthRW.core.AuthService;
import basicAuthRW.web.api.AuthAPI;
import com.sun.jersey.api.core.InjectParam;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;

/**
 * Endpoint for Login, logout, and admin functions.
 */
@Path("/auth")
public class AuthEndpoint implements AuthAPI {

  @InjectParam
  @SuppressWarnings({"UnusedDeclaration"})
  private AuthService authService;

  static final String SESSION_ID_PARAM_NAME = "session-id";

  @Path("/login")
  @GET
  @StatusCodes({
    @ResponseCode(code = 200, condition = "User logged in and session ID obtained"),
    @ResponseCode(code = 403, condition = "Email and password combination is incorrect")
  })
  public Response login(@QueryParam(UserEndpoint.EMAIL_PARAM_NAME) String email,
    @QueryParam(UserEndpoint.PASSWORD_PARAM_NAME) String password) {

    String sessionId = authService.login(email, password);
    if(sessionId == null) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    return Response.ok().entity("Success! Your session ID is: <b>" + sessionId + "</b>").build();
  }

  @Path("/admin")
  @GET
  @StatusCodes({
    @ResponseCode(code = 200, condition = "Admin requested info has been sent"),
    @ResponseCode(code = 403, condition = "Incorrect admin email and session ID")
  })
  public Response getAllSessionIds(@QueryParam(SESSION_ID_PARAM_NAME) String sessionId) {
    String sessionIdsHtmlStr;
    try {
      sessionIdsHtmlStr = authService.getAllSessionIds(sessionId);
    } catch(AuthenticationException e) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    return Response.ok().entity(sessionIdsHtmlStr).build();
  }

  @Path("/logout")
  @GET
  @StatusCodes({
    @ResponseCode(code = 200, condition = "User has successfully logged out"),
    @ResponseCode(code = 403, condition = "Incorrect email and session ID")
  })
  public Response logout(@QueryParam(UserEndpoint.EMAIL_PARAM_NAME) String email,
    @QueryParam(SESSION_ID_PARAM_NAME) String sessionId) {
    try {
      authService.logout(email, sessionId);
    } catch (AuthenticationException e) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }

    return Response.ok().entity("Successfully logged out <b>" + email + "</b>.").build();
  }
}

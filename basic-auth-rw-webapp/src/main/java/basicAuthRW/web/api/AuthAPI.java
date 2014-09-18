package basicAuthRW.web.api;

import javax.ws.rs.core.Response;

/**
 * Required API methods for the authorization functionality.
 */
public interface AuthAPI {

  /**
   * Login the user associated with the email account
   *
   * @param email the email of the user logging in
   * @param password the password associated with the email
   */
  Response login(String email, String password);

  /**
   * (Admin) Retrieve all active emails and associated session ID's
   *
   * Logout the user associated with the email account
   * @param sessionId the session ID assigned to the user upon login.
   */
  Response getAllSessionIds(String sessionId);

  /**
   * Logout the user associated with the email account
   *
   * @param email the email of the user logging out
   * @param sessionId the session ID assigned to the user upon login.
   */
  Response logout(String email, String sessionId);
}

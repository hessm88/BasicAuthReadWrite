package basicAuthRW.web.api;

import javax.ws.rs.core.Response;

/**
 * Required API methods for functionality dealing with users.
 */
public interface UserAPI {

  /**
   * Get the name of the user associated with the email. Note that
   * a user can only retrieve their own name, i.e. not the names
   * of any other users.
   *
   * @param userEmail the email associated with the user
   * @param sessionId the session ID assigned to the user
   */
  Response getNameForUser(String userEmail, String sessionId);

  /**
   * Create a new user with the given characteristics.
   * This function does NOT log the new user in.
   *
   * @param email the new email to create a user account for
   * @param password the password to associate with the email
   * @param lastName the last name of the new user
   * @param firstName the first name of the new user
   */
  Response createUser(String email, String password, String lastName, String firstName);
}

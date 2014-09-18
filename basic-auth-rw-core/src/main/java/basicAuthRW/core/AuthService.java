package basicAuthRW.core;

import java.util.HashMap;
import javax.naming.AuthenticationException;

/**
 * Service for authorizing/deauthorizing users, as well as admin functions
 */
public class AuthService {

  private static final HashMap<String, String> passwordsByEmail = new HashMap<String, String>();
  private static final HashMap<String, String> sessionIdsByEmail = new HashMap<String, String>();

  public static final String ADMIN_EMAIL = "admin@familysearch.org";
  public static final String ADMIN_PASSWORD = "test";

  public AuthService() {
    if(!passwordsByEmail.containsKey(ADMIN_EMAIL)) {
      passwordsByEmail.put(ADMIN_EMAIL, ADMIN_PASSWORD);
    }
  }

  /**
   * Creates a password to associate with the email address
   *
   * @param userEmail the email address to assign the password to
   * @param password the password to assign to the email
   */
  public void createPasswordForEmail(String userEmail, String password) {
    if(password == null) {
      throw new IllegalArgumentException("Password cant be null");
    }

    if(passwordsByEmail.containsKey(userEmail)) {
      throw new RuntimeException("Email " + userEmail + " already has an account");
    }

    passwordsByEmail.put(userEmail, password);
  }

  /**
   * Login the user associated with the user email
   *
   * @param userEmail the email of the user to login
   * @param password the password associated with the email
   * @return the session ID assigned to the user
   */
  public String login(String userEmail, String password) {
    return (passwordForEmailIsCorrect(userEmail, password)) ? getNewSessionId(userEmail) : null;
  }

  /**
   * Logout the user associated with the user email
   *
   * @param userEmail the email of the user to logout
   * @param sessionId the session ID currently associated with the email
   * @throws AuthenticationException thrown when session ID does not match the session ID
   * assigned to the user upon login.
   */
  public void logout(String userEmail, String sessionId) throws AuthenticationException {
    verifySessionIdForUser(userEmail, sessionId);
    destroySession(userEmail);
  }

  /**
   * Verifies if the passed in session ID matches the assigned session ID given
   * to the user (whose email is userEmail) upon login.
   *
   * @param userEmail the email used to login the user
   * @param sessionId the session ID to be verified for the user associated with the email
   * @throws AuthenticationException thrown when session ID does not match the session ID
   * assigned to the user upon login.
   */
  public void verifySessionIdForUser(String userEmail, String sessionId) throws AuthenticationException {
    if(!sessionIdsByEmail.containsKey(userEmail)) {
      throw new AuthenticationException("User userEmail " + userEmail + " has no session id");
    }

    if(!sessionIdsByEmail.get(userEmail).equals(sessionId)) {
      throw new AuthenticationException("Session id for " + userEmail + " does not match");
    }
  }

  /**
   * Gets all of the active users with their associated session ID. Admin function.
   *
   * @param sessionId the session ID of the admin
   * @return returns an HTML string of all the users and their associated session ID's
   * @throws AuthenticationException thrown when session ID does not match the session ID
   * assigned to the user upon login.
   */
  public String getAllSessionIds(String sessionId) throws AuthenticationException {
    verifySessionIdForUser(ADMIN_EMAIL, sessionId);

    StringBuilder sessionIds = new StringBuilder();
    sessionIds.append("<h3>Current Sessions:</h3><br/>");
    for(String email : sessionIdsByEmail.keySet()) {
      sessionIds.append("<b>");
      sessionIds.append(email);
      sessionIds.append("</b>");
      sessionIds.append(": ");

      sessionIds.append(sessionIdsByEmail.get(email));
      sessionIds.append("<br/>");
    }

    return sessionIds.toString();
  }

  private boolean passwordForEmailIsCorrect(String userEmail, String password) {
    return (passwordsByEmail.containsKey(userEmail) && passwordsByEmail.get(userEmail).equals(password));
  }

  private String getNewSessionId(String userEmail) {
    String newSessionId = userEmail + System.nanoTime();
    sessionIdsByEmail.put(userEmail, newSessionId);

    return newSessionId;
  }

  private void destroySession(String userEmail) throws AuthenticationException {
    sessionIdsByEmail.remove(userEmail);
  }

}

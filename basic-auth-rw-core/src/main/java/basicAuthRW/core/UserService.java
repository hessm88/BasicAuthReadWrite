package basicAuthRW.core;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

/**
 * Service for interacting with the user objects
 */
public class UserService {

  private static final HashMap<String, User> usersByEmail = new HashMap<String, User>();

  public UserService() {
    loadUsersByEmail();
  }

  /**
   * Get the user associated with the email.
   *
   * @param email the email of the user
   * @return the user
   */
  public User getUser(String email) {
    if(StringUtils.isBlank(email)) {
      throw new IllegalArgumentException("email cant be null or blank");
    }

    return (usersByEmail.containsKey(email)) ? usersByEmail.get(email) : null;
  }

  /**
   * Create a user with the characteristics identified by the parameters
   *
   * @param newEmail the email address of the new user
   * @param firstName first name of the new user
   * @param lastName last name of the new user
   * @return the created user object
   */
  public User createUser(String newEmail, String firstName, String lastName) {
    return addNewUser(newEmail, firstName, lastName);
  }

  /**
   * Remove the user associated with the email
   *
   * @param email the email of the user to remove
   */
  public void removeUser(String email) {
    if(!usersByEmail.containsKey(email)) {
      throw new IllegalArgumentException("Email " + email + " doesnt exist");
    }

    usersByEmail.remove(email);
  }

  private User addNewUser(String email, String firstName, String lastName) {
    User newUser;
    try {
      newUser = new User(email, firstName, lastName);
    } catch(IllegalArgumentException e) {
      return null;
    }

    usersByEmail.put(email, newUser);
    return newUser;
  }

  private void loadUsersByEmail() {
    User newUser = new User("Admin", "User", AuthService.ADMIN_EMAIL);
    if(!usersByEmail.containsKey(newUser.getEmail())) {
      usersByEmail.put(newUser.getEmail(), newUser);
    }
  }

}

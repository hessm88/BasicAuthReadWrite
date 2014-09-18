package basicAuthRW.core;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * Class that stores basic user information
 */
@XmlRootElement
public class User {

  private String firstName;
  private String lastName;
  private String email;

  private User() {}

  /**
   * Constructor.
   *
   * @param emailAddress email address of the user
   * @param first first name of the user
   * @param last last name of the user
   */
  public User(String emailAddress, String first, String last) {
    firstName = first;
    lastName = last;

    assignEmailAddress(emailAddress);
  }

  /**
   * Get the user's first name
   *
   * @return the user's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Get the user's last name
   *
   * @return the user's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Get the user's email address
   *
   * @return the user's email address
   */
  public String getEmail() {
    return email;
  }

  private void assignEmailAddress(String emailAddress) {
    verifyEmailAddress(emailAddress);
    email = emailAddress;
  }

  private void verifyEmailAddress(String emailAddress) {
    if(StringUtils.isBlank(emailAddress)) {
      throw new IllegalArgumentException("Email address is blank");
    }
  }

}

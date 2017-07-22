package tabletopsPD;

/**
 * Client is a class representing a client that requests Eagle Event Planning's services.
 */
public class Client {

	/**
	 * Name of the Client.
	 */
	private String name;
	/**
	 * Phone number for the Client.
	 */
	private String phoneNumber;
	/**
	 * Email address for the Client.
	 */
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
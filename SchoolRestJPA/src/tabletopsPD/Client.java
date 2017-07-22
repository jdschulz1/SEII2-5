package tabletopsPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Client is a class representing a client that requests Eagle Event Planning's services.
 */
@XmlRootElement(name = "client")
@Entity(name = "client")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id //signifies the primary key
	@Column(name = "client_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int clientId;
	
	/**
	 * Name of the Client.
	 */
	@Column(name = "name",nullable = false, length = 40)
	private String name;
	
	/**
	 * Phone number for the Client.
	 */
	@Column(name = "phoneNumber",nullable = true)
	private String phoneNumber;
	
	/**
	 * Email address for the Client.
	 */
	@Column(name = "email",nullable = true)
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
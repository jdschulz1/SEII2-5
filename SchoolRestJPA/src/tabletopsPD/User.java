package tabletopsPD;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A general user of the software system.
 */
@XmlRootElement(name = "user")
@Entity(name = "user")
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id //signifies the primary key
	@Column(name = "user_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	
	/**
	 * The legal name of the user.
	 */
	@Column(name = "name",nullable = false,length = 20)
	private String name;
	
	/**
	 * User name for authentication to the system.
	 */
	@Column(name = "user_name",nullable = false,length = 20)
	private String userName;
	
	/**
	 * Password for authentication to the system.
	 */
	@Column(name = "password",nullable = false,length = 20)
	private String password;
	
	/**
	 * Role for the user of the system that determines permissions on the system. ?
	 */
	@Column(name = "role",nullable = false,length = 20)
	private String role;
	
	/**
	 * Email address for the User.
	 */
	@Column(name = "email",nullable = false,length = 20)
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "primaryPlanner", orphanRemoval = true)
	private List<Event> events;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="system",referencedColumnName="system_id")
	private tabletopsPD.System system;

	/**
	 * Checks the password to the one stored for a user and if valid returns true.
	 */
	public boolean checkPassword() {
		// TODO - implement User.checkPassword
		throw new UnsupportedOperationException();
	}

	/**
	 * A user enters their old password and the new password and the password is set to the new password if and only if the old password is correct.
	 * @param old_pass The old password the User wishes to change.
	 * @param new_pass The new password the User wishes to change to.
	 */
	@XmlElement
	public boolean setPassword(String old_pass, String new_pass) {
		// TODO - implement User.setPassword
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	@XmlElement
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	@XmlElement
	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public List<Event> getEvents() {
		return events;
	}

	@XmlElement
	public void setEvents(List<Event> events) {
		this.events = events;
	}

}
package tabletopsPD;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.owlike.genson.annotation.JsonIgnore;

import schoolUT.Message;
import tabletopsDAO.ClientDAO;
import tabletopsDAO.UserDAO;

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
	private int userId;
	
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
	@JoinColumn(name="company",referencedColumnName="company_id")
	private Company company;

	/**
	 * Checks the password to the one stored for a user and if valid returns true.
	 */
	public boolean checkPassword() {
		// TODO - implement User.checkPassword
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the company
	 */
	@JsonIgnore
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	@XmlElement
	public void setCompany(Company company) {
		this.company = company;
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

	public int getUserID() {
		return userId;
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

	@JsonIgnore
	public List<Event> getEvents() {
		return events;
	}

	@XmlElement
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public User findUserByID(int id) {
		  return UserDAO.findUserById(id);
	}

	public Boolean delete() {
		UserDAO.removeUser(this);
		return true;
	}
	
	public ArrayList<Message> validate() {
		ArrayList<Message> messages= new ArrayList<Message>();
		Message message;
		//if (getUserID() == 0){
		//	message = new Message ("User000","UserId must have a value","userId");
		//	messages.add(message);
		//}
		if (getName() == null || getName().length() ==0){
			message = new Message ("User001","Name must have a value","name");
			messages.add(message);
		}
		if (getUserName() == null || getUserName().length() ==0){
			message = new Message ("User002","Username must have a value","username");
			messages.add(message);
		}
		if (getPassword() == null || getPassword().length() ==0){
			message = new Message ("User003","Password must have a value","password");
			messages.add(message);
		}
		if (getRole() == null || getRole().length() ==0){
			message = new Message ("User004","Role must have a value","role");
			messages.add(message);
		}
		if (getEmail() == null || getEmail().length() ==0){
			message = new Message ("User005","Email must have a value","email");
			messages.add(message);
		}
		
		if (messages.size() == 0 ) 
			return null;
		else 
			return messages;
		
	}
	
	public Boolean update(User user) {
	    setName(user.getName());
	    setUserName(user.getUserName());
	    setPassword(user.getPassword());
	    setRole(user.getRole());
	    setEmail(user.getEmail());

	    return true;
	}
}
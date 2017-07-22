package tabletopsPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import schoolDAO.StudentDAO;
import schoolPD.Student;
import tabletopsDAO.ClientDAO;

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
	
	@ManyToOne(optional=false)
	@JoinColumn(name="system",referencedColumnName="system_id")
	private tabletopsPD.System system;
	
	public int getClientID() {
		return clientId;
	}
	
	@XmlElement
	public void setClientId(int id) {
		this.clientId = id;
	}
	
	public String getName() {
		return name;
	}
	
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	@XmlElement
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Client findClientByID(int id) {
		  return ClientDAO.findClientById(id);
	}
	
	public Boolean delete() {
		ClientDAO.removeClient(this);
		return true;
	}

}
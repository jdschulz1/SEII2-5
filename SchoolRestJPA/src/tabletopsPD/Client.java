package tabletopsPD;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.owlike.genson.annotation.JsonIgnore;

import tabletopsDAO.ClientDAO;
import tabletopsUT.Message;

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
	private long clientId;
	
	/**
	 * Name of the Client.
	 */
	@Column(name = "name",nullable = false, length = 80)
	private String name;
	
	/**
	 * Phone number for the Client.
	 */
	@Column(name = "phoneNumber",nullable = true)
	private String phoneNumber;
	
	/**
	 * Email address for the Client.
	 */
	@Column(name = "email",nullable = true, length = 80)
	private String email;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="company",referencedColumnName="company_id")
	private Company company;
	
//	/**
//	 * @return the company
//	 */
//	@JsonIgnore
//	public Company getCompany() {
//		return company;
//	}
//
//	/**
//	 * @param company the company to set
//	 */
//	public void setCompany(Company company) {
//		this.company = company;
//	}

	public long getClientID() {
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
	
	public ArrayList<Message> validate() {
		ArrayList<Message> messages= new ArrayList<Message>();
		Message message;
		if (getName() == null || getName().length() ==0){
			message = new Message ("Client001","Name must have a value","name");
			messages.add(message);
		}
		if (getPhoneNumber() == null || getPhoneNumber().length() ==0){
			message = new Message ("Client002","Phone number must have a value","phoneNumber");
			messages.add(message);
		}
		if (getEmail() == null || getEmail().length() ==0){
			message = new Message ("Client003","Email must have a value","email");
			messages.add(message);
		}
		
		if (messages.size() == 0 ) 
			return null;
		else 
			return messages;
		
	}
	
	public Boolean update(Client client) {
	    setName(client.getName());
	    setPhoneNumber(client.getPhoneNumber());
	    setEmail(client.getEmail());

	    return true;
	}
	
	public Boolean delete() {
		ClientDAO.removeClient(this);
		return true;
	}

}
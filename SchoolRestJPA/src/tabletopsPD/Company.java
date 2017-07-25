package tabletopsPD;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tabletopsDAO.ClientDAO;
import tabletopsDAO.EventDAO;
import tabletopsDAO.EventTableDAO;
import tabletopsDAO.UserDAO;

/**
 * The object representing the system and overall company information for ACME Couriers.
 */
@XmlRootElement(name = "company")
@Entity(name = "company")
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id //signifies the primary key
	@Column(name = "company_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long companyId;
	
	/**
	 * The name of the Company.
	 */
	@Column(name = "company_name",nullable = false,length = 20)
	private String companyName;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "company", orphanRemoval = true)
	private List<Client> clients;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "company", orphanRemoval = true)
	private List<Event> events;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "company", orphanRemoval = true)
	private List<User> users;

	public String getCompanyName() {
		return this.companyName;
	}

	@XmlElement
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<Client> getClients() {
		return clients;
	}

	@XmlElement
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	
	public Boolean addClient(Client client) {
		//TODO: Double check this line
		this.clients.add(client);
		ClientDAO.addClient(client);
		return true;
	}
	
	public Boolean addUser(User user) {
		//TODO: Double check this line
		this.users.add(user);
		UserDAO.addUser(user);
		return true;
	}
	
	public Boolean addEvent(Event event) {
		//TODO: Double check this line
		this.events.add(event);
		EventDAO.addEvent(event);
		return true;
	}

	public List<Event> getEvents() {
		return events;
	}

	@XmlElement
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<User> getUsers() {
		return users;
	}

	@XmlElement
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<Event> getAllEvents(int page, int perPage) {
		
		List<Event> eventList= EventDAO.getAllEvents(page,  perPage);
		return eventList;
	}
	
	public List<Event> getEventsForUser(String idNumber, int page, int perPage) {
		
		List<Event> eventList= EventDAO.getEventsForUser(idNumber, page,  perPage);
		return eventList;
	}
	
	public Event findEventByIdNumber(String idNumber) {
	    return EventDAO.findEventByIdNumber(idNumber); 
	}
	
	public List<Guest> getGuestsForEvent(String idNumber, int page, int perPage) {
		List<Guest> guestList= EventDAO.getGuestsForEvent(idNumber, page,  perPage);
		return guestList;
	}
	
	public List<Guest> getGuestsForTable(String idNumber, int page, int perPage) {
		List<Guest> guestList= EventTableDAO.getGuestsForTable(idNumber, page,  perPage);
		return guestList;
	}
	
	public List<Client> getAllClients(int page, int perPage) {
		List<Client> clientList= ClientDAO.getAllClients(page,  perPage);
		return clientList;
	}
	
	public Client findClientByIdNumber(String idNumber) {
	    return ClientDAO.findClientByIdNumber(idNumber); 
	}
	
	public List<User> getAllUsers(int page, int perPage) {
		List<User> userList= UserDAO.getAllUsers(page,  perPage);
		return userList;
	}
	
	public User findUserByIdNumber(String idNumber) {
	    return UserDAO.findUserByIdNumber(idNumber); 
	}
}
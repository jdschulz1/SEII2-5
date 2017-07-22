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

import schoolDAO.StudentDAO;
import schoolPD.Student;
import tabletopsDAO.EventDAO;

/**
 * The object representing the system and overall company information for ACME Couriers.
 */
@XmlRootElement(name = "system")
@Entity(name = "system")
public class System implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id //signifies the primary key
	@Column(name = "system_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long systemId;
	
	/**
	 * The name of the Company.
	 */
	@Column(name = "company_name",nullable = false,length = 20)
	private String companyName;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "system", orphanRemoval = true)
	private List<Client> clients;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "system", orphanRemoval = true)
	private List<Event> events;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "system", orphanRemoval = true)
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
		
		List eventList= EventDAO.getAllEvents(this, page,  perPage);
		return eventList;
	}
	
	public Event findEventByIdNumber(String idNumber) {
	    return EventDAO.findEventByIdNumber(idNumber); 
	}

}
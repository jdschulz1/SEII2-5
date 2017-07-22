package tabletopsPD;

import java.util.List;

/**
 * The object representing the system and overall company information for ACME Couriers.
 */
public class System {

	/**
	 * The name of the Company.
	 */
	private String companyName;
	
	private List<Client> clients;
	
	private List<Event> events;
	
	private List<User> users;

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
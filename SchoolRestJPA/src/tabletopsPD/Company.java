package tabletopsPD;

import java.io.Serializable;
import java.util.Collection;
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

import com.owlike.genson.annotation.JsonIgnore;

import tabletopsDAO.ClientDAO;
import tabletopsDAO.EventDAO;
import tabletopsDAO.EventTableDAO;
import tabletopsDAO.GuestDAO;
import tabletopsDAO.SeatingArrangementDAO;
import tabletopsDAO.UserDAO;
import tabletopsDAO.TokenDAO;

/**
 * The object representing the system and overall company information for ACME
 * Couriers.
 */
@XmlRootElement(name = "company")
@Entity(name = "company")
public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id // signifies the primary key
	@Column(name = "company_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long companyId;

	/**
	 * The name of the Company.
	 */
	@Column(name = "company_name", nullable = false, length = 20)
	private String companyName;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true)
	private List<Client> clients;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true)
	private List<Event> events;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true)
	private List<User> users;

	private Collection<Token> tokens;

	public String getCompanyName() {
		return this.companyName;
	}

	@XmlElement
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@JsonIgnore
	public List<Client> getClients() {
		return clients;
	}

	@XmlElement
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public Boolean addClient(Client client) {
		// TODO: Double check this line
		this.clients.add(client);
		// WHY!?!?!?!?!?!?!??!
		// client.setCompany(this);
		ClientDAO.addClient(client);
		return true;
	}

	public Boolean addUser(User user) {
		// TODO: Double check this line
		this.users.add(user);
		// user.setCompany(this);
		UserDAO.addUser(user);
		return true;
	}

	public Boolean addEvent(Event event) {
		// TODO: Double check this line
		this.events.add(event);
		// event.setCompany(this);
		EventDAO.addEvent(event);
		return true;
	}

	// public Boolean addGuest(Guest guest) {
	// //TODO: Double check this line
	// this.guests.add(guest);
	// GuestDAO.addGuest(guest);
	// return true;
	// }

	// public Boolean addGuestToSeatingArrangement(SeatingArrangement
	// seatingArrangement) {
	// //TODO: Double check this line
	// this.seatingArrangements.add(seatingArrangement);
	// SeatingArrangementDAO.addSeatingArrangement(seatingArrangement);
	// return true;
	// }

	@JsonIgnore
	public List<Event> getEvents() {
		return events;
	}

	@XmlElement
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	@JsonIgnore
	public List<User> getUsers() {
		return users;
	}

	@XmlElement
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Event> getAllEvents(int page, int perPage) {

		List<Event> eventList = EventDAO.getAllEvents(page, perPage);
		return eventList;
	}

	public List<SeatingArrangement> getAllSeatingArrangements(int page, int perPage) {

		List<SeatingArrangement> seatingArrangementList = SeatingArrangementDAO.getAllSeatingArrangements(page,
				perPage);
		return seatingArrangementList;
	}

	public List<EventTable> getEventTablesForEvent(String idNumber) {
		Event event = EventDAO.findEventByIdNumber(idNumber);
		List<EventTable> eventTableList = event.acquireSA().getEventTables();
		return eventTableList;
	}

	public List<Event> getEventsForUser(String token, int page, int perPage) {
		User user = UserDAO.findUserByToken(token);
		List<Event> eventList = user.getEvents();
		return eventList;
	}

	public List<Event> getEventsForClient(String idNumber, int page, int perPage) {

		List<Event> eventList = EventDAO.getEventsForClient(idNumber, page, perPage);
		return eventList;
	}

	public List<Event> getEventsForDate(String date, int page, int perPage) {

		List<Event> eventList = EventDAO.getEventsForDate(date, page, perPage);
		return eventList;
	}

	public List<Event> getEventsForClientAndDate(String idNumber, String date, int page, int perPage) {

		List<Event> eventList = EventDAO.getEventsForClientAndDate(idNumber, date, page, perPage);
		return eventList;
	}

	public Event findEventByIdNumber(String idNumber) {
		return EventDAO.findEventByIdNumber(idNumber);
	}

	public EventTable findEventTableByIdNumber(String idNumber) {
		return EventTableDAO.findEventTableByIdNumber(idNumber);
	}

	public SeatingArrangement findSeatingArrangementByIdNumber(String idNumber) {
		return SeatingArrangementDAO.findSeatingArrangementByIdNumber(idNumber);
	}

	public List<Guest> getGuestsForEvent(String idNumber, int page, int perPage) {
		List<Guest> guestList = EventDAO.getGuestsForEvent(idNumber, page, perPage);
		System.out.println(guestList.toString());
		return guestList;
	}

	public List<Guest> getGuestsForTable(String idNumber, int page, int perPage) {
		List<Guest> guestList = EventTableDAO.getGuestsForTable(idNumber, page, perPage);
		return guestList;
	}

	public Guest findGuestByIdNumber(String idNumber) {
		return GuestDAO.findGuestByIdNumber(idNumber);
	}

	public List<Client> getAllClients(int page, int perPage) {
		List<Client> clientList = ClientDAO.getAllClients(page, perPage);
		return clientList;
	}

	public Client findClientByIdNumber(String idNumber) {
		return ClientDAO.findClientByIdNumber(idNumber);
	}

	public List<User> getAllUsers(int page, int perPage) {
		List<User> userList = UserDAO.getAllUsers(page, perPage);
		return userList;
	}

	public User findUserByIdNumber(String idNumber) {
		return UserDAO.findUserByIdNumber(idNumber);
	}

	public static User findUserByUserName(String userName) {
		return UserDAO.findUserByUserName(userName);
	}

	public Collection<Token> getTokens() {
		return this.tokens;
	}

	public void setTokens(Collection<Token> tokens) {
		this.tokens = tokens;
	}

	// public Collection<User> getUsers() {
	// return this.users;
	// }

	/**
	 * 
	 * @param token
	 */
	public static Token findToken(String token) {
		return TokenDAO.findTokenByToken(token);
	}
}
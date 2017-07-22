package tabletopsPD;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tabletopsDAO.LocalDateTimeConverter;

/**
 * Events are records containing all information related to an event, including guest list and seating assignment information.
 */
@XmlRootElement(name = "event")
@Entity(name = "event")
public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id //signifies the primary key
	@Column(name = "event_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long eventId;
	
	public long getEventId() {
		return eventId;
	}

	/**
	 * Date and time that the event will take place.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "order_date_time", columnDefinition = "TIMESTAMP")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime eventDateTime;
	
	/**
	 * The title of the event.
	 */
	@Column(name = "event_title",nullable = false,length = 50)
	private String eventTitle;
	
	/**
	 * The name of the venue where the event will be held.
	 */
	@Column(name = "venue_name",nullable = false,length = 50)
	private String venueName;
	
	/**
	 * The number of people who can be seated at each table for the event.
	 */
	@Column(name = "table_size")
	private int tableSize;
	
	/**
	 * The maximum number of seats which can be left empty at a table for the event.
	 */
	@Column(name = "max_empty_seats")
	private int maxEmptySeats;

	@OneToOne(mappedBy = "event",
	        cascade = CascadeType.ALL, orphanRemoval = true)
	private SeatingArrangement seatingArrangement;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "event", orphanRemoval = true)
	private List<Guest> guestList;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="user_id",referencedColumnName="user_id")
	private User primaryPlanner;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="client_id",referencedColumnName="client_id")
	private Client client;
	
	/**
	 * This operation executes the genetic algorithm to attempt to find the best SeatingArrangement for the Event.
	 */
	public boolean calculateSeatingArrangement() {
		// TODO - implement Event.calculateSeatingArrangement
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for adding to the List of Guests attending the Event.
	 */
	public void addToGuestList() {
		// TODO - implement Event.addToGuestList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for removing from the List of Guests attending the Event.
	 */
	public void removeFromGuestList() {
		// TODO - implement Event.removeFromGuestList
		throw new UnsupportedOperationException();
	}

	public LocalDateTime getEventDateTime() {
		return eventDateTime;
	}

	@XmlElement
	public void setEventDateTime(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	@XmlElement
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getVenueName() {
		return venueName;
	}

	@XmlElement
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public int getTableSize() {
		return tableSize;
	}

	@XmlElement
	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public int getMaxEmptySeats() {
		return maxEmptySeats;
	}

	@XmlElement
	public void setMaxEmptySeats(int maxEmptySeats) {
		this.maxEmptySeats = maxEmptySeats;
	}

	public List<Guest> getGuestList() {
		return guestList;
	}

	@XmlElement
	public void setGuestList(List<Guest> guestList) {
		this.guestList = guestList;
	}

	public User getPrimaryPlanner() {
		return primaryPlanner;
	}

	@XmlElement
	public void setPrimaryPlanner(User primaryPlanner) {
		this.primaryPlanner = primaryPlanner;
	}

	public Client getClient() {
		return client;
	}

	@XmlElement
	public void setClient(Client client) {
		this.client = client;
	}

}
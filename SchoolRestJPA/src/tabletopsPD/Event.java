package tabletopsPD;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

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
	
	private long eventId;
	
	/**
	 * Date and time that the event will take place.
	 */
	private LocalDateTime eventDateTime;
	/**
	 * The title of the event.
	 */
	private String eventTitle;
	/**
	 * The name of the venue where the event will be held.
	 */
	private String venueName;
	/**
	 * The number of people who can be seated at each table for the event.
	 */
	private int tableSize;
	/**
	 * The maximum number of seats which can be left empty at a table for the event.
	 */
	private int maxEmptySeats;

	private List<Guest> guestList;
	
	private User primaryPlanner;
	
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

	public void setEventDateTime(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public int getMaxEmptySeats() {
		return maxEmptySeats;
	}

	public void setMaxEmptySeats(int maxEmptySeats) {
		this.maxEmptySeats = maxEmptySeats;
	}

	public List<Guest> getGuestList() {
		return guestList;
	}

	public void setGuestList(List<Guest> guestList) {
		this.guestList = guestList;
	}

	public User getPrimaryPlanner() {
		return primaryPlanner;
	}

	public void setPrimaryPlanner(User primaryPlanner) {
		this.primaryPlanner = primaryPlanner;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
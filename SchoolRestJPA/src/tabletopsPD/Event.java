package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import schoolUT.Message;
import tabletopsDAO.EventDAO;
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
	private int eventId;
	
	public int getEventId() {
		return eventId;
	}

//	/**
//	 * Date and time that the event will take place.
//	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_date_time", columnDefinition = "TIMESTAMP")
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
	@Column(name = "event_table_size")
	private int eventTableSize;
	
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
	@JoinColumn(name="user",referencedColumnName="user_id")
	private User primaryPlanner;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="client_id",referencedColumnName="client_id")
	private Client client;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="company",referencedColumnName="company_id")
	private Company company;
	
	/**
	 * This operation executes the genetic algorithm to attempt to find the best SeatingArrangement for the Event.
	 */
	public boolean calculateSeatingArrangement() {
		// TODO - implement Event.calculateSeatingArrangement
		/*
		 * Start with a generated population of 100 SeatingArrangements
		 * while there isn't a solution above the fitness threshold 
		 * (or maybe after a certain number of generations?),crossover each 
		 * SeatingArrangement with every other SeatingArrangement 
		 * in the population.
		 * 
		 * Set the best fitness(or one of the best if tied) to the Event's 
		 * seatingArrangment.
		 * */
		List<SeatingArrangement> population = new ArrayList<SeatingArrangement>();
		SeatingArrangement temp, currentMostFit = this.seatingArrangement != null ? this.seatingArrangement : new SeatingArrangement(), parent, child;
		
		if(this.seatingArrangement != null) population.add(this.seatingArrangement);
		
		for(int i = 0; i < 100; i++){
			temp = SeatingArrangement.createArrangement(this);
			
			if(currentMostFit.getOverallFitnessRating() == null){
				currentMostFit = temp;
			}
			else if (currentMostFit.getOverallFitnessRating().compareTo(temp.getOverallFitnessRating()) == 1){
				currentMostFit = temp;
			}
			
			population.add(temp);
		}
		
		while(currentMostFit.getOverallFitnessRating().compareTo(BigDecimal.valueOf(90)) == 1){
			for(int i = 0; i < population.size(); i++){
				parent = population.get(i); 
				for(int j = 1; j < population.size(); j++){
					parent = population.get(i).crossover(population.get(j));
					population.add(parent);
					temp = parent;
					if(currentMostFit.getOverallFitnessRating().compareTo(temp.getOverallFitnessRating()) == 1)
						currentMostFit = temp;
				}
			}
		}
		
		if(currentMostFit.getOverallFitnessRating() != null){
			this.seatingArrangement = currentMostFit;
			return true;
		}
		else return false;
			
	}

	/**
	 * A method for adding to the List of Guests attending the Event.
	 */
	public boolean addToGuestList(Guest guest) {
		int size = this.guestList.size();
		this.guestList.add(guest);
		return this.guestList.size() > size;
	}

	/**
	 * A method for removing from the List of Guests attending the Event.
	 */
	public boolean removeFromGuestList(Guest guest) {
		int size = this.guestList.size();
		if(size > 0) {
			this.guestList.remove(guest);
			return this.guestList.size() < size;
		}
		else return false;
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

	public int getEventTableSize() {
		return eventTableSize;
	}

	@XmlElement
	public void setEventTableSize(int eventTableSize) {
		this.eventTableSize = eventTableSize;
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

	public Boolean delete() {
		EventDAO.removeEvent(this);
		return true;
	}
	
	public ArrayList<Message> validate() {
		ArrayList<Message> messages= new ArrayList<Message>();
		Message message;
		if (getEventId() == 0){
			message = new Message ("Event000","EventId must have a value","eventId");
			messages.add(message);
		}
		if (getEventTableSize() == 0){
			message = new Message ("Event001","Event Table Size must have a value","getEventTableSize");
			messages.add(message);
		}
		if (getEventTitle() == null || getEventTitle().length() ==0){
			message = new Message ("Event002","Event Title Number must have a value","eventTitle");
			messages.add(message);
		}
		if (getVenueName() == null || getVenueName().length() ==0){
			message = new Message ("Event003","Venue Name must have a value","venueName");
			messages.add(message);
		}
		if (getMaxEmptySeats() == 0){
			message = new Message ("Event004","Max Empty Seats must have a value","maxEmptySeats");
			messages.add(message);
		}
		
		if (messages.size() == 0 ) 
			return null;
		else 
			return messages;
		
	}
	
	public Boolean update(Event event) {
	    setEventTableSize(event.getEventTableSize());
	    setEventTitle(event.getEventTitle());
	    setVenueName(event.getVenueName());
	    setMaxEmptySeats(event.getMaxEmptySeats());

	    return true;
	}
}
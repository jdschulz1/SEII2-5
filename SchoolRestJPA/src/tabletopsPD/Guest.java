package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.owlike.genson.annotation.JsonIgnore;

import tabletopsDAO.ClientDAO;
import tabletopsDAO.EM;
import tabletopsDAO.GuestDAO;
import tabletopsUT.Message;

@XmlRootElement(name = "guest")
@Entity(name = "guest")
public class Guest implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Guest(){
		
	}
	
	public Guest(int guestNumber, String name, String clientRelationship, Event event){
		this.guestNumber = guestNumber;
		this.name = name;
		this.clientRelationship = clientRelationship;
		this.event = event;
		this.blacklist = new ArrayList<Guest>();
		this.whitelist = new ArrayList<Guest>();
		this.eventTable = EventTable.getDefaultTable();
		EntityTransaction userTransaction = EM.getEM().getTransaction();
	    userTransaction.begin();
		GuestDAO.addGuest(this);
		userTransaction.commit();
	}
	
	public Guest guestCopy(){
		try {
			return (Guest) this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isSameGuest(Guest g){
		return this.event.getEventId() == g.event.getEventId() && this.guestNumber == g.guestNumber && this.name == g.name;
	}
	
	@Id //signifies the primary key
	@Column(name = "guest_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long guestId;
	
	public long getGuestId() {
		return guestId;
	}
	@XmlElement
	public void setGuestId(long guestId) {
		this.guestId = guestId;
	}

	/**
	 * The number that identifies a Guest.
	 */
	@Column(name = "guest_number")
	private int guestNumber;
	
	/**
	 * The name of the Guest attending the Event.
	 */
	@Column(name = "name",nullable = false,length = 200)
	private String name;
	
	/**
	 * The special notes about the relationship between the Guest and the Client requesting the Event.
	 */
	@Column(name = "client_relationship",nullable = false,length = 200)
	private String clientRelationship;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="event",referencedColumnName="event_id")
	private Event event;
	
	/**
	 * @return the event
	 */
	@JsonIgnore
	public Event getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	@XmlElement
	public void setEvent(Event event) {
		this.event = event;
	}

	@ManyToOne(optional=true)
	@JoinColumn(name="event_table", referencedColumnName="event_table_id", nullable=true)
	private EventTable eventTable;
	
	@JoinTable(name="whitelist", 
			joinColumns= {@JoinColumn(name="list_owner", referencedColumnName = "guest_id", nullable = false)}, 
			inverseJoinColumns = {@JoinColumn(name="list_member", referencedColumnName = "guest_id", nullable=false)})
	@ManyToMany
	private List<Guest> whitelist;
	
	@JoinTable(name="blacklist", 
			joinColumns= {@JoinColumn(name="list_owner", referencedColumnName = "guest_id", nullable = false)}, 
			inverseJoinColumns = {@JoinColumn(name="list_member", referencedColumnName = "guest_id", nullable=false)})
	@ManyToMany
	private List<Guest> blacklist;

	@Transient
	private BigDecimal guestFitness;
	
	public BigDecimal getGuestFitness() {
		return guestFitness;
	}

	public void setGuestFitness(BigDecimal guestFitness) {
		this.guestFitness = guestFitness;
	}

	/**
	 * A method for adding to the Black List of Guests for the current Guest.
	 */
	public void addToBlackList(Guest member) {
		EntityTransaction userTransaction = EM.getEM().getTransaction();
	    userTransaction.begin();
		this.blacklist.add(member);
		GuestDAO.saveGuest(this);
		userTransaction.commit();
	}

	/**
	 * A method for adding to the White List of Guests for the current Guest.
	 */
	public void addToWhiteList(Guest member) {
		EntityTransaction userTransaction = EM.getEM().getTransaction();
	    userTransaction.begin();
	    this.whitelist.add(member);
		GuestDAO.saveGuest(this);
		userTransaction.commit();
	}

	/**
	 * A method for remove from the Black List of Guests for the current Guest.
	 */
	public void removeFromBlackList(Guest member) {
		EntityTransaction userTransaction = EM.getEM().getTransaction();
	    userTransaction.begin();
	    this.blacklist.remove(member);
		GuestDAO.saveGuest(this);
		userTransaction.commit();
	}

	/**
	 * A method for removing from the White List of Guests for the current Guest.
	 */
	public void removeFromWhiteList(Guest member) {
		EntityTransaction userTransaction = EM.getEM().getTransaction();
	    userTransaction.begin();
	    this.whitelist.remove(member);
		GuestDAO.saveGuest(this);
		userTransaction.commit();
	}

	public int getGuestNumber() {
		return guestNumber;
	}

	@XmlElement
	public void setGuestNumber(int guestNumber) {
		this.guestNumber = guestNumber;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	
//	public EventTable getEventTable() {
//		return eventTable;
//	}
//
//	@JsonIgnore
//	public void setEventTable(EventTable eventTable) {
//		this.eventTable = eventTable;
//	}
	
	@XmlElement(name = "eventTableNumber")
    public String getEventTableNumber() {
        return "" + this.eventTable.getEventTableNum();
    }
	
	
	public void setEventTable(EventTable table) {
		this.eventTable = table;
	}

	public String getClientRelationship() {
		return clientRelationship;
	}

	@XmlElement
	public void setClientRelationship(String clientRelationship) {
		this.clientRelationship = clientRelationship;
	}
	
	public ArrayList<Message> validate() {
		ArrayList<Message> messages= new ArrayList<Message>();
		Message message;
		if (getName() == null || getName().length() ==0){
			message = new Message ("Guest001","Name must have a value","name");
			messages.add(message);
		}
		
		if (messages.size() == 0 ) 
			return null;
		else 
			return messages;
		
	}
	
	/**
	 * @return the whitelist
	 */
	@JsonIgnore
	public List<Guest> getWhitelist() {
		return whitelist;
	}

	/**
	 * @param whitelist the whitelist to set
	 */
	@XmlElement
	public void setWhitelist(List<Guest> whitelist) {
		this.whitelist = whitelist;
	}

	/**
	 * @return the blacklist
	 */
	@JsonIgnore
	public List<Guest> getBlacklist() {
		return blacklist;
	}

	/**
	 * @param blacklist the blacklist to set
	 */
	@XmlElement
	public void setBlacklist(List<Guest> blacklist) {
		this.blacklist = blacklist;
	}

	public Boolean update(Guest guest) {
	    setName(guest.getName());
	    setClientRelationship(guest.getClientRelationship());
//		GuestDAO.saveGuest(this);
	    return true;
	}
	
	public Boolean delete() {
		GuestDAO.removeGuest(this);
		return true;
	}
	
	public Boolean moveToTable(EventTable newEventTable) {
		// Check if there is room at the new table
		EventTable oldEventTable = this.eventTable;
		int guestsAtTable = newEventTable.getGuests().size();
		int numOpenSeats = getEvent().getEventTableSize() - guestsAtTable;
		if((this.getWhitelist().size() + 1) > numOpenSeats) {
			return false;
		}
		// Move selected guest
		oldEventTable.removeGuest(this);
		newEventTable.addGuest(this);
		
		// Move all guests in whitelist
		for(Guest g : this.whitelist) {
			oldEventTable.removeGuest(g);
			newEventTable.addGuest(g);
		}
		
		// TODO: Validate 
		Boolean isValidSeatingArrangement = this.event.getSeatingArrangement().isValid();
		
		if(isValidSeatingArrangement) {
			return true;
		}
		else {
			// Revert changes if invalid
			// Move selected guest
			newEventTable.removeGuest(this);
			oldEventTable.addGuest(this);
			
			// Move all guests in whitelist
			for(Guest g : this.whitelist) {
				newEventTable.removeGuest(g);
				oldEventTable.addGuest(g);
			}
			return false;
		}
	}
}
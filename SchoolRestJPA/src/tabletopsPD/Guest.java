package tabletopsPD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.owlike.genson.annotation.JsonIgnore;

import schoolUT.Message;
import tabletopsDAO.ClientDAO;
import tabletopsDAO.GuestDAO;

@XmlRootElement(name = "guest")
@Entity(name = "guest")
public class Guest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id //signifies the primary key
	@Column(name = "guest_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long guestId;
	
	/**
	 * The number that identifies a Guest.
	 */
	@Column(name = "guest_number")
	private int guestNumber;
	
	/**
	 * The name of the Guest attending the Event.
	 */
	@Column(name = "name",nullable = false,length = 20)
	private String name;
	
	/**
	 * The special notes about the relationship between the Guest and the Client requesting the Event.
	 */
	@Column(name = "client_relationship",nullable = false,length = 140)
	private String clientRelationship;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="event",referencedColumnName="event_id")
	private Event event;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="event_table",referencedColumnName="event_table_id")
	private EventTable eventTable;

	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "listOwner", orphanRemoval = true)
	private List<J_Guest_WL> guestWhiteList;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "listOwner", orphanRemoval = true)
	private List<J_Guest_BL> guestBlackList;
	
	/**
	 * A method for adding to the Black List of Guests for the current Guest.
	 */
	public void addToBlackList(Guest member) {
		// TODO - implement Guest.addToBlackList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for adding to the White List of Guests for the current Guest.
	 */
	public void addToWhiteList(Guest member) {
		// TODO - implement Guest.addToWhiteList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for remove from the Black List of Guests for the current Guest.
	 */
	public void removeFromBlackList(Guest member) {
		// TODO - implement Guest.removeFromBlackList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for removing from the White List of Guests for the current Guest.
	 */
	public void removeFromWhiteList(Guest member) {
		// TODO - implement Guest.removeFromWhiteList
		throw new UnsupportedOperationException();
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

	public String getClientRelationship() {
		return clientRelationship;
	}

	@XmlElement
	public void setClientRelationship(String clientRelationship) {
		this.clientRelationship = clientRelationship;
	}

	@JsonIgnore
	public List<J_Guest_WL> getGuestWhiteList() {
		return guestWhiteList;
	}

	@XmlElement
	public void setGuestWhiteList(List<J_Guest_WL> guestWhiteList) {
		this.guestWhiteList = guestWhiteList;
	}

	@JsonIgnore
	public List<J_Guest_BL> getGuestBlackList() {
		return guestBlackList;
	}

	@XmlElement
	public void setGuestBlackList(List<J_Guest_BL> guestBlackList) {
		this.guestBlackList = guestBlackList;
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
	
	public Boolean update(Guest guest) {
	    setName(guest.getName());
	    setClientRelationship(guest.getClientRelationship());

	    return true;
	}
	
	public Boolean delete() {
		GuestDAO.removeGuest(this);
		return true;
	}

}
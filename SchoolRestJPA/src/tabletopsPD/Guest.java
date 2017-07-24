package tabletopsPD;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
	@JoinColumn(name="table",referencedColumnName="table_id")
	private EventTable table;

	@OneToMany(mappedBy = "wlOwner")
	private List<Guest> guestWhiteList;
	
	@ManyToOne
	private Guest wlOwner;
	
	@OneToMany(mappedBy = "blOwner")
	private List<Guest> guestBlackList;
	
	@ManyToOne
	private Guest blOwner;
	
	/**
	 * A method for adding to the Black List of Guests for the current Guest.
	 */
	public void addToBlackList() {
		// TODO - implement Guest.addToBlackList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for adding to the White List of Guests for the current Guest.
	 */
	public void addToWhiteList() {
		// TODO - implement Guest.addToWhiteList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for remove from the Black List of Guests for the current Guest.
	 */
	public void removeFromBlackList() {
		// TODO - implement Guest.removeFromBlackList
		throw new UnsupportedOperationException();
	}

	/**
	 * A method for removing from the White List of Guests for the current Guest.
	 */
	public void removeFromWhiteList() {
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

	public List<Guest> getGuestWhiteList() {
		return guestWhiteList;
	}

	@XmlElement
	public void setGuestWhiteList(List<Guest> guestWhiteList) {
		this.guestWhiteList = guestWhiteList;
	}

	public List<Guest> getGuestBlackList() {
		return guestBlackList;
	}

	@XmlElement
	public void setGuestBlackList(List<Guest> guestBlackList) {
		this.guestBlackList = guestBlackList;
	}

}
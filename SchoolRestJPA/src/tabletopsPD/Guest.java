package tabletopsPD;

import java.util.List;

public class Guest {

	/**
	 * The number that identifies a Guest.
	 */
	private int guestNumber;
	/**
	 * The name of the Guest attending the Event.
	 */
	private String name;
	/**
	 * The special notes about the relationship between the Guest and the Client requesting the Event.
	 */
	private String clientRelationship;

	private List<Guest> guestWhiteList;
	
	private List<Guest> guestBlackList;
	
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

	public void setGuestNumber(int guestNumber) {
		this.guestNumber = guestNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientRelationship() {
		return clientRelationship;
	}

	public void setClientRelationship(String clientRelationship) {
		this.clientRelationship = clientRelationship;
	}

	public List<Guest> getGuestWhiteList() {
		return guestWhiteList;
	}

	public void setGuestWhiteList(List<Guest> guestWhiteList) {
		this.guestWhiteList = guestWhiteList;
	}

	public List<Guest> getGuestBlackList() {
		return guestBlackList;
	}

	public void setGuestBlackList(List<Guest> guestBlackList) {
		this.guestBlackList = guestBlackList;
	}

}
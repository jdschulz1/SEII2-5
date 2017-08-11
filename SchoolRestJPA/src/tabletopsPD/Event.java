package tabletopsPD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

import com.owlike.genson.annotation.JsonIgnore;

import javafx.scene.shape.Line;
import tabletopsDAO.ClientDAO;
import tabletopsDAO.EventDAO;
import tabletopsDAO.GuestDAO;
import tabletopsDAO.LocalDateTimeConverter;
import tabletopsDAO.UserDAO;
import tabletopsUT.CSVReader;
import tabletopsUT.Message;

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
	
	@XmlElement
	public void setEventId(int id) {
		this.eventId = id;
	}
//	/**
//	 * Date and time that the event will take place.
//	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_date_time", columnDefinition = "TIMESTAMP")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime eventDateTime;
//  WHYYYYYYYYYYYYYYYYYY!?!?!??!	
//	/**
//	 * @return the company
//	 */
//	@JsonIgnore
//	public Company getCompany() {
//		return company;
//	}
//
//	/**
//	 * @param company the company to set
//	 */
//	public void setCompany(Company company) {
//		this.company = company;
//	}

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
	public boolean calculateSeatingArrangement(BigDecimal threshold) {

		if(this.guestList.isEmpty()) 
			return false;
		
		NavigableSet<SeatingArrangement> population = new TreeSet<SeatingArrangement>();
		SeatingArrangement temp, currentMostFit = this.seatingArrangement != null ? this.seatingArrangement : new SeatingArrangement(), parent, child;
		List<SeatingArrangement> topTen = new ArrayList<SeatingArrangement>(); 
		if(this.seatingArrangement != null) population.add(this.seatingArrangement);
		
		for(int i = 0; i < 1000; i++){
			temp = SeatingArrangement.createArrangement(this);
			
//			if(currentMostFit.getOverallFitnessRating() == null){
//				currentMostFit = temp;
//			}
//			else if (currentMostFit.getOverallFitnessRating().compareTo(temp.getOverallFitnessRating()) == -1){
//				currentMostFit = temp;
//			}
			
			population.add(temp);
		}
		
		while(topTen.size() < 75){
			topTen.add(population.pollLast());
		}
		
		while(topTen.get(0).getOverallFitnessRating().compareTo(threshold) == -1){
			for(int i = 0; i < topTen.size(); i++){
				parent = topTen.get(i);
				for(int j = 0; j < topTen.size(); j++){
					if(!parent.equals(topTen.get(j))){
						child = parent.crossover(topTen.get(j));
					
						if(child != null && !topTen.contains(child)){
							topTen.add(child);
							Collections.sort(topTen, Collections.reverseOrder());
							if(topTen.size() > 99){
								population.add(topTen.get(99));
								topTen.remove(99);
							}
						}
					}
					
					
//					if(child.getOverallFitnessRating().compareTo(topTen.get(9).getOverallFitnessRating()) == 1 &&
//							child.getOverallFitnessRating().compareTo(topTen.get(8).getOverallFitnessRating()) == -1){
//						population.add(topTen.get(9));
//						topTen.remove(9);
//						topTen.add(child);
//					}
//					else if() {
//				
//					}
				}
			}
//			for(int i = 0; i < population.size(); i++){
//				parent = population.pollFirst(); 
//				for(int j = 1; j < population.size(); j++){
//					parent = ((SeatingArrangement)population.get(i));
//					child = population.get(i).crossover(population.get(j));
//					population.add(child);
//					temp = parent;
//					if(currentMostFit.getOverallFitnessRating().compareTo(temp.getOverallFitnessRating()) == -1){
//						currentMostFit = temp;
//						if(currentMostFit.getOverallFitnessRating().compareTo(threshold) != -1){
//							this.seatingArrangement = this.finalSA(currentMostFit); 
//							return true;
//						}
//					}
//						
//				}
//			}
			
			System.out.println("solution fitness" + topTen.get(0).getOverallFitnessRating());
		}
		
		if(currentMostFit.getOverallFitnessRating() != null){
			this.seatingArrangement = currentMostFit;
			return true;
		}
		else return false;	
	}
	
	public SeatingArrangement bullshit() {
		return seatingArrangement;
	}

	public SeatingArrangement finalSA(SeatingArrangement sa){
		SeatingArrangement newSA = sa;
		List<Guest> newGuestList = new ArrayList<Guest>();
		for(EventTable et : newSA.getEventTables()){
			for(Guest g : et.getGuests()){
				newGuestList.add(g);
			}
		}
		this.guestList = newGuestList;
		return newSA;
	}
	
	public Guest findGuestByNum (int num){
		for(Guest g : this.getGuestList()){
			if(g.getGuestNumber() == num)return g;
		}
		return null;
	}

	/**
	 * A method for adding to the List of Guests attending the Event.
	 */
	public boolean addToGuestList(Guest guest) {
		if(guest.getGuestNumber() == 0) {
			int maxId = 0;
			for(Guest g: this.guestList) {
				maxId = Math.max(maxId, g.getGuestNumber());
			}
			guest.setGuestNumber(maxId + 1); 
		}
		guest.setEventTable(EventTable.getDefaultTable());
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

	// Anna commented out - may have to remove again
//	@XmlElement
	public void setEventDateTime2(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}
	
	@XmlElement
	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = LocalDateTime.parse(eventDateTime);
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
//		if (getEventId() == 0){
//			message = new Message ("Event000","EventId must have a value","eventId");
//			messages.add(message);
//		}
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
		
		System.out.println("Event error messages: " + messages.size());
		
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
	    setEventDateTime2(event.getEventDateTime());
	    setClient(ClientDAO.findClientById(event.client.getClientID()));
	    setPrimaryPlanner(UserDAO.findUserById(event.primaryPlanner.getUserID()));

	    return true;
	}
	
	public Guest findGuestByGuestNumber(int guestNum) {
		for(Guest g: this.guestList)
			if (g.getGuestNumber() == guestNum)
				return g;
		return null;
	}
	
	public Boolean importGuestList(String filePath) {
		try {
			Scanner scanner = new Scanner(new File(filePath));
			List<String> headers = CSVReader.parseLine(scanner.nextLine());
			int whitelistCount = 0;
			for(String header:headers) {
				if(header.equalsIgnoreCase("Same Table")) 
					whitelistCount++;
			}
			// Add actual guests
			while (scanner.hasNext()) {
	            List<String> line = CSVReader.parseLine(scanner.nextLine());
	            if(!line.get(0).contains("#"))
	            {
	            	Guest g = new Guest(Integer.parseInt(line.get(0)), line.get(1), "", this);
	            	this.addToGuestList(g);
	            }
	        }
			// Start over to add whitelist/blacklist
			scanner = new Scanner(new File(filePath));
			while (scanner.hasNext()) {
	            List<String> line = CSVReader.parseLine(scanner.nextLine());
	            if(!line.get(0).contains("#"))
	            {
            		Guest mainGuest = this.findGuestByGuestNumber(Integer.parseInt(line.get(0)));
            		if(mainGuest != null) {
		            	for(int i = 2; i < line.size(); i++) {
		            		try {
		            			Guest guestToAdd = this.findGuestByGuestNumber(Integer.parseInt(line.get(i)));
		            		
			            		if((i - 2) < whitelistCount) {
			            			mainGuest.addToWhiteList(guestToAdd);
			            		}
			            		else {
			            			mainGuest.addToBlackList(guestToAdd);
			            		}
		            		} catch(Exception e) {
		            		}
		            	}
            		}
	            }
	        }
	        scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
				
		return true;
	}
}
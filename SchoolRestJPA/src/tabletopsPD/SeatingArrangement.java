package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import schoolUT.Message;
import tabletopsDAO.GuestDAO;
import tabletopsDAO.SeatingArrangementDAO;

/**
 * The SeatingArrangement is a solution in the genetic algorithm for best seating arrangement for the Event.
 */
@XmlRootElement(name = "seating_arrangement")
@Entity(name = "seating_arrangement")
public class SeatingArrangement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeatingArrangement(){
		this.eventTables = new ArrayList<EventTable>();
	}
	
	public SeatingArrangement(Event e){
		this.event = e;
		this.eventTables = new ArrayList<EventTable>();
	}
	
	@Id //signifies the primary key
	@Column(name = "seating_arrangement_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long seatingArrangementId;
	
	/**
	 * The overall fitness rating for the SeatingArrangement for the purpose of finding the best Seating Arrangement.
	 */
	@Column(name = "overall_fitness_rating")
	private BigDecimal overallFitnessRating;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "seatingArrangement", orphanRemoval = true)
	private List<EventTable> eventTables;
	
	@OneToOne
	@JoinColumn(name = "event_id")
	private Event event;

	/**
	 * Generates a random seating arrangement.
	 */
	public static SeatingArrangement createArrangement(Event e) {
		
		SeatingArrangement newArrangement = new SeatingArrangement(e);
		
		int guestidx;
		Guest newGuest;
		
		try {
		     SecureRandom number = SecureRandom.getInstanceStrong();
		     EventTable first = new EventTable(1);
		     newArrangement.eventTables.add(first);
		     // Generate guestList.size() integers 0..guestList.size()
		     for (int i = 0; i < newArrangement.event.getGuestList().size(); i++) {
		    	 guestidx = number.nextInt(newArrangement.event.getGuestList().size());
		    	 newGuest = newArrangement.event.getGuestList().get(guestidx).guestCopy();
		    	 System.out.println(guestidx);
		    	 if(newArrangement.eventTables.get(newArrangement.eventTables.size()-1).getGuests().size() < newArrangement.event.getEventTableSize()){
		    		 newArrangement.eventTables.get(newArrangement.eventTables.size()-1).addGuest(newGuest);
		    	 }
		    	 else{
		    		 EventTable newTable = new EventTable(newArrangement.eventTables.size()+1);
		    		 newTable.addGuest(newGuest);
		    		 newArrangement.eventTables.add(newTable);
		    	 }
		     }
		     newArrangement.calculateOverallFitness();
		     return newArrangement;
		   } catch (NoSuchAlgorithmException nsae) {
		     // Forward to handler
			   return null;
		   }
	}

	/**
	 * Performs a random change to the seating arrangement (i.e. swapping 2 guests between tables).
	 */
	public void mutate(Guest orig, String moveTableNum) {
		EventTable moveTable = this.eventTables.get(Integer.parseInt(moveTableNum)), 
				origTable = this.eventTables.get(Integer.parseInt(orig.getEventTableNumber()));
		Guest displaced = moveTable.getGuests().get(0);
		
		BigDecimal leastFit = displaced.getGuestFitness();
		
		for (Guest g : moveTable.getGuests()){
			if(g.getGuestFitness().compareTo(leastFit) == 1){
				displaced = g;
				leastFit = displaced.getGuestFitness();
				break;
			}
		}
		
		moveTable.removeGuest(displaced);
		origTable.removeGuest(orig);
		moveTable.addGuest(orig);
		origTable.addGuest(displaced);
	}

	/**
	 * Produces a child seating arrangement based on two parent seating arrangements.
	 * @param SeatingArrangement
	 */
	public SeatingArrangement crossover(SeatingArrangement seatingArrangement) {
		// TODO - implement SeatingArrangement.crossover
		/*
		 * Swap guests with a fitness below 50% from the passed seatingArrangement with their equivalent position
		 * on this seatingArrangement and repair by moving the displaced guest to where the incoming
		 * guest's counterpart on this seatingArrangement is.  The candidates on this seatingArrangement 
		 * for swap are the lowest fitness guests.
		 * 
		 * ideas for implementation:
		 * 
		 * Pre-condition:  SeatingArrangment needs to have an overallFitness
		 * 
		 * 1. find the bottom 50% guests based on fitness
		 * 2. for each guest do the following
		 * 		a. save the table number where the passed version of the guest resides
		 * 		b. pick the lowest fitness guest at this table, then save and remove them from the table
		 * 		c. add the guest(original) to this table and remove the guest(original) from their original table
		 * 		d. add the guest(displaced) to the original guest's original table
		 * */
		try {
			SeatingArrangement origSA = (SeatingArrangement) this.clone();
		
			List<Guest> bottom50 = new ArrayList<Guest>();
			
			for(EventTable et : this.getEventTables()){
				for(Guest g : et.getGuests()){
					if(g.getGuestFitness().compareTo(BigDecimal.valueOf(50))==1){
						bottom50.add(g);
					}
				}
			}
			
			for(EventTable et : seatingArrangement.getEventTables()){
				for(Guest g : et.getGuests()){
					for(Guest g2 : bottom50){
						if(g.getName() == g2.getName())
							this.mutate(g2, g.getEventTableNumber());
					}
				}
			}
			
			return origSA.getOverallFitnessRating().compareTo(this.getOverallFitnessRating()) == -1 ? origSA : this;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return this;
		}
	}

	/**
	 * The operation that calculates the overall fitness of the solution for the SeatingArrangement for the genetic algorithm.
	 * This should be the average fitness over all of the tables in the Seating Arrangement
	 */
	public void calculateOverallFitness() {
		BigDecimal fitness = BigDecimal.ZERO;
		
		for(EventTable et : this.eventTables){
			fitness.add(et.getFitnessRating());
		}
		
		this.overallFitnessRating = fitness.divide(BigDecimal.valueOf(this.eventTables.size()));
	}

	public BigDecimal getOverallFitnessRating() {
		return overallFitnessRating;
	}

	@XmlElement
	public void setOverallFitnessRating(BigDecimal overallFitnessRating) {
		this.overallFitnessRating = overallFitnessRating;
	}

	public List<EventTable> getEventTables() {
		return eventTables;
	}

	@XmlElement
	public void setEventTables(List<EventTable> eventTables) {
		this.eventTables = eventTables;
	}

	public Event getEvent() {
		return event;
	}

	@XmlElement
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public ArrayList<Message> validate() {
		ArrayList<Message> messages= new ArrayList<Message>();
		Message message;
		if (getOverallFitnessRating() == null){
			message = new Message ("SeatingArrangement001","Overall Fitness Rating must have a value","overall fitness rating");
			messages.add(message);
		}
		
		if (messages.size() == 0 ) 
			return null;
		else 
			return messages;
		
	}
	
	public Boolean update(SeatingArrangement seatingArrangement) {
	    setOverallFitnessRating(seatingArrangement.getOverallFitnessRating());

	    return true;
	}
	
	public Boolean delete() {
		SeatingArrangementDAO.removeSeatingArrangement(this);
		return true;
	}

}
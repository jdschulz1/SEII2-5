package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
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

import tabletopsDAO.GuestDAO;
import tabletopsDAO.SeatingArrangementDAO;
import tabletopsUT.Message;

/**
 * The SeatingArrangement is a solution in the genetic algorithm for best seating arrangement for the Event.
 */
@XmlRootElement(name = "seating_arrangement")
@Entity(name = "seating_arrangement")
public class SeatingArrangement implements Serializable, Cloneable{

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
		     List<Integer> usedRandoms = new ArrayList<Integer>();
		     first.setSeatingArrangement(newArrangement);
		     newArrangement.eventTables.add(first);
		     // Generate guestList.size() integers 0..guestList.size()
		     for (int i = 0; i < newArrangement.event.getGuestList().size(); i++) {
		    	 guestidx = number.nextInt(newArrangement.event.getGuestList().size());
		    	 while(usedRandoms.contains(guestidx)){
		    		 guestidx = number.nextInt(newArrangement.event.getGuestList().size());
		    	 }
		    	 usedRandoms.add(guestidx);
		    	 
		    	 newGuest = newArrangement.event.getGuestList().get(guestidx).guestCopy();
		    	 System.out.println(guestidx);
		    	 if(newArrangement.eventTables.get(newArrangement.eventTables.size()-1).getGuests().size() < newArrangement.event.getEventTableSize()){
		    		 newArrangement.eventTables.get(newArrangement.eventTables.size()-1).addGuest(newGuest);
		    	 }
		    	 else{
		    		 EventTable newTable = new EventTable(newArrangement.eventTables.size()+1);
		    		 newTable.addGuest(newGuest);
		    		 newTable.setSeatingArrangement(newArrangement);
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
	public Mutation mutate(Guest orig, String moveTableNum) {
		//EventTable moveTable = this.eventTables.get(Integer.parseInt(moveTableNum));
		//EventTable origTable = this.eventTables.get(Integer.parseInt(orig.getEventTableNumber()));
		EventTable moveTable  = this.getEventTableByNumber(moveTableNum);
		String origTableNum = this.findGuestByNumber(orig.getGuestNumber()).getEventTableNumber();
		EventTable origTable = this.getEventTableByNumber(origTableNum);
		
		Guest displaced = moveTable.getGuests().get(0);
		
		BigDecimal leastFit = displaced.getGuestFitness();
		
		for (Guest g : moveTable.getGuests()){
			if(g.getGuestFitness().compareTo(leastFit) == -1){
				displaced = g;
				leastFit = displaced.getGuestFitness();
				break;
			}
		}
		
		return new Mutation(orig.guestCopy(), displaced.guestCopy());
		
//		moveTable.removeGuest(displaced);
//		origTable.removeGuest(orig);
//		moveTable.addGuest(orig);
//		origTable.addGuest(displaced);
//		System.out.println("moveTable fitness before: " + moveTable.getFitnessRating());
//		moveTable.calculateFitness();
//		System.out.println("moveTable fitness after: " + moveTable.getFitnessRating());
//		System.out.println("origTable fitness before: " + origTable.getFitnessRating());
//		origTable.calculateFitness();
//		System.out.println("origTable fitness after: " + origTable.getFitnessRating());
	}
	
	public EventTable getEventTableByNumber(String num){
		for (EventTable et : this.eventTables){
			if(et.getEventTableNum() == Integer.parseInt(num))return et;
		}
		return null;
	}
	
	public Guest findGuestByNumber(int num){
		for (Guest g : this.allGuests()){
			if(g.getGuestNumber() == num)return g;
		}
		return null;
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
					if(g.getGuestFitness().compareTo(BigDecimal.valueOf(50))==-1){
						bottom50.add(g.guestCopy());
					}
				}
			}
			
			System.out.println("Before crossover fitnesses (" + this.getOverallFitnessRating() + ")");
			this.printCurrentGuestFitness();
			
			Iterator<Guest> gIter = bottom50.iterator(), gIter2 = seatingArrangement.allGuests().iterator();
			Guest g, g2;
			Mutation m;
			EventTable movedFrom, movedTo;
			
			while(gIter.hasNext()){
				g = gIter.next();
				//for(Guest g2 : bottom50){
				while(gIter2.hasNext()){
					g2 = gIter2.next().guestCopy();
						
					if(g.getName() == g2.getName()){
						if(Integer.parseInt(g2.getEventTableNumber()) != Integer.parseInt(g.getEventTableNumber())){
							m = this.mutate(g2, g.getEventTableNumber());
							movedFrom = this.getEventTableByNumber(m.getMoving().getEventTableNumber());
							movedTo = this.getEventTableByNumber(m.getDisplaced().getEventTableNumber());
							
							//System.out.println("moveTable fitness before: " + et1.getFitnessRating());
							//System.out.println("origTable fitness before: " + et2.getFitnessRating());
							
							System.out.println("moveTable size: " + movedFrom.getGuests().size());
							System.out.println("origTable size: " + movedTo.getGuests().size());
							movedFrom.removeGuestByClone(m.getMoving());
							movedTo.removeGuestByClone(m.getDisplaced());
							movedFrom.addGuestByClone(m.getDisplaced());
							movedTo.addGuestByClone(m.getMoving());
							gIter.remove();
							System.out.println("moveTable size after: " + movedFrom.getGuests().size());
							System.out.println("origTable size after: " + movedTo.getGuests().size());
							movedFrom.calculateFitness();
							movedTo.calculateFitness();
							break;
						}
						//System.out.println("moveTable fitness after: " + et1.getFitnessRating());
						//System.out.println("origTable fitness after: " + et2.getFitnessRating());
						break;
					}
				}
			}
			
			
//			Iterator<EventTable> etIter = seatingArrangement.getEventTables().iterator();
//			Iterator<Guest> gIter, gIter2;
//			EventTable et;
//			Guest g, g2;
//			Mutation m;
			
//			for(EventTable et : seatingArrangement.getEventTables()){
//			while(etIter.hasNext()){
//				et = etIter.next();
//				gIter = et.getGuests().iterator();
//				//for(Guest g : et.getGuests()){
//				while(gIter.hasNext()){
//					g = gIter.next();
//					gIter2 = bottom50.iterator();
//					//for(Guest g2 : bottom50){
//					while(gIter2.hasNext()){
//						g2 = gIter2.next();
//						if(g.getName() == g2.getName()){
//							m = this.mutate(g2, g.getEventTableNumber());
//							break;
//						}
//					}
//					
//				}
//			}
			
			this.calculateOverallFitness();
			System.out.println("After crossover fitnesses (" + this.getOverallFitnessRating() + ")");
			this.printCurrentGuestFitness();
			return origSA.getOverallFitnessRating().compareTo(this.getOverallFitnessRating()) == 1 ? origSA : this;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return this;
		}
	}
	
	public void printCurrentGuestFitness(){
		for (EventTable et : this.eventTables){
			System.out.println("Table#" + et.getEventTableNum());
			for (Guest g : et.getGuests()){
				System.out.println(g.getName() + "(" + g.getGuestFitness() + ")");
			}
		}
	}
	
	public List<Guest> allGuests(){
		List<Guest> all = new ArrayList<Guest>();
		
		for (EventTable et : this.eventTables){
			all.addAll(et.getGuests());
		}
		
		return all;
	}

	/**
	 * The operation that calculates the overall fitness of the solution for the SeatingArrangement for the genetic algorithm.
	 * This should be the average fitness over all of the tables in the Seating Arrangement
	 */
	public void calculateOverallFitness() {
		BigDecimal fitness = BigDecimal.ZERO;
		
		for(EventTable et : this.eventTables){
			if(et.getFitnessRating() == null) {
				et.calculateFitness();
			}
			System.out.println("fitness(" + fitness + ") + Table #" + et.getEventTableNum() + "("+ et.getFitnessRating() + ") = " + fitness.add(et.getFitnessRating()).toString());
			fitness = fitness.add(et.getFitnessRating());
		}
		
		this.overallFitnessRating = fitness.divide(BigDecimal.valueOf(this.eventTables.size()), 2, RoundingMode.HALF_UP);
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
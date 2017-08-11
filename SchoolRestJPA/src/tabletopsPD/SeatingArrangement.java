package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Random;
import java.util.TreeSet;

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
public class SeatingArrangement implements Serializable, Cloneable, Comparable{

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
	
	private boolean saValid = true;
	
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
		
	     Random number = new Random();
	     number.setSeed(System.currentTimeMillis());
	     EventTable first = new EventTable(1);
	     List<Integer> usedRandoms = new ArrayList<Integer>();
	     first.setSeatingArrangement(newArrangement);
	     newArrangement.eventTables.add(first);
	     
	     for (int i = 0; i < newArrangement.event.getGuestList().size(); i++) {
	    	 guestidx = number.nextInt(newArrangement.event.getGuestList().size());
	    	 while(usedRandoms.contains(guestidx)){
	    		 guestidx = number.nextInt(newArrangement.event.getGuestList().size());
	    	 }
	    	 usedRandoms.add(guestidx);
	    	 
	    	 newGuest = newArrangement.event.getGuestList().get(guestidx).guestCopy();
	    	 
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
	}

	/**
	 * Performs a random change to the seating arrangement (i.e. swapping 2 guests between tables).
	 */
	public Mutation mutate(Guest orig, int moveTableNum) {
		//EventTable moveTable = this.eventTables.get(Integer.parseInt(moveTableNum));
		//EventTable origTable = this.eventTables.get(Integer.parseInt(orig.getEventTableNumber()));
		EventTable moveTable  = this.getEventTableByNumber(moveTableNum);
		String origTableNum = this.findGuestByNumber(orig.getGuestNumber()).getEventTableNumber();
		//EventTable origTable = this.getEventTableByNumber(origTableNum);
		
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
	}
	
	public void copyTable(EventTable et){
		System.out.println("Start new copyTable");
		System.out.println(" ");
		//EventTable current = this.getEventTableByNumber(et.getEventTableNum());
		EventTable current = this.leastFitTable();
		System.out.println("Currently in SeatingArrangement table#" + current.getEventTableNum());
		int i = 1;
		for(Guest g : current.getGuests()){
			System.out.println("#" + i + " " + g.getName() + "(id#" + g.getGuestId() + " #" + g.getGuestNumber() + ") :" + g.getGuestFitness());
			i++;
		}
		System.out.println("Equivalent table#" + et.getEventTableNum());
		i = 1;
		for(Guest g : et.getGuests()){
			System.out.println("#" + i + " " + g.getName() + "(id#" + g.getGuestId() + " #" + g.getGuestNumber() + ") :" + g.getGuestFitness());
			i++;
		}
		
		//1
		this.destroyAllDupes(et.getGuests());
		
		//2
		List<Guest> diff = this.eventTableGuestDiff(et, current);
		
		//3
		current.removeGuest(diff);
		
		//4
		this.removeEventTable(current);
		
		//5
		this.addEventTable(et);
		for(Guest g : et.getGuests()){
			g.setEventTable(et);
		}
		
		//6
		this.fillSeats(diff);
		
		System.out.println(" ");
		System.out.println("End copyTable");
		System.out.println(" ");
	}
	
	public EventTable equivalentTable(EventTable et){
		List<Guest> etGuests = new ArrayList<Guest>();
		for (Guest g : et.getGuests()){
			etGuests.add(this.findGuestByNumber(g.getGuestNumber()));
		}
		
		EventTable equivalent = new EventTable(this.leastFitTable().getEventTableNum(), etGuests);
		equivalent.setFitnessRating(et.getFitnessRating());
		
		return equivalent;
	}
	
	public List<Guest> eventTableGuestDiff (EventTable newTable, EventTable oldTable){
		List<Guest> diff = new ArrayList<Guest>();
		for(Guest g : oldTable.getGuests()){
			if(!newTable.getGuests().contains(g))diff.add(g);
		}
		
		return diff;
	}
	
	public EventTable leastFitTable(){
		NavigableSet<EventTable> tables = new TreeSet<EventTable>();
		
		for(EventTable et : this.eventTables){
			tables.add(et);
		}
		
		return tables.pollFirst();
	}
	
	public void destroyAllDupes (List<Guest> coGuests){
		List<Guest> rmGuests;
		for(EventTable et : this.eventTables){
			rmGuests = new ArrayList<Guest>();
			for(Guest g : coGuests){
				if(et.getGuests().contains(g)){
					rmGuests.add(g);
					System.out.println("Removed guest " + g.getName() + " from table #" + et.getEventTableNum());
				}
				
			}
			et.removeGuest(rmGuests);
		}
	}
	
	public void fillSeats(List<Guest> guests){
		int tableSize = this.event.getEventTableSize();
		Guest g;
		
		for(int i=0; i < guests.size(); i++){
			g = guests.get(i);
			
			for(EventTable et : this.eventTables){
				if(et.getGuests().size() < tableSize){
					et.addGuest(g);
					while(et.getGuests().size() < tableSize && i+1 < guests.size()){
						i++;
						g = guests.get(i);
						et.addGuest(g);
						System.out.println("Added guest " + g.getName() + " to table #" + et.getEventTableNum());
					}
					break;
				}
			}
		}
	}
	
	public EventTable getEventTableByNumber(int num){
		for (EventTable et : this.eventTables){
			if(et.getEventTableNum() == num)return et;
		}
		return null;
	}
	
	public Guest findGuestByNumber(int num){
		for (Guest g : this.allGuests()){
			if(g.getGuestNumber() == num){
				return g;
			}
		}
		return null;
	}
	
	/**
	 * Produces a child seating arrangement based on two parent seating arrangements.
	 * @param SeatingArrangement
	 */
	public SeatingArrangement crossover(SeatingArrangement seatingArrangement) {
		
		try {
			SeatingArrangement origSA = (SeatingArrangement) this.clone();
		
			List<EventTable> top50 = new ArrayList<EventTable>(),
					saTables = seatingArrangement.eventTables;
			
			Collections.sort(saTables, new Comparator<EventTable>() {

				@Override
				public int compare(EventTable et1, EventTable et2) {
					return et1.getFitnessRating().compareTo(et2.getFitnessRating());
				}
			});
			
			for(int i = 0; i < saTables.size()/2; i++){
				top50.add(saTables.get(i));
			}
			
			for(EventTable et : top50){
				this.copyTable(this.equivalentTable(et));
				this.calculateOverallFitness();
			}

			return origSA.getOverallFitnessRating().compareTo(this.getOverallFitnessRating()) == 1 ? origSA : this;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
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
			if(!et.isValid()){
				this.saValid = false;
			}
			
			//System.out.println("fitness(" + fitness + ") + Table #" + et.getEventTableNum() + "("+ et.getFitnessRating() + ") = " + fitness.add(et.getFitnessRating()).toString());
			fitness = fitness.add(et.getFitnessRating());
		}
		
		this.overallFitnessRating = fitness.divide(BigDecimal.valueOf(this.eventTables.size()), 2, RoundingMode.HALF_UP);
	}
	
	// TODO: Check if seating valid 
	public Boolean isValid() {
		this.calculateOverallFitness();
		return saValid;
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

	public void addEventTable(EventTable et){
		this.eventTables.add(et);
	}
	
	public void removeEventTable(EventTable et){
		this.eventTables.remove(et);
		System.out.println("table #" + et.getEventTableNum() + " was removed:");
		for(Guest g : et.getGuests()){
			System.out.println("--" + g.getName());
		}
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
	
	@Override
	public int compareTo(Object o) {
		return this.overallFitnessRating.compareTo(((SeatingArrangement)o).overallFitnessRating);
	}

}
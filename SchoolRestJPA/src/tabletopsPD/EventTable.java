package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.owlike.genson.annotation.JsonIgnore;

import tabletopsDAO.EM;
import tabletopsDAO.EventDAO;

@XmlRootElement(name = "event_table")
@Entity(name = "event_table")
public class EventTable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EventTable(int tableNum){
		this.eventTableNum = tableNum;
		this.guests = new ArrayList<Guest>();
	}
	
	public EventTable() {
		
	}
	
	@Id //signifies the primary key
	@Column(name = "event_table_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long eventTableId;
	
	/**
	 * The number that identifies the Table at the Event.
	 */
	@Column(name = "event_table_num")
	private int eventTableNum;
	
	/**
	 * The fitness rating for determining the greater fitness rating of the SeatingArrangement containing this instance of Table.
	 */
	@Column(name = "fitness_rating")
	private BigDecimal fitnessRating;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "eventTable", orphanRemoval = true)
	private List<Guest> guests;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="seating_arrangement",referencedColumnName="seating_arrangement_id")
	private SeatingArrangement seatingArrangement;

	@JsonIgnore
	public SeatingArrangement getSeatingArrangement() {
		return seatingArrangement;
	}

	@XmlElement
	public void setSeatingArrangement(SeatingArrangement seatingArrangement) {
		this.seatingArrangement = seatingArrangement;
	}

	/**
	 * An operation that calculates the fitness of the current instance of Table, based on the number of empty seats and the satisfaction of the guests with who they are seated with (determined by their black and white lists).
	 */
	public void calculateFitness() {
		BigDecimal fitness = BigDecimal.ZERO;
		BigDecimal perGuest = BigDecimal.valueOf(100).divide(BigDecimal.valueOf(this.guests.size()), 2, RoundingMode.HALF_UP);
		
		int emptySeatsDiffMax = this.seatingArrangement.getEvent().getEventTableSize() - this.guests.size() - this.seatingArrangement.getEvent().getMaxEmptySeats();
		
		for (Guest g : this.guests){
			BigDecimal total = BigDecimal.ZERO;
			int totalGuestsInBLWL = g.getBlacklist().size() + g.getWhitelist().size();
			
			//TODO: create "is same guest" function using event number and guest number for use here
			
			//decrease fitness score for each black listed guest at the table
			if(totalGuestsInBLWL != 0){
				for(Guest g2 : g.getBlacklist()){
					if(this.tableHasGuest(g2)){//.getListMember())){
						total = total.subtract(perGuest);
					}
				}
				
				//increase fitness score for each white listed guest at the table
				for(Guest g3 : g.getWhitelist()){
					if(this.tableHasGuest(g3)){//.getListMember())){
						total = total.add(perGuest);
					}
				}
				
				total = total.divide(BigDecimal.valueOf(totalGuestsInBLWL));
			}
			
			g.setGuestFitness(total);
			fitness = fitness.add(total);
		}
		
		//decrease fitness score for each empty chair over the maximum allowed empty chairs at a table
		//this will give us our EventTable fitness
		this.fitnessRating = fitness.subtract(BigDecimal.valueOf(emptySeatsDiffMax).multiply(perGuest));
	}

	public int getEventTableNum() {
		return eventTableNum;
	}

	@XmlElement
	public void setEventTableNum(int eventTableNum) {
		this.eventTableNum = eventTableNum;
	}

	public BigDecimal getFitnessRating() {
		return fitnessRating;
	}

	@XmlElement
	public void setFitnessRating(BigDecimal fitnessRating) {
		this.fitnessRating = fitnessRating;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public void addGuest(Guest g){
		g.setEventTable(this);
		guests.add(g);
	}
	
	public void removeGuest(Guest g){
		guests.remove(g);
	}
	
	public boolean tableHasGuest(Guest g){
		for(Guest g2 : this.guests){
			if(g2.isSameGuest(g))return true;
		}
		return false;
	}
	
	@XmlElement
	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}
}
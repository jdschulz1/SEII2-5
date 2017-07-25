package tabletopsPD;

import java.io.Serializable;
import java.math.BigDecimal;
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
	public SeatingArrangement createArrangement() {
		// TODO - implement SeatingArrangement.createArrangement
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs a random change to the seating arrangement (i.e. swapping 2 guests between tables).
	 */
	public void mutate() {
		// TODO - implement SeatingArrangement.mutate
		throw new UnsupportedOperationException();
	}

	/**
	 * Produces a child seating arrangement based on two parent seating arrangements.
	 * @param SeatingArrangement
	 */
	public SeatingArrangement crossover(SeatingArrangement seatingArrangement) {
		// TODO - implement SeatingArrangement.crossover
		throw new UnsupportedOperationException();
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

}
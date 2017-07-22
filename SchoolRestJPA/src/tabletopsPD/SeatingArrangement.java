package tabletopsPD;

import java.math.BigDecimal;
import java.util.List;

/**
 * The SeatingArrangement is a solution in the genetic algorithm for best seating arrangement for the Event.
 */
public class SeatingArrangement {

	/**
	 * The overall fitness rating for the SeatingArrangement for the purpose of finding the best Seating Arrangement.
	 */
	private BigDecimal overallFitnessRating;
	
	private List<Table> tables;
	
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
	public SeatingArrangement crossover(int SeatingArrangement) {
		// TODO - implement SeatingArrangement.crossover
		throw new UnsupportedOperationException();
	}

	/**
	 * The operation that calculates the overall fitness of the solution for the SeatingArrangement for the genetic algorithm.
	 */
	public void calculateOverallFitness() {
		// TODO - implement SeatingArrangement.calculateOverallFitness
		throw new UnsupportedOperationException();
	}

	public BigDecimal getOverallFitnessRating() {
		return overallFitnessRating;
	}

	public void setOverallFitnessRating(BigDecimal overallFitnessRating) {
		this.overallFitnessRating = overallFitnessRating;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
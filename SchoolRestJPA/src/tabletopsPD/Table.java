package tabletopsPD;

import java.math.BigDecimal;
import java.util.List;

public class Table {

	/**
	 * The number that identifies the Table at the Event.
	 */
	private int tableNum;
	/**
	 * The fitness rating for determining the greater fitness rating of the SeatingArrangement containing this instance of Table.
	 */
	private BigDecimal fitnessRating;
	
	private List<Guest> guests;

	/**
	 * An operation that calculates the fitness of the current instance of Table, based on the number of empty seats and the satisfaction of the guests with who they are seated with (determined by their black and white lists).
	 */
	public void calculateFitness() {
		// TODO - implement Table.calculateFitness
		throw new UnsupportedOperationException();
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}

	public BigDecimal getFitnessRating() {
		return fitnessRating;
	}

	public void setFitnessRating(BigDecimal fitnessRating) {
		this.fitnessRating = fitnessRating;
	}

	public List<Guest> getGuests() {
		return guests;
	}

	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}

}
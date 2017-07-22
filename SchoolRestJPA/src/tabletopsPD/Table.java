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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "table")
@Entity(name = "table")
public class Table implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id //signifies the primary key
	@Column(name = "table_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long tableId;
	
	/**
	 * The number that identifies the Table at the Event.
	 */
	@Column(name = "table_num")
	private int tableNum;
	
	/**
	 * The fitness rating for determining the greater fitness rating of the SeatingArrangement containing this instance of Table.
	 */
	@Column(name = "fitness_rating")
	private BigDecimal fitnessRating;
	
	@OneToMany(cascade = CascadeType.ALL, 
	        mappedBy = "table", orphanRemoval = true)
	private List<Guest> guests;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="seating_arrangement",referencedColumnName="seating_arrangement_id")
	private SeatingArrangement seatingArrangement;

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

	@XmlElement
	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
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

	@XmlElement
	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}

}
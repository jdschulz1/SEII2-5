package tabletopsDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsPD.SeatingArrangement;

public class SeatingArrangementDAO {

	public static void saveSeatingArrangement(SeatingArrangement seatingArrangement) {
		EM.getEM().persist(seatingArrangement);
	}

	public static void addSeatingArrangement(SeatingArrangement seatingArrangement) {
		EM.getEM().persist(seatingArrangement);
	}

	public static List<SeatingArrangement> listSeatingArrangement() {
		TypedQuery<SeatingArrangement> query = EM.getEM().createQuery(
				"SELECT seatingArrangement FROM seatingArrangement seatingArrangement", SeatingArrangement.class);
		return query.getResultList();
	}

	public static List<SeatingArrangement> getAllSeatingArrangements(int page, int pageSize) {
		TypedQuery<SeatingArrangement> query = EM.getEM().createQuery(
				"SELECT seating_arrangement FROM seating_arrangement seating_arrangement", SeatingArrangement.class);
		return query.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
	}

	public static SeatingArrangement findSeatingArrangementByIdNumber(String idNumber) {
		String qString = "SELECT seating_arrangement FROM seating_arrangement seating_arrangement  WHERE seating_arrangement.seating_arrangement_id ="
				+ idNumber;
		Query query = EM.getEM().createQuery(qString);
		SeatingArrangement seatingArrangement = (SeatingArrangement) query.getSingleResult();
		return seatingArrangement;
	}

	public static SeatingArrangement findSeatingArrangementById(int id) {
		SeatingArrangement seatingArrangement = EM.getEM().find(SeatingArrangement.class, new Integer(id));
		return seatingArrangement;
	}

	/**
	 * @param seating
	 *            arrangement
	 */
	public static void removeSeatingArrangement(SeatingArrangement seatingArrangement) {
		EM.getEM().remove(seatingArrangement);
	}
}

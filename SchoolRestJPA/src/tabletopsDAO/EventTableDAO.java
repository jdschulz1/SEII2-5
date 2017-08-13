package tabletopsDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsPD.EventTable;
import tabletopsPD.Guest;

public class EventTableDAO {

	public static void saveEventTable(EventTable eventTable) {
		EM.getEM().persist(eventTable);
	}

	public static void addEventTable(EventTable eventTable) {
		EM.getEM().persist(eventTable);
	}

	public static List<EventTable> listEventTable() {
		TypedQuery<EventTable> query = EM.getEM().createQuery("SELECT event_table FROM event_table event_table",
				EventTable.class);
		return query.getResultList();
	}

	public static EventTable findEventTableById(long id) {

		EventTable eventTable = EM.getEM().find(EventTable.class, new Long(id));
		return eventTable;
	}

	public static EventTable findEventTableByIdNumber(String idNumber) {
		String qString = "SELECT event_table FROM event_table event_table WHERE event_table.eventTableId =" + idNumber;
		Query query = EM.getEM().createQuery(qString);
		EventTable eventTable = (EventTable) query.getSingleResult();
		return eventTable;
	}

	/**
	 * @param eventTable
	 */
	public static void removeEventTable(EventTable eventTable) {
		EM.getEM().remove(eventTable);
	}

	public static List<Guest> getGuestsForTable(String idNumber, int page, int pageSize) {
		EventTable eventTable = EventTableDAO.findEventTableByIdNumber(idNumber);
		TypedQuery<Guest> query = EM.getEM()
				.createQuery("SELECT guest FROM guest guest WHERE guest.eventTable = :eventTable", Guest.class);
		query.setParameter("eventTable", eventTable);
		return query.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
	}
}
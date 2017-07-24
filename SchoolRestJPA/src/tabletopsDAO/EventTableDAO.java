package tabletopsDAO;
import java.util.List;

import javax.persistence.TypedQuery;

import tabletopsPD.EventTable;

public class EventTableDAO { 

    public static void saveEventTable(EventTable table) {
      EM.getEM().persist(table);
    }
    public static void addEventTable(EventTable table) {
      EM.getEM().persist(table);
    }

    public static List<EventTable> listEventTable()
    {
      TypedQuery<EventTable> query = EM.getEM().createQuery("SELECT table FROM event_table event_table", EventTable.class);
      return query.getResultList();
    }

    public static EventTable findEventTableById(int id)
    {
      EventTable eventTable = EM.getEM().find(EventTable.class, new Integer(id));
      return eventTable;
    }

    /**
     * @param eventTable
     */
    public static void removeEventTable(EventTable eventTable)
    {
      EM.getEM().remove(eventTable);
    }
  }



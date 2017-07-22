package tabletopsDAO;
import java.util.List;

import javax.persistence.TypedQuery;

import tabletopsPD.Event;
import tabletopsPD.Guest;

public class EventDAO { 

    public static void saveEvent(Event event) {
      EM.getEM().persist(event);
    }
    public static void addEvent(Event event) {
      EM.getEM().persist(event);
    }

    public static List<Event> listEvent()
    {
      TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
      return query.getResultList();
    }

    public static Guest findGuestById(int id)
    {
      Guest guest = EM.getEM().find(Guest.class, new Integer(id));
      return guest;
    }

    /**
     * @param guest
     */
    public static void removeGuest(Guest guest)
    {
      EM.getEM().remove(guest);
    }
  }



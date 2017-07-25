package tabletopsDAO;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsDAO.EM;
import tabletopsPD.Client;
import tabletopsPD.Event;
import tabletopsPD.Guest;
import tabletopsPD.User;

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
    
    public static List<Event> getAllEvents(int page, int pageSize)
    {
      TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
      return query.setFirstResult(page * pageSize)
              .setMaxResults(pageSize)
              .getResultList();
    }
    public static List<Event> getEventsForUser(String idNumber, int page, int pageSize)
    {
    	User user = UserDAO.findUserByIdNumber(idNumber);
        TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event WHERE event.primaryPlanner = :user", Event.class);
        query.setParameter("user", user);
        return query.setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public static Event findEventByIdNumber(String idNumber)
    {
      String qString = "SELECT event FROM event event WHERE event.eventId ="+idNumber;
      Query query = EM.getEM().createQuery(qString);
      Event event = (Event)query.getSingleResult();
      return event;
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
    
    public static void removeEvent(Event event)
    {
      EM.getEM().remove(event);
    }
  }



package tabletopsDAO;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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
    
    public static List<Event> getEventsForClient(String idNumber, int page, int pageSize)
    {
    	Client client = ClientDAO.findClientByIdNumber(idNumber);
        TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event WHERE event.client = :client", Event.class);
        query.setParameter("client", client);
        return query.setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    
    public static List<Event> getEventsForDate(String datestr, int page, int pageSize)
    {
    	//User user = UserDAO.findUserByIdNumber(idNumber);
    	//Date date = new SimpleDateFormat("yyyy-MM-dd").parse(datestr);
    	//String newdate = date.toString();
    	//Date date = formatter.format(datestr);
    	//Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(datestr);
    	LocalDateTime date = LocalDateTime.parse(datestr);
    	LocalDateTime startDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MIN);
		LocalDateTime endDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MAX);
//    	LocalDateTime endDate = date.MAX;
//    	LocalDateTime startDate = date.MIN;
        TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event WHERE event.eventDateTime >= :startDate AND event.eventDateTime < :endDate", Event.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    
    public static List<Event> getEventsForClientAndDate(String idNumber, String datestr, int page, int pageSize)
    {
    	Client client = ClientDAO.findClientByIdNumber(idNumber);
    	
    	LocalDateTime date = LocalDateTime.parse(datestr);
    	LocalDateTime startDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MIN);
		LocalDateTime endDate = LocalDateTime.of(date.toLocalDate(), LocalTime.MAX);
        TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event WHERE event.client = :client AND event.eventDateTime >= :startDate AND event.eventDateTime < :endDate", Event.class);
        query.setParameter("client", client);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public static List<Guest> getGuestsForEvent(String idNumber, int page, int pageSize)
    {
    	Event event = EventDAO.findEventByIdNumber(idNumber);
    	TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest WHERE guest.event = :event", Guest.class);
        query.setParameter("event", event);
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



package tabletopsDAO;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsPD.Event;
import tabletopsPD.Guest;

public class GuestDAO { 

    public static void saveGuest(Guest guest) {
      EM.getEM().persist(guest);
    }
    public static void addGuest(Guest guest) {
      EM.getEM().persist(guest);
    }
    public static List<Guest> listGuest()
    {
      TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest", Guest.class);
      return query.getResultList();
    }
    
    public static List<Guest> getAllGuestsForEvent(Event event)
    {
      TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest", Guest.class);
      return query.getResultList();
    }
    public static Guest findGuestById(int id)
    {
      Guest guest = EM.getEM().find(Guest.class, new Integer(id));
      return guest;
    }
    
    public static Guest findGuestByIdNumber(String idNumber)
    {
      String qString = "SELECT guest FROM guest guest  WHERE guest.guestId ="+idNumber;
      Query query = EM.getEM().createQuery(qString);
      Guest guest = (Guest)query.getSingleResult();
      return guest;
    }

    public static List<Guest> getAllGuestsForEvent(Event event,int page, int pageSize)
    {
      TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest", Guest.class);
      return query.setFirstResult(page * pageSize)
              .setMaxResults(pageSize)
              .getResultList();
    }
    
    public static void removeGuest(Guest guest)
    {
      EM.getEM().remove(guest);
    }
  }


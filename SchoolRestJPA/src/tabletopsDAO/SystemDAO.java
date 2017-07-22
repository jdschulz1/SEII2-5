package tabletopsDAO;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsDAO.EM;
import tabletopsPD.Client;
import tabletopsPD.Event;
import tabletopsPD.User;

public class SystemDAO { 

    public static void saveSystem(tabletopsPD.System system) {
      EM.getEM().persist(system);
    }
    public static void addSystem(tabletopsPD.System system) {
      EM.getEM().persist(system);
    }

    public static List<tabletopsPD.System> listSystem()
    {
      TypedQuery<tabletopsPD.System> query = EM.getEM().createQuery("SELECT system FROM system system", tabletopsPD.System.class);
      return query.getResultList();
    }

    public static List<Event> listEvent()
    {
      TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
      return query.getResultList();
    }
    
    public static Event findEventByIdNumber(String idNumber)
    {
      String qString = "SELECT event FROM event event  WHERE event.idNumber ="+idNumber;
      Query query = EM.getEM().createQuery(qString);
      Event event = (Event)query.getSingleResult();
      return event;
    }
    
    public static List<Client> listClient()
    {
      TypedQuery<Client> query = EM.getEM().createQuery("SELECT client FROM client client", Client.class);
      return query.getResultList();
    }
    
    public static List<User> listUser()
    {
      TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
      return query.getResultList();
    }
  }



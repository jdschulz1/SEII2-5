package tabletopsDAO;
import java.util.List;

import javax.persistence.TypedQuery;

import tabletopsPD.Client;
import tabletopsPD.Event;
import tabletopsPD.User;

public class SystemDAO { 

    public static void saveSystem(System system) {
      EM.getEM().persist(system);
    }
    public static void addSystem(System system) {
      EM.getEM().persist(system);
    }

    public static List<System> listSystem()
    {
      TypedQuery<System> query = EM.getEM().createQuery("SELECT system FROM system system", System.class);
      return query.getResultList();
    }

    public static List<Event> listEvent()
    {
      TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
      return query.getResultList();
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



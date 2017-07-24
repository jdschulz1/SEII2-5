package tabletopsDAO;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsDAO.EM;
import tabletopsPD.Client;
import tabletopsPD.Company;
import tabletopsPD.Event;
import tabletopsPD.User;

public class CompanyDAO { 

    public static void saveSystem(Company company) {
      EM.getEM().persist(company);
    }
    public static void addSystem(Company company) {
      EM.getEM().persist(company);
    }

    public static List<Company> listCompany()
    {
      TypedQuery<Company> query = EM.getEM().createQuery("SELECT company FROM company company", Company.class);
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



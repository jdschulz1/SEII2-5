package tabletopsDAO;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import schoolDAO.EM;
import tabletopsPD.Client;

public class ClientDAO { 

    public static void saveClient(Client client) {
      EM.getEM().persist(client);
    }
    public static void addClient(Client client) {
      EM.getEM().persist(client);
    }

    public static List<Client> listClient()
    {
      TypedQuery<Client> query = EM.getEM().createQuery("SELECT client FROM client client", Client.class);
      return query.getResultList();
    }
    
    public static List<Client> getAllClients(int page, int pageSize)
    {
      TypedQuery<Client> query = EM.getEM().createQuery("SELECT client FROM client client", Client.class);
      return query.setFirstResult(page * pageSize)
              .setMaxResults(pageSize)
              .getResultList();
    }

    public static Client findClientById(int id)
    {
      Client client = EM.getEM().find(Client.class, new Integer(id));
      return client;
    }
    
    public static Client findClientByIdNumber(String idNumber)
    {
      String qString = "SELECT client FROM client client  WHERE client.idNumber ="+idNumber;
      Query query = EM.getEM().createQuery(qString);
      Client client = (Client)query.getSingleResult();
      return client;
    }

    /**
     * @param client
     */
    public static void removeClient(Client client)
    {
      EM.getEM().remove(client);
    }
  }



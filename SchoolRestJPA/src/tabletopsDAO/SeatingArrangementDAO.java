package tabletopsDAO;
import java.util.List;

import javax.persistence.TypedQuery;

import tabletopsPD.SeatingArrangement;

public class SeatingArrangementDAO { 

    public static void saveSeatingArrangement(SeatingArrangement seatingArrangement) {
      EM.getEM().persist(seatingArrangement);
    }
    public static void addSeatingArrangement(SeatingArrangement seatingArrangement) {
      EM.getEM().persist(seatingArrangement);
    }

    public static List<SeatingArrangement> listSeatingArrangement()
    {
      TypedQuery<SeatingArrangement> query = EM.getEM().createQuery("SELECT seatingArrangement FROM seatingArrangement seatingArrangement", SeatingArrangement.class);
      return query.getResultList();
    }

    public static SeatingArrangement findSeatingArrangementById(int id)
    {
      SeatingArrangement seatingArrangement = EM.getEM().find(SeatingArrangement.class, new Integer(id));
      return seatingArrangement;
    }

    /**
     * @param seating arrangement
     */
    public static void removeSeatingArrangement(SeatingArrangement seatingArrangement)
    {
      EM.getEM().remove(seatingArrangement);
    }
  }



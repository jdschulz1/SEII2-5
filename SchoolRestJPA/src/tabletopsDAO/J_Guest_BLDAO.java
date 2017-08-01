//package tabletopsDAO;
//
//import java.util.List;
//
//import javax.persistence.Query;
//import javax.persistence.TypedQuery;
//
//import tabletopsPD.J_Guest_BL;
//
//public class J_Guest_BLDAO {
//    public static void saveClient(J_Guest_BL bl) {
//        EM.getEM().persist(bl);
//      }
//      public static void addClient(J_Guest_BL bl) {
//        EM.getEM().persist(bl);
//      }
//
//      public static List<J_Guest_BL> listClient()
//      {
//        TypedQuery<J_Guest_BL> query = EM.getEM().createQuery("SELECT j_guest_bl FROM j_guest_bl j_guest_bl", J_Guest_BL.class);
//        return query.getResultList();
//      }
//      
//      public static List<J_Guest_BL> getAllClients(int page, int pageSize)
//      {
//        TypedQuery<J_Guest_BL> query = EM.getEM().createQuery("SELECT j_guest_bl FROM j_guest_bl j_guest_bl", J_Guest_BL.class);
//        return query.setFirstResult(page * pageSize)
//                .setMaxResults(pageSize)
//                .getResultList();
//      }
//
//      public static J_Guest_BL findClientById(int id)
//      {
//    	J_Guest_BL bl = EM.getEM().find(J_Guest_BL.class, new Integer(id));
//        return bl;
//      }
//      
//      public static J_Guest_BL findClientByIdNumber(String idNumber)
//      {
//        String qString = "SELECT j_guest_bl FROM j_guest_bl j_guest_bl  WHERE j_guest_bl.jGuestBLId ="+idNumber;
//        Query query = EM.getEM().createQuery(qString);
//        J_Guest_BL bl = (J_Guest_BL)query.getSingleResult();
//        return bl;
//      }
//
//      /**
//       * @param j_guest_bl
//       */
//      public static void removeClient(J_Guest_BL bl)
//      {
//        EM.getEM().remove(bl);
//      }
//}

//package tabletopsDAO;
//
//import java.util.List;
//
//import javax.persistence.Query;
//import javax.persistence.TypedQuery;
//
//import tabletopsPD.J_Guest_WL;
//
//public class J_Guest_WLDAO {
//    public static void saveClient(J_Guest_WL wl) {
//        EM.getEM().persist(wl);
//      }
//      public static void addClient(J_Guest_WL wl) {
//        EM.getEM().persist(wl);
//      }
//
//      public static List<J_Guest_WL> listClient()
//      {
//        TypedQuery<J_Guest_WL> query = EM.getEM().createQuery("SELECT j_guest_wl FROM j_guest_wl j_guest_wl", J_Guest_WL.class);
//        return query.getResultList();
//      }
//      
//      public static List<J_Guest_WL> getAllClients(int page, int pageSize)
//      {
//        TypedQuery<J_Guest_WL> query = EM.getEM().createQuery("SELECT j_guest_wl FROM j_guest_wl j_guest_wl", J_Guest_WL.class);
//        return query.setFirstResult(page * pageSize)
//                .setMaxResults(pageSize)
//                .getResultList();
//      }
//
//      public static J_Guest_WL findClientById(int id)
//      {
//    	J_Guest_WL wl = EM.getEM().find(J_Guest_WL.class, new Integer(id));
//        return wl;
//      }
//      
//      public static J_Guest_WL findClientByIdNumber(String idNumber)
//      {
//        String qString = "SELECT j_guest_wl FROM j_guest_wl j_guest_wl  WHERE j_guest_wl.jGuestWLId ="+idNumber;
//        Query query = EM.getEM().createQuery(qString);
//        J_Guest_WL wl = (J_Guest_WL)query.getSingleResult();
//        return wl;
//      }
//
//      /**
//       * @param j_guest_wl
//       */
//      public static void removeClient(J_Guest_WL wl)
//      {
//        EM.getEM().remove(wl);
//      }
//}

package tabletopsDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsDAO.EM;
import tabletopsPD.RoleAssignment;

public class RoleAssignmentDAO { 

  public static void saveRoleAssignment(RoleAssignment roleAssignment) {
    EM.getEM().persist(roleAssignment);
  }
  public static void addRoleAssignment(RoleAssignment roleAssignment) {
    EM.getEM().persist(roleAssignment);
  }
  public static List<RoleAssignment> listRoleAssignment()
  {
    TypedQuery<RoleAssignment> query = EM.getEM().createQuery("SELECT roleAssignment FROM roleAssignment roleAssignment", RoleAssignment.class);
    return query.getResultList();
  }
  
  public static List<RoleAssignment> getAllRoleAssignments()
  {
    TypedQuery<RoleAssignment> query = EM.getEM().createQuery("SELECT roleAssignment FROM roleAssignment roleAssignment", RoleAssignment.class);
    return query.getResultList();
  }
  public static RoleAssignment findRoleAssignmentById(int id)
  {
    RoleAssignment roleAssignment = EM.getEM().find(RoleAssignment.class, new Integer(id));
    return roleAssignment;
  }
  
  public static RoleAssignment findRoleAssignmentByRoleAssignmentName(String roleAssignmentname)
  {
    String qString = "SELECT roleAssignment FROM roleAssignment roleAssignment  WHERE roleAssignment.roleAssignmentname ="+roleAssignmentname;
    Query query = EM.getEM().createQuery(qString);
    RoleAssignment roleAssignment = (RoleAssignment)query.getSingleResult();
    return roleAssignment;
  }

  public static List<RoleAssignment> getAllRoleAssignments(int page, int pageSize)
  {
    TypedQuery<RoleAssignment> query = EM.getEM().createQuery("SELECT roleAssignment FROM roleAssignment roleAssignment", RoleAssignment.class);
    return query.setFirstResult(page * pageSize)
            .setMaxResults(pageSize)
            .getResultList();

  }
  
  public static void removeRoleAssignment(RoleAssignment roleAssignment)
  {
    EM.getEM().remove(roleAssignment);
  }
}



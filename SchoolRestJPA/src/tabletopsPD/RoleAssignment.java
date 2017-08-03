package tabletopsPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "roleAssignment")
@Entity(name = "role_assignment")
public class RoleAssignment implements Serializable {

private static final long serialVersionUID = 1L;

@Id //signifies the primary key
@Column(name = "role_assignment_id", nullable = false)
@GeneratedValue(strategy = GenerationType.AUTO)
  private long roleAssignmentID;

@Enumerated(EnumType.STRING)
  private Role role;

@ManyToOne(optional=false)
@JoinColumn(name="user_id",referencedColumnName="user_id")
  private User user;

  public RoleAssignment(){};
  
  public RoleAssignment(Role role, User user){
    setRole(role);
    setUser(user);
  }
  public long getRoleAssigmentID() {
    return this.roleAssignmentID;
  }
  @XmlTransient
  public void setRoleAssignmentID(long roleAssigmentID) {
    this.roleAssignmentID = roleAssigmentID;
  }
  
  public Role getRole() {
    return this.role;
  }
  @XmlElement
  public void setRole(Role role) {
    this.role = role;
  }

  public User getUser() {
    return this.user;
  }
  @XmlTransient
  public void setUser(User user) {
    this.user = user;
  }

}
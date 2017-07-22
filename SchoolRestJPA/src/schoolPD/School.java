package schoolPD;

import java.io.Serializable;
//import java.util.Collection;
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import schoolDAO.StudentDAO;
//import schoolUT.Log;
//
//@XmlRootElement(name = "school")
//@Entity(name = "school")
public class School implements Serializable {

   private static final long serialVersionUID = 1L;
//   @Id //signifies the primary key
//   @Column(name = "school_id", nullable = false)
//   @GeneratedValue(strategy = GenerationType.AUTO)
//   private int schoolId;
//   
//   @Column(name = "name",nullable = false,length = 40)
//   private String name;
//
//   @OneToMany(mappedBy="school",targetEntity=Student.class,fetch=FetchType.EAGER)
//   private Collection<Student> students;
//
//   public School(){
//
//   }
//   
//   public School(String name){
//     this();
//     this.name = name;
//   }
//
//   public int getSchoolId() {
//      return schoolId;
//   }
//
//
//   public void setId(int schoolId) {
//      this.schoolId = schoolId;
//   }
//   
//	public String getName() {
//		return name;
//	}
//	@XmlElement
//	public void setName(String name) {
//	
//		this.name = name;
//	}
//	
//
//  public Collection<Student> getStudents() {
//    return students;
//  }
//  
//  public Boolean addStudent(Student student) {
//    student.setSchool(this);
//    StudentDAO.addStudent(student);
//    return true;
//
//  }
//  
//  public Boolean removeStudent(Student student)
//  {
//    StudentDAO.removeStudent(student);
//    return true;
//  }
//	public List<Student> getAllStudents(int page, int perPage) {
//		
//		List studentList= StudentDAO.getAllStudentsForSchool( this, page,  perPage);
//		return studentList;
//	}
//	
//	public Student findStudentByID(int id) {
//	  return StudentDAO.findStudentById(id);
//		
//	}
//	
//	public Student findStudentByIdNumber(String idNumber) {
//    return StudentDAO.findStudentByIdNumber(idNumber);
//    
//  }
	
}



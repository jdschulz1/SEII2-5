package schoolDAO;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import schoolPD.School;
import schoolPD.Student;

public class StudentDAO { 

    public static void saveStudent(Student student) {
      EM.getEM().persist(student);
    }
    public static void addStudent(Student student) {
      EM.getEM().persist(student);
    }
    public static List<Student> listStudent()
    {
      TypedQuery<Student> query = EM.getEM().createQuery("SELECT student FROM student student", Student.class);
      return query.getResultList();
    }
    
    public static List<Student> getAllStudentsForSchool(School school)
    {
      TypedQuery<Student> query = EM.getEM().createQuery("SELECT student FROM student student", Student.class);
      return query.getResultList();
    }
    public static Student findStudentById(int id)
    {
      Student student = EM.getEM().find(Student.class, new Integer(id));
      return student;
    }
    
    public static Student findStudentByIdNumber(String idNumber)
    {
      String qString = "SELECT student FROM student student  WHERE student.idNumber ="+idNumber;
      Query query = EM.getEM().createQuery(qString);
      Student student = (Student)query.getSingleResult();
      return student;
    }

    public static List<Student> getAllStudentsForSchool(School school,int page, int pageSize)
    {
      TypedQuery<Student> query = EM.getEM().createQuery("SELECT student FROM student student", Student.class);
      return query.setFirstResult(page * pageSize)
              .setMaxResults(pageSize)
              .getResultList();

    }
    
    public static void removeStudent(Student student)
    {
      EM.getEM().remove(student);
    }
  }


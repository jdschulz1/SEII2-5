package schoolDAO;
import java.util.List;

import javax.persistence.TypedQuery;

import schoolPD.School;
import schoolPD.Student;

public class SchoolDAO { 

    public static void saveSchool(School school) {
      EM.getEM().persist(school);
    }
    public static void addSchool(School school) {
      EM.getEM().persist(school);
    }

    public static List<School> listSchool()
    {
      TypedQuery<School> query = EM.getEM().createQuery("SELECT school FROM school school", School.class);
      return query.getResultList();
    }

    public static Student findStudentById(int id)
    {
      Student student = EM.getEM().find(Student.class, new Integer(id));
      return student;
    }

    /**
     * @param student
     */
    public static void removeStudent(Student student)
    {
      EM.getEM().remove(student);
    }
  }



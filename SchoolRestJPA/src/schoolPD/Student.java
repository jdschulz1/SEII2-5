package schoolPD;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import schoolDAO.EM;
import schoolDAO.StudentDAO;
import schoolUT.Log;
import schoolUT.Message;


@XmlRootElement(name = "student")
@Entity(name = "student")
public class Student implements Serializable {

private static final long serialVersionUID = 1L;
  

  @Id //signifies the primary key
  @Column(name = "student_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long studentId;
  @ManyToOne(optional=false)
  @JoinColumn(name="school_id",referencedColumnName="school_id")
   private School school;
  @Column(name = "id_number",nullable = false,length = 20)
   private String idNumber;
  @Column(name = "first_name",nullable = false,length = 20)
   private String firstName;
  @Column(name = "last_name",nullable = false,length = 20)
   private String lastName;
  @Column(name = "gender",nullable = false,length = 1)
   private String gender;
  @Column(name = "classification",nullable = false,length = 20)
   private String classification;
  @Column(name = "city",nullable = false,length = 40)
   private String city;
  @Column(name = "state",nullable = false,length = 2)
   private String state;
  @Column(name = "zip",nullable = false,length = 5)
   private String zip;
  @Column(name = "email",nullable = false,length = 50)
   private String email;

   public Student(){}
   
   public Student(School school, String idNumber, String firstName, String lastName, String gender,
		   	String classification, String city, String state, String zip, String email)
   {
	   
	   this.school = school;
	   this.idNumber= idNumber;
	   this.firstName=firstName;
	   this.lastName=lastName;
	   this.gender=gender;
	   this.classification=classification;
	   this.city=city;
	   this.state=state;
	   this.zip=zip;
	   this.email=email;
  
   }

   public long getStudentId() {
      return studentId;
   }

   public void setId(int studentId) {
      this.studentId = studentId;
   }
  
	public String getFirstName() {
		return firstName;
	}
	
  @XmlElement
    public void setFirstName(String firstName) {
		this.firstName = firstName;
    }
    

	public String getLastName() {
		return lastName;
	}
	
  @XmlElement
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getGender() {
		return gender;
	}
  
  @XmlElement
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getClassification() {
		return classification;
	}

  @XmlElement
	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getCity() {
		return city;
	}

  @XmlElement
	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

  @XmlElement
	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

  @XmlElement
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public School getSchool() {
		return school;
	}
	
	@XmlTransient
	public void setSchool(School school) {
		this.school = school;
	}

	public String getEmail() {
		return email;
	}

  @XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdNumber() {
		return idNumber;
	}
	
  @XmlElement
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
	
	public Boolean save() {
		
		StudentDAO.saveStudent(this);
		return true;
		
	}
	public Boolean delete() {
	  
		StudentDAO.removeStudent(this);

	  return true;

	}
	
	public Boolean update(Student student) {

    setFirstName(student.getFirstName());
    setLastName(student.getLastName());
    setEmail(student.getEmail());
    setCity(student.getCity());
    setState(student.getState());
    setZip(student.getZip());
    setGender(student.getGender());
    setClassification(student.getClassification());

    return true;
	}
	
	public ArrayList<Message> validate() {
		ArrayList<Message> messages= new ArrayList<Message>();
		Message message;
		if (getIdNumber() == null || getIdNumber().length() ==0){
			message = new Message ("Student000","Id Number must have a value","idNumber");
			messages.add(message);
		}
		if (getFirstName() == null || getFirstName().length() ==0){
			message = new Message ("Student001","First Name must have a value","firstName");
			messages.add(message);
		}

		if (getLastName() == null || getLastName().length() ==0){
			message = new Message ("Student002","Last Name must have a value","lastName");
			messages.add(message);
		}

		if (getEmail() == null || getEmail().length() ==0){
			message = new Message ("Student003","Email must have a value","email");
			messages.add(message);
		}
		if (getGender() == null || getGender().length() ==0){
			message = new Message ("Student004","Gender must have a value","gender");
			messages.add(message);
		}
		
		if (getClassification() == null || getClassification().length() ==0){
			message = new Message ("Student005","Classification must have a value","classification");
			messages.add(message);
		}
		if (getZip() == null || getZip().length() ==0){
			message = new Message ("Student006","Zip must have a value","zip");
			messages.add(message);
		}
		if (getCity() == null || getCity().length() ==0){
			message = new Message ("Student007","City must have a value","city");
			messages.add(message);
		}
		if (getState() != null && getState().length() !=2){
			message = new Message ("Student009","State must be a lenth of 2","state");
			messages.add(message);
		}
		if (getState() == null || getState().length() ==0){
			message = new Message ("Student008","State must have a value","state");
			messages.add(message);
		}

		if (messages.size() == 0 ) return null;
		else return messages;
		
	}

}

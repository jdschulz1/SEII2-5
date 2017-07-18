package schoolPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "zip")
@Entity (name ="zip")
public class Zip implements Serializable {


   private static final long serialVersionUID = 1L;
   @Id //signifies the primary key
   @Column(name = "zip_id", nullable = false)
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;
   @Column(name = "zip")
   private String zip;
   @Column(name = "city")
   private String city;
   @Column(name = "state")
   private String state;


   public Zip(){}
   
   public Zip( String city, String state, String zip){
      this.zip = zip;
      this.city = city;
      this.setState(state);
   }
   
   public int getId() {
     return id;
   }
   
   public void setId(int id){
     this.id = id;
   }

   public String getZip() {
      return zip;
   }

   @XmlElement
   public void setZip(String zip) {
      this.zip = zip;
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
}

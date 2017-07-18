package schoolUT;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
public class Message implements Serializable {

   private static final long serialVersionUID = 1L;
   private String code;
   private String message;
   private String attributeName;

   public Message(String code, String message, String attributeName){
	   this.code = code;
	   this.message = message;
	   this.attributeName = attributeName;
   }
   
   public String getCode() {
      return code;
   }

   @XmlElement
   public void setCode(String code) {
      this.code = code;
   }
   
	public String getMessage() {
		return message;
	}
	@XmlElement
	public void setMessage(String message) {
	
		this.message = message;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	@XmlElement
	public void setAttributeName(String attributeName) {
	
		this.attributeName = attributeName;
	}
}
	

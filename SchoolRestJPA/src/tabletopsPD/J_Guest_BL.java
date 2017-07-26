package tabletopsPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.owlike.genson.annotation.JsonIgnore;

@XmlRootElement(name = "j_guest_bl")
@Entity(name = "j_guest_bl")
public class J_Guest_BL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id //signifies the primary key
	@Column(name = "j_guest_bl_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long jGuestBLId;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="guest",referencedColumnName="guest_id")
	@JoinTable(name="guest")
	private Guest listOwner;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="guest",referencedColumnName="guest_id")
	@JoinTable(name="guest")
	private Guest listMember;
	
	@JsonIgnore
	public Guest getListOwner() {
		return listOwner;
	}

	@XmlElement
	public void setListOwner(Guest listOwner) {
		this.listOwner = listOwner;
	}

	@JsonIgnore
	public Guest getListMember() {
		return listMember;
	}

	@XmlElement
	public void setListMember(Guest listMember) {
		this.listMember = listMember;
	}
}

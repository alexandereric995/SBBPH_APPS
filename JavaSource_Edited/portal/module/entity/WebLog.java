package portal.module.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import bph.entities.kod.Negara;

@Entity
@Table(name="web_log")
public class WebLog {

	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="ip_address")
	private String IPAddress;

	@Column(name="counter")
	private int counter;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "last_log_date")
	private Date lastLogDate;
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="first_log_date")
	private Date firstLogDate;

	@Column(name="public_ip_address")
	private String publicIPAddress;
	
	@ManyToOne
	@JoinColumn(name = "id_kod_negara")
	private Negara negara;
	
	@Column(name="os")
	private String OS;
	
	@Column(name="browser")
	private String browser;
	
	public WebLog() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public Date getLastLogDate() {
		return lastLogDate;
	}

	public void setLastLogDate(Date lastLogDate) {
		this.lastLogDate = lastLogDate;
	}

	public Date getFirstLogDate() {
		return firstLogDate;
	}

	public void setFirstLogDate(Date firstLogDate) {
		this.firstLogDate = firstLogDate;
	}

	public String getPublicIPAddress() {
		return publicIPAddress;
	}

	public void setPublicIPAddress(String publicIPAddress) {
		this.publicIPAddress = publicIPAddress;
	}

	public Negara getNegara() {
		return negara;
	}

	public void setNegara(Negara negara) {
		this.negara = negara;
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String oS) {
		OS = oS;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}	
}
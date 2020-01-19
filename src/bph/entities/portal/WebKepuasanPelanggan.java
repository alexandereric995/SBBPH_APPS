package bph.entities.portal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;

@Entity
@Table(name = "web_kepuasan_pelanggan")
public class WebKepuasanPelanggan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tahap_kepuasan")
	private int tahapKepuasan;

	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="tarikh_undi")
	private Date tarikhUndi;

	public WebKepuasanPelanggan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTahapKepuasan() {
		return tahapKepuasan;
	}

	public void setTahapKepuasan(int tahapKepuasan) {
		this.tahapKepuasan = tahapKepuasan;
	}

	public Date getTarikhUndi() {
		return tarikhUndi;
	}

	public void setTarikhUndi(Date tarikhUndi) {
		this.tarikhUndi = tarikhUndi;
	}	
}

package bph.entities.jrp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "jrp_syor_ulasan_teknikal")
public class JrpSyorUlasanTeknikal {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_ulasan_teknikal")
	private JrpUlasanTeknikal jrpUlasanTeknikal;

	@Column(name = "syor")
	private String syor;

	public JrpSyorUlasanTeknikal() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JrpUlasanTeknikal getJrpUlasanTeknikal() {
		return jrpUlasanTeknikal;
	}

	public void setJrpUlasanTeknikal(JrpUlasanTeknikal jrpUlasanTeknikal) {
		this.jrpUlasanTeknikal = jrpUlasanTeknikal;
	}

	public String getSyor() {
		return syor;
	}

	public void setSyor(String syor) {
		this.syor = syor;
	}
}

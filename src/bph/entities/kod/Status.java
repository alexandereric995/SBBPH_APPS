package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "ruj_status")
public class Status {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "turutan")
	private int turutan;

	@ManyToOne
	@JoinColumn(name = "id_seksyen")
	private Seksyen seksyen;

	public Status() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public int getTurutan() {
		return turutan;
	}

	public void setTurutan(int turutan) {
		this.turutan = turutan;
	}

	public Seksyen getSeksyen() {
		return seksyen;
	}

	public void setSeksyen(Seksyen seksyen) {
		this.seksyen = seksyen;
	}
}

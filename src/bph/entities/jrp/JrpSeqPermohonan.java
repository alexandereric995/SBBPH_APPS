package bph.entities.jrp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.Agensi;
import bph.entities.kod.Kementerian;
import bph.entities.kod.Negeri;

@Entity
@Table(name = "jrp_seq_permohonan")
public class JrpSeqPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tahun")
	private int tahun;

	@ManyToOne
	@JoinColumn(name = "id_kementerian")
	private Kementerian kementerian;

	@ManyToOne
	@JoinColumn(name = "id_agensi")
	private Agensi agensi;

	@ManyToOne
	@JoinColumn(name = "id_negeri")
	private Negeri negeri;

	@Column(name = "bil")
	private int bil;

	public JrpSeqPermohonan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBil() {
		return bil;
	}

	public void setBil(int bil) {
		this.bil = bil;
	}

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public Agensi getAgensi() {
		return agensi;
	}

	public void setAgensi(Agensi agensi) {
		this.agensi = agensi;
	}

	public Negeri getNegeri() {
		return negeri;
	}

	public void setNegeri(Negeri negeri) {
		this.negeri = negeri;
	}

	public Kementerian getKementerian() {
		return kementerian;
	}

	public void setKementerian(Kementerian kementerian) {
		this.kementerian = kementerian;
	}

}
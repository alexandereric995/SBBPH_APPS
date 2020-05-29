package bph.entities.utk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JenisKuartersUtk;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.KawasanUtk;
import bph.entities.kod.ZonUtk;

@Entity
@Table(name = "utk_seq_operasi")
public class UtkSeqOperasi {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_zon")
	private ZonUtk zon;

	@ManyToOne
	@JoinColumn(name = "id_jenis_kuarters")
	private JenisKuartersUtk jenisKuarters;

	@ManyToOne
	@JoinColumn(name = "id_kawasan")
	private KawasanUtk kawasan;

	@ManyToOne
	@JoinColumn(name = "id_jenis_operasi")
	private JenisOperasiUtk jenisOperasi;

	@Column(name = "bil")
	private int bil;

	public UtkSeqOperasi() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonUtk getZon() {
		return zon;
	}

	public void setZon(ZonUtk zon) {
		this.zon = zon;
	}

	public JenisKuartersUtk getJenisKuarters() {
		return jenisKuarters;
	}

	public void setJenisKuarters(JenisKuartersUtk jenisKuarters) {
		this.jenisKuarters = jenisKuarters;
	}

	public KawasanUtk getKawasan() {
		return kawasan;
	}

	public void setKawasan(KawasanUtk kawasan) {
		this.kawasan = kawasan;
	}

	public JenisOperasiUtk getJenisOperasi() {
		return jenisOperasi;
	}

	public void setJenisOperasi(JenisOperasiUtk jenisOperasi) {
		this.jenisOperasi = jenisOperasi;
	}

	public int getBil() {
		return bil;
	}

	public void setBil(int bil) {
		this.bil = bil;
	}

}
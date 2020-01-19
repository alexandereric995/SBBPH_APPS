package bph.entities.utk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;

	@Entity
	@Table(name = "utk_pegawai_operasi")
	public class UtkPegawaiOperasi {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_operasi")
	private UtkOperasi operasi;
	
	/*@ManyToOne
	@JoinColumn(name = "id_pegawai")
	private Users pegawai;*/
	
	@Column(name = "nama_pegawai")
	private String namaPegawai;
	
	@Column(name = "jawatan")
	private String jawatan;
	
	@Column(name = "flag_ketua_operasi")
	private String flagKetuaOperasi;
	
	public UtkPegawaiOperasi() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public UtkOperasi getOperasi() {
		return operasi;
	}


	public void setOperasi(UtkOperasi operasi) {
		this.operasi = operasi;
	}


	public String getNamaPegawai() {
		return namaPegawai;
	}


	public void setNamaPegawai(String namaPegawai) {
		this.namaPegawai = namaPegawai;
	}


	public String getJawatan() {
		return jawatan;
	}


	public void setJawatan(String jawatan) {
		this.jawatan = jawatan;
	}


	public String getFlagKetuaOperasi() {
		return flagKetuaOperasi;
	}


	public void setFlagKetuaOperasi(String flagKetuaOperasi) {
		this.flagKetuaOperasi = flagKetuaOperasi;
	}
}

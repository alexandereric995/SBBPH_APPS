package bph.entities.integrasi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;

@Entity
@Table(name = "int_bpp")
public class IntBPP {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "tarikh_hantar")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikHantar;

	@Column(name = "no_pengenalan")
	private String noPengenalan;

	@Column(name = "tarikh_terima")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhTerima;

	@Column(name = "flag_jawapan")
	private String flagJawapan;

	@Column(name = "mesej")
	private String mesej;

	@Column(name = "kod_jenis_pinjaman")
	private String kodJenisPinjaman;

	@Column(name = "nama")
	private String nama;

	@Column(name = "mukim")
	private String mukim;

	@Column(name = "kod_mukim")
	private String kodMukim;

	@Column(name = "daerah")
	private String daerah;

	@Column(name = "kod_daerah")
	private String kodDaerah;

	@Column(name = "negeri")
	private String negeri;

	@Column(name = "kod_negeri")
	private String kodNegeri;

	@Column(name = "no_akaun")
	private String noAkaun;

	@Column(name = "status_pinjaman")
	private String statusPinjaman;

	@Column(name = "status_data")
	private String statusData;

	public IntBPP() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTarikHantar() {
		return tarikHantar;
	}

	public void setTarikHantar(Date tarikHantar) {
		this.tarikHantar = tarikHantar;
	}

	public String getNoPengenalan() {
		return noPengenalan;
	}

	public void setNoPengenalan(String noPengenalan) {
		this.noPengenalan = noPengenalan;
	}

	public Date getTarikhTerima() {
		return tarikhTerima;
	}

	public void setTarikhTerima(Date tarikhTerima) {
		this.tarikhTerima = tarikhTerima;
	}

	public String getFlagJawapan() {
		return flagJawapan;
	}

	public void setFlagJawapan(String flagJawapan) {
		this.flagJawapan = flagJawapan;
	}

	public String getMesej() {
		return mesej;
	}

	public void setMesej(String mesej) {
		this.mesej = mesej;
	}

	public String getKodJenisPinjaman() {
		return kodJenisPinjaman;
	}

	public void setKodJenisPinjaman(String kodJenisPinjaman) {
		this.kodJenisPinjaman = kodJenisPinjaman;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getMukim() {
		return mukim;
	}

	public void setMukim(String mukim) {
		this.mukim = mukim;
	}

	public String getKodMukim() {
		return kodMukim;
	}

	public void setKodMukim(String kodMukim) {
		this.kodMukim = kodMukim;
	}

	public String getDaerah() {
		return daerah;
	}

	public void setDaerah(String daerah) {
		this.daerah = daerah;
	}

	public String getKodDaerah() {
		return kodDaerah;
	}

	public void setKodDaerah(String kodDaerah) {
		this.kodDaerah = kodDaerah;
	}

	public String getNegeri() {
		return negeri;
	}

	public void setNegeri(String negeri) {
		this.negeri = negeri;
	}

	public String getKodNegeri() {
		return kodNegeri;
	}

	public void setKodNegeri(String kodNegeri) {
		this.kodNegeri = kodNegeri;
	}

	public String getNoAkaun() {
		return noAkaun;
	}

	public void setNoAkaun(String noAkaun) {
		this.noAkaun = noAkaun;
	}

	public String getStatusPinjaman() {
		return statusPinjaman;
	}

	public void setStatusPinjaman(String statusPinjaman) {
		this.statusPinjaman = statusPinjaman;
	}

	public String getStatusData() {
		return statusData;
	}

	public void setStatusData(String statusData) {
		this.statusData = statusData;
	}
}

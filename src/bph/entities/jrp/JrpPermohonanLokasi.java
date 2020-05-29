package bph.entities.jrp;

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
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.Daerah;

@Entity
@Table(name = "jrp_permohonan_lokasi")
public class JrpPermohonanLokasi {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private JrpPermohonan permohonan;

	@Column(name = "flag_lokasi")
	private String flagLokasi;

	@Column(name = "jenis_bangunan")
	private String jenisBangunan;

	@Column(name = "cawangan")
	private String cawangan;

	@Column(name = "nama_bangunan")
	private String namaBangunan;

	@Column(name = "alamat_1")
	private String alamat1;

	@Column(name = "alamat_2")
	private String alamat2;

	@Column(name = "alamat_3")
	private String alamat3;

	@Column(name = "poskod")
	private String poskod;

	@ManyToOne
	@JoinColumn(name = "id_daerah")
	private Daerah daerah;

	@ManyToOne
	@JoinColumn(name = "id_bandar")
	private Bandar bandar;

	@Column(name = "nama_pemilik_premis")
	private String namaPemilikPremis;

	@Column(name = "sewa_sebulan")
	private Double sewaSebulan;

	@Column(name = "total_sewa")
	private Double totalSewa;

	@Column(name = "kadar_gst")
	private Double kadarGst;

	@Column(name = "sewa_mp")
	private Double sewaMp;

	@Column(name = "sewa_kp")
	private Double sewaKp;

	public Double getTotalSewa() {
		return totalSewa;
	}

	public void setTotalSewa(Double totalSewa) {
		this.totalSewa = totalSewa;
	}

	public Double getKadarGst() {
		return kadarGst;
	}

	public void setKadarGst(Double kadarGst) {
		this.kadarGst = kadarGst;
	}

	@Column(name = "luas_mp")
	private String luasMp;

	@Column(name = "luas_kp")
	private String luasKp;

	@Temporal(TemporalType.DATE)
	@Column(name = "tempoh_sewa_mula")
	private Date tempohSewaMula;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula_mohon")
	private Date tarikhMulaMohon;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat_mohon")
	private Date tarikhTamatMohon;

	@Temporal(TemporalType.DATE)
	@Column(name = "tempoh_sewa_tamat")
	private Date tempohSewaTamat;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public JrpPermohonanLokasi() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public JrpPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(JrpPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getFlagLokasi() {
		return flagLokasi;
	}

	public void setFlagLokasi(String flagLokasi) {
		this.flagLokasi = flagLokasi;
	}

	public String getJenisBangunan() {
		return jenisBangunan;
	}

	public void setJenisBangunan(String jenisBangunan) {
		this.jenisBangunan = jenisBangunan;
	}

	public String getCawangan() {
		return cawangan;
	}

	public void setCawangan(String cawangan) {
		this.cawangan = cawangan;
	}

	public String getNamaBangunan() {
		return namaBangunan;
	}

	public void setNamaBangunan(String namaBangunan) {
		this.namaBangunan = namaBangunan;
	}

	public String getAlamat1() {
		return alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public Daerah getDaerah() {
		return daerah;
	}

	public void setDaerah(Daerah daerah) {
		this.daerah = daerah;
	}

	public Bandar getBandar() {
		return bandar;
	}

	public void setBandar(Bandar bandar) {
		this.bandar = bandar;
	}

	public String getNamaPemilikPremis() {
		return namaPemilikPremis;
	}

	public void setNamaPemilikPremis(String namaPemilikPremis) {
		this.namaPemilikPremis = namaPemilikPremis;
	}

	public Double getSewaSebulan() {
		return sewaSebulan;
	}

	public void setSewaSebulan(Double sewaSebulan) {
		this.sewaSebulan = sewaSebulan;
	}

	public Double getSewaMp() {
		return sewaMp;
	}

	public void setSewaMp(Double sewaMp) {
		this.sewaMp = sewaMp;
	}

	public Double getSewaKp() {
		return sewaKp;
	}

	public void setSewaKp(Double sewaKp) {
		this.sewaKp = sewaKp;
	}

	public String getLuasMp() {
		return luasMp;
	}

	public void setLuasMp(String luasMp) {
		this.luasMp = luasMp;
	}

	public String getLuasKp() {
		return luasKp;
	}

	public void setLuasKp(String luasKp) {
		this.luasKp = luasKp;
	}

	public Date getTarikhMulaMohon() {
		return tarikhMulaMohon;
	}

	public void setTarikhMulaMohon(Date tarikhMulaMohon) {
		this.tarikhMulaMohon = tarikhMulaMohon;
	}

	public Date getTarikhTamatMohon() {
		return tarikhTamatMohon;
	}

	public void setTarikhTamatMohon(Date tarikhTamatMohon) {
		this.tarikhTamatMohon = tarikhTamatMohon;
	}

	public Date getTempohSewaMula() {
		return tempohSewaMula;
	}

	public void setTempohSewaMula(Date tempohSewaMula) {
		this.tempohSewaMula = tempohSewaMula;
	}

	public Date getTempohSewaTamat() {
		return tempohSewaTamat;
	}

	public void setTempohSewaTamat(Date tempohSewaTamat) {
		this.tempohSewaTamat = tempohSewaTamat;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}

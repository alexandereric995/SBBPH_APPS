package bph.entities.utk;

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
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.JenisPelanggaranSyaratUtk;
import bph.entities.qtr.KuaPenghuni;

@Entity
@Table(name = "utk_kesalahan")
public class UtkKesalahan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_operasi")
	private UtkOperasi operasi;

	@ManyToOne
	@JoinColumn(name = "id_penghuni")
	private KuaPenghuni penghuni;

	@Column(name = "no_siri")
	private String noSiri;

	@Column(name = "jenis_kenderaan")
	private String jenisKenderaan;

	@Column(name = "no_plat")
	private String noPlat;

	@Column(name = "catatan")
	private String catatan;

	@Column(name = "status")
	private String status;

	@Column(name = "status_penghuni")
	private String statusPenghuni;

	@ManyToOne
	@JoinColumn(name = "id_jenis_operasi")
	private JenisOperasiUtk jenisOperasi;

	@Column(name = "id_jenis_operasi", insertable = false, updatable = false)
	private String idJenisOperasi;

	@ManyToOne
	@JoinColumn(name = "id_jenis_pelanggaran_syarat")
	private JenisPelanggaranSyaratUtk jenisPelanggaranSyarat;

	@Column(name = "id_jenis_pelanggaran_syarat", insertable = false, updatable = false)
	private String idJenisPelanggaranSyarat;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh")
	private Date tarikh;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public UtkKesalahan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
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

	public KuaPenghuni getPenghuni() {
		return penghuni;
	}

	public void setPenghuni(KuaPenghuni penghuni) {
		this.penghuni = penghuni;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getNoSiri() {
		return noSiri;
	}

	public void setNoSiri(String noSiri) {
		this.noSiri = noSiri;
	}

	public String getJenisKenderaan() {
		return jenisKenderaan;
	}

	public void setJenisKenderaan(String jenisKenderaan) {
		this.jenisKenderaan = jenisKenderaan;
	}

	public String getNoPlat() {
		return noPlat;
	}

	public void setNoPlat(String noPlat) {
		this.noPlat = noPlat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusPenghuni() {
		return statusPenghuni;
	}

	public void setStatusPenghuni(String statusPenghuni) {
		this.statusPenghuni = statusPenghuni;
	}

	public JenisOperasiUtk getJenisOperasi() {
		return jenisOperasi;
	}

	public void setJenisOperasi(JenisOperasiUtk jenisOperasi) {
		this.jenisOperasi = jenisOperasi;
	}

	public JenisPelanggaranSyaratUtk getJenisPelanggaranSyarat() {
		return jenisPelanggaranSyarat;
	}

	public void setJenisPelanggaranSyarat(
			JenisPelanggaranSyaratUtk jenisPelanggaranSyarat) {
		this.jenisPelanggaranSyarat = jenisPelanggaranSyarat;
	}

	public String getIdJenisOperasi() {
		return idJenisOperasi;
	}

	public void setIdJenisOperasi(String idJenisOperasi) {
		this.idJenisOperasi = idJenisOperasi;
	}

	public String getIdJenisPelanggaranSyarat() {
		return idJenisPelanggaranSyarat;
	}

	public void setIdJenisPelanggaranSyarat(String idJenisPelanggaranSyarat) {
		this.idJenisPelanggaranSyarat = idJenisPelanggaranSyarat;
	}

	public Date getTarikh() {
		return tarikh;
	}

	public void setTarikh(Date tarikh) {
		this.tarikh = tarikh;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}

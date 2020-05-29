package bph.entities.senggara;

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

@Entity
@Table(name = "mtn_penilaian_kontraktor")
public class MtnPenilaianKontraktor {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "kekemasan_kerja")
	private Integer kekemasanKerja;

	@Column(name = "pelaksanaan_kerja")
	private Integer pelaksanaanKerja;

	@Column(name = "pematuhan_skop")
	private Integer pematuhanSkop;

	@Column(name = "kebersihan")
	private Integer kebersihan;

	@Column(name = "inisiatif")
	private Integer inisiatif;

	@Column(name = "bilangan_pekerja")
	private Integer bilanganPekerja;

	@Column(name = "peralatan_kerja")
	private Integer peralatanKerja;

	@Column(name = "tempoh_kerja")
	private Integer tempohKerja;

	@Column(name = "pengurusan")
	private Integer pengurusan;

	@Column(name = "mata_keseluruhan")
	private Integer mataKeseluruhan;

	@Column(name = "gred_penilaian")
	private String gredPenilaian;

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

	public MtnPenilaianKontraktor() {
		setId(UID.getUID());
		setKekemasanKerja(0);
		setPelaksanaanKerja(0);
		setPematuhanSkop(0);
		setKebersihan(0);
		setInisiatif(0);
		setBilanganPekerja(0);
		setPeralatanKerja(0);
		setTempohKerja(0);
		setPengurusan(0);
		setTarikhMasuk(new Date());
	}

	public Integer getTotalPenilaianFinishing() {
		Integer total = 0;
		total = (this.kekemasanKerja + this.pelaksanaanKerja
				+ this.pematuhanSkop + this.kebersihan);
		return total;
	}

	public Integer getTotalPenilaianDisiplin() {
		Integer total = 0;
		total = (this.inisiatif + this.bilanganPekerja + this.peralatanKerja);
		return total;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getKekemasanKerja() {
		return kekemasanKerja;
	}

	public void setKekemasanKerja(Integer kekemasanKerja) {
		this.kekemasanKerja = kekemasanKerja;
	}

	public Integer getPelaksanaanKerja() {
		return pelaksanaanKerja;
	}

	public void setPelaksanaanKerja(Integer pelaksanaanKerja) {
		this.pelaksanaanKerja = pelaksanaanKerja;
	}

	public Integer getPematuhanSkop() {
		return pematuhanSkop;
	}

	public void setPematuhanSkop(Integer pematuhanSkop) {
		this.pematuhanSkop = pematuhanSkop;
	}

	public Integer getKebersihan() {
		return kebersihan;
	}

	public void setKebersihan(Integer kebersihan) {
		this.kebersihan = kebersihan;
	}

	public Integer getInisiatif() {
		return inisiatif;
	}

	public void setInisiatif(Integer inisiatif) {
		this.inisiatif = inisiatif;
	}

	public Integer getBilanganPekerja() {
		return bilanganPekerja;
	}

	public void setBilanganPekerja(Integer bilanganPekerja) {
		this.bilanganPekerja = bilanganPekerja;
	}

	public Integer getPeralatanKerja() {
		return peralatanKerja;
	}

	public void setPeralatanKerja(Integer peralatanKerja) {
		this.peralatanKerja = peralatanKerja;
	}

	public Integer getTempohKerja() {
		return tempohKerja;
	}

	public void setTempohKerja(Integer tempohKerja) {
		this.tempohKerja = tempohKerja;
	}

	public Integer getPengurusan() {
		return pengurusan;
	}

	public void setPengurusan(Integer pengurusan) {
		this.pengurusan = pengurusan;
	}

	public Integer getMataKeseluruhan() {
		return mataKeseluruhan;
	}

	public void setMataKeseluruhan(Integer mataKeseluruhan) {
		this.mataKeseluruhan = mataKeseluruhan;
	}

	public String getGredPenilaian() {
		return gredPenilaian;
	}

	public void setGredPenilaian(String gredPenilaian) {
		this.gredPenilaian = gredPenilaian;
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

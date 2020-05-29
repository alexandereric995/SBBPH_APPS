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
import bph.entities.kod.Status;
import bph.entities.qtr.KuaKuarters;

@Entity
@Table(name = "mtn_kuarters")
public class MtnKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private KuaKuarters kuarters;

	@Column(name = "tarikh_terima_laporan")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhTerimaLaporan;

	@Column(name = "jenis_laporan")
	private String jenisLaporan;

	@Column(name = "keterangan_laporan")
	private String keteranganLaporan;

	@ManyToOne
	@JoinColumn(name = "id_penerima_kunci")
	private Users penerimaKunci;

	@Column(name = "tarikh_terima_kunci")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhTerimaKunci;

	@Column(name = "catatan_terimaan_kunci")
	private String catatanTerimaanKunci;

	@ManyToOne
	@JoinColumn(name = "id_tugasan")
	private MtnAgihanTugas tugasan;

	@Column(name = "tarikh_serah_kunci")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhSerahKunci;

	@Column(name = "catatan_serahan_kunci")
	private String catatanSerahanKunci;

	@ManyToOne
	@JoinColumn(name = "id_penyerah_kunci")
	private Users penyerahKunci;

	@ManyToOne
	@JoinColumn(name = "status")
	private Status status;

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

	public MtnKuarters() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaKuarters getKuarters() {
		return kuarters;
	}

	public void setKuarters(KuaKuarters kuarters) {
		this.kuarters = kuarters;
	}

	public Date getTarikhTerimaLaporan() {
		return tarikhTerimaLaporan;
	}

	public void setTarikhTerimaLaporan(Date tarikhTerimaLaporan) {
		this.tarikhTerimaLaporan = tarikhTerimaLaporan;
	}

	public String getJenisLaporan() {
		return jenisLaporan;
	}

	public void setJenisLaporan(String jenisLaporan) {
		this.jenisLaporan = jenisLaporan;
	}

	public String getKeteranganLaporan() {
		return keteranganLaporan;
	}

	public void setKeteranganLaporan(String keteranganLaporan) {
		this.keteranganLaporan = keteranganLaporan;
	}

	public Users getPenerimaKunci() {
		return penerimaKunci;
	}

	public void setPenerimaKunci(Users penerimaKunci) {
		this.penerimaKunci = penerimaKunci;
	}

	public Date getTarikhTerimaKunci() {
		return tarikhTerimaKunci;
	}

	public void setTarikhTerimaKunci(Date tarikhTerimaKunci) {
		this.tarikhTerimaKunci = tarikhTerimaKunci;
	}

	public String getCatatanTerimaanKunci() {
		return catatanTerimaanKunci;
	}

	public void setCatatanTerimaanKunci(String catatanTerimaanKunci) {
		this.catatanTerimaanKunci = catatanTerimaanKunci;
	}

	public MtnAgihanTugas getTugasan() {
		return tugasan;
	}

	public void setTugasan(MtnAgihanTugas tugasan) {
		this.tugasan = tugasan;
	}

	public Date getTarikhSerahKunci() {
		return tarikhSerahKunci;
	}

	public void setTarikhSerahKunci(Date tarikhSerahKunci) {
		this.tarikhSerahKunci = tarikhSerahKunci;
	}

	public String getCatatanSerahanKunci() {
		return catatanSerahanKunci;
	}

	public void setCatatanSerahanKunci(String catatanSerahanKunci) {
		this.catatanSerahanKunci = catatanSerahanKunci;
	}

	public Users getPenyerahKunci() {
		return penyerahKunci;
	}

	public void setPenyerahKunci(Users penyerahKunci) {
		this.penyerahKunci = penyerahKunci;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

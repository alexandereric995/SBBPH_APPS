package bph.entities.qtr;

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
@Table(name = "kua_penghuni")
public class KuaPenghuni {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private KuaPermohonan permohonan;

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private Users pemohon;

	@ManyToOne
	@JoinColumn(name = "id_kuarters")
	private KuaKuarters kuarters;

	@Column(name = "tarikh_masuk_kuarters")
	@Temporal(TemporalType.DATE)
	private Date tarikhMasukKuarters;

	@Column(name = "tarikh_keluar_kuarters")
	@Temporal(TemporalType.DATE)
	private Date tarikhKeluarKuarters;

	@Column(name = "tarikh_mula_potong_gaji")
	@Temporal(TemporalType.DATE)
	private Date tarikhMulaPotongGaji;

	@Column(name = "id_waris")
	private String idWaris;

	@Column(name = "nama_waris")
	private String namaWaris;

	@Column(name = "no_telefon_waris")
	private String noTelefonWaris;

	@Column(name = "no_fail_lama")
	private String noFailLama;

	@Column(name = "no_rujukan_kuarters_syspintar")
	private String noRujukanKuartersSyspintar;
	
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

	public KuaPenghuni() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getStatusPenghuni() {
		String statusPenghuni = "";

		if (getTarikhMasukKuarters() != null) {
			if (getTarikhKeluarKuarters() != null) {
				statusPenghuni = "PENGHUNI KELUAR";
			} else {
				statusPenghuni = "PENGHUNI";
			}
		} else {
			if (getTarikhKeluarKuarters() != null) {
				statusPenghuni = "PENGHUNI KELUAR";
			} else {
				statusPenghuni = "TIADA REKOD";
			}
		}
		return statusPenghuni;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KuaPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(KuaPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public Users getPemohon() {
		return pemohon;
	}

	public void setPemohon(Users pemohon) {
		this.pemohon = pemohon;
	}

	public KuaKuarters getKuarters() {
		return kuarters;
	}

	public void setKuarters(KuaKuarters kuarters) {
		this.kuarters = kuarters;
	}

	public Date getTarikhMasukKuarters() {
		return tarikhMasukKuarters;
	}

	public void setTarikhMasukKuarters(Date tarikhMasukKuarters) {
		this.tarikhMasukKuarters = tarikhMasukKuarters;
	}

	public Date getTarikhKeluarKuarters() {
		return tarikhKeluarKuarters;
	}

	public void setTarikhKeluarKuarters(Date tarikhKeluarKuarters) {
		this.tarikhKeluarKuarters = tarikhKeluarKuarters;
	}

	public Date getTarikhMulaPotongGaji() {
		return tarikhMulaPotongGaji;
	}

	public void setTarikhMulaPotongGaji(Date tarikhMulaPotongGaji) {
		this.tarikhMulaPotongGaji = tarikhMulaPotongGaji;
	}

	public String getIdWaris() {
		return idWaris;
	}

	public void setIdWaris(String idWaris) {
		this.idWaris = idWaris;
	}

	public String getNamaWaris() {
		return namaWaris;
	}

	public void setNamaWaris(String namaWaris) {
		this.namaWaris = namaWaris;
	}

	public String getNoTelefonWaris() {
		return noTelefonWaris;
	}

	public void setNoTelefonWaris(String noTelefonWaris) {
		this.noTelefonWaris = noTelefonWaris;
	}

	public String getNoFailLama() {
		return noFailLama;
	}

	public void setNoFailLama(String noFailLama) {
		this.noFailLama = noFailLama;
	}

	public String getNoRujukanKuartersSyspintar() {
		return noRujukanKuartersSyspintar;
	}

	public void setNoRujukanKuartersSyspintar(String noRujukanKuartersSyspintar) {
		this.noRujukanKuartersSyspintar = noRujukanKuartersSyspintar;
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

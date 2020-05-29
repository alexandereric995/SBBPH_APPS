package bph.entities.rk;

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
@Table(name = "rk_mesyuarat_permohonan")
public class RkMesyuaratPermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_mesyuarat")
	private RkMesyuarat mesyuarat;

	@ManyToOne
	@JoinColumn(name = "id_permohonan")
	private RkPermohonan permohonan;

	@Column(name = "flag_jenis_permohonan")
	private String flagJenisPermohonan;

	@Column(name = "flag_keputusan")
	private String flagKeputusan;

	@Column(name = "catatan")
	private String catatan;

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

	public RkMesyuaratPermohonan() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
		setFlagJenisPermohonan("B");
		setTarikhMasuk(new Date());
	}

	public String getKeteranganKeputusan() {
		String keputusan = "";

		if (this.getFlagKeputusan() != null) {
			if ("L".equals(this.getFlagKeputusan())) {
				keputusan = "LULUS";
			} else if ("LB".equals(this.getFlagKeputusan())) {
				keputusan = "LULUS BERSYARAT";
			} else if ("TG".equals(this.getFlagKeputusan())) {
				keputusan = "TANGGUH";
			} else if ("T".equals(this.getFlagKeputusan())) {
				keputusan = "TOLAK";
			}
		}

		return keputusan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RkMesyuarat getMesyuarat() {
		return mesyuarat;
	}

	public void setMesyuarat(RkMesyuarat mesyuarat) {
		this.mesyuarat = mesyuarat;
	}

	public RkPermohonan getPermohonan() {
		return permohonan;
	}

	public void setPermohonan(RkPermohonan permohonan) {
		this.permohonan = permohonan;
	}

	public String getFlagJenisPermohonan() {
		return flagJenisPermohonan;
	}

	public void setFlagJenisPermohonan(String flagJenisPermohonan) {
		this.flagJenisPermohonan = flagJenisPermohonan;
	}

	public String getFlagKeputusan() {
		return flagKeputusan;
	}

	public void setFlagKeputusan(String flagKeputusan) {
		this.flagKeputusan = flagKeputusan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
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

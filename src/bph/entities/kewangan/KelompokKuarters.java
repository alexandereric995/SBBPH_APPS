package bph.entities.kewangan;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.DbPersistence;
import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "kelompok_kuarters")
public class KelompokKuarters {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nama_agensi")
	private String namaAgensi;

	@Column(name = "keterangan")
	private String keterangan;

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

	public KelompokKuarters() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamaAgensi() {
		return namaAgensi;
	}

	public void setNamaAgensi(String namaAgensi) {
		this.namaAgensi = namaAgensi;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	@SuppressWarnings("unchecked")
	public List<KelompokKuartersPenghuni> getSenaraiKuartersPenghuni() {
		DbPersistence db = new DbPersistence();
		List<KelompokKuartersPenghuni> list = null;
		if (this.id != null) {
			list = db
					.list("select x from KelompokKuartersPenghuni x where x.kelompokKuarters.id = '"
							+ this.id + "' ");
		}
		return list;
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

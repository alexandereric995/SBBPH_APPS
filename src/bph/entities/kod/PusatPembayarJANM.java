package bph.entities.kod;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import portal.module.entity.Users;

@Entity
@Table(name = "ruj_pusat_pembayar_janm")
public class PusatPembayarJANM {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_cawangan")
	private CawanganJANM cawangan;

	@ManyToOne
	@JoinColumn(name = "id_jabatan")
	private JabatanPembayarJANM jabatan;

	@Column(name = "id_pusat_pembayar")
	private String idPusatPembayar;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CawanganJANM getCawangan() {
		return cawangan;
	}

	public void setCawangan(CawanganJANM cawangan) {
		this.cawangan = cawangan;
	}

	public JabatanPembayarJANM getJabatan() {
		return jabatan;
	}

	public void setJabatan(JabatanPembayarJANM jabatan) {
		this.jabatan = jabatan;
	}

	public String getIdPusatPembayar() {
		return idPusatPembayar;
	}

	public void setIdPusatPembayar(String idPusatPembayar) {
		this.idPusatPembayar = idPusatPembayar;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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

package bph.entities.kod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
}

package bph.entities.rpp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name="rpp_tetapan_buka_tempahan")
public class RppTetapanBukaTempahan {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "perkara")
	private String perkara;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_buka_tempahan")
	private Date tarikhBukaTempahan;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_menginap_dari")
	private Date tarikhMenginapDari;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_menginap_hingga")
	private Date tarikhMenginapHingga;
	
	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;
	
	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@Column(name = "flag_aktif")
	private String flagAktif;
	
	public RppTetapanBukaTempahan() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPerkara() {
		return perkara;
	}

	public void setPerkara(String perkara) {
		this.perkara = perkara;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public Date getTarikhBukaTempahan() {
		return tarikhBukaTempahan;
	}

	public void setTarikhBukaTempahan(Date tarikhBukaTempahan) {
		this.tarikhBukaTempahan = tarikhBukaTempahan;
	}

	public Date getTarikhMenginapDari() {
		return tarikhMenginapDari;
	}

	public void setTarikhMenginapDari(Date tarikhMenginapDari) {
		this.tarikhMenginapDari = tarikhMenginapDari;
	}

	public Date getTarikhMenginapHingga() {
		return tarikhMenginapHingga;
	}

	public void setTarikhMenginapHingga(Date tarikhMenginapHingga) {
		this.tarikhMenginapHingga = tarikhMenginapHingga;
	}

	public String getFlagAktif() {
		return flagAktif;
	}

	public void setFlagAktif(String flagAktif) {
		this.flagAktif = flagAktif;
	}
	
	public String keteranganFlagAktif(){
		String status = "";
		if(this.flagAktif!=null && this.flagAktif.equalsIgnoreCase("Y")){
			status = "YA";
		}else{
			status = "TIDAK";
		}
		return status;
	}
	
	public String statusFlagAktif(){
		String status = "";
		if(this.flagAktif!=null && this.flagAktif.equalsIgnoreCase("Y")){
			status = "TELAH DIAKTIFKAN";
		}else{
			status = "BELUM DIAKTIFKAN";
		}
		return status;
	}

}

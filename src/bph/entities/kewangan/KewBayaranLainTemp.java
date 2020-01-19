package bph.entities.kewangan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "kew_bayaran_lain_temp")
public class KewBayaranLainTemp {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_resit")
	private String noResit;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TARIKH_RESIT")
	private Date tarikhResit;
	
	@Column(name = "nama")
	private String nama;
	
	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "no_kp")
	private String noKP;
	
	@Column(name = "street")
	private String street;	
	
	@Column(name = "street2")
	private String street2;
	
	@Column(name = "postcode")
	private String postcode;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "juruwang")
	private String juruwang;
	
	@Column(name = "cashier_name")
	private String cashierName;
	
	@Column(name = "mode")
	private String mode;
	
	@Column(name = "mod_bayaran")
	private String modBayaran;
	
	@Column(name = "no_dokumen")
	private String noDokumen;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public Date getTarikhResit() {
		return tarikhResit;
	}

	public void setTarikhResit(Date tarikhResit) {
		this.tarikhResit = tarikhResit;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getNoKP() {
		return noKP;
	}

	public void setNoKP(String noKP) {
		this.noKP = noKP;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getJuruwang() {
		return juruwang;
	}

	public void setJuruwang(String juruwang) {
		this.juruwang = juruwang;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getModBayaran() {
		return modBayaran;
	}

	public void setModBayaran(String modBayaran) {
		this.modBayaran = modBayaran;
	}

	public String getNoDokumen() {
		return noDokumen;
	}

	public void setNoDokumen(String noDokumen) {
		this.noDokumen = noDokumen;
	}
}

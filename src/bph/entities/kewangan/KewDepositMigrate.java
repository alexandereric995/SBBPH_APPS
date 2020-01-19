package bph.entities.kewangan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kew_deposit_migrate")
public class KewDepositMigrate {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "nama")
	private String nama;
	
	@Column(name = "lokasi")
	private String lokasi;
	
	@Column(name = "no_kp")
	private String noKP;
	
	@Column(name = "no_resit")
	private String noResit;
	
	@Column(name = "tarikh_resit")
	private String tarikhResit;	
	
	@Column(name = "no_pp")
	private String noPP;
	
	@Column(name = "tarikh_pp")
	private String tarikhPP;
	
	@Column(name = "amaun")
	private String amaun;
	
	@Column(name = "no_baucer_bayaran")
	private String noBaucerBayaran;
	
	@Column(name = "tarikh_bayaran")
	private String tarikhBayaran;
	
	@Column(name = "amaun_bayaran")
	private String amaunBayaran;
	
	@Column(name = "flag_migrate")
	private String flagMigrate;
	
	@Column(name = "msg")
	private String msg;
	
	public KewDepositMigrate() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public String getNoKP() {
		return noKP;
	}

	public void setNoKP(String noKP) {
		this.noKP = noKP;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public String getTarikhResit() {
		return tarikhResit;
	}

	public void setTarikhResit(String tarikhResit) {
		this.tarikhResit = tarikhResit;
	}

	public String getNoPP() {
		return noPP;
	}

	public void setNoPP(String noPP) {
		this.noPP = noPP;
	}

	public String getTarikhPP() {
		return tarikhPP;
	}

	public void setTarikhPP(String tarikhPP) {
		this.tarikhPP = tarikhPP;
	}

	public String getAmaun() {
		return amaun;
	}

	public void setAmaun(String amaun) {
		this.amaun = amaun;
	}

	public String getNoBaucerBayaran() {
		return noBaucerBayaran;
	}

	public void setNoBaucerBayaran(String noBaucerBayaran) {
		this.noBaucerBayaran = noBaucerBayaran;
	}

	public String getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(String tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public String getAmaunBayaran() {
		return amaunBayaran;
	}

	public void setAmaunBayaran(String amaunBayaran) {
		this.amaunBayaran = amaunBayaran;
	}

	public String getFlagMigrate() {
		return flagMigrate;
	}

	public void setFlagMigrate(String flagMigrate) {
		this.flagMigrate = flagMigrate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

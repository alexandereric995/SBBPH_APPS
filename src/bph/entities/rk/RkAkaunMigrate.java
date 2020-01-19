package bph.entities.rk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rk_akaun_migrate")
public class RkAkaunMigrate {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "no_resit")
	private String noResit;
	
	@Column(name = "tarikh_bayaran")
	private String tarikhBayaran;
	
	@Column(name = "amaun")
	private String amaun;
	
	@Column(name = "id_kod_hasil")
	private String idKodHasil;
	
	@Column(name = "flag_migrate")
	private String flagMigrate;
	
	@Column(name = "msg")
	private String msg;
	
	public RkAkaunMigrate() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getNoResit() {
		return noResit;
	}

	public void setNoResit(String noResit) {
		this.noResit = noResit;
	}

	public String getTarikhBayaran() {
		return tarikhBayaran;
	}

	public void setTarikhBayaran(String tarikhBayaran) {
		this.tarikhBayaran = tarikhBayaran;
	}

	public String getAmaun() {
		return amaun;
	}

	public void setAmaun(String amaun) {
		this.amaun = amaun;
	}

	public String getIdKodHasil() {
		return idKodHasil;
	}

	public void setIdKodHasil(String idKodHasil) {
		this.idKodHasil = idKodHasil;
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

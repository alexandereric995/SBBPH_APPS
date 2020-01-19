package testing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kua_penghuni_migrate")
public class KuaPenghuniMigrate {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "no_rujukan_kuarters")
	private String noRujukanKuarters;
	
	@Column(name = "id_kuarters")
	private String idKuarters;
	
	@Column(name = "no_pengenalan")
	private String noPengenalan;
	
	@Column(name = "tarikh_masuk")
	private String tarikhMasuk;
	
	@Column(name = "tarikh_keluar")
	private String tarikhKeluar;
	
	@Column(name = "flag_migrate")
	private String flagMigrate;
	
	@Column(name = "msg")
	private String msg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoRujukanKuarters() {
		return noRujukanKuarters;
	}

	public void setNoRujukanKuarters(String noRujukanKuarters) {
		this.noRujukanKuarters = noRujukanKuarters;
	}

	public String getIdKuarters() {
		return idKuarters;
	}

	public void setIdKuarters(String idKuarters) {
		this.idKuarters = idKuarters;
	}

	public String getNoPengenalan() {
		return noPengenalan;
	}

	public void setNoPengenalan(String noPengenalan) {
		this.noPengenalan = noPengenalan;
	}

	public String getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(String tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public String getTarikhKeluar() {
		return tarikhKeluar;
	}

	public void setTarikhKeluar(String tarikhKeluar) {
		this.tarikhKeluar = tarikhKeluar;
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

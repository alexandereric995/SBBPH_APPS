package bph.entities.rpp;

import java.util.Date;
import java.util.List;

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
import db.persistence.MyPersistence;

@Entity
@Table(name="rpp_selenggara")
public class RppSelenggara {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "perkara")
	private String perkara;  
	  
	@Column(name = "catatan")
	private String catatan;  
	  
	@Column(name = "flag_jenis_selenggara")
	private String flagJenisSelenggara; // 'UNIT / LOKASI'
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_mula")
	private Date tarikhMula;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_tamat")
	private Date tarikhTamat;
	
	@OneToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;
	
	@OneToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@Column(name = "id_peranginan")
	private String idPeranginan;  
	
	public RppSelenggara() {
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

	public String getFlagJenisSelenggara() {
		return flagJenisSelenggara;
	}

	public void setFlagJenisSelenggara(String flagJenisSelenggara) {
		this.flagJenisSelenggara = flagJenisSelenggara;
	}

	public Date getTarikhMula() {
		return tarikhMula;
	}

	public void setTarikhMula(Date tarikhMula) {
		this.tarikhMula = tarikhMula;
	}

	public Date getTarikhTamat() {
		return tarikhTamat;
	}

	public void setTarikhTamat(Date tarikhTamat) {
		this.tarikhTamat = tarikhTamat;
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
	
	@SuppressWarnings("unchecked")
	public List<RppSelenggaraUnitLokasi> getListSelenggaraLokasi() {
		MyPersistence mp = null;
		List<RppSelenggaraUnitLokasi> list = null;
		try {
			mp = new MyPersistence();
			list = mp.list("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+this.id+"' ");
		} catch (Exception e) {
			System.out.println("Error getListSelenggaraLokasi : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return list;
	}

	public String getIdPeranginan() {
		String str = idPeranginan;
		if(this.getListSelenggaraLokasi() != null){
			RppSelenggaraUnitLokasi obj = this.getListSelenggaraLokasi().get(0);
			if(obj != null){
				str = obj.getRppPeranginan().getId();
			}
		}
		return str;
	}

	public void setIdPeranginan(String idPeranginan) {
		this.idPeranginan = idPeranginan;
	}
	
	public String keteranganJenisSelenggara(){
		String status = "";
		if(this.flagJenisSelenggara !=null && this.flagJenisSelenggara.equalsIgnoreCase("LOKASI")){
			status = "KESELURUHAN RPP";
		}else{
			status = "SEBILANGAN UNIT";
		}
		return status;
	}

}

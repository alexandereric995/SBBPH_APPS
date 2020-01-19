package bph.entities.rpp;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.db.Db;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevRuang;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

@Entity
@Table(name="rpp_unit")
public class RppUnit {

	@Id
	@Column(name = "id")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "id_bangunan")
	private DevBangunan bangunan;
	
	@OneToOne
	@JoinColumn(name = "id_ruang")
	private DevRuang ruang;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_unit")
	private JenisUnitRPP jenisUnit;
	
	@Column(name = "nama_unit")
	private String namaUnit;
	
	@Column(name = "no_unit")
	private String noUnit;
	
	@Column(name = "catatan")
	private String catatan;
	
	@Column(name = "status")
	private String status;
	
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
	
	public RppUnit() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevBangunan getBangunan() {
		return bangunan;
	}

	public void setBangunan(DevBangunan bangunan) {
		this.bangunan = bangunan;
	}

	public DevRuang getRuang() {
		return ruang;
	}

	public void setRuang(DevRuang ruang) {
		this.ruang = ruang;
	}

	public JenisUnitRPP getJenisUnit() {
		return jenisUnit;
	}

	public void setJenisUnit(JenisUnitRPP jenisUnit) {
		this.jenisUnit = jenisUnit;
	}

	public String getNamaUnit() {
		return namaUnit;
	}

	public void setNamaUnit(String namaUnit) {
		this.namaUnit = namaUnit;
	}

	public String getNoUnit() {
		return noUnit;
	}

	public void setNoUnit(String noUnit) {
		this.noUnit = noUnit;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	/** 
	 * Untuk Blocking / Selenggara
	 */
	public String getCheckedFlag(String idSelenggara) throws Exception{
		MyPersistence mp = null;
		String str = "";
		try {
			mp = new MyPersistence();
			
			RppSelenggaraUnitLokasi obj =  null;
			if(idSelenggara!=null && this.id!=null){
				obj = (RppSelenggaraUnitLokasi) mp.get("select x from RppSelenggaraUnitLokasi x "+
						" where x.rppSelenggara.id = '"+idSelenggara+"' and x.rppUnit.id = '"+this.id+"' ");
			}
			
			if(obj!=null){
				str = "Y";
			}
			
		} catch (Exception e) {
			System.out.println("Error getCheckedFlag : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return str;
	}
	
	/** 
	 * Get status availabity for Blocking / Selenggara
	 */
	@SuppressWarnings("unchecked")
	public List<RppJadualTempahan> getStatusUnit(String idSelenggara){
		MyPersistence mp = null;
		List<RppJadualTempahan> list = null;
		try {
			mp = new MyPersistence();
			RppSelenggara obj =  null;
			if(idSelenggara!=null && this.id!=null){
				obj = (RppSelenggara) mp.get("select x from RppSelenggara x where x.id = '"+idSelenggara+"' ");
			}
			
			if(this.id != null && obj != null){
				String tarikhMula = new SimpleDateFormat("yyyy-MM-dd").format(obj.getTarikhMula());
				String tarikhTamat = new SimpleDateFormat("yyyy-MM-dd").format(obj.getTarikhTamat());
				list = mp.list("select x from RppJadualTempahan x where x.unit.id = '"+this.id+"' "+
							   " and ((x.tarikhMula <= '"+tarikhMula+"' AND x.tarikhTamat > '"+tarikhMula+"') "
							   + "	OR (x.tarikhMula < '"+tarikhTamat+"' AND x.tarikhTamat >= '"+tarikhTamat+"') "
							   + " 	OR (x.tarikhMula >= '"+tarikhMula+"' AND x.tarikhTamat < '"+tarikhTamat+"')) ");
			}
			
		} catch (Exception e) {
			System.out.println("Error getStatusUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return list;
	}
	
	/** 
	 * Checking bilik diselenggara
	 */
	public String getCheckedSelenggara(String strDateIn, String strDateOut) throws Exception{
		String selenggara = "T";
		Db db1 = null;
		String sql = "";
		
		try{
			db1 = new Db();
			sql = "select a1.id_unit from rpp_selenggara_unit_lokasi a1, rpp_selenggara b " 
					+" where a1.id_selenggara = b.id "
					+" and b.flag_jenis_selenggara = 'UNIT' and a1.id_unit = "+this.id+" "
					+" and ((b.tarikh_mula <= '"+strDateIn+"' AND b.tarikh_tamat > '"+strDateIn+"')  "
					+" OR (b.tarikh_mula < '"+strDateOut+"' AND b.tarikh_tamat >= '"+strDateOut+"')   "
					+" OR (b.tarikh_mula >= '"+strDateIn+"' AND b.tarikh_tamat < '"+strDateOut+"')) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			
			if (rs.next()){
				selenggara = "Y";
			}
			
		}catch(Exception e){
			System.out.println("error getCheckedSelenggara : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}	
		
		return selenggara;
	}
	
	public List<Object> listKekosonganByUnit(Date dtIn, Date dtOut) throws Exception{
		
		//List<RppUnit> list = null;
		List<Object> list = new ArrayList<Object>();
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dtIn);
		
		while(calendar.getTime().before(dtOut) /*|| calendar.getTime().equals(dtOut)*/){
			Date result = calendar.getTime();
			calendar.add(Calendar.DATE, 1);
			String strdate = Util.getDateTime(result, "yyyy-MM-dd");
			String displaydate = Util.getDateTime(result, "dd/MM/yyyy");
			
			String available = "T";
			if(UtilRpp.checkingAvailableAndSelenggaraDaily(this, strdate) == true){
				available = "Y";
			}
			
			Map<String, Object> hmap = new HashMap<String, Object>();
			hmap.put("tarikhLoop", displaydate);
			hmap.put("flagAvailable", available);
			
			list.add(hmap);
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

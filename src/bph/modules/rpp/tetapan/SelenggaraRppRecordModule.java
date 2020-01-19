package bph.modules.rpp.tetapan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorMultipleValue;
import portal.module.entity.Users;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppSelenggara;
import bph.entities.rpp.RppSelenggaraUnitLokasi;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SelenggaraRppRecordModule extends LebahRecordTemplateModule<RppSelenggara> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public Class<RppSelenggara> getPersistenceClass() {return RppSelenggara.class;}

	@Override
	public String getPath() { return "bph/modules/rpp/tetapan/selenggara"; }

	@SuppressWarnings("unchecked")
	@Override
	public void beforeSave() {
		
		try {
			mp = new MyPersistence();
			
			//checking if change kategori selenggara, delete data yg simpan kategori lain.
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, getParam("idSelenggara"));
			if( r!=null ){
				String previousflag = r.getFlagJenisSelenggara();
				String newflag = getParam("flagJenisSelenggara");
				if(!previousflag.equalsIgnoreCase(newflag)){
					List<RppSelenggaraUnitLokasi> sl = mp.list("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+r.getId()+"' ");
					
					mp.begin();
					for(int i=0;i<sl.size();i++){
						mp.remove(sl.get(i));
					}
					mp.commit();
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error beforeSave : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public void afterSave(RppSelenggara r) {
		context.put("r", r);
		context.put("selectedTab", "1");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(RppSelenggara r) throws Exception {
		
		try {
			mp = new MyPersistence();
			
			List<RppSelenggaraUnitLokasi> sl = mp.list("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+r.getId()+"' ");
			
			mp.begin();
			for(int i=0;i<sl.size();i++){
				mp.remove(sl.get(i));
			}
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("Error delete : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		
		
		String idrp = getParam("find_idrp");
		String idjenisUnit = getParam("find_idjenisUnit");
		String idUnit = getParam("find_idUnit");
		String perkara = getParam("find_perkara");
		String flagjenis = getParam("find_flagJenisSelenggara");
		
		String multipleItem = "";
		String valueIn = "('')";
		List<String> results = null;
		Map<String, Object> q = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			mp = new MyPersistence();
			
			if(idrp.length() > 0  || idjenisUnit.length() > 0){
				
				q.put("idrp", idrp);
				q.put("idjenisUnit", idjenisUnit);
				q.put("idUnit", idUnit);
				
				results = UtilRpp.resultSearchSelenggara(mp,q);
				
				if(results != null){
					for(String i : results){
						if (multipleItem.length() == 0) {
							multipleItem = "'" + i + "'";
						} else {
							multipleItem = multipleItem + "," + "'" + i + "'";
						}
					}
				}
				
				if(!multipleItem.equalsIgnoreCase("")){
					valueIn = "("+multipleItem+")";
				}
				map.put("id", new OperatorMultipleValue(valueIn));
			}
			
			map.put("perkara", perkara);
			map.put("flagJenisSelenggara", flagjenis);
			
		} catch (Exception e) {
			System.out.println("Error searchCriteria : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return map;
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		this.addFilter("tarikhTamat >= '"
				+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		this.setOrderBy("tarikhTamat");
		this.setOrderType("asc");		
		
		if(userRole.equalsIgnoreCase("(RPP) Blocking Selenggara")){
			this.setReadonly(false);
		} else {
			this.setReadonly(true);			
		}
		
		defaultButtonOption();
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("listPeranginan",dataUtil.getListPeranginanRpp());
		context.put("listJenisUnit",null);
		context.put("listUnit",null);
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	@Command("filterJenisUnit")
	public String filterJenisUnit() throws Exception {
		String find_idrp = getParam("find_idrp");
		context.put("listJenisUnit", dataUtil.getListJenisUnitByRpp(find_idrp));
		return getPath() + "/senaraiJenisUnit.vm";
	}
	
	@Command("filterUnit")
	public String filterUnit() throws Exception {
		String find_idjenisUnit = getParam("find_idjenisUnit");
		context.put("listUnit", dataUtil.getListUnitByJenisUnit(find_idjenisUnit));
		return getPath() + "/senaraiUnit.vm";
	}

	private void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	@Override
	public void getRelatedData(RppSelenggara r) {
		context.put("selectedTab", "1");
	}
	
	@Command("maklumatSelenggara")
	public String maklumatSelenggara() {
		try {
			mp = new MyPersistence();
			
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, getParam("idSelenggara"));
			context.put("r", r);
			context.put("selectedTab", "1");
			
		} catch (Exception e) {
			System.out.println("Error maklumatSelenggara : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	@Command("selenggaraRumahPeranginan")
	public String selenggaraRumahPeranginan() throws Exception {
		try {
			mp = new MyPersistence();
			
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, getParam("idSelenggara"));
			
			List<RppPeranginan> listRppPeranginan = getListRppPeranginan(mp,r);
			context.put("listRppPeranginan", listRppPeranginan);
			
			List<RppSelenggaraUnitLokasi> listSelenggaraLokasi = getListSelenggaraLokasi(mp,r);
			context.put("listSelenggaraLokasi", listSelenggaraLokasi);

			context.put("r", r);
			context.put("selectedTab", "2");
			
		} catch (Exception e) {
			System.out.println("Error selenggaraRumahPeranginan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("selenggaraUnit")
	public String selenggaraUnit() {
		try {
			mp = new MyPersistence();
			
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, getParam("idSelenggara"));
			
			RppSelenggaraUnitLokasi objsl = (RppSelenggaraUnitLokasi) mp.get("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+r.getId()+"' and x.rppSelenggara.flagJenisSelenggara = 'UNIT' ");
			
			context.remove("listUnit");
			
			List<RppUnit> listUnit = null;
			if( objsl!=null ){
				listUnit = mp.list("select x from RppUnit x where x.jenisUnit.peranginan.id = '"+objsl.getRppPeranginan().getId()+"' order by x.jenisUnit.keterangan asc ");
			}
			context.put("listUnit", listUnit);
			
			context.put("objsl", objsl);
			context.put("selectPeranginan",dataUtil.getListPeranginanRpp());
			context.put("r", r);
			context.put("selectedTab", "3");
			
		} catch (Exception e) {
			System.out.println("Error selenggaraUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	@Override
	public void save(RppSelenggara r) throws Exception {
		
		try {
			mp = new MyPersistence();
			
			userId = (String) request.getSession().getAttribute("_portal_login");
			Users objUser = (Users) mp.find(Users.class, userId);
			
			r.setFlagJenisSelenggara(getParam("flagJenisSelenggara"));
			r.setPerkara(getParam("perkara"));
			r.setCatatan(getParam("catatan"));
			r.setTarikhMula(getDate("tarikhMula"));
			r.setTarikhTamat(getDate("tarikhTamat"));
			r.setIdMasuk(objUser);
			r.setTarikhMasuk(new Date());
			
		} catch (Exception e) {
			System.out.println("Error save(getObj) : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<RppPeranginan> getListRppPeranginan(MyPersistence mp,RppSelenggara r){
		List<RppPeranginan> list = mp.list("select x from RppPeranginan x "+
		"where x.id not in (select y.rppPeranginan.id from RppSelenggaraUnitLokasi y "+
		"  where y.rppSelenggara.id = '"+r.getId()+"' and y.rppSelenggara.flagJenisSelenggara = 'LOKASI' ) "+
		"order by x.namaPeranginan asc ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppSelenggaraUnitLokasi> getListSelenggaraLokasi(MyPersistence mp, RppSelenggara r){
		List<RppSelenggaraUnitLokasi> list = mp.list("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+r.getId()+"' and x.rppSelenggara.flagJenisSelenggara = 'LOKASI' order by x.rppPeranginan.namaPeranginan asc ");
		return list;
	}
	
	@Command("savePilihanLokasiRpp")
	public String savePilihanLokasiRpp() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			RppPeranginan rpp = (RppPeranginan) mp.find(RppPeranginan.class, getParam("idrpp"));
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, getParam("idSelenggara"));
			RppSelenggaraUnitLokasi rs = new RppSelenggaraUnitLokasi();
			
			mp.begin();
			
			rs.setRppPeranginan(rpp);
			rs.setRppSelenggara(r);
			mp.persist(rs);
			
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("Error savePilihanLokasiRpp : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return selenggaraRumahPeranginan();
	}
	
	@Command("deletePilihanLokasiRpp")
	public String deletePilihanLokasiRpp() throws Exception {
		try {
			mp = new MyPersistence();
			
			RppSelenggaraUnitLokasi sl = (RppSelenggaraUnitLokasi) mp.find(RppSelenggaraUnitLokasi.class, getParam("idSelenggaraLokasi"));
			
			if(sl != null){
				mp.begin();
				mp.remove(sl);
				mp.commit();
			}
			
		} catch (Exception e) {
			System.out.println("Error deletePilihanLokasiRpp : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return selenggaraRumahPeranginan();
	}
	
	@SuppressWarnings("unchecked")
	@Command("listingUnit")
	public String listingUnit() {
		try {
			mp = new MyPersistence();
			
			List<RppUnit> listUnit = mp.list("select x from RppUnit x where x.jenisUnit.peranginan.id = '"+getParam("peranginan")+"' order by x.jenisUnit.keterangan asc ");
			context.put("listUnit", listUnit);
			
		} catch (Exception e) {
			System.out.println("Error listingUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/listingUnit.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("savePilihanUnit")
	public String savePilihanUnit() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			String[] cbPilihan = request.getParameterValues("cbPilihan");
			String peranginan = getParam("peranginan");
			RppPeranginan lokasi = (RppPeranginan) mp.find(RppPeranginan.class, peranginan);
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, getParam("idSelenggara"));
			
			//remove previous record first
			List<RppSelenggaraUnitLokasi> listDelete = mp.list("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+r.getId()+"' and x.rppSelenggara.flagJenisSelenggara = 'UNIT' ");
			
			mp.begin();
			
			for(int i=0;i<listDelete.size();i++){
				mp.remove(listDelete.get(i));
			}
			
			for(int i = 0; i < cbPilihan.length; i++){
				RppSelenggaraUnitLokasi rs = new RppSelenggaraUnitLokasi();
				RppUnit unit = (RppUnit) mp.find(RppUnit.class, cbPilihan[i]);
				
				rs.setRppSelenggara(r);
				rs.setRppUnit(unit);
				rs.setRppPeranginan(lokasi);
				mp.persist(rs);
			}
			
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("Error savePilihanUnit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return selenggaraUnit();
	}
	
	@Command("openPopupBookingList")
	public String openPopupBookingList() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			context.remove("listBooking");
			String idSelenggara = getParam("idSelenggara");
			String idUnit = getParam("idUnit");

			RppUnit unit = (RppUnit) mp.find(RppUnit.class, idUnit);
			context.put("listBooking",unit.getStatusUnit(idSelenggara));
			
			RppSelenggara r = (RppSelenggara) mp.find(RppSelenggara.class, idSelenggara);
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/bookingList.vm";
	}
	
}



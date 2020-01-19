package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppSelenggara;
import bph.entities.rpp.RppSelenggaraUnitLokasi;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.HTML;
import bph.utils.Util;

public class RppSelenggaraRecordModule extends LebahRecordTemplateModule<RppSelenggara> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<RppSelenggara> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppSelenggara.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/rpp/rppSelenggara";
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(RppSelenggara r) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void afterSave(RppSelenggara r) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean delete(RppSelenggara r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;		
		}
		return val;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("perkara", getParam("find_perkara"));
		return map;
	}
	
	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		defaultButtonOption();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	private void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	@Override
	public void getRelatedData(RppSelenggara r) {

		if(r.getFlagJenisSelenggara().equalsIgnoreCase("LOKASI")){
			List<RppPeranginan> rppList = getListPeranginan(r);
			context.put("listPeranginan", rppList);
			
			List<RppSelenggaraUnitLokasi> savedList = getListSavedSelenggara(r);
			context.put("listSavedPeranginan", savedList);
		}else{
			RppSelenggaraUnitLokasi sk = (RppSelenggaraUnitLokasi) db.get("select z from RppSelenggaraUnitLokasi z where z.rppSelenggara.id = '"+r.getId()+"' ");
			try {
				context.put("selectPeranginan", HTML.SelectPeranginan("peranginan",sk.getRppPeranginan().getId(), "id=\"peranginan\" style=\"width:100%\" ", "onchange=\" doListingUnit(); $('err_peranginan').innerHTML=''; at(this, event); \""));
			} catch (Exception e) {
				System.out.println("error "+e.getMessage()+" "+command);
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<RppPeranginan> getListPeranginan(RppSelenggara r){
		List<RppPeranginan> list = 
				db.list("select x from RppPeranginan x "+
					    "where x.id not in (select y.rppPeranginan.id from RppSelenggaraUnitLokasi y where y.rppSelenggara.id = '"+r.getId()+"' ) "+
						"order by x.namaPeranginan asc ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppSelenggaraUnitLokasi> getListSavedSelenggara(RppSelenggara r){
		List<RppSelenggaraUnitLokasi> 
			list = db.list("select x from RppSelenggaraUnitLokasi x where x.rppSelenggara.id = '"+r.getId()+"' ");
		return list;
	}
	
	@Command("maklumatSelenggara")
	public String maklumatSelenggara() throws Exception {
		
		RppSelenggara r = db.find(RppSelenggara.class, getParam("idSelenggara"));
		context.put("r", r);

		return getPath() + "/entry_page.vm";
	}
	

	@Command("getListRppDanUnit")
	public String getListRppDanUnit() throws Exception {
		
		String vm = "";
		String flagJenisSelenggara = getParam("flagJenisSelenggara");
		
		if(flagJenisSelenggara.equalsIgnoreCase("UNIT")){
			context.put("selectPeranginan", HTML.SelectPeranginan("peranginan",null, "id=\"peranginan\" style=\"width:100%\" ", "onchange=\" doListingUnit(); $('err_peranginan').innerHTML=''; at(this, event); \""));
			//List<RppUnit> listUnit = db.list("select x from RppUnit x where x.jenisUnit.peranginan.id = '"+getParam("peranginan")+"' ");
			//context.put("listUnit", listUnit);
			vm = "/form/listingUnit.vm";
		}else{
			context.put("listPeranginan", dataUtil.getListPeranginanRpp());
			vm = "/form/listingLokasiPeranginan.vm";
		}
		return getPath() + vm;
	}
	
	@SuppressWarnings("unchecked")
	@Command("listingUnit")
	public String listingUnit() throws Exception {
		context.put("selectPeranginan", HTML.SelectPeranginan("peranginan",getParam("peranginan"), "id=\"peranginan\" style=\"width:100%\" ", "onchange=\" doListingUnit(); $('err_peranginan').innerHTML=''; at(this, event); \""));
		List<RppUnit> listUnit = db.list("select x from RppUnit x where x.jenisUnit.peranginan.id = '"+getParam("peranginan")+"' ");
		context.put("listUnit", listUnit);
		return getPath() + "/form/listingUnit.vm";
	}
	
	@SuppressWarnings("unused")
	@Command("simpanPilihan")
	public String simpanPilihan() throws Exception {
		
		String flagJenisSelenggara = getParam("flagJenisSelenggara");
		
		RppSelenggara r = db.find(RppSelenggara.class, getParam("idSelenggara"));
		
		if(r==null){
			r = new RppSelenggara();
		}
		
		db.begin();
		r.setFlagJenisSelenggara(flagJenisSelenggara);
		r.setPerkara(getParam("perkara"));
		r.setCatatan(getParam("catatan"));
		r.setTarikhMula(getDate("tarikhMula"));
		r.setTarikhTamat(getDate("tarikhTamat"));
		
		if(r==null){
			r.setTarikhMasuk(new Date());
		}
		
		db.persist(r);
		
		try {
			db.commit();
			
			db.begin();
			if(flagJenisSelenggara.equalsIgnoreCase("UNIT")){
				saveByUnit(r);
			}else{
				saveByLokasi(r);
			}
			
			db.commit();
			
		} catch (Exception e) {
			System.out.println("error saving.. "+e.getMessage());
		}
		
		return maklumatSelenggara();
	}
	
	
	@Command("saveByUnit")
	public void saveByUnit(RppSelenggara r) throws Exception {
		
		String[] cbPilihan = request.getParameterValues("cbPilihan");
		
		String peranginan = getParam("peranginan");
		RppPeranginan lokasi = db.find(RppPeranginan.class, peranginan);
		for(int i = 0; i < cbPilihan.length; i++){
			RppSelenggaraUnitLokasi rs = new RppSelenggaraUnitLokasi();
			RppUnit unit = db.find(RppUnit.class, cbPilihan[i]);
			
			rs.setRppSelenggara(r);
			rs.setRppUnit(unit);
			rs.setRppPeranginan(lokasi);
			//rs.setCatatan(catatan);
			//rs.setStatus(status);
			db.persist(rs);
		}
		
	}
	
	@Command("saveByLokasi")
	public void saveByLokasi(RppSelenggara r) throws Exception {
		
		String[] cbPilihan = request.getParameterValues("cbPilihan");
		
		for(int i = 0; i < cbPilihan.length; i++){
			RppSelenggaraUnitLokasi rs = new RppSelenggaraUnitLokasi();
			RppPeranginan lokasi = db.find(RppPeranginan.class, cbPilihan[i]);
			
			rs.setRppPeranginan(lokasi);
			rs.setRppSelenggara(r);
			//rs.setCatatan(catatan);
			//rs.setStatus(status);
			db.persist(rs);
		}
		
	}
	
	
}



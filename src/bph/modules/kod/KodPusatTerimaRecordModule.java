package bph.modules.kod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.KodPusatTerima;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class KodPusatTerimaRecordModule extends LebahRecordTemplateModule<KodPusatTerima> {

	/**
	* @author muhdsyazreen
	*/
	
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(KodPusatTerima r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");	
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
//		if (!"add_new_record".equals(command)){
//			this.setDisableBackButton(true);
//			this.setDisableDefaultButton(true);
//		}		
	}

	private void addfilter() {		
		this.setOrderBy("unit");
		this.setOrderType("asc");

	}

	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {

	}

	@Override
	public boolean delete(KodPusatTerima r) throws Exception {
		if (r.getListJuruwang().size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/kodPusatTerima";
	}

	@Override
	public Class<KodPusatTerima> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KodPusatTerima.class;
	}

	@Override
	public void getRelatedData(KodPusatTerima r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KodPusatTerima r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		r.setUnit(getParam("unit").trim());
		r.setKodPusatTerima(getParam("kodPusatTerima").trim());
		r.setKodPenyataPemungut(getParam("kodPenyataPemungut").trim());
		r.setFlagAktif(getParam("flagAktif").trim());
		r.setCatatan(getParam("catatan").trim());

		if (getParam("id").equals("")){
			r.setDaftarOleh(db.find(Users.class, userId));
		} else {
			r.setKemaskiniOleh(db.find(Users.class, userId));
			r.setTarikhKemaskini(new Date());
		}			
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findUnit = get("findUnit").trim();
		String findKodPusatTerima = get("findKodPusatTerima").trim();
		String findKodPenyataPemungut = get("findKodPenyataPemungut").trim();
		String findFlagAktif = get("findFlagAktif").trim();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("unit", findUnit);
		map.put("kodPusatTerima", findKodPusatTerima);
		map.put("kodPenyataPemungut", findKodPenyataPemungut);
		map.put("flagAktif", new OperatorEqualTo(findFlagAktif));
		return map;
	}
	
	@Command("semakanKodPusatTerima")
	public String semakanKodPusatTerima() throws Exception {
		String id = getParam("id");
		String kodPusatTerima = getParam("kodPusatTerima");
		KodPusatTerima semakanKod = null;

		try {
			mp = new MyPersistence();
			KodPusatTerima kod = (KodPusatTerima) mp.find(KodPusatTerima.class, id);
			if (kod != null) {
				semakanKod = (KodPusatTerima) mp.get("select x from KodPusatTerima x where x.kodPusatTerima = '" + kodPusatTerima + "' and x.id not in (" + kod.getId() + ")");
			} else {
				semakanKod = (KodPusatTerima) mp.get("select x from KodPusatTerima x where x.kodPusatTerima = '" + kodPusatTerima + "'");
			}
			
			if (semakanKod != null) {
				context.put("semakanKodPusatTerimaExist", "Y");
			} else {
				context.remove("semakanKodPusatTerimaExist");
			}
				
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/semakanKodPusatTerima.vm";
	}
	
	@Command("semakanKodPenyataPemungut")
	public String semakanKodPenyataPemungut() throws Exception {
		String id = getParam("id");
		String kodPenyataPemungut = getParam("kodPenyataPemungut");
		KodPusatTerima semakanKod = null;

		try {
			mp = new MyPersistence();
			KodPusatTerima kod = (KodPusatTerima) mp.find(KodPusatTerima.class, id);
			if (kod != null) {
				semakanKod = (KodPusatTerima) mp.get("select x from KodPusatTerima x where x.kodPenyataPemungut = '" + kodPenyataPemungut + "' and x.id not in (" + kod.getId() + ")");
			} else {
				semakanKod = (KodPusatTerima) mp.get("select x from KodPusatTerima x where x.kodPenyataPemungut = '" + kodPenyataPemungut + "'");
			}
			
			if (semakanKod != null) {
				context.put("semakanKodPenyataPemungutExist", "Y");
			} else {
				context.remove("semakanKodPenyataPemungutExist");
			}
				
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/semakanKodPenyataPemungut.vm";
	}
}

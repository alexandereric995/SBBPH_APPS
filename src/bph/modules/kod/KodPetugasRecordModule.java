/**
	* @author roszaineza
	*/

package bph.modules.kod;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.KodPetugas;
import bph.entities.kod.KodPusatTerima;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class KodPetugasRecordModule extends LebahRecordTemplateModule<KodPetugas> {
	
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
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
		
		List<KodPusatTerima> listPusatTerima = dataUtil.getListPusatTerima();		
		context.put("selectPusatTerima", listPusatTerima);
		
		context.remove("idPusatTerima");	
		context.remove("petugas");
		context.remove("flagAktif");	
		context.remove("catatan");		
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	private void addfilter() {		
		this.setOrderBy("petugas.userName");
		this.setOrderType("asc");

	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {

	}

	@Override
	public String getPath() {
		return "bph/modules/kod/kodPetugas";
	}

	@Override
	public Class<KodPetugas> getPersistenceClass() {
		return KodPetugas.class;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findPusatTerima = get("findPusatTerima").trim();
		String findNoPengenalan = get("findNoPengenalan").trim();
		String findNama = get("findNama").trim();		

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cawangan.id", new OperatorEqualTo(findPusatTerima));
		map.put("petugas.id", findNoPengenalan);
		map.put("petugas.userName", findNama);
		return map;
	}
	
	@Command("getRegisteredUser")
	public String getRegisteredUser() throws Exception {
		String idJuruwang = getParam("idJuruwang");
		try {
			mp = new MyPersistence();
			Users petugas = (Users) mp.find(Users.class, idJuruwang);
			context.put("petugas", petugas);	
			context.put("idPusatTerima", getParam("idPusatTerima"));		
			context.put("flagAktif", getParam("flagAktif"));
			context.put("catatan", getParam("catatan"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/daftarRekod.vm";
	}
	
	@Command("getJawatan")
	public String getJawatan() throws Exception {
		String idPetugas = getParam("idJuruwang");
		try {
			mp = new MyPersistence();
			Users petugas = (Users) mp.find(Users.class, idPetugas);
			context.put("petugas", petugas);	
			context.put("idPusatTerima", getParam("idPusatTerima"));		
			context.put("flagAktif", getParam("flagAktif"));
			context.put("catatan", getParam("catatan"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/daftarRekod.vm";
	}
	
	@Override
	public void afterSave(KodPetugas r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(KodPetugas r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void getRelatedData(KodPetugas r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(KodPetugas r) throws Exception {
		// TODO Auto-generated method stub
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		KodPusatTerima pusatTerima = db.find(KodPusatTerima.class, getParam("idPusatTerima"));

		r.setCawangan(pusatTerima);
		r.setKodCawangan(pusatTerima.getKodPusatTerima());
		r.setPetugas(db.find(Users.class, getParam("idJuruwang")));
		r.setCatatan(getParam("catatan"));
		
		if ("".equals(id)) {
			r.setDaftarOleh(db.find(Users.class, userId));
			r.setTarikhMasuk(new Date());
		} else {
			r.setKemaskiniOleh(db.find(Users.class, userId));
			r.setTarikhKemaskini(new Date());
		}
	}
}

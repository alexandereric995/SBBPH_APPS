package bph.modules.cms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.portal.CmsOperatorUnit;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsOperatorUnitRecordModule extends LebahRecordTemplateModule<CmsOperatorUnit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private Util util = new Util();
	
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}
	
	@Override
	public Class<CmsOperatorUnit> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsOperatorUnit.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/operatorUnit";
	}
	
	@Override
	public void begin() {
				
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
		this.setOrderBy("unit");
		this.setOrderType("asc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void save(CmsOperatorUnit r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		r.setUnit(getParam("unit"));
		r.setNama(getParam("nama"));
		r.setNoTelefon(getParam("noTelefon"));
		r.setEmel(getParam("emel"));
		r.setFlagAktif("Y");
		r.setDaftarOleh(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());		
	}

	@Override
	public void afterSave(CmsOperatorUnit r) {
		// TODO Auto-generated method stub
	}	

	@Override
	public boolean delete(CmsOperatorUnit r) throws Exception {
		return true;
	}	
	
	@Override
	public void getRelatedData(CmsOperatorUnit r) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("unit", get("findUnit").trim());
		map.put("nama", get("findNama").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
		
		return map;
	}
	
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		CmsOperatorUnit operatorUnit = null;
		
		mp = new MyPersistence();
		try {			
			operatorUnit = (CmsOperatorUnit) mp.find(CmsOperatorUnit.class, id);
			
			if (operatorUnit != null) {
				mp.begin();
				operatorUnit.setUnit(getParam("unit"));
				operatorUnit.setNama(getParam("nama"));
				operatorUnit.setNoTelefon(getParam("noTelefon"));
				operatorUnit.setEmel(getParam("emel"));
				operatorUnit.setFlagAktif(getParam("flagAktif"));
				operatorUnit.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				operatorUnit.setTarikhKemaskini(new Date());	
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", operatorUnit);
		return getPath() + "/entry_page.vm";
	}
}

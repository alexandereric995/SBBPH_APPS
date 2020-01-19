package bph.modules.cms;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.portal.CmsPengumuman;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsPengumumanRecordModule extends LebahRecordTemplateModule<CmsPengumuman> {

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
	public void afterSave(CmsPengumuman r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		
		//--------- start Function Turutan ---------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsPengumuman pengumuman = (CmsPengumuman) mp.get("select x from CmsPengumuman x where x.flagAktif = 'Y' order by x.turutan desc");
			if (pengumuman != null) {
				count = pengumuman.getTurutan() + 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		Vector pengumuman = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			pengumuman.addElement(h);
		}	
		
		context.put("selectPengumuman", pengumuman);
		//--------- End Function Turutan ---------
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//--------- add byzul ---------
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}

	private void addfilter() {
		this.setOrderBy("turutan");
		this.setOrderType("asc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub
		
	}
	//--------- add byzul ---------

	@Override
	public boolean delete(CmsPengumuman rekod) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/pengumuman";
	}

	@Override
	public Class<CmsPengumuman> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsPengumuman.class;
	}

	@Override
	public void getRelatedData(CmsPengumuman r) {
		// TODO Auto-generated method stub
		
		//------- Start Function Turutan ------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsPengumuman pengumuman = (CmsPengumuman) mp.get("select x from CmsPengumuman x where x.flagAktif = 'Y' order by x.turutan desc");
			if (pengumuman != null) {
				if ("Y".equals(r.getFlagAktif())) {
					count = pengumuman.getTurutan();
				} else {
					count = pengumuman.getTurutan() + 1;
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		Vector pengumuman = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			pengumuman.addElement(h);
		}	
		
		context.put("selectPengumuman", pengumuman);
		//------- End Function Turutan ------
		
	}

	@Override
	public void save(CmsPengumuman simpan) throws Exception {
		// TODO Auto-generated method stub
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		simpan.setTajuk(getParam("tajuk").trim());
		simpan.setButiran(getParam("butiran").trim());
		simpan.setFlagAktif(getParam("flagAktif"));

		if ("Y".equals(getParam("flagAktif"))) {
			simpan.setTurutan(getParamAsInteger("turutan"));
		} else {
			simpan.setTurutan(99999);
		}
		
		simpan.setDaftarOleh(db.find(Users.class, userId));
		simpan.setTarikhMasuk(new Date());		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("tajuk", get("findTajuk").trim());
		map.put("flagAktif", new OperatorEqualTo(get("findFlagAktif")));
		
		return map;
	}
	
	/************************************* START FUNCTION PENGUMUMAN *************************************/
	//------- start function kemaskini ------
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idPengumuman");
		int turutan = getParamAsInteger("turutan");
		CmsPengumuman pengumuman = null;
		
		mp = new MyPersistence();
		try {			
			pengumuman = (CmsPengumuman) mp.find(CmsPengumuman.class, id);
			
			if (pengumuman != null) {
				mp.begin();
				pengumuman.setTajuk(getParam("tajuk").trim());
				pengumuman.setButiran(getParam("butiran").trim());
				if ("T".equals(getParam("flagAktif"))) {
					pengumuman.setTurutan(99999);
				} else {
					if (pengumuman.getTurutan() != turutan) {
						reArrangeTurutan(pengumuman.getId(), turutan, mp);
					}
					pengumuman.setTurutan(turutan);
				}
				pengumuman.setFlagAktif(getParam("flagAktif"));
				pengumuman.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				pengumuman.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		if ("T".equals(getParam("flagAktif"))) {
			reArrangeTurutanTidakAktif();
		}
		
		context.put("r", pengumuman);
		return getPath() + "/entry_page.vm";
	}
	//------- end function kemaskini ------
	
	private void reArrangeTurutan(String id, int turutan, MyPersistence mp) {
		int count = 1;
		List <CmsPengumuman> listPengumuman = mp.list("select x from CmsPengumuman x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listPengumuman.size(); i++) {
			CmsPengumuman pengumuman = (CmsPengumuman) mp.find(CmsPengumuman.class, listPengumuman.get(i).getId());
			if (pengumuman != null) {
				if (count == turutan) {
					count++;
				}
				pengumuman.setTurutan(count);
				count++;
			}
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
			List <CmsPengumuman> listPengumuman = mp.list("select x from CmsPengumuman x where x.flagAktif = 'Y' order by x.turutan asc");
			for (int i = 0; i < listPengumuman.size(); i++) {
				CmsPengumuman pengumuman = (CmsPengumuman) mp.find(CmsPengumuman.class, listPengumuman.get(i).getId());
				if (pengumuman != null) {
					pengumuman.setTurutan(i+1);
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}

	/************************************* END FUNCTION PENGUMUMAN *************************************/
	
}

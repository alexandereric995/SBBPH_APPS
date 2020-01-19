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
import bph.entities.portal.CmsMakluman;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsMaklumanRecordModule extends LebahRecordTemplateModule<CmsMakluman>{

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
	public void afterSave(CmsMakluman arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		//--------- start Function Turutan ---------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsMakluman makluman = (CmsMakluman) mp.get("select x from CmsMakluman x where x.flagAktif = 'Y' order by x.turutan desc");
			if (makluman != null) {
				count = makluman.getTurutan() + 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		Vector makluman = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			makluman.addElement(h);
		}	
			
		context.put("selectMakluman", makluman);
		//--------- End Function Turutan ---------
		
		defaultButtonOption();
		addfilter();
		doOverideFilterRecord(); //TODO IMPLEMENT BILA ADA SUBCLASS
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
	public boolean delete(CmsMakluman arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/makluman";
	}

	@Override
	public Class<CmsMakluman> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsMakluman.class;
	}

	@Override
	public void getRelatedData(CmsMakluman r) {
		// TODO Auto-generated method stub
		
		//------- Start Function Turutan ------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsMakluman makluman = (CmsMakluman) mp.get("select x from CmsMakluman x where x.flagAktif = 'Y' order by x.turutan desc");
			if (makluman != null) {
				if ("Y".equals(r.getFlagAktif())) {
					count = makluman.getTurutan();
				} else {
					count = makluman.getTurutan() + 1;
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		Vector makluman = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			makluman.addElement(h);
		}	
		
		context.put("selectMakluman", makluman);
		//------- End Function Turutan ------
		
	}

	@Override
	public void save(CmsMakluman simpan) throws Exception {
		// TODO Auto-generated method stub
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
//		simpan.setTajuk(getParam("tajuk"));
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
	
	
	/************************************* START FUNCTION MAKLUMAN *************************************/
	//------- start function kemaskini ------
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idMakluman");
		int turutan = getParamAsInteger("turutan");
		CmsMakluman makluman = null;
		
		mp = new MyPersistence();
		try {			
			makluman = (CmsMakluman) mp.find(CmsMakluman.class, id);
			
			if (makluman != null) {
				mp.begin();
//					makluman.setTajuk(getParam("tajuk"));
					makluman.setButiran(getParam("butiran").trim());
					if ("T".equals(getParam("flagAktif"))) {
						makluman.setTurutan(99999);
					} else {
						if (makluman.getTurutan() != turutan) {
							reArrangeTurutan(makluman.getId(), turutan, mp);
						}
						makluman.setTurutan(turutan);
					}
					makluman.setFlagAktif(getParam("flagAktif"));
					makluman.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					makluman.setTarikhKemaskini(new Date());				
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
		
		context.put("r", makluman);
		return getPath() + "/entry_page.vm";
	}
	//------- end function kemaskini ------
	
	private void reArrangeTurutan(String id, int turutan, MyPersistence mp) {
		int count = 1;
		List <CmsMakluman> listMakluman = mp.list("select x from CmsMakluman x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listMakluman.size(); i++) {
			CmsMakluman makluman = (CmsMakluman) mp.find(CmsMakluman.class, listMakluman.get(i).getId());
			if (makluman != null) {
				if (count == turutan) {
					count++;
				}
				makluman.setTurutan(count);
				count++;
			}
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
				List <CmsMakluman> listMakluman = mp.list("select x from CmsMakluman x where x.flagAktif = 'Y' order by x.turutan asc");
				for (int i = 0; i < listMakluman.size(); i++) {
					CmsMakluman makluman = (CmsMakluman) mp.find(CmsMakluman.class, listMakluman.get(i).getId());
					if (makluman != null) {
						makluman.setTurutan(i+1);
					}
				}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}
	/************************************* END FUNCTION MAKLUMAN *************************************/
	
	
}

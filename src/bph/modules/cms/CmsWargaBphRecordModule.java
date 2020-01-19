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
import bph.entities.portal.CmsWargaBph;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsWargaBphRecordModule extends LebahRecordTemplateModule<CmsWargaBph> {

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
	public void afterSave(CmsWargaBph r) {
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
			CmsWargaBph turutan = (CmsWargaBph) mp.get("select x from CmsWargaBph x where x.flagAktif = 'Y' order by x.turutan desc");
			if (turutan != null) {
				count = turutan.getTurutan() + 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);
		//--------- End Function Turutan ---------
		
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
		this.setOrderBy("id");
		this.setOrderType("asc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(CmsWargaBph rekod) throws Exception {
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/wargaBph";
	}

	@Override
	public Class<CmsWargaBph> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsWargaBph.class;
	}

	@Override
	public void getRelatedData(CmsWargaBph rekod) {
		// TODO Auto-generated method stub	
		
		//------- Start Function Turutan ------
		int count = 1;
		try {
			mp = new MyPersistence();
			CmsWargaBph turutan = (CmsWargaBph) mp.get("select x from CmsWargaBph x where x.flagAktif = 'Y' order by x.turutan desc");
			if (turutan != null) {
				count = turutan.getTurutan();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		Vector turutan = new Vector();
		Hashtable h = null;
		for (int i = 1; i <= count; i++) {			
			h = new Hashtable();
			h.put("id", i);
			turutan.addElement(h);
		}	
		
		context.put("selectTurutan", turutan);
		//------- End Function Turutan ------
	}

	@Override
	public void save(CmsWargaBph simpan) throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");		
		simpan.setTajuk(getParam("tajuk"));
		simpan.setUrl(getParam("url"));
		//------- Start Save Turutan ------
		if ("Y".equals(getParam("flagAktif"))) {
			simpan.setTurutan(getParamAsInteger("turutan"));
		} else {
			simpan.setTurutan(99999);
		}
		//------- Edn Save Turutan ------
		simpan.setFlagAktif(getParam("flagAktif"));
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
	
	/************************************* START FUNCTION WARGA BPH *************************************/
	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		int strTurutan = getParamAsInteger("turutan");
		CmsWargaBph kemaskini = null;
		
		mp = new MyPersistence();
		try {			
			kemaskini = (CmsWargaBph) mp.find(CmsWargaBph.class, id);
			
			if (kemaskini != null) {
				mp.begin();
				kemaskini.setTajuk(getParam("tajuk"));
				kemaskini.setUrl(getParam("url"));
				if ("T".equals(getParam("flagAktif"))) {
					kemaskini.setTurutan(99999);
				} else {
					if (kemaskini.getTurutan() != strTurutan) {
						reArrangeTurutan(kemaskini.getId(), strTurutan, mp);
					}
					kemaskini.setTurutan(strTurutan);
				}
				kemaskini.setFlagAktif(getParam("flagAktif"));
				kemaskini.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				kemaskini.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("r", strTurutan);
		return getPath() + "/entry_page.vm";
	}
	
	//---------- Start Function Turutan ----------
	private void reArrangeTurutan(String id, int intTurutan, MyPersistence mp) {
		int count = 1;
		List <CmsWargaBph> listBantuan = mp.list("select x from CmsWargaBph x where x.id not in (" + id + ") and x.flagAktif = 'Y' order by x.turutan asc");
		for (int i = 0; i < listBantuan.size(); i++) {
			CmsWargaBph turutan = (CmsWargaBph) mp.find(CmsWargaBph.class, listBantuan.get(i).getId());
			if (turutan != null) {
				if (count == intTurutan) {
					count++;
				}
				turutan.setTurutan(count);
				count++;
			}
		}		
	}
	
	private void reArrangeTurutanTidakAktif() {
		try {
			mp = new MyPersistence();
			mp.begin();
			List <CmsWargaBph> listBantuan = mp.list("select x from CmsWargaBph x where x.flagAktif = 'Y' order by x.turutan asc");
			for (int i = 0; i < listBantuan.size(); i++) {
				CmsWargaBph turutan = (CmsWargaBph) mp.find(CmsWargaBph.class, listBantuan.get(i).getId());
				if (turutan != null) {
					turutan.setTurutan(i+1);
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}
	//---------- End Function Turutan ----------
	
	/************************************* END FUNCTION WARGA BPH *************************************/
	

}

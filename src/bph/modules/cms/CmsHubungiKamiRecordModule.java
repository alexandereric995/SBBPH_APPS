package bph.modules.cms;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.portal.CmsHubungiKami;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class CmsHubungiKamiRecordModule extends LebahRecordTemplateModule<CmsHubungiKami>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private Util util = new Util();
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<CmsHubungiKami> getPersistenceClass() {
		// TODO Auto-generated method stub
		return CmsHubungiKami.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/cms/hubungiKami";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
				
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
		this.setOrderBy("flagUtama");
		this.setOrderType("desc");
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(CmsHubungiKami r) throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			r.setBahagian(getParam("bahagian"));
			r.setJabatan(getParam("jabatan"));
			r.setAlamat1(getParam("alamat1"));
			r.setAlamat2(getParam("alamat2"));
			r.setAlamat3(getParam("alamat3"));
			r.setPoskod(getParam("poskod"));
			r.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
			r.setNoTelefon(getParam("noTelefon"));
			r.setNoFaks(getParam("noFaks"));
			r.setEmel(getParam("emel"));
			r.setFlagUtama("T");
			r.setDaftarOleh((Users) mp.find(Users.class, userId));
			r.setTarikhMasuk(new Date());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void afterSave(CmsHubungiKami r) {
		// TODO Auto-generated method stub
	}	
	
	@Override
	public boolean delete(CmsHubungiKami r) throws Exception {
		
		if ("T".equals(r.getFlagUtama())) {
			return true;
		} else {
			return false;
		}
	}	
	
	@Override
	public void getRelatedData(CmsHubungiKami r) {
		if (r.getBandar() != null) {
			if (r.getBandar().getNegeri() != null) {
				context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
			}			
		}
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("bahagian", get("findBahagian").trim());
		map.put("bandar.id", new OperatorEqualTo(get("findBandar")));
		map.put("bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		
		return map;
	}
		

	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idHubungiKami");
		CmsHubungiKami r = null;
		
		mp = new MyPersistence();
		try {			
			r = (CmsHubungiKami) mp.find(CmsHubungiKami.class, id);
			
			if (r != null) {
				mp.begin();
				r.setBahagian(getParam("bahagian"));
				r.setJabatan(getParam("jabatan"));
				r.setAlamat1(getParam("alamat1"));
				r.setAlamat2(getParam("alamat2"));
				r.setAlamat3(getParam("alamat3"));
				r.setPoskod(getParam("poskod"));
				r.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				r.setNoTelefon(getParam("noTelefon"));
				r.setNoFaks(getParam("noFaks"));
				r.setEmel(getParam("emel"));
				r.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				r.setTarikhKemaskini(new Date());				
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		if (r.getBandar() != null) {
			if (r.getBandar().getNegeri() != null) {
				context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
			}			
		}
		context.put("r", r);
		return getPath() + "/entry_page.vm";
	}
	
	/** START DROPDOWN **/
	@Command("findBandar")
	public String findBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0)
			idNegeri = getParam("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("findBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";
		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/selectBandar.vm";
	}
	/** END DROPDOWN **/
}

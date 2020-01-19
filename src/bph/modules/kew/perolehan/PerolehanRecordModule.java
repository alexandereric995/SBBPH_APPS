/**
 * @author muhdsyazreen
 */

package bph.modules.kew.perolehan;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kew.KewPerolehan;
import bph.entities.kod.KodJenisPerolehan;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class PerolehanRecordModule extends LebahRecordTemplateModule<KewPerolehan>{

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void afterSave(KewPerolehan r) { }

	@Override
	public void beforeSave() { }

	@Override
	public String getPath() {return "bph/modules/kewangan/perolehan";}
	
	@Override
	public Class<KewPerolehan> getPersistenceClass() { return KewPerolehan.class; }
	
	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try{
			mp = new MyPersistence();
			Users userLogin = (Users) mp.find(Users.class, userId);
			
			defaultButtonOption();
			filtering(userRole);
			
			context.put("listStatus", dataUtil.getListStatusPerolehan());
			context.put("listKodJenisPerolehan",dataUtil.getListKodJenisPerolehan());
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("command", command);
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			
			context.put("userRole",userRole);
			context.put("userLogin", userLogin);
			
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	private void filtering(String userRole) {
		if(userRole.equalsIgnoreCase("(PEROLEHAN) Pelulus")){
			this.addFilter("status.id IN ('1422943424808','1422943424811','15027197149467845')");
		}else if(userRole.equalsIgnoreCase("(PEROLEHAN) Penyemak")){
			this.addFilter("status.id IN ('1422941888314','1422943424799','1422943424802','15027197149467845')");
		}else{
			this.addFilter("status.id IN ('1422934789302','1422941888314','1422943424799','1422943424802','1422943424808','1422943424811','15027197149467845')");
		}
	}
	
	private void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisabledInfoNextTab(true);
		this.setDisableUpperBackButton(true);
		this.setHideDeleteButton(true);
	}
	
	@Override
	public boolean delete(KewPerolehan r) throws Exception { 
		boolean x = false;
		if(r.getStatus().getId() == "1422934789302"){
			x = true; 
		}
		return x;
	}
	
	@Override
	public void save(KewPerolehan r) throws Exception { }

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tajuk", getParam("findTajuk"));
		map.put("unit", getParam("findUnit"));
		map.put("kodJenisPerolehan.id", new OperatorEqualTo(getParam("findKodJenisPerolehan")));
		map.put("status.id", new OperatorEqualTo(getParam("findStatus")));
		return map;
	}
	
	@Override
	public void getRelatedData(KewPerolehan r) {
		try {
			mp = new MyPersistence();
			KewPerolehan rr = (KewPerolehan) mp.find(KewPerolehan.class, r.getId());
			context.put("r", rr);
			context.put("selectedTab", "1");
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan() throws Exception {
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			context.put("r", r);
			context.put("selectedTab", "1");
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error getMaklumatPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatPermohonan.vm";
	}

	@Command("getSokongan")
	public String getSokongan() throws Exception {
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			context.put("r", r);
			context.put("selectedTab", "2");
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error getSokongan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatSokongan.vm";
	}

	@Command("getKelulusan")
	public String getKelulusan() throws Exception {
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			context.put("r", r);
			context.put("selectedTab", "3");
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error getKelulusan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatKelulusan.vm";
	}
	
	@Command("savePerolehan")
	public String savePerolehan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			
			mp.begin();
			
			if(r == null){ r = new KewPerolehan(); }
			
			r.setTajuk(getParam("tajuk"));
			r.setUnit(getParam("unit"));
			r.setKodJenisPerolehan((KodJenisPerolehan) mp.find(KodJenisPerolehan.class, getParam("kodJenisPerolehan")));
			r.setJustifikasi(getParam("justifikasi"));
			r.setStatus((Status) mp.find(Status.class, "1422934789302"));
			r.setIdMasuk((Users) mp.find(Users.class, userId));
			r.setTarikhMasuk(new Date());
			mp.persist(r);
			
			mp.commit();
			
			context.put("r", r);
			context.put("selectedTab", "1");
			context.put("userRole",userRole);
			
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error savePerolehan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("hantarSokongan")
	public String hantarSokongan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			
			if(r != null){ 
				mp.begin();
				r.setStatus((Status) mp.find(Status.class, "1422941888314"));
				r.setIdKemaskini((Users) mp.find(Users.class, userId));
				r.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
			context.put("r", r);
			context.put("selectedTab", "1");
			context.put("userRole",userRole);
			
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error hantarSokongan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("saveSokongan")
	public String saveSokongan() throws Exception {
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			
			if(r != null){ 
				mp.begin();
				
				String status = "";
				String sokongan = getParam("sokongan");
				if(sokongan.equalsIgnoreCase("S")){
					status = "1422943424799"; //PERMOHONAN DISOKONG
				}else if(sokongan.equalsIgnoreCase("TS")){
					status = "1422943424802"; //PERMOHONAN TIDAK DISOKONG
				}
				
				r.setFlagSokong(sokongan);
				r.setCatatanSokongan(getParam("catatanSokongan"));
				r.setStatus((Status) mp.find(Status.class, status));
				r.setIdKemaskini((Users) mp.find(Users.class, userId));
				r.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error saveSokongan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getSokongan();
	}
	
	@Command("hantarKelulusan")
	public String hantarKelulusan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			
			if(r != null){ 
				mp.begin();
				r.setStatus((Status) mp.find(Status.class, "15027197149467845")); //MENUNGGU KELULUSAN
				r.setIdKemaskini((Users) mp.find(Users.class, userId));
				r.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error hantarKelulusan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getSokongan();
	}

	@Command("saveKelulusan")
	public String saveKelulusan() throws Exception {
		try {
			mp = new MyPersistence();
			String idperolehan = getParam("idperolehan");
			KewPerolehan r = (KewPerolehan) mp.find(KewPerolehan.class, idperolehan);
			
			if(r != null){ 
				mp.begin();
				
				String status = "";
				String kelulusan = getParam("kelulusan");
				if(kelulusan.equalsIgnoreCase("L")){
					status = "1422943424808"; //PERMOHONAN LULUS
				}else if(kelulusan.equalsIgnoreCase("TL")){
					status = "1422943424811"; //PERMOHONAN TIDAK LULUS
				}
				
				r.setFlagLulus(kelulusan);
				r.setCatatanLulus(getParam("catatanLulus"));
				r.setStatus((Status) mp.find(Status.class, status));
				r.setIdKemaskini((Users) mp.find(Users.class, userId));
				r.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
		} catch (Exception e) {
			System.out.println("[PerolehanRecordModule] Error saveKelulusan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getKelulusan();
	}
	
	
}





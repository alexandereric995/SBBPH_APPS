package bph.modules.senggara;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.senggara.MtnKontraktorSenaraiHitam;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiHitamKontraktorRecordModule extends LebahRecordTemplateModule<MtnKontraktorSenaraiHitam> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(MtnKontraktorSenaraiHitam r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}

	private void defaultButtonOption() {
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
	
	private void addfilter() {
		
		this.setOrderBy("flagAktif");
		this.setOrderType("desc");
	}	

	@Override
	public boolean delete(MtnKontraktorSenaraiHitam r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/senggara/senaraiHitamKontraktor";
	}

	@Override
	public Class<MtnKontraktorSenaraiHitam> getPersistenceClass() {
		// TODO Auto-generated method stub
		return MtnKontraktorSenaraiHitam.class;
	}

	@Override
	public void getRelatedData(MtnKontraktorSenaraiHitam r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(MtnKontraktorSenaraiHitam r) throws Exception {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findNoPendaftaran = get("findNoPendaftaran");
		String findNamaKontraktor = get("findNamaKontraktor");
		String findNamaPemilik = get("findNamaPemilik");
		String flag = get("flag");


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kontraktor.id", findNoPendaftaran);
		map.put("kontraktor.namaKontraktor", findNamaKontraktor);
		map.put("kontraktor.namaPemilik", findNamaPemilik);
		map.put("flagAktif", flag);		

		return map;
	}
	
	@Command("paparMaklumatSenaraiHitam")
	public String paparMaklumatSenaraiHitam() throws Exception {	
		try {
			mp = new MyPersistence();
			MtnKontraktorSenaraiHitam senaraiHitam = (MtnKontraktorSenaraiHitam) mp.find(MtnKontraktorSenaraiHitam.class, getParam("idSenaraiHitam"));
			context.put("r", senaraiHitam);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("daftarBebasSenaraiHitam")
	public String daftarBebasSenaraiHitam() throws Exception {	
		try {
			mp = new MyPersistence();
			MtnKontraktorSenaraiHitam senaraiHitam = (MtnKontraktorSenaraiHitam) mp.find(MtnKontraktorSenaraiHitam.class, getParam("idSenaraiHitam"));
			context.put("r", senaraiHitam);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("simpanBebasSenaraiHitam")
	public String simpanBebasSenaraiHitam() throws Exception {		
		
		try {
			mp = new MyPersistence();
			Users pegawai = (Users) mp.find(Users.class, userId);
			MtnKontraktorSenaraiHitam senaraiHitam = (MtnKontraktorSenaraiHitam) mp.find(MtnKontraktorSenaraiHitam.class, getParam("idSenaraiHitam"));
			if (senaraiHitam != null) {
				mp.begin();
				senaraiHitam.setCatatan(getParam("catatanBebasSenaraiHitam"));
				senaraiHitam.setPegawaiBebas(pegawai);
				senaraiHitam.setTarikhBebas(new Date());
				senaraiHitam.setFlagAktif("T");
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return paparMaklumatSenaraiHitam();
	}
}

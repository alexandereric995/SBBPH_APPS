package bph.modules.integrasi;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.entities.integrasi.IntHRMIS;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.StatusPerkahwinan;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class HRMISRecordModule extends LebahRecordTemplateModule <IntHRMIS>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("HRMISRecordModule");
	private MyPersistence mp;
	
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntHRMIS r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		this.setReadonly(true);
		this.setDisableBackButton(true);
		
		this.setOrderBy("tarikhTerima");
		this.setOrderType("desc");
		
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public boolean delete(IntHRMIS r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/HRMIS";
	}

	@Override
	public Class<IntHRMIS> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntHRMIS.class;
	}

	@Override
	public void getRelatedData(IntHRMIS r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(IntHRMIS r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		
		m.put("noPengenalan", getParam("findNoPengenalan"));
		m.put("nama", getParam("findNama"));
		
		return m;
	}
	
	@Command("semakHRMIS")
	public String semakHRMIS() throws Exception {
		
		IntHRMIS hrmis = db.find(IntHRMIS.class, get("idHRMIS"));
		if (hrmis != null) {			
			
			try {
				System.out.println("VALIDATE HRMIS USING BPH WEB SERVICE");

				BPHServicesImplService service = new BPHServicesImplService();
				BPHServices bphService = service.getPort(BPHServices.class);
				// EXTRA CONTEXT
				String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
				int connectionTimeOutInMs = 10000;// 10 Second
				BindingProvider p = (BindingProvider) bphService;
				p.getRequestContext().put(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						WS_URL);
				myLogger.debug("requesting service from=> :" + bphService.toString());
				
				sbbph.ws.HrmisManager hrmisManagerService = null;
				
				hrmisManagerService = bphService.semakanPenjawatAwam(hrmis.getNoPengenalan());
				
				if (hrmisManagerService.isFlagSemakanHRMIS()) {
					context.put("success", "Y");
				} else {
					context.put("success", "T");
				}
			} catch (Exception ex) {
				System.out.println("ERROR ACCESS BPH WEB SERVICE : " + ex.getMessage());
			}			
		}
		
		try {
			mp = new MyPersistence();
			hrmis = (IntHRMIS) mp.find(IntHRMIS.class, hrmis.getId());
			if (hrmis != null) {
				updateUsers(hrmis, mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		context.put("r", hrmis);
		return getPath() + "/entry_page.vm";
	}
	
	private static void updateUsers(IntHRMIS hrmis, MyPersistence mp) {
		try {
			Users pengguna = (Users) mp.find(Users.class, hrmis.getNoPengenalan());
			if (pengguna != null) {
				if (hrmis != null) {
					mp.begin();
					if (hrmis.getKodGelaran() != null)
						pengguna.setGelaran((Gelaran) mp.find(Gelaran.class, hrmis.getKodGelaran()));
					if (hrmis.getNoTelefonPejabat() != null)	
						pengguna.setNoTelefonPejabat(hrmis.getNoTelefonPejabat());
					if (hrmis.getKodAgensi() != null)
						pengguna.setAgensi((Agensi) mp.find(Agensi.class, hrmis.getKodAgensi()));
					if (hrmis.getJabatan() != null)
						pengguna.setBahagian(hrmis.getJabatan());
					if (hrmis.getKodKelasPerkhidmatan() != null)
						pengguna.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, hrmis.getKodKelasPerkhidmatan()));
					if (hrmis.getKodGredPerkhidmatan() != null)
						pengguna.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, hrmis.getKodGredPerkhidmatan()));
					if (hrmis.getKodEtnik() != null) 
						pengguna.setEtnik((Etnik) mp.find(Etnik.class, hrmis.getKodEtnik()));
					if (hrmis.getKodStatusPerkahwinan() != null)
						pengguna.setStatusPerkahwinan((StatusPerkahwinan) mp.find(StatusPerkahwinan.class, hrmis.getKodStatusPerkahwinan()));
					pengguna.setFlagSemakanHRMIS("Y");
					
					// USER JOB
					boolean addJob = false;
					UsersJob job = (UsersJob) mp.get("select x from UsersJob x where x.users.id = '" + pengguna.getId() + "'");
					if (job == null) {
						addJob = true;
						job = new UsersJob();
						job.setUsers(pengguna);
					}
					job.setJawatan(pengguna.getJawatan());
					job.setGredJawatan(pengguna.getGredPerkhidmatan());
					job.setJenisPerkhidmatan(pengguna.getJenisPerkhidmatan());
					job.setKelasPerkhidmatan(pengguna.getKelasPerkhidmatan());
					job.setAgensi(pengguna.getAgensi());
					job.setBahagian(pengguna.getBahagian());
					job.setBadanBerkanun(pengguna.getBadanBerkanun());
					if (hrmis != null) {
						job.setTarikhLantikan(hrmis.getTarikhMulaSandang());
						job.setTarikhTamat(hrmis.getTarikhTamatSandang());
						job.setAlamat1(hrmis.getAlamatPejabat1());
						job.setAlamat2(hrmis.getAlamatPejabat2());
						job.setAlamat3(hrmis.getAlamatPejabat3());
						job.setPoskod(hrmis.getPoskodPejabat());
						if (hrmis.getKodBandarPejabat() != null)
							job.setBandar((Bandar) mp.find(Bandar.class, hrmis.getKodBandarPejabat()));
						job.setNoTelPejabat(hrmis.getNoTelefonPejabat());	
						job.setEmel(hrmis.getEmel());
					}
					if (addJob)
						mp.persist(job);
					
					// USER SPOUSE
					boolean addSpouse = false;
					UsersSpouse spouse = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + pengguna.getId() + "'");
					if (spouse == null) {
						addSpouse = true;
						spouse = new UsersSpouse();
						spouse.setUsers(pengguna);
					}
					// DATA DR HRMIS
					if (hrmis != null) {
						spouse.setNamaPasangan(hrmis.getNamaPasangan());
						spouse.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
						spouse.setNoKPPasangan(hrmis.getNoPengenalanPasangan());
						spouse.setNoTelBimbit(hrmis.getNoTelefonPasangan());
						spouse.setJenisPekerjaan(hrmis.getPekerjaanPasangan());
						spouse.setNamaSyarikat(hrmis.getMajikanPasangan());
					}
					if (addSpouse)
						mp.persist(spouse);	
					
					mp.commit();
				}						
			}
		} catch (Exception ex) {
			System.out.println("ERROR UPDATING USERS (" + hrmis.getNoPengenalan() + ") : " + ex.getMessage());
		}
	}
}

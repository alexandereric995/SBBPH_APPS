package bph.scheduler.portal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.db.Db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
import db.persistence.MyPersistence;

public class KemaskiniMaklumatPenjawatAwamJob implements Job {
	
	static Logger myLogger = Logger.getLogger("KemaskiniMaklumatPenjawatAwamJob");
	private MyPersistence mp;
	private Db lebahDb;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing KemaskiniMaklumatPenjawatAwamJob on : " + new Date());
		System.out.println("Executing KemaskiniMaklumatPenjawatAwamJob on : " + new Date());

		String sql = "";
		
		try {
			mp = new MyPersistence();
			
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();	
			
			sql = "SELECT no_pengenalan"
					+ " FROM int_hrmis"
					+ " GROUP BY no_pengenalan"
					+ " HAVING ( COUNT(no_pengenalan) > 1 )";			
			ResultSet rsDoubleNoPengenalan = stmt.executeQuery(sql);
			while (rsDoubleNoPengenalan.next()) {
				mp.begin();
				String noPengenalan = rsDoubleNoPengenalan.getString("no_pengenalan");
				List <IntHRMIS> listHRMIS = mp.list("select x from IntHRMIS x where x.noPengenalan = '" + noPengenalan + "' order by x.tarikhTerima desc");
				for (int i = 0; i <listHRMIS.size(); i++) {
					if (i > 0) {
						mp.remove(listHRMIS.get(i));
					}
				}
				mp.commit();
			}
			
			sql = "SELECT no_pengenalan"
					+ " FROM int_hrmis"
					+ " WHERE TIMESTAMPDIFF(month, tarikh_terima, now()) > 6"
					+ " ORDER BY no_pengenalan DESC";			
			ResultSet rsUpdateHrmis = stmt.executeQuery(sql);
			while (rsUpdateHrmis.next()) {
				
				String noPengenalan = rsUpdateHrmis.getString("no_pengenalan");
				if (noPengenalan != null) {
					updateHRMIS(noPengenalan, mp);
				}				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
			if (lebahDb != null) { lebahDb.close(); }
		}
		myLogger.info("Finish KemaskiniMaklumatPenjawatAwamJob on : " + new Date());
		System.out.println("Finish KemaskiniMaklumatPenjawatAwamJob on : " + new Date());
	}
	
	private static void updateHRMIS(String noPengenalan, MyPersistence mp) {
		try {
			BPHServicesImplService service = new BPHServicesImplService();
			BPHServices bphService = service.getPort(BPHServices.class);
			// EXTRA CONTEXT
			String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
			int connectionTimeOutInMs = 10000;// 10 Second
			BindingProvider p = (BindingProvider) bphService;
			p.getRequestContext().put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					WS_URL);
			
			sbbph.ws.HrmisManager hrmisManagerService = null;			
			hrmisManagerService = bphService.semakanPenjawatAwam(noPengenalan);
			if (hrmisManagerService.isFlagSemakanHRMIS()) {
				updateUsers(noPengenalan, mp);
			}
		} catch (Exception ex) {
			System.out.println("ERROR ACCESS BPH WEB SERVICE : " + ex.getMessage());
		}
	}

	private static void updateUsers(String noPengenalan, MyPersistence mp) {
		try {
			IntHRMIS hrmis = (IntHRMIS) mp
					.get("select x FROM IntHRMIS x where x.noPengenalan = '"
							+ noPengenalan + "' order by x.tarikhTerima desc");
			
			if(hrmis != null){
				Users pengguna = (Users) mp.find(Users.class, noPengenalan);
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
			}
		} catch (Exception ex) {
			System.out.println("ERROR UPDATING USERS (" + noPengenalan + ") : " + ex.getMessage());
		}
	}
}

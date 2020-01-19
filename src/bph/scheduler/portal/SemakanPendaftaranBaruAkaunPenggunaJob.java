package bph.scheduler.portal;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.util.PasswordService;

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
import bph.entities.integrasi.IntPESARA;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.StatusPerkahwinan;
import bph.mail.mailer.DaftarAkaunBaruMailer;
import db.persistence.MyPersistence;

public class SemakanPendaftaranBaruAkaunPenggunaJob implements Job {
	
	static Logger myLogger = Logger.getLogger("SemakanPendaftaranBaruAkaunPenggunaJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing SemakanPendaftaranBaruAkaunPenggunaJob on : " + new Date());
		System.out.println("Executing SemakanPendaftaranBaruAkaunPenggunaJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			
			//SEMAKAN HRMIS
			semakanPendaftaranBaruPenjawatAwam(mp);
			
			//SEMAKAN PESARA
			semakanPendaftaranBaruPesaraAwam(mp);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish SemakanPendaftaranBaruAkaunPenggunaJob on : " + new Date());
		System.out.println("Finish SemakanPendaftaranBaruAkaunPenggunaJob on : " + new Date());
	}
	
	private void semakanPendaftaranBaruPenjawatAwam(MyPersistence mp) {
		try {
			BPHServicesImplService service = new BPHServicesImplService();
			BPHServices bphService = service.getPort(BPHServices.class);
			// EXTRA CONTEXT
			String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
			int connectionTimeOutInMs = 10000;// 10 Second
			BindingProvider p = (BindingProvider) bphService;
			p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,WS_URL);
			sbbph.ws.HrmisManager hrmisManagerService = null;
			
			List<Users> listUsers = mp.list("select x from Users x where x.role.name = '(AWAM) Penjawat Awam' and x.flagDaftarSBBPH = 'Y' and x.flagSemakanHRMIS = 'T' and x.flagAktif = 'T' order by x.dateRegistered desc");
			for (int i = 0; i < listUsers.size(); i++) {
				Users users = listUsers.get(i);
				if (users != null) {
					hrmisManagerService = bphService.semakanPenjawatAwam(users.getId());
					if (hrmisManagerService.isFlagSemakanHRMIS()) {
						updatePendaftaranBaruPenjawatAwam(users, mp);
					} else if (hrmisManagerService.isFlagBukanPenjawatAwam()) {
						deleteUsers(users, mp);
						//GENERATE EMEL
						DaftarAkaunBaruMailer.get().gagalSemakanPenjawatAwam(users.getEmel());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	private void semakanPendaftaranBaruPesaraAwam(MyPersistence mp) {
		try {
			BPHServicesImplService service = new BPHServicesImplService();
			BPHServices bphService = service.getPort(BPHServices.class);
			// EXTRA CONTEXT
			String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
			int connectionTimeOutInMs = 10000;// 10 Second
			BindingProvider p = (BindingProvider) bphService;
			p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,WS_URL);
			sbbph.ws.PesaraManager pesaraManagerService = null;
			
			List<Users> listUsers = mp.list("select x from Users x where x.role.name = '(AWAM) Pesara' and x.flagDaftarSBBPH = 'Y' and x.flagSemakanPESARA = 'T' and x.flagAktif = 'T' order by x.dateRegistered desc");
			for (int i = 0; i < listUsers.size(); i++) {
				Users users = listUsers.get(i);
				if (users != null) {
					pesaraManagerService = bphService.semakanPesara(users.getId());
					if (pesaraManagerService.isFlagSemakanPESARA()) {
						updatePendaftaranBaruPesaraAwam(users, mp);
					} else if (pesaraManagerService.isFlagBukanPesara()) {
						deleteUsers(users, mp);
						//GENERATE EMEL
						DaftarAkaunBaruMailer.get().gagalSemakanPesaraAwam(users.getEmel());						
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	private void updatePendaftaranBaruPenjawatAwam(Users pengguna, MyPersistence mp) throws Exception {
		IntHRMIS hrmis = (IntHRMIS) mp.get("select x FROM IntHRMIS x where x.noPengenalan = '" + pengguna.getId() + "' order by x.tarikhTerima desc");	
		
		mp.begin();
		Random r = new Random();
		String c = "";
		String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < 7; i++) {
			c = c + alphabet.charAt(r.nextInt(alphabet.length()));
		}
		pengguna.setUserPassword(PasswordService.encrypt(c));
		if (hrmis != null) {
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
		}		
		pengguna.setFlagAktif("Y");
		pengguna.setFlagSemakanHRMIS("Y");
		
		// USER JOB
		boolean addJob = false;
		UsersJob job = (UsersJob) mp.get("select x from UsersJob x where x.users.id = '" + pengguna.getId() + "'");
		if (job == null) {
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
			spouse = new UsersSpouse();
			spouse.setUsers(pengguna);
		}
		// DATA DR HRMIS
		if (hrmis != null) {
			spouse.setNamaPasangan(hrmis.getNamaPasangan());
			spouse.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
			spouse.setNoKPPasangan(hrmis.getNoPengenalanPasangan());
		}
		if (addSpouse)
			mp.persist(spouse);		
		mp.commit();
		
		//GENERATE EMEL
		DaftarAkaunBaruMailer.get().selesaiSemakanPenjawatAwamPesaraAwam(pengguna.getEmel(), pengguna.getId(), c);
	}
	
	private void updatePendaftaranBaruPesaraAwam(Users pengguna, MyPersistence mp) throws Exception {
		IntPESARA pesara = (IntPESARA) mp.get("select x FROM IntPESARA x where x.noPengenalan = '" + pengguna.getId() + "' order by x.tarikhTerima desc");	
		
		mp.begin();
		Random r = new Random();
		String c = "";
		String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < 7; i++) {
			c = c + alphabet.charAt(r.nextInt(alphabet.length()));
		}
		pengguna.setUserPassword(PasswordService.encrypt(c));
		if (pesara != null) {
			if (pesara.getCawangan() != null)
				pengguna.setBahagian(pesara.getCawangan());
			if (pesara.getKodJawatan() != null)
				pengguna.setJawatan((Jawatan) mp.find(Jawatan.class, pesara.getKodJawatan()));
			if (pesara.getJawatanTerakhir() != null)
				pengguna.setKeteranganJawatan(pesara.getJawatanTerakhir());
			if (pesara.getKelasPerkhidmatan() != null)
				pengguna.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, pesara.getKelasPerkhidmatan()));
			if (pesara.getGredPerkhidmatan() != null)
				pengguna.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, pesara.getGredPerkhidmatan()));
		}
		
		pengguna.setFlagAktif("Y");
		pengguna.setFlagSemakanPESARA("Y");		
		mp.commit();
		
		//GENERATE EMEL
		DaftarAkaunBaruMailer.get().selesaiSemakanPenjawatAwamPesaraAwam(pengguna.getEmel(), pengguna.getId(), c);
	}
	
	private void deleteUsers(Users pengguna, MyPersistence mp) throws Exception {
		mp.begin();		
		mp.remove(pengguna);
		mp.commit();
	}
}

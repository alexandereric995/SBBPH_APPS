/*cara buat scheduler (28/9/2016)
1.create java dalam scr/bph/scheduler.
2.bukak properties/quartz_data.xml
3.guna contoh kod dibawah.
------------------
<schedule>
	<job>
	<name>GenerateKuartersSewaBulananJob</name>
		<job-class>bph.scheduler.kuarters.GenerateKuartersSewaBulananJob</job-class>
	</job>
	<trigger>
		<cron>
		<name>GenerateKuartersSewaBulananJob</name>
		<job-name>GenerateKuartersSewaBulananJob</job-name>
		<cron-expression>0 0/1 * 1/1 * ? *</cron-expression>
		</cron>
	</trigger>
</schedule>
---------------------
4.bukak www.cronmaker.com/ untuk create con expression.
5. untuk test-open web.xml (uncomment <!-- SCHEDULER START -->)
DONE*/

package bph.scheduler.kuarters;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.KodHasil;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import db.persistence.MyPersistence;

public class GenerateKuartersSewaBulananJob implements Job {
	static Logger myLogger = Logger.getLogger("GenerateKuartersSewaBulananJob");
	private MyPersistence mp;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing GenerateKuartersSewaBulananJob on : " + new Date());
		System.out.println("Executing GenerateKuartersSewaBulananJob on : " + new Date());	
		try {
			mp = new MyPersistence();
			@SuppressWarnings("unchecked")
			List<KuaPenghuni> listPenghuni = mp.list("select t from KuaPenghuni t where t.tarikhMasuk is not null and t.tarikhKeluar is null");
			for (int i = 0; i < listPenghuni.size(); i++) {
				KuaPenghuni penghuni = (KuaPenghuni) mp.find(KuaPenghuni.class, listPenghuni.get(i).getId());
				KuaKuarters kuarters=(KuaKuarters) mp.find(KuaKuarters.class, penghuni.getKuarters().getId());
				Double sewa=kuarters.getSewa();
				createRecordBayaran(penghuni,kuarters,sewa,mp);
			}
			System.out.println("test");			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish GenerateKuartersSewaBulananJob on : " + new Date());
		System.out.println("Finish GenerateKuartersSewaBulananJob on : " + new Date());
	}
	
	public void createRecordBayaran(KuaPenghuni penghuni,KuaKuarters kuarters,double sewa,MyPersistence mp) {
		try {
			Double kadarSewa = sewa;
			// create main ledger (sewa)
			KuaAkaun mn = new KuaAkaun();
			mn.setAmaunBayaranSeunit(kadarSewa);
			mn.setBilanganUnit(1);
			mn.setDebit(0d);
			mn.setKredit(0d);
			mn.setFlagBayar("T");
			mn.setFlagVoid("T");
			mn.setKeterangan("");
			mn.setCatatan("BAYARAN SEWAAN BULANAN KUARTERS");
			mn.setKodHasil((KodHasil) mp.find(KodHasil.class, "74201")); 
			mn.setNoInvois(mn.getId()); // TEMPORARY SET TO ID PERMOHONAN
			mn.setTarikhInvois(new Date());
			mn.setTarikhMasuk(new Date());
			mn.setTarikhKemaskini(new Date());
			mp.persist(mn);
			mp.begin();
			mp.commit();
			createInvoisInFinance(mn,mp);
		} catch (Exception e) {
			System.out.println("Error createRecordBayaran : " + e.getMessage());
		}
	}
	
	public void createInvoisInFinance(KuaAkaun ak,MyPersistence mp) {
		KewInvois inv = new KewInvois();
		inv.setFlagBayar("T");
		inv.setDebit(ak.getDebit());
		inv.setKredit(ak.getDebit());
		inv.setFlagBayaran("SEWA");
		inv.setFlagQueue("Y");
		inv.setIdLejar(ak.getId());
		try {
			inv.setJenisBayaran((KewJenisBayaran) mp.find(
					KewJenisBayaran.class, "01")); // 01 - KUARTERS
			inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
			inv.setKodHasil(ak.getKodHasil());
			inv.setNoInvois(ak.getNoInvois());
			// inv.setNoRujukan(ak.getPermohonan().getNoTempahan().toUpperCase());
			inv.setPembayar(ak.getPermohonan().getPemohon());
			inv.setTarikhInvois(ak.getTarikhInvois());
			inv.setTarikhDaftar(new Date());
			inv.setTarikhKemaskini(new Date());
			mp.persist(inv);
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error createInvoisInFinance : "
					+ e.getMessage());
		}
	}
}


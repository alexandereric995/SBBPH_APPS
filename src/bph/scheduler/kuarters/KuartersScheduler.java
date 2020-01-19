package bph.scheduler.kuarters;

import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaPenghuni;

public class KuartersScheduler {

	@SuppressWarnings("unchecked")
	public void autoGenerateSewa() throws Exception {
		System.out.println("running generate sewa kuarters...");

		DbPersistence db = new DbPersistence();
		
		List<KuaPenghuni> penghuni = db.list("SELECT x FROM KuaPenghuni x WHERE x.tarikhMasuk IS NOT NULL AND x.tarikhKeluar IS NULL");
		
		System.out.println("PENGHUNI SAIZ : " + penghuni.size());
	}
	
	@SuppressWarnings("unchecked")
	public void autoFixNoGiliran(String idLokasi, String kelasKuarters) throws Exception {
		System.out.println("Running AutoFix NoGiliran...");

		DbPersistence db = new DbPersistence();
		
		List<KuaAgihan> agihan = db.list("SELECT x FROM KuaAgihan x WHERE x.permohonan.lokasi.id = '" + idLokasi + "' AND x.kelasKuarters = '" + kelasKuarters + "' ORDER BY x.noGiliran ASC, x.permohonan.tarikhPermohonan ASC");
		
		System.out.println("START AUTOFIX");
		
		for (int i = 0; i < agihan.size(); i++) {
			KuaAgihan a = db.find(KuaAgihan.class, agihan.get(i).getId());
			int n = i + 1;
			
			a.setNoGiliran(n);
			
			db.begin();
			db.commit();
			
			double percent = ((n) * 100) / agihan.size();
			
			System.out.println("COMPLETION : " + n + "/" + agihan.size() + " " + percent + "%");
		}
		
		System.out.println("COMPLETE AUTOFIX (Total Data Fix) : " + agihan.size());
	}

}

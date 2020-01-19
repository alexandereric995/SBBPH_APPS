package bph.scheduler.kuarters;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.kod.KelasKuarters;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.qtr.KuaAgihan;
import db.persistence.MyPersistence;

public class AutoFixNoGiliranJob implements Job {
	static Logger myLogger = Logger.getLogger("AutoFixNoGiliranJob");
	private MyPersistence mp;
	double percent=0;
	String lokasi="";
	String kelas="";
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing AutoFixNoGiliranJob on : " + new Date());
		System.out.println("Executing AutoFixNoGiliranJob on : " + new Date());
		try {
			mp = new MyPersistence();
			@SuppressWarnings("unchecked")
			List<LokasiPermohonan> listLokasiPermohonan = mp.list("select x from LokasiPermohonan x where x.id in ('01','02','03','04','06') order by x.id ASC");
			@SuppressWarnings("unchecked")
			List<KelasKuarters> listKelasKuarters = mp.list("select x from KelasKuarters x where x.id in ('A','B','C','D','E','F','G') order by x.id ASC");
			for (int x = 0; x < listLokasiPermohonan.size(); x++) 
			{
				lokasi = listLokasiPermohonan.get(x).getId();
				
				for (int y = 0; y < listKelasKuarters.size(); y++) 
				{
					kelas = listKelasKuarters.get(y).getId();
					
					@SuppressWarnings("unchecked")
					List<KuaAgihan> listagihan = mp.list("SELECT x FROM KuaAgihan x WHERE x.idLokasi.id = '"+ lokasi + "' AND x.kelasKuarters = '"+ kelas+ "' AND x.status.id = '1419601227590' AND x.noGiliran <> 0 ORDER BY x.noGiliran ASC");
					for (int m = 0; m < listagihan.size(); m++) 
					{
						KuaAgihan a = (KuaAgihan)mp.find(KuaAgihan.class, listagihan.get(m).getId());
						int n = m + 1;
						a.setNoGiliran(n);
					}
					mp.begin();
					mp.commit();		
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish AutoFixNoGiliranJob on : " + new Date());
		System.out.println("Finish AutoFixNoGiliranJob on : " + new Date());
	}
}


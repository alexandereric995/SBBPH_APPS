package bph.modules.qtr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import portal.module.entity.UsersJob;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.VW_KuaAgihan;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaUtiliti extends LebahModule {

	private static final long serialVersionUID = -3874176134810226398L;
	//private KuartersUtils kUtils = new KuartersUtils();
	private DataUtil dataUtil;
	private DbPersistence db = new DbPersistence();
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/KuartersUtils");
	private MyPersistence mp;
	
	@Override
	public String start() {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectLokasiPermohonan", dataUtil.getListLokasiPermohonan());
		context.put("selectKelasKuarters", dataUtil.getListKelasKuarters());
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	public String getPath() {
		return "bph/modules/qtr/utiliti";
	}

	@Command("getAutoFixNoGiliran")
	public String getAutoFixNoGiliran() {
		String idLokasiPermohonan = getParam("idLokasiPermohonan");
		String idKelasKuarters = getParam("idKelasKuarters");
		if(!"".equalsIgnoreCase(idLokasiPermohonan) && !"".equalsIgnoreCase(idKelasKuarters)){
			autoFixNoGiliran(idLokasiPermohonan,idKelasKuarters);
		}else{
			autoFixNoGiliran();
		}
		return getPath() + "/autoFixNoGiliran.vm";
	}

	@SuppressWarnings("unchecked")
	public double autoFixNoGiliran(String idLokasi,String idKelas) {
		double percent = 0;
		boolean success=false;
		String lokasi=idLokasi;
		String kelas=idKelas;
		try {
			mp = new MyPersistence();
			List<KuaAgihan> listagihan = mp.list("SELECT x FROM KuaAgihan x WHERE x.idLokasi.id = '"+ lokasi + "' AND x.kelasKuarters = '"+ kelas+ "' AND x.status.id = '1419601227590' AND x.noGiliran <> 0 ORDER BY x.noGiliran ASC");
			for (int m = 0; m < listagihan.size(); m++) 
			{
				KuaAgihan a = (KuaAgihan)mp.find(KuaAgihan.class, listagihan.get(m).getId());
				int n = m + 1;
				a.setNoGiliran(n);
			}
			mp.begin();
			mp.commit();		
			percent=100;
			context.put("success","success" );		
		} catch (Exception e) {
			System.out.println("Error autoFixNoGiliran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		if(percent==100){
			success=true;
			context.put("success", success);
		}
		return percent;
	}
	
	@SuppressWarnings("unchecked")
	public Double autoFixNoGiliran() {
		double percent=0;
		String lokasi="";
		String kelas="";
		try {
			mp = new MyPersistence();
			List<LokasiPermohonan> listLokasiPermohonan = mp.list("select x from LokasiPermohonan x where x.id in ('01','02','03','04','06') order by x.id ASC");
			List<KelasKuarters> listKelasKuarters = mp.list("select x from KelasKuarters x where x.id in ('A','B','C','D','E','F','G') order by x.id ASC");
			for (int x = 0; x < listLokasiPermohonan.size(); x++) 
			{
				lokasi = listLokasiPermohonan.get(x).getId();
				
				for (int y = 0; y < listKelasKuarters.size(); y++) 
				{
					kelas = listKelasKuarters.get(y).getId();
					
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
			percent=100;
			if(percent==100)
			{
				context.put("success","success" );
			}
		} catch (Exception e) {
			System.out.println("Error autoFixNoGiliran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return percent;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public double autoHantarSenaraiMenunggu(String idLokasi) {
		double percent = 0;
		boolean success=false;
		boolean wujudKelasDowngrade = false;
		KuaPermohonan permohonan=null;
		UsersJob uj=null;
		KuaAgihan agihan=null;
		LokasiPermohonan lp=null;
		String statusPermohonanAsal="";
		KelasKuarters k1 = null;
		KelasKuarters k2 = null;
		
		try {
			mp = new MyPersistence();
			List<KuaPermohonan> x = mp.list("SELECT x FROM KuaPermohonan x WHERE x.lokasi.id = '"+ idLokasi + "' GROUP BY x.status");
			for (int f = 0; f < x.size(); f++) 
			{
				List<KuaPermohonan> listpermohonan = mp.list("SELECT x FROM KuaPermohonan x WHERE x.lokasi.id = '"+ idLokasi
					+ "' AND x.status.id = '1419483289678' GROUP BY x.id ORDER BY x.tarikhPermohonan ASC");								
				for (int i = 0; i < listpermohonan.size(); i++) 
				{		
					permohonan = (KuaPermohonan) mp.find(KuaPermohonan.class, listpermohonan.get(i).getId());
					uj = (UsersJob) mp.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '" + permohonan.getPemohon().getId() + "'");
					agihan = (KuaAgihan) mp.get("SELECT a FROM KuaAgihan a WHERE a.permohonan.id = '" + permohonan.getId() + "'");
					lp = (LokasiPermohonan) mp.find(LokasiPermohonan.class, permohonan.getLokasi().getId());

					if (uj != null) {
						String gredKelasKuarters = "";
						if (uj.getGredJawatan() != null) {
							gredKelasKuarters = uj.getGredJawatan().getKelasKuarters();
						}
						String gredKelasKuartersDowngrade = getKelasDowngrade(gredKelasKuarters);
						k1 = (KelasKuarters) mp.find(KelasKuarters.class, gredKelasKuarters);
						k2 = (KelasKuarters) mp.find(KelasKuarters.class, gredKelasKuartersDowngrade);
					}
					permohonan.setStatus((Status) mp.find(Status.class, "1419601227590"));
					mp.begin();
					mp.commit();
					
					List kelasKuarters = new ArrayList();
					if (k1 != null)
						kelasKuarters.add(k1.getId());

					if (agihan != null) {
						if (k2 != null) {
							if (agihan.getKelasKuarters().equals(k2.getId())) {
								wujudKelasDowngrade = true;
							}
						}
					}

					if (wujudKelasDowngrade == false) {
						if ("1".equals(permohonan.getFlagDowngrade())) {
							if (k2 != null)
								kelasKuarters.add(k2.getId());
						}
					}
					
					for (int j = 0; j < kelasKuarters.size(); j++) {
						String jenisKelasKuarters = "";
						KuaAgihan ka = new KuaAgihan();

						if (k2 != null) {
							if (k2.getId().equals(kelasKuarters.get(j).toString())) {
								jenisKelasKuarters = "D";
							} else {
								jenisKelasKuarters = "L";
							}
						} else {
							jenisKelasKuarters = "L";
						}
						ka.setPermohonan(permohonan);
						ka.setPemohon(permohonan.getPemohon());
						ka.setPekerjaan(uj);
						if(statusPermohonanAsal.equalsIgnoreCase("1431327994524"))
						{
							ka.setNoGiliran(1);
						}
						else
						{
							ka.setNoGiliran(getNoGiliran(kelasKuarters.get(j).toString(), permohonan.getLokasi().getId()));

						}
						ka.setStatus((Status) mp.find(Status.class, "1419601227590"));
						ka.setTarikhAgih(new Date());
						ka.setKelasKuarters(kelasKuarters.get(j).toString());
						ka.setJenisKelasKuarters(jenisKelasKuarters);
						ka.setIdLokasi(lp);
						mp.begin();
						mp.persist(ka);
						mp.commit();
						success = true;
					}
					//end loop
				}
				
			}
			
		} catch (Exception e) {
			System.out.println("Error autoFixNoGiliran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		if(percent==100){
			success=true;
			context.put("success", success);
		}
		return percent;
	}
	
	public int getNoGiliran(String kelasKuarters, String lokasi) {
		int i = 1;
		VW_KuaAgihan ka = (VW_KuaAgihan) mp.get("SELECT ka FROM VW_KuaAgihan ka WHERE ka.kelasKuarters = '"+ kelasKuarters + "' AND ka.idLokasi = '" + lokasi + "'");
		if (ka != null) {
			i = ka.getMaxNoGiliran() + 1;
		}
		return i;
	}
	
	public String getKelasDowngrade(String kelas) {
		String kelasDowngrade = "";
		if ("A".equals(kelas))
			kelasDowngrade = "";
		else if ("B".equals(kelas))
			kelasDowngrade = "";
		else if ("D".equals(kelas))
			kelasDowngrade = "F";
		else if ("E".equals(kelas))
			kelasDowngrade = "F";
		else
			kelasDowngrade = "";
		return kelasDowngrade;
	}

}

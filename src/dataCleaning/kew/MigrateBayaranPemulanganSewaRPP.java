package dataCleaning.kew;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kewangan.KewSubsidiariRPPMigrate;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppPermohonanBayaranBalik;

public class MigrateBayaranPemulanganSewaRPP {

	private static DbPersistence db;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		doJob();
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob() {		
		try {
			
			db = new DbPersistence();
			db.begin();
			
			List<KewSubsidiariRPPMigrate> listMigrate = db.list("select x from KewSubsidiariRPPMigrate x where x.flagMigrate = 'T'");
			for (KewSubsidiariRPPMigrate dataMigrate : listMigrate) {
				String msg = "";
				boolean success = false;
				Date tarikhTerimaPermohonanBayaranBalik = null;
				if (dataMigrate.getTarikhTerimaPermohonan() != null) {
					tarikhTerimaPermohonanBayaranBalik = sdf.parse(dataMigrate.getTarikhTerimaPermohonan());
				}
				
				KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + dataMigrate.getNoResit() + "'");
				if (resit != null) {
					if (resit.getIdPermohonan() != null) {
						RppPermohonan permohonan =(RppPermohonan) db.find(RppPermohonan.class, resit.getIdPermohonan());
						if (permohonan != null) {						
							
							boolean addPermohonanBayaranBalik = false;
							RppPermohonanBayaranBalik permohonanBayaranBalik = (RppPermohonanBayaranBalik) db.get("select x from RppPermohonanBayaranBalik x where x.permohonan.id = '" + permohonan.getId() + "'");
							if (permohonanBayaranBalik == null) {
								permohonanBayaranBalik = new RppPermohonanBayaranBalik();
								addPermohonanBayaranBalik = true;
							}
							permohonanBayaranBalik.setPermohonan(permohonan);
							permohonanBayaranBalik.setAmaun(Double.valueOf(dataMigrate.getJumlahBayaran()));
							permohonanBayaranBalik.setStatus((Status) db.find(Status.class, "1425259713427"));
							permohonanBayaranBalik.setTarikhPermohonan(tarikhTerimaPermohonanBayaranBalik);
							if(addPermohonanBayaranBalik){
								db.persist(permohonanBayaranBalik);
							}
							permohonan.setPermohonanBayaranBalik(permohonanBayaranBalik);
							
							boolean addSubsidiari = false;
							KewSubsidiari subsidiari = (KewSubsidiari) db.get("select x from KewSubsidiari x where x.idFail = '" + permohonan.getId() + "'");
							if (subsidiari == null) {
								subsidiari = new KewSubsidiari();
								addSubsidiari = true;
							}
							subsidiari.setIdFail(permohonan.getId());
							subsidiari.setJenisSubsidiari((KewJenisBayaran) db.find(KewJenisBayaran.class, "02"));
							subsidiari.setPemohon(permohonan.getPemohon());
							subsidiari.setStatus((Status) db.find(Status.class, "1436510785725")); //SUBSIDIARI TELAH DIPULANGKAN
							subsidiari.setTarikhPermohonan(tarikhTerimaPermohonanBayaranBalik);
							subsidiari.setFlagResitBayaran("Y");
							subsidiari.setFlagSalinanAkaunBank("Y");
							subsidiari.setFlagSuratTawaran("Y");
							subsidiari.setFlagSuratSokongan("Y");
							subsidiari.setNoBaucerBayaran(dataMigrate.getNoBaucer());
							if (dataMigrate.getTarikhBaucer() != null)
								subsidiari.setTarikhBaucer(sdf.parse(dataMigrate.getTarikhBaucer()));
							subsidiari.setNoEFT(dataMigrate.getNoEft());
							if (dataMigrate.getTarikhEft() != null)
								subsidiari.setTarikhEFT(sdf.parse(dataMigrate.getTarikhEft()));
							if(addSubsidiari){
								db.persist(subsidiari);
							}
							
							List<KewInvois> listInvois = db.list("select x from KewInvois x where x.pembayar.id = '" + permohonan.getPemohon().getId() + "'"
									+ " and x.jenisBayaran.id = '02' and x.noRujukan = '" + permohonan.getNoTempahan() + "'");
								
							for (KewInvois invois : listInvois) {
								invois.setAmaunPelarasan(Double.valueOf(dataMigrate.getJumlahBayaran()));
								if(invois.getKeteranganBayaran().contains("TAMBAHAN") || invois.getKeteranganBayaran().contains("BOT")){
									invois.setAmaunPelarasan(0d);
								}		
								invois.setCatatanPelarasan("BAYARAN BALIK SUBSIDIARI");
								invois.setUserKemaskini(db.find(Users.class, "faizal"));
							}
							
							success = true;
						} else {
							msg = "PERMOHONAN TIDAK WUJUD";
						}
					}  else {
						RppAkaun akaun = (RppAkaun) db.get("select x from RppAkaun x where x.noResit = '" + resit.getNoResit() + "'");
						if (akaun != null) {
							if (akaun.getPermohonan() != null) {
								RppPermohonan permohonan = akaun.getPermohonan();
								boolean addPermohonanBayaranBalik = false;
								RppPermohonanBayaranBalik permohonanBayaranBalik = (RppPermohonanBayaranBalik) db.get("select x from RppPermohonanBayaranBalik x where x.permohonan.id = '" + permohonan.getId() + "'");
								if (permohonanBayaranBalik == null) {
									permohonanBayaranBalik = new RppPermohonanBayaranBalik();
									addPermohonanBayaranBalik = true;
								}
								permohonanBayaranBalik.setPermohonan(permohonan);
								permohonanBayaranBalik.setAmaun(Double.valueOf(dataMigrate.getJumlahBayaran()));
								permohonanBayaranBalik.setStatus((Status) db.find(Status.class, "1425259713427"));
								permohonanBayaranBalik.setTarikhPermohonan(tarikhTerimaPermohonanBayaranBalik);
								if(addPermohonanBayaranBalik){
									db.persist(permohonanBayaranBalik);
								}
								permohonan.setPermohonanBayaranBalik(permohonanBayaranBalik);
								
								boolean addSubsidiari = false;
								KewSubsidiari subsidiari = (KewSubsidiari) db.get("select x from KewSubsidiari x where x.idFail = '" + permohonan.getId() + "'");
								if (subsidiari == null) {
									subsidiari = new KewSubsidiari();
									addSubsidiari = true;
								}
								subsidiari.setIdFail(permohonan.getId());
								subsidiari.setJenisSubsidiari((KewJenisBayaran) db.find(KewJenisBayaran.class, "02"));
								subsidiari.setPemohon(permohonan.getPemohon());
								subsidiari.setStatus((Status) db.find(Status.class, "1436510785725")); //SUBSIDIARI TELAH DIPULANGKAN
								subsidiari.setTarikhPermohonan(tarikhTerimaPermohonanBayaranBalik);
								subsidiari.setFlagResitBayaran("Y");
								subsidiari.setFlagSalinanAkaunBank("Y");
								subsidiari.setFlagSuratTawaran("Y");
								subsidiari.setFlagSuratSokongan("Y");
								subsidiari.setNoBaucerBayaran(dataMigrate.getNoBaucer());
								subsidiari.setTarikhBaucer(sdf.parse(dataMigrate.getTarikhBaucer()));
								subsidiari.setNoEFT(dataMigrate.getNoEft());
								subsidiari.setTarikhEFT(sdf.parse(dataMigrate.getTarikhEft()));
								if(addSubsidiari){
									db.persist(subsidiari);
								}
								
								List<KewInvois> listInvois = db.list("select x from KewInvois x where x.pembayar.id = '" + permohonan.getPemohon().getId() + "'"
										+ " and x.jenisBayaran.id = '02' and x.noRujukan = '" + permohonan.getNoTempahan() + "'");
									
								for (KewInvois invois : listInvois) {
									invois.setAmaunPelarasan(Double.valueOf(dataMigrate.getJumlahBayaran()));
									if(invois.getKeteranganBayaran().contains("TAMBAHAN") || invois.getKeteranganBayaran().contains("BOT")){
										invois.setAmaunPelarasan(0d);
									}		
									invois.setCatatanPelarasan("BAYARAN BALIK SUBSIDIARI");
									invois.setUserKemaskini(db.find(Users.class, "faizal"));
								}
								
								success = true;
							} else {
								msg = "PERMOHONAN TIDAK WUJUD";
							}
						} else {
							msg = "RPP_AKAUN TIADA REKOD";
						}
					}
				} else {
					msg = "RESIT TIDAK WUJUD";
				}
				
				if (success) {
					dataMigrate.setFlagMigrate("Y");
				} else {
					dataMigrate.setFlagMigrate("T");
					dataMigrate.setMsg(msg);
				}				
			}	
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

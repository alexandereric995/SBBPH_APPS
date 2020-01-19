package dataCleaning.kew;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewDepositMigrate;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.Status;

public class MigrateBayaranPemulanganDeposit {

	private static DbPersistence db;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		doJob();
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob() {
		String msg = "";
		boolean success = false;
		try {
			db = new DbPersistence();
			db.begin();
			
			List<KewDepositMigrate> listDepositMigrate = db.list("select x from KewDepositMigrate x where x.flagMigrate = 'T' order by x.id asc");
			for (KewDepositMigrate depositMigrate : listDepositMigrate) {
				success = false;
				
				String noResit = depositMigrate.getNoResit();
				String tarikhBayaranString = depositMigrate.getTarikhBayaran();
				String noBaucerBayaran = depositMigrate.getNoBaucerBayaran();
				Double amaunBayaran = Double.valueOf(depositMigrate.getAmaunBayaran());
								
				List<KewDeposit> listDeposit = db.list("select x from KewDeposit x where x.noResit = '" + noResit + "'");
				if (listDeposit.size() > 0) {
					if (listDeposit.size() > 1) {
						msg = "REKOD DEPOSIT WUJUD LEBIH DARI 1";
					} else {
						KewDeposit deposit = listDeposit.get(0);
						if (deposit != null) {
							if (deposit.getTuntutanDeposit() != null) {
								
								Date tarikhBayaran = sdf.parse(tarikhBayaranString);
								deposit.setTarikhPulanganDeposit(tarikhBayaran);
								deposit.setFlagPulangDeposit("Y");
								deposit.setBakiDeposit(0D);
								deposit.setTarikhBayaran(tarikhBayaran);
								deposit.setFlagWarta("T");
								deposit.setFlagBayar("Y");
								deposit.setFlagQueue("T");
								
								deposit.getTuntutanDeposit().setStatus(db.find(Status.class, "1436464445695"));
								deposit.getTuntutanDeposit().setSuratPengesahanDeposit("Y");
								deposit.getTuntutanDeposit().setNoBaucerPulanganDeposit(noBaucerBayaran);
								deposit.getTuntutanDeposit().setAmaunPelarasanDeposit(amaunBayaran);
								deposit.getTuntutanDeposit().setTarikhBaucer(tarikhBayaran);
								
								success = true;
							} else {
								KewTuntutanDeposit tuntutanDeposit = (KewTuntutanDeposit) db.get("select x from KewTuntutanDeposit x where x.deposit.id = '" + deposit.getId() + "'");
								if (tuntutanDeposit != null) {
									deposit.setTuntutanDeposit(tuntutanDeposit);
									
									Date tarikhBayaran = sdf.parse(tarikhBayaranString);
									deposit.setTarikhPulanganDeposit(tarikhBayaran);
									deposit.setFlagPulangDeposit("Y");
									deposit.setBakiDeposit(0D);
									deposit.setTarikhBayaran(tarikhBayaran);
									deposit.setFlagWarta("T");
									deposit.setFlagBayar("Y");
									deposit.setFlagQueue("T");
									
									deposit.getTuntutanDeposit().setStatus(db.find(Status.class, "1436464445695"));
									deposit.getTuntutanDeposit().setSuratPengesahanDeposit("Y");
									deposit.getTuntutanDeposit().setNoBaucerPulanganDeposit(noBaucerBayaran);
									deposit.getTuntutanDeposit().setAmaunPelarasanDeposit(amaunBayaran);
									deposit.getTuntutanDeposit().setTarikhBaucer(tarikhBayaran);
									
									success = true;
								} else {
									msg = "REKOD TUNTUTAN DEPOSIT TIDAK WUJUD";
								}								
							}
						} else {
							msg = "MAKLUMAT DEPOSIT IS NULL";
						}
					}
				} else {
					KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.noResit = '" + noResit + "'");
					if (resit != null) {
						if ("2".equals(resit.getFlagJenisResit())) {
							msg = "RESIT ADALAH RESIT SEWA";
						} else {
							List<KewResitSenaraiInvois> listRSI = db.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "' and x.deposit.id != null and x.deposit.id != ''");
							if (listRSI.size() > 0) {
								for (KewResitSenaraiInvois rsi : listRSI) {
									KewDeposit deposit = rsi.getDeposit();
									if (deposit.getTuntutanDeposit() != null) {
										
										Date tarikhBayaran = sdf.parse(tarikhBayaranString);
										deposit.setTarikhPulanganDeposit(tarikhBayaran);
										deposit.setFlagPulangDeposit("Y");
										deposit.setBakiDeposit(0D);
										deposit.setTarikhBayaran(tarikhBayaran);
										deposit.setFlagWarta("T");
										deposit.setFlagBayar("Y");
										deposit.setFlagQueue("T");
										
										deposit.getTuntutanDeposit().setStatus(db.find(Status.class, "1436464445695"));
										deposit.getTuntutanDeposit().setSuratPengesahanDeposit("Y");
										deposit.getTuntutanDeposit().setNoBaucerPulanganDeposit(noBaucerBayaran);
										deposit.getTuntutanDeposit().setAmaunPelarasanDeposit(amaunBayaran);
										deposit.getTuntutanDeposit().setTarikhBaucer(tarikhBayaran);
										
										success = true;
									} else {
										KewTuntutanDeposit tuntutanDeposit = (KewTuntutanDeposit) db.get("select x from KewTuntutanDeposit x where x.deposit.id = '" + deposit.getId() + "'");
										if (tuntutanDeposit != null) {
											deposit.setTuntutanDeposit(tuntutanDeposit);
											
											Date tarikhBayaran = sdf.parse(tarikhBayaranString);
											deposit.setTarikhPulanganDeposit(tarikhBayaran);
											deposit.setFlagPulangDeposit("Y");
											deposit.setBakiDeposit(0D);
											deposit.setTarikhBayaran(tarikhBayaran);
											deposit.setFlagWarta("T");
											deposit.setFlagBayar("Y");
											deposit.setFlagQueue("T");
											
											deposit.getTuntutanDeposit().setStatus(db.find(Status.class, "1436464445695"));
											deposit.getTuntutanDeposit().setSuratPengesahanDeposit("Y");
											deposit.getTuntutanDeposit().setNoBaucerPulanganDeposit(noBaucerBayaran);
											deposit.getTuntutanDeposit().setAmaunPelarasanDeposit(amaunBayaran);
											deposit.getTuntutanDeposit().setTarikhBaucer(tarikhBayaran);
											
											success = true;
										} else {
											msg = "REKOD TUNTUTAN DEPOSIT TIDAK WUJUD";
										}								
									}
								}
							} else {
								msg = "TIADA REKOD DI KEW_RESIT_SENARAI_INVOIS";
							}
						}					
					} else {
						msg = "RESIT TIDAK WUJUD DI KEW_BAYARAN_RESIT";
					}					
				}
				
				if (success) {
					depositMigrate.setFlagMigrate("Y");
					depositMigrate.setMsg(null);
				} else {
					depositMigrate.setFlagMigrate("T");
					depositMigrate.setMsg(msg);
				}
			}				
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

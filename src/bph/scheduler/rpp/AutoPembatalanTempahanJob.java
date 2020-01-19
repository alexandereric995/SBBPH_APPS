package bph.scheduler.rpp;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.FPXRecordsRequest;
import bph.entities.rpp.RppPermohonan;
import bph.integrasi.fpx.FPXUtil;
import bph.mail.mailer.RppMailer;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class AutoPembatalanTempahanJob implements Job {

	static Logger myLogger = Logger.getLogger("AutoPembatalanTempahanJob");
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		myLogger.info("Executing AutoPembatalanTempahanJob on : " + new Date());
		System.out.println("Executing AutoPembatalanTempahanJob on : " + new Date());

		Boolean belumBayar = true;
		try {
			mp = new MyPersistence();

			String today = Util.getCurrentDate("yyyy-MM-dd");
			String hql = "select x from RppPermohonan x where x.jenisPemohon = 'INDIVIDU' and x.jenisPermohonan = 'ONLINE' and x.rppPeranginan.jenisPeranginan.id <> 'RP' "
					+ " and x.statusBayaran = 'T' and x.status.id = '1425259713412' and x.pemohon.id not in ('anon','faizal') and (x.tarikhAkhirBayaran < '" + today + "' or x.tarikhMasukRpp <= '" + today + "')";
			List<RppPermohonan> listbatal = mp.list(hql);

			for (RppPermohonan r : listbatal) {
				belumBayar = true;				
				belumBayar = reCheckPaymentStatus(mp, r.getId());
				
				if (belumBayar) {
					mp.begin(); 
					UtilRpp.batalPermohonan(mp, r);
					mp.commit();
					emailtoGuest(r);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		myLogger.info("Finish AutoPembatalanTempahanJob on : " + new Date());
		System.out.println("Finish AutoPembatalanTempahanJob on : " + new Date());
	}

	private Boolean reCheckPaymentStatus(MyPersistence mp, String idPermohonan) {
		boolean bool = true;
		
		try {
			//REQUERY BASED ON NO RESPONSE DURING TRANSACTION.
			requeryNoResponseTransaction(mp, idPermohonan);
			
			//REQUERY BASED ON DEBITAUTHCODE = 09 (TRANSACTION PENDING).
			requeryPendingTransaction(mp, idPermohonan);
			
			//QUERY DATA FPXRECORDS - CHECK PAYMENT SUCCESS(00) / PENDING(09)
			FPXRecords fpxRecords = (FPXRecords) mp.get("select x from FPXRecords x where x.debitAuthCode in ('00', '09') and x.sellerOrderNo = '" + idPermohonan + "' order by x.id asc");
			if (fpxRecords != null) {
				bool = false;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return bool;
	}

	private void requeryNoResponseTransaction(MyPersistence mp, String idPermohonan) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;			
			while (loopRequery < 3) {
				List<FPXRecordsRequest> listFPXRecordRequest = mp.list("select x from FPXRecordsRequest x where (x.fpxTxnId is null or x.fpxTxnId = '') and x.sellerOrderNo = '" + idPermohonan + "' order by x.id asc");
				if (listFPXRecordRequest.size() > 0) {
					for (FPXRecordsRequest fpxRecordRequest : listFPXRecordRequest) {
						FPXRecords fpxMyClear = fpxUtil.reQueryFPX(fpxRecordRequest.getSellerOrderNo(), fpxRecordRequest.getSellerExOrderNo(), fpxRecordRequest.getTxnAmount());
						mp.begin();
						if (fpxMyClear != null) {	
							boolean addRecord = false;
							FPXRecords fpxRecords = (FPXRecords) mp.find(FPXRecords.class, fpxMyClear.getId());
							if (fpxRecords == null) {
								fpxRecords = new FPXRecords();
								fpxRecords.setId(fpxMyClear.getId());
								addRecord = true;
							}
							fpxRecords.setBuyerBankBranch(fpxMyClear.getBuyerBankBranch());
							fpxRecords.setBuyerBankId(fpxMyClear.getBuyerBankId());
							fpxRecords.setBuyerIban(fpxMyClear.getBuyerIban());
							fpxRecords.setBuyerId(fpxMyClear.getBuyerId());
							fpxRecords.setBuyerName(fpxMyClear.getBuyerName());
							fpxRecords.setCreditAuthCode(fpxMyClear.getCreditAuthCode());
							fpxRecords.setCreditAuthNo(fpxMyClear.getCreditAuthNo());
							fpxRecords.setDebitAuthCode(fpxMyClear.getDebitAuthCode());
							fpxRecords.setDebitAuthNo(fpxMyClear.getDebitAuthNo());
							fpxRecords.setFpxTxnTime(fpxMyClear.getFpxTxnTime());
							fpxRecords.setMakerName(fpxMyClear.getMakerName());
							fpxRecords.setMsgToken(fpxMyClear.getMsgToken());
							fpxRecords.setMsgType(fpxMyClear.getMsgType());
							fpxRecords.setSellerExId(fpxMyClear.getSellerExId());
							fpxRecords.setSellerExOrderNo(fpxMyClear.getSellerExOrderNo());
							fpxRecords.setSellerId(fpxMyClear.getSellerId());
							fpxRecords.setSellerOrderNo(fpxMyClear.getSellerOrderNo());
							fpxRecords.setSellerTxnTime(fpxMyClear.getSellerTxnTime());
							fpxRecords.setTxnAmount(fpxMyClear.getTxnAmount());
							fpxRecords.setTxnCurrency(fpxMyClear.getTxnCurrency());
							if (addRecord) {
								mp.persist(fpxRecords);							
							}
							fpxRecordRequest.setFpxTxnId(fpxMyClear.getId());
							fpxRecordRequest.setRespondDate(new Date());
						} else {
							if (fpxUtil.isSuccessRequery()) {
								int daysBetween = Util.daysBetween(fpxRecordRequest.getRequestDate(), new Date());
								if (daysBetween >= 2) {
									mp.remove(fpxRecordRequest);
								}
							}
						}
						mp.commit();
					}
					loopRequery++;
				} else {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	private void requeryPendingTransaction(MyPersistence mp, String idPermohonan) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;			
			while (loopRequery < 3) {
				List<FPXRecords> listFPXRecords = mp.list("select x from FPXRecords x where x.debitAuthCode = '09' and x.sellerOrderNo = '" + idPermohonan + "' order by x.id asc");
				if (listFPXRecords.size() > 0) {
					for (FPXRecords fpxRecords : listFPXRecords) {
						FPXRecords fpxMyClear = fpxUtil.reQueryFPX(fpxRecords.getSellerOrderNo(), fpxRecords.getSellerExOrderNo(), fpxRecords.getTxnAmount());
						mp.begin();
						if (fpxMyClear != null) {	
							boolean addRecord = false;
							FPXRecords newFpxRecords= (FPXRecords) mp.find(FPXRecords.class, fpxMyClear.getId());
							if (newFpxRecords == null) {
								newFpxRecords = new FPXRecords();
								newFpxRecords.setId(fpxMyClear.getId());
								addRecord = true;
							}
							newFpxRecords.setBuyerBankBranch(fpxMyClear.getBuyerBankBranch());
							newFpxRecords.setBuyerBankId(fpxMyClear.getBuyerBankId());
							newFpxRecords.setBuyerIban(fpxMyClear.getBuyerIban());
							newFpxRecords.setBuyerId(fpxMyClear.getBuyerId());
							newFpxRecords.setBuyerName(fpxMyClear.getBuyerName());
							newFpxRecords.setCreditAuthCode(fpxMyClear.getCreditAuthCode());
							newFpxRecords.setCreditAuthNo(fpxMyClear.getCreditAuthNo());
							newFpxRecords.setDebitAuthCode(fpxMyClear.getDebitAuthCode());
							newFpxRecords.setDebitAuthNo(fpxMyClear.getDebitAuthNo());
							newFpxRecords.setFpxTxnTime(fpxMyClear.getFpxTxnTime());
							newFpxRecords.setMakerName(fpxMyClear.getMakerName());
							newFpxRecords.setMsgToken(fpxMyClear.getMsgToken());
							newFpxRecords.setMsgType(fpxMyClear.getMsgType());
							newFpxRecords.setSellerExId(fpxMyClear.getSellerExId());
							newFpxRecords.setSellerExOrderNo(fpxMyClear.getSellerExOrderNo());
							newFpxRecords.setSellerId(fpxMyClear.getSellerId());
							newFpxRecords.setSellerOrderNo(fpxMyClear.getSellerOrderNo());
							newFpxRecords.setSellerTxnTime(fpxMyClear.getSellerTxnTime());
							newFpxRecords.setTxnAmount(fpxMyClear.getTxnAmount());
							newFpxRecords.setTxnCurrency(fpxMyClear.getTxnCurrency());
							if (addRecord) {
								mp.persist(newFpxRecords);	
								mp.remove(fpxRecords);
							}
						} else {
							if (fpxUtil.isSuccessRequery()) {
								mp.remove(fpxRecords);
							}
						}
						mp.commit();
					}
					loopRequery++;
				} else {
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	/**
	 * EMAIL TO GUEST - EMAIL PEMBERITAHUAN PERMOHONAN DIBATALKAN
	 * */
	public static void emailtoGuest(RppPermohonan r) {
		if (r.getPemohon() != null) {
			if ((!r.getPemohon().getId().equalsIgnoreCase("faizal") && !r.getPemohon().getId().equalsIgnoreCase("anon"))
					&& ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")) {
				if (r.getPemohon().getEmel() != null) {
					if (!r.getPemohon().getEmel().equalsIgnoreCase("")) {
						RppMailer.get().notifikasiPembatalanTempahan(r.getPemohon().getEmel(), r);
					}
				}
			}
		} else {
			myLogger.info("AutoPembatalanTempahanJob PEMOHON IS NULL : ID_PERMOHONAN = " + r.getId());
		}
	}// close emailtoGuest

}

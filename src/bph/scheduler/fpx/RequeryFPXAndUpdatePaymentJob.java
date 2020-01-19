package bph.scheduler.fpx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.FPXRecordsRequest;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.utiliti.UtilPermohonan;
import bph.integrasi.fpx.FPXUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class RequeryFPXAndUpdatePaymentJob implements Job {
	
	static Logger myLogger = Logger.getLogger("RequeryFPXAndUpdatePaymentJob");
	private MyPersistence mp;
	UtilFPX utilFPX = new UtilFPX();

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {
		myLogger.info("Executing RequeryFPXAndUpdatePaymentJob on : " + new Date());
		System.out.println("Executing RequeryFPXAndUpdatePaymentJob on : " + new Date());
		
		try {
			mp = new MyPersistence();
			
			//REQUERY BASED ON NO RESPONSE DURING TRANSACTION.
			requeryNoResponseTransaction(mp);
			
			//REQUERY BASED ON DEBITAUTHCODE = 09 (TRANSACTION PENDING).
			requeryPendingTransaction(mp);
			
			//FLAG MODUL IS NULL FOR DEBITAUTHCODE != 00
			updateFlagModul(mp);	
			
			//UPDATE STATUS BAYARAN RPP
			updateStatusBayaranRPP(mp);
			
			//UPDATE STATUS BAYARAN LONDON
			updateStatusBayaranLondon(mp);
			
			//UPDATE STATUS BAYARAN DEWAN GELANGGANG
			updateStatusBayaranDewanGelanggang(mp);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null)
				if (mp != null) { mp.close(); }
		}
		myLogger.info("Finish RequeryFPXAndUpdatePaymentJob on : " + new Date());
		System.out.println("Finish RequeryFPXAndUpdatePaymentJob on : " + new Date());
	}	

	private void requeryNoResponseTransaction(MyPersistence mp) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;			
			while (loopRequery < 3) {
				List<FPXRecordsRequest> listFPXRecordRequest = mp.list("select x from FPXRecordsRequest x where (x.fpxTxnId is null or x.fpxTxnId = '') order by x.id asc");
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
	
	private void requeryPendingTransaction(MyPersistence mp) {
		FPXUtil fpxUtil = new FPXUtil();
		try {
			int loopRequery = 0;			
			while (loopRequery < 3) {
				List<FPXRecords> listFPXRecords = mp.list("select x from FPXRecords x where x.debitAuthCode = '09' order by x.id asc");
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
	
	private void updateFlagModul(MyPersistence mp) {
		try {
			mp.begin();
			List<FPXRecords> listFPXRecords = mp.list("select x from FPXRecords x where x.debitAuthCode not in ('00') and (x.flagModul = '' or x.flagModul = 'null' or x.flagModul is null) order by x.id asc");
			for (int i = 0; i < listFPXRecords.size(); i++) {
				FPXRecords fpxRecords = listFPXRecords.get(i);
				
				UtilPermohonan util = (UtilPermohonan) mp.find(UtilPermohonan.class, fpxRecords.getSellerOrderNo());
				if (util != null) {
					fpxRecords.setFlagModul("UTIL");
				}
				
				RppPermohonan rpp = (RppPermohonan) mp.find(RppPermohonan.class, fpxRecords.getSellerOrderNo());
				if (rpp != null) {
					fpxRecords.setFlagModul("IR");
				}
				
				RppRekodTempahanLondon london = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, fpxRecords.getSellerOrderNo());
				if (london != null) {
					fpxRecords.setFlagModul("LONDON");
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	private void updateStatusBayaranRPP(MyPersistence mp) {
		try {
			mp.begin();
			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			List<RppPermohonan> listPermohonan = mp.list("select x from RppPermohonan x where x.statusBayaran = 'T' and x.jenisPermohonan = 'ONLINE'"
					+ " and x.jenisPemohon = 'INDIVIDU' and x.rppPeranginan.id not in ('3', '11', '12', '13', '14')"
					+ " and x.status.id not in (1435093978588, 1688545253001455, 1430809277099, 1435512646303, 1430809277096, 1425259713415)"
					+ " and x.tarikhMasukRpp >= '" + today + "' order by x.id asc");
			for (int i = 0; i < listPermohonan.size(); i++) {
				RppPermohonan permohonan = listPermohonan.get(i);
				if (permohonan != null) {
					FPXRecords fpxRecords = (FPXRecords) mp.get("select x from FPXRecords x where x.sellerOrderNo = '" + permohonan.getId() + "' and x.debitAuthCode = '00' and x.flagManagePayment != 'Y'");
					if (fpxRecords != null) {
						if (fpxRecords.getFpxTxnTime() != null) {
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(0, 4)));
							cal.set(Calendar.MONTH, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(4, 6)) - 1);
							cal.set(Calendar.DATE, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(6, 8)));
							
							utilFPX.transaksiBayarTempahan(fpxRecords.getId(), permohonan, cal.getTime(), mp); // fpx
							fpxRecords.setFlagModul("IR");
							fpxRecords.setFlagManagePayment("Y");							
						}						
					}
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateStatusBayaranLondon(MyPersistence mp) {
		try {
			mp.begin();
			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			List<RppRekodTempahanLondon> listPermohonan = mp.list("select x from RppRekodTempahanLondon x where x.flagBayar = 'T' and x.tarikhMasukRpp >= '" + today + "' order by x.id asc");
			for (int i = 0; i < listPermohonan.size(); i++) {
				RppRekodTempahanLondon permohonan = listPermohonan.get(i);
				if (permohonan != null) {
					FPXRecords fpxRecords = (FPXRecords) mp.get("select x from FPXRecords x where x.sellerOrderNo = '" + permohonan.getId() + "' and x.debitAuthCode = '00' and x.flagManagePayment != 'Y'");
					if (fpxRecords != null) {
						if (fpxRecords.getFpxTxnTime() != null) {
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(0, 4)));
							cal.set(Calendar.MONTH, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(4, 6)) - 1);
							cal.set(Calendar.DATE, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(6, 8)));							
							
							utilFPX.transaksiBayarTempahanLondon(fpxRecords.getId(), permohonan, cal.getTime(), mp); // fpx
							fpxRecords.setFlagModul("LONDON");
							fpxRecords.setFlagManagePayment("Y");							
						}
					}
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateStatusBayaranDewanGelanggang(MyPersistence mp) {
		try {
			mp.begin();
			List<UtilPermohonan> listPermohonan = mp.list("select x from UtilPermohonan x where x.statusBayaran = 'T' and x.statusPermohonan != 'B' order by x.id asc");
			for (int i = 0; i < listPermohonan.size(); i++) {
				UtilPermohonan permohonan = listPermohonan.get(i);
				if (permohonan != null) {
					FPXRecords fpxRecords = (FPXRecords) mp.get("select x from FPXRecords x where x.sellerOrderNo = '" + permohonan.getId() + "' and x.debitAuthCode = '00' and x.flagManagePayment != 'Y'");
					if (fpxRecords != null) {
						if (fpxRecords.getFpxTxnTime() != null) {
							Calendar cal = Calendar.getInstance();
							cal.set(Calendar.YEAR, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(0, 4)));
							cal.set(Calendar.MONTH, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(4, 6)) - 1);
							cal.set(Calendar.DATE, Integer.valueOf(fpxRecords.getFpxTxnTime().substring(6, 8)));							
							
							utilFPX.transaksiBayarTempahanDewanGelanggang(fpxRecords.getId(), permohonan, cal.getTime(), mp); // fpx
							fpxRecords.setFlagModul("UTIL");
							fpxRecords.setFlagManagePayment("Y");							
						}
					}
				}
			}	
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

package bph.utils;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.integrasi.FPXRecords;
import bph.entities.integrasi.MIGSRecords;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppUnit;
import db.persistence.MyPersistence;

/**
 * UTK IR SAHAJA 
 * */
public class KemaskiniRekodBayaran extends AjaxBasedModule {
	
	private static final long serialVersionUID = 1L;
	private static String path = "bph/utils/kemaskiniRekodBayaran/";
	static Logger myLogger = Logger.getLogger(KemaskiniRekodBayaran.class);
	private MyPersistence mp;
	protected DbPersistence db;
	private DataUtil dataUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	public String doTemplate2() throws Exception {
		myLogger.info(".:KemaskiniRekodBayaran:.");

		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		myLogger.info("command : "+command);
		
		db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		
		String userIdLogin = (String) request.getSession().getAttribute("_portal_login");
		
		if(command.equalsIgnoreCase("getResultCarian")){
		
			try {
				mp = new MyPersistence();
				
				String carianPermohonan = getParam("carianPermohonan");
				List<RppPermohonan> listPermohonan = searchPermohonan(mp,carianPermohonan);
				context.put("listPermohonan", listPermohonan);
			
			} catch (Exception e) {
				System.out.println("Error[getResultCarian] : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			vm = path + "/senaraiPermohonan.vm";
			
		}else if(command.equalsIgnoreCase("getMaklumatBayaran")){

			String idpermohonan = getParam("idpermohonan");
			
			try {
				mp = new MyPersistence();
				
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idpermohonan);
				context.put("r", r);
				
				List<FPXRecords> listFPX = null;
				List<MIGSRecords> listMIGS = null;
				List<RppAkaun> listAkaun = null;
				ArrayList<KewInvois> listInvois = new ArrayList<KewInvois>();
				ArrayList<KewDeposit> listDeposit = new ArrayList<KewDeposit>();
				ArrayList<KewResitSenaraiInvois> listResitSenaraiInvoisDeposit = new ArrayList<KewResitSenaraiInvois>();
				ArrayList<KewBayaranResit> listBayaranResit = new ArrayList<KewBayaranResit>();
				ArrayList<KewResitKaedahBayaran> listKaedahBayaran = new ArrayList<KewResitKaedahBayaran>();
				
				boolean hasDoublePayment = false;
				
				if( r!=null ){
					
					listFPX = mp.list("select x from FPXRecords x where x.sellerOrderNo = '" + r.getId() + "' and x.debitAuthCode = '00' order by x.id asc");
					if (listFPX.size() > 1)
						hasDoublePayment = true;
					listMIGS = mp.list("select x from MIGSRecords x where x.idPermohonan = '" + r.getId() + "' and x.vpcMessage = 'Approved' order by x.vpcTransactionNo asc");
					if (listMIGS.size() > 1)
						hasDoublePayment = true;
					
					listAkaun = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
					if( listAkaun.size() > 0 ){
						for (int i = 0; i < listAkaun.size(); i++) {
							RppAkaun ak = listAkaun.get(i);
							if (ak.getKodHasil().getId().equalsIgnoreCase("72311")) {
								
								KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+ ak.getId() + "' ");
								
								if( dep!=null ){
									listDeposit.add(dep);
									KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.deposit.id = '"+dep.getId()+"' ");
									
									if( rsi!=null ){
										listResitSenaraiInvoisDeposit.add(rsi);
									}
									
									if( r.getIdResitDeposit() != null ){
										KewBayaranResit rst = (KewBayaranResit) mp.find(KewBayaranResit.class, r.getIdResitDeposit());
										if(rst!=null){
											if(!listBayaranResit.contains(rst)){
												listBayaranResit.add(rst);
											}
										}
										
										KewResitKaedahBayaran kdb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+r.getIdResitDeposit()+"' ");
										if( kdb!=null ){
											if(!listKaedahBayaran.contains(kdb)){
												listKaedahBayaran.add(kdb);
											}
										}
									}
								}
								
							} else {
								KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+ak.getId()+"' ");
								
								if( inv!=null ){
									listInvois.add(inv);
									KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+inv.getId()+"' ");
									
									if( rsi!=null ){
										listResitSenaraiInvoisDeposit.add(rsi);
									}
									
									if( r.getIdResitSewa() != null ){
										KewBayaranResit rst = (KewBayaranResit) mp.find(KewBayaranResit.class, r.getIdResitSewa());
										if(rst!=null){
											if(!listBayaranResit.contains(rst)){
												listBayaranResit.add(rst);
											}
										}
										
										KewResitKaedahBayaran kdb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+r.getIdResitSewa()+"' ");
										if( kdb!=null ){
											if(!listKaedahBayaran.contains(kdb)){
												listKaedahBayaran.add(kdb);
											}
										}
									}									
								}																
							}
						}
					}					
				}
				
				context.put("listFPX", listFPX);
				context.put("listMIGS", listMIGS);
				context.put("listAkaun", listAkaun);
				context.put("listInvois", listInvois);
				context.put("listDeposit", listDeposit);
				context.put("listResitSenaraiInvoisDeposit", listResitSenaraiInvoisDeposit);
				context.put("listBayaranResit", listBayaranResit);
				context.put("listKaedahBayaran", listKaedahBayaran);
				context.put("userIdLogin", userIdLogin);
				context.put("hasDoublePayment", hasDoublePayment);
				
			} catch (Exception e) {
				System.out.println("Error[getMaklumatBayaran] : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			vm = path + "/maklumatBayaran.vm";
			
			
		}else if(command.equalsIgnoreCase("openPopupMaklumatBayaran")){
			
			try {
				mp = new MyPersistence();
				
				String idrekod = getParam("idrekod");
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				
				KewBayaranResit resitSewa = null;
				KewBayaranResit resitDeposit = null;
				KewResitKaedahBayaran kb = null;
				if( r!=null){
					resitSewa = (KewBayaranResit) mp.find(KewBayaranResit.class, r.getIdResitSewa());
					resitDeposit = (KewBayaranResit) mp.find(KewBayaranResit.class, r.getIdResitDeposit());
					if( resitSewa!=null ){
						kb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+resitSewa.getId()+"' ");
					}
				}
				
				context.put("r",r);
				context.put("kaedahBayaran",kb);
				context.put("resitSewa",resitSewa);
				context.put("resitDeposit",resitDeposit);
				context.put("selectModBayaran", dataUtil.getCaraBayar());
				
			} catch (Exception e) {
				System.out.println("Error[openPopupMaklumatBayaran] : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			vm = path + "/popupMaklumatBayaran.vm";
			
		}else if(command.equalsIgnoreCase("janaSemulaMaklumatBayaran")){
			
			String idrekod = getParam("idrekod");
			String noResitSewa = getParam("noResitSewa");
			String noResitDeposit = getParam("noResitDeposit");
			Date tarikhBayaran = getDate("tarikhBayaran");
			String kaedahBayaran = getParam("kaedahBayaran");
			String jenisBayaran = getParam("jenisBayaran");
			String idTransaksiBank = getParam("idTransaksiBank");
			if (idTransaksiBank.equals("")) {
				idTransaksiBank = null;
			}
			
			try {
				mp = new MyPersistence();
				
				mp.begin();
				
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				
				String idResitSewa = "";
				String idResitDeposit = "";
				if( r!=null ){
					idResitSewa = r.getIdResitSewa();
					idResitDeposit = r.getIdResitDeposit();
				}
				
				/**CREATE RESIT SEWA KALAU BELUM ADA*/
//				KewBayaranResit resitSewa = null;
//				if( !idResitSewa.equalsIgnoreCase("") && idResitSewa != null ){
//					resitSewa = (KewBayaranResit) mp.find(KewBayaranResit.class, idResitSewa);
				String flagJenisResit = "";
				if(jenisBayaran.equalsIgnoreCase("KAUNTER") || jenisBayaran.equalsIgnoreCase("MANUAL") ){
					flagJenisResit = "3";
				}else{
					flagJenisResit = "2";
				}
				KewBayaranResit resitSewa = createUpdateKewBayaranResit(mp,idResitSewa,r,tarikhBayaran,jenisBayaran,userIdLogin,noResitSewa,flagJenisResit, idTransaksiBank);
//				}else{
//					resitSewa = createUpdateKewBayaranResit(mp,idResitSewa,r,tarikhBayaran,jenisBayaran,userIdLogin,noResitSewa,flagJenisResit);
//				}
				
				/**CREATE RESIT DEPOSIT KALAU BELUM ADA
				 * UNTUK KAUNTER, HANYA 1 RESIT SHJ
				 * */
				KewBayaranResit resitDeposit = null;
				if(jenisBayaran.equalsIgnoreCase("ONLINE")){
//					if( !idResitDeposit.equalsIgnoreCase("") && idResitDeposit != null ){
//						resitDeposit = (KewBayaranResit) mp.find(KewBayaranResit.class, idResitDeposit);
//					}else{
					resitDeposit = createUpdateKewBayaranResit(mp,idResitDeposit,r,tarikhBayaran,jenisBayaran,userIdLogin,noResitDeposit,"1", idTransaksiBank);
//					}
				}else{
					
//					if(jenisBayaran.equalsIgnoreCase("KAUNTER")){
//						if(noResitDeposit !=null && !noResitDeposit.equalsIgnoreCase("")){
//							resitDeposit = createUpdateKewBayaranResit(mp,idResitDeposit,r,tarikhBayaran,jenisBayaran,userIdLogin,noResitDeposit,"1", idTransaksiBank);
//						}else{
//							resitDeposit = resitSewa;
//						}
//					}else{
						resitDeposit = resitSewa;
//					}
						
				}
				
				/**UPDATE MAIN TABLE
				 * RPPPERMOHONAN, RPPAKAUN, KEWINVOIS, KEWDEPOSIT
				 * */
				updateRppPermohonan(mp,r,tarikhBayaran,resitSewa,resitDeposit);
				createUpdateRppAkaunDanInvoisDeposit(mp,r,tarikhBayaran,userIdLogin,resitSewa,resitDeposit);
				
				createUpdateKewResitKaedahBayaran(mp,resitSewa,kaedahBayaran);
				createUpdateKewResitKaedahBayaran(mp,resitDeposit,kaedahBayaran);
				
				/** UPDATE FPX RECORDS **/
				if (kaedahBayaran.equals("FPX") && idTransaksiBank != null) {
					FPXRecords fpx = (FPXRecords) mp.find(FPXRecords.class, idTransaksiBank);
					if (fpx != null) {
						fpx.setFlagModul("IR");
						fpx.setFlagManagePayment("Y");
					}
				}				
				
				mp.commit();
				
			} catch (Exception e) {
				System.out.println("Error janaSemulaMaklumatBayaran :  "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
				
			vm = path + "/clear.vm";
			
		}//close janaSemulaMaklumatBayaran
		
		
		/**
		 * CREATE RPPAKAUN, KEW INVOIS DAN DEPOSIT
		 * **/
		// PEJE CREATE AKAUN
		else if(command.equalsIgnoreCase("createRppAkaun")){
			
			String idrekod = getParam("idrekod");
			
			try {
				mp = new MyPersistence();
				
				mp.begin();
				
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				UtilRpp.createRecordBayaran(mp,r.getPemohon().getId(),r);
				
				mp.commit();
				
			} catch (Exception e) {
				System.out.println("Error createRppAkaun :  "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			
		}else if(command.equalsIgnoreCase("createKewInvois")){
			
			String idrekod = getParam("idrekod");
			
			try {
				mp = new MyPersistence();
				
				mp.begin();
				
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' and x.kodHasil.id != '72311' ");
				for(int i=0;i<listAkaun.size();i++){
					UtilRpp.createInvoisInFinance(listAkaun.get(i),r);
				}
				
				mp.commit();
				
			} catch (Exception e) {
				System.out.println("Error createKewInvois :  "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			
		}else if(command.equalsIgnoreCase("createKewDeposit")){
			
			String idrekod = getParam("idrekod");
			
			try {
				mp = new MyPersistence();
				
				mp.begin();
				
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' and x.kodHasil.id = '72311' ");
				for(int i=0;i<listAkaun.size();i++){
					UtilRpp.createDepositInFinance(listAkaun.get(i));
				}
				
				mp.commit();
				
			} catch (Exception e) {
				System.out.println("Error createKewDeposit :  "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			
		} else if(command.equalsIgnoreCase("deletePermohonan")){
		
			try {
				mp = new MyPersistence();
				
				String idrekod = getParam("idrekod");
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				
				if( r!=null ){
					mp.begin();
					UtilRpp.deletePermohonan(mp,r);
					mp.commit();
				}
				
			} catch (Exception e) {
				System.out.println("Error deletePermohonan : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			
			vm = path + "/status.vm";
			
		}else if(command.equalsIgnoreCase("openPopupUndoPembatalan")){
			
			try {
				mp = new MyPersistence();
				
				String idrekod = getParam("idrekod");
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				
				List<RppUnit> listUnitAvailable = null;
				if( r!=null){
					listUnitAvailable = UtilRpp.walkInListUnitAvailable(mp,r.getTarikhMasukRpp(),r.getTarikhKeluarRpp(),r.getJenisUnitRpp().getId());
				}
				
				String strTarikhMasuk = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhMasukRpp());
				String strTarikhKeluar = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhKeluarRpp());
				context.put("strTarikhMasuk", strTarikhMasuk);
				context.put("strTarikhKeluar", strTarikhKeluar);
				context.put("listUnitAvailable",listUnitAvailable);
				context.put("r",r);
				
			} catch (Exception e) {
				System.out.println("Error[openPopupUndoPembatalan] : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			vm = path + "/popupUndoPembatalan.vm";
			
		}else if(command.equalsIgnoreCase("undoPembatalan")){
		
			try {
				mp = new MyPersistence();
				
				String idrekod = getParam("idrekod");
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				
				if( r!=null ){
					
					mp.begin();
					
					//create jadual tempahan
					String[] cbUnit = request.getParameterValues("cbUnit");
					for(int i = 0; i < cbUnit.length; i++){
						String idUnit = cbUnit[i];
						RppJadualTempahan jt = new RppJadualTempahan();
						jt.setFlagStatusTempahan("TEMP");
						jt.setPermohonan(r);
						jt.setStatus("B");
						jt.setTarikhMula(r.getTarikhMasukRpp());
						jt.setTarikhTamat(r.getTarikhKeluarRpp());
						jt.setUnit((RppUnit) mp.find(RppUnit.class, idUnit));
						mp.persist(jt);
					}
					
					//create invois/deposit, update rppakaun
					List<RppAkaun> akInvs = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' and x.kodHasil.id != '72311' ");
					if(akInvs.size() > 0){
						for(int i=0;i<akInvs.size();i++){
							RppAkaun akInv = akInvs.get(i);
							akInv.setTarikhVoid(null);
							akInv.setFlagVoid(null);
							akInv.setAmaunVoid(0d);
							
							if(!r.getRppPeranginan().getId().equalsIgnoreCase("3") && !r.getRppPeranginan().getId().equalsIgnoreCase("14")){
								KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+akInv.getId()+"' ");
								if(inv == null){
									UtilRpp.createInvoisInFinance(akInvs.get(i),r);
								}
							}
						}
					}else{
						//create rppakaun baru
						//takperlu buat dlu
					}
					
					
					List<RppAkaun> akDeps = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' and x.kodHasil.id = '72311' ");
					if(akDeps.size() > 0){
						for(int i=0;i<akDeps.size();i++){
							RppAkaun akDep = akDeps.get(i);
							akDep.setTarikhVoid(null);
							akDep.setFlagVoid(null);
							akDep.setAmaunVoid(0d);
							
							if(!r.getRppPeranginan().getId().equalsIgnoreCase("3") && !r.getRppPeranginan().getId().equalsIgnoreCase("14")){
								KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+akDep.getId()+"' ");
								if(dep == null){
									UtilRpp.createDepositInFinance(akDeps.get(i));
								}
							}
						}
					}else{
						//create rppakaun baru
						//takperlu buat dlu
					}
					//rozai edit 31/10/2017
					//kalau status semasa ialah BATAL PERMOHONAN, maklumat rekod pembatalan akan kekal.
					if(!r.getStatus().getId().equalsIgnoreCase("1425259713418"))
					{
						r.setTarikhPembatalan(null);
						r.setPemohonBatal(null);
						r.setCatatanPembatalan(null);
					}
					//update status permohonan jadi PERMOHONAN BARU
					r.setStatus((Status) mp.find(Status.class, "1425259713412"));
					mp.commit();
					
				}
				
			} catch (Exception e) {
				System.out.println("Error undoPembatalan : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			
			vm = path + "/clear.vm";
			
		}else if(command.equalsIgnoreCase("openPopupMaklumatResitLain")){ //ADD BY PEJE - TO VIEW DOUBLE PAYMENT PUNYA RESIT
			List<KewBayaranResit> listResitLain = null;
			List<KewResitSenaraiInvois> listResitSenaraiInvoisLain = new ArrayList<KewResitSenaraiInvois>();
			List<KewResitKaedahBayaran> listKaedahBayaranLain = new ArrayList<KewResitKaedahBayaran>();
			
			try {
				mp = new MyPersistence();
				
				String idrekod = getParam("idrekod");
				RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idrekod);
				
				listResitLain = mp.list("select x from KewBayaranResit x where x.idPermohonan = '" + r.getId() + "' and x.id not in ('" + r.getResitDeposit().getId() + "','" + r.getResitSewa().getId() + "') order by x.noResit asc");
				for (int i = 0; i < listResitLain.size(); i ++) {
					List<KewResitSenaraiInvois> listResitSenaraiInvois = mp.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + listResitLain.get(i).getId() + "'");
					for (int j = 0; j < listResitSenaraiInvois.size(); j++) {
						listResitSenaraiInvoisLain.add(listResitSenaraiInvois.get(j));
					}
					List<KewResitKaedahBayaran> listKaedahBayaran = mp.list("select x from KewResitKaedahBayaran x where x.resit.id = '" + listResitLain.get(i).getId() + "'");
					for (int k = 0; k < listKaedahBayaran.size(); k++) {
						listKaedahBayaranLain.add(listKaedahBayaran.get(k));
					}
				}
				
				context.put("r",r);
				context.put("listResitLain", listResitLain);
				context.put("listResitSenaraiInvoisLain", listResitSenaraiInvoisLain);
				context.put("listKaedahBayaranLain", listKaedahBayaranLain);
				
			} catch (Exception e) {
				System.out.println("Error[openPopupMaklumatResitLain] : "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
			vm = path + "/popupMaklumatResitLain.vm";
			
		}
		
		
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		context.put("util", new Util());
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
	
	
	/******/
	
	
	private List<RppPermohonan> searchPermohonan(MyPersistence mp,String param) {
		ArrayList<RppPermohonan> list = new ArrayList<RppPermohonan>();
		Db db1 = null;
		try {
			db1 = new Db();

			String sql = "SELECT DISTINCT x.id FROM rpp_permohonan x, users y "
					+ " WHERE x.id_pemohon = y.user_login "
					+ " AND x.tarikh_permohonan >= str_to_date('01,08,2015','%d,%m,%Y') "
					+ " AND ( x.id = '" + param + "' "
					+ "	OR upper(x.no_tempahan) LIKE upper('%"+ param + "%') "
					+ "	OR upper(y.user_name) LIKE upper('%"+ param + "%') "
					+ " OR y.user_login = '" + param + "' ) "
					+ " ORDER BY x.status_bayaran desc ";

			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				RppPermohonan us = (RppPermohonan)mp.find(RppPermohonan.class, rs.getString("id"));
				list.add(us);
			}

		} catch (Exception e) {
			System.out.println("error searchPermohonan : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
		
	}//close searchPermohonan
	
	
	public void createUpdateKewResitKaedahBayaran(MyPersistence mp,KewBayaranResit resit,String kaedahBayaran){
		KewResitKaedahBayaran kb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+resit.getId()+"' ");
		if(kb == null){
			kb = new KewResitKaedahBayaran();
		}
		kb.setResit(resit);
		kb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
		kb.setModBayaran((CaraBayar)mp.find(CaraBayar.class,kaedahBayaran));
		mp.persist(kb);
	}//close createUpdateKewResitKaedahBayaran
	
	
	public KewBayaranResit createUpdateKewBayaranResit(MyPersistence mp,String idresit,RppPermohonan r,Date tarikhBayaran,
			String jenisBayaran,String userIdLogin,String noResit,String flagJenisResit, String idTransaksiBank){
		
		/**
		 * flagJenisResit 1 = DEPOSIT , 2 = INVOIS, 3 = SEWA&DEPOSIT
		 * */
		
		KewBayaranResit rt = (KewBayaranResit) mp.find(KewBayaranResit.class,idresit);
		if(rt == null){
			rt = new KewBayaranResit();
		}
		
		Double amaunBayaran = 0d;
		if(flagJenisResit.equalsIgnoreCase("2")){
			amaunBayaran = r.amaunTotalSewaRpWithoutDeposit()!=null?r.amaunTotalSewaRpWithoutDeposit():0d;
		}else if(flagJenisResit.equalsIgnoreCase("3")){
			amaunBayaran = r.amaunTotalSewaRpWithDeposit()!=null?r.amaunTotalSewaRpWithDeposit():0d;
		}else{
			amaunBayaran = r.amaunDeposit()!=null?r.amaunDeposit():0d;
		}
		
		if( noResit==null || noResit.equalsIgnoreCase("") ){
			noResit = UtilKewangan.generateReceiptNoOnline(mp, tarikhBayaran);
		}
		
		Users pembayar = r.getPemohon();
		if (pembayar != null) {
			rt.setPembayar(pembayar);
			rt.setNoPengenalanPembayar(pembayar.getId());
			rt.setNamaPembayar(pembayar.getUserName());				
			rt.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
		}
		rt.setNoResit(noResit);
		rt.setTarikhBayaran(tarikhBayaran);
		rt.setTarikhResit(tarikhBayaran);
		rt.setMasaResit(tarikhBayaran);
		rt.setFlagJenisBayaran(jenisBayaran);
		rt.setTarikhDaftar(tarikhBayaran);
		rt.setUserPendaftar((Users) mp.find(Users.class, userIdLogin));
		rt.setJumlahAmaunBayaran(amaunBayaran);
		
		if(!flagJenisResit.equalsIgnoreCase("3")){
			rt.setJuruwangKod("09");
		}
		
		rt.setFlagJenisResit(flagJenisResit);
		rt.setIdTransaksiBank(idTransaksiBank);
		rt.setIdPermohonan(r.getId());
		mp.persist(rt);
		
		return rt;
	
	}//close createUpdateKewBayaranResit
	
	@SuppressWarnings("unchecked")
	public void createUpdateRppAkaunDanInvoisDeposit(MyPersistence mp,RppPermohonan r,Date tarikhBayaran,String userIdLogin,KewBayaranResit resitSewa,KewBayaranResit resitDeposit){
		List<RppAkaun> listAkaun = mp.list("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
		for(int i=0;i<listAkaun.size();i++){
			RppAkaun ak = listAkaun.get(i);
			ak.setKredit(ak.getDebit());
			ak.setFlagBayar("Y");
			ak.setTarikhTransaksi(tarikhBayaran);
			ak.setIdKemaskini((Users) mp.find(Users.class, userIdLogin));
			ak.setTarikhKemaskini(tarikhBayaran);
			
			/** Update/create KewInvois / KewDeposit */
			if(ak.getKodHasil().getId().equalsIgnoreCase("72311")){ //DEPOSIT
				ak.setNoResit(resitDeposit.getNoResit());
				updateCreateDepositInFinance(mp,ak,r,userIdLogin,resitDeposit);
			}else{
				ak.setNoResit(resitSewa.getNoResit());
				updateCreateInvoisInFinance(mp,ak,r,userIdLogin,resitSewa);				
			}
			
		}
	}//close createUpdateRppAkaun
	
	public void updateCreateInvoisInFinance(MyPersistence mp, RppAkaun ak,RppPermohonan r,String userIdLogin,KewBayaranResit resitSewa){
		KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+ak.getId()+"' and x.jenisBayaran.id = '02' ");
		if( inv == null ){
			inv = new KewInvois();
		}
		
		inv.setKodHasil(ak.getKodHasil());
		inv.setFlagBayaran("SEWA");
		inv.setNoInvois(r.getNoTempahan());
		inv.setNoRujukan(r.getNoTempahan());
		inv.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class,"02"));
		inv.setTarikhInvois(r.getTarikhPermohonan());
		inv.setKeteranganBayaran(ak.getKeterangan());
		inv.setDebit(ak.getDebit());
		inv.setIdLejar(ak.getId());
		inv.setUserPendaftar((Users) mp.find(Users.class, userIdLogin));
		inv.setTarikhDaftar(r.getTarikhPermohonan());
		inv.setTarikhDari(r.getTarikhMasukRpp());
		inv.setTarikhHingga(r.getTarikhKeluarRpp());
		inv.setPembayar(r.getPemohon());
		inv.setFlagBayar("Y");
		inv.setKredit(ak.getDebit());
		inv.setFlagQueue("Y");
		mp.persist(inv);
		
		KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+inv.getId()+"' ");
		if( rsi == null ){
			rsi = new KewResitSenaraiInvois();
		}
		rsi.setInvois(inv);
		rsi.setResit(resitSewa);
		rsi.setFlagJenisResit("INVOIS");
		mp.persist(rsi);
		
	}//close updateCreateInvoisInFinance
	
	public void updateCreateDepositInFinance(MyPersistence mp, RppAkaun ak,RppPermohonan r,String userIdLogin,KewBayaranResit resitDeposit){
		KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+ak.getId()+"' and x.jenisBayaran.id = '02' ");
		if( dep == null ){
			dep = new KewDeposit();
		}
		
		dep.setKodHasil(ak.getKodHasil());
		dep.setIdLejar(ak.getId());
		dep.setPendeposit(r.getPemohon());
		dep.setNoInvois(r.getNoTempahan());
		dep.setKeteranganDeposit(ak.getKeterangan());
		dep.setTarikhDeposit(r.getTarikhPermohonan());
		dep.setJumlahDeposit(ak.getDebit());
		dep.setFlagPulangDeposit("T");
		dep.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class,"02"));
		dep.setTarikhDari(r.getTarikhMasukRpp());
		dep.setTarikhHingga(r.getTarikhKeluarRpp());
		dep.setFlagWarta("T");
		dep.setFlagBayar("Y");
		dep.setFlagQueue("T");
		dep.setNoResit(ak.getNoResit());
		dep.setTarikhBayaran(ak.getTarikhResit());
		dep.setBakiDeposit(ak.getDebit());
		mp.persist(dep);
		
		KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.deposit.id = '"+dep.getId()+"' ");
		if( rsi == null ){
			rsi = new KewResitSenaraiInvois();
		}
		rsi.setDeposit(dep);
		rsi.setResit(resitDeposit);
		rsi.setFlagJenisResit("DEPOSIT");
		mp.persist(rsi);
		
	}//close updateCreateDepositInFinance
	
	public void updateRppPermohonan(MyPersistence mp,RppPermohonan r,Date tarikhBayaran,KewBayaranResit resitSewa, KewBayaranResit resitDeposit){
		String status = r.getStatus().getId();
		if(r.getStatus().getId().equalsIgnoreCase("1425259713412") || r.getStatus().getId().equalsIgnoreCase("1430809277102")){
			status = "1425259713415";
		}
		r.setStatusBayaran("Y");
		r.setTarikhBayaran(tarikhBayaran);
		r.setStatus((Status) mp.find(Status.class, status)); /**kalau permohonan baru, tukar kpd permohonan lulus*/
		r.setResitSewa(resitSewa);
		r.setResitDeposit(resitDeposit);
	}//close updateRppPermohonan
		
	public Date getDate(String paramName) {
		Date dateTxt = null;
		String dateParam = request.getParameter(paramName);
		if (dateParam != null && !"".equals(dateParam)) {
			try {
				dateTxt = new SimpleDateFormat("dd-MM-yyyy").parse(dateParam);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateTxt;
	}
}
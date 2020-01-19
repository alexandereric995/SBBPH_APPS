package bph.modules.kewangan.kontrak;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kontrak.KontrakKontrak;
import bph.entities.kontrak.KontrakMaklumatBayaran;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;


public class SenaraiKontrakBayaranRecordModule extends LebahRecordTemplateModule<KontrakKontrak>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("SenaraiKontrakBayaranRecordModule");
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<KontrakKontrak> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KontrakKontrak.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/kewangan/kontrak/kontrakBayaran";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);

		context.put("selectKaedahPerolehan", dataUtil.getListKaedahPerolehan());
		context.put("selectJenisKontrak", dataUtil.getListJenisKontrak());
		context.put("selectSeksyen", dataUtil.getListSeksyen());
		
		//---- Start list bayaran -----
		KontrakKontrak kontrak = (KontrakKontrak) db.find(KontrakKontrak.class, get("idKontrak"));
		if (kontrak != null) {
//			myLogger.debug("PRINTTTT kontrak ====== " + kontrak);
			List<KontrakMaklumatBayaran> rekodMb = db.list("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id ='"+ kontrak.getId() +"'");
			if (rekodMb != null) {
//				myLogger.debug("PRINT ID BAYARAN ::" + rekodMb.size());
				context.put("rekodMaklumatBayaran", rekodMb);
			}	
		}
		//---- End list bayaran -----
		
		addfilter(userId, userRole);
		defaultButtonOption();	
		
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
//		getStatusPeringatan();
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
		context.remove("flagAllowUpdate");
	}
	
	private void addfilter(String userId, String userRole) {
		// TODO Auto-generated method stub
		
	}

	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableUpperBackButton(true);
		if (!"add_new_record".equals(command)){
//			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		this.addFilter("status in ('04')"); //04=KONTRAK AKTIF
		this.addFilter("statusBayaran in ('1')"); //Status Bayaran = 1
		this.setOrderBy("id");
		this.setOrderType("asc");
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void save(KontrakKontrak r) throws Exception {
		
		
	
	}

	@Override
	public void afterSave(KontrakKontrak r) {
		context.put("selectedTab", 1);		
	}
	
	@Override
	public boolean delete(KontrakKontrak r) throws Exception {
		if (r.getStatus().equals("01")) {
			return true;
		} else {
			return false;
		}		
	}

	@Override
	public void getRelatedData(KontrakKontrak r) {
//		myLogger.debug("PRINT ID KONTRAK ::" + r.getId());
		
		if (r.getStatus().equals("01") || r.getStatus().equals("02") || r.getStatus().equals("03")) {
			context.put("flagAllowUpdate", "Y");
		} else {
			context.remove("flagAllowUpdate");
		}
		
		try {			
			mp = new MyPersistence();
			
			//---- Start list bayaran -----
			List<KontrakMaklumatBayaran> rekodMb = mp.list("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id ='"+ r.getId() +"'");
			//myLogger.debug("PRINT ID BAYARAN ::" + rekodMb);
			if (rekodMb != null) {
				context.put("rekodMaklumatBayaran", rekodMb);
			}
			//---- End list bayaran -----
			
			//----- FOR NOTIFICATION -----
			KontrakMaklumatBayaran kmb =(KontrakMaklumatBayaran) db.get("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id ='"+ r.getId()+"'");
			// System.out.println("KontrakMaklumatBayaran ===== " + kmb );
			if(kmb != null){
				String statusPeringatan = "";
				Date tarikhTransaksi = kmb.getTarikhTransaksi();
				int bilHari = 0;
	
				if(tarikhTransaksi != null && tarikhTransaksi.toString().length() > 0){
					
					Calendar calTarikhTransaksi = new GregorianCalendar();
					Date dateTarikhTransaksi = tarikhTransaksi;
					calTarikhTransaksi.setTime(dateTarikhTransaksi);
					
					Calendar calCurrent = new GregorianCalendar();
					Date dateCurrent = new Date();
					calCurrent.setTime(dateCurrent);
					
					int diffYear = calTarikhTransaksi.get(Calendar.YEAR) - calCurrent.get(Calendar.YEAR);
					int diffMonth = diffYear * 12 + calTarikhTransaksi.get(Calendar.MONTH) - calCurrent.get(Calendar.MONTH);
					bilHari = daysBetween(calTarikhTransaksi.getTime(), calCurrent.getTime());
					
					if (calCurrent.getTime().after(calTarikhTransaksi.getTime())) {   //BILANGAN HARI STATUS BARU
						statusPeringatan = bilHari + " HARI";
					}
				}
				context.put("statusPeringatan", statusPeringatan);
				//----- FOR NOTIFICATION -----
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
	}
	private int daysBetween(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("noDaftarKontrak", get("findNoDaftarKontrak"));
		map.put("jenisKontrak.id", new OperatorEqualTo(get("findJenisKontrak")));
		map.put("perkhidmatan", get("findPerkhidmatan"));
		map.put("kodProgram", get("findKodProgram"));
		map.put("kodObjek", get("findKodObjek"));
		map.put("seksyen.id", new OperatorEqualTo(get("findSeksyen")));
		map.put("idPelaksana", new OperatorEqualTo(get("findLantikan")));
		map.put("kontraktor.id", get("findNoPendaftaranKontraktor"));
		map.put("kontraktor.namaKontraktor", get("findNamaKontraktor"));
		
		return map;
	}		
	
	/******************************* START MAKLUMAT BAYRAN KONTRAK *******************************/	
	
	@Command("getMaklumatBayaran")
	public String getMaklumatBayaran() {
		
		String id = getParam("idMaklumatBayaran");
		myLogger.debug(" id Maklumat Bayaran ======== :: " + id);
		KontrakKontrak kontrak = null;
		KontrakMaklumatBayaran kemaskiniMaklumat = null;
		
		try {
			mp = new MyPersistence();
			
			kontrak = (KontrakKontrak) mp.get("SELECT x FROM KontrakKontrak x WHERE x.id = '"+ getParam("idKontrak") +"'");
			
//			kemaskiniMaklumat = (KontrakMaklumatBayaran) mp.find(KontrakMaklumatBayaran.class, id);
			kemaskiniMaklumat = (KontrakMaklumatBayaran) mp.get("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id = '"+ kontrak.getId() +"'");
			context.put("rekodMb", kemaskiniMaklumat);
			myLogger.debug("PRINT DATE === " + kemaskiniMaklumat.getId());
			
			//---- Start list bayaran -----
			List<KontrakMaklumatBayaran> rekodMb = mp.list("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id ='"+ kontrak.getId() +"'");
			//myLogger.debug("PRINT ID BAYARAN ::" + rekodMb);
			if (rekodMb != null) {
				context.put("rekodMaklumatBayaran", rekodMb);
			}
			//---- End list bayaran -----
			
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		// context.remove("rekod");
		return getPath() + "/entry_page.vm";
	}
	
	//-----START SIMPAN DAN KEMASKINI ------
	@Command("saveMaklumatBayaran")
	public String saveMaklumatBayaran() throws Exception {
	
		String statusInfo = "";	
		String flagStatusInfo = "";	
		Boolean addMaklumatBayaran = false;
		KontrakKontrak kontrak = null;
		KontrakMaklumatBayaran maklumatBayaran = null;
		
		try {
			mp = new MyPersistence();	
			mp.begin();
//			myLogger.debug("PRINT idBayaran ====== :" + getParam("idMaklumatBayaran"));
			maklumatBayaran = (KontrakMaklumatBayaran) mp.find(KontrakMaklumatBayaran.class, getParam("idMaklumatBayaran"));
			if (maklumatBayaran == null) {
				addMaklumatBayaran = true;
				maklumatBayaran = new KontrakMaklumatBayaran();
			}
			maklumatBayaran.setTarikhResit(getDate("tarikhResit"));
			maklumatBayaran.setNoResit(getParam("noResit"));
			maklumatBayaran.setTarikhBaucer(getDate("tarikhBaucer"));
			maklumatBayaran.setNoBaucer(getParam("noBaucer"));
			maklumatBayaran.setKeterangan(getParam("keterangan"));
			maklumatBayaran.setKredit(Double.valueOf(util.RemoveComma(getParam("kredit"))));
			maklumatBayaran.setTarikhBayaran(getDate("tarikhBayaran"));
			maklumatBayaran.setUserTransaksiKewanganId((Users) mp.find(Users.class, userId));
			maklumatBayaran.setUserTransaksiKewanganTarikh(new Date());	
			maklumatBayaran.setStatusBayaran("2"); // STATUS TELAH BAYAR
			maklumatBayaran.setCatatanAmaunPotonganDenda(getParam("catatanAmaunPotonganDenda"));
			maklumatBayaran.setCatatanAmaunPotonganCdc(getParam("catatanAmaunPotonganCdc"));
			
			if(addMaklumatBayaran){
				mp.persist(maklumatBayaran);
			}
			
			kontrak = (KontrakKontrak) mp.get("SELECT x FROM KontrakKontrak x WHERE x.id = '"+ getParam("idKontrak") +"'");
			System.out.println("KONTRAK ID ====== " + kontrak.getId());
			kontrak.setStatusBayaran("2");
			
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "MAKLUMAT BAYARAN TELAH BERJAYA DIKEMASKINI";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT BAYARAN TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);	
//		 return getPath() + "/entry_page.vm";
		return getMaklumatBayaran();
	}
	//--------------------END SIMPAN MAKLUMAT DAN KEMASKINI --------------------
	
	/*-------------------- START FOR POPUP ------------*/
	@Command("addMaklumatBayaran")
	public String addMaklumatBayaran() {
		
		context.remove("rekod");
		return getPath() + "/maklumatPembayaranForm.vm";
	}
	
	@Command("editMaklumatBayaran")
	public String editMaklumatBayaran() {
		
		String id = getParam("addMaklumatBayaran");
//		myLogger.debug("idKontrak" + id);
		KontrakMaklumatBayaran kemaskiniMaklumat = null;
		
		try {
			mp = new MyPersistence();
			
			kemaskiniMaklumat = (KontrakMaklumatBayaran) mp.find(KontrakMaklumatBayaran.class, id);
			context.put("rekodMb", kemaskiniMaklumat);
//			myLogger.debug("PRINT DATE === " + kemaskiniMaklumat.getTarikhInvois());
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/maklumatPembayaranForm.vm";
	}
	
	@Command("kembali")
	public String kembali() {
		
		String id = getParam("idMaklumatBayaran");
		myLogger.debug(" id Maklumat Bayaran ======== :: " + id);
		KontrakKontrak kontrak = null;
		KontrakMaklumatBayaran kemaskiniMaklumat = null;
		
		try {
			mp = new MyPersistence();
			
			kontrak = (KontrakKontrak) mp.get("SELECT x FROM KontrakKontrak x WHERE x.id = '"+ getParam("idKontrak") +"'");
			
			kemaskiniMaklumat = (KontrakMaklumatBayaran) mp.get("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id = '"+ kontrak.getId() +"'");
			context.put("rekodMb", kemaskiniMaklumat);
			myLogger.debug("PRINT DATE === " + kemaskiniMaklumat.getId());
			
			//---- Start list Maklumat Bayaran -----
			List<KontrakMaklumatBayaran> rekodMb = mp.list("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id ='"+ kontrak.getId() +"'");
			//myLogger.debug("PRINT ID BAYARAN ::" + rekodMb);
			if (rekodMb != null) {
				context.put("rekodMaklumatBayaran", rekodMb);
			}
			//---- End list Maklumat Bayaran -----
			
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
//		context.remove("rekod");
		return getPath() + "/entry_page.vm";
	}
	/*-------------------- END FOR POPUP ------------*/
	/******************************* END MAKLUMAT BAYARAN KONTRAK *******************************/
	
	/***************************** START DROP DOWN LIST *****************************/	
	@Command("selectBandar")
	public String selectBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0){
			idNegeri = get("idNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/maklumatKontraktor/selectBandar.vm";
	}
	/***************************** END DROP DOWN LIST *****************************/

	
}





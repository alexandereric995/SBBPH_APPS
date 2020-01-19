package bph.modules.bil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.bil.BayaranBil;
import bph.entities.bil.DaftarBil;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class MaklumatBayaranRecordModule extends LebahRecordTemplateModule<BayaranBil>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<BayaranBil> getPersistenceClass() {
		// TODO Auto-generated method stub
		return BayaranBil.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
	
		return "bph/modules/bil/maklumatBayaran";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		context.remove("statusInfo");	
		context.remove("info");	
		
		//----
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
			
		//----
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		//----
		dataUtil = DataUtil.getInstance(db);
		
		//----
		context.put("selectKodBil", dataUtil.getListJenisBil()); //dropdown Jenis Bil
		
		/*this.addFilter("statusBayaran = 'T'");
		this.addFilter("statusLulus = 'T'");*/
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
	}
	
	private void addfilter() {

//		if ("(BIL) Penyemak".equals(userRole)){
//			this.addFilter("statusBil.id = '1433131904918'");
//		}

	}
	
	private void defaultButtonOption() {
		//----
		this.setDisableSaveAddNewButton(true);
		this.setReadonly(false);
		this.setDisableBackButton(false);
		
		if ("(BIL) Penyemak".equals(userRole) || "(BIL) Pelulus".equals(userRole)){
			this.setReadonly(true);
			this.setDisableBackButton(true);
		}
		
//		JIKA PENYEMAK TIDAK BOLEH SIMPAN ATAU KEMASKINI
		if ("(RPP) Penyemak".equals(userRole)
				|| "(UTK) Penyemak".equals(userRole)
				|| "(UTILITI - BIL) Penyemak".equals(userRole)
				|| "(TNH) Penyemak".equals(userRole)
				|| "(SENGGARA) Penyemak".equals(userRole)
				|| "(RPP) Penyemak".equals(userRole)
				|| "(RK) Penyemak".equals(userRole)
				|| "(QTR) Penyemak".equals(userRole)
				|| "(PEROLEHAN) Penyemak".equals(userRole)
				|| "(KONTRAK) Penyemak".equals(userRole)
				|| "(JRP) Penyemak".equals(userRole)
				|| "(BGS) Penyemak".equals(userRole)
				|| "(ICT) Pentadbir Sistem".equals(userRole)
				|| "(PENTADBIRAN - BIL) Penyemak".equals(userRole)) {
			this.setReadonly(true);
			this.setDisableBackButton(true);
		}
		
//		if(statusBayaran == 'Y'){
//			this.setDisableSaveAddNewButton(true);
//		}
		
//		if (!"add_new_record".equals(command)) {
//			this.setDisableSaveAddNewButton(true);
//		}
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}

	@Override
	public void save(BayaranBil simpan) throws Exception {
		// TODO Auto-generated method stub
	
		try {			
			mp = new MyPersistence();
			
			String idPendaftaranBil = getParam("idPendaftaranBil");
			DaftarBil daftarBil = (DaftarBil) mp.find(DaftarBil.class, idPendaftaranBil);
			
			simpan.setPendaftaranBil(daftarBil);
			simpan.setBulan(getParam("bulan"));
			simpan.setTahun(getParam("tahun"));
			simpan.setNoBil(get("noBil"));
			simpan.setTarikhBil(getDate("tarikhBil"));
			simpan.setTarikhTerimaBil(getDate("tarikhTerimaBil"));
			simpan.setAmaunTunggakan(Double.valueOf(Util.RemoveComma(get("amaunTunggakan"))));
			simpan.setAmaunSemasa(Double.valueOf(Util.RemoveComma(get("amaunSemasa"))));		
			simpan.setJumlahBil(Double.valueOf(util.RemoveComma(get("jumlahBil"))));		
			simpan.setTarikhAkhirBayaran(getDate("tarikhAkhirBayaran"));
//			simpan.setStatusBil("1433131904918");
			simpan.setStatusBil((Status) mp.find(Status.class, "1433131904918")); 
			simpan.setStatusBayaran("T");

 
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public boolean delete(BayaranBil r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		
		String findKodBil = getParam("findKodBil");
		String findPenerimaBayaran = getParam("find_penerima_bayaran");
		String findNoAkaun = getParam("find_no_akaun");
		String findNoBil = getParam("find_no_bil");
		String findNoRujukan = getParam("find_no_rujukan");
		String findStatusBayaran = getParam("findStatusBayaran");
		
		HashMap<String, Object> cari = new HashMap<String, Object>();
		cari.put("pendaftaranBil.jenisBil.id", new OperatorEqualTo(findKodBil));
		cari.put("pendaftaranBil.penerimaBayaran", findPenerimaBayaran);
		cari.put("pendaftaranBil.noAkaun", findNoAkaun);
		cari.put("noBil", findNoBil);
		cari.put("noRujukan", findNoRujukan);
		cari.put("statusBayaran", findStatusBayaran);
		
		return cari;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(BayaranBil r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(BayaranBil r) {
			
		if("(BIL) Penyedia".equals(userRole)){
			if(r.getStatusBayaran() == "1433131904924" ){
				this.setReadonly(true);
			}
		}
	}
	
	@Command("calculateJumlahBil")
	public String calculateJumlahBil() throws Exception {
		Double jumlahBil = 0D;
		Double amaunSemasa = 0D;
		Double amaunTunggakan = 0D;
		if(!getParam("amaunSemasa").equals("")){
			amaunSemasa = Double.valueOf(util.RemoveComma(getParam("amaunSemasa")));
		}
		if(!getParam("amaunTunggakan").equals("")){
			amaunTunggakan = Double.valueOf(util.RemoveComma(getParam("amaunTunggakan")));
		}
		jumlahBil = amaunSemasa + amaunTunggakan;
		context.put("jumlahBil", jumlahBil);
		return getPath() + "/calculateJumlahBil.vm";
	}
	
	
	@Command("saveMaklumatBayaran")
	public String saveMaklumatBayaran() throws Exception {		
					
		try {			
			mp = new MyPersistence();
			mp.begin();
				//MAKLUMAT BAYARAN
				BayaranBil bayaranbil = new BayaranBil();
				bayaranbil.setNoBil(get("noBil"));
				bayaranbil.setTarikhBil(getDate("tarikhBil"));
				bayaranbil.setTarikhTerimaBil(getDate("tarikhTerimaBil"));
				bayaranbil.setAmaunTunggakan(Double.valueOf(Util.RemoveComma(get("amaunTunggakan"))));
				bayaranbil.setAmaunSemasa(Double.valueOf(Util.RemoveComma(get("amaunSemasa"))));		
				bayaranbil.setJumlahBil(Double.valueOf(util.RemoveComma(get("jumlahBil"))));		
				bayaranbil.setTarikhAkhirBayaran(getDate("tarikhAkhirBayaran"));
//			db.persist(bayaranbil);
			mp.commit();			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
//		//MAKLUMAT BAYARAN
//		BayaranBil bayaranbil = new BayaranBil();
//		bayaranbil.setNoBil(get("noBil"));
//		bayaranbil.setTarikhBil(getDate("tarikhBil"));
//		bayaranbil.setTarikhTerimaBil(getDate("tarikhTerimaBil"));
//		bayaranbil.setAmaunTunggakan(Double.valueOf(Util.RemoveComma(get("amaunTunggakan"))));
//		bayaranbil.setAmaunSemasa(Double.valueOf(Util.RemoveComma(get("amaunSemasa"))));		
//		bayaranbil.setJumlahBil(Double.valueOf(util.RemoveComma(get("jumlahBil"))));		
//		bayaranbil.setTarikhAkhirBayaran(getDate("tarikhAkhirBayaran"));
//	
//		db.begin();
////		db.persist(bayaranbil);
//		
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
		return getPath() + "/entry_page.vm";		
	}
	
	@Command("lulusMaklumatBayaran")
	public String lulusMaklumatBayaran() throws Exception {		

		try {			
			mp = new MyPersistence();
			mp.begin();
			//MAKLUMAT BAYARAN		
				BayaranBil simpan = (BayaranBil) mp.find(BayaranBil.class, get("idMaklumatBil"));
				simpan.setStatusLulus("L");
				mp.commit();
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	
		return getPath() + "/entry_page.vm";		
	}
	
	@Command("doHantarSemakan")
	public String doHantarSemakan() throws Exception {		
		
		try {			
			mp = new MyPersistence();
			mp.begin();
				BayaranBil simpan = (BayaranBil) mp.find(BayaranBil.class, get("idMaklumatBil"));
				simpan.setStatusBil((Status) mp.find(Status.class, "1433131904918"));//UPDATE STATUS SEMAKAN	
				mp.commit();
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	
		return getPath() + "/entry_page.vm";		
	}
	
	//------ START APABILA PENYEMAK HANTAR KE KEWANGAN -----------
	@Command("doHantarKewangan")
	public String doHantarKewangan() throws Exception {		
		String info = "";	
		String statusInfo = "";	
		try {
			mp = new MyPersistence();	
			
			BayaranBil simpan = (BayaranBil) mp.find(BayaranBil.class, get("idMaklumatBil"));
			simpan.setStatusBil((Status) mp.find(Status.class, "1433131904921"));//UPDATE STATUS SEMAKAN KEWANGAN
			if (simpan != null) {
				mp.begin();
					if ("(BIL) Penyemak".equals(userRole)){
						simpan.setDiSemakOleh((Users) mp.find(Users.class, userId));
						simpan.setTarikhSemakan(new Date());
					}
				mp.commit();
				statusInfo = "Y";
				info = "MAKLUMAT TELAH BERJAYA DIHANTAR";
			}			
		} catch (Exception ex) {
			statusInfo = "T";
			info = "MAKLUMAT TIDAK BERJAYA DIHANTAR";
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("statusInfo", statusInfo);
		context.put("info", info);
		return getPath() + "/entry_page.vm";		
	}
	//------ APABILA PENYEMAK HANTAR KE KEWANGAN -----------
	
	/** START AUTO DAPATKAN MAKLUMAT BIL **/
	@Command("getRegisteredBil")
	public String getRegisteredBil() throws Exception {
		
		context.remove("bayaranBil");
		context.remove("maklumatBil");
		
		try {			
			mp = new MyPersistence();
			
			String noAkaun = getParam("noAkaun");
			if (!"".equals(noAkaun)) {
				
				DaftarBil maklumatBil = (DaftarBil) mp.get("SELECT mb FROM DaftarBil mb WHERE mb.noAkaun = '" + noAkaun + "'");
				if(maklumatBil == null){
					context.put("maklumatBil", "");
				}else{
					List<BayaranBil> bayaranBil = mp.list("SELECT bb FROM BayaranBil bb WHERE bb.pendaftaranBil.id = '" + maklumatBil.getId() + "' ORDER BY bb.bulan DESC, bb.tahun DESC");
					if(bayaranBil.size() > 0){
						context.put("bayaranBil", bayaranBil.get(0));
					}
					context.put("maklumatBil", maklumatBil);
				}
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/sub_entry_page.vm";
	}
		
}

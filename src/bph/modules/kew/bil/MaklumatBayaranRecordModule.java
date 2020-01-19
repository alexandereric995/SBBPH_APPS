package bph.modules.kew.bil;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.bil.BayaranBil;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;

public class MaklumatBayaranRecordModule extends LebahRecordTemplateModule<BayaranBil>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	
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
	
		return "bph/modules/kewangan/bil/maklumatBayaran";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		this.setReadonly(true);
		this.setDisableBackButton(true);
		
		dataUtil = DataUtil.getInstance(db);		
		
		context.put("selectKodBil", dataUtil.getListJenisBil()); //dropdown Jenis Bil
		
//		this.addFilter("statusLulus = 'L'");
		
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	private void addfilter() {

		
//		this.addFilter("statusBayaran = 'T'");
		
		if ("(KEW) Penyedia".equals(userRole)){
			this.addFilter("statusBil.id IN ('1433131904921','1433131904924')"); //SEMAKAN KEWANGAN
		}
		
		if ("(KEW) Bayaran Bil".equals(userRole)){
			this.addFilter("statusBil.id IN ('1433131904921','1433131904924')"); //SEMAKAN KEWANGAN
		}

	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}

	@Override
	public void save(BayaranBil simpan) throws Exception {
		// TODO Auto-generated method stub
//		
////		simpan.setNoBil(get("noBil"));
////		simpan.setTarikhBil(getDate("tarikhBil"));
////		simpan.setTarikhTerimaBil(getDate("tarikhTerimaBil"));
////		simpan.setAmaunTunggakan(Double.valueOf(Util.RemoveComma(get("amaunTunggakan"))));
////		simpan.setAmaunSemasa(Double.valueOf(Util.RemoveComma(get("amaunSemasa"))));		
////		simpan.setJumlahBil(Double.valueOf(util.RemoveComma(get("jumlahBil"))));		
////		simpan.setTarikhAkhirBayaran(getDate("tarikhAkhirBayaran"));
//		simpan.setTarikhBayaran(getDate("tarikhBayaran"));
//		simpan.setAmaunBayaran(Double.valueOf(util.RemoveComma(get("amaunBayaran"))));		
//		simpan.setNoRujukan(get("noRujukan"));
//		simpan.setNoEftBayaran(get("noEftBayaran"));
//		simpan.setCatatan(get("catatan"));
//		
//		if (getParamAsDouble("amaunBayaran") == getParamAsDouble("jumlahBil")) {
//			simpan.setStatusBayaran("Y");
//		} else {
//			simpan.setStatusBayaran("T");
//		}
		
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
			
	}
	
	@Command("saveMaklumatBayaran")
	public String saveMaklumatBayaran() throws Exception {		
		
		//MAKLUMAT BAYARAN MODULE KEWANGAN	
		BayaranBil simpan = db.find(BayaranBil.class, get("idMaklumatBil"));
		simpan.setTarikhBayaran(getDate("tarikhBayaran"));
		simpan.setAmaunBayaran(Double.valueOf(util.RemoveComma(get("amaunBayaran"))));		
		simpan.setNoRujukan(get("noRujukan"));
		simpan.setNoEftBayaran(get("noEftBayaran"));
		simpan.setCatatan(get("catatan"));
		simpan.setStatusBil(db.find(Status.class, "1433131904924")); //LULUS		
		simpan.setTarikhTerimaBilKewangan(getDate("tarikhTerimaBilKewangan"));
		simpan.setTarikhBaucer(getDate("tarikhBaucer"));
		simpan.setTarikhEft(getDate("tarikhEft"));
		
		simpan.setStatusBayaran("Y");
//		if (getParamAsDouble("amaunBayaran") == getParamAsDouble("jumlahBil")) {
//			simpan.setStatusBayaran("Y");
//		} else {
//			simpan.setStatusBayaran("T");
//		}
		
		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return getPath() + "/entry_page.vm";		
	}
	
}

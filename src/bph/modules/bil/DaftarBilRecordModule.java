package bph.modules.bil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.bil.DaftarBil;
import bph.entities.kod.Bandar;
import bph.entities.kod.KodBil;
import bph.entities.kod.Negeri;
import bph.entities.kod.Seksyen;
import bph.entities.rpp.RppPeranginan;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class DaftarBilRecordModule extends LebahRecordTemplateModule<DaftarBil>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<DaftarBil> getPersistenceClass() {
		// TODO Auto-generated method stub
		return DaftarBil.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/bil/maklumatBil";
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		this.setDisableSaveAddNewButton(true);
		
		dataUtil = DataUtil.getInstance(db);
		
		context.put("selectKodBil", dataUtil.getListJenisBil()); //dropdown Jenis Bil
		context.put("selectSeksyen", dataUtil.getListSeksyen()); //dropdown Seksyen
		context.put("selectPeranginan", dataUtil.getListPeranginanRpp()); //peranginan
		
		//start dropdown Negeri
		context.remove("selectNegeri");
		List<Negeri> negeriList = dataUtil.getListNegeri();
		context.put("selectNegeri", negeriList);
		//end dropdown Negeri
		
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}

	@Override
	public void save(DaftarBil simpan) throws Exception {
		// TODO Auto-generated method stub
		try {			
			mp = new MyPersistence();
			mp.begin();
	//			System.out.println("dalam save =============== " + getParam("idSeksyen"));
				simpan.setJenisBil((KodBil) mp.find(KodBil.class, get("idKodBil"))); //yang ni telah direname kepada jenis Bil
				simpan.setPenerimaBayaran(get("penerimaBayaran"));  //telah direname Bayaran Kepada
				simpan.setNoAkaun(get("noAkaun"));
				simpan.setAlamat1(get("alamat1"));
				simpan.setAlamat2(get("alamat2"));
				simpan.setAlamat3(get("alamat3"));
				simpan.setPoskod(get("poskod"));
				simpan.setBandar((Bandar) mp.find(Bandar.class, get("idBandar")));
				simpan.setCatatan(get("catatan"));
				simpan.setSeksyen((Seksyen) mp.find(Seksyen.class, getParam("idSeksyen")));
	//			simpan.setIdPeranginan(get("idJenisPeranginan"));
				simpan.setIdPeranginan((RppPeranginan) mp.find(RppPeranginan.class, get("idPeranginan")));
				simpan.setNamaPegawai(get("namaPegawai"));
				simpan.setStatus(get("idStatus"));
			mp.persist(simpan);
			mp.commit();
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		
	}

	@Override
	public boolean delete(DaftarBil r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub

		String findKodBil = getParam("findKodBil");
		String findPenerimaBayaran = getParam("find_penerima_bayaran");
		String findNoAkaun = getParam("find_no_akaun");
		String findNegeri = getParam("find_negeri");
		String findBandar = getParam("find_bandar");
		String findStatus = getParam("find_status");
		
		HashMap<String, Object> cari = new HashMap<String, Object>();
		cari.put("jenisBil.id", new OperatorEqualTo(findKodBil));
		cari.put("penerimaBayaran", findPenerimaBayaran);
		cari.put("noAkaun", findNoAkaun);
		cari.put("bandar.negeri.id", new OperatorEqualTo(findNegeri));
		cari.put("bandar.id", new OperatorEqualTo(findBandar));
		cari.put("status", findStatus);
		
		return cari;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(DaftarBil r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(DaftarBil r) {
		// TODO Auto-generated method stub
		
		//for dropdown Bandar papar balik bila tekan button edit
		String idNegeri = "";
		if (r.getBandar() != null && r.getBandar().getNegeri().getId().trim().length() > 0){
			idNegeri = r.getBandar().getNegeri().getId();
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		//end
		
		//for dropdown Peranginan
		List<RppPeranginan> listPeranginan = dataUtil.getListPeranginanRpp();
		context.put("selectPeranginan", listPeranginan);
		//end
	}

	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}
	
	//START Carian By Status
	@Command("findStatus")
	public String findStatus() throws Exception {

		String idStatus = "0";
		if (getParam("selectStatus").trim().length() > 0){
			idStatus = getParam("selectStatus");
		}
		context.put("status", idStatus);

		return getPath() + path + "/findStatus.vm";
	}
	//END Carian By Status
	
	//START Carian By Jenis Bil
	@Command("findKodBil")
	public String findKodBil() throws Exception {
		
		String idKodBil = "0";
		if (get("findKodBil").trim().length() > 0){
			idKodBil = get("findKodBil");
		}
		
		List<KodBil> list = dataUtil.getListJenisBil();
		context.put("selectKodBil", list);

		return getPath() + "/findKodBil.vm";
	}
	//END Carian By Jenis Bil

	
	//START Carian By Bandar
	@Command("findBandar")
	public String findBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0){
			idNegeri = get("find_negeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/findBandar.vm";
	}
	//END Carian By Bandar
	
	//START dropdown Bandar
	@Command("selectBandar")
	public String selectBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0){
			idNegeri = get("idNegeri");
		}
			
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/selectBandar.vm";
	}
	//END dropdown Bandar
	
	@Command("selectSeksyen")
	public String selectSeksyen() throws Exception {
		
		String idSeksyen = "0";
		if (getParam("idSeksyen").trim().length() > 0) {
			idSeksyen = getParam("idSeksyen");
		}			

		context.put("idSeksyen", idSeksyen);
		
		return getPath() + "/fieldPeranginan.vm";
	}
	
}

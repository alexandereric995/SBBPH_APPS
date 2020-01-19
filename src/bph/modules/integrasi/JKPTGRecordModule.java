package bph.modules.integrasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.integrasi.IntJKPTG;
import bph.entities.kod.Agensi;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisHakmilik;
import bph.entities.kod.KategoriTanah;
import bph.entities.kod.Kementerian;
import bph.entities.kod.Lot;
import bph.entities.kod.Luas;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.entities.kod.SubKategoriTanah;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class JKPTGRecordModule extends LebahRecordTemplateModule<IntJKPTG>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("JKPTGRecordModule"); 
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntJKPTG r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", userRole);
		
		context.remove("selectZon");
		context.remove("selectNegeri");
		context.remove("selectKementerian");
		context.remove("selectJenisHakmilik");
		context.remove("selectJenisLot");
		context.remove("info");
		context.remove("statusInfo");

		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectJenisLot", dataUtil.getListLot());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectZon", dataUtil.getListZon());
		context.put("selectKementerian", dataUtil.getListKementerian()); //LIST DROPDOWN KEMENTERIAN
		context.put("selectJenisLuas", dataUtil.getListLuas());
		context.put("selectKategoriTanah", dataUtil.getListKategoriTanah());
		context.put("selectSubKategoriTanah", dataUtil.getListSubkategori());
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectLot", dataUtil.getListLot());

		Users users = db.find(Users.class, userId);
		context.put("users", users);
		context.put("path", getPath());
		context.put("util", util);
		context.put("command", command);
		
		defaultButtonOption();
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
//		if (!"add_new_record".equals(command)) {
//			this.setDisableBackButton(true);
//			this.setDisableDefaultButton(true);
//		}
	}

	@Override
	public boolean delete(IntJKPTG r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/JKPTG";
	}

	@Override
	public Class<IntJKPTG> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntJKPTG.class;
	}

	@Override
	public void getRelatedData(IntJKPTG r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(IntJKPTG r) throws Exception {
		// TODO Auto-generated method stub
		r.setNoFail(getParam("noFail"));
		r.setKementerian(db.find(Kementerian.class, getParam("idKementerian")));
		r.setAgensi(db.find(Agensi.class, getParam("idAgensi")));
		r.setStatusDaftar(getParam("statusDaftar"));
		r.setNegeri(db.find(Negeri.class, getParam("idNegeri")));
		r.setDaerah(db.find(Daerah.class, getParam("idDaerah")));
		r.setMukim(db.find(Mukim.class, getParam("idMukim")));
		r.setLokasi(getParam("lokasi"));
		r.setPeganganHakmilik(getParam("peganganHakmilik"));
		r.setJenisHakmilik(db.find(JenisHakmilik.class, getParam("jenisHakmilik")));
		r.setNoHakmilik(getParam("noHakmilik"));
		r.setLot(db.find(Lot.class, getParam("lot")));
		r.setNoLot(getParam("noLot"));
		r.setNoWarta(getParam("noWarta"));
		r.setJenisLuas(db.find(Luas.class, getParam("Luas")));
		r.setLuas(getParam("luas"));
		r.setKategoriTanah(db.find(KategoriTanah.class, getParam("kategoriTanah")));
		r.setSubKategoriTanah(db.find(SubKategoriTanah.class, getParam("subKategoriTanah")));
		r.setSyarat(getParam("syarat"));
		r.setSekatan(getParam("sekatan"));
		r.setKegunaanTanah(getParam("kegunaanTanah"));	
		r.setNoPelan(getParam("noPelan"));
		r.setNoSyit(getParam("noSyit"));
		r.setNoPu(getParam("noPu"));
		r.setTarikhDaftar(getDate("tarikhDaftar"));
		r.setTarikhLuput(getDate("tarikhLuput"));
		r.setTarafHakmilik(getParam("tarafHakmilik"));
		r.setCukai(Double.parseDouble(util.RemoveComma(getParam("cukai"))));
		r.setCukaiTerkini(Double.parseDouble(util.RemoveComma(getParam("cukaiTerkini"))));
		r.setHakmilikBerikut(getParam("hakmilikBerikut"));

		// tak pakai untuk module ini hanya pakai di tanah dan bangunan
//		r.setIdMasuk(db.find(Users.class, getParam("idMasuk")));
//		r.setTarikhMasuk(new Date());
//		r.setFlagAktif("Y");
//		r.setHakmilikAsal(getParam("hakmilikAsal"));
//		r.setCukaiTerkini(getParamAsDouble("cukaiTerkini"));
//		r.setCukai(getParamAsDouble("cukai"));
//		r.setTarikhWarta(getDate("tarikhWarta"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("peganganHakmilik", get("findPeganganHakmilik"));
		m.put("jenisHakmilik.id", new OperatorEqualTo(get("findJenisHakmilik")));
		m.put("noHakmilik", get("findNoHakmilik"));
		m.put("lot.id", new OperatorEqualTo(get("findJenisLot")));
		m.put("noLot", get("findNoLot"));

		m.put("lokasi", get("findLokasi"));
		m.put("mukim.daerah.negeri.id", new OperatorEqualTo(get("findNegeri")));
		m.put("mukim.daerah.negeri.zon.id", get("findZon"));
		m.put("mukim.daerah.id", new OperatorEqualTo(get("findDaerah")));
		m.put("mukim.id", new OperatorEqualTo(get("findMukim")));

		return m;
	}
	
//	@Command("kemaskiniMaklumatBPP")
//	public String kemaskiniMaklumatBPP() throws Exception {
//		
////		Date tarikhTerima = getDate("tarikhTerima");
//		
//		//start
//		IntBPP simpan = db.find(IntBPP.class, get("idBPP"));
////		System.out.println("masuk savee=======" + get("idBPP"));
//		
//		if(simpan == null){
//			simpan = new IntBPP();
//		}
//		
//		db.begin();
//		simpan.setTarikhTerima(getDate("tarikhTerima"));
//		simpan.setFlagJawapan(get("flagJawapan"));
//		simpan.setMesej(get("mesej"));
//		simpan.setKodJenisPinjaman(get("kodJenisPinjaman"));
//		simpan.setNama(get("nama"));
////		simpan.setMukim(db.find(Mukim.class, get("findMukim")));
////		simpan.setDaerah(db.find(Daerah.class, get("findDaerah")));
////		simpan.setNegeri(db.find(Negeri.class, get("findNegeri")));
//		simpan.setNoAkaun(get("noAkaun"));
//		simpan.setStatusPinjaman(get("statusPinjaman"));
//		
//		db.persist(simpan);
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//end
//		
//		return getPath() + "/maklumatBPP.vm";
//	}
//	
//	
//	@Command("findDaerah")
//	public String findDaerah() throws Exception {	
//		
//		String idNegeri = "0";
//		if (get("findNegeri").trim().length() > 0)
//			idNegeri = get("findNegeri");
//		List<Daerah> list = du.getListDaerah(idNegeri);
//		context.put("selectDaerah", list);
//		
//		return getPath() + "/findDaerah.vm";
//	}
//	
//	@Command("findMukim")
//	public String findMukim() throws Exception {
//		
//		String idDaerah = "0";
//		if (get("findDaerah").trim().length() > 0)
//			idDaerah = get("findDaerah");
//	
//		List<Mukim> list = du.getListMukim(idDaerah);
//		context.put("selectMukim", list);
//
//		return getPath() + "/findMukim.vm";
//	}

	/******************************************* START DROPDOWN LIST *******************************************/
	@Command("findNegeri")
	public String findNegeri() throws Exception {

		String idZon = "0";
		if (get("findZon").trim().length() > 0)
			idZon = get("findZon");
		List<Negeri> list = dataUtil.getListNegeri(idZon);
		context.put("selectNegeri", list);

		return getPath() + "/select/findNegeri.vm";
	}

	@Command("findDaerah")
	public String findDaerah() throws Exception {

		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);

		return getPath() + "/select/findDaerah.vm";
	}

	@Command("findMukim")
	public String findMukim() throws Exception {

		String idDaerah = "0";
		if (get("findDaerah").trim().length() > 0)
			idDaerah = get("findDaerah");

		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/select/findMukim.vm";
	}

	@Command("findAgensi")
	public String findAgensi() throws Exception {

		String idKementerian = "0";
		if (get("findKementerian").trim().length() > 0)
			idKementerian = get("findKementerian");

		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/select/findAgensi.vm";
	}
	
	/** SELECT DAERAH **/
	@Command("selectDaerah")
	public String selectDaerah() throws Exception {
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0){
			idNegeri = get("idNegeri");
		}
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);

		return getPath() + "/select/selectDaerah.vm";
	}
	
	/** SELECT MUKIM **/
	@Command("selectMukim")
	public String selectMukim() throws Exception {

		String idDaerah = "0";
		if (get("idDaerah").trim().length() > 0){
			idDaerah = get("idDaerah");
		}
		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/select/selectMukim.vm";
	}
	
	/** SELECT AGENSI **/
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		
		String idKementerian = "0";
		
		if (get("idKementerian").trim().length() > 0){
			idKementerian = get("idKementerian");
		}
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/select/selectAgensi.vm";
	}
	/******************************************* END DROPDOWN LIST *******************************************/

	
}

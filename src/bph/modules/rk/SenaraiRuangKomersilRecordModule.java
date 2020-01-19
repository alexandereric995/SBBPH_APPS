/**
 * Author : zulfazdliabuas@gmail.com
 * Date : 2016
 */

package bph.modules.rk;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisKegunaanRuang;
import bph.entities.kod.Negeri;
import bph.entities.kod.Seksyen;
import bph.entities.rk.RkDokumen;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkPerjanjian;
import bph.entities.rk.RkRuangKomersil;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiRuangKomersilRecordModule extends LebahRecordTemplateModule<RkRuangKomersil> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;
	
	@Override
	public Class getIdType() {
		return String.class;
	}
	
	@Override
	public Class<RkRuangKomersil> getPersistenceClass() {
		return RkRuangKomersil.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/rk/senaraiRuangKomersil";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectJenisKegunaanRuang", dataUtil.getListJenisKegunaanRuang());
		context.put("selectSeksyen", dataUtil.getListSeksyen());
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		context.put("util", util);
		
		boolean showSeksyen = true;
		context.put("showSeksyen", showSeksyen);
		context.remove("idSeksyen");

		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
			
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	private void addfilter() {
		this.setOrderBy("idRuang");
		this.setOrderType("asc");		
	}	

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void save(RkRuangKomersil r) throws Exception {
		String idRuang = "";
		String idSeksyen = getParam("idSeksyen");
		String idNegeri = getParam("idNegeri");
		String idJenisKegunaanRuang = getParam("jenisKegunaanRuang");
		
		if ("06".equals(idSeksyen)) {
			idRuang = "BGS";
		} else if ("07".equals(idSeksyen)) {
			idRuang = "UKK";
		} else {
			idRuang = "BPH";
		}
		
		Negeri negeri = (Negeri) db.find(Negeri.class, idNegeri);
		if (negeri != null) { 
			idRuang = idRuang + "/" + negeri.getAbbrev(); 
		} 
		
		JenisKegunaanRuang jenisKegunaanRuang = (JenisKegunaanRuang) db.find(JenisKegunaanRuang.class, idJenisKegunaanRuang);
		if (jenisKegunaanRuang != null) { 
			idRuang = idRuang + "/" + jenisKegunaanRuang.getKod(); 
		}
		int turutan = generateIdRuang(negeri, jenisKegunaanRuang, idSeksyen);
		idRuang = idRuang + "/" + new DecimalFormat("000").format(turutan); 
		r.setIdRuang(idRuang);
		r.setNamaRuang(getParam("namaRuang"));
		r.setAlamat1(getParam("alamat1"));
		r.setAlamat2(getParam("alamat2"));
		r.setAlamat3(getParam("alamat3"));
		r.setPoskod(getParam("poskod"));
		r.setBandar((Bandar) db.find(Bandar.class, getParam("idBandar")));
		r.setLokasi(getParam("lokasi"));		
		r.setSeksyen((Seksyen) db.find(Seksyen.class, get("idSeksyen")));
		r.setNamaPegawai(getParam("namaPegawai"));		
		r.setNoTelefonPegawai(getParam("noTelefonPegawai"));		
		r.setJenisKegunaanRuang((JenisKegunaanRuang) db.find(JenisKegunaanRuang.class, getParam("jenisKegunaanRuang")));
		if ("10".equals(getParam("jenisKegunaanRuang"))) {
			r.setJenisKegunaanLain(getParam("jenisKegunaanLain"));
		} else {
			r.setJenisKegunaanLain(null);
		}		
		r.setJenisMeterAir(getParam("jenisMeterAir"));
		r.setJenisMeterElektrik(getParam("jenisMeterElektrik"));
		r.setJenisMeterIWK(getParam("jenisMeterIWK"));
		r.setIdJenisSewa(getParam("idJenisSewa"));
		r.setKadarSewa(Double.valueOf(util.RemoveComma(getParam("kadarSewa"))));
		if (!"".equals(getParam("luasKps"))) {
			r.setLuasKps(Double.valueOf(util.RemoveComma(getParam("luasKps"))));
		} else {
			r.setLuasKps(0D);
		}
		if (!"".equals(getParam("luasMps"))) {
			r.setLuasMps(Double.valueOf(util.RemoveComma(getParam("luasMps"))));
		} else {
			r.setLuasMps(0D);
		}
		r.setCatatan(getParam("catatan"));
		r.setFlagSewa("T");
		r.setFlagAktif(getParam("flagAktif"));
		r.setFlagSyspintar("T");
		r.setTurutan(turutan);
		r.setDaftarOleh((Users) db.find(Users.class, userId));				
	}	
	
	private int generateIdRuang(Negeri negeri, JenisKegunaanRuang jenisKegunaanRuang, String idSeksyen) {
		int counter = 1;
		try {
			mp = new MyPersistence();
			
			if (negeri != null && jenisKegunaanRuang != null) {
				List<RkRuangKomersil> listRuang = mp.list("select x from RkRuangKomersil x where x.bandar.negeri.id = '" + negeri.getId() + "'"
						+ " and x.jenisKegunaanRuang.id = '" + jenisKegunaanRuang.getId() + "' order by x.turutan asc");
				for (int i = 0; i < listRuang.size(); i++) {
					RkRuangKomersil ruang = listRuang.get(i);
					if (ruang.getTurutan() == counter) {
						counter++;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return counter;
	}

	@Override
	public void afterSave(RkRuangKomersil r) {
		context.put("selectedTab", "1");		
	}	
	
	@Override
	public boolean delete(RkRuangKomersil r) throws Exception {
		// TODO KENE CHECK NGAN DATA PERMOHONAN
		boolean allowDelete = false;
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.get("select x from RkFail x where x.ruang.id = '" + r.getId() + "'");
			if (fail == null) {
				allowDelete = true;
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		return allowDelete;
	}
	
	@Override
	public void getRelatedData(RkRuangKomersil r) {		
		List<Bandar> listBandar = null;		
		if (r.getBandar() != null) {				
			if (r.getBandar().getNegeri() != null) {
				if (r.getBandar().getNegeri().getId() != null) {
					listBandar = dataUtil.getListBandar(r.getBandar().getNegeri().getId());						
				}
			}
		}
		context.put("selectBandar", listBandar);
		context.put("selectedTab", "1");
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idRuang", get("findIdRuang").trim());		
		map.put("namaRuang", get("findNamaRuang").trim());
		map.put("jenisKegunaanRuang.id", new OperatorEqualTo(get("findJenisKegunaanRuang")));
		map.put("idJenisSewa", new OperatorEqualTo(get("findJenisSewaan")));
		map.put("lokasi", get("findLokasi").trim());		
		map.put("bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		map.put("bandar.id", new OperatorEqualTo(get("findBandar")));
		map.put("flagSewa", new OperatorEqualTo(get("findStatus")));
		
		return map;
	}
	
	@Command("doChangeJenisKegunaanRuang")
	public String doChangeJenisKegunaanRuang() throws Exception {			
		context.put("idJenisKegunaanRuang", getParam("jenisKegunaanRuang"));		
		return getPath() + "/jenisKegunaanRuangLain.vm";
	}
	
	/*********************************************** START TAB *****************************************/
	@Command("getMaklumatRuang")
	public String getMaklumatRuang() {
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();
		
			RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, get("id"));
			context.put("r", ruang);			
			if (ruang.getBandar() != null) {				
				if (ruang.getBandar().getNegeri() != null) {
					if (ruang.getBandar().getNegeri().getId() != null) {
						listBandar = dataUtil.getListBandar(ruang.getBandar().getNegeri().getId());						
					}
				}
			}
			context.put("selectBandar", listBandar);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() throws Exception {
		try {
			mp = new MyPersistence();
		
			context.put("selectJenisDokumen", dataUtil.getListJenisDokumen());
			
			RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, get("id"));
			context.put("r", ruang);
			
			List<RkDokumen> listDokumen = null;
			if (ruang != null) {
				listDokumen = mp.list("select x from RkDokumen x where x.ruang.id = '" + ruang.getId() +"'");
			}			
			context.put("listDokumen", listDokumen);
		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSejarahPenyewaan")
	public String getSejarahPenyewaan() {
		
		try {
			mp = new MyPersistence();
				
			RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, get("id"));
			context.put("r", ruang);
			
			List<RkPerjanjian> listPerjanjian = null;
			if (ruang != null) {
				listPerjanjian = mp.list("select x from RkPerjanjian x where x.idJenisPerjanjian in ('1', '2') and x.flagAktif = 'Y' and x.fail.ruang.id = '" + ruang.getId() + "' order by x.tarikhMula asc");
			}			
			context.put("listPerjanjian", listPerjanjian);		
		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	/*********************************************** END TAB *****************************************/
	
	@Command("saveMaklumatRuang")
	public String saveMaklumatRuang() throws Exception {			
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();
			
			mp.begin();
			RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, get("id"));
			if(ruang != null){
				ruang.setNamaRuang(getParam("namaRuang"));
				ruang.setAlamat1(getParam("alamat1"));
				ruang.setAlamat2(getParam("alamat2"));
				ruang.setAlamat3(getParam("alamat3"));
				ruang.setPoskod(getParam("poskod"));
				ruang.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				ruang.setLokasi(getParam("lokasi"));		
				ruang.setSeksyen((Seksyen) mp.find(Seksyen.class, get("idSeksyen")));
				ruang.setNamaPegawai(getParam("namaPegawai"));		
				ruang.setNoTelefonPegawai(getParam("noTelefonPegawai"));		
				ruang.setJenisKegunaanRuang((JenisKegunaanRuang) mp.find(JenisKegunaanRuang.class, getParam("jenisKegunaanRuang")));
				if ("10".equals(getParam("jenisKegunaanRuang"))) {
					ruang.setJenisKegunaanLain(getParam("jenisKegunaanLain"));
				} else {
					ruang.setJenisKegunaanLain(null);
				}
				ruang.setJenisMeterAir(getParam("jenisMeterAir"));
				ruang.setJenisMeterElektrik(getParam("jenisMeterElektrik"));
				ruang.setJenisMeterIWK(getParam("jenisMeterIWK"));
				ruang.setIdJenisSewa(getParam("idJenisSewa"));
				ruang.setKadarSewa(Double.valueOf(util.RemoveComma(getParam("kadarSewa"))));
				ruang.setLuasKps(Double.valueOf(util.RemoveComma(getParam("luasKps"))));
				ruang.setLuasMps(Double.valueOf(util.RemoveComma(getParam("luasMps"))));
				ruang.setCatatan(getParam("catatan"));
				ruang.setFlagAktif(getParam("flagAktif"));
				ruang.setFlagSyspintar("T");
				ruang.setKemaskiniOleh((Users) mp.find(Users.class, userId));	
				ruang.setTarikhKemaskini(new Date());
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatRuang();		
	}
	
	/*************************************** START DOKUMEN SOKONGAN ***************************************/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {		
		String id = get("id");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		RkDokumen dokumen = new RkDokumen();
		
		String uploadDir = "rk/ruang/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists()){
			dir.mkdir();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			String imgName = uploadDir + id + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf(".")) + "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanDokumen(id, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen);
			}
		}
				
		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idRuangKomersil, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, RkDokumen dokumen)
			throws Exception {
		
		try {			
			mp = new MyPersistence();
			
			RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, idRuangKomersil);			
			if (ruang != null) {
				mp.begin();
				dokumen.setRuang(ruang);
				dokumen.setPhotofilename(imgName);
				dokumen.setThumbfilename(avatarName);
				dokumen.setTajuk(tajukDokumen);
				dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
				dokumen.setKeterangan(keteranganDokumen);
				mp.persist(dokumen);
				mp.commit();
			}
		} catch (Exception ex) {
			System.out.println("ERROR simpanDokumen : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
			
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = get("idDokumen");
		
		try {
		mp = new MyPersistence();
			RkDokumen dokumen = (RkDokumen) mp.find(RkDokumen.class, idDokumen);
	
			if (dokumen != null) {
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.begin();
				mp.remove(dokumen);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getDokumenSokongan();
	}
	
	@Command("refreshList")
	public String refreshList() throws Exception { 
		return getDokumenSokongan(); 
	}	
	/*************************************** END DOKUMEN SOKONGAN ***************************************/
	
	/** START DROP DOWN LIST **/
	@Command("findBandar")
	public String findBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/selectBandar.vm";
	}
	/** END DROP DOWN LIST **/
}
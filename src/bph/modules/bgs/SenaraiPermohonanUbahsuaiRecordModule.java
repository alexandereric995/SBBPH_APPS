package bph.modules.bgs;

import java.io.File;
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
import bph.entities.bgs.BgsDokumen;
import bph.entities.bgs.BgsPermohonanUbahSuai;
import bph.entities.kod.Agensi;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Mukim;
import bph.entities.kod.Status;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevPremis;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiPermohonanUbahsuaiRecordModule extends LebahRecordTemplateModule<BgsPermohonanUbahSuai> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<BgsPermohonanUbahSuai> getPersistenceClass() {
		// TODO Auto-generated method stub
		return BgsPermohonanUbahSuai.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/bgs/permohonanUbahsuai";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");		
		context.put("userRole", userRole);
		
		//LIST DROPDOWN
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("selectNegeri", dataUtil.getListNegeri());
				
		defaultButtonOption();
		addfilter(userId, userRole);
				
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);		
	}
	
	private void addfilter(String userId, String userRole) {
		
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(BgsPermohonanUbahSuai r) throws Exception {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void afterSave(BgsPermohonanUbahSuai r) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void getRelatedData(BgsPermohonanUbahSuai r) {
		// TODO Auto-generated method stub		
		context.put("selectedTab", 1);
	}

	@Override
	public boolean delete(BgsPermohonanUbahSuai r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("noFail", getParam("findNoFail"));
		
		m.put("permohonan.jabatan", getParam("findJabatan"));
		m.put("permohonan.agensi.kementerian.id", new OperatorEqualTo(getParam("findKementerian")));
		m.put("permohonan.agensi.id", new OperatorEqualTo(getParam("findAgensi")));
		
		m.put("permohonan.bangunan.premis.mukim.daerah.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		m.put("permohonan.bangunan.premis.mukim.daerah.id", new OperatorEqualTo(getParam("findDaerah")));
		m.put("permohonan.bangunan.premis.mukim.id", new OperatorEqualTo(getParam("findMukim")));
		m.put("permohonan.bangunan.premis.id", new OperatorEqualTo(getParam("findPremis")));
		m.put("permohonan.bangunan.id", new OperatorEqualTo(getParam("findBangunan")));		

		return m;
	}
	
	/** START TAB **/
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan() {
		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		context.put("r", r);
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getUlasanPengurusBangunan")
	public String getUlasanPengurusBangunan() {
		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		context.put("r", r);
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getUlasanKKR")
	public String getUlasanKKR() {
		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		context.put("r", r);
		
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		context.put("r", r);
		
		List<JenisDokumen> list = dataUtil.getListJenisDokumen();
		context.put("selectJenisDokumen", list);

		List<BgsDokumen> listDokumen = db.list("SELECT x FROM BgsDokumen x WHERE x.permohonanUbahsuai.id= '" + r.getId() + "'");
		context.put("listDokumen", listDokumen);
		
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSemakanKelulusan")
	public String getSemakanKelulusan() {
		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		context.put("r", r);
		
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START MAKLUMAT PERMOHONAN **/
	@Command("saveMaklumatPermohonan")
	public String saveMaklumatPermohonan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, getParam("idPermohonan"));
		
		db.begin();
		r.setTarikhPermohonan(getDate("tarikhPermohonan"));
		r.setTujuan(get("tujuan"));

		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getMaklumatPermohonan();
	}
	/** END MAKLUMAT PERMOHONAN **/
	
	/** START ULASAN PENGURUS BANGUNAN **/
	@Command("saveUlasanPengurusBangunan")
	public String saveUlasanPengurusBangunan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, getParam("idPermohonan"));
		
		db.begin();
		r.setTarikhUlasanPengurusBangunan(getDate("tarikhUlasanPengurusBangunan"));
		r.setUlasanPengurusBangunan(get("ulasanPengurusBangunan"));

		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getUlasanPengurusBangunan();
	}
	/** END ULASAN PENGURUS BANGUNAN **/
	
	/** START ULASAN KKR **/
	@Command("saveUlasanKKR")
	public String saveUlasanKKR() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";		
		BgsPermohonanUbahSuai r = db.find(BgsPermohonanUbahSuai.class, getParam("idPermohonan"));
		
		db.begin();
		r.setTarikhUlasanKKR(getDate("tarikhUlasanKKR"));
		r.setUlasanKKR(get("ulasanKKR"));

		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getUlasanKKR();
	}
	/** END ULASAN KKR **/
	
	/** START DOKUMEN SOKONGAN **/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idPermohonan = get("idPermohonan");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		BgsDokumen dokumen = new BgsDokumen();
		String uploadDir = "bgs/permohonanUbahSuaiRuangPejabat/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

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
			String imgName = uploadDir + idPermohonan + "_" + dokumen.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar"
						+ imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName,
						600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
						150, 90, 100);
			}

			if (!imgName.equals("")) {
				simpanDokumen(idPermohonan, imgName, avatarName, tajukDokumen,
						idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumen(String idPermohonan, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen,
			String keteranganDokumen, BgsDokumen dokumen) throws Exception {

		dokumen.setPermohonanUbahsuai(db.find(BgsPermohonanUbahSuai.class, idPermohonan));
		dokumen.setPhotofilename(imgName);
		dokumen.setThumbfilename(avatarName);
		dokumen.setTajuk(tajukDokumen);
		dokumen.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		dokumen.setKeterangan(keteranganDokumen);

		db.begin();
		db.persist(dokumen);
		db.commit();
	}

	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = get("idDokumen");
		BgsDokumen dokumen = db.find(BgsDokumen.class, idDokumen);

		if (dokumen != null) {
			Util.deleteFile(dokumen.getPhotofilename());
			Util.deleteFile(dokumen.getThumbfilename());
			db.begin();
			db.remove(dokumen);
			db.commit();
		}

		return getDokumenSokongan();
	}

	@Command("refreshList")
	public String refreshList() throws Exception {

		return getDokumenSokongan();
	}
	/** END DOKUMEN SOKONGAN **/
	
	/** START SEMAKAN KELULUSAN**/
	@Command("hantarSemakan")
	public String hantarSemakan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		Users penyedia = db.find(Users.class, userId);
		
		db.begin();		
		BgsPermohonanUbahSuai permohonan = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		permohonan.setStatus(db.find(Status.class, "1423568441683")); // SEMAKAN
		permohonan.setPenyedia(penyedia);
		permohonan.setUlasanPenyedia(get("catatanPenyedia"));
		
		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "PERMOHONAN TELAH DIHANTAR UNTUK SEMAKAN";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DIHANTAR UNTUK SEMAKAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKelulusan();
	}
	
	@Command("semakanTidakSokong")
	public String semakanTidakSokong() throws Exception {		
		return getSemakanKelulusan();
	}
	
	@Command("semakanSokong")
	public String semakanSokong() throws Exception {		
		return getSemakanKelulusan();
	}
	
	@Command("keputusanLulus")
	public String keputusanLulus() throws Exception {		
		return getSemakanKelulusan();
	}
	
	@Command("keputusanTolak")
	public String keputusanTolak() throws Exception {		
		return getSemakanKelulusan();
	}
	
	@Command("simpanSokong")
	public String simpanSokong() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		Users penyemak = db.find(Users.class, userId);
		
		db.begin();		
		BgsPermohonanUbahSuai permohonan = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		permohonan.setStatus(db.find(Status.class, "1423568441684")); // KEPUTUSAN
		permohonan.setPenyemak(penyemak);
		permohonan.setTarikhSemakan(new Date());
		permohonan.setUlasanPenyemak(get("catatan"));
		permohonan.setFlagKeputusanPenyemak("Y");
		
		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "PERMOHONAN TELAH DIHANTAR UNTUK KEPUTUSAN";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DIHANTAR UNTUK KEPUTUSAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKelulusan();
	}
	
	@Command("simpanTidakSokong")
	public String simpanTidakSokong() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		Users penyemak = db.find(Users.class, userId);
		
		db.begin();		
		BgsPermohonanUbahSuai permohonan = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		permohonan.setStatus(db.find(Status.class, "1423568441684")); // KEPUTUSAN
		permohonan.setPenyemak(penyemak);
		permohonan.setTarikhSemakan(new Date());
		permohonan.setUlasanPenyemak(get("catatan"));
		permohonan.setFlagKeputusanPenyemak("T");
		
		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "PERMOHONAN TELAH DIHANTAR UNTUK KEPUTUSAN";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DIHANTAR UNTUK KEPUTUSAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKelulusan();
	}
	
	@Command("simpanLulus")
	public String simpanLulus() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		Users pelulus = db.find(Users.class, userId);
		
		db.begin();		
		BgsPermohonanUbahSuai permohonan = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		permohonan.setStatus(db.find(Status.class, "1423568441697")); // LULUS
		permohonan.setPelulus(pelulus);
		permohonan.setTarikhKelulusan(new Date());
		permohonan.setUlasanPelulus(get("catatan"));
		permohonan.setFlagKeputusanPelulus("Y");
		
		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "PERMOHONAN TELAH BERJAYA DILULUSKAN";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DILULUSKAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKelulusan();
	}
	
	@Command("simpanTolak")
	public String simpanTolak() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		Users pelulus = db.find(Users.class, userId);
		
		db.begin();		
		BgsPermohonanUbahSuai permohonan = db.find(BgsPermohonanUbahSuai.class, get("idPermohonan"));
		permohonan.setStatus(db.find(Status.class, "1423568441700")); // TOLAK
		permohonan.setPelulus(pelulus);
		permohonan.setTarikhKelulusan(new Date());
		permohonan.setUlasanPelulus(get("catatan"));
		permohonan.setFlagKeputusanPelulus("T");
		
		try {
			db.commit();
			flagStatusInfo = "Y";
			statusInfo = "PERMOHONAN TELAH DITOLAK";
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DITOLAK";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKelulusan();
	}
	/** END SEMAKAN KELULUSAN KERTAS PERTIMBANGAN **/
	
	/** START DROP DOWN **/
	@Command("findAgensi")
	public String findAgensi() throws Exception {
		String idKementerian = "0";
		if (get("findKementerian").trim().length() > 0)
			idKementerian = get("findKementerian");
		
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/findAgensi.vm";
	}
	
	@Command("findDaerah")
	public String findDaerah() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);

		return getPath() + "/findDaerah.vm";
	}
	
	@Command("findMukim")
	public String findMukim() throws Exception {
		String idDaerah = "0";
		if (get("findDaerah").trim().length() > 0)
			idDaerah = get("findDaerah");
		
		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/findMukim.vm";
	}
	
	@Command("findPremis")
	public String findPremis() throws Exception {
		String idMukim = "0";
		if (get("findMukim").trim().length() > 0)
			idMukim = get("findMukim");
		
		List<DevPremis> list = dataUtil.getListPremisBGS(idMukim);
		context.put("selectPremis", list);

		return getPath() + "/findPremis.vm";
	}
	
	@Command("findBangunan")
	public String findBangunan() throws Exception {
		String idPremis = "0";
		if (get("findPremis").trim().length() > 0)
			idPremis = get("findPremis");
		
		List<DevBangunan> list = dataUtil.getListBangunanBGS(idPremis);
		context.put("selectBangunan", list);

		return getPath() + "/findBangunan.vm";
	}
	/** END DROP DOWN **/
}

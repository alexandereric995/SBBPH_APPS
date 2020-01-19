package bph.modules.bgs;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
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

import bph.entities.bgs.BgsAras;
import bph.entities.bgs.BgsDokumen;
import bph.entities.bgs.BgsPenghuni;
import bph.entities.bgs.BgsPremis;
import bph.entities.kod.Agensi;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Mukim;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiArasBGSRecordModule extends LebahRecordTemplateModule<BgsAras> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(BgsAras aras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);	
		context.put("selectNegeri", dataUtil.getListNegeri());
		this.setReadonly(true);
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);
	}

	@Override
	public boolean delete(BgsAras aras) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/bgs/senaraiArasBGS";
	}

	@Override
	public Class<BgsAras> getPersistenceClass() {
		// TODO Auto-generated method stub
		return BgsAras.class;
	}

	@Override
	public void getRelatedData(BgsAras aras) {
		context.put("selectedTab", "1");
	}

	@Override
	public void save(BgsAras aras) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("aras.bangunan.premis.namaPremis", getParam("findPremis"));
		map.put("aras.bangunan.namaBangunan", getParam("findBangunan"));
		map.put("aras.namaAras", getParam("findAras"));		
		map.put("aras.bangunan.premis.mukim.daerah.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		map.put("aras.bangunan.premis.mukim.daerah.id",  new OperatorEqualTo(getParam("findDaerah")));
		map.put("aras.bangunan.premis.mukim.id",  new OperatorEqualTo(getParam("findMukim")));

		return map;
	}
	
	/** START TAB **/
	@Command("getMaklumatAras")
	public String getMaklumatAras() {
		
		BgsAras aras = db.find(BgsAras.class, get("idAras"));
		context.put("r", aras);
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatKonsesi")
	public String getMaklumatKonsesi() {
		
		BgsAras aras = db.find(BgsAras.class, get("idAras"));
		context.put("r", aras);
		
		BgsPremis premis = (BgsPremis) db.get("SELECT x FROM BgsPremis x WHERE x.premis.id = '" + aras.getAras().getBangunan().getPremis().getId() + "'");
		context.put("premis", premis);
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatFail")
	public String getMaklumatFail() {
		
		BgsAras aras = db.find(BgsAras.class, get("idAras"));
		context.put("r", aras);
		
		BgsPremis premis = (BgsPremis) db.get("SELECT x FROM BgsPremis x WHERE x.premis.id = '" + aras.getAras().getBangunan().getPremis().getId() + "'");
		context.put("premis", premis);
		
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPenghuni")
	public String getMaklumatPenghuni() {
		
		BgsAras aras = db.find(BgsAras.class, get("idAras"));
		context.put("r", aras);
		
		List<BgsPenghuni> listPenghuni = db.list("SELECT x FROM BgsPenghuni x WHERE x.aras.id = '" + aras.getId() + "'");
		context.put("listPenghuni", listPenghuni);

		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() throws Exception {
		
		List<JenisDokumen> list = dataUtil.getListJenisDokumen();
		context.put("selectJenisDokumen", list);
		
		BgsAras aras = db.find(BgsAras.class, get("idAras"));
		context.put("r", aras);
		
		List<BgsDokumen> listDokumen = db.list("SELECT x FROM BgsDokumen x WHERE x.aras.id = '" + aras.getId() + "'");
		context.put("listDokumen", listDokumen);

		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START KONSESI **/
	@Command("saveKonsesi")
	public String saveKonsesi() throws Exception {	
		db.begin();
		boolean addPremis = false;
		BgsAras aras = db.find(BgsAras.class, get("idAras"));		
		BgsPremis premis = (BgsPremis) db.get("SELECT x FROM BgsPremis x WHERE x.premis.id = '" + aras.getAras().getBangunan().getPremis().getId() + "'");
		
		//MAKLUMAT PREMIS	
		if (premis == null) {
			addPremis = true;
			premis = new BgsPremis();
			premis.setPremis(aras.getAras().getBangunan().getPremis());
		}
		premis.setNamaKonsesi(get("namaKonsesi"));
		premis.setNamaPegawai(get("namaPegawai"));
		premis.setNoTelefon(get("noTelefon"));
		premis.setNoFaks(get("noFaks"));
		premis.setEmel(get("emel"));
		
		if (addPremis) {
			db.persist(premis);
		}
		
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getMaklumatKonsesi();		
	}
	/** END KONSESI **/
	
	/** START FAIL **/
	@Command("saveFail")
	public String saveFail() throws Exception {	
		db.begin();
		boolean addPremis = false;
		BgsAras aras = db.find(BgsAras.class, get("idAras"));		
		BgsPremis premis = (BgsPremis) db.get("SELECT x FROM BgsPremis x WHERE x.premis.id = '" + aras.getAras().getBangunan().getPremis().getId() + "'");
		
		//MAKLUMAT PREMIS	
		if (premis == null) {
			addPremis = true;
			premis = new BgsPremis();
			premis.setPremis(aras.getAras().getBangunan().getPremis());
		}
		premis.setNoFail(get("noFail"));
		premis.setCatatan(get("catatan"));
		
		if (addPremis) {
			db.persist(premis);
		}
		
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getMaklumatFail();		
	}
	/** END FAIL **/
	
	/** START PENGHUNI **/
	@Command("addPenghuni")
	public String addPenghuni() {
		context.put("rekod", "");
		
		dataUtil = DataUtil.getInstance(db);	
		context.put("selectKementerian", dataUtil.getListKementerian());
		
		return getPath() + "/maklumatPenghuni/popupMaklumatPenghuni.vm";
	}
	
	@Command("editPenghuni")
	public String editPenghuni() {
		context.put("rekod", "");
		
		dataUtil = DataUtil.getInstance(db);	
		context.put("selectKementerian", dataUtil.getListKementerian());
		
		BgsPenghuni penghuni = db.find(BgsPenghuni.class, get("idPenghuni"));

		if(penghuni != null){
			context.put("rekod", penghuni);
			if (penghuni.getAgensi() != null) {
				if (penghuni.getAgensi().getKementerian() != null) {
					context.put("selectAgensi", dataUtil.getListAgensi(penghuni.getAgensi().getKementerian().getId()));
				}				
			}
		}
		return getPath() + "/maklumatPenghuni/popupMaklumatPenghuni.vm";
	}
	
	@Command("savePenghuni")
	public String savePenghuni() throws ParseException {
		String statusInfo = "";
		
		BgsAras aras = db.find(BgsAras.class, get("idAras"));		
		BgsPenghuni penghuni = db.find(BgsPenghuni.class, get("idPenghuni"));
		Boolean addPenghuni = false;
		
		if(penghuni == null){
			addPenghuni = true;
			penghuni = new BgsPenghuni();
		}
		
		penghuni.setAras(aras);
		penghuni.setAgensi(db.find(Agensi.class, getParam("idAgensi")));
		penghuni.setTarikhMasuk(getDate("tarikhMasuk"));
		penghuni.setTarikhKeluar(getDate("tarikhKeluar"));
		penghuni.setLuas(get("luas"));
		penghuni.setRuang(get("ruang"));
		penghuni.setCatatan(get("catatan"));
		
		db.begin();
		if ( addPenghuni ) db.persist(penghuni);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		
		return getMaklumatPenghuni();
	}
	
	@SuppressWarnings("unchecked")
	@Command("removePenghuni")
	public String removePenghuni() {
		String statusInfo = "";
		
		BgsPenghuni penghuni = db.find(BgsPenghuni.class, get("idPenghuni"));

		db.begin();
		db.remove(penghuni);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);		
		return getMaklumatPenghuni();
	}
	/** END PENGHUNI **/
	
	/** START DOKUMEN SOKONGAN **/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idAras = get("idAras");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		BgsDokumen dokumen = new BgsDokumen();
		String uploadDir = "bgs/aras/dokumenSokongan/";
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
			String imgName = uploadDir + idAras + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90,
						100);
			}

			if (!imgName.equals("")) {
				simpanDokumen(idAras, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idAras, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, BgsDokumen dokumen)
			throws Exception {
		
		BgsAras aras = db.find(BgsAras.class, idAras);			

		dokumen.setAras(aras);
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

	
	/** START DROPDOWN LIST **/
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
	
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {

		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");

		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/maklumatPenghuni/selectAgensi.vm";

	}
	/** END DROPDOWN LIST **/	
}

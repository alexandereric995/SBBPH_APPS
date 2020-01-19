package bph.modules.pembangunan;

import java.io.File;
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

import bph.entities.kod.Daerah;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.entities.pembangunan.DevAras;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevDokumen;
import bph.entities.pembangunan.DevPremis;
import bph.entities.pembangunan.DevRuang;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiPremisRecordModule extends LebahRecordTemplateModule<DevPremis> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8238850097171829701L;
	private DataUtil du;
	private Util util = new Util();

	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub```````````````````````````````````````
		return String.class;
	}

	@Override
	public void afterSave(DevPremis premis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		du = DataUtil.getInstance(db);
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);	
		this.setDisableBackButton(true);
		
		context.remove("selectNegeri");
		context.remove("selectZon");

		context.put("selectZon", du.getListZon());
		context.put("selectNegeri", du.getListNegeri());
		
		context.put("path", getPath());
		context.put("util", util);
		context.put("command", command);
	}

	@Override
	public boolean delete(DevPremis premis) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/pembangunan/premis";
	}

	@Override
	public Class<DevPremis> getPersistenceClass() {
		// TODO Auto-generated method stub
		return DevPremis.class;
	}

	@Override
	public void getRelatedData(DevPremis premis) {
		context.put("selectedTab", "1");
	}

	@Override
	public void save(DevPremis devHakmilik) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();		
		
		m.put("namaPremis", get("findNamaPremis"));	
		m.put("mukim.daerah.negeri.zon.id", get("findZon"));	
		m.put("mukim.daerah.negeri.id", new OperatorEqualTo(get("findNegeri")));
		m.put("mukim.daerah.id", new OperatorEqualTo(get("findDaerah")));
		m.put("mukim.id", new OperatorEqualTo(get("findMukim")));
		
		return m;
	}

	/** START SENARAI TAB **/
	@Command("getMaklumatPremis")
	public String getMaklumatPremis() {
		
		DevPremis premis = db.find(DevPremis.class, get("idPremis"));
		context.put("r", premis);

		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatBangunan")
	public String getMaklumatBangunan() {
		context.remove("selectedSubTab");
		
		List<DevBangunan> listBangunan = db.list("Select x from DevBangunan x where x.premis.id = '" + get("idPremis") + "'");
		context.put("listBangunan", listBangunan);

		context.put("selectedTab", "2");
		context.put("namaSubTab", "MAKLUMAT BANGUNAN");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDetailBangunan")
	public String getDetailBangunan() {
		
		DevBangunan bangunan = db.find(DevBangunan.class, get("idBangunan"));
		context.put("rekod", bangunan);

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatAras")
	public String getMaklumatAras() {
		
		context.remove("selectedSubSubTab");
		
		List<DevAras> listAras = db.list("Select x from DevAras x where x.bangunan.id = '" + get("idBangunan") + "'");
		context.put("listAras", listAras);

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "2");
		context.put("namaSubTab", "SENARAI ARAS");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDetailAras")
	public String getDetailAras() {
		
		DevAras aras = db.find(DevAras.class, get("idAras"));
		context.put("rekod", aras);

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "2");
		context.put("selectedSubSubTab", "1");
//		return getPath() + "/maklumatBangunan/maklumatAras/start.vm";
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getMaklumatRuang")
	public String getMaklumatRuang() {
		/*context.remove("selectedSubSubTab");
		context.remove("selectedSubSubSubTab");*/
		
		List<DevRuang> listRuang = db.list("Select x from DevRuang x where x.aras.id = '" + get("idAras") + "'");
		context.put("listRuang", listRuang);

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "2");
		context.put("selectedSubSubTab", "2");
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getDetailRuang")
	public String getDetailRuang() {
		
		DevRuang ruang = db.find(DevRuang.class, get("idRuang"));
		context.put("rekod", ruang);

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "2");
		context.put("selectedSubSubTab", "2");
		context.put("selectedSubSubSubTab", "1");
		return getPath() + "/maklumatBangunan/maklumatAras/maklumatRuang/detailMaklumatRuang.vm";
//		return getPath() + "/maklumatBangunan/senaraiSubTab.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		
		DevPremis premis = db.find(DevPremis.class, get("idPremis"));
		context.put("r", premis);
		
		List<JenisDokumen> list = du.getListJenisDokumen();
		context.put("selectJenisDokumen", list);
		
		List<DevDokumen> listDokumen = db.list("SELECT x FROM DevDokumen x WHERE x.premis.id= '" + get("idPremis") +"'");
		context.put("listDokumen", listDokumen);
		
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	/** END SENARAI TAB **/
	
	/** START DOKUMEN SOKONGAN **/
	
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idPremis = getParam("idPremis");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		DevDokumen dokumen = new DevDokumen();
		String uploadDir = "pembangunan/premis/dokumenSokongan/";
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
			String imgName = uploadDir + idPremis + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

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
				simpanDokumen(idPremis, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idPremis, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, DevDokumen dokumen)
			throws Exception {
		

		dokumen.setPremis(db.find(DevPremis.class, idPremis));
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
		DevDokumen dokumen = db.find(DevDokumen.class, idDokumen);

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
	
	/** START DROP DOWN LIST **/
	@Command("findNegeri")
	public String findNegeri() throws Exception {	
		
		String idZon = "0";
		if (get("findZon").trim().length() > 0)
			idZon = get("findZon");
		List<Negeri> list = du.getListNegeri(idZon);
		context.put("selectNegeri", list);
		
		return getPath() + "/findNegeri.vm";
	}
	
	@Command("findDaerah")
	public String findDaerah() throws Exception {	
		
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		List<Daerah> list = du.getListDaerah(idNegeri);
		context.put("selectDaerah", list);
		
		return getPath() + "/findDaerah.vm";
	}
	
	@Command("findMukim")
	public String findMukim() throws Exception {
		
		String idDaerah = "0";
		if (get("findDaerah").trim().length() > 0)
			idDaerah = get("findDaerah");
	
		List<Mukim> list = du.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/findMukim.vm";
	}
	/** END DROP DOWN LIST **/
}
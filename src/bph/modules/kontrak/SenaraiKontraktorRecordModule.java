package bph.modules.kontrak;

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

import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Negeri;
import bph.entities.kontrak.KontrakDokumen;
import bph.entities.kontrak.KontrakKontraktor;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiKontraktorRecordModule extends LebahRecordTemplateModule<KontrakKontraktor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<KontrakKontraktor> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KontrakKontraktor.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kontrak/kontraktor";
	}

	@Override
	public void begin() {
		
		this.setReadonly(true);
		this.setDisableBackButton(true);
		
		dataUtil = DataUtil.getInstance(db);		

		context.remove("selectNegeri");
		
		List<Negeri> negeriList = dataUtil.getListNegeri();
		
		context.put("selectNegeri", negeriList);
		
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		
	}

	@Override
	public void save(KontrakKontraktor kontraktor) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(KontrakKontraktor kontraktor) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findNoPendaftaran = get("findNoPendaftaran");
		String findNamaKontraktor = get("findNamaKontraktor");
		String findNamaPemilik = get("findNamaPemilik");
		String findNegeri = get("findNegeri");
		String findBandar = get("findBandar");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", findNoPendaftaran);
		map.put("namaKontraktor", findNamaKontraktor);
		map.put("namaPemilik", findNamaPemilik);
		map.put("bandar.negeri.id", new OperatorEqualTo(findNegeri));
		map.put("bandar.id", new OperatorEqualTo(findBandar));

		return map;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(KontrakKontraktor kontraktor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(KontrakKontraktor kontraktor) {
		String idNegeri = "";
		if (kontraktor.getBandar() != null && kontraktor.getBandar().getNegeri().getId().trim().length() > 0)
			idNegeri = kontraktor.getBandar().getNegeri().getId();
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		context.put("selectedTab", "1");
	}
	
	/** START TAB **/
	@Command("getMaklumatKontraktor")
	public String getMaklumatKontraktor() {
		
		try {			
			mp = new MyPersistence();
			
			KontrakKontraktor kontraktor = (KontrakKontraktor) mp.find(KontrakKontraktor.class, get("idKontraktor"));
			if (kontraktor != null) {
				List<Bandar> list = dataUtil.getListBandar(kontraktor.getBandar().getNegeri().getId());
				context.put("selectBandar", list);
				context.put("r", kontraktor);
			} else {
				context.put("selectBandar", "");
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("getLampiran")
	public String getLampiran() throws Exception {
		
		try {			
			mp = new MyPersistence();
			
			List<JenisDokumen> list = dataUtil.getListJenisDokumen();
			context.put("selectJenisDokumen", list);
			
			KontrakKontraktor kontraktor = (KontrakKontraktor) mp.find(KontrakKontraktor.class, get("idKontraktor"));	
			context.put("r", kontraktor);
			
			List<KontrakDokumen> listDokumen = mp.list("SELECT x FROM KontrakDokumen x WHERE x.kontraktor.id = '" + kontraktor.getId() +"'");
			context.put("listDokumen", listDokumen);
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	@Command("saveKontraktor")
	public String saveKontraktor() throws Exception {		
		
		try {			
			mp = new MyPersistence();
			
			mp.begin();
				//MAKLUMAT KONTRAKTOR		
				KontrakKontraktor kontraktor = (KontrakKontraktor) mp.find(KontrakKontraktor.class, get("idKontraktor"));
				kontraktor.setNamaKontraktor(get("namaKontraktor"));
				kontraktor.setNamaPemilik(get("namaPemilik"));
				kontraktor.setAlamat1(get("alamat1"));
				kontraktor.setAlamat2(get("alamat2"));
				kontraktor.setAlamat3(get("alamat3"));
				kontraktor.setPoskod(getParamAsInteger("poskod"));
				kontraktor.setBandar((Bandar) mp.find(Bandar.class, get("idBandar")));
				kontraktor.setNoTelefon(get("noTelefon"));
				kontraktor.setNoTelefonBimbit(get("noTelefonBimbit"));
				kontraktor.setNoFaks(get("noFaks"));
				kontraktor.setEmel(get("emel"));
			mp.commit();
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatKontraktor();		
	}
	
	/** START DOKUMEN SOKONGAN **/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idKontraktor = get("idKontraktor");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		KontrakDokumen dokumen = new KontrakDokumen();
		//String uploadDir = ResourceBundle.getBundle("dbconnection").getString("folder") + "kontrak/kontraktor/dokumenSokongan/";
		String uploadDir = "kontrak/kontraktor/dokumenSokongan/";
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
			String imgName = uploadDir + idKontraktor + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

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
				simpanDokumen(idKontraktor, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idKontraktor, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, KontrakDokumen dokumen)
			throws Exception {
		
		try {			
			mp = new MyPersistence();
			
			mp.begin();
				KontrakKontraktor kontraktor = (KontrakKontraktor) mp.find(KontrakKontraktor.class, idKontraktor);			
				
				dokumen.setKontraktor(kontraktor);
				dokumen.setPhotofilename(imgName);
				dokumen.setThumbfilename(avatarName);
				dokumen.setTajuk(tajukDokumen);
				dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
				dokumen.setKeterangan(keteranganDokumen);
			mp.persist(dokumen);
			mp.commit();
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		
		try {			
			mp = new MyPersistence();
			
			String idDokumen = get("idDokumen");
			KontrakDokumen dokumen = (KontrakDokumen) mp.find(KontrakDokumen.class, idDokumen);

			if (dokumen != null) {
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.begin();
				mp.remove(dokumen);
				mp.commit();
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getLampiran();
	}
	
	@Command("refreshList")
	public String refreshList() throws Exception {

		return getLampiran();
	}
	/** END DOKUMEN SOKONGAN **/
	
	/** START DROP DOWN LIST **/
	@Command("findBandar")
	public String findBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
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

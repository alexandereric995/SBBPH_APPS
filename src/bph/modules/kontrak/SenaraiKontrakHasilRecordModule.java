package bph.modules.kontrak;

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

import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisJaminan;
import bph.entities.kod.JenisKontrak;
import bph.entities.kod.KaedahPerolehan;
import bph.entities.kod.KategoriKontrak;
import bph.entities.kod.Seksyen;
import bph.entities.kod.StatusBonKontrak;
import bph.entities.kontrak.KontrakBon;
import bph.entities.kontrak.KontrakDokumen;
import bph.entities.kontrak.KontrakKontrak;
import bph.entities.kontrak.KontrakKontraktor;
import bph.entities.kontrak.KontrakMaklumatBayaran;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;


public class SenaraiKontrakHasilRecordModule extends LebahRecordTemplateModule<KontrakKontrak>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		return "bph/modules/kontrak/kontrakHasil";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectKaedahPerolehan", dataUtil.getListKaedahPerolehan());
		context.put("selectJenisKontrak", dataUtil.getListJenisKontrak());
		context.put("selectSeksyen", dataUtil.getListSeksyen());
		context.put("selectLantikanKontrak", dataUtil.getListLantikanKontrak());
		context.put("selectStatusBon", dataUtil.getListStatusBonKontrak());
		
		addfilter(userId, userRole);
		defaultButtonOption();	
		
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
		context.remove("flagAllowUpdate");
	}

	private void addfilter(String userId, String userRole) {
		// TODO Auto-generated method stub
		
	}

	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableUpperBackButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		this.addFilter("status in ('01', '03')");
		this.setOrderBy("id");
		this.setOrderType("asc");
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void save(KontrakKontrak r) throws Exception {
		
		r.setNoDaftarKontrak(getParam("noDaftarKontrak"));		
		r.setPerkhidmatan(getParam("perkhidmatan"));
		r.setJenisKontrak(db.find(JenisKontrak.class, getParam("idJenisKontrak")));
		r.setKategoriKontrak(db.find(KategoriKontrak.class, getParam("idKategoriKontrak")));
		r.setKaedahPerolehan(db.find(KaedahPerolehan.class, getParam("idKaedahPerolehan")));
		r.setKodProgram(getParam("kodProgram"));
		r.setKodObjek(getParam("kodObjek"));
		r.setIdPelaksana(getParam("idLantikan"));
		r.setSeksyen(db.find(Seksyen.class, getParam("idSeksyen")));
		
		r.setTarikhMula(getDate("tarikhMula"));
		r.setTarikhTamat(getDate("tarikhTamat"));
		r.setNilaiKontrak(Double.valueOf(util.RemoveComma(getParam("nilaiKontrak"))));
		r.setModAnsuran(getParam("modAnsuran"));
		r.setAmaunAnsuran(Double.valueOf(util.RemoveComma(getParam("amaunAnsuran"))));
		
		r.setTarikhKeluarSst(getDate("tarikhKeluarSst"));
		r.setTarikhTerimaSst(getDate("tarikhTerimaSst"));		
		r.setCatatan(getParam("catatan"));
		r.setStatus("01"); //DAFTAR KONTRAK
	
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
		if (r.getStatus().equals("01") || r.getStatus().equals("02") || r.getStatus().equals("03")) {
			context.put("flagAllowUpdate", "Y");
		} else {
			context.remove("flagAllowUpdate");
		}
		context.put("selectedTab", "1");	
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("noDaftarKontrak", getParam("findNoDaftarKontrak"));
		map.put("jenisKontrak.id", new OperatorEqualTo(getParam("findJenisKontrak")));
		map.put("kaedahPerolehan.id", new OperatorEqualTo(getParam("findKaedahPerolehan")));
		map.put("perkhidmatan", getParam("findPerkhidmatan"));
		map.put("kodProgram", getParam("findKodProgram"));
		map.put("kodObjek", getParam("findKodObjek"));
		map.put("seksyen.id", new OperatorEqualTo(getParam("findSeksyen")));
		map.put("idPelaksana", new OperatorEqualTo(getParam("findLantikan")));
		map.put("kontraktor.id", getParam("findNoPendaftaranKontraktor"));
		map.put("kontraktor.namaKontraktor", getParam("findNamaKontraktor"));
		map.put("status", new OperatorEqualTo(getParam("findStatus")));
		
		return map;
	}	
	
	/** START TAB **/
	@Command("getMaklumatKontrak")
	public String getMaklumatKontrak() {
		try {			
			mp = new MyPersistence();
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			context.put("r", kontrak);
			if (kontrak != null) {
				if (kontrak.getStatus().equals("01") || kontrak.getStatus().equals("02") || kontrak.getStatus().equals("03")) {
					context.put("flagAllowUpdate", "Y");
				} else {
					context.remove("flagAllowUpdate");
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatKontraktor")
	public String getMaklumatKontraktor() {
		context.put("selectNegeri", dataUtil.getListNegeri());
		try {			
			mp = new MyPersistence();

			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			context.put("r", kontrak);
			if (kontrak.getKontraktor() != null) {
				if (kontrak.getStatus().equals("01") || kontrak.getStatus().equals("02") || kontrak.getStatus().equals("03")) {
					context.put("flagAllowUpdate", "Y");
				} else {
					context.remove("flagAllowUpdate");
				}
				
				List<Bandar> list = dataUtil.getListBandar((kontrak.getKontraktor().getBandar().getNegeri().getId()));
				context.put("selectBandar", list);
			} else {
				context.put("selectBandar", null);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatBon")
	public String getMaklumatBon() {
		try {			
			mp = new MyPersistence();			
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			context.put("r", kontrak);
			if (kontrak != null) {
				if (kontrak.getStatus().equals("01") || kontrak.getStatus().equals("02") || kontrak.getStatus().equals("03")) {
					context.put("flagAllowUpdate", "Y");
				} else {
					context.remove("flagAllowUpdate");
				}
				
				List<KontrakBon> listMaklumatBon = mp.list("SELECT x FROM KontrakBon x WHERE x.kontrak.id = '" + kontrak.getId() +"'");
				context.put("listMaklumatBon", listMaklumatBon);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() throws Exception {
		
		try {			
			mp = new MyPersistence();

			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			context.put("r", kontrak);
			if (kontrak != null) {
				if (kontrak.getStatus().equals("01") || kontrak.getStatus().equals("02") || kontrak.getStatus().equals("03")) {
					context.put("flagAllowUpdate", "Y");
				} else {
					context.remove("flagAllowUpdate");
				}
				
				List<KontrakDokumen> listDokumen = mp.list("SELECT x FROM KontrakDokumen x WHERE x.kontrak.id = '" + kontrak.getId() +"'");
				context.put("listDokumen", listDokumen);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		List<JenisDokumen> list = dataUtil.getListJenisDokumen();
		context.put("selectJenisDokumen", list);
		
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPembayaran")
	public String getMaklumatPembayaran() {
		try {			
			mp = new MyPersistence();
			
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			System.out.println("PRINT =====:" + kontrak.getId());
			context.put("r", kontrak);
			if (kontrak != null) {
				if (kontrak.getStatus().equals("01") || kontrak.getStatus().equals("02") || kontrak.getStatus().equals("03")) {
					context.put("flagAllowUpdate", "Y");
				} else {
					context.remove("flagAllowUpdate");
				}
				
				List<KontrakMaklumatBayaran> listMaklumatPembayaran = mp.list("SELECT x FROM KontrakMaklumatBayaran x WHERE x.kontrak.id = '" + kontrak.getId() +"'");
				context.put("listMaklumatPembayaran", listMaklumatPembayaran);
			}				
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START MAKLUMAT KONTRAK **/
	@Command("doSaveKemaskiniKontrak")
	public String doSaveKemaskiniKontrak() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		KontrakKontrak kontrak = null;
		try {
			mp = new MyPersistence();	
				
			kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, getParam("idKontrak"));
			if (kontrak != null) {
				mp.begin();
				
				kontrak.setNoDaftarKontrak(getParam("noDaftarKontrak"));		
				kontrak.setPerkhidmatan(getParam("perkhidmatan"));
				kontrak.setJenisKontrak((JenisKontrak) mp.find(JenisKontrak.class, getParam("idJenisKontrak")));
				kontrak.setKaedahPerolehan((KaedahPerolehan) mp.find(KaedahPerolehan.class, getParam("idKaedahPerolehan")));
				kontrak.setKodProgram(getParam("kodProgram"));
				kontrak.setKodObjek(getParam("kodObjek"));
				kontrak.setIdPelaksana(getParam("idLantikan"));
				kontrak.setSeksyen((Seksyen) mp.find(Seksyen.class, getParam("idSeksyen")));
				kontrak.setTarikhMula(getDate("tarikhMula"));
				kontrak.setTarikhTamat(getDate("tarikhTamat"));
				kontrak.setNilaiKontrak(Double.valueOf(util.RemoveComma(getParam("nilaiKontrak"))));
				
				kontrak.setModAnsuran(getParam("modAnsuran"));
				kontrak.setAmaunAnsuran(Double.valueOf(util.RemoveComma(getParam("amaunAnsuran"))));
				kontrak.setTarikhKeluarSst(getDate("tarikhKeluarSst"));
				kontrak.setTarikhTerimaSst(getDate("tarikhTerimaSst"));		
				kontrak.setCatatan(getParam("catatan"));
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KONTRAK TELAH BERJAYA DIKEMASKINI";
			}						
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KONTRAK TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getMaklumatKontrak();		
	}
	/** END MAKLUMAT KONTRAK **/
	
	/** START MAKLUMAT KONTRAKTOR **/
	@Command("getRegisteredKontraktor")
	public String getRegisteredKontraktor() throws Exception {
		
		KontrakKontraktor kontraktor = null;
		String noPendaftaran = get("noPendaftaran").trim();
		try {			
			mp = new MyPersistence();
			
			kontraktor = (KontrakKontraktor) mp.find(KontrakKontraktor.class, noPendaftaran);	
			if (kontraktor != null){
				context.put("kontraktor", kontraktor);
				List<Bandar> list = dataUtil.getListBandar(kontraktor.getBandar().getNegeri().getId());
				context.put("selectBandar", list);
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("noPendaftaran", noPendaftaran);
		context.put("kontraktor", kontraktor);
		return getPath() + "/maklumatKontraktor/maklumatKontraktor.vm";
	}
	
	@Command("doSaveKontraktor")
	public String doSaveKontraktor() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		Boolean addMaklumatKontraktor = false;				
		KontrakKontraktor kontraktor = null;
		String noPendaftaran = get("noPendaftaran").trim();
		try {			
			mp = new MyPersistence();
			mp.begin();				
			
			kontraktor = (KontrakKontraktor) mp.find(KontrakKontraktor.class, noPendaftaran);
			if(kontraktor == null){
				addMaklumatKontraktor = true;
				kontraktor = new KontrakKontraktor();
			}
			kontraktor.setId(getParam("noPendaftaran"));
			kontraktor.setNamaKontraktor(getParam("namaKontraktor"));
			kontraktor.setKodPembekal(getParam("kodPembekal"));
			kontraktor.setNamaPemilik(getParam("namaPemilik"));				
			kontraktor.setAlamat1(getParam("alamat1"));
			kontraktor.setAlamat2(getParam("alamat2"));
			kontraktor.setAlamat3(getParam("alamat3"));
			kontraktor.setPoskod(getParamAsInteger("poskod"));
			kontraktor.setBandar((Bandar) mp.find(Bandar.class, get("idBandar")));
			kontraktor.setNoTelefon(getParam("noTelefon"));
			kontraktor.setNoTelefonBimbit(getParam("noTelefonBimbit"));		
			kontraktor.setNoFaks(getParam("noFaks"));
			kontraktor.setEmel(getParam("emel"));
			kontraktor.setCatatan(getParam("catatan"));
				
			if ( addMaklumatKontraktor ){
				mp.persist(kontraktor);
			}
			
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			if (kontrak != null) {
				kontrak.setKontraktor(kontraktor);
			}
			mp.commit();	
			flagStatusInfo = "Y";
			statusInfo = "KONTRAKTOR TELAH BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KONTRAKTOR TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getMaklumatKontraktor();		
	}
	/** END MAKLUMAT KONTRAKTOR **/
	
	/** START MAKLUMAT BON **/
	@Command("addMaklumatBon")
	public String addMaklumatBon() {
		
		context.put("selectJenisJaminan", dataUtil.getListJenisJaminan());
		context.remove("rekod");

		return getPath() + "/maklumatBon/popupMaklumatBon.vm";
	}
	
	@Command("editMaklumatBon")
	public String editMaklumatBon() {
		context.put("selectJenisJaminan", dataUtil.getListJenisJaminan());
		context.remove("rekod");
		try {			
			mp = new MyPersistence();			
			
			KontrakBon bon = (KontrakBon) mp.find(KontrakBon.class, getParam("idMaklumatBon"));
			if(bon != null){
				context.put("rekod", bon);
			} 			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath()  + "/maklumatBon/popupMaklumatBon.vm";
	}
	
	@Command("saveMaklumatBon")
	public String saveMaklumatBon() throws ParseException {
		
		String statusInfo = "";	
		String flagStatusInfo = "";	
		Boolean addMaklumatBon = false;
		KontrakBon bon = null;
		
		try {			
			mp = new MyPersistence();
			mp.begin();
			
			bon = (KontrakBon) mp.find(KontrakBon.class, get("idMaklumatBon"));
			if(bon == null){
				addMaklumatBon = true;
				bon = new KontrakBon();
			}		
			bon.setJaminan((JenisJaminan) mp.find(JenisJaminan.class, getParam("idJenisJaminan")));
			bon.setKontrak((KontrakKontrak)mp.find(KontrakKontrak.class, getParam("idKontrak")));
			bon.setNilaiBon(Double.valueOf(util.RemoveComma(getParam("nilaiBon"))));
			bon.setNoRujukan(getParam("noRujukan"));
			bon.setStatusBon((StatusBonKontrak) mp.find(StatusBonKontrak.class, getParam("idStatusBon")));
			bon.setTarikhLuput(getDate("tarikhLuput"));
			bon.setTarikhReleaseBon(getDate("tarikhReleaseBon"));
			bon.setNamaBank(get("namaBank"));
			if ( addMaklumatBon ){
				mp.persist(bon);
			}
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "MAKLUMAT BON TELAH BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT BON TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getMaklumatBon();
	}

	@SuppressWarnings("unchecked")
	@Command("removeMaklumatBon")
	public String removeMaklumatBon() {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		try {			
			mp = new MyPersistence();
			
			KontrakBon cadangan = (KontrakBon) mp.find(KontrakBon.class, get("idMaklumatBon"));
			if(cadangan != null){
				mp.begin();
				mp.remove(cadangan);
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "MAKLUMAT BON TELAH BERJAYA DIHAPUS";
			}					
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT BON TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getMaklumatBon();
	}
	/** END MAKLUMAT BON **/
	
	/** START DOKUMEN SOKONGAN **/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idKontrak = get("idKontrak");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		KontrakDokumen dokumen = new KontrakDokumen();
		String uploadDir = "kontrak/kontrakBayaran/dokumenSokongan/";
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
			String imgName = uploadDir + idKontrak + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

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
				simpanDokumen(idKontrak, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(String idKontrak, String imgName, String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, 
			KontrakDokumen dokumen) throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		try {			
			mp = new MyPersistence();
			
			mp.begin();
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, idKontrak);			
			dokumen.setKontrak(kontrak);
			dokumen.setPhotofilename(imgName);
			dokumen.setThumbfilename(avatarName);
			dokumen.setTajuk(tajukDokumen);
			dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
			dokumen.setKeterangan(keteranganDokumen);
			mp.persist(dokumen);
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "DOKUMEN TELAH BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
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
				flagStatusInfo = "Y";
				statusInfo = "DOKUMEN TELAH BERJAYA DIHAPUS";
			}
			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getDokumenSokongan();
	}
	
	@Command("refreshList")
	public String refreshList() throws Exception {

	return getDokumenSokongan();
	}
	/** END DOKUMEN SOKONGAN **/
	
	/** START HANTAR PENGESAHAN 
	 * @throws Exception **/
	@Command("doHantarPengesahan")
	public String doHantarPengesahan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		try {			
			mp = new MyPersistence();
			
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			if(kontrak != null){
				mp.begin();

				kontrak.setStatus("02"); //PENGESAHAN MAKLUMAT KONTRAK
				
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KONTRAK TELAH BERJAYA DIHANTAR UNTUK PENGESAHAN";
			}					
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KONTRAK TIDAK BERJAYA DIHANTAR UNTUK PENGESAHAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getDokumenSokongan();
	}
	/** END HANTAR PENGESAHAN **/
	
	/** START AKTIF KONTRAK **/
	@Command("doSahKontrak")
	public String doSahKontrak() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		try {			
			mp = new MyPersistence();
			
			KontrakKontrak kontrak = (KontrakKontrak) mp.find(KontrakKontrak.class, get("idKontrak"));
			if(kontrak != null){
				mp.begin();

				kontrak.setStatus("04"); //KONTRAK AKTIF
				
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KONTRAK TELAH BERJAYA DISAHKAN";
			}					
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KONTRAK TIDAK BERJAYA DISAHKAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getMaklumatKontrak();
	}
	/** END HANTAR PENGESAHAN **/
		
	/** START DROP DOWN LIST **/	
	@Command("selectBandar")
	public String selectBandar() throws Exception {	
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/maklumatKontraktor/selectBandar.vm";
	}
	/** END DROP DOWN LIST **/
}


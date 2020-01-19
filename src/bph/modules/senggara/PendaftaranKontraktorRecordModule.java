package bph.modules.senggara;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import bph.entities.kod.Bank;
import bph.entities.kod.GredKontraktor;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.KategoriBidangKontraktor;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Negeri;
import bph.entities.kod.PengkhususanBidangKontraktor;
import bph.entities.senggara.MtnDaftarKontraktor;
import bph.entities.senggara.MtnDokumen;
import bph.entities.senggara.MtnKontraktor;
import bph.entities.senggara.MtnPengkhususanBidangKontraktor;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class PendaftaranKontraktorRecordModule extends LebahRecordTemplateModule<MtnDaftarKontraktor> {

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
	public String getPath() {
		return "bph/modules/senggara/pendaftaranKontraktor";
	}

	@Override
	public Class<MtnDaftarKontraktor> getPersistenceClass() {		
		return MtnDaftarKontraktor.class;
	}
	
	@Override
	public void begin() {				
		
		dataUtil = DataUtil.getInstance(db);		
		
		List<LokasiPermohonan> kawasanList = dataUtil.getListLokasiPermohonan();
		List<Negeri> negeriList = dataUtil.getListNegeri();
		List<GredKontraktor> gredlist = dataUtil.getListGredKontraktor();		
		List<KategoriBidangKontraktor> kategorilist = dataUtil.getListKategoriBidangKontraktor();	
		List<Bank> bankList = dataUtil.getListBank();
		
		context.put("selectKawasan", kawasanList);
		context.put("selectNegeri", negeriList);
		context.put("selectGred", gredlist);
		context.put("selectKategori", kategorilist);
		context.put("selectBank", bankList);
		
		//TAHUN PENDAFTARAN
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		int year = cal.get(Calendar.YEAR);
		context.put("tahunPendaftaran", year);
		context.put("tarikhPendaftaran", new Date());
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		// TODO Auto-generated method stub
	}

	private void defaultButtonOption() {
				
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}
	
	private void addfilter() {
		
		this.setOrderBy("tahun");
		this.setOrderType("desc");
	}
	
	@Override
	public void beforeSave() {
		
		//DAFTAR MAKLUMAT KONTRAKTOR
		boolean addKontraktor = false;
		MtnKontraktor kontraktor = null;
		String noPendaftaran = getParam("noPendaftaran").trim();
		kontraktor = db.find(MtnKontraktor.class, noPendaftaran);
		
		if ( kontraktor == null ) {
			kontraktor = new MtnKontraktor();
			kontraktor.setId(noPendaftaran);
			addKontraktor = true;
		}		
		kontraktor.setKawasan(db.find(LokasiPermohonan.class, getParam("idKawasan")));
		kontraktor.setNamaKontraktor(getParam("namaKontraktor"));
		kontraktor.setNamaPemilik(getParam("namaPemilik"));
		kontraktor.setAlamat1(getParam("alamat1"));
		kontraktor.setAlamat2(getParam("alamat2"));
		kontraktor.setAlamat3(getParam("alamat3"));
		kontraktor.setPoskod(getParamAsInteger("poskod"));
		kontraktor.setBandar(db.find(Bandar.class, getParam("idBandar")));
		kontraktor.setNoTelefon(getParam("noTelefon"));
		kontraktor.setNoTelefonBimbit(getParam("noTelefonBimbit"));		
		kontraktor.setNoFaks(getParam("noFaks"));
		kontraktor.setEmel(getParam("emel"));
		kontraktor.setBank(db.find(Bank.class, getParam("idBank")));
		kontraktor.setNoAkaun(getParam("noAkaun"));
		kontraktor.setFlagAwam(getParam("flagAwam"));
		kontraktor.setFlagElektrik(getParam("flagElektrik"));
		kontraktor.setCatatan(getParam("catatan"));
		
		db.begin();
		if ( addKontraktor ) db.persist(kontraktor);
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	public void save(MtnDaftarKontraktor daftarKontraktor) throws Exception {
		String noPendaftaran = getParam("noPendaftaran").trim();
		daftarKontraktor.setKontraktor(db.find(MtnKontraktor.class, noPendaftaran));
		daftarKontraktor.setTahun(getParamAsInteger("tahun"));
		daftarKontraktor.setTarikhPendaftaran(getDate("tarikhPendaftaran"));
		daftarKontraktor.setNoProfil(getParam("noProfil"));
		daftarKontraktor.setTarikhTerimaProfil(getDate("tarikhTerimaProfil"));		
		daftarKontraktor.setTurutan(9999);
		daftarKontraktor.setFlagHadirCabutan("T");
	}

	@Override
	public void afterSave(MtnDaftarKontraktor daftarKontraktor) {	
				
		List<Bandar> list = null;
		if (daftarKontraktor.getKontraktor() != null) {
			if (daftarKontraktor.getKontraktor().getBandar() != null) {
				if (daftarKontraktor.getKontraktor().getBandar().getNegeri() != null) {
					list = dataUtil.getListBandar(daftarKontraktor.getKontraktor().getBandar().getNegeri().getId());
				}
			}			
		}
		
		context.put("selectBandar", list);
		context.remove("kontraktor");
		context.put("selectedTab", "1");
	}
	
	@Override
	public boolean delete(MtnDaftarKontraktor daftarKontraktor) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getRelatedData(MtnDaftarKontraktor daftarKontraktor) {
		MtnDaftarKontraktor daftar = null;
		List<Bandar> list = null;
		
		try {
			mp = new MyPersistence();
			
			daftar = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, daftarKontraktor.getId());
			if (daftar != null) {				
				if (daftarKontraktor.getKontraktor() != null) {
					if (daftarKontraktor.getKontraktor().getBandar() != null) {
						if (daftarKontraktor.getKontraktor().getBandar().getNegeri() != null) {
							list = dataUtil.getListBandar(daftarKontraktor.getKontraktor().getBandar().getNegeri().getId());
						}
					}			
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectBandar", list);
		context.remove("kontraktor");
		context.put("r", daftar);
		context.put("selectedTab", "1");
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findNoPendaftaran = getParam("findNoPendaftaran").trim();
		String findNamaKontraktor = getParam("findNamaKontraktor").trim();
		String findNamaPemilik = getParam("findNamaPemilik").trim();
		String findTahunPendaftaran = getParam("findTahunPendaftaran").trim();
		String findKawasan = getParam("findKawasan");
		String findKehadiran = getParam("findKehadiran");
		
		String findGred = getParam("findGred");
		String findBidang = getParam("findBidang");
		String findKategori = getParam("findKategori");
		String findPengkhususan = getParam("findPengkhususan");		

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kontraktor.id", findNoPendaftaran);
		map.put("kontraktor.namaKontraktor", findNamaKontraktor);
		map.put("kontraktor.namaPemilik", findNamaPemilik);
		map.put("tahun", new OperatorEqualTo(findTahunPendaftaran));
		map.put("kontraktor.kawasan.id", new OperatorEqualTo(findKawasan));
		map.put("flagHadirCabutan", new OperatorEqualTo(findKehadiran));

		if (findGred != null && !"-".equals(findGred) && findGred.length() > 0) {
			this.addFilter("kontraktor.id IN (SELECT a.kontraktor.id FROM MtnPengkhususanBidangKontraktor a WHERE a.gred.id = '" + findGred + "')");
		}
		if (findKategori != null && !"-".equals(findKategori) && findKategori.length() > 0) {
			this.addFilter("kontraktor.id IN (SELECT b.kontraktor.id FROM MtnPengkhususanBidangKontraktor b WHERE b.pengkhususan.kategori.id = '" + findKategori + "')");
		}
		if (findPengkhususan != null && !"-".equals(findPengkhususan) && findPengkhususan.length() > 0) {
			this.addFilter("kontraktor.id IN (SELECT c.kontraktor.id FROM MtnPengkhususanBidangKontraktor c WHERE c.pengkhususan.id = '" + findPengkhususan + "')");
		}
		
		if (findBidang != null && !"-".equals(findBidang) && findBidang.length() > 0) {
			
			if("A".equals(findBidang)){
				this.addFilter("kontraktor.id IN (SELECT c.id FROM MtnKontraktor c WHERE c.flagAwam = 'Y')");
			}else if("E".equals(findBidang)){
				this.addFilter("kontraktor.id IN (SELECT c.id FROM MtnKontraktor c WHERE c.flagElektrik = 'Y')");
			}else if("AE".equals(findBidang)){
				this.addFilter("kontraktor.id IN (SELECT c.id FROM MtnKontraktor c WHERE c.flagAwam = 'Y' AND c.flagElektrik = 'Y')");
			}
		} 

		return map;
	}
	
	@Command("getRegisteredKontraktor")
	public String getRegisteredKontraktor() throws Exception {	
		MtnKontraktor kontraktor = null;
		String noPendaftaran = getParam("noPendaftaran").trim();
		
		try {
			mp = new MyPersistence();
			
			kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, noPendaftaran);			
			if (kontraktor != null){
				context.put("kontraktor", kontraktor);				
				
				List<Bandar> list = null;
				if (kontraktor.getBandar() != null) {
					if (kontraktor.getBandar().getNegeri() != null) {
						list = dataUtil.getListBandar(kontraktor.getBandar().getNegeri().getId());
					}
				}				
				context.put("selectBandar", list);
			} else {
				context.remove("kontraktor");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatDetailKontraktor.vm";
	}

	/** START TAB **/
	@Command("getMaklumatKontraktor")
	public String getMaklumatKontraktor() {
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			context.put("r", daftarKontraktor);
			
			List<Bandar> list = null;
			if (daftarKontraktor.getKontraktor() != null) {
				if (daftarKontraktor.getKontraktor().getBandar() != null) {
					if (daftarKontraktor.getKontraktor().getBandar().getNegeri() != null) {
						list = dataUtil.getListBandar(daftarKontraktor.getKontraktor().getBandar().getNegeri().getId());
					}
				}			
			}
			
			context.put("selectBandar", list);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPengkhususan")
	public String getMaklumatPengkhususan() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			context.put("r", daftarKontraktor);
			
			List<MtnPengkhususanBidangKontraktor> listMaklumatPengkhususan = mp.list("SELECT x FROM MtnPengkhususanBidangKontraktor x WHERE x.kontraktor.id = '" + daftarKontraktor.getKontraktor().getId() + "' ORDER BY x.gred.id, x.kategori.id, x.pengkhususan.id ASC");
			context.put("listMaklumatPengkhususan", listMaklumatPengkhususan);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
        		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSijilSokongan")
	public String getSijilSokongan() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));				
			context.put("r", daftarKontraktor);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getLampiran")
	public String getLampiran() throws Exception {
		
		try {
			mp = new MyPersistence();
			
			List<JenisDokumen> list = dataUtil.getListJenisDokumen();
			context.put("selectJenisDokumen", list);
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			context.put("r", daftarKontraktor);
			
			List<MtnDokumen> listDokumen = mp.list("SELECT x FROM MtnDokumen x WHERE x.kontraktor.id = '" + daftarKontraktor.getKontraktor().getId() +"'");
			context.put("listDokumen", listDokumen);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START MAKLUMAT KONTRAKTOR **/
	@Command("saveKontraktor")
	public String saveKontraktor() throws Exception {	
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				mp.begin();
				daftarKontraktor.setTarikhPendaftaran(getDate("tarikhPendaftaran"));
				daftarKontraktor.setTarikhTerimaProfil(getDate("tarikhTerimaProfil"));
				daftarKontraktor.setNoProfil(getParam("noProfil"));
				if (getParam("turutan") != null && getParam("turutan").trim().length() > 0 && getParamAsInteger("turutan") != 9999) {
					daftarKontraktor.setTurutan(getParamAsInteger("turutan"));
					daftarKontraktor.setFlagHadirCabutan("Y");
				}
				
				//MAKLUMAT KONTRAKTOR		
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					kontraktor.setKawasan((LokasiPermohonan) mp.find(LokasiPermohonan.class, getParam("idKawasan")));
					kontraktor.setNamaKontraktor(getParam("namaKontraktor"));
					kontraktor.setNamaPemilik(getParam("namaPemilik"));
					kontraktor.setAlamat1(getParam("alamat1"));
					kontraktor.setAlamat2(getParam("alamat2"));
					kontraktor.setAlamat3(getParam("alamat3"));
					kontraktor.setPoskod(getParamAsInteger("poskod"));
					kontraktor.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
					kontraktor.setNoTelefon(getParam("noTelefon"));
					kontraktor.setNoTelefonBimbit(getParam("noTelefonBimbit"));		
					kontraktor.setNoFaks(getParam("noFaks"));
					kontraktor.setEmel(getParam("emel"));
					kontraktor.setBank((Bank) mp.find(Bank.class, getParam("idBank")));
					kontraktor.setNoAkaun(getParam("noAkaun"));
					kontraktor.setFlagAwam(getParam("flagAwam"));
					kontraktor.setFlagElektrik(getParam("flagElektrik"));
					kontraktor.setCatatan(getParam("catatan"));
				}							
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatKontraktor();		
	}
	
	@Command("hapusPendaftaranKontraktor")
	public String hapusPendaftaranKontraktor() throws Exception {	
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				mp.begin();
				mp.remove(daftarKontraktor);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();		
	}
	
	@Command("hapusKontraktor")
	public String hapusKontraktor() throws Exception {		
		MtnDaftarKontraktor daftarKontraktor = null;
		MtnKontraktor kontraktor = null;
		
		try {
			mp = new MyPersistence();
			
			
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {				
				kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					//DELETE DOKUMEN SOKONGAN
					int i = 0;
					while (i < kontraktor.getListDokumenKontraktor().size()) {
						Util.deleteFile(kontraktor.getListDokumenKontraktor().get(i).getPhotofilename());
						Util.deleteFile(kontraktor.getListDokumenKontraktor().get(i).getThumbfilename());
						mp.remove(kontraktor.getListDokumenKontraktor().get(i));
						i++;
					}
					
					//DELETE PENGKHUSUSAN BIDANG KONTRAKTOR
					i = 0;
					while (i < kontraktor.getListPengkhususanBidangKontraktor().size()) {
						mp.remove(kontraktor.getListPengkhususanBidangKontraktor().get(i));
						i++;
					}
					
					//DELETE LIST REKOD DAFTAR KONTRAKTOR
					i = 0;
					while (i < kontraktor.getListDaftarKontraktor().size()) {
						db.remove(kontraktor.getListDaftarKontraktor().get(i));
						i++;
					}
					
					//DELETE FAIL SOKONGAN
					Util.deleteFile(kontraktor.getFilenameProfil());
					Util.deleteFile(kontraktor.getFilenamePP());
					Util.deleteFile(kontraktor.getFilenameSPKK());
					Util.deleteFile(kontraktor.getFilenameST());
					Util.deleteFile(kontraktor.getFilenameSTB());
					mp.remove(kontraktor);		
					
					mp.commit();
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();		
	}
	/** END MAKLUMAT KONTRAKTOR **/
	
	/** START MAKLUMAT PENGKHUSUSAN **/
	@Command("addMaklumatPengkhususan")
	public String addMaklumatPengkhususan() {
		List<GredKontraktor> listGred = dataUtil.getListGredKontraktor();
		context.put("selectGred", listGred);
		List<KategoriBidangKontraktor> listKategori = dataUtil.getListKategoriBidangKontraktor();
		context.put("selectKategori", listKategori);
		context.remove("selectPengkhususan");
		
		context.remove("rekod");
		return getPath() + "/pengkhususanKontraktor/popupMaklumatPengkhususan.vm";
	}
	
	@Command("saveMaklumatPengkhususan")
	public String saveMaklumatPengkhususan() throws Exception {
		String statusInfo = "";
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				mp.begin();
				
				MtnPengkhususanBidangKontraktor pengkhususan = (MtnPengkhususanBidangKontraktor) mp.get("select x from MtnPengkhususanBidangKontraktor x where x.kontraktor.id = '" + daftarKontraktor.getKontraktor().getId() + 
						"' and x.gred.id = '" + db.find(GredKontraktor.class, getParam("idGred")).getId() + "' and x.pengkhususan.id = '" + db.find(PengkhususanBidangKontraktor.class, getParam("idPengkhususan")).getId() + "'");
				Boolean addMaklumatPengkhususan = false;
				
				if(pengkhususan == null){
					addMaklumatPengkhususan = true;
					pengkhususan = new MtnPengkhususanBidangKontraktor();
					
					pengkhususan.setKontraktor(daftarKontraktor.getKontraktor());
					pengkhususan.setGred((GredKontraktor) mp.find(GredKontraktor.class, getParam("idGred")));
					pengkhususan.setKategori((KategoriBidangKontraktor) mp.find(KategoriBidangKontraktor.class, getParam("idKategori")));
					pengkhususan.setPengkhususan((PengkhususanBidangKontraktor) mp.find(PengkhususanBidangKontraktor.class, getParam("idPengkhususan")));
				}
				if ( addMaklumatPengkhususan ) mp.persist(pengkhususan);
				
				mp.commit();
				statusInfo = "success";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			statusInfo = "error";
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPengkhususan();		
	}
	
	@SuppressWarnings("unchecked")
	@Command("removeMaklumatPengkhususan")
	public String removeMaklumatPengkhususan() throws Exception {
		String statusInfo = "";
		
		try {
			mp = new MyPersistence();
			
			MtnPengkhususanBidangKontraktor pengkhususan = (MtnPengkhususanBidangKontraktor) mp.find(MtnPengkhususanBidangKontraktor.class, getParam("idMaklumatPengkhususan"));
			if (pengkhususan != null) {
				mp.begin();
				mp.remove(pengkhususan);
				mp.commit();
				statusInfo = "success";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			statusInfo = "error";
		} finally {
			if (mp != null) { mp.close(); }
		}	

		return getMaklumatPengkhususan();	
	}
	/** END MAKLUMAT PENGKHUSUSAN **/
	
	/** START SIJIL SOKONGAN **/
	@Command("saveSijilSokongan")
	public String saveSijilSokongan() throws Exception { 
		
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					
					kontraktor.setTarikhMulaPP(getDate("tarikhMulaPP"));
					kontraktor.setTarikhTamatPP(getDate("tarikhTamatPP"));
					kontraktor.setTarikhMulaSPKK(getDate("tarikhMulaSPKK"));
					kontraktor.setTarikhTamatSPKK(getDate("tarikhTamatSPKK"));
					kontraktor.setTarikhMulaST(getDate("tarikhMulaST"));
					kontraktor.setTarikhTamatST(getDate("tarikhTamatST"));
					kontraktor.setTarikhMulaSTB(getDate("tarikhMulaSTB"));
					kontraktor.setTarikhTamatSTB(getDate("tarikhTamatSTB"));
					
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	
		return getSijilSokongan();
	}
	
	@Command("uploadPP")
	public String uploadPP() throws Exception {
		MtnDaftarKontraktor daftarKontraktor = null;
		try {
			mp = new MyPersistence();		
		
			String idPendaftaran = getParam("idPendaftaran");
			String uploadDir = "senggara/kontraktor/sijil/pp/";
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
				daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));	
				if (daftarKontraktor.getKontraktor().getFilenamePP() != null && daftarKontraktor.getKontraktor().getFilenamePP().length() > 0) {
					Util.deleteFile(daftarKontraktor.getKontraktor().getFilenamePP());
				}
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + fileName.substring(fileName.lastIndexOf("."));
	
				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
	
				String mimetype = item.getContentType();
				String type = mimetype.split("/")[0];
	
				if (!imgName.equals("")) {
					simpanPP(daftarKontraktor, imgName, avatarName, mp);
				}
			}
	
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", daftarKontraktor);
		context.put("selectedTab", "3");
		return getPath() + "/sijilSokongan/uploadSijil.vm";
	}
	
	public void simpanPP(MtnDaftarKontraktor daftarKontraktor, String imgName, String avatarName, MyPersistence mp) throws Exception {
		
		MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
		if (kontraktor != null) {
			mp.begin();
			kontraktor.setFilenamePP(imgName);
			mp.commit();
		}
	}
	
	@Command("uploadSPKK")
	public String uploadSPKK() throws Exception {
		MtnDaftarKontraktor daftarKontraktor = null;
		try {
			mp = new MyPersistence();		
		
			String idPendaftaran = getParam("idPendaftaran");
			String uploadDir = "senggara/kontraktor/sijil/spkk/";
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
				daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));	
				if (daftarKontraktor.getKontraktor().getFilenameSPKK() != null && daftarKontraktor.getKontraktor().getFilenameSPKK().length() > 0) {
					Util.deleteFile(daftarKontraktor.getKontraktor().getFilenameSPKK());
				}
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + fileName.substring(fileName.lastIndexOf("."));
	
				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
	
				String mimetype = item.getContentType();
				String type = mimetype.split("/")[0];
	
				if (!imgName.equals("")) {
					simpanSPKK(daftarKontraktor, imgName, avatarName, mp);
				}
			}
	
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", daftarKontraktor);
		context.put("selectedTab", "3");
		return getPath() + "/sijilSokongan/uploadSijil.vm";
	}
	
	public void simpanSPKK(MtnDaftarKontraktor daftarKontraktor, String imgName, String avatarName, MyPersistence mp) throws Exception {
		
		MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
		if (kontraktor != null) {
			mp.begin();
			kontraktor.setFilenameSPKK(imgName);
			mp.commit();
		}
	}
	
	@Command("uploadST")
	public String uploadST() throws Exception {
		MtnDaftarKontraktor daftarKontraktor = null;
		try {
			mp = new MyPersistence();		
		
			String idPendaftaran = getParam("idPendaftaran");
			String uploadDir = "senggara/kontraktor/sijil/st/";
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
				daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));	
				if (daftarKontraktor.getKontraktor().getFilenameST() != null && daftarKontraktor.getKontraktor().getFilenameST().length() > 0) {
					Util.deleteFile(daftarKontraktor.getKontraktor().getFilenameST());
				}
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + fileName.substring(fileName.lastIndexOf("."));
	
				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
	
				String mimetype = item.getContentType();
				String type = mimetype.split("/")[0];
	
				if (!imgName.equals("")) {
					simpanST(daftarKontraktor, imgName, avatarName, mp);
				}
			}
	
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", daftarKontraktor);
		context.put("selectedTab", "3");
		return getPath() + "/sijilSokongan/uploadSijil.vm";
	}
	
	public void simpanST(MtnDaftarKontraktor daftarKontraktor, String imgName, String avatarName, MyPersistence mp) throws Exception {
		
		MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
		if (kontraktor != null) {
			mp.begin();
			kontraktor.setFilenameST(imgName);
			mp.commit();
		}
	}
	
	@Command("uploadSTB")
	public String uploadSTB() throws Exception {
		MtnDaftarKontraktor daftarKontraktor = null;
		try {
			mp = new MyPersistence();		
		
			String idPendaftaran = getParam("idPendaftaran");
			String uploadDir = "senggara/kontraktor/sijil/stb/";
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
				daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));	
				if (daftarKontraktor.getKontraktor().getFilenameSTB() != null && daftarKontraktor.getKontraktor().getFilenameSTB().length() > 0) {
					Util.deleteFile(daftarKontraktor.getKontraktor().getFilenameSTB());
				}
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + fileName.substring(fileName.lastIndexOf("."));
	
				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
	
				String mimetype = item.getContentType();
				String type = mimetype.split("/")[0];
	
				if (!imgName.equals("")) {
					simpanSTB(daftarKontraktor, imgName, avatarName, mp);
				}
			}
	
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", daftarKontraktor);
		context.put("selectedTab", "3");
		return getPath() + "/sijilSokongan/uploadSijil.vm";
	}
	
	public void simpanSTB(MtnDaftarKontraktor daftarKontraktor, String imgName, String avatarName, MyPersistence mp) throws Exception {
		
		MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
		if (kontraktor != null) {
			mp.begin();
			kontraktor.setFilenameSTB(imgName);
			mp.commit();
		}
	}
	
	@Command("uploadProfil")
	public String uploadProfil() throws Exception {
		MtnDaftarKontraktor daftarKontraktor = null;
		try {
			mp = new MyPersistence();		
		
			String idPendaftaran = getParam("idPendaftaran");
			String uploadDir = "senggara/kontraktor/sijil/profil/";
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
				daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));	
				if (daftarKontraktor.getKontraktor().getFilenameProfil() != null && daftarKontraktor.getKontraktor().getFilenameProfil().length() > 0) {
					Util.deleteFile(daftarKontraktor.getKontraktor().getFilenameProfil());
				}
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + fileName.substring(fileName.lastIndexOf("."));
	
				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
	
				String mimetype = item.getContentType();
				String type = mimetype.split("/")[0];
	
				if (!imgName.equals("")) {
					simpanProfil(daftarKontraktor, imgName, avatarName, mp);
				}
			}
	
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", daftarKontraktor);
		context.put("selectedTab", "3");
		return getPath() + "/sijilSokongan/uploadSijil.vm";
	}
	
	public void simpanProfil(MtnDaftarKontraktor daftarKontraktor, String imgName, String avatarName, MyPersistence mp) throws Exception {
		
		MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
		if (kontraktor != null) {
			mp.begin();
			kontraktor.setFilenameProfil(imgName);
			mp.commit();
		}
	}
	
	@Command("uploadBank")
	public String uploadBank() throws Exception {
		MtnDaftarKontraktor daftarKontraktor = null;
		try {
			mp = new MyPersistence();		
		
			String idPendaftaran = getParam("idPendaftaran");
			String uploadDir = "senggara/kontraktor/sijil/bank/";
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
				daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));	
				if (daftarKontraktor.getKontraktor().getFilenameBank() != null && daftarKontraktor.getKontraktor().getFilenameBank().length() > 0) {
					Util.deleteFile(daftarKontraktor.getKontraktor().getFilenameBank());
				}
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + fileName.substring(fileName.lastIndexOf("."));
	
				imgName = imgName.replaceAll(" ", "_");
				item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
	
				String mimetype = item.getContentType();
				String type = mimetype.split("/")[0];
	
				if (!imgName.equals("")) {
					simpanBank(daftarKontraktor, imgName, avatarName, mp);
				}
			}
	
			daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", daftarKontraktor);
		context.put("selectedTab", "3");
		return getPath() + "/sijilSokongan/uploadSijil.vm";
	}
	
	public void simpanBank(MtnDaftarKontraktor daftarKontraktor, String imgName, String avatarName, MyPersistence mp) throws Exception {
		
		MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
		if (kontraktor != null) {
			mp.begin();
			kontraktor.setFilenameBank(imgName);
			mp.commit();
		}
	}
	
	@Command("doHapusProfil")
	public String doHapusProfil() throws Exception {
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					if (kontraktor.getFilenameProfil() != null && kontraktor.getFilenameProfil().length() > 0) {
						Util.deleteFile(kontraktor.getFilenameProfil());
					}
					kontraktor.setFilenameProfil(null);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSijilSokongan();
	}
	
	@Command("doHapusBank")
	public String doHapusBank() throws Exception {
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					if (kontraktor.getFilenameBank() != null && kontraktor.getFilenameBank().length() > 0) {
						Util.deleteFile(kontraktor.getFilenameBank());
					}
					kontraktor.setFilenameBank(null);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSijilSokongan();
	}
	
	@Command("doHapusPP")
	public String doHapusPP() throws Exception {
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					if (kontraktor.getFilenamePP() != null && kontraktor.getFilenamePP().length() > 0) {
						Util.deleteFile(kontraktor.getFilenamePP());
					}
					kontraktor.setFilenamePP(null);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSijilSokongan();
	}
	
	@Command("doHapusSPKK")
	public String doHapusSPKK() throws Exception {
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					if (kontraktor.getFilenameSPKK() != null && kontraktor.getFilenameSPKK().length() > 0) {
						Util.deleteFile(kontraktor.getFilenameSPKK());
					}
					kontraktor.setFilenameSPKK(null);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSijilSokongan();
	}
	
	@Command("doHapusST")
	public String doHapusST() throws Exception {
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					if (kontraktor.getFilenameST() != null && kontraktor.getFilenameST().length() > 0) {
						Util.deleteFile(kontraktor.getFilenameST());
					}
					kontraktor.setFilenameST(null);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSijilSokongan();
	}
	
	@Command("doHapusSTB")
	public String doHapusSTB() throws Exception {
		try {
			mp = new MyPersistence();
			
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
			if (daftarKontraktor != null) {
				MtnKontraktor kontraktor = (MtnKontraktor) mp.find(MtnKontraktor.class, daftarKontraktor.getKontraktor().getId());
				if (kontraktor != null) {
					mp.begin();
					if (kontraktor.getFilenameSTB() != null && kontraktor.getFilenameSTB().length() > 0) {
						Util.deleteFile(kontraktor.getFilenameSTB());
					}
					kontraktor.setFilenameSTB(null);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getSijilSokongan();
	}	
	/** END SIJIL SOKONGAN **/
	
	/** START DOKUMEN SOKONGAN **/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idPendaftaran = getParam("idPendaftaran");
		String tajukDokumen = getParam("tajukDokumen");
		String idJenisDokumen = getParam("idJenisDokumen");
		String keteranganDokumen = getParam("keteranganDokumen");
		String uploadDir = "senggara/kontraktor/dokumenSokongan/";
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
		
		try {
			mp = new MyPersistence();
			MtnDokumen dokumen = new MtnDokumen();
			
			for (FileItem item : files) {
				String avatarName = "";
				String fileName = item.getName();
				MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idPendaftaran"));
				String imgName = uploadDir + daftarKontraktor.getKontraktor().getId() + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

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
					simpanDokumen(daftarKontraktor, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen, mp);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}
	
	public void simpanDokumen(MtnDaftarKontraktor daftarKontraktor, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, MtnDokumen dokumen, MyPersistence mp)
			throws Exception {
		
		mp.begin();
		dokumen.setKontraktor(daftarKontraktor.getKontraktor());
		dokumen.setPhotofilename(imgName);
		dokumen.setThumbfilename(avatarName);
		dokumen.setTajuk(tajukDokumen);
		dokumen.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		dokumen.setKeterangan(keteranganDokumen);
		mp.persist(dokumen);
		mp.commit();
	}
	
	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = getParam("idDokumen");
		
		try {
			mp = new MyPersistence();
			
			MtnDokumen dokumen = (MtnDokumen) mp.find(MtnDokumen.class, idDokumen);
			if (dokumen != null) {
				mp.begin();
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.remove(dokumen);
				mp.commit();
			}
		} catch (Exception ex) {
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
	@Command("selectBandar")
	public String selectBandar() throws Exception {	
		
		String idNegeri = "0";
		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		
		return getPath() + "/selectBandar.vm";
	}
	
	@Command("findPengkhususan")
	public String findPengkhususan() throws Exception {	
		
		String idKategori = "0";
		if (getParam("findKategori").trim().length() > 0)
			idKategori = getParam("findKategori");
		List<PengkhususanBidangKontraktor> list = dataUtil.getListPengkhususanBidangKontraktor(idKategori);
		context.put("selectPengkhususan", list);

		return getPath() + "/findPengkhususan.vm";
	}
	
	@Command("selectPengkhususan")
	public String selectPengkhususan() throws Exception {	
		
		String idKategori = "0";
		if (getParam("idKategori").trim().length() > 0)
			idKategori = getParam("idKategori");
		List<PengkhususanBidangKontraktor> list = dataUtil.getListPengkhususanBidangKontraktor(idKategori);
		context.put("selectPengkhususan", list);

		return getPath() + "/pengkhususanKontraktor/selectPengkhususan.vm";
	}
}

package portal.module;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.template.UID;
import lebah.util.PasswordService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.entities.integrasi.IntHRMIS;
import bph.entities.integrasi.IntPESARA;
import bph.entities.kod.Agama;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Bank;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.StatusPerkahwinan;
import bph.mail.mailer.TetapanSemulaKataLaluanMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class UsersListRecordModule extends LebahRecordTemplateModule<Users> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Util util = new Util();
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		return String.class;
	}
	
	@Override
	public String getPath() {
		return "vtl/users_list/";
	}
	
	@Override
	public Class<Users> getPersistenceClass() {
		return Users.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Hashtable<String, Object> map = new Hashtable<String, Object>();

		map.put("id", get("findLogPengguna").trim());
		map.put("userName", get("findNamaPengguna").trim());
		
		map.put("role.name", new OperatorEqualTo(get("findRole").trim()));	
		
		return map;
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectRole", dataUtil.getListRole());
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
			
	}

	private void defaultButtonOption() {
		
		this.setDisableAddNewRecordButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
		
	private void addfilter() {
		
		this.addFilter("flagDaftarSBBPH = 'Y'");
		this.setOrderBy("id");
		this.setOrderType("asc");
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(Users users) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void afterSave(Users users) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getRelatedData(Users users) {
		try {
			mp = new MyPersistence();
			defaultValue(mp, users.getId());	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.remove("updateProfileStatus");
		context.remove("resetPasswordStatus");
		context.remove("updateProfilePhoto");
		context.remove("semakanPerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		context.remove("updateMaklumatBank");
		context.remove("updateLampiranAkaun");
		context.put("selectedTab", 1);		
	}

	@Override
	public boolean delete(Users users) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}
	
	private void defaultValue(MyPersistence mp, String idPengguna) {
		dataUtil = DataUtil.getInstance(db);	
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userId", userId);
		context.put("userName", userName);
		context.put("userRole", userRole);
		
		Users pengguna = (Users) mp.find(Users.class, idPengguna);
		context.put("pengguna", pengguna);
		
		context.put("selectKategoriPengguna", dataUtil.getListKategoriPenggunaOnline());
		context.put("selectGelaran", dataUtil.getListGelaran());
		context.put("selectBadanBerkanun", dataUtil.getListBadanBerkanun());
		context.put("selectJantina", dataUtil.getListJantina());
		context.put("selectBangsa", dataUtil.getListBangsa());
		context.put("selectAgama", dataUtil.getListAgama());
		context.put("selectNegeri", dataUtil.getListNegeri());
		if (pengguna != null && pengguna.getUserBandar() != null)
			context.put("selectBandar", dataUtil.getListBandar(pengguna.getUserBandar().getNegeri().getId()));
		context.put("selectKementerian", dataUtil.getListKementerian());
		if (pengguna != null && pengguna.getAgensi() != null)
			context.put("selectAgensi", dataUtil.getListAgensi(pengguna.getAgensi().getKementerian().getId()));
		context.put("selectKelasPerkhidmatan", dataUtil.getListKelasPerkhidmatan());
		context.put("selectGredPerkhidmatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectBank", dataUtil.getListBank());
				
		//PROFILE PHOTO
		if (pengguna != null && pengguna.getProfilePhoto() != "") {
			String dir = ResourceBundle.getBundle("dbconnection").getString("folder");
			String urlSource = dir + pengguna.getAvatar();
			File source = new File(urlSource);
			if (source.exists()) {
				context.put("profilePhotoName", pengguna.getProfilePhoto());
				context.put("avatarPhotoName", pengguna.getAvatar());
			} else {
				context.put("profilePhotoName", "profilePhoto/no_photo.jpg");
				context.put("avatarPhotoName", "profilePhoto/no_photo_avatar.jpg");
			}
		} else {
			context.put("profilePhotoName", "profilePhoto/no_photo.jpg");
			context.put("avatarPhotoName", "profilePhoto/no_photo_avatar.jpg");
		}
		
		//TARIKH SEMAKAN HRMIS
		if (pengguna != null && "(AWAM) Penjawat Awam".equals(pengguna.getRole().getName())) {
			IntHRMIS hrmis = (IntHRMIS) mp.get("select x from IntHRMIS x where x.noPengenalan = '" + pengguna.getId() + "'");
			if (hrmis != null) {
				context.put("tarikhSemakan", util.getDateTime(hrmis.getTarikhTerima(), "dd-MM-yyyy hh:mm:ss a"));
			} else {
				context.put("tarikhSemakan", "-");
			}
		}
		//TARIKH SEMAKAN PESARA
		if (pengguna != null && "(AWAM) Pesara".equals(pengguna.getRole().getName())) {
			IntPESARA pesara = (IntPESARA) mp.get("select x from IntPESARA x where x.noPengenalan = '" + pengguna.getId() + "'");
			if (pesara != null) {
				context.put("tarikhSemakan", util.getDateTime(pesara.getTarikhTerima(), "dd-MM-yyyy hh:mm:ss a"));
			} else {
				context.put("tarikhSemakan", "-");
			}
		}
	}
	
	/** START SENARAI TAB **/
	@Command("getMaklumatProfil")
	public String getMaklumatProfil() {

		try {
			mp = new MyPersistence();
			defaultValue(mp, getParam("idPengguna"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.remove("semakanPerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		context.remove("updateMaklumatBank");
		context.remove("updateLampiranAkaun");
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPerjawatan")
	public String getMaklumatPerjawatan() {

		try {
			mp = new MyPersistence();
			defaultValue(mp, getParam("idPengguna"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.remove("updateProfileStatus");
		context.remove("resetPasswordStatus");
		context.remove("updateProfilePhoto");
		context.remove("updateMaklumatBank");
		context.remove("updateLampiranAkaun");
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {

		try {
			mp = new MyPersistence();
			defaultValue(mp, getParam("idPengguna"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.remove("updateProfileStatus");
		context.remove("resetPasswordStatus");
		context.remove("updateProfilePhoto");
		context.remove("semakanPerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	/** END SENARAI TAB **/
	
	/** START MAKLUMAT PROFIL **/
	@Command("kemaskiniPengguna")
	public String kemaskiniPengguna() {
		userId = getParam("idPengguna");
		boolean updateProfileStatus = false;
		try {
			mp = new MyPersistence();
			
			Gelaran gelaran = null;
			if (!"".equals(getParam("idGelaran"))) {
				gelaran = (Gelaran) mp.find(Gelaran.class, getParam("idGelaran"));
			}
			BadanBerkanun badanBerkanun = null;
			if (!"".equals(getParam("idBadanBerkanun"))) {
				badanBerkanun = (BadanBerkanun) mp.find(BadanBerkanun.class, getParam("idBadanBerkanun"));
			}
			Jantina jantina = null;
			if (!"".equals(getParam("idJantina"))) {
				jantina = (Jantina) mp.find(Jantina.class, getParam("idJantina"));
			}
			Bangsa bangsa = null;
			if (!"".equals(getParam("idBangsa"))) {
				bangsa = (Bangsa) mp.find(Bangsa.class, getParam("idBangsa"));
			}
			Agama agama = null;
			if (!"".equals(getParam("idAgama"))) {
				agama = (Agama) mp.find(Agama.class, getParam("idAgama"));
			}
			String userAddress = getParam("userAddress");
			String userAddress2 = getParam("userAddress2");
			String userAddress3 = getParam("userAddress3");
			String poskod = getParam("poskod");
			Bandar bandar = null;
			if (!"".equals(getParam("idBandar"))) {
				bandar = (Bandar) mp.find(Bandar.class, getParam("idBandar"));
			}
			String noTel = getParam("noTelefon");
			String noTelBimbit = getParam("noTelefonBimbit");
			String emel = getParam("emel");
			
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				pengguna.setGelaran(gelaran);
				pengguna.setBadanBerkanun(badanBerkanun);
				pengguna.setJantina(jantina);
				pengguna.setBangsa(bangsa);
				pengguna.setAgama(agama);
				pengguna.setUserAddress(userAddress);
				pengguna.setUserAddress2(userAddress2);
				pengguna.setUserAddress3(userAddress3);
				pengguna.setUserPostcode(poskod);
				pengguna.setUserBandar(bandar);
				pengguna.setNoTelefon(noTel);
				pengguna.setNoTelefonBimbit(noTelBimbit);
				pengguna.setEmel(emel);
				mp.commit();
				updateProfileStatus = true;
			} 
				
		} catch (Exception ex) {
			System.out.println("ERROR KEMASKINI PROFIL PENGGUNA : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("updateProfileStatus", updateProfileStatus);
		context.remove("resetPasswordStatus");
		context.remove("updateProfilePhoto");
		return getMaklumatProfil();
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadProfilPhoto")
	public String uploadProfilPhoto() throws Exception {
		String idPengguna = getParam("idPengguna");
			
		String uploadDir = "profilePhoto/";
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

			String imgName = uploadDir + idPengguna + "_"+ UID.getUID()
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
						250, 130, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
						38, 38, 100);
			}
			
			updateProfilePhoto(idPengguna, imgName, avatarName);
		}
		return getPath() + "/uploadProfilePhoto.vm";
	}

	private void updateProfilePhoto(String idPengguna, String profilePhoto, String avatarName) { 
		boolean updateProfilePhoto = false;
		mp = new MyPersistence();
		try {			
			Users pengguna = (Users) mp.find(Users.class, idPengguna);
			if (pengguna != null) {
				mp.begin();
				Util.deleteFile(pengguna.getProfilePhoto());
				Util.deleteFile(pengguna.getAvatar());
				pengguna.setProfilePhoto(profilePhoto);
				pengguna.setAvatar(avatarName);
				mp.commit();
				updateProfilePhoto = true;
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE PROFILE PHOTO : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("updateProfilePhoto", updateProfilePhoto);
	}

	@Command("refreshProfilePhoto")
	public String refreshProfilePhoto() throws Exception {
		context.put("updateProfilePhoto", getParam("updateProfilePhotoReload"));
		context.remove("updateProfileStatus");
		context.remove("resetPasswordStatus");
		return getMaklumatProfil();
	}
	
	@Command("resetPassword")
	public String resetPassword() {
		userId = getParam("idPengguna");
		boolean resetPasswordStatus = false;
		try {
			mp = new MyPersistence();
						
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				if (pengguna.getEmel() == null || pengguna.getEmel().length() == 0) {
					pengguna.setEmel(getParam("emel"));
				}
				pengguna.setUserPassword(PasswordService.encrypt(pengguna.getId()));				
				mp.commit();
				resetPasswordStatus = true;
				
				// GENERATE EMEL
				generateEmel(pengguna, pengguna.getId(), pengguna.getEmel());				
			} 
				
		} catch (Exception ex) {
			System.out.println("ERROR RESET PASSWORD PENGGUNA : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.remove("updateProfileStatus");
		context.put("resetPasswordStatus", resetPasswordStatus);
		context.remove("updateProfilePhoto");
		return getMaklumatProfil();
	}
	
	private void generateEmel(Users user, String password, String emel) {
		if (user.getRole().getName().equals("(AWAM) Pengguna Awam")) {
			TetapanSemulaKataLaluanMailer.get().hantarPassword(emel,
					user.getId(), password);
		} else if (user.getRole().getName().equals("(AWAM) Penjawat Awam")) {
			if (user.getFlagSemakanHRMIS().equals("Y")) {
				TetapanSemulaKataLaluanMailer.get().hantarPassword(emel,
						user.getId(), password);
			} else {
				TetapanSemulaKataLaluanMailer.get().hantarSemakan(emel);
			}
		} else if (user.getRole().getName().equals("(AWAM) Pesara")) {
			if (user.getFlagSemakanPESARA().equals("Y")) {
				TetapanSemulaKataLaluanMailer.get().hantarPassword(emel,
						user.getId(), password);
			} else {
				TetapanSemulaKataLaluanMailer.get().hantarSemakan(emel);
			}
		} else if (user.getRole().getName().equals("(AWAM) Badan Berkanun")
				|| user.getRole().getName()
						.equals("((AWAM) Pesara Polis / Tentera")
				|| user.getRole().getName()
						.equals("((AWAM) Pesara Polis / Tentera")) {
			if (user.getFlagAktif().equals("Y")) {
				TetapanSemulaKataLaluanMailer.get().hantarPassword(emel,
						user.getId(), password);
			} else {
				TetapanSemulaKataLaluanMailer.get().hantarPasswordBadanBerkanun(
						emel, user.getId(), password);
			}
		} else {
			TetapanSemulaKataLaluanMailer.get().hantarPassword(emel,
					user.getId(), password);
		}
	}
	/** END MAKLUMAT PROFIL **/
	
	/** START MAKLUMAT PERJAWATAN **/
	@Command("semakanHRMIS")
	public String semakanHRMIS() {
		userId = getParam("idPengguna");
		boolean semakanPerjawatanStatus = false;		
		try {
			mp = new MyPersistence();
			
			System.out.println("VALIDATE HRMIS USING BPH WEB SERVICE");	
			BPHServicesImplService service = new BPHServicesImplService();
			BPHServices bphService = service.getPort(BPHServices.class);
			// EXTRA CONTEXT
			String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
			int connectionTimeOutInMs = 10000;// 10 Second
			BindingProvider p = (BindingProvider) bphService;
			p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,WS_URL);
			sbbph.ws.HrmisManager hrmisManagerService = null;
			hrmisManagerService = bphService.semakanPenjawatAwam(userId);

			if (hrmisManagerService.isFlagSemakanHRMIS()) {
				semakanPerjawatanStatus =  true;
				
				IntHRMIS hrmis = (IntHRMIS) mp
						.get("select x FROM IntHRMIS x where x.noPengenalan = '"
								+ userId + "' order by x.tarikhTerima desc");
				
				if(hrmis != null){
					Users pengguna = (Users) mp.find(Users.class, userId);
					if (pengguna != null) {
						if (hrmis != null) {
							mp.begin();
							if (hrmis.getKodGelaran() != null)
								pengguna.setGelaran((Gelaran) mp.find(Gelaran.class, hrmis.getKodGelaran()));
							if (hrmis.getNoTelefonPejabat() != null)	
								pengguna.setNoTelefonPejabat(hrmis.getNoTelefonPejabat());
							if (hrmis.getKodAgensi() != null)
								pengguna.setAgensi((Agensi) mp.find(Agensi.class, hrmis.getKodAgensi()));
							if (hrmis.getJabatan() != null)
								pengguna.setBahagian(hrmis.getJabatan());
							if (hrmis.getKodKelasPerkhidmatan() != null)
								pengguna.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, hrmis.getKodKelasPerkhidmatan()));
							if (hrmis.getKodGredPerkhidmatan() != null)
								pengguna.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, hrmis.getKodGredPerkhidmatan()));
							if (hrmis.getKodEtnik() != null) 
								pengguna.setEtnik((Etnik) mp.find(Etnik.class, hrmis.getKodEtnik()));
							if (hrmis.getKodStatusPerkahwinan() != null)
								pengguna.setStatusPerkahwinan((StatusPerkahwinan) mp.find(StatusPerkahwinan.class, hrmis.getKodStatusPerkahwinan()));
							pengguna.setFlagAktif("Y");
							pengguna.setFlagSemakanHRMIS("Y");
							
							// USER JOB
							boolean addJob = false;
							UsersJob job = (UsersJob) mp.get("select x from UsersJob x where x.users.id = '" + pengguna.getId() + "'");
							if (job == null) {
								addJob = true;
								job = new UsersJob();
								job.setUsers(pengguna);
							}
							job.setJawatan(pengguna.getJawatan());
							job.setGredJawatan(pengguna.getGredPerkhidmatan());
							job.setJenisPerkhidmatan(pengguna.getJenisPerkhidmatan());
							job.setKelasPerkhidmatan(pengguna.getKelasPerkhidmatan());
							job.setAgensi(pengguna.getAgensi());
							job.setBahagian(pengguna.getBahagian());
							job.setBadanBerkanun(pengguna.getBadanBerkanun());
							if (hrmis != null) {
								job.setTarikhLantikan(hrmis.getTarikhMulaSandang());
								job.setTarikhTamat(hrmis.getTarikhTamatSandang());
								job.setAlamat1(hrmis.getAlamatPejabat1());
								job.setAlamat2(hrmis.getAlamatPejabat2());
								job.setAlamat3(hrmis.getAlamatPejabat3());
								job.setPoskod(hrmis.getPoskodPejabat());
								if (hrmis.getKodBandarPejabat() != null)
									job.setBandar((Bandar) mp.find(Bandar.class, hrmis.getKodBandarPejabat()));
								job.setNoTelPejabat(hrmis.getNoTelefonPejabat());	
								job.setEmel(hrmis.getEmel());
							}
							if (addJob)
								mp.persist(job);
							
							// USER SPOUSE
							boolean addSpouse = false;
							UsersSpouse spouse = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + pengguna.getId() + "'");
							if (spouse == null) {
								addSpouse = true;
								spouse = new UsersSpouse();
								spouse.setUsers(pengguna);
							}
							// DATA DR HRMIS
							if (hrmis != null) {
								spouse.setNamaPasangan(hrmis.getNamaPasangan());
								spouse.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
								spouse.setNoKPPasangan(hrmis.getNoPengenalanPasangan());
								spouse.setNoTelBimbit(hrmis.getNoTelefonPasangan());
								spouse.setJenisPekerjaan(hrmis.getPekerjaanPasangan());
								spouse.setNamaSyarikat(hrmis.getMajikanPasangan());
							}
							if (addSpouse)
								mp.persist(spouse);	
							
							mp.commit();
						}						
					}					
				}				
			}
		} catch (Exception ex) {
			System.out.println("ERROR SEMAKAN HRMIS : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("semakanPerjawatanStatus", semakanPerjawatanStatus);
		context.remove("updatePerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		return getMaklumatPerjawatan();
	}
	
	@Command("semakanPESARA")
	public String semakanPESARA() {
		userId = getParam("idPengguna");
		boolean semakanPerjawatanStatus = false;		
		try {
			mp = new MyPersistence();
			
			System.out.println("VALIDATE PESARA USING BPH WEB SERVICE");
			BPHServicesImplService service = new BPHServicesImplService();
			BPHServices bphService = service.getPort(BPHServices.class);
			// EXTRA CONTEXT
			String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
			int connectionTimeOutInMs = 10000;// 10 Second
			BindingProvider p = (BindingProvider) bphService;
			p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,WS_URL);
			sbbph.ws.PesaraManager pesaraManagerService = null;
			pesaraManagerService = bphService.semakanPesara(userId);

			if (pesaraManagerService.isFlagSemakanPESARA()) {
				semakanPerjawatanStatus =  true;
				
				IntPESARA pesara = (IntPESARA) mp
						.get("select x FROM IntPESARA x where x.noPengenalan = '"
								+ userId + "' order by x.tarikhTerima desc");
				
				if(pesara != null){
					Users pengguna = (Users) mp.find(Users.class, userId);
					if (pesara != null) {
						if (pesara != null) {
							mp.begin();
							if (pesara.getCawangan() != null)
								pengguna.setBahagian(pesara.getCawangan());
							if (pesara.getKodJawatan() != null)
								pengguna.setJawatan((Jawatan) mp.find(Jawatan.class, pesara.getKodJawatan()));
							if (pesara.getJawatanTerakhir() != null)
								pengguna.setKeteranganJawatan(pesara.getJawatanTerakhir());
							if (pesara.getKelasPerkhidmatan() != null)
								pengguna.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, pesara.getKelasPerkhidmatan()));
							if (pesara.getGredPerkhidmatan() != null)
								pengguna.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, pesara.getGredPerkhidmatan()));
							pengguna.setFlagAktif("Y");
							pengguna.setFlagSemakanPESARA("Y");
							mp.commit();
						}						
					}					
				}				
			}
		} catch (Exception ex) {
			System.out.println("ERROR SEMAKAN PESARA : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("semakanPerjawatanStatus", semakanPerjawatanStatus);
		context.remove("updatePerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		return getMaklumatPerjawatan();
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadDokumenPengesahan")
	public String uploadDokumenPengesahan() throws Exception {
		String idPengguna = getParam("idPengguna");
			
		String uploadDir = "pendaftaranPengguna/dokumenPengesahan/";
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
			String fileName = item.getName();

			String imgName = uploadDir + idPengguna + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(imgName);
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			
			updateDokumenPengesahan(idPengguna, imgName);
		}
		return getPath() + "/uploadDokumenPengesahan.vm";
	}

	private void updateDokumenPengesahan(String idPengguna, String dokumenPengesahan) { 
		boolean updateDokumenPengesahan = false;
		mp = new MyPersistence();
		try {			
			Users pengguna = (Users) mp.find(Users.class, idPengguna);
			if (pengguna != null) {
				mp.begin();				
				pengguna.setDokumenSokongan(dokumenPengesahan);
				pengguna.setCatatanPengesahan(null);
				mp.commit();
				updateDokumenPengesahan = true;
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE DOKUMEN PENGESAHAN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("updateDokumenPengesahan", updateDokumenPengesahan);
	}

	@Command("refreshDokumenPengesahan")
	public String refreshDokumenPengesahan() throws Exception {
		context.put("updateDokumenPengesahan", getParam("updateDokumenPengesahanReload"));
		context.remove("updatePerjawatanStatus");
		context.remove("semakanPerjawatanStatus");
		return getMaklumatPerjawatan();
	}
	
	@Command("hapusDokumenPengesahan")
	public String hapusDokumenPengesahan() {
		userId = getParam("idPengguna");
		try {
			mp = new MyPersistence();			
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				Util.deleteFile(pengguna.getDokumenSokongan());
				pengguna.setDokumenSokongan(null);
				mp.commit();
			} 
		} catch (Exception ex) {
			System.out.println("ERROR KEMASKINI DOKUMEN PENGESAHAN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.remove("updatePerjawatanStatus");
		context.remove("semakanPerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		return getMaklumatPerjawatan();
	}
	
	@Command("kemaskiniPerjawatan")
	public String kemaskiniPerjawatan() {
		userId = getParam("idPengguna");
		boolean updatePerjawatanStatus = false;
		try {
			mp = new MyPersistence();
			
			Agensi agensi = null;
			if (!"".equals(getParam("idAgensi"))) {
				agensi = (Agensi) mp.find(Agensi.class, getParam("idAgensi"));
			}
			String bahagian = getParam("bahagian");
			KelasPerkhidmatan kelasPerkhidmatan = null;
			if (!"".equals(getParam("idKelasPerkhidmatan"))) {
				kelasPerkhidmatan = (KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, getParam("idKelasPerkhidmatan"));
			}
			GredPerkhidmatan gredPerkhidmatan = null;
			if (!"".equals(getParam("idGredPerkhidmatan"))) {
				gredPerkhidmatan = (GredPerkhidmatan) mp.find(GredPerkhidmatan.class, getParam("idGredPerkhidmatan"));
			}									
			
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				pengguna.setAgensi(agensi);
				pengguna.setBahagian(bahagian);
				pengguna.setKelasPerkhidmatan(kelasPerkhidmatan);
				pengguna.setGredPerkhidmatan(gredPerkhidmatan);				
				mp.commit();
				updatePerjawatanStatus = true;
			} 
				
		} catch (Exception ex) {
			System.out.println("ERROR KEMASKINI PROFIL PENGGUNA : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("updatePerjawatanStatus", updatePerjawatanStatus);
		context.remove("semakanPerjawatanStatus");
		context.remove("updateDokumenPengesahan");
		return getMaklumatPerjawatan();
	}
	/** END MAKLUMAT PERJAWATAN **/
	
	/** START DOKUMEN SOKONGAN **/
	@Command("kemaskiniMaklumatBank")
	public String kemaskiniMaklumatBank() {
		userId = getParam("idPengguna");
		boolean updateMaklumatBank = false;
		try {
			mp = new MyPersistence();
			
			Bank bank = null;
			if (!"".equals(getParam("idBank"))) {
				bank = (Bank) mp.find(Bank.class, getParam("idBank"));
			}
			String noAkaunBank = getParam("noAkaunBank");
			String cawanganBank = getParam("cawanganBank");
			
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				pengguna.setBank(bank);
				pengguna.setNoAkaunBank(noAkaunBank);
				pengguna.setCawanganBank(cawanganBank);
				mp.commit();
				updateMaklumatBank = true;
			} 
		} catch (Exception ex) {
			System.out.println("ERROR KEMASKINI MAKLUMAT BANK : " + ex.getMessage());
			ex.printStackTrace();
		}
		finally {
			if (mp != null) { mp.close(); }
		}

		context.put("updateMaklumatBank", updateMaklumatBank);
		context.remove("updateLampiranAkaun");
		return getDokumenSokongan();
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadLampiranAkaun")
	public String uploadLampiranAkaun() throws Exception {
		String idPengguna = getParam("idPengguna");
			
		String uploadDir = "lampiranBank/";
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
			String fileName = item.getName();

			String imgName = uploadDir + idPengguna + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(imgName);
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			
			updateLampiranAkaun(idPengguna, imgName);
		}
		return getPath() + "/uploadLampiranAkaun.vm";
	}

	private void updateLampiranAkaun(String idPengguna, String dokumenBank) { 
		boolean updateLampiranAkaun = false;
		mp = new MyPersistence();
		try {			
			Users pengguna = (Users) mp.find(Users.class, idPengguna);
			if (pengguna != null) {
				mp.begin();				
				pengguna.setDokumenBank(dokumenBank);
				mp.commit();
				updateLampiranAkaun = true;
			}			
		} catch (Exception ex) {
			System.out.println("ERROR UPDATE LAMPIRAN AKAUN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("updateLampiranAkaun", updateLampiranAkaun);
	}

	@Command("refreshLampiranAkaun")
	public String refreshLampiranAkaun() throws Exception {
		context.put("updateLampiranAkaun", getParam("updateLampiranAkaunReload"));
		context.remove("updateMaklumatBank");
		return getDokumenSokongan();
	}
	
	@Command("hapusLampiranAkaun")
	public String hapusLampiranAkaun() {
		userId = getParam("idPengguna");
		try {
			mp = new MyPersistence();			
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				Util.deleteFile(pengguna.getDokumenBank());
				pengguna.setDokumenBank(null);
				mp.commit();
			} 	
		} catch (Exception ex) {
			System.out.println("ERROR KEMASKINI MAKLUMAT BANK : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.remove("updateMaklumatBank");
		context.remove("updateLampiranAkaun");
		return getDokumenSokongan();
	}
	/** END DOKUMEN SOKONGAN **/

	/** START DROP DOWN **/
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";

		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");
		context.put("selectBandar", dataUtil.getListBandar(idNegeri));
		return getPath() + "/selectBandar.vm";
	}
	
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";

		if (getParam("idKementerian").trim().length() > 0)
			idKementerian = getParam("idKementerian");
		context.put("selectAgensi", dataUtil.getListAgensi(idKementerian));
		return getPath() + "/selectAgensi.vm";
	}
	
	@Command("selectSeksyen")
	public String selectSeksyen() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idBahagian = "0";

		if (getParam("idBahagian").trim().length() > 0)
			idBahagian = getParam("idBahagian");
		context.put("selectSeksyen", dataUtil.getListSeksyen(idBahagian));
		return getPath() + "/selectSeksyen.vm";
	}
	/** END DROP DOWN **/
	
	@Command("kemaskiniFlagGelanggang")
	public String kemaskiniFlagGelanggang() {
		userId = getParam("idPengguna");
		try {
			mp = new MyPersistence();
			Users pengguna = (Users) mp.find(Users.class, userId);
			if (pengguna != null) {
				mp.begin();
				if(pengguna.getFlagGelanggang().equalsIgnoreCase("T")){
					pengguna.setFlagGelanggang("Y");
				}else{
					pengguna.setFlagGelanggang("T");
				}
				mp.commit();
			} 		
		} catch (Exception ex) {
			System.out.println("ERROR KEMASKINI FLAG GELANGGANG : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatProfil();
	}
}
// Author : zulfazdliabuas@gmail.com Date 2015 - 2017

package bph.portal;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import lebah.template.UID;
import lebah.util.PasswordService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import portal.module.entity.UsersSpouse;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.entities.integrasi.IntHRMIS;
import bph.entities.integrasi.IntJPN;
import bph.entities.integrasi.IntPESARA;
import bph.entities.kod.Agama;
import bph.entities.kod.Agensi;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Etnik;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisAduan;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.KategoriPengguna;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.Status;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.SumberAduan;
import bph.entities.kontrak.KontrakKontraktor;
import bph.entities.portal.Giliran;
import bph.entities.portal.WebKepuasanPelanggan;
import bph.entities.portal.WebUndian;
import bph.entities.pro.ProAduan;
import bph.entities.pro.ProSequenceAduan;
import bph.mail.mailer.AduanMailer;
import bph.mail.mailer.DaftarAkaunBaruMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmPortalHome extends LebahModule {

	/**
	 * 
	 **/
	static Logger myLogger = Logger.getLogger("FrmPortalHome");
	private static final long serialVersionUID = 494389425922921030L;
	private DbPersistence db = new DbPersistence();
	private MyPersistence mp;
	private DataUtil dataUtil;
	private PortalUtil portalUtil = new PortalUtil();
	private Util util = new Util();
	
	protected List<String> filterList = new ArrayList<String>();
	
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/portal";
	}

	@Override
	public void preProcess() {
		context.put("util", util);
		context.put("path", getPath());
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	public String start() {
		// TODO : SAVE ONLY BY UNIQUE SESSION
		// REMARK FIRST BECAUSE OF PERFORMANCE ISSUES
		// util.setPageView(request.getHeader("User-Agent"));
		
		
		try {
			mp = new MyPersistence();
			
			if(portalUtil.getListPengumuman(mp).size() > 0){
				context.put("listPengumuman", portalUtil.getListPengumuman(mp));
			} else{
				context.remove("listPengumuman");
			}
			
			if(portalUtil.getListMakluman(mp).size() > 0){
				context.put("listMakluman", portalUtil.getListMakluman(mp));
			} else{
				context.remove("listMakluman");
			}
			
			context.put("listProfilKorporat", portalUtil.getListProfilKorporat(mp));
			context.put("listRujukan", portalUtil.getListRujukan(mp));
			context.put("listBantuan", portalUtil.getListBantuan(mp));		
			context.put("listSlideShow", portalUtil.getListSlideShow(mp));		
			context.put("listInformasi", portalUtil.getListInformasi(mp));		
			context.put("listPautan1", portalUtil.getListPautan1(mp));
			context.put("listPautan2", portalUtil.getListPautan2(mp));		
			context.put("hubungiKami", portalUtil.getHubungiKami(mp));		
			context.put("wargaBph", portalUtil.getListWargaBph(mp));		
			context.put("listkuarters", portalUtil.getListPenyelenggaraanKuarters(mp));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}

		context.put("util", util);
		context.put("path", getPath());
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		return getPath() + "/main/start.vm";
	}
	
	/********************************************** START MAIN CONTENT PORTAL *********************************************/
	@Command("getMainContent")
	public String getMainContent() throws Exception {
		
		String header = getParam("header");
		myLogger.debug("getMainContent Header === " + header);
		String paramId = "";		
		
		/** START EDITED BY PEJE - DROPDOWN MENU **/
		if ("Peta Laman".equals(header)) {
			context.put("getContent", "getPetaLaman");
			
		} else if ("HubungiKami".equals(header)) {
			context.put("getContent", "getHubungiKami");
			
		} else if ("ProfilKorporat".equals(header)) {
			header = "Profil Korporat";
			paramId = getParam("idProfilKorporat");
			context.put("getContent", "getProfilKorporat");
			
		} else if ("Rujukan".equals(header)) {
			//header = "Rujukan";  ## Rujukan diminta tukar kepada Pekeliling pada 21/2/2012 oleh puan aida it
			header = "Pekeliling";
			paramId = getParam("idRujukan");
			context.put("getContent", "getRujukan");
			
		} else if ("Bantuan".equals(header)) {
			header = "Bantuan";
			paramId = getParam("idBantuan");
			context.put("getContent", "getBantuan");
			
		} else if ("Direktori".equals(header)) {
			header = "Direktori";
			context.put("getContent", "getDirektori");
			
		} else if ("PendaftaranAkaun".equals(header)) {
			header = "Pendaftaran Akaun Pengguna";
			context.put("getContent", "getRegister");
			
		} else if ("PerkhidmatanAduan".equals(header)) {
			header = "Perkhidmatan > e-Aduan";
			context.put("getContent", "getAduan");
			
		} else if ("Informasi".equals(header)) {
			header = "Informasi";
			paramId = getParam("idInformasi");
			context.put("getContent", "getInformasi");
			
		} else if ("GaleriGambar".equals(header)) {			
			header = "Galeri Gambar";
			context.put("getContent", "getGaleriGambar");
			
		} else if ("GaleriVideo".equals(header)) {			
			header = "Galeri Video";			
			context.put("getContent", "getGaleriVideo");
			
		} else if ("KeputusanMaklumbalas".equals(header)) {
			header = "Keputusan Maklumbalas Pengguna";
			context.put("getContent", "getKeputusanMaklumbalas");
			
		} else if ("KajianKepuasanPelanggan".equals(header)) {
			header = "Kajian Kepuasan Pelanggan";
			context.put("getContent", "getKeputusanKepuasanPelanggan");
			
		} else if ("Keharta".equals(header)) {
			context.put("getContent", "getKeharta");
			
		} else if ("Puspanita".equals(header)) {
			context.put("getContent", "getPuspanita");
		}
		/** END EDITED BY PEJE  - DROPDOWN MENU **/
		
		//Start AddBy zulfazdliabuas@gmail.com
		else if ("Akrab".equals(header)) {
			context.put("getContent", "getAkrab");
		}//End AddBy zul
		
		else if ("PerkhidmatanKuarters".equals(header)) {
			header = "Perkhidmatan > Pengurusan Kuarters";
			context.put("getContent", "getPerkhidmatanKuarters");
			
		} else if ("PerkhidmatanRuangPejabat".equals(header)) {
			header = "Perkhidmatan > Ruang Pejabat";
			context.put("getContent", "getPerkhidmatanRuangPejabat");
			
		} else if ("PenyelenggaraanKuarters".equals(header)) {
			header = "Perkhidmatan > Penyelenggaraan Kuarters";
			paramId = getParam("idKuarters");
			context.put("getContent", "getPenyelenggaraanKuarters");
			
		} else if ("PerkhidmatanRumahPeranginan".equals(header)) {
			header = "Perkhidmatan > Rumah Peranginan Persekutuan & Transit";
			context.put("getContent", "getPerkhidmatanRumahPeranginan");
			
		} else if ("PerkhidmatanDewanGelanggang".equals(header)) {
			header = "Perkhidmatan > Dewan Gelanggang";
			context.put("getContent", "getPerkhidmatanDewanGelanggang");
			
		} else if ("Pekeliling".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getPekeliling");
			
		} else if ("Hakcipta Penafian".equals(header)) {
			context.put("getContent", "getDisclaimer");
			
		} else if ("Dasar Keselamatan".equals(header)) {
			context.put("getContent", "getSecurityPolicy");
			
		} else if ("Dasar Privasi".equals(header)) {
			context.put("getContent", "getPrivacyPolicy");
			
		} else if ("Soalan Lazim".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getSoalanLazim");
			
		} else if ("Manual Portal".equals(header)) {
			header = "Bantuan";
			context.put("getContent", "getManualPengguna");
			
		} else if ("FAQ Rumah Peranginan".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getFAQRumahPeranginan");
			
		} else if ("FAQ Kuarters".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getFAQKuarters");
			
		} else if ("FAQ Ruang Pejabat".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getFAQRuangPejabat");
			
		} else if ("FAQ Unit Pembangunan".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getFAQUnitPembangunan");
			
		} else if ("FAQ Unit Perhubungan Awam".equals(header)) {
			header = "Rujukan";
			context.put("getContent", "getFAQUnitPRO");
			
		} else if ("Panduan Pengguna".equals(header)) {
			header = "Panduan Pengguna";
			context.put("getContent", "getPanduanPengguna");
			
		} else if ("Semak Senarai Menunggu".equals(header)) {
			header = "Semak Senarai Menunggu";
			context.put("getContent", "getSemakSenaraiMenunggu");
		}

		context.put("paramId", paramId);
		context.put("header", header);
		context.put("path", getPath());
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));

		return getPath() + "/main/content.vm";
    }
	/*********************************************** END MAIN CONTENT PORTAL ***********************************************/
	
	/*********************************************** START LEFT MENU PORTAL ***********************************************/
	@Command("getLeftMenu")
	public String getLeftMenu() throws Exception {		
		
		String leftMenu = getParam("leftMenu");
		myLogger.debug("getLeftMenu leftMenu === " + leftMenu);
		String subPath = "";

		try {
			mp = new MyPersistence();
			
			/** START EDITED BY PEJE **/
			if ("getPetaLaman".equals(leftMenu)) {
				subPath = "petaLaman";
				
			} else if ("getHubungiKami".equals(leftMenu)) {
				subPath = "hubungiKami";
				
			} else if ("getProfilKorporat".equals(leftMenu)) {			
				subPath = "profilKorporat";
				
			} else if ("getRujukan".equals(leftMenu)) {			
				subPath = "rujukan";
				
			} else if ("getBantuan".equals(leftMenu)) {			
				subPath = "bantuan";
				
			} else if ("getDirektori".equals(leftMenu)) {			
				subPath = "direktori";
				
			} else if ("getRegister".equals(leftMenu)) {
				subPath = "register";
				
			} else if ("getAduan".equals(leftMenu)) {
				subPath = "aduan";
				
			} else if ("getInformasi".equals(leftMenu)) {
				context.put("listInformasi", portalUtil.getListInformasi(mp));
				subPath = "informasi";
				
			} else if ("getGaleriGambar".equals(leftMenu)) {
				subPath = "galeri";
				
			} else if ("getGaleriVideo".equals(leftMenu)) {
				subPath = "galeri";
				
			} else if ("getKeputusanMaklumbalas".equals(leftMenu)) {
				subPath = "keputusanMaklumbalas";
				
			} else if ("getKeputusanKepuasanPelanggan".equals(leftMenu)) {
				subPath = "keputusanKepuasanPelanggan";
				
			} else if ("getKeharta".equals(leftMenu)) {
				subPath = "keharta";
				
			} else if ("getPuspanita".equals(leftMenu)) {
				subPath = "puspanita";
				
			}	
			/** END EDITED BY PEJE **/	
			
			/** Start AddBy zulfazdliabuas@gmail.com Date 14/6/2017**/	
			else if ("getAkrab".equals(leftMenu)) {
				subPath = "akrab";
			}
			/** Start AddBy zul **/	
			
			else if ("getPerkhidmatanKuarters".equals(leftMenu)) {
				subPath = "old/kuarters";
				
			} else if ("getPerkhidmatanRuangPejabat".equals(leftMenu)) {
				subPath = "old/ruangPejabat";
				
			} else if ("getPenyelenggaraanKuarters".equals(leftMenu)) {
	     		subPath = "old/penyelenggaraanKuarters";  // pggl file penyelenggaraanKuarters.vm utk link left menu - ain
			
			} else if ("getPerkhidmatanRumahPeranginan".equals(leftMenu)) {
				subPath = "old/rumahPeranginan";
			
			} else if ("getPerkhidmatanDewanGelanggang".equals(leftMenu)) {
				subPath = "old/dewanGelanggang";
			
			} else if ("getPekeliling".equals(leftMenu)) {
				subPath = "pekeliling";
			
			}else if ("getDisclaimer".equals(leftMenu)) {
				subPath = "old/disclaimer";
			
			} else if ("getSecurityPolicy".equals(leftMenu)) {
				subPath = "old/securityPolicy";
			
			} else if ("getPrivacyPolicy".equals(leftMenu)) {
				subPath = "old/privacyPolicy";
			
			}     else if ("getMainPengumumanStatusGST".equals(leftMenu)) {
				subPath = "statusGST";
			
			} else if ("getMainPengumumanDeposit".equals(leftMenu)) {
				subPath = "pengumumanDeposit";
			
			} else if ("getMainPengumumanRPPPD".equals(leftMenu)) {
				subPath = "pengumumanRPPPD";
			
			} else if ("getMainPengumumanPeluangPekerjaan".equals(leftMenu)) {
				subPath = "peluangKerja";
			
			} else if ("getMainPengumumanNotisMakluman".equals(leftMenu)) {
				subPath = "notisMakluman";
			
			} else if ("getSoalanLazim".equals(leftMenu)) {
				subPath = "soalanLazim";
			
			} else if ("getManualPengguna".equals(leftMenu)) {
				subPath = "manualPortal";
			
			} else if ("getPerutusanSUB".equals(leftMenu)) {
				subPath = "perutusanSUB";
			
			} else if ("getPencapaianPiagam".equals(leftMenu)) {
				subPath = "pencapaianPiagam";
			
			} else if ("getFAQRumahPeranginan".equals(leftMenu)) {
				subPath = "faqRumahPeranginan";
			
			} else if ("getFAQKuarters".equals(leftMenu)) {
				subPath = "faqKuarters";
			
			} else if ("getFAQRuangPejabat".equals(leftMenu)) {
				subPath = "faqRuangPejabat";
			
			} else if ("getFAQUnitPembangunan".equals(leftMenu)) {
				subPath = "faqUnitPembangunan";
			
			} else if ("getFAQUnitPRO".equals(leftMenu)) {
				subPath = "faqUnitPRO";
			
			} else if ("getPanduanPengguna".equals(leftMenu)) {
				subPath = "panduanPengguna";
			
			} else if ("getMainIklanRuangKomersil".equals(leftMenu)) {
				subPath = "iklanRuangKomersil";
			
			} else if ("getMainTenderSebutHarga".equals(leftMenu)) {
				subPath = "iklanSebutHarga";
			
			} else if ("getMainIklanPengambilanPS_IT".equals(leftMenu)) {
				subPath = "iklanPengambilanPS_IT";
			
			} else if ("getMainIklanKerjaRequisition".equals(leftMenu)) {
				subPath = "iklanKerjaRequisition";
			
			}else if ( "getSemakSenaraiMenunggu".equals(leftMenu) ) {
	            subPath = "semakanMenunggu";    
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}

		return getPath() + "/main/left-menu/" + subPath + ".vm";
	}	
	/*********************************************** END LEFT MENU PORTAL ***********************************************/
	
	/********************************************** START CONTENT PORTAL *********************************************/
	@Command("getLamanUtama")
	public String getLamanUtama() {		
		return getPath() + "/main/main-content.vm";
	}
	
	@Command("getPetaLaman")
	public String getPetaLaman() {
		try {
			mp = new MyPersistence();			
			context.put("listInformasi", portalUtil.getListInformasi(mp));
			context.put("listProfilKorporat", portalUtil.getListProfilKorporat(mp));
			context.put("listPekeliling", portalUtil.getListSubRujukan("2204502651635", mp)); // PEKELILING
			context.put("listSoalanLazim", portalUtil.getListSubRujukan("4472112711857", mp)); // SOALAN LAZIM
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		return getPath() + "/content/petaLaman/start.vm";
	}
	
	@Command("getHubungiKami")
	public String getHubungiKami() {
		try {
			mp = new MyPersistence();
			context.put("hubungiKami", portalUtil.getHubungiKami(mp));
			context.put("listHubungiKami", portalUtil.getListHubungiKami(mp));
			context.put("operatorUnit", portalUtil.getListOperatorUnit(mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}
		return getPath() + "/content/hubungiKami/start.vm";
	}
	
	@Command("getPetaLokasi")
	public String getPetaLokasi() {
		return getPath() + "/content/hubungiKami/peta/start.vm";
	}
	
	@Command("getOperatorUnit")
	public String getOperatorUnit() {
		return getPath() + "/content/hubungiKami/operatorUnit/start.vm";
	}
	
	@Command("getProfilKorporat")
	public String getProfilKorporat() {
		try {
			mp = new MyPersistence();
			String idProfilKorporat = getParam("paramId");	
			context.put("listProfilKorporat", portalUtil.getListProfilKorporat(mp));
			context.put("profilKorporat", portalUtil.getProfilKorporat(idProfilKorporat, mp));
			context.put("listSubProfilKorporat", portalUtil.getListSubProfilKorporat(idProfilKorporat, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/profilKorporat/start.vm";
	}
	
	@Command("getRujukan")
	public String getRujukan() {
		try {
			mp = new MyPersistence();
			String idRujukan = getParam("paramId");
			context.put("listRujukan", portalUtil.getListRujukan(mp));
			context.put("rujukan", portalUtil.getRujukan(idRujukan, mp));
			context.put("listSubRujukan", portalUtil.getListSubRujukan(idRujukan, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/rujukan/start.vm";
	}
	
	@Command("getBantuan")
	public String getBantuan() {
		try {
			mp = new MyPersistence();
			String idBantuan = getParam("paramId");		
			context.put("listBantuan", portalUtil.getListBantuan(mp));
			context.put("bantuan", portalUtil.getBantuan(idBantuan, mp));
			context.put("listSubBantuan", portalUtil.getListSubBantuan(idBantuan, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/bantuan/start.vm";
	}
	
	@Command("getDirektori")
	public String getDirektori() {
		try {
			mp = new MyPersistence();
			context.put("listBahagian1", portalUtil.getListBahagian1(mp));
			context.put("listBahagian2", portalUtil.getListBahagian2(mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/direktori/start.vm";
	}

	@Command("getSubDirektori")
	public String getSubDirektori() {
		try {
			mp = new MyPersistence();
			String idBahagian = getParam("idBahagian");
			context.put("bahagian", portalUtil.getBahagian(idBahagian, mp));
			context.put("listKetuaBahagian", portalUtil.getListKetuaBahagian(idBahagian, mp));
			context.put("listUnit1", portalUtil.getListUnit1(idBahagian, mp));
			context.put("listUnit2", portalUtil.getListUnit2(idBahagian, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/direktori/subDirektori/start.vm";
	}
	
	@Command("getSubSubDirektori")
	public String getSubSubDirektori() {
		try {
			mp = new MyPersistence();
			String idUnit = getParam("idUnit");
			context.put("unit", portalUtil.getUnit(idUnit, mp));
			context.put("listKetuaUnit", portalUtil.getListKetuaUnit(idUnit, mp));
			context.put("listStafUnit", portalUtil.getListStafUnit(idUnit, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}
		return getPath() + "/content/direktori/subDirektori/subSubDirektori/start.vm";
	}
	
	/** START DAFTAR AKAUN BARU **/
	@Command("getRegister")
	public String getRegister() {
		dataUtil = DataUtil.getInstance(db);

		context.put("selectKategoriPengguna", dataUtil.getListKategoriPenggunaOnline());
		context.put("selectJantina", dataUtil.getListJantina());
		context.put("selectBangsa", dataUtil.getListBangsa());
		context.put("selectAgama", dataUtil.getListAgama());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectBadanBerkanun", dataUtil.getListBadanBerkanun());

		context.remove("replyMsg");
		context.remove("success");
		context.put("command",command);
		return getPath() + "/content/register/start.vm";
	}
	
	@Command("validateIC")
	public String validateIC() throws Exception {
		try {
			mp = new MyPersistence();
			
			if (getParam("regUsername").trim().length() != 12) {
				// USERS EXIST
				String replyMsg = "SILA MASUKKAN NO. MYKAD DENGAN FORMAT YANG BETUL.";
				boolean validIC = false;

				boolean flagSemakanJPN = false;
				boolean flagSemakanHRMIS = false;
				boolean flagSemakanPESARA = false;
				
				context.put("replyMsg", replyMsg);
				context.put("validIC", validIC);

				context.put("flagSemakanJPN", flagSemakanJPN);
				context.put("flagSemakanHRMIS", flagSemakanHRMIS);
				context.put("flagSemakanPESARA", flagSemakanPESARA);
			} else {
				Users pemohon = (Users) mp.find(Users.class, getParam("regUsername"));
				
				if (pemohon != null) {
					if (pemohon.getFlagDaftarSBBPH() != null) {
						if (pemohon.getFlagDaftarSBBPH().equals("Y")) {
							// USERS EXIST
							String replyMsg = "MYKAD TELAH DIDAFTARKAN. SILA GUNA FUNGSI LUPA KATALALUAN JIKA TERLUPA KATA LALUAN.";
							boolean validIC = false;

							boolean flagSemakanJPN = false;
							boolean flagSemakanHRMIS = false;
							boolean flagSemakanPESARA = false;
							
							context.put("replyMsg", replyMsg);
							context.put("validIC", validIC);

							context.put("flagSemakanJPN", flagSemakanJPN);
							context.put("flagSemakanHRMIS", flagSemakanHRMIS);
							context.put("flagSemakanPESARA", flagSemakanPESARA);
						} else {
							getValidateIC();		
						}
					} else {
						getValidateIC();		
					}										
				} else {
					getValidateIC();		
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}			
		return getPath() + "/content/register/validateIC.vm";
	}

	private void getValidateIC() throws Exception {
		String replyMsg = "SEMAKAN NO. MYKAD ANDA TIDAK BERJAYA. SILA CUBA SEKALI LAGI";
		boolean validIC = false;

		boolean flagSemakanJPN = false;
		boolean flagSemakanHRMIS = false;
		boolean flagSemakanPESARA = false;

		String idKategoriPengguna = getParam("idKategoriPengguna");
		if (idKategoriPengguna != null) {
			if (idKategoriPengguna.trim().length() > 0) {
				
				try {
					System.out.println("VALIDATE JPN USING BPH WEB SERVICE");
					org.tempuri.crsservice.JpnManager jpnManagerService = new org.tempuri.crsservice.JpnManager();
					BPHServicesImplService service = new BPHServicesImplService();
					BPHServices bphService = service.getPort(BPHServices.class);
					// EXTRA CONTEXT
					String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
					int connectionTimeOutInMs = 10000;// 10 Second
					BindingProvider p = (BindingProvider) bphService;
					p.getRequestContext().put(
							BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
							WS_URL);
					myLogger.debug("requesting service from=> :" + bphService.toString());
	
					jpnManagerService = bphService.retreiveCitizensData(
							getParam("regUsername"), getParam("regName"), "T7");
					validIC = jpnManagerService.isValidIc();
					flagSemakanJPN = jpnManagerService.isFlagSemakanJPN();
					replyMsg = jpnManagerService.getReplyMsg();
	
					if (flagSemakanJPN) {
	
						sbbph.ws.HrmisManager hrmisManagerService = null;
						sbbph.ws.PesaraManager pesaraManagerService = null;
						
						if ("01".equals(idKategoriPengguna)) {
							System.out.println("VALIDATE HRMIS USING BPH WEB SERVICE");
							hrmisManagerService = bphService
									.semakanPenjawatAwam(getParam("regUsername"));
	
							validIC = hrmisManagerService.isValidIc();
							flagSemakanHRMIS = hrmisManagerService
									.isFlagSemakanHRMIS();
							replyMsg = hrmisManagerService.getReplyMsg();
	
							System.out.println("VALIDATE PESARA USING BPH WEB SERVICE");
							pesaraManagerService = bphService
									.semakanPesara(getParam("regUsername"));
							if (pesaraManagerService.isFlagSemakanPESARA() && pesaraManagerService.isValidIc()) {
								validIC = pesaraManagerService.isValidIc();
								flagSemakanPESARA = pesaraManagerService.isFlagSemakanPESARA();
							}
							
						} else if ("02".equals(idKategoriPengguna)) {
							// SEMAKAN DENGAN PESARA
							System.out.println("VALIDATE PESARA USING BPH WEB SERVICE");
							pesaraManagerService = bphService
									.semakanPesara(getParam("regUsername"));
							validIC = pesaraManagerService.isValidIc();
							flagSemakanPESARA = pesaraManagerService
									.isFlagSemakanPESARA();
							replyMsg = pesaraManagerService.getReplyMsg();
						}
					}					
				} catch (Exception ex) {
					System.out.println("ERROR ACCESS BPH WEB SERVICE : " + ex.getMessage());
				}
			}
		}
		
		if ("SEMAKAN BERJAYA".equals(replyMsg)) {
			replyMsg = "";
		}

		context.put("replyMsg", replyMsg);
		context.put("validIC", validIC);

		context.put("flagSemakanJPN", flagSemakanJPN);
		context.put("flagSemakanHRMIS", flagSemakanHRMIS);
		context.put("flagSemakanPESARA", flagSemakanPESARA);
	}

	@Command("getDaftar")
	public String getDaftar() throws Exception {
		boolean success = false;
		String emelDidaftarkan = "";

		try {
			mp = new MyPersistence();
			
			IntJPN jpn = null;
			if (getParam("flagSemakanJPN").equals("true")) {
				jpn = (IntJPN) mp
						.get("select x FROM IntJPN  x where x.noPengenalan = '"
								+ getParam("regUsername")
								+ "' order by x.tarikhTerima desc");
			}
			IntHRMIS hrmis = null;
			if (getParam("flagSemakanHRMIS").equals("true")) {
				hrmis = (IntHRMIS) mp
						.get("select x FROM IntHRMIS x where x.noPengenalan = '"
								+ getParam("regUsername")
								+ "' order by x.tarikhTerima desc");
			}
			IntPESARA pesara = null;
			if (getParam("flagSemakanPESARA").equals("true")) {
				pesara = (IntPESARA) mp
						.get("select x FROM IntPESARA x where x.noPengenalan = '"
								+ getParam("regUsername")
								+ "' order by x.tarikhTerima desc");
			}

			String idKategoriPengguna = getParam("idKategoriPengguna");
			KategoriPengguna kategoriPengguna = (KategoriPengguna) mp.find(KategoriPengguna.class, idKategoriPengguna);
			String username = getParam("regUsername");
			String name = getParam("regName");
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
			String email = getParam("regEmail");
			BadanBerkanun badanBerkanun = null;
			if (!"".equals(getParam("idBadanBerkanun"))) {
				badanBerkanun = (BadanBerkanun) mp.find(BadanBerkanun.class, getParam("idBadanBerkanun"));
			}
			String dokumenPengesahan = getParam("dokumenPengesahan");
			
			Users user = (Users) mp.find(Users.class, username);				
			
			mp.begin();
			// DAFTAR AKAUN BARU
			if (user == null) {
				user = new Users();

				user.setId(username);
				Random r = new Random();
				String c = "";
				String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
				for (int i = 0; i < 7; i++) {
					c = c + alphabet.charAt(r.nextInt(alphabet.length()));
				}
				user.setUserPassword(PasswordService.encrypt(c));
				if (hrmis != null) {
					if (hrmis.getKodGelaran() != null) {
						user.setGelaran((Gelaran) mp.find(Gelaran.class, hrmis.getKodGelaran()));
					}					
				}
				user.setUserName(name.toUpperCase());
				
				// JENIS PENGGUNA
				user.setJenisPengguna(kategoriPengguna);
				if ("01".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Penjawat Awam"));
					user.setFlagMenungguPengesahan("T");
					if (getParam("flagSemakanHRMIS").equals("true")) {
						user.setFlagAktif("Y");
					} else {
						user.setFlagAktif("T");
					}
				} else if ("02".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pesara"));
					user.setFlagMenungguPengesahan("T");
					if (getParam("flagSemakanPESARA").equals("true")) {
						user.setFlagAktif("Y");
					} else {
						user.setFlagAktif("T");
					}
				} else if ("03".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Badan Berkanun"));
					user.setFlagAktif("T");
					user.setFlagMenungguPengesahan("Y");
					user.setFlagUrusanPemohon(3); // URUSAN KUARTERS || URUSAN RPP
				} else if ("06".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pengguna Awam"));
					user.setFlagAktif("Y");
					user.setFlagMenungguPengesahan("T");
				} else if ("04".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Polis / Tentera"));
					user.setFlagAktif("T");
					user.setFlagMenungguPengesahan("Y");
					user.setFlagUrusanPemohon(3); // URUSAN KUARTERS || URUSAN RPP
				} else if ("05".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pesara Polis / Tentera"));
					user.setFlagAktif("T");
					user.setFlagMenungguPengesahan("Y");
					user.setFlagUrusanPemohon(2); // URUSAN RPP
				}
				
				user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
				user.setUserLoginAlt("-");
				
				user.setUserAddress(userAddress);
				user.setUserAddress2(userAddress2);
				user.setUserAddress3(userAddress3);
				user.setUserPostcode(poskod);
				user.setUserBandar(bandar);
				user.setDateRegistered(new Date());
				
				user.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
				user.setNoKP(username);
				if (jpn != null) {
					user.setNoKP2(jpn.getNoPengenalanLama());
				}				
				user.setNoTelefon(noTel);
				user.setNoTelefonBimbit(noTelBimbit);
				if (hrmis != null) {
					user.setNoTelefonPejabat(hrmis.getNoTelefonPejabat());
				}
				user.setEmel(email);
				emelDidaftarkan = email;
				
				//MAKLUMAT PERJAWATAN DARI HRMIS
				if (hrmis != null) {					
					if (hrmis.getKodKelasPerkhidmatan() != null) {
						user.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, hrmis.getKodKelasPerkhidmatan()));
					}
					if (hrmis.getKodGredPerkhidmatan() != null) {
						user.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, hrmis.getKodGredPerkhidmatan()));
					}
					if (hrmis.getKodAgensi() != null) {
						user.setAgensi((Agensi) mp.find(Agensi.class, hrmis.getKodAgensi()));
					}
					user.setBahagian(hrmis.getJabatan());
				}
				//MAKLUMAT PERJAWATAN DARI PESARA
				if (pesara != null) {
					if (pesara.getKodJawatan() != null) {
						Jawatan jawatan = (Jawatan) mp.find(Jawatan.class, pesara.getKodJawatan());
						user.setJawatan(jawatan);
						user.setKeteranganJawatan(jawatan.getKeterangan());
					} else {
						user.setKeteranganJawatan(pesara.getJawatanTerakhir());
					}
					if (pesara.getKelasPerkhidmatan() != null) {
						user.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, pesara.getKelasPerkhidmatan()));
					}
					if (pesara.getGredPerkhidmatan() != null) {
						user.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, pesara.getGredPerkhidmatan()));
					}
					//TODO UPDATE AGENSI BASED ON REKOD FROM PESARA
					user.setAgensi(null);
					user.setBahagian(pesara.getCawangan());
				}
				
				//TODO REVISE CODE BASED ON RUJ_JENIS_PERKHIDMATAN
				if ("03".equals(idKategoriPengguna)) {
					user.setJenisPerkhidmatan((JenisPerkhidmatan) mp.find(JenisPerkhidmatan.class,"03"));
				} else if ("01".equals(idKategoriPengguna) || "02".equals(idKategoriPengguna)) {
					user.setJenisPerkhidmatan((JenisPerkhidmatan) mp.find(JenisPerkhidmatan.class,"01"));
				}
				
				user.setJantina(jantina);
				user.setBangsa(bangsa);
				if (hrmis != null) {
					if (hrmis.getKodEtnik() != null) {
						user.setEtnik((Etnik) mp.find(Etnik.class, hrmis.getKodEtnik()));
					}					
				}				
				user.setAgama(agama);
				
				if (jpn != null) {
					user.setTarikhLahir(jpn.getTarikhLahir());
				}				
				if (hrmis != null) {
					if (hrmis.getKodStatusPerkahwinan() != null) {
						user.setStatusPerkahwinan((StatusPerkahwinan) mp.find(StatusPerkahwinan.class, hrmis.getKodStatusPerkahwinan()));
					}
				}
				
				user.setAlamat1(userAddress);
				user.setAlamat2(userAddress2);
				user.setAlamat3(userAddress3);
				user.setPoskod(poskod);
				user.setBandar(bandar);
				
				// FLAG SEMAKAN
				user.setFlagSemakanJPN("Y");
				if (getParam("flagSemakanHRMIS").equals("true")) {
					user.setFlagSemakanHRMIS("Y");
				} else {
					user.setFlagSemakanHRMIS("T");
				}
				if (getParam("flagSemakanPESARA").equals("true")) {
					user.setFlagSemakanPESARA("Y");
				} else {
					user.setFlagSemakanPESARA("T");
				}
				//DOKUMEN PENGESAHAN
				if (dokumenPengesahan != null && dokumenPengesahan.trim().length() > 0)
					user.setDokumenSokongan(dokumenPengesahan);	
				user.setBadanBerkanun(badanBerkanun);
				user.setFlagSahMaklumatBank("T");
				user.setFlagDaftarSBBPH("Y");
				user.setFlagGelanggang("T");
				mp.persist(user);
				
				// USER JOB
				boolean addJob = false;
				UsersJob job = (UsersJob) mp.get("select x from UsersJob x where x.users.id = '" + user.getId() + "'");
				if (job == null) {
					job = new UsersJob();
					job.setUsers(user);
					addJob = true;
				}
				job.setJawatan(user.getJawatan());
				job.setGredJawatan(user.getGredPerkhidmatan());
				job.setJenisPerkhidmatan(user.getJenisPerkhidmatan());
				job.setKelasPerkhidmatan(user.getKelasPerkhidmatan());
				job.setAgensi(user.getAgensi());
				job.setBahagian(user.getBahagian());
				job.setBadanBerkanun(user.getBadanBerkanun());
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
				
				if (pesara != null) {
					job.setTarikhLantikan(null);
					job.setTarikhTamat(null);
					job.setAlamat1(pesara.getAlamat1());
					job.setAlamat2(pesara.getAlamat2());
					job.setAlamat3(pesara.getAlamat3());
					job.setPoskod(pesara.getPoskod());
					if (pesara.getKodBandar() != null)
						job.setBandar((Bandar) mp.find(Bandar.class, pesara.getKodBandar()));
					job.setNoTelPejabat(null);	
					job.setEmel(null);
					job.setTarikhBersara(pesara.getTarikhPencen());
				}
				if (addJob)
					mp.persist(job);

				// USER SPOUSE
				boolean addSpouse = false;
				UsersSpouse spouse = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + user.getId() + "'");
				if (spouse == null) {
					spouse = new UsersSpouse();
					spouse.setUsers(user);
					addSpouse = true;
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

				// PENGGUNA AWAM SUCCESS
				if ("06".equals(idKategoriPengguna)) {
					DaftarAkaunBaruMailer.get().hantarPassword(email, username, c);
				}

				// PENJAWAT AWAM SUCCESS
				if ("01".equals(idKategoriPengguna)) {
					if (getParam("flagSemakanHRMIS").equals("true")) {
						DaftarAkaunBaruMailer.get().hantarPassword(email, username,
								c);
					} else {
						DaftarAkaunBaruMailer.get().daftarBaru(email);
					}
				}

				// PESARA AWAM SUCCESS
				if ("02".equals(idKategoriPengguna)) {
					if (getParam("flagSemakanPESARA").equals("true")) {
						DaftarAkaunBaruMailer.get().hantarPassword(email, username,
								c);
					} else {
						DaftarAkaunBaruMailer.get().daftarBaru(email);
					}
				}

				// BADAN BERKANUN || POLIS / TENTERA || PESARA POLIS / TENTERA SUCCESS
				if ("03".equals(idKategoriPengguna) || "04".equals(idKategoriPengguna) || "05".equals(idKategoriPengguna)) {
					DaftarAkaunBaruMailer.get().hantarPasswordBadanBerkanun(email, username, c);
				}
				success = true;
			
			} else { // REGISTERED USER
				
				Random r = new Random();
				String c = "";
				String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
				for (int i = 0; i < 7; i++) {
					c = c + alphabet.charAt(r.nextInt(alphabet.length()));
				}
				user.setUserPassword(PasswordService.encrypt(c));
				if (hrmis != null) {
					if (hrmis.getKodGelaran() != null) {
						user.setGelaran((Gelaran) mp.find(Gelaran.class, hrmis.getKodGelaran()));
					}
				}
				user.setUserName(name.toUpperCase());
				
				// JENIS PENGGUNA
				user.setJenisPengguna(kategoriPengguna);
				if ("01".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Penjawat Awam"));
					user.setFlagMenungguPengesahan("T");
					if (getParam("flagSemakanHRMIS").equals("true")) {
						user.setFlagAktif("Y");
					} else {
						user.setFlagAktif("T");
					}
				} else if ("02".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pesara"));
					user.setFlagMenungguPengesahan("T");
					if (getParam("flagSemakanPESARA").equals("true")) {
						user.setFlagAktif("Y");
					} else {
						user.setFlagAktif("T");
					}
				} else if ("03".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Badan Berkanun"));
					user.setFlagAktif("T");
					user.setFlagMenungguPengesahan("Y");
					user.setFlagUrusanPemohon(3); // URUSAN KUARTERS || URUSAN RPP
				} else if ("06".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pengguna Awam"));
					user.setFlagMenungguPengesahan("T");
				} else if ("04".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Polis / Tentera"));
					user.setFlagAktif("T");
					user.setFlagMenungguPengesahan("Y");
					user.setFlagUrusanPemohon(3); // URUSAN KUARTERS || URUSAN RPP
				} else if ("05".equals(idKategoriPengguna)) {
					user.setRole((Role) mp.find(Role.class, "(AWAM) Pesara Polis / Tentera"));
					user.setFlagAktif("T");
					user.setFlagMenungguPengesahan("Y");
					user.setFlagUrusanPemohon(2); // URUSAN RPP
				}
				
				user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
				user.setUserLoginAlt("-");
				
				user.setUserAddress(userAddress);
				user.setUserAddress2(userAddress2);
				user.setUserAddress3(userAddress3);
				user.setUserPostcode(poskod);
				user.setUserBandar(bandar);
				user.setDateRegistered(new Date());
				
				user.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
				user.setNoKP(username);
				if (jpn != null) {
					user.setNoKP2(jpn.getNoPengenalanLama());
				}				
				user.setNoTelefon(noTel);
				user.setNoTelefonBimbit(noTelBimbit);
				if (hrmis != null) {
					user.setNoTelefonPejabat(hrmis.getNoTelefonPejabat());
				}
				user.setEmel(email);
				emelDidaftarkan = email;
				
				//MAKLUMAT PERJAWATAN DARI HRMIS
				if (hrmis != null) {					
					if (hrmis.getKodKelasPerkhidmatan() != null) {
						user.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, hrmis.getKodKelasPerkhidmatan()));
					}
					if (hrmis.getKodGredPerkhidmatan() != null) {
						user.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, hrmis.getKodGredPerkhidmatan()));
					}
					if (hrmis.getKodAgensi() != null) {
						user.setAgensi((Agensi) mp.find(Agensi.class, hrmis.getKodAgensi()));
					}
					user.setBahagian(hrmis.getJabatan());
				}
				//MAKLUMAT PERJAWATAN DARI PESARA
				if (pesara != null) {
					if (pesara.getKodJawatan() != null) {
						Jawatan jawatan = (Jawatan) mp.find(Jawatan.class, pesara.getKodJawatan());
						user.setJawatan(jawatan);
						user.setKeteranganJawatan(jawatan.getKeterangan());
					} else {
						user.setKeteranganJawatan(pesara.getJawatanTerakhir());
					}
					if (pesara.getKelasPerkhidmatan() != null) {
						user.setKelasPerkhidmatan((KelasPerkhidmatan) mp.find(KelasPerkhidmatan.class, pesara.getKelasPerkhidmatan()));
					}
					if (pesara.getGredPerkhidmatan() != null) {
						user.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, pesara.getGredPerkhidmatan()));
					}
					//TODO UPDATE AGENSI BASED ON REKOD FROM PESARA
					user.setAgensi(null);
					user.setBahagian(pesara.getCawangan());
				}
				
				//TODO REVISE CODE BASE ON JENIS PERKHIDMATAN
				if ("03".equals(idKategoriPengguna)) {
					user.setJenisPerkhidmatan((JenisPerkhidmatan) mp.find(JenisPerkhidmatan.class,"03"));
				} else if ("01".equals(idKategoriPengguna) || "02".equals(idKategoriPengguna)) {
					user.setJenisPerkhidmatan((JenisPerkhidmatan) mp.find(JenisPerkhidmatan.class,"01"));
				}
				
				user.setJantina(jantina);
				user.setBangsa(bangsa);
				if (hrmis != null) {
					if (hrmis.getKodEtnik() != null) {
						user.setEtnik((Etnik) mp.find(Etnik.class, hrmis.getKodEtnik()));
					}
				}				
				user.setAgama(agama);
				
				if (jpn != null) {
					user.setTarikhLahir(jpn.getTarikhLahir());
				}				
				if (hrmis != null) {
					if (hrmis.getKodStatusPerkahwinan() != null) {
						user.setStatusPerkahwinan((StatusPerkahwinan) mp.find(StatusPerkahwinan.class, hrmis.getKodStatusPerkahwinan()));
					}
				}
				
				user.setAlamat1(userAddress);
				user.setAlamat2(userAddress2);
				user.setAlamat3(userAddress3);
				user.setPoskod(poskod);
				user.setBandar(bandar);
				
				// FLAG SEMAKAN
				user.setFlagSemakanJPN("Y");
				if (getParam("flagSemakanHRMIS").equals("true")) {
					user.setFlagSemakanHRMIS("Y");
				} else {
					user.setFlagSemakanHRMIS("T");
				}
				if (getParam("flagSemakanPESARA").equals("true")) {
					user.setFlagSemakanPESARA("Y");
				} else {
					user.setFlagSemakanPESARA("T");
				}
				//DOKUMEN PENGESAHAN
				if (dokumenPengesahan != null && dokumenPengesahan.trim().length() > 0)
					user.setDokumenSokongan(dokumenPengesahan);	
				user.setBadanBerkanun(badanBerkanun);
				user.setFlagSahMaklumatBank("T");
				user.setFlagDaftarSBBPH("Y");
				user.setFlagGelanggang("T");
				mp.persist(user);
				
				// USER JOB
				boolean addJob = false;
				UsersJob job = (UsersJob) mp.get("select x from UsersJob x where x.users.id = '" + user.getId() + "'");
				if (job == null) {
					job = new UsersJob();
					job.setUsers(user);
					addJob = true;
				}
				job.setJawatan(user.getJawatan());
				job.setGredJawatan(user.getGredPerkhidmatan());
				job.setJenisPerkhidmatan(user.getJenisPerkhidmatan());
				job.setKelasPerkhidmatan(user.getKelasPerkhidmatan());
				job.setAgensi(user.getAgensi());
				job.setBahagian(user.getBahagian());
				job.setBadanBerkanun(user.getBadanBerkanun());
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
				
				if (pesara != null) {
					job.setTarikhLantikan(null);
					job.setTarikhTamat(null);
					job.setAlamat1(pesara.getAlamat1());
					job.setAlamat2(pesara.getAlamat2());
					job.setAlamat3(pesara.getAlamat3());
					job.setPoskod(pesara.getPoskod());
					if (pesara.getKodBandar() != null)
						job.setBandar((Bandar) mp.find(Bandar.class, pesara.getKodBandar()));
					job.setNoTelPejabat(null);	
					job.setEmel(null);
					job.setTarikhBersara(pesara.getTarikhPencen());
				}
				if (addJob)
					mp.persist(job);

				// USER SPOUSE
				boolean addSpouse = false;
				UsersSpouse spouse = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + user.getId() + "'");
				if (spouse == null) {
					spouse = new UsersSpouse();
					spouse.setUsers(user);
					addSpouse = true;
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

				// PENGGUNA AWAM SUCCESS
				if ("06".equals(idKategoriPengguna)) {
					DaftarAkaunBaruMailer.get().hantarPassword(email, username, c);
				}

				// PENJAWAT AWAM SUCCESS
				if ("01".equals(idKategoriPengguna)) {
					if (getParam("flagSemakanHRMIS").equals("true")) {
						DaftarAkaunBaruMailer.get().hantarPassword(email, username,
								c);
					} else {
						DaftarAkaunBaruMailer.get().daftarBaru(email);
					}
				}

				// PESARA AWAM SUCCESS
				if ("02".equals(idKategoriPengguna)) {
					if (getParam("flagSemakanPESARA").equals("true")) {
						DaftarAkaunBaruMailer.get().hantarPassword(email, username,
								c);
					} else {
						DaftarAkaunBaruMailer.get().daftarBaru(email);
					}
				}

				// BADAN BERKANUN || POLIS / TENTERA || PESARA POLIS / TENTERA SUCCESS
				if ("03".equals(idKategoriPengguna) || "04".equals(idKategoriPengguna) || "05".equals(idKategoriPengguna)) {
					DaftarAkaunBaruMailer.get().hantarPasswordBadanBerkanun(email, username, c);
				}
				success = true;
			}
		} catch (Exception ex) {
			System.out.println("GAGAL MENDAFTAR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}
		
		context.put("success", success);
		context.put("command", command);
		context.put("emelDidaftarkan", emelDidaftarkan);
		return getPath() + "/content/register/start.vm";
	}

	@SuppressWarnings("rawtypes")
	@Command("uploadDokumenPengesahan")
	public String uploadDokumenPengesahan() throws Exception {
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
			String imgName = uploadDir + getParam("noPengenalan") + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(getParam("dokumenPengesahan"));

			item.write(new File(ResourceBundle.getBundle("dbconnection")
					.getString("folder") + imgName));
			context.put("imgName", imgName);
		}

		return getPath() + "/content/register/uploaddoc.vm";
	}

	@Command("refreshUploadDokumenPengesahan")
	public String refreshUploadDokumenPengesahan() throws Exception {
		String imgName = getParam("imgName");

		context.put("imgName", imgName);

		return getPath() + "/content/register/dokumenPengesahan.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);

		String idNegeri = "0";

		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");

		context.put("selectBandar", dataUtil.getListBandar(idNegeri));

		return getPath() + "/content/register/selectBandar.vm";
	}
	/** END DAFTAR AKAUN BARU **/
	
	/*********************************************** START ADUAN ***********************************************/
	@Command("getAduan")
	public String getAduan() {
		dataUtil = DataUtil.getInstance(db);

		context.put("selectJenisAduan", dataUtil.getListJenisAduan());
		context.put("selectNegeriAduan", dataUtil.getListNegeri());
		context.put("path", getPath());
		
		String idAduan = UID.getUID();
		context.put("idAduan", idAduan);

		context.remove("success");
		return getPath() + "/content/aduan/start.vm";
	}	

	@Command("hantarAduan")
	public String hantarAduan() {
		boolean success = false;
		mp = new MyPersistence();
		
		try {
			mp.begin();
			String noAduan = generateNoAduan(mp);

			ProAduan aduan = new ProAduan();
			if (!"".equals(getParam("idAduan")))
				aduan.setId(getParam("idAduan")); //GENERATED BEFORE - SYNC WITH UPLOAD DOC NAME
			aduan.setNoAduan(noAduan);
			aduan.setSumberAduan((SumberAduan) mp.find(SumberAduan.class, "01"));
			aduan.setNama(getParam("nama"));
			aduan.setNoPengenalan(getParam("noPengenalan"));
			aduan.setAlamat1(getParam("alamat1"));
			aduan.setAlamat2(getParam("alamat2"));
			aduan.setAlamat3(getParam("alamat3"));
			aduan.setPoskod(getParam("poskod"));
			aduan.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandarAduan")));
			aduan.setNoTelefon(getParam("noTelefon"));
			aduan.setEmel(getParam("emel"));
			aduan.setJenisAduan((JenisAduan) mp.find(JenisAduan.class, getParam("idJenisAduan")));
			aduan.setTarikhAduan(new Date());
			aduan.setTajuk(getParam("tajuk"));
			aduan.setButiran(getParam("butiran"));
			aduan.setStatus((Status) mp.find(Status.class, "1434787994722")); // BARU
			aduan.setFileName(getParam("lampiranAduan"));
			aduan.setSequence(getMaxTurutanAduan(mp));
			aduan.setTarikhMasuk(new Date());
				
			mp.persist(aduan);			
			mp.commit();
			
			success = true;
			context.put("r", aduan);
			context.put("success", success);
			
			if (aduan.getEmel() != null) {
				AduanMailer.get().daftarAduanBaru(aduan);
			}
		} catch (Exception ex) {
			context.put("success", success);
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}
		return getPath() + "/content/aduan/result.vm";
	}

	private synchronized String generateNoAduan(MyPersistence mp) {
		Calendar calCurrent = new GregorianCalendar();
		calCurrent.setTime(new Date());
		int bulan = calCurrent.get(Calendar.MONTH) + 1;
		int tahun = calCurrent.get(Calendar.YEAR);
		int counter = 0;
		String id = String.valueOf(tahun) + new DecimalFormat("00").format(bulan);
		
		ProSequenceAduan seq = (ProSequenceAduan) mp.get("select x from ProSequenceAduan x where x.bulan = '"+ bulan + "' and x.tahun = '" + tahun + "'");

		if(seq != null){
			mp.pesismisticLock(seq);
			counter = seq.getBilangan() + 1;
			seq.setBilangan(counter);
			seq = (ProSequenceAduan) mp.merge(seq);
		} else {
			counter = 1;
			seq = new ProSequenceAduan();
			seq.setId(id);
			seq.setBulan(bulan);
			seq.setTahun(tahun);
			seq.setBilangan(counter);
			mp.persist(seq);
			mp.flush();
		}
		
		String formatserial = new DecimalFormat("000").format(counter);
		String noAduan = "BPH/AD/" + tahun + "/"
				+ new DecimalFormat("00").format(bulan) + "/" + formatserial;
		return noAduan;
	}
	
	private int getMaxTurutanAduan(MyPersistence mp) {
		int seq = 0;
		try {
			seq =  mp.getRecordMax("SELECT p.sequence FROM ProAduan p ORDER BY p.sequence DESC",1);
		} catch (Exception x) {
			seq = 1;
		}
		return seq;
	}

	@Command("selectBandarAduan")
	public String selectBandarAduan() throws Exception {
		dataUtil = DataUtil.getInstance(db);
		String idNegeriAduan = "0";

		if (getParam("idNegeriAduan").trim().length() > 0)
			idNegeriAduan = getParam("idNegeriAduan");
		context.put("selectBandarAduan", dataUtil.getListBandar(idNegeriAduan));
		return getPath() + "/content/aduan/selectBandarAduan.vm";
	}

	@SuppressWarnings("rawtypes")
	@Command("uploadLampiranAduan")
	public String uploadLampiranAduan() throws Exception {
		String idAduan = getParam("idAduan");
		String uploadDir = "pro/aduan/lampiran/";
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
			
			String imgName = uploadDir + idAduan + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(getParam("lampiranAduan"));
			
			item.write(new File(ResourceBundle.getBundle("dbconnection")
					.getString("folder") + imgName));
			context.put("imgName", imgName);
			context.put("idAduan", idAduan);
		}

		return getPath() + "/content/aduan/uploaddoc.vm";
	}

	@Command("refreshList")
	public String refreshList() throws Exception {
		String imgName = getParam("imgName");
		context.put("imgName", imgName);
		return getPath() + "/content/aduan/refreshLampiranAduan.vm";
	}
	
	@Command("getSemakanAduan")
	public String getSemakanAduan() {
		context.put("path", getPath());
		context.put("command", command);
		return getPath() + "/content/aduan/semakanAduan/start.vm";
	}

	@Command("hantarSemakAduan")
	public String hantarSemakAduan() {
		Util util = new Util();
		mp = new MyPersistence();
		ProAduan aduan = null;
		try {
			aduan = (ProAduan) mp.get("SELECT x FROM ProAduan x WHERE x.noPengenalan = '"
					+ getParam("noKadPengenalan") + "' and x.noAduan = '" + getParam("noAduan") + "' ");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}
		
		context.put("aduan", aduan);
		context.put("util", util);
		context.put("command", command);
		return getPath() + "/content/aduan/semakanAduan/result.vm";
	}
	/** END ADUAN **/
	
	@Command("getInformasi")
	public String getInformasi() {
		try {
			mp = new MyPersistence();
			String idInformasi = getParam("paramId");
			context.put("informasi", portalUtil.getInformasi(idInformasi, mp));
			context.put("listSubInformasi", portalUtil.getListSubInformasi(idInformasi, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}	
		return getPath() + "/content/informasi/start.vm";
	}
	
	@Command("getPenyelenggaraanKuarters") //edited by ain nadia
	public String getPenyelenggaraanKuarters() {
		try {
			mp = new MyPersistence();
			context.put("listkuarters", portalUtil.getListPenyelenggaraanKuarters(mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/penyelenggaraanKuarters/start.vm";
	}
	
	
	@Command("getGaleriGambar")
	public String getGaleriGambar() {
		try {
			mp = new MyPersistence();
			context.put("listGaleriGambar", portalUtil.getListGaleriGambar(mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/galeri/gambar/start.vm";
	}
	
	@Command("getGaleriVideo")
	public String getGaleriVideo() {
		try {
			mp = new MyPersistence();
			context.put("listGaleriVideo", portalUtil.getListGaleriVideo(mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/galeri/video/start.vm";
	}

	/** START MAKLUMBALAS **/
	@Command("getMaklumbalas")
	public synchronized String getMaklumbalas() throws Exception {

		Calendar calCurrent = new GregorianCalendar();
		calCurrent.setTime(new Date());
		int bulan = calCurrent.get(Calendar.MONTH) + 1;
		int tahun = calCurrent.get(Calendar.YEAR);
		int undian = getParamAsInteger("undian");

		int tidakPasti = 0;
		int tidakPuas = 0;
		int kurangPuas = 0;
		int puas = 0;
		int sangatPuas = 0;		
		
		String id = String.valueOf(tahun) + new DecimalFormat("00").format(bulan);
		if (undian != 0) {
			try {
				mp = new MyPersistence();
				WebUndian seq = (WebUndian) mp.get("select x from WebUndian x where x.bulan = '" + bulan + "' and x.tahun = '" + tahun + "'" );
				
				mp.begin();
				if (seq != null) {
					mp.pesismisticLock(seq);
					if (undian == 1) {
						tidakPasti = seq.getTidakPasti() + 1;
						tidakPuas = seq.getTidakPuas();
						kurangPuas = seq.getKurangPuas();
						puas = seq.getPuas();
						sangatPuas = seq.getSangatPuas();
					} else if (undian == 2) {
						tidakPasti = seq.getTidakPasti();
						tidakPuas = seq.getTidakPuas() + 1;
						kurangPuas = seq.getKurangPuas();
						puas = seq.getPuas();
						sangatPuas = seq.getSangatPuas();
					} else if (undian == 3) {
						tidakPasti = seq.getTidakPasti();
						tidakPuas = seq.getTidakPuas();
						kurangPuas = seq.getKurangPuas() + 1;
						puas = seq.getPuas();
						sangatPuas = seq.getSangatPuas();
					} else if (undian == 4) {
						tidakPasti = seq.getTidakPasti();
						tidakPuas = seq.getTidakPuas();
						kurangPuas = seq.getKurangPuas();
						puas = seq.getPuas() + 1;
						sangatPuas = seq.getSangatPuas();
					} else if (undian == 5) {
						tidakPasti = seq.getTidakPasti();
						tidakPuas = seq.getTidakPuas();
						kurangPuas = seq.getKurangPuas();
						puas = seq.getPuas();
						sangatPuas = seq.getSangatPuas() + 1;
					}

					seq.setTidakPasti(tidakPasti);
					seq.setTidakPuas(tidakPuas);
					seq.setKurangPuas(kurangPuas);
					seq.setPuas(puas);
					seq.setSangatPuas(sangatPuas);
					seq.setTarikhKemaskini(new Date());
					seq = (WebUndian) mp.merge(seq);
				} else {
					if (undian == 1) {
						tidakPasti = 1;
					} else if (undian == 2) {					
						tidakPuas = 1;					
					} else if (undian == 3) {
						kurangPuas = 1;	
					} else if (undian == 4) {
						puas = 1;
					} else if (undian == 5) {
						sangatPuas = 1;
					}
					
					seq = new WebUndian();
					seq.setId(id);
					seq.setBulan(bulan);
					seq.setTahun(tahun);
					seq.setTidakPasti(tidakPasti);
					seq.setTidakPuas(tidakPuas);
					seq.setKurangPuas(kurangPuas);
					seq.setPuas(puas);
					seq.setSangatPuas(sangatPuas);
					seq.setTarikhKemaskini(new Date());
					mp.persist(seq);
					mp.flush();
				}
				mp.commit();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (mp != null) { if (mp != null) { mp.close(); } }
			}
		}		
		context.put("status", "sukses");
		return getPath() + "/content/maklumbalas/status.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("getKeputusanMaklumbalas")
	public String getKeputusanMaklumbalas() throws Exception {
		Util util = new Util();
		List<WebUndian> undi = null;
		try {
			mp = new MyPersistence();
			
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(new Date());
			int tahun = cal.get(Calendar.YEAR);
			
			undi = mp.list("SELECT u FROM WebUndian u where u.tahun = '" + tahun + "' order by u.id asc");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		context.put("undi", undi);
		context.put("util", util);
		return getPath() + "/content/maklumbalas/start.vm";
	}
	/** END MAKLUMBALAS **/
	
	/** START KEPUASAN PELANGGAN **/
	@Command("getKepuasanPelanggan")
	public String getKepuasanPelanggan() throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			WebKepuasanPelanggan k = new WebKepuasanPelanggan();
			k.setTahapKepuasan(getParamAsInteger("kepuasan"));
			k.setTarikhUndi(new Date());
			mp.persist(k);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}	

		context.put("status", "sukses");
		return getPath() + "/content/kepuasanPelanggan/status.vm";
	}

	@Command("getKeputusanKepuasanPelanggan")
	public String getKeputusanKepuasanPelanggan() throws Exception {
		Util util = new Util();
		Long countTm = (long) 0;
		Long countM = (long) 0;
		Long countC = (long) 0;

		try {
			mp = new MyPersistence();
			countTm = (Long) mp.get("Select count(x.id) from WebKepuasanPelanggan x where x.tahapKepuasan = '1'"); 
			countM = (Long) db.get("Select count(x.id) from WebKepuasanPelanggan x where x.tahapKepuasan = '2'");
			countC = (Long) db.get("Select count(x.id) from WebKepuasanPelanggan x where x.tahapKepuasan = '3'"); 
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}
		context.put("countTm", countTm);
		context.put("countM", countM);
		context.put("countC", countC);
		context.put("util", util);
		return getPath() + "/content/kepuasanPelanggan/start.vm";
	}
	/** END KEPUASAN PELANGGAN **/
	
	@Command("getKeharta")
	public String getKeharta() {
		
		try {
			mp = new MyPersistence();
			String idKeharta = "";
			context.put("listKeharta", portalUtil.getListKeharta(mp));
			if (portalUtil.getListKeharta(mp).size() > 0) {
				idKeharta = portalUtil.getListKeharta(mp).get(0).getId();
				context.put("keharta", portalUtil.getListKeharta(mp).get(0));
			} else {
				context.remove("keharta");
			}
			context.put("listAktivitiKeharta", portalUtil.getListAktivitiKeharta(idKeharta, mp));
			context.put("selectedTab", idKeharta);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/keharta/start.vm";
	}
	
	@Command("getTabKeharta")
	public String getTabKeharta() {
		try {
			mp = new MyPersistence();
			String idKeharta = getParam("idKeharta");
			context.put("listKeharta", portalUtil.getListKeharta(mp));
			context.put("keharta", portalUtil.getKeharta(idKeharta, mp));
			context.put("listAktivitiKeharta", portalUtil.getListAktivitiKeharta(idKeharta, mp));
			context.put("selectedTab", idKeharta);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/keharta/start.vm";
	}
	
	@Command("getAktivitiKeharta")
	public String getAktivitiKeharta() {
		try {
			mp = new MyPersistence();
			String idAktiviti = getParam("idAktiviti");
			context.put("aktiviti", portalUtil.getAktivitiKeharta(idAktiviti, mp));
			context.put("listGambarAktivitiKeharta", portalUtil.getListGambarAktivitiKeharta(idAktiviti, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		return getPath() + "/content/keharta/listGambarAktiviti.vm";
	}

	@Command("getPuspanita")
	public String getPuspanita() {
		try {
			mp = new MyPersistence();
			String idPuspanita = "";
			context.put("listPuspanita", portalUtil.getListPuspanita(mp));
			if (portalUtil.getListPuspanita(mp).size() > 0) {
				idPuspanita = portalUtil.getListPuspanita(mp).get(0).getId();
				context.put("puspanita", portalUtil.getListPuspanita(mp).get(0));
			} else {
				context.remove("puspanita");
			}
			context.put("listAktivitiPuspanita", portalUtil.getListAktivitiPuspanita(idPuspanita, mp));
			context.put("selectedTab", idPuspanita);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/puspanita/start.vm";
	}
	
	@Command("getTabPuspanita")
	public String getTabPuspanita() {
		try {
			mp = new MyPersistence();
			String idPuspanita = getParam("idPuspanita");
			context.put("listPuspanita", portalUtil.getListPuspanita(mp));
			context.put("puspanita", portalUtil.getPuspanita(idPuspanita, mp));
			context.put("listAktivitiPuspanita", portalUtil.getListAktivitiPuspanita(idPuspanita, mp));
			context.put("selectedTab", idPuspanita);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/puspanita/start.vm";
	}
	
	@Command("getAktivitiPuspanita")
	public String getAktivitiPuspanita() {
		try {
			mp = new MyPersistence();
			String idAktiviti = getParam("idAktiviti");
			context.put("aktiviti", portalUtil.getAktivitiPuspanita(idAktiviti, mp));
			context.put("listGambarAktivitiPuspanita", portalUtil.getListGambarAktivitiPuspanita(idAktiviti, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		return getPath() + "/content/puspanita/listGambarAktiviti.vm";
	}

	//---------- START FUNCTION AKRAB ADDBY zulfazdliabuas@gmail.com Date 14/6/2017 ---------- 
	@Command("getAkrab")
	public String getAkrab() {
		try {
			mp = new MyPersistence();
			String idAkrab = "";
			context.put("listAkrab", portalUtil.getListAkrab(mp));
			if (portalUtil.getListAkrab(mp).size() > 0) {
				idAkrab = portalUtil.getListAkrab(mp).get(0).getId();
				context.put("akrab", portalUtil.getListAkrab(mp).get(0));
			} else {
				context.remove("akrab");
			}
			context.put("listAktivitiAkrab", portalUtil.getListAktivitiAkrab(idAkrab, mp));
			context.put("selectedTab", idAkrab);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/akrab/start.vm";
	}
	
	@Command("getTabAkrab")
	public String getTabAkrab() {
		try {
			mp = new MyPersistence();
			String idAkrab = getParam("idAkrab");
			context.put("listAkrab", portalUtil.getListAkrab(mp));
			context.put("akrab", portalUtil.getAkrab(idAkrab, mp));
			context.put("listAktivitiAkrab", portalUtil.getListAktivitiAkrab(idAkrab, mp));
			context.put("selectedTab", idAkrab);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/content/akrab/start.vm";
	}
	
	@Command("getAktivitiAkrab")
	public String getAktivitiAkrab() {
		try {
			mp = new MyPersistence();
			String idAktiviti = getParam("idAktiviti");
			context.put("aktiviti", portalUtil.getAktivitiAkrab(idAktiviti, mp));
			context.put("listGambarAkrab", portalUtil.getListGambarAkrab(idAktiviti, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		return getPath() + "/content/akrab/listGambarAkrab.vm";
	}
	//---------- END FUNCTION AKRAB ADDBY zulfazdliabuas@gmail.com Date 14/6/2017 ---------- 
	/********************************************** END CONTENT PORTAL *********************************************/

	
	@Command("getPerkhidmatanKuarters")
	public String getPerkhidmatanKuarters() {
		context.put("path", getPath());
		return getPath() + "/kuarters/start.vm";
	}
	
	@Command("getPermohonanLogin")
	public String getPermohonanLogin() {
		return "/vtl/main/portalLogin.vm";
	}
	
	/************************************ START SEMAKAN NO GILIRAN KUARTERS *********************************/
	@Command("getSemakSenaraiMenunggu")
	public String getSemakSenaraiMenunggu() {
		//context.put("rekod", false);
		//return getPath() + "/kuarters/semakSenaraiMenunggu/start.vm";
		return "/vtl/main/portalLogin.vm";
	}
	
	@Command("getNoGiliran")
	public String getNoGiliran() throws Exception {
		try {
			mp = new MyPersistence();
			List<Giliran> giliran = mp
					.list("SELECT x FROM Giliran x WHERE x.noKP = '"
							+ getParam("noPengenalan") + "'");

			if (giliran != null) {
				context.put("giliran", giliran);
				context.put("rekod", true);
			} else {
				context.put("rekod", false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/kuarters/semakSenaraiMenunggu/start.vm";
	}
	/*********************************** END SEMAKAN NO GILIRAN KUARTERS *************************************/
	
	
	/** START PENYELENGGARAAN KUARTERS **/
	@Command("getMaklumatKontraktor")
	public String getMaklumatKontraktor() throws Exception {
		try {
			mp = new MyPersistence();
			List<KontrakKontraktor> kontraktor = mp
					.list("SELECT x FROM KontrakKontraktor x");
			if (kontraktor != null) {
				context.put("kontraktor", kontraktor);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		return getPath() + "/penyelenggaraanKuarters/start.vm";
	}
	/*********************************** END PENYELENGGARAAN KUARTERS *************************************/

	@Command("getPerkhidmatanRuangPejabat")
	public String getPerkhidmatanRuangPejabat() {
		return getPath() + "/ruangPejabat/start.vm";
	}
	
	@Command("getPerkhidmatanRumahPeranginan")
	public String getPerkhidmatanRumahPeranginan() {
		context.put("path", getPath());
		return getPath() + "/rumahPeranginan/start.vm";
	}
	
	@Command("getRumahPeranginanPengenalan")
	public String getRumahPeranginanPengenalan() {
		return getPath() + "/rumahPeranginan/pengenalan.vm";
	}
	
	@Command("getRumahPenginapan")
	public String getRumahPenginapan() {
		return getPath() + "/rumahPeranginan/rumahPenginapan.vm";
	}
	
	@Command("getRumahPeranginan")
	public String getRumahPeranginan() {
		return getPath() + "/rumahPeranginan/rumahPeranginan.vm";
	}

	@Command("getRumahTransit")
	public String getRumahTransit() {
		return getPath() + "/rumahPeranginan/rumahTransit.vm";
	}
	
	@Command("getPerkhidmatanDewanGelanggang")
	public String getPerkhidmatanDewanGelanggang() {
		return getPath() + "/dewanGelanggang/start.vm";
	}
	
	
	@Command("getSepintasLalu")
	public String getSepintasLalu() {
		return getPath() + "/tentangKami/sepintasLalu/start.vm";
	}

	@Command("getManualPengguna")
	public String getManualPengguna() {
		return getPath() + "/manual-pengguna/start.vm";
	}

	@Command("getSejarah")
	public String getSejarah() {
		return getPath() + "/tentangKami/sejarah/start.vm";
	}

	@Command("getPiagam")
	public String getPiagam() {
		return getPath() + "/tentangKami/piagamPelanggan/piagam/start.vm";
	}

	@Command("getKuarters")
	public String getKuarters() {
		return getPath() + "/kuarters/kuarters.vm";
	}

	@Command("getKuaNaziran")
	public String getKuaNaziran() {
		return getPath() + "/kuarters/naziran.vm";
	}

	@Command("getKuaSenggara")
	public String getKuaSenggara() {
		return getPath() + "/kuarters/senggara.vm";
	}

	@Command("getKuaKomuniti")
	public String getKuaKomuniti() {
		return getPath() + "/kuarters/komuniti.vm";
	}

	@Command("getPencapaianPiagam")
	public String getPencapaianPiagam() {
		return getPath()
				+ "/tentangKami/piagamPelanggan/pencapaianPiagam/start.vm";
	}

	@Command("getCarta")
	public String getCarta() {
		return getPath() + "/organization/carta/start.vm";
	}

	@Command("getContactUs")
	public String getContactUs() {
		return getPath() + "/hubungiKami/start.vm";
	}
	
	@Command("getDisclaimer")
	public String getDisclaimer() {
		return getPath() + "/disclaimer/start.vm";
	}

	@Command("getSecurityPolicy")
	public String getSecurityPolicy() {
		return getPath() + "/securityPolicy/start.vm";
	}

	@Command("getPrivacyPolicy")
	public String getPrivacyPolicy() {
		return getPath() + "/privacyPolicy/start.vm";
	}

	@Command("getPeranginanBukitFraser")
	public String getPeranginanBukitFraser() {
		return getPath() + "/rumahPeranginan/bukitFraser/start.vm";
	}

	@Command("getPeranginanMorib")
	public String getPeranginanMorib() {
		return getPath() + "/rumahPeranginan/morib/start.vm";
	}

	@Command("getPeranginanCameron")
	public String getPeranginanCameron() {
		return getPath() + "/rumahPeranginan/cameron/start.vm";
	}

	@Command("getPeranginanPD")
	public String getPeranginanPD() {
		return getPath() + "/rumahPeranginan/pd/start.vm";
	}

	@Command("getPeranginanLangkawi")
	public String getPeranginanLangkawi() {
		return getPath() + "/rumahPeranginan/langkawi/start.vm";
	}

	@Command("getPeranginanKenyir")
	public String getPeranginanKenyir() {
		return getPath() + "/rumahPeranginan/kenyir/start.vm";
	}

	@Command("getPenginapanBukitFraser")
	public String getPenginapanBukitFraser() {
		return getPath() + "/rumahPeranginan/bukitFraserPenginapan/start.vm";
	}

	@Command("getPenginapanTanjungTuan")
	public String getPenginapanTanjungTuan() {
		return getPath() + "/rumahPeranginan/tanjungTuanPenginapan/start.vm";
	}

	@Command("getPeranginanPP")
	public String getPeranginanPP() {
		return getPath() + "/rumahPeranginan/pulauPinang/start.vm";
	}

	@Command("getPenginapanCameron")
	public String getPenginapanCameron() {
		return getPath() + "/rumahPeranginan/cameronPenginapan/start.vm";
	}

	@Command("getPenginapanSingapura")
	public String getPenginapanSingapura() {
		return getPath() + "/rumahPeranginan/singapuraPenginapan/start.vm";
	}

	@Command("getPenginapanLabuan")
	public String getPenginapanLabuan() {
		return getPath() + "/rumahPeranginan/labuanPenginapan/start.vm";
	}

	@Command("getPenginapanLondon")
	public String getPenginapanLondon() {
		return getPath() + "/rumahPeranginan/londonPenginapan/start.vm";
	}

	

	@Command("getPuspanita20061")
	public String getPuspanita20061() {
		return getPath() + "/content/puspanita/2006/majlis-berbuka-puasa.vm";
	}

	@Command("getPuspanita20062")
	public String getPuspanita20062() {
		return getPath() + "/content/puspanita/2006/pertandingan-bola-tampar.vm";
	}

	@Command("getPuspanita20063")
	public String getPuspanita20063() {
		return getPath() + "/content/puspanita/2006/pertandingan-treasure-hunt.vm";
	}

	@Command("getPuspanita20064")
	public String getPuspanita20064() {
		return getPath() + "/content/puspanita/2006/pertandingan-bowling.vm";
	}

	@Command("getPuspanita20071")
	public String getPuspanita20071() {
		return getPath() + "/content/puspanita/2007/jualan-hari-karnival.vm";
	}

	@Command("getPuspanita20072")
	public String getPuspanita20072() {
		return getPath() + "/content/puspanita/2007/lawatan-langkawi.vm";
	}

	@Command("getPuspanita20073")
	public String getPuspanita20073() {
		return getPath() + "/content/puspanita/2007/lawatan-rumah-kanak-kanak.vm";
	}

	@Command("getPuspanita20074")
	public String getPuspanita20074() {
		return getPath() + "/content/puspanita/2007/mesyuarat-terhimpun.vm";
	}

	@Command("getPuspanita20075")
	public String getPuspanita20075() {
		return getPath() + "/content/puspanita/2007/lawatan-kota-bharu.vm";
	}

	@Command("getPuspanita20076")
	public String getPuspanita20076() {
		return getPath() + "/content/puspanita/2007/ceramah-hak-wanita.vm";
	}

	@Command("getPuspanita20077")
	public String getPuspanita20077() {
		return getPath() + "/content/puspanita/2007/lawatan-sambil-belajar.vm";
	}

	@Command("getPuspanita20081")
	public String getPuspanita20081() {
		return getPath() + "/content/puspanita/2008/jualan-puspanita.vm";
	}

	@Command("getPuspanita20082")
	public String getPuspanita20082() {
		return getPath() + "/content/puspanita/2008/pertandingan-congkak.vm";
	}

	@Command("getPuspanita20083")
	public String getPuspanita20083() {
		return getPath() + "/content/puspanita/2008/pertandingan-dart.vm";
	}

	@Command("getPuspanita20084")
	public String getPuspanita20084() {
		return getPath() + "/content/puspanita/2008/pertandingan-batu-seremban.vm";
	}

	@Command("getPuspanita20085")
	public String getPuspanita20085() {
		return getPath() + "/content/puspanita/2008/seminar-ketrampilan-diri.vm";
	}

	@Command("getPuspanita20086")
	public String getPuspanita20086() {
		return getPath() + "/content/puspanita/2008/lawatan-anak-anak-yatim.vm";
	}

	@Command("getPuspanita20087")
	public String getPuspanita20087() {
		return getPath() + "/content/puspanita/2008/mesyuarat-terhimpun.vm";
	}

	@Command("getPuspanita20088")
	public String getPuspanita20088() {
		return getPath() + "/content/puspanita/2008/kelas-memasak.vm";
	}

	@Command("getPuspanita20089")
	public String getPuspanita20089() {
		return getPath() + "/content/puspanita/2008/seminar-rahsia-kejayaan.vm";
	}

	@Command("getPuspanita200810")
	public String getPuspanita200810() {
		return getPath() + "/content/puspanita/2008/pertandingan-bowling.vm";
	}

	@Command("getPuspanita200811")
	public String getPuspanita200811() {
		return getPath() + "/content/puspanita/2008/seminar-hari-ibu.vm";
	}

	@Command("getPuspanita20091")
	public String getPuspanita20091() {
		return getPath() + "/content/puspanita/2009/lawatan-langkawi.vm";
	}

	@Command("getPuspanita20092")
	public String getPuspanita20092() {
		return getPath() + "/content/puspanita/2009/mesyuarat-terhimpun.vm";
	}

	@Command("getPuspanita20093")
	public String getPuspanita20093() {
		return getPath() + "/content/puspanita/2009/lawatan-penjara.vm";
	}

	

	@Command("getMainTemujanji")
	public String getMainTemujanji() {
		context.put("path", getPath());

		return getPath() + "/temujanji/start.vm";
	}

	@Command("getMainSemakanPembayaran")
	public String getMainSemakanPembayaran() {
		return getPath() + "/semakanPembayaran/start.vm";
	}

	@Command("getMainTenderSebutHarga")
	public String getMainTenderSebutHarga() {
		return getPath() + "/vendor/tenderSebutHarga/start.vm";
	}

	@Command("getMainIklanPengambilanPS_IT")
	public String getMainIklanPengambilanPS_IT() {
		return getPath() + "/tenderPengambilanPS_IT/start.vm";
	}

	@Command("getMainIklanKerjaRequisition")
	public String getMainIklanKerjaRequisition() {
		return getPath() + "/tenderKerjaRequisition/start.vm";
	}

	@Command("getSebutHarga1")
	public String getSebutHarga1() {
		return getPath() + "/vendor/tenderSebutHarga/sebutHarga1/start.vm";
	}

	@Command("getSebutHarga2")
	public String getSebutHarga2() {
		return getPath() + "/vendor/tenderSebutHarga/sebutHarga2/start.vm";
	}

	@Command("getMainIklanRuangKomersil")
	public String getMainIklanRuangKomersil() {
		return getPath() + "/iklanRuangKomersil/start.vm";
	}

	@Command("getMainArkib")
	public String getMainArkib() {
		return getPath() + "/arkib/start.vm";
	}

	@Command("getMainPengumumanStatusGST")
	public String getMainPengumumanStatusGST() {
		return getPath() + "/pengumuman/statusGST/start.vm";
	}

	@Command("getStatusGSTSub")
	public String getStatusGSTSub() {
		return getPath() + "/pengumuman/statusGST/statusGST/start.vm";
	}

	@Command("getTakwimJRP")
	public String getTakwimJRP() {
		return getPath() + "/pengumuman/statusGST/takwimJRP/start.vm";
	}

	@Command("getMainPengumumanDeposit")
	public String getMainPengumumanDeposit() {
		return getPath() + "/pengumuman/bayaranDeposit/start.vm";
	}

	@Command("getBrgTuntutanDeposit")
	public String getBrgTuntutanDeposit() {
		return getPath()
				+ "/pengumuman/bayaranDeposit/brgTuntutanDeposit/start.vm";
	}

	@Command("getPanduanDepositKuarters")
	public String getPanduanDepositKuarters() {
		return getPath()
				+ "/pengumuman/bayaranDeposit/panduanDepositKuarters/start.vm";
	}

	@Command("getPanduanDepositRuangPejabat")
	public String getPanduanDepositRuangPejabat() {
		return getPath()
				+ "/pengumuman/bayaranDeposit/panduanDepositRuangPejabat/start.vm";
	}

	@Command("getMainPengumumanRPPPD")
	public String getMainPengumumanRPPPD() {
		return getPath() + "/pengumuman/rppPD/start.vm";
	}

	@Command("getMainPengumumanPeluangPekerjaan")
	public String getMainPengumumanPeluangPekerjaan() {
		return getPath() + "/pengumuman/peluangPekerjaan/start.vm";
	}

	@Command("getMainPengumumanNotisMakluman")
	public String getMainPengumumanNotisMakluman() {
		return getPath() + "/pengumuman/notisMakluman/start.vm";
	}

	@Command("getETemuJanji")
	public String getETemuJanji() {
		return getPath() + "/kuarters/eTemujanji/start.vm";
	}

	@Command("getPermohonan")
	public String getPermohonan() {
		return getPath() + "/kuarters/permohonan/start.vm";
	}

	@Command("getDirektoriRumahPeranginanSub")
	public String getDirektoriRumahPeranginanSub() {
		return getPath()
				+ "/organization/direktori/rumahPeranginan/rumahPeranginan/start.vm";
	}

	@Command("getDirektoriPusatPerkhidmatan")
	public String getDirektoriPusatPerkhidmatan() {
		return getPath()
				+ "/organization/direktori/rumahPeranginan/pusatPerkhidmatan/start.vm";
	}

	@Command("getDirektoriRPCawangan")
	public String getDirektoriRPCawangan() {
		return getPath()
				+ "/organization/direktori/rumahPeranginan/cawangan/start.vm";
	}

	@Command("getDirektoriRuangPejabatSub")
	public String getDirektoriRuangPejabatSub() {
		return getPath()
				+ "/organization/direktori/ruangPejabat/ruangPejabat/start.vm";
	}

	@Command("getDirektoriBSI")
	public String getDirektoriBSI() {
		return getPath() + "/organization/direktori/ruangPejabat/bsi/start.vm";
	}

	@Command("getDirektoriTBS")
	public String getDirektoriTBS() {
		return getPath() + "/organization/direktori/ruangPejabat/tbs/start.vm";
	}

	@Command("getDirektoriKuaCawangan")
	public String getDirektoriKuaCawangan() {
		return getPath()
				+ "/organization/direktori/pengurusanKuarters/cawangan/start.vm";
	}

	@Command("getPermohonanRuangPejabat")
	public String getPermohonanRuangPejabat() {
		return getPath() + "/ruangPejabat/permohonanRuangPejabat/start.vm";
	}

	@Command("getPromosi")
	public String getPromosi() {
		return getPath() + "/ruangPejabat/promosi/start.vm";
	}

	@Command("getMySMSPanduan")
	public String getMySMSPanduan() {
		return getPath() + "/mySMS/panduan/start.vm";
	}

	@Command("getMySMSProjek")
	public String getMySMSProjek() {
		return getPath() + "/mySMS/projek/start.vm";
	}

	@Command("getStatistik")
	public String getStatistik() {
		return getPath() + "/statistik/start.vm";
	}

	@Command("getKontraktorPenyelenggaraan")
	public String getKontraktorPenyelenggaraan() {
		return getPath() + "/kontraktorPenyelenggaraan/start.vm";
	}

	@Command("getGarisPanduan")
	public String getGarisPanduan() {
		return getPath() + "/pekeliling/garis-panduan/start.vm";
	}

	@Command("getPerkhidmatanBil2")
	public String getPerkhidmatanBil2() {
		return getPath() + "/pekeliling/pekeliling/perkhidmatanBil2/start.vm";
	}

	@Command("getPerkhidmatanBil13")
	public String getPerkhidmatanBil13() {
		return getPath() + "/pekeliling/pekeliling/perkhidmatanBil13/start.vm";
	}

	@Command("getPerbendaharaanBil11")
	public String getPerbendaharaanBil11() {
		return getPath()
				+ "/pekeliling/pekeliling/perbendaharaanBil11/start.vm";
	}

	@Command("getPekelilingAm")
	public String getPekelilingAm() {
		return getPath() + "/pekeliling/start.vm";
	}

	@Command("getAmBil42013")
	public String getAmBil42013() {
		return getPath() + "/pekeliling/surat-pekeliling/amBil42013/start.vm";
	}

	@Command("getAmBil32011")
	public String getAmBil32011() {
		return getPath() + "/pekeliling/surat-pekeliling/amBil32011/start.vm";
	}

	@Command("getAmBil12010")
	public String getAmBil12010() {
		return getPath() + "/pekeliling/surat-pekeliling/amBil12010/start.vm";
	}

	@Command("getAmBil32007")
	public String getAmBil32007() {
		return getPath() + "/pekeliling/surat-pekeliling/amBil32007/start.vm";
	}

	@Command("getAmBil22006")
	public String getAmBil22006() {
		return getPath() + "/pekeliling/surat-pekeliling/amBil22006/start.vm";
	}

	@Command("getFAQRumahPeranginan")
	public String getFAQRumahPeranginan() {
		return getPath() + "/faq/pengurusan/rumahPeranginan.vm";
	}

	@Command("getFAQKuarters")
	public String getFAQKuarters() {
		return getPath() + "/faq/pengurusan/kuarters.vm";
	}

	@Command("getFAQRuangPejabat")
	public String getFAQRuangPejabat() {
		return getPath() + "/faq/pengurusan/ruangPejabat.vm";
	}

	@Command("getFAQUnitPembangunan")
	public String getFAQUnitPembangunan() {
		return getPath() + "/faq/pembangunan.vm";
	}

	@Command("getFAQUnitPRO")
	public String getFAQUnitPRO() {
		return getPath() + "/faq/perhubunganAwam.vm";
	}

	@Command("getFAQUnitPenguatkuasaan")
	public String getFAQUnitPenguatkuasaan() {
		return getPath() + "/faq/penguatkuasaan.vm";
	}

	@Command("getFAQCajPembentungan")
	public String getFAQCajPembentungan() {
		return getPath() + "/faq/utiliti/pembentungan.vm";
	}

	@Command("getFAQKebocoranPaip")
	public String getFAQKebocoranPaip() {
		return getPath() + "/faq/utiliti/kebocoranPaip.vm";
	}

	@Command("getMuatTurunDokumen")
	public String getMuatTurunDokumen() {
		return getPath() + "/dokumen/muat-turun/start.vm";
	}

	@Command("getDokumenISO")
	public String getDokumenISO() {
		return getPath() + "/dokumen/muat-turun/dokumen-iso/start.vm";
	}

	@Command("getDokumenDalaman")
	public String getDokumenDalaman() {
		return getPath() + "/dokumen/muat-turun/dalaman/start.vm";
	}

	@Command("getDokumenBooklet")
	public String getDokumenBooklet() {
		return getPath() + "/dokumen/muat-turun/booklet/start.vm";
	}

	@Command("getDokumenSenarai")
	public String getDokumenSenarai() {
		return getPath() + "/dokumen/muat-turun/senarai/start.vm";
	}

	@Command("getDokumenLaporan")
	public String getDokumenLaporan() {
		return getPath() + "/dokumen/muat-turun/laporan-tahunan/start.vm";
	}

	@Command("getDokumenBuletin")
	public String getDokumenBuletin() {
		return getPath() + "/dokumen/muat-turun/buletin/start.vm";
	}

	@Command("getDokumenPamplet")
	public String getDokumenPamplet() {
		return getPath() + "/dokumen/muat-turun/pamplet/start.vm";
	}

	@Command("getDokumenBorang")
	public String getDokumenBorang() {
		return getPath() + "/dokumen/muat-turun/borang/start.vm";
	}
	
	public void addFilter(String s) {
		this.filterList.add(s);
	}

	@Command("getPekeliling")
	public String getPekeliling() {
		return getPath() + "/pekeliling/start.vm";
	}

	@Command("getHalatuju")
	public String getHalatuju() {
		
//		context.put("halaTuju", portalUtil.getHalaTujuStrategik());
//		context.put("misi", portalUtil.getHalaTujuMisi());
//		context.put("objektif", portalUtil.getHalaTujuObjektif());
//		context.put("dasarKualiti", portalUtil.getHalaTujuDasarKualiti());
//		context.put("objektifKualiti", portalUtil.getHalaTujuObjektifKualiti());
		return getPath() + "/tentangKami/start.vm";
	}
	
//	@Command("getObjektif")
//	public String getObjektif() {
//		
//		context.put("objektif", portalUtil.getHalaTujuObjektif());
//		context.put("dasarKualiti", portalUtil.getHalaTujuDasarKualiti());
//		context.put("objektifKualiti", portalUtil.getHalaTujuObjektifKualiti());
//		return getPath() + "/tentangKami/sepintasLalu/objektif.vm";
//	}

	@Command("getSoalanLazim")
	public String getSoalanLazim() {
		return getPath() + "/faq/start.vm";
	}

	@Command("getPerutusanSUB")
	public String getPerutusanSUB() {
		return getPath() + "/perutusan/start.vm";
	}

	@Command("getListSubGaleriGambar")
	public String getListSubGaleriGambar() {
		try {
			mp = new MyPersistence();
			context.put("listSubGaleriGambar", portalUtil.getListSubGaleriGambar(getParam("idGaleri"), mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { if (mp != null) { mp.close(); } }
		}		
		//return getPath() + "/galeri/arkibGambar/listGambar.vm";
		return getPath() + "/content/galeri/gambar/listGambar.vm";
	}

	@Command("getRumahPenginapanSUB")
	public String getRumahPenginapanSUB() {
		return getPath() + "/rumahPeranginan/rumahPenginapanSub.vm";
	}

	@Command("getRumahPeranginanSUB")
	public String getRumahPeranginanSUB() {
		return getPath() + "/rumahPeranginan/rumahPeranginanSub.vm";
	}

	@Command("getRumahTransitSUB")
	public String getRumahTransitSUB() {
		return getPath() + "/rumahPeranginan/rumahTransitSub.vm";
	}

	@Command("getTransitPutrajaya")
	public String getTransitPutrajaya() {
		return getPath() + "/rumahPeranginan/putrajayaTransit/start.vm";
	}

	@Command("getTransitKKKAJlnDuta")
	public String getTransitKKKAJlnDuta() {
		return getPath() + "/rumahPeranginan/jlnDutaTransit/start.vm";
	}

	@Command("getTransitKKKAJlnUThant")
	public String getTransitKKKAJlnUThant() {
		return getPath() + "/rumahPeranginan/jlnUthantTransit/start.vm";
	}

	@Command("getTransitKIKTgKupang")
	public String getTransitKIKTgKupang() {
		return getPath() + "/rumahPeranginan/tgKupangTransit/start.vm";
	}

	@Command("getTransitLabuan")
	public String getTransitLabuan() {
		return getPath() + "/rumahPeranginan/labuanTransit/start.vm";
	}
	
	@Command("getTransitPortDickson")
	public String getTransitPortDickson() {
		return getPath() + "/rumahPeranginan/pdTransit/start.vm";
	}	

	@Command("getPanduanPengguna")
	public String getPanduanPengguna() {
		return getPath() + "/panduanPengguna/start.vm";
	}

	@Command("getUMPenggunaAwam")
	public String getUMPenggunaAwam() {
		return getPath() + "/panduanPengguna/start1.vm";
	}

	@Command("getUMPenjawatAwam")
	public String getUMPenjawatAwam() {
		return getPath() + "/panduanPengguna/start2.vm";
	}

	@Command("getUMBadanBerkanun")
	public String getUMBadanBerkanun() {
		return getPath() + "/panduanPengguna/start3.vm";
	}
}

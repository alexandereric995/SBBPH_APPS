package bph.modules.senggara;

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
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kod.Fasa;
import bph.entities.kod.LokasiKuarters;
import bph.entities.kod.Status;
import bph.entities.kod.StatusKuarters;
import bph.entities.qtr.KuaKuarters;
import bph.entities.senggara.MtnAgihanTugas;
import bph.entities.senggara.MtnJKH;
import bph.entities.senggara.MtnKuarters;
import bph.entities.senggara.MtnLawatanTapak;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class RekodKunciRecordModule extends LebahRecordTemplateModule<MtnKuarters>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<MtnKuarters> getPersistenceClass() {
		// TODO Auto-generated method stub
		return MtnKuarters.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/senggara/rekodKunci";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectKawasan", dataUtil.getListLokasiPermohonan());
		context.put("selectKelasKuarters", dataUtil.getListKelasKuarters());
		context.put("selectJenisKuarters", dataUtil.getListJenisKediaman());
		context.put("selectStatus", dataUtil.getListStatusModulSenggara());
		
		boolean showTerimaanKunci = true;
		context.put("showTerimaanKunci", showTerimaanKunci);	
		boolean showSerahanKunci = true;
		context.put("showSerahanKunci", showSerahanKunci);		
		boolean showTugasan = true;
		context.put("showTugasan", showTugasan);
		boolean showStatus = true;
		context.put("showStatus", showStatus);
		
		context.put("flagSkrin", "RekodKunciRecordModule");
		
		context.remove("idPilihanTugasan");
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}

	private void defaultButtonOption() {
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
	
	private void addfilter() {
		
		this.setOrderBy("tarikhTerimaLaporan");
		this.setOrderType("desc");
	}

	@Override
	public void save(MtnKuarters kuarters) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(MtnKuarters kuarters) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("kuarters.noRujukan", getParam("findNoRujukan").trim());
		map.put("kuarters.noUnit", getParam("findNoUnit").trim());
		map.put("kuarters.blok", getParam("findBlok").trim());
		map.put("kuarters.alamat1", getParam("findAlamat").trim());	
		map.put("kuarters.alamat2", getParam("findAlamat").trim());	
		map.put("kuarters.alamat3", getParam("findAlamat").trim());	
		map.put("tarikhTerimaLaporan", new OperatorDateBetween(getDate("findTarikhLaporanMula"), getDate("findTarikhLaporanHingga")));
		
		map.put("kuarters.lokasi.lokasi.id", new OperatorEqualTo(get("findKawasan")));
		map.put("kuarters.lokasi.id", new OperatorEqualTo(get("findLokasi")));
		map.put("kuarters.fasa.id", new OperatorEqualTo(get("findFasa")));
		map.put("kuarters.kelas.id", new OperatorEqualTo(get("findKelasKuarters")));
		map.put("kuarters.jenisKediaman.id", new OperatorEqualTo(get("findJenisKuarters")));
		map.put("status.id", new OperatorEqualTo(get("findStatus")));
		
		return map;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(MtnKuarters kuarters) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getRelatedData(MtnKuarters kuarters) {		
		
		try {
			mp = new MyPersistence();				

			//SENARAI SEMAKAN KUARTERS
			List<MtnLawatanTapak> listSemakanLama = mp.list("select x from MtnLawatanTapak x where x.tarikhLawatan is not null and x.kuartersSenggara.id = '" + kuarters.getId() + "' order by x.tarikhLawatan asc");
			context.put("listSemakanLama", listSemakanLama);
			
			//SEMAKAN KUARTERS SEMASA
			MtnLawatanTapak semakanSemasa = (MtnLawatanTapak) mp.get("select x from MtnLawatanTapak x where x.flagAktif = 'Y' and x.kuartersSenggara.id = '" + kuarters.getId() + "' order by x.tarikhLawatan asc");
			context.put("semakanSemasa", semakanSemasa);
			
			MtnJKH jkh = (MtnJKH) mp.get("select x from MtnJKH x where x.kuartersSenggara.id = '" + kuarters.getId() + "'");
			context.put("jkh", jkh);
			
			List<Users> penyedia = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPenyediaSecondary() + ") OR u.role.description = '(SENGGARA) Penyedia' ORDER BY u.userName ASC");
			List<Users> penyemak = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPenyemakSecondary() + ") OR u.role.description = '(SENGGARA) Penyemak' ORDER BY u.userName ASC");
			context.put("selectPenyediaSenggara", penyedia);
			context.put("selectPenyemakSenggara", penyemak);
			
			context.put("selectedTab", 1);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
	}
	
	@Command("paparRekod")
	public String paparRekod() throws Exception {
		try {
			mp = new MyPersistence();
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			context.put("r", kuarters);
			
			//SENARAI SEMAKAN KUARTERS
			List<MtnLawatanTapak> listSemakanLama = mp.list("select x from MtnLawatanTapak x where x.tarikhLawatan is not null and x.kuartersSenggara.id = '" + kuarters.getId() + "' order by x.tarikhLawatan asc");
			context.put("listSemakanLama", listSemakanLama);
			
			//SEMAKAN KUARTERS SEMASA
			MtnLawatanTapak semakanSemasa = (MtnLawatanTapak) mp.get("select x from MtnLawatanTapak x where x.flagAktif = 'Y' and x.kuartersSenggara.id = '" + kuarters.getId() + "' order by x.tarikhLawatan asc");
			context.put("semakanSemasa", semakanSemasa);
			
			MtnJKH jkh = (MtnJKH) mp.get("select x from MtnJKH x where x.kuartersSenggara.id = '" + kuarters.getId() + "'");
			context.put("jkh", jkh);			

			List<Users> penyedia = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPenyediaSecondary() + ") OR u.role.description = '(SENGGARA) Penyedia' ORDER BY u.userName ASC");
			List<Users> penyemak = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPenyemakSecondary() + ") OR u.role.description = '(SENGGARA) Penyemak' ORDER BY u.userName ASC");
			List<Users> pelulus = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPengesyorSecondary() + ") OR u.role.description = '(SENGGARA) Pengesyor' ORDER BY u.userName ASC");
			context.put("selectPenyediaSenggara", penyedia);
			context.put("selectPenyemakSenggara", penyemak);
			context.put("selectPelulusSenggara", pelulus);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 		
		
		return getPath() + "/entry_page.vm";
	}
	
	/** START TAB **/
	@Command("getMaklumatTerimaanKunci")
	public String getMaklumatTerimaanKunci() throws Exception {
		context.put("selectedTab", 1); 		
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatSemakanKuarters")
	public String getMaklumatSemakanKuarters() throws Exception {
		try {
			mp = new MyPersistence();
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			context.put("r", kuarters);
			
			//SENARAI SEMAKAN KUARTERS
			List<MtnLawatanTapak> listSemakanLama = mp.list("select x from MtnLawatanTapak x where x.tarikhLawatan is not null and x.kuartersSenggara.id = '" + kuarters.getId() + "' order by x.tarikhLawatan asc");
			context.put("listSemakanLama", listSemakanLama);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 		
		context.put("selectedTab", 2); 		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatJKH")
	public String getMaklumatJKH() throws Exception {
		try {
			mp = new MyPersistence();
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			context.put("r", kuarters);
			
			MtnJKH jkh = (MtnJKH) mp.get("select x from MtnJKH x where x.kuartersSenggara.id = '" + kuarters.getId() + "'");
			context.put("jkh", jkh);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 		
		context.put("selectedTab", 3); 		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatSerahanKunci")
	public String getMaklumatSerahanKunci() throws Exception {
		context.put("selectedTab", 4); 		
		
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START TERIMAAN KUNCI */	
	@Command("simpanTerimaanKunci")
	public String simpanTerimaanKunci() throws Exception {		
		
		try {
			mp = new MyPersistence();
			Users penerimaKunci = (Users) mp.find(Users.class, userId);
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(kuarters);
			agihanTugas.setPegawaiAgihan(penerimaKunci);
			agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, get("idPenyemakSenggara")));
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(get("catatanTerimaanKunci"));
			agihanTugas.setStatus((Status) mp.find(Status.class, "1427773516426"));
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);		
			
			kuarters.setTarikhTerimaKunci(getDate("tarikhTerimaKunci"));
			kuarters.setPenerimaKunci(penerimaKunci);
			kuarters.setCatatanTerimaanKunci(get("catatanTerimaanKunci"));
			kuarters.setTugasan(agihanTugas);
			kuarters.setStatus((Status) mp.find(Status.class, "1427773516426")); //AGIHAN TUGAS SEMAKAN KUARTERS
			
			mp.commit();
		} catch (Exception ex) {
			System.out.println("GAGAL DAFTAR TERIMAAN KUNCI : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
				
		return paparRekod();
	}
	/** END TERIMAAN KUNCI */
	
	/** START AGIHAN TUGAS SEMAKAN KUARTERS */
	@Command("pilihanTugasan")
	public String pilihanTugasan() throws Exception {
		
		context.put("idPilihanTugasan", getParam("idPilihanTugasan"));
		
		return paparRekod();
	}
	
	@Command("simpanAgihanTugas")
	public String simpanAgihanTugas() throws Exception {

		try {
			mp = new MyPersistence();
			Users penyemak = (Users) mp.find(Users.class, userId);
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			String idPilihanTugasan = getParam("idPilihanTugasan");
			context.put("idPilihanTugasan", idPilihanTugasan);
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
			agihanTugasLama.setFlagAktif("T");
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(kuarters);
			agihanTugas.setPegawaiAgihan(penyemak);
			if (idPilihanTugasan.equals("1")) {
				agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, get("idPenyediaSenggara")));
				agihanTugas.setStatus((Status) mp.find(Status.class, "762326425830")); //SEMAKAN KUARTERS
				
				kuarters.setStatus((Status) mp.find(Status.class, "762326425830")); //SEMAKAN KUARTERS
				
				MtnLawatanTapak lawatanTapak = new MtnLawatanTapak();
				lawatanTapak.setKuartersSenggara(kuarters);
				lawatanTapak.setFlagAktif("Y");
				mp.persist(lawatanTapak);
			} else {
				agihanTugas.setStatus((Status) mp.find(Status.class, "277591106039815")); //TUGASAN DIPULANGKAN
				
				kuarters.setStatus((Status) mp.find(Status.class, "277591106039815")); //TUGASAN DIPULANGKAN
			}
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(get("catatanTugasan"));			
			
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);		
			
			kuarters.setTugasan(agihanTugas);
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparRekod();
	}
	/** START AGIHAN TUGAS SEMAKAN KUARTERS */
	
	/** START SEMAKAN KUARTERS */
	@Command("doChangeJenisPembaikan")
	public String doChangeJenisPembaikan() throws Exception {
		String idPilihanTugasan = getParam("idPilihanTugasan");
		context.put("idPilihanTugasan", idPilihanTugasan);
		context.put("dateLawatan", getParam("tarikhLawatan"));
		context.put("idJenisPembaikan", getParam("idJenisPembaikan"));
		context.put("idKontraktor", getParam("idKontraktor"));
		context.put("ulasanLawatan", getParam("ulasanLawatan"));
		if (getParam("updateLampiranLaporanKerosakanReload").trim().length() > 0) {
			context.put("updateLampiranLaporanKerosakanReload", getParam("updateLampiranLaporanKerosakanReload"));
		}
		
		return paparRekod();
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadLampiranLaporanKerosakan")
	public String uploadLampiranLaporanKerosakan() throws Exception {
		String idLawatanTapak = getParam("idLawatanTapak");
			
		String uploadDir = "senggara/laporanKerosakan/";
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

			String imgName = uploadDir + idLawatanTapak + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(imgName);
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			context.put("lampiranLaporanKerosakanReload", imgName);
		}
		return getPath() + "/semakanKuarters/uploadLampiranLaporanKerosakan.vm";
	}

	@Command("refreshLampiranLaporanKerosakan")
	public String refreshLampiranLaporanKerosakan() throws Exception {
		context.put("updateLampiranLaporanKerosakanReload", getParam("updateLampiranLaporanKerosakanReload"));
		return getPath() + "/semakanKuarters/updateLampiranLaporanKerosakan.vm";
	}
	
	@Command("simpanSemakanKuarters")
	public String simpanSemakanKuarters() throws Exception {
		try {
			mp = new MyPersistence();
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			MtnLawatanTapak lawatanTapak = (MtnLawatanTapak) mp.find(MtnLawatanTapak.class, getParam("idLawatanTapak"));			
			String idPilihanTugasan = getParam("idPilihanTugasan");
			context.put("idPilihanTugasan", idPilihanTugasan);
			
			mp.begin();						
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
			agihanTugasLama.setFlagAktif("T");
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(kuarters);
			agihanTugas.setPegawaiAgihan(penyedia);			
			agihanTugas.setTarikhTugasan(new Date());
			
			if (idPilihanTugasan.equals("1")) {
				agihanTugas.setCatatan(getParam("ulasanLawatan"));
				if (getParam("idJenisPembaikan").equals("1")) {
					agihanTugas.setPegawaiTugasan(null);
					agihanTugas.setStatus((Status) mp.find(Status.class, "144160488701579")); //SERAHAN KUNCI	
					
					kuarters.setStatus((Status) mp.find(Status.class, "144160488701579")); //SERAHAN KUNCI	
				} else if (getParam("idJenisPembaikan").equals("2")) {
					agihanTugas.setPegawaiTugasan(penyedia);
					agihanTugas.setStatus((Status) mp.find(Status.class, "1426130691702")); //PENYEDIAAN JKH
					
					kuarters.setStatus((Status) mp.find(Status.class, "1426130691702")); //PENYEDIAAN JKH
					
					MtnJKH jkh = new MtnJKH();
					jkh.setKuartersSenggara(kuarters);
					jkh.setNoJKH(kuarters.getKuarters().getNoUnit());
					jkh.setTarikhJKH(new Date());
					jkh.setPenyedia(penyedia);
					jkh.setJumlah(0D);
					jkh.setGst(0D);
					jkh.setJumlahKeseluruhan(0D);
					jkh.setStatusPembaikan("T");
					mp.persist(jkh);
					
				} else if (getParam("idJenisPembaikan").equals("3")) {
					agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyemakSenggara")));
					agihanTugas.setStatus((Status) mp.find(Status.class, "1436163572107")); //PEMBAIKAN OLEH AGENSI PELAKSANA / PEMAJU
					
					kuarters.setStatus((Status) mp.find(Status.class, "1436163572107")); //PEMBAIKAN OLEH AGENSI PELAKSANA / PEMAJU
				}
			} else {
				agihanTugas.setCatatan(getParam("catatanTugasan"));
				agihanTugas.setPegawaiTugasan(agihanTugasLama.getPegawaiAgihan());
				agihanTugas.setStatus((Status) mp.find(Status.class, "1427773516426")); //AGIHAN TUGAS SEMAKAN KUARTERS
				
				kuarters.setStatus((Status) mp.find(Status.class, "1427773516426")); //AGIHAN TUGAS SEMAKAN KUARTERS
			}
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);	
			kuarters.setTugasan(agihanTugas);
			
			if (lawatanTapak != null) {
				if ("1".equals(idPilihanTugasan)) {
					lawatanTapak.setTarikhLawatan(getDate("tarikhLawatan"));
					lawatanTapak.setFlagPembaikan(getParam("idJenisPembaikan"));
					if (getParam("idJenisPembaikan").equals("3")) {
						lawatanTapak.setKontraktor(getParam("idKontraktor"));
						if (getParam("idKontraktor").equals("L")) {
							lawatanTapak.setKontraktorLain(getParam("kontraktorLain"));
						}
					} else {
						lawatanTapak.setFlagAktif("T");
					}
					lawatanTapak.setUlasanLawatan(getParam("ulasanLawatan"));
					//LAPORAN KEROSAKAN
					if (getParam("updateLampiranLaporanKerosakanReload").trim().length() > 0) {
						lawatanTapak.setFileLaporanKerosakan(getParam("updateLampiranLaporanKerosakanReload"));
					}
				} else {
					mp.remove(lawatanTapak);
				}								
			}			
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparRekod();
	}
	/** END SEMAKAN KUARTERS */
	
	/** START PEMBAIKAN AGENSI PELAKSANA / PEMAJU **/
	@Command("simpanPembaikanAgensi")
	public String simpanPembaikanAgensi() throws Exception {
		
		try {
			mp = new MyPersistence();
			Users penyemak = (Users) mp.find(Users.class, userId);
			MtnLawatanTapak lawatanTapak = (MtnLawatanTapak) mp.find(MtnLawatanTapak.class, getParam("idLawatanTapak"));
			
			mp.begin();
			lawatanTapak.setTarikhSerahKontraktor(getDate("tarikhSerahKontraktor"));
			lawatanTapak.setTarikhTerimaKontraktor(getDate("tarikhTerimaKontraktor"));
			lawatanTapak.setUlasanPembaikanMajor(getParam("ulasan"));
			lawatanTapak.setFlagAktif("T");
			
			MtnLawatanTapak newLawatanTapak = new MtnLawatanTapak();
			newLawatanTapak.setKuartersSenggara(lawatanTapak.getKuartersSenggara());
			newLawatanTapak.setFlagAktif("Y");
			mp.persist(newLawatanTapak);
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + lawatanTapak.getKuartersSenggara().getId() + "'");
			agihanTugasLama.setFlagAktif("T");

			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(lawatanTapak.getKuartersSenggara());
			agihanTugas.setPegawaiAgihan(penyemak);
			agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(getParam("ulasan"));
			agihanTugas.setStatus((Status) mp.find(Status.class, "762326425830")); //SEMAKAN KUARTERS
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);		
							
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, lawatanTapak.getKuartersSenggara().getId());
			kuarters.setTugasan(agihanTugas);
			kuarters.setStatus((Status) mp.find(Status.class, "762326425830")); //SEMAKAN KUARTERS
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparRekod();
	}
	/** END PEMBAIKAN AGENSI PELAKSANA / PEMAJU **/
	
	/** START PENYEDIAAN JKH */	
	@SuppressWarnings("rawtypes")
	@Command("uploadLampiranJadualKadarHarga")
	public String uploadLampiranJadualKadarHarga() throws Exception {
		String idJKH = getParam("idJKH");
			
		String uploadDir = "senggara/jkh/";
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

			String imgName = uploadDir + idJKH + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(imgName);
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			context.put("lampiranJadualKadarHargaReload", imgName);
		}
		return getPath() + "/penyediaanJKH/uploadLampiranJadualKadarHarga.vm";
	}

	@Command("refreshLampiranJadualKadarHarga")
	public String refreshLampiranJadualKadarHarga() throws Exception {
		context.put("updateLampiranJadualKadarHargaReload", getParam("updateLampiranJadualKadarHargaReload"));
		return getPath() + "/penyediaanJKH/updateLampiranJadualKadarHarga.vm";
	}
	
	@Command("doCalculateJumlahKeseluruhan")
	public String doCalculateJumlahKeseluruhan() throws Exception {		
		Double jumlah = 0D;
		Double gst = 0D;
		Double jumlahKeseluruhan = 0D;
		
		if (!getParam("jumlah").equals("") && !Util.removeNonNumeric(getParam("jumlah")).equals("")) {
			jumlah = Double.valueOf(Util.removeNonNumeric(getParam("jumlah")));
		}
		if (!getParam("gst").equals("") && !Util.removeNonNumeric(getParam("gst")).equals("")) {
			gst = Double.valueOf(Util.removeNonNumeric(getParam("gst")));
		}
		jumlahKeseluruhan = (gst / 100 * jumlah) + jumlah;
		
		context.put("dateJKH", getParam("tarikhJKH"));
		context.put("jumlah", jumlah);
		context.put("gst", gst);	
		context.put("jumlahKeseluruhan", jumlahKeseluruhan);		
		if (getParam("updateLampiranJadualKadarHargaReload").trim().length() > 0) {
			context.put("updateLampiranJadualKadarHargaReload", getParam("updateLampiranJadualKadarHargaReload"));
		}
		context.put("catatanPenyedia", getParam("catatanPenyedia"));
		
		return paparRekod();
	}
	
	@Command("simpanPenyediaanJKH")
	public String simpanPenyediaanJKH() throws Exception {
		Double jumlah = 0D;
		Double gst = 0D;
		Double jumlahKeseluruhan = 0D;
		
		try {
			mp = new MyPersistence();
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			if (jkh != null) {
				jkh.setTarikhJKH(getDate("tarikhJKH"));				
				if (!getParam("jumlah").equals("")) {
					jumlah = Double.valueOf(Util.RemoveComma(getParam("jumlah")));
				}
				if (!getParam("gst").equals("")) {
					gst = Double.valueOf(Util.RemoveComma(getParam("gst")));
				}
				jumlahKeseluruhan = (gst / 100 * jumlah) + jumlah;
				jkh.setJumlah(jumlah);
				jkh.setGst(gst);
				jkh.setJumlahKeseluruhan(jumlahKeseluruhan);
				//JADUAL KADAR HARGA
				if (getParam("updateLampiranJadualKadarHargaReload").trim().length() > 0) {
					jkh.setFileJKH(getParam("updateLampiranJadualKadarHargaReload"));
				}	
				jkh.setPenyedia(penyedia);
				jkh.setCatatanPenyedia(getParam("catatanPenyedia"));
			}			
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + jkh.getKuartersSenggara().getId() + "'");
			agihanTugasLama.setFlagAktif("T");
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(jkh.getKuartersSenggara());
			agihanTugas.setPegawaiAgihan(penyedia);			
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(jkh.getCatatanPenyedia());
			agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyemakSenggara")));
			agihanTugas.setStatus((Status) mp.find(Status.class, "1426130691705")); //SEMAKAN JKH
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);				
			
			jkh.getKuartersSenggara().setTugasan(agihanTugas);
			jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "1426130691705")); //SEMAKAN JKH
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparRekod();
	}
	/** END PENYEDIAAN JKH */
	
	/** START SEMAKAN PENYEDIAAN JKH */
	@Command("doChangeKeputusanSemakanJKH")
	public String doChangeKeputusanSemakanJKH() throws Exception {
		context.put("idKeputusan", getParam("idKeputusan"));
		context.put("catatanPenyemak", getParam("catatanPenyemak"));
		
		return paparRekod();
	}
	
	@Command("simpanSemakanPenyediaanJKH")
	public String simpanSemakanPenyediaanJKH() throws Exception {
		try {
			mp = new MyPersistence();
			Users penyemak = (Users) mp.find(Users.class, userId);
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			if (jkh != null) {
				if (getParam("idKeputusan").equals("2")) {
					jkh.setCatatanPenyedia(null);
					jkh.setFlagKeputusanPenyemak("T");
				} else {
					jkh.setFlagKeputusanPenyemak("Y");
				}
				jkh.setTarikhSemakan(new Date());
				jkh.setPenyemak(penyemak);
				jkh.setCatatanPenyemak(getParam("catatanPenyemak"));
			}			
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + jkh.getKuartersSenggara().getId() + "'");
			agihanTugasLama.setFlagAktif("T");
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(jkh.getKuartersSenggara());
			agihanTugas.setPegawaiAgihan(penyemak);			
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(jkh.getCatatanPenyemak());
			if (getParam("idKeputusan").equals("1")) {
				agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPelulusSenggara")));
				agihanTugas.setStatus((Status) mp.find(Status.class, "1426130691708")); //PENGESYORAN JKH
			} else {
				agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
				agihanTugas.setStatus((Status) mp.find(Status.class, "1427773516431")); //PINDAAN PENYEDIAAN JKH
			}			
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);				
			
			jkh.getKuartersSenggara().setTugasan(agihanTugas);
			if (getParam("idKeputusan").equals("1")) {
				jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "1426130691708")); //PENGESYORAN JKH
			} else {
				jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "1427773516431")); //PINDAAN PENYEDIAAN JKH
			}			
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparRekod();
	}
	/** END SEMAKAN PENYEDIAAN JKH */
	
	/** START PENGESYORAN PENYEDIAAN JKH */
	@Command("doChangeKeputusanPengesyoranJKH")
	public String doChangeKeputusanPengesyoranJKH() throws Exception {
		context.put("idKeputusan", getParam("idKeputusan"));
		context.put("catatanPelulus", getParam("catatanPelulus"));
		
		return paparRekod();
	}
	
	@Command("simpanPengesyoranPenyediaanJKH")
	public String simpanPengesyoranPenyediaanJKH() throws Exception {
		try {
			mp = new MyPersistence();
			Users pelulus = (Users) mp.find(Users.class, userId);
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			if (jkh != null) {
				if (getParam("idKeputusan").equals("2")) {
					jkh.setCatatanPenyedia(null);
					jkh.setCatatanPenyemak(null);
					jkh.setFlagKeputusanPelulus("T");
				} else {
					jkh.setFlagKeputusanPelulus("Y");
				}
				jkh.setTarikhKelulusan(new Date());
				jkh.setPelulus(pelulus);
				jkh.setCatatanPelulus(getParam("catatanPelulus"));
			}			
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + jkh.getKuartersSenggara().getId() + "'");
			agihanTugasLama.setFlagAktif("T");
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(jkh.getKuartersSenggara());
			agihanTugas.setPegawaiAgihan(pelulus);			
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(jkh.getCatatanPelulus());
			if (getParam("idKeputusan").equals("1")) {
				agihanTugas.setStatus((Status) mp.find(Status.class, "1426130691711")); //PENYEDIAAN INDEN KERJA
			} else {
				agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
				agihanTugas.setStatus((Status) mp.find(Status.class, "1427773516431")); //PINDAAN PENYEDIAAN JKH
			}			
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);				
			
			jkh.getKuartersSenggara().setTugasan(agihanTugas);
			if (getParam("idKeputusan").equals("1")) {
				jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "1426130691711")); //PENYEDIAAN INDEN KERJA
			} else {
				jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "1427773516431")); //PINDAAN PENYEDIAAN JKH
			}			
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparRekod();
	}
	/** END PENGESYORAN PENYEDIAAN JKH */
	
	/** START SERAHAN KUNCI */	
	@Command("simpanSerahanKunci")
	public String simpanSerahanKunci() throws Exception {		
		
		try {
			mp = new MyPersistence();
			MtnKuarters kuarters = (MtnKuarters) mp.find(MtnKuarters.class, getParam("idLaporan"));
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
						
			kuarters.setTarikhSerahKunci(getDate("tarikhSerahKunci"));
			kuarters.setCatatanSerahanKunci(get("catatanSerahanKunci"));
			kuarters.setTugasan(null);
			kuarters.setStatus((Status) mp.find(Status.class, "1427855971461")); //SELESAI
			
			//KEMASKINI STATUS KUARTERS
			kuarters.getKuarters().setStatusKuarters((StatusKuarters) mp.find(StatusKuarters.class, "03")); // SEDIA DIDUDUKI
			kuarters.getKuarters().setFlagAgihan(0);
			
			mp.commit();
		} catch (Exception ex) {
			System.out.println("GAGAL DAFTAR SERAHAN KUNCI : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
				
		return paparRekod();
	}
	/** END TERIMAAN KUNCI */
	
	/** START DROP DOWN  */
	@Command("findLokasi")
	public String findLokasi() throws Exception {
		String idKawasan = "0";
		if (getParam("findKawasan").trim().length() > 0)
			idKawasan = getParam("findKawasan");
		
		List<LokasiKuarters> list = dataUtil.getListLokasiKuartersByLokasiPermohonan(idKawasan);
		context.put("selectLokasi", list);

		return getPath() + "/findLokasi.vm";
	}
	
	@Command("findFasa")
	public String findFasa() throws Exception {
		String idKawasan = "0";
		if (getParam("findKawasan").trim().length() > 0)
			idKawasan = getParam("findKawasan");
		
		List<Fasa> list = null;
		if (idKawasan.equals("01")) {
			list = dataUtil.getListFasa();
		}
		context.put("selectFasa", list);

		return getPath() + "/findFasa.vm";
	}
	/** END DROP DOWN  */
	
	/** START DAFTAR KUARTERS KE SENGGARA  */
	public void daftarKeluarKuarters(KuaKuarters kuarters, Date tarikhKeluarKuarters, MyPersistence mp) {
		try {

			MtnKuarters laporan = new MtnKuarters();
			laporan.setKuarters(kuarters);
			if (tarikhKeluarKuarters != null) {
				laporan.setTarikhTerimaLaporan(tarikhKeluarKuarters);
			} else {
				laporan.setTarikhTerimaLaporan(new Date());
			}			
			laporan.setJenisLaporan("D"); //DAFTAR KELUAR KUARTERS
			laporan.setKeteranganLaporan("KUARTERS KOSONG");
			laporan.setStatus((Status) mp.find(Status.class, "1426130691699")); //BARU		
			mp.persist(laporan);

		} catch (Exception e) {
			System.out.println("RALAT DAFTAR KUARTERS KOSONG : " + e.getMessage());
		}		
	}
	/** END DAFTAR KUARTERS KE SENGGARA  */
}
package bph.modules.senggara;

import java.io.File;
import java.text.DecimalFormat;
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

import portal.module.entity.Users;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Status;
import bph.entities.senggara.MtnAgihanTugas;
import bph.entities.senggara.MtnDaftarKontraktor;
import bph.entities.senggara.MtnIndenKerja;
import bph.entities.senggara.MtnJKH;
import bph.entities.senggara.MtnKontraktorSenaraiHitam;
import bph.entities.senggara.MtnKuarters;
import bph.entities.senggara.MtnPemantauanKerja;
import bph.entities.senggara.MtnPenilaianKontraktor;
import bph.entities.senggara.MtnSeqInden;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class RekodIndenKerjaRecordModule extends LebahRecordTemplateModule<MtnIndenKerja>{

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
	public Class<MtnIndenKerja> getPersistenceClass() {
		// TODO Auto-generated method stub
		return MtnIndenKerja.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/senggara/rekodIndenKerja";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectLokasi", dataUtil.getListLokasiPermohonan());
			
		boolean showKontraktor = true;
		context.put("showKontraktor", showKontraktor);
		boolean showTugasan = true;
		context.put("showTugasan", showTugasan);
		boolean showStatus = true;
		context.put("showStatus", showStatus);
						
		context.put("flagSkrin", "RekodIndenKerjaRecordModule");
		
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
				
		this.setOrderBy("noInden");
		this.setOrderType("desc");
	}	
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void save(MtnIndenKerja r) throws Exception {
		r.setLokasi(db.find(LokasiPermohonan.class, getParam("idLokasi")));
		r.setVot("B-06-111-060100/240500");
		r.setKerja("Menjalankan pembaikan kecil kuarters");
		r.setTarikhIndenKerja(new Date());
		r.setJumlah(0D);
		r.setStatus(db.find(Status.class, "1426130691711")); //PENYEDIAAN INDEN KERJA		
	}
	
	@Override
	public void afterSave(MtnIndenKerja r) {
		int bil = 0;
		try {
			if (r != null) {
				db.begin();
				Calendar cal = new GregorianCalendar();
				cal.setTime(new Date());
				String idSeqInden = r.getLokasi().getId() + cal.get(Calendar.YEAR);
				MtnSeqInden seqInden = (MtnSeqInden) db.get("select x from MtnSeqInden x where x.id = '" + idSeqInden + "'");
				if (seqInden != null) {
					bil = seqInden.getBil() + 1;
					seqInden.setBil(bil);
				} else {
					bil = 1;
					seqInden = new MtnSeqInden();
					seqInden.setId(idSeqInden);
					seqInden.setLokasiPermohonan(r.getLokasi());
					seqInden.setTahun(cal.get(Calendar.YEAR));
					seqInden.setBil(bil);
					db.persist(seqInden);
				}
				String noInden = r.getLokasi().getAbbrev() + new DecimalFormat("00").format(bil) + "/" +  cal.get(Calendar.YEAR);
				r.setNoInden(noInden);
				db.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		context.put("selectedTab", "1");		
	}
	
	@Override
	public void getRelatedData(MtnIndenKerja r) {	
		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, r.getId());
			context.put("r", indenKerja);
			
			//SENARAI JKH DALAM PEMBAIKAN
			MtnJKH jkhDalamPembaikan = (MtnJKH) mp.get("select x from MtnJKH x where x.statusPembaikan = 'T' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			context.put("jkhDalamPembaikan", jkhDalamPembaikan);
			
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			context.put("listSenaraiJKH", listJKH);
			
			List<MtnPemantauanKerja> listLog = mp.list("select x from MtnPemantauanKerja x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			context.put("listSenaraiLog", listLog);
			
			List<Users> penyedia = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPenyediaSecondary() + ") OR u.role.description = '(SENGGARA) Penyedia' ORDER BY u.userName ASC");
			context.put("selectPenyediaSenggara", penyedia);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedTab", "1");
	}

	@Override
	public boolean delete(MtnIndenKerja r) throws Exception {
		return true;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("lokasi.id", new OperatorEqualTo(get("findLokasi")));
		map.put("vot", get("findNoVOT"));
		map.put("noInden", get("findNoInden"));	
		map.put("kerja", get("findKerja"));	
		
		return map;
	}

	/** START TAB **/
	@Command("getMaklumatInden")
	public String getMaklumatInden() throws Exception {
		
		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			context.put("r", indenKerja);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiJKH")
	public String getSenaraiJKH() throws Exception {

		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			context.put("r", indenKerja);
			
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			context.put("listSenaraiJKH", listJKH);
			
			context.remove("flagPaparMaklumatJadualKadarHarga");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatKontraktor")
	public String getMaklumatKontraktor() throws Exception {

		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			context.put("r", indenKerja);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatNotaMinta")
	public String getMaklumatNotaMinta() throws Exception {

		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			context.put("r", indenKerja);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getLogPemantauanKerja")
	public String getLogPemantauanKerja() throws Exception {

		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			context.put("r", indenKerja);
			
			//SENARAI JKH DALAM PEMBAIKAN
			MtnJKH jkhDalamPembaikan = (MtnJKH) mp.get("select x from MtnJKH x where x.statusPembaikan = 'T' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			context.put("jkhDalamPembaikan", jkhDalamPembaikan);
			
			List<MtnPemantauanKerja> listLog = mp.list("select x from MtnPemantauanKerja x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			context.put("listSenaraiLog", listLog);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START MAKLUMAT INDEN KERJA **/	
	@Command("deleteInden")
	public String deleteInden() throws Exception {
		
		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			
			mp.begin();
			for (int i = 0; i < listJKH.size(); i++) {
				listJKH.get(i).setIndenKerja(null);
			}
			mp.remove(indenKerja);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END MAKLUMAT INDEN KERJA **/
	
	/** START SENARAI JKH **/
	@Command("deleteSenaraiJKH")
	public String deleteSenaraiJKH() throws Exception {

		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			jkh.setIndenKerja(null);
			
			Double total = 0D;
			total = indenKerja.getJumlah() - jkh.getJumlahKeseluruhan();
			indenKerja.setJumlah(total);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getSenaraiJKH();
	}
	
	@Command("hantarPemilihanKontraktor")
	public String hantarPemilihanKontraktor() throws Exception {
		
		try {
			mp = new MyPersistence();	
			Users penyeliaInden = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(penyeliaInden);
			agihanTugas.setPegawaiTugasan(null);
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan("PEMILIHAN KONTRAKTOR REQUISITION BAGI INDEN KERJA");
			agihanTugas.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
			
			for (int i = 0; i < listJKH.size(); i++) {
				MtnKuarters kuarters = listJKH.get(i).getKuartersSenggara();
				if (kuarters != null) {
					//DEACTIVATE TUGASAN LAMA
					MtnAgihanTugas agihanTugasLamaKuarters = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
					if (agihanTugasLamaKuarters != null) {
						agihanTugasLamaKuarters.setFlagAktif("T");
					}			
					
					//DAFTAR AGIHAN TUGAS
					MtnAgihanTugas agihanTugasKuarters = new MtnAgihanTugas();
					agihanTugasKuarters.setKuarters(kuarters);
					agihanTugasKuarters.setPegawaiAgihan(penyeliaInden);
					agihanTugasKuarters.setPegawaiTugasan(null);
					agihanTugasKuarters.setTarikhTugasan(new Date());
					agihanTugasKuarters.setCatatan("PEMILIHAN KONTRAKTOR REQUISITION BAGI INDEN KERJA");
					agihanTugasKuarters.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
					agihanTugasKuarters.setFlagAktif("Y");
					mp.persist(agihanTugasKuarters);
					
					kuarters.setTugasan(agihanTugasKuarters);
					kuarters.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
				}
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END SENARAI JKH **/
	
	/** START PERLANTIKAN KONTRAKTOR **/
	@Command("gotoSenaraiPilihanKontraktor")
	public String gotoSenaraiPilihanKontraktor() throws Exception {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());

		try {
			mp = new MyPersistence();	
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnDaftarKontraktor> listKontraktor = mp.list("select x from MtnDaftarKontraktor x where x.kontraktor.kawasan.id = '"
					+ indenKerja.getLokasi().getId() + "' and x.kontraktor.id not in "
					+ "(select y.kontraktor.id from MtnKontraktorSenaraiHitam y where y.flagAktif = 'Y') and x.tahun = '" + cal.get(Calendar.YEAR) + "' and x.flagHadirCabutan = 'Y'"
					+ " order by x.turutan asc");
			context.put("listKontraktor", listKontraktor);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/pemilihanKontraktor/senaraiPilihanKontraktor.vm";
	}
	
	@Command("simpanLantikanKontraktor")
	public String simpanLantikanKontraktor() throws Exception {

		try {
			mp = new MyPersistence();			
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.find(MtnDaftarKontraktor.class, getParam("idDaftarKontraktor"));
			
			mp.begin();
			if (daftarKontraktor != null) {
				indenKerja.setKontraktor(daftarKontraktor.getKontraktor());
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatKontraktor();
	}
	
	@Command("selesaiLantikKontraktor")
	public String selesaiLantikKontraktor() throws Exception {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		
		try {
			mp = new MyPersistence();		
			Users pegawaiKewangan = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(pegawaiKewangan);
			agihanTugas.setPegawaiTugasan(null);
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan("KONTRAKTOR REQUISITION BAGI INDEN KERJA TELAH DIPILIH");
			agihanTugas.setStatus((Status) mp.find(Status.class, "17859229601059")); // CETAKAN INDEN KERJA / NOTA MINTA
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "17859229601059")); // CETAKAN INDEN KERJA / NOTA MINTA
			
			for (int i = 0; i < listJKH.size(); i++) {
				MtnKuarters kuarters = listJKH.get(i).getKuartersSenggara();
				if (kuarters != null) {
					//DEACTIVATE TUGASAN LAMA
					MtnAgihanTugas agihanTugasLamaKuarters = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
					if (agihanTugasLamaKuarters != null) {
						agihanTugasLamaKuarters.setFlagAktif("T");
					}			
					
					//DAFTAR AGIHAN TUGAS
					MtnAgihanTugas agihanTugasKuarters = new MtnAgihanTugas();
					agihanTugasKuarters.setKuarters(kuarters);
					agihanTugasKuarters.setPegawaiAgihan(pegawaiKewangan);
					agihanTugasKuarters.setPegawaiTugasan(null);
					agihanTugasKuarters.setTarikhTugasan(new Date());
					agihanTugasKuarters.setCatatan("KONTRAKTOR REQUISITION BAGI INDEN KERJA TELAH DIPILIH");
					agihanTugasKuarters.setStatus((Status) mp.find(Status.class, "17859229601059")); // CETAKAN INDEN KERJA / NOTA MINTA
					agihanTugasKuarters.setFlagAktif("Y");
					mp.persist(agihanTugasKuarters);
					
					kuarters.setTugasan(agihanTugasKuarters);
					kuarters.setStatus((Status) mp.find(Status.class, "17859229601059")); // CETAKAN INDEN KERJA / NOTA MINTA
				}
			}
			
			//UPDATE GILIRAN KONTRAKTOR YANG DIPILIH
			int noGiliranSemasa = 0;
			int noGiliran = 0;
			MtnDaftarKontraktor daftarKontraktor = (MtnDaftarKontraktor) mp.get("select x from MtnDaftarKontraktor x where x.kontraktor.id = '" +  indenKerja.getKontraktor().getId() + "' and x.tahun = '" + cal.get(Calendar.YEAR) + "'");
			noGiliranSemasa = daftarKontraktor.getTurutan();
			List<MtnDaftarKontraktor> listDaftarKontraktor = mp.list("select x from MtnDaftarKontraktor x where x.tahun = '"
					+ cal.get(Calendar.YEAR) + "' and x.kontraktor.kawasan.id = '" + daftarKontraktor.getKontraktor().getKawasan().getId() + "'"
					+ " and x.flagHadirCabutan = 'Y' order by x.turutan desc");
			if (listDaftarKontraktor.size() > 0) {
				noGiliran = listDaftarKontraktor.get(0).getTurutan();
				daftarKontraktor.setTurutan(noGiliran);
			}
			
			//REARRANGE SEQUENCE SENARAI KONTRAKTOR LAIN
			List<MtnDaftarKontraktor> listDaftarKontraktorLain = mp.list("select x from MtnDaftarKontraktor x where x.tahun = '"
					+ cal.get(Calendar.YEAR) + "' and x.kontraktor.kawasan.id = '" + daftarKontraktor.getKontraktor().getKawasan().getId() + "'"
					+ " and x.flagHadirCabutan = 'Y' and x.id != '" + daftarKontraktor.getId() + "' and x.turutan > '" + noGiliranSemasa + "' order by x.turutan asc");
			for (int i = 0; i < listDaftarKontraktorLain.size(); i++) {
				listDaftarKontraktorLain.get(i).setTurutan(i + noGiliranSemasa); 
			}
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END PERLANTIKAN KONTRAKTOR **/
	
	/** START MAKLUMAT NOTA MINTA **/
	@Command("updateNotaMinta")
	public String updateNotaMinta() throws Exception {

		try {
			mp = new MyPersistence();			
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));			
			
			mp.begin();
			if (indenKerja != null) {
				indenKerja.setTarikhTTPengesyor(getDate("tarikhTTPengesyor"));
				indenKerja.setTarikhTTKPSU(getDate("tarikhTTKPSU"));
				indenKerja.setTarikhTTKew(getDate("tarikhTTKew"));
				indenKerja.setTarikhTTSUB(getDate("tarikhTTSUB"));
				
				indenKerja.setTarikhSST(getDate("tarikhSST"));
				Calendar cal = new GregorianCalendar();
				cal.setTime(getDate("tarikhSST"));
				cal.add(Calendar.DATE, 21);
				indenKerja.setTarikhAkhirSiapKerja(cal.getTime());
				indenKerja.setNoRujukanSST(getParam("noRujukanSST"));
				indenKerja.setTarikhTTSST(getDate("tarikhTTSST"));
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatNotaMinta();
	}
	
	@Command("hantarTugasan")
	public String hantarTugasan() throws Exception {	
		try {
			mp = new MyPersistence();				
			List<Users> penyemak = mp.list("SELECT u FROM Users u WHERE u.id IN (" + UtilSenggara.getPenyemakSecondary() + ") OR u.role.description = '(SENGGARA) Penyemak' ORDER BY u.userName ASC");
			context.put("selectPenyemakSenggara", penyemak);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatNotaMinta();
	}
	
	@Command("penukaranKontraktor")
	public String penukaranKontraktor() throws Exception {
		
		return getMaklumatNotaMinta();
	}
	
	@Command("simpanPenukaranKontraktor")
	public String simpanPenukaranKontraktor() throws Exception {

		try {
			mp = new MyPersistence();	
			Users penyeliaInden = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			
			mp.begin();
			//BLACKLIST KONTRAKTOR
			if (!getParam("idSebabPenukaran").equals("3")) {
				MtnKontraktorSenaraiHitam senaraiHitam = new MtnKontraktorSenaraiHitam();
				senaraiHitam.setKontraktor(indenKerja.getKontraktor());
				senaraiHitam.setTarikhMula(new Date());
				Calendar cal = new GregorianCalendar();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR, 2);
				senaraiHitam.setTarikhTamat(cal.getTime());
				senaraiHitam.setFlagAktif("Y");
				if (getParam("idSebabPenukaran").equals("1")) {
					senaraiHitam.setSebab("KONTRAKTOR MENOLAK TUGAS");
				} else if (getParam("idSebabPenukaran").equals("2")) {
					senaraiHitam.setSebab("KONTRAKTOR TIDAK DAPAT DIHUBUNGI");
				}				
				mp.persist(senaraiHitam);
			}

			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(penyeliaInden);
			agihanTugas.setPegawaiTugasan(null);
			agihanTugas.setTarikhTugasan(new Date());
			if (getParam("idSebabPenukaran").equals("1")) {
				agihanTugas.setCatatan("KONTRAKTOR MENOLAK TUGAS");
			} else if (getParam("idSebabPenukaran").equals("2")) {
				agihanTugas.setCatatan("KONTRAKTOR TIDAK DAPAT DIHUBUNGI");
			} else if (getParam("idSebabPenukaran").equals("3")) {
				agihanTugas.setCatatan("SIJIL SOKONGAN KONTRAKTOR YANG DIPILIH TELAH TAMAT TEMPOH");
			}			
			agihanTugas.setStatus((Status) mp.find(Status.class, "17859229601059")); // CETAKAN INDEN KERJA / NOTA MINTA
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
			indenKerja.setKontraktor(null);
			indenKerja.setTarikhTTPengesyor(null);
			indenKerja.setTarikhTTKPSU(null);
			indenKerja.setTarikhTTKew(null);
			indenKerja.setTarikhTTSUB(null);			
			indenKerja.setTarikhSST(null);
			indenKerja.setNoRujukanSST(null);
			indenKerja.setTarikhTTSST(null);
			
			for (int i = 0; i < listJKH.size(); i++) {
				MtnKuarters kuarters = listJKH.get(i).getKuartersSenggara();
				if (kuarters != null) {
					//DEACTIVATE TUGASAN LAMA
					MtnAgihanTugas agihanTugasLamaKuarters = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
					if (agihanTugasLamaKuarters != null) {
						agihanTugasLamaKuarters.setFlagAktif("T");
					}			
					
					//DAFTAR AGIHAN TUGAS
					MtnAgihanTugas agihanTugasKuarters = new MtnAgihanTugas();
					agihanTugasKuarters.setKuarters(kuarters);
					agihanTugasKuarters.setPegawaiAgihan(penyeliaInden);
					agihanTugasKuarters.setPegawaiTugasan(null);
					agihanTugasKuarters.setTarikhTugasan(new Date());
					if (getParam("idSebabPenukaran").equals("1")) {
						agihanTugasKuarters.setCatatan("KONTRAKTOR MENOLAK TUGAS");
					} else if (getParam("idSebabPenukaran").equals("2")) {
						agihanTugasKuarters.setCatatan("KONTRAKTOR TIDAK DAPAT DIHUBUNGI");
					} else if (getParam("idSebabPenukaran").equals("3")) {
						agihanTugasKuarters.setCatatan("SIJIL SOKONGAN KONTRAKTOR YANG DIPILIH TELAH TAMAT TEMPOH");
					}
					agihanTugasKuarters.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
					agihanTugasKuarters.setFlagAktif("Y");
					mp.persist(agihanTugasKuarters);
					
					kuarters.setTugasan(agihanTugasKuarters);
					kuarters.setStatus((Status) mp.find(Status.class, "17859229601035")); // PEMILIHAN KONTRAKTOR
				}
			}
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	
	@Command("simpanHantarTugasan")
	public String simpanHantarTugasan() throws Exception {

		try {
			mp = new MyPersistence();	
			Users penyeliaInden = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(penyeliaInden);
			agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyemakSenggara")));
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(getParam("catatan"));			
			agihanTugas.setStatus((Status) mp.find(Status.class, "17859229601072")); // AGIHAN TUGAS PEMANTAUAN KERJA
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "17859229601072")); // AGIHAN TUGAS PEMANTAUAN KERJA
			
			for (int i = 0; i < listJKH.size(); i++) {
				MtnKuarters kuarters = listJKH.get(i).getKuartersSenggara();
				if (kuarters != null) {
					//DEACTIVATE TUGASAN LAMA
					MtnAgihanTugas agihanTugasLamaKuarters = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
					if (agihanTugasLamaKuarters != null) {
						agihanTugasLamaKuarters.setFlagAktif("T");
					}			
					
					//DAFTAR AGIHAN TUGAS
					MtnAgihanTugas agihanTugasKuarters = new MtnAgihanTugas();
					agihanTugasKuarters.setKuarters(kuarters);
					agihanTugasKuarters.setPegawaiAgihan(penyeliaInden);
					agihanTugasKuarters.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyemakSenggara")));
					agihanTugasKuarters.setTarikhTugasan(new Date());
					agihanTugasKuarters.setCatatan(getParam("catatan"));		
					agihanTugasKuarters.setStatus((Status) mp.find(Status.class, "17859229601072")); // AGIHAN TUGAS PEMANTAUAN KERJA
					agihanTugasKuarters.setFlagAktif("Y");
					mp.persist(agihanTugasKuarters);
					
					kuarters.setTugasan(agihanTugasKuarters);
					kuarters.setStatus((Status) mp.find(Status.class, "17859229601072")); // AGIHAN TUGAS PEMANTAUAN KERJA
				}
			}
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END MAKLUMAT NOTA MINTA **/
	
	/** START AGIHAN TUGAS PEMANTAUAN KERJA **/
	@Command("simpanAgihanTugas")
	public String simpanAgihanTugas() throws Exception {

		try {
			mp = new MyPersistence();	
			Users penyemak = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "'");
			
			mp.begin();
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(penyemak);
			agihanTugas.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(getParam("catatanTugasan"));			
			agihanTugas.setStatus((Status) mp.find(Status.class, "17859229601095")); // PEMANTAUAN KERJA
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "17859229601095")); // PEMANTAUAN KERJA
			indenKerja.setPenyelia((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
			
			for (int i = 0; i < listJKH.size(); i++) {
				MtnKuarters kuarters = listJKH.get(i).getKuartersSenggara();
				if (kuarters != null) {
					//DEACTIVATE TUGASAN LAMA
					MtnAgihanTugas agihanTugasLamaKuarters = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
					if (agihanTugasLamaKuarters != null) {
						agihanTugasLamaKuarters.setFlagAktif("T");
					}			
					
					//DAFTAR AGIHAN TUGAS
					MtnAgihanTugas agihanTugasKuarters = new MtnAgihanTugas();
					agihanTugasKuarters.setKuarters(kuarters);
					agihanTugasKuarters.setPegawaiAgihan(penyemak);
					agihanTugasKuarters.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
					agihanTugasKuarters.setTarikhTugasan(new Date());
					agihanTugasKuarters.setCatatan(getParam("catatanTugasan"));		
					agihanTugasKuarters.setStatus((Status) mp.find(Status.class, "17859229601095")); // PEMANTAUAN KERJA
					agihanTugasKuarters.setFlagAktif("Y");
					mp.persist(agihanTugasKuarters);
					
					kuarters.setTugasan(agihanTugasKuarters);
					kuarters.setStatus((Status) mp.find(Status.class, "17859229601095")); // PEMANTAUAN KERJA
				}
			}
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatInden();
	}
	/** END AGIHAN TUGAS PEMANTAUAN KERJA **/
	
	/** START LOG PEMANTAUAN KERJA **/
	@Command("addLogPemantauanKerja")
	public String addLogPemantauanKerja() {

		try {
			mp = new MyPersistence();
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, get("idIndenKerja"));
			context.put("indenKerja", indenKerja);
			
			context.remove("rekod");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/pemantauanKerja/popupLog.vm";
	}
	
	@Command("kemaskiniLogPemantauanKerja")
	public String kemaskiniLogPemantauanKerja() {

		try {
			mp = new MyPersistence();
			MtnPemantauanKerja pemantauanKerja = (MtnPemantauanKerja) mp.find(MtnPemantauanKerja.class, getParam("idLog"));
			context.put("rekod", pemantauanKerja);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/pemantauanKerja/popupLog.vm";
	}
	
	@Command("simpanKemaskiniLogPemantauanKerja")
	public String simpanKemaskiniLogPemantauanKerja() throws Exception {
		boolean addRecord = false;

		try {
			mp = new MyPersistence();
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			MtnPemantauanKerja pemantauanKerja = (MtnPemantauanKerja) mp.find(MtnPemantauanKerja.class, getParam("idLog"));
			
			mp.begin();
			if (pemantauanKerja == null) {
				pemantauanKerja = new MtnPemantauanKerja();
				pemantauanKerja.setDaftarOleh(penyedia);
				pemantauanKerja.setTarikhMasuk(new Date());
				addRecord = true;
			} else {
				pemantauanKerja.setKemaskiniOleh(penyedia);
				pemantauanKerja.setTarikhKemaskini(new Date());
			}
			pemantauanKerja.setIndenKerja(indenKerja);
			pemantauanKerja.setTarikh(getDate("tarikh"));
			pemantauanKerja.setCatatan(getParam("catatan"));
			pemantauanKerja.setPeratusanKerja(getParamAsDouble("peratusanKerja"));
			
			if (addRecord) {
				mp.persist(pemantauanKerja);
			}
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return getLogPemantauanKerja();
	}
	
	@Command("deleteLogPemantauanKerja")
	public String deleteLogPemantauanKerja() throws Exception {

		try {
			mp = new MyPersistence();
			MtnPemantauanKerja pemantauanKerja = (MtnPemantauanKerja) mp.find(MtnPemantauanKerja.class, getParam("idLog"));
			
			if (pemantauanKerja != null) {
				mp.begin();
				mp.remove(pemantauanKerja);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return getLogPemantauanKerja();
	}	
	
	@Command("tamatPemantauanKerja")
	public String tamatPemantauanKerja() throws Exception {

		try {
			mp = new MyPersistence();	
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			List<MtnJKH> listJKH = mp.list("select x from MtnJKH x where x.indenKerja.id = '" + indenKerja.getId() + "' and x.statusPembaikan = 'T'");
			
			mp.begin();
			MtnPenilaianKontraktor penilaianKontraktor = new MtnPenilaianKontraktor();
			mp.persist(penilaianKontraktor);
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(penyedia);
			agihanTugas.setPegawaiTugasan(penyedia);
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan("PENILAIAN KONTRAKTOR");			
			agihanTugas.setStatus((Status) mp.find(Status.class, "17859229601108")); // PENILAIAN KONTRAKTOR
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "17859229601108")); // PENILAIAN KONTRAKTOR
			indenKerja.setPenilaian(penilaianKontraktor);
			
			for (int i = 0; i < listJKH.size(); i++) {
				MtnKuarters kuarters = listJKH.get(i).getKuartersSenggara();
				if (kuarters != null) {
					
					// TODO BAGI KUARTERS YANG TERBENGKALAI
//					//DEACTIVATE TUGASAN LAMA
//					MtnAgihanTugas agihanTugasLamaKuarters = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + kuarters.getId() + "'");
//					if (agihanTugasLamaKuarters != null) {
//						agihanTugasLamaKuarters.setFlagAktif("T");
//					}			
//					
//					//DAFTAR AGIHAN TUGAS
//					MtnAgihanTugas agihanTugasKuarters = new MtnAgihanTugas();
//					agihanTugasKuarters.setKuarters(kuarters);
//					agihanTugasKuarters.setPegawaiAgihan(penyemak);
//					agihanTugasKuarters.setPegawaiTugasan((Users) mp.find(Users.class, getParam("idPenyediaSenggara")));
//					agihanTugasKuarters.setTarikhTugasan(new Date());
//					agihanTugasKuarters.setCatatan(getParam("catatanTugasan"));		
//					agihanTugasKuarters.setStatus((Status) mp.find(Status.class, "17859229601095")); // PEMANTAUAN KERJA
//					agihanTugasKuarters.setFlagAktif("Y");
//					mp.persist(agihanTugasKuarters);
//					
//					kuarters.setTugasan(agihanTugasKuarters);
//					kuarters.setStatus((Status) mp.find(Status.class, "17859229601095")); // PEMANTAUAN KERJA
				}
			}			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END LOG PEMANTAUAN KERJA **/
	
	/** START MAKLUMAT TERPERINCI JADUAL KADAR HARGA **/
	@Command("paparMaklumatJadualKadarHarga")
	public String paparMaklumatJadualKadarHarga() throws Exception {

		try {
			mp = new MyPersistence();
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, get("idJKH"));
			context.put("jkh", jkh);
			
			context.put("flagPaparMaklumatJadualKadarHarga", "Y");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}
	
	@Command("daftarSelesaiPembaikan")
	public String daftarSelesaiPembaikan() throws Exception {
		return paparMaklumatJadualKadarHarga();
	}
	
	@Command("daftarTamatPembaikan")
	public String daftarTamatPembaikan() throws Exception {
		return paparMaklumatJadualKadarHarga();
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadLampiranLaporanPembaikan")
	public String uploadLampiranLaporanPembaikan() throws Exception {
		String idJKH = getParam("idJKH");
			
		String uploadDir = "senggara/laporanPembaikan/";
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
			context.put("lampiranLaporanPembaikanReload", imgName);
		}
		return getPath() + "/pemantauanKerja/maklumatJadualKadarHarga/uploadLampiranLaporanPembaikan.vm";
	}

	@Command("refreshLampiranLaporanPembaikan")
	public String refreshLampiranLaporanPembaikan() throws Exception {
		context.put("updateLampiranLaporanPembaikanReload", getParam("updateLampiranLaporanPembaikanReload"));
		return getPath() + "/pemantauanKerja/maklumatJadualKadarHarga/updateLampiranLaporanPembaikan.vm";
	}
	
	@Command("simpanSelesaiPembaikan")
	public String simpanSelesaiPembaikan() throws Exception {
		
		try {
			mp = new MyPersistence();
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			if (jkh != null) {
				jkh.setTarikhSelesaiPembaikan(getDate("tarikhSelesaiPembaikan"));				
				//LAPORAN PEMBAIKAN
				if (getParam("updateLampiranLaporanPembaikanReload").trim().length() > 0) {
					jkh.setFileLaporanPembaikan(getParam("updateLampiranLaporanPembaikanReload"));
				}	
				jkh.setCatatanPembaikan(getParam("catatanSelesaiPembaikan"));
				jkh.setStatusPembaikan("Y");
			}			
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + jkh.getKuartersSenggara().getId() + "'");
			agihanTugasLama.setFlagAktif("T");
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setKuarters(jkh.getKuartersSenggara());
			agihanTugas.setPegawaiAgihan(penyedia);			
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan(jkh.getCatatanPembaikan());
			agihanTugas.setPegawaiTugasan(null);
			agihanTugas.setStatus((Status) mp.find(Status.class, "144160488701579")); //SERAHAN KUNCI
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);				
			
			jkh.getKuartersSenggara().setTugasan(agihanTugas);
			jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "144160488701579")); //SERAHAN KUNCI
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparMaklumatJadualKadarHarga();
	}
	
	@Command("simpanTamatPembaikan")
	public String simpanTamatPembaikan() throws Exception {
		
		try {
			mp = new MyPersistence();
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			if (jkh != null) {
				jkh.setTarikhSelesaiPembaikan(getDate("tarikhSelesaiPembaikan"));				
				//LAPORAN PEMBAIKAN
				if (getParam("updateLampiranLaporanPembaikanReload").trim().length() > 0) {
					jkh.setFileLaporanPembaikan(getParam("updateLampiranLaporanPembaikanReload"));
				}	
				jkh.setCatatanPembaikan(getParam("catatanSelesaiPembaikan"));
				jkh.setStatusPembaikan("B");
			}			
			
			//KENE CEK KALAU TERBENGKALAI DIA BUAT APA
			
//			//DEACTIVATE TUGASAN LAMA
//			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.kuarters.id = '" + jkh.getKuartersSenggara().getId() + "'");
//			agihanTugasLama.setFlagAktif("T");
//			
//			//DAFTAR AGIHAN TUGAS
//			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
//			agihanTugas.setKuarters(jkh.getKuartersSenggara());
//			agihanTugas.setPegawaiAgihan(penyedia);			
//			agihanTugas.setTarikhTugasan(new Date());
//			agihanTugas.setCatatan(jkh.getCatatanPembaikan());
//			agihanTugas.setPegawaiTugasan(null);
//			agihanTugas.setStatus((Status) mp.find(Status.class, "144160488701579")); //SERAHAN KUNCI
//			agihanTugas.setFlagAktif("Y");
//			mp.persist(agihanTugas);				
//			
//			jkh.getKuartersSenggara().setTugasan(agihanTugas);
//			jkh.getKuartersSenggara().setStatus((Status) mp.find(Status.class, "144160488701579")); //SERAHAN KUNCI
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		} 
		
		return paparMaklumatJadualKadarHarga();
	}
	/** END MAKLUMAT TERPERINCI JADUAL KADAR HARGA **/

	/** START PENILAIAN KONTRAKTOR **/	
	@Command("savePenilaianKontraktor")
	public String savePenilaianKontraktor() throws Exception {
		int mataKeseluruhan = 0;
		try {
			mp = new MyPersistence();			
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			MtnPenilaianKontraktor penilaian = indenKerja.getPenilaian();
			
			mp.begin();
			penilaian.setKekemasanKerja(getParamAsInteger("kekemasanKerja"));
			penilaian.setPelaksanaanKerja(getParamAsInteger("pelaksanaanKerja"));
			penilaian.setPematuhanSkop(getParamAsInteger("pematuhanSkop"));
			penilaian.setKebersihan(getParamAsInteger("kebersihan"));
			penilaian.setInisiatif(getParamAsInteger("inisiatif"));
			penilaian.setBilanganPekerja(getParamAsInteger("bilanganPekerja"));
			penilaian.setPeralatanKerja(getParamAsInteger("peralatanKerja"));
			penilaian.setTempohKerja(getParamAsInteger("tempohKerja"));
			penilaian.setPengurusan(getParamAsInteger("pengurusan"));
			mataKeseluruhan = getParamAsInteger("kekemasanKerja") + getParamAsInteger("pelaksanaanKerja") + getParamAsInteger("pematuhanSkop")
					+ getParamAsInteger("kebersihan") + getParamAsInteger("inisiatif") + getParamAsInteger("bilanganPekerja")
					+ getParamAsInteger("peralatanKerja") + getParamAsInteger("tempohKerja") + getParamAsInteger("pengurusan");
			penilaian.setMataKeseluruhan(mataKeseluruhan);
			if (mataKeseluruhan >= 85) {
				penilaian.setGredPenilaian("A");
			} else if (mataKeseluruhan >= 70 && mataKeseluruhan < 85) {
				penilaian.setGredPenilaian("B");
			} else if (mataKeseluruhan >= 50 && mataKeseluruhan < 70) {
				penilaian.setGredPenilaian("C");
			} else {
				penilaian.setGredPenilaian("D");
			}
			
			mp.commit();
			
			context.put("r", indenKerja);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("selesaiPenilaianKontraktor")
	public String selesaiPenilaianKontraktor() throws Exception {

		try {
			mp = new MyPersistence();	
			Users penyedia = (Users) mp.find(Users.class, userId);
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			MtnPenilaianKontraktor penilaian = indenKerja.getPenilaian();
			
			mp.begin();
			
			//BLACKLIST KONTRAKTOR
			if (penilaian.getMataKeseluruhan() < 70) {
				MtnKontraktorSenaraiHitam senaraiHitam = new MtnKontraktorSenaraiHitam();
				senaraiHitam.setKontraktor(indenKerja.getKontraktor());
				senaraiHitam.setTarikhMula(new Date());
				Calendar cal = new GregorianCalendar();
				cal.setTime(new Date());
				cal.add(Calendar.YEAR, 2);
				senaraiHitam.setTarikhTamat(cal.getTime());
				senaraiHitam.setFlagAktif("Y");
				senaraiHitam.setSebab("GRED PENILAIAN KONTRAKTOR YANG DITERIMA ADALAH RENDAH.");		
				senaraiHitam.setGredPrestasi(penilaian.getGredPenilaian());
				mp.persist(senaraiHitam);
			}
			
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			//DAFTAR AGIHAN TUGAS
			MtnAgihanTugas agihanTugas = new MtnAgihanTugas();
			agihanTugas.setIndenKerja(indenKerja);
			agihanTugas.setPegawaiAgihan(penyedia);
			agihanTugas.setPegawaiTugasan(penyedia);
			agihanTugas.setTarikhTugasan(new Date());
			agihanTugas.setCatatan("PROSES BAYARAN");			
			agihanTugas.setStatus((Status) mp.find(Status.class, "144160488701580")); // PROSES BAYARAN
			agihanTugas.setFlagAktif("Y");
			mp.persist(agihanTugas);
			
			indenKerja.setTugasan(agihanTugas);
			indenKerja.setStatus((Status) mp.find(Status.class, "144160488701580")); // PROSES BAYARAN
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END PENILAIAN KONTRAKTOR **/
	
	/** START PROSES BAYARAN **/
	@Command("updateProsesBayaran")
	public String updateProsesBayaran() throws Exception {

		try {
			mp = new MyPersistence();			
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));			
			
			mp.begin();
			if (indenKerja != null) {
				indenKerja.setTarikhTuntutanBayaran(getDate("tarikhTuntutanBayaran"));
				indenKerja.setTarikhHantarKewangan(getDate("tarikhHantarKewangan"));
				indenKerja.setTarikhTerimaKunci(getDate("tarikhTerimaKunci"));
				
				indenKerja.setTarikhPerakuanSiapKerja(getDate("tarikhPerakuanSiapKerja"));
				if (getDate("tarikhPerakuanSiapKerja") != null) {
					Calendar cal = new GregorianCalendar();
					cal.setTime(getDate("tarikhPerakuanSiapKerja"));
					cal.add(Calendar.MONTH, 6);
					cal.add(Calendar.DATE, -1);
					indenKerja.setTarikhAkhirWaranti(cal.getTime());
				}
			}
			mp.commit();
			
			context.put("r", indenKerja);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("selesaiProsesBayaran")
	public String selesaiProsesBayaran() throws Exception {

		try {
			mp = new MyPersistence();	
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			
			mp.begin();
						
			//DEACTIVATE TUGASAN LAMA
			MtnAgihanTugas agihanTugasLama = (MtnAgihanTugas) mp.get("select x from MtnAgihanTugas x where x.flagAktif = 'Y' and x.indenKerja.id = '" + indenKerja.getId() + "'");
			if (agihanTugasLama != null) {
				agihanTugasLama.setFlagAktif("T");
			}			
			
			indenKerja.setTugasan(null);
			indenKerja.setStatus((Status) mp.find(Status.class, "1427855971461")); //SELESAI
			
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return listPage();
	}
	/** END PROSES BAYARAN **/
}

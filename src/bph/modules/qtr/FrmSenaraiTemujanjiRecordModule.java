package bph.modules.qtr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.SebabBertukar;
import bph.entities.kod.Status;
import bph.entities.kod.StatusKuarters;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaTemujanji;
import bph.entities.qtr.KuaTemujanjiHistory;
import bph.mail.mailer.TemujanjiKuartersMailer;
import bph.modules.senggara.RekodKunciRecordModule;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmSenaraiTemujanjiRecordModule extends LebahRecordTemplateModule<KuaTemujanji> {
	
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
	public Class<KuaTemujanji> getPersistenceClass() {
		return KuaTemujanji.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/temujanji/senaraiTemujanji";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectSebabBertukar", dataUtil.getListSebabBertukar());
		context.put("selectKementerian", dataUtil.getListKementerian());
		
		context.put("util", util);
		context.remove("flagSejarahTemujanji");

		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
					
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		this.setHideDeleteButton(true);
	}

	private void addfilter() {
		this.addFilter("flagInternal = 0");
		this.setOrderBy("tarikhMulaTemujanji");
		this.setOrderType("asc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KuaTemujanji temujanji) throws Exception {
		temujanji.setPenghuni(db.find(KuaPenghuni.class, getParam("idPenghuni")));
		temujanji.setKuarters(db.find(KuaKuarters.class, getParam("idKuarters")));
		temujanji.setPemohon(db.find(Users.class, getParam("noPengenalan")));
		temujanji.setTarikhMohonTemujanji(getDate("tarikhMohonTemujanji"));
		temujanji.setTarikhTemujanji(getDate("tarikhTemujanji"));
		temujanji.setTarikhKeluarKuarters(getDate("tarikhTemujanji"));	
		temujanji.setTarikhTerimaKunci(getDate("tarikhTemujanji"));	
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		String tarikhTemujanji = getParam("tarikhTemujanji");
		String masaTemujanji = getParam("idMasaTemujanji");
		String dateTimeTemujanji = tarikhTemujanji + " " + masaTemujanji;
		Date dateTemujanji = formatter.parse(dateTimeTemujanji);
		temujanji.setTarikhMulaTemujanji(dateTemujanji);
		temujanji.setTarikhAkhirTemujanji(dateTemujanji);
		
		temujanji.setSebabBertukar(db.find(SebabBertukar.class, getParam("idSebabBertukar")));
		temujanji.setKeterangan(getParam("keterangan"));
		temujanji.setAlamat1(getParam("alamat1"));
		temujanji.setAlamat2(getParam("alamat2"));
		temujanji.setAlamat3(getParam("alamat3"));
		temujanji.setPoskod(getParam("poskod"));
		temujanji.setBandar(db.find(Bandar.class, getParam("idBandar")));
		temujanji.setEmel(getParam("emel"));
		temujanji.setNoTelefonBimbit(getParam("noTelefonBimbit"));
		
		if (getParam("idSebabBertukar").equals("01")) {
			temujanji.setAgensi(db.find(Agensi.class, getParam("idAgensi")));
			temujanji.setBahagian(getParam("bahagian"));
			temujanji.setAlamatPejabat1(getParam("alamatPejabat1"));
			temujanji.setAlamatPejabat2(getParam("alamatPejabat2"));
			temujanji.setAlamatPejabat3(getParam("alamatPejabat3"));
			temujanji.setPoskodPejabat(getParam("poskodPejabat"));
			temujanji.setBandarPejabat(db.find(Bandar.class, getParam("idBandarPejabat")));
			temujanji.setNoTelefonPejabat(getParam("noTelefonPejabat"));
		}
		
		temujanji.setFlagInternal(0);
		temujanji.setDaftarOleh(db.find(Users.class, userId));
	}
	
	@Override
	public void afterSave(KuaTemujanji temujanji) {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectPetugas", dataUtil.getListPenyediaKuarters());
		
		KuaTemujanji kuaTemujanji = null;
		mp = new MyPersistence();
		
		if (temujanji != null) {
			kuaTemujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, temujanji.getId());
			if (kuaTemujanji != null) {
				String alamat = "";
				if (kuaTemujanji.getAlamat1() != null && kuaTemujanji.getAlamat1().trim().length() > 0) alamat = kuaTemujanji.getAlamat1();
				if (kuaTemujanji.getAlamat2() != null && kuaTemujanji.getAlamat2().trim().length() > 0) alamat = alamat + ", " + kuaTemujanji.getAlamat2();
				if (kuaTemujanji.getAlamat3() != null && kuaTemujanji.getAlamat3().trim().length() > 0) alamat = alamat + ", " + kuaTemujanji.getAlamat3();
				if (kuaTemujanji.getPoskod() != null) alamat = alamat + ", " + kuaTemujanji.getPoskod();
				if (kuaTemujanji.getBandar() != null) alamat = alamat + " " + kuaTemujanji.getBandar().getKeterangan();
				if (kuaTemujanji.getBandar().getNegeri() != null) alamat = alamat + ", " + kuaTemujanji.getBandar().getNegeri().getKeterangan();
				TemujanjiKuartersMailer.get().hantarNotifikasiUnitKuartersDaftarTemujanji(kuaTemujanji.getPemohon().getUserName(), alamat, 
						kuaTemujanji.getPemohon().getId(), kuaTemujanji.getNoTelefonBimbit(), "etemujanji@bph.gov.my", 
						Util.getDateTime(kuaTemujanji.getTarikhTemujanji(), "dd/MM/yyyy"), 
						Util.getDateTime(kuaTemujanji.getTarikhMulaTemujanji(), "hh:mm a"));
			}
		}		
	}
	
	@Override
	public void getRelatedData(KuaTemujanji temujanji) {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectPetugas", dataUtil.getListPenyediaKuarters());
	}

	@Override
	public boolean delete(KuaTemujanji temujanji) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("pemohon.id", getParam("findNoPengenalan"));
		r.put("tarikhMohonTemujanji", getDate("findTarikhMohonTemujanji"));
		r.put("tarikhTemujanji", getDate("findTarikhTemujanji"));
		return r;
	}
	
	@Command("doChangeSebabBertukar")
	public String doChangeSebabBertukar() throws Exception {

		String idSebabBertukar = getParam("idSebabBertukar");
		context.put("idSebabBertukar", idSebabBertukar);

		return getPath() + "/maklumatPertukaranJabatan/start.vm";
	}

	@Command("getMaklumatPenghuni")
	public String getMaklumatPenghuni() throws Exception{		
		String noPengenalan = get("noPengenalan").replace("-", "").trim();	
		try {
			mp = new MyPersistence();
			KuaTemujanji temujanji = null;
			KuaPenghuni penghuni = (KuaPenghuni) mp.get("select x from KuaPenghuni x where x.pemohon.id = '" + noPengenalan + "' and x.tarikhKeluarKuarters is null");
			if (penghuni == null) {
				noPengenalan = "";
			} else {
				temujanji = (KuaTemujanji) mp.get("select x from KuaTemujanji x where x.penghuni.id = '" + penghuni.getId() + "'");
				if (temujanji != null) {
					noPengenalan = "";
				}				
			}
			context.put("penghuni", penghuni);
			context.put("temujanji", temujanji);
			context.put("noPengenalan", noPengenalan);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatPenghuni/maklumatPenghuni.vm";
	}
	
	@Command("doSimpanPetugas")
	public String doSimpanPetugas() throws Exception {
		String idTemujanji = getParam("idTemujanji");
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, idTemujanji);
			if (temujanji != null) {
				mp.begin();
				String idPetugas = getParam("idPetugas");
				Users petugas = (Users) mp.find(Users.class, idPetugas);
				if (petugas != null) {
					temujanji.setPetugas(petugas);
				} else {
					temujanji.setPetugas(null);
				}
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", temujanji);
		return getPath() + "/kegunaanPejabat/selectPetugas.vm";
	}
	
	@Command("doSimpanMaklumatTemujanjiPengurusan")
	public String doSimpanMaklumatTemujanjiPengurusan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idTemujanji = getParam("idTemujanji");
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, idTemujanji);
			if (temujanji != null) {
				mp.begin();
				temujanji.setTarikhTerimaKunci(getDate("tarikhTerimaKunci"));
				temujanji.setTarikhKeluarKuarters(getDate("tarikhKeluarKuarters"));
				temujanji.setTarikhSerahKunci(getDate("tarikhSerahKunci"));
				temujanji.setCatatan(getParam("catatan"));
				
				temujanji.setMemoDaripada(getParam("memoDaripada"));
				temujanji.setMemoKepada(getParam("memoKepada"));
				temujanji.setMemoPerkara(getParam("memoPerkara"));
				temujanji.setMemoRujukan(getParam("memoRujukan"));
				temujanji.setMemoSk(getParam("memoSk"));
				temujanji.setMemoTarikh(getDate("memoTarikh"));
				
				temujanji.setKemaskiniOleh(db.find(Users.class, userId));
				temujanji.setTarikhKemaskini(new Date());
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", temujanji);
		return getPath() + "/maklumatTemujanji/paparMaklumatTemujanji.vm";
	}
	
	@Command("doTidakHadirTemujanji")
	public String doTidakHadirTemujanji() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idTemujanji = getParam("idTemujanji");
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, idTemujanji);
			if (temujanji != null) {
				mp.begin();
				temujanji.setTarikhTerimaKunci(getDate("tarikhTerimaKunci"));
				temujanji.setTarikhKeluarKuarters(getDate("tarikhKeluarKuarters"));
				temujanji.setTarikhSerahKunci(getDate("tarikhSerahKunci"));				
				
				temujanji.setMemoDaripada(getParam("memoDaripada"));
				temujanji.setMemoKepada(getParam("memoKepada"));
				temujanji.setMemoPerkara(getParam("memoPerkara"));
				temujanji.setMemoRujukan(getParam("memoRujukan"));
				temujanji.setMemoSk(getParam("memoSk"));
				temujanji.setMemoTarikh(getDate("memoTarikh"));
				
				temujanji.setKehadiran(2);
				temujanji.setStatusTemujanji("3");
				if (getParam("catatan").equals("")) {
					temujanji.setCatatan("TEMUJANJI BATAL KERANA PENGHUNI TIDAK HADIR.");
				} else {
					temujanji.setCatatan(getParam("catatan"));
				}
				
				temujanji.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				temujanji.setTarikhKemaskini(new Date());
				mp.commit();
				
				simpanHistory(temujanji, temujanji.getStatusTemujanji(), mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", temujanji);
		return getPath() + "/maklumatTemujanji/paparMaklumatTemujanji.vm";
	}
	
	@Command("doBatalMaklumatTemujanjiPengurusan")
	public String doBatalMaklumatTemujanjiPengurusan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idTemujanji = getParam("idTemujanji");
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, idTemujanji);
			if (temujanji != null) {
				mp.begin();
				temujanji.setTarikhTerimaKunci(getDate("tarikhTerimaKunci"));
				temujanji.setTarikhKeluarKuarters(getDate("tarikhKeluarKuarters"));
				temujanji.setTarikhSerahKunci(getDate("tarikhSerahKunci"));				
				
				temujanji.setMemoDaripada(getParam("memoDaripada"));
				temujanji.setMemoKepada(getParam("memoKepada"));
				temujanji.setMemoPerkara(getParam("memoPerkara"));
				temujanji.setMemoRujukan(getParam("memoRujukan"));
				temujanji.setMemoSk(getParam("memoSk"));
				temujanji.setMemoTarikh(getDate("memoTarikh"));
				
				temujanji.setStatusTemujanji("3");
				temujanji.setCatatan(getParam("catatan"));
				
				temujanji.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				temujanji.setTarikhKemaskini(new Date());
				mp.commit();
				
				simpanHistory(temujanji, temujanji.getStatusTemujanji(), mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", temujanji);
		return getPath() + "/maklumatTemujanji/paparMaklumatTemujanji.vm";
	}
	
	@Command("doSelesaiMaklumatTemujanjiPengurusan")
	public String doSelesaiMaklumatTemujanjiPengurusan() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idTemujanji = getParam("idTemujanji");
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, idTemujanji);
			if (temujanji != null) {
				mp.begin();
				temujanji.setTarikhTerimaKunci(getDate("tarikhTerimaKunci"));
				temujanji.setTarikhKeluarKuarters(getDate("tarikhKeluarKuarters"));
				temujanji.setTarikhSerahKunci(getDate("tarikhSerahKunci"));				
				
				temujanji.setMemoDaripada(getParam("memoDaripada"));
				temujanji.setMemoKepada(getParam("memoKepada"));
				temujanji.setMemoPerkara(getParam("memoPerkara"));
				temujanji.setMemoRujukan(getParam("memoRujukan"));
				temujanji.setMemoSk(getParam("memoSk"));
				temujanji.setMemoTarikh(getDate("memoTarikh"));
				
				temujanji.setKehadiran(1);
				temujanji.setStatusTemujanji("0");
				temujanji.setCatatan(getParam("catatan"));
				
				temujanji.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				temujanji.setTarikhKemaskini(new Date());
				
				//UPDATE REKOD PENGHUNI
				if (temujanji.getPenghuni() != null) {
					temujanji.getPenghuni().setTarikhKeluarKuarters(temujanji.getTarikhKeluarKuarters());
					
					//UPDATE STATUS KUARTERS
					if (temujanji.getPenghuni().getKuarters() != null) {
						temujanji.getPenghuni().getKuarters().setStatusKuarters((StatusKuarters) mp.find(StatusKuarters.class, "02"));
						
						//HANTAR REKOD KE SENGGARA
						RekodKunciRecordModule senggara = new RekodKunciRecordModule();
						senggara.daftarKeluarKuarters(temujanji.getPenghuni().getKuarters(), temujanji.getTarikhKeluarKuarters(), mp);
						
					} else {
						System.out.println("TEMUJANJI : KUARTERS IS NULL - ID_TEMUJANJI = " + temujanji.getId());
					}
					
					//UPDATE STATUS PERMOHONAN
					if (temujanji.getPenghuni().getPermohonan() != null) {
						temujanji.getPenghuni().getPermohonan().setStatus((Status) mp.find(Status.class, "1431903258428"));
						temujanji.getPenghuni().getPermohonan().setTarikhKemaskini(new Date());
						
						/**SIMPAN MAKLUMAT SUBSIDIARI*/
						KewSubsidiari subsidiari = new KewSubsidiari();
						subsidiari.setIdFail(temujanji.getPenghuni().getPermohonan().getId());
						subsidiari.setJenisSubsidiari((KewJenisBayaran) mp.find(KewJenisBayaran.class, "01"));
						subsidiari.setJustifikasiPemohon("Permohonan semakan maklumat tunggakan dan lebihan sewa kuarters");
						subsidiari.setPemohon(temujanji.getPenghuni().getPermohonan().getPemohon());
						subsidiari.setStatus((Status) mp.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
						subsidiari.setTarikhPermohonan(new Date());
						subsidiari.setFlagSijilAkuanKeluar("Y");
						subsidiari.setFlagSijilAkuanMasuk("Y");
						mp.persist(subsidiari);
						
						/**SIMPAN MAKLUMAT TUNTUTAN DEPOSIT*/
						boolean addTuntutanDeposit = false;
						KewTuntutanDeposit tuntutanDeposit = null;
						KuaAkaun akaun = (KuaAkaun) mp.get("select x from KuaAkaun x where x.permohonan.id = '" + temujanji.getPenghuni().getPermohonan().getId() + "' and x.kodHasil.id = '72310'");
						if (akaun != null) {
							KewDeposit deposit = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '" + akaun.getId() + "' ");
							if (deposit != null) {
								if (deposit.getTuntutanDeposit() == null) {
									addTuntutanDeposit = true;
									tuntutanDeposit = new KewTuntutanDeposit();
								} else {
									tuntutanDeposit = deposit.getTuntutanDeposit();
								}
								tuntutanDeposit.setDeposit(deposit);
								tuntutanDeposit.setJenisTuntutan((KewJenisBayaran) mp.find(KewJenisBayaran.class, "01"));//KUARTERS
								tuntutanDeposit.setPenuntut(temujanji.getPenghuni().getPermohonan().getPemohon());
								tuntutanDeposit.setStatus((Status) mp.find(Status.class, "1436464445665"));
								tuntutanDeposit.setTarikhPermohonan(new Date());
								tuntutanDeposit.setIdDaftar((Users) mp.find(Users.class, userId));
								tuntutanDeposit.setTarikhDaftar(new Date());
								if (addTuntutanDeposit) {
									mp.persist(tuntutanDeposit);
								}
								deposit.setTuntutanDeposit(tuntutanDeposit);
							}
						}
					} else {
						System.out.println("TEMUJANJI : PERMOHONAN IS NULL - ID_TEMUJANJI = " + temujanji.getId());
					}
				} else {
					System.out.println("TEMUJANJI : PENGHUNI IS NULL - ID_TEMUJANJI = " + temujanji.getId());
				}
				mp.commit();
				
				simpanHistory(temujanji, temujanji.getStatusTemujanji(), mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", temujanji);
		return getPath() + "/maklumatTemujanji/paparMaklumatTemujanji.vm";
	}
	
	private static void simpanHistory(KuaTemujanji temujanji, String statusTemujanji, MyPersistence mp) {
		boolean addSejarah = false;
		KuaTemujanjiHistory sejarah = null;
		try {
			mp.begin();
			sejarah = (KuaTemujanjiHistory) mp.find(KuaTemujanjiHistory.class, temujanji.getId());
			if (sejarah == null) {
				sejarah = new KuaTemujanjiHistory();
				sejarah.setId(temujanji.getId());
				addSejarah = true;
			}
			sejarah.setAgihan(temujanji.getAgihan());
			sejarah.setPenghuni(temujanji.getPenghuni());
			sejarah.setKuarters(temujanji.getKuarters());
			sejarah.setPemohon(temujanji.getPemohon());
			sejarah.setTarikhMohonTemujanji(temujanji.getTarikhMohonTemujanji());
			sejarah.setTarikhTemujanji(temujanji.getTarikhTemujanji());
			sejarah.setTarikhMulaTemujanji(temujanji.getTarikhMulaTemujanji());
			sejarah.setTarikhAkhirTemujanji(temujanji.getTarikhAkhirTemujanji());
			sejarah.setSebabBertukar(temujanji.getSebabBertukar());
			sejarah.setKeterangan(temujanji.getKeterangan());
			sejarah.setAlamat1(temujanji.getAlamat1());
			sejarah.setAlamat2(temujanji.getAlamat2());
			sejarah.setAlamat3(temujanji.getAlamat3());
			sejarah.setPoskod(temujanji.getPoskod());
			sejarah.setBandar(temujanji.getBandar());			
			sejarah.setEmel(temujanji.getEmel());
			sejarah.setNoTelefonBimbit(temujanji.getNoTelefonBimbit());
			sejarah.setAlamatPejabat1(temujanji.getAlamatPejabat1());
			sejarah.setAlamatPejabat2(temujanji.getAlamatPejabat2());
			sejarah.setAlamatPejabat3(temujanji.getAlamatPejabat3());
			sejarah.setPoskodPejabat(temujanji.getPoskodPejabat());
			sejarah.setBandarPejabat(temujanji.getBandarPejabat());		
			sejarah.setNoTelefonPejabat(temujanji.getNoTelefonPejabat());
			sejarah.setFlagInternal(temujanji.getFlagInternal());
			sejarah.setCatatan(temujanji.getCatatan());
			sejarah.setPetugas(temujanji.getPetugas());
			sejarah.setKehadiran(temujanji.getKehadiran());
			sejarah.setTarikhTerimaKunci(temujanji.getTarikhTerimaKunci());
			sejarah.setTarikhKeluarKuarters(temujanji.getTarikhKeluarKuarters());
			sejarah.setTarikhSerahKunci(temujanji.getTarikhSerahKunci());
			sejarah.setMemoDaripada(temujanji.getMemoDaripada());
			sejarah.setMemoKepada(temujanji.getMemoKepada());
			sejarah.setMemoPerkara(temujanji.getMemoPerkara());
			sejarah.setMemoRujukan(temujanji.getMemoRujukan());
			sejarah.setMemoSk(temujanji.getMemoSk());
			sejarah.setMemoTarikh(temujanji.getMemoTarikh());			
			sejarah.setStatusTemujanji(statusTemujanji);
			sejarah.setDaftarOleh(temujanji.getDaftarOleh());
			sejarah.setTarikhMasuk(temujanji.getTarikhMasuk());
			sejarah.setKemaskiniOleh(temujanji.getKemaskiniOleh());
			sejarah.setTarikhKemaskini(temujanji.getTarikhKemaskini());
			if (addSejarah) {
				mp.persist(sejarah);
			}
			mp.remove(temujanji);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error simpanHistory : " + e.getMessage());
		} 
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {

		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0) {
			idNegeri = get("idNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatSelepasKeluarKuarters/selectBandar.vm";
	}
	
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {

		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0) {
			idKementerian = get("idKementerian");
		}
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/maklumatPertukaranJabatan/selectAgensi.vm";
	}
	
	@Command("selectBandarPejabat")
	public String selectBandarPejabat() throws Exception {

		String idNegeri = "0";
		if (get("idNegeriPejabat").trim().length() > 0) {
			idNegeri = get("idNegeriPejabat");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatPertukaranJabatan/selectBandar.vm";
	}
}

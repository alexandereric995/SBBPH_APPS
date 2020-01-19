package bph.modules.qtr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.SebabBertukar;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaTemujanji;
import bph.entities.qtr.KuaTemujanjiHistory;
import bph.mail.mailer.TemujanjiKuartersMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmTemujanjiAwam extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;
	private DbPersistence db;
	private String userId;

	public String getPath() {
		return "bph/modules/qtr/temujanji/temujanjiAwam";
	}
	
	@Override
	public String start() {
		String vm = "/start.vm";
		userId = (String) request.getSession().getAttribute("_portal_login");		
		
		KuaPenghuni penghuni = null;
		KuaTemujanji temujanji = null;
		String allowBatalTemujanji = "";
		try {
			mp = new MyPersistence();
			db = new DbPersistence();
			
			dataUtil = DataUtil.getInstance(db);
			context.put("selectSebabBertukar", dataUtil.getListSebabBertukar());
			context.put("selectNegeri", dataUtil.getListNegeri());
			
			// CHECK SAMA ADA PEMOHON ADALAH PENGHUNI ATAU TIDAK
			penghuni = (KuaPenghuni) mp.get("select x from KuaPenghuni x where x.tarikhKeluarKuarters is null and x.pemohon.id = '" + userId + "'");
			if (penghuni == null) {
				return getPath() + "/startBukanPenghuni.vm";
			} else {
				// CHECK SAMA ADA DATA KUARTERS LENGKAP ATAU TIDAK
				if (penghuni.getKuarters() == null) {
					return getPath() + "/startDataTidakLengkap.vm";
				}
			}
			
			//PEMOHON ADA PENGHUNI DAN MAKLUMAT KUARTERS DI PENGHUNI LENGKAP
			if (penghuni != null && penghuni.getKuarters() != null) {
				temujanji = (KuaTemujanji) mp.get("select x from KuaTemujanji x where x.flagInternal = '0' and x.penghuni.id = '"  + penghuni.getId() + "'");
			}			
			
			allowBatalTemujanji = allowBatalTemujanji(temujanji, mp);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("allowBatalTemujanji", allowBatalTemujanji);
		context.put("penghuni", penghuni);
		context.put("temujanji", temujanji);
		context.put("path", getPath());
		context.put("util", util);
		return getPath() + vm;
	}
	
	private String allowBatalTemujanji(KuaTemujanji temujanji, MyPersistence mp) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String allowBatal = "Y";
		if (temujanji != null) {
			if (temujanji.getTarikhTemujanji() != null) {
				Calendar calCurrent = new GregorianCalendar();
				calCurrent.setTime(sdf.parse(sdf.format(new Date())));
				Calendar calTemujanji = new GregorianCalendar();
				calTemujanji.setTime(temujanji.getTarikhTemujanji());
				calCurrent.add(Calendar.DATE, 3);
				if (calCurrent.after(calTemujanji)) {
					allowBatal = "T";
				}
			}
		}
		return allowBatal;
	}

	@Command("getMasaTemujanji")
	public String getMasaTemujanji() throws ParseException {
		String idMasaTemujanji = getParam("pilihanMasaTemujanji");
		try {
			mp = new MyPersistence();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat shf = new SimpleDateFormat("hh:mm a");
			Date dateTemujanji = new SimpleDateFormat("dd-MM-yyyy").parse(getParam("tarikhTemujanji"));
			List<KuaTemujanji> listMasaTemujanji = mp.list("SELECT x FROM KuaTemujanji x " + "WHERE x.tarikhMulaTemujanji LIKE '" + sdf.format(dateTemujanji) + "%' order by x.tarikhMulaTemujanji asc");
			String[] k = { "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "02:00 PM",
					"02:30 PM", "03:00 PM", "03:30 PM" };
			for (int i = 0; i < k.length; i++) {
				//PAINT TO GREEN - AVAILABLE
				context.put("bgColor" + i, "#008000");
				
				//PAINT TO BLUE - SELECTED
				if (idMasaTemujanji.equals(k[i])) {
					context.put("bgColor" + i, "#0000FF");
				}
				
				//PAINT TO RED - BOOKED
				if (listMasaTemujanji.size() > 0) {
					for (int x = 0; x < listMasaTemujanji.size(); x++) {
						if (k[i].equals(shf.format(listMasaTemujanji.get(x).getTarikhMulaTemujanji()))) {
							context.put("bgColor" + i, "#FF0000");
						}												
					}
				}
			}
			context.put("idMasaTemujanji", idMasaTemujanji);
			context.put("listMasaTemujanji", listMasaTemujanji);
			context.put("util", util);
		} catch (Exception e) {
			System.out.println("Error getMasaTemujanji : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/maklumatTemujanji/masaTemujanji.vm";
	}
	
	@Command("doSimpanMaklumatTemujanji")
	public String doSimpanMaklumatTemujanji() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPenghuni = getParam("idPenghuni");
		
		KuaPenghuni penghuni = null;
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			penghuni = (KuaPenghuni) mp.find(KuaPenghuni.class, idPenghuni);
			if (penghuni != null) {
				mp.begin();
				temujanji = new KuaTemujanji();
				temujanji.setPenghuni(penghuni);
				temujanji.setKuarters(penghuni.getKuarters());
				temujanji.setPemohon(penghuni.getPemohon());
				temujanji.setTarikhMohonTemujanji(new Date());
				temujanji.setTarikhTemujanji(sdf.parse(getParam("tarikhTemujanji")));	
				temujanji.setTarikhKeluarKuarters(sdf.parse(getParam("tarikhTemujanji")));	
				temujanji.setTarikhTerimaKunci(sdf.parse(getParam("tarikhTemujanji")));	
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
				String tarikhTemujanji = getParam("tarikhTemujanji");
				String masaTemujanji = getParam("idMasaTemujanji");
				String dateTimeTemujanji = tarikhTemujanji + " " + masaTemujanji;
				Date dateTemujanji = formatter.parse(dateTimeTemujanji);
				temujanji.setTarikhMulaTemujanji(dateTemujanji);
				temujanji.setTarikhAkhirTemujanji(dateTemujanji);
				
				temujanji.setSebabBertukar((SebabBertukar) mp.find(SebabBertukar.class, getParam("idSebabBertukar")));
				temujanji.setKeterangan(getParam("keterangan"));
				temujanji.setAlamat1(getParam("alamat1"));
				temujanji.setAlamat2(getParam("alamat2"));
				temujanji.setAlamat3(getParam("alamat3"));
				temujanji.setPoskod(getParam("poskod"));
				temujanji.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				temujanji.setEmel(getParam("emel"));
				temujanji.setNoTelefonBimbit(getParam("noTelefonBimbit"));
				
				if (getParam("idSebabBertukar").equals("01")) {
					temujanji.setAgensi((Agensi) mp.find(Agensi.class, getParam("idAgensi")));
					temujanji.setBahagian(getParam("bahagian"));
					temujanji.setAlamatPejabat1(getParam("alamatPejabat1"));
					temujanji.setAlamatPejabat2(getParam("alamatPejabat2"));
					temujanji.setAlamatPejabat3(getParam("alamatPejabat3"));
					temujanji.setPoskodPejabat(getParam("poskodPejabat"));
					temujanji.setBandarPejabat((Bandar) mp.find(Bandar.class, getParam("idBandarPejabat")));
					temujanji.setNoTelefonPejabat(getParam("noTelefonPejabat"));
				}
				
				temujanji.setFlagInternal(0);
				temujanji.setDaftarOleh((Users) mp.find(Users.class, userId));
				mp.persist(temujanji);
				mp.commit();
				
				TemujanjiKuartersMailer.get().hantarNotifikasiDaftarTemujanji(temujanji.getEmel(), 
						Util.getDateTime(temujanji.getTarikhTemujanji(), "dd/MM/yyyy"), 
						Util.getDateTime(temujanji.getTarikhMulaTemujanji(), "hh:mm a"));
				
				String alamat = "";
				if (temujanji.getAlamat1() != null && temujanji.getAlamat1().trim().length() > 0) alamat = temujanji.getAlamat1();
				if (temujanji.getAlamat2() != null && temujanji.getAlamat2().trim().length() > 0) alamat = alamat + ", " + temujanji.getAlamat2();
				if (temujanji.getAlamat3() != null && temujanji.getAlamat3().trim().length() > 0) alamat = alamat + ", " + temujanji.getAlamat3();
				if (temujanji.getPoskod() != null) alamat = alamat + ", " + temujanji.getPoskod();
				if (temujanji.getBandar() != null) alamat = alamat + " " + temujanji.getBandar().getKeterangan();
				if (temujanji.getBandar().getNegeri() != null) alamat = alamat + ", " + temujanji.getBandar().getNegeri().getKeterangan();
				TemujanjiKuartersMailer.get().hantarNotifikasiUnitKuartersDaftarTemujanji(temujanji.getPemohon().getUserName(), alamat, 
						temujanji.getPemohon().getId(), temujanji.getNoTelefonBimbit(), "etemujanji@bph.gov.my", 
						Util.getDateTime(temujanji.getTarikhTemujanji(), "dd/MM/yyyy"), 
						Util.getDateTime(temujanji.getTarikhMulaTemujanji(), "hh:mm a"));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("penghuni", penghuni);
		context.put("temujanji", temujanji);
		context.put("path", getPath());
		context.put("util", util);
		context.put("command", command);
		return getPath() + "/start.vm";
	}
	
	@Command("doBatalMaklumatTemujanji")
	public String doBatalMaklumatTemujanji() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idTemujanji = getParam("idTemujanji");
		KuaPenghuni penghuni = null;
		KuaTemujanji temujanji = null;
		try {
			mp = new MyPersistence();
			
			temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, idTemujanji);
			if (temujanji != null) {
				penghuni = temujanji.getPenghuni();
				
				mp.begin();				
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
		
		context.remove(temujanji);
		context.put("penghuni", penghuni);
		context.put("path", getPath());
		context.put("util", util);
		context.put("command", command);
		return getPath() + "/start.vm";
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
	
	@Command("doChangeSebabBertukar")
	public String doChangeSebabBertukar() throws Exception {
		db = new DbPersistence();
		
		dataUtil = DataUtil.getInstance(db);
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKementerian", dataUtil.getListKementerian());
		
		String idSebabBertukar = getParam("idSebabBertukar");
		context.put("idSebabBertukar", idSebabBertukar);
		context.put("path", getPath());
		return getPath() + "/maklumatPertukaranJabatan/start.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		db = new DbPersistence();
		
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";
		if (getParam("idNegeri").trim().length() > 0) {
			idNegeri = getParam("idNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatSelepasKeluarKuarters/selectBandar.vm";
	}
	
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		db = new DbPersistence();
		
		dataUtil = DataUtil.getInstance(db);
		String idKementerian = "0";
		if (getParam("idKementerian").trim().length() > 0) {
			idKementerian = getParam("idKementerian");
		}
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/maklumatPertukaranJabatan/selectAgensi.vm";
	}
	
	@Command("selectBandarPejabat")
	public String selectBandarPejabat() throws Exception {
		db = new DbPersistence();
		
		dataUtil = DataUtil.getInstance(db);
		String idNegeri = "0";
		if (getParam("idNegeriPejabat").trim().length() > 0) {
			idNegeri = getParam("idNegeriPejabat");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatPertukaranJabatan/selectBandar.vm";
	}
}

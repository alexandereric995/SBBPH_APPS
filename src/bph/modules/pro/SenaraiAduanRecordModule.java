/**
 * Author : zulfazdliabuas@gmail.com Date 2015 - 2017
 */

package bph.modules.pro;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisAduan;
import bph.entities.kod.Seksyen;
import bph.entities.kod.Status;
import bph.entities.kod.SumberAduan;
import bph.entities.pro.ProAduan;
import bph.entities.pro.ProAgihanAduan;
import bph.entities.pro.ProAgihanUnit;
import bph.entities.pro.ProKategoriTeknikal;
import bph.entities.pro.ProKeteranganTeknikal;
import bph.entities.pro.ProPegawaiUnitPertama;
import bph.entities.pro.ProSenaraiUnit;
import bph.entities.pro.ProSequenceAduan;
import bph.mail.mailer.AduanMailer;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiAduanRecordModule extends LebahRecordTemplateModule<ProAduan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(ProAduan aduan) {
		// TODO Auto-generated method stub
		
//		//--------- Start Generate NoAduan old------
//		int next = 0;
//		db.begin();
//		Calendar calCurrent = new GregorianCalendar();
//		calCurrent.setTime(new Date());
//		int bulan = calCurrent.get(Calendar.MONTH) + 1;
//		int tahun = calCurrent.get(Calendar.YEAR);
//		
//		ProSequenceAduan seq;
//		synchronized(this) {
//		 seq = (ProSequenceAduan) db.get("select x from ProSequenceAduan x where x.bulan = '" + bulan + "' and x.tahun = '" + tahun + "'");
//	
//			if(seq == null){
//				seq = new ProSequenceAduan();
//				next = 1;
//				seq.setBulan(bulan);
//				seq.setTahun(tahun);
//				seq.setBilangan(next);
//				db.persist(seq);
//			} else {
//				next = seq.getBilangan() + 1;
//				seq.setBilangan(next);
//			}
//		}
//		
//		String formatserial = new DecimalFormat("00").format(seq.getBilangan());
//		String noAduan = "BPH/AD/" + tahun + "/" + new DecimalFormat("00").format(bulan) + "/"+ formatserial;
//		
//		aduan.setNoAduan(noAduan);
//		//--------- End Generate NoAduan ------
		
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			//--------- Start Generate NoAduan - New ambil dari portal ------
			mp.begin();
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
			String noAduan = "BPH/AD/" + tahun + "/" + new DecimalFormat("00").format(bulan) + "/" + formatserial;
			
			aduan.setNoAduan(noAduan);
			//--------- End Generate NoAduan - New ambil dari portal ------
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.put("selectedTab", 1);
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		dataUtil = DataUtil.getInstance(db);
		
//		context.remove("r");
		context.put("r", "");
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectSumberAduan", dataUtil.getListSumberAduan());
		context.put("selectJenisAduan", dataUtil.getListJenisAduan());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectStatusAduan", dataUtil.getListStatusAduan());
		
		if ("add_new_record".equals(command))
			context.put("tarikhAduan", new Date());
		
		context.put("flagPenyemak", false);
		context.put("flagPetugas", false);
				
		context.put("util", util);
		
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
		this.setHideDeleteButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}
		
	private void addfilter() {
		
		Users users = db.find(Users.class, userId);

		if ("(ICT) Pentadbir Sistem".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '04')");
		}
		
		if ("(RPP) Penyedia".equals(userRole) || "(RPP) Penyemak".equals(userRole) || "(RPP) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '10')");
		}

		if ("(UTK) Penyedia".equals(userRole) || "(UTK) Pelulus".equals(userRole) || "(UTK) Penyemak".equals(userRole) || "(UTK) Penyelia".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '21')");
		}
		
		if ("(UTILITI) Penyedia".equals(userRole) || "(UTILITI) Penyemak".equals(userRole) || "(UTILITI) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '07')");
		}
		
		if ("(QTR) Penyedia".equals(userRole) || "(QTR) Penyemak".equals(userRole) || "(QTR) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '02')");
		}
		
		if ("(RK) Penyedia".equals(userRole) || "(RK) Penyemak".equals(userRole) || "(RK) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '12')");
		}
		
		if ("(JRP) Penyedia".equals(userRole) || "(JRP) Penyemak".equals(userRole) || "(JRP) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '09')");
		}
		
		if ("(TNH) Penyedia".equals(userRole) || "(TNH) Penyemak".equals(userRole) || "(TNH) Pelulus".equals(userRole)){
//			this.addFilter("status.id = '1440506568974'"); // MAKLUMBALAS UNIT
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
//			this.addFilterOr("status.id = '1440506568974'");
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '01')");
		}
			
		if ("(BGS) Penyedia".equals(userRole) || "(BGS) Penyemak".equals(userRole) || "(BGS) Pelulus".equals(userRole)){
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '06')");
		}
		
		if ("(DASAR) Penyedia".equals(userRole) || "(DASAR) Penyemak".equals(userRole) || "(DASAR) Pelulus".equals(userRole)){
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '15')");
		}
		
		if ("(KEW) Bayaran".equals(userRole) || "(DEPOSIT) Penyemak".equals(userRole)){
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '08')");
		}
		
		if ("(HASIL) Penyelia".equals(userRole)){
			this.addFilter("status.id = '1440506568977'"); // TINDAKAN UNIT
			this.addFilter("id in (select y.aduan.id from ProAgihanAduan y where y.seksyen.id = '11')");
		}
		
		this.setOrderBy("noAduan desc");
//		this.setOrderBy("id desc");
//		this.setOrderBy("sequence desc");
//		this.setOrderBy("tarikhAduan desc");
	}

	@Override
	public boolean delete(ProAduan aduan) throws Exception {
		// TODO Auto-generated method stub
		if (aduan.getStatus().getId().equals("1434787994722")) { //BARU
			return true;
		} else return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/pro/senaraiAduan";
	}

	@Override
	public Class<ProAduan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return ProAduan.class;
	}

	@Override
	public void getRelatedData(ProAduan aduan) {
		// TODO Auto-generated method stub
		if (aduan.getBandar() != null) {
			if (aduan.getBandar().getNegeri() != null) {
				context.put("selectBandar", dataUtil.getListBandar(aduan.getBandar().getNegeri().getId()));
			}
		}
		context.put("selectedTab", 1);
	}
	
	//****************SAVE ADUAN INTERNAL****************
	@Override
	public void save(ProAduan aduan) throws Exception {
		// TODO Auto-generated method stub
		//--- Kod Asal Sebelum tukar kepada Mp ---
//		aduan.setSumberAduan(db.find(SumberAduan.class, getParam("idSumberAduan")));
//		aduan.setJenisAduan(db.find(JenisAduan.class, getParam("idJenisAduan")));
//		aduan.setTarikhAduan(getDate("tarikhAduan"));
//		aduan.setTajuk(getParam("tajuk"));
//		aduan.setButiran(getParam("butiran"));
//		aduan.setNoPengenalan(getParam("noPengenalan"));
//		aduan.setNama(getParam("nama"));
//		aduan.setAlamat1(getParam("alamat1"));
//		aduan.setAlamat2(getParam("alamat2"));
//		aduan.setAlamat3(getParam("alamat3"));
//		aduan.setPoskod(getParam("poskod"));
//		aduan.setBandar(db.find(Bandar.class, getParam("idBandar")));
//		aduan.setNoTelefon(getParam("noTelefon"));
//		aduan.setEmel(getParam("emel"));
//		aduan.setStatus(db.find(Status.class, "1434787994722")); // BARU	
//		aduan.setIdMasuk(db.find(Users.class, userId));
//		aduan.setTarikhMasuk(new Date());	
		//--- Kod Asal Sebelum tukar kepada Mp ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			// mp.begin();
				aduan.setSumberAduan((SumberAduan) mp.find(SumberAduan.class, getParam("idSumberAduan")));
				aduan.setJenisAduan((JenisAduan) mp.find(JenisAduan.class, getParam("idJenisAduan")));
				aduan.setTarikhAduan(getDate("tarikhAduan"));
				aduan.setTajuk(getParam("tajuk"));
				aduan.setButiran(getParam("butiran"));
				aduan.setNoPengenalan(getParam("noPengenalan"));
				aduan.setNama(getParam("nama"));
				aduan.setAlamat1(getParam("alamat1"));
				aduan.setAlamat2(getParam("alamat2"));
				aduan.setAlamat3(getParam("alamat3"));
				aduan.setPoskod(getParam("poskod"));
				aduan.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				aduan.setNoTelefon(getParam("noTelefon"));
				aduan.setEmel(getParam("emel"));
				aduan.setStatus((Status) mp.find(Status.class, "1434787994722")); // BARU	
				aduan.setIdMasuk((Users) mp.find(Users.class, userId));
				aduan.setTarikhMasuk(new Date());
			// mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
	}
	//****************SAVE ADUAN INTERNAL****************
	
	//**************** CARIAN ****************
	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noAduan", getParam("findNoAduan"));
		map.put("sumberAduan.id", new OperatorEqualTo(getParam("findSumberAduan")));
		map.put("jenisAduan.id", new OperatorEqualTo(getParam("findJenisAduan")));
		map.put("status.id", new OperatorEqualTo(getParam("findStatusAduan")));
		map.put("tarikhAduan", new OperatorDateBetween(getDate("findTarikhAduanMula"), getDate("findTarikhAduanHingga")));
		map.put("tajuk", getParam("findTajuk"));
		
		map.put("noPengenalan", getParam("findNoPengenalan"));
		map.put("nama", getParam("findNama"));
		map.put("bandar.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		map.put("bandar.id", new OperatorEqualTo(getParam("findBandar")));
		return map;
	}
	//**************** CARIAN ****************
	
	//**************** START TAB ****************
	@Command("getMaklumatAduan")
	public String getMaklumatAduan() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp ---
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		context.put("r", aduan);
//		if (aduan.getBandar() != null) {
//			if (aduan.getBandar().getNegeri() != null) {
//				context.put("selectBandar", dataUtil.getListBandar(aduan.getBandar().getNegeri().getId()));
//			}
//		}
//		context.put("selectedTab", 1);
		//--- Kod Asal Sebelum tukar kepada Mp ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
			context.put("r", aduan);
			if (aduan.getBandar() != null) {
				if (aduan.getBandar().getNegeri() != null) {
					context.put("selectBandar", dataUtil.getListBandar(aduan.getBandar().getNegeri().getId()));
				}
			}
			context.put("selectedTab", 1);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatAgihan")
	public String getMaklumatAgihan() throws Exception {
		//--- Kod Asal Sebelum tukar kepada Mp ---
//		ProAduan aduan = db.find(ProAduan.class, getParam("idAduan"));
//		context.put("r", aduan);
//		
//		//----------START KETERANGAN TEKNIKAL-------------
//		// cara hardcode
//		List<ProKategoriTeknikal> listKategoriTeknikal = db.list("select x from ProKategoriTeknikal x where x.aduan.id = '" + aduan.getId() + "'");
//		context.put("listKategoriTeknikal", listKategoriTeknikal);
//		
//		// untuk list keterangan teknikal dari database
//		List<ProKeteranganTeknikal> listCheckboxKeteranganTeknikal = db.list("Select x from ProKeteranganTeknikal x order by x.id asc");
//		context.put("listCheckboxKeteranganTeknikal", listCheckboxKeteranganTeknikal);
//		//----------END KETERANGAN TEKNIKAL-------------
//		
////		//AGIHAN TUGASAN
////		List<ProAgihanAduan> listAgihan = db.list("select x from ProAgihanAduan x where x.aduan.id = '" + aduan.getId() + "' order by x.tarikhTugasan asc");
////		context.put("listAgihan", listAgihan);
//				
//		List<ProAgihanAduan> listpegawai = db.list("Select x from ProAgihanAduan x where x.aduan.id ='"+ aduan.getId() +"'");
//		context.put("listpegawai", listpegawai);
//		
//		//----------START CHECKBOX SENARAI UNIT-------------
//		//--- cara hardcode ---
//		List<ProAgihanUnit> listUnit = db.list("Select x from ProAgihanUnit x where x.aduan.id = '"+ aduan.getId() +"'");
//		context.put("listunit", listUnit);
//		
//		//--- untuk list Senarai Unit dari database ---
//		List<ProSenaraiUnit> listCheckBoxUnit = db.list("Select x from ProSenaraiUnit x order by x.id asc");
//		context.put("listCheckBoxUnit", listCheckBoxUnit);
//		//----------END START CHECKBOX SENARAI UNIT-------------
//		
//		context.put("selectedTab", 2);
		//--- Kod Asal Sebelum tukar kepada Mp ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();

			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, getParam("idAduan"));
			context.put("r", aduan);
			
			//----------START KETERANGAN TEKNIKAL-------------
			// cara hardcode
			List<ProKategoriTeknikal> listKategoriTeknikal = db.list("select x from ProKategoriTeknikal x where x.aduan.id = '" + aduan.getId() + "'");
			context.put("listKategoriTeknikal", listKategoriTeknikal);
			
			// untuk list keterangan teknikal dari database
			List<ProKeteranganTeknikal> listCheckboxKeteranganTeknikal = db.list("Select x from ProKeteranganTeknikal x order by x.id asc");
			context.put("listCheckboxKeteranganTeknikal", listCheckboxKeteranganTeknikal);
			//----------END KETERANGAN TEKNIKAL-------------
			
//			//AGIHAN TUGASAN
//			List<ProAgihanAduan> listAgihan = db.list("select x from ProAgihanAduan x where x.aduan.id = '" + aduan.getId() + "' order by x.tarikhTugasan asc");
//			context.put("listAgihan", listAgihan);
					
			List<ProAgihanAduan> listpegawai = db.list("Select x from ProAgihanAduan x where x.aduan.id ='"+ aduan.getId() +"'");
			context.put("listpegawai", listpegawai);
			
			//----------START CHECKBOX SENARAI UNIT-------------
			//--- cara hardcode ---
			List<ProAgihanUnit> listUnit = db.list("Select x from ProAgihanUnit x where x.aduan.id = '"+ aduan.getId() +"'");
			context.put("listunit", listUnit);
			
			//--- untuk list Senarai Unit dari database ---
			List<ProSenaraiUnit> listCheckBoxUnit = db.list("Select x from ProSenaraiUnit x order by x.id asc");
			context.put("listCheckBoxUnit", listCheckBoxUnit);
			//----------END START CHECKBOX SENARAI UNIT-------------
			
			context.put("selectedTab", 2);
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/entry_page.vm";
	}
	//**************** END TAB ****************
	
	//**************** START SAVE APABILA EDIT MAKLUMAT ADUAN ****************
	@Command("saveAduan")
	public synchronized String saveAduan() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		db.begin();
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		aduan.setSumberAduan(db.find(SumberAduan.class, getParam("idSumberAduan")));
//		aduan.setJenisAduan(db.find(JenisAduan.class, getParam("idJenisAduan")));
//		aduan.setTarikhAduan(getDate("tarikhAduan"));
//		aduan.setTajuk(getParam("tajuk"));
//		aduan.setButiran(getParam("butiran"));
//		
//		aduan.setNoPengenalan(getParam("noPengenalan"));
//		aduan.setNama(getParam("nama"));
//		aduan.setAlamat1(getParam("alamat1"));
//		aduan.setAlamat2(getParam("alamat2"));
//		aduan.setAlamat3(getParam("alamat3"));
//		aduan.setPoskod(getParam("poskod"));
//		aduan.setBandar(db.find(Bandar.class, getParam("idBandar")));
//		aduan.setNoTelefon(getParam("noTelefon"));
//		aduan.setEmel(getParam("emel"));
//		aduan.setIdKemaskini(db.find(Users.class, userId));
//		aduan.setTarikhKemaskini(new Date());
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			mp.begin();
				ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
				aduan.setSumberAduan((SumberAduan) mp.find(SumberAduan.class, getParam("idSumberAduan")));
				aduan.setJenisAduan((JenisAduan) mp.find(JenisAduan.class, getParam("idJenisAduan")));
				aduan.setTarikhAduan(getDate("tarikhAduan"));
				aduan.setTajuk(getParam("tajuk"));
				aduan.setButiran(getParam("butiran"));
				aduan.setNoPengenalan(getParam("noPengenalan"));
				aduan.setNama(getParam("nama"));
				aduan.setAlamat1(getParam("alamat1"));
				aduan.setAlamat2(getParam("alamat2"));
				aduan.setAlamat3(getParam("alamat3"));
				aduan.setPoskod(getParam("poskod"));
				aduan.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				aduan.setNoTelefon(getParam("noTelefon"));
				aduan.setEmel(getParam("emel"));
				aduan.setIdKemaskini((Users) mp.find(Users.class, userId));
				aduan.setTarikhKemaskini(new Date());
			mp.commit();
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	//**************** END SAVE APABILA EDIT MAKLUMAT ADUAN ****************
	
	//**************** START MAKLUMBALAS UNIT ****************
	@Command("maklumbalasUnit")
	public String maklumbalasUnit() throws Exception {
		return getMaklumatAduan();
	}
	@Command("saveMaklumbalasUnit")
	public String saveMaklumbalasUnit() throws Exception {
			
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		db.begin();
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		aduan.setUlasanUnit(getParam("ulasanUnit"));
//		aduan.setCatatanUnit(getParam("catatanUnit"));
//		aduan.setTarikhMaklumbalasUnit(new Date());
////		aduan.setUrusetia(db.find(Users.class, userId));
//		aduan.setUrusetiaMaklumbalasUnit(db.find(Users.class, userId));
//		aduan.setStatus(db.find(Status.class, "1440506568974")); // MAKLUMBALAS UNIT
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Users urussetia = db.find(Users.class, userId);
//		String nama = urussetia.getUserName();
//		String email = urussetia.getEmel();
////		String to = aduan.getEmel();  // takperlu hantar email kepada pengadu
//		String to = aduan.getEmailPenerimaAduan();
//		String cc = urussetia.getEmel();
//		if (to.equals("") == false || email.equals("") == false){
//			AduanMailer.get().maklumbalasUnit(aduan, nama, email, to, cc); 
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			mp.begin();
				ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
				aduan.setUlasanUnit(getParam("ulasanUnit"));
				aduan.setCatatanUnit(getParam("catatanUnit"));
				aduan.setTarikhMaklumbalasUnit(new Date());
	//			aduan.setUrusetia(db.find(Users.class, userId));
				aduan.setUrusetiaMaklumbalasUnit((Users) mp.find(Users.class, userId));
				aduan.setStatus((Status) mp.find(Status.class, "1440506568974")); // MAKLUMBALAS UNIT
			mp.commit();
			
			Users urussetia = (Users) mp.find(Users.class, userId);
			String nama = urussetia.getUserName();
			String email = urussetia.getEmel();
//			String to = aduan.getEmel();  // takperlu hantar email kepada pengadu
			String to = aduan.getEmailPenerimaAduan();
			String cc = urussetia.getEmel();
			if (to.equals("") == false || email.equals("") == false){
				AduanMailer.get().maklumbalasUnit(aduan, nama, email, to, cc); 
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	//**************** END MAKLUMBALAS UNIT ****************
	
	//**************** START TERIMA ADUAN ****************
	@Command("terimaAduan")
	public String terimaAduan() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		// TEMPLATE EMEL
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		// DAPATKAN JENIS ADUAN
//		String templateJenis = "";
//		if(aduan.getJenisAduan().getId().equalsIgnoreCase("02") ) {
//			templateJenis = "aduan";
//		}
//		else if (aduan.getJenisAduan().getId().equalsIgnoreCase("03")) {
//			templateJenis = "rayuan";
//			}
//		else if (aduan.getJenisAduan().getId().equalsIgnoreCase("04")) {
//			templateJenis = "pertanyaan";
//			}
//		else if (aduan.getJenisAduan().getId().equalsIgnoreCase("05")) {
//			templateJenis = "permohonan";
//		}
//		//MASSAGE TEMPLATE
//		String msg = "Terima kasih atas " + templateJenis + " Tuan/Puan. Bahagian Pengurusan Hartanah(BPH) mengambil maklum dan " + templateJenis + " ini dan akan dipanjangkan kepada unit yang berkenaan untuk tindakan selanjutnya";
//		context.put("templatemsg", msg);
		//--- Eed Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			// TEMPLATE EMEL
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
			
			// DAPATKAN JENIS ADUAN
			String templateJenis = "";
			if(aduan.getJenisAduan().getId().equalsIgnoreCase("02") ) {
				templateJenis = "aduan";
			}else if (aduan.getJenisAduan().getId().equalsIgnoreCase("03")) {
				templateJenis = "rayuan";
			}else if (aduan.getJenisAduan().getId().equalsIgnoreCase("04")) {
				templateJenis = "pertanyaan";
			}else if (aduan.getJenisAduan().getId().equalsIgnoreCase("05")) {
				templateJenis = "permohonan";
			}
			
			//MASSAGE TEMPLATE
			String msg = "Terima kasih atas " + templateJenis + " Tuan/Puan. Bahagian Pengurusan Hartanah(BPH) mengambil maklum dan " + templateJenis + " ini dan akan dipanjangkan kepada unit yang berkenaan untuk tindakan selanjutnya";
			context.put("templatemsg", msg);
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	@Command("saveTerimaAduan")
	public String saveTerimaAduan() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		Users urussetia = db.find(Users.class, userId);
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		
//		db.begin();
//		if(aduan.getId() != null){
//			aduan.setUlasanUrusetia(getParam("ulasanUrusetia"));
//			aduan.setCatatanUrusetia(getParam("catatanUrusetia"));
//			aduan.setTarikhTerimaAduan(new Date());
//			aduan.setFlagAgihan("Y");
//			aduan.setUrusetia(db.find(Users.class, userId));
//			aduan.setStatus(db.find(Status.class, "1434787994725")); // DALAM TINDAKAN
//			aduan.setEmailPenerimaAduan(urussetia.getEmel());
//		}
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if(aduan.getUrusetia().getId() != null){
//			String nama = urussetia.getUserName();
//			String email = urussetia.getEmel();
//			String to = aduan.getEmel();
//			String cc = urussetia.getEmel();
//			if (to.equals("") == false){
//				AduanMailer.get().terimaAduan(aduan, nama, email, to, cc); 
//			}
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			Users urussetia = (Users) mp.find(Users.class, userId);
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
			
			mp.begin();
				if(aduan.getId() != null){
					aduan.setUlasanUrusetia(getParam("ulasanUrusetia"));
					aduan.setCatatanUrusetia(getParam("catatanUrusetia"));
					aduan.setTarikhTerimaAduan(new Date());
					aduan.setFlagAgihan("Y");
					aduan.setUrusetia((Users) mp.find(Users.class, userId));
					aduan.setStatus((Status) mp.find(Status.class, "1434787994725")); // DALAM TINDAKAN
					aduan.setEmailPenerimaAduan(urussetia.getEmel());
				}
			mp.commit();
			
			if(aduan.getUrusetia().getId() != null){
				String nama = urussetia.getUserName();
				String email = urussetia.getEmel();
				String to = aduan.getEmel();
				String cc = urussetia.getEmel();
				if (to.equals("") == false){
					AduanMailer.get().terimaAduan(aduan, nama, email, to, cc); 
				}
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	//**************** END TERIMA ADUAN ****************
	
	//**************** START TEMPLATE TOLAK ADUAN ****************
	@Command("tolakAduan")
	public String tolakAduan() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		// TEMPLATE EMEL
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		// DAPATKAN JENIS ADUAN
//		String templateJenis = "";
//		if(aduan.getJenisAduan().getId().equalsIgnoreCase("02") ) {
//			templateJenis = "aduan";
//		}
//		else if (aduan.getJenisAduan().getId().equalsIgnoreCase("03")) {
//			templateJenis = "rayuan";
//			}
//		else if (aduan.getJenisAduan().getId().equalsIgnoreCase("04")) {
//			templateJenis = "pertanyaan";
//			}
//		else if (aduan.getJenisAduan().getId().equalsIgnoreCase("05")) {
//			templateJenis = "permohonan";
//		}
//		//MASSAGE TEMPLATE
//		String msg = "Bahagian Pengurusan Hartanah (BPH) mengucapkan terima kasih di atas " + templateJenis + " yang dikemukan untuk meningkatkan mutu perkhidmatan BPH. Walau bagaimanapun " + templateJenis + " tuan/puan telah disemak dan didapati tindakan terhadap aduan tersebut adalah di luar bidang kuasa BPH.";
//		context.put("templatemsg", msg);
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			// TEMPLATE EMEL
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
			
			// DAPATKAN JENIS ADUAN
			String templateJenis = "";
			if(aduan.getJenisAduan().getId().equalsIgnoreCase("02") ) {
				templateJenis = "aduan";
			}else if (aduan.getJenisAduan().getId().equalsIgnoreCase("03")) {
				templateJenis = "rayuan";
			}else if (aduan.getJenisAduan().getId().equalsIgnoreCase("04")) {
				templateJenis = "pertanyaan";
			}else if (aduan.getJenisAduan().getId().equalsIgnoreCase("05")) {
				templateJenis = "permohonan";
			}
			
			//MASSAGE TEMPLATE
			String msg = "Bahagian Pengurusan Hartanah (BPH) mengucapkan terima kasih di atas " + templateJenis + " yang dikemukan untuk meningkatkan mutu perkhidmatan BPH. Walau bagaimanapun " + templateJenis + " tuan/puan telah disemak dan didapati tindakan terhadap aduan tersebut adalah di luar bidang kuasa BPH.";
			context.put("templatemsg", msg);
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	//**************** END TEMPLATE TOLAK ADUAN ****************
	
	//**************** START SAVE TOLAK ADUAN ****************
	@Command("savePenolakanAduan")
	public String savePenolakanAduan() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		db.begin();
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		aduan.setUlasanSelesai(getParam("ulasanSelesai"));
//		aduan.setCatatanSelesai(getParam("catatanSelesai"));
////		aduan.setUrusetia(db.find(Users.class, userId));
//		aduan.setUrusetiaTolakAduan(db.find(Users.class, userId));
//		aduan.setTarikhSelesai(new Date());
//		aduan.setStatus(db.find(Status.class, "1434787994731")); // TIDAK BERASAS
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Users urussetia = db.find(Users.class, userId);
//		String nama = urussetia.getUserName();
//		String email = urussetia.getEmel();
//		String to = aduan.getEmel();
//		String cc = urussetia.getEmel();
//		if (to.equals("") == false){
//			AduanMailer.get().selesaiAduan(aduan, nama, email, to, cc); 
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			mp.begin();
				ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
				aduan.setUlasanSelesai(getParam("ulasanSelesai"));
				aduan.setCatatanSelesai(getParam("catatanSelesai"));
	//			aduan.setUrusetia(db.find(Users.class, userId));
				aduan.setUrusetiaTolakAduan((Users) mp.find(Users.class, userId));
				aduan.setTarikhSelesai(new Date());
				aduan.setStatus((Status) mp.find(Status.class, "1434787994731")); // TIDAK BERASAS
			mp.commit();
			
			Users urussetia = (Users) mp.find(Users.class, userId);
			String nama = urussetia.getUserName();
			String email = urussetia.getEmel();
			String to = aduan.getEmel();
			String cc = urussetia.getEmel();
			if (to.equals("") == false){
				AduanMailer.get().selesaiAduan(aduan, nama, email, to, cc); 
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	//**************** END SAVE TOLAK ADUAN ****************
	
	//**************** START SELESAI ADUAN ****************
	@Command("selesaiAduan")
	public String selesaiAduan() throws Exception {
		return getMaklumatAduan();
	}
	@Command("saveSelesaiAduan")
	public String saveSelesaiAduan() throws Exception {
				
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		db.begin();
//		ProAduan aduan = db.find(ProAduan.class, get("idAduan"));
//		aduan.setUlasanSelesai(getParam("ulasanSelesai"));
//		aduan.setCatatanSelesai(getParam("catatanSelesai"));
////		aduan.setUrusetia(db.find(Users.class, userId));
//		aduan.setUrusetiaSelesaiAduan(db.find(Users.class, userId));
//		aduan.setTarikhSelesai(new Date());
//		aduan.setStatus(db.find(Status.class, "1434787994728")); // SELESAI
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Users urussetia = db.find(Users.class, userId);
//		String nama = urussetia.getUserName();
//		String email = urussetia.getEmel();
//		String to = aduan.getEmel();
//		String cc = urussetia.getEmel();
//		if (to.equals("") == false){
//			AduanMailer.get().selesaiAduan(aduan, nama, email, to, cc); 
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			mp.begin();
				ProAduan aduan = (ProAduan) mp.find(ProAduan.class, get("idAduan"));
				aduan.setUlasanSelesai(getParam("ulasanSelesai"));
				aduan.setCatatanSelesai(getParam("catatanSelesai"));
				//aduan.setUrusetia(db.find(Users.class, userId));
				aduan.setUrusetiaSelesaiAduan((Users) mp.find(Users.class, userId));
				aduan.setTarikhSelesai(new Date());
				aduan.setStatus((Status) mp.find(Status.class, "1434787994728")); // SELESAI
			mp.commit();
			
			Users urussetia = (Users) mp.find(Users.class, userId);
			String nama = urussetia.getUserName();
			String email = urussetia.getEmel();
			String to = aduan.getEmel();
			String cc = urussetia.getEmel();
			if (to.equals("") == false){
				AduanMailer.get().selesaiAduan(aduan, nama, email, to, cc); 
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getMaklumatAduan();
	}
	//**************** END SELESAI ADUAN ****************
	
	//**************** START MAKLUMAT FM ****************
	@Command("emailKeFm")
	public String emailKeFm() throws Exception {
		return getMaklumatAduan();
	}
	@Command("saveEmailKeFm")
	public String saveEmailKeFm() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		db.begin();
//		ProAduan aduan =  db.find(ProAduan.class, get("idAduan"));
//		
//		//Simpan email FM
//		aduan.setEmailFm(getParam("emailFm"));
//		aduan.setEmailCc(getParam("emailCc"));
//		aduan.setKeteranganTeknikal(getParam("keteranganTeknikal"));
////		aduan.setStatus(db.find(Status.class, "1440473608806"));  //TELAH DIMAJUKAN
////		aduan.setStatus(db.find(Status.class, "1440473608806"));  //TELAH DIMAJUKAN
//		aduan.setStatus(db.find(Status.class, "1440506568974"));  //MAKLUMBALAS UNIT
//		aduan.setUrusetiaFm(db.find(Users.class, userId));
//		aduan.setTarikhMaklumbalasFm(new Date());
//		try {
//			db.commit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//HANTAR EMAIL
//		Users urussetia = db.find(Users.class, userId);
//		String nama = urussetia.getUserName();
//		String email = urussetia.getEmel();
//		String to = aduan.getEmailFm();
//		String cc = aduan.getEmailCc();
//		if (to.equals("") == false){
//			AduanMailer.get().hantarEmailKeFm(aduan, nama, email, to, cc); 
//		}
//		
//		context.put("selectedTab", 1);	
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			mp.begin();
				ProAduan aduan =  (ProAduan) mp.find(ProAduan.class, get("idAduan"));
				//Simpan email FM
				aduan.setEmailFm(getParam("emailFm"));
				aduan.setEmailCc(getParam("emailCc"));
				aduan.setKeteranganTeknikal(getParam("keteranganTeknikal"));
				//aduan.setStatus(db.find(Status.class, "1440473608806"));  //TELAH DIMAJUKAN
				//aduan.setStatus(db.find(Status.class, "1440473608806"));  //TELAH DIMAJUKAN
				aduan.setStatus((Status) mp.find(Status.class, "1440506568974"));  //MAKLUMBALAS UNIT
				aduan.setUrusetiaFm((Users) mp.find(Users.class, userId));
				aduan.setTarikhMaklumbalasFm(new Date());
			mp.commit();
			
			//HANTAR EMAIL
			Users urussetia = (Users) mp.find(Users.class, userId);
			String nama = urussetia.getUserName();
			String email = urussetia.getEmel();
			String to = aduan.getEmailFm();
			String cc = aduan.getEmailCc();
			if (to.equals("") == false){
				AduanMailer.get().hantarEmailKeFm(aduan, nama, email, to, cc); 
			}
			
			context.put("selectedTab", 1);	
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/entry_page.vm";
	}
	//**************** END MAKLUMAT FM ****************
	
	
	//**************** START SAVE PILIH UNIT ****************
	//**************** CODE LAMA CHECKBOX HARDCODE ****************
//	@Command("savePilihUnit")
//	public String savePilihUnit() throws Exception {
//		ProAduan aduan =  db.find(ProAduan.class, get("idAduan"));
//		
//		//DELETE
//		ProAgihanUnit deletePilihUnit = null;
//		int i = 0;
//		List<ProAgihanUnit> listDeletePilihUnit = db.list("select x from ProAgihanUnit x where x.aduan.id = '" + aduan.getId() + "'");
//		while (i < listDeletePilihUnit.size()) {
//			deletePilihUnit = db.find(ProAgihanUnit.class, listDeletePilihUnit.get(i).getId());
//			db.begin();
//			db.remove(deletePilihUnit);
//			db.commit();
//			i++;
//		}
//		
//		//ADD UNIT
//		ProAgihanUnit pilihUnit = null;
//		String[] cbUnit = request.getParameterValues("cbUnit");
//		if(cbUnit != null){
//			for (int x = 0; x < cbUnit.length; x++) {
//				List<ProAgihanUnit> check = db.list("Select x from ProAgihanUnit x where x.aduan.id = '"+ aduan.getId() +"' and x.seksyen.id ='"+ cbUnit[x] +"'");
//				if(check.size() == 0){
//					pilihUnit = new ProAgihanUnit();
//					pilihUnit.setAduan(aduan);
//					pilihUnit.setSeksyen(db.find(Seksyen.class, cbUnit[x]));
//					db.begin();
//					db.persist(pilihUnit);
//					db.commit();
//				}
//			}
//		}
//		
//		return getPath() + "/agihanUnit/result.vm";
//	}
	//**************** CODE CHECKBOX DARI DATABASE ****************
	@Command("savePilihUnit")
	public String savePilihUnit() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		ProAduan aduan =  db.find(ProAduan.class, get("idAduan"));
//		
//		//DELETE
//		ProAgihanUnit deletePilihUnit = null;
//		int i = 0;
//		List<ProAgihanUnit> listDeletePilihUnit = db.list("select x from ProAgihanUnit x where x.aduan.id = '" + aduan.getId() + "'");
//		while (i < listDeletePilihUnit.size()) {
//			deletePilihUnit = db.find(ProAgihanUnit.class, listDeletePilihUnit.get(i).getId());
//			db.begin();
//			db.remove(deletePilihUnit);
//			db.commit();
//			i++;
//		}
//		
//		//ADD UNIT
//		String statusInfo = "";
//		String[] checkUnit = request.getParameterValues("NmCheckboxUnit");
//		if (checkUnit != null){
//			for (String strUnit : checkUnit){
//				ProSenaraiUnit agihanUnit = (ProSenaraiUnit) db.get("Select x from ProSenaraiUnit x where x.id='"+ strUnit +"'");
//				ProAgihanUnit savecheckunit = new ProAgihanUnit();
//				savecheckunit.setAduan(db.find(ProAduan.class, get("idAduan")));
//				savecheckunit.setSeksyen(db.find(Seksyen.class, agihanUnit.getSeksyen().getId()));
//				savecheckunit.setIdSenaraiUnit(db.find(ProSenaraiUnit.class, agihanUnit.getId()));
//				db.begin();
//				db.persist(savecheckunit);
//				db.commit();
//			}
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			ProAduan aduan =  (ProAduan) mp.find(ProAduan.class, get("idAduan"));
			
			//DELETE
			ProAgihanUnit deletePilihUnit = null;
			int i = 0;
			List<ProAgihanUnit> listDeletePilihUnit = mp.list("select x from ProAgihanUnit x where x.aduan.id = '" + aduan.getId() + "'");
			while (i < listDeletePilihUnit.size()) {
				deletePilihUnit = (ProAgihanUnit) mp.find(ProAgihanUnit.class, listDeletePilihUnit.get(i).getId());
				mp.begin();
				mp.remove(deletePilihUnit);
				mp.commit();
				i++;
			}
			
			//ADD UNIT
			String statusInfo = "";
			String[] checkUnit = request.getParameterValues("NmCheckboxUnit");
			if (checkUnit != null){
				for (String strUnit : checkUnit){
					ProSenaraiUnit agihanUnit = (ProSenaraiUnit) mp.get("Select x from ProSenaraiUnit x where x.id='"+ strUnit +"'");
					ProAgihanUnit savecheckunit = new ProAgihanUnit();
					savecheckunit.setAduan((ProAduan) mp.find(ProAduan.class, get("idAduan")));
					savecheckunit.setSeksyen((Seksyen) mp.find(Seksyen.class, agihanUnit.getSeksyen().getId()));
					savecheckunit.setIdSenaraiUnit((ProSenaraiUnit) mp.find(ProSenaraiUnit.class, agihanUnit.getId()));
					mp.begin();
					mp.persist(savecheckunit);
					mp.commit();
				}
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/refreshDiv.vm";
	}
	//**************** END SAVE PILIH UNIT ****************
	

//	//**************** START CHACKBOX MAKLUMAT TEKNIKAL ****************
	//**************** CODE lama Chackbox bukan dari database ****************
//	@Command("saveKeteranganTeknikal")
//	public String saveKeteranganTeknikal() throws Exception {
//				
//		ProAduan aduan =  db.find(ProAduan.class, get("idAduan"));
//		
//		//DELETE KATEGORI TEKNIKAL
//		ProKategoriTeknikal kategori = null;
//		int i = 0;
//		List<ProKategoriTeknikal> listKategoriTeknikal = db.list("select x from ProKategoriTeknikal x where x.aduan.id = '" + aduan.getId() + "'");
//		while (i < listKategoriTeknikal.size()) {
//			kategori = db.find(ProKategoriTeknikal.class, listKategoriTeknikal.get(i).getId());
//			db.begin();
//			db.remove(kategori);
//			db.commit();
//			i++;
//		}
//		
//		//ADD KATEGORI TEKNIKAL
//		ProKategoriTeknikal kategoriTeknikal = null;
//		String[] cbKategori = request.getParameterValues("cbKategori");
//		if(cbKategori != null){
//			for (int x = 0; x < cbKategori.length; x++) { 
//				kategoriTeknikal = new ProKategoriTeknikal();
//				kategoriTeknikal.setAduan(aduan);
//				kategoriTeknikal.setKategori(cbKategori[x]);
//				db.begin();
//				db.persist(kategoriTeknikal);
//				db.commit();
//			}
//		}
//				
//		return getPath() + "/refreshDiv.vm";
//	}
//	//**************** NEW CODE CHECKBOX DARI DATABASE ****************
	@Command("saveKeteranganTeknikal")
	public String saveKeteranganTeknikal() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		ProAduan aduan =  db.find(ProAduan.class, get("idAduan"));
//		
//		//DELETE KATEGORI TEKNIKAL
//		ProKategoriTeknikal kategori = null;
//		int i = 0;
//		List<ProKategoriTeknikal> listKategoriTeknikal = db.list("select x from ProKategoriTeknikal x where x.aduan.id = '" + aduan.getId() + "'");
//		while (i < listKategoriTeknikal.size()) {
//			kategori = db.find(ProKategoriTeknikal.class, listKategoriTeknikal.get(i).getId());
//			db.begin();
//			db.remove(kategori);
//			db.commit();
//			i++;
//		}
//		
//		//ADD KATEGORI TEKNIKAL
//		String statusInfo = "";
//		String[] idcheckbox = request.getParameterValues("kategori");
//		if(idcheckbox != null){
//			for (String kategoriId : idcheckbox){
//				ProKeteranganTeknikal keteranganteknikalid = (ProKeteranganTeknikal) db.get("Select x from ProKeteranganTeknikal x where x.id='"+ kategoriId +"'");
//				ProKategoriTeknikal savecheck = new ProKategoriTeknikal();
//				savecheck.setAduan(db.find(ProAduan.class, get("idAduan")));
//				savecheck.setIdKeteranganTeknikal(db.find(ProKeteranganTeknikal.class, keteranganteknikalid.getId()));
//				db.begin();
//				db.persist(savecheck);
//				db.commit();
//			}
//		}
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			ProAduan aduan =  (ProAduan) mp.find(ProAduan.class, get("idAduan"));
			
			//DELETE KATEGORI TEKNIKAL
			ProKategoriTeknikal kategori = null;
			int i = 0;
			List<ProKategoriTeknikal> listKategoriTeknikal = mp.list("select x from ProKategoriTeknikal x where x.aduan.id = '" + aduan.getId() + "'");
			while (i < listKategoriTeknikal.size()) {
				kategori = (ProKategoriTeknikal) mp.find(ProKategoriTeknikal.class, listKategoriTeknikal.get(i).getId());
				mp.begin();
				mp.remove(kategori);
				mp.commit();
				i++;
			}
			
			//ADD KATEGORI TEKNIKAL
			String statusInfo = "";
			String[] idcheckbox = request.getParameterValues("kategori");
			if(idcheckbox != null){
				for (String kategoriId : idcheckbox){
					ProKeteranganTeknikal keteranganteknikalid = (ProKeteranganTeknikal) mp.get("Select x from ProKeteranganTeknikal x where x.id='"+ kategoriId +"'");
					ProKategoriTeknikal savecheck = new ProKategoriTeknikal();
					savecheck.setAduan((ProAduan) mp.find(ProAduan.class, get("idAduan")));
					savecheck.setIdKeteranganTeknikal((ProKeteranganTeknikal) mp.find(ProKeteranganTeknikal.class, keteranganteknikalid.getId()));
					mp.begin();
					mp.persist(savecheck);
					mp.commit();
				}
			}
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/refreshDiv.vm";
	}
	//**************** END CHACKBOX MAKLUMAT TEKNIKAL ****************
	
	//**************** START PILIH PEGAWAI PERTAMA untuk LIST ****************
	@Command("addPilihPegawaiPertama")
	public String addPilihPegawaiPertama() {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		ProAduan aduan = db.find(ProAduan.class, getParam("idAduan"));
//		context.put("r", aduan);
//		
//		List<ProPegawaiUnitPertama> listpegawai = db.list("Select x from ProPegawaiUnitPertama x where x.seksyen.id in(Select y.seksyen.id from ProAgihanUnit y where y.aduan.id ='"+ aduan.getId() +"') and x.pegawai.id not in (select z.pegawaiAgihan.id from ProAgihanAduan z where z.aduan.id ='"+ aduan.getId() +"')");
//		context.put("listpegawai", listpegawai);
//				
//		context.put("x", "");
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, getParam("idAduan"));
			context.put("r", aduan);
			
			List<ProPegawaiUnitPertama> listpegawai = mp.list("Select x from ProPegawaiUnitPertama x where x.seksyen.id in(Select y.seksyen.id from ProAgihanUnit y where y.aduan.id ='"+ aduan.getId() +"') and x.pegawai.id not in (select z.pegawaiAgihan.id from ProAgihanAduan z where z.aduan.id ='"+ aduan.getId() +"')");
			context.put("listpegawai", listpegawai);
					
			context.put("x", "");
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/popupPilihPegawai.vm";
	}
	//**************** END PILIH PEGAWAI PERTAMA untuk LIST ****************
	
	//**************** START SAVE PILIH PEGAWAI PERTAMA ****************
	@Command("savePilihPegawaiPertama")
	public String savePilihPegawaiPertama() throws Exception {
		
		//--- Kod Asal Sebelum tukar kepada Mp - Delete code ini bila dah confrim to live ---
//		ProAduan aduan = db.find(ProAduan.class, getParam("idAduan"));
//		context.put("r", aduan);
//		
//		String statusInfo = "";
//		String[] pegawai = request.getParameterValues("pegawai");
//		db.begin();
//		for (String pegawaiId : pegawai){
//			ProPegawaiUnitPertama unitpertama = (ProPegawaiUnitPertama) db.get("Select x from ProPegawaiUnitPertama x where x.pegawai.id='"+ pegawaiId +"'");
//			ProAgihanAduan namapegawai = new ProAgihanAduan();
//			namapegawai.setAduan(db.find(ProAduan.class, get("idAduan")));
//			namapegawai.setSeksyen(db.find(Seksyen.class, unitpertama.getSeksyen().getId()));
//			namapegawai.setPegawaiAgihan(db.find(Users.class, pegawaiId));
//			namapegawai.setTarikhTugasan(new Date());
//			db.persist(namapegawai);
//		}
////		aduan.setStatus(db.find(Status.class, "1440506568974")); // MAKLUMBALAS UNIT
//		aduan.setStatus(db.find(Status.class, "1440506568977")); // TINDAKAN UNIT
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		List<ProAgihanAduan> listpegawai = db.list("Select x from ProAgihanAduan x where x.aduan.id ='"+ aduan.getId() +"'");
//		context.put("listpegawai", listpegawai);
//		
//		context.put("selectedTab", 2);	
		//--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, getParam("idAduan"));
			context.put("r", aduan);
			
			String statusInfo = "";
			String[] pegawai = request.getParameterValues("pegawai");
			mp.begin();
				for (String pegawaiId : pegawai){
					ProPegawaiUnitPertama unitpertama = (ProPegawaiUnitPertama) mp.get("Select x from ProPegawaiUnitPertama x where x.pegawai.id='"+ pegawaiId +"'");
					ProAgihanAduan namapegawai = new ProAgihanAduan();
					namapegawai.setAduan((ProAduan) mp.find(ProAduan.class, get("idAduan")));
					namapegawai.setSeksyen((Seksyen) mp.find(Seksyen.class, unitpertama.getSeksyen().getId()));
					namapegawai.setPegawaiAgihan((Users) mp.find(Users.class, pegawaiId));
					namapegawai.setTarikhTugasan(new Date());
					mp.persist(namapegawai);
				}
				// aduan.setStatus(db.find(Status.class, "1440506568974")); // MAKLUMBALAS UNIT
				aduan.setStatus((Status) mp.find(Status.class, "1440506568977")); // TINDAKAN UNIT
			mp.commit();
			
			
			List<ProAgihanAduan> listpegawai = mp.list("Select x from ProAgihanAduan x where x.aduan.id ='"+ aduan.getId() +"'");
			context.put("listpegawai", listpegawai);
			
			context.put("selectedTab", 2);	
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/agihanPegawai/result.vm";
	}
	//**************** END SAVE PILIH PEGAWAI PERTAMA ****************
	
	//**************** START REMOVE PEGAWAI PERTAMA ****************
	@Command("removePegawaiPertama")
	public String removePegawaiPertama() {
		
		
//		String statusInfo = "";
//		
//		ProAduan aduan = db.find(ProAduan.class, getParam("idAduan"));
//		context.put("r", aduan);
//		
//		ProAgihanAduan maklumatPegawai = db.find(ProAgihanAduan.class, getParam("idPegawaiTugas"));
//		
//		db.begin();
//		
//		db.remove(maklumatPegawai);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		
//		db.begin();
////		aduan.setStatus(db.find(Status.class, "1440506568974")); // MAKLUMBALAS UNIT
//		aduan.setStatus(db.find(Status.class, "1440506568977")); // TINDAKAN UNIT
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		List<ProAgihanAduan> listpegawai = db.list("Select x from ProAgihanAduan x where x.aduan.id ='"+ aduan.getId() +"'");
//		context.put("listpegawai", listpegawai);
//		
//        context.put("selectedTab", 2);			
        //--- End Kod Asal ---
		
		//--- Tukar kepada MP by zulfazdliabuas@gmail.com date: 13/7/2017 ---
		try {
			mp = new MyPersistence();
			
			String statusInfo = "";
			
			ProAduan aduan = (ProAduan) mp.find(ProAduan.class, getParam("idAduan"));
			context.put("r", aduan);
			
			ProAgihanAduan maklumatPegawai = (ProAgihanAduan) mp.find(ProAgihanAduan.class, getParam("idPegawaiTugas"));
			mp.begin();
			mp.remove(maklumatPegawai);
				
			// aduan.setStatus(db.find(Status.class, "1440506568974")); // MAKLUMBALAS UNIT
			aduan.setStatus((Status) mp.find(Status.class, "1440506568977")); // TINDAKAN UNIT
			mp.commit();
				
			List<ProAgihanAduan> listpegawai = mp.list("Select x from ProAgihanAduan x where x.aduan.id ='"+ aduan.getId() +"'");
			context.put("listpegawai", listpegawai);
			
	        context.put("selectedTab", 2);		
			
		} catch (Exception ex) {
			System.out.println("ERROR : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		//--- End ---
        
		return getPath() + "/entry_page.vm";
	}
	//**************** END REMOVE PEGAWAI PERTAMA ****************
	
	
	//**************** START DROPDOWN LIST ****************
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		
		if (get("findNegeri").trim().length() > 0) 
			idNegeri = get("findNegeri");		
		context.put("selectBandar", dataUtil.getListBandar(idNegeri));	
			
		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		
		if (get("idNegeri").trim().length() > 0) 
			idNegeri = get("idNegeri");		
		context.put("selectBandar", dataUtil.getListBandar(idNegeri));	
			
		return getPath() + "/selectBandar.vm";
	}
	//**************** END DROPDOWN LIST ****************
	
}

package bph.modules.kewangan.terimaanHasil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kew.KewPenyataPemungut;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewSeqPenyataPemungut;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.KodPusatTerima;
import bph.utils.DataUtil;
import bph.utils.NumberToWords;
import db.persistence.MyPersistence;

public class PenyataPemungutRecordModule extends LebahRecordTemplateModule<KewPenyataPemungut>{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KewPenyataPemungut> getPersistenceClass() {
		return KewPenyataPemungut.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/kewangan/penyataPemungut";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users userLogin = db.find(Users.class, userId);
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
		
		String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		context.put("userLogin", userLogin);
		context.put("userRole", userRole);
		context.put("listKaedahBayaran", dataUtil.getCaraBayar()); //carian
		if (juruwang != null) {
			context.put("listDailyKaedahBayaran", dataUtil.getCaraBayarInDailyTransaction(dt, juruwang.getKodPusatTerima()));
		} else {
			context.put("listDailyKaedahBayaran", dataUtil.getCaraBayarInDailyTransaction(dt, null));
		}
		
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
			
	}

	private void defaultButtonOption() {			
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
	}
		
	private void addfilter() {
			
		this.setOrderBy("tarikhPenyataPemungut");
		this.setOrderType("desc");
	}

	@Override
	public void save(KewPenyataPemungut r) throws Exception {
		
	}

	@Override
	public boolean delete(KewPenyataPemungut r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();	
		m.put("kaedahBayaran.id", getParam("findKaedahBayaran"));
		m.put("noPenyataPemungut", getParam("findNoPenyataPemungut"));
		m.put("tarikhPenyataPemungut", getDate("findTarikhPenyataPemungut"));
		return m;
	}

	@Override
	public void beforeSave() { }

	@Override
	public void afterSave(KewPenyataPemungut r) { }

	@Override
	public void getRelatedData(KewPenyataPemungut r) {	
		String dt = new SimpleDateFormat("yyyy-MM-dd").format(r.getTarikhPenyataPemungut());
		context.put("listDailyKaedahBayaran", dataUtil.getCaraBayarInDailyTransaction(dt, r.getKodPusat()));
	}
	
	@Command("filterModBayaranByDate")
	public String filterModBayaranByDate() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
		
		String dt = new SimpleDateFormat("yyyy-MM-dd").format(getDate("tarikhPenyataPemungut"));
		context.put("listDailyKaedahBayaran", dataUtil.getCaraBayarInDailyTransaction(dt, juruwang.getKodPusatTerima()));
		return getPath() + "/selectKaedahBayaran.vm";
	}
	
	@Command("getMaklumatPenyataPemungutByModAndDate")
	public String getMaklumatPenyataPemungutByModAndDate() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
		
		context.remove("r");
		String kaedahBayaran = getParam("kaedahBayaran");
		String tarikhPenyataPemungut = new SimpleDateFormat("yyyy-MM-dd").format(getDate("tarikhPenyataPemungut"));
		
		KewPenyataPemungut r = getMaklumatPenyataPemungut(kaedahBayaran,tarikhPenyataPemungut, juruwang.getKodPusatTerima());
		context.put("r", r);
		
		return getPath() + "/maklumatPenyataPemungut.vm";
	}
	
	public KewPenyataPemungut getMaklumatPenyataPemungut(String kaedahBayaran, String tarikhPenyataPemungut, String kodPusatTerima){
		KewPenyataPemungut r = (KewPenyataPemungut) db.get("select x from KewPenyataPemungut x where x.kaedahBayaran.id = '"+kaedahBayaran+"' and x.tarikhPenyataPemungut = '"+tarikhPenyataPemungut+"' and x.kodPusat = '" + kodPusatTerima + "' ");
		return r;
	}
	
	@SuppressWarnings("unchecked")
	@Command("saveUpdate")
	public String saveUpdate() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users userLogin = db.find(Users.class, userId);
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
			
		String idPenyataPemungut = getParam("idPenyataPemungut");
		Date tarikhPenyataPemungut = getDate("tarikhPenyataPemungut");
		String kaedahBayaran = getParam("kaedahBayaran");
		String noAkaun = getParam("noAkaun");
		String kodJabatan = getParam("kodJabatan");
		String namaAkaun = getParam("namaAkaun");
		String noPenyataPemungut = getParam("noPenyataPemungut");
		//Date tarikhKutipanDari = getDate("tarikhKutipanDari");
		//Date tarikhKutipanHingga = getDate("tarikhKutipanHingga");
		
		String dt = new SimpleDateFormat("yyyy-MM-dd").format(tarikhPenyataPemungut);
		
		//get kod pusat terima
		KodJuruwang kod = (KodJuruwang)db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
		String kodPusat = kod!=null?kod.getKodPusatTerima():null;
		
		List<KewResitKaedahBayaran> listrkb = 
//				db.list("select x from KewResitKaedahBayaran x where x.resit.tarikhBayaran = '"+dt+"' and x.modBayaran.id = '"+kaedahBayaran+"' and x.resit.id in (select y.id from KewBayaranResit y where y.tarikhBayaran='"+dt+"' and y.noResit is not null and COALESCE(y.flagVoid,'T') = 'T') and x.resit.kodJuruwang.id is not null");
				db.list("select x from KewResitKaedahBayaran x where x.modBayaran.id = '"+kaedahBayaran+"' and x.resit.tarikhBayaran = '"+dt+"' and x.resit.noResit is not null and COALESCE(x.resit.flagVoid,'T') = 'T' and x.resit.kodJuruwang.id is not null and x.resit.kodJuruwang.kodPusatTerima = '" + juruwang.getKodPusatTerima() + "'");
		Double amaun = 0d;
		
		for(int i=0;i<listrkb.size();i++){
			amaun = amaun + listrkb.get(i).getAmaunBayaran();
			
		}
		
		KewPenyataPemungut r = db.find(KewPenyataPemungut.class, idPenyataPemungut);
		
		if(r==null){ r = new KewPenyataPemungut(); }
		
		db.begin();
		r.setJenisKod("140"); //DEFAULT
		r.setJumlahAmaun(amaun);
		r.setKaedahBayaran(db.find(CaraBayar.class, kaedahBayaran));
		r.setKodJabatan(kodJabatan);
		r.setKodPusat(kodPusat); //get from user kewangan login
		r.setNamaAkaun(namaAkaun);
		r.setNoAkaun(noAkaun);
		r.setNoPenyataPemungut(noPenyataPemungut);
		r.setPenyedia(userLogin);
		r.setTarikhKutipanDari(tarikhPenyataPemungut);
		r.setTarikhKutipanHingga(tarikhPenyataPemungut);
		r.setTarikhPenyataPemungut(tarikhPenyataPemungut);
		r.setTarikhDisediakan(new Date());
		String amaunPerkataan = NumberToWords.convertNumberToWords(String.valueOf(amaun));
		r.setAmaunPerkataan(amaunPerkataan.toUpperCase());
		db.persist(r);
		
		try {
			db.commit();
		} catch (Exception e) {
			System.out.println("error saveUpdate : "+e.getMessage());
			e.printStackTrace();
		}
		
		context.put("r", r);
		context.put("listDailyKaedahBayaran", dataUtil.getCaraBayarInDailyTransaction(dt, kodPusat));
		
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("updatePerakuan")
	public String updatePerakuan() {
		mp = new MyPersistence();		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPenyataPemungut = getParam("idPenyataPemungut");
		KewPenyataPemungut penyataPemungut = null;
		try {			
			
			Users userLogin = (Users) mp.find(Users.class, userId);
			penyataPemungut = (KewPenyataPemungut) mp.find(KewPenyataPemungut.class, idPenyataPemungut);
			if (penyataPemungut != null) {
				mp.begin();
				penyataPemungut.setPerakuan1(userLogin);
				penyataPemungut.setTarikhPerakuan1(new Date());
				if (penyataPemungut.getNoPenyataPemungut() == null || ("").equals(penyataPemungut.getNoPenyataPemungut())){
					penyataPemungut.setNoPenyataPemungut(generateNoPenyataPemungut(mp, penyataPemungut));
				}				
				mp.commit();
			} else {
				System.out.println("updatePerakuan : PENYATA PEMUNGUT IS NULL");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", penyataPemungut);
		return templateDir + "/entry_fields.vm";
	}

	private String generateNoPenyataPemungut(MyPersistence mp, KewPenyataPemungut penyataPemungut) {
		Calendar calCurrent = new GregorianCalendar();
		calCurrent.setTime(new Date());
		int tahun = calCurrent.get(Calendar.YEAR);
		int counter = 0;
		String kod = "XXX";
		KodPusatTerima pusatTerima = (KodPusatTerima) mp.get("select x from KodPusatTerima x where x.kodPusatTerima = '" + penyataPemungut.getKodPusat() + "'");
		if (pusatTerima != null) {
			kod = pusatTerima.getKodPenyataPemungut();
		}
		String id = kod + String.valueOf(tahun);
		
		KewSeqPenyataPemungut seq = (KewSeqPenyataPemungut) mp.get("select x from KewSeqPenyataPemungut x where x.kod = '" + kod + "' and x.tahun = '" + tahun + "'");

		if(seq != null){
			mp.pesismisticLock(seq);
			counter = seq.getBil() + 1;
			seq.setBil(counter);
			seq = (KewSeqPenyataPemungut) mp.merge(seq);
		} else {
			counter = 1;
			seq = new KewSeqPenyataPemungut();
			seq.setId(id);
			seq.setKod(kod);
			seq.setTahun(tahun);
			seq.setBil(counter);
			mp.persist(seq);
			mp.flush();
		}
		
		String formatserial = new DecimalFormat("0000").format(counter);
		String noPenyataPemungut = kod + formatserial;
		return noPenyataPemungut;
	}
}

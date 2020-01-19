/**
 * @author muhdsyazreen
 */

package bph.modules.kewangan.terimaanHasil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewTempBayar;
import bph.entities.kewangan.KewTempInQueue;
import bph.entities.kewangan.PembayarLain;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bank;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.KodMesin;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class JanaInvoisBayaran extends LebahRecordTemplateModule<PembayarLain>{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kewangan/janaInvoisBayaran";
	}
	
	@Override
	public Class<PembayarLain> getPersistenceClass() {
		return PembayarLain.class;
	}
	
	@Override
	public void beforeSave() { }

	@Override
	public boolean delete(PembayarLain arg0) throws Exception {
		return false;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", getParam("findId").trim());
		map.put("nama", getParam("findNama").trim());
//		map.put("noInvois", getParam("findNoInvois"));
		return map;
	}
	
	@Override
	public void begin() {

		clearcontext();
		
		dataUtil = DataUtil.getInstance(db);
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		KodJuruwang juruwang = (KodJuruwang) db.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "'");
		context.put("juruwang", juruwang);
		
		defaultButtonOption();
		filtering();
		
		context.put("listKodHasil",dataUtil.getListKodHasil());
		context.put("listModBayaran",dataUtil.getCaraBayarKaunter());
		context.put("listBank",dataUtil.getListBank());
		context.put("listNegeri",dataUtil.getListNegeri());
		context.put("selectMesin",dataUtil.getListMesin());
		context.remove("listBandar");
		
		context.put("userRole",userRole);
	}
	
	private void clearcontext() {
		context.remove("r");
		context.remove("rkb");
		context.remove("rsi");
		context.remove("listInvois");
		context.remove("listTempBayar");
		context.remove("listInQueue");
		context.remove("sizeRecord");
	}
	
	private void filtering() {
//		this.addFilter("id in (select y.resit.id from KewResitSenaraiInvois y where y.invois.id in (select z.id from KewInvois z where z.pembayarLainIndividu is not null OR z.pembayarLainSyarikat is not null))");
	}
	
	public void defaultButtonOption() {
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
	
	@Override
	public void getRelatedData(PembayarLain r) {
		
		mp = new MyPersistence();
		mp.begin();
		
		List<KewTempInQueue> listInQueue = null;
		List<KewTempBayar> listTempBayar = null;
		List<KewInvois> listInvois = null;
		int sizeRecord = 0;
		
		try {
			listInvois = mp.list("select x from KewInvois x where x.pembayarLain.id ='" + r.getId()+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y'");
			listInQueue = getListInvoisInQueue(mp, r.getId());
			listTempBayar = getListTempBayaran(mp, r.getId());
			sizeRecord = getSizeRecord(mp, r.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
		context.put("listTempBayar", listTempBayar);
		context.put("listInQueue", listInQueue);
		context.put("listInvois", listInvois);
		context.put("sizeRecord", sizeRecord);
		context.put("listBandar",dataUtil.getListBandar());
	}

	@Override
	public void save(PembayarLain r) throws Exception {
	}
	
	@Override
	public void afterSave(PembayarLain r) { 
		
	}
	
	
	@Command("filterBandar")
	public String filterBandar() throws Exception {
		context.put("listBandar",dataUtil.getListBandar(getParam("negeri")));
		return getPath() + "/bandar.vm";
	}
	
	@Command("addInvois")
	public String addInvois() throws Exception {

		List<KewInvois> listInvois = null;
		
		try {
			
			mp = new MyPersistence();
			mp.begin();
			
			String flagJenisPembayarLain = getParam("flagJenisPembayarLain");
			KewInvois v = new KewInvois();
			int sizeRecord = 0;
			
			v.setKodHasil((KodHasil) mp.find(KodHasil.class, getParam("kodHasil")));
			v.setNoInvois(getParam("noInvois"));
			v.setDebit(Util.getDoubleRemoveComma(getParam("debit")));
			v.setTarikhDari(getDate("tarikhDari"));
			v.setTarikhHingga(getDate("tarikhHingga"));
			v.setKeteranganBayaran(getParam("keteranganBayaran"));
			v.setFlagBayar("T");
			v.setFlagJenisPembayarLain(flagJenisPembayarLain);
			v.setUserPendaftar((Users) mp.find(Users.class, userId));
			
			PembayarLain l = (PembayarLain) mp.find(PembayarLain.class, getParam("id"));
			
			v.setPembayarLain(l);
			
			/*if(flagJenisPembayarLain.equalsIgnoreCase("INDIVIDU")){
				PembayarIndividu i = db.find(PembayarIndividu.class, getParam("id"));
				v.setPembayarLainIndividu(i);
			}else if(flagJenisPembayarLain.equalsIgnoreCase("SYARIKAT")){
				PembayarSyarikat s = db.find(PembayarSyarikat.class, getParam("id"));
				v.setPembayarLainSyarikat(s);
			}*/
			mp.persist(v);
			mp.commit();
			
			listInvois = mp.list("select x from KewInvois x where x.pembayarLain.id ='" + getParam("id")+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y'");
			/*sizeRecord = getSizeRecord(getParam("id"));
			
			context.put("sizeRecord", sizeRecord);*/
			
		
		} catch (Exception e) {
			System.out.println("error addInvois : "+e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("listInvois", listInvois);
		return getPath() + "/maklumatInvois.vm";
	}
	
	@Command("callPopupSenaraiResit")
	public String callPopupSenaraiResit() throws Exception {
		
		List<KewBayaranResit> listResit = null;
			
		try {
			
			mp = new MyPersistence();
			mp.begin();
			
			listResit = mp.list("select x from KewBayaranResit x where x.bilCetakResit=0 and x.id in " +
					"(select y.resit.id from KewResitSenaraiInvois y where y.invois.id in " +
					"(select z.id from KewInvois z where z.pembayarLain.id ='" + get("id") + "'))");
		}catch (Exception e) {
			System.out.println("error callPopupSenaraiResit : "+e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
			
		context.put("listResit", listResit);
		return getPath() + "/popup/senaraiResit.vm"; 
	}
	
	/*@SuppressWarnings("unchecked")
	private List<KewInvois> getListInvois(String payerId, String flagPembayar) throws Exception {
		
		String sql = "";
		if(flagPembayar.equalsIgnoreCase("INDIVIDU")){
			sql = "x.pembayarLainIndividu.id ='" + payerId + "'";
		}else if(flagPembayar.equalsIgnoreCase("SYARIKAT")){
			sql = "x.pembayarLainSyarikat.id ='" + payerId + "'";
		}
		
		List<KewInvois> list = db.list("select x from KewInvois x where " + sql + " and COALESCE(x.flagBayar,'T') <> 'Y' ");
		return list;
	}*/
	
	@Command("simpanSemua")
	public String simpanSemua() throws Exception {
		String errorMakePayment = "T";
		List<KewInvois> listInvoisAsal = null;
		PembayarLain pembayarLain = null;
		try {
			mp = new MyPersistence();
			mp.begin();
			pembayarLain = (PembayarLain) mp.get("select x from PembayarLain x where x.id = '" + get("id")+"'");
			Users juruwang = (Users) mp.find(Users.class, userId);
			KodJuruwang kodj = (KodJuruwang)mp.get("select x from KodJuruwang x where x.juruwang.id = '"+juruwang.getId() + "' and x.flagAktif = 'Y'");
			List<KewTempBayar> listModPayment = getListTempBayaran(mp, get("id"));
			
			Double sum = 0d;
			String kodMesin = null;
			for (int h = 0; h < listModPayment.size(); h++) {
				sum = sum + listModPayment.get(h).getAmaunBayaran();
				kodMesin = listModPayment.get(h).getMesin().getKodMesin();
			}
			
			//create resit
			KewBayaranResit resit = new KewBayaranResit();
			
			if (pembayarLain != null) {
				resit.setPembayar(null);
				resit.setNoPengenalanPembayar(pembayarLain.getId());
				resit.setNamaPembayar(pembayarLain.getNama());				
				resit.setAlamatPembayar(UtilKewangan.getAlamatPembayarLain(pembayarLain));
			}
			
			String noResit = UtilKewangan.generateReceiptNo(mp,juruwang);
			resit.setNoResit(noResit);
			resit.setTarikhBayaran(new Date());
			resit.setTarikhResit(new Date());
			resit.setMasaResit(new Date());
			resit.setFlagJenisBayaran("KAUNTER");
			resit.setKodJuruwang(kodj); // BUAT ADMIN SCREEN TAMBAH USER
			resit.setKodPusatTerima(kodj.getKodPusatTerima());
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar(juruwang);
			resit.setJumlahAmaunBayaran(sum); // JUMLAH DEBIT
			resit.setKodMesin(kodMesin);
			if (kodj != null) {
				resit.setJuruwangKod(kodj.getKodJuruwang());
			}
			resit.setFlagJenisResit("2"); //INVOIS
			mp.persist(resit);
			
			//dapatkan semua invois yg telah di pilih
			List<KewTempInQueue> listInQueue = getListInvoisInQueue(mp, get("id"));
			
	//		List<KewInvois> listInvois = db.list("select x from KewInvois x where x.pembayarLain.id ='" + getParam("id")+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y'");
			
			for(KewTempInQueue v : listInQueue){
			//create invois resit
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setInvois(v.getInvois());
				rsi.setResit(resit);
				rsi.setFlagJenisResit("INVOIS");
				mp.persist(rsi);
				
				KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.id = '" + v.getInvois().getId()+"'");
				inv.setKredit(inv.getDebit());
				inv.setUserKemaskini((Users) mp.find(Users.class, userId));
				inv.setTarikhKemaskini(new Date());
				inv.setFlagBayar("Y");
				mp.persist(inv);
				
				mp.remove(v);
			}
			
			// create record senarai kaedah bayaran dalam 1 resit
			for (int j = 0; j < listModPayment.size(); j++) {
				createRecordModBayaran(mp, listModPayment.get(j), resit);
				mp.remove(listModPayment.get(j));
			}
			
				/*//create kaedah bayaran
			KewResitKaedahBayaran rkb = new KewResitKaedahBayaran();
			rkb.setResit(resit);
			rkb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
			rkb.setBank(db.find(Bank.class, getParam("bank")));
			rkb.setModBayaran(db.find(CaraBayar.class, getParam("modBayaran")));
			rkb.setNoRujukan(getParam("noRujukan"));
			rkb.setTempat(getParam("tempat"));
			rkb.setNoCek(getParam("noCek"));
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy"); 
			try {
				String tarikhCek = getParam("tarikhCek");
				if(!tarikhCek.equalsIgnoreCase("") && tarikhCek.length() > 0)
					rkb.setTarikhCek(df.parse(getParam("tarikhCek")));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			db.persist(rkb);*/			
			listInvoisAsal = getListInvoisAsal(mp, get("id"));
			
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("error simpanSemua : "+e.getMessage());
			e.printStackTrace();
			errorMakePayment = "Y";
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("listInvois", listInvoisAsal);
		context.put("r", pembayarLain);
		context.put("listBandar",dataUtil.getListBandar());
		context.put("errorMakePayment", errorMakePayment);
		return getPath() + "/entry_page.vm";
	}
	
	private void createRecordModBayaran(MyPersistence mp, KewTempBayar temp, KewBayaranResit resit) throws Exception {
		KewResitKaedahBayaran rkb = new KewResitKaedahBayaran();
		rkb.setResit(resit);
		rkb.setAmaunBayaran(temp.getAmaunBayaran());
		rkb.setBank(temp.getBank());
		rkb.setModBayaran(temp.getModBayaran());
		// rkb.setNoCek();
		// rkb.setNoLoTempahan();
		rkb.setNoRujukan(temp.getNoRujukan());
		rkb.setTempat(temp.getTempat());
		rkb.setTarikhCek(temp.getTarikhCek());
		rkb.setNoCek(temp.getNoCek());
		mp.persist(rkb);
		
		}
	
	@Command("hapusTempBayaran")
	public String hapusTempBayaran() throws Exception {
		int sizeRecord = 0;
		List<KewTempBayar> listTempBayar = null;
		try{

			mp = new MyPersistence();
			mp.begin();
			
			hapusMaklumatBayaran(mp, getParam("idtempBayaran"));
			listTempBayar = getListTempBayaran(mp, get("id"));
			// listInvois = getListInvois(payerId);
			sizeRecord = getSizeRecord(mp, get("id"));
			mp.commit();
		}catch (Exception e) {
			System.out.println("error hapusTempBayaran : "+e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("sizeRecord", sizeRecord);
		context.put("listTempBayar", listTempBayar);
		
		return getPath() + "/maklumatBayaran.vm";
	}
	
	@Command("hapusInvois")
	public String hapusInvois() throws Exception {
		
		int sizeRecord = 0;
		List<KewInvois> listInvois = null;
		try{

			mp = new MyPersistence();
			mp.begin();
			
			String flagJenisPembayarLain = getParam("flagJenisPembayarLain");
			KewInvois v = (KewInvois) mp.find(KewInvois.class, getParam("idInvois"));

			
			mp.remove(v);
	
			listInvois = mp.list("select x from KewInvois x where x.pembayarLain.id ='" + getParam("id")+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y'");
			sizeRecord = getSizeRecord(mp, getParam("id"));
			
			mp.commit();
			} catch (Exception e) {
				System.out.println("error hapusInvois : " + e.getMessage());
				e.printStackTrace();
			}finally{
				if (mp != null) { mp.close(); }
			}
		
		context.put("sizeRecord", sizeRecord);
		context.put("listInvois", listInvois);
		
		return getPath() + "/maklumatInvois.vm";
	}
	
	@Command("simpanPembayar")
	public String simpanPembayar() throws Exception {
		
		String statusInfo = "";
		String flagJenisPembayarLain = getParam("flagJenisPembayarLain");
		int sizeRecord = 0;
		
		try{

			mp = new MyPersistence();
			mp.begin();
		
			//save maklumat pembayar
			Users user = (Users) mp.find(Users.class, getParam("id"));
			PembayarLain i = (PembayarLain) mp.find(PembayarLain.class, getParam("id"));
			
			if(user == null){
				if(i == null){
					i = new PembayarLain();
					i.setId(getParam("id"));
					i.setNama(getParam("nama"));
					i.setAlamat1(getParam("alamat1"));
					i.setAlamat2(getParam("alamat2"));
					i.setAlamat3(getParam("alamat3"));
					i.setPoskod(getParam("poskod"));
					i.setBandar((Bandar) mp.find(Bandar.class, getParam("bandar")));
					i.setFlagJenisPembayarLain(flagJenisPembayarLain);
					mp.persist(i);
					
					context.put("r", i);
					
				}else{
					statusInfo = "error";
					context.put("command", "add_new_record");
				}
			}else{
				statusInfo = "error2";
				context.put("command", "add_new_record");
			}
			
			List<KewInvois> listInvois = mp.list("select x from KewInvois x where x.pembayarLain.id ='" + getParam("id")+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y'");
			sizeRecord = getSizeRecord(mp, getParam("id"));
			
			mp.commit();
			} catch (Exception e) {
				System.out.println("error simpanPembayar : " + e.getMessage());
				e.printStackTrace();
			}finally{
				if (mp != null) { mp.close(); }
			}
			
			context.put("sizeRecord", sizeRecord);
			context.put("listBandar",dataUtil.getListBandar());
			context.put("statusInfo", statusInfo);
			context.put("flagJenisPembayarLain", flagJenisPembayarLain);
			return getPath() + "/entry_page.vm";
	}
	
	@Command("editPembayar")
	public String editPembayar() throws Exception {
		
		String statusInfo = "";
		String flagJenisPembayarLain = getParam("flagJenisPembayarLain");
		int sizeRecord = 0;
		PembayarLain i = null;
		try{

			mp = new MyPersistence();
			mp.begin();
		
		//save maklumat pembayar
			i = (PembayarLain) mp.find(PembayarLain.class, getParam("id"));
		
			i.setNama(getParam("nama"));
			i.setAlamat1(getParam("alamat1"));
			i.setAlamat2(getParam("alamat2"));
			i.setAlamat3(getParam("alamat3"));
			i.setPoskod(getParam("poskod"));
			i.setBandar((Bandar) mp.find(Bandar.class, getParam("bandar")));
			i.setFlagJenisPembayarLain(flagJenisPembayarLain);
			mp.persist(i);
		
			mp.commit();
		} catch (Exception e) {
			System.out.println("error editPembayar : " + e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", i);
		context.put("listBandar",dataUtil.getListBandar());
		return getPath() + "/editPembayar.vm";
	}
	
	@Command("savePilihanBayar")
	public String savePilihanBayar() throws Exception {
		
		String idPilihan = getParam("idPilihan");
		KewInvois ki = null;
		int sizeRecord = 0;
		List<KewInvois> listInvois = null;
		try{
			mp = new MyPersistence();
			mp.begin();
			
			//set flag queue di KewInvois
			ki = (KewInvois) mp.find(KewInvois.class, idPilihan);
			ki.setFlagQueue("Y");
			PembayarLain l = ki.getPembayarLain();
			
			// create temp queue
			KewTempInQueue q = new KewTempInQueue();
	
			q.setInvois(ki);
			q.setPembayarLain(l);
			mp.persist(q);
			// checking kalau bercampur jenis bayaran
	
			listInvois = mp.list("select x from KewInvois x where x.pembayarLain.id ='" + getParam("id")+ "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y'");
			sizeRecord = getSizeRecord(mp, getParam("id"));

			mp.commit();
			
		}catch (Exception e) {
			System.out.println("error savePilihanBayar : " + e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
		context.put("sizeRecord", sizeRecord);
		context.put("listInvois", listInvois);
		return getPath() + "/maklumatInvois.vm";
	}
	
	@Command("tambahBayaran")
	public String tambahBayaran() throws Exception {
		List<KewTempBayar> listTempBayar = null;
		int sizeRecord = 0;
		try{
			mp = new MyPersistence();
			mp.begin();
			
			simpanMaklumatBayaran(mp);
			listTempBayar = getListTempBayaran(mp, get("id"));
			// listInvois = getListInvois(payerId);
			sizeRecord = getSizeRecord(mp, get("id"));
		}catch (Exception e) {
			System.out.println("error tambahBayaran : " + e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
		context.put("sizeRecord", sizeRecord);
		context.put("listTempBayar", listTempBayar);
		return getPath() + "/maklumatBayaran.vm";
	}
	
	@Command("refreshDivQueue")
	public String refreshDivQueue() throws Exception {
		mp = new MyPersistence();
		mp.begin();
		
		List<KewTempInQueue> listInQueue = getListInvoisInQueue(mp, get("id"));
		List<KewTempBayar> listTempBayar = getListTempBayaran(mp, get("id"));
		if (mp != null) { mp.close(); }
		context.put("listTempBayar", listTempBayar);
		context.put("listInQueue", listInQueue);
		return getPath() + "/maklumatInvoisPilihan.vm";
	}
	
	@Command("refreshMaklumatBayaran")
	public String refreshMaklumatBayaran() throws Exception {
		int sizeRecord = 0;
		mp = new MyPersistence();
		mp.begin();
		sizeRecord = getSizeRecord(mp, get("id"));
		if (mp != null) { mp.close(); }
		context.put("sizeRecord", sizeRecord);
		return getPath() + "/maklumatBayaran.vm";
	}
	
	@Command("savePilihanRemoveQueue")
	public String savePilihanRemoveQueue() throws Exception {
		List<KewTempInQueue> listInQueue = null;
		try{
			mp = new MyPersistence();
			mp.begin();
			
			String idPilihan = getParam("idPilihan");
			KewTempInQueue q = (KewTempInQueue) mp.find(KewTempInQueue.class, idPilihan);
			
			q.getInvois().setFlagQueue("T");
			mp.remove(q);
			
	
			listInQueue = getListInvoisInQueue(mp, get("id"));
			mp.commit();
		}catch (Exception e) {
			System.out.println("error savePilihanRemoveQueue : " + e.getMessage());
			e.printStackTrace();
		}finally{
			if (mp != null) { mp.close(); }
		}
			
		context.put("listInQueue", listInQueue);
		return getPath() + "/maklumatInvoisPilihan.vm";
	}
	
	private void simpanMaklumatBayaran(MyPersistence mp) throws Exception {
		
		try{
			mp = new MyPersistence();
			mp.begin();
			
			KewTempBayar temp = new KewTempBayar();
			temp.setPembayarLain((PembayarLain) mp.find(PembayarLain.class, getParam("id")));
			temp.setAmaunBayaran(Util.getDoubleRemoveComma(getParam("amaunBayaran")));
			temp.setBank((Bank) mp.find(Bank.class, getParam("bank")));
			temp.setModBayaran((CaraBayar) mp.find(CaraBayar.class, getParam("modBayaran")));
			temp.setNoRujukan(getParam("noRujukan"));
			temp.setTempat(getParam("tempat"));
			temp.setMesin((KodMesin) mp.find(KodMesin.class, getParam("mesin")));
			temp.setNoCek(getParam("noCek"));
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String tarikhCek = getParam("tarikhCek");
			if(!tarikhCek.equalsIgnoreCase("") && tarikhCek.length() > 0)
				temp.setTarikhCek(df.parse(tarikhCek));
		
			mp.persist(temp);
			mp.commit();
			} catch (Exception e) {
				System.out.println("error simpanMaklumatBayaran : " + e.getMessage());
				e.printStackTrace();
			}finally{
				if (mp != null) { mp.close(); }
			}
	}
	
	private void hapusMaklumatBayaran(MyPersistence mp, String idtempBayaran) {
		KewTempBayar temp = (KewTempBayar) mp.find(KewTempBayar.class, idtempBayaran);
		mp.remove(temp);
	}
	
	@Command("refreshDivAsal")
	public String refreshDivAsal() throws Exception {
		mp = new MyPersistence();
		mp.begin();
		
		List<KewInvois> listInvoisAsal = getListInvoisAsal(mp, get("id"));
		
		context.put("listInvois", listInvoisAsal);
		if (mp != null) { mp.close(); }
		return getPath() + "/maklumatInvois.vm";
	}
	
	@SuppressWarnings("unchecked")
	private List<KewTempInQueue> getListInvoisInQueue(MyPersistence mp, String payerId) throws Exception {
		// List<KewInvois> list =
		// db.list("select x from KewInvois x where x.pembayar.id = '"+payerId+"' and COALESCE(x.flagQueue,'T') = 'Y' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		List<KewTempInQueue> list = mp.list("select x from KewTempInQueue x where x.pembayarLain.id = '" + payerId + "' ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<KewInvois> getListInvoisAsal(MyPersistence mp, String payerId) throws Exception {
		
		List<KewInvois> list = mp.list("select x from KewInvois x where x.pembayarLain.id = '" + payerId + "' and COALESCE(x.flagQueue,'T') = 'T' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private List<KewTempBayar> getListTempBayaran(MyPersistence mp, String payerId) throws Exception {
		List<KewTempBayar> list = mp.list("select x from KewTempBayar x where x.pembayarLain.id = '" + payerId + "' ");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private int getSizeRecord(MyPersistence mp, String payerId) throws Exception {

		int size = 0;

		List<KewInvois> list = mp.list("select x from KewInvois x where x.pembayarLain.id = '" + payerId + "' and COALESCE(x.flagBayar,'T') <> 'Y' ");
		if (list != null) {
			for (KewInvois v : list){
				size = size + 1;
			}
		}

		return size;
	}
}




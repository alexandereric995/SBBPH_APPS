package bph.modules.rpp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.KodHasil;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppKemudahan;
import bph.entities.rpp.RppPenggunaanKemudahan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class RppPenggunaanKemudahanRecordModule extends
		LebahRecordTemplateModule<RppPenggunaanKemudahan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppPenggunaanKemudahan r) {
		//rozai tambah-25/6/2015
		RppKemudahan kemudahan=null;
		String idKemudahan = "";
		if (get("kemudahan").trim().length() > 0)
			idKemudahan = get("kemudahan");
		kemudahan = db.find(RppKemudahan.class, idKemudahan);
		
		double sewa=kemudahan.getKadarSewa();
		int kuantiti=r.getBilangan();
		Date tarikhMula=r.getTarikhMulaGuna();
		Date tarikhTamat=r.getTarikhTamatGuna();
		
		long diff=tarikhTamat.getTime()-tarikhMula.getTime();
		long jumlahHari=(diff/(24*60*60*1000))+1;
		double jumlahKadarSewa=jumlahHari*sewa*kuantiti;
		//r.setJumlahKadarSewa(jumlahKadarSewa);
	
		//rozai create ledger
		db.begin();
		RppAkaun mn = new RppAkaun();
		mn.setAmaunBayaranSeunit(sewa);
		mn.setBilanganUnit(kuantiti);
		mn.setDebit(jumlahKadarSewa);
		mn.setKredit(0d);
		mn.setFlagBayar("T");
		mn.setFlagVoid("T");
		mn.setKeterangan("SEWAAN "+r.getKemudahan().getNama()+" DI "+r.getKemudahan().getPeranginan().getNamaPeranginan());
		mn.setKodHasil(db.find(KodHasil.class, "29199")); //BAYARAN-BAYARAN SEWA YANG LAIN
		mn.setNoInvois(r.getId()); //TEMPORARY SET TO ID PERMOHONAN
		mn.setTarikhInvois(new Date());
		mn.setIdMasuk(db.find(Users.class, userId));
		mn.setTarikhMasuk(new Date());
		db.persist(mn);
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();

			RppPeranginan peranginan = null;
			String idrp = "";
			if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
				idrp = UtilRpp.getPeranginanByIdPenyelia(mp,userId)!=null?UtilRpp.getPeranginanByIdPenyelia(mp,userId).getId():"";
				if(idrp!=null && !idrp.equalsIgnoreCase("")){
					peranginan = db.find(RppPeranginan.class, idrp);
					this.setDisableAddNewRecordButton(false);
				}else{
					this.setDisableAddNewRecordButton(true);
				}
				this.addFilter("kemudahan.peranginan.id = '" +idrp+ "'");
			}
			
			context.put("rppPeranginan", peranginan);
			context.put("listKemudahan", dataUtil.getListKemudahan(idrp));
			context.put("kadarSewa", 0d);
			context.put("bilangan", 0);
			
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("userRole",userRole);
			context.put("command", command);
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	
	}

	@Override
	public boolean delete(RppPenggunaanKemudahan r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;		
		}
		return val;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/rppPenggunaanKemudahan";
	}

	@Override
	public Class<RppPenggunaanKemudahan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppPenggunaanKemudahan.class;
	}

	@Override
	public void getRelatedData(RppPenggunaanKemudahan r) {
		context.put("objUser","");
		String idrp = r.getKemudahan()!=null?r.getKemudahan().getPeranginan().getId():"";
		context.put("kemudahan", r.getKemudahan());
		context.put("listPermohonan", dataUtil.getListPermohonanCheckin(idrp));
		//context.put("listPemohon", dataUtil.getListPemohonRpp());
	}

	@Override
	public void save(RppPenggunaanKemudahan r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String flagJenisPemohon = getParam("flagJenisPemohon");

		if (flagJenisPemohon.equalsIgnoreCase("MENGINAP")) {
			RppPermohonan rp = db.find(RppPermohonan.class,
					getParam("permohonan"));
			r.setPemohon(rp.getPemohon());
			r.setPermohonan(rp);
		} else {
			Users us = db.find(Users.class, getParam("pemohon"));
			r.setPemohon(us);
			r.setPermohonan(null);
		}

		r.setBilangan(getParamAsInteger("bilangan"));
		r.setCatatan(getParam("catatan"));
		r.setFlagJenisPemohon(flagJenisPemohon);
		r.setJumlahKadarSewa(Util.getDoubleRemoveComma(getParam("kadarSewa")));
		r.setKemudahan(db.find(RppKemudahan.class, getParam("kemudahan")));
		r.setTarikhMulaGuna(getDate("tarikhMulaGuna"));
		r.setTarikhTamatGuna(getDate("tarikhTamatGuna"));
		//kadarSewa(r);
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.userName", getParam("findNama"));
		map.put("pemohon.noKP", getParam("findNoKP"));
		map.put("kemudahan.id", getParam("findKemudahan"));

		return map;
	}

	@Command("filterKategoriPemohon")
	public String filterKategoriPemohon() throws Exception {

		userId = (String) request.getSession().getAttribute("_portal_login");
		String vm = "";
		try{
			mp = new MyPersistence();

			String flagJenisPemohon = getParam("flagJenisPemohon");
			String idrp = "";
			if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
				idrp = UtilRpp.getPeranginanByIdPenyelia(mp,userId)!=null?UtilRpp.getPeranginanByIdPenyelia(mp,userId).getId():"";
			}
			
			if (flagJenisPemohon.equalsIgnoreCase("MENGINAP")) {
				context.put("listPermohonan", dataUtil.getListPermohonanCheckin(idrp));
				vm = "/pilihanPermohonan.vm";
			} else {
				//context.put("listPemohon", dataUtil.getListPemohonRpp());
				vm = "/pilihanPemohon.vm";
			}
			
		}catch (Exception ex){
			System.out.println("ERROR filterKategoriPemohon : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}	
		context.put("objUser","");
		return getPath() + vm;
	}

	// rozai tambah-24/6/2015
	@Command("findKadarSewa")
	public String findKadarSewa() throws Exception {
		String idKemudahan = "";
		if (get("kemudahan").trim().length() > 0)
			idKemudahan = get("kemudahan");
		RppKemudahan kemudahan = null;
		kemudahan = db.find(RppKemudahan.class, idKemudahan);
		context.put("kemudahan", kemudahan);
		return getPath() + "/maklumatDetailKemudahan.vm";
	}
	
	//rozai tambah-25/6/2015
	public void kadarSewa(RppPenggunaanKemudahan r) throws Exception {
		if (get("bilangan").trim().length() > 0 && get("kadarSewa").trim().length() > 0)
		{	
			RppKemudahan kemudahan=null;
			String idKemudahan = "";
			if (get("kemudahan").trim().length() > 0)
				idKemudahan = get("kemudahan");
			kemudahan = db.find(RppKemudahan.class, idKemudahan);
			
			double sewa=kemudahan.getKadarSewa();
			double kuantiti=r.getBilangan();
			Date tarikhMula=r.getTarikhMulaGuna();
			Date tarikhTamat=r.getTarikhTamatGuna();
			
			long diff=tarikhTamat.getTime()-tarikhMula.getTime();
			long jumlahHari=(diff/(24*60*60*1000))+1;
			double jumlahKadarSewa=jumlahHari*sewa*kuantiti;
			r.setJumlahKadarSewa(jumlahKadarSewa);
		}
	}
	
	@Command("callPopupCarianTetamu")
	public String callPopupCarianTetamu() throws Exception {
		String carianTetamu = getParam("carianTetamu");
		List<Users> userList = searchUsers(carianTetamu);
		context.put("userList", userList);
		return getPath() + "/popup/popupCarianTetamu.vm";
	}
	
	private List<Users> searchUsers(String param) {
		ArrayList<Users> list = new ArrayList<Users>();
		Db db1 = null;
		try {
			db1 = new Db();
			String sql = "SELECT DISTINCT a.user_login FROM users a "+
						 " WHERE (upper(user_name) LIKE upper('%"+param+"%') OR upper(no_kp) LIKE upper('%"+param+"%')) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				Users us = db.find(Users.class, rs.getString("user_login"));
				list.add(us);
			}	
			
		}catch(Exception e){
			System.out.println("error searchUsers : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	@Command("savePilihanTetamu")
	public String savePilihanTetamu() throws Exception {
		String guestId = getParam("radTetamu");
		Users objUser = db.find(Users.class, guestId);
		context.put("objUser", objUser);
		return getPath() + "/maklumatTetamu.vm";
	}
	
}

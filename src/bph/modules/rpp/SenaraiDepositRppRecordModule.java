package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SenaraiDepositRppRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void afterSave(RppPermohonan r) { }

	@Override
	public void beforeSave() {}

	@Override
	public String getPath() {return "bph/modules/rpp/pendeposit";}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class;}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try{
			mp = new MyPersistence();
			defaultButtonOption();
			filtering(mp,userId,userRole);
		}catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("listStatus",dataUtil.getListStatusPulanganDeposit());
		context.put("listPeranginan",dataUtil.getListPeranginanRpp());
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		
		this.setOrderBy("tarikhKeluarRpp desc");
	}
	
	private void filtering(MyPersistence mp, String userId, String userRole) {
		String currentdate = Util.getDateTime(new Date(), "yyyy-MM-dd");
		String dateOnSystem = "2015-08-17";
		this.addFilter("statusBayaran = 'Y' ");
		this.addFilter("noTempahan is not null");
		this.addFilter("status.id in ('1425259713415','1425259713421','1425259713424','1435512646303','1433083787409','1435093978588') "); //PERMOHONAN LULUS & DAFTAR MASUK & DAFTAR KELUAR & SELESAI & TAK HADIR
		//this.addFilter("status.id in ('1425259713424','1435512646303') "); //DAFTAR KELUAR & SELESAI
		//Kecuali pd dan langkawi
		this.addFilter("rppPeranginan.id not in ('3','14','11','12','13')");
		
		//Kecuali Dewan2..
		this.addFilter("jenisUnitRpp.id not in ('1439720204760','1439720205052','1439720205055','1439720205058','1439720205061','1439720205064','1439720205074','1439720205077') ");
		
		//Data baru sahaja
		this.addFilter("tarikhPermohonan >= '"+dateOnSystem+"'");
		
		//Record utk penyelia
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			this.addFilter("tarikhKeluarRpp <= '"+currentdate+"'");
			String listPeranginan = UtilRpp.multipleListSeliaan(mp,userId);
			if (listPeranginan.length() == 0) {
				this.addFilter("rppPeranginan.id = ''");
			} else {
				this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
			}	
			this.setOrderBy("rppPeranginan.namaPeranginan asc");
		}else{
			this.addFilter("tarikhKeluarRpp < '"+currentdate+"'");
		}
		
	}
	
	private void defaultButtonOption() {
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setReadonly(true);
		this.setDisableKosongkanUpperButton(true);
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rppPeranginan.id", getParam("findPeranginan"));
		map.put("pemohon.noKP", getParam("findNoKP"));
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		//map.put("getObjTuntutanDeposit().status.id", getParam("findStatus"));
		return map;
	}

	@Override
	public boolean delete(RppPermohonan r) throws Exception {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppPermohonan r) {
		context.remove("listRosak");
		try{
			mp = new MyPersistence();
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			
			RppAkaun lejar = (RppAkaun) mp.get("select w from RppAkaun w where w.permohonan.id = '"+r.getId()+"' and w.kodHasil.id = '72311' ");
			KewDeposit dep = null;
			if(lejar!=null){
				dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lejar.getId()+"' ");
			}
			
			List<RppAduanKerosakan> listRosak = mp.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("listRosak",listRosak);
			
			context.put("r", rr);
			context.put("dep",dep);
			
		}catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	@Override
	public void save(RppPermohonan r) throws Exception { }
	
	@SuppressWarnings("unchecked")
	@Command("simpanPulanganDeposit")
	public String simpanPulanganDeposit() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try{
			mp = new MyPersistence();
			
			String permohonanId = getParam("permohonanId");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, permohonanId);
			
			String depositId = getParam("depositId");
			KewDeposit dep = (KewDeposit) mp.find(KewDeposit.class, depositId);
			
			if(dep!=null){
				KewTuntutanDeposit t = new KewTuntutanDeposit();
				
				mp.begin();
				
				r.setStatus((Status) mp.find(Status.class, "1435512646303")); //status permohonan to selesai
				t.setDeposit(dep);
				t.setJenisTuntutan((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
				t.setPenuntut(r.getPemohon());
				t.setStatus((Status) mp.find(Status.class, "1436464445665")); //Permohonan Deposit
				t.setTarikhPermohonan(new Date());
				t.setSuratPengesahanDeposit("Y");
				t.setCatatanPenyeliaRpp(getParam("catatanPenyeliaRpp"));
				Users pengguna=(Users) mp.find(Users.class, userId);
				t.setIdDaftar(pengguna);
				mp.persist(t);
				dep.setTuntutanDeposit(t);
				mp.commit();
			}
			
			List<RppAduanKerosakan> listRosak = mp.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+r.getId()+"' ");
			
			context.put("listRosak",listRosak);
			context.put("r", r);
			context.put("dep",dep);
			
		}catch (Exception e) {
			System.out.println("Error simpanPulanganDeposit : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return templateDir + "/entry_fields.vm";
	}
	
}





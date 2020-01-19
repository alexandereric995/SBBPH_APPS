package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.util.Util;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kod.Status;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppPermohonan;
import bph.utils.DataUtil;
import bph.utils.HTML;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

/**
 * TIDAK HADIR (PEMBAYARAN TELAH DIBUAT)
 * - UPDDATE FLAG JADUAL TEMPAHAN KEPADA "NOSHOW"
 * */
public class NoShowRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void afterSave(RppPermohonan r) {
//		try {
//			simpanPulanganDeposit(r);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void beforeSave() {
		try {
			simpanPulanganDeposit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getPath() {return "bph/modules/rpp/noShow";}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class;}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		defaultButtonOption();
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		try{
			mp = new MyPersistence();
			filtering(mp,userId,userRole);
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		try {
			context.put("carianJenisPeranginan", HTML.SelectJenisPeranginan("carianJenisPeranginan",null, "id=\"carianJenisPeranginan\" style=\"width:100%\" ", "onchange=\" doFilterCarianPeranginan(); \""));
			context.put("carianRppPeranginan", HTML.SelectPeranginanByJenisPeranginan(null,"carianRppPeranginan",null, "id=\"carianRppPeranginan\" style=\"width:100%\" ", "onchange=\"at(this, event);\""));
			context.put("carianStatusNoShow", HTML.SelectStatusNoShow("carianStatusNoShow",null, "id=\"carianStatusNoShow\" style=\"width:100%\" ", ""));
		} catch (Exception e) {
			System.out.println("error getting dropdown list "+e.getMessage());
		}
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	

	private void filtering(MyPersistence mp, String userId, String userRole) {
		String currentdate = Util.getDateTime(new Date(), "yyyy-MM-dd");
		this.addFilter("statusBayaran = 'Y' "); //dah bayar
		this.addFilter("status.id in ('1425259713415','1433083787409')"); //lulus & noshow
		this.addFilter("jenisPemohon = 'INDIVIDU' ");
		this.addFilter("tarikhMasukRpp < '"+currentdate+"' ");
		
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			String listPeranginan = UtilRpp.multipleListSeliaan(mp,userId);
			if (listPeranginan.length() == 0) {
				this.addFilter("rppPeranginan.id = ''");
			} else {
				this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
			}	
			this.setOrderBy("rppPeranginan.namaPeranginan asc");
		}
		
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String carianRppPeranginan = getParam("carianRppPeranginan");
		String carianJenisPeranginan = getParam("carianJenisPeranginan");
		String carianStatusNoShow = getParam("carianStatusNoShow");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rppPeranginan.id", carianRppPeranginan);
		map.put("rppPeranginan.jenisPeranginan.id", carianJenisPeranginan);
		map.put("status.id", carianStatusNoShow);
		map.put("pemohon.noKP", getParam("noKP"));
		map.put("pemohon.userName", getParam("userName"));
		//FATIN
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("tarikhMasukRpp"), getDate("tarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("tarikhMasukRpp"), getDate("tarikhKeluarRpp")));
		return map;
	}

	@Override
	public boolean delete(RppPermohonan r) throws Exception {return false;}

	@Override
	public void getRelatedData(RppPermohonan r) {
		context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(db,r));
	}
	
	@Override
	public void save(RppPermohonan r) throws Exception {
		
		if(!r.getStatus().getId().equalsIgnoreCase("1433083787409")){
			r.setStatus(db.find(Status.class, "1433083787409")); //STATUS TIDAK HADIR
			RppJadualTempahan jadual = (RppJadualTempahan) db.get("select x from RppJadualTempahan x where x.permohonan.id = '"+r.getId()+"' ");
			jadual.setFlagStatusTempahan("NOSHOW");
		}
		r.setCatatanTidakHadir(getParam("catatanTidakHadir"));
	}

	@SuppressWarnings("unchecked")
	@Command("simpanPulanganDeposit")
	public void simpanPulanganDeposit() throws Exception {
		Boolean addNew=false;
		userId = (String) request.getSession().getAttribute("_portal_login");
		try{
			mp = new MyPersistence();
			
			String idRppPermohonan = getParam("idRppPermohonan");
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			String noTempahan = r.getNoTempahan();
			KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.noInvois = '"+noTempahan+"' ");
			KewTuntutanDeposit t= (KewTuntutanDeposit) mp.get("select x from KewTuntutanDeposit x where x.deposit.id = '"+dep.getId()+"' ");
			mp.begin();	
			if(t==null){
				t = new KewTuntutanDeposit();
				addNew=true;
			}
			r.setStatus((Status) mp.find(Status.class, "1433083787409"));//TAK HADIR
			t.setDeposit(dep);
			t.setJenisTuntutan((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
			t.setPenuntut(r.getPemohon());
			t.setStatus((Status) mp.find(Status.class, "1436464445665"));
			t.setTarikhPermohonan(new Date());
			t.setSuratPengesahanDeposit("Y");
			t.setCatatanPenyeliaRpp(getParam("catatanPenyeliaRpp"));
			t.setIdDaftar((Users) mp.find(Users.class, userId));
			t.setTarikhDaftar(new Date());
			if(addNew){
				mp.persist(t);
			}
			
			dep.setTuntutanDeposit(t);
			/**Notifikasi kepada HQ untuk tuntutan deposit*/
			//List<Role> roles = mp.list("select x from Role x where x.name in ('(RPP) Penyemak','(RPP) Pelulus') ");
			//UtilRpp.saveNotifikasi(mp,roles,r.getId(),"Y",getClass().getName(),"TUNTUTAN_DEPOSIT");
			mp.commit();	
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	@Command("filterCarianRpp")
	public String filterCarianRpp() throws Exception {
		String carianJenisPeranginan = getParam("carianJenisPeranginan");
		context.put("carianRppPeranginan", HTML.SelectPeranginanByJenisPeranginan(carianJenisPeranginan,"carianRppPeranginan",null, "id=\"carianRppPeranginan\" style=\"width:100%\" ", "onchange=\"at(this, event);\""));
		return getPath() + "/divCarianPeranginan.vm";
	}
}

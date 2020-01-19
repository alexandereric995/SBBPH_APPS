package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import bph.entities.kod.Status;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SUBKelulusanRP extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void beforeSave() {}

	@Override
	public String getPath() {return "bph/modules/rpp/subKelulusanRp";}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class;}
	
	@Override
	public boolean delete(RppPermohonan r) throws Exception { return false; }
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.noKP", getParam("findNoKp"));
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("rppPeranginan.id", getParam("findPeranginan"));
		//FATIN
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		return map;
	}
	
	@Override
	public void begin() {
		try {
			mp = new MyPersistence();
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		this.addFilter("status.id in ('1430809277096')");  //tunggu kelulusan sub
		this.addFilter("jenisPemohon = 'INDIVIDU' ");
		diffFiltering();
		defaultButtonOption();
		
		Users user = (Users)mp.find(Users.class, userId);
		context.put("user", user);
		
		listPeranginan();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		
		this.setOrderBy("tarikhPermohonan desc");
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	public void listPeranginan() {
		List<RppPeranginan> lst = mp.list("select x from RppPeranginan x where x.jenisPeranginan.id = 'RP' and x.flagKelulusanSub = 'Y' ");
		context.put("selectPeranginan", lst);
	}
	
	public void diffFiltering() {
		this.addFilter("rppPeranginan.jenisPeranginan.id = 'RP' ");
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}

	@Override
	public void getRelatedData(RppPermohonan r) { 
		String userId = (String) request.getSession().getAttribute("_portal_login");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			context.put("r", rr);
			
			/**Read notification*/
			mp.begin();
			UtilRpp.readNotification(mp,r.getId(),userRole,userId,"TEMPAHAN_PREMIER_BARU");
			mp.commit();
			
			/** ADD BY PEJE - TO CHECK KEOSONGAN UNIT SEBELUM LULUS **/
			toCheckKekosonganUnit(r, mp);
			
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}
	
	private void toCheckKekosonganUnit(RppPermohonan r, MyPersistence mp) {
		List<RppUnit> listUnitAvailable = mp.list("select x from RppUnit x where x.jenisUnit.id = '"+r.getJenisUnitRpp().getId()+"' and COALESCE(x.status,'') <> 'RESERVED' ");
		context.put("listUnitAvailable",listUnitAvailable);
		context.put("tarikhMula", r.getTarikhMasukRpp());
		context.put("tarikhAkhir", r.getTarikhKeluarRpp());
		context.put("dtfrom", Util.getDateTime(r.getTarikhMasukRpp(), "yyyy-MM-dd"));
		context.put("dtto", Util.getDateTime(r.getTarikhKeluarRpp(), "yyyy-MM-dd"));
	}

	@Override
	public void save(RppPermohonan r) throws Exception {
		try {
			mp = new MyPersistence();
			String idPelulus = (String) request.getSession().getAttribute("_portal_login");
			String idStatus = "";
			Users pelulus=(Users)mp.find(Users.class, idPelulus);
			String flagKelulusanSub = getParam("flagKelulusanSub");
			r.setTarikhKelulusanSub(getDate("tarikhKelulusanSub"));
			r.setFlagKelulusanSub(flagKelulusanSub);
			r.setCatatanSub(getParam("catatanSub"));
			r.setPelulusPremier(pelulus);
			r.setTarikhKemaskini(new Date());
			if(flagKelulusanSub.equalsIgnoreCase("Y")){
				idStatus = "1430809277102";
			}else{
				idStatus = "1430809277099";
			}
			Status status=(Status)mp.find(Status.class, idStatus);
			r.setStatus(status);
		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	@Override
	public void afterSave(RppPermohonan r) {
		if(r.getFlagKelulusanSub().equalsIgnoreCase("Y")){
			try {
				mp = new MyPersistence();
				UtilRpp.saveTarikhAkhirBayaranInSql(r);
				UtilRpp.daftarChalet(mp,r);
				UtilRpp.createRecordBayaran(mp,r.getPemohon().getId(),r);
			} catch (Exception e) {
				System.out.println(":: ERROR afterSave KELULUSAN SUB "+e.getMessage());
			} finally{
				if (mp != null) { mp.close(); }
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Command("openPopupDetailKekosongan")
	public String openPopupDetailKekosongan() throws Exception {
		try {
			mp = new MyPersistence();
			String idpermohonan = getParam("idpermohonan");
			RppPermohonan r = (RppPermohonan)mp.find(RppPermohonan.class, idpermohonan);
			List<RppUnit> listUnitAvailable = mp.list("select x from RppUnit x where x.jenisUnit.id = '"+r.getJenisUnitRpp().getId()+"' and COALESCE(x.status,'') <> 'RESERVED' ");
			context.put("listUnitAvailable",listUnitAvailable);
			context.put("tarikhMula", r.getTarikhMasukRpp());
			context.put("tarikhAkhir", r.getTarikhKeluarRpp());
			context.put("dtfrom", Util.getDateTime(r.getTarikhMasukRpp(), "yyyy-MM-dd"));
			context.put("dtto", Util.getDateTime(r.getTarikhKeluarRpp(), "yyyy-MM-dd"));
		} catch (Exception e) {
			System.out.println(":: ERROR afterSave KELULUSAN SUB "+e.getMessage());
		} finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/popup/senaraiKekosonganByUnit.vm";
	}
}
package bph.modules.rpp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.utils.DataUtil;
import bph.utils.UtilRpp;
import bph.utils.UtilRpp.SummaryRp;
import db.persistence.MyPersistence;


public class TempahanRT extends TempahanRPRecordModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	/**BILANGAN HAD TEMPOH MENGINAP
	 * DEFAULT 7 HARI
	 * */
	public void hadTempahanMenginap(){
		int hadtempoh = 14;
		context.put("hadTempohMenginap", hadtempoh);
	}
	
	public void filtering(MyPersistence mp) {
		/** paparan permohonan baru & tunggu kelulusan sub & xlulus sub & lulus sub & Permohonan Diluluskan */
		this.addFilter("status.id in ('1425259713412','1430809277096','1430809277099','1430809277102','1425259713415')"); 
		this.addFilter("jenisPemohon = 'INDIVIDU' ");
		this.addFilter("rppPeranginan.jenisPeranginan.id in ('RT') ");
		
		if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak")
				&& !userRole.equalsIgnoreCase("(RPP) Pelulus") && !userRole.equalsIgnoreCase("(PRO) Pengguna")){
			
			this.addFilter("pemohon.id = '"+userId+"'");
			
		}else if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			String idrp = UtilRpp.getPeranginanByIdPenyelia(mp,userId)!=null?UtilRpp.getPeranginanByIdPenyelia(mp,userId).getId():null;
			this.addFilter("rppPeranginan.id = '" +idrp+ "'");
		}
	}
	
	public void senaraiRPbyGred(Users user){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searching", false);
		List<SummaryRp> list = null;
		try {
			list = getSenaraiRPbyGred(user,true,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.put("listRpByGred",list);
	}
	
	@Command("listPeranginanDanJenisUnit")
	public void listPeranginanDanJenisUnit(String userRole){
		dataUtil = DataUtil.getInstance(db);
		context.put("listPeranginan",dataUtil.getListPeranginanRTByGred("")); /**Carian*/
		//context.put("qListPeranginan",dataUtil.getListPeranginanRTByGred("")); /**Main screen*/
		//context.put("qListJenisUnit",dataUtil.getListJenisUnitRTByGred(""));
	}
	
	@Command("paparSemuaKekosongan")
	public String paparSemuaKekosongan() throws Exception {

		userRole = (String) request.getSession().getAttribute("_portal_role");
		String userIdMohon = (String) request.getSession().getAttribute("_portal_login");
		
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")
		   || userRole.equalsIgnoreCase("(RPP) Penyemak") 
		   || userRole.equalsIgnoreCase("(RPP) Pelulus")
		   || userRole.equalsIgnoreCase("(RPP) Penyelia") ) {
			
			String daftarkanPenginapId = getParam("penginap");
			if(daftarkanPenginapId != null && !daftarkanPenginapId.equalsIgnoreCase("")){
				userIdMohon = daftarkanPenginapId;
			}
		}
		
		try{
			mp = new MyPersistence();

			Users user = (Users) mp.find(Users.class, userIdMohon);
		
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searching", false);
			map.put("gredId", getParam("qGred"));		
			List<SummaryRp> list = getSenaraiRPbyGred(user,true,map);
			context.put("listRpByGred",list);

		}catch (Exception ex){
			System.out.println("ERROR paparSemuaKekosongan : "+ex.getMessage()); /** TRUE FOR RUMAH TRANSIT **/
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/carianKekosongan/start.vm";
	}
	
	@Command("carianKekosongan")
	public String carianKekosongan() throws Exception {
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users user = null;
		
		try{
			mp = new MyPersistence();
			
//			if(userRole.equalsIgnoreCase("(AWAM) Penjawat Awam") 
//				|| userRole.equalsIgnoreCase("(AWAM) Pesara") 
//				|| userRole.equalsIgnoreCase("(AWAM) Badan Berkanun")
//				|| userRole.equalsIgnoreCase("(AWAM) Pegawai Tadbir") ){
//					
//					user = (Users) mp.find(Users.class, userId);
//			}
			
			if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak")
				&& !userRole.equalsIgnoreCase("(RPP) Pelulus") && !userRole.equalsIgnoreCase("(RPP) Penyelia") ){
				user = (Users) mp.find(Users.class, userId);
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("gredId", getParam("qGred"));
			map.put("peranginanId", getParam("qPeranginan"));
			map.put("searching", true);
			List<SummaryRp> list = getSenaraiRPbyGred(user,true,map); /** TRUE FOR RUMAH TRANSIT **/
			context.put("listRpByGred",list);
			
		}catch (Exception ex){
			System.out.println("ERROR carianKekosongan : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/carianKekosongan/start.vm";
		
	}
	
	//RT TAK PERLU QUOTA
	@Command("checkingHadPermohonan")
	public void checkingHadPermohonan(){
		context.put("blockBooking",false);
	}
	
	public void reportFFiltering() {
		context.put("jenisReport", "RT");
	}
	
	@Command("filterRppByPenginap")
	public String filterRppByPenginap(){
		try{
			mp = new MyPersistence();
			String guestId = getParam("radNama");
			Users rekodPenginap = (Users) mp.find(Users.class, guestId);
			context.put("rekodPenginap", rekodPenginap);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("searching", false);
			List<SummaryRp> list = null;
			list = getSenaraiRPbyGred(rekodPenginap,true,map); /** TRUE FOR RUMAH TRANSIT **/
			context.put("listRpByGred",list);
		}catch (Exception ex){
			System.out.println("ERROR filterRppByPenginap : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/carianKekosongan/start.vm";
	}
	
}

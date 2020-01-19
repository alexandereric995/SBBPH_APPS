package bph.modules.rpp;

import java.util.Date;
import java.util.List;

import lebah.portal.action.Command;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;


public class TempahanOffline extends TempahanWalkIn{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	public void enabledEditDate(){
		context.put("enabledEditDate", true);
	}
	
	@SuppressWarnings("unchecked")
	public List<RppUnit> getListUnit(MyPersistence mp,Date tarikhMasuk,Date tarikhKeluar,String idJenisUnit){
		List<RppUnit> list = mp.list("select x from RppUnit x where x.jenisUnit.id = '"+idJenisUnit+"' ");
		return list;
	}
	
	public String getFlagDaftarOffline(){
		return "Y";
	}
	
	public void otherFiltering(){
		this.addFilter("flagDaftarOffline = 'Y' ");
	}
	
	/**
	 * 
	 * PAPAR MAKLUMAT RPP
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Command("paparMaklumatRpp")
	public String paparMaklumatRpp(){
		
		String loginId = (String) request.getSession().getAttribute("_portal_login");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		
		try {
			mp = new MyPersistence();
			if(userRole.equalsIgnoreCase("(ICT) Pentadbir Sistem")){
				List<RppPeranginan> listRp = mp.list("select x from RppPeranginan x where x.id not in ('11','12','13') order by x.namaPeranginan asc "); 
				context.put("listRp",listRp);
			}else{
				List<RppPeranginan> listRp = mp.list("select x from RppPeranginan x where x.id in "+listRppByPenyelia(mp,loginId,userRole)+" and x.id not in ('3','14','11','12','13') order by x.namaPeranginan asc "); 
				context.put("listRp",listRp);
			}
					
			/**SHOW/HIDE KALAU WALKIN / OFFLINE*/
			enabledEditDate();
			
		} catch (Exception e) {
			System.out.println("Error paparMaklumatRpp : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/form/maklumatRpp.vm";
	}

}

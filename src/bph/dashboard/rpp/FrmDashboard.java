package bph.dashboard.rpp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.rpp.RppMaklumatTerkini;
import bph.entities.rpp.RppPermohonan;
import db.persistence.MyPersistence;

public class FrmDashboard extends LebahModule{

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;

	private String getPath() { return "bph/dashboard/rpp"; }
	
	@Override
	public String start() {
		
		String portal_role  = (String)request.getSession().getAttribute("_portal_role");
		try{
			mp = new MyPersistence();
			addcontext();
			
			List<RppMaklumatTerkini> list = listMaklumatTerkini(mp);
			context.put("senaraiMaklumatTerkini",list);
			context.put("jumPengesahanPengguna",getJumPengesahanPengguna(mp));
			
			/**Bil Permohonan Kelompok Baru*/
			context.put("bilPrmhnKelompokBaru", getBilNotifikasi(mp,portal_role,"TEMPAHAN_KELOMPOK_BARU"));
			context.put("bilPrmhnKelompokSejarah", getBilSejarahPermohonanKelompok(mp,portal_role));
			context.put("bilPrmhnPremierBaru", getBilNotifikasi(mp,portal_role,"TEMPAHAN_PREMIER_BARU"));
			context.put("bilPrmhnLondonBaru", getBilNotifikasi(mp,portal_role,"TEMPAHAN_LONDON_BARU"));
			//context.put("bilTuntutanDeposit", getBilNotifikasi(mp,portal_role,"TUNTUTAN_DEPOSIT"));
			
		}catch (Exception ex){
			System.out.println("ERROR start : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/start.vm";
	}
	
	@Command("openPopupDaftar")
	public String openPopupDaftar() throws Exception {
		return getPath() + "/popupDaftar.vm";
	}
	
	@Command("saveMaklumatTerkini")
	public String saveMaklumatTerkini() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			String userId = (String) request.getSession().getAttribute("_portal_login");
			String catatan = getParam("catatan");
			
			String id = UID.getUID();
			String sql = " INSERT INTO `rpp_maklumat_terkini` (`id`, `catatan`, `tarikh_catatan`, `id_pendaftar` ) "+
						 " VALUES "+
						 " ('"+id+"', '"+catatan+"', now(), '"+userId+"' ) ";
			
			Db dbase = null;
	        try{
	        	dbase = new Db();
	            Statement stmtm = dbase.getStatement();
	            stmtm.executeUpdate(sql);
	        }catch(SQLException ex){
	        	System.out.println(":: ERROR saveMaklumatTerkini : "+ex.getMessage());
	        }finally { if ( dbase != null ) dbase.close(); }
			
	        List<RppMaklumatTerkini> list = listMaklumatTerkini(mp);
	        context.put("senaraiMaklumatTerkini",list);
	        
	        addcontext();
	        
		}catch (Exception ex){
			System.out.println("ERROR saveMaklumatTerkini : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}	
		return getPath() + "/senaraiMaklumatTerkini.vm";
	}
	
	@SuppressWarnings("unchecked")
	public List<RppMaklumatTerkini> listMaklumatTerkini(MyPersistence mp){
		 List<RppMaklumatTerkini> list = mp.list("select x from RppMaklumatTerkini x order by x.tarikhCatatan desc ");
		 return list;
	}
	
	@Command("deleteRecord")
	public String deleteRecord() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			String idrekod = getParam("idrekod");
			
			String sql = " DELETE FROM rpp_maklumat_terkini WHERE id = '"+idrekod+"' ";
			
			Db dbase = null;
	        try{
	        	dbase = new Db();
	            Statement stmtm = dbase.getStatement();
	            stmtm.executeUpdate(sql);
	        }catch(SQLException ex){
	        	System.out.println(":: ERROR deleteRecord : "+ex.getMessage());
	        }finally { if ( dbase != null ) dbase.close(); }
			
	        List<RppMaklumatTerkini> list = listMaklumatTerkini(mp);
	        context.put("senaraiMaklumatTerkini",list);
	        
	        addcontext();
	        
		}catch (Exception ex){
			System.out.println("ERROR deleteRecord : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}	
		return getPath() + "/senaraiMaklumatTerkini.vm";
	}
	
	public void addcontext() {
		String portal_role  = (String)request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		lebah.util.Util lebahUtil = new lebah.util.Util();
		context.put("lebahUtil", lebahUtil);
		context.put("path", getPath());
	}
	
	@SuppressWarnings("unchecked")
	public int getJumPengesahanPengguna(MyPersistence mp) {
		int x = 0;
		List<Users> kp = null;
		kp = mp.list("SELECT x FROM Users x WHERE x.flagDaftarSBBPH = 'Y' and x.flagAktif != 'Y'"
				+ " and x.dokumenSokongan != null and x.flagMenungguPengesahan = 'Y' and x.flagUrusanPemohon in ('2', '3')"
				+ " and x.role.name in ('(AWAM) Badan Berkanun', '(AWAM) Polis / Tentera', '(AWAM) Pesara Polis / Tentera')");
		
		if(kp!= null){ x = kp.size();}
		
		return x;
	}
	
	public int getBilNotifikasi(MyPersistence mp, String userRole, String flagAktiviti) throws Exception {
		int i = 0;
		Db dtbs = null;
		try {
			dtbs = new Db();
			String sql = "select x.id "+ 
						" from rpp_notifikasi x, rpp_user_role_notifikasi y "+
						" where x.id = y.id_notifikasi "+
						" and x.flag_aktiviti = '"+flagAktiviti+"' "+ 
						" and x.flag_aktif = 'Y' "+
						" and y.id_role = '"+userRole+"' ";
			ResultSet rs = dtbs.getStatement().executeQuery(sql);
			while (rs.next()){
				i++;
			}
		} catch(SQLException ex){
			System.out.println("Error getBilNotifikasi "+flagAktiviti+" : "+ex.getMessage());
		}finally { if ( dtbs != null ) dtbs.close(); }	

		return i;
	}
	
	public int getBilSejarahPermohonanKelompok(MyPersistence mp, String userRole) throws Exception {
		int i = 0;
		List<RppPermohonan> listPermohonan = mp.list("select x from RppPermohonan x where x.jenisPemohon in ('KELOMPOK')"
				+ " and x.status.id in ('1430809277099','1425259713418', '1425259713424','1425259713430','1425259713421','1433083787409',"
				+ " '1435093978588','1435512646303','1688545253001455')");
		i = listPermohonan.size();
		return i;
	}
	
}

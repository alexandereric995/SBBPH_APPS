package bph.print;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lebah.db.Db;
import lebah.portal.velocity.VTemplate;
import lebah.template.DbPersistence;

import org.apache.velocity.Template;

import portal.module.entity.UsersSpouse;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.rpp.RppPermohonan;
import bph.utils.Util;

public class PrintSuratRPP extends VTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4349743529793407370L;
	
	String path = "bph/modules/rpp/tempahanPermohonan";
	String path2 = "bph/modules/rpp/resit";
	DbPersistence db = new DbPersistence();
	private Util util = new Util();
	
	public Template doTemplate() throws Exception {
		setShowVM(false);
		context.put("dateFormat", new SimpleDateFormat("dd-MM-yyyy"));
		context.put("timeFormat", new SimpleDateFormat("hh:mm a"));	
		context.put("numFormat", new DecimalFormat("#,###,###.00"));
		context.put("util", util);
		context.put("todayDate", new Date());
		context.put("path", path);
		Template template = engine.getTemplate(getForm());
		return template;
	}
	
	private String getForm() {
		String serverName = request.getServerName();
		String contextPath = request.getContextPath();
		String fullPath = "";
		
		int serverPort = request.getServerPort();
		String server = serverPort != 80 ? serverName + ":" + serverPort : serverName;
		String serverUrl = "http://" + server;
		String image_url = "http://" + server + contextPath;
		
		context.put("imageUrl", image_url);
		context.put("serverUrl", serverUrl);
		
		String jenisSurat = getParam("jenisSurat");
		String idRppPermohonan = getParam("idRppPermohonan");
		
		RppPermohonan p = db.find(RppPermohonan.class, idRppPermohonan);
		
		//maklumat isteri
		UsersSpouse spouse = null;
		if (p!=null) {
			spouse = (UsersSpouse) db.get("select x from UsersSpouse x where x.users.id = '"+p.getPemohon().getId()+"' ");
		}
		
		//RppAkaun a = null;
		ArrayList<KewInvois> listInvois = new ArrayList<KewInvois>();
		KewInvois oneInv = null;
		KewResitSenaraiInvois rsi = null;
		KewBayaranResit resit = null;
		KewResitKaedahBayaran rkb = null;
		
		System.out.println("permohonan "+p.getId());
		
		if (p!=null) {
			//a = (RppAkaun) db.get("SELECT a FROM RppAkaun a WHERE a.permohonan.id = '" + p.getId() + "' AND a.kodHasil.id NOT IN ('72311')");
			
			/**GET LIST INVOIS**/
			Db db1 = null;
			try {
				db1 = new Db();
				String sql = " select b.id from rpp_akaun a, kew_invois b "+
							 " where a.id = b.id_lejar "+
							 " and a.id_kod_hasil != '72311' "+
							 " and a.id_permohonan = '"+p.getId()+"' ";
				System.out.println("sql "+sql);
				ResultSet rs = db1.getStatement().executeQuery(sql);			
				while (rs.next()) {
					KewInvois inv = db.find(KewInvois.class, rs.getString("id"));
					listInvois.add(inv);
				}	
			}catch(Exception e){
				System.out.println("error getListInvois() : "+e.getMessage());
			}finally { 
				if ( db1 != null ) db1.close();
			}
			
			System.out.println("listInvois "+listInvois);
			
			/**Get KewResitKaedahBayaran by RESIT **/
			if(listInvois.size() > 0){
				oneInv = listInvois.get(0);
			}
			
			System.out.println("oneInv "+oneInv);
			
			if(oneInv!=null){
				rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+oneInv.getId()+"' ");
			}
			
			System.out.println("rsi "+rsi);
			
			if(rsi!=null){
				resit = rsi.getResit();
			}
			
			System.out.println("resit "+resit);
			
			if(resit !=null){
				rkb = (KewResitKaedahBayaran) db.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+resit.getId()+"' ");
			}
			
			System.out.println("rkb "+rkb);
			
			//List<> listResitKaedahBayaran = db.list("");
			
		}
		
		if ( "suratKelulusan".equals(jenisSurat) ) {
			fullPath = path + "/print/suratKelulusan.vm";
			
		} else if ( "resitBayaran".equals(jenisSurat) ) {
			fullPath = path2 + "/resitBayaran.vm";
			
		} else if ( "suratKelulusanLangkawi".equals(jenisSurat) ) {
			fullPath = path + "/print/suratKelulusanLangkawi.vm";
			
		} else if ( "suratKelulusanPD".equals(jenisSurat) ) {
			fullPath = path + "/print/suratKelulusanPD.vm";
			
		}
		
		context.put("p", p);
		context.put("rkb", rkb);
		context.put("resit", resit);
		context.put("util", new Util());
		context.put("spouse", spouse);
		context.put("listInvois", listInvois);
		//context.put("a", a);
		
		return fullPath;
	}
	
}

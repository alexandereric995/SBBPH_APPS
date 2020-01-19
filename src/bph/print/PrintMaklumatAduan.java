package bph.print;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lebah.portal.velocity.VTemplate;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;

import bph.entities.pro.ProAduan;
import bph.utils.Util;

public class PrintMaklumatAduan extends VTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4349743529793407370L;
	static Logger myLogger = Logger.getLogger("bph/print/PrintMaklumatAduan");

	String[] path = { "bph/modules/pro/senaraiAduan/cetak"};

	DbPersistence db = new DbPersistence();
	private Util util = new Util();

	public Template doTemplate() throws Exception {
		setShowVM(false);
		context.put("dateFormat", new SimpleDateFormat("dd-MM-yyyy"));
		context.put("timeFormat", new SimpleDateFormat("hh:mm a"));
		context.put("numFormat", new DecimalFormat("#,###,###.00"));
		context.put("util", util);
		context.put("todayDate", new Date());
		Template template = engine.getTemplate(getDetailMaklumatAduan());
		return template;
	}

	private String getDetailMaklumatAduan() {
	String serverName = request.getServerName();
	String contextPath = request.getContextPath();
	String fullPath = "";

	int serverPort = request.getServerPort();
	String server = serverPort != 80 ? serverName + ":" + serverPort
			: serverName;
	String serverUrl = "http://" + server;
	String image_url = "http://" + server + contextPath;

	context.put("imageUrl", image_url);
	context.put("serverUrl", serverUrl);
	
	ProAduan aduan = db.find(ProAduan.class, getParam("idAduan"));		
	context.put("aduan", aduan);
	

	String jenisSurat = getParam("jenisSurat");
	String idTawaran = getParam("idTawaran");
	String idTemujanji = getParam("idTemujanji");
	String idAkaun = getParam("idDeposit");

	for (int i = 0; i < path.length; i++) {
		String file = path[i] + "/" + jenisSurat + ".vm";
//		myLogger.debug("DIR ::: " + file);

		if (fileExists(image_url + "/" + file) == true) {
			fullPath = file;
			break;
		}
	}

//	myLogger.debug("FULL PATH ::: " + fullPath);


	return fullPath;
}
	
//	private String getForm() {
//		String serverName = request.getServerName();
//		String contextPath = request.getContextPath();
//		String fullPath = "";
//
//		int serverPort = request.getServerPort();
//		String server = serverPort != 80 ? serverName + ":" + serverPort
//				: serverName;
//		String serverUrl = "http://" + server;
//		String image_url = "http://" + server + contextPath;
//
//		context.put("imageUrl", image_url);
//		context.put("serverUrl", serverUrl);
//
//		String jenisSurat = getParam("jenisSurat");
//		String idTawaran = getParam("idTawaran");
//		String idTemujanji = getParam("idTemujanji");
//		String idAkaun = getParam("idDeposit");
//
//		KuaTawaran tawaran = db.find(KuaTawaran.class, idTawaran);
//		KuaTemujanji temujanji = db.find(KuaTemujanji.class, idTemujanji);
//		KuaAkaun akaun = db.find(KuaAkaun.class, idAkaun);
//		UsersJob uj = null;
//		KuaAgihan a = null;
//		KuaPenghuni p = null;
//		if (tawaran != null) {
//			uj = (UsersJob) db
//					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
//							+ tawaran.getAgihan().getPemohon().getId() + "'");
//			a = db.find(KuaAgihan.class, tawaran.getAgihan().getId());
//		}
//		if (temujanji != null) {
//			p = (KuaPenghuni) db
//					.get("SELECT p FROM KuaPenghuni p WHERE p.pemohon.id = '"
//							+ temujanji.getAgihan().getPemohon().getId() + "'");
//			uj = (UsersJob) db
//					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
//							+ temujanji.getAgihan().getPemohon().getId() + "'");
//			a = db.find(KuaAgihan.class, temujanji.getAgihan().getId());
//			tawaran = (KuaTawaran) db
//					.get("SELECT p FROM KuaTawaran p WHERE p.agihan.id = '"
//							+ temujanji.getAgihan().getId() + "'");
//		}
//		if (akaun != null) {
//			KuaAgihan agihan = (KuaAgihan) db
//					.get("SELECT a FROM KuaAgihan a WHERE a.kuarters.id IS NOT NULL AND a.permohonan.id = '"
//							+ akaun.getPermohonan().getId() + "'");
//			uj = (UsersJob) db
//					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
//							+ akaun.getPembayar().getId() + "'");
//			if (agihan!=null) { 
//				a = db.find(KuaAgihan.class, agihan.getId());
//				tawaran = (KuaTawaran) db
//						.get("SELECT p FROM KuaTawaran p WHERE p.agihan.id = '"
//								+ agihan.getId() + "'");
//			}
//			
//		}
//
//		for (int i = 0; i < path.length; i++) {
//			String file = path[i] + "/" + jenisSurat + ".vm";
////			myLogger.debug("DIR ::: " + file);
//
//			if (fileExists(image_url + "/" + file) == true) {
//				fullPath = file;
//				break;
//			}
//		}
//
////		myLogger.debug("FULL PATH ::: " + fullPath);
//
//		context.put("p", tawaran);
//		context.put("a", a);
//		context.put("t", temujanji);
//		context.put("uj", uj);
//		context.put("akaun", akaun);
//		context.put("penghuni", p);
//
//		return fullPath;
//	}

	public static boolean fileExists(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URLName)
					.openConnection();
			con.setRequestMethod("HEAD");
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
				return true;
			else
				return false;
		} catch (Exception e) {
			myLogger.debug("ERROR ::: " + e.getMessage());
			return false;
		}
	}

}

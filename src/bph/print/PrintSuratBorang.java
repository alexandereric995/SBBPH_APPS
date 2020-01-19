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

import portal.module.entity.UsersJob;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaTawaran;
import bph.utils.Util;

public class PrintSuratBorang extends VTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4349743529793407370L;
	static Logger myLogger = Logger.getLogger("bph/print/PrintSuratBorang");

	String[] path = { "bph/modules/qtr/tawaran/sub_page/suratTawaran",
					  "bph/modules/qtr/deposit/surat" };

	//DbPersistence db = new DbPersistence();
	//private Util util = new Util();
	DbPersistence db = null;
	Util util = null;
	
	public Template doTemplate() throws Exception {
		db = new DbPersistence();
		util = new Util();
		setShowVM(false);
		context.put("dateFormat", new SimpleDateFormat("dd-MM-yyyy"));
		context.put("timeFormat", new SimpleDateFormat("hh:mm a"));
		context.put("numFormat", new DecimalFormat("#,###,###.00"));
		context.put("util", util);
		context.put("todayDate", new Date());
		Template template = engine.getTemplate(getForm());
		return template;
	}

	private String getForm() {
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

		String jenisSurat = getParam("jenisSurat");
		
		String idTawaran = getParam("idTawaran");
		String idAkaun = getParam("idDeposit");
		
		//AZAM - ADD 09/09/2015
		if (idTawaran == "" && idAkaun=="") {
			context.put("errorMsg", "Sila semak id yang dihantar!");
			return "print-report-error.vm";
		}
		
		KuaTawaran tawaran = null;
		KuaAkaun akaun = null;
		UsersJob uj = null;
		KuaAgihan a = null;
		KuaPenghuni p = null;
		
		//AZAM CHANGE ON 10/9/2015
		if (idTawaran != "" && idTawaran !=null) {
			tawaran = db.find(KuaTawaran.class, idTawaran);
			
			if (tawaran != null) {
				uj = (UsersJob) db
						.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
								+ tawaran.getAgihan().getPemohon().getId() + "'");
				a = db.find(KuaAgihan.class, tawaran.getAgihan().getId());
			}
		}
		else if (idAkaun != "" && idAkaun !=null) {
			akaun = db.find(KuaAkaun.class, idAkaun);
			if (akaun != null) {
				KuaAgihan agihan = (KuaAgihan) db.get("SELECT a FROM KuaAgihan a WHERE a.kuarters.id IS NOT NULL AND a.permohonan.id = '"
								+ akaun.getPermohonan().getId() + "'");
				uj = (UsersJob) db.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+ akaun.getPembayar().getId() + "'");
				if (agihan!=null) { 
					a = db.find(KuaAgihan.class, agihan.getId());
					tawaran = (KuaTawaran) db.get("SELECT p FROM KuaTawaran p WHERE p.agihan.id = '"+ agihan.getId() + "'");
				}
			}
		}
		

		for (int i = 0; i < path.length; i++) {
			String file = path[i] + "/" + jenisSurat + ".vm";

			if (fileExists(image_url + "/" + file) == true) {
				fullPath = file;
				break;
			}
		}

		context.put("p", tawaran);
		context.put("a", a);
		context.put("uj", uj);
		context.put("akaun", akaun);
		context.put("penghuni", p);

		return fullPath;
	}

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

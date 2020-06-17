/**
 * 
 */
package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import lebah.db.Db;
import lebah.template.DbPersistence;

import org.apache.commons.lang.StringUtils;

import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.BankFPX;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaAkaun;
import bph.integrasi.fpx.FPXPkiImplementation;

/**
 * @author Mohd Faizal
 * 
 */
public class TestingPeje {

	private static DbPersistence db;
	private static Db db2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("START JOB ON : " + new Date());
		
		doJob();
		
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob() {
		Db lebahDb = null;
		try {
			db = new DbPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();	
			int i = 0;
			List<KuaAkaun> listAkaun = db.list("select x from KuaAkaun x where x.kodHasil.id = '72310' and x.flagBayar = 'Y' and x.penghuni is null");
			for (KuaAkaun akauan : listAkaun) {
				KuaAgihan agihan = (KuaAgihan) db.get("select x from KuaAgihan x where x.pemohon.id = '" + akauan.getPembayar().getId() + "'");
				if (agihan != null && agihan.getKuarters() != null) {
					i++;
				}
			}
			System.out.println(i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
	
}

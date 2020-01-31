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

import org.apache.commons.lang.StringUtils;

import db.persistence.MyPersistence;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kewangan.KewSubsidiariAgihan;
import bph.entities.kod.BankFPX;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppPermohonanBayaranBalik;
import bph.entities.utiliti.UtilAkaun;
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
		
//		doJob();
		updateStatusFPXBankList(db);
		
		System.out.println("FINISH JOB ON : " + new Date());
	}

	private static void doJob() {
		Db lebahDb = null;
		try {
			db = new DbPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();	
			int z = 1;
			for (int i = 2014; i < 2020; i++) {
				for (int y = 1; y <= 12; y++) {
					db.begin();
					String sql = "SELECT id FROM kew_bayaran_resit WHERE id_pembayar != no_pengenalan_pembayar AND flag_jenis_bayaran != 'ONLINE'"
							+ " and YEAR(tarikh_resit) = '" + i + "' and MONTH(tarikh_resit) = '" + y + "'";
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						KewBayaranResit resit = db.find(KewBayaranResit.class, rs.getString("id"));
						if (resit != null) {
							List<KewResitSenaraiInvois> listRSI = db.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "'");
							if (listRSI.size() > 0) {
								for (KewResitSenaraiInvois rsi : listRSI) {
									if (rsi.getDeposit() != null) {
										KewDeposit deposit = rsi.getDeposit();
										if (deposit.getPendeposit() != null && !deposit.getPendeposit().getId().equalsIgnoreCase("FAIZAL")) {
											resit.setPembayar(deposit.getPendeposit());
										}
//										System.out.println("DEPOSIT : " + resit.getNoResit() + " - " + resit.getPembayar().getId() + " - " + deposit.getPendeposit().getId());
									}
									
									if (rsi.getInvois() != null) {
										KewInvois invois = rsi.getInvois();
										if (invois.getPembayar() != null && !invois.getPembayar().getId().equalsIgnoreCase("FAIZAL")) {
											resit.setPembayar(invois.getPembayar());
										}
//										System.out.println("INVOIS : " + resit.getNoResit() + " - " + resit.getPembayar().getId() + " - " + invois.getUserPendaftar().getId());
									}
								}
							}
							System.out.println(z);
							z++;
						}
					}
					db.commit();					
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
	}
	
	private static void updateStatusFPXBankList(DbPersistence db3) {
		try {	
			db3 = new DbPersistence();
			db3.begin();
			List<BankFPX> listBankFPX = db3.list("select x from BankFPX x where x.isActive = 'Y'");
			for (BankFPX bankFPX : listBankFPX) {
				bankFPX.setIsOnline("T");
			}
			db3.commit();
			
			String fpx_msgType = "BE";
			String fpx_msgToken = "01";
			String fpx_sellerExId = ResourceBundle.getBundle("dbconnection").getString("FPX_SELLER_EX_ID");
			String fpx_version = "7.0";

			HashMap<String, String> respMap = new HashMap<String, String>();

			String chkSumStr = fpx_msgToken + "|" + fpx_msgType + "|"
					+ fpx_sellerExId + "|" + fpx_version;

			String final_checkSum = FPXPkiImplementation.signData(
					ResourceBundle.getBundle("dbconnection").getString("FPX_KEY_PATH"), chkSumStr, "SHA1withRSA");
			StringBuilder postDataStrBuilder = new StringBuilder();
			postDataStrBuilder.append("fpx_msgType="
					+ URLEncoder.encode(fpx_msgType, "UTF-8"));
			postDataStrBuilder.append("&fpx_msgToken="
					+ URLEncoder.encode(fpx_msgToken, "UTF-8"));
			postDataStrBuilder.append("&fpx_sellerExId="
					+ URLEncoder.encode(fpx_sellerExId, "UTF-8"));
			postDataStrBuilder.append("&fpx_version="
					+ URLEncoder.encode(fpx_version, "UTF-8"));
			postDataStrBuilder.append("&fpx_checkSum="
					+ URLEncoder.encode(final_checkSum, "UTF-8"));

			URLConnection conn = (HttpsURLConnection) new URL(
					"https://mepsfpx.com.my/FPXMain/RetrieveBankList")
					.openConnection();

			conn.setDoOutput(true);
			BufferedWriter outputWriter = new BufferedWriter(
					new OutputStreamWriter(conn.getOutputStream()));
			outputWriter.write(postDataStrBuilder.toString(), 0, postDataStrBuilder
					.toString().length());

			outputWriter.flush();
			outputWriter.close();
			
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String strResponse = inputReader.readLine();
			inputReader.close();
			strResponse = strResponse.trim();
			if (strResponse == null || strResponse.equals("ERROR")) {
				System.out.println("An error occured!..Response[" + strResponse
						+ "]");
				return;
			} else {
				StringTokenizer strToknzr = new StringTokenizer(strResponse, "&");
				while (strToknzr.hasMoreElements()) {
					String temp = strToknzr.nextToken();
					if (temp.contains("=")) {
						String nvp[] = temp.split("=");
						String name = nvp[0];
						String value = "";
						if (nvp.length == 2)
							value = URLDecoder.decode(nvp[1], "UTF-8");
						respMap.put(name, value);					
					} else {
						System.out.println("Parsing Error!" + temp);
					}
				}				
				
				db3.begin();
				String[] parts = respMap.get("fpx_bankList").toString().split(",");
				for (String r:parts) {
					String bankCode = StringUtils.substringBefore(r, "~").trim();
					String isActive = StringUtils.substringAfter(r, "~").trim();
					
					BankFPX bankFPX = (BankFPX) db3.get("select x from BankFPX x where x.isActive = 'Y' and x.code = '" + bankCode + "'");
					if (bankFPX != null) {
						if ("A".equals(isActive)) {
							bankFPX.setIsOnline("Y");
						}
					}
				}
				db3.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

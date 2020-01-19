package bph.laporan.senggara;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.db.Db;

import org.apache.log4j.Logger;

import db.persistence.MyPersistence;

import bph.entities.senggara.MtnJKH;
import bph.laporan.ReportServlet;

public class CetakNotaMinta extends ReportServlet {

	static Logger myLog = Logger.getLogger(CetakNotaMinta.class);

	public CetakNotaMinta() {

		super.setFolderName("senggara");
		super.setReportName("NotaMinta");

	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {

		// ADD EXTRA PARAMETER
		String idIndenKerja = (String) parameters.get("idIndenKerja");
		parameters.put("JUMLAH_KESELURUHAN", getJumlahKeseluruhan(idIndenKerja));
		
	}
	
	private Double getJumlahKeseluruhan(String idIndenKerja) {
		Double jumlahKeseluruhan = 0D;
		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			String sql = "SELECT sum(jumlah_keseluruhan) as jumlah_keseluruhan FROM mtn_jkh WHERE id_inden_kerja = '" +idIndenKerja + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				jumlahKeseluruhan = rs.getDouble("jumlah_keseluruhan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}	
		return jumlahKeseluruhan;
	}

}

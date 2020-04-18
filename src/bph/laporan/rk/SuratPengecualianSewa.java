package bph.laporan.rk;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.laporan.ReportServlet;
import db.persistence.MyPersistence;

public class SuratPengecualianSewa extends ReportServlet {

	static Logger myLog = Logger.getLogger(SuratPengecualianSewa.class);
	private MyPersistence mp;

	public SuratPengecualianSewa() {

		super.setFolderName("rk");
		super.setReportName("SuratPengecualianSewa");
	}

	@Override
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
	}
}

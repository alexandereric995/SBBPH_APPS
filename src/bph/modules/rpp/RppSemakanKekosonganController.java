package bph.modules.rpp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.kod.JenisUnitRPP;
import bph.entities.rpp.RppUnit;
import bph.utils.HTML;
import bph.utils.UtilRpp;

public class RppSemakanKekosonganController extends AjaxBasedModule {
	
	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/rpp/rppSemakanKekosongan/";
	static Logger myLogger = Logger.getLogger(RppSemakanKekosonganController.class);
	protected DbPersistence db;
	
	@SuppressWarnings("unchecked")
	@Override
	public String doTemplate2() throws Exception {
		myLogger.info(".:RppSemakanKekosonganController:.");

		HttpSession session = request.getSession();
		
		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		myLogger.info("command : "+command);
		
		if(command.equalsIgnoreCase("filterPeranginan")){
			
			String socJenisPeranginan = getParam("socJenisPeranginan");
			context.put("selectPeranginan", HTML.SelectPeranginanByJenisPeranginan(socJenisPeranginan,"socPeranginan",null, "id=\"socPeranginan\" style=\"width:100%\" ", "onchange=\" doFilterJenisUnit(); $('err_socPeranginan').innerHTML=''; at(this, event);\""));
			return path + "/divFilterPeranginan.vm";
			
		}else if(command.equalsIgnoreCase("filterJenisUnit")){
			
			String socPeranginan = getParam("socPeranginan");
			context.put("selectJenisUnit", HTML.SelectJenisUnitByPeranginan(socPeranginan,"socJenisUnit",null, "id=\"socJenisUnit\" style=\"width:100%\" ", "onchange=\" $('err_socJenisUnit').innerHTML=''; at(this, event);\""));
			return path + "/divFilterJenisUnit.vm";
			
		}else if(command.equalsIgnoreCase("checkBilikKosong")){
			
			db = new DbPersistence();
			
			boolean avroom = false;
			String jenisUnit = getParam("socJenisUnit");
			Date tarikhMasuk = getDate("tarikhMasukRpp");
			Date tarikhKeluar = getDate("tarikhKeluarRpp");
			
			String dtIn = new SimpleDateFormat("yyyy-MM-dd").format(tarikhMasuk);
			String dtOut = new SimpleDateFormat("yyyy-MM-dd").format(tarikhKeluar);
			
			List<RppUnit> rpList = db.list("select x from RppUnit x where x.jenisUnit.id = '"+jenisUnit+"' ");
			JenisUnitRPP jenisUnitRPP = db.find(JenisUnitRPP.class, jenisUnit);
			context.put("jenisUnitRPP", jenisUnitRPP);
			
			int totalOfAvailable = 0;
			for(int i = 0; i < rpList.size(); i++){
				avroom = UtilRpp.checkingAvailableRoom(rpList.get(i),dtIn,dtOut,"INDIVIDU");
				if(avroom){
					totalOfAvailable ++;
				}
			}
			
			//List<RppGambarJenisUnit> listGambar = db.list("select x from RppGambarJenisUnit x where x.jenisUnit.id = '"+jenisUnit+"' ");
			//context.put("listGambar",listGambar);
			//context.put("uploadDir",ResourceBundle.getBundle("dbconnection").getString("folder"));
			
			
			Map<String, Object> mk = new HashMap<String, Object>();
			mk.put("totalOfAvailable", totalOfAvailable);
			mk.put("jenisUnit", jenisUnitRPP.getKeterangan());
			mk.put("kadarSewa", UtilRpp.kadarSewaBiasaAtauWaktuPuncak(jenisUnitRPP, dtIn, dtOut));
			mk.put("gredKelayakan", UtilRpp.gredKelayakanBiasaAtauWaktuPuncak(jenisUnitRPP, dtIn, dtOut));
			mk.put("flagCheckWaktuPuncak", UtilRpp.checkWaktuPuncak(dtIn));
			mk.put("hadKuantiti", jenisUnitRPP.getHadKuantiti());
			mk.put("hadDewasa", jenisUnitRPP.getHadDewasa());
			mk.put("hadKanakKanak", jenisUnitRPP.getHadKanakKanak());
			
			context.put("mk", mk);
			
			return path + "/maklumatDanPilihanKekosongan.vm";
			
		}else{
			dropdown(null,null,null);
		}
		
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
	private void dropdown(String jenisPeranginan, String peranginan, String jenisUnit) throws Exception {
		context.put("selectJenisPeranginan", HTML.SelectJenisPeranginan("socJenisPeranginan",jenisPeranginan, "id=\"socJenisPeranginan\" style=\"width:100%\" ", "onchange=\" doFilterPeranginan(); $('err_socJenisPeranginan').innerHTML=''; at(this, event);\""));
		context.put("selectPeranginan", HTML.SelectPeranginanByJenisPeranginan(jenisPeranginan,"socPeranginan",peranginan, "id=\"socPeranginan\" style=\"width:100%\" ", "onchange=\" doFilterRppUnit(); $('err_socPeranginan').innerHTML=''; at(this, event);\""));
		context.put("selectJenisUnit", HTML.SelectJenisUnitByPeranginan(peranginan,"socJenisUnit",jenisUnit, "id=\"socJenisUnit\" style=\"width:100%\" ", "onchange=\" $('err_socJenisUnit').innerHTML=''; at(this, event);\""));
	}
	
	private Date getDate(String paramName) {
		Date dateTxt = null;
		String dateParam = request.getParameter(paramName);
		if ( dateParam != null && !"".equals(dateParam)) {
			try {
				dateTxt = new SimpleDateFormat("dd-MM-yyyy").parse(dateParam);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateTxt;
	}
	
}

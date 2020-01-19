package bph.modules.kewangan.deposit;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lebah.db.Db;
import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewDepositWarta;
import bph.entities.kewangan.KewWarta;
import bph.utils.DataUtil;
import bph.utils.Util;

public class PewartaanDeposit extends AjaxBasedModule {

	private static final long serialVersionUID = 1L;
	private static String path = "bph/modules/kewangan/deposit/pewartaan/";
	static Logger myLogger = Logger.getLogger(PewartaanDeposit.class);
	protected DbPersistence db;
	private DataUtil dataUtil;
	
	@Override
	public String doTemplate2() throws Exception {

		db = new DbPersistence();
		dataUtil = DataUtil.getInstance(db);
		String userIdLogin = (String) request.getSession().getAttribute("_portal_login");
		String vm = "";
		String pageIndex = "index.vm";
		String command = getParam("command");
		String selectedTab = "1";
		context.put("util", new Util());
		
		clearcontext();
		
		List<KewWarta> listWarta = null;
				
		if(command.equalsIgnoreCase("getSkrinBelumWarta")){
			selectedTab = "1";
			vm = path + "/belumWarta.vm";
		}else if(command.equalsIgnoreCase("getSkrinProsesWarta")){
			selectedTab = "2";
			listWarta = dbListWarta();
			vm = path + "/prosesWarta.vm";
		}else if(command.equalsIgnoreCase("getSavePilihanHantarWarta")){
			selectedTab = "2";
			saveHantarWarta();
			listWarta = dbListWarta();
			vm = path + "/prosesWarta.vm";
		}else if(command.equalsIgnoreCase("getUpdateWarta")){
			selectedTab = "2";
			updateWarta();
			listWarta = dbListWarta();
			vm = path + "/prosesWarta.vm";
		}else if(command.equalsIgnoreCase("getSenaraiPendeposit")){
			List<KewDepositWarta> listDepositWarta = dbListDepositWarta();
			context.put("listDepositWarta", listDepositWarta);
			KewWarta objW = db.find(KewWarta.class, getParam("idWarta"));
			context.put("objW", objW);
			vm = path + "/senaraiPendeposit.vm";
		}
		
		
		/**SENARAI WARTA DALAM PROSES / TELAH DIWARTAKAN **/
		context.put("listWarta", listWarta);
		
		/**SENARAI PENDEPOSIT KUARTERS TAK TUNTUT > 12bln */
		List<KewDeposit> listWtd = dbListWtd();
		context.put("listWtd", listWtd);
		context.put("userLogin", userIdLogin);
		
		/**TAB*/
		context.put("selectedTab", selectedTab);
		
		if(vm==""){vm = path+pageIndex;}
		context.put("templateDir",path);
		return vm;
	}
	
	private void clearcontext(){
		context.remove("");
	}
	
	/**
	 * Get senarai pendepositwarta
	 * */
	@SuppressWarnings("unchecked")
	private List<KewDepositWarta> dbListDepositWarta() {
		String idWarta = getParam("idWarta");
		List<KewDepositWarta> list = db.list("select x from KewDepositWarta x where x.warta.id = '"+idWarta+"' ");
		return list;
	}
	
	
	/**
	 * Update warta
	 * */
	public void updateWarta(){
		String idWarta = getParam("idWarta");
		String noWarta = getParam("noWarta"+idWarta);
		Date tarikhWarta = getDate("tarikhWarta"+idWarta);
		
		KewWarta w = db.find(KewWarta.class, idWarta);
		db.begin();
		w.setNoWarta(noWarta);
		w.setTarikhWarta(tarikhWarta);
		try {
			db.commit();
		} catch (Exception e) {
			myLogger.error("error updateWarta : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Save hantar warta
	 * */
	public void saveHantarWarta(){
		String[] listIdDeposit = request.getParameterValues("cbDeposit");
		Date tarikhHantarWarta = getDate("tarikhHantarWarta"); 
		String catatanHantarWarta = getParam("catatanHantarWarta");
		
		db.begin();
		
		KewWarta w = new KewWarta();
		w.setCatatanHantarWarta(catatanHantarWarta);
		w.setTarikhHantarWarta(tarikhHantarWarta);
		db.persist(w);
		
		try {
			db.commit();
		}catch (Exception e) {
			myLogger.error("error saveHantarWarta : "+e.getMessage());
			e.printStackTrace();
		}finally{
			db.begin();
			for(String id : listIdDeposit){
				KewDeposit d = db.find(KewDeposit.class, id);
				KewDepositWarta dw = new KewDepositWarta();
				dw.setDeposit(d);
				dw.setWarta(w);
				db.persist(dw);

				d.setFlagWarta("Y");
			}
			
			try {
				db.commit();
			} catch (Exception e) {
				myLogger.error("error saveHantarWarta.listDeposit : "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Get senarai wang tidak dituntut
	 * */
	private List<KewDeposit> dbListWtd() {
		
		ArrayList<KewDeposit> list = new ArrayList<KewDeposit>();
		Db db1 = null;
		try {
			db1 = new Db();
			
			//String sql = "select a.id from kew_deposit a where a.id_kod_hasil = '72310' and ifnull(a.flag_warta,'T') <> 'Y'  ";
			String sql = "select a.id from kew_deposit a, kua_akaun b, kua_penghuni c "
					+ " where a.id_lejar = b.id "
					+ " and b.id_pembayar = c.id_pemohon "
					+ " and a.id_kod_hasil = '72310' "
					+ " and ifnull(a.flag_warta,'T') <> 'Y' "
					+ " and a.id_jenis_bayaran = '01' "
					+ " and c.tarikh_keluar_kuarters is not null "
					+ " and date_add(c.tarikh_keluar_kuarters,INTERVAL 12 month) <= current_date() order by a.tarikh_bayaran desc";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				KewDeposit kd = db.find(KewDeposit.class, rs.getString("id"));
				list.add(kd);
			}	
			
		}catch(Exception e){
			myLogger.error("error dbListWtd : "+e.getMessage());
			e.printStackTrace();
		}finally { 
			if ( db1 != null ) db1.close();
		}
		return list;
	}
	
	/**
	 * Get senarai warta
	 * */
	@SuppressWarnings("unchecked")
	private List<KewWarta> dbListWarta() {
		List<KewWarta> list = db.list("select x from KewWarta x order by x.tarikhHantarWarta desc");
		return list;
	}
	
	public Date getDate(String paramName) {
		Date dateTxt = null;
		String dateParam = request.getParameter(paramName);
		if (dateParam != null && !"".equals(dateParam)) {
			try {
				dateTxt = new SimpleDateFormat("dd-MM-yyyy").parse(dateParam);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dateTxt;
	}
	
}

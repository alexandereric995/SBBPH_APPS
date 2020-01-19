package bph.modules.integrasi;

import java.text.DecimalFormat;
import java.util.List;

import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;
import bph.entities.integrasi.IntJANMRekod;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class JANMSubsidiariRecordModule extends AjaxBasedModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DbPersistence db;
	private MyPersistence mp;
	private DataUtil dataUtil;
	private String path = "bph/modules/integrasi/JANMSubsidiari";

	@Override
	public String doTemplate2() throws Exception {
		String command = getParam("command");
		context.put("command", command);
		String vm = "/start.vm";
		context.put("path", path);
		context.put("util", new Util());
		
		try {
			db = new DbPersistence();
			mp = new MyPersistence();
			
			dataUtil = DataUtil.getInstance(db);
			context.put("selectBulan", dataUtil.getListBulan());
			context.put("selectAgBrancCode", dataUtil.getListCawanganJANM());
			context.put("selectAgDepartmentCode", dataUtil.getListJabatanPembayarJANM());
			
			String sql = "";
			List<IntJANMRekod> listRekod = null;
			if ("doCarian".equals(command)) {
				
				sql = "select x from IntJANMRekod x where x.id is not null";
				if (!getParam("findAgBranchCode").equals("")) {
					sql = sql + " and x.janm.agBranch.id = '" + getParam("findAgBranchCode") + "'";
				}
				if (!getParam("findDepartmentCode").equals("")) {
					sql = sql + " and x.department.id = '" + getParam("findDepartmentCode") + "'";
				}
				if (!getParam("tarikhDari").equals("") && !getParam("tarikhHingga").equals("")) {
					String tarikhDari = getParam("tarikhDari");
					String tarikhHingga = getParam("tarikhHingga");

					String dateDari = new DecimalFormat("0000").format(Integer.parseInt(tarikhDari.substring(3, tarikhDari.length()))) + new DecimalFormat("00").format(Integer.parseInt(tarikhDari.substring(0, 2)));
					String dateHingga = new DecimalFormat("0000").format(Integer.parseInt(tarikhHingga.substring(3, tarikhHingga.length()))) + new DecimalFormat("00").format(Integer.parseInt(tarikhHingga.substring(0, 2)));

					sql = sql + " and x.janm.date between '" + dateDari + "' and '" + dateHingga + "'";
				}
				if (!getParam("findPersonnelNo").equals("")) {
					sql = sql + " and x.personnelNo like '%" + getParam("findPersonnelNo") + "%'";
				}
				if (!getParam("findIc").equals("")) {
					sql = sql + " and x.ic like '%" + getParam("findIc") + "%'";
				}
				if (!getParam("findAccNo").equals("")) {
					sql = sql + " and x.accountNo like '%" + getParam("findAccNo") + "%'";
				}
				if (!getParam("findName").equals("")) {
					sql = sql + " and x.name like '%" + getParam("findName") + "%'";
				}
				if (!getParam("findDeductionCode").equals("")) {
					sql = sql + " and x.deductionCode like '%" + getParam("findDeductionCode") + "%'";
				}
				sql = sql + " order by x.janm.date desc";
				listRekod = mp.list(sql);
			
				vm = "/result_page.vm";
			} else if ("doReset".equals(command)) {
				vm = "/result_page.vm";
			}
			context.put("listRekod", listRekod);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			vm = "/result_page.vm";
		} finally {
			if (mp != null) { mp.close(); }
		}
		return path + vm;
	}
}

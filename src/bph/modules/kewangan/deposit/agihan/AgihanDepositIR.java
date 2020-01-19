package bph.modules.kewangan.deposit.agihan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.OperatorDateBetween;
import lebah.template.OperatorMultipleValue;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.utils.UtilKewangan;

public class AgihanDepositIR extends AgihanDeposit{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/agihan/rumahPeranginan"; }
	
	public void filtering(){
		this.addFilter("jenisTuntutan.id = '02' ");
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String findPemohon = getParam("findPemohon");
		String findNoKp = getParam("findNoKp");
		String findNoTempahan = getParam("findNoTempahan");
		String findPeranginan = getParam("findPeranginan");
		Date findTarikhMohonDeposit = getDate("findTarikhMohonDeposit");
		Date findTarikhMohonDeposit2 = getDate("findTarikhMohonDeposit2");
		String findStatus = getParam("findStatus");

		String strdate = "";
		if(findTarikhMohonDeposit!=null){
			strdate = new SimpleDateFormat("yyyy-MM-dd").format(findTarikhMohonDeposit);
		}
		
		String strdate2 = "";
		if(findTarikhMohonDeposit2!=null){
			strdate2 = new SimpleDateFormat("yyyy-MM-dd").format(findTarikhMohonDeposit2);
		}
		
		Map<String, Object> q = new HashMap<String, Object>();
		q.put("findPemohon", findPemohon);
		q.put("findNoKp", findNoKp);
		q.put("findNoTempahan", findNoTempahan);
		q.put("findPeranginan", findPeranginan);
		q.put("findTarikhMohonDeposit", strdate);
		q.put("findTarikhMohonDeposit2", strdate2);
		q.put("findStatus", findStatus);
		
		List<KewTuntutanDeposit> list = UtilKewangan.listTuntutanDeposit(db,userRole,q);
		
		String multipleItem = "";
		String valueIn = "('')";
		for (int i = 0; i< list.size(); i++) {
			if(list != null){
				if (multipleItem.length() == 0) {
					multipleItem = "'" + list.get(i).getId() + "'";
				} else {
					multipleItem = multipleItem + "," + "'" + list.get(i).getId() + "'";
				}
			}
		}
		
		if(!multipleItem.equalsIgnoreCase("")){
			valueIn = "("+multipleItem+")";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", new OperatorMultipleValue(valueIn) );
		map.put("tarikhPermohonan", new OperatorDateBetween(getDate("findTarikhMohonDeposit"), getDate("findTarikhMohonDeposit2")));
		return map;
	}
	
	@Override
	public void getRelatedData(KewTuntutanDeposit r) {
		
		RppAkaun akaun = (RppAkaun) db.get("Select x from RppAkaun x where x.id='"+r.getDeposit().getIdLejar()+"'");
		
		RppPermohonan p = (RppPermohonan) db.get("select x from RppPermohonan x where x.id ='" + akaun.getPermohonan().getId() +"'");
		if(r!=null){
			context.put("permohonan", p);
		}
	}
}

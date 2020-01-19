package bph.modules.kewangan.deposit.pelarasan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.OperatorMultipleValue;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.utils.UtilKewangan;

public class PelarasanDepositIR extends PelarasanDeposit{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/pelarasan/rumahPeranginan"; }
	
	public void filtering(){
		this.addFilter("jenisTuntutan.id = '02' ");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KewTuntutanDeposit r) {
		String idLejar = r.getDeposit().getIdLejar();
		RppAkaun ak = db.find(RppAkaun.class, idLejar);
		List<RppAduanKerosakan> listKerosakan = db.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+ak.getPermohonan().getId()+"' ");
		
		RppPermohonan p = (RppPermohonan) db.get("select x from RppPermohonan x where x.id ='" + ak.getPermohonan().getId() +"'");
		if(r!=null){
			context.put("permohonan", p);
		}
		
		context.put("listKerosakan",listKerosakan);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void afterSave(KewTuntutanDeposit r) { 
		String idLejar = r.getDeposit().getIdLejar();
		RppAkaun ak = db.find(RppAkaun.class, idLejar);
		List<RppAduanKerosakan> listKerosakan = db.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+ak.getPermohonan().getId()+"' ");
		db.begin();
		for(int i=0;i<listKerosakan.size();i++){
			RppAduanKerosakan ad = listKerosakan.get(i);
			ad.setBaucerJurnal(getParam("baucerJurnal"+ad.getId()));
		}
		
		try {
			db.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String findPemohon = getParam("findPemohon");
		String findNoKp = getParam("findNoKp");
		String findNoTempahan = getParam("findNoTempahan");
		String findPeranginan = getParam("findPeranginan");
		Date findTarikhMohonDeposit = getDate("findTarikhMohonDeposit");

		String strdate = "";
		if(findTarikhMohonDeposit!=null){
			strdate = new SimpleDateFormat("yyyy-MM-dd").format(findTarikhMohonDeposit);
		}
		
		Map<String, Object> q = new HashMap<String, Object>();
		q.put("findPemohon", findPemohon);
		q.put("findNoKp", findNoKp);
		q.put("findNoTempahan", findNoTempahan);
		q.put("findPeranginan", findPeranginan);
		q.put("findTarikhMohonDeposit", strdate);
		q.put("findStatus", "");
		
		List<KewTuntutanDeposit> list = UtilKewangan.listTuntutanDeposit(db,userRole,q);
		
		String multipleItem = "";
		String valueIn = "";
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
		if(!valueIn.equalsIgnoreCase("")){
			map.put("id", new OperatorMultipleValue(valueIn) );
		}
		
		return map;
	}
}

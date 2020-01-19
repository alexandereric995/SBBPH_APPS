package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bph.entities.kod.Status;
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppGambarAduan;
import db.persistence.MyPersistence;

public class HQAduanRecordModule extends SenaraiAduanKerosakanRecordModule{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/hQAduanKerosakan";
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("barangDeposit.id", getParam("findBarangDeposit"));	
		map.put("tarikhAduan", getDate("findTarikhAduan"));
		map.put("status.id", getParam("findStatus"));
		return map;
	}
	
	public void addfilter(MyPersistence mp,String userId) {
		//Remove ALL filter from main controller
	}
	
	public void hQdefaultButtonOption() {
		this.setReadonly(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppAduanKerosakan r) {
		//Update status from aduan baru kepada aduan diterima
		String status = (r.getStatus()!=null?r.getStatus().getId():"");
		if(status.equalsIgnoreCase("1429870728744")){ 
			updateStatusAduan(r);
		}
		
		List<RppGambarAduan> senaraiGambar = db.list("select x from RppGambarAduan x where x.aduanKerosakan.id = '"+r.getId()+"' ");
		context.put("senaraiGambar", senaraiGambar);
	}
	
	private void updateStatusAduan(RppAduanKerosakan r){
		db.begin();
		r.setStatus(db.find(Status.class,"1429870728755"));
		r.setTarikhTerimaAduan(new Date());
		try {
			db.commit();
		} catch (Exception e) {
			System.out.println("error updateStatusAduan : "+e.getMessage());
		}
	}
	
	@Override
	public void save(RppAduanKerosakan r) throws Exception {
		r.setUlasanHq(getParam("ulasanHq"));
		r.setTarikhUlasanHq(new Date());
		
		String flagSelesai = getParam("flagSelesai");
		if(flagSelesai.equalsIgnoreCase("Y")){
			r.setFlagSelesai(flagSelesai);
			r.setStatus(db.find(Status.class,"1429870728761"));
		}else{
			r.setFlagSelesai(null);
			r.setStatus(db.find(Status.class,"1429870728755"));
		}
		
	}
	
}

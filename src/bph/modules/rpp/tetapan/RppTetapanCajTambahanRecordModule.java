package bph.modules.rpp.tetapan;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.rpp.RppTetapanCajTambahan;
import bph.utils.DataUtil;
import bph.utils.Util;


public class RppTetapanCajTambahanRecordModule extends LebahRecordTemplateModule<RppTetapanCajTambahan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppTetapanCajTambahan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		
		context.put("listCajTambahan", dataUtil.getListCajTambahan());
		this.setDisableKosongkanUpperButton(true);
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/tetapan/cajTambahan";
	}

	@Override
	public Class<RppTetapanCajTambahan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppTetapanCajTambahan.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keterangan", getParam("findKeterangan"));
		return map;
	}
	
	@Override
	public boolean delete(RppTetapanCajTambahan r) throws Exception {
		boolean del = true;
		if( r.getId().equalsIgnoreCase("1432867359415") || r.getId().equalsIgnoreCase("1432887883848")
			|| r.getId().equalsIgnoreCase("1436755298337") ){
			del = false;
		}
		return del;
	}

	@Override
	public void getRelatedData(RppTetapanCajTambahan r) {
		
	}
	
	@Override
	public void save(RppTetapanCajTambahan r) throws Exception {
		r.setKeterangan(getParam("keterangan"));
		r.setCajBayaran(Util.getDoubleRemoveComma(getParam("cajBayaran")));
	}
	
	
}

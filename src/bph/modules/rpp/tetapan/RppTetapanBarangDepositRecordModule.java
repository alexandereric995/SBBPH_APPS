package bph.modules.rpp.tetapan;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.rpp.RppTetapanBarangDeposit;
import bph.utils.DataUtil;
import bph.utils.Util;

public class RppTetapanBarangDepositRecordModule extends LebahRecordTemplateModule<RppTetapanBarangDeposit>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppTetapanBarangDeposit r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		dataUtil = DataUtil.getInstance(db);
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		
		context.put("userRole", userRole);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		context.put("command", command);
		context.put("util", new Util());
		context.put("path", getPath());
	}

	@Override
	public boolean delete(RppTetapanBarangDeposit r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;
			context.put("error_flag","record_delete_success" );
		}
		return val;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/tetapan/barangDeposit";
	}

	@Override
	public Class<RppTetapanBarangDeposit> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppTetapanBarangDeposit.class;
	}

	@Override
	public void getRelatedData(RppTetapanBarangDeposit r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(RppTetapanBarangDeposit r) throws Exception {
		// TODO Auto-generated method stub
		
		r.setNama(getParam("nama"));
		r.setKeterangan(getParam("keterangan"));
		r.setHarga(Util.getDoubleRemoveComma(getParam("harga")));
		r.setFlagKategori(getParam("flagKategori"));
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nama", getParam("find_nama"));
		map.put("keterangan", getParam("find_keterangan"));
		return map;
	}

	
}

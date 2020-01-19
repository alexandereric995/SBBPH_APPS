package bph.modules.rpp;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import bph.entities.rpp.RppKemudahan;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiKemudahanRPPRecordModule extends LebahRecordTemplateModule<RppKemudahan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppKemudahan kemudahan) {
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
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		context.put("selectPeranginan", dataUtil.getListPeranginanRpp());
		
		RppPeranginan lokasi = null;
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			RppPenyeliaPeranginan rppPenyeliaPeranginan = (RppPenyeliaPeranginan) db.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+userId+"' and x.statusPerkhidmatan = 'Y'");
			lokasi = (RppPeranginan) (rppPenyeliaPeranginan!=null?rppPenyeliaPeranginan.getPeranginan():"");
			this.addFilter("peranginan.id = '" + lokasi.getId() + "'");
		}
		
		context.put("rppPeranginan", lokasi);
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public boolean delete(RppKemudahan kemudahan) throws Exception {
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/senaraiKemudahanRPP";
	}

	@Override
	public Class<RppKemudahan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppKemudahan.class;
	}

	@Override
	public void getRelatedData(RppKemudahan kemudahan) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(RppKemudahan kemudahan) throws Exception {

		kemudahan.setPeranginan(db.find(RppPeranginan.class, getParam("idPeranginan")));
		kemudahan.setNama(get("nama"));
		kemudahan.setBilangan(getParamAsInteger("bilangan"));
		kemudahan.setJenisKadarSewa(get("jenisKadarSewa"));
		kemudahan.setCatatan(get("catatan"));
		kemudahan.setFlagSewa(getParam("flagSewa"));
		
		Double kadar = 0d;
		if(getParam("flagSewa").equalsIgnoreCase("Y")){
			kadar = Util.getDoubleRemoveComma(getParam("kadarSewa"));
		}else{
			kadar = 0d;
		}
		
		kemudahan.setKadarSewa(kadar);
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("peranginan.id", get("findPeranginan"));
		map.put("nama", get("findNama"));

		return map;
	}
}

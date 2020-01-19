package bph.modules.rpp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.utils.DataUtil;

public class OperatorNoShow extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {return String.class; }

	@Override
	public Class<RppPermohonan> getPersistenceClass() {return RppPermohonan.class; }

	@Override
	public String getPath() {return "bph/modules/rpp/operatorNoShow"; }

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String listPeranginan = "";
		
		List<RppPenyeliaPeranginan> listPenyelia = db.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + userId + "'");
		for (int i = 0; i < listPenyelia.size(); i ++) {
			if ("".equals(listPeranginan)) {
				listPeranginan = "'" + listPenyelia.get(i).getPeranginan().getId() + "'";
			} else {
				listPeranginan = listPeranginan + "," + "'" + listPenyelia.get(i).getPeranginan().getId() + "'";
			}
		}
		
		if ("".equals(listPeranginan)) {
			this.addFilter("rppPeranginan.id = '" + listPeranginan + "'");
		} else {
			this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
		}
		
		this.addFilter("statusBayaran = 'Y'");
		this.addFilter("status.id = '1433083787409' ");
		
		this.setOrderBy("tarikhMasukRpp");
		this.setOrderType("asc");
		
		defaultButtonOption();		
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	@Override
	public void save(RppPermohonan r) throws Exception { }

	@Override
	public boolean delete(RppPermohonan r) throws Exception {return false; }

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noTempahan", getParam("findNoTempahan"));	
		map.put("pemohon.userName", getParam("findNama"));
		map.put("pemohon.noKP", getParam("findNoPengenalan"));		
		map.put("tarikhPermohonan", new OperatorDateBetween(getDate("findTarikhMohonMula"), getDate("findTarikhMohonHingga")));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukMula"), getDate("findTarikhMasukHingga")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhKeluarMula"), getDate("findTarikhKeluarMula")));
		return map;
	}

	@Override
	public void beforeSave() { }

	@Override
	public void afterSave(RppPermohonan r) { }

	@Override
	public void getRelatedData(RppPermohonan r) { }	
	
}

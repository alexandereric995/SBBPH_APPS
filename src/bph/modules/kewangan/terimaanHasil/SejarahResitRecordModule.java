/**
 * @author nurasna
 */

package bph.modules.kewangan.terimaanHasil;

import java.util.Hashtable;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import bph.entities.kewangan.KewBayaranLainTemp;
import bph.utils.UtilKewangan;

public class SejarahResitRecordModule extends LebahRecordTemplateModule<KewBayaranLainTemp> {

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(KewBayaranLainTemp arg0) {
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void begin() {

		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		this.setOrderBy("tarikhResit");
		this.setOrderType("desc");

		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}

	@Override
	public boolean delete(KewBayaranLainTemp arg0) throws Exception {
		return false;
	}

	@Override
	public String getPath() {
		return "bph/modules/kewangan/sejarahResit";
	}

	@Override
	public Class<KewBayaranLainTemp> getPersistenceClass() {
		return KewBayaranLainTemp.class;
	}

	@Override
	public void getRelatedData(KewBayaranLainTemp r) {

	}

	@Override
	public void save(KewBayaranLainTemp r) throws Exception {

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Hashtable<String, Object> map = new Hashtable<String, Object>();

		map.put("noKP", get("findNoKp").trim());
		map.put("nama", get("findNamaPembayar").trim());
		map.put("noResit", getParam("findNoResit").trim());
		map.put("tarikhResit", new OperatorDateBetween(getDate("findTarikhResitMula"), getDate("findTarikhResitHingga")));
		
		return map;
	}
}

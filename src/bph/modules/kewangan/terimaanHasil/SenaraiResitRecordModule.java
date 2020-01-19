/**
 * @author nurasna
 */

package bph.modules.kewangan.terimaanHasil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import lebah.template.OperatorMultipleValue;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class SenaraiResitRecordModule extends LebahRecordTemplateModule<KewBayaranResit> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(KewBayaranResit arg0) {
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);

		context.put("selectKodHasil", dataUtil.getListKodHasil());

		context.put("util", util);

		defaultButtonOption();
		addfilter();
		// TODO IMPLEMENT BILA ADA SUBCLASS
		doOverideFilterRecord();
	}

	// TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {

	}

	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}

	private void addfilter() {
		this.setOrderBy("tarikhBayaran");
		this.setOrderType("desc");
	}

	@Override
	public boolean delete(KewBayaranResit r) throws Exception {
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kewangan/senaraiResit";
	}

	@Override
	public Class<KewBayaranResit> getPersistenceClass() {
		return KewBayaranResit.class;
	}

	@Override
	public void getRelatedData(KewBayaranResit r) {
		RppPermohonan rpp = null;
		context.remove("rpp");
		RppRekodTempahanLondon london = null;
		context.remove("london");
		UtilPermohonan gelanggang = null;
		context.remove("gelanggang");
		
		try {
			if (r.getIdPermohonan() != null) {
				rpp = db.find(RppPermohonan.class, r.getIdPermohonan());
				if (rpp != null) {
					context.put("rpp", rpp);
				}
				
				london = db.find(RppRekodTempahanLondon.class, r.getIdPermohonan());
				if (london != null) {
					context.put("london", london);
				}
				
				gelanggang = db.find(UtilPermohonan.class, r.getIdPermohonan());
				if (gelanggang != null) {
					context.put("gelanggang", gelanggang);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void save(KewBayaranResit r) throws Exception {

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findKodHasil = get("findKodHasil");
		String findStatusResit = get("findStatusResit");
		Map<String, Object> map = new HashMap<String, Object>();

		String multipleItem = "";
		String valueIn = "('')";
		List<String> listPembayar = null;

		if (findKodHasil.length() > 0) {
			listPembayar = UtilKewangan.listPembayar(db, findKodHasil);
			if (listPembayar != null) {
				for (String i : listPembayar) {
					if (multipleItem.length() == 0) {
						multipleItem = "'" + i + "'";
					} else {
						multipleItem = multipleItem + "," + "'" + i + "'";
					}
				}
			}

			if (!multipleItem.equalsIgnoreCase("")) {
				valueIn = "(" + multipleItem + ")";
			}

			map.put("id", new OperatorMultipleValue(valueIn));
		}

		map.put("noPengenalanPembayar", getParam("findNoKp").trim());
		map.put("namaPembayar", getParam("findNamaPembayar").trim());
		map.put("noResit", getParam("findNoResit").trim());
		map.put("noResitLama", getParam("findNoResitLama").trim());
		map.put("idTransaksiBank", getParam("findFpxId").trim());
		map.put("flagJenisBayaran", getParam("findJenisBayaran").trim());
		map.put("tarikhBayaran", new OperatorDateBetween(
				getDate("findTarikhBayaran"),
				getDate("findTarikhBayaranHingga")));
		map.put("flagVoid", new OperatorEqualTo(findStatusResit));
		
		return map;
	}

	@Command("resetBilanganCetak")
	public String resetBilanganCetak() throws Exception {
		String idResit = getParam("id");
		try {
			KewBayaranResit resit = db.find(KewBayaranResit.class, idResit);
			if (resit != null) {
				db.begin();
				resit.setBilCetakResit(0);
				db.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		
		return getPath() + "/entry_page.vm";
	}
}

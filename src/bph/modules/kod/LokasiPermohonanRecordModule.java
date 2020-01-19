package bph.modules.kod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Bandar;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Negeri;
import bph.utils.DataUtil;

public class LokasiPermohonanRecordModule extends LebahRecordTemplateModule<LokasiPermohonan> {

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
	public void afterSave(LokasiPermohonan arg0) {
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
		context.remove("selectNegeri");
		
		List<Negeri> NegeriList = dataUtil.getListNegeri();
		context.put("selectNegeri", NegeriList);
		context.put ("path",getPath());
	}

	@Override
	public boolean delete(LokasiPermohonan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/lokasiPermohonan";
	}

	@Override
	public Class<LokasiPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return LokasiPermohonan.class;
	}

	@Override
	public void getRelatedData(LokasiPermohonan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(LokasiPermohonan r) throws Exception {
		// TODO Auto-generated method stub
		r.setId(get("id"));
		r.setLokasi(get("lokasi"));
		r.setBandar(db.find(Bandar.class, get("idBandar")));
		r.setNegeri(db.find(Negeri.class, get("idNegeri")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", find_id);
		map.put("keterangan", find_keterangan);
		return map;
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		
		String idNegeri = "0";

		if (get("idNegeri").trim().length() > 0) 
			idNegeri = get("idNegeri");
		
		List<Bandar> BandarList = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", BandarList);

		return getPath() + "/selectBandar.vm";
	}
}

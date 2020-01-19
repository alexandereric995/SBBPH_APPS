package bph.modules.pembangunan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Bandar;
import bph.entities.kod.Negeri;
import bph.entities.pembangunan.DevRuang;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiRuangRecordModule extends LebahRecordTemplateModule<DevRuang> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8238850097171829701L;
	private DataUtil du;
	private Util util = new Util();

	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(DevRuang ruang) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		du = DataUtil.getInstance(db);
		this.setOrderBy("aras.bangunan.premis.id");
		this.setOrderType("asc");
		this.setReadonly(true);
		this.setDisableDefaultButton(true);	
		
		context.remove("selectZon");
		context.remove("selectNegeri");
		context.put("selectZon", du.getListZon());
		context.put("selectNegeri", du.getListNegeri());
		
		context.put("path", getPath());
		context.put("util", util);
		context.put("command", command);
	}

	@Override
	public boolean delete(DevRuang ruang) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/pembangunan/ruang";
	}

	@Override
	public Class<DevRuang> getPersistenceClass() {
		// TODO Auto-generated method stub
		return DevRuang.class;
	}

	@Override
	public void getRelatedData(DevRuang ruang) {
		context.put("selectedTab", "1");
	}

	@Override
	public void save(DevRuang ruang) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();		
		
		m.put("aras.bangunan.premis.bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		m.put("aras.bangunan.premis.bandar.id", new OperatorEqualTo(get("findBandar")));
		m.put("aras.bangunan.premis.namaPremis", get("findNamaPremis"));	
		m.put("aras.bangunan.namaBangunan", get("findNamaBangunan"));	
		m.put("aras.namaAras", get("findNamaAras"));
		m.put("namaRuang", get("findNamaRuang"));	
		m.put("aras.bangunan.premis.mukim.daerah.negeri.zon.id", get("findZon"));
		
		return m;
	}
	
	/** START SENARAI TAB **/
	@Command("findNegeri")
	public String findNegeri() throws Exception {	
		
		String idZon = "0";
		if (get("findZon").trim().length() > 0)
			idZon = get("findZon");
		List<Negeri> list = du.getListNegeri(idZon);
		context.put("selectNegeri", list);
		
		return getPath() + "/findNegeri.vm";
	}
	
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Bandar> list = du.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("getMaklumatRuang")
	public String getMaklumatRuang() {
		
		DevRuang ruang = db.find(DevRuang.class, get("idRuang"));
		context.put("r", ruang);

		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPremis")
	public String getMaklumatPremis() {
		
		DevRuang ruang = db.find(DevRuang.class, get("idRuang"));
		context.put("r", ruang);

		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END SENARAI TAB **/
}
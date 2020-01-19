package bph.modules.kod;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.JenisBangunan;
import bph.entities.kod.TermaSyaratRpp;
import bph.utils.DataUtil;

public class TermaSyaratRppRecordModule extends LebahRecordTemplateModule<TermaSyaratRpp> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(TermaSyaratRpp r) {
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
		
		context.put("selectKategori", dataUtil.getListJenisPeranginan());
		context.put("path", getPath());
	}

	@Override
	public boolean delete(TermaSyaratRpp r) throws Exception {
		// TODO Auto-generated method stub
		context.put("error_flag","record_delete_success" );
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/termaSyaratRpp";
	}

	@Override
	public Class<TermaSyaratRpp> getPersistenceClass() {
		// TODO Auto-generated method stub
		return TermaSyaratRpp.class;
	}

	@Override
	public void getRelatedData(TermaSyaratRpp r) {
		context.put("selectKategori", dataUtil.getListJenisPeranginan());
	}

	@Override
	public void save(TermaSyaratRpp r) throws Exception {
		//r.setId(get("id"));
		r.setKeterangan(get("keterangan"));
		r.setKategoriTerma(db.find(JenisBangunan.class, getParam("kategoriTerma")));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		//String find_id = get("find_id");
		String find_keterangan = get("find_keterangan");
		String find_kategori = get("find_kategori");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keterangan", find_keterangan);
		map.put("kategoriTerma.id",new OperatorEqualTo(find_kategori));
		return map;
	}
}

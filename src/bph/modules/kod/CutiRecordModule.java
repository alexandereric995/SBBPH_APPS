package bph.modules.kod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.Cuti;
import bph.utils.Util;

public class CutiRecordModule extends LebahRecordTemplateModule<Cuti> {

	private static final long serialVersionUID = 1L;
	//private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(Cuti r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		//dataUtil = DataUtil.getInstance(db);
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		
		context.put("path", getPath());
		context.put("util", new Util());
	}

	@Override
	public boolean delete(Cuti r) throws Exception {
		// TODO Auto-generated method stub
		context.put("error_flag","record_delete_success" );
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kod/cuti";
	}

	@Override
	public Class<Cuti> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Cuti.class;
	}

	@Override
	public void getRelatedData(Cuti r) {

	}

	@Override
	public void save(Cuti r) throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users users = db.find(Users.class, userId);
		
		if(r.getIdMasuk()!=null){
			r.setIdKemaskini(users);
			r.setTarikhKemaskini(new Date());
		}else{
			r.setIdMasuk(users);
			r.setTarikhMasuk(new Date());
		}
		
		r.setPerkara(getParam("perkara"));
		r.setKeterangan(get("keterangan"));
		r.setTarikhDari(getDate("tarikhDari"));
		r.setTarikhHingga(getDate("tarikhHingga"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String perkara = get("findPerkara");
		String keterangan = get("findKeterangan");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("perkara", perkara);
		map.put("keterangan", keterangan);
		return map;
	}
}

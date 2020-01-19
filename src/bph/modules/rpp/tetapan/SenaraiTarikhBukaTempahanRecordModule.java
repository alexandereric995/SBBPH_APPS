package bph.modules.rpp.tetapan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.rpp.RppTetapanBukaTempahan;
import bph.utils.Util;

public class SenaraiTarikhBukaTempahanRecordModule extends LebahRecordTemplateModule<RppTetapanBukaTempahan> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppTetapanBukaTempahan r) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		List<RppTetapanBukaTempahan> c = db.list("SELECT x FROM RppTetapanBukaTempahan x ");
		for(RppTetapanBukaTempahan flag : c ) {
			flag.setFlagAktif("T");
		}
		
	}

	@Override
	public void begin() {
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		
		this.setOrderBy("tarikhBukaTempahan asc");
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
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
		return "bph/modules/rpp/tetapan/tarikhBukaTempahan";
	}

	@Override
	public Class<RppTetapanBukaTempahan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppTetapanBukaTempahan.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("perkara", get("findPerkara"));
		map.put("catatan", get("findCatatan"));
		
		return map;
	}
	
	@Override
	public boolean delete(RppTetapanBukaTempahan r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;		
		}
		return val;
	}

	@Override
	public void getRelatedData(RppTetapanBukaTempahan r) {

	}
	
	@Override
	public void save(RppTetapanBukaTempahan r) throws Exception {

		userId = (String) request.getSession().getAttribute("_portal_login");
		Users users = db.find(Users.class, userId);
		
		if(r.getIdMasuk()!=null){
			r.setIdKemaskini(users);
			r.setTarikhKemaskini(new Date());
		}else{
			r.setIdMasuk(users);
			r.setTarikhMasuk(new Date());
		}
		//r.getFlagAktif();
		
		r.setFlagAktif(get("flagAktif"));
		r.setPerkara(getParam("perkara"));
		r.setTarikhBukaTempahan(getDate("tarikhBukaTempahan"));
		r.setCatatan(get("catatan"));
		r.setTarikhMenginapDari(getDate("tarikhMenginapDari"));
		r.setTarikhMenginapHingga(getDate("tarikhMenginapHingga"));
		
	}

	@Command("openPopupCheckingTempahan")
	public String openPopupCheckingTempahan() throws Exception {
		String record_id = getParam("record_id");
		context.put("record_id", record_id);
		return getPath() + "/popup/todo.vm";
	}
	
}

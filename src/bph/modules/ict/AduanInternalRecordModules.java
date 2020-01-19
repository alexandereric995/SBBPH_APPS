package bph.modules.ict;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.ict.AduanInternal;
import bph.utils.DataUtil;

public class AduanInternalRecordModules extends LebahRecordTemplateModule<AduanInternal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String catatan;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(AduanInternal arg0) {
		// TODO Auto-generated method stub
		
		context.put("kemaskini", true);
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		dataUtil = DataUtil.getInstance(db);
//		
		userId = (String) request.getSession().getAttribute("_portal_login");
		Users users = db.find(Users.class, userId);
		
	}

	@Override
	public boolean delete(AduanInternal arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/customer";
	}

	@Override
	public Class<AduanInternal> getPersistenceClass() {
		// TODO Auto-generated method stub
		return AduanInternal.class;
	}

	@Override
	public void getRelatedData(AduanInternal arg0) {
		// TODO Auto-generated method stub
		context.put("kemaskini", true);
		
		
	}

	@Override
	public void save(AduanInternal customer) throws Exception {
		// TODO Auto-generated method stub
//		String idMasuk = getParam("idMasuk");
		Date tarikhAduan = getDate ("tarikh_aduan");
		String noPengenalan = get ("noPengenalan");
		String tajuk = get ("idTajuk");
		String butiran = get ("butiran");
		String id_masuk = get ("id_masuk");
		//String id_selesai = get ("id_selesai");
		String status = get ("status");
		String noTelefon = get ("noTelefon");
		String catatan = get ("catatan");
		//Date tarikhSelesai = getDate ("tarikh_selesai");
		
		customer.setTarikhAduan(new Date());
		customer.setNoPengenalan(noPengenalan);
		customer.setTajuk(tajuk);
		customer.setButiran(butiran);
		customer.setIdMasuk(db.find(Users.class, userId));
		//customer.setIdMasuk(id_masuk);
		//customer.setIdSelesai(db.find(Users.class, userId));
		//customer.setTarikhSelesai(new Date());
		customer.setStatus("BARU");
		customer.setNoTelefon(noTelefon);
		customer.setCatatan(catatan);

		if(catatan.length()>0){
			customer.setIdSelesai(db.find(Users.class, userId));
			customer.setTarikhSelesai(new Date());
			customer.setStatus("BARU");
			customer.setCatatan(catatan);
		}
		context.put("kemaskini", true);
}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stud
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noPengenalan", getParam("findNoPengenalan").trim());
		map.put("tajuk", getParam("findTajuk").trim());
		return map;
	}
	
	//**************** START SAVE APABILA EDIT ADUAN INTERNAL ****************
	@Command("saveAduanInternal")
	public synchronized String saveAduanInternal() throws Exception {
		
		db.begin();
		
		AduanInternal customer = db.find(AduanInternal.class, get("idAduan"));
		customer.setIdSelesai(db.find(Users.class, userId));
		customer.setTarikhSelesai(new Date());
		customer.setStatus("BARU");
		customer.setCatatan(catatan);
		
		db.commit();
		
	return saveAduanInternal();
	}
	//**************** END SAVE APABILA EDIT MAKLUMAT ADUAN ****************
	
	
	
	
}

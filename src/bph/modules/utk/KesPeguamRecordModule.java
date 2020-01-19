// Author : zulfazdliabuas@gmail.com Data 2015-2017

package bph.modules.utk;

import java.util.HashMap;
import java.util.Map;

import lebah.template.LebahRecordTemplateModule;
import portal.module.Util;
import bph.entities.utk.UtkKesPeguam;
import bph.utils.DataUtil;

public class KesPeguamRecordModule extends LebahRecordTemplateModule<UtkKesPeguam>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Util util = new Util();
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(UtkKesPeguam r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
		disableButton();
	}
	
	public void disableButton() {
//		setReadonly(true);
		setDisableAddNewRecordButton(true);
//		setDisableSaveAddNewButton(true);
//		setDisableAddNewRecordButton(true);
//		setDisableBackButton(true);
	}
	
	public void addFilter(){
//		this.addFilter("");
	}
	
//public void addFilter(){
//		
//		// PS & SELAIN 1435633886800 - PELANGGARAN SYARAT & SELAIN PENGUNCIAN TAYAR
////		this.addFilter("operasi.jenisOperasi.id = 'PS'");
////		this.addFilter("operasi.jenisPelanggaranSyarat.id != '1435633886800'");
////		pakai bawah ni
//		this.addFilter("jenisOperasi.id = 'PS'");
//		this.addFilter("jenisPelanggaranSyarat.id != '1435633886800'"); //ini adalah untuk status PENGUNCIAN TAYAR KENDERAAN
//	}

	@Override
	public boolean delete(UtkKesPeguam r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/kesPeguam";
	}

	@Override
	public Class<UtkKesPeguam> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtkKesPeguam.class;
	}

	@Override
	public void getRelatedData(UtkKesPeguam r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(UtkKesPeguam r) throws Exception {
		// TODO Auto-generated method stub
		
//		r.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
		r.setCatatan(get("catatan"));
		r.setTarikhKeputusan(getDate("tarikh"));
		r.setFlagKeputusan(get("flagKeputusan"));
		r.setStatusJenisKes(getParam("statusJenisKes"));
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		
		String namaPenghuni = get("namaPenghuni");
		String noKp = get("noKp");
		String noUnit = get("noUnit");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("penghuni.pemohon.noKP", noKp);
		map.put("penghuni.pemohon.userName", namaPenghuni);
		map.put("penghuni.kuarters.noUnit", noUnit);
		return map;
	}

}

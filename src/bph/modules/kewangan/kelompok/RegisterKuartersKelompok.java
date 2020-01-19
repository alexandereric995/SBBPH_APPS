package bph.modules.kewangan.kelompok;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KelompokKuarters;
import bph.entities.kewangan.KelompokKuartersPenghuni;
import bph.entities.qtr.KuaPenghuni;
import bph.utils.DataUtil;
import bph.utils.Util;

public class RegisterKuartersKelompok extends LebahRecordTemplateModule<KelompokKuarters> {
	
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	
	@Override
	public void begin() { 
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		Users objUserLogin = db.find(Users.class, userId);
		context.put("objUserLogin", objUserLogin);
		
		disabledButton();
		filtering();
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	public void filtering(){
		
	}
	
	public void disabledButton(){
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
	}

	@Override
	public boolean delete(KelompokKuarters r) throws Exception {
		boolean delete = true;
		if(r.getSenaraiKuartersPenghuni().size() > 0){
			delete = false;
		}
		return delete;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/kelompokKuarters"; }

	@Override
	public Class<KelompokKuarters> getPersistenceClass() {
		return KelompokKuarters.class;
	}

	@Override
	public void getRelatedData(KelompokKuarters r) {
		context.put("listKelompokPenghuni", r.getSenaraiKuartersPenghuni());
	}

	@Override
	public void beforeSave() { 

	}
	
	@Override
	public void save(KelompokKuarters r) throws Exception {
		r.setNamaAgensi(getParam("namaAgensi"));
		r.setKeterangan(getParam("keterangan"));
	}
	
	@Override
	public void afterSave(KelompokKuarters r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("namaAgensi", getParam("findNamaAgensi"));
		return map;
	}
	
	/** 
	 * POPUP SENARAI PENGHUNI
	 */
	@SuppressWarnings("unchecked")
	@Command("openPopupSenaraiPenghuni")
	public String openPopupSenaraiPenghuni() throws Exception {
		context.remove("listPenghuni");
		String mainId = getParam("mainId");
		KelompokKuarters r = db.find(KelompokKuarters.class, mainId);
		List<KuaPenghuni> listPenghuni = 
				db.list("select x from KuaPenghuni x where x.tarikhKeluar is null "
						+ "and x.id not in (select y.kuaPenghuni.id from KelompokKuartersPenghuni y where y.kelompokKuarters.id = '"+mainId+"' ) "
						+ "order by x.pemohon.userName ");
		context.put("r", r);
		context.put("listPenghuni", listPenghuni);
		return getPath() + "/popup/penghuni.vm";
	}
	
	/**
	 * SAVE PILIHAN POPUP
	 * */
	@Command("savePilihanPenghuni")
	public String savePilihanPenghuni() throws Exception {
		String mainId = getParam("mainId");
		KelompokKuarters r = db.find(KelompokKuarters.class, mainId);
		
		String[] pilihan = request.getParameterValues("cbPenghuni");
		db.begin();
		for(int i=0;i<pilihan.length;i++){
			String pid = pilihan[i];
			KuaPenghuni p = db.find(KuaPenghuni.class, pid);
			KelompokKuartersPenghuni kkp = new KelompokKuartersPenghuni();
			kkp.setKelompokKuarters(r);
			kkp.setKuaPenghuni(p);
			db.persist(kkp);
		}
		db.commit();
		
		context.put("r", r);
		context.put("listKelompokPenghuni", r.getSenaraiKuartersPenghuni());
		return templateDir + "/entry_fields.vm";
	}
	
	/**
	 * DELETE PILIHAN PENGHUNI
	 * */
	@Command("deletePilihanPenghuni")
	public String deletePilihanPenghuni() throws Exception {
		String mainId = getParam("mainId");
		String idPilihan = getParam("idPilihan");
		KelompokKuarters r = db.find(KelompokKuarters.class, mainId);
		KelompokKuartersPenghuni kkp = db.find(KelompokKuartersPenghuni.class, idPilihan);
				
		db.begin();
		db.remove(kkp);
		db.commit();
		
		context.put("r", r);
		context.put("listKelompokPenghuni", r.getSenaraiKuartersPenghuni());
		return templateDir + "/entry_fields.vm";
	}
	
	
}







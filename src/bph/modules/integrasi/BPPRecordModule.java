package bph.modules.integrasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import bph.entities.integrasi.IntBPP;
import bph.entities.kod.Daerah;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.utils.DataUtil;

public class BPPRecordModule extends LebahRecordTemplateModule<IntBPP>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil du;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntBPP r) {
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
		this.setReadonly(true);
		this.setDisableBackButton(true);
		
		this.setOrderBy("tarikhTerima");
		this.setOrderType("desc");
		
		context.put("selectMukim", du.getListMukim());
		context.put("selectDaerah", du.getListDaerah());
		List<Negeri> NegeriList = du.getListNegeri();
		context.put("selectNegeri", NegeriList);
		
	}

	@Override
	public boolean delete(IntBPP r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/BPP";
	}

	@Override
	public Class<IntBPP> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntBPP.class;
	}

	@Override
	public void getRelatedData(IntBPP r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(IntBPP r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		
		m.put("noPengenalan", getParam("findNoPengenalan"));
		m.put("nama", getParam("findNama"));
		m.put("noAkaun", getParam("findNoAkaun"));
		
		return m;
	}
	
	@Command("kemaskiniMaklumatBPP")
	public String kemaskiniMaklumatBPP() throws Exception {
		
//		Date tarikhTerima = getDate("tarikhTerima");
		
		//start
		IntBPP simpan = db.find(IntBPP.class, get("idBPP"));
//		System.out.println("masuk savee=======" + get("idBPP"));
		
		if(simpan == null){
			simpan = new IntBPP();
		}
		
		db.begin();
		simpan.setTarikhTerima(getDate("tarikhTerima"));
		simpan.setFlagJawapan(get("flagJawapan"));
		simpan.setMesej(get("mesej"));
		simpan.setKodJenisPinjaman(get("kodJenisPinjaman"));
		simpan.setNama(get("nama"));
//		simpan.setMukim(db.find(Mukim.class, get("findMukim")));
//		simpan.setDaerah(db.find(Daerah.class, get("findDaerah")));
//		simpan.setNegeri(db.find(Negeri.class, get("findNegeri")));
		simpan.setNoAkaun(get("noAkaun"));
		simpan.setStatusPinjaman(get("statusPinjaman"));
		
		db.persist(simpan);
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//end
		
		return getPath() + "/maklumatBPP.vm";
	}
	
	
	@Command("findDaerah")
	public String findDaerah() throws Exception {	
		
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		List<Daerah> list = du.getListDaerah(idNegeri);
		context.put("selectDaerah", list);
		
		return getPath() + "/findDaerah.vm";
	}
	
	@Command("findMukim")
	public String findMukim() throws Exception {
		
		String idDaerah = "0";
		if (get("findDaerah").trim().length() > 0)
			idDaerah = get("findDaerah");
	
		List<Mukim> list = du.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/findMukim.vm";
	}

}

package bph.modules.rpp.tetapan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppTetapanBukaTempahan;
import bph.entities.rpp.RppTetapanTarikhTempahanPeranginan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class RppBukaTempahanPeranginanRecordModule extends LebahRecordTemplateModule<RppPeranginan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppPeranginan r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		
		context.put("selectJenisPeranginan", dataUtil.getListJenisPeranginan());
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		defaultButtonOption();
		addfilter();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	private void addfilter() {
		this.setReadonly(true);
	}
	
	private void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/tetapan/bukaTempahanPeranginan";
	}

	@Override
	public Class<RppPeranginan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppPeranginan.class;
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("namaPeranginan", get("find_nama"));
		map.put("jenisPeranginan.id", new OperatorEqualTo(get("findJenisPeranginan")));
		map.put("bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		map.put("bandar.id", new OperatorEqualTo(get("findBandar")));
		
		return map;
	}
	
	@Override
	public boolean delete(RppPeranginan r) throws Exception {
		boolean val=false;
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus"))
		{	
			val = true;		
		}
		return val;
	}

	@Override
	public void getRelatedData(RppPeranginan r) {
		
		List<RppTetapanBukaTempahan> listBukaTempahan = getListBukaTempahan(r);
		context.put("listBukaTempahan", listBukaTempahan);

		List<RppTetapanTarikhTempahanPeranginan> listTarikhTempahanPeranginan = getListTarikhTempahanPeranginan(r);
		context.put("listTarikhTempahanPeranginan", listTarikhTempahanPeranginan);
	}

	@Command("getMaklumatTempahanDibuka")
	public String getMaklumatTempahanDibuka() throws Exception {
		
		RppPeranginan r = db.find(RppPeranginan.class, getParam("idPeranginan"));
		
		List<RppTetapanBukaTempahan> listBukaTempahan = getListBukaTempahan(r);
		context.put("listBukaTempahan", listBukaTempahan);
		
		List<RppTetapanTarikhTempahanPeranginan> listTarikhTempahanPeranginan = getListTarikhTempahanPeranginan(r);
		context.put("listTarikhTempahanPeranginan", listTarikhTempahanPeranginan);
		
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<RppTetapanBukaTempahan> getListBukaTempahan(RppPeranginan r){
		List<RppTetapanBukaTempahan> listBukaTempahan = 
				db.list("select x from RppTetapanBukaTempahan x "+
					    "where x.id not in (select y.tetapanBukaTempahan.id from RppTetapanTarikhTempahanPeranginan y where y.peranginan.id = '"+r.getId()+"' ) "+
						"order by x.tarikhBukaDari desc ");

		return listBukaTempahan;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppTetapanTarikhTempahanPeranginan> getListTarikhTempahanPeranginan(RppPeranginan r){
		List<RppTetapanTarikhTempahanPeranginan> 
			listTarikhTempahanPeranginan = db.list("select x from RppTetapanTarikhTempahanPeranginan x where x.peranginan.id = '"+r.getId()+"' order by x.tetapanBukaTempahan.tarikhBukaDari desc ");
		return listTarikhTempahanPeranginan;
	}
	
	@Override
	public void save(RppPeranginan r) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("savePilihanTarikhTempahan")
	public String savePilihanTarikhTempahan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		RppPeranginan r = db.find(RppPeranginan.class, getParam("idPeranginan"));
		RppTetapanBukaTempahan ts = db.find(RppTetapanBukaTempahan.class, getParam("idPilihan"));
		RppTetapanTarikhTempahanPeranginan sp = new RppTetapanTarikhTempahanPeranginan();
		Users user = db.find(Users.class, userId);
		Integer bilRekod = getListTarikhTempahanPeranginan(r).size();
		
		db.begin();
		sp.setTetapanBukaTempahan(ts);
		sp.setIdMasuk(user);
		sp.setTarikhMasuk(new Date());
		sp.setPeranginan(r);
		if(bilRekod==0){sp.setFlagAktif("Y");}else{sp.setFlagAktif("T");};
		
		db.persist(sp);
		
		db.commit();
		
		return getMaklumatTempahanDibuka();
	}
	
	@Command("deletePilihanTarikhTempahan")
	public String deletePilihanTarikhTempahan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		RppTetapanTarikhTempahanPeranginan sp = db.find(RppTetapanTarikhTempahanPeranginan.class, getParam("idBukaTempahanLokasi"));
		
		db.begin();
		db.remove(sp);
		db.commit();
		
		return getMaklumatTempahanDibuka();
	}
	
	@Command("updateAktif")
	public String updateAktif() throws Exception{
		
		RppTetapanTarikhTempahanPeranginan sp = db.find(RppTetapanTarikhTempahanPeranginan.class, getParam("idBukaTempahanLokasi"));
		
		RppTetapanTarikhTempahanPeranginan prev = 
				(RppTetapanTarikhTempahanPeranginan) db.get("select x from RppTetapanTarikhTempahanPeranginan x where x.peranginan.id = '"+sp.getPeranginan().getId()+"' and x.flagAktif = 'Y' ");
		
		db.begin();
		prev.setFlagAktif("T");
		
		try {
			db.commit();
			
			db.begin();
			sp.setFlagAktif("Y");
			db.commit();
			
		} catch (Exception e) {
			System.out.println("error update flag aktif... "+e.getMessage());
		}
		
		return getMaklumatTempahanDibuka();
	}
	
	
	
	
}

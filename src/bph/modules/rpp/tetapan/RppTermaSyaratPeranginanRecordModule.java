package bph.modules.rpp.tetapan;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.TermaSyaratRpp;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppTermaSyaratPeranginan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class RppTermaSyaratPeranginanRecordModule extends LebahRecordTemplateModule<RppPeranginan> {

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
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if(userRole.equalsIgnoreCase("(RPP) Penyedia")){
			this.setReadonly(true);
		}else{
			this.setReadonly(false);
		}
		
		defaultButtonOption();
		addfilter();
		
		context.put("userRole", userRole);
		context.put("command", command);
		context.put("util", new Util());
		context.put("path", getPath());
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
		return "bph/modules/rpp/tetapan/termaSyaratPeranginan";
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
		
		List<TermaSyaratRpp> listTermaSyaratRpp = getListTermaSyaratRpp(r);
		context.put("listTermaSyaratRpp", listTermaSyaratRpp);
		
		List<RppTermaSyaratPeranginan> listTermaSyaratPeranginan = getListTermaSyaratPeranginan(r);
		context.put("listTermaSyaratPeranginan", listTermaSyaratPeranginan);
	}

	@Command("getMaklumatTermaSyarat")
	public String getMaklumatTermaSyarat() throws Exception {
		
		RppPeranginan r = db.find(RppPeranginan.class, getParam("idPeranginan"));
		
		List<TermaSyaratRpp> listTermaSyaratRpp = getListTermaSyaratRpp(r);
		context.put("listTermaSyaratRpp", listTermaSyaratRpp);
		
		List<RppTermaSyaratPeranginan> listTermaSyaratPeranginan = getListTermaSyaratPeranginan(r);
		context.put("listTermaSyaratPeranginan", listTermaSyaratPeranginan);
		
		return getPath() + "/entry_page.vm";
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<TermaSyaratRpp> getListTermaSyaratRpp(RppPeranginan r){
		List<TermaSyaratRpp> listTermaSyaratRpp = 
				db.list("select x from TermaSyaratRpp x "+
					    "where x.id not in (select y.termaSyaratRpp.id from RppTermaSyaratPeranginan y where y.rppPeranginan.id = '"+r.getId()+"' ) "+
						//"and x.kategoriTerma.id = '"+r.getJenisPeranginan().getId()+"' or x.kategoriTerma is null "+
						"order by x.keterangan");

		Db db1 = null;
		
		try {
			db1 = new Db();
			String sql = "select id from ruj_terma_syarat_rpp where (id_kategori_terma = '"+r.getJenisPeranginan().getId()+"' or id_kategori_terma is null)";
			ResultSet rs = db1.getStatement().executeQuery(sql);			
			while (rs.next()) {
				TermaSyaratRpp ts = db.find(TermaSyaratRpp.class, id);
				if (ts != null){
					listTermaSyaratRpp.add(ts);
				}
			}	
			
		}catch(Exception e){
			System.out.println("error getting List TermaSyaratRpp : "+e.getMessage());
		}finally { 
			if ( db1 != null ) db1.close();
		}
		
		return listTermaSyaratRpp;
	}
	
	@SuppressWarnings("unchecked")
	public List<RppTermaSyaratPeranginan> getListTermaSyaratPeranginan(RppPeranginan r){
		List<RppTermaSyaratPeranginan> listTermaSyaratPeranginan = db.list("select x from RppTermaSyaratPeranginan x where x.rppPeranginan.id = '"+r.getId()+"' ");
		return listTermaSyaratPeranginan;
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
	
	@Command("savePilihanTermaSyarat")
	public String savePilihanTermaSyarat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		RppPeranginan r = db.find(RppPeranginan.class, getParam("idPeranginan"));
		TermaSyaratRpp ts = db.find(TermaSyaratRpp.class, getParam("idTerma"));
		RppTermaSyaratPeranginan sp = new RppTermaSyaratPeranginan();
		Users user = db.find(Users.class, userId);
		
		db.begin();
		sp.setTermaSyaratRpp(ts);
		sp.setIdMasuk(user);
		sp.setTarikhMasuk(new Date());
		sp.setRppPeranginan(r);
		db.persist(sp);
		
		db.commit();
		
		return getMaklumatTermaSyarat();
	}
	
	@Command("deletePilihanTermaSyarat")
	public String deletePilihanTermaSyarat() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		RppTermaSyaratPeranginan sp = db.find(RppTermaSyaratPeranginan.class, getParam("idTermaPeranginan"));
		
		db.begin();
		db.remove(sp);
		db.commit();
		
		return getMaklumatTermaSyarat();
	}
	
	
}

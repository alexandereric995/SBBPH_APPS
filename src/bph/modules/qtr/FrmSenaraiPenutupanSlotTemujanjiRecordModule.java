package bph.modules.qtr;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import bph.entities.qtr.KuaTemujanji;
import bph.entities.qtr.KuaTemujanjiHistory;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class FrmSenaraiPenutupanSlotTemujanjiRecordModule extends LebahRecordTemplateModule<KuaTemujanji> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;	

	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaTemujanji> getPersistenceClass() {
		return KuaTemujanji.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/temujanji/penutupanSlotTemujanji";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("util", util);

		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
					
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableInfoPaparLink(true);
		this.setHideDeleteButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableUpperBackButton(true);
	}

	private void addfilter() {
		this.addFilter("tarikhTemujanji >= '"+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		this.addFilter("flagInternal = 1");
		this.addFilter("statusTemujanji = 1");
		this.setOrderBy("tarikhMulaTemujanji");
		this.setOrderType("asc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(KuaTemujanji r) throws Exception {

	}
	
	@Override
	public void afterSave(KuaTemujanji r) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void getRelatedData(KuaTemujanji r) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean delete(KuaTemujanji r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("tarikhTemujanji", new OperatorDateBetween(getDate("findTarikhTemujanjiMula"), getDate("findTarikhTemujanjiHingga")));
		r.put("keterangan", getParam("findKeterangan"));
		return r;
	}
	
	@Command("hapusRekod")
	public String hapusRekod() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("idTemujanji");
		try {
			mp = new MyPersistence();			
			KuaTemujanji temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, id);
			if (temujanji != null) {
				mp.begin();
				temujanji.setStatusTemujanji("3");
				temujanji.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				temujanji.setTarikhKemaskini(new Date());
				mp.commit();
				
				simpanHistory(temujanji, temujanji.getStatusTemujanji(), mp);
			}
		} catch (Exception ex) {
			System.out.println("ERROR hapusRekod : " + ex.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return listPage();
	}
	
	private static void simpanHistory(KuaTemujanji temujanji, String statusTemujanji, MyPersistence mp) {
		boolean addSejarah = false;
		KuaTemujanjiHistory sejarah = null;
		try {
			mp.begin();
			sejarah = (KuaTemujanjiHistory) mp.find(KuaTemujanjiHistory.class, temujanji.getId());
			if (sejarah == null) {
				sejarah = new KuaTemujanjiHistory();
				sejarah.setId(temujanji.getId());
				addSejarah = true;
			}
			sejarah.setAgihan(temujanji.getAgihan());
			sejarah.setPenghuni(temujanji.getPenghuni());
			sejarah.setKuarters(temujanji.getKuarters());
			sejarah.setPemohon(temujanji.getPemohon());
			sejarah.setTarikhMohonTemujanji(temujanji.getTarikhMohonTemujanji());
			sejarah.setTarikhTemujanji(temujanji.getTarikhTemujanji());
			sejarah.setTarikhMulaTemujanji(temujanji.getTarikhMulaTemujanji());
			sejarah.setTarikhAkhirTemujanji(temujanji.getTarikhAkhirTemujanji());
			sejarah.setSebabBertukar(temujanji.getSebabBertukar());
			sejarah.setKeterangan(temujanji.getKeterangan());
			sejarah.setAlamat1(temujanji.getAlamat1());
			sejarah.setAlamat2(temujanji.getAlamat2());
			sejarah.setAlamat3(temujanji.getAlamat3());
			sejarah.setPoskod(temujanji.getPoskod());
			sejarah.setBandar(temujanji.getBandar());			
			sejarah.setEmel(temujanji.getEmel());
			sejarah.setNoTelefonBimbit(temujanji.getNoTelefonBimbit());
			sejarah.setAlamatPejabat1(temujanji.getAlamatPejabat1());
			sejarah.setAlamatPejabat2(temujanji.getAlamatPejabat2());
			sejarah.setAlamatPejabat3(temujanji.getAlamatPejabat3());
			sejarah.setPoskodPejabat(temujanji.getPoskodPejabat());
			sejarah.setBandarPejabat(temujanji.getBandarPejabat());		
			sejarah.setNoTelefonPejabat(temujanji.getNoTelefonPejabat());
			sejarah.setFlagInternal(temujanji.getFlagInternal());
			sejarah.setCatatan(temujanji.getCatatan());
			sejarah.setPetugas(temujanji.getPetugas());
			sejarah.setKehadiran(temujanji.getKehadiran());
			sejarah.setTarikhTerimaKunci(temujanji.getTarikhTerimaKunci());
			sejarah.setTarikhKeluarKuarters(temujanji.getTarikhKeluarKuarters());
			sejarah.setTarikhSerahKunci(temujanji.getTarikhSerahKunci());
			sejarah.setMemoDaripada(temujanji.getMemoDaripada());
			sejarah.setMemoKepada(temujanji.getMemoKepada());
			sejarah.setMemoPerkara(temujanji.getMemoPerkara());
			sejarah.setMemoRujukan(temujanji.getMemoRujukan());
			sejarah.setMemoSk(temujanji.getMemoSk());
			sejarah.setMemoTarikh(temujanji.getMemoTarikh());			
			sejarah.setStatusTemujanji(statusTemujanji);
			sejarah.setDaftarOleh(temujanji.getDaftarOleh());
			sejarah.setTarikhMasuk(temujanji.getTarikhMasuk());
			sejarah.setKemaskiniOleh(temujanji.getKemaskiniOleh());
			sejarah.setTarikhKemaskini(temujanji.getTarikhKemaskini());
			if (addSejarah) {
				mp.persist(sejarah);
			}
			mp.remove(temujanji);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error simpanHistory : " + e.getMessage());
		} 
	}
	
	@Command("simpanRekod")
	public String simpanRekod() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		boolean success = false;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		String tarikhMula = getParam("tarikhMula");
		String tarikhTamat = getParam("tarikhTamat");
		String masaMula = getParam("masaMula");
		String masaTamat = getParam("masaTamat");
		String keterangan = getParam("keterangan");
		String[] masa = { "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM" };
		List<String> getFinalDateTime = Util.getFinalDateTime(tarikhMula, tarikhTamat, masaMula, masaTamat, masa);

		Db lebahDb = null;
		try {
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();	
			
			mp = new MyPersistence();
			mp.begin();
			if (getFinalDateTime.size() > 0) {				
				for (int i = 0; i < getFinalDateTime.size(); i++) {
					String sql = "SELECT id FROM kua_temujanji WHERE flag_internal = 1 AND status_temujanji = 1 AND DATE_FORMAT(tarikh_mula_temujanji, '%d-%m-%Y %I:%i %p') = '" + getFinalDateTime.get(i) + "'";
					ResultSet rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String id = rs.getString("id");
						KuaTemujanji temujanji = (KuaTemujanji) mp.find(KuaTemujanji.class, id);
						if (temujanji != null) {
							temujanji.setStatusTemujanji("3");
							temujanji.setKemaskiniOleh((Users) mp.find(Users.class, userId));
							temujanji.setTarikhKemaskini(new Date());
						}
					}
				}
				
				for (int i = 0; i < getFinalDateTime.size(); i++) {
					Date date = formatter.parse(getFinalDateTime.get(i));
					KuaTemujanji newTemujanji = new KuaTemujanji();
					newTemujanji.setTarikhTemujanji(date);
					newTemujanji.setTarikhMulaTemujanji(date);
					newTemujanji.setTarikhAkhirTemujanji(date);				
					newTemujanji.setKeterangan(keterangan);
					newTemujanji.setFlagInternal(1);
					newTemujanji.setMemoDaripada(null);
					newTemujanji.setMemoKepada(null);
					newTemujanji.setMemoPerkara(null);
					newTemujanji.setMemoRujukan(null);
					newTemujanji.setMemoSk(null);				
					newTemujanji.setDaftarOleh(db.find(Users.class, userId));				
					mp.persist(newTemujanji);			
				}
				mp.commit();
				success = true;				
			}			
		} catch (Exception e) {
			System.out.println("Error simpanRekod : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
			if (lebahDb != null) { lebahDb.close(); }
		}
		context.put("success", success);
		return getPath() + "/result.vm";
	}
}

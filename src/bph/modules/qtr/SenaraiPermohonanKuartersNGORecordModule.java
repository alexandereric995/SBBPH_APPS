package bph.modules.qtr;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaPermohonanNGO;
import bph.entities.qtr.KuaSequencePermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanKuartersNGORecordModule extends LebahRecordTemplateModule<KuaPermohonanNGO> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;	
	
	@Override
	public Class getIdType() {
		return String.class;
	}	
	
	@Override
	public Class<KuaPermohonanNGO> getPersistenceClass() {
		return KuaPermohonanNGO.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/qtr/permohonanKuartersNGO";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		context.put("util", util);
		
		context.put("selectNegeriPemohon", dataUtil.getListNegeri());
		context.put("selectNegeriAgensi", dataUtil.getListNegeri());
		context.put("selectStatusPermohonan", dataUtil.getListStatus("21"));
		
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
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	private void addfilter() {
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void save(KuaPermohonanNGO r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		r.setJenisPermohonan("1"); //BAHARU
		r.setTarikhPermohonan(getDate("tarikhPermohonan"));
		r.setNoRujukanPermohonan(getParam("noRujukanPermohonan"));
		r.setTarikhTerimaPermohonan(getDate("tarikhTerimaPermohonan"));
		r.setCatatanPermohonan(getParam("catatan"));
		
		r.setIdPemohon(getParam("noPengenalan"));
		r.setNamaPemohon(getParam("namaPemohon"));
		r.setJawatanPemohon(getParam("jawatanPemohon"));
		r.setAlamat1Pemohon(getParam("alamat1Pemohon"));
		r.setAlamat2Pemohon(getParam("alamat2Pemohon"));
		r.setAlamat3Pemohon(getParam("alamat3Pemohon"));
		r.setPoskodPemohon(getParam("poskodPemohon"));
		r.setBandarPemohon((Bandar) db.find(Bandar.class, getParam("idBandarPemohon")));
		r.setNoTelefonBimbit(getParam("noTelefonBimbit"));
		r.setEmel(getParam("emel"));
		
		r.setNamaAgensi(getParam("namaAgensi"));
		r.setAlamat1Agensi(getParam("alamat1Agensi"));
		r.setAlamat2Agensi(getParam("alamat2Agensi"));
		r.setAlamat3Agensi(getParam("alamat3Agensi"));
		r.setPoskodAgensi(getParam("poskodAgensi"));
		r.setBandarAgensi((Bandar) db.find(Bandar.class, getParam("idBandarAgensi")));
		r.setNoTelefon(getParam("noTelefon"));		
		r.setNoFaks(getParam("noFaks"));		
		
		r.setStatus(db.find(Status.class, "57376685728661"));
		r.setDaftarOleh(db.find(Users.class, userId));
	}

	@Override
	public void afterSave(KuaPermohonanNGO r) {
		List<Bandar> listBandar = null;
		try {
			db = new DbPersistence();
			db.begin();
	
			if (r != null) {
				r.setNoFail(generateNoPermohonan(db));
			}			
			db.commit();
			
			if (r != null) {
				if (r.getBandarPemohon() != null) {
					if (r.getBandarPemohon().getNegeri() != null) {
						listBandar = dataUtil.getListBandar(r.getBandarPemohon().getNegeri().getId());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		context.put("selectBandarPemohon", listBandar);
		context.put("allowUpdatePermohonan", "Y");
		context.put("r", r);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "1");	
	}
	
	@Override
	public void getRelatedData(KuaPermohonanNGO r) {
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();			
			context.put("allowUpdatePermohonan", getFlagAllowUpdatePermohonan(r, mp));
			
			if (r != null) {
				if (r.getBandarPemohon() != null) {
					if (r.getBandarPemohon().getNegeri() != null) {
						listBandar = dataUtil.getListBandar(r.getBandarPemohon().getNegeri().getId());
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectBandarPemohon", listBandar);
		context.put("r", r);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "1");	
	}
	
	@Override
	public boolean delete(KuaPermohonanNGO r) throws Exception {
		boolean allowDelete = false;
		try {
			if ("57376685728661".equals(r.getStatus().getId())) {
				allowDelete = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allowDelete;
	}	

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		
		m.put("noFail", get("findNoFail"));
		m.put("idPemohon", get("findNoPengenalan"));
		m.put("namaPemohon", get("findNamaPemohon"));
		m.put("namaAgensi", get("findNamaAgensi"));
		m.put("jenisPermohonan", new OperatorEqualTo(get("findJenisPermohonan")));
		m.put("status.id", new OperatorEqualTo(get("findStatusPermohonan")));
				
		return m;
	}
	
	private String getFlagAllowUpdatePermohonan(KuaPermohonanNGO permohonan, MyPersistence mp) {
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String allowUpdate = "T";
		if (permohonan != null) {
			if (permohonan.getStatus() != null) {
				if ("57376685728661".equals(permohonan.getStatus().getId())) { //BARU
					allowUpdate = "Y";
				}
			}
		}		
		return allowUpdate;
	}
	
	/** START TAB **/
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		KuaPermohonanNGO permohonan = null;
		try {
			mp = new MyPersistence();
			permohonan = (KuaPermohonanNGO) mp.find(KuaPermohonanNGO.class, idPermohonan);			
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPemohon")
	public String getMaklumatPemohon(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		KuaPermohonanNGO permohonan = null;
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();
			permohonan = (KuaPermohonanNGO) mp.find(KuaPermohonanNGO.class, idPermohonan);			
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
				
				if (permohonan.getBandarPemohon() != null) {
					if (permohonan.getBandarPemohon().getNegeri() != null) {
						listBandar = dataUtil.getListBandar(permohonan.getBandarPemohon().getNegeri().getId());
					}
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectBandarPemohon", listBandar);
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	@Command("getMaklumatPemohonBerdaftar")
	public String getMaklumatPemohonBerdaftar() throws Exception{		
		String noPengenalan = get("noPengenalan").replace("-", "").trim();	
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();
			
			KuaPermohonanNGO pemohon = (KuaPermohonanNGO) mp.get("select x from KuaPermohonanNGO x where x.idPemohon = '" + noPengenalan + "'");
			context.put("pemohon", pemohon);
			
			if (pemohon != null) {
				if (pemohon.getBandarPemohon() != null) {
					if (pemohon.getBandarPemohon().getNegeri() != null) {
						listBandar = dataUtil.getListBandar(pemohon.getBandarPemohon().getNegeri().getId());
					}
				}
			}
			context.put("selectBandarPemohon", listBandar);
			context.put("noPengenalan", noPengenalan);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatPemohon/maklumatPemohon.vm";
	}
	
	public synchronized String generateNoPermohonan(DbPersistence db) {
		Calendar calCurrent = new GregorianCalendar();
		calCurrent.setTime(new Date());
		int bulan = calCurrent.get(Calendar.MONTH) + 1;
		int tahun = calCurrent.get(Calendar.YEAR);
		int counter = 0;

		String month = new DecimalFormat("00").format(bulan);
		String year = new DecimalFormat("0000").format(tahun);
		String key = "NGO-" + year + month;

		KuaSequencePermohonan seq = (KuaSequencePermohonan) db
				.get("select x from KuaSequencePermohonan x where x.id = '" + key + "'");

		if (seq != null) {
			db.pesismisticLock(seq);
			counter = seq.getBilangan() + 1;
			seq.setBilangan(counter);
			seq = (KuaSequencePermohonan) db.merge(seq);
		} else {
			counter = 1;
			seq = new KuaSequencePermohonan();
			seq.setId(key);
			seq.setBilangan(counter);
			db.persist(seq);
			db.flush();
		}

		String formatserial = new DecimalFormat("0000").format(counter);
		String noPermohonan = key + formatserial;
		return noPermohonan;
	}
	
	/** START DROPDOWN **/
	@Command("selectBandarPemohon")
	public String selectBandarPemohon() throws Exception {

		String idNegeriPemohon = "0";
		if (get("idNegeriPemohon").trim().length() > 0) {
			idNegeriPemohon = get("idNegeriPemohon");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeriPemohon);
		context.put("selectBandarPemohon", list);

		return getPath() + "/maklumatPemohon/selectBandar.vm";
	}
	
	@Command("selectBandarAgensi")
	public String selectBandarAgensi() throws Exception {

		String idNegeriAgensi = "0";
		if (get("idNegeriAgensi").trim().length() > 0) {
			idNegeriAgensi = get("idNegeriAgensi");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeriAgensi);
		context.put("selectBandarAgensi", list);

		return getPath() + "/maklumatAgensi/selectBandar.vm";
	}
	/** END DROPDOWN **/
}

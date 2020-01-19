package bph.modules.rk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kod.Status;
import bph.entities.rk.RkDokumen;
import bph.entities.rk.RkMesyuarat;
import bph.entities.rk.RkMesyuaratPermohonan;
import bph.entities.rk.RkPermohonan;
import bph.entities.rk.RkSST;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiMesyuaratRecordModule extends LebahRecordTemplateModule<RkMesyuarat> {

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
	public Class<RkMesyuarat> getPersistenceClass() {
		return RkMesyuarat.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/rk/senaraiMesyuarat";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		context.put("util", util);		

		context.remove("flagSelesaiMesyuarat");
		context.remove("errMsg");
		
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
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}

	private void addfilter() {
		this.setOrderBy("tarikh");
		this.setOrderType("desc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void save(RkMesyuarat r) throws Exception {
		r.setTajuk(getParam("tajuk"));
		r.setBil(getParam("bil"));
		r.setTarikh(getDate("tarikh"));
		r.setLokasi(getParam("lokasi"));
		r.setCatatan(getParam("catatan"));
		
		r.setDaftarOleh(db.find(Users.class, userId));
	}

	@Override
	public void afterSave(RkMesyuarat r) {
		context.put("selectedTab", "1");	
	}

	@Override
	public void getRelatedData(RkMesyuarat r) {
		context.put("selectedTab", "1");	
	}

	@Override
	public boolean delete(RkMesyuarat r) throws Exception {
		boolean allowDelete = false;
		if (r.getStatus() != null) {
			if ("B".equals(r.getStatus())) {				
				try {
					mp = new MyPersistence();
					if (r != null) {
						List<RkMesyuaratPermohonan> listMesyuaratPermohonan = mp.list("select x from RkMesyuaratPermohonan x where x.mesyuarat.id = '" + r.getId() + "'");
						if (listMesyuaratPermohonan.size() > 0) {
							mp.begin();
							for (RkMesyuaratPermohonan mesyuaratPermohonan : listMesyuaratPermohonan) {							
								mp.remove(mesyuaratPermohonan);
							}
							mp.commit();
						}						
					}
					allowDelete = true;
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (mp != null) { mp.close(); }
				}				
			}
		}		
		return allowDelete;
	}	

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("tajuk", getParam("findTajuk").trim());
		m.put("bil", getParam("findBil").trim());
		m.put("tarikh", getDate("findTarikh"));
		return m;
	}
	
	/** START TAB **/
	@Command("getMaklumatMesyuarat")
	public String getMaklumatMesyuarat(){
		String idMesyuarat = getParam("idMesyuarat");
		try {
			mp = new MyPersistence();
			RkMesyuarat mesyuarat = (RkMesyuarat) mp.find(RkMesyuarat.class, idMesyuarat);
			context.put("r", mesyuarat);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");	
		return getPath() + "/senaraiTab.vm";
	}
	
	@Command("getSenaraiPermohonan")
	public String getSenaraiPermohonan(){
		String idMesyuarat = getParam("idMesyuarat");
		RkMesyuarat mesyuarat = null;
		List<RkMesyuaratPermohonan> listMesyuaratPermohonanBaru = null;
		List<RkMesyuaratPermohonan> listMesyuaratPermohonanTangguh = null;
		try {
			mp = new MyPersistence();
			mesyuarat = (RkMesyuarat) mp.find(RkMesyuarat.class, idMesyuarat);			
			if (mesyuarat != null) {
				listMesyuaratPermohonanBaru = mp.list("select x from RkMesyuaratPermohonan x where x.flagJenisPermohonan = 'B' and x.mesyuarat.id = '" + mesyuarat.getId() + "'");
				listMesyuaratPermohonanTangguh = mp.list("select x from RkMesyuaratPermohonan x where x.flagJenisPermohonan = 'TG' and x.mesyuarat.id = '" + mesyuarat.getId() + "'");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", mesyuarat);
		context.put("listMesyuaratPermohonanBaru", listMesyuaratPermohonanBaru);
		context.put("listMesyuaratPermohonanTangguh", listMesyuaratPermohonanTangguh);
		context.put("selectedTab", "2");	
		return getPath() + "/senaraiTab.vm";
	}
	/** END TAB **/
	
	@Command("doKemaskiniMaklumatMesyuarat")
	public String doKemaskiniMaklumatMesyuarat(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuarat = getParam("idMesyuarat");
		try {
			mp = new MyPersistence();
			RkMesyuarat mesyuarat = (RkMesyuarat) mp.find(RkMesyuarat.class, idMesyuarat);
			if (mesyuarat != null) {
				mp.begin();
				mesyuarat.setTajuk(getParam("tajuk"));
				mesyuarat.setBil(getParam("bil"));
				mesyuarat.setTarikh(getDate("tarikh"));
				mesyuarat.setLokasi(getParam("lokasi"));
				mesyuarat.setCatatan(getParam("catatan"));
				
				mesyuarat.setDaftarOleh((Users) mp.find(Users.class, userId));
				mesyuarat.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatMesyuarat();
	}
	
	@Command("getSenaraiDokumenSokonganPermohonan")
	public String getSenaraiDokumenSokonganPermohonan(){
		String idMesyuaratPermohonan = getParam("idMesyuaratPermohonan");
		String idMesyuarat = "";
		List<RkDokumen> listDokumenSokongan = null;
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				listDokumenSokongan = mp.list("select x from RkDokumen x where x.permohonan.id = '" + mesyuaratPermohonan.getPermohonan().getId() + "' order by x.id asc");
				idMesyuarat = mesyuaratPermohonan.getMesyuarat().getId();
			}			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("idMesyuarat", idMesyuarat);
		context.put("listDokumenSokongan", listDokumenSokongan);
		return getPath() + "/senaraiPermohonan/popupSenaraiDokumenSokonganPermohonan.vm";
	}
	
	@Command("getSenaraiPermohonanBaru")
	public String getSenaraiPermohonanBaru(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuarat = getParam("idMesyuarat");
		List<RkPermohonan> listPermohonan = null;
		try {
			mp = new MyPersistence();
			listPermohonan = mp.list("select x from RkPermohonan x where x.status.id = '22803475845603'"
					+ " and x.id not in (select y.permohonan.id from RkMesyuaratPermohonan y where y.mesyuarat.status = 'B')");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("idMesyuarat", idMesyuarat);
		context.put("listPermohonan", listPermohonan);
		return getPath() + "/senaraiPermohonan/popupSenaraiPermohonan.vm";
	}
	
	@Command("getSenaraiPermohonanTangguh")
	public String getSenaraiPermohonanTangguh(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuarat = getParam("idMesyuarat");
		List<RkPermohonan> listPermohonan = null;
		try {
			mp = new MyPersistence();
			listPermohonan = mp.list("select x from RkPermohonan x where x.status.id = '22803475845618'"
					+ " and x.id not in (select y.permohonan.id from RkMesyuaratPermohonan y where y.mesyuarat.status = 'B')");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("idMesyuarat", idMesyuarat);
		context.put("listPermohonan", listPermohonan);
		return getPath() + "/senaraiPermohonan/popupSenaraiPermohonan.vm";
	}
	
	@Command("doSavePilihanPermohonan")
	public String doSavePilihanPermohonan(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuarat = getParam("idMesyuarat");
		try {
			mp = new MyPersistence();
			RkMesyuarat mesyuarat = (RkMesyuarat) mp.find(RkMesyuarat.class, idMesyuarat);
			if (mesyuarat != null) {
				String[] cbPilihan = request.getParameterValues("checkPermohonan");
				for(int i = 0; i < cbPilihan.length; i++){
					String idPermohonan = cbPilihan[i];
					RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
					if (permohonan != null) {
						mp.begin();
						RkMesyuaratPermohonan mesyuaratPermohonan = new RkMesyuaratPermohonan();
						mesyuaratPermohonan.setMesyuarat(mesyuarat);
						mesyuaratPermohonan.setPermohonan(permohonan);
						if (permohonan.getStatus().getId().equals("22803475845603")) {
							mesyuaratPermohonan.setFlagJenisPermohonan("B");
						}
						if (permohonan.getStatus().getId().equals("22803475845618")) {
							mesyuaratPermohonan.setFlagJenisPermohonan("TG");
						}
						mesyuaratPermohonan.setDaftarOleh((Users) mp.find(Users.class, userId));
						mp.persist(mesyuaratPermohonan);
						mp.commit();
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getSenaraiPermohonan();
	}
	
	@Command("doHapusMesyuaratPermohonan")
	public String doHapusMesyuaratPermohonan(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuaratPermohonan = getParam("idMesyuaratPermohonan");
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				mp.begin();
				mp.remove(mesyuaratPermohonan);
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getSenaraiPermohonan();
	}
	
	@Command("doSaveKeputusanBaru")
	public String doSaveKeputusanBaru(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuaratPermohonan = getParam("idMesyuaratPermohonan");
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				mp.begin();
				mesyuaratPermohonan.setFlagKeputusan(getParam("idKeputusan" + mesyuaratPermohonan.getId()));
				mesyuaratPermohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				mesyuaratPermohonan.setTarikhKemaskini(new Date());
				mp.commit();
				context.put("cBaru", mesyuaratPermohonan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getPath() + "/senaraiPermohonan/selectKeputusanBaru.vm";
	}
	
	@Command("doSaveCatatanKeputusanBaru")
	public String doSaveCatatanKeputusanBaru(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuaratPermohonan = getParam("idMesyuaratPermohonan");
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				mp.begin();
				mesyuaratPermohonan.setCatatan(getParam("catatanKeputusan" + mesyuaratPermohonan.getId()));
				mesyuaratPermohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				mesyuaratPermohonan.setTarikhKemaskini(new Date());
				mp.commit();
				context.put("cBaru", mesyuaratPermohonan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getPath() + "/senaraiPermohonan/catatanKeputusanBaru.vm";
	}
	
	@Command("doSaveKeputusanTangguh")
	public String doSaveKeputusanTangguh(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuaratPermohonan = getParam("idMesyuaratPermohonan");
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				mp.begin();
				mesyuaratPermohonan.setFlagKeputusan(getParam("idKeputusan" + mesyuaratPermohonan.getId()));
				mesyuaratPermohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				mesyuaratPermohonan.setTarikhKemaskini(new Date());
				mp.commit();
				context.put("cTangguh", mesyuaratPermohonan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getPath() + "/senaraiPermohonan/selectKeputusanTangguh.vm";
	}
	
	@Command("doSaveCatatanKeputusanTangguh")
	public String doSaveCatatanKeputusanTangguh(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuaratPermohonan = getParam("idMesyuaratPermohonan");
		try {
			mp = new MyPersistence();
			RkMesyuaratPermohonan mesyuaratPermohonan = (RkMesyuaratPermohonan) mp.find(RkMesyuaratPermohonan.class, idMesyuaratPermohonan);
			if (mesyuaratPermohonan != null) {
				mp.begin();
				mesyuaratPermohonan.setCatatan(getParam("catatanKeputusan" + mesyuaratPermohonan.getId()));
				mesyuaratPermohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				mesyuaratPermohonan.setTarikhKemaskini(new Date());
				mp.commit();
				context.put("cTangguh", mesyuaratPermohonan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		return getPath() + "/senaraiPermohonan/catatanKeputusanTangguh.vm";
	}
	
	@Command("doSelesaiMesyuarat")
	public String doSelesaiMesyuarat(){
		context.remove("flagSelesaiMesyuarat");
		context.remove("errMsg");
		
		String flagSelesaiMesyuarat = "Y";
		String errMsg = "";
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idMesyuarat = getParam("idMesyuarat");
		try {
			mp = new MyPersistence();
			RkMesyuarat mesyuarat = (RkMesyuarat) mp.find(RkMesyuarat.class, idMesyuarat);
			if (mesyuarat != null) {
				List<RkMesyuaratPermohonan> listMesyuaratPermohonan = mp.list("select x from RkMesyuaratPermohonan x where x.mesyuarat.id = '" + mesyuarat.getId() + "'");
				if (listMesyuaratPermohonan.size() > 0) {
					for (RkMesyuaratPermohonan mesyuaratPermohonan : listMesyuaratPermohonan) {						
						if (mesyuaratPermohonan.getFlagKeputusan() == null || mesyuaratPermohonan.getFlagKeputusan().equals("")) {
							flagSelesaiMesyuarat = "T";
							errMsg = "MASIH TERDAPAT PERMOHONAN YANG BELUM DIDAFTARKAN KEPUTUSAN.";
						}
					}
				} else {
					flagSelesaiMesyuarat = "T";
					errMsg = "TIADA PERMOHONAN DIDAFTARKAN DIDALAM MESYUARAT INI.";
				}
				
				//MESYUARAT LENGKAP
				if (flagSelesaiMesyuarat.equals("Y")) {
					mp.begin();
					for (RkMesyuaratPermohonan mesyuaratPermohonan : listMesyuaratPermohonan) {		
						RkPermohonan permohonan = mesyuaratPermohonan.getPermohonan();
						permohonan.setMesyuarat(mesyuarat);
						permohonan.setFlagKeputusanMesyuarat(mesyuaratPermohonan.getFlagKeputusan());
						permohonan.setCatatanKeputusanMesyuarat(mesyuaratPermohonan.getCatatan());
						if (mesyuaratPermohonan.getFlagKeputusan().equals("L")) {
							permohonan.setStatus((Status) mp.find(Status.class, "1434441177868"));							
							//CREATE REKOD SST
							generateRekodSST(permohonan, mesyuaratPermohonan, mp);
						} else if (mesyuaratPermohonan.getFlagKeputusan().equals("LB")) {
							permohonan.setStatus((Status) mp.find(Status.class, "326867100304436"));
							//CREATE REKOD SST
							generateRekodSST(permohonan, mesyuaratPermohonan, mp);
						} else if (mesyuaratPermohonan.getFlagKeputusan().equals("TG")) {
							permohonan.setStatus((Status) mp.find(Status.class, "22803475845618"));
						} else if (mesyuaratPermohonan.getFlagKeputusan().equals("T")) {
							permohonan.setStatus((Status) mp.find(Status.class, "1434079231886"));
						}
						permohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
						permohonan.setTarikhKemaskini(new Date());
					}
					mesyuarat.setStatus("S");
					mesyuarat.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					mesyuarat.setTarikhKemaskini(new Date());
					mp.commit();
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagSelesaiMesyuarat", flagSelesaiMesyuarat);
		context.put("errMsg", errMsg);
		return getSenaraiPermohonan();
	}

	private void generateRekodSST(RkPermohonan permohonan, RkMesyuaratPermohonan mesyuaratPermohonan, MyPersistence mp) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		boolean addSST = false;
		try {
			RkSST sst = (RkSST) mp.get("select x from RkSST x where x.permohonan.id = '" + permohonan.getId() + "'");
			if (sst == null) {
				sst = new RkSST();
				addSST = true;
			}
			sst.setFail(permohonan.getFail());
			sst.setPermohonan(permohonan);
			sst.setTarikhMulaSST(mesyuaratPermohonan.getMesyuarat().getTarikh());
			sst.setTarikhTamatSST(Util.getNextWorkingDate(mesyuaratPermohonan.getMesyuarat().getTarikh(), 14));
			sst.setTarikhMula(permohonan.getTarikhMulaOperasi());
			sst.setTempoh(permohonan.getTempoh());
			sst.setTarikhTamat(permohonan.getTarikhTamatOperasi());
			sst.setKadarSewa(permohonan.getHargaTawaranSewa());
			sst.setDeposit(permohonan.getHargaTawaranSewa() * 3);
			sst.setFlagCajIWK("T");
			sst.setKadarBayaranIWK(0D);
			sst.setIdJenisSewa(permohonan.getIdJenisSewa());
			sst.setIdJenisPerjanjian(permohonan.getIdJenisPermohonan());
			
			if (addSST) {
				sst.setDaftarOleh((Users) mp.find(Users.class, userId));
				mp.persist(sst);
			} else {
				sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				sst.setTarikhKemaskini(new Date());
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
}

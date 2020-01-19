/**
	* @author muhdsyazreen
	*/

package bph.modules.kod;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.KodPusatTerima;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class KodJuruwangRecordModule extends LebahRecordTemplateModule<KodJuruwang> {
	
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(KodJuruwang r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");		
		
		List<KodPusatTerima> listPusatTerima = dataUtil.getListPusatTerima();		
		context.put("selectPusatTerima", listPusatTerima);
		
		context.remove("idPusatTerima");	
		context.remove("juruwang");
		context.remove("jawatan");	
		context.remove("kodJuruwang");	
		context.remove("flagAktif");	
		context.remove("catatan");		
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
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
		this.setOrderBy("juruwang.userName");
		this.setOrderType("asc");

	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {

	}

	@Override
	public boolean delete(KodJuruwang r) throws Exception {
		boolean bool = true;
		try {
			mp = new MyPersistence();
			List<KewBayaranResit> listResit = mp.list("select x from KewBayaranResit x where x.kodJuruwang.id = '" + r.getId() + "'");
			if (listResit.size() > 0) {
				bool = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		return bool;
	}

	@Override
	public String getPath() {
		return "bph/modules/kod/kodJuruwang";
	}

	@Override
	public Class<KodJuruwang> getPersistenceClass() {
		return KodJuruwang.class;
	}

	@Override
	public void getRelatedData(KodJuruwang r) {

	}

	@Override
	public void save(KodJuruwang r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		KodPusatTerima pusatTerima = db.find(KodPusatTerima.class, getParam("idPusatTerima"));
		
		r.setPusatTerima(pusatTerima);
		r.setKodPusatTerima(pusatTerima.getKodPusatTerima());
		r.setJuruwang(db.find(Users.class, getParam("idJuruwang")));
		r.setJawatan(getParam("jawatan"));
		r.setKodJuruwang(getParam("kodJuruwang"));
		r.setFlagAktif(getParam("flagAktif"));
		r.setCatatan(getParam("catatan"));
		
		if ("".equals(id)) {
			r.setDaftarOleh(db.find(Users.class, userId));
			r.setTarikhMasuk(new Date());
		} else {
			r.setKemaskiniOleh(db.find(Users.class, userId));
			r.setTarikhKemaskini(new Date());
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findPusatTerima = get("findPusatTerima").trim();
		String findJawatan = get("findJawatan").trim();
		String findNoPengenalan = get("findNoPengenalan").trim();
		String findNama = get("findNama").trim();		
		String findKodJuruwang = get("findKodJuruwang").trim();
		String findFlagAktif = get("findFlagAktif").trim();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pusatTerima.id", new OperatorEqualTo(findPusatTerima));
		map.put("jawatan", new OperatorEqualTo(findJawatan));
		map.put("juruwang.id", findNoPengenalan);
		map.put("juruwang.userName", findNama);
		map.put("kodJuruwang", findKodJuruwang);
		map.put("flagAktif", new OperatorEqualTo(findFlagAktif));
		return map;
	}
	
	@Command("getRegisteredUser")
	public String getRegisteredUser() throws Exception {
		String idJuruwang = getParam("idJuruwang");
		try {
			mp = new MyPersistence();
			Users juruwang = (Users) mp.find(Users.class, idJuruwang);
			context.put("juruwang", juruwang);	
			context.put("idPusatTerima", getParam("idPusatTerima"));		
			context.put("jawatan", getParam("jawatan"));
			context.put("kodJuruwang", getParam("kodJuruwang"));
			context.put("flagAktif", getParam("flagAktif"));
			context.put("catatan", getParam("catatan"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/daftarRekod.vm";
	}
	
	@Command("getJawatan")
	public String getJawatan() throws Exception {
		String idJuruwang = getParam("idJuruwang");
		try {
			mp = new MyPersistence();
			Users juruwang = (Users) mp.find(Users.class, idJuruwang);
			context.put("juruwang", juruwang);	
			context.put("idPusatTerima", getParam("idPusatTerima"));		
			context.put("jawatan", getParam("jawatan"));
			context.put("kodJuruwang", getParam("kodJuruwang"));
			context.put("flagAktif", getParam("flagAktif"));
			context.put("catatan", getParam("catatan"));	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/daftarRekod.vm";
	}
	
	@Command("validateJuruwang")
	public String validateJuruwang() throws Exception {
		String idPusatTerima = getParam("idPusatTerima");
		String idJuruwang = getParam("idJuruwang");
		String jawatan = getParam("jawatan");
		String kodJuruwang = getParam("kodJuruwang");
		String validJuruwang = "";
		String validJuruwangMsg = "";
		KodJuruwang kodJ = null;
		try {
			mp = new MyPersistence();
			
			KodPusatTerima pusatTerima = (KodPusatTerima) mp.find(KodPusatTerima.class, idPusatTerima);
			Users juruwang = (Users) mp.find(Users.class, idJuruwang);
			
			if (pusatTerima != null) {
				/** CHECKING
				 * 1) TIDAK BOLEH DIDAFTARKAN DI PUSAT TERIMA YANG SAMA
				 * 2) TIDAK BOLEH DIDAFTARKAN SEKIRANYA STATUS JURUWANG AKTIF DI MANA2 PUSAT TERIMA
				 * 3) BAGI JAWATAN JURUWANG - TIDAK BOLEH DIDAFTARKAN SEKIRANYA KOD JURUWANG TELAH WUJUD DI PUSAT TERIMA YANG DIPILIH
				 */
				kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getId() + "' and x.pusatTerima.id = '" + pusatTerima.getId() +"'");
				if (kodJ != null) {
					validJuruwang = "T";
					validJuruwangMsg = "ID TELAH DIDAFTARKAN DI PUSAT TERIMA INI.";
				} else {
					//PENYELIA
					if ("PENYELIA".equals(jawatan)) {
						kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getId() + "' and x.jawatan = 'JURUWANG' and x.flagAktif = 'Y'");
						if (kodJ != null) {
							validJuruwang = "T";
							validJuruwangMsg = "ID TELAH DIDAFTARKAN SEBAGAI JURUWANG.";
						} else {
							validJuruwang = "Y";
							validJuruwangMsg = "";
						}
					} else {
						//JURUWANG
						kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getId() + "' and x.jawatan = 'PENYELIA' and x.flagAktif = 'Y'");
						if (kodJ != null) {
							validJuruwang = "T";
							validJuruwangMsg = "ID TELAH DIDAFTARKAN SEBAGAI PENYELIA.";
						} else {
							kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getId() + "' and x.jawatan = 'JURUWANG' and x.flagAktif = 'Y'");
							if (kodJ != null) {
								validJuruwang = "T";
								validJuruwangMsg = "ID TELAH DIDAFTARKAN SEBAGAI JURUWANG.";
							} else {
								kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.pusatTerima.id = '" + pusatTerima.getId() + "' and x.kodJuruwang = '" + kodJuruwang + "'");
								if (kodJ != null) {
									validJuruwang = "T";
									validJuruwangMsg = "KOD JURUWANG TELAH WUJUD.";
								} else {
									validJuruwang = "Y";
									validJuruwangMsg = "";
								}
							}
						}
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("validJuruwang", validJuruwang);
		context.put("validJuruwangMsg", validJuruwangMsg);
		return getPath() + "/validateJuruwang.vm";
	}
	
	@Command("validateStatusJuruwang")
	public String validateStatusJuruwang() throws Exception {
		String id = getParam("id");
		String validJuruwang = "";
		String validJuruwangMsg = "";
		KodJuruwang kodJ = null;
		try {
			mp = new MyPersistence();
			
			KodJuruwang juruwang = (KodJuruwang) mp.find(KodJuruwang.class, id);
			
			if (juruwang != null) {
				/** CHECKING
				 * 1) TIDAK BOLEH AKTIF SEBAGAI JURUWANG DI MANA2 PUSAT TERIMA
				 * 2) BAGI JURUWANG - TIDAK BOLEH AKTIF SEBAGAI PENYELIA DI MANA2 PUSAT TERIMA
				 */
				kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getJuruwang().getId() + "' and x.jawatan = 'JURUWANG' and x.flagAktif = 'Y' and x.id not in ('" + juruwang.getId() + "')");
				if (kodJ != null) {
					validJuruwang = "T";
					validJuruwangMsg = "ID TELAH AKTIF SEBAGAI JURUWANG.";
				} else {
					if ("JURUWANG".equals(juruwang.getJawatan())) {
						kodJ = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + juruwang.getJuruwang().getId() + "' and x.jawatan = 'PENYELIA' and x.flagAktif = 'Y' and x.id not in ('" + juruwang.getId() + "')");
						if (kodJ != null) {
							validJuruwang = "T";
							validJuruwangMsg = "ID TELAH AKTIF SEBAGAI PENYELIA.";
						} else {
							validJuruwang = "Y";
							validJuruwangMsg = "";
						}
					} else {
						validJuruwang = "Y";
						validJuruwangMsg = "";
					}
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("validJuruwang", validJuruwang);
		context.put("validJuruwangMsg", validJuruwangMsg);
		return getPath() + "/validateStatusJuruwang.vm";
	}
	
	@Command("kemaskiniJuruwang")
	public String kemaskiniJuruwang() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		KodJuruwang juruwang = null;
		try {
			mp = new MyPersistence();
			
			juruwang = (KodJuruwang) mp.find(KodJuruwang.class, id);			
			if (juruwang != null) {
				mp.begin();
				juruwang.setFlagAktif(getParam("flagAktif"));
				juruwang.setCatatan(getParam("catatan"));
				juruwang.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				juruwang.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("r", juruwang);
		context.remove("validJuruwang");
		return getPath() + "/paparRekod.vm";
	}
}

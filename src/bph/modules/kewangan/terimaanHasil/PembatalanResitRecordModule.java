/**
 * @author muhdsyazreen
 */

package bph.modules.kewangan.terimaanHasil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.qtr.KuaAkaun;
import bph.entities.rk.RkAkaun;
import bph.entities.rk.RkFail;
import bph.entities.rpp.RppAkaun;
import bph.modules.rk.UtilRk;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class PembatalanResitRecordModule extends LebahRecordTemplateModule<KewBayaranResit> {

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public String getPath() {		
		return "bph/modules/kewangan/pembatalanResit";
	}

	@Override
	public Class<KewBayaranResit> getPersistenceClass() {
		return KewBayaranResit.class;
	}

	@Override
	public void afterSave(KewBayaranResit arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean delete(KewBayaranResit arg0) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noResit", getParam("findNoResit"));
		map.put("pembayar.userName", getParam("findNamaPembayar"));
		map.put("pembayar.noKP", getParam("findNoKp"));
		return map;
	}

	@Override
	public void begin() {
		context.remove("showButtonBatal");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");	

		addfilter();
		defaultButtonOption();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}

	private void addfilter() {
		this.addFilter("noResit is not null and COALESCE(x.flagVoid,'T') = 'T' ");
		this.setOrderBy("tarikhBayaran");
		this.setOrderType("desc");		
	}

	public void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}

	@Override
	public void getRelatedData(KewBayaranResit r) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		String sysdate = Util.getDateTime(new Date(), "dd/MM/yyyy");
		String tarikhBayaran = Util.getDateTime(r.getTarikhBayaran(), "dd/MM/yyyy");
		String showButtonBatal = "";
		if (sysdate.equalsIgnoreCase(tarikhBayaran)) {
			showButtonBatal = "true";
		} else {
			showButtonBatal = "false";
		}
		/** HANYA ADMIN BOLEH BATAL RESIT **/
		if (userRole.equals("(ICT) Pentadbir Sistem")) {
			showButtonBatal = "true";
		}
		context.put("showButtonBatal", showButtonBatal);
	}

	@Override
	public void save(KewBayaranResit r) throws Exception {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Command("simpanBatalResit")
	public String simpanBatalResit() {

		String statusInfo = "";
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		KewBayaranResit resit = null;
		//ADD BY PEJE - UNTUK DAPATKAN SENARAI FAIL RK YANG TERLIBAT DENGAN BAYARAN
		List<RkFail> listFailRuangKomersil = new ArrayList<>();
		
		try {
			mp = new MyPersistence();
			mp.begin();
			Users userLogin = (Users) mp.find(Users.class, userId);
			resit = (KewBayaranResit) mp.find(KewBayaranResit.class, getParam("id"));
			if (resit != null) {
				resit.setFlagVoid("Y");
				resit.setAmaunVoid(resit.getJumlahAmaunBayaran());
				resit.setCatatanBatalResit(getParam("catatanBatalResit"));
				if (userRole.equals("(ICT) Pentadbir Sistem")) {
					resit.setTarikhBatalResit(resit.getTarikhBayaran());
				} else {
					resit.setTarikhBatalResit(new Date());
				}
				resit.setTarikhKemaskini(new Date());
				resit.setUserKemaskini(userLogin);
				
				// update invois to belum bayar
				List<KewResitSenaraiInvois> listRI = mp.list("select x from KewResitSenaraiInvois x where x.resit.id = '" + resit.getId() + "' ");
				for (int i = 0; i < listRI.size(); i++) {
					// ubah by Asna
					if (listRI.get(i).getInvois() != null) {
						KewInvois inv = listRI.get(i).getInvois();					

						// update lejar to blom bayar
						String idModul = inv.getJenisBayaran() != null ? inv.getJenisBayaran().getId() : "";
						// find and update by modul
						if (idModul.equalsIgnoreCase("01")) { // KUARTERS							
							inv.setFlagBayar("T");
							inv.setFlagQueue("T");
							inv.setKredit(0d);
							
							KuaAkaun kuak = (KuaAkaun) mp.find(KuaAkaun.class, inv.getIdLejar());
							kuak.setFlagBayar("T");
							kuak.setIdKemaskini(userLogin);
							kuak.setKredit(0d);
							kuak.setNoResit(null);
							kuak.setTarikhKemaskini(new Date());
							kuak.setTarikhResit(null);
							kuak.setTarikhTransaksi(null);
						} else if (idModul.equalsIgnoreCase("02")) { // IR
							inv.setFlagBayar("T");
							inv.setFlagQueue("T");
							inv.setKredit(0d);
							
							RppAkaun rppak = (RppAkaun) mp.find(RppAkaun.class, inv.getIdLejar());
							rppak.setFlagBayar("T");
							rppak.setIdKemaskini(userLogin);
							rppak.setKredit(0d);
							rppak.setNoResit(null);
							rppak.setTarikhKemaskini(null);
							rppak.setTarikhResit(null);
							rppak.setTarikhTransaksi(null);
						} else if (idModul.equalsIgnoreCase("08")) { // BAYARAN LAIN
							//BATALKAN INVOIS DAN USER KENE CREATE BARU
							inv.setFlagBayar("BTL");
							inv.setFlagQueue("T");
							
						} else if (idModul.equalsIgnoreCase("04")) { // RK
							//BATALKAN KEW_INVOIS
							inv.setFlagBayar("BTL");
							inv.setFlagQueue("T");
							
							//HAPUS BAYARAN DI LEJAR RK
							RkAkaun akaun = (RkAkaun) mp.get("select x from RkAkaun x where x.fail.id = '" + inv.getIdLejar() + "' and x.resit.id = '" + resit.getId() + "'");
							if (akaun != null) {
								akaun.setFlagAktif("T");
								akaun.setKemaskiniOleh((Users) mp.find(Users.class, userId));
								akaun.setTarikhKemaskini(new Date());
							}
							
							RkFail fail = (RkFail) mp.find(RkFail.class, inv.getIdLejar());	
							if (fail != null) {
								if(!listFailRuangKomersil.contains(fail)) {
									listFailRuangKomersil.add(fail);
								}
							}
						} else {
							//BATALKAN KEW_INVOIS DAN USER KENE CREATE BARU
							inv.setFlagBayar("BTL");
							inv.setFlagQueue("T");
						}
						// TODO lain-lain lejar
					} else if (listRI.get(i).getDeposit() != null) {
						KewDeposit dep = listRI.get(i).getDeposit();					

						// update lejar to blom bayar
						String idModul = dep.getJenisBayaran() != null ? dep.getJenisBayaran().getId() : "";
						// find and update by modul
						if (idModul.equalsIgnoreCase("01")) { //KUARTERS
							dep.setFlagBayar("T");
							dep.setFlagQueue("T");
							
							KuaAkaun kuak = (KuaAkaun) mp.find(KuaAkaun.class, dep.getIdLejar());
							kuak.setFlagBayar("T");
							kuak.setIdKemaskini(userLogin);
							kuak.setKredit(0d);
							kuak.setNoResit(null);
							kuak.setTarikhKemaskini(new Date());
							kuak.setTarikhResit(null);
							kuak.setTarikhTransaksi(null);
						} else if (idModul.equalsIgnoreCase("02")) { // IR
							dep.setFlagBayar("T");
							dep.setFlagQueue("T");
							
							RppAkaun rppak = (RppAkaun) mp.find(RppAkaun.class, dep.getIdLejar());
							rppak.setFlagBayar("T");
							rppak.setIdKemaskini(userLogin);
							rppak.setKredit(0d);
							rppak.setNoResit(null);
							rppak.setTarikhKemaskini(null);
							rppak.setTarikhResit(null);
							rppak.setTarikhTransaksi(null);
						}  else if (idModul.equalsIgnoreCase("08")) { // BAYARAN LAIN
							//BATALKAN DEPOSIT DAN USER KENE CREATE BARU
							dep.setFlagBayar("BTL");
							dep.setFlagQueue("T");
						} else if (idModul.equalsIgnoreCase("04")) { // RK
							//BATALKAN KEW_DEPOSIT
							dep.setFlagBayar("BTL");
							dep.setFlagQueue("T");
							
							//CREATE NEW KEW_DEPOSIT
							recreateNewDeposit(dep, mp);
							
							//HAPUS BAYARAN DI LEJAR RK
							RkAkaun akaun = (RkAkaun) mp.get("select x from RkAkaun x where x.permohonan.id = '" + dep.getIdLejar() + "' and x.resit.id = '" + resit.getId() + "'");
							if (akaun != null) {
								akaun.setFlagAktif("T");
							}							
						} else {
							//BATALKAN DEPOSIT DAN USER KENE CREATE BARU
							dep.setFlagBayar("BTL");
							dep.setFlagQueue("T");
						}
					}
				}
			}
			mp.commit();
			
			for (RkFail fail : listFailRuangKomersil) {
				ServletContext servletContext = getServletContext();
				UtilRk.kemaskiniRekodPerjanjianDanAkaun(fail, false, false, mp, servletContext);
			}
			statusInfo = "success";
		} catch (Exception ex) {
			System.out.println("error simpanBatalResit : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("r", resit);
		context.put("statusInfo", statusInfo);

		return getPath() + "/entry_page.vm";
	}

	private void recreateNewDeposit(KewDeposit dep, MyPersistence mp) {
		KewDeposit newDeposit = new KewDeposit();
		newDeposit.setKodHasil(dep.getKodHasil());
		newDeposit.setJenisBayaran(dep.getJenisBayaran());
		newDeposit.setIdLejar(dep.getIdLejar());
		newDeposit.setNoInvois(dep.getNoInvois());
		newDeposit.setTarikhDari(dep.getTarikhDari());
		newDeposit.setTarikhHingga(dep.getTarikhHingga());
		newDeposit.setKeteranganDeposit(dep.getKeteranganDeposit());
		newDeposit.setPendeposit(dep.getPendeposit());
		newDeposit.setJumlahDeposit(dep.getJumlahDeposit());
		newDeposit.setTarikhDeposit(dep.getTarikhDeposit());
		newDeposit.setFlagBayar("T");
		newDeposit.setFlagQueue("T");
		mp.persist(newDeposit);		
	}
}

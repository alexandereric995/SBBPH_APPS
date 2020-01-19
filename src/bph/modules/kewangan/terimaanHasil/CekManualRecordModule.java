/**
 * @author muhdsyazreen
 */

package bph.modules.kewangan.terimaanHasil;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodJuruwang;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class CekManualRecordModule extends LebahRecordTemplateModule<KewInvois>{

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public void afterSave(KewInvois arg0) {
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void begin() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		context.put("util", new Util());
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		
		//Belum Bayar
		this.addFilter("flagBayar = 'T' ");
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public boolean delete(KewInvois arg0) throws Exception {
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kewangan/cekManual";
	}

	@Override
	public Class<KewInvois> getPersistenceClass() {
		return KewInvois.class;
	}

	@Override
	public void getRelatedData(KewInvois r) {
		KewResitSenaraiInvois rs = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+r.getId()+"' ");
		KewResitKaedahBayaran rkb = null;
		if(rs!=null){
			rkb = (KewResitKaedahBayaran)db.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+rs.getId()+"' ");
		}
		context.put("rs",rs);
		context.put("rkb",rkb);
	}

	@Override
	public void save(KewInvois r) throws Exception {
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pembayar.noKP", getParam("findNoKp"));
		map.put("pembayar.userName", get("findNamaPembayar"));
		map.put("noInvois", getParam("noInvois"));
		return map;
	}
	
	@Command("simpanCek")
	public String simpanCek() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		String statusInfo = "";
		String idInvois = getParam("id");
		
		String noCek = getParam("noCek");
		Date tarikhCek = getDate("tarikhCek");
		Date tarikhBayaran = getDate("tarikhBayaran");
		String catatanCek = getParam("catatanCek");
		Double amaunBayaran = Util.getDoubleRemoveComma(getParam("amaunBayaran"));
		
		KewResitSenaraiInvois rs = null;
		KewResitKaedahBayaran rkb = null;
		KewInvois inv = null;
		try {
			mp = new MyPersistence();
			mp.begin();
			
			KodJuruwang kodj = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
			inv = (KewInvois) mp.find(KewInvois.class, idInvois);
			Users userLogin = (Users) mp.find(Users.class, userId);
			
			rs = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+inv.getId()+"' ");
			
			KewBayaranResit resit = new KewBayaranResit();
			KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
			if(rs != null){
				resit = rs.getResit();
				rsi = rs;
			}
		
			Users pembayar = inv.getPembayar();
			if (pembayar != null) {
				resit.setPembayar(pembayar);
				resit.setNoPengenalanPembayar(pembayar.getId());
				resit.setNamaPembayar(pembayar.getUserName());				
				resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
			}
			resit.setNoResit(UtilKewangan.generateReceiptNo(mp,userLogin));
			resit.setTarikhBayaran(tarikhBayaran);
			resit.setTarikhResit(tarikhBayaran);
			resit.setMasaResit(tarikhBayaran);
			resit.setFlagJenisBayaran("MANUAL");
			if (kodj != null) {
				resit.setKodJuruwang(kodj); // BUAT ADMIN SCREEN TAMBAH USER
				resit.setKodPusatTerima(kodj.getKodPusatTerima()); // ADMIN SKRIN. SEKALIKAN DENGAN KOD JURUWANG
			}
			
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar(userLogin);
			resit.setJumlahAmaunBayaran(amaunBayaran);
			String computername=InetAddress.getLocalHost().getHostName();
			resit.setKodMesin(computername); //nama pc
			mp.persist(resit);
			
			rsi.setInvois(inv);
			rsi.setResit(resit);
			mp.persist(rsi);
			
			rkb = (KewResitKaedahBayaran)mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+resit.getId()+"' ");
			if(rkb == null){
				rkb = new KewResitKaedahBayaran();
			}
			
			rkb.setResit(resit);
			rkb.setAmaunBayaran(amaunBayaran);
			rkb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "CT"));
			rkb.setNoCek(noCek);
			rkb.setNoRujukan(noCek);
			rkb.setTarikhCek(tarikhCek);
			rkb.setCatatanCek(catatanCek);
			mp.persist(rkb);
			
			inv.setKredit(amaunBayaran);
			inv.setUserKemaskini(userLogin);
			inv.setTarikhKemaskini(new Date());
			inv.setFlagBayar("Y");
			
			UtilKewangan.updateInvoisLejarModul(mp,inv,resit,null, userId);
			
			mp.commit();
			statusInfo = "success";
		} catch (Exception ex) {
			statusInfo = "error";
			System.out.println("Error getting status : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("rs", rs);
		context.put("rkb", rkb);
		context.put("r", inv);
		context.put("statusInfo", statusInfo);
		
		return getPath() + "/entry_page.vm";
	}

	
	@Command("doCetakResit")
	public String doCetakResit() {
		
		KewInvois r = db.find(KewInvois.class, getParam("id"));
		KewBayaranResit y = db.find(KewBayaranResit.class, get("idResit"));
		Boolean cetak = false;
		
		if (y == null) {
			cetak = true;
			y = new KewBayaranResit();
		}
		
		y.setTarikhCetakResit(new Date());
		
		db.begin();
		if (cetak)
			db.persist(y);
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+r.getId()+"' ");
		context.put("rsi", rsi);
		
		return getPath() + "/entry_page.vm";
	}
	
}

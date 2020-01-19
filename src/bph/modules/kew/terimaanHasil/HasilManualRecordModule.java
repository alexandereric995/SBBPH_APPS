/**
 * @author muhdsyazreen
 */

package bph.modules.kew.terimaanHasil;

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
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class HasilManualRecordModule extends LebahRecordTemplateModule<KewInvois>{

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(KewInvois arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kewangan/hasilManual";
	}

	@Override
	public Class<KewInvois> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KewInvois.class;
	}

	@Override
	public void getRelatedData(KewInvois r) {
		KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+r.getId()+"' ");
		context.put("rsi", rsi);
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
	
	@Command("doSimpanTerimaanManual")
	public String doSimpanTerimaanManual() throws Exception {
		
		MyPersistence mp = null;
		String statusInfo = "";
		String idInvois = getParam("id");
		String idResit = getParam("idResit");
		KewResitSenaraiInvois rsi = null;
		KewBayaranResit resit = null;
		KewInvois inv = null;
		try {
			mp = new MyPersistence();
			mp.begin();
			
			inv = (KewInvois)mp.find(KewInvois.class, idInvois);
			Users us = (Users)mp.find(Users.class, inv.getPembayar().getId());
			
			rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+idInvois+"' ");
			
			resit = (KewBayaranResit)mp.find(KewBayaranResit.class, idResit);
			if(resit == null){
				resit = new KewBayaranResit();
			}
			
			Users pembayar = us;
			if (pembayar != null) {
				resit.setPembayar(pembayar);
				resit.setNoPengenalanPembayar(pembayar.getId());
				resit.setNamaPembayar(pembayar.getUserName());				
				resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(pembayar));
			}
			resit.setNoResit(get("noResit"));
			resit.setTarikhBayaran(new Date());
			resit.setTarikhResit(getDate("tarikhResit"));
			resit.setMasaResit(getDate("tarikhResit"));
			resit.setFlagJenisBayaran("MANUAL");
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar((Users)mp.find(Users.class, userId));
			resit.setJumlahAmaunBayaran(inv.getDebit());
			resit.setCatatanBayarManual(getParam("catatanBayarManual"));
			String computername=InetAddress.getLocalHost().getHostName();
			resit.setKodMesin(computername); //nama pc
			mp.persist(resit);
			
			inv.setKredit(inv.getDebit());
			inv.setUserKemaskini((Users)mp.find(Users.class, userId));
			inv.setTarikhKemaskini(new Date());
			inv.setFlagBayar("Y");
			
			
			
			if(rsi == null){
				rsi = new KewResitSenaraiInvois();
			}

			rsi.setInvois(inv);
			rsi.setResit(resit);
			mp.persist(rsi);
			
			KewResitKaedahBayaran rkb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+resit.getId()+"' ");
			
			if(rkb == null){
				rkb = new KewResitKaedahBayaran();
			}
			
			rkb.setResit(resit);
			rkb.setAmaunBayaran(inv.getDebit());
			rkb.setModBayaran((CaraBayar)mp.find(CaraBayar.class, getParam("modBayaran")));
			rkb.setNoRujukan(resit.getNoResit());
			mp.persist(rkb);
			
			/**
			 * TODO
			 * update lejar
			 * */
			
			UtilKewangan.updateInvoisLejarModul(mp, rsi.getInvois(), resit, null, userId);
			// update kewinvois
			updateInvois(rsi.getInvois(), userId);
			
			
				mp.commit();
				statusInfo = "success";
			
			
			
		} catch (Exception e) {
			statusInfo = "error";
		}
		finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", inv);
		context.put("statusInfo", statusInfo);
		context.put("rsi", rsi);
		
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
	
	private void updateInvois(KewInvois inv, String userIdLogin) {
		inv.setKredit(inv.getDebit());
		inv.setUserKemaskini(db.find(Users.class, userIdLogin));
		inv.setTarikhKemaskini(new Date());
		inv.setFlagBayar("Y");
	}

	
}

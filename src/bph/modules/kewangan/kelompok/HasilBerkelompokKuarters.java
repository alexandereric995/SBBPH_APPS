/**
 * @author muhdsyazreen
 */

package bph.modules.kewangan.kelompok;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kewangan.KelompokKuarters;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodJuruwang;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class HasilBerkelompokKuarters extends LebahRecordTemplateModule<KelompokKuarters>{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterSave(KelompokKuarters r) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		KewBayaranResit resit = new KewBayaranResit();
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			Users juruwang = (Users) mp.find(Users.class, userId);
			KodJuruwang kodj = (KodJuruwang)mp.get("select x from KodJuruwang x where x.juruwang.id = '"+juruwang.getId() + "' and x.flagAktif = 'Y'");
			
			List<KewInvois> lv = mp.list("select x from KewInvois x where x.kodHasil.id = '92401' and x.flagBayar = 'T' and x.pembayar.id "
					+ "in (select y.kuaPenghuni.pemohon.id from KelompokKuartersPenghuni y where y.kelompokKuarters.id = '"+r.getId()+"' ) ");
			
			Double totalKadarSewa = 0d;
			for(int i=0;i<lv.size();i++){
				KewInvois inv = lv.get(i);
				totalKadarSewa = totalKadarSewa + inv.getDebit();
			}
			
			//resit.setPembayar(db.find(Users.class, payerId)); RESIT UNDER SAPE ?
			resit.setNoResit(UtilKewangan.generateReceiptNo(mp,juruwang));
			resit.setTarikhBayaran(new Date());
			resit.setTarikhResit(new Date());
			resit.setMasaResit(new Date());
			resit.setFlagJenisBayaran("KAUNTER");
			resit.setKodJuruwang(kodj);
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar(juruwang);
			resit.setJumlahAmaunBayaran(totalKadarSewa);
			resit.setFlagJenisResit("2"); // INVOIS
			mp.persist(resit);
			
			for(int i=0;i<lv.size();i++){
				KewInvois inv = lv.get(i);
				KewResitSenaraiInvois rsi = new KewResitSenaraiInvois();
				rsi.setInvois(inv);
				rsi.setResit(resit);
				rsi.setFlagJenisResit("INVOIS");
				mp.persist(rsi);
				
				UtilKewangan.updateInvoisLejarModul(mp,inv,resit,null, userId);
				
				inv.setKredit(inv.getDebit());
				inv.setUserKemaskini(db.find(Users.class, userId));
				inv.setTarikhKemaskini(new Date());
				inv.setFlagBayar("Y");
			}
			
			KewResitKaedahBayaran rkb = new KewResitKaedahBayaran();
			rkb.setResit(resit);
			rkb.setAmaunBayaran(resit.getJumlahAmaunBayaran());
			rkb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "CT"));
			rkb.setNoCek(null);
			rkb.setNoRujukan(null);
			mp.persist(rkb);
			
		} catch (Exception ex) {
			System.out.println("error aftersave[HasilBerkelompokKuarters] : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Class<KelompokKuarters> getPersistenceClass() {
		return KelompokKuarters.class;
	}

	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		Users objUserLogin = db.find(Users.class, userId);
		context.put("objUserLogin", objUserLogin);
		
		disabledButton();
		filtering();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	public void filtering(){
		
	}
	
	public void disabledButton(){
		this.setDisableKosongkanUpperButton(true);
		this.setHideDeleteButton(true);
		this.setDisableAddNewRecordButton(true);
		this.setDisabledInfoNextTab(true);
	}
	
	@Override
	public boolean delete(KelompokKuarters r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		return "bph/modules/kewangan/hasilBerkelompokKuarters";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KelompokKuarters r) {
		List<KewInvois> lv = db.list("select x from KewInvois x where x.kodHasil.id = '92401' and x.flagBayar = 'T' and x.pembayar.id "
				+ "in (select y.kuaPenghuni.pemohon.id from KelompokKuartersPenghuni y where y.kelompokKuarters.id = '"+r.getId()+"' ) ");
		context.put("listInvois",lv);
		context.put("listKelompokPenghuni", r.getSenaraiKuartersPenghuni());
	}

	@Override
	public void save(KelompokKuarters r) throws Exception {
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("namaAgensi", getParam("findNamaAgensi"));
		return map;
	}
	
}

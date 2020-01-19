/**
R * @author muhdsyazreen
 */

package bph.modules.kew.terimaanHasil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorMultipleValue;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewResitKaedahBayaran;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.kewangan.KewTempInQueue;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class HasilBerkelompokRecordModule extends LebahRecordTemplateModule<RppPermohonan>{

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(RppPermohonan arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {

		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		
		this.addFilter("jenisPemohon = 'KELOMPOK' ");
		this.addFilter("noLoTempahan is not null ");
		this.addFilter("status.id in ('1433097397170','1425259713415') ");
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public boolean delete(RppPermohonan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kewangan/hasilBerkelompok";
	}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppPermohonan.class;
	}

	@Override
	public void getRelatedData(RppPermohonan r) {
		
		
		try{
			mp = new MyPersistence();
			
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			context.put("r", rr);
			
			RppAkaun ak = (RppAkaun) mp.get("select x from RppAkaun x where x.permohonan.id = '"+r.getId()+"' ");
			context.put("ak", ak);
			
			KewInvois inv = null;
			if( ak != null ){
				inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+ak.getId()+"' ");
			}
			context.put("inv", inv);
			
			
			KewResitSenaraiInvois rsi = null;
			if( inv != null ){
				rsi = (KewResitSenaraiInvois) mp.get("select x from KewResitSenaraiInvois x where x.invois.id = '"+inv.getId()+"' ");
			}
			context.put("rsi", rsi);
			
			KewResitKaedahBayaran rkb = null;
			if( rsi != null){
				rkb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+rsi.getResit().getId()+"' ");
			}
			context.put("rkb", rkb);
			
		}catch(Exception ex){
			System.out.println("error getRelatedData : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public void save(RppPermohonan r) throws Exception {
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> q = new HashMap<String, Object>();
		List<String> listPemohonKelompok = null;

		String multipleItem = "";
		String valueIn = "('')";
		String noInvois = get("findNoInvois");
		
		if(noInvois.length() > 0){
			
			q.put("findNoInvois", noInvois);
			listPemohonKelompok = UtilKewangan.listPemohonKelompok(db,q);
			
			if(listPemohonKelompok != null){
				for(String i : listPemohonKelompok){
						if (multipleItem.length() == 0) {
							multipleItem = "'" + i + "'";
						} else {
							multipleItem = multipleItem + "," + "'" + i + "'";
						}
					}
			}
			
			if(!multipleItem.equalsIgnoreCase("")){
				valueIn = "("+multipleItem+")";
			}

			map.put("id", new OperatorMultipleValue(valueIn));
		}
		
		map.put("noLoTempahan", getParam("findNoLoTempahan"));
		map.put("bahagianUnit", get("findNamaAgensi"));
		map.put("pemohon.id", get("findIdPemohon"));
		return map;
	}
	
	@Command("doSimpanNoInvois")
	public String doSimpanNoInvois() throws Exception {

		String statusInfo = "";
		userId = (String) request.getSession().getAttribute("_portal_login");
		String id = getParam("id");
		String noInvoisManual = getParam("noInvois");
		
		try{
			mp = new MyPersistence();

			Users userLogin = (Users) mp.find(Users.class, userId);
		
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, id);
			List<RppAkaun> listAk = mp.list("select x from RppAkaun x where x.permohonan.id = '"+id+"' ");
			
			mp.begin();
			
			for(RppAkaun ak : listAk){
				
				if(ak == null){
					statusInfo = "REKOD INVOIS TIDAK BERJAYA DISIMPAN";
					context.put("errorMakePayment", "T");
					return getPath() + "/entry_page.vm";
				}else{
					//create invois baru dengan no.invois manual
					KewInvois inv = (KewInvois) mp.get("Select x from KewInvois x where x.idLejar='" + ak.getId()+"'");
					if( inv==null ){
						inv = new KewInvois();
					}
				
				//update kredit/status invois manual
				inv.setDebit(ak.getDebit());
				inv.setFlagBayaran("SEWA");
				inv.setIdLejar(ak.getId());
				inv.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class,"02")); // 02 - IR
				inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
				inv.setKodHasil(ak.getKodHasil());
				inv.setNoInvois(noInvoisManual);
				inv.setNoRujukan(ak.getPermohonan().getNoTempahan().toUpperCase());
				inv.setTarikhInvois(new Date());
				inv.setUserPendaftar(userLogin);
				inv.setTarikhDaftar(new Date());
				inv.setFlagBayar("T");
				inv.setFlagQueue("Y");
				inv.setTarikhDari(r.getTarikhMasukRpp());
				inv.setTarikhHingga(r.getTarikhKeluarRpp());
				
				mp.persist(inv);
			
				//listkan semua invois utk kelompok di dalam table Queue
				KewTempInQueue q = new KewTempInQueue();
				q.setInvois(inv);
				q.setPembayar(inv.getPembayar());
				
				mp.persist(q);
			
				}
				
			}
				
			mp.commit();
			statusInfo = "REKOD INVOIS BERJAYA DISIMPAN";
			
			KewInvois invois = (KewInvois) mp.get("Select x from KewInvois x where x.noInvois='" + noInvoisManual+"'");
			context.put("inv", invois);
			
		}catch(Exception ex){
			statusInfo = "REKOD INVOIS TIDAK BERJAYA DISIMPAN";
			System.out.println("error doSimpanNoInvois : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("statusInfo", statusInfo);
		return getPath() + "/entry_page.vm";
		
	}
	
	@Command("doSimpanTerimaanBerkelompok")
	public String doSimpanTerimaanBerkelompok() throws Exception {

		userId = (String) request.getSession().getAttribute("_portal_login");
		String statusInfo = "";
		String id = getParam("id");
		String idResit = getParam("idResit");
		Double amaunBayaran = Util.getDoubleRemoveComma(getParam("amaunBayaran"));
		String noEft = getParam("noEft");
		Date tarikhEft = getDate("tarikhEft");
		
		try{
			mp = new MyPersistence();
			
			Users userLogin = (Users) mp.find(Users.class, userId);
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, id);
			
			KewBayaranResit resit = (KewBayaranResit) mp.get("Select x from KewBayaranResit x where x.id='" + idResit+"'");
			if( resit==null ){ resit = new KewBayaranResit(); }
			
			KewResitKaedahBayaran rkb = (KewResitKaedahBayaran) mp.get("select x from KewResitKaedahBayaran x where x.resit.id = '"+resit.getId()+"' ");
			if( rkb == null){ rkb = new KewResitKaedahBayaran(); }
			
			mp.begin();
			
			//create / update resit
			if (r.getPemohon() != null) {
				resit.setPembayar(r.getPemohon());
				resit.setNoPengenalanPembayar(r.getPemohon().getId());
				resit.setNamaPembayar(r.getPemohon().getUserName());				
				resit.setAlamatPembayar(UtilKewangan.getAlamatPembayar(r.getPemohon()));
			}
			if( resit.getNoResit() == null || resit.getNoResit().equalsIgnoreCase("") ){ 
				resit.setNoResit(UtilKewangan.generateReceiptNo(mp,userLogin)); 
			}
			
			KodJuruwang kodj = (KodJuruwang) mp.get("select x from KodJuruwang x where x.juruwang.id = '"+ userId + "' and x.flagAktif = 'Y'");
			if (kodj != null) {
				resit.setKodJuruwang(kodj);
				resit.setJuruwangKod(kodj.getKodJuruwang());
				resit.setKodPusatTerima(kodj.getKodPusatTerima());
			}
			
			resit.setTarikhBayaran(new Date());
			resit.setTarikhResit(new Date());
			resit.setMasaResit(new Date());
			resit.setFlagJenisBayaran("KAUNTER");
			resit.setTarikhDaftar(new Date());
			resit.setUserPendaftar(userLogin);
			resit.setJumlahAmaunBayaran(amaunBayaran);
			resit.setBilCetakResit(0);
			if(get("namaPembayarLain") != ""){
				resit.setNamaPembayar(get("namaPembayarLain"));
			}else{
				resit.setNamaPembayar(get("namaPembayar"));
			}
			resit.setFlagJenisResit("2"); //INVOIS
			
			mp.persist(resit);
			
			rkb.setAmaunBayaran(amaunBayaran);
			rkb.setModBayaran((CaraBayar) mp.find(CaraBayar.class, "EF"));
			rkb.setNoEft(noEft);
			rkb.setResit(resit);
			rkb.setTarikhEft(tarikhEft);
			
			mp.persist(rkb);
			
			List<RppAkaun> listAk = mp.list("select x from RppAkaun x where x.permohonan.id = '"+id+"'");
			
			
			for(RppAkaun ak : listAk){
				
				//create invois baru dengan no.invois manual
				KewInvois inv = (KewInvois) mp.get("Select x from KewInvois x where x.idLejar='" + ak.getId()+"'");
				
				KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) mp.get("Select x from KewResitSenaraiInvois x where x.invois.id='" + inv.getId()+"'");
				if( rsi==null ){
					rsi = new KewResitSenaraiInvois();
				}
				
				//create KewResitSenaraiInvois
				rsi.setFlagJenisResit("INVOIS");
				rsi.setInvois(inv);
				rsi.setResit(resit);
				mp.persist(rsi);
				
				//update kredit/status invois manual
				inv.setFlagQueue("Y");
				inv.setPembayar(ak.getPermohonan().getPemohon());
				inv.setKredit(inv.getDebit());
				inv.setUserKemaskini(userLogin);
				inv.setTarikhKemaskini(new Date());
				inv.setFlagBayar("Y");
				mp.persist(inv);
				
				//update lejar
				ak.setFlagBayar("Y");
				ak.setIdKemaskini((Users) mp.find(Users.class, userId));
				ak.setKredit(ak.getDebit());
				ak.setNoResit(resit.getNoResit());
				ak.setTarikhKemaskini(new Date());
				ak.setTarikhResit(resit.getTarikhResit());
				ak.setTarikhTransaksi(resit.getTarikhBayaran());
				
				KewTempInQueue q = (KewTempInQueue) mp.get("select y from KewTempInQueue y where y.invois.id=' "+inv.getId()+"'");
				if(q !=null){
					mp.remove(q);
				}
				
				context.put("ak", ak);
				context.put("inv", inv);
				context.put("rsi", rsi);
			
			}
			
			r.setStatusBayaran("Y");
			r.setTarikhBayaran(new Date());
			r.setStatus((Status) mp.find(Status.class, "1425259713415"));
			
			mp.commit();
			
			statusInfo = "REKOD PEMBAYARAN BERJAYA DISIMPAN";
			
			context.put("r", r);
			context.put("rkb", rkb);
			
		}catch(Exception ex){
			statusInfo = "REKOD PEMBAYARAN TIDAK BERJAYA DISIMPAN";
			System.out.println("error doSimpanTerimaanBerkelompok : "+ex.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("statusInfo", statusInfo);
		return getPath() + "/entry_page.vm";
		
	}
	
}

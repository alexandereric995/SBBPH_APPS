package bph.modules.rpp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppPermohonanBayaranBalik;
import bph.utils.DataUtil;
import bph.utils.HTML;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class RppPembatalanRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public void afterSave(RppPermohonan r) { }

	@Override
	public void beforeSave() { }

	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		//permohonan baru, permohonan lulus, permohonan diluluskan sub, menunggu kelulusan sub
		this.addFilter("status.id in ('1425259713412','1425259713415','1430809277102','1430809277096')"); 
		
		try {
			mp = new MyPersistence();
			
			if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
				RppPenyeliaPeranginan rppPenyeliaPeranginan = (RppPenyeliaPeranginan) mp.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+userId+"' and x.statusPerkhidmatan = 'Y'");
				String idPeranginan = (rppPenyeliaPeranginan!=null?rppPenyeliaPeranginan.getPeranginan().getId():"");
				this.addFilter("rppPeranginan.id = '" + idPeranginan + "'");
				
			}else if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak")
					&& !userRole.equalsIgnoreCase("(RPP) Pelulus")){
				this.addFilter("pemohon.id = '" + userId + "'");
			}
			
			defaultButtonOption();
			getDataPemohon(mp,userId);
			
			try {
				context.put("carianJenisPeranginan", HTML.SelectJenisPeranginan("carianJenisPeranginan",null, "id=\"carianJenisPeranginan\" style=\"width:100%\" ", "onchange=\" doFilterCarianPeranginan(); \""));
				context.put("carianRppPeranginan", HTML.SelectPeranginanByJenisPeranginan(null,"carianRppPeranginan",null, "id=\"carianRppPeranginan\" style=\"width:100%\" ", "onchange=\"at(this, event);\""));
			} catch (Exception e) {
				System.out.println("error getting dropdown list");
			}
			
			context.remove("r");
			
			context.put("command", command);
			context.put("userRole",userRole);
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	@Override
	public boolean delete(RppPermohonan r) throws Exception {return false;}

	@Override
	public String getPath() { return "bph/modules/rpp/rppPembatalan"; }

	@Override
	public Class<RppPermohonan> getPersistenceClass() { return RppPermohonan.class; }

	@Override
	public void getRelatedData (RppPermohonan r) { 
		try {
			mp = new MyPersistence();
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(mp,r));
			context.put("r", rr);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void save(RppPermohonan r) throws Exception { }

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		String carianRppPeranginan = getParam("carianRppPeranginan");
		String carianJenisPeranginan = getParam("carianJenisPeranginan");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rppPeranginan.id", new OperatorEqualTo(carianRppPeranginan));
		map.put("rppPeranginan.jenisPeranginan.id", new OperatorEqualTo(carianJenisPeranginan));
		map.put("pemohon.noKP", getParam("noKP"));
		map.put("pemohon.userName", getParam("userName"));
		map.put("statusBayaran", getParam("findStatusBayaran"));
		//Fatin
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("tarikhMasukRpp"), getDate("tarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("tarikhMasukRpp"), getDate("tarikhKeluarRpp")));
		
		return map;
	}
	
	@Command("filterCarianRpp")
	public String filterCarianRpp() throws Exception {
		String carianJenisPeranginan = getParam("carianJenisPeranginan");
		context.put("carianRppPeranginan", HTML.SelectPeranginanByJenisPeranginan(carianJenisPeranginan,"carianRppPeranginan",null, "id=\"carianRppPeranginan\" style=\"width:100%\" ", "onchange=\"at(this, event);\""));
		return getPath() + "/divCarianPeranginan.vm";
	}
	
	private void getDataPemohon(MyPersistence mp,String userId){
		Users users = (Users) mp.find(Users.class, userId);
		context.put("users", users);
	}
	
	@Command("getMaklumatPembatalan")
	public String getMaklumatPembatalan() throws Exception {
		String idRppPermohonan = getParam("idRppPermohonan");
		context.remove("r");
		try {
			mp = new MyPersistence();
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			context.put("r",r);
			context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(mp,r));
		} catch (Exception e) {
			System.out.println("Error getMaklumatPembatalan "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath()+ "/maklumatPembatalan.vm";
	}
	
	
	@SuppressWarnings("unchecked")
	@Command("getSavePembatalan")
	public String getSavePembatalan() throws Exception {
		
		String statusInfo = "";
		String idRppPermohonan = getParam("idRppPermohonan");
		String catatanPembatalan = getParam("catatanPembatalan");
		String idStatusPembatalan = "1425259713418";
		String statusDibatalPemohon = "1435093978588";
		String justifikasi = "Pembatalan tempahan setelah bayaran sewa dibuat.";
		
		try {
			mp = new MyPersistence();
			
			userId = (String) request.getSession().getAttribute("_portal_login");
			userRole = (String) request.getSession().getAttribute("_portal_role");
			Users user = (Users) mp.find(Users.class, userId);
			
			mp.begin();
			
			//update main table (permohonan) berkenaan data pembatalan
			RppPermohonan p = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			p.setTarikhPembatalan(new Date());
			p.setCatatanPembatalan(catatanPembatalan);
			p.setPemohonBatal(user);
			
			//create table bayaran balik
			//Bayaran balik dan subsidiari berlaku untuk permohonan yang telah lulus sahaja
			RppPermohonanBayaranBalik pbb = null;
			
			int bil = getDayBeforeMasukRPP(p);
			
			//JIKA PEMBATALAN OLEH PEMOHON <14 HARI DARI TARIKH MENGINAP,DATA TIDAK SEPATUTNYA MASUK DI SUBSIDIARI.PERLU PENGESAHAN RPP PENYEMAK(AGIH KE UNIT KEWANGAN)
			if (("(RPP) Penyemak".equals(userRole) || ("(RPP) Pelulus".equals(userRole)))){
				if (p != null) {
					if (p.getRppPeranginan() != null) {
						if (p.getRppPeranginan().getFlagOperator() != null) {
							if (!p.getRppPeranginan().getFlagOperator().equals("Y")) {
								if (p.getStatus().getId().equalsIgnoreCase("1425259713415")){
									pbb = new RppPermohonanBayaranBalik();
									pbb.setPermohonan(p);
									pbb.setAmaun(p.getJumlahAmaun());
									pbb.setStatus((Status) mp.find(Status.class, "1425259713427"));
									pbb.setTarikhPermohonan(new Date());
									pbb.setSebab(catatanPembatalan);
									pbb.setCatatan(justifikasi);
									mp.persist(pbb);
									
									p.setPermohonanBayaranBalik(pbb);
									
									KewSubsidiari sub = new KewSubsidiari();
									sub.setIdFail(p.getId());
									sub.setJenisSubsidiari((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
									sub.setJustifikasiPemohon(justifikasi);
									sub.setPemohon(p.getPemohon());
									sub.setStatus((Status) mp.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
									sub.setTarikhPermohonan(new Date());
									sub.setFlagResitBayaran("Y");
									sub.setFlagSalinanAkaunBank("Y");
									sub.setFlagSuratTawaran("Y");
									sub.setFlagSuratSokongan("Y");
									mp.persist(sub);
								}
							}
						}
					}
				}				
			}else{
				if(bil >= 14){//JIKA PEMBATALAN OLEH PEMOHON >14 HARI DARI TARIKH MENGINAP, ADD PERMOHONAN BAYARAN BALIK AND SUBSIDIARI
					if (p != null) {
						if (p.getRppPeranginan() != null) {
							if (p.getRppPeranginan().getFlagOperator() != null) {
								if (!p.getRppPeranginan().getFlagOperator().equals("Y")) {
									if(p.getStatus().getId().equalsIgnoreCase("1425259713415")){
										pbb = new RppPermohonanBayaranBalik();
										pbb.setPermohonan(p);
										pbb.setAmaun(p.getJumlahAmaun());
										pbb.setStatus((Status) mp.find(Status.class, "1425259713427"));
										pbb.setTarikhPermohonan(new Date());
										pbb.setSebab(catatanPembatalan);
										pbb.setCatatan(justifikasi);
										mp.persist(pbb);
										
										p.setPermohonanBayaranBalik(pbb);
										
										KewSubsidiari sub = new KewSubsidiari();
										sub.setIdFail(p.getId());
										sub.setJenisSubsidiari((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
										sub.setJustifikasiPemohon(justifikasi);
										sub.setPemohon(p.getPemohon());
										sub.setStatus((Status) mp.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
										sub.setTarikhPermohonan(new Date());
										sub.setFlagResitBayaran("Y");
										sub.setFlagSalinanAkaunBank("Y");
										sub.setFlagSuratTawaran("Y");
										sub.setFlagSuratSokongan("Y");
										mp.persist(sub);
									}
								}
							}
						}
					}					
				}
			}
			
			if(p.getStatus().getId().equalsIgnoreCase("1425259713415")){
//				pbb = new RppPermohonanBayaranBalik();
//				pbb.setPermohonan(p);
//				pbb.setAmaun(p.getJumlahAmaun());
//				pbb.setStatus((Status) mp.find(Status.class, "1425259713427"));
//				pbb.setTarikhPermohonan(new Date());
//				pbb.setSebab(catatanPembatalan);
//				pbb.setCatatan(justifikasi);
//				mp.persist(pbb);
				
				//get deposit
				RppAkaun akdep = (RppAkaun) mp.get("select x from RppAkaun x where x.permohonan.id = '"+p.getId()+"' and x.kodHasil.id = '72311' ");
				if(akdep != null){
					KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+akdep.getId()+"' ");
					if(dep != null){
						KewTuntutanDeposit t = new KewTuntutanDeposit();
						t.setDeposit(dep);
						t.setJenisTuntutan((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
						t.setPenuntut(p.getPemohon());
						t.setStatus((Status) mp.find(Status.class, "1436464445665"));
						t.setTarikhPermohonan(new Date());
						t.setSuratPengesahanDeposit("Y");
						mp.persist(t);
						
						dep.setTuntutanDeposit(t);
					}
				}
				
//				p.setPermohonanBayaranBalik(pbb);
//				
//				KewSubsidiari sub = new KewSubsidiari();
//				sub.setIdFail(p.getId());
//				sub.setJenisSubsidiari((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
//				sub.setJustifikasiPemohon(justifikasi);
//				sub.setPemohon(p.getPemohon());
//				sub.setStatus((Status) mp.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
//				sub.setTarikhPermohonan(new Date());
//				sub.setFlagResitBayaran("Y");
//				sub.setFlagSalinanAkaunBank("Y");
//				sub.setFlagSuratTawaran("Y");
//				sub.setFlagSuratSokongan("Y");
//				mp.persist(sub);
				
			}
			
			List<RppAkaun> lk = mp.list("select x from RppAkaun x where x.permohonan.id = '"+p.getId()+"' ");
			
			if(lk != null){
				for(int i=0;i<lk.size();i++){
					RppAkaun lj = lk.get(i);
		
					if(lj.getKodHasil().getId().equalsIgnoreCase("72311")){
						//deposit
						KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+lj.getId()+"' ");
						if(dep!=null){
							if(p.getStatusBayaran().equalsIgnoreCase("Y")){
								//
							}else{
								mp.remove(dep);
							}
						}
					}else{
						//invois
						KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
						if(inv!=null){
							if(p.getStatusBayaran().equalsIgnoreCase("Y")){
								//
							}else{
								mp.remove(inv);
							}
						}						
					}
					
					if(lj!=null){
						lj.setAmaunVoid(lj.getDebit());
						lj.setFlagVoid("Y");
						lj.setTarikhVoid(new Date());
					}
				}
			}
			
			if(p.getStatus().getId().equalsIgnoreCase("1425259713415")){
				statusInfo = "Tempahan anda telah dibatalkan. Uruskan bayaran balik sedang diproses.";
				//UtilRpp.saveNotifikasi(p.getId(),"PEMBATALAN_SELEPAS_BAYAR",getClass().getName(), new Date(), null, null, null);
			}else{
				statusInfo = "Pembatalan telah dibuat. Maklumat pembatalan tempahan anda akan direkodkan";
				//UtilRpp.saveNotifikasi(p.getId(),"PEMBATALAN_SEBELUM_BAYAR",getClass().getName(), new Date(), null, null, null);
			}
			
			if(p.getStatus().getId().equalsIgnoreCase("1425259713415")){
				p.setStatus((Status) mp.find(Status.class, idStatusPembatalan));
			}else{
				p.setStatus((Status) mp.find(Status.class, statusDibatalPemohon));
			}
			
			UtilRpp.deleteChildTempahan(mp, idRppPermohonan);
			
			mp.commit();
			
		} catch (Exception e) {
			System.out.println("Error getSavePembatalan : "+e.getMessage());
			statusInfo = "Pembatalan tidak berjaya";
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("statusInfo", statusInfo);
		return getPath()+ "/status.vm";
	}
	
	public static int getDayBeforeMasukRPP(RppPermohonan r){
		int days = 0;
		if(r.getTarikhMasukRpp() != null && r.getTarikhKeluarRpp() != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			Calendar dateMasukRPP = Calendar.getInstance();
			dateMasukRPP.setTime(r.getTarikhMasukRpp());
			dateMasukRPP.set(Calendar.HOUR_OF_DAY, 0);
			dateMasukRPP.set(Calendar.MINUTE, 0);
			dateMasukRPP.set(Calendar.SECOND, 0);
			dateMasukRPP.set(Calendar.MILLISECOND, 0);
			//days = (int)( (cal.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24) );
			days = Util.daysBetween(cal.getTime(),dateMasukRPP.getTime());
		}
		return days;
	}
	
}







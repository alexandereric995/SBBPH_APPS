package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppPermohonanBayaranBalik;
import bph.utils.DataUtil;
import bph.utils.HTML;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class RppHistoryRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@Override
	public String getPath() { return "bph/modules/rpp/rppHistory"; }
	
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
		
		//String currentdate = Util.getDateTime(new Date(), "yyyy-MM-dd");
		//this.addFilter("tarikhKeluarRpp <= '"+currentdate+"' ");
		
		this.addFilter("status.id in ('1430809277099','1425259713418','1425259713424','1425259713430','1425259713421','1433083787409','1435093978588','1435512646303','1688545253001455')");
		
		try{
			mp = new MyPersistence();
			filtering(mp,userId,userRole);
			
			defaultButtonOption();
			removeContext();
			getDataPemohon(mp,userId);
			addfilter(); //filter sejarah by individu, transit dan kelompok
			
			context.put("carianJenisPeranginan", HTML.SelectJenisPeranginan("carianJenisPeranginan",null, "id=\"carianJenisPeranginan\" style=\"width:100%\" ", "onchange=\" doFilterCarianPeranginan(); \""));
			context.put("carianRppPeranginan", HTML.SelectPeranginanByJenisPeranginan(null,"carianRppPeranginan",null, "id=\"carianRppPeranginan\" style=\"width:100%\" ", "onchange=\"at(this, event);\""));
			context.put("carianStatusPermohonan", HTML.SelectStatusHistory("carianStatusPermohonan",null, "id=\"carianStatusPermohonan\" style=\"width:100%\" ", ""));
			
		}catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("listStatus", dataUtil.getListStatusHistory());
		context.put("command", command);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole", userRole);
		context.put("success", "");
	}
	
	private void filtering(MyPersistence mp, String userId, String userRole) {
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			String listPeranginan = UtilRpp.multipleListSeliaan(mp,userId);
			if (listPeranginan.length() == 0) {
				this.addFilter("rppPeranginan.id = ''");
			} else {
				this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
			}	
			this.setOrderBy("tarikhMasukRpp desc");
			
		}else if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak")
				&& !userRole.equalsIgnoreCase("(RPP) Pelulus")){
			this.addFilter("pemohon.id = '" + userId + "' ");
		}
	}
	
	public void addfilter() {
		dataUtil = DataUtil.getInstance(db);
		this.addFilter("jenisPemohon = 'INDIVIDU' ");
		this.addFilter("rppPeranginan.jenisPeranginan.id in ('RP','RPP') ");
		context.put("listPeranginan", dataUtil.getListPeranginanByGred(null));
	}

	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	private void removeContext(){
		context.remove("users");
		context.remove("r");
		context.remove("carianJenisPeranginan");
		context.remove("carianRppPeranginan");
		context.remove("carianStatusPermohonan");
	}
	
	@Override
	public boolean delete(RppPermohonan r) throws Exception { return false; }

	@Override
	public Class<RppPermohonan> getPersistenceClass() { return RppPermohonan.class; }

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(RppPermohonan r) {
		
		try{
			mp = new MyPersistence();
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			
			List<RppAduanKerosakan> listAduan = (List<RppAduanKerosakan>) mp.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+rr.getId()+"' ");
			context.put("listAduan",listAduan);
			context.put("r",r);
			context.put("listTempahanDanBayaran", UtilRpp.getListTempahanDanBayaran(mp,r));
			context.put("r", rr);
			
			context.put("statusInfo", "");
			context.put("idBayaranBalik", "");
			
		}catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public void save(RppPermohonan r) throws Exception { }

	@Override
	public Map<String, Object> searchCriteria() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.noKP", getParam("findNoKp"));
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("rppPeranginan.id", new OperatorEqualTo(getParam("findRpp")));
		map.put("status.id", getParam("findStatus"));
		map.put("statusBayaran", getParam("findStatusBayaran"));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
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
	
	@Command("saveAgihanSubsidari")
	public String saveAgihanSubsidari(){
		String statusInfo = "";
		String idRppPermohonan = getParam("idRppPermohonan");
		context.put("idBayaranBalik", "");
		boolean success = false;
		boolean addNew = false;
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession().getAttribute("_portal_login");
			//Users user = (Users) mp.find(Users.class, userId);
			mp.begin();
			RppPermohonan p = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			Users pegawai = (Users) mp.find(Users.class, userId);
			Users user = (Users) mp.find(Users.class,p.getPemohon().getId() );
			//update main table (permohonan) berkenaan data pembatalan
			//RppPermohonan p = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			//p.setTarikhPembatalan(new Date());
			//p.setCatatanPembatalan(catatanPembatalan);
			//p.setPemohonBatal(user);
			
			//create table bayaran balik
			//Bayaran balik dan subsidiari berlaku untuk permohonan yang telah lulus sahaja
			RppPermohonanBayaranBalik pbb = null;
			if(p.getStatus().getId().equalsIgnoreCase("1425259713418") || p.getStatus().getId().equalsIgnoreCase("1433083787409") || p.getStatus().getId().equalsIgnoreCase("1435093978588")){
				pbb = new RppPermohonanBayaranBalik();
				pbb.setPermohonan(p);
				pbb.setAmaun(p.getJumlahAmaun());
				pbb.setStatus((Status) mp.find(Status.class, "1425259713427"));
				pbb.setTarikhPermohonan(new Date());
				mp.persist(pbb);
				
				//get deposit
				RppAkaun akdep = (RppAkaun) mp.get("select x from RppAkaun x where x.permohonan.id = '"+p.getId()+"' and x.kodHasil.id = '72311' ");
				if(akdep != null){
					KewDeposit dep = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar = '"+akdep.getId()+"' ");
					if(dep != null){
						KewTuntutanDeposit t=null;
						KewTuntutanDeposit tuntutan = (KewTuntutanDeposit) mp.get("select x from KewTuntutanDeposit x where x.deposit.id = '"+dep.getId()+"' ");
						if(tuntutan != null){
							t=(KewTuntutanDeposit) mp.get("select x from KewTuntutanDeposit x where x.deposit.id = '"+dep.getId()+"' ");
						}else{
							t = new KewTuntutanDeposit();
							addNew=true;
						}
						t.setDeposit(dep);
						t.setJenisTuntutan((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
						t.setPenuntut(p.getPemohon());
						t.setStatus((Status) mp.find(Status.class, "1436464445665"));
						t.setTarikhPermohonan(new Date());
						t.setSuratPengesahanDeposit("Y");
						if(addNew){
							mp.persist(t);
						}
						dep.setTuntutanDeposit(t);
					}
				}
				
				p.setPermohonanBayaranBalik(pbb);
				KewSubsidiari sub = new KewSubsidiari();
				sub.setIdFail(p.getId());
				sub.setJenisSubsidiari((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
				sub.setPemohon(user);
				sub.setStatus((Status) mp.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
				sub.setTarikhPermohonan(new Date());
				sub.setFlagResitBayaran("Y");
				sub.setFlagSalinanAkaunBank("Y");
				sub.setFlagSuratTawaran("Y");
				sub.setFlagSuratSokongan("Y");
				sub.setDaftarOleh(pegawai);
				sub.setKemaskiniOleh(pegawai);
				mp.persist(sub);
				
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
								//mp.remove(dep);
							}
						}
					}else{
						//invois
						KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
						if(inv!=null){
							if(p.getStatusBayaran().equalsIgnoreCase("Y")){
								//
							}else{
								//mp.remove(inv);
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
			
//			if(p.getStatus().getId().equalsIgnoreCase("1425259713415")){
//				statusInfo = "Tempahan anda telah dibatalkan. Uruskan bayaran balik sedang diproses.";
//				//UtilRpp.saveNotifikasi(p.getId(),"PEMBATALAN_SELEPAS_BAYAR",getClass().getName(), new Date(), null, null, null);
//			}else{
//				statusInfo = "Pembatalan telah dibuat. Maklumat pembatalan tempahan anda akan direkodkan";
//				//UtilRpp.saveNotifikasi(p.getId(),"PEMBATALAN_SEBELUM_BAYAR",getClass().getName(), new Date(), null, null, null);
//			}
			
//			if(p.getStatus().getId().equalsIgnoreCase("1425259713415")){
//				p.setStatus((Status) mp.find(Status.class, idStatusPembatalan));
//			}else{
//				p.setStatus((Status) mp.find(Status.class, statusDibatalPemohon));
//			}
			
		//	UtilRpp.deleteChildTempahan(mp, idRppPermohonan);
			
			mp.commit();
			context.put("r", p);
			success = true;
		} catch (Exception e) {
			System.out.println("Error saveAgihanSubsidari : "+e.getMessage());
			//statusInfo = "Pembatalan tidak berjaya";
		}finally{
			if (mp != null) { mp.close(); }
		}
		statusInfo = "AGIHAN KEPADA KEWANGAN TELAH DIBUAT.HARAP MAKLUM";
		context.put("statusInfo", statusInfo);
		context.put("success", success);
		return getPath()+ "/status.vm";
		//return getPath()+ "/entry_page.vm";
		
	}
	
}







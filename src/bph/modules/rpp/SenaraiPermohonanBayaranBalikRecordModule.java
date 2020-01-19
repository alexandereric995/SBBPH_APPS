package bph.modules.rpp;

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
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanBayaranBalikRecordModule extends LebahRecordTemplateModule<RppPermohonan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void afterSave(RppPermohonan r) { }

	@Override
	public void beforeSave() { }

	@Override
	public Class<RppPermohonan> getPersistenceClass() { return RppPermohonan.class; }
	
	@Override
	public String getPath() { return "bph/modules/rpp/senaraiPermohonanBayaranBalik"; }
	
	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		
		context.remove("selectStatusBayaranBalik");
		context.remove("selectNoTempahan");
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		defaultButtonOption();
		
		try {
			mp = new MyPersistence();
			
			Users users = (Users) mp.find(Users.class, userId);
			
			this.setReadonly(true);
			addfilter(mp);
			
			context.put("users", users);
			context.put("selectStatusBayaranBalik", dataUtil.getListStatusRppBayaranBalik());
			context.put("selectStatusTempahan", dataUtil.getListStatusPermohonan());
			context.put("listPeranginan", dataUtil.getListPeranginanExceptRppLondon());
			context.put("path", getPath());
			context.put("util", new Util());
			context.put("userRole",userRole);
			context.put("command", command);
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.noKP", getParam("findNoKp"));
		map.put("pemohon.userName", getParam("findUserName"));
		map.put("rppPeranginan.id", new OperatorEqualTo(getParam("findRpp")));
		map.put("status.id", new OperatorEqualTo(getParam("findStatus")));
		map.put("statusBayaran", getParam("findStatusBayaran"));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		return map;
	}
	
	private void addfilter(MyPersistence mp) {
		
		if(userRole.equalsIgnoreCase("(RPP) Penyelia")){
			RppPenyeliaPeranginan rppPenyeliaPeranginan = (RppPenyeliaPeranginan) mp.get("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"+userId+"' and x.statusPerkhidmatan = 'Y'");
			String idPeranginan = (rppPenyeliaPeranginan!=null?rppPenyeliaPeranginan.getPeranginan().getId():"");
			this.addFilter("rppPeranginan.id = '" + idPeranginan + "'");
		
		}else if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak")
				&& !userRole.equalsIgnoreCase("(RPP) Pelulus")){
			this.addFilter("pemohon.id = '" + userId + "'");
		}

		//status lulus & status bayaran = Y
		this.addFilter("status.id in ('1425259713418','1435512646303','1433083787409','1435093978588')");
		this.addFilter("statusBayaran='Y'");
	}
	
	private void defaultButtonOption() {
		this.setDisableBackButton(true);
	}

	@Override
	public boolean delete(RppPermohonan r) throws Exception {
		if(userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus")){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void getRelatedData(RppPermohonan r) {
		
		try{
			mp = new MyPersistence();
			RppPermohonan rr = (RppPermohonan) mp.find(RppPermohonan.class, r.getId());
			KewSubsidiari sub = (KewSubsidiari) mp.get("select x from KewSubsidiari x where x.idFail = '"+rr.getId()+"' and x.jenisSubsidiari.id = '02' ");
			
			context.put("r", rr);
			
			if(sub != null){
				context.put("s",sub);
				context.put("listInvois",getListInvois(sub,mp));
			}
			
			RppAkaun bb = (RppAkaun) mp.get("select x from RppAkaun x where x.permohonan.id ='" + rr.getId() +"' and x.kodHasil.id='72311'");
			String idLejar = bb.getId();
			KewDeposit depo = (KewDeposit) mp.get("select x from KewDeposit x where x.idLejar ='" + idLejar +"'");
			KewTuntutanDeposit tuntutan = (KewTuntutanDeposit) mp.get("select x from KewTuntutanDeposit x where x.deposit.id ='" + depo.getId() +"'");

			RppPermohonan p = (RppPermohonan) mp.get("select x from RppPermohonan x where x.id ='" + bb.getPermohonan().getId() +"'");
			if(p!=null){
				context.put("permohonan", p);
				context.put("tuntutan", tuntutan);
			}
			
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());	
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<KewInvois> getListInvois(KewSubsidiari r, MyPersistence mp) {
		RppPermohonan permohonan = (RppPermohonan) mp.get("select x from RppPermohonan x where x.id = '"+r.getIdFail()+"'");
		List<KewInvois> listInvois = 
				mp.list("select x from KewInvois x where x.pembayar.id = '"+r.getPemohon().getId()+"' "+
						" and x.jenisBayaran.id = '"+r.getJenisSubsidiari().getId()+"' and x.noRujukan = '" +permohonan.getNoTempahan()+ "'");
		return listInvois;
	}
	
	@Override
	public void save(RppPermohonan r) throws Exception { }
	
	@Command("getMaklumatBayaranBalik")
	public String getMaklumatBayaranBalik() throws Exception {
		
		String idRppPermohonan = getParam("idPermohonan");
		try{
			mp = new MyPersistence();
			
			RppPermohonan r = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
			
			KewSubsidiari sub = (KewSubsidiari) mp.get("select x from KewSubsidiari x where x.idFail = '"+r.getId()+"' and x.jenisSubsidiari.id = '02' ");
			context.put("s",sub);
			context.put("r",r);
			
		} catch (Exception e) {
			System.out.println("Error getMaklumatBayaranBalik : "+e.getMessage());	
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/maklumatPermohonanBayaranBalik.vm";
	}
	
	@Command("savePermohonan")
	public String savePermohonan() throws Exception {
		
		String userId = (String) request.getSession().getAttribute("_portal_login");
		
		String statusInfo = "";
		String idRppPermohonan = getParam("idPermohonan");
		String justifikasi = getParam("justifikasi");

		try{
			mp = new MyPersistence();
			
			Users user = (Users) mp.find(Users.class, userId);
			
			KewSubsidiari sub = (KewSubsidiari) mp.get("select x from KewSubsidiari x where x.idFail = '"+idRppPermohonan+"' and x.jenisSubsidiari.id = '02' ");
			RppPermohonan rp = (RppPermohonan) mp.find(RppPermohonan.class, idRppPermohonan);
		    
			if(sub==null){ sub = new KewSubsidiari(); }
			
			mp.begin();
			
			sub.setIdFail(rp.getId());
			sub.setJenisSubsidiari((KewJenisBayaran) mp.find(KewJenisBayaran.class, "02"));
			sub.setJustifikasiPemohon(justifikasi);
			sub.setPemohon(user);
			sub.setStatus((Status) mp.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
			sub.setTarikhPermohonan(new Date());
			sub.setFlagResitBayaran("Y");
			sub.setFlagSalinanAkaunBank("Y");
			sub.setFlagSuratTawaran("Y");
			sub.setFlagSuratSokongan("Y");
			
			mp.persist(sub);
			
			mp.commit();
			statusInfo = "success";
			
		} catch (Exception e) {
			System.out.println("Error savePermohonan : "+e.getMessage());	
			statusInfo = "error";
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		context.put("statusInfo", statusInfo);
		
		return getMaklumatBayaranBalik();
	}
	
	@Command("getPemohon")
	public String getPemohon() throws Exception {
		
		try{
			mp = new MyPersistence();
			
			String namaPemohon = "";
			String idPemohon = "";
			Double amaun = 0.0;
			RppPermohonan permohonan = (RppPermohonan) mp.find(RppPermohonan.class, get("selectNoTempahan"));
			Users pemohon = (Users) mp.find(Users.class, permohonan.getPemohon().getId());
			if (pemohon != null)
				namaPemohon = pemohon.getUserName().toUpperCase();
				idPemohon = pemohon.getId();
				amaun = permohonan.getJumlahAmaun();
			
			context.put("namaPemohon", namaPemohon);
			context.put("idPemohon", idPemohon);
			context.put("amaun", amaun);
			
		} catch (Exception e) {
			System.out.println("Error getPemohon : "+e.getMessage());	
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/namaPemohon.vm";
	}
	
}

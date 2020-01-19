package bph.modules.kewangan.subsidiari.pelarasan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import portal.module.entity.Users;
import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kod.Status;
import bph.entities.rpp.RppPermohonan;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilKewangan;

public class PelarasanSubsidiari extends LebahRecordTemplateModule<KewSubsidiari> {
	
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public void begin() { 
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		Users user = db.find(Users.class, userId);	
		disabledButton();
		filtering();
		if(userRole.equalsIgnoreCase("(SUBSIDIARI) Penyedia")){
			this.addFilter("status.id IN ('1436510785718','1436510785725','1436510785729') ");
			this.addFilter("agihan.penyedia.id = '"+userId+"' ");
		}else if(userRole.equalsIgnoreCase("(SUBSIDIARI) Penyemak")){
			this.addFilter("status.id IN ('1438023402951','1436510785721','1436510785725') "); 
		}else if(userRole.equalsIgnoreCase("(SUBSIDIARI) Pelulus")){
			this.addFilter("status.id IN ('1438023402975','1436510785725') ");
		}
		context.put("user", user);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		context.put("utilKewangan", new UtilKewangan());
		context.put("findStatusPermohonan", dataUtil.getListStatusSubsidari());
	}
	
	public void filtering(){
		//Bergantung pada class extend
	}
	
	public void disabledButton(){
		this.setHideDeleteButton(true);
		this.setDisableBackButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableAddNewRecordButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
	}

	@Override
	public boolean delete(KewSubsidiari r) throws Exception {
		return false;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/pelarasan/kuarters"; }

	@Override
	public Class<KewSubsidiari> getPersistenceClass() {
		return KewSubsidiari.class;
	}

	@Override
	public void getRelatedData(KewSubsidiari r) {

		context.put("listInvois",getListInvois(r));
	}

	@Override
	public void beforeSave() { 

	}
	
	@Override
	public void save(KewSubsidiari r) throws Exception {		
		Users user = db.find(Users.class, userId);	
		List<KewInvois> listInvois = getListInvoisIR(r);
		for(int i=0;i<listInvois.size();i++){
			String id = listInvois.get(i).getId();
			KewInvois inv = db.find(KewInvois.class, id);
			Double amaunPelarasan = Util.getDoubleRemoveComma(getParam("amaunPelarasan"));
			String catatanPelarasan = getParam("catatanPelarasan");
			if(inv.getKeteranganBayaran().contains("TAMBAHAN") || inv.getKeteranganBayaran().contains("BOT")){
				inv.setAmaunPelarasan(0d);
			}else{
				inv.setAmaunPelarasan(amaunPelarasan);
			}			
			inv.setCatatanPelarasan(catatanPelarasan);
			inv.setUserKemaskini(user);
			inv.setTarikhKemaskini(new Date());
		}
		
		r.setStatus(db.find(Status.class, "1436510785729")); //STATUS JADI PENYEDIAAN BAUCER
	}

	@Command("simpanMaklumatBaucer")
	public String simpanMaklumatBaucer() throws Exception {
		String id = getParam("id");
		KewSubsidiari r = db.find(KewSubsidiari.class, id);
		List<KewInvois> listInvois = getListInvoisIR(r);
		db.begin();
		for(int i=0;i<listInvois.size();i++){
			String noBaucer = getParam("noBaucer");
			String noEFT = getParam("noEFT");
			Date tarikhBaucer=getDate("tarikhBaucer");
			Date tarikhEFT=getDate("tarikhEFT");
			r.setNoBaucerBayaran(noBaucer);
			r.setTarikhBaucer(tarikhBaucer);
			r.setNoEFT(noEFT);
			r.setTarikhEFT(tarikhEFT);
			if(noBaucer != null && tarikhBaucer!=null && noEFT!=null && tarikhEFT!=null){
				r.setStatus(db.find(Status.class, "1436510785721"));//STATUS JADI PENYEDIAAN BAUCER SELESAI
			}
		}
		db.commit();
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("hantarPengesahan")
	public String hantarPengesahan() throws Exception {

		String id = getParam("id");
		KewSubsidiari r = db.find(KewSubsidiari.class, id);
		
		db.begin();
		r.setStatus(db.find(Status.class, "1438023402951")); //MOHON PENGESAHAN PELARASAN
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("hantarKelulusan")
	public String hantarKelulusan() throws Exception {

		String id = getParam("id");
		KewSubsidiari r = db.find(KewSubsidiari.class, id);
		Users user = db.find(Users.class, userId);
		
		db.begin();
		
		List<KewInvois> listInvois = getListInvois(r);
		for(int i=0;i<listInvois.size();i++){
			String idInvois = listInvois.get(i).getId();
			KewInvois inv = db.find(KewInvois.class, idInvois);
			Double amaunPelarasan = Util.getDoubleRemoveComma(getParam("amaunPelarasan"));
			String catatanPelarasan = getParam("catatanPelarasan");
			if(inv.getKeteranganBayaran().contains("TAMBAHAN") || inv.getKeteranganBayaran().contains("BOT")){
				inv.setAmaunPelarasan(0d);
			}else{
				inv.setAmaunPelarasan(amaunPelarasan);
			}		
			inv.setCatatanPelarasan(catatanPelarasan);
			inv.setUserKemaskini(user);
			inv.setTarikhKemaskini(new Date());
		}
		
		r.setStatus(db.find(Status.class, "1438023402975")); //MOHON KELULUSAN PELARASAN
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("lulusPelarasan")
	public String lulusPelarasan() throws Exception {

		String id = getParam("id");
		KewSubsidiari r = db.find(KewSubsidiari.class, id);
		Users user = db.find(Users.class, userId);
		
		db.begin();
		
		List<KewInvois> listInvois = getListInvois(r);
		for(int i=0;i<listInvois.size();i++){
			String idInvois = listInvois.get(i).getId();
			KewInvois inv = db.find(KewInvois.class, idInvois);
			Double amaunPelarasan = Util.getDoubleRemoveComma(getParam("amaunPelarasan"));
			String catatanPelarasan = getParam("catatanPelarasan");
			
			if(inv.getKeteranganBayaran().contains("TAMBAHAN") || inv.getKeteranganBayaran().contains("BOT")){
				inv.setAmaunPelarasan(0d);
			}else{
				inv.setAmaunPelarasan(amaunPelarasan);
			}	
			
			inv.setCatatanPelarasan(catatanPelarasan);
			inv.setUserKemaskini(user);
			inv.setTarikhKemaskini(new Date());
		}
		r.setStatus(db.find(Status.class, "1436510785725")); //SUBSIDIARI TELAH DIPULANGKAN
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
//	End Enable byzul
	
	@Override
	public void afterSave(KewSubsidiari r) { 

	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.userName", getParam("findPemohon"));
		map.put("pemohon.noKP", getParam("findNoKP"));
		map.put("status.id", getParam("findStatusPermohonan"));
		map.put("tarikhPermohonan", new OperatorDateBetween(
				getDate("findTarikhPermohonan"),
				getDate("findTarikhPermohonanHingga")));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<KewInvois> getListInvois(KewSubsidiari r) {
		
		List<KewInvois> listInvois = 
				db.list("select x from KewInvois x where x.pembayar.id = '"+r.getPemohon().getId()+"' "+
						" and x.jenisBayaran.id = '"+r.getJenisSubsidiari().getId()+"'");
		return listInvois;
	}
	
	@SuppressWarnings("unchecked")
	public List<KewInvois> getListInvoisIR(KewSubsidiari r) {
		RppPermohonan permohonan = (RppPermohonan) db.get("select x from RppPermohonan x where x.id = '"+r.getIdFail()+"'");
		List<KewInvois> listInvois = 
				db.list("select x from KewInvois x where x.pembayar.id = '"+r.getPemohon().getId()+"' "+
						" and x.jenisBayaran.id = '"+r.getJenisSubsidiari().getId()+"' and x.noRujukan = '" +permohonan.getNoTempahan()+ "'");
		return listInvois;
	}
}

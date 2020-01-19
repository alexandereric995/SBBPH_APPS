package bph.modules.kewangan.deposit.pelarasan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.Status;
import bph.utils.DataUtil;
import bph.utils.Util;

public class PelarasanDeposit extends LebahRecordTemplateModule<KewTuntutanDeposit> {
	
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
		
		if(userRole.equalsIgnoreCase("(DEPOSIT) Penyedia")){
			this.addFilter("status.id IN ('1436464445668','1436464445673','1438023402980','1436464445695') ");
			this.addFilter("agihan.penyedia.id = '"+userId+"' ");
		}else if(userRole.equalsIgnoreCase("(DEPOSIT) Penyemak")){
			this.addFilter("status.id IN ('1438023402951','1436464445695') ");
		}else if(userRole.equalsIgnoreCase("(DEPOSIT) Pelulus")){
			this.addFilter("status.id IN ('1438023402975','1436464445695') ");
		}
		
		context.put("listStatus",dataUtil.getListStatusPulanganDeposit());
		context.put("listPeranginan",dataUtil.getListPeranginanRppHaveDeposit());
		context.put("user", user);
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
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
	public boolean delete(KewTuntutanDeposit r) throws Exception {
		return false;
	}

	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/pelarasan/kuarters"; }

	@Override
	public Class<KewTuntutanDeposit> getPersistenceClass() {
		return KewTuntutanDeposit.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KewTuntutanDeposit r) {
		List<KewInvois> listInvois = db.list("select x from KewInvois x where x.pembayar.id = '"+r.getPenuntut().getId()+"' "+
						" and x.jenisBayaran.id = '"+r.getJenisTuntutan().getId()+"' and x.flagBayar = 'Y' ");
		
		context.put("listInvois",listInvois);
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		
		String findPemohon = getParam("findPemohon");
		String findNoKp = getParam("findNoKp");
		Date findTarikhMohonDeposit = getDate("findTarikhMohonDeposit");

		String strdate = "";
		if(findTarikhMohonDeposit!=null){
			strdate = new SimpleDateFormat("yyyy-MM-dd").format(findTarikhMohonDeposit);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noResit", getParam("findNoResit"));
		map.put("penuntut.userName", findPemohon);
		map.put("penuntut.id", findNoKp);
		map.put("tarikhPermohonan", strdate);
		
		return map;
	}
	
	@Override
	public void beforeSave() { 

	}
	
	@Override
	public void afterSave(KewTuntutanDeposit r) { 

	}
	
	@Override
	public void save(KewTuntutanDeposit r) throws Exception {
		if(r.getStatus().getId().equalsIgnoreCase("1436464445668")){ //PERMOHONAN TELAH DIAGIHKAN
			r.setStatus(db.find(Status.class, "1436464445673")); //PELARASAN AMAUN TUNTUTAN
		}
		r.setNoBaucerPulanganDeposit(getParam("noBaucerPulanganDeposit"));
		r.setAmaunPelarasanDeposit(Util.getDoubleRemoveComma(getParam("amaunPelarasanDeposit")));
		r.setCatatanPelarasanDeposit(getParam("catatanPelarasanDeposit"));
		r.setNoEft(getParam("noEft"));
		r.setTarikhBaucer(getDate("tarikhBaucer"));
		r.setTarikhEft(getDate("tarikhEft"));
	}
	
	@Command("hantarPengesahan")
	public String hantarPengesahan() throws Exception {

		String id = getParam("id");
		KewTuntutanDeposit r = db.find(KewTuntutanDeposit.class, id);
		
		db.begin();
		r.setStatus(db.find(Status.class, "1438023402951")); //MOHON PENGESAHAN PELARASAN
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("hantarKelulusan")
	public String hantarKelulusan() throws Exception {

		String id = getParam("id");
		KewTuntutanDeposit r = db.find(KewTuntutanDeposit.class, id);
		
		db.begin();
		r.setStatus(db.find(Status.class, "1438023402975")); //MOHON KELULUSAN PELARASAN
		r.setNoBaucerPulanganDeposit(getParam("noBaucerPulanganDeposit"));
		r.setAmaunPelarasanDeposit(Util.getDoubleRemoveComma(getParam("amaunPelarasanDeposit")));
		r.setCatatanPelarasanDeposit(getParam("catatanPelarasanDeposit"));
		r.setNoEft(getParam("noEft"));
		r.setTarikhBaucer(getDate("tarikhBaucer"));
		r.setTarikhEft(getDate("tarikhEft"));
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("lulusPelarasan")
	public String lulusPelarasan() throws Exception {

		String id = getParam("id");
		KewTuntutanDeposit r = db.find(KewTuntutanDeposit.class, id);
		
		db.begin();
		//r.setStatus(db.find(Status.class, "1438023402980")); //PELARASAN DILULUSKAN
		r.setNoBaucerPulanganDeposit(getParam("noBaucerPulanganDeposit"));
		r.setAmaunPelarasanDeposit(Util.getDoubleRemoveComma(getParam("amaunPelarasanDeposit")));
		r.setCatatanPelarasanDeposit(getParam("catatanPelarasanDeposit"));
		r.setNoEft(getParam("noEft"));
		r.setTarikhBaucer(getDate("tarikhBaucer"));
		r.setTarikhEft(getDate("tarikhEft"));
		
		//String id = getParam("id");
		//KewTuntutanDeposit r = db.find(KewTuntutanDeposit.class, id);
		KewDeposit dep = r.getDeposit();
		//db.begin();
		r.setStatus(db.find(Status.class, "1436464445695")); //DEPOSIT TELAH DIPULANGKAN
		Double baki = (dep.getJumlahDeposit() - r.getAmaunPelarasanDeposit());
		dep.setFlagPulangDeposit("Y");
		dep.setBakiDeposit(baki);
		dep.setTarikhPulanganDeposit(getDate("tarikhEft"));
		//db.commit();
		//context.put("r", r);
		//return templateDir + "/entry_fields.vm";
		
		
		/**Dah bayar nanti baru update flag n baki deposit yg dipulangkan*/
		//KewDeposit dep = r.getDeposit();
		//Double baki = dep.getJumlahDeposit() - Util.getDoubleRemoveComma(getParam("amaunPelarasanDeposit"));
		//dep.setFlagPulangDeposit("Y");
		//dep.setBakiDeposit(baki);
		
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
	
	@Command("pulangDeposit")
	public String pulangDeposit() throws Exception {

		String id = getParam("id");
		KewTuntutanDeposit r = db.find(KewTuntutanDeposit.class, id);
		KewDeposit dep = r.getDeposit();
		
		db.begin();
		r.setStatus(db.find(Status.class, "1436464445695")); //DEPOSIT TELAH DIPULANGKAN
		
		Double baki = (dep.getJumlahDeposit() - r.getAmaunPelarasanDeposit());
		dep.setFlagPulangDeposit("Y");
		dep.setBakiDeposit(baki);
		
		db.commit();
		
		context.put("r", r);
		return templateDir + "/entry_fields.vm";
	}
}

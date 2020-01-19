package bph.modules.rpp;

import lebah.portal.action.Command;
import portal.module.entity.Users;
import bph.entities.kod.Status;
import bph.entities.rpp.RppSenaraiHitam;

public class PrmhnSenaraiHitamRecordModule extends SenaraiHitamRecordModule{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/prmhnSenaraiHitam";
	}

	@Override
	public void save(RppSenaraiHitam r) throws Exception {
		String strStatus = "1428990717386"; //STATUS PERMOHONAN SENARAI HITAM
		
		if(r.getPermohonan() == null){
			r.setPemohon(db.find(Users.class, getParam("pemohon")));
		}
		
		r.setTarikhPermohonan(getDate("tarikhPermohonan"));
		r.setSebab(getParam("sebab"));
		r.setCatatan(getParam("catatan"));
		if(r.getStatus()==null){
			r.setStatus(db.find(Status.class, strStatus));
		}
		r.setFlagAktif("T");
	}
	
	private String form(RppSenaraiHitam r) {
		context.put("r",r);
		return getPath() + "/entry_page.vm";
	}
	
	@Override
	public void defaultButtonOption() {
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}
	
	@Command("batalSenaraiHitam")
	public String batalSenaraiHitam() throws Exception {
		
		RppSenaraiHitam sh = db.find(RppSenaraiHitam.class, getParam("idSenaraiHitam"));
		
		db.begin();
		db.remove(sh);
		db.commit();
		
		return form(sh);
	}
	
}

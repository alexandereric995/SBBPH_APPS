package bph.modules.kewangan.deposit.lejar.individu;

import java.util.List;

import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewResitSenaraiInvois;
import bph.entities.rpp.RppAduanKerosakan;
import bph.entities.rpp.RppAkaun;

public class LejarIndividuIR extends LejarIndividu{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/deposit/lejar/individu/rumahPeranginan"; }
	
	public void filtering(){
		//display yang dah terima pulangan deposit
		this.addFilter("jenisBayaran.id = '02' ");
		this.addFilter("kodHasil.id = '72311' and COALESCE(x.flagWarta,'T') <> 'Y' and COALESCE(x.flagBayar,'T') = 'Y'");
	}
	
	public void disabledButton(){
		this.setDisableKosongkanUpperButton(true);
		this.setReadonly(true);
		this.setDisableBackButton(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KewDeposit r) {
		String idLejar = r.getIdLejar();
		List<RppAduanKerosakan> listKerosakan = null;
		
		if(idLejar.equals(null) || idLejar.length() >0){
			RppAkaun ak = db.find(RppAkaun.class, idLejar);
			if(ak !=null){
				listKerosakan = db.list("select x from RppAduanKerosakan x where x.permohonan.id = '"+ak.getPermohonan().getId()+"' ");
				
				if(listKerosakan != null){
					listKerosakan = listKerosakan;
				}
			}
		}
		
		context.put("listKerosakan",listKerosakan);
		
		//ADD BY PEJE - GET ID_RESIT FOR DEPOSIT
		context.remove("idResit");
		KewResitSenaraiInvois rsi = (KewResitSenaraiInvois) db.get("select x from KewResitSenaraiInvois x where x.deposit.id = '" + r.getId() + "'");
		if (rsi != null) {
			if (rsi.getResit() != null) {
				context.put("idResit", rsi.getResit().getId());
			}
		}
	}
	/*
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pendeposit.userName", getParam("findUserName"));
		map.put("pendeposit.noKP", getParam("findNoKP"));
		map.put("flagPulangDeposit", getParam("findFlagPulangDeposit"));
		return map;
	}*/

}

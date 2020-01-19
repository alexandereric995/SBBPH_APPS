package bph.modules.kewangan.subsidiari.agihan;

import bph.entities.kewangan.KewSubsidiari;
import bph.entities.rpp.RppPermohonan;

public class AgihanSubsidiariIR extends AgihanSubsidiari{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/agihan/ir"; }
	
	
	
	public void filtering(){
		this.addFilter("jenisSubsidiari.id = '02' ");
		//this.addFilter("status.id IN ('1436510785697') ");
	}
	
	@Override
	public void getRelatedData(KewSubsidiari r) {
		RppPermohonan p = db.find(RppPermohonan.class, r.getIdFail());
		if(r!=null){
			context.put("permohonan", p);
		}
	}

}

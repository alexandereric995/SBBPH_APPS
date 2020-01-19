package bph.modules.kewangan.subsidiari.pelarasan;

import java.util.List;

import bph.entities.kewangan.KewInvois;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.rpp.RppPermohonan;

public class PelarasanSubsidiariIR extends PelarasanSubsidiari{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/pelarasan/rumahPeranginan"; }
	
	public void filtering(){
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if(userRole.equalsIgnoreCase("(SUBSIDIARI) Penyedia")){
			this.addFilter("status.id IN ('1436510785718','1436510785725','1436510785729') ");
			this.addFilter("agihan.penyedia.id = '"+userId+"' ");
		}else if(userRole.equalsIgnoreCase("(SUBSIDIARI) Penyemak")){
			this.addFilter("status.id IN ('1438023402951','1436510785721','1436510785725') "); 
		}else if(userRole.equalsIgnoreCase("(SUBSIDIARI) Pelulus")){
			this.addFilter("status.id IN ('1438023402975','1436510785725') ");
		}
				
		this.addFilter("jenisSubsidiari.id = '02' ");
	}

	@Override
	public void getRelatedData(KewSubsidiari r) {
		
		context.put("listInvois",getListInvois(r));
		
		RppPermohonan p = db.find(RppPermohonan.class, r.getIdFail());
		if(r!=null){
			context.put("permohonan", p);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<KewInvois> getListInvois(KewSubsidiari r) {
		RppPermohonan permohonan = (RppPermohonan) db.get("select x from RppPermohonan x where x.id = '"+r.getIdFail()+"'");
		List<KewInvois> listInvois = 
				db.list("select x from KewInvois x where x.pembayar.id = '"+r.getPemohon().getId()+"' "+
						" and x.jenisBayaran.id = '"+r.getJenisSubsidiari().getId()+"' and x.noRujukan = '" +permohonan.getNoTempahan()+ "'");
		return listInvois;
	}
}

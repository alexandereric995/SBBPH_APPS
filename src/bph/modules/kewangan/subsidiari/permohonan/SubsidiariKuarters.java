package bph.modules.kewangan.subsidiari.permohonan;

import java.util.Date;

import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewSubsidiari;
import bph.entities.kod.Status;

public class SubsidiariKuarters extends Subsidiari{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/permohonan/kuarters"; }
	
	@Override
	public void save(KewSubsidiari r) throws Exception {
		Users user = db.find(Users.class, userId);
		//r.setIdFail();
		r.setJenisSubsidiari(db.find(KewJenisBayaran.class, "01"));
		r.setJustifikasiPemohon(getParam("justifikasiPemohon"));
		r.setPemohon(user);
		r.setStatus(db.find(Status.class, "1436510785697")); //PERMOHONAN SUBSIDIARI
		r.setTarikhPermohonan(new Date());
		r.setFlagSijilAkuanMasuk(getParam("flagSijilAkuanMasuk"));
		r.setFlagSijilAkuanKeluar(getParam("flagSijilAkuanKeluar"));
	}
	
	public void filtering(){
		this.addFilter("jenisSubsidiari.id = '01' ");
	}	
	
}

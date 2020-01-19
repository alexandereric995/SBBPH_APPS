package bph.modules.kewangan.deposit.permohonan;

import java.util.Date;

import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kew.KewTuntutanDeposit;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kod.Status;

/**
 * Tuntutan deposit dari penghuni tidak melebihi 6 bulan keluar rumah
 * */

public class TuntutanDepositKuarters extends TuntutanDeposit{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/permohonan/kuarters"; }
	
	public void filtering(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		this.addFilter("jenisBayaran.id = '01' ");
		this.addFilter("pendeposit.id = '"+userId+"' ");
		this.addFilter("flagWarta = 'T' ");
	}

	@Override
	public void afterSave(KewDeposit r) { 
		
		Users user = db.find(Users.class, userId);
		
		KewTuntutanDeposit td = r.getTuntutanDeposit();
		if(td == null){
			td = new KewTuntutanDeposit();
		}
		
		db.begin();
		td.setDeposit(r);
		td.setJenisTuntutan(db.find(KewJenisBayaran.class, "01"));
		td.setPenuntut(user);
		td.setTarikhPermohonan(new Date());
		td.setResitBayaranDeposit(getParam("resitBayaranDeposit"));
		td.setSalinanAkaunBank(getParam("salinanAkaunBank"));
		td.setSijilAkaunKeluar(getParam("sijilAkaunKeluar"));
		td.setSijilAkaunMasuk(getParam("sijilAkaunMasuk"));
		td.setStatus(db.find(Status.class, "1436464445665")); //status PERMOHONAN DEPOSIT
		db.persist(td);
		
		r.setTuntutanDeposit(td);
		
		try {
			db.commit();
		} catch (Exception e) {
			System.out.println("error afterSave : "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
}

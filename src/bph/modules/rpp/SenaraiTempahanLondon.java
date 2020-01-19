package bph.modules.rpp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class SenaraiTempahanLondon extends LebahRecordTemplateModule<RppRekodTempahanLondon> {

	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() { return String.class; }

	@Override
	public Class<RppRekodTempahanLondon> getPersistenceClass() { return RppRekodTempahanLondon.class; }

	@Override
	public String getPath() { return "bph/modules/rpp/senaraiTempahanLondon"; }

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		
		try {
			mp = new MyPersistence();
			
			dataUtil = DataUtil.getInstance(db);
			
			userId = (String) request.getSession().getAttribute("_portal_login");
			userName = (String) request.getSession().getAttribute("_portal_username");
			userRole = (String) request.getSession().getAttribute("_portal_role");
			
			String listPeranginan = "";
			
			List<RppPenyeliaPeranginan> listPenyelia = mp.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + userId + "'");
			for (int i = 0; i < listPenyelia.size(); i ++) {
				if ("".equals(listPeranginan)) {
					listPeranginan = "'" + listPenyelia.get(i).getPeranginan().getId() + "'";
				} else {
					listPeranginan = listPeranginan + "," + "'" + listPenyelia.get(i).getPeranginan().getId() + "'";
				}
			}
			
			if ("".equals(listPeranginan)) {
				this.addFilter("jenisUnitRpp.peranginan.id = '" + listPeranginan + "'");
			} else {
				this.addFilter("jenisUnitRpp.peranginan.id in (" + listPeranginan + ")");
			}
			
			context.put("listJenisUnitLondon", dataUtil.getListJenisUnitLondon());
			
			//hanya permohonan baru sahaja. yang dah lulus berada dlm list lain.
			this.addFilter("flagKelulusanPmo is null");
			this.addFilter("status.id = '1425259713412' ");
			this.addFilter("jenisUnitRpp.peranginan.id not in ('1','2','3','4','5','6','7','8','9','10','14','15','16','17','18','19','20','21','22','25','1439720205023')");
			
			this.setOrderBy("tarikhDaftarRekod");
			this.setOrderType("asc");
			
			defaultButtonOption();	
			
		} catch (Exception e) {
			System.out.println("Error begin : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}//close begin
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	@Override
	public void save(RppRekodTempahanLondon r) throws Exception { }

	@Override
	public boolean delete(RppRekodTempahanLondon r) throws Exception { return false; }

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pemohon.userName", getParam("findNama"));
		map.put("pemohon.noKP", getParam("findNoPengenalan"));		
		map.put("jenisUnitRpp.id", new OperatorEqualTo(getParam("findJenisRpp")));
		map.put("tarikhMasukRpp", new OperatorDateBetween(
				getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(
				getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		return map;
	}

	@Override
	public void beforeSave() { }

	@Override
	public void afterSave(RppRekodTempahanLondon r) { }

	@Override
	public void getRelatedData(RppRekodTempahanLondon r) {
		try{
			mp = new MyPersistence();
			RppRekodTempahanLondon rr = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, r.getId());
			context.put("r", rr);
		}finally{
			if (mp != null) { mp.close(); }
		}
	}	
	
	
	@Command("lulusPermohonan")
	public String lulusPermohonan() throws Exception {
		
		String flag = getParam("flag");
		String id = getParam("id");
		
		try {
			mp = new MyPersistence();
			
			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, id);
			
			if(r != null){
				
				mp.begin();
				r.setFlagKelulusanPmo("Y");
				mp.commit();
				
				/**EMEL KEPADA UNIT IR*/
				//UtilRpp.emailtoPemohon(r,"TIDAK");
				
			}
			
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error[Operator] lulusPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		String vm = "";
		if(flag.equalsIgnoreCase("LIST")){
			vm = getPath() + "/refresh.vm";
		}else{
			vm = templateDir + "/entry_fields.vm";
		}
		return vm;
		
	}
	
	@Command("tidakLulusPermohonan")
	public String tidakLulusPermohonan() throws Exception {
		
		String flag = getParam("flag");
		String id = getParam("id");
		
		try {
			mp = new MyPersistence();
			
			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, id);
			
			if(r != null){
				
				mp.begin();
				r.setFlagKelulusanPmo("T");
				mp.commit();
				
				/**EMEL KEPADA UNIT IR*/
				//UtilRpp.emailtoPemohon(r,"TIDAK");
				
			}
			
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error[Operator] tidakLulusPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		String vm = "";
		if(flag.equalsIgnoreCase("LIST")){
			vm = getPath() + "/refresh.vm";
		}else{
			vm = templateDir + "/entry_fields.vm";
		}
		return vm;
		
	}
	
//	@Command("lulusPermohonan")
//	public String lulusPermohonan() throws Exception {
//		
//		String pelulusId = (String) request.getSession().getAttribute("_portal_login");
//		String flag = getParam("flag");
//		String id = getParam("id");
//		
//		try {
//			mp = new MyPersistence();
//			
//			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, id);
//			
//			if(r != null){
//				
//				mp.begin();
//				
//				r.setStatus((Status) mp.find(Status.class, "1430809277102")); //status permohonan lulus
//				
//				String keterangan = ("SEWA "+r.getTotalBilMalam()+" MALAM DI  "+r.getJenisUnitRpp().getKeterangan()+", "+r.getJenisUnitRpp().getPeranginan().getNamaPeranginan().toUpperCase());
//				
//				RppAkaun ak = new RppAkaun();
//				ak.setRekodTempahanLondon(r); //utk london shj
//				ak.setAmaunBayaranSeunit(r.getJenisUnitRpp().getKadarSewa());
//				ak.setDebit(r.getDebit());
//				ak.setKredit(0d);
//				ak.setFlagBayar("T");
//				ak.setFlagVoid("T");
//				ak.setKeterangan(keterangan);
//				ak.setKodHasil((KodHasil) mp.find(KodHasil.class, "74299"));
//				ak.setNoInvois(r.getNoTempahan());
//				ak.setTarikhInvois(new Date());
//				ak.setIdMasuk((Users) mp.find(Users.class, pelulusId));
//				ak.setTarikhMasuk(new Date());
//				mp.persist(ak);
//
//				String notempahan = ak.getRekodTempahanLondon().getNoTempahan()!=null?ak.getRekodTempahanLondon().getNoTempahan():null;
//				
//				KewInvois inv = new KewInvois();
//				inv.setDebit(ak.getDebit());
//				inv.setFlagBayaran("SEWA");
//				inv.setFlagQueue("T");
//				inv.setIdLejar(ak.getId());
//				inv.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class,"12")); // 12 - LONDON
//				inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
//				inv.setKodHasil(ak.getKodHasil());
//				inv.setNoInvois(ak.getNoInvois());
//				inv.setNoRujukan(notempahan);
//				inv.setPembayar(ak.getRekodTempahanLondon().getPemohon());
//				inv.setTarikhInvois(ak.getTarikhInvois());
//				inv.setUserPendaftar((Users) mp.find(Users.class, pelulusId));
//				inv.setTarikhDaftar(new Date());
//				inv.setTarikhDari(r.getTarikhMasukRpp());
//				inv.setTarikhHingga(r.getTarikhKeluarRpp());
//				mp.persist(inv);
//				
//				/**EMEL KEPADA PEMOHON (LULUS) */
//				UtilRpp.emailtoPemohon(r,"LULUS");
//				
//				mp.commit();
//			}
//			
//			context.put("r", r);
//			
//		} catch (Exception e) {
//			System.out.println("Error[Operator] lulusPermohonan : "+e.getMessage());
//		}finally{
//			if (mp != null) { mp.close(); }
//		}
//		
//		String vm = "";
//		if(flag.equalsIgnoreCase("LIST")){
//			vm = getPath() + "/refresh.vm";
//		}else{
//			vm = templateDir + "/entry_fields.vm";
//		}
//		return vm;
//		
//	}
	
	
//	@Command("tidakLulusPermohonan")
//	public String tidakLulusPermohonan() throws Exception {
//		
//		String flag = getParam("flag");
//		
//		try {
//			mp = new MyPersistence();
//
//			String id = getParam("id");
//			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, id);
//			
//			if(r != null){
//				
//				mp.begin();
//				
//				r.setStatus((Status) mp.find(Status.class, "1430809277099")); //status permohonan tidak lulus
//				
//				/**EMEL KEPADA PEMOHON (TIDAK LULUS) */
//				UtilRpp.emailtoPemohon(r,"TIDAK");
//				
//				mp.commit();
//			}
//			
//			context.put("r", r);
//			
//		} catch (Exception e) {
//			System.out.println("Error[Operator] tidakLulusPermohonan : "+e.getMessage());
//		}finally{
//			if (mp != null) { mp.close(); }
//		}
//		
//		String vm = "";
//		if(flag.equalsIgnoreCase("LIST")){
//			vm = getPath() + "/refresh.vm";
//		}else{
//			vm = templateDir + "/entry_fields.vm";
//		}
//		return vm;
//		
//	}
	
}

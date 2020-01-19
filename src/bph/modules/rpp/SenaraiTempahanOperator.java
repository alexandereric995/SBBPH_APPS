package bph.modules.rpp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.utils.DataUtil;
import bph.utils.UtilRpp;

public class SenaraiTempahanOperator extends LebahRecordTemplateModule<RppPermohonan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<RppPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return RppPermohonan.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/rppPermohonanOperator";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		context.remove("statusInfo");

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String listPeranginan = "";
		
		List<RppPenyeliaPeranginan> listPenyelia = db.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '" + userId + "'");
		for (int i = 0; i < listPenyelia.size(); i ++) {
			if ("".equals(listPeranginan)) {
				listPeranginan = "'" + listPenyelia.get(i).getPeranginan().getId() + "'";
			} else {
				listPeranginan = listPeranginan + "," + "'" + listPenyelia.get(i).getPeranginan().getId() + "'";
			}
		}
		
		if ("".equals(listPeranginan)) {
			this.addFilter("rppPeranginan.id = '" + listPeranginan + "'");
		} else {
			this.addFilter("rppPeranginan.id in (" + listPeranginan + ")");
		}
		
		context.put("listRppByOperator", dataUtil.getListRppByOperator(listPeranginan));
		
		//hanya pd & langkawi shj
		this.addFilter("rppPeranginan.id not in ('1','2','4','5','6','7','8','9','10','11','12','13','15','16','17','18','19','20','21','22','25','1439720205023')");
		
		this.addFilter("statusBayaran = 'Y'");
		this.addFilter("status.id not in ('1425259713421','1433083787409','1435512646303')");
		
		this.setOrderBy("tarikhMasukRpp");
		this.setOrderType("asc");
		
		defaultButtonOption();		
	}
	
	private void defaultButtonOption() {
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
	}

	@Override
	public void save(RppPermohonan r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(RppPermohonan r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noTempahan", getParam("findNoTempahan"));	
		map.put("pemohon.userName", getParam("findNama"));
		map.put("pemohon.noKP", getParam("findNoPengenalan"));		
		map.put("tarikhPermohonan", new OperatorDateBetween(getDate("findTarikhMohonMula"), getDate("findTarikhMohonHingga")));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukMula"), getDate("findTarikhMasukHingga")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhKeluarMula"), getDate("findTarikhKeluarMula")));
		
		map.put("rppPeranginan.id", new OperatorEqualTo(getParam("findRpp")));
		
		return map;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(RppPermohonan r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRelatedData(RppPermohonan r) {
		// TODO Auto-generated method stub
		
	}	
	
	@Command("tolakPembayaran")
	public String tolakPembayaran() throws Exception {
		
		RppPermohonan r = db.find(RppPermohonan.class, get("idPermohonan"));
		
		if(r != null){
			try {
				db.begin();
				
				r.setStatusBayaran("T");
				r.setTarikhBayaran(null);
				r.setStatus(db.find(Status.class, "1425259713412")); //revert to permohonan baru
				r.setPhotofilename(null);
				r.setThumbfilename(null);
				r.setCatatanPulangTempahan("MAKLUMAT BAYARAN TEMPAHAN TIDAK SAH. SILA MUAT NAIK SEMULA RESIT PEMBAYARAN.");
				
				List<RppAkaun> listAkaun = UtilRpp.getListTempahanDanBayaran(db, r);
				for(int i=0;i<listAkaun.size();i++){
					listAkaun.get(i).setKredit(0d);
					listAkaun.get(i).setTarikhTransaksi(null);
					listAkaun.get(i).setFlagBayar("T");
				}
				
				db.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
		
		context.put("statusInfo", "MAKLUMAT PEMBAYARAN TELAH DITOLAK.");
		return getPath() + "/status.vm";
	}
	
	@Command("checkedIn")
	public String checkedIn() throws Exception {
		String id = getParam("id");
		RppPermohonan r = db.find(RppPermohonan.class, id);
		Status status = db.find(Status.class, "1425259713421"); //DAFTAR MASUK
		if( r != null){
			db.begin();
			r.setStatus(status);
			db.commit();
		}
		return getPath() + "/refresh.vm";
	}
	
	@Command("noShow")
	public String noShow() throws Exception {
		String id = getParam("id");
		RppPermohonan r = db.find(RppPermohonan.class, id);
		Status status = db.find(Status.class, "1433083787409"); //TIDAK HADIR
		if( r != null){
			db.begin();
			r.setStatus(status);
			db.commit();
		}
		return getPath() + "/refresh.vm";
	}
	
	@Command("returnTempahan")
	public String returnTempahan() throws Exception {
		
		String id = getParam("id");
		RppPermohonan r = db.find(RppPermohonan.class, id);
		
		if(r != null){
			try {
				db.begin();
				
				r.setStatusBayaran("T");
				r.setTarikhBayaran(null);
				r.setStatus(db.find(Status.class, "1425259713412")); //revert to permohonan baru
				r.setPhotofilename(null);
				r.setThumbfilename(null);
				r.setCatatanPulangTempahan("MAKLUMAT BAYARAN TEMPAHAN TIDAK SAH. SILA MUAT NAIK SEMULA RESIT PEMBAYARAN.");
				
				List<RppAkaun> listAkaun = UtilRpp.getListTempahanDanBayaran(db, r);
				for(int i=0;i<listAkaun.size();i++){
					listAkaun.get(i).setKredit(0d);
					listAkaun.get(i).setTarikhTransaksi(null);
					listAkaun.get(i).setFlagBayar("T");
				}
				
				db.commit();
			} catch (Exception e) {
				System.out.println("error saving returnTempahan : "+e.getMessage());
				e.printStackTrace();
			}
		}
		return getPath() + "/refresh.vm";
	}
	
}

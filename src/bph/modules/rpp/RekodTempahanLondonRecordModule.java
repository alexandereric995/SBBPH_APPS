package bph.modules.rpp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.KodHasil;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.integrasi.fpx.FPXPkiImplementation;
import bph.integrasi.fpx.FPXUtil;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class RekodTempahanLondonRecordModule extends LebahRecordTemplateModule<RppRekodTempahanLondon> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {return String.class;}

	@SuppressWarnings("unchecked")
	@Override
	public void afterSave(RppRekodTempahanLondon r) {
		if(r.getNoTempahan() == null){
			String kodLokasi = r.getJenisUnitRpp().getPeranginan().getKodLokasi()!=null?r.getJenisUnitRpp().getPeranginan().getKodLokasi():"00";
			try {
				mp = new MyPersistence();
				
				mp.begin();
				
				r.setNoTempahan(UtilRpp.generateNoTempahanIndividuSQL(kodLokasi, null));
				
				/**Notifikasi kepada HQ Penyemak dan Pelulus*/
				List<Role> roles = mp.list("select x from Role x where x.name in ('(RPP) Penyemak','(RPP) Pelulus') ");
				UtilRpp.saveNotifikasi(mp,roles,r.getId(),"Y",getClass().getName(),"TEMPAHAN_LONDON_BARU");
				
				/**EMEL KEPADA PMO*/
				UtilRpp.emailtoPmo(r);
				
				mp.commit();
				
			} catch (Exception e) {
				System.out.println("Error afterSave "+e.getMessage());
			}finally{
				if (mp != null) { mp.close(); }
			}
		}
	}

	@Override
	public void beforeSave() {}

	@Override
	public String getPath() {return "bph/modules/rpp/rekodTempahanLondon";}

	@Override
	public Class<RppRekodTempahanLondon> getPersistenceClass() {return RppRekodTempahanLondon.class;}
	
	@Override
	public void begin() {
		
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		defaultButtonOption();
		filtering(userId,userRole);
		
		context.remove("qualifyLondon");
		if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak") && !userRole.equalsIgnoreCase("(RPP) Pelulus")){
			checkingKelayakanRpLondon(userId, null);
		} else {
			context.put("qualifyLondon", true);
			this.setDisabledInfoNextTab(false);
		}
		
		context.put("userRole", userRole);
		context.put("listPeranginan", dataUtil.getListPeranginanRppLondon());
		context.put("listPeranginanLondon", dataUtil.getListPeranginanRppLondon());
		context.put("listStatus", dataUtil.getListStatusLondon());
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
		
		context.remove("objUser");
		
		this.setOrderBy("tarikhDaftarRekod desc");
	}

	public void checkingKelayakanRpLondon(String userId, String idPeranginan) {
		boolean qualifyLondon = false;
		this.setDisabledInfoNextTab(true);
		
		try {
			mp = new MyPersistence();
			
			Users user = (Users) mp.find(Users.class, userId);
			if (user != null) {
				GredPerkhidmatan gredPerkhidmatan = user.getGredPerkhidmatan();
				if (gredPerkhidmatan != null) {
					int jawatan = Integer.parseInt(gredPerkhidmatan.getId());
					if (idPeranginan != null) {
						JenisUnitRPP jenisUnitRPP = (JenisUnitRPP) mp.get("select x from JenisUnitRPP x where x.peranginan.id = '" + idPeranginan + "' order by x.gredMinimumKelayakan asc");
						if (jenisUnitRPP != null) {
							if (jawatan >= jenisUnitRPP.getGredMinimumKelayakan()) {
								qualifyLondon = true;
								this.setDisabledInfoNextTab(false);
							}
						}
					} else {
						JenisUnitRPP jenisUnitRPP = (JenisUnitRPP) mp.get("select x from JenisUnitRPP x where x.peranginan.id in (11,12,13) order by x.gredMinimumKelayakan asc");
						if (jenisUnitRPP != null) {
							if (jawatan >= jenisUnitRPP.getGredMinimumKelayakan()) {
								qualifyLondon = true;
								this.setDisabledInfoNextTab(false);
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error checkingKelayakanRpLondon : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		if (qualifyLondon) {
			context.put("qualifyLondon", qualifyLondon);
		}	
	}
	
	private void filtering(String userId, String userRole) {

		if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak")
			&& !userRole.equalsIgnoreCase("(RPP) Pelulus") && !userRole.equalsIgnoreCase("(RPP) Penyelia")){
			this.addFilter("pemohon.id = '"+userId+"' ");
		}
		this.setOrderBy("tarikhDaftarRekod desc");
	}
	
	private void defaultButtonOption() {
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("noKp", getParam("findNoKp"));
		map.put("namaPemohon", getParam("findUserName"));
		map.put("jenisUnitRpp.peranginan.id", new OperatorEqualTo(getParam("findRpp")));
		map.put("status.id", getParam("findStatus"));
		map.put("flagBayar", getParam("findStatusBayaran"));
		map.put("flagKelulusanPmo", getParam("findStatusLulusPmo"));
		map.put("tarikhMasukRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		map.put("tarikhKeluarRpp", new OperatorDateBetween(getDate("findTarikhMasukRpp"), getDate("findTarikhKeluarRpp")));
		return map;
	}

	@Override
	public boolean delete(RppRekodTempahanLondon r) throws Exception {
		boolean del = true;
		if(r.getStatus().getId().equalsIgnoreCase("1430809277102") || r.getStatus().getId().equalsIgnoreCase("1430809277099") || r.getStatus().getId().equalsIgnoreCase("1435093978588")  ){ //permohonan lulus / tak / batal
			del = false;
		}
		return del;
	}

	@Override
	public void getRelatedData(RppRekodTempahanLondon r) {
		
		String userId = (String) request.getSession().getAttribute("_portal_login");
		String userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			
			RppRekodTempahanLondon rr = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, r.getId());
			context.put("r", rr);
			
			/**Read notification*/
			mp.begin();
			UtilRpp.readNotification(mp,r.getId(),userRole,userId,"TEMPAHAN_LONDON_BARU");
			mp.commit();
			
			context.put("listJenisUnitRpp", dataUtil.getListJenisUnitByPeranginan(r.getJenisUnitRpp().getPeranginan().getId()));
			
		} catch (Exception e) {
			System.out.println("Error getRelatedData : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}
	
	@Override
	public void save(RppRekodTempahanLondon r) throws Exception {
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idJenisUnit = getParam("jenisUnit");
		
		try {
			mp = new MyPersistence();
			
			JenisUnitRPP objJenisUnit = (JenisUnitRPP) mp.find(JenisUnitRPP.class, idJenisUnit);
			Double kadarHarga = objJenisUnit.getKadarSewa()!=null?objJenisUnit.getKadarSewa():0d;
			
			Date tarikhMasukRpp = getDate("tarikhMasukRpp");
			Date tarikhKeluarRpp = getDate("tarikhKeluarRpp");
			Integer days = 1;
			if(tarikhKeluarRpp != null && tarikhMasukRpp != null){
				days = (int)( (tarikhKeluarRpp.getTime() - tarikhMasukRpp.getTime()) / (1000 * 60 * 60 * 24) );
			}
			
			Double totalSewa = (kadarHarga * days);
			
			/**Hanya hq sahaja yang boleh registerkan pemohon lain*/
			Users pemohon = null;
			if( userRole.equalsIgnoreCase("(RPP) Penyedia") || userRole.equalsIgnoreCase("(RPP) Penyemak") || userRole.equalsIgnoreCase("(RPP) Pelulus")){
				String carianNokp = getParam("nokp");
				pemohon = (Users) mp.find(Users.class, carianNokp);
				
				if( pemohon==null ){
					r.setNamaPemohon(getParam("namaPemohon"));
					r.setNoKp(carianNokp);
					r.setNoTelefonBimbit(getParam("noTelefonBimbit"));
					r.setNoTelefonPejabat(getParam("noTelefonPejabat"));
					r.setNoFaks(getParam("noFaks"));
					r.setEmel(getParam("emel"));
					r.setJawatanGred(getParam("jawatanGred"));
					r.setKementerianJabatan(getParam("kementerianJabatan"));
					r.setAlamatPejabat1(getParam("alamatPejabat1"));
					r.setAlamatPejabat2(getParam("alamatPejabat2"));
					r.setAlamatPejabat3(getParam("alamatPejabat3"));
				}else{
					r.setNamaPemohon(pemohon.getUserName());
					r.setNoKp(pemohon.getNoKP());
				}
				
				r.setCatatanHq(getParam("catatanHq"));
				
			}else{
				pemohon = (Users) mp.find(Users.class, userId);
				r.setNamaPemohon(pemohon.getUserName());
				r.setNoKp(pemohon.getNoKP());
			}
			
			r.setPemohon(pemohon);
			r.setJenisUnitRpp(objJenisUnit);
			r.setTarikhMasukRpp(tarikhMasukRpp);
			r.setTarikhKeluarRpp(tarikhKeluarRpp);
			r.setTarikhDaftarRekod(new Date());
			
			r.setDebit(totalSewa);
			r.setKredit(0d);
			r.setFlagBayar("T");
			
			//status 1425259713412 (permohonan baru)
			if( r.getStatus() == null ){
				r.setStatus((Status) mp.find(Status.class, "1425259713412"));
			}
			
		} catch (Exception e) {
			System.out.println("Error save : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
	}//close save

	@Command("filterJenisUnit")
	public String filterJenisUnit() throws Exception {
		String idPeranginan = getParam("peranginan");
		context.put("listJenisUnitRpp", dataUtil.getListJenisUnitByPeranginan(idPeranginan));
		
		context.remove("qualifyLondon");
		if(!userRole.equalsIgnoreCase("(RPP) Penyedia") && !userRole.equalsIgnoreCase("(RPP) Penyemak") && !userRole.equalsIgnoreCase("(RPP) Pelulus")){
			checkingKelayakanRpLondon(userId, idPeranginan);
		} else {
			context.put("qualifyLondon", true);
			this.setDisabledInfoNextTab(false);
		}
		
		return getPath() + "/divJenisUnit.vm";
	}
	
	@Command("checkDataPemohon")
	public String checkDataPemohon() throws Exception {
		String carianNokp = getParam("nokp");
		try {
			mp = new MyPersistence();
			Users objUser = (Users) mp.find(Users.class, carianNokp);
			context.put("objUser", objUser);
		} catch (Exception e) {
			System.out.println("Error checkDataPemohon : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/detailPemohon.vm";
	}
	
	@Command("luluskanPermohonan")
	public String luluskanPermohonan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();
			
			String idrekod = getParam("idrekod");
			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idrekod);
			
			/** BILA LULUS SAHAJA CREATE NO TEMPAHAN DAN KEW BAYARAN */
			if(r!=null){
				
				mp.begin();
				
				r.setStatus((Status) mp.find(Status.class, "1430809277102")); //status permohonan lulus
				
				RppAkaun ak = new RppAkaun();
				//ak.setPermohonan(r);
				ak.setRekodTempahanLondon(r); //utk london shj
				ak.setAmaunBayaranSeunit(r.getJenisUnitRpp().getKadarSewa());
				ak.setDebit(r.getDebit());
				ak.setKredit(0d);
				ak.setFlagBayar("T");
				ak.setFlagVoid("T");
				ak.setKeterangan("SEWA "+r.getTotalBilMalam()+" MALAM DI "+r.getJenisUnitRpp().getPeranginan().getNamaPeranginan().toUpperCase());
				ak.setKodHasil((KodHasil) mp.find(KodHasil.class, "74299"));
				ak.setNoInvois(r.getNoTempahan());
				ak.setTarikhInvois(new Date());
				ak.setIdMasuk((Users) mp.find(Users.class, userId));
				ak.setTarikhMasuk(new Date());
				mp.persist(ak);

				String notempahan = ak.getRekodTempahanLondon().getNoTempahan()!=null?ak.getRekodTempahanLondon().getNoTempahan():null;
				
				KewInvois inv = new KewInvois();
				inv.setDebit(ak.getDebit());
				inv.setFlagBayaran("SEWA");
				inv.setFlagQueue("T");
				inv.setIdLejar(ak.getId());
				inv.setJenisBayaran((KewJenisBayaran) mp.find(KewJenisBayaran.class,"12")); // 12 - LONDON
				inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
				inv.setKodHasil(ak.getKodHasil());
				inv.setNoInvois(ak.getNoInvois());
				inv.setNoRujukan(notempahan);
				inv.setPembayar(ak.getRekodTempahanLondon().getPemohon());
				inv.setTarikhInvois(ak.getTarikhInvois());
				inv.setUserPendaftar((Users) mp.find(Users.class, userId));
				inv.setTarikhDaftar(new Date());
				inv.setTarikhDari(r.getTarikhMasukRpp());
				inv.setTarikhHingga(r.getTarikhKeluarRpp());
				mp.persist(inv);
				
				mp.commit();
				
				/**EMEL KEPADA PEMOHON (LULUS) */
				UtilRpp.emailtoPemohon(r,"LULUS");
				
			}
			
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error luluskanPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return templateDir + "/entry_fields.vm";
	}
	
	
	@Command("tidakLulusPermohonan")
	public String tidakLulusPermohonan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();

			String idrekod = getParam("idrekod");
			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idrekod);
			
			if(r != null){
				
				mp.begin();
				
				r.setStatus((Status) mp.find(Status.class, "1430809277099")); //status permohonan tidak lulus
				
				/**EMEL KEPADA PEMOHON (TIDAK LULUS) */
				UtilRpp.emailtoPemohon(r,"TIDAK");
				
				mp.commit();
			}
			
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error tidakLulusPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return templateDir + "/entry_fields.vm";
	}
	
	
	/**
	 * ONLINE PAYMENT
	 * 
	 * */
	@Command("paparPilihan")
	public String paparPilihan() throws Exception {
		
		String idrekod = getParam("idrekod");
		
		try {
			mp = new MyPersistence();
			RppRekodTempahanLondon ldn = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idrekod);
			
			String serverName = request.getServerName();
			String contextPath = request.getContextPath();
			int serverPort = request.getServerPort();
			String server = serverPort != 80 ? serverName + ":" + serverPort : serverName;
			String image_url = "http://" + server + contextPath;
			context.put("imageUrl", image_url);

			String fpx_checkSum = "";
			String final_checkSum = "";
			String fpx_msgType = "AR";
			String fpx_msgToken = "01";
			String fpx_sellerExId = "EX00000345";
			String fpx_sellerExOrderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fpx_sellerTxnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fpx_sellerOrderNo = ldn.getId();
			String fpx_sellerId = "SE00000392";
			String fpx_sellerBankCode = "01";
			String fpx_txnCurrency = "MYR";

			String fpx_txnAmount = Double.toString(ldn.getDebit());
			String fpx_buyerEmail = "";
			String fpx_buyerName = "";
			String fpx_buyerBankId = "";
			String fpx_buyerBankBranch = "";
			String fpx_buyerAccNo = "";
			String fpx_buyerId = "";
			String fpx_makerName = "";
			String fpx_buyerIban = "";
			String fpx_productDesc = "Pembayaran Tempahan London";
			String fpx_version = "5.0";

			fpx_checkSum = fpx_buyerAccNo + "|" + fpx_buyerBankBranch + "|"
			+ fpx_buyerBankId + "|" + fpx_buyerEmail + "|" + fpx_buyerIban
			+ "|" + fpx_buyerId + "|" + fpx_buyerName + "|";
			fpx_checkSum += fpx_makerName + "|" + fpx_msgToken + "|" + fpx_msgType
			+ "|" + fpx_productDesc + "|" + fpx_sellerBankCode + "|"
			+ fpx_sellerExId + "|";
			fpx_checkSum += fpx_sellerExOrderNo + "|" + fpx_sellerId + "|"
			+ fpx_sellerOrderNo + "|" + fpx_sellerTxnTime + "|"
			+ fpx_txnAmount + "|" + fpx_txnCurrency + "|" + fpx_version;

			final_checkSum = FPXPkiImplementation.signData(
			"D:\\SMIExchange\\bph.gov.my.key", fpx_checkSum,
			"SHA1withRSA");

			context.put("fpx_msgType", fpx_msgType);
			context.put("fpx_msgToken", fpx_msgToken);
			context.put("fpx_sellerExId", fpx_sellerExId);
			context.put("fpx_sellerExOrderNo", fpx_sellerExOrderNo);
			context.put("fpx_sellerTxnTime", fpx_sellerTxnTime);
			context.put("fpx_sellerOrderNo", fpx_sellerOrderNo);
			context.put("fpx_sellerId", fpx_sellerId);
			context.put("fpx_sellerBankCode", fpx_sellerBankCode);
			context.put("fpx_txnCurrency", fpx_txnCurrency);
			context.put("fpx_txnAmount", fpx_txnAmount);
			context.put("fpx_buyerEmail", fpx_buyerEmail);
			context.put("fpx_buyerName", fpx_buyerName);
			context.put("fpx_buyerBankId", fpx_buyerBankId);
			context.put("fpx_buyerBankBranch", fpx_buyerBankBranch);
			context.put("fpx_buyerAccNo", fpx_buyerAccNo);
			context.put("fpx_buyerId", fpx_buyerId);
			context.put("fpx_makerName", fpx_makerName);
			context.put("fpx_buyerIban", fpx_buyerIban);
			context.put("fpx_productDesc", fpx_productDesc);
			context.put("fpx_version", fpx_version);
			context.put("fpx_checkSum", final_checkSum);
			
			String noruj = ldn.getNoTempahan()!=null?ldn.getNoTempahan():"";

			HttpSession session = request.getSession();
			session.setAttribute("sesIdPermohonan", ldn.getId());
			session.setAttribute("sesModul", "LONDON");
			session.setAttribute("sesRole",(String) request.getSession().getAttribute("_portal_role") );
			session.setAttribute("returnlink","../sbbphv2/c/1425001362331?_portal_module=bph.modules.rpp.RekodTempahanLondonRecordModule");	
			
			FPXUtil fpxUtil = new FPXUtil(session) ;
			fpxUtil.registerFPXRequest(fpx_sellerId, fpx_sellerExId, fpx_sellerOrderNo, fpx_sellerExOrderNo, fpx_txnAmount, fpx_productDesc, "LONDON", mp);
			
		} catch (Exception e) {
			System.out.println("Error paparPilihan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}

		return "bph/modules/fpx/pilihan.vm";
	}
	
	@SuppressWarnings("unchecked")
	@Command("batalPermohonan")
	public String batalPermohonan() throws Exception {
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();

			String idrekod = getParam("idrekod");
			RppRekodTempahanLondon r = (RppRekodTempahanLondon) mp.find(RppRekodTempahanLondon.class, idrekod);
			
			if(r != null){
				
				List<RppAkaun> lk = mp.list("select x from RppAkaun x where x.rekodTempahanLondon.id = '"+r.getId()+"' ");
				
				mp.begin();
				
				r.setStatus((Status) mp.find(Status.class, "1435093978588")); //status tempahan dibatalkan
				
				if(lk != null){
					for(int i=0;i<lk.size();i++){
						RppAkaun lj = lk.get(i);
			
						if(lj.getKodHasil().getId().equalsIgnoreCase("74299")){
							//invois
							KewInvois inv = (KewInvois) mp.get("select x from KewInvois x where x.idLejar = '"+lj.getId()+"' ");
							if(inv!=null){
								mp.remove(inv);
							}
						}
						
						if(lj!=null){
							lj.setAmaunVoid(lj.getDebit());
							lj.setFlagVoid("Y");
							lj.setTarikhVoid(new Date());
						}
					}
				}
				
				/**EMEL KEPADA PEMOHON (BATAL TEMPAHAN) */
				UtilRpp.emailtoPemohon(r,"BATAL");
				
				mp.commit();
			}
			
			context.put("r", r);
			
		} catch (Exception e) {
			System.out.println("Error batalPermohonan : "+e.getMessage());
		}finally{
			if (mp != null) { mp.close(); }
		}
		
		return templateDir + "/entry_fields.vm";
	}

}




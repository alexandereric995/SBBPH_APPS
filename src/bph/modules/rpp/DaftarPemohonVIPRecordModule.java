package bph.modules.rpp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.util.PasswordService;

import org.apache.log4j.Logger;

import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import portal.module.entity.UsersSpouse;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.entities.integrasi.IntJPN;
import bph.entities.kod.Agama;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Bank;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.Jantina;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.KategoriPengguna;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class DaftarPemohonVIPRecordModule extends LebahRecordTemplateModule<Users> {

	/**
	 * 
	 */
	static Logger myLogger = Logger.getLogger("DaftarPemohonVIPRecordModule");
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}
	
	@Override
	public Class<Users> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Users.class;
	}
	
	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/rpp/daftarPemohonVIP";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		context.put("selectGelaran", dataUtil.getListGelaran());
		context.put("selectGredPerkhidmatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectJantina", dataUtil.getListJantina());
		context.put("selectBangsa", dataUtil.getListBangsa());
		context.put("selectAgama", dataUtil.getListAgama());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectBank", dataUtil.getListBank());
		
		context.remove("success");
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	private void addfilter() {
		this.addFilter("id not in ('faizal','anon','admin') ");
		this.addFilter("role.name = '(AWAM) Pegawai Tadbir' ");
		this.addFilter("flagDaftarSBBPH = 'Y'");
		this.setOrderBy("id");
		this.setOrderType("asc");		
	}

	private void doOverideFilterRecord() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		try {
			mp = new MyPersistence();
			
			Users pengguna = (Users) mp.find(Users.class, getParam("idPengguna"));
			if (pengguna != null) {
				if (pengguna.getFlagDaftarSBBPH().equals("T")) {
					mp.begin();
					mp.remove(pengguna);
					mp.commit();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		} 		
	}

	@Override
	public void save(Users r) throws Exception {
		IntJPN jpn = (IntJPN) db.get("select x FROM IntJPN  x where x.noPengenalan = '" + getParam("idPengguna") + "' order by x.tarikhTerima desc");

		r.setId(getParam("idPengguna"));
		r.setUserPassword(PasswordService.encrypt(getParam("idPengguna")));
		r.setGelaran(db.find(Gelaran.class, getParam("idGelaran")));
		r.setUserName(getParam("nama"));
		
		// JENIS PENGGUNA
		r.setJenisPengguna(db.find(KategoriPengguna.class, "07"));
		r.setRole(db.find(Role.class, "(AWAM) Pegawai Tadbir"));
		r.setFlagAktif("Y");
		r.setFlagMenungguPengesahan("T");
		r.setFlagUrusanPemohon(3);
		
		r.setCss(db.find(CSS.class, "BPH-Anon"));
		r.setUserLoginAlt("-");
		
		r.setUserAddress(getParam("alamat1"));
		r.setUserAddress2(getParam("alamat2"));
		r.setUserAddress3(getParam("alamat3"));
		r.setUserPostcode(getParam("poskod"));
		r.setUserBandar(db.find(Bandar.class, getParam("idBandar")));
		r.setDateRegistered(new Date());
		
		r.setJenisPengenalan(db.find(JenisPengenalan.class, "B"));
		r.setNoKP(getParam("idPengguna"));
		if (jpn != null) {
			r.setNoKP2(jpn.getNoPengenalanLama());
		}
		r.setNoTelefon(getParam("noTelefon"));
		r.setNoTelefonBimbit(getParam("noTelefonBimbit"));
		r.setEmel(getParam("emel"));
		
		r.setGredPerkhidmatan(db.find(GredPerkhidmatan.class, getParam("idGredPerkhidmatan")));
				
		r.setJantina(db.find(Jantina.class, getParam("idJantina")));
		r.setBangsa(db.find(Bangsa.class, getParam("idBangsa")));		
		r.setAgama(db.find(Agama.class, getParam("idAgama")));
		if (jpn != null) {
			r.setTarikhLahir(jpn.getTarikhLahir());
		}
		
		r.setAlamat1(getParam("alamat1"));
		r.setAlamat2(getParam("alamat2"));
		r.setAlamat3(getParam("alamat3"));
		r.setPoskod(getParam("poskod"));
		r.setBandar(db.find(Bandar.class, getParam("idBandar")));
		
		// FLAG SEMAKAN
		r.setFlagSemakanJPN("Y");
		r.setFlagSemakanHRMIS("T");
		r.setFlagSemakanPESARA("T");
		r.setFlagSahMaklumatBank("T");
		r.setFlagDaftarSBBPH("Y");
		r.setFlagDaftarManual("Y");
		r.setFlagGelanggang("T");
		
		r.setNoAkaunBank(getParam("noAkaunBank"));
		r.setBank(db.find(Bank.class, getParam("idBank")));
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		r.setIdMasuk(db.find(Users.class, userId));
		r.setTarikhMasuk(new Date());
	}

	@Override
	public void afterSave(Users r) {
		try {
			boolean addPasangan = false;
			mp = new MyPersistence();
			mp.begin();
			
			UsersSpouse pasangan = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + r.getId() + "'");
			if (pasangan == null) {
				pasangan = new UsersSpouse();
				addPasangan = true;
			}			
			pasangan.setUsers(r);
			pasangan.setNamaPasangan(getParam("namaPasangan"));
			pasangan.setNoKPPasangan(getParam("noPengenalanPasangan"));
			if (addPasangan) mp.persist(pasangan);
			mp.commit();
			
			context.put("pasangan", pasangan);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		
		if (r.getBandar() != null) {
			if (r.getBandar().getNegeri() != null) {
				context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
			}
		}
	}	

	@Override
	public boolean delete(Users r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getRelatedData(Users r) {
		try {
			mp = new MyPersistence();
			
			UsersSpouse pasangan = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + r.getId() + "'");			
			context.put("pasangan", pasangan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		
		context.remove("selectBandar");
		if (r.getBandar() != null) {
			if (r.getBandar().getNegeri() != null) {
				context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
			}
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findNoPengenalan = getParam("findNoPengenalan").trim();
		String findNama = getParam("findNama").trim();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", findNoPengenalan);
		map.put("userName", findNama);
		
		return map;
	}
	
	@Command("semakanIDPengguna")
	public String semakanIDPengguna() throws Exception {
		String idPengguna = getParam("idPengguna");
		try {
			mp = new MyPersistence();
			Users pengguna = (Users)mp.get("select x from Users x where x.id = '" + idPengguna + "' and x.flagDaftarSBBPH = 'Y'");
			
			if (pengguna != null) {
				pengguna.setId(null);
				context.put("semakanIDPenggunaMsg", "PENGGUNA TELAH BERDAFTAR DENGAN SISTEM");		
			} else {
				pengguna = new Users();
				//CHECKING DENGAN JPN
				System.out.println("VALIDATE JPN USING BPH WEB SERVICE");
				org.tempuri.crsservice.JpnManager jpnManagerService = new org.tempuri.crsservice.JpnManager();
				BPHServicesImplService service = new BPHServicesImplService();
				BPHServices bphService = service.getPort(BPHServices.class);
				// EXTRA CONTEXT
				String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
				int connectionTimeOutInMs = 10000;// 10 Second
				BindingProvider p = (BindingProvider) bphService;
				p.getRequestContext().put(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						WS_URL);
				myLogger.debug("requesting service from=> :" + bphService.toString());
				
				jpnManagerService = bphService.retreiveCitizensData(idPengguna, "", "T2");
				
				if (jpnManagerService.isFlagSemakanJPN()) {					
					pengguna.setId(idPengguna);					
					context.remove("semakanIDPenggunaMsg");
				} else {
					pengguna.setId(null);
					context.put("semakanIDPenggunaMsg", jpnManagerService.getReplyMsg());
				}
			}
			
			pengguna.setGelaran((Gelaran) mp.find(Gelaran.class, getParam("idGelaran")));
			pengguna.setUserName(getParam("nama"));				
			pengguna.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, getParam("idGredPerkhidmatan")));
			pengguna.setJantina((Jantina) mp.find(Jantina.class, getParam("idJantina")));
			pengguna.setBangsa((Bangsa) mp.find(Bangsa.class, getParam("idBangsa")));
			pengguna.setAgama((Agama) mp.find(Agama.class, getParam("idAgama")));					
			pengguna.setAlamat1(getParam("alamat1"));		
			pengguna.setAlamat2(getParam("alamat2"));		
			pengguna.setAlamat3(getParam("alamat3"));		
			pengguna.setPoskod(getParam("poskod"));		
			pengguna.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
			pengguna.setNoTelefon(getParam("noTelefon"));
			pengguna.setNoTelefonBimbit(getParam("noTelefonBimbit"));
			pengguna.setEmel(getParam("emel"));
			pengguna.setNoAkaunBank(getParam("noAkaunBank"));
			pengguna.setBank((Bank) mp.find(Bank.class, getParam("idBank")));
			context.put("pengguna", pengguna);
			
			UsersSpouse pasangan = new UsersSpouse();
			pasangan.setUsers(pengguna);
			pasangan.setNamaPasangan(getParam("namaPasangan"));
			pasangan.setNoKPPasangan(getParam("noPengenalanPasangan"));
			context.put("pasangan", pasangan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		return getPath() + "/daftarPengguna.vm";
	}
	
	@Command("kemaskiniPengguna")
	public String kemaskiniPengguna() throws Exception {
		String idPengguna = getParam("id");
		try {
			mp = new MyPersistence();
			Users pengguna = (Users) mp.find(Users.class, idPengguna);
			if (pengguna != null) {
				mp.begin();
				pengguna.setGelaran((Gelaran) mp.find(Gelaran.class, getParam("idGelaran")));
				pengguna.setUserName(getParam("nama"));	

				// JENIS PENGGUNA
				pengguna.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "07"));
				pengguna.setRole((Role) mp.find(Role.class, "(AWAM) Pegawai Tadbir"));
				pengguna.setFlagAktif("Y");
				pengguna.setFlagMenungguPengesahan("T");
				pengguna.setFlagUrusanPemohon(3);
				
				pengguna.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
				pengguna.setUserLoginAlt("-");
				
				pengguna.setUserAddress(getParam("alamat1"));
				pengguna.setUserAddress2(getParam("alamat2"));
				pengguna.setUserAddress3(getParam("alamat3"));
				pengguna.setUserPostcode(getParam("poskod"));
				pengguna.setUserBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));

				pengguna.setJenisPengenalan((JenisPengenalan) mp.find(JenisPengenalan.class, "B"));
				pengguna.setNoKP(getParam("idPengguna"));

				pengguna.setNoTelefon(getParam("noTelefon"));
				pengguna.setNoTelefonBimbit(getParam("noTelefonBimbit"));
				pengguna.setEmel(getParam("emel"));

				pengguna.setGredPerkhidmatan((GredPerkhidmatan) mp.find(GredPerkhidmatan.class, getParam("idGredPerkhidmatan")));
				pengguna.setJantina((Jantina) mp.find(Jantina.class, getParam("idJantina")));
				pengguna.setBangsa((Bangsa) mp.find(Bangsa.class, getParam("idBangsa")));
				pengguna.setAgama((Agama) mp.find(Agama.class, getParam("idAgama")));					
				pengguna.setAlamat1(getParam("alamat1"));		
				pengguna.setAlamat2(getParam("alamat2"));		
				pengguna.setAlamat3(getParam("alamat3"));		
				pengguna.setPoskod(getParam("poskod"));		
				pengguna.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				pengguna.setNoTelefon(getParam("noTelefon"));
				pengguna.setNoTelefonBimbit(getParam("noTelefonBimbit"));
				pengguna.setEmel(getParam("emel"));
				pengguna.setNoAkaunBank(getParam("noAkaunBank"));
				pengguna.setBank((Bank) mp.find(Bank.class, getParam("idBank")));

				// FLAG SEMAKAN
				pengguna.setFlagSemakanJPN("Y");
				pengguna.setFlagSemakanHRMIS("T");
				pengguna.setFlagSemakanPESARA("T");
				pengguna.setFlagSahMaklumatBank("T");
				pengguna.setFlagDaftarSBBPH("Y");
				pengguna.setFlagDaftarManual("Y");
				pengguna.setFlagGelanggang("T");
				
				boolean addPasangan = false;
				UsersSpouse pasangan = (UsersSpouse) mp.get("select x from UsersSpouse x where x.users.id = '" + pengguna.getId() + "'");
				if (pasangan == null) {
					pasangan = new UsersSpouse();
					addPasangan = true;
				}			
				pasangan.setUsers(pengguna);
				pasangan.setNamaPasangan(getParam("namaPasangan"));
				pasangan.setNoKPPasangan(getParam("noPengenalanPasangan"));
				if (addPasangan) mp.persist(pasangan);
				mp.commit();
				context.put("success", "Y");

			}			
		} catch (Exception ex) {
			context.put("success", "T");
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}

		return getPath() + "/statusKemaskiniPengguna.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);

		String idNegeri = "0";

		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");

		context.put("selectBandar", dataUtil.getListBandar(idNegeri));

		return getPath() + "/selectBandar.vm";
	}
}

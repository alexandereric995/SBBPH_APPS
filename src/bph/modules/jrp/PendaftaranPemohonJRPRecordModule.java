package bph.modules.jrp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.entity.UserRole;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.util.PasswordService;
import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.jrp.JrpPemohon;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.KategoriPengguna;
import bph.mail.mailer.DaftarAkaunBaruMailer;
import bph.mail.mailer.TetapanSemulaKataLaluanMailer;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class PendaftaranPemohonJRPRecordModule extends LebahRecordTemplateModule<JrpPemohon> {

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<JrpPemohon> getPersistenceClass() {
		// TODO Auto-generated method stub
		return JrpPemohon.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/jrp/pendaftaranPemohon";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");		
		context.put("userRole", userRole);

		//LIST DROPDOWN
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("selectNegeri", dataUtil.getListNegeri());

		defaultButtonOption();
		addfilter(userId, userRole);

		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}

	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	private void addfilter(String userId, String userRole) {		
		this.setOrderBy("tarikhLuput");
		this.setOrderType("desc");

	}

	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {

	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(JrpPemohon r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		Agensi agensi = null;
		Bandar bandar = null;
		try {
			mp = new MyPersistence();
			agensi = (Agensi) mp.find(Agensi.class, getParam("idAgensi"));
			bandar = (Bandar) mp.find(Bandar.class, getParam("idBandar"));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		r.setId(getParam("noPengenalan"));
		r.setNamaPegawai(getParam("namaPegawai"));
		r.setNoTelefon(getParam("noTelefon"));
		r.setEmel(getParam("emel"));
		r.setAgensi(agensi);
		r.setNamaJabatan(getParam("namaJabatan"));
		r.setAlamat1(getParam("alamat1"));
		r.setAlamat2(getParam("alamat2"));
		r.setAlamat3(getParam("alamat3"));
		r.setPoskod(getParam("poskod"));
		r.setBandar(bandar);
		r.setTarikhDaftar(new Date());
		r.setTarikhLuput(cal.getTime());
		r.setFlagAktif("Y");
		r.setFlagHQ("T");
	}

	@Override
	public void afterSave(JrpPemohon r) {
		Users user = null;

		try {
			mp = new MyPersistence();
			mp.begin();

			user = (Users) mp.find(Users.class, r.getId());
			//USER BELUM WUJUD
			if (user == null) {
				user = new Users();
				user.setId(r.getId());
				user.setUserPassword(PasswordService.encrypt(r.getId()));
				user.setUserName(r.getNamaPegawai().toUpperCase());
				user.setAgensi(r.getAgensi());
				user.setBahagian(r.getNamaJabatan());
				user.setUserAddress(r.getAlamat1());
				user.setUserAddress2(r.getAlamat2());
				user.setUserAddress3(r.getAlamat3());
				user.setUserPostcode(r.getPoskod());
				user.setUserBandar(r.getBandar());
				user.setAlamat1(r.getAlamat1());
				user.setAlamat2(r.getAlamat2());
				user.setAlamat3(r.getAlamat3());
				user.setPoskod(r.getPoskod());
				user.setBandar(r.getBandar());
				user.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "09"));
				user.setRole((Role) mp.find(Role.class, "(JRP) Pemohon"));
				user.setFlagAktif("Y");
				user.setFlagSemakanJPN("T");
				user.setFlagSemakanHRMIS("T");
				user.setFlagSemakanPESARA("T");
				user.setFlagDaftarSBBPH("Y");
				user.setFlagDaftarManual("T");
				user.setFlagMenungguPengesahan("T");
				user.setFlagHq(r.getFlagHQ());
				user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
				user.setUserLoginAlt("-");
				user.setDateRegistered(new Date());
				user.setNoTelefon(r.getNoTelefon());
				user.setEmel(r.getEmel());
				mp.persist(user);
				
				// GENERATE EMEL
				DaftarAkaunBaruMailer.get().hantarPasswordPemohonBGSJRP(r.getEmel(), r.getId(), r.getId());
			} else {
				// USER TELAH BERDAFTAR DENGAN SBBPH
				if ("Y".equals(user.getFlagDaftarSBBPH())) {
					user.setAgensi(r.getAgensi());
					user.setBahagian(r.getNamaJabatan());
					
					UserRole userRole = (UserRole) mp.get("select x from UserRole x where x.userId = '" + r.getId() + "' and x.roleId = '(JRP) Pemohon'");
					if (userRole == null) {
						userRole = new UserRole();
						userRole.setUserId(r.getId());
						userRole.setRoleId("(JRP) Pemohon");
						mp.persist(userRole);		
					}		
					
					// GENERATE EMEL
					DaftarAkaunBaruMailer.get().hantarSuccessRolePemohonBGSJRP(r.getEmel(), r.getId(), "JRP");
					
				} else {
					// USER TELAH WUJUD (MIGRATION) TETAPI TIDAK BERDAFTAR DENGAN SBBPH
//					Random rand = new Random();
//					String c = "";
//					String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//					for (int i = 0; i < 7; i++) {
//						c = c + alphabet.charAt(rand.nextInt(alphabet.length()));
//					}
					user.setUserPassword(PasswordService.encrypt(r.getId()));
					user.setUserName(r.getNamaPegawai().toUpperCase());
					user.setAgensi(r.getAgensi());
					user.setBahagian(r.getNamaJabatan());
					user.setUserAddress(r.getAlamat1());
					user.setUserAddress2(r.getAlamat2());
					user.setUserAddress3(r.getAlamat3());
					user.setUserPostcode(r.getPoskod());
					user.setUserBandar(r.getBandar());
					user.setAlamat1(r.getAlamat1());
					user.setAlamat2(r.getAlamat2());
					user.setAlamat3(r.getAlamat3());
					user.setPoskod(r.getPoskod());
					user.setBandar(r.getBandar());
					user.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "09"));
					user.setRole((Role) mp.find(Role.class, "(JRP) Pemohon"));
					user.setFlagAktif("Y");
					user.setFlagSemakanJPN("T");
					user.setFlagSemakanHRMIS("T");
					user.setFlagSemakanPESARA("T");
					user.setFlagDaftarSBBPH("Y");
					user.setFlagDaftarManual("T");
					user.setFlagMenungguPengesahan("T");
					user.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
					user.setUserLoginAlt("-");
					user.setDateRegistered(new Date());
					user.setNoTelefon(r.getNoTelefon());
					user.setEmel(r.getEmel());
					
					// GENERATE EMEL
					DaftarAkaunBaruMailer.get().hantarPasswordPemohonBGSJRP(r.getEmel(), r.getId(), r.getId());
				}				
			}			
			mp.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}

	@Override
	public boolean delete(JrpPemohon r) throws Exception {
		try {
			mp = new MyPersistence();
			Users users = (Users) mp.find(Users.class, r.getId());
			if (users != null) {
				mp.begin();
				//DELETE SECONDARY ROLE
				UserRole userRole = (UserRole) mp.get("select x from UserRole x where x.userId = '" + r.getId() + "' and x.roleId = '(JRP) Pemohon'");
				if (userRole != null) {
					mp.remove(userRole);
				} else {
					// DELETE MAIN USER
					if ("(JRP) Pemohon".equals(users.getRole().getName())) {
						mp.remove(users);
					}
				}
				
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return true;
	}

	@Override
	public void getRelatedData(JrpPemohon r) {
		dataUtil = DataUtil.getInstance(db);
		if (r.getAgensi() != null) {
			if (r.getAgensi().getKementerian() != null) {
				context.put("selectAgensi", dataUtil.getListAgensi(r.getAgensi().getKementerian().getId()));
			}
		}		
		if (r.getBandar() != null) {
			if (r.getBandar().getNegeri() != null) {
				context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
			}
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("id", getParam("findId"));
		m.put("noPengenalan", getParam("findNoPengenalan"));
		m.put("namaPegawai", getParam("findNamaPegawai"));		
		m.put("agensi.kementerian.id", new OperatorEqualTo(getParam("findKementerian")));
		m.put("agensi.id", new OperatorEqualTo(getParam("findAgensi")));
		m.put("namaJabatan", getParam("findNamaJabatan"));
		m.put("bandar.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		m.put("bandar.id", new OperatorEqualTo(getParam("findBandar")));
		m.put("flagAktif", new OperatorEqualTo(getParam("findFlagAktif")));

		return m;
	}

	@Command("kemaskiniPemohon")
	public String kemaskiniPemohon() throws Exception {
		String idPemohon = getParam("idPemohon");
		JrpPemohon pemohon = null;
		String flagAktifDB = null;
		String flagAktif = getParam("flagAktif");

		try {
			mp = new MyPersistence();
			mp.begin();

			pemohon = (JrpPemohon) mp.find(JrpPemohon.class, idPemohon);
			if (pemohon != null) {
				Users users = (Users) mp.find(Users.class, pemohon.getId());
				
				flagAktifDB = pemohon.getFlagAktif();
				
				pemohon.setNamaPegawai(getParam("namaPegawai"));
				pemohon.setNoTelefon(getParam("noTelefon"));
				pemohon.setEmel(getParam("emel"));
				pemohon.setAgensi((Agensi) mp.find(Agensi.class, getParam("idAgensi")));
				pemohon.setNamaJabatan(getParam("namaJabatan"));
				pemohon.setAlamat1(getParam("alamat1"));
				pemohon.setAlamat2(getParam("alamat2"));
				pemohon.setAlamat3(getParam("alamat3"));
				pemohon.setPoskod(getParam("poskod"));
				pemohon.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				pemohon.setFlagAktif(flagAktif);
				pemohon.setFlagHQ(getParam("flagHQ"));
				
				if ("Y".equals(flagAktif)) {
					if ("T".equals(flagAktifDB)) {
						//GENERATE BALIK USER AND TARIKH LUPUT
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.YEAR, 1);
						pemohon.setTarikhDaftar(new Date());
						pemohon.setTarikhLuput(cal.getTime());
						
						if (users == null) {
							users = new Users();
							users.setId(pemohon.getId());
							users.setUserPassword(PasswordService.encrypt(pemohon.getId()));
							users.setUserName(pemohon.getNamaPegawai().toUpperCase());
							users.setAgensi(pemohon.getAgensi());
							users.setBahagian(pemohon.getNamaJabatan());
							users.setUserAddress(pemohon.getAlamat1());
							users.setUserAddress2(pemohon.getAlamat2());
							users.setUserAddress3(pemohon.getAlamat3());
							users.setUserPostcode(pemohon.getPoskod());
							users.setUserBandar(pemohon.getBandar());
							users.setAlamat1(pemohon.getAlamat1());
							users.setAlamat2(pemohon.getAlamat2());
							users.setAlamat3(pemohon.getAlamat3());
							users.setPoskod(pemohon.getPoskod());
							users.setBandar(pemohon.getBandar());
							users.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "09"));
							users.setRole((Role) mp.find(Role.class, "(JRP) Pemohon"));
							users.setFlagAktif("Y");
							users.setFlagSemakanJPN("T");
							users.setFlagSemakanHRMIS("T");
							users.setFlagSemakanPESARA("T");
							users.setFlagDaftarSBBPH("Y");
							users.setFlagDaftarManual("T");
							users.setFlagMenungguPengesahan("T");
							users.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
							users.setUserLoginAlt("-");
							users.setDateRegistered(new Date());
							users.setNoTelefon(pemohon.getNoTelefon());
							users.setEmel(pemohon.getEmel());
							mp.persist(users);
							
							// GENERATE EMEL
							DaftarAkaunBaruMailer.get().hantarPasswordPemohonBGSJRP(pemohon.getEmel(), pemohon.getId(), pemohon.getId());
						} else {
							// USER TELAH BERDAFTAR DENGAN SBBPH
							if ("Y".equals(users.getFlagDaftarSBBPH())) {
								users.setAgensi(pemohon.getAgensi());
								users.setBahagian(pemohon.getNamaJabatan());
								
								UserRole userRole = (UserRole) mp.get("select x from UserRole x where x.userId = '" + users.getId() + "' and x.roleId = '(JRP) Pemohon'");
								if (userRole == null) {
									userRole = new UserRole();
									userRole.setUserId(users.getId());
									userRole.setRoleId("(JRP) Pemohon");
									mp.persist(userRole);		
								}
								
								// GENERATE EMEL
								DaftarAkaunBaruMailer.get().hantarSuccessRolePemohonBGSJRP(pemohon.getEmel(), pemohon.getId(), "BGS");
								
							} else {
								users.setUserPassword(PasswordService.encrypt(pemohon.getId()));
								users.setUserName(pemohon.getNamaPegawai().toUpperCase());
								users.setAgensi(pemohon.getAgensi());
								users.setBahagian(pemohon.getNamaJabatan());
								users.setUserAddress(pemohon.getAlamat1());
								users.setUserAddress2(pemohon.getAlamat2());
								users.setUserAddress3(pemohon.getAlamat3());
								users.setUserPostcode(pemohon.getPoskod());
								users.setUserBandar(pemohon.getBandar());
								users.setAlamat1(pemohon.getAlamat1());
								users.setAlamat2(pemohon.getAlamat2());
								users.setAlamat3(pemohon.getAlamat3());
								users.setPoskod(pemohon.getPoskod());
								users.setBandar(pemohon.getBandar());
								users.setJenisPengguna((KategoriPengguna) mp.find(KategoriPengguna.class, "09"));
								users.setRole((Role) mp.find(Role.class, "(JRP) Pemohon"));
								users.setFlagHq(getParam("flagHQ"));
								users.setFlagAktif("Y");
								users.setFlagSemakanJPN("T");
								users.setFlagSemakanHRMIS("T");
								users.setFlagSemakanPESARA("T");
								users.setFlagDaftarSBBPH("Y");
								users.setFlagDaftarManual("T");
								users.setFlagMenungguPengesahan("T");
								users.setCss((CSS) mp.find(CSS.class, "BPH-Anon"));
								users.setUserLoginAlt("-");
								users.setDateRegistered(new Date());
								users.setNoTelefon(pemohon.getNoTelefon());
								users.setEmel(pemohon.getEmel());
								
								// GENERATE EMEL
								DaftarAkaunBaruMailer.get().hantarPasswordPemohonBGSJRP(pemohon.getEmel(), pemohon.getId(), pemohon.getId());
							}
						}
					} else {
						users.setFlagHq(getParam("flagHQ"));	
					}
			}
		}
			
		mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", pemohon);
		return getPath() + "/entry_page.vm";
	}

	@Command("setSemulaKatalaluan")
	public String setSemulaKatalaluan() throws Exception {
		String idPemohon = getParam("idPemohon");
		JrpPemohon pemohon = null;
		try {
			mp = new MyPersistence();
						
			pemohon = (JrpPemohon) mp.find(JrpPemohon.class, idPemohon);
			
			if (pemohon != null) {
				Users pengguna = (Users) mp.find(Users.class, pemohon.getId());
				
				if (pengguna != null) {
					mp.begin();
					if (pengguna.getEmel() == null || pengguna.getEmel().length() == 0) {
						pengguna.setEmel(getParam("emel"));
					}
					pengguna.setUserPassword(PasswordService.encrypt(pengguna.getId()));				
					mp.commit();
					// GENERATE EMEL
					TetapanSemulaKataLaluanMailer.get().hantarPassword(pengguna.getEmel(), pengguna.getId(), pengguna.getId());				
				}
			}			 
				
		} catch (Exception ex) {
			System.out.println("ERROR RESET PASSWORD PENGGUNA : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", pemohon);
		return getPath() + "/entry_page.vm";
	}

	/** START DROP DOWN **/
	@Command("findAgensi")
	public String findAgensi() throws Exception {
		String idKementerian = "0";
		if (get("findKementerian").trim().length() > 0)
			idKementerian = get("findKementerian");

		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/findAgensi.vm";
	}

	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");

		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/selectAgensi.vm";
	}

	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");

		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");

		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/selectBandar.vm";
	}
	/** END DROP DOWN **/
}

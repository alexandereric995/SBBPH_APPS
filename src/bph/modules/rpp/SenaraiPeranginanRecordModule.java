package bph.modules.rpp;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.db.RegisterUser;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.util.PasswordService;
import portal.module.entity.CSS;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisBangunan;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.rpp.RppJadualTempahan;
import bph.entities.rpp.RppKemudahan;
import bph.entities.rpp.RppPenyeliaPeranginan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppUnit;
import bph.utils.DataUtil;
import bph.utils.Util;
import bph.utils.UtilRpp;
import db.persistence.MyPersistence;

public class SenaraiPeranginanRecordModule extends LebahRecordTemplateModule<RppPeranginan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<RppPeranginan> getPersistenceClass() {
		return RppPeranginan.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/rpp/senaraiPeranginan";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		try {
			mp = new MyPersistence();
			boolean checkingPenyelia = true;
			String idrp = "";
			if (userRole.equalsIgnoreCase("(RPP) Penyelia")) {
				idrp = UtilRpp.getPeranginanByIdPenyelia(mp, userId) != null ? UtilRpp.getPeranginanByIdPenyelia(mp, userId).getId() : null;
				if (idrp == null || idrp.equalsIgnoreCase("")) {
					checkingPenyelia = false;
				}
			}
			context.put("checkingPenyelia", checkingPenyelia);
			context.put("idrp", idrp);

			defaultButtonOption();
			addfilter(mp);

			context.put("selectJenisPeranginan",dataUtil.getListJenisPeranginan());
			context.put("selectNegeri", dataUtil.getListNegeri());
			context.put("command", command);
			context.put("util", new Util());
			context.put("path", getPath());
			context.put("selectedTab", "1");
			context.put("userRole", userRole);
			context.put("uploadDir", ResourceBundle.getBundle("dbconnection")
					.getString("folder"));

		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}

	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		if (userRole.equalsIgnoreCase("(RPP) Penyelia")) {
			this.setReadonly(true);
		} else {
			this.setReadonly(false);
		}
	}

	@SuppressWarnings("unchecked")
	private void addfilter(MyPersistence mp) {
		if (userRole.equalsIgnoreCase("(RPP) Penyelia")) {
			String listPeranginan = "";
			List<RppPenyeliaPeranginan> listPeranginanSeliaan = mp
					.list("select x from RppPenyeliaPeranginan x where x.penyelia.id = '"
							+ userId + "'");
			for (int i = 0; i < listPeranginanSeliaan.size(); i++) {
				if (listPeranginan.length() == 0) {
					listPeranginan = "'"
							+ listPeranginanSeliaan.get(i).getPeranginan()
									.getId() + "'";
				} else {
					listPeranginan = listPeranginan
							+ ","
							+ "'"
							+ listPeranginanSeliaan.get(i).getPeranginan()
									.getId() + "'";
				}
			}
			if (listPeranginan.length() == 0) {
				this.addFilter("id = ''");
			} else {
				this.addFilter("id in (" + listPeranginan + ")");
			}
		}
		this.setOrderBy("namaPeranginan");
		this.setOrderType("asc");
	}

	@Override
	public void save(RppPeranginan peranginan) throws Exception {

		JenisBangunan jnb = null;
		Bandar bdr = null;

		try {

			mp = new MyPersistence();
			jnb = (JenisBangunan) mp.find(JenisBangunan.class,
					getParam("idJenisPeranginan"));
			bdr = (Bandar) mp.find(Bandar.class, getParam("idBandar"));

		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		peranginan.setJenisPeranginan(jnb);
		peranginan.setNamaPeranginan(getParam("namaPeranginan"));
		peranginan.setKodLokasi(getParam("kodLokasi"));
		peranginan.setKodUnit(getParam("kodUnit"));
		peranginan.setAlamat1(getParam("alamat1"));
		peranginan.setAlamat2(getParam("alamat2"));
		peranginan.setAlamat3(getParam("alamat3"));
		peranginan.setPoskod(getParam("poskod"));
		peranginan.setBandar(bdr);
		peranginan.setNoTelefon(getParam("noTelefon"));
		peranginan.setNoFaks(getParam("noFaks"));
		peranginan.setEmel(getParam("emel"));
		peranginan.setCatatan(getParam("catatan"));
		peranginan.setFlagKelulusanSub(getParam("flagKelulusanSub"));
		peranginan.setFlagOperator(getParam("flagOperator"));

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(RppPeranginan peranginan) throws Exception {
		boolean del = false;

		try {

			mp = new MyPersistence();

			List<JenisUnitRPP> listJenisUnit = mp
					.list("select x from JenisUnitRPP x where x.peranginan.id = '"
							+ peranginan.getId() + "' ");

			mp.begin();

			for (int i = 0; i < listJenisUnit.size(); i++) {
				// delete unit
				List<RppUnit> listUnit = mp
						.list("select y from RppUnit y where y.jenisUnit.id = '"
								+ listJenisUnit.get(i).getId() + "' ");
				for (int j = 0; j < listUnit.size(); j++) {
					mp.remove(listUnit.get(j));
				}
				// delete jenis unit
				mp.remove(listJenisUnit.get(i));
			}

			mp.commit();
			del = true;
			context.put("error_flag","record_delete_success" );

		} catch (Exception ex) {
			System.out.println("ERROR delete : " + ex.getMessage());
			del = false;
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return del;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("namaPeranginan", getParam("find_nama"));
		map.put("jenisPeranginan.id", new OperatorEqualTo(
				getParam("findJenisPeranginan")));
		map.put("bandar.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		map.put("bandar.id", new OperatorEqualTo(getParam("findBandar")));
		return map;
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterSave(RppPeranginan peranginan) {
		context.put("selectedTab", "1");
	}

	@Override
	public void getRelatedData(RppPeranginan peranginan) {
		try {
			mp = new MyPersistence();
			RppPeranginan rr = (RppPeranginan) mp.find(RppPeranginan.class,
					peranginan.getId());
			context.put("r", rr);

			String idNegeri = "";
			String idBandar = peranginan.getBandar() != null ? peranginan
					.getBandar().getId() : null;
			if (idBandar != null) {
				idNegeri = peranginan.getBandar() != null ? peranginan
						.getBandar().getNegeri().getId() : null;
			}

			List<Bandar> listBandar = dataUtil.getListBandar(idNegeri);
			context.put("selectBandar", listBandar);
			context.put("selectedTab", "1");

		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
	}

	/**
	 * MAKLUMAT LOKASI PERANGINAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("getMaklumatLokasiPeranginan")
	public String getMaklumatLokasiPeranginan() throws Exception {
		try {

			mp = new MyPersistence();

			RppPeranginan peranginan = (RppPeranginan) mp.find(
					RppPeranginan.class, getParam("idPeranginan"));
			context.put("r", peranginan);

			context.put("selectedTab", "1");

		} catch (Exception e) {
			System.out.println("Error getMaklumatLokasiPeranginan : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_page.vm";
	}

	@Command("doSaveMaklumatLokasiPeranginan")
	public String doSaveMaklumatLokasiPeranginan() throws Exception {

		String statusInfo = "";

		try {
			mp = new MyPersistence();

			RppPeranginan peranginan = (RppPeranginan) mp.find(
					RppPeranginan.class, getParam("idPeranginan"));

			mp.begin();
			peranginan.setJenisPeranginan((JenisBangunan) mp.find(
					JenisBangunan.class, getParam("idJenisPeranginan")));
			peranginan.setNamaPeranginan(getParam("namaPeranginan"));
			peranginan.setKodLokasi(getParam("kodLokasi"));
			peranginan.setKodUnit(getParam("kodUnit"));
			peranginan.setAlamat1(getParam("alamat1"));
			peranginan.setAlamat2(getParam("alamat2"));
			peranginan.setAlamat3(getParam("alamat3"));
			peranginan.setPoskod(getParam("poskod"));
			peranginan.setBandar((Bandar) mp.find(Bandar.class,
					getParam("idBandar")));
			peranginan.setNoTelefon(getParam("noTelefon"));
			peranginan.setNoFaks(getParam("noFaks"));
			peranginan.setEmel(getParam("emel"));
			peranginan.setCatatan(getParam("catatan"));
			peranginan.setFlagKelulusanSub(getParam("flagKelulusanSub"));
			peranginan.setFlagOperator(getParam("flagOperator"));

			mp.commit();
			statusInfo = "success";

		} catch (Exception e) {
			System.out.println("Error doSaveMaklumatLokasiPeranginan : "
					+ e.getMessage());
			statusInfo = "error";
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "1");
		context.put("statusInfo", statusInfo);

		return getPath() + "/entry_page.vm";
	}

	/**
	 * MAKLUMAT PENYELIA PERANGINAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Command("getMaklumatPenyelia")
	public String getMaklumatPenyelia() throws Exception {

		String idPeranginan = getParam("idPeranginan");
		try {
			mp = new MyPersistence();

			RppPeranginan peranginan = (RppPeranginan) mp.find(
					RppPeranginan.class, idPeranginan);
			context.put("r", peranginan);

			List<RppPenyeliaPeranginan> listMaklumatPenyelia = mp
					.list("SELECT x FROM RppPenyeliaPeranginan x WHERE x.peranginan.id = '"
							+ idPeranginan + "'");
			context.put("listMaklumatPenyelia", listMaklumatPenyelia);
			context.put("selectedTab", "2");

		} catch (Exception ex) {
			System.out
					.println("Error getMaklumatPenyelia : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/entry_page.vm";
	}

	@Command("addMaklumatPenyelia")
	public String addMaklumatPenyelia() {
		context.put("rekod", "");
		return getPath() + "/popupMaklumatPenyelia.vm";
	}

	@Command("editMaklumatPenyelia")
	public String editMaklumatPenyelia() {

		context.put("rekod", "");
		try {
			mp = new MyPersistence();

			RppPenyeliaPeranginan maklumatPenyelia = (RppPenyeliaPeranginan) mp
					.find(RppPenyeliaPeranginan.class,
							getParam("idMaklumatPenyelia"));
			if (maklumatPenyelia != null) {
				context.put("rekod", maklumatPenyelia);
			}

		} catch (Exception ex) {
			System.out.println("Error editMaklumatPenyelia : "
					+ ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/popupMaklumatPenyelia.vm";
	}

	@Command("saveMaklumatPenyelia")
	public String saveMaklumatPenyelia() throws Exception {

		String statusInfo = "";

		try {
			mp = new MyPersistence();

			mp.begin();

			// CHECK EXISTING USER
			Users penyelia = (Users) mp.find(Users.class,
					getParam("noPengenalan"));
			if (penyelia != null) {
				if ("Y".equals(getParam("flagOperator"))) {
					// CHECK ROLE OPERATOR
					if (!"(RPP) Operator".equals(penyelia.getRole())) {
						// CHECK SECONDARY ROLE PENYELIA
						if (checkExistingSecondaryRole(penyelia.getId(),
								getParam("flagOperator"))) {
							// DO NOTHING
						} else {
							// REGISTER SECONDARY ROLE
							registerSecondaryRole(penyelia.getId(),
									getParam("flagOperator"));
						}
					}
				} else {
					// CHECK ROLE PENYELIA
					if (!"(RPP) Penyelia".equals(penyelia.getRole())) {
						// CHECK SECONDARY ROLE PENYELIA
						if (checkExistingSecondaryRole(penyelia.getId(),
								getParam("flagOperator"))) {
							// DO NOTHING
						} else {
							// REGISTER SECONDARY ROLE
							registerSecondaryRole(penyelia.getId(),
									getParam("flagOperator"));
						}
					}
				}

				/** Update record existing user */
				penyelia.setNoTelefon(getParam("noTelefon"));
				penyelia.setEmel(getParam("emel"));

			} else {
				// REGISTER USER
				penyelia = new Users();
				penyelia.setId(getParam("noPengenalan"));
				penyelia.setUserPassword(PasswordService
						.encrypt(getParam("noPengenalan")));
				penyelia.setUserName(getParam("namaPenyelia"));
				if ("Y".equals(getParam("flagOperator"))) {
					penyelia.setRole((Role) mp.find(Role.class,
							"(RPP) Operator"));
				} else {
					penyelia.setRole((Role) mp.find(Role.class,
							"(RPP) Penyelia"));
				}
				penyelia.setCss((CSS) mp.find(CSS.class, "BPH-RPP"));
				penyelia.setUserLoginAlt("-");
				penyelia.setNoTelefon(getParam("noTelefon"));
				penyelia.setEmel(getParam("emel"));

				mp.persist(penyelia);

				RegisterUser.setPageStyle(getParam("namaPenyelia"), "BPH-RPP");
			}

			/** SAVE MAKLUMAT PENYELIA PERANGINAN */

			penyelia = (Users) mp.find(Users.class, getParam("noPengenalan"));

			RppPenyeliaPeranginan maklumatPenyelia = (RppPenyeliaPeranginan) mp
					.get("select x from RppPenyeliaPeranginan x where x.peranginan.id = '"
							+ getParam("idPeranginan")
							+ "' and x.penyelia.id = '"
							+ penyelia.getId()
							+ "'");

			if (maklumatPenyelia == null) {
				maklumatPenyelia = new RppPenyeliaPeranginan();
			}

			maklumatPenyelia.setPeranginan((RppPeranginan) mp.find(
					RppPeranginan.class, getParam("idPeranginan")));
			maklumatPenyelia.setPenyelia((Users) mp.find(Users.class,
					getParam("noPengenalan")));
			maklumatPenyelia.setTarikhMulaKhidmat(getDate("tarikhMulaKhidmat"));
			maklumatPenyelia
					.setTarikhTamatKhidmat(getDate("tarikhTamatKhidmat"));
			maklumatPenyelia.setNoTelefon(getParam("noTelefon"));
			maklumatPenyelia.setEmel(getParam("emel"));
			maklumatPenyelia.setCatatan(getParam("catatan"));
			maklumatPenyelia
					.setStatusPerkhidmatan(getParam("idStatusPerkhidmatan"));
			maklumatPenyelia.setFlagOperator(getParam("flagOperator"));

			mp.persist(maklumatPenyelia);

			mp.commit();
			statusInfo = "success";

		} catch (Exception ex) {
			System.out.println("Error saveMaklumatPenyelia : "
					+ ex.getMessage());
			statusInfo = "error";
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "2");
		context.put("statusInfo", statusInfo);

		return getPath() + "/entry_page.vm";
	}

	private boolean checkExistingSecondaryRole(String id, String flagOperator) {
		Db db1 = null;
		String sql = "";
		boolean secondaryRole = false;

		try {
			db1 = new Db();
			ResultSet rs = null;
			if ("Y".equals(flagOperator)) {
				sql = "select * from user_role x where user_id = '" + id
						+ "' and role_id = '(RPP) Operator' ";
				rs = db1.getStatement().executeQuery(sql);
			} else {
				sql = "select * from user_role x where user_id = '" + id
						+ "' and role_id = '(RPP) Penyelia' ";
				rs = db1.getStatement().executeQuery(sql);
			}

			if (rs.next())
				secondaryRole = true;

		} catch (Exception e) {
			System.out
					.println("checkExistingSecondaryRole : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
		return secondaryRole;
	}

	private void registerSecondaryRole(String id, String flagOperator) {
		Db db1 = null;
		String sql = "";
		try {
			db1 = new Db();
			if ("Y".equals(flagOperator)) {
				sql = "INSERT INTO user_role (user_id, role_id) VALUES ('" + id
						+ "', '(RPP) Operator')";
				db1.getStatement().executeUpdate(sql);
			} else {
				sql = "INSERT INTO user_role (user_id, role_id) VALUES ('" + id
						+ "', '(RPP) Penyelia')";
				db1.getStatement().executeUpdate(sql);
			}

		} catch (Exception e) {
			System.out.println("registerSecondaryRole : " + e.getMessage());
		} finally {
			if (db1 != null)
				db1.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Command("removeMaklumatPenyelia")
	public String removeMaklumatPenyelia() {

		String statusInfo = "";

		try {
			mp = new MyPersistence();

			RppPenyeliaPeranginan maklumatPenyelia = (RppPenyeliaPeranginan) mp
					.find(RppPenyeliaPeranginan.class,
							getParam("idMaklumatPenyelia"));

			if (maklumatPenyelia != null) {
				mp.begin();
				mp.remove(maklumatPenyelia);
				mp.commit();
				statusInfo = "success";
			}

			List<RppPenyeliaPeranginan> listMaklumatPenyelia = mp
					.list("SELECT x FROM RppPenyeliaPeranginan x WHERE x.peranginan.id = '"
							+ getParam("idPeranginan") + "'");
			context.put("listMaklumatPenyelia", listMaklumatPenyelia);

		} catch (Exception ex) {
			System.out.println("ERROR removeMaklumatPenyelia : "
					+ ex.getMessage());
			statusInfo = "error";
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "2");
		context.put("statusInfo", statusInfo);

		return getPath() + "/entry_page.vm";
	}

	/**
	 * DROPDOWN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0)
			idNegeri = getParam("findNegeri");

		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}

	@Command("selectJenisPeranginan")
	public String selectJenisPeranginan() throws Exception {

		try {
			mp = new MyPersistence();

			String namaPeranginan = getParam("namaPeranginan").trim()
					.toUpperCase();
			namaPeranginan = namaPeranginan.replace(
					"RUMAH PENGINAPAN PERSEKUTUAN", "").trim();
			namaPeranginan = namaPeranginan.replace(
					"RUMAH PERANGINAN PERSEKUTUAN", "").trim();
			namaPeranginan = namaPeranginan.replace("RUMAH TRANSIT", "").trim();

			if (getParam("idJenisPeranginan") != null) {
				JenisBangunan jenisPeranginan = (JenisBangunan) mp.find(
						JenisBangunan.class, getParam("idJenisPeranginan"));
				if (jenisPeranginan != null)
					namaPeranginan = jenisPeranginan.getKeterangan()
							.toUpperCase().trim()
							+ " " + namaPeranginan;
			}

			context.put("namaPeranginan", namaPeranginan);

		} catch (Exception ex) {
			System.out.println("ERROR selectJenisPeranginan : "
					+ ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/namaPeranginan.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");

		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/selectBandar.vm";
	}

	@Command("getPenyelia")
	public String getPenyelia() throws Exception {

		try {
			mp = new MyPersistence();

			// String namaPenyelia = "";
			Users existPenyelia = (Users) mp.find(Users.class,
					getParam("noPengenalan"));
			// if (penyelia != null){
			// namaPenyelia = penyelia.getUserName().toUpperCase();
			// }
			context.put("existPenyelia", existPenyelia);

		} catch (Exception ex) {
			System.out.println("ERROR getPenyelia : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/namaPenyelia.vm";
	}

	/**
	 * SENARAI JENIS UNIT
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Command("getSenaraiJenisUnit")
	public String getSenaraiJenisUnit() throws Exception {

		String idPeranginan = getParam("idPeranginan");
		try {
			mp = new MyPersistence();

			RppPeranginan peranginan = (RppPeranginan) mp.find(
					RppPeranginan.class, idPeranginan);
			context.put("r", peranginan);

			List<JenisUnitRPP> listJenisUnit = mp
					.list("select x from JenisUnitRPP x where x.peranginan.id = '"
							+ idPeranginan + "' ");
			context.put("listJenisUnit", listJenisUnit);

			context.put("selectedTab", "3");

		} catch (Exception ex) {
			System.out
					.println("ERROR getSenaraiJenisUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_page.vm";

	}

	/**
	 * SENARAI KEMUDAHAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Command("getSenaraiKemudahan")
	public String getSenaraiKemudahan() throws Exception {
		String idPeranginan = getParam("idPeranginan");

		try {
			mp = new MyPersistence();

			RppPeranginan peranginan = (RppPeranginan) mp.find(
					RppPeranginan.class, idPeranginan);
			context.put("r", peranginan);

			List<RppKemudahan> listKemudahan = mp
					.list("select x from RppKemudahan x where x.peranginan.id = '"
							+ idPeranginan + "'");
			context.put("listKemudahan", listKemudahan);

			context.put("selectedTab", "4");

		} catch (Exception ex) {
			System.out
					.println("ERROR getSenaraiKemudahan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/entry_page.vm";
	}

	/**
	 * POPUP KEMUDAHAN
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("openPopupKemudahan")
	public String openPopupKemudahan() throws Exception {
		context.remove("k");
		String idKemudahan = getParam("idKemudahan");
		String idPeranginan = getParam("idPeranginan");

		try {
			mp = new MyPersistence();

			RppPeranginan r = (RppPeranginan) mp.find(RppPeranginan.class,
					idPeranginan);
			RppKemudahan k = (RppKemudahan) mp.find(RppKemudahan.class,
					idKemudahan);
			context.put("k", k);
			context.put("r", r);

		} catch (Exception ex) {
			System.out.println("ERROR openPopupKemudahan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/popup/kemudahan.vm";
	}

	@Command("saveKemudahan")
	public String saveKemudahan() throws Exception {
		String idPeranginan = getParam("idPeranginan");
		String idKemudahan = getParam("idKemudahan");

		try {
			mp = new MyPersistence();

			RppPeranginan r = (RppPeranginan) mp.find(RppPeranginan.class,
					idPeranginan);
			RppKemudahan k = (RppKemudahan) mp.find(RppKemudahan.class,
					idKemudahan);

			if (k == null) {
				k = new RppKemudahan();
			}

			mp.begin();

			k.setPeranginan(r);
			k.setNama(getParam("nama"));
			k.setBilangan(getParamAsInteger("bilangan"));
			k.setJenisKadarSewa(getParam("jenisKadarSewa"));
			k.setCatatan(getParam("catatan"));
			k.setFlagSewa(getParam("flagSewa"));

			Double kadar = 0d;
			if (getParam("flagSewa").equalsIgnoreCase("Y")) {
				kadar = Util.getDoubleRemoveComma(getParam("kadarSewa"));
			} else {
				kadar = 0d;
			}
			k.setKadarSewa(kadar);

			mp.persist(k);

			mp.commit();

		} catch (Exception ex) {
			System.out.println("ERROR saveKemudahan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getSenaraiKemudahan();

	}

	@Command("deleteKemudahan")
	public String deleteKemudahan() throws Exception {
		String idKemudahan = getParam("idKemudahan");
		try {
			mp = new MyPersistence();

			RppKemudahan k = (RppKemudahan) mp.find(RppKemudahan.class,
					idKemudahan);

			if (k != null) {
				mp.begin();
				mp.remove(k);
				mp.commit();
			}

		} catch (Exception ex) {
			System.out.println("ERROR deleteKemudahan : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getSenaraiKemudahan();
	}

	@SuppressWarnings("unchecked")
	@Command("viewSenaraiUnit")
	public String viewSenaraiUnit() throws Exception {

		String idJenisUnit = getParam("idJenisUnit");
		try {
			mp = new MyPersistence();

			List<RppUnit> listUnit = mp
					.list("select y from RppUnit y where y.jenisUnit.id = '"
							+ idJenisUnit + "' order by y.noUnit ");
			JenisUnitRPP ju = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);
			context.put("listUnit", listUnit);
			context.put("ju", ju);

		} catch (Exception ex) {
			System.out.println("ERROR viewSenaraiUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/senaraiUnit.vm";
	}

	/**
	 * POPUP JENIS UNIT
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("openPopupJenisUnit")
	public String openPopupJenisUnit() throws Exception {
		context.remove("ju");
		String idJenisUnit = getParam("idJenisUnit");
		String idPeranginan = getParam("idPeranginan");

		try {
			mp = new MyPersistence();

			RppPeranginan r = (RppPeranginan) mp.find(RppPeranginan.class,
					idPeranginan);
			JenisUnitRPP ju = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);

			String flagLuarPuncak = "";
			String flagPuncak = "";
			if (ju != null) {
				if (ju.getFlagTiadaHadKelayakan().equalsIgnoreCase("Y")) {
					flagLuarPuncak = "";
				} else if (ju.getFlagJulatGredKelayakan().equalsIgnoreCase("Y")) {
					flagLuarPuncak = "JULAT";
				} else {
					flagLuarPuncak = "HAD";
				}

				if (ju.getFlagTiadaHadKelayakanWaktuPuncak().equalsIgnoreCase(
						"Y")) {
					flagPuncak = "";
				} else if (ju.getFlagJulatGredKelayakanWaktuPuncak()
						.equalsIgnoreCase("Y")) {
					flagPuncak = "JULATWP";
				} else {
					flagPuncak = "HADWP";
				}
			}

			context.put("flagPuncak", flagPuncak);
			context.put("flagLuarPuncak", flagLuarPuncak);
			context.put("ju", ju);
			context.put("r", r);
			context.put("listGredPerkhidmatan",
					dataUtil.getListGredPerkhidmatan());

		} catch (Exception ex) {
			System.out.println("ERROR openPopupJenisUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getPath() + "/popup/jenisUnit.vm";
	}

	@Command("saveJenisUnit")
	public String saveJenisUnit() throws Exception {
	
		String idPeranginan = getParam("idPeranginan");
		String idJenisUnit = getParam("idJenisUnit");
		Boolean addNew=false;
		try {
			mp = new MyPersistence();

			RppPeranginan r = (RppPeranginan) mp.find(RppPeranginan.class,
					idPeranginan);
			JenisUnitRPP ju = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);

			if (ju == null) {
				ju = new JenisUnitRPP();
				addNew=true;
			}

			mp.begin();

			ju.setPeranginan(r);
			ju.setKeterangan(getParam("keterangan"));
			ju.setHadDewasa(getParamAsInteger("hadDewasa"));
			ju.setHadKanakKanak(getParamAsInteger("hadKanakKanak"));
			ju.setHadKuantiti(getParamAsInteger("hadKuantiti"));
			ju.setKadarSewa(Util.getDoubleRemoveComma(getParam("kadarSewa")));
			ju.setKadarSewaWaktuPuncak(Util
					.getDoubleRemoveComma(getParam("kadarSewa"))); // temporary
																	// sama
																	// seperti
																	// kadar
																	// sewa

			/**
			 * Modified on 07042016 by syaz
			 */
			String flagLuarPuncak = getParam("flagLuarPuncak");
			String flagPuncak = getParam("flagPuncak");
			int gredKelayakanWaktuPuncak = 0;
			int gredMaksimumKelayakanWaktuPuncak = 0;
			int gredMinimumKelayakan = 0;
			int gredMaksimumKelayakan = 0;

			String flagTiadaHadKelayakan = "";
			String flagJulatGredKelayakan = "";
			if (flagLuarPuncak.equalsIgnoreCase("TIADAHAD")) {
				flagTiadaHadKelayakan = "Y";
				flagJulatGredKelayakan = "N";
			} else if (flagLuarPuncak.equalsIgnoreCase("HAD")) {
				gredMinimumKelayakan = getParamAsInteger("gredMinimumKelayakan");
				flagTiadaHadKelayakan = "N";
				flagJulatGredKelayakan = "N";
			} else if (flagLuarPuncak.equalsIgnoreCase("JULAT")) {
				gredMinimumKelayakan = getParamAsInteger("gredMinimumKelayakan");
				gredMaksimumKelayakan = getParamAsInteger("gredMaksimumKelayakan");
				flagTiadaHadKelayakan = "N";
				flagJulatGredKelayakan = "Y";
			}

			String flagTiadaHadKelayakanWP = "";
			String flagJulatGredKelayakanWP = "";
			if (flagPuncak.equalsIgnoreCase("TIADAHADWP")) {
				flagTiadaHadKelayakanWP = "Y";
				flagJulatGredKelayakanWP = "N";
			} else if (flagPuncak.equalsIgnoreCase("HADWP")) {
				gredKelayakanWaktuPuncak = getParamAsInteger("gredKelayakanWaktuPuncak");
				flagTiadaHadKelayakanWP = "N";
				flagJulatGredKelayakanWP = "N";
			} else if (flagPuncak.equalsIgnoreCase("JULATWP")) {
				gredKelayakanWaktuPuncak = getParamAsInteger("gredKelayakanWaktuPuncak");
				gredMaksimumKelayakanWaktuPuncak = getParamAsInteger("gredMaksimumKelayakanWaktuPuncak");
				flagTiadaHadKelayakanWP = "N";
				flagJulatGredKelayakanWP = "Y";
			}

			ju.setGredMinimumKelayakan(gredMinimumKelayakan);
			ju.setGredKelayakanWaktuPuncak(gredKelayakanWaktuPuncak);
			ju.setGredMaksimumKelayakan(gredMaksimumKelayakan);
			ju.setGredMaksimumKelayakanWaktuPuncak(gredMaksimumKelayakanWaktuPuncak);

			ju.setFlagTiadaHadKelayakan(flagTiadaHadKelayakan);
			ju.setFlagJulatGredKelayakan(flagJulatGredKelayakan);
			ju.setFlagTiadaHadKelayakanWaktuPuncak(flagTiadaHadKelayakanWP);
			ju.setFlagJulatGredKelayakanWaktuPuncak(flagJulatGredKelayakanWP);
			
			//rozai add id_masuk,tarikh_masuk,id_kemaskini,tarikh_kemaskini (05/02/2018)	

			if (addNew) {
				ju.setIdMasuk((Users) mp.find(Users.class, userId));
				ju.setTarikhMasuk(new Date());
				mp.persist(ju);	
			}else{
				ju.setIdKemaskini((Users) mp.find(Users.class, userId));
				ju.setTarikhKemaskini(new Date());
			}
			mp.commit();

		} catch (Exception ex) {
			System.out.println("ERROR saveJenisUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return getSenaraiJenisUnit();
	}

	@Command("deleteJenisUnit")
	public String deleteJenisUnit() throws Exception {

		String idJenisUnit = getParam("idJenisUnit");
		try {
			mp = new MyPersistence();

			JenisUnitRPP ju = (JenisUnitRPP) mp.find(JenisUnitRPP.class,
					idJenisUnit);

			if (ju != null) {
				mp.begin();
				mp.remove(ju);
				mp.commit();
			}

		} catch (Exception ex) {
			System.out.println("ERROR deleteJenisUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getSenaraiJenisUnit();
	}

	/**
	 * POPUP UNIT
	 * 
	 * @return
	 * @throws Exception
	 */
	@Command("openPopupUnit")
	public String openPopupUnit() throws Exception {
		context.remove("u");
		String idUnit = getParam("idUnit");
		String idPeranginan = getParam("idPeranginan");

		try {
			mp = new MyPersistence();

			RppPeranginan r = (RppPeranginan) mp.find(RppPeranginan.class,
					idPeranginan);
			RppUnit u = (RppUnit) mp.find(RppUnit.class, idUnit);
			context.put("u", u);
			context.put("r", r);

		} catch (Exception ex) {
			System.out.println("ERROR openPopupUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/popup/unit.vm";
	}

	@Command("saveUnit")
	public String saveUnit() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");

		String idUnit = getParam("idUnit");
		String idJenisUnit = getParam("idJenisUnit");

		if (idJenisUnit != null && !idJenisUnit.equalsIgnoreCase("")) {

			try {
				mp = new MyPersistence();
				mp.begin();
				boolean addUnit = false;
				RppUnit u = (RppUnit) mp.find(RppUnit.class, idUnit);
				if (u == null) {
					u = new RppUnit();
					addUnit = true;
				}				
				u.setJenisUnit((JenisUnitRPP) mp.find(JenisUnitRPP.class,idJenisUnit));
				u.setNamaUnit(getParam("namaUnit"));
				u.setNoUnit(getParam("noUnit"));
				u.setCatatan(getParam("catatan"));

				String status = getParam("flagStatus");
				if (status.equalsIgnoreCase("")) {
					u.setStatus(null);
				} else {
					u.setStatus(status);
					//TODO - REASSIGN BILIK
					reassignUnit(u, mp);
				}

				if (addUnit) {
					u.setIdMasuk((Users) mp.find(Users.class, userId));
					u.setTarikhMasuk(new Date());
					mp.persist(u);
				} else {
					u.setIdKemaskini((Users) mp.find(Users.class, userId));
					u.setTarikhKemaskini(new Date());
				}
				
				mp.commit();

			} catch (Exception ex) {
				System.out.println("ERROR saveUnit : " + ex.getMessage());
			} finally {
				if (mp != null) {
					mp.close();
				}
			}
		}
		return viewSenaraiUnit();
	}

	private void reassignUnit(RppUnit unit, MyPersistence mp) {
		List<RppJadualTempahan> listJadualTempahan = mp.list("select x from RppJadualTempahan x where x.unit.id = '" + unit.getId() + "' and x.flagStatusTempahan = 'TEMP' order by x.tarikhMula asc");
		for (int i = 0; i < listJadualTempahan.size(); i ++) {
			RppJadualTempahan jadualTempahan = listJadualTempahan.get(i);
			if (jadualTempahan.getTarikhMula().after(new Date())) {
				List<RppUnit> listUnitAvailable = UtilRpp.walkInListUnitAvailable(mp, jadualTempahan.getTarikhMula(), jadualTempahan.getTarikhTamat(), jadualTempahan.getUnit().getJenisUnit().getId());
				for (int j = 0; j < listUnitAvailable.size(); j++) {
					RppUnit unitAvailable = listUnitAvailable.get(j);
					if (unitAvailable.getStatus() == null || !unitAvailable.getStatus().equals("RESERVED")){
						jadualTempahan.setCatatan("PERTUKARAN UNIT DARI UNIT " + jadualTempahan.getUnit().getId() + " KE UNIT " + unitAvailable.getId() + " DISEBABKAN UNIT " + unit.getId() + " TELAH DIRESERVED PADA " + Util.getDateTime(new Date(), "dd/MM/yyyy") +".");
						jadualTempahan.setUnit(unitAvailable);
						break;
					}
				}
			}			
		}
	}

	@Command("deleteUnit")
	public String deleteUnit() throws Exception {
		String idUnit = getParam("idUnit");

		try {
			mp = new MyPersistence();

			RppUnit u = (RppUnit) mp.find(RppUnit.class, idUnit);
			if (u != null) {
				mp.begin();
				mp.remove(u);
				mp.commit();
			}

		} catch (Exception ex) {
			System.out.println("ERROR deleteUnit : " + ex.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return viewSenaraiUnit();
	}

	@Command("showGred")
	public String showGred() throws Exception {
		String flagLuarPuncak = getParam("flagLuarPuncak");
		context.put("flagLuarPuncak", flagLuarPuncak);
		return getPath() + "/popup/waktuLuarPuncak.vm";
	}

	@Command("showGredWP")
	public String showGredWP() throws Exception {
		String flagPuncak = getParam("flagPuncak");
		context.put("flagPuncak", flagPuncak);
		return getPath() + "/popup/waktuPuncak.vm";
	}
}
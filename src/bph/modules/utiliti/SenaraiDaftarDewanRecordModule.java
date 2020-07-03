package bph.modules.utiliti;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import portal.module.entity.Users;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisKegunaanRuang;
import bph.entities.kod.KodPusatTerima;
import bph.entities.kod.Negeri;
import bph.entities.kod.Seksyen;
import bph.entities.rk.RkRuangKomersil;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilPeralatan;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiDaftarDewanRecordModule extends
		LebahRecordTemplateModule<UtilDewan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<UtilDewan> getPersistenceClass() {
		return UtilDewan.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/utiliti/senaraiDaftarDewan";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute(
				"_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);

		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectPusatTerima", dataUtil.getListPusatTerima());
		context.put("selectSeksyen", dataUtil.getListSeksyen());

		context.put("util", util);

		defaultButtonOption();
		addfilter();
		// TODO IMPLEMENT BILA ADA SUBCLASS
		doOverideFilterRecord();
	}

	// TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {

	}

	private void defaultButtonOption() {
		this.setHideDeleteButton(true);
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}

	private void addfilter() {
		this.setOrderBy("bandar.negeri.id");
		this.setOrderType("desc");
	}	

	@Override
	public void beforeSave() {
	}

	@Override
	public void save(UtilDewan dewan) throws Exception {
		dewan.setNama(getParam("nama"));
		dewan.setAlamat1(getParam("alamat1"));
		dewan.setAlamat2(getParam("alamat2"));
		dewan.setAlamat3(getParam("alamat3"));
		dewan.setLokasi(getParam("lokasi"));
		dewan.setPoskod(getParam("poskod"));
		dewan.setBandar((Bandar) db.find(Bandar.class, getParam("idBandar")));

		if (!getParam("kadarSewa").equals(""))
			dewan.setKadarSewa(Double.valueOf(util
					.RemoveComma(getParam("kadarSewa"))));
		if (!getParam("kadarSewaAwam").equals(""))
			dewan.setKadarSewaAwam(Double.valueOf(util
					.RemoveComma(getParam("kadarSewaAwam"))));
		dewan.setCatatan(getParam("catatan"));

		dewan.setWaktuBuka(getParam("masaMula"));
		dewan.setWaktuTutup(getParam("masaTamat"));
		dewan.setSeksyen((Seksyen) db
				.find(Seksyen.class, getParam("idSeksyen")));
		dewan.setKodCawangan((KodPusatTerima) db.find(KodPusatTerima.class,
				getParam("idCawangan")));
		dewan.setDaftarOleh(db.find(Users.class, userId));
	}

	@Override
	public void afterSave(UtilDewan dewan) {
		context.put("selectedTab", "1");
	}

	@Override
	public boolean delete(UtilDewan dewan) throws Exception {
		return false;
	}

	@Override
	public void getRelatedData(UtilDewan dewan) {
		String idNegeri = "";
		if (dewan.getBandar() != null && dewan.getBandar().getNegeri().getId().trim().length() > 0)
			idNegeri = dewan.getBandar().getNegeri().getId();
		List<Bandar> listBandar = dataUtil.getListBandar(idNegeri);		
		context.put("selectBandar", listBandar);
		context.put("selectedTab", "1");
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nama", getParam("findNama"));
		map.put("lokasi", getParam("findLokasi"));
		map.put("bandar.id", new OperatorEqualTo(getParam("findBandar")));
		map.put("bandar.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		map.put("seksyen.id", new OperatorEqualTo(getParam("findSeksyen")));
		map.put("flagAktif", new OperatorEqualTo(getParam("findStatus")));

		return map;
	}	
	
	/** START TAB **/
	@Command("getMaklumatDewan")
	public String getMaklumatDewan() {
		try {
			mp = new MyPersistence();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, getParam("idDewan"));
			context.put("r", dewan);			
		} catch (Exception e) {
			System.out.println("Error getMaklumatDewan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}

	@Command("getMaklumatPeralatan")
	public String getMaklumatPeralatan() {
		this.setDisableDefaultButton(true);
		try {
			mp = new MyPersistence();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, getParam("idDewan"));
			context.put("r", dewan);
			List<UtilPeralatan> listMaklumatPeralatan = mp
					.list("SELECT x FROM UtilPeralatan x WHERE x.dewan.id = '"
							+ dewan.getId() + "'");
			context.put("listMaklumatPeralatan", listMaklumatPeralatan);
			
		} catch (Exception e) {
			System.out.println("Error getMaklumatPeralatan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("util", util);
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** KEMASKINI MAKLUMAT DEWAN */
	@Command("saveMaklumatDewan")
	public String saveMaklumatDewan() throws Exception {			
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		try {
			mp = new MyPersistence();
			
			mp.begin();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class, get("idDewan"));
			if(dewan != null){
				dewan.setNama(getParam("nama"));
				dewan.setAlamat1(getParam("alamat1"));
				dewan.setAlamat2(getParam("alamat2"));
				dewan.setAlamat3(getParam("alamat3"));
				dewan.setLokasi(getParam("lokasi"));
				dewan.setPoskod(getParam("poskod"));
				dewan.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));

				if (!getParam("kadarSewa").equals(""))
					dewan.setKadarSewa(Double.valueOf(util
							.RemoveComma(getParam("kadarSewa"))));
				if (!getParam("kadarSewaAwam").equals(""))
					dewan.setKadarSewaAwam(Double.valueOf(util
							.RemoveComma(getParam("kadarSewaAwam"))));
				dewan.setCatatan(getParam("catatan"));

				dewan.setWaktuBuka(getParam("masaMula"));
				dewan.setWaktuTutup(getParam("masaTamat"));
				dewan.setSeksyen((Seksyen) mp
						.find(Seksyen.class, getParam("idSeksyen")));
				dewan.setKodCawangan((KodPusatTerima) mp.find(KodPusatTerima.class,
						getParam("idCawangan")));
				dewan.setFlagAktif(getParam("flagAktif"));
				dewan.setKemaskiniOleh((Users) mp.find(Users.class, userId));	
				dewan.setTarikhKemaskini(new Date());
			}
			mp.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatDewan();		
	}

	/** START MAKLUMAT PERALATAN **/
	@Command("addMaklumatPeralatan")
	public String addMaklumatPeralatan() {
		context.put("selectJenisJaminan", dataUtil.getListJenisJaminan());
		context.remove("rekod");
		return getPath() + "/maklumatPeralatan/popupMaklumatPeralatan.vm";
	}

	@Command("editMaklumatPeralatan")
	public String editMaklumatPeralatan() {
		context.remove("rekod");
		try {
			mp = new MyPersistence();
			UtilPeralatan peralatan = (UtilPeralatan) mp.find(
					UtilPeralatan.class, getParam("idMaklumatPeralatan"));
			if (peralatan != null) {
				context.put("rekod", peralatan);
			}
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getPath() + "/maklumatPeralatan/popupMaklumatPeralatan.vm";
	}

	@Command("saveMaklumatPeralatan")
	public String saveMaklumatPeralatan() throws ParseException {

		try {
			mp = new MyPersistence();
			mp.begin();
			UtilPeralatan peralatan = (UtilPeralatan) mp.find(
					UtilPeralatan.class, getParam("idMaklumatPeralatan"));
			Boolean addMaklumatPeralatan = false;

			if (peralatan == null) {
				addMaklumatPeralatan = true;
				peralatan = new UtilPeralatan();
			}

			peralatan.setDewan((UtilDewan) mp.find(UtilDewan.class,
					getParam("idDewan")));
			peralatan.setNama(getParam("nama"));
			peralatan.setKuantiti(getParam("kuantiti"));
			peralatan.setCatatan(getParam("catatan"));
			if (!getParam("kadarSewa").equals(""))
				peralatan.setKadarSewa(Double.valueOf(util
						.RemoveComma(getParam("kadarSewa"))));

			
			if (addMaklumatPeralatan)
				mp.persist(peralatan);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error saveMaklumatPeralatan : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getMaklumatPeralatan();
	}

	@Command("removeMaklumatPeralatan")
	public String removeMaklumatPeralatan() {

		try {
			mp = new MyPersistence();
			UtilPeralatan peralatan = (UtilPeralatan) mp.find(
					UtilPeralatan.class, getParam("idMaklumatPeralatan"));
			if (peralatan != null) {
				mp.begin();
				mp.remove(peralatan);
				mp.commit();
			}

		} catch (Exception e) {
			System.out.println("Error removeMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return getMaklumatPeralatan();	
	}
	/** END MAKLUMAT PERALATAN **/

	/** START DROP DOWN LIST **/
	@Command("findBandar")
	public String findBandar() throws Exception {

		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0)
			idNegeri = getParam("findNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		return getPath() + "/findBandar.vm";
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
}

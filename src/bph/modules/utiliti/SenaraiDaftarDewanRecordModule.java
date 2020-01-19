package bph.modules.utiliti;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Bandar;
import bph.entities.kod.KodPusatTerima;
import bph.entities.kod.Negeri;
import bph.entities.kod.Seksyen;
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
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		List<Negeri> negeriList = dataUtil.getListNegeri();
		List<Seksyen> seksyenList = dataUtil.getListSeksyen();
		context.put("selectNegeri", negeriList);
		context.put("selectSeksyen", seksyenList);
		this.setDisableSaveAddNewButton(true);
		//this.setDisableAddNewRecordButton(true);
		this.setHideDeleteButton(true);
		List<KodPusatTerima> listPusatTerima = dataUtil.getListPusatTerima();		
		context.put("selectPusatTerima", listPusatTerima);
	}

	@Override
	public String getPath() {
		return "bph/modules/utiliti/senaraiDaftarDewan";
	}

	@Override
	public void beforeSave() {
	}

	@SuppressWarnings("static-access")
	@Override
	public void save(UtilDewan simpan) throws Exception {
		try {
			mp = new MyPersistence();
			simpan.setNama(get("nama"));
			simpan.setAlamat1(get("alamat1"));
			simpan.setAlamat2(get("alamat2"));
			simpan.setAlamat3(get("alamat3"));
			simpan.setLokasi(get("lokasi"));
			simpan.setPoskod(get("poskod"));
			simpan.setBandar((Bandar) mp.find(Bandar.class, get("idBandar")));
			simpan.setCatatan(get("catatan"));
			simpan.setKadarSewa(Double.valueOf(util
					.RemoveComma(get("kadarSewa"))));
			simpan.setKadarSewaAwam(Double.valueOf(util
					.RemoveComma(get("kadarSewaAwam"))));
			simpan.setWaktuBuka(get("waktuBuka"));
			simpan.setWaktuTutup(get("waktuTutup"));
			simpan.setSeksyen((Seksyen) mp.find(Seksyen.class,
					getParam("idSeksyen")));
			simpan.setKodCawangan((KodPusatTerima) mp.find(KodPusatTerima.class, getParam("idCawangan")));
		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void afterSave(UtilDewan r) {
		context.put("selectedTab", "1");
	}

	@Override
	public boolean delete(UtilDewan r) throws Exception {
		return true;
	}

	@Override
	public void getRelatedData(UtilDewan r) {
		String idNegeri = "";
		if (r.getBandar() != null
				&& r.getBandar().getNegeri().getId().trim().length() > 0)
			idNegeri = r.getBandar().getNegeri().getId();
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		context.put("selectedTab", "1");
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nama", get("findNama"));
		map.put("bandar.id", new OperatorEqualTo(get("findBandar")));
		map.put("bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		map.put("bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		map.put("lokasi", get("findLokasi"));
		return map;
	}

	/** START TAB **/

	@Command("getMaklumatDewan")
	public String getMaklumatDewan() {
		try {
			mp = new MyPersistence();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class,
					get("idDewan"));
			context.put("r", dewan);
			context.put("selectedTab", "1");
		} catch (Exception e) {
			System.out.println("Error getMaklumatDewan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("getMaklumatPeralatan")
	public String getMaklumatPeralatan() {
		try {
			mp = new MyPersistence();
			UtilDewan dewan = (UtilDewan) mp.find(UtilDewan.class,
					get("idDewan"));
			context.put("r", dewan);
			List<UtilPeralatan> listMaklumatPeralatan = mp
					.list("SELECT x FROM UtilPeralatan x WHERE x.dewan.id = '"
							+ dewan.getId() + "'");
			context.put("listMaklumatPeralatan", listMaklumatPeralatan);
			context.put("selectedTab", "2");
		} catch (Exception e) {
			System.out
					.println("Error getMaklumatPeralatan : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}

	/** END TAB **/

	/** START MAKLUMAT PERALATAN **/

	@Command("addMaklumatPeralatan")
	public String addMaklumatPeralatan() {
		context.put("selectJenisJaminan", dataUtil.getListJenisJaminan());
		context.put("rekod", "");
		return getPath() + "/maklumatPeralatan/popupMaklumatPeralatan.vm";
	}

	@Command("editMaklumatPeralatan")
	public String editMaklumatPeralatan() {
		context.put("selectJenisJaminan", dataUtil.getListJenisJaminan());
		context.put("rekod", "");
		try {
			mp = new MyPersistence();
			UtilPeralatan peralatan = (UtilPeralatan) mp.find(
					UtilPeralatan.class, get("idMaklumatPeralatan"));
			if (peralatan != null) {
				context.put("rekod", peralatan);
			}
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/maklumatPeralatan/popupMaklumatPeralatan.vm";
	}

	@SuppressWarnings("static-access")
	@Command("saveMaklumatPeralatan")
	public String saveMaklumatPeralatan() throws ParseException {

		try {

			mp = new MyPersistence();
			UtilPeralatan peralatan = (UtilPeralatan) mp.find(
					UtilPeralatan.class, get("idMaklumatPeralatan"));
			Boolean addMaklumatPeralatan = false;

			if (peralatan == null) {
				addMaklumatPeralatan = true;
				peralatan = new UtilPeralatan();
			}

			peralatan.setDewan((UtilDewan) mp.find(UtilDewan.class,
					get("idDewan")));
			peralatan.setNama(get("nama"));
			peralatan.setKuantiti(get("kuantiti"));
			peralatan.setCatatan(get("catatan"));
			peralatan.setKadarSewa(Double.valueOf(util
					.RemoveComma(get("kadarSewa"))));

			mp.begin();
			if (addMaklumatPeralatan)
				mp.persist(peralatan);
			mp.commit();
			// String statusInfo = "success";
		} catch (Exception e) {
			System.out.println("Error editMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatPeralatan();
	}

	@Command("removeMaklumatPeralatan")
	public String removeMaklumatPeralatan() {

		try {
			mp = new MyPersistence();
			UtilPeralatan cadangan = (UtilPeralatan) mp.find(
					UtilPeralatan.class, get("idMaklumatPeralatan"));
			mp.begin();
			mp.remove(cadangan);
			mp.commit();

		} catch (Exception e) {
			System.out.println("Error removeMaklumatPeralatan : "
					+ e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getMaklumatPeralatan();
	}

	/** END MAKLUMAT PERALATAN **/

	/** START DROP DOWN LIST **/

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
}

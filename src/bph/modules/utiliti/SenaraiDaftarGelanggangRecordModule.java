package bph.modules.utiliti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Bandar;
import bph.entities.kod.Negeri;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiDaftarGelanggangRecordModule extends
		LebahRecordTemplateModule<UtilGelanggang> {

	/**
	 * 
	 */
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
	public void afterSave(UtilGelanggang r) {

	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void begin() {
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection")
				.getString("folder"));
		dataUtil = DataUtil.getInstance(db);
		context.put("selectDewan", dataUtil.getListDewan());
		List<Negeri> negeriList = dataUtil.getListNegeri();
		context.put("findNegeri", negeriList);
		context.put("selectNegeri", negeriList);
		context.put("findDewan", dataUtil.getListDewan());
		context.put("dataUtil", dataUtil);
		context.put("path", getPath());

	}

	@Override
	public boolean delete(UtilGelanggang r) throws Exception {

		return true;
	}

	@Override
	public String getPath() {
		return "bph/modules/utiliti/senaraiDaftarGelanggang";
	}

	@Override
	public Class<UtilGelanggang> getPersistenceClass() {
		return UtilGelanggang.class;
	}

	@Override
	public void getRelatedData(UtilGelanggang r) {

	}

	@SuppressWarnings("static-access")
	@Override
	public void save(UtilGelanggang simpan) throws Exception {
		try {
			mp = new MyPersistence();
			simpan.setNama(get("nama"));
			simpan.setAlamat1(get("alamat1"));
			simpan.setAlamat2(get("alamat2"));
			simpan.setAlamat3(get("alamat3"));
			simpan.setLokasi(get("lokasi"));
			simpan.setPoskod(get("poskod"));
			simpan.setBandar((Bandar) mp.find(Bandar.class, get("idBandar")));
			simpan.setDewan((UtilDewan) mp.find(UtilDewan.class, get("idDewan")));
			simpan.setStatus("1");
			simpan.setCatatan(get("catatan"));
			simpan.setKadarSewa(Double.valueOf(util
					.RemoveComma(get("kadarSewa"))));
			simpan.setKadarSewaAwam(Double.valueOf(util
					.RemoveComma(get("kadarSewaAwam"))));
			simpan.setWaktuBuka(get("waktuBuka"));
			simpan.setWaktuTutup(get("waktuTutup"));
			simpan.setWaktuBukaSiang(get("waktuBukaSiang"));
			simpan.setWaktuTutupSiang(get("waktuTutupSiang"));
			simpan.setWaktuBukaMalam(get("waktuBukaMalam"));
			simpan.setWaktuTutupMalam(get("waktuTutupMalam"));
		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("nama", get("find_nama"));
		map.put("dewan.nama", get("find_dewan"));
		map.put("dewan.bandar.id", new OperatorEqualTo(get("findBandar")));
		map.put("dewan.bandar.negeri.id",
				new OperatorEqualTo(get("findNegeri")));
		map.put("lokasi", get("find_lokasi"));
		return map;
	}

	/** START DROP DOWN LIST **/
	@Command("findDewan")
	public String findDewan() throws Exception {
		List<UtilDewan> list = dataUtil.getListDewan();
		context.put("findDewan", list);
		return getPath() + "/findDewan.vm";
	}

	/** START DROP DOWN LIST **/
	@Command("findBandar")
	public String findBandar() throws Exception {

		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("findBandar", list);

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

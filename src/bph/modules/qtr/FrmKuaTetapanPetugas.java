package bph.modules.qtr;

import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.qtr.KuaTetapanPetugas;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaTetapanPetugas extends LebahRecordTemplateModule<Users> {

	private static final long serialVersionUID = 1L;
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(Users arg0) {

	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void begin() {
		context.remove("checkPenyelia");
		context.remove("checkPTKunci");
		context.remove("checkPTAdun");

		DataUtil.getInstance(db);
		// TODO Auto-generated method stub
		this.addFilter("role.name='(QTR) Penyedia'");
		setHideDeleteButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		// setDisableKosongkanUpperButton(true);
		setDisableUpperBackButton(true);
		// setDisableSaveAddNewButton(true);
		setDisableAddNewRecordButton(true);
	}

	@Override
	public boolean delete(Users arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/qtr/taskPetugas";
	}

	@Override
	public Class<Users> getPersistenceClass() {
		// TODO Auto-generated method stub
		return Users.class;
	}

	@Override
	public void getRelatedData(Users r) {
		// TODO Auto-generated method stub
		mp = new MyPersistence();
		String flagPenyelia = "";
		String flagPTKunci = "";
		String flagPTAdun = "";

		KuaTetapanPetugas petugas = (KuaTetapanPetugas) mp
				.get("SELECT u FROM KuaTetapanPetugas u WHERE u.petugas.id = '"
						+ r.getId() + "'");

		if (petugas != null) {
			flagPenyelia = petugas.getPTPenyelia();
			flagPTKunci = petugas.getPTKunci();
			flagPTAdun = petugas.getPTAdun();
		}

		if (flagPenyelia.equalsIgnoreCase("Y")) {
			context.put("checkPenyelia", true);
		} else {
			context.put("checkPenyelia", false);
		}

		if (flagPTKunci.equalsIgnoreCase("Y")) {
			context.put("checkPTKunci", true);
		} else {
			context.put("checkPTKunci", false);
		}

		if (flagPTAdun.equalsIgnoreCase("Y")) {
			context.put("checkPTAdun", true);
		} else {
			context.put("checkPTAdun", false);
		}
	}

	@Override
	public void save(Users arg0) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("pemohon.noKP", getParam("findUserNoKP"));
		r.put("pemohon.userName", getParam("findUserName"));

		return r;
	}

	@SuppressWarnings("unused")
	@Command("hantarTetapan")
	public String hantarTetapan() {
		try {
			mp = new MyPersistence();

			boolean newRecord = false;
			KuaTetapanPetugas petugas = (KuaTetapanPetugas) mp
					.get("SELECT u FROM KuaTetapanPetugas u WHERE u.petugas.id = '"
							+ getParam("idPetugas") + "'");
			if (petugas == null) {
				petugas = new KuaTetapanPetugas();
				newRecord = true;
			}

			String flagPenyelia = "T";
			String flagPTKunci = "T";
			String flagPTAdun = "T";

			String[] peranan = request.getParameterValues("flagPeranan");
			if (peranan != null) {
				for (int i = 0; i < peranan.length; i++) {
					if (peranan[i].equalsIgnoreCase("Penyelia")) {
						flagPenyelia = "Y";
					}
					if (peranan[i].equalsIgnoreCase("PtKunci")) {
						flagPTKunci = "Y";
					}
					if (peranan[i].equalsIgnoreCase("PtAdun")) {
						flagPTAdun = "Y";
					}
				}
			}
			mp.begin();
			petugas.setPetugas((Users) mp.find(Users.class,
					getParam("idPetugas")));
			petugas.setPTPenyelia(flagPenyelia);
			petugas.setPTKunci(flagPTKunci);
			petugas.setPTAdun(flagPTAdun);
			if (newRecord = true)
				mp.persist(petugas);
			mp.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		getRelatedData((Users) mp.find(Users.class, getParam("idPetugas")));
		return getPath() + "/entry_page.vm";
	}
}

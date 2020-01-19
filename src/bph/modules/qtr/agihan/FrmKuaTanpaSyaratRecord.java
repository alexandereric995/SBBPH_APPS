package bph.modules.qtr.agihan;

import java.util.Date;

import lebah.portal.action.Command;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenolakan;
import bph.entities.qtr.KuaPermohonan;
import bph.modules.qtr.FrmKuaAgihanRecord;
import bph.utils.DataUtil;

public class FrmKuaTanpaSyaratRecord extends FrmKuaAgihanRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134008276663362859L;
	private DataUtil dataUtil;

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setRecordOnly(true);
		setDisableKosongkanUpperButton(true);
		setDisableSaveAddNewButton(true);
		setDisableDefaultButton(true);
		setDisableUpperBackButton(true);
		setReadonly(true);

		addFilter("status.id = '1432591089524'");

		userRole = (String) request.getSession().getAttribute("_portal_role");

		String[] role = userRole.split(" ");

		for (int i = 0; i < role.length; i++) {
			if ("Penyemak".equals(role[i])) {
				context.put("currentRoleQTR", role[i]);
			}
		}

		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
	}

	@Override
	public void getRelatedData(KuaAgihan r) {

		KelasKuarters k1 = db.find(KelasKuarters.class, r.getPekerjaan()
				.getGredJawatan().getKelasKuarters());
		KelasKuarters k2 = db.find(KelasKuarters.class, dataUtil
				.getKelasDowngrade(r.getPekerjaan().getGredJawatan()
						.getKelasKuarters()));

		String idLokasi = r.getPermohonan().getLokasi().getId();
		// String kategoriPenghuni =
		// dataUtil.getKategoriPenghuni(r.getPemohon().getId());
		String kelas1 = k1.getId();
		String kelas2 = "";

		context.put("kuarters", dataUtil.getListKuartersTawaran(idLokasi, kelas1));

		if (k2 != null) {
			kelas2 = k2.getId();
			context.put("kuarters", dataUtil.getListKuartersTawaran(idLokasi, kelas1,
					kelas2));
		}

		context.put("kelas2", dataUtil.getKelasDowngrade(r.getPekerjaan()
				.getGredJawatan().getKelasKuarters()));

		if (r.getKuarters() != null)
			context.put("kuarters", db.find(KuaKuarters.class, r.getKuarters()
					.getId()));
	}

	@Command("agihKuarters")
	public String agihKuarters() {
		boolean success = false;

		KuaAgihan ka = db.find(KuaAgihan.class, getParam("idAgihan"));
		KuaPermohonan kp = db.find(KuaPermohonan.class, ka.getPermohonan()
				.getId());
		KuaKuarters kk = db.find(KuaKuarters.class,
				getParam("valuePilihKuarters"));
		KuaPenolakan penolakan = (KuaPenolakan) db
				.get("SELECT p FROM KuaPenolakan p WHERE p.agihan.id = '"
						+ ka.getId() + "'");
		KuaPenolakan p = db.find(KuaPenolakan.class, penolakan.getId());

		if (kk != null) {
			if (ka != null) {
				ka.setKuarters(kk);
				ka.setTarikhKemaskini(new Date());
				ka.setStatus(db.find(Status.class, "1431294274203"));

				if (kp != null) {
					kp.setStatus(db.find(Status.class, "1423101446188"));
					kp.setTarikhKemaskini(new Date());
				}
			}

			// kk.setKapasitiSemasa(kk.getKapasitiSemasa() + 1);
			// kk.setKekosongan(1);
			kk.setFlagAgihan(1);
		}

		db.begin();
		if (p != null)
			db.remove(p);
		try {
			db.commit(request, "PROCESSING FILE (Agihan Kuarters) : "
					+ ka.getId() + "|" + kk.getId(), 2);
			success = true;
		} catch (Exception e) {

			e.printStackTrace();
		}

		context.put("success", success);

		return getPath() + "/result/agihKuarters.vm";
	}
}

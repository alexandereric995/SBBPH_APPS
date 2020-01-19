package bph.modules.qtr.agihan;

import bph.entities.kod.KelasKuarters;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKuarters;
import bph.modules.qtr.FrmKuaAgihanRecord;
import bph.utils.DataUtil;

public class FrmKuaKelasBRecord extends FrmKuaAgihanRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134008276663362859L;
	private DataUtil dataUtil;

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		dataUtil = DataUtil.getInstance(db);

		setRecordOnly(true);
		setDisableKosongkanUpperButton(true);
		setDisableSaveAddNewButton(true);
		setDisableDefaultButton(true);
		setDisableUpperBackButton(true);
		setReadonly(true);

		addFilter("status.id <> '1431327994521'");
		addFilter("pekerjaan.gredJawatan.kelasKuarters = 'B'");
		setOrderBy("noGiliran");
		setOrderType("ASC");

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
		// TODO Auto-generated method stub
		KelasKuarters k1 = db.find(KelasKuarters.class, r.getPekerjaan()
				.getGredJawatan().getKelasKuarters());
		KelasKuarters k2 = db.find(KelasKuarters.class, dataUtil
				.getKelasDowngrade(r.getPekerjaan().getGredJawatan()
						.getKelasKuarters()));

		String idLokasi = r.getPermohonan().getLokasi().getId();
		String kategoriPenghuni = dataUtil.getKategoriPenghuni(r.getPemohon()
				.getId());
		String kelas1 = k1.getId();

		context.put("kuarters", dataUtil.getListKuarters(idLokasi,
				kategoriPenghuni, kelas1));

		if (r.getPermohonan().getPerakuan() == 1
				&& !"G".equals(r.getPekerjaan().getGredJawatan()
						.getKelasKuarters())) {
			String kelas2 = k2.getId();
			context.put("kuarters", dataUtil.getListKuarters(idLokasi,
					kategoriPenghuni, kelas1, kelas2));
		}

		context.put("kelas2", dataUtil.getKelasDowngrade(r.getPekerjaan()
				.getGredJawatan().getKelasKuarters()));

		if (r.getKuarters() != null)
			context.put("kuarters", db.find(KuaKuarters.class, r.getKuarters()
					.getId()));
	}
}

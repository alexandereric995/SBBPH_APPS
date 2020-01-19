package bph.modules.qtr;

import java.util.List;

import portal.module.entity.Users;
import bph.entities.kod.KelasKuarters;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaKuarters;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaAgihanSubRecord extends FrmKuaAgihanRecord {

	private static final long serialVersionUID = -4694709486982124681L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		setDisableKosongkanUpperButton(true);
		setDisableSaveAddNewButton(true);
		setDisableDefaultButton(true);
		setDisableUpperBackButton(true);
		setDisableAddNewRecordButton(true);
		setDisableBackButton(true);
		setReadonly(true);
		setRecordOnly(true);
		setDisableInfoPaparLink(true);

		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");

		addFilter("pemohon.id = '" + userId + "'");
		addFilter("idStatus = '1432614959825'");
		setOrderBy("kelasKuarters ASC");
		setOrderBy("noGiliran ASC");

		context.put("bersyarat", "tidak");
		context.put("personal", "ya");
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findLokasiKuarters", dataUtil.getListLokasiPermohonan());

		try {
			mp = new MyPersistence();
			Users objUser = (Users) mp.find(Users.class, userId);
			context.put("objUser", objUser);

			// sementara
			if (objUser.getId().toString().equalsIgnoreCase("831110065559")) {
				context.put("dev", "true");
			} else {
				context.put("dev", "false");
			}
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaAgihan r) {

		try {
			mp = new MyPersistence();

			KuaAgihan rr = (KuaAgihan) mp.find(KuaAgihan.class, r.getId());
			context.put("r", rr);

			String kelas1 = "";
			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r
					.getKelasKuarters());
			String idLokasi = r.getPermohonan().getLokasi().getId();
			if (k1 != null) {
				kelas1 = k1.getId();
			} else {
				kelas1 = r.getKelasKuarters();
			}

			context
					.put("kuarters", dataUtil
							.getListKuartersTawaran(idLokasi, kelas1));
			context.put("kelas1", kelas1);
			if ("".equals(r.getPermohonan().getFlagDowngrade()))
				context.put("kelas2", dataUtil.getKelasDowngrade(r
						.getKelasKuarters()));

			if (r.getKuarters() != null)
				context.put("kuarters", mp.find(KuaKuarters.class, r
						.getKuarters().getId()));

			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN ("
					+ getPT() + ")");
			context.put("selectPetugas", users);

		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
}

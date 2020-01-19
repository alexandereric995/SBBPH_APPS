package bph.modules.qtr;

import java.util.Date;

import lebah.portal.action.Command;
import portal.module.entity.Users;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.KodHasil;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenolakan;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaSuratTawaranRecord extends FrmKuaAgihanRecord {
	private static final long serialVersionUID = -1108126530610535802L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableKosongkanUpperButton(true);
		setDisableSaveAddNewButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		setDisableUpperBackButton(true);
		setReadonly(true);
		userId = (String) request.getSession().getAttribute("_portal_login");
		addFilter("permohonan.status.id = '1419601227595'");
		context.put("suratTawaran", true);
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("path", getPath());
	}

	@Override
	public void getRelatedData(KuaAgihan r) {
		try {
			mp = new MyPersistence();
			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r
					.getPekerjaan().getGredJawatan().getKelasKuarters());
			KelasKuarters k2 = (KelasKuarters) mp.find(KelasKuarters.class,
					dataUtil.getKelasDowngrade(r.getPekerjaan()
							.getGredJawatan().getKelasKuarters()));
			String idLokasi = r.getPermohonan().getLokasi().getId();
			String kategoriPenghuni = dataUtil.getKategoriPenghuni(r
					.getPemohon().getId());
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
				context.put("kuarters", mp.find(KuaKuarters.class, r
						.getKuarters().getId()));
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	
	@Command("getSuratTawaran")
	public String getSuratTawaran() {
		boolean newPenolakan = false;
		boolean newDeposit = false;
		try {
			mp = new MyPersistence();
			KuaAgihan r = (KuaAgihan) mp.find(KuaAgihan.class,
					getParam("idAgihan"));
			KuaPenolakan kp = null;
			KuaAkaun ka = null;

			KuaPenolakan penolakan = (KuaPenolakan) mp
					.get("SELECT p FROM KuaPenolakan p WHERE p.agihan.id = '"
							+ r.getId() + "'");
			KuaAkaun deposit = (KuaAkaun) mp
					.get("SELECT d FROM KuaAkaun d WHERE d.noAkaun = '"
							+ r.getPermohonan().getNoPermohonan() + "'");

			if (penolakan != null)
				kp = (KuaPenolakan) mp.find(KuaPenolakan.class, penolakan
						.getId());
			else
				kp = new KuaPenolakan();
			newPenolakan = true;

			if (deposit != null)
				ka = (KuaAkaun) mp.find(KuaAkaun.class, deposit.getId());
			else
				ka = new KuaAkaun();
			newDeposit = true;

			kp.setAgihan(r);
			if (kp.getTarikhMasuk() == null)
				kp.setTarikhMasuk(new Date());

			ka.setKodHasil((KodHasil) mp.find(KodHasil.class, "72310"));
			ka.setNoInvois(getLongTransactionNo());
			ka.setTarikhInvois(new Date());
			ka.setKeterangan("DEPOSIT KUARTERS");
			ka.setBilanganUnit(1);
			ka.setId(userId);
			ka
					.setPembayar((Users) mp.find(Users.class, r.getPemohon()
							.getId()));
			if (ka.getTarikhMasuk() == null)
				ka.setTarikhMasuk(new Date());

			mp.begin();
			if (newPenolakan == true)
				mp.persist(kp);
			if (newDeposit == true)
				mp.persist(ka);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error getSuratTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/suratTawaran.vm";
	}
}

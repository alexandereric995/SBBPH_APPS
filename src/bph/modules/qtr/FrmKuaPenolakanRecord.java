package bph.modules.qtr;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.GelaranDalamSurat;
import bph.entities.kod.JenisPenolakan;
import bph.entities.kod.KodHasil;
import bph.entities.kod.SebabPenolakan;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenolakan;
import bph.entities.qtr.KuaPermohonan;
import bph.mail.mailer.TawaranKuartersMailer;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaPenolakanRecord extends
		LebahRecordTemplateModule<KuaPenolakan> {

	private static final long serialVersionUID = 7591790327982249488L;
	static Logger myLogger = Logger
			.getLogger("bph/modules/qtr/FrmKuaPenolakanRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;


	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaPenolakan> getPersistenceClass() {
		return KuaPenolakan.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/penolakan";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableAddNewRecordButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		userId = (String) request.getSession().getAttribute("_portal_login");
		addFilter("agihan.petugas.id = '" + userId + "'");
		addFilter("statusTawaran IS NULL");
		addFilterOr("statusTawaran = ''");
		context.put("selectJenisPenolakan", dataUtil.getListJenisPenolakan());
		context.put("selectSebabPenolakan", dataUtil.getListSebabPenolakan());
		context.put("selectGelaranDalamSurat", dataUtil
				.getListGelaranDalamSurat());
		context.put("path", getPath());
	}

	@Override
	public void save(KuaPenolakan r) throws Exception {
		JenisPenolakan jp = db.find(JenisPenolakan.class,
				getParam("idJenisPenolakan"));
		SebabPenolakan sp = db.find(SebabPenolakan.class,
				getParam("idSebabPenolakan"));
		r.setTarikhSuratDiterima(getDate("tarikhSuratDiterima"));
		r.setBil(getParam("bil"));
		r.setTarikhSuratSebenar(getDate("tarikhSurat"));
		r.setCatatan(getParam("catatan"));
		r.setJenisPenolakan(jp);
		r.setSebabPenolakan(sp);
		r.setTarikhKemaskini(new Date());
		r.setStatusTawaran(getParam("statusTawaran"));
	}

	@Override
	public boolean delete(KuaPenolakan r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("agihan.permohonan.noPermohonan", getParam("findNoPermohonan"));
		r.put("agihan.pemohon.userName", getParam("findNamaPemohon"));
		r.put("agihan.pemohon.noKP", getParam("findNoKPPemohon"));
		return r;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterSave(KuaPenolakan r) {
		String jenisPenolakan = getParam("idJenisPenolakan");
		String idStatus = "";
		String idStatusAgihan = "";
		boolean newDeposit = false;
		try {
			mp = new MyPersistence();
			KuaAgihan ka = (KuaAgihan) mp.find(KuaAgihan.class, r.getAgihan()
					.getId());
			KuaKuarters kk = (KuaKuarters) mp.find(KuaKuarters.class, r
					.getAgihan().getKuarters().getId());
			KuaPermohonan kp = (KuaPermohonan) mp.find(KuaPermohonan.class, r
					.getAgihan().getPermohonan().getId());
			KuaAkaun deposit = (KuaAkaun) mp
					.get("SELECT d FROM KuaAkaun d WHERE d.kodHasil.id = '72310' AND d.pembayar.id = '"
							+ kp.getPemohon().getId() + "'");
			KuaAkaun kd = null;

			idStatusAgihan = "1431327994521";
			if ("1".equals(jenisPenolakan))
				idStatus = "1431405647231"; // PENOLAKAN BERSYARAT
			else if ("2".equals(jenisPenolakan))
				idStatus = "1432591089524"; // PENOLAKAN TANPA SYARAT
			else if ("3".equals(jenisPenolakan))
				idStatus = "1431405647234"; // PENOLAKAN DOWNGRADE
			else if (deposit != null)
				kd = (KuaAkaun) mp.find(KuaAkaun.class, deposit.getId());

			if (kd == null) {
				kd = new KuaAkaun();
				newDeposit = true;
			}

			idStatus = "1419601227595";// PERMOHONAN DILULUSKAN
			idStatusAgihan = "1431327994498"; // KUARTERS TELAH DITERIMA
			kd.setKodHasil((KodHasil) mp.find(KodHasil.class, "72310"));
			kd.setNoInvois(getLongTransactionNo());
			kd.setTarikhInvois(new Date());
			kd.setKeterangan("DEPOSIT KUARTERS");
			kd.setAmaunBayaranSeunit(Double.parseDouble(getParam("deposit")
					.replaceAll(",", "")));
			kd.setDebit(Double.parseDouble(getParam("deposit").replaceAll(",",
					"")));
			kd.setBilanganUnit(1);
			kd.setIdMasuk((Users) mp.find(Users.class, r.getAgihan()
					.getPemohon().getId()));
			kd.setPembayar((Users) mp.find(Users.class, r.getAgihan()
					.getPemohon().getId()));
			if (kd.getTarikhMasuk() == null)
				kd.setTarikhMasuk(new Date());
			ka.setStatus((Status) mp.find(Status.class, idStatusAgihan));
			if (!"".equals(jenisPenolakan)) {
				ka.setKuarters(null);
				ka.setNoGiliran(0);
				kk.setFlagAgihan(0);
			}

			kp.setStatus((Status) mp.find(Status.class, idStatus));
			mp.begin();
			if (newDeposit == true)
				mp.persist(kd);
			if ("3".equals(jenisPenolakan))
				mp.remove(ka);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error afterSave : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void getRelatedData(KuaPenolakan r) {
		try {
			mp = new MyPersistence();
			UsersJob uj = (UsersJob) mp
					.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"
							+ r.getAgihan().getPemohon().getId() + "'");
			context.put("uj", uj);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("getSuratTawaran")
	public String getSuratTawaran() {
		try {
			mp = new MyPersistence();
			KuaPenolakan penolakan = (KuaPenolakan) mp.find(KuaPenolakan.class,
					getParam("idPenolakan"));
			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, penolakan.getAgihan().getPermohonan()
							.getId());
			GelaranDalamSurat gds = (GelaranDalamSurat) mp.find(
					GelaranDalamSurat.class, getParam("idGelaranDalamSurat"));
			Date d = getDate("tarikhSurat");
			if (d == null)
				d = new Date();
			if (!"Y".equals(penolakan.getGenerateEmail()))
				TawaranKuartersMailer.get().emelNotifikasiTawaran(
						permohonan.getPemohon().getEmel(),
						permohonan.getTarikhPermohonan());
			if (penolakan.getTarikhSurat() == null)
				penolakan.setTarikhSurat(new Date());
			penolakan.setTarikhSuratSebenar(getDate("tarikhSurat"));
			penolakan.setTitleDalamSurat(gds);
			penolakan.setNoFail(getParam("noFail"));
			penolakan.setBil(getParam("bil"));
			penolakan.setCetakSurat("ya");
			penolakan.setKepada(getParam("kepadaSiapa"));
			penolakan.setGenerateEmail("Y");
			mp.begin();
			mp.commit();
			context.put("idPenolakan", penolakan.getId());
		} catch (Exception e) {
			System.out.println("Error getSuratTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/suratTawaran.vm";
	}

	@Command("batalPermohonan")
	public String batalPermohonan() {
		try {
			mp = new MyPersistence();
			KuaPenolakan penolakan = (KuaPenolakan) mp.find(KuaPenolakan.class,
					getParam("idPenolakan"));
			KuaAgihan agihan = (KuaAgihan) mp.find(KuaAgihan.class, penolakan
					.getAgihan().getId());
			KuaKuarters kuarters = (KuaKuarters) mp.find(KuaKuarters.class,
					agihan.getKuarters().getId());
			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, agihan.getPermohonan().getId());
			kuarters.setFlagAgihan(0);
			agihan.setStatus((Status) mp.find(Status.class, "1431327994524"));
			agihan.setKuarters(null);
			permohonan.setStatus((Status) mp
					.find(Status.class, "1431327994524")); // PERMOHONAN
			// DIBATALKAN
			mp.begin();
			mp.remove(penolakan);
			mp.commit();
			context.put("idPenolakan", penolakan.getId());
		} catch (Exception e) {
			System.out.println("Error getSuratTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/sub_page/suratTawaran.vm";
	}
}

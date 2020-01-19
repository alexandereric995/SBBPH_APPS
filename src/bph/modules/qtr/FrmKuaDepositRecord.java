package bph.modules.qtr;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import bph.entities.kod.Status;
import bph.entities.kod.StatusKuarters;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaAkaun;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.qtr.KuaTawaran;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaDepositRecord extends LebahRecordTemplateModule<KuaAkaun> {
	private static final long serialVersionUID = 3419244770420513670L;
	private DataUtil dataUtil;
	private MyPersistence mp;


	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaAkaun> getPersistenceClass() {
		return KuaAkaun.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/deposit";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);

		setDisableBackButton(true);
		setDisableUpperBackButton(true);
		setReadonly(true);
		setDisableAddNewRecordButton(true);

		addFilter("kodHasil.id = '72310'");
		addFilter("idPenghuni IS NULL");
		addFilter("flagBayar = 'Y'");
		setOrderBy("tarikhMasuk DESC");

		context.put("selectKodPusatTerima", dataUtil.getListPusatTerima());
		context.put("selectKodKementerian", dataUtil.getListKementerian());
		context.put("now", new Date());
		context.put("path", getPath());
	}

	@Override
	public void save(KuaAkaun r) throws Exception {

	}

	@Override
	public boolean delete(KuaAkaun r) throws Exception {
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("pembayar.userName", getParam("findPenamaAkaun"));
		r.put("pembayar.noKP", getParam("findICPenamaAkaun"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(KuaAkaun r) {

	}

	@Override
	public void getRelatedData(KuaAkaun r) {
		try {
			mp = new MyPersistence();
			KuaTawaran tawaran = (KuaTawaran) mp.get("SELECT p FROM KuaTawaran p WHERE p.statusTawaran = '01' AND p.agihan.pemohon.id = '"+ r.getPembayar().getId() + "'");
			context.put("tawaran", tawaran);
			String tarikhTamatTempoh=getTarikhTamatSurat(tawaran.getTarikhSuratDiterima(), "dd-MM-yyyy");
			context.put("tarikhTamatTempoh", tarikhTamatTempoh);
			KuaAgihan checkAgihan = (KuaAgihan) mp.get("SELECT a FROM KuaAgihan a WHERE a.pemohon.id = '"+ r.getIdPembayar()+ "' AND a.kuarters.id IS NOT NULL AND a.noGiliran <> 0");
			context.put("checkAgihan", checkAgihan);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("setPenghuni")
	public String setPenghuni() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAkaun ka = (KuaAkaun) mp.find(KuaAkaun.class,
					getParam("idDeposit"));
			KuaAgihan agihan = (KuaAgihan) mp
					.get("SELECT a FROM KuaAgihan a WHERE a.pemohon.id = '"
							+ ka.getPembayar().getId()
							+ "' AND a.kuarters.id IS NOT NULL AND a.noGiliran <> 0");
			KuaKuarters kk = null;
			if (agihan != null) {
				kk = (KuaKuarters) mp.find(KuaKuarters.class, agihan
						.getKuarters().getId());
			}

			KuaPermohonan permohonan = (KuaPermohonan) mp.find(
					KuaPermohonan.class, agihan.getPermohonan().getId());
			KuaPenghuni kp = new KuaPenghuni();
			kp.setPermohonan(permohonan);
			kp.setPemohon(ka.getPembayar());
			kp.setKuarters(agihan.getKuarters());
			kp.setTarikhMasukKuarters(getDate("tarikhMasukKuarters"));
			kp.setIdWaris(getParam("MyIDWaris"));
			kp.setNamaWaris(getParam("namaPenuhWaris"));
			kp.setNoTelefonWaris(getParam("noTelefonWaris"));
			if ("BUJANG".equals(kk.getKategoriPenghuni())) {
				kk.setKapasitiSemasa(kk.getKapasitiSemasa() + 1);
				kk.setFlagAgihan(0);
			} else {
				kk.setStatusKuarters((StatusKuarters) mp.find(
						StatusKuarters.class, "01"));
				kk.setFlagAgihan(0);
			}

			permohonan.setStatus((Status) mp.find(Status.class, "1423101446117"));//PENGHUNI
			permohonan.setNoFail(getParam("noFail"));

			ka.setPenghuni(kp);
			ka.setTarikhKemaskini(new Date());
			agihan.setNoGiliran(0);
			agihan.setStatus((Status) mp.find(Status.class, "1423101446117"));//PENGHUNI
			mp.begin();
			mp.persist(kp);
			mp.commit();
			success = true;
		} catch (Exception e) {
			System.out.println("Error setPenghuni : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		return getPath() + "/result/resultSetPenghuni.vm";
	}

	@Command("batalTawaran")
	public String batalTawaran() {
		boolean success = false;
		try {
			mp = new MyPersistence();
			KuaAkaun ka = (KuaAkaun) mp.find(KuaAkaun.class,
					getParam("idDeposit"));
			KuaAgihan kuaAgihan = (KuaAgihan) mp
					.get("SELECT a FROM KuaAgihan a WHERE a.pemohon.id = '"
							+ ka.getPembayar().getId()
							+ "' AND a.kuarters.id IS NOT NULL AND a.noGiliran <> 0");
			KuaAgihan agihan = (KuaAgihan) mp.find(KuaAgihan.class, kuaAgihan
					.getId());
			KuaKuarters kuarters = (KuaKuarters) mp.find(KuaKuarters.class,
					agihan.getKuarters().getId());
			KuaTawaran tawaran = (KuaTawaran) mp
					.get("SELECT p FROM KuaTawaran p WHERE p.agihan.id = '"
							+ kuaAgihan.getId()
							+ "' AND p.statusTawaran = '01'");
			agihan.setStatus((Status) mp.find(Status.class, "1432591089524"));
			kuarters.setFlagAgihan(0);

			mp.begin();
			mp.remove(ka);
			mp.remove(tawaran);
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error batalTawaran : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("success", success);
		return getPath() + "/result/resultBatalTawaran.vm";
	}
	
	public static String getTempohTamatPotongGaji(String tarikh, String dateFormat) throws ParseException {
		DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		
		if (!"".equals(tarikh)) date = formatDate.parse(tarikh);
		
		String afterFormat = "";
		if (date != null) {
			if (date.toString().length() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.clear();
				
				cal.setTime(date);
				cal.add(Calendar.MONTH, 3);
				
				afterFormat = new SimpleDateFormat(dateFormat).format(cal.getTime());
			} else {
				afterFormat = "";
			}
		} else {
			afterFormat = "";
		}
		return afterFormat;
	}
	
	@SuppressWarnings("static-access")
	public static String getTarikhTamatSurat(Date date, String format) {
		String afterFormat = "";
		Locale malaysia = new Locale("ms","MY","MY");
		if (date != null) {
			if (date.toString().length() > 0) {
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();			
				cal1.setTime(date);
				cal2.add(cal1.DAY_OF_MONTH, 30);
				Date date1 = cal2.getTime();		
				afterFormat = new java.text.SimpleDateFormat(format,malaysia).format(date1);
			} else {
				afterFormat = "";
			}
		} else {
			afterFormat = "";
		}
		return afterFormat;
	}
}

package bph.modules.qtr;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kod.Bandar;
import bph.entities.kod.Fasa;
import bph.entities.kod.JenisKediaman;
import bph.entities.kod.JenisKegunaanKuarters;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.LokasiKuarters;
import bph.entities.kod.StatusKuarters;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.qtr.KuaSequenceKuarters;
import bph.entities.qtr.LogKuarters;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaKuartersRecord extends LebahRecordTemplateModule<KuaKuarters> {

	private static final long serialVersionUID = 7859853801709412089L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public Class<KuaKuarters> getPersistenceClass() {
		return KuaKuarters.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/qtr/kuarters";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("selectJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("selectJenisKegunaanKuarters", dataUtil.getListJenisKegunaanKuarters());
		context.put("selectStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("selectLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("selectLokasiDibenar", dataUtil.getListLokasiDibenar());
		context.put("selectFasa", dataUtil.getListFasa());
		// context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("findStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("findFasa", dataUtil.getListFasa());
		context.put("findNegeri", dataUtil.getList6Negeri());
		context.put("path", getPath());
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		this.setHideDeleteButton(true);
		//this.addFilter("flagDelete=0");
	}

	@Override
	public void save(KuaKuarters r) throws Exception {
		try {
			mp = new MyPersistence();
			KelasKuarters kelasKuarters = (KelasKuarters) mp.find(KelasKuarters.class, getParam("idKelasKuarters"));
			JenisKediaman jenisKediaman = (JenisKediaman) mp.find(JenisKediaman.class, getParam("idJenisKediaman"));
			JenisKegunaanKuarters jenisKegunaanKuarters = (JenisKegunaanKuarters) mp.find(JenisKegunaanKuarters.class,
					getParam("idJenisKegunaanKuarters"));
			StatusKuarters statusKuarters = (StatusKuarters) mp.find(StatusKuarters.class,
					getParam("idStatusKuarters"));
			LokasiKuarters lokasiKuarters = (LokasiKuarters) mp.find(LokasiKuarters.class,
					getParam("idLokasiKuarters"));
//			LokasiDibenar lokasiDibenar = (LokasiDibenar) mp.find(LokasiDibenar.class, getParam("idLokasiDibenar"));
			Fasa fasa = (Fasa) mp.find(Fasa.class, getParam("idFasa"));
			Bandar bandar = (Bandar) mp.find(Bandar.class, getParam("idBandar"));
			r.setKelas(kelasKuarters);
			r.setJenisKediaman(jenisKediaman);
			r.setJenisKegunaanKuarters(jenisKegunaanKuarters);
			r.setDeposit(Double.parseDouble(getParam("deposit").replaceAll("RM", "").replaceAll(",", "")));
			r.setSewa(Double.parseDouble(getParam("sewa").replaceAll("RM", "").replaceAll(",", "")));
			r.setStatusKuarters(statusKuarters);
			r.setFlagAktif(getParamAsInteger("pilihFlagAktif"));
			r.setCatatan(getParam("catatanFlagAktif"));
			r.setFasa(fasa);
			r.setLokasi(lokasiKuarters);
			//r.setLokasiDibenar(lokasiDibenar);
			r.setKawasan(getParam("kawasan"));
			r.setLotNo(getParam("lotNo"));
			r.setNoUnit(getParam("noUnit"));
			r.setAlamat1(getParam("alamat1"));
			r.setAlamat2(getParam("alamat2"));
			r.setAlamat3(getParam("alamat3"));
			r.setPoskod(getParam("poskod"));
			r.setBandar(bandar);
			r.setKapasiti(getParamAsInteger("kapasiti"));
			r.setKapasitiSemasa(getParamAsInteger("kapasitiSemasa"));
			r.setTarikhSerahan(getDate("tarikhSerahan"));
			r.setFlagDelete(getParamAsInteger("idFlagKuarters"));
			if (statusKuarters != null) {
				if ("03".equals(statusKuarters.getId())) {
					r.setFlagAgihan(0);
				}
			}
			if(getParamAsInteger("idFlagKuarters")==1)
			{
				r.setStatusKuarters((StatusKuarters) mp.find(StatusKuarters.class,"09"));
			}
			else
			{
				r.setStatusKuarters(statusKuarters);
			}
			context.put("success", true);
			simpanAuditTrailKuarters(r.getId());
		} catch (Exception e) {
			System.out.println("Error save : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public boolean delete(KuaKuarters r) throws Exception {
		try {
			mp = new MyPersistence();
			KuaKuarters kuarters = (KuaKuarters) mp.find(KuaKuarters.class, r.getId());
			kuarters.setFlagDelete(1);
			mp.begin();
			mp.commit();
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("bandar.negeri.id", getParam("findNegeri"));
		// r.put("lokasi.bandar.negeri.id", getParam("findNegeri"));
		r.put("lokasi.id", getParam("findLokasiKuarters"));
		r.put("alamat1", getParam("findJalan"));
		r.put("noUnit", getParam("findNoUnit"));
		r.put("noRujukan", getParam("findNoRujukan"));
		r.put("kapasiti", getParam("findKapasitiKuarters"));
		r.put("kelas.id", getParam("findKelasKuarters"));
		r.put("kategoriPenghuni", getParam("findKategoriPenghuni"));
		r.put("statusKuarters.id", getParam("findStatusKuarters"));
		r.put("fasa.id", getParam("findFasa"));
		r.put("statusKuarters.id", getParam("findDetailKuarters"));
		return r;
	}

	@Override
	public void beforeSave() {

	}

	@Override
	public void afterSave(KuaKuarters r) {
		if(r.getNoRujukan()==null)
		{
			int next = 0;
			db.begin();
			Calendar calCurrent = new GregorianCalendar();
			calCurrent.setTime(new Date());
			int bulan = calCurrent.get(Calendar.MONTH) + 1;
			int tahun = calCurrent.get(Calendar.YEAR);

			KuaSequenceKuarters seq;
			synchronized (this) {
				seq = (KuaSequenceKuarters) db.get("select x from KuaSequenceKuarters x where x.bulan = '" + bulan
						+ "' and x.tahun = '" + tahun + "'");
				if (seq == null) {
					seq = new KuaSequenceKuarters();
					next = 1;
					String idBaru = new DecimalFormat("0000").format(tahun) + new DecimalFormat("00").format(bulan);
					seq.setId(idBaru);
					seq.setBulan(bulan);
					seq.setTahun(tahun);
					seq.setBilangan(next);
					db.persist(seq);
				} else {
					next = seq.getBilangan() + 1;
					seq.setBilangan(next);
				}
			}
			String formatserial = new DecimalFormat("00").format(seq.getBilangan());
			String noRujukan = "BPH/QTR/" + tahun + "/" + new DecimalFormat("00").format(bulan) + "/" + formatserial;
			r.setNoRujukan(noRujukan);
			try {
				db.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//rozai edit sort by tarikh masuk 11/3/2019
	@Override
	public void getRelatedData(KuaKuarters r) {
		try {
			mp = new MyPersistence();
			context.put("listpenghuni", mp.list("SELECT p FROM KuaPenghuni p WHERE p.kuarters.id = '" + r.getId()
					+ "' ORDER BY p.tarikhMasukKuarters ASC"));
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");
		context.put("selectBandar", dataUtil.getListBandar(idNegeri));
		return getPath() + "/select/selectBandar.vm";
	}

	@Command("viewDetailPenghuni")
	public String viewDetailPenghuni() throws Exception {
		try {
			mp = new MyPersistence();
			String noKP = "";
			KuaPenghuni penghuni = (KuaPenghuni) mp.find(KuaPenghuni.class, getParam("idPenghuni"));
			noKP = penghuni.getPemohon().getNoKP();
			Users user = (Users) mp.get("SELECT d FROM Users d WHERE d.noKP ='" + noKP + "'");
			UsersJob pekerjaan = (UsersJob) mp.get("SELECT x FROM UsersJob x WHERE x.users.id ='" + noKP + "'");
			context.put("pemohon", user);
			context.put("uj", pekerjaan);
		} catch (Exception e) {
			System.out.println("Error viewDetailPenghuni : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/penghuni.vm";
	}

	@Command("findJalan")
	public String findJalan() throws Exception {
		String idLokasi = "0";
		if (get("findLokasiKuarters").trim().length() > 0)
			idLokasi = get("findLokasiKuarters");
		context.put("findJalan", dataUtil.getListJalanKuarters(idLokasi));
		return getPath() + "/find/findJalan.vm";
	}

	@Command("findLokasiKuarters")
	public String findLokasiKuarters() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters(idNegeri));
		return getPath() + "/find/findLokasiKuarters.vm";
	}

	@Command("findDetailKuarters")
	public String findDetailKuarters() throws Exception {
		String idStatus = "0";
		if (getParam("status_kuarters") != null) {
			idStatus = getParam("status_kuarters");
		}
		List<StatusKuarters> list = dataUtil.getListDetailKuarters(idStatus);
		context.put("findDetailKuarters", list);
		return getPath() + "/find/findDetailKuarters.vm";
	}

	@Command("kemaskiniMaklumat")
	public String kemaskiniMaklumat() {
		try {
			mp = new MyPersistence();
			KuaKuarters kuarters = (KuaKuarters) mp.find(KuaKuarters.class, getParam("idKuarters"));
			KelasKuarters kelasKuarters = (KelasKuarters) mp.find(KelasKuarters.class, getParam("idKelasKuarters"));
			JenisKediaman jenisKediaman = (JenisKediaman) mp.find(JenisKediaman.class, getParam("idJenisKediaman"));
			JenisKegunaanKuarters jenisKegunaanKuarters = (JenisKegunaanKuarters) mp.find(JenisKegunaanKuarters.class,
					getParam("idJenisKegunaanKuarters"));
			StatusKuarters statusKuarters = (StatusKuarters) mp.find(StatusKuarters.class,
					getParam("idStatusKuarters"));
			LokasiKuarters lokasiKuarters = (LokasiKuarters) mp.find(LokasiKuarters.class,
					getParam("idLokasiKuarters"));
//			LokasiDibenar lokasiDibenar = (LokasiDibenar) mp.find(LokasiDibenar.class, getParam("idLokasiDibenar"));
			Fasa fasa = (Fasa) mp.find(Fasa.class, getParam("idFasa"));
			Bandar bandar = (Bandar) mp.find(Bandar.class, getParam("idBandar"));
			kuarters.setKelas(kelasKuarters);
			kuarters.setJenisKediaman(jenisKediaman);
			kuarters.setJenisKegunaanKuarters(jenisKegunaanKuarters);
			kuarters.setDeposit(Double.parseDouble(getParam("deposit").replaceAll("RM", "").replaceAll(",", "")));
			kuarters.setSewa(Double.parseDouble(getParam("sewa").replaceAll("RM", "").replaceAll(",", "")));
			kuarters.setStatusKuarters(statusKuarters);
			kuarters.setFlagAktif(getParamAsInteger("pilihFlagAktif"));
			kuarters.setCatatan(getParam("catatanFlagAktif"));
			kuarters.setFasa(fasa);
			kuarters.setLokasi(lokasiKuarters);
			//kuarters.setLokasiDibenar(lokasiDibenar);
			kuarters.setKawasan(getParam("kawasan"));
			kuarters.setLotNo(getParam("lotNo"));
			kuarters.setNoUnit(getParam("noUnit"));
			kuarters.setAlamat1(getParam("alamat1"));
			kuarters.setAlamat2(getParam("alamat2"));
			kuarters.setAlamat3(getParam("alamat3"));
			kuarters.setPoskod(getParam("poskod"));
			kuarters.setBandar(bandar);
			kuarters.setKapasiti(getParamAsInteger("kapasiti"));
			kuarters.setKapasitiSemasa(getParamAsInteger("kapasitiSemasa"));
			kuarters.setTarikhSerahan(getDate("tarikhSerahan"));
			simpanAuditTrailKuarters(kuarters.getId());
			context.put("success", true);			
		} catch (Exception e) {
			System.out.println("Error kemaskiniMaklumat : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/entry_page.vm";
	}

	public void simpanAuditTrailKuarters(String idKuarters) {
		mp = new MyPersistence();
		KuaKuarters kuarters = (KuaKuarters) mp.find(KuaKuarters.class, idKuarters);
		LogKuarters logKuarters = new LogKuarters();
		if (kuarters != null) {
			KelasKuarters kelasKuarters = (KelasKuarters) mp.find(KelasKuarters.class, getParam("idKelasKuarters"));
			JenisKediaman jenisKediaman = (JenisKediaman) mp.find(JenisKediaman.class, getParam("idJenisKediaman"));
			JenisKegunaanKuarters jenisKegunaanKuarters = (JenisKegunaanKuarters) mp.find(JenisKegunaanKuarters.class,getParam("idJenisKegunaanKuarters"));
			StatusKuarters statusKuarters = (StatusKuarters) mp.find(StatusKuarters.class,getParam("idStatusKuarters"));
			LokasiKuarters lokasiKuarters = (LokasiKuarters) mp.find(LokasiKuarters.class,getParam("idLokasiKuarters"));
//			LokasiDibenar lokasiDibenar = (LokasiDibenar) mp.find(LokasiDibenar.class, getParam("idLokasiDibenar"));
			Fasa fasa = (Fasa) mp.find(Fasa.class, getParam("idFasa"));
			Bandar bandar = (Bandar) mp.find(Bandar.class, getParam("idBandar"));
			logKuarters.setKelas(kelasKuarters);
			logKuarters.setJenisKediaman(jenisKediaman);
			logKuarters.setJenisKegunaanKuarters(jenisKegunaanKuarters);
			logKuarters.setDeposit(Double.parseDouble(getParam("deposit").replaceAll("RM", "").replaceAll(",", "")));
			logKuarters.setSewa(Double.parseDouble(getParam("sewa").replaceAll("RM", "").replaceAll(",", "")));
			logKuarters.setStatusKuarters(statusKuarters);
			logKuarters.setFlagAktif(getParamAsInteger("pilihFlagAktif"));
			logKuarters.setCatatan(getParam("catatanFlagAktif"));
			logKuarters.setFasa(fasa);
			logKuarters.setLokasi(lokasiKuarters);
			//logKuarters.setLokasiDibenar(lokasiDibenar);
			logKuarters.setKawasan(getParam("kawasan"));
			logKuarters.setLotNo(getParam("lotNo"));
			logKuarters.setNoUnit(getParam("noUnit"));
			logKuarters.setAlamat1(getParam("alamat1"));
			logKuarters.setAlamat2(getParam("alamat2"));
			logKuarters.setAlamat3(getParam("alamat3"));
			logKuarters.setPoskod(getParam("poskod"));
			logKuarters.setBandar(bandar);
			logKuarters.setKapasiti(getParamAsInteger("kapasiti"));
			logKuarters.setKapasitiSemasa(getParamAsInteger("kapasitiSemasa"));
			logKuarters.setTarikhSerahan(getDate("tarikhSerahan"));
			logKuarters.setNoRujukan(kuarters.getNoRujukan());
			LogKuarters lk = (LogKuarters) mp.get("SELECT lk FROM LogKuarters lk WHERE lk.noRujukan = '"+ kuarters.getNoRujukan() + "' ORDER BY lk.turutan DESC");
			if (lk != null) {
				int turutan = lk.getTurutan() + 1;
				logKuarters.setTurutan(turutan);
				logKuarters.setTarikhKemaskini(new Date());
				userId = (String) request.getSession().getAttribute("_portal_login");
				logKuarters.setKemaskiniOleh(userId);
				mp.begin();
				mp.persist(logKuarters);
				try {
					mp.commit();
				} catch (Exception e) {
					System.out.println("Error kemaskiniPermohonan : " + e.getMessage());
				} 
			}
			else
			{
				LogKuarters logKuartersAsal=new LogKuarters();
				logKuartersAsal.setKelas(kuarters.getKelas());
				logKuartersAsal.setJenisKediaman(kuarters.getJenisKediaman());
				logKuartersAsal.setJenisKegunaanKuarters(kuarters.getJenisKegunaanKuarters());
				logKuartersAsal.setDeposit(kuarters.getDeposit());
				logKuartersAsal.setSewa(kuarters.getSewa());
				logKuartersAsal.setStatusKuarters(kuarters.getStatusKuarters());
				logKuartersAsal.setFlagAktif(kuarters.getFlagAktif());
				logKuartersAsal.setCatatan(kuarters.getCatatan());
				logKuartersAsal.setFasa(kuarters.getFasa());
				logKuartersAsal.setLokasi(kuarters.getLokasi());
				//logKuartersAsal.setLokasiDibenar(kuarters.getLokasi().getLokasi().getLokasi());
				logKuartersAsal.setKawasan(kuarters.getKawasan());
				logKuartersAsal.setLotNo(kuarters.getLotNo());
				logKuartersAsal.setNoUnit(kuarters.getNoUnit());
				logKuartersAsal.setAlamat1(kuarters.getAlamat1());
				logKuartersAsal.setAlamat2(kuarters.getAlamat2());
				logKuartersAsal.setAlamat3(kuarters.getAlamat3());
				logKuartersAsal.setPoskod(kuarters.getPoskod());
				logKuartersAsal.setBandar(kuarters.getBandar());
				logKuartersAsal.setKapasiti(kuarters.getKapasiti());
				logKuartersAsal.setKapasitiSemasa(kuarters.getKapasitiSemasa());
				logKuartersAsal.setTarikhSerahan(kuarters.getTarikhSerahan());
				logKuartersAsal.setNoRujukan(kuarters.getNoRujukan());
				logKuartersAsal.setTurutan(0);
				int turutan = 0 + 1;
				logKuarters.setTurutan(turutan);
				userId = (String) request.getSession().getAttribute("_portal_login");
				logKuarters.setTarikhKemaskini(new Date());
				logKuarters.setKemaskiniOleh(userId);
				mp.begin();
				mp.persist(logKuartersAsal);
				mp.persist(logKuarters);
				try {
					mp.commit();
				} catch (Exception e) {
					System.out.println("Error kemaskiniPermohonan : " + e.getMessage());
				} 
			}
		}
	}
}

/* Author :
 * zulfazdliabuas@gmail.com Data 2015-2017
 */
package bph.modules.utk;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import portal.module.entity.UsersJob;
import bph.entities.kew.KewJenisBayaran;
import bph.entities.kewangan.KewInvois;
import bph.entities.kod.Bandar;
import bph.entities.kod.BlokUtk;
import bph.entities.kod.Fasa;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisKenderaanUtk;
import bph.entities.kod.JenisKuartersUtk;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.JenisPelanggaranSyaratUtk;
import bph.entities.kod.KawasanUtk;
import bph.entities.kod.KodHasil;
import bph.entities.kod.ZonUtk;
import bph.entities.qtr.KuaPenghuni;
import bph.entities.senggara.MtnKontraktor;
import bph.entities.utk.UtkAbt;
import bph.entities.utk.UtkAkaun;
import bph.entities.utk.UtkDokumen;
import bph.entities.utk.UtkHilangKelayakan;
import bph.entities.utk.UtkKesPeguam;
import bph.entities.utk.UtkKesalahan;
import bph.entities.utk.UtkNaziranKebersihan;
import bph.entities.utk.UtkNotis;
import bph.entities.utk.UtkOperasi;
import bph.entities.utk.UtkPegawaiOperasi;
import bph.entities.utk.UtkRayuan;
import bph.entities.utk.UtkSeqOperasi;
import bph.utils.DataUtil;
import bph.utils.Util;
/**
 *
 *
 */
public class OperasiTidakBerjadualRecordModule extends LebahRecordTemplateModule<UtkOperasi>{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(UtkOperasi r) {
		// TODO Auto-generated method stub
		if (r.getNoRujukanOperasi() == null){
			db.begin();
			UtkSeqOperasi seq = (UtkSeqOperasi) db.get("select x from UtkSeqOperasi x where x.zon.id = '" + r.getZon().getId() 
								+ "' and x.jenisKuarters.id = '" + r.getJenisKuarters().getId() 
								+ "' and x.kawasan.id = '" + r.getKawasan().getId() 
								+ "' and x.jenisOperasi.id ='" + r.getJenisOperasi().getId() + "'");
			
			if(seq == null){
				seq = new UtkSeqOperasi();
				seq.setZon(r.getZon());
				seq.setJenisKuarters(r.getJenisKuarters());
				seq.setKawasan(r.getKawasan());
				seq.setJenisOperasi(r.getJenisOperasi());
				seq.setBil(1);
				db.persist(seq);
			} else {
				int next = seq.getBil() + 1;
				seq.setBil(next);
			}
			
			String formatserial = new DecimalFormat("00").format(seq.getBil());
			String noRujukanOperasi = r.getZon().getId() + "/" + r.getJenisKuarters().getId() + "/" + r.getKawasan().getId() + "/" 
							+ r.getJenisOperasi().getId() + "/"  + formatserial; 
			
			r.setNoRujukanOperasi(noRujukanOperasi);
			
			try {
				db.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		context.put("selectedTab", 1);
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		this.setDisableSaveAddNewButton(true);
		
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
		userId = (String) request.getSession().getAttribute("_portal_login");
		dataUtil = DataUtil.getInstance(db);
		
		context.remove("selectZon");
		context.remove("selectJenisKuarters");
		context.remove("selectKawasan");
		context.remove("selectJenisOperasi");
		context.remove("selectJenisPelanggaran");
		
		addfilter();
		
		List<ZonUtk> zonList = dataUtil.getListZonUtk();
		context.put("selectZon", zonList);
		
		List<JenisKuartersUtk> jenisKuartersList = dataUtil.getListJenisKuartersUtk();
		context.put("selectJenisKuarters", jenisKuartersList);
		
		List<KawasanUtk> kawasanUtkList = dataUtil.getListKawasanUtk();
		context.put("selectKawasan", kawasanUtkList);
		
		List<JenisOperasiUtk> jenisOperasiUtkList = dataUtil.getListJenisOperasiUtk();
		context.put("selectJenisOperasi", jenisOperasiUtkList);
		
		List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyarat = dataUtil.getListJenisPelanggaranSyaratUtk();
		context.put("selectJenisPelanggaran", jenisPelanggaranSyarat);
		
		List<Fasa> senaraiFasa = dataUtil.getListFasaUtk();
		context.put("selectFasa", senaraiFasa);
		
		List<BlokUtk> senaraiBlok = dataUtil.getListBlokUtk();
		context.put("selectBlok", senaraiBlok);
		
		List<JenisKenderaanUtk> senaraiJenisKenderaan = dataUtil.getListJenisKenderaan();
		context.put("selectJenisKenderaan", senaraiJenisKenderaan);
		
		context.put("command", command);
		context.put("path", getPath());
		context.put("util", new portal.module.Util());
		context.put("util", util);
		this.setOrderBy("tarikhOperasi");
		
	}
	private void addfilter() {

		this.addFilter("flagOperasi= '2'"); // OPERASI TIDAK BERJADUAL
	}

	@Override
	public boolean delete(UtkOperasi r) throws Exception {
		// TODO Auto-generated method stub
		
		List<UtkPegawaiOperasi> listPegawai = db.list("select x from UtkPegawaiOperasi x where x.operasi.id='" + r.getId() +"'");
		List<UtkKesalahan> listKesalahan = db.list("select x from UtkKesalahan x where x.operasi.id='" + r.getId() +"'");
		List<UtkNaziranKebersihan> listKebersihan = db.list("select x from UtkNaziranKebersihan x where x.operasi.id='" + r.getId() +"'");
		
		db.begin();
		if(listPegawai != null){
			for (UtkPegawaiOperasi c : listPegawai)
				db.remove(c);
		}
		
		if(listKesalahan != null){
			for(UtkKesalahan d : listKesalahan){
				
				List<UtkNotis> listNotis = db.list("select x from UtkNotis x where x.kesalahan.id='" + d.getId() +"'");
				if(listNotis != null){
					for(UtkNotis notis : listNotis)
						db.remove(notis);
				}
				
				List<UtkRayuan> listRayuan = db.list("select x from UtkRayuan x where x.kesalahan.id='" + d.getId() +"'");
				if(listRayuan != null){
					for(UtkRayuan rayuan : listRayuan)
						db.remove(rayuan);
				}
				
				List<UtkDokumen> listDokumen = db.list("select x from UtkDokumen x where x.kesalahan.id='" + d.getId() +"'");
				if(listDokumen != null){
					for(UtkDokumen dokumen : listDokumen)
					db.remove(dokumen);
				}
				
				List<UtkKesPeguam> listPeguam = db.list("select x from UtkKesPeguam x where x.kesalahan.id='" + d.getId() +"'");
				if(listPeguam.size() > 0){
					for(UtkKesPeguam peguam : listPeguam)
					db.remove(peguam);
				}
				
				db.remove(d);
			}
		}
		
		if(listKebersihan != null){
			for(UtkNaziranKebersihan e : listKebersihan){
				
				List<UtkNotis> listNotis = db.list("select x from UtkNotis x where x.naziranKebersihan.id='" + e.getId() +"'");
				if(listNotis != null){
					for(UtkNotis notis : listNotis)
						db.remove(notis);
				}
				
				List<UtkRayuan> listRayuan = db.list("select x from UtkRayuan x where x.naziranKebersihan.id='" + e.getId() +"'");
				if(listRayuan != null){
					for(UtkRayuan rayuan : listRayuan)
						db.remove(rayuan);
				}
				
				db.remove(e);
			}
		}
		
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utk/operasi/operasiTidakBerjadual";
	}

	@Override
	public Class<UtkOperasi> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtkOperasi.class;
	}

	@Override
	public void getRelatedData(UtkOperasi r) {
		// TODO Auto-generated method stub
		
		List<UtkDokumen> listDokumen = db.list("SELECT x FROM UtkDokumen x WHERE x.operasi.id= '" + r.getId() + "'");
		System.out.println("print ::: " + listDokumen.size());
		if (listDokumen != null) {
			context.put("listDokumen", listDokumen);
		}
		
		context.put("selectedTab", "1");
	}

	@Override
	public void save(UtkOperasi r) throws Exception {
		r.setTajuk(get("tajuk"));
		r.setZon(db.find(ZonUtk.class, get("idZon")));
		r.setJenisKuarters(db.find(JenisKuartersUtk.class, get("idKuarters")));
		r.setKawasan(db.find(KawasanUtk.class, get("idKawasan")));
		r.setJenisOperasi(db.find(JenisOperasiUtk.class, get("idJenisOperasi")));
		r.setJenisPelanggaranSyarat(db.find(JenisPelanggaranSyaratUtk.class, get("idJenisPelanggaran")));
		r.setTarikhAduan(getDate("tarikhAduan"));
		r.setTarikhOperasi(getDate("tarikhOperasi"));
		r.setMasaMula(get("masaMula"));
		r.setMasaTamat(get("masaTamat"));
		r.setCatatan(get("catatan"));
		r.setFlagOperasi("2"); //OPERASI TIDAK BERJADUAL
		//addbyzul
		r.setFasa(db.find(Fasa.class, get("idFasa")));
		r.setBlok(db.find(BlokUtk.class, get("idBlok")));
		r.setJenisKenderaan(db.find(JenisKenderaanUtk.class, get("vmIdJenisKenderaan")));
		r.setNoPlatKenderaan(get("noPlatKenderaan"));
		r.setModelKenderaan(get("modelKenderaan"));
		r.setTarikhDataMasuk(new Date());
		r.setTarikhAduan(getDate("tarikhAduan"));

	}
	
	@Command("saveMaklumatOperasi")
	public String saveMaklumatOperasi() throws ParseException {
		String statusInfo = "";
		
		UtkOperasi mo = db.find(UtkOperasi.class, get("idOperasi"));
		Boolean addMaklumatOperasi = false;
		
		if(mo == null){
			addMaklumatOperasi = true;
			mo = new UtkOperasi();
		}
		
		mo.setTajuk(get("tajuk"));
		mo.setZon(db.find(ZonUtk.class, get("idZon")));
		mo.setJenisKuarters(db.find(JenisKuartersUtk.class, get("idKuarters")));
		mo.setKawasan(db.find(KawasanUtk.class, get("idKawasan")));
		mo.setJenisOperasi(db.find(JenisOperasiUtk.class, get("idJenisOperasi")));
		mo.setJenisPelanggaranSyarat(db.find(JenisPelanggaranSyaratUtk.class, get("idJenisPelanggaran")));
		mo.setTarikhAduan(getDate("tarikhAduan"));
		mo.setTarikhOperasi(getDate("tarikhOperasi"));
		mo.setMasaMula(get("masaMula"));
		mo.setMasaTamat(get("masaTamat"));
		mo.setCatatan(get("catatan"));
		mo.setFlagOperasi("2"); //OPERASI BERJADUAL
		//addbyzul
		mo.setFasa(db.find(Fasa.class, get("idFasa")));
		mo.setBlok(db.find(BlokUtk.class, get("idBlok")));
		mo.setJenisKenderaan(db.find(JenisKenderaanUtk.class, get("vmIdJenisKenderaan")));
		mo.setNoPlatKenderaan(get("noPlatKenderaan"));
		mo.setModelKenderaan(get("modelKenderaan"));
		mo.setTarikhKemaskiniData(new Date());
		mo.setTarikhAduan(getDate("tarikhAduan"));
		
		db.begin();
		if ( addMaklumatOperasi ) db.persist(mo);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "1");
		
		return getPath() + "/entry_page.vm";
	}
	

	@Override	
	public Map<String, Object> searchCriteria() throws Exception {
	
	String idKuarters = get("findKuarters");
	String findOperasi = get("findOperasi");
	String findKawasan = get("findKawasan");
	String findZon = get("findZon");
	String id = get("findId");
	String tajuk = get("findTajuk");

	
	Map<String, Object> map = new HashMap<String, Object>();

	map.put("id", id);
	map.put("tajuk", tajuk);
	map.put("jenisKuarters.id", idKuarters);
	map.put("jenisOperasi.id", findOperasi);
	map.put("kawasan.id", findKawasan);
	map.put("zon.id", findZon);
	
	return map;

	}
	/** SENARAI TAB **/
	
	@Command("getMaklumatOperasi")
	public String getMaklumatOperasi() {
		
		UtkOperasi operasi = db.find(UtkOperasi.class, get("idOperasi"));
		context.put("r", operasi);
		
		List<UtkDokumen> listDokumen = db.list("SELECT x FROM UtkDokumen x WHERE x.operasi.id= '" + operasi.getId() + "'");
		System.out.println("print ::: " + listDokumen.size());
		context.put("listDokumen", listDokumen);
		
		context.put("selectedTab", "1");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPegawai")
	public String getMaklumatPegawai() {
		
		List<UtkPegawaiOperasi> listPegawai = db.list("select x from UtkPegawaiOperasi x where x.operasi.id='" + get("idOperasi") +"'");
			
        context.put("listPegawai", listPegawai);
		
		context.put("selectedTab", "2");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatKesalahan")
	public String getMaklumatKesalahan() {
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		List<UtkKesalahan> listKesalahan = db.list("select x from UtkKesalahan x where x.operasi.id='" + get("idOperasi") +"'");			
        context.put("listKesalahan", listKesalahan);
		
		context.put("selectedTab", "3");

		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatKontraktor")
	public String getMaklumatKontraktor() {
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		List<UtkNaziranKebersihan> listKesalahan = db.list("select x from UtkNaziranKebersihan x where x.operasi.id='" + get("idOperasi") +"'");			
        context.put("listKesalahan", listKesalahan);
		
		context.put("selectedTab", "4");

		return getPath() + "/entry_page.vm";
	}
	
	/** END SENARAI TAB **/
	
	/** MAKLUMAT PEGAWAI **/
	
	@Command("editPegawai")
	public String editPegawai() {
		
		UtkPegawaiOperasi rekod = db.find(UtkPegawaiOperasi.class, getParam("idPegawai"));
        context.put("rekod", rekod);
        
		context.put("selectedTab", "2");

		return getPath() + "/pegawaiOperasi/maklumatPegawaiOperasi.vm";
	}
	
	@Command("addPegawai")
	public String addPegawai() {
		
		context.remove("rekod");
		/*UtkPegawaiOperasi rekod = db.find(UtkPegawaiOperasi.class, getParam("idPegawai"));
        context.put("rekod", rekod);
        */
		context.put("selectedTab", "2");

		return getPath() + "/pegawaiOperasi/maklumatPegawaiOperasi.vm";
	}
	
	@Command("savePegawai")
	public String savePegawai() throws ParseException {
		String statusInfo = "";
		
		UtkPegawaiOperasi maklumatPegawai = db.find(UtkPegawaiOperasi.class, get("idPegawai"));
		Boolean addMaklumatPegawai = false;
		
		if(maklumatPegawai == null){
			addMaklumatPegawai = true;
			maklumatPegawai = new UtkPegawaiOperasi();
		}
		
		maklumatPegawai.setNamaPegawai(get("namaPegawai"));
		maklumatPegawai.setJawatan(get("jawatan"));
		maklumatPegawai.setOperasi(db.find(UtkOperasi.class, get("idOperasi")));
		maklumatPegawai.setFlagKetuaOperasi(get("flagKetuaOperasi"));
		
		db.begin();
		if ( addMaklumatPegawai ) db.persist(maklumatPegawai);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "2");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	@Command("removePegawai")
	public String removePegawai() {
		String statusInfo = "";
		
		UtkPegawaiOperasi maklumatPegawai = db.find(UtkPegawaiOperasi.class, get("idPegawai"));

		db.begin();
		db.remove(maklumatPegawai);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkPegawaiOperasi> listPegawai = db.list("select x from UtkPegawaiOperasi x where x.operasi.id='" + get("idOperasi") +"'");
			
        context.put("listPegawai", listPegawai);
        context.put("selectedTab", "2");
		
		return getPath() + "/entry_page.vm";
	}
	
	/** END MAKLUMAT PEGAWAI **/
	
	/** MAKLUMAT PESALAH **/
	
	@Command("addPesalah")
	public String addPesalah() {
		
		context.remove("rekod");
		/*UtkPegawaiOperasi rekod = db.find(UtkPegawaiOperasi.class, getParam("idPegawai"));
        context.put("rekod", rekod);
        */
		
		String noKp = get("noKp");
		String namaPesalah = get("namaPesalah");
		String noUnit = get("noUnit");
		String idNegeri = get("findNegeri");
		String idBandar = get("findBandar");
		
		List<KuaPenghuni> pesalahList = searchListPesalah(noKp, namaPesalah, noUnit, idNegeri, idBandar);
		context.put("pesalahList", pesalahList);
		
		context.put("selectedTab", "3");

		return getPath() + "/kesalahan/maklumatPesalah.vm";
	}
	
		private List<KuaPenghuni> searchListPesalah(String noKp, String namaPesalah, String noUnit, String idNegeri, String idBandar) {
		
		List<KuaPenghuni> list = new ArrayList<KuaPenghuni>();

		
			String sql = "select x from KuaPenghuni x where x.id not in(select y.penghuni.id from UtkKesalahan y where y.operasi.id='" + get("idOperasi") + "')";
			
			if(!noKp.equalsIgnoreCase("")){
				sql = sql + "  and x.pemohon.noKP like '%" + noKp + "%'";
			}
			
			if(!namaPesalah.equalsIgnoreCase("")){
				sql = sql + "  and x.pemohon.userName like upper('%" + namaPesalah + "%')";
			}
			
			if(!noUnit.equalsIgnoreCase("")){
				sql = sql + "  and x.kuarters.noUnit like upper('%" + noUnit + "%')";
			}
			
			if(!idNegeri.equalsIgnoreCase("")){
				sql = sql + "  and x.kuarters.bandar.negeri.id ='" + idNegeri +"'";
			}
			
			if(!idBandar.equalsIgnoreCase("")){
				sql = sql + "  and x.kuarters.bandar.id ='" + idBandar +"'";
			}
			
			System.out.println("ceking sql =========== " + sql);
			 list = db.list(sql);

		return list;
	}

	@Command("savePesalah")
	public String savePesalah() throws ParseException {
		String statusInfo = "";
		System.out.println("cekinnnn =========== " + get("idJenisOperasi"));
		String[] pesalah = request.getParameterValues("pesalah");
		db.begin();
		for (String pesalahId : pesalah){
			UtkKesalahan kesalahan = new UtkKesalahan();
			System.out.println("cekk pesalah ============= " + pesalahId);
			kesalahan.setOperasi(db.find(UtkOperasi.class, get("idOperasi")));
			kesalahan.setPenghuni(db.find(KuaPenghuni.class, pesalahId));
			kesalahan.setStatus("BARU");

			db.persist(kesalahan);
			}
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
	
	/** END MAKLUMAT PESALAH **/
	
	/** START MAKLUMAT KONTRAKTOR **/
	
	@Command("addKontraktor")
	public String addKontraktor() {
		
		context.remove("rekod");
		
		String noPendaftaran = get("id");
		String namaKontraktor = get("namaKontraktor");
		String idNegeri = get("findNegeri");
		String idBandar = get("findBandar");
		
		List<MtnKontraktor> kontraktorList = searchListKontraktor(noPendaftaran, namaKontraktor, idNegeri, idBandar);
		context.put("kontraktorList", kontraktorList);
		
		context.put("selectedTab", "4");

		return getPath() + "/kontraktor/maklumatKontraktor.vm";
	}
	
	private List<MtnKontraktor> searchListKontraktor(String noPendaftaran, String namaKontraktor, String idNegeri, String idBandar) {
		
		List<MtnKontraktor> list = new ArrayList<MtnKontraktor>();

		
			String sql = "select x from MtnKontraktor x where x.id not in(select y.kontraktor.id from UtkNaziranKebersihan y where y.operasi.id='" + get("idOperasi") + "')";
			
			if(!noPendaftaran.equalsIgnoreCase("")){
				sql = sql + "  and x.id like '%" + noPendaftaran + "%'";
			}
			
			if(!namaKontraktor.equalsIgnoreCase("")){
				sql = sql + "  and x.namaKontraktor like upper('%" + namaKontraktor + "%')";
			}
			
			if(!idNegeri.equalsIgnoreCase("")){
				sql = sql + "  and x.bandar.negeri.id ='" + idNegeri +"'";
			}
			
			if(!idBandar.equalsIgnoreCase("")){
				sql = sql + "  and x.bandar.id ='" + idBandar +"'";
			}
			
			System.out.println("ceking sql =========== " + sql);
			 list = db.list(sql);

		return list;
	}

	@Command("saveKontraktor")
	public String saveKontraktor() throws ParseException {
		String statusInfo = "";
		
		String[] kontraktor = request.getParameterValues("kontraktor");
		db.begin();
		for (String kontraktorId : kontraktor){
			UtkNaziranKebersihan kebersihan = new UtkNaziranKebersihan();
			kebersihan.setOperasi(db.find(UtkOperasi.class, get("idOperasi")));
			kebersihan.setKontraktor(db.find(MtnKontraktor.class, kontraktorId));
			kebersihan.setStatus("1"); //BARU
			db.persist(kebersihan);
			}
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "4");
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("removeKontraktor")
	public String removeKontraktor() {
		String statusInfo = "";
		
		UtkKesalahan kesalahan = db.find(UtkKesalahan.class, getParam("idKesalahan"));

		db.begin();
		db.remove(kesalahan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkNaziranKebersihan> listKesalahan = db.list("select x from UtkNaziranKebersihan x where x.operasi.id='" + get("idOperasi") +"'");
			
        context.put("listKesalahan", listKesalahan);
        context.put("selectedTab", "4");
		
		return getPath() + "/entry_page.vm";
	}
	
	/** END MAKLUMAT KONTRAKTOR **/
	
	/** START MAKLUMAT NAZIRAN KEBERSIHAN **/
	
	@Command("editKebersihan")
	public String editKontraktor() {
		
		UtkNaziranKebersihan rekod = db.find(UtkNaziranKebersihan.class, getParam("idKebersihan"));
		context.put("rekod", rekod);
		
		context.put("selectedTab", "4");

		return getPath() + "/kontraktor/maklumatKebersihan.vm";
	}
	
	@Command("saveKebersihan")
	public String saveKebersihan() {
		String statusInfo="";
		UtkNaziranKebersihan kebersihan = db.find(UtkNaziranKebersihan.class, get("idKebersihan"));

		kebersihan.setTarikhMula(getDate("tarikhMula"));
		kebersihan.setTarikhTamat(getDate("tarikhTamat"));
		kebersihan.setCatatan(get("catatan"));
		kebersihan.setStatus("1"); //DALAM PROSES
		
		db.begin();
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
        
		context.put("selectedTab", "4");
		return getPath() + "/kontraktor/maklumatKebersihan.vm";
	}
	
	/** START MAKLUMAT NAZIRAN KEBERSIHAN **/
	
	/** MAKLUMAT KESALAHAN **/

//	@Command("editKesalahan")
//	public String editKesalahan() {
//		
//		UtkKesalahan rekod = db.find(UtkKesalahan.class, getParam("idKesalahan"));
//		/*if(rekod.getOperasi().getJenisOperasi().getId().equals("PS")){
//			if(rekod.getOperasi().getJenisPelanggaranSyarat() != null){
//				List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(rekod.getOperasi().getJenisPelanggaranSyarat().getFlagKes());
//				context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);
//			}
//		}*/
//		context.put("rekod", rekod);
//		
//		context.put("selectedTab", "3");
//
//		return getPath() + "/kesalahan/maklumatKesalahan.vm";
//	}
	@Command("paparMaklumatPesalah")
	public String paparMaklumatPesalah() {
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, getParam("idKesalahan"));
		context.put("rekod", rekod);
		
		KuaPenghuni penghuni = (KuaPenghuni) db.get("select p from KuaPenghuni p where p.id='" + rekod.getPenghuni().getId() + "'");
		context.put("penghuni", penghuni);
		
		UsersJob pekerjaan = (UsersJob) db.get("Select u from UsersJob u where u.users.id='" + penghuni.getPemohon().getId() + "'");
		context.put("pekerjaan", pekerjaan);
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		return getPath() + "/kesalahan/maklumatPesalah/start.vm";
	}
	
	@Command("saveKesalahan")
	public String saveKesalahan() {
		String statusInfo="";
		UtkKesalahan rekod = db.find(UtkKesalahan.class, get("idKesalahan"));
		rekod.setCatatan(get("catatan"));
		
		db.begin();
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
        
		context.put("selectedTab", "3");

//		return getPath() + "/entry_page.vm";
		return getPath() + "/kesalahan/maklumatKesalahan.vm";
	}
	
	
	@Command("removeKesalahan")
	public String removeKesalahan() {
		String statusInfo = "";
		
		UtkKesalahan kesalahan = db.find(UtkKesalahan.class, getParam("idKesalahan"));

		db.begin();
		List<UtkNotis> listNotis = db.list("select x from UtkNotis x where x.kesalahan.id='" + kesalahan.getId() +"'");
		if(listNotis != null){
			for(UtkNotis notis : listNotis)
				db.remove(notis);
		}
		
		List<UtkRayuan> listRayuan = db.list("select x from UtkRayuan x where x.kesalahan.id='" + kesalahan.getId() +"'");
		if(listRayuan != null){
			for(UtkRayuan rayuan : listRayuan)
				db.remove(rayuan);
		}
		
		List<UtkDokumen> listDokumen = db.list("select x from UtkDokumen x where x.kesalahan.id='" + kesalahan.getId() +"'");
		if(listDokumen != null){
			for(UtkDokumen dokumen : listDokumen)
			db.remove(dokumen);
		}
		
		List<UtkKesPeguam> listPeguam = db.list("select x from UtkKesPeguam x where x.kesalahan.id='" + kesalahan.getId() +"'");
		if(listPeguam.size() > 0){
			for(UtkKesPeguam peguam : listPeguam)
			db.remove(peguam);
		}

		
		db.remove(kesalahan);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkKesalahan> listKesalahan = db.list("select x from UtkKesalahan x where x.operasi.id='" + get("idOperasi") +"'");
			
        context.put("listKesalahan", listKesalahan);
        context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
	
	/** END MAKLUMAT KESALAHAN **/
	
/** MAKLUMAT PENGUNCIAN TAYAR **/
	
	@Command("editPenguncian")
	public String editPenguncian() {
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, getParam("idPenguncian"));
        context.put("rekod", rekod);
        
        UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + get("idPenguncian") + "'");
        context.put("ak", ak);
        
		return getPath() + "/kesalahan/maklumatPenguncianTayar.vm";
	}
	
	@Command("savePenguncian")
	public String savePenguncian() throws ParseException {
		String statusInfo = "";
		
		UtkKesalahan p = db.find(UtkKesalahan.class, get("idPenguncian"));
		Boolean addMaklumatPenguncian = false;
		
		if(p == null){
			addMaklumatPenguncian = true;
			p = new UtkKesalahan();
		}
		
		p.setOperasi(db.find(UtkOperasi.class, get("idOperasi")));
		p.setNoSiri(get("noSiri"));
		p.setJenisKenderaan(get("jenisKenderaan"));
		p.setNoPlat(get("noPlat"));
		p.setCatatan(get("catatan"));
		
		
		db.begin();
		if ( addMaklumatPenguncian ) db.persist(p);
		
//		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + get("idPenguncian") + "'");
//		Boolean addMaklumatAkaun = false;
//		
//		if(ak == null){
//			addMaklumatAkaun = true;
//			ak = new UtkAkaun();
//		}
//		
//		ak.setKesalahan(p);
//		ak.setDebit(Util.getDouble(Util.RemoveComma(get("amaun"))));
//		ak.setFlagBayaran("DENDA"); // SEWA / DEPOSIT
//		ak.setFlagBayar("T"); // BELUM BAYAR
//		ak.setFlagQueue("T");
//		ak.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
//		ak.setKodHasil(db.find(KodHasil.class,"76199")); // PELBAGAI BAYARAN HUKUMAN
//		ak.setNoInvois(get("idPenguncian"));
//		ak.setUserPendaftar(db.find(Users.class,userId)); //user login
//		ak.setTarikhDaftar(new Date()); 
//		
//		if ( addMaklumatAkaun ) db.persist(ak);
//		
//		KewInvois inv = (KewInvois) db.get("Select x from KewInvois x where x.idLejar='" + ak.getId() + "'");
//		Boolean addMaklumatInv = false;
//		
//		if(inv == null){
//			addMaklumatInv = true;
//			inv = new KewInvois();
//			inv.setTarikhDaftar(new Date()); 
//		}
//		
//		inv.setDebit(ak.getDebit());
//		inv.setFlagBayaran(ak.getFlagBayaran()); // SEWA / DEPOSIT
//		inv.setFlagQueue("T");
//		inv.setIdLejar(ak.getId());
//		inv.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
//		inv.setKeteranganBayaran(ak.getKeteranganBayaran().toUpperCase()); // PELBAGAI BAYARAN HUKUMAN
//		inv.setKodHasil(ak.getKodHasil());
//		inv.setNoInvois(ak.getNoInvois());
//		inv.setNoRujukan(ak.getKesalahan().getOperasi().getNoFail()); // NO FAIL OPERASI
//		inv.setPembayar(db.find(Users.class,ak.getKesalahan().getPenghuni().getPemohon().getId()));
//		inv.setTarikhInvois(ak.getKesalahan().getOperasi().getTarikh());
//		inv.setUserPendaftar(db.find(Users.class, userId)); //user login
//		inv.setTarikhKemaskini(new Date()); 
//		
//		if ( addMaklumatInv ) db.persist(inv);
//		
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		Boolean akaun = saveAkaun();
		
		context.put("statusInfo", statusInfo);
		context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
//		return getPath() + "/maklumatPermohonan/start.vm";
	}
	
	public Boolean saveAkaun() throws ParseException {
		Boolean statusInfo = false;
		
		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + get("idPenguncian") + "'");
		Boolean addMaklumatAkaun = false;
		
		if(ak == null){
			addMaklumatAkaun = true;
			ak = new UtkAkaun();
		}
		
		ak.setKesalahan(db.find(UtkKesalahan.class, get("idPenguncian")));
		ak.setDebit(Util.getDouble(Util.RemoveComma(get("amaun"))));
		ak.setFlagBayaran("DENDA"); // SEWA / DEPOSIT
		ak.setFlagBayar("T"); // BELUM BAYAR
		ak.setFlagQueue("T");
		ak.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
		ak.setKodHasil(db.find(KodHasil.class,"76199")); // PELBAGAI BAYARAN HUKUMAN
		ak.setNoInvois(get("idPenguncian"));
		ak.setUserPendaftar(db.find(Users.class,userId)); //user login
		ak.setTarikhDaftar(new Date()); 
		
		db.begin();
		if ( addMaklumatAkaun ) db.persist(ak);
		
		try {
			db.commit();
			Boolean kewInvois = pushKew();
			if(kewInvois){
				statusInfo = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = false;
		}
		return statusInfo;
	}
	
	public Boolean pushKew() throws ParseException {
		Boolean statusInfo = false;
		
		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + get("idPenguncian") + "'");
		KewInvois inv = (KewInvois) db.get("Select x from KewInvois x where x.idLejar='" + ak.getId() + "'");
		Boolean addMaklumatInv = false;
		
		if(inv == null){
			addMaklumatInv = true;
			inv = new KewInvois();
			inv.setTarikhDaftar(new Date()); 
		}
		inv.setDebit(ak.getDebit());
		inv.setFlagBayaran(ak.getFlagBayaran()); // SEWA / DEPOSIT
		inv.setFlagQueue("T");
		inv.setIdLejar(ak.getId());
		inv.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
		inv.setKeteranganBayaran(ak.getKodHasil().getKeterangan());
		inv.setKodHasil(ak.getKodHasil());  // PELBAGAI BAYARAN HUKUMAN
		inv.setNoInvois(ak.getNoInvois());
		inv.setNoRujukan(ak.getKesalahan().getOperasi().getNoFail()); // NO FAIL OPERASI
		inv.setPembayar(db.find(Users.class,ak.getKesalahan().getPenghuni().getPemohon().getId()));
		inv.setTarikhInvois(ak.getKesalahan().getOperasi().getTarikhOperasi());
		inv.setUserPendaftar(db.find(Users.class, userId)); //user login
		inv.setTarikhKemaskini(new Date());
		inv.setUserKemaskini(db.find(Users.class, userId));
		
		db.begin();
		if ( addMaklumatInv ) db.persist(inv);
		
		try {
			db.commit();
			statusInfo = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = false;
		}
		return statusInfo;
	}
	
	@Command("removePenguncian")
	public String removePenguncian() {
		String statusInfo = "";
		System.out.println("cking idpenguncian ============ " + get("idPenguncian"));
		UtkKesalahan maklumatPenguncian = db.find(UtkKesalahan.class, get("idPenguncian"));

		db.begin();
		db.remove(maklumatPenguncian);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkKesalahan> listKesalahan = db.list("select x from UtkKesalahan x where x.operasi.id='" + get("idOperasi") +"'");
			
        context.put("listKesalahan", listKesalahan);
        context.put("selectedTab", "3");
		
		return getPath() + "/entry_page.vm";
	}
	
	/** END MAKLUMAT PENGUNCIAN TAYAR **/
	
	/** DROP DOWN **/
	
	@Command("findBandar")
	public String findBandar() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/kesalahan/findBandar.vm";
	}
	
	@Command("selectPelanggaran")
	public String selectPelanggaran() throws Exception {
		String idKes = "0";
		if (get("idKes").trim().length() > 0)
			idKes = get("idKes");

		List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(idKes);
		context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);

		return getPath() + "/kesalahan/selectJenisPelanggaranSyarat.vm";
	}
	
	@Command("selectPelanggaranOperasi")
	public String selectPelanggaranOperasi() throws Exception {
		String idKes = "0";
		if (get("idKes").trim().length() > 0)
			idKes = get("idKes");

		List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(idKes);
		context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);

		return getPath() + "/selectJenisPelanggaranSyarat.vm";
	}
	
	@Command("selectKes")
	public String selectKes() throws Exception {
		
		context.put("showOperasi", getParam("idJenisOperasi"));

		context.put("selectedTab", "1");
		return getPath() + "/divKes.vm";
	}
	
	/** END DROP DOWN **/
	
	/*############################################### START FUNCTION - FUNCTION SUB TAB ###############################################*/
	/*************************************************** START MAKLUMAT PENGHUNI ***************************************************/
	@Command("getMaklumatPenghuni")
	public String getMaklumatPenghuni() {
		
		context.remove("pekerjaan");
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, getParam("idKesalahan"));
		context.put("rekod", rekod);
//		
//		KuaPenghuni penghuni = (KuaPenghuni) db.get("select p from KuaPenghuni p where p.id='" + rekod.getPenghuni().getId() + "'");
//		context.put("penghuni", penghuni);
//		
//		UsersJob pekerjaan = (UsersJob) db.get("Select u from UsersJob u where u.users.id='" + penghuni.getPemohon().getId() + "'");
//		context.put("pekerjaan", pekerjaan);
//		
		//context.put("selectedTab", "1");
		//return getPath() + "/entry_page.vm";
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		return getPath() + "/kesalahan/formMaklumatKesalahan/start.vm";
	}
	/*************************************************** END MAKLUMAT PENGHUNI ***************************************************/
	
	/*************************************************** START PELANGGARAN SYARAT ***************************************************/
	@SuppressWarnings("unchecked")
	@Command("getPelanggaranSyarat")
	public String getPelanggaranSyarat() {
		
		System.out.println("cking idKesalahan ============ " + get("idKesalahan"));
		System.out.println("cking idpenguncian ============ " + get("idPenguncian"));
		
//		UtkKesalahan rekod = db.find(UtkKesalahan.class, getParam("idKesalahan"));
//		context.put("rekod", rekod);
//		myLogger.debug("STATUS ::: " + rekod.getId());
//		System.out.println("ceking id idKesalahan ====== " + rekod.getId());
		
		List<UtkKesalahan> psList = db.list("Select x from UtkKesalahan x where x.penghuni.id ='" + get("idPenghuni") + "' and x.idJenisOperasi in ('CP','PS','KT')");
		context.put("psList", psList);
		for(UtkKesalahan k : psList)
			System.out.println(" data: " + k.getId());
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");

		return getPath() + "/kesalahan/maklumatKesalahan/start.vm";
	}
	
	@Command("addLanggarSyarat")
	public String addLanggarSyarat() {
		
		context.remove("rekod");
		return getPath() + "/kesalahan/maklumatKesalahan/pelanggaranSyarat/entry_page.vm";
	}
	
	@Command("editPS")
	public String editPS() {
		context.put("rekod", "");
		
		UtkKesalahan ps = db.find(UtkKesalahan.class,get("idPS"));
		String jenisOperasi = ps.getIdJenisOperasi();
		
		 UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + get("idPS") + "'");
	        
		if("PS".equals(jenisOperasi)){
			System.out.println("ceking jenis operasi ====== " + jenisOperasi);
			List<JenisPelanggaranSyaratUtk> jenisPelanggaranSyaratUtkList = dataUtil.getListJenisPelanggaranSyaratUtk(ps.getJenisPelanggaranSyarat().getFlagKes());
			context.put("selectJenisPelanggaran", jenisPelanggaranSyaratUtkList);
		}
		
		context.put("rekod", ps);
		context.put("ak", ak);
		context.put("selectedTab", "2");
		return getPath() + "/pelanggaranSyarat/entry_page.vm";
	}
	
	// Simpan maklumat pelanggaran syarat - popup
	@Command("savePelanggaranSyarat")
	public String savePelanggaranSyarat() throws Exception {
		String statusInfo="";
		System.out.println("ceking idPS ========= " + getParam("idPS"));
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, get("idPS"));
		Boolean addMaklumatPS = false;
		
		if(rekod == null){
			addMaklumatPS = true;
			rekod = new UtkKesalahan();
		}
		
		String idJenisOperasi =  get("idJenisOperasi"); // JENIS PELANGGARAN SYARAT
		
		if("PS".equals(idJenisOperasi)){
			
			String idJenisPS = get("idJenisPelanggaran");
			rekod.setJenisPelanggaranSyarat(db.find(JenisPelanggaranSyaratUtk.class, idJenisPS));
			
			if("1435633886800".equals(idJenisPS)){
				rekod.setJenisKenderaan(get("jenisKenderaan"));
				rekod.setNoPlat(get("noPlat"));
			}
		} else if("CP".equals(idJenisOperasi)){
			rekod.setJenisKenderaan(get("jenisKenderaan"));
			rekod.setNoPlat(get("noPlat"));
		}
		
		rekod.setJenisOperasi(db.find(JenisOperasiUtk.class, get("idJenisOperasi")));
		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
		rekod.setCatatan(get("catatan"));
		rekod.setTarikh(getDate("tarikh"));
		rekod.setStatusPenghuni(get("flagStatusTindakan"));
		
		System.out.println("ceking id kesalahan = '" + rekod.getId() + "'");
		db.begin();
		if ( addMaklumatPS ) db.persist(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}

		if("PS".equals(idJenisOperasi)){
			String idJenisPS = get("idJenisPelanggaran");
			if("1435633886800".equals(idJenisPS)){
				Boolean akaun = saveAkaun(rekod.getId());
			}
		} else if("CP".equals(idJenisOperasi)){
			Boolean akaun = saveAkaun(rekod.getId());
		}
        
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/pelanggaranSyarat/entry_page.vm";
	}
	
	@Command("removePelanggaranSyarat")
	public String removePelanggaranSyarat() {
		String statusInfo = "";
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, get("idPS"));

		db.begin();
		db.remove(rekod);
		try {
			db.commit();
			statusInfo = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = "error";
		}
		
		List<UtkKesalahan> psList = db.list("Select x from UtkKesalahan x where x.penghuni.id ='" + get("idPenghuni") + "' and x.idJenisOperasi in ('CP','PS','KT')");
		context.put("psList", psList);
		
        context.put("selectedTab", "2");
		
		return getPath() + "/entry_page.vm";
	}
	/*************************************************** END PELANGGARAN SYARAT ***************************************************/
	
	/*************************************************** Start HILANG KELAYAKAN ***************************************************/
	@SuppressWarnings("unchecked")
	@Command("getHilangKelayakan")
	public String getHilangKelayakan() {
		
		UtkKesalahan rekod = db.find(UtkKesalahan.class, getParam("idKesalahan"));
		context.put("rekod", rekod);
		
		List<UtkHilangKelayakan> hkList = db.list("Select x from UtkHilangKelayakan x where x.penghuni.id='" + get("idPenghuni") + "'");
		context.put("hkList", hkList);
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "3");
		return getPath() + "/kesalahan/maklumatKesalahan/start.vm";
	}
	/*************************************************** END HILANG KELAYAKAN ***************************************************/
	
	/*************************************************** START ABT ***************************************************/
	@SuppressWarnings("unchecked")
	@Command("getABT")
	public String getABT() {

		List<UtkAbt> abtList = db.list("Select x from UtkAbt x where x.penghuni.id='" + get("idPenghuni") + "'");
		context.put("abtList", abtList);
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "4");
		return getPath() + "/kesalahan/maklumatKesalahan/start.vm";
	}
	
//	@Command("editABT")
//	public String editABT() {
//		context.put("rekod", "");
//		
//		UtkAbt abt = db.find(UtkAbt.class,get("idABT"));
//		
//		context.put("rekod", abt);
//		context.put("selectedTab", "4");
//		return getPath() + "/ABT/entry_page.vm";
//	}
	/*************************************************** END ABT ***************************************************/
	
	
	/*************************************************** START KES PEGUAM ***************************************************/
	@SuppressWarnings("unchecked")
	@Command("getKesPeguam")
	public String getKesPeguam() {

		List<UtkKesPeguam> kesPeguamList = db.list("Select x from UtkKesPeguam x where x.penghuni.id='" + get("idPenghuni") + "'");
		context.put("kesPeguamList", kesPeguamList);
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "5");
		return getPath() + "/kesalahan/maklumatKesalahan/start.vm";
	}
	
//	@Command("addPeguam")
//	public String addPeguam() {
//		return getPath() + "/kesPeguam/entry_page.vm";
//	}
//	
//	@Command("editKP")
//	public String editKP() {
//		context.put("rekod", "");
//		
//		UtkKesPeguam kp = db.find(UtkKesPeguam.class,getParam("idKP"));
//		
//		context.put("rekod", kp);
//		context.put("selectedTab", "5");
//		return getPath() + "/kesPeguam/entry_page.vm";
//	}
//	
//	@Command("simpanPeguam")
//	public String simpanPeguam() throws Exception {
//		String statusInfo = "";
//		
//		UtkKesPeguam rekod = db.find(UtkKesPeguam.class, get("idKP"));
//		Boolean addMaklumatKP = false;
//		
//		if(rekod == null){
//			addMaklumatKP = true;
//			rekod = new UtkKesPeguam();
//		}
//		
//		rekod.setPenghuni(db.find(KuaPenghuni.class, get("idPenghuni")));
//		rekod.setCatatan(get("catatan"));
//		rekod.setTarikhKeputusan(getDate("tarikh"));
//		rekod.setFlagKeputusan(get("flagKeputusan"));
//		
//		db.begin();
//		if ( addMaklumatKP ) db.persist(rekod);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//
//		context.put("selectedTab", "5");
//		return getPath() + "/kesPeguam/entry_page.vm";
//		}
//
//	@Command("removePeguam")
//	public String removePeguam() {
//		String statusInfo = "";
//		
//		UtkKesPeguam rekod = db.find(UtkKesPeguam.class, get("idKP"));
//
//		db.begin();
//		db.remove(rekod);
//		try {
//			db.commit();
//			statusInfo = "success";
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			statusInfo = "error";
//		}
//		
//		List<UtkKesPeguam> kesPeguamList = db.list("Select x from UtkKesPeguam x where x.penghuni.id='" + get("idPenghuni") + "'");
//		
//		context.put("kesPeguamList", kesPeguamList);
//        context.put("selectedTab", "5");
//		
//		return getPath() + "/entry_page.vm";
//	}
	/*************************************************** END KES PEGUAM ***************************************************/
	/*############################################### END FUNCTION - FUNCTION SUB TAB ###############################################*/
	
	/*************************************************** START PUSH DATA ***************************************************/
	public Boolean saveAkaun(String idPS) throws ParseException {
		Boolean statusInfo = false;
		
		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + idPS + "'");
		Boolean addMaklumatAkaun = false;
		
		if(ak == null){
			addMaklumatAkaun = true;
			ak = new UtkAkaun();
		}
		System.out.println("ceking id dalam akaun ======== " + idPS);
		ak.setKesalahan(db.find(UtkKesalahan.class, idPS));
		ak.setDebit(Util.getDouble(Util.RemoveComma(get("amaun"))));
		ak.setFlagBayaran("DENDA"); // SEWA / DEPOSIT
		ak.setFlagBayar("T"); // BELUM BAYAR
		ak.setFlagQueue("T");
		ak.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
		ak.setKodHasil(db.find(KodHasil.class,"76199")); // PELBAGAI BAYARAN HUKUMAN
		ak.setNoInvois(idPS);
		ak.setUserPendaftar(db.find(Users.class,userId)); //user login
		ak.setTarikhDaftar(new Date()); 
		
		db.begin();
		if ( addMaklumatAkaun ) db.persist(ak);
		
		try {
			db.commit();
			Boolean kewInvois = pushKew(idPS);
			if(kewInvois){
				statusInfo = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = false;
		}
		return statusInfo;
	}
	
	public Boolean pushKew(String idPS) throws ParseException {
		Boolean statusInfo = false;
		
		UtkAkaun ak = (UtkAkaun) db.get("Select x from UtkAkaun x where x.kesalahan.id='" + idPS + "'");
		KewInvois inv = (KewInvois) db.get("Select x from KewInvois x where x.idLejar='" + ak.getId() + "'");
		Boolean addMaklumatInv = false;
		
		if(inv == null){
			addMaklumatInv = true;
			inv = new KewInvois();
			inv.setTarikhDaftar(new Date()); 
		}
		inv.setDebit(ak.getDebit());
		inv.setFlagBayaran(ak.getFlagBayaran()); // SEWA / DEPOSIT
		inv.setFlagQueue("T");
		inv.setIdLejar(ak.getId());
		inv.setJenisBayaran(db.find(KewJenisBayaran.class,"08")); // BAYARAN LAIN
		inv.setKeteranganBayaran(ak.getKodHasil().getKeterangan());
		inv.setKodHasil(ak.getKodHasil());  // PELBAGAI BAYARAN HUKUMAN
		inv.setNoInvois(ak.getNoInvois());
		inv.setNoRujukan(ak.getKesalahan().getOperasi().getNoRujukanOperasi()); // NO FAIL OPERASI
		inv.setPembayar(db.find(Users.class,ak.getKesalahan().getPenghuni().getPemohon().getId()));
		inv.setTarikhInvois(ak.getKesalahan().getOperasi().getTarikhOperasi());
		inv.setUserPendaftar(db.find(Users.class, userId)); //user login
		inv.setTarikhKemaskini(new Date());
		inv.setUserKemaskini(db.find(Users.class, userId));
		
		db.begin();
		if ( addMaklumatInv ) db.persist(inv);
		
		try {
			db.commit();
			statusInfo = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			statusInfo = false;
		}
		return statusInfo;
	}
	/*************************************************** END PUSH DATA ***************************************************/
	

	/******************************************* START DOKUMEN SOKONGAN *******************************************/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		
		String idOperasi = get("idOperasi");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		
		UtkDokumen dokumen = new UtkDokumen();
		
		String uploadDir = "utk/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists()){
			dir.mkdir();
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null) && (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			String imgName = uploadDir + idOperasi + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf(".")) + "_avatar"
						+ imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + imgName,
						600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder")
						+ imgName, ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName,
						150, 90, 100);
			}

			if (!imgName.equals("")) {
				simpanDokumen(idOperasi, imgName, avatarName, tajukDokumen,
						idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumen(String idOperasi, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen,
			String keteranganDokumen, UtkDokumen dokumen) throws Exception {

//		dokumen.setHakmilik(db.find(UtkDokumen.class, idOperasi));
		dokumen.setOperasi(db.find(UtkOperasi.class, idOperasi));
		dokumen.setPhotofilename(imgName);
		dokumen.setThumbfilename(avatarName);
		dokumen.setTajuk(tajukDokumen);
		dokumen.setJenisDokumen(db.find(JenisDokumen.class, idJenisDokumen));
		dokumen.setKeterangan(keteranganDokumen);

		db.begin();
		db.persist(dokumen);
		db.commit();
	}

	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = get("idDokumen");
		UtkDokumen dokumen = db.find(UtkDokumen.class, idDokumen);

		if (dokumen != null) {
			Util.deleteFile(dokumen.getPhotofilename());
			Util.deleteFile(dokumen.getThumbfilename());
			db.begin();
			db.remove(dokumen);
			db.commit();
		}

		return getMaklumatOperasi();
	}

	@Command("refreshList")
	public String refreshList() throws Exception {
		return getMaklumatOperasi();
	}
	/******************************************* END DOKUMEN SOKONGAN *******************************************/

}

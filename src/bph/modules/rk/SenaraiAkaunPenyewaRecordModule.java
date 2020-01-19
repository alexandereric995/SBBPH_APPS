package bph.modules.rk;

import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.Seksyen;
import bph.entities.kod.Status;
import bph.entities.kod.Warganegara;
import bph.entities.rk.RkAkaun;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkIndividu;
import bph.entities.rk.RkInvois;
import bph.entities.rk.RkPerjanjian;
import bph.entities.rk.RkPermohonan;
import bph.entities.rk.RkRuangKomersil;
import bph.entities.rk.RkSeqPermohonan;
import bph.entities.rk.RkSyarikat;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiAkaunPenyewaRecordModule extends LebahRecordTemplateModule<RkFail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;	
	
	private static String kodHasilSewa = "74202"; // SEWA BANGUNAN PEJABAT LUAR WILAYAH
	private static String kodHasilDeposit = "79503"; // DEPOSIT PELBAGAI
	private static String kodHasilIWK = "81199"; // BAYARAN-BAYARAN BALIK YANG LAIN (UTILITI/IWK)

	@Override
	public Class getIdType() {
		return String.class;
	}	
	
	@Override
	public Class<RkFail> getPersistenceClass() {
		return RkFail.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/rk/senaraiAkaunPenyewa";
	}
	
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectNegeriSurat", dataUtil.getListNegeri());
		context.put("selectWargaNegara", dataUtil.getListWarganegara());
		context.put("selectJenisKegunaanRuang", dataUtil.getListJenisKegunaanRuang());
		
		context.put("util", util);

		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();

	}
			
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
					
	}
	
	private void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}

	private void addfilter() {
		this.addFilter("flagAktifPerjanjian = 'Y'");
		this.setOrderBy("noFail");
		this.setOrderType("asc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void save(RkFail r) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterSave(RkFail r) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getRelatedData(RkFail r) {
		String idFail = r.getId();
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				List<RkAkaun> listDeposit = mp.list("select x from RkAkaun x where x.kodHasil.id = '" + kodHasilDeposit + "' and x.flagAktif = 'Y' and x.fail.id = '" + fail.getId() + "' order by x.tarikhTransaksi, x.idJenisTransaksi asc");
				context.put("listDeposit", listDeposit);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");
	}

	@Override
	public boolean delete(RkFail r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}	

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		
		m.put("noFail", getParam("findNoFail").trim());
		m.put("pemohon.individu.id", getParam("findNoPengenalan").trim());
		m.put("pemohon.syarikat.id", getParam("findNoPendaftaran").trim());
		m.put("pemohon.individu.nama", getParam("findNamaPemohon").trim());
		m.put("pemohon.syarikat.nama", getParam("findNamaSyarikat").trim());
		
		m.put("ruang.idRuang", getParam("findIdRuang").trim());
		m.put("ruang.namaRuang", getParam("findNamaRuang").trim());
		m.put("ruang.jenisKegunaanRuang.id", new OperatorEqualTo(getParam("findJenisKegunaanRuang")));		
		m.put("ruang.lokasi", getParam("findLokasi").trim());
		m.put("ruang.bandar.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		m.put("ruang.bandar.id", new OperatorEqualTo(getParam("findBandar")));
		
		return m;
	}
	
	/** START TAB **/
	@Command("getMaklumatDeposit")
	public String getMaklumatDeposit(){
		String idFail = getParam("idFail");
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				List<RkAkaun> listDeposit = mp.list("select x from RkAkaun x where x.kodHasil.id = '" + kodHasilDeposit + "' and x.flagAktif = 'Y' and x.fail.id = '" + fail.getId() + "' order by x.tarikhTransaksi, x.idJenisTransaksi asc");
				context.put("listDeposit", listDeposit);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "1");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatSewa")
	public String getMaklumatSewa(){
		String idFail = getParam("idFail");
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				List<RkAkaun> listSewa = mp.list("select x from RkAkaun x where x.kodHasil.id = '" + kodHasilSewa + "' and x.flagAktif = 'Y' and x.fail.id = '" + fail.getId() + "' order by x.tarikhTransaksi, x.idJenisTransaksi asc");
				context.put("listSewa", listSewa);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "2");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatIWK")
	public String getMaklumatIWK(){
		String idFail = getParam("idFail");
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				List<RkAkaun> listIWK = mp.list("select x from RkAkaun x where x.kodHasil.id = '" + kodHasilIWK + "' and x.flagAktif = 'Y' and x.fail.id = '" + fail.getId() + "' order by x.tarikhTransaksi, x.idJenisTransaksi asc");
				context.put("listIWK", listIWK);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "3");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPerjanjian")
	public String getMaklumatPerjanjian(){
		String idFail = getParam("idFail");
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {	
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				List<RkPerjanjian> listPerjanjian = mp.list("select x from RkPerjanjian x where x.flagAktif = 'Y' and x.fail.id = '" + fail.getId() + "' order by x.tarikhMula, x.idJenisPerjanjian asc");
				context.put("listPerjanjian", listPerjanjian);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.remove("flagSuccessDaftar");
		context.remove("errMsg");
		context.put("selectedTab", "4");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatIndividu")
	public String getMaklumatIndividu(){
		String idFail = getParam("idFail");
		List<Bandar> listBandar = null;
		List<Bandar> listBandarSurat = null;
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				if (fail.getPemohon() != null) {
					if (fail.getPemohon().getIndividu() != null) {
						if (fail.getPemohon().getIndividu().getBandar() != null) {
							if (fail.getPemohon().getIndividu().getBandar().getNegeri() != null) {
								listBandar = dataUtil.getListBandar(fail.getPemohon().getIndividu().getBandar().getNegeri().getId());
							}
						}
						if (fail.getPemohon().getIndividu().getBandarSurat() != null) {
							if (fail.getPemohon().getIndividu().getBandarSurat().getNegeri() != null) {
								listBandarSurat = dataUtil.getListBandar(fail.getPemohon().getIndividu().getBandarSurat().getNegeri().getId());
							}
						}
					}
				}
			}

			context.put("selectBandar", listBandar);
			context.put("selectBandarSurat", listBandarSurat);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "5");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatSyarikat")
	public String getMaklumatSyarikat(){
		String idFail = getParam("idFail");
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			context.put("r", fail);
			
			if (fail != null) {
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				if (fail.getPemohon() != null) {
					if (fail.getPemohon().getSyarikat() != null) {
						if (fail.getPemohon().getSyarikat().getBandar() != null) {
							if (fail.getPemohon().getSyarikat().getBandar().getNegeri() != null) {
								listBandar = dataUtil.getListBandar(fail.getPemohon().getSyarikat().getBandar().getNegeri().getId());
							}
						}							
					}
				}
			}

			context.put("selectBandar", listBandar);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "6");	
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	@Command("doKemaskiniMaklumatIndividu")
	public String doKemaskiniMaklumatIndividu(){
		boolean addIndividu = false;
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idFail = getParam("idFail");
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			if (fail != null) {
				mp.begin();
				RkIndividu individu = (RkIndividu) mp.find(RkIndividu.class, getParam("noPengenalan"));
				if (individu == null) {
					individu = new RkIndividu();
					individu.setId(getParam("noPengenalan"));
					addIndividu = true;
				}
				individu.setNama(getParam("nama"));
				individu.setJawatan(getParam("jawatan"));
				individu.setWarganegara((Warganegara) mp.find(Warganegara.class, getParam("idWarganegara")));
				individu.setAlamat1(getParam("alamat1"));
				individu.setAlamat2(getParam("alamat2"));
				individu.setAlamat3(getParam("alamat3"));
				individu.setPoskod(getParam("poskod"));
				individu.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				individu.setAlamat1Surat(getParam("alamat1Surat"));
				individu.setAlamat2Surat(getParam("alamat2Surat"));
				individu.setAlamat3Surat(getParam("alamat3Surat"));
				individu.setPoskodSurat(getParam("poskodSurat"));
				individu.setBandarSurat((Bandar) mp.find(Bandar.class, getParam("idBandarSurat")));
				individu.setNoTelefon(getParam("noTelefon"));
				individu.setNoTelefonBimbit(getParam("noTelefonBimbit"));
				individu.setNoFaks(getParam("noFaks"));
				individu.setEmel(getParam("emel"));
				if (addIndividu) {
					mp.persist(individu);
				}
				
				if (fail.getPemohon() != null) {
					fail.getPemohon().setIndividu(individu);					
				}
				mp.commit();
				
				UtilRk.daftarUsersBagiPenyewaRK(fail, mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatIndividu();
	}
	
	@Command("doKemaskiniMaklumatSyarikat")
	public String doKemaskiniMaklumatSyarikat(){
		boolean addSyarikat = false;
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idFail = getParam("idFail");
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			if (fail != null) {
				mp.begin();
				RkSyarikat syarikat = (RkSyarikat) mp.find(RkSyarikat.class, getParam("noPendaftaran"));
				if (syarikat == null) {
					syarikat = new RkSyarikat();
					syarikat.setId(getParam("noPendaftaran"));
					addSyarikat = true;
				}
				syarikat.setNama(getParam("nama"));
				syarikat.setIdJenisPemilikan(getParam("idJenisPemilikan"));
				syarikat.setTarafBumiputera(getParam("idTarafBumiputera"));
				syarikat.setTarikhPenubuhan(getDate("tarikhPenubuhan"));
				syarikat.setBidangSyarikat(getParam("bidangSyarikat"));
				syarikat.setAlamat1(getParam("alamat1"));
				syarikat.setAlamat2(getParam("alamat2"));
				syarikat.setAlamat3(getParam("alamat3"));
				syarikat.setPoskod(getParam("poskod"));
				syarikat.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				if (addSyarikat) {
					mp.persist(syarikat);
				}
				
				if (fail.getPemohon() != null) {
					fail.getPemohon().setSyarikat(syarikat);
				}
				mp.commit();
				
				UtilRk.daftarUsersBagiPenyewaRK(fail, mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatSyarikat();
	}
	
	@Command("doKemaskiniMaklumatPerjanjian")
	public String doKemaskiniMaklumatPerjanjian(){
		boolean addPerjanjian = false;
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idFail = getParam("idFail");
		String idPerjanjian = getParam("idPerjanjian");
		String idJenisPerjanjian = getParam("idJenisPerjanjian");
		Integer tempoh = getParamAsInteger("tempoh");
		String kadarSewa = Util.RemoveComma(getParam("kadarSewa"));
		String deposit = Util.RemoveComma(getParam("deposit"));
		String kadarBayaranIWK = Util.RemoveComma(getParam("kadarBayaranIWK"));
		if ("".equals(tempoh)) { tempoh = 0; }
		if ("".equals(kadarSewa)) { kadarSewa = "0"; }
		if ("".equals(deposit)) { deposit = "0"; }		
		if ("".equals(kadarBayaranIWK)) { kadarBayaranIWK = "0"; }		
		
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			if (fail != null) {
				mp.begin();
				RkPerjanjian perjanjian = (RkPerjanjian) mp.find(RkPerjanjian.class, idPerjanjian);
				if (perjanjian == null) {
					perjanjian = new RkPerjanjian();
					perjanjian.setFail(fail);
					perjanjian.setFlagKutipanData("Y");
					perjanjian.setDaftarOleh((Users) mp.find(Users.class, userId));
					addPerjanjian = true;
				} else {
					perjanjian.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					perjanjian.setTarikhKemaskini(new Date());
				}
				perjanjian.setNoRujukanSST(getParam("noRujukanSST"));
				perjanjian.setTarikhMulaSST(getDate("tarikhMulaSST"));
				perjanjian.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
				
				perjanjian.setIdJenisPerjanjian(idJenisPerjanjian);
				perjanjian.setNoRujukan(getParam("noRujukan"));
				perjanjian.setTarikhMula(getDate("tarikhMula"));
				perjanjian.setTempoh(tempoh);
				perjanjian.setTarikhTamat(getDate("tarikhTamat"));
				if ("4".equals(idJenisPerjanjian)) {
					kadarSewa = "0";
				}
				perjanjian.setKadarSewa(Double.valueOf(kadarSewa));
				perjanjian.setDeposit(Double.valueOf(deposit));				
				perjanjian.setIdJenisSewa(getParam("idJenisSewa"));
				perjanjian.setFlagCajIWK(getParam("flagCajIWK"));
				perjanjian.setKadarBayaranIWK(Double.valueOf(kadarBayaranIWK));
				perjanjian.setFlagAktif("Y");
				perjanjian.setCatatan(getParam("catatanPerjanjian"));	
				if (addPerjanjian) {
					mp.persist(perjanjian);
				}
				mp.commit();
				
				ServletContext servletContext = getServletContext();
				UtilRk.kemaskiniRekodPerjanjianDanAkaun(fail, false, true, mp, servletContext);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPerjanjian();
	}
	
	@Command("deletePerjanjian")
	public String deletePerjanjian(){
		boolean deletePerjanjian = false;
		String idPerjanjian = getParam("perjanjianId");
		try {
			mp = new MyPersistence();
			RkPerjanjian perjanjian = (RkPerjanjian) mp.find(RkPerjanjian.class, idPerjanjian);
			if (perjanjian != null) {
				RkFail fail = perjanjian.getFail();
				if (perjanjian.getIdJenisPerjanjian().equals("1") || perjanjian.getIdJenisPerjanjian().equals("2")){
					List<RkPerjanjian> listPerjanjian = mp.list("select x from RkPerjanjian x where x.flagAktif = 'Y' and x.idJenisPerjanjian in ('1', '2') and x.fail.id = '" + perjanjian.getFail().getId() + "'");
					if (listPerjanjian.size() > 1) {
						deletePerjanjian = true;		
					}
				} else {
					deletePerjanjian = true;
				}				
				
				if (deletePerjanjian) {
					mp.begin();
					mp.remove(perjanjian);
					mp.commit();
					
					ServletContext servletContext = getServletContext();
					UtilRk.kemaskiniRekodPerjanjianDanAkaun(fail, false, true, mp, servletContext);
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPerjanjian();
	}
	
	@Command("popupDaftarMaklumatPerjanjian")
	public String popupDaftarMaklumatPerjanjian() throws Exception{	
		context.remove("perjanjian");	
		context.put("flagDaftarPerjanjian", "Y");
		return getPath() + "/maklumatPerjanjian/popupMaklumatPerjanjian.vm";
	}
	
	@Command("popupPaparMaklumatPerjanjian")
	public String popupMaklumatPerjanjian() throws Exception{	
		String idPerjanjian = getParam("perjanjianId");	
		try {
			mp = new MyPersistence();
			
			RkPerjanjian perjanjian = (RkPerjanjian) mp.find(RkPerjanjian.class, idPerjanjian);
			context.put("perjanjian", perjanjian);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("flagDaftarPerjanjian", "T");
		return getPath() + "/maklumatPerjanjian/popupMaklumatPerjanjian.vm";
	}
	
	@Command("doChangeJenisPerjanjian")
	public String doChangeJenisPerjanjian() throws Exception{	
		String idFail = getParam("idFail");
		String idPerjanjian = getParam("idPerjanjian");	
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			
			String idJenisPerjanjian = getParam("idJenisPerjanjian");
			String idJenisSewa = getParam("idJenisSewa");
			Integer tempoh = getParamAsInteger("tempoh");
			String kadarSewa = Util.RemoveComma(getParam("kadarSewa"));
			String deposit = Util.RemoveComma(getParam("deposit"));
			String kadarBayaranIWK = Util.RemoveComma(getParam("kadarBayaranIWK"));
			if ("".equals(tempoh)) { tempoh = 0; }
			if ("".equals(kadarSewa)) { kadarSewa = "0"; }
			if ("".equals(deposit)) { deposit = "0"; }	
			if ("".equals(kadarBayaranIWK)) { kadarBayaranIWK = "0"; }		
			
			RkPerjanjian perjanjian = (RkPerjanjian) mp.find(RkPerjanjian.class, idPerjanjian);
			if (perjanjian == null) {
				perjanjian = new RkPerjanjian();
			}			
			perjanjian.setIdJenisPerjanjian(idJenisPerjanjian);
			perjanjian.setIdJenisSewa(idJenisSewa);
			perjanjian.setNoRujukan(getParam("noRujukan"));
			perjanjian.setTarikhMula(getDate("tarikhMula"));
			perjanjian.setTempoh(getParamAsInteger("tempoh"));
			if (!"".equals(getParam("tarikhMula")) && !"".equals(getParam("tempoh")) && !"0".equals(getParam("tempoh")) && !"".equals(perjanjian.getIdJenisSewa())){
				Calendar calTamat = new GregorianCalendar();
				calTamat.setTime(getDate("tarikhMula"));
				if ("H".equals(perjanjian.getIdJenisSewa())) {
					calTamat.add(Calendar.DATE, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					perjanjian.setTarikhTamat(calTamat.getTime());
				} else if ("B".equals(perjanjian.getIdJenisSewa())) {
					calTamat.add(Calendar.MONTH, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					perjanjian.setTarikhTamat(calTamat.getTime());
				} else if ("T".equals(perjanjian.getIdJenisSewa())) {
					calTamat.add(Calendar.YEAR, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					perjanjian.setTarikhTamat(calTamat.getTime());
				} else {
					perjanjian.setTarikhTamat(getDate("tarikhTamat"));
				}
			} else {
				perjanjian.setTarikhTamat(getDate("tarikhTamat"));
			}
			if ("4".equals(idJenisPerjanjian)) {
				kadarSewa = "0";
			}
			perjanjian.setKadarSewa(Double.valueOf(kadarSewa));
			perjanjian.setDeposit(Double.valueOf(deposit));
			perjanjian.setFlagCajIWK(getParam("flagCajIWK"));
			perjanjian.setKadarBayaranIWK(Double.valueOf(kadarBayaranIWK));
			perjanjian.setIdJenisSewa(getParam("idJenisSewa"));
			perjanjian.setNoRujukanSST(getParam("noRujukanSST"));
			perjanjian.setTarikhMulaSST(getDate("tarikhMulaSST"));
			perjanjian.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
			perjanjian.setCatatan(getParam("catatanPerjanjian"));
			
			if (fail != null) {
				perjanjian.setFail(fail);
			}			
			context.put("perjanjian", perjanjian);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagDaftarPerjanjian", getParam("flagDaftarPerjanjian"));
		return getPath() + "/maklumatPerjanjian/popupMaklumatPerjanjian.vm";
	}
	
	@Command("popupDaftarPermohonanPerjanjianTambahan")
	public String popupDaftarPermohonanPerjanjianTambahan() throws Exception{	
		context.remove("permohonan");	
		context.remove("perjanjian");		
		return getPath() + "/maklumatPerjanjian/popupPermohonanPerjanjianTambahan.vm";
	}
	
	@Command("doChangeJenisPermohonanPerjanjianTambahan")
	public String doChangeJenisPermohonanPerjanjianTambahan() throws Exception{	
		try {
			mp = new MyPersistence();
			RkPerjanjian perjanjianSemasa = null;
			
			String idFail = getParam("idFail");
			String idJenisPerjanjian = getParam("idJenisPerjanjian");
			String kadarSewa = Util.RemoveComma(getParam("kadarSewa"));
			String deposit = Util.RemoveComma(getParam("deposit"));
			if ("".equals(kadarSewa)) { kadarSewa = "0"; }
			if ("".equals(deposit)) { deposit = "0"; }	
			
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			if (fail != null) {			
				perjanjianSemasa = fail.getPerjanjianSemasa();
			}
			
			RkPermohonan permohonan = new RkPermohonan();
			permohonan.setTarikhPermohonan(getDate("tarikhPermohonan"));
			permohonan.setNoRujukanPermohonan(getParam("noRujukanPermohonan"));
			permohonan.setTarikhTerimaPermohonan(getDate("tarikhTerimaPermohonan"));
			
			RkPerjanjian perjanjian = new RkPerjanjian();	
			if (perjanjianSemasa != null) {
				perjanjian.setIdJenisSewa(perjanjianSemasa.getIdJenisSewa());
			}
			perjanjian.setIdJenisPerjanjian(idJenisPerjanjian);
			perjanjian.setTarikhMula(getDate("tarikhMula"));
			perjanjian.setTempoh(getParamAsInteger("tempoh"));
			if (!"".equals(getParam("tarikhMula")) && !"".equals(getParam("tempoh")) && !"0".equals(getParam("tempoh")) && !"".equals(perjanjian.getIdJenisSewa())){
				Calendar calTamat = new GregorianCalendar();
				calTamat.setTime(getDate("tarikhMula"));
				if ("H".equals(perjanjian.getIdJenisSewa())) {
					calTamat.add(Calendar.DATE, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					perjanjian.setTarikhTamat(calTamat.getTime());
				} else if ("B".equals(perjanjian.getIdJenisSewa())) {
					calTamat.add(Calendar.MONTH, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					perjanjian.setTarikhTamat(calTamat.getTime());
				} else if ("T".equals(perjanjian.getIdJenisSewa())) {
					calTamat.add(Calendar.YEAR, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					perjanjian.setTarikhTamat(calTamat.getTime());
				} else {
					perjanjian.setTarikhTamat(getDate("tarikhTamat"));
				}
			} else {
				perjanjian.setTarikhTamat(getDate("tarikhTamat"));
			}
			perjanjian.setKadarSewa(Double.valueOf(kadarSewa));
			perjanjian.setDeposit(Double.valueOf(deposit));
			perjanjian.setCatatan(getParam("catatanPerjanjian"));			
					
			context.put("permohonan", permohonan);
			context.put("perjanjian", perjanjian);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatPerjanjian/popupPermohonanPerjanjianTambahan.vm";
	}
	
	@Command("doDaftarPermohonanPerjanjianTambahan")
	public String doDaftarPermohonanPerjanjianTambahan(){
		String flagSuccessDaftar = "T";
		String errMsg = "";
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idFail = getParam("idFail");
		String idJenisPerjanjian = getParam("idJenisPerjanjian");
		String kadarSewa = Util.RemoveComma(getParam("kadarSewa"));
		if ("".equals(kadarSewa)) { kadarSewa = "0"; }		
		
		try {
			mp = new MyPersistence();
			RkFail fail = (RkFail) mp.find(RkFail.class, idFail);
			if (fail != null) {			
				RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.flagPerjanjianSemasa = 'Y' and x.fail.id = '" + fail.getId() + "'");
				context.put("perjanjian", perjanjian);
				
				List<RkPerjanjian> listPerjanjian = mp.list("select x from RkPerjanjian x where x.flagAktif = 'Y' and x.fail.id = '" + fail.getId() + "' order by x.tarikhMula, x.idJenisPerjanjian asc");
				context.put("listPerjanjian", listPerjanjian);
				
				RkPerjanjian perjanjianSemasa = fail.getPerjanjianSemasa();
				if (perjanjianSemasa != null) {
					mp.begin();
					
					RkPermohonan permohonan = new RkPermohonan();
					permohonan.setIdJenisPermohonan(idJenisPerjanjian);
					permohonan.setTarikhPermohonan(getDate("tarikhPermohonan"));
					permohonan.setNoRujukanPermohonan(getParam("noRujukanPermohonan"));
					permohonan.setTarikhTerimaPermohonan(getDate("tarikhTerimaPermohonan"));
					permohonan.setCatatan(getParam("catatanPerjanjian"));
					
					permohonan.setFail(fail);
					permohonan.setNoPermohonan(generateNoPermohonan(mp, fail.getRuang()));
					
					permohonan.setIdJenisSewa(perjanjianSemasa.getIdJenisSewa());
					permohonan.setKadarSewaJPPH(0D);
					permohonan.setHargaTawaranSewa(Double.valueOf(Util.RemoveComma(kadarSewa)));
					permohonan.setTarikhMulaOperasi(getDate("tarikhMula"));
					permohonan.setTempoh(getParamAsInteger("tempoh"));
					permohonan.setTarikhTamatOperasi(getDate("tarikhTamat"));
					permohonan.setStatus((Status) mp.find(Status.class, "1433140179898"));
					permohonan.setDaftarOleh((Users) mp.find(Users.class, userId));
					
					mp.persist(permohonan);
					mp.commit();
					flagSuccessDaftar = "Y";
				}				
			}
		} catch (Exception ex) {
			errMsg = ex.toString();
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagSuccessDaftar", flagSuccessDaftar);
		context.put("errMsg", errMsg);
		context.put("selectedTab", "4");	
		return getPath() + "/entry_page.vm";
	}
	
	private String generateNoPermohonan(MyPersistence mp, RkRuangKomersil ruangKomersil) {
		String noPermohonan = "";
		String kodSeksyen = "";
		int bil = 1;
		try {
			
			Calendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			String year = String.valueOf(cal.get(Calendar.YEAR));
			String idSeksyen = ruangKomersil.getSeksyen().getId();
			String id = year + idSeksyen;
			
			RkSeqPermohonan seq = (RkSeqPermohonan) mp.find(RkSeqPermohonan.class, id);
			if(seq != null){
				mp.pesismisticLock(seq);
				bil = seq.getBil() + 1;
				seq.setBil(bil);
				seq = (RkSeqPermohonan) mp.merge(seq);
			} else {
				seq = new RkSeqPermohonan();
				seq.setId(id);
				seq.setSeksyen((Seksyen) mp.find(Seksyen.class, idSeksyen));
				seq.setTahun(cal.get(Calendar.YEAR));
				seq.setBil(bil);
				mp.persist(seq);
				mp.flush();
			}
			
			if ("06".equals(idSeksyen)) {
				kodSeksyen = "BGS";
			} else if ("07".equals(idSeksyen)) {
				kodSeksyen = "UT";
			} else {
				kodSeksyen = "00";
			}
			
			noPermohonan = "BPH/RK/" + kodSeksyen + "/" + cal.get(Calendar.YEAR) + "/" + new DecimalFormat("000").format(bil);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return noPermohonan;
	}
	
	@Command("doCetakResit")
	public String doCetakResit() throws Exception{		
		String idAkaun = getParam("akaunId");	
		try {
			mp = new MyPersistence();
			
			RkAkaun akaun = (RkAkaun) mp.find(RkAkaun.class, idAkaun);
			if (akaun != null) {
				context.put("idResit", akaun.getResit().getId());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/cetakResit.vm";
	}
	
	@Command("doCetakInvois")
	public String doCetakInvois() throws Exception{		
		String idAkaun = getParam("akaunId");	
		try {
			mp = new MyPersistence();
			
			RkAkaun akaun = (RkAkaun) mp.find(RkAkaun.class, idAkaun);
			if (akaun != null) {
				context.put("idInvois", akaun.getInvois().getId());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/cetakInvois.vm";
	}
	
	@Command("doCetakInvoisTerkini")
	public String doCetakInvoisTerkini() throws Exception{		
		String idFail = getParam("idFail");	
		try {
			mp = new MyPersistence();
			
			RkInvois invois = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilSewa + "' and x.fail.id = '" + idFail + "' order by x.tarikhInvois desc");
			if (invois != null) {
				context.put("idInvois", invois.getId());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/cetakInvois.vm";
	}
	
	@Command("doEmelInvoisTerkini")
	public String doEmelInvoisTerkini() throws Exception{
		Db lebahDb = null;
		String idFail = getParam("idFail");	
		try {
			mp = new MyPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			RkInvois invois = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilSewa + "' and x.fail.id = '" + idFail + "' order by x.tarikhInvois desc");
			if (invois != null) {
				context.put("idInvois", invois.getId());
				
				//GENERATE EMEL
				ServletContext servletContext = getServletContext();
				UtilRk.janaEmelInvois(invois, stmt, servletContext); //GENERATE EMEL
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
			if (lebahDb != null) { lebahDb.close(); }
		}
		
		return getPath() + "/cetakInvois.vm";
	}
	
	@Command("doCetakInvoisIWK")
	public String doCetakInvoisIWK() throws Exception{		
		String idAkaun = getParam("akaunId");	
		try {
			mp = new MyPersistence();
			
			RkAkaun akaun = (RkAkaun) mp.find(RkAkaun.class, idAkaun);
			if (akaun != null) {
				context.put("idInvois", akaun.getInvois().getId());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/cetakInvoisIWK.vm";
	}
	
	@Command("doCetakInvoisIWKTerkini")
	public String doCetakInvoisIWKTerkini() throws Exception{		
		String idFail = getParam("idFail");	
		try {
			mp = new MyPersistence();
			
			RkInvois invois = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilIWK + "' and x.fail.id = '" + idFail + "' order by x.tarikhInvois desc");
			if (invois != null) {
				context.put("idInvois", invois.getId());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/cetakInvoisIWK.vm";
	}
	
	@Command("doEmelInvoisIWKTerkini")
	public String doEmelInvoisIWKTerkini() throws Exception{	
		Db lebahDb = null;
		String idFail = getParam("idFail");	
		try {
			mp = new MyPersistence();
			lebahDb = new Db();
			Statement stmt = lebahDb.getStatement();
			
			RkInvois invois = (RkInvois) mp.get("select x from RkInvois x where x.flagAktif = 'Y' and x.kodHasil.id = '" + kodHasilIWK + "' and x.fail.id = '" + idFail + "' order by x.tarikhInvois desc");
			if (invois != null) {
				context.put("idInvois", invois.getId());
				
				//GENERATE EMEL
				ServletContext servletContext = getServletContext();
				UtilRk.janaEmelInvois(invois, stmt, servletContext); //GENERATE EMEL
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
			if (lebahDb != null) { lebahDb.close(); }
		}
		
		return getPath() + "/cetakInvoisIWK.vm";
	}
		
	/** START DROP DOWN LIST **/
	@Command("findBandar")
	public String findBandar() throws Exception {

		String idNegeri = "0";
		if (getParam("findNegeri").trim().length() > 0) {
			idNegeri = getParam("findNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {

		String idNegeri = "0";
		if (getParam("idNegeri").trim().length() > 0) {
			idNegeri = getParam("idNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatIndividu/selectBandar.vm";
	}
	
	@Command("selectBandarSurat")
	public String selectBandarSurat() throws Exception {

		String idNegeri = "0";
		if (getParam("idNegeriSurat").trim().length() > 0) {
			idNegeri = getParam("idNegeriSurat");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandarSurat", list);

		return getPath() + "/maklumatIndividu/selectBandarSurat.vm";
	}
	
	@Command("selectBandarSyarikat")
	public String selectBandarSyarikat() throws Exception {

		String idNegeri = "0";
		if (getParam("idNegeri").trim().length() > 0) {
			idNegeri = getParam("idNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatSyarikat/selectBandar.vm";
	}
	/** END DROP DOWN LIST **/
}

package bph.modules.rk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletContext;

import lebah.portal.AjaxBasedModule;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.entities.kod.Bandar;
import bph.entities.kod.Warganegara;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkIndividu;
import bph.entities.rk.RkPemohon;
import bph.entities.rk.RkPerjanjian;
import bph.entities.rk.RkRuangKomersil;
import bph.entities.rk.RkSyarikat;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class KutipanDataFailPenyewaUKK extends AjaxBasedModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String path = "bph/modules/rk/kutipanData";
	private static final String idSeksyen = "07";
	private Util util = new Util();
	private DataUtil dataUtil;
	private MyPersistence mp;
	private DbPersistence lebahDb = new DbPersistence();

	@Override
	public String doTemplate2() throws Exception {
		String userId = (String) request.getSession().getAttribute("_portal_login");;
		
		String vm = "/start.vm";
		String command = getParam("command");
		RkFail fail = null;
		RkPerjanjian perjanjian = null;
		boolean successDaftar = false;
		
		try {
			mp = new MyPersistence();
			dataUtil = DataUtil.getInstance(lebahDb);
			context.put("selectNegeri", dataUtil.getListNegeri());
			context.put("selectNegeriSurat", dataUtil.getListNegeri());
			context.put("selectWargaNegara", dataUtil.getListWarganegara());
			
			context.remove("existNoFail");			
			if ("doSemakNoFail".equals(command)) {
				String noFail = getParam("noFail").trim().toUpperCase();
				fail = (RkFail) mp.get("select x from RkFail x where x.noFail = '" + noFail + "'");
				if (fail != null) {
					fail = null;
					context.put("existNoFail", "Y");
				} else {
					fail = new RkFail();
					fail.setNoFail(noFail);					
				}
				context.put("fail", fail);
				vm = "/maklumatFail.vm";
				
			} else if ("getSenaraiRuang".equals(command)) {
				List<RkRuangKomersil> listRuang = mp.list("select x from RkRuangKomersil x where x.flagSewa = 'T' and x.flagAktif = 'Y' and x.seksyen.id = '" + idSeksyen + "'");
				context.put("listRuang", listRuang);
				
				vm = "/maklumatRuang/popupSenaraiRuang.vm";
				
			} else if ("getPilihanRuang".equals(command)) {
				String strRadioRuang = getParam("radioRuang");
				
				RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, strRadioRuang);
				context.put("ruang", ruang);
				
				vm = "/maklumatRuang/maklumatRuang.vm";
				
			} else if ("getMaklumatPerjanjian".equals(command)) {
				perjanjian = new RkPerjanjian();
				perjanjian.setNoRujukanSST(getParam("noRujukanSST"));
				perjanjian.setTarikhMulaSST(getDate("tarikhMulaSST"));
				perjanjian.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
				perjanjian.setNoRujukan(getParam("noRujukan"));
				perjanjian.setTarikhMula(getDate("tarikhMula"));
				perjanjian.setTempoh(getParamAsInteger("tempoh"));
				if (!"".equals(getParam("tarikhMula")) && !"".equals(getParam("tempoh")) && !"0".equals(getParam("tempoh")) && !"".equals(getParam("idJenisSewa"))){
					Calendar calTamat = new GregorianCalendar();
					calTamat.setTime(getDate("tarikhMula"));
					if ("H".equals(getParam("idJenisSewa"))) {
						calTamat.add(Calendar.DATE, getParamAsInteger("tempoh"));
						calTamat.add(Calendar.DATE, -1);
						perjanjian.setTarikhTamat(calTamat.getTime());
					} else if ("B".equals(getParam("idJenisSewa"))) {
						calTamat.add(Calendar.MONTH, getParamAsInteger("tempoh"));
						calTamat.add(Calendar.DATE, -1);
						perjanjian.setTarikhTamat(calTamat.getTime());
					} else if ("T".equals(getParam("idJenisSewa"))) {
						calTamat.add(Calendar.YEAR, getParamAsInteger("tempoh"));
						calTamat.add(Calendar.DATE, -1);
						perjanjian.setTarikhTamat(calTamat.getTime());
					} else {
						perjanjian.setTarikhTamat(getDate("tarikhTamat"));
					}
				} else {
					perjanjian.setTarikhTamat(getDate("tarikhTamat"));
				}
				if (getParam("kadarSewa") != null && getParam("kadarSewa").length() > 0) 
					perjanjian.setKadarSewa(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarSewa")))));
				if (getParam("deposit") != null && getParam("deposit").length() > 0) 
					perjanjian.setDeposit(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("deposit")))));
				perjanjian.setFlagCajIWK(getParam("flagCajIWK"));
				if (getParam("kadarBayaranIWK") != null && getParam("kadarBayaranIWK").length() > 0) 
					perjanjian.setKadarBayaranIWK(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarBayaranIWK")))));
				perjanjian.setIdJenisSewa(getParam("idJenisSewa"));
				perjanjian.setCatatan(getParam("catatan"));

				context.put("perjanjian", perjanjian);
				vm = "/maklumatPerjanjian/maklumatPerjanjian.vm";
				
			} else if ("getSenaraiIndividu".equals(command)) {
				List<RkIndividu> listIndividu = mp.list("select x from RkIndividu x order by x.nama asc");
				context.put("listIndividu", listIndividu);
				
				vm = "/maklumatIndividu/popupSenaraiIndividu.vm";
				
			} else if ("getPilihanIndividu".equals(command)) {
				String strRadioIndividu = getParam("radioIndividu");
				
				List<Bandar> listBandar = null;
				List<Bandar> listBandarSurat = null;
				RkIndividu individu = (RkIndividu) mp.find(RkIndividu.class, strRadioIndividu);
				context.put("individu", individu);

				if (individu != null) {
					if (individu.getBandar() != null) {
						if (individu.getBandar().getNegeri() != null) {
							listBandar = dataUtil.getListBandar(individu.getBandar().getNegeri().getId());
						}
					}
					if (individu.getBandarSurat() != null) {
						if (individu.getBandarSurat().getNegeri() != null) {
							listBandarSurat = dataUtil.getListBandar(individu.getBandarSurat().getNegeri().getId());
						}
					}
					context.put("noPengenalan", individu.getId());
				} else {
					context.remove("noPengenalan");
				}
				context.put("selectBandar", listBandar);
				context.put("selectBandarSurat", listBandarSurat);
				
				
				vm = "/maklumatIndividu/maklumatIndividu.vm";
				
			} else if ("getMaklumatIndividuBerdaftar".equals(command)) {
				String noPengenalan = getParam("noPengenalan").replace("-", "").trim().toUpperCase();
				List<Bandar> listBandar = null;
				List<Bandar> listBandarSurat = null;
				RkIndividu individu = (RkIndividu) mp.find(RkIndividu.class, noPengenalan);
				context.put("individu", individu);
				
				if (individu != null) {
					if (individu.getBandar() != null) {
						if (individu.getBandar().getNegeri() != null) {
							listBandar = dataUtil.getListBandar(individu.getBandar().getNegeri().getId());
						}
					}
					if (individu.getBandarSurat() != null) {
						if (individu.getBandarSurat().getNegeri() != null) {
							listBandarSurat = dataUtil.getListBandar(individu.getBandarSurat().getNegeri().getId());
						}
					}
				}
				context.put("selectBandar", listBandar);
				context.put("selectBandarSurat", listBandarSurat);
				context.put("noPengenalan", noPengenalan);
				
				vm = "/maklumatIndividu/maklumatIndividu.vm";
				
			} else if ("selectBandar".equals(command)) {
				
				String idNegeri = "0";
				if (getParam("idNegeri").trim().length() > 0) {
					idNegeri = getParam("idNegeri");
				}
				List<Bandar> list = dataUtil.getListBandar(idNegeri);
				context.put("selectBandar", list);

				vm = "/maklumatIndividu/selectBandar.vm";
				
			} else if ("selectBandarSurat".equals(command)) {
				
				String idNegeri = "0";
				if (getParam("idNegeriSurat").trim().length() > 0) {
					idNegeri = getParam("idNegeriSurat");
				}
				List<Bandar> list = dataUtil.getListBandar(idNegeri);
				context.put("selectBandarSurat", list);

				vm = "/maklumatIndividu/selectBandarSurat.vm";
			
			} else if ("getSenaraiSyarikat".equals(command)) {
				List<RkSyarikat> listSyarikat = mp.list("select x from RkSyarikat x order by x.nama asc");
				context.put("listSyarikat", listSyarikat);
				
				vm = "/maklumatSyarikat/popupSenaraiSyarikat.vm";
				
			} else if ("getPilihanSyarikat".equals(command)) {
				String strRadioSyarikat = getParam("radioSyarikat");
				
				List<Bandar> listBandar = null;
				RkSyarikat syarikat = (RkSyarikat) mp.find(RkSyarikat.class, strRadioSyarikat);
				context.put("syarikat", syarikat);

				if (syarikat != null) {
					if (syarikat.getBandar() != null) {
						if (syarikat.getBandar().getNegeri() != null) {
							listBandar = dataUtil.getListBandar(syarikat.getBandar().getNegeri().getId());
						}
					}
					context.put("noPendaftaran", syarikat.getId());
				} else {
					context.remove("noPendaftaran");
				}
				context.put("selectBandar", listBandar);				
				
				vm = "/maklumatSyarikat/maklumatSyarikat.vm";
				
			} else if ("getMaklumatSyarikatBerdaftar".equals(command)) {
				String noPendaftaran = getParam("noPendaftaran").trim().toUpperCase();
				List<Bandar> listBandar = null;
				RkSyarikat syarikat = (RkSyarikat) mp.find(RkSyarikat.class, noPendaftaran);
				context.put("syarikat", syarikat);
				
				if (syarikat != null) {
					if (syarikat.getBandar() != null) {
						if (syarikat.getBandar().getNegeri() != null) {
							listBandar = dataUtil.getListBandar(syarikat.getBandar().getNegeri().getId());
						}
					}
				}
				context.put("selectBandar", listBandar);
				context.put("noPendaftaran", noPendaftaran);
				
				vm = "/maklumatSyarikat/maklumatSyarikat.vm";
			
			} else if ("selectBandarSyarikat".equals(command)) {
				String idNegeri = "0";
				if (getParam("idNegeriSyarikat").trim().length() > 0) {
					idNegeri = getParam("idNegeriSyarikat");
				}
				List<Bandar> list = dataUtil.getListBandar(idNegeri);
				context.put("selectBandar", list);

				vm = "/maklumatSyarikat/selectBandar.vm";
				
			} else if ("doDaftarKutipanData".equals(command)) {
				mp.begin();
				boolean addIndividu = false;
				boolean addSyarikat = false;
				
				RkIndividu individu = (RkIndividu) mp.find(RkIndividu.class, getParam("noPengenalan").trim());
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
				
				RkSyarikat syarikat = (RkSyarikat) mp.find(RkSyarikat.class, getParam("noPendaftaran").trim());
				if (syarikat == null) {
					syarikat = new RkSyarikat();
					syarikat.setId(getParam("noPendaftaran"));
					addSyarikat = true;
				}
				syarikat.setNama(getParam("namaSyarikat"));
				syarikat.setIdJenisPemilikan(getParam("idJenisPemilikan"));
				syarikat.setTarafBumiputera(getParam("idTarafBumiputera"));
				syarikat.setTarikhPenubuhan(getDate("tarikhPenubuhan"));
				syarikat.setBidangSyarikat(getParam("bidangSyarikat"));
				syarikat.setAlamat1(getParam("alamat1Syarikat"));
				syarikat.setAlamat2(getParam("alamat2Syarikat"));
				syarikat.setAlamat3(getParam("alamat3Syarikat"));
				syarikat.setPoskod(getParam("poskodSyarikat"));
				syarikat.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandarSyarikat")));
				if (addSyarikat) {
					mp.persist(syarikat);
				}
				
				RkPemohon pemohon = new RkPemohon();
				pemohon.setIndividu(individu);
				pemohon.setSyarikat(syarikat);
				mp.persist(pemohon);
				
				fail = new RkFail();
				fail.setNoFail(getParam("noFail"));
				fail.setRuang((RkRuangKomersil) mp.find(RkRuangKomersil.class, getParam("idRuang")));
				fail.setPemohon(pemohon);
				fail.setFlagAktifPerjanjian("Y");
				mp.persist(fail);
				
				perjanjian = new RkPerjanjian();
				perjanjian.setFail(fail);
				perjanjian.setFlagKutipanData("Y");
				perjanjian.setNoRujukanSST(getParam("noRujukanSST"));
				perjanjian.setTarikhMulaSST(getDate("tarikhMulaSST"));
				perjanjian.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
				perjanjian.setNoRujukan(getParam("noRujukan"));
				perjanjian.setTarikhMula(getDate("tarikhMula"));
				perjanjian.setTempoh(getParamAsInteger("tempoh"));
				perjanjian.setTarikhTamat(getDate("tarikhTamat"));
				perjanjian.setKadarSewa(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarSewa")))));
				perjanjian.setDeposit(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("deposit")))));
				perjanjian.setFlagCajIWK(getParam("flagCajIWK"));
				if (getParam("flagCajIWK").equals("Y")) {
					perjanjian.setKadarBayaranIWK(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarBayaranIWK")))));
				} else {
					perjanjian.setKadarBayaranIWK(0D);
				}				
				perjanjian.setIdJenisSewa(getParam("idJenisSewa"));
				perjanjian.setCatatan(getParam("catatan"));
				perjanjian.setIdJenisPerjanjian("1");
				perjanjian.setFlagPerjanjianSemasa("Y");
				perjanjian.setFlagAktif("Y");
				perjanjian.setDaftarOleh((Users) mp.find(Users.class, userId));
				mp.persist(perjanjian);
				List<RkPerjanjian> senaraiPerjanjian = new ArrayList<>();
				if (perjanjian != null)
					senaraiPerjanjian.add(perjanjian);
				fail.setListPerjanjian(senaraiPerjanjian);
				
				RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, getParam("idRuang"));
				if (ruang != null) {
					ruang.setFlagSewa("Y");
				}
				mp.commit();
				successDaftar = true;
				
				UtilRk.daftarUsersBagiPenyewaRK(fail, mp);
				ServletContext servletContext = getServletContext();
				UtilRk.kemaskiniRekodPerjanjianDanAkaun(fail, false, true, mp, servletContext);		
				
				vm = "/button.vm";
			} else {
				context.remove("fail");
				context.remove("ruang");
				context.remove("individu");
				context.remove("syarikat");
				context.remove("perjanjian");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}			
		
		context.put("command", command);
		context.put("path", path);
		context.put("util", util);
		context.put("idSeksyen", idSeksyen);
		context.put("successDaftar", successDaftar);
		return path + vm;
	}
	
	public Date getDate(String paramName) {
		Date dateTxt = null;
	    String dateParam = this.request.getParameter(paramName);
	    if ((dateParam != null) && (!("".equals(dateParam))))
	    try {
	    	dateTxt = new SimpleDateFormat("dd-MM-yyyy").parse(dateParam);
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }

		return dateTxt;
	}
}

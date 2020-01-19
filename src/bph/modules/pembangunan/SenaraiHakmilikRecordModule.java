package bph.modules.pembangunan;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kod.Agensi;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisHakmilik;
import bph.entities.kod.KategoriTanah;
import bph.entities.kod.Kementerian;
import bph.entities.kod.Lot;
import bph.entities.kod.Luas;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.entities.kod.SubKategoriTanah;
import bph.entities.kod.UrusanJKPTG;
import bph.entities.pembangunan.DevCadangan;
import bph.entities.pembangunan.DevDokumen;
import bph.entities.pembangunan.DevEOT;
import bph.entities.pembangunan.DevHakmilik;
import bph.entities.pembangunan.DevLogSemakan;
import bph.entities.pembangunan.DevPenguatkuasaan;
import bph.entities.pembangunan.DevRekodUrusan;
import bph.entities.pembangunan.DevSemakan;
import bph.entities.pembangunan.DevSkop;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiHakmilikRecordModule extends LebahRecordTemplateModule<DevHakmilik> {

	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("SenaraiHakmilikRecordModule");
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@Override
	public String getPath() {
		return "bph/modules/pembangunan/hakmilik";
	}

	@Override
	public Class<DevHakmilik> getPersistenceClass() {
		return DevHakmilik.class;
	}

	@Override
	public void begin() {

		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		context.put("userId", userId);
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);

		context.remove("selectJenisHakmilik");
		context.remove("selectLot");
		context.remove("selectJenisLuas");
		context.remove("selectKategoriTanah");
		context.remove("selectSubKategoriTanah");
		context.remove("selectZon");
		context.remove("selectNegeri");
		context.remove("selectDaerah");
		context.remove("selectMukim");
		context.remove("selectKementerian");
		context.remove("selectAgensi");
		context.remove("info");
		context.remove("statusInfo");
		context.put("allowUpdate", "Y");

		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectLot", dataUtil.getListLot());
		context.put("selectJenisLuas", dataUtil.getListLuas());
		context.put("selectKategoriTanah", dataUtil.getListKategoriTanah());
		context.put("selectZon", dataUtil.getListZon());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKementerian", dataUtil.getListKementerian());

		addfilter();
		doOverideFilterRecord();
		defaultButtonOption();
	}

	private void addfilter() {
		this.addFilter("flagAktif = 'Y'");
	}

	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}
	}

	// TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
	}

	@Override
	public boolean delete(DevHakmilik hakmilik) throws Exception {
		return false;
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void save(DevHakmilik hakmilik) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		hakmilik.setNoFail(getParam("noFail"));
		if (getParam("idKementerian") != "") {
			hakmilik.setKementerian(db.find(Kementerian.class, getParam("idKementerian")));
		} else {
			hakmilik.setKementerian(db.find(Kementerian.class, "12"));
		}
		if (getParam("idAgensi") != "") {
			hakmilik.setAgensi(db.find(Agensi.class, getParam("idAgensi")));
		} else {
			hakmilik.setAgensi(db.find(Agensi.class, "1235"));
		}
		if (getParam("statusDaftar") != "") {
			hakmilik.setStatusDaftar(getParam("statusDaftar"));
		} else {
			hakmilik.setStatusDaftar("D");
		}
		hakmilik.setNegeri(db.find(Negeri.class, getParam("idNegeri")));
		hakmilik.setDaerah(db.find(Daerah.class, getParam("idDaerah")));
		hakmilik.setMukim(db.find(Mukim.class, getParam("idMukim")));
		hakmilik.setLokasi(getParam("lokasi"));

		hakmilik.setJenisHakmilik(db.find(JenisHakmilik.class, getParam("idJenisHakmilik")));
		hakmilik.setNoHakmilik(getParam("noHakmilik"));
		hakmilik.setLot(db.find(Lot.class, getParam("idLot")));
		hakmilik.setNoLot(getParam("noLot"));
		hakmilik.setJenisLuas(db.find(Luas.class, getParam("idLuas")));
		hakmilik.setLuas(getParam("luas"));
		hakmilik.setNoWarta(getParam("noWarta"));
		hakmilik.setTarikhWarta(getDate("tarikhWarta"));

		hakmilik.setKategoriTanah(db.find(KategoriTanah.class, getParam("idKategoriTanah")));
		hakmilik.setSubKategoriTanah(db.find(SubKategoriTanah.class, getParam("idSubKategoriTanah")));
		hakmilik.setSyarat(getParam("syarat"));
		hakmilik.setSekatan(getParam("sekatan"));
		hakmilik.setKegunaanTanah(getParam("kegunaanTanah"));
		hakmilik.setCatatan(getParam("catatan"));
		hakmilik.setHakmilikAsal(getParam("hakmilikAsal"));
		hakmilik.setHakmilikBerikut(getParam("hakmilikBerikut"));

		hakmilik.setNoPelan(getParam("noPelan"));
		hakmilik.setNoSyit(getParam("noSyit"));
		hakmilik.setNoPu(getParam("noPu"));
		hakmilik.setTarikhDaftar(getDate("tarikhDaftar"));
		hakmilik.setTarafHakmilik(getParam("tarafHakmilik"));
		hakmilik.setTarikhLuput(getDate("tarikhLuput"));
		if (!"".equals(getParam("cukai")))
			hakmilik.setCukai(Double.parseDouble(Util.RemoveComma(getParam("cukai"))));
		if (!"".equals(getParam("cukaiTerkini")))
			hakmilik.setCukaiTerkini(Double.parseDouble(Util.RemoveComma(getParam("cukaiTerkini"))));
		hakmilik.setIdMasuk(db.find(Users.class, userId));
	}

	@Override
	public void afterSave(DevHakmilik hakmilik) {
		String idHakmilik = "";
		try {
			if (hakmilik.getMukim() != null && hakmilik.getJenisHakmilik() != null && hakmilik.getNoHakmilik() != null) {
				db.begin();
				idHakmilik = hakmilik.getMukim().getId() + hakmilik.getJenisHakmilik().getId().toUpperCase()
						+ new DecimalFormat("0000000").format(Integer.parseInt(hakmilik.getNoHakmilik()));
				hakmilik.setPeganganHakmilik(idHakmilik);
				db.commit();
			}

			if (hakmilik.getKementerian() != null) {
				context.put("selectAgensi", dataUtil.getListAgensi(hakmilik.getKementerian().getId()));
			}
			if (hakmilik.getNegeri() != null) {
				context.put("selectDaerah", dataUtil.getListDaerah(hakmilik.getNegeri().getId()));
			}
			if (hakmilik.getDaerah() != null) {
				context.put("selectMukim", dataUtil.getListMukim(hakmilik.getDaerah().getId()));
			}
			if (hakmilik.getKategoriTanah() != null) {
				context.put("selectSubKategoriTanah", dataUtil.getListSubkategori(hakmilik.getKategoriTanah().getId()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		context.put("selectedTab", "1");
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("peganganHakmilik", getParam("findPeganganHakmilik"));
		m.put("jenisHakmilik.id", new OperatorEqualTo(get("findJenisHakmilik")));
		m.put("noHakmilik", get("findNoHakmilik"));
		m.put("lot.id", new OperatorEqualTo(get("findJenisLot")));
		m.put("noLot", get("findNoLot"));

		m.put("mukim.daerah.negeri.zon.id", get("findZon"));
		m.put("mukim.daerah.negeri.id", new OperatorEqualTo(get("findNegeri")));
		m.put("mukim.daerah.id", new OperatorEqualTo(get("findDaerah")));
		m.put("mukim.id", new OperatorEqualTo(get("findMukim")));
		m.put("lokasi", get("findLokasi"));

		m.put("statusDaftar", new OperatorEqualTo(get("findStatus")));

		return m;
	}

	@Override
	public void getRelatedData(DevHakmilik hakmilik) {
		if (hakmilik.getKementerian() != null) {
			context.put("selectAgensi", dataUtil.getListAgensi(hakmilik.getKementerian().getId()));
		}
		if (hakmilik.getNegeri() != null) {
			context.put("selectDaerah", dataUtil.getListDaerah(hakmilik.getNegeri().getId()));
		}
		if (hakmilik.getDaerah() != null) {
			context.put("selectMukim", dataUtil.getListMukim(hakmilik.getDaerah().getId()));
		}
		if (hakmilik.getKategoriTanah() != null) {
			context.put("selectSubKategoriTanah", dataUtil.getListSubkategori(hakmilik.getKategoriTanah().getId()));
		}
		context.put("selectedTab", "1");
	}

	/******************************************* START SENARAI TAB *******************************************/
	@Command("getMaklumatHakmilik")
	public String getMaklumatHakmilik() {

		try {
			mp = new MyPersistence();

			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, get("idHakmilik"));
			context.put("r", hakmilik);

			if (hakmilik != null) {
				if (hakmilik.getKementerian() != null) {
					context.put("selectAgensi", dataUtil.getListAgensi(hakmilik.getKementerian().getId()));
				}
				if (hakmilik.getNegeri() != null) {
					context.put("selectDaerah", dataUtil.getListDaerah(hakmilik.getNegeri().getId()));
				}
				if (hakmilik.getDaerah() != null) {
					context.put("selectMukim", dataUtil.getListMukim(hakmilik.getDaerah().getId()));
				}
				if (hakmilik.getKategoriTanah() != null) {
					context.put("selectSubKategoriTanah", dataUtil.getListSubkategori(hakmilik.getKategoriTanah().getId()));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}

	@Command("getCadanganPembangunan")
	public String getCadanganPembangunan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
			mp = new MyPersistence();

			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);

			List<DevCadangan> listCadanganPembangunan = mp
					.list("SELECT x FROM DevCadangan x WHERE x.flagAktif = 'Y' and x.hakmilik.id= '"
							+ getParam("idHakmilik") + "' AND x.statusCadangan = '1'");
			context.put("listCadanganPembangunan", listCadanganPembangunan);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
	}

	@Command("getDalamPembangunan")
	public String getDalamPembangunan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
			mp = new MyPersistence();

			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);

			List<DevCadangan> listCadanganPembangunan = mp					
					.list("SELECT x FROM DevCadangan x WHERE x.flagAktif = 'Y' and x.hakmilik.id= '"
							+ getParam("idHakmilik") + "' AND x.statusCadangan = '2'");
			context.put("listCadanganPembangunan", listCadanganPembangunan);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
	}

	@Command("getSelesaiPembangunan")
	public String getSelesaiPembangunan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
			mp = new MyPersistence();

			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);

			List<DevCadangan> listCadanganPembangunan = mp.list("SELECT x FROM DevCadangan x WHERE x.flagAktif = 'Y' and x.hakmilik.id= '"
							+ getParam("idHakmilik") + "' AND x.statusCadangan = '3'");
			context.put("listCadanganPembangunan", listCadanganPembangunan);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		context.put("selectedTab", "2");
		context.put("selectedSubTab", "3");
		return getPath() + "/entry_page.vm";
	}	
	
	@Command("getRekodUrusan")
	public String getRekodUrusan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
		    mp = new MyPersistence();
		     
		    DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);
			
			List<DevRekodUrusan> listRekodUrusan = mp.list("SELECT x FROM DevRekodUrusan x WHERE x.hakmilik.id= '"
					+ getParam("idHakmilik") + "' AND x.flagUrusan = '1'");
			context.put("listRekodUrusan", listRekodUrusan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
	}

	@Command("getRekodUrusanJPPH")
	public String getRekodUrusanJPPH() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
		    mp = new MyPersistence();
		     
		    DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);
			
			List<DevRekodUrusan> listRekodUrusan = mp.list("SELECT x FROM DevRekodUrusan x WHERE x.hakmilik.id= '"
					+ getParam("idHakmilik") + "' AND x.flagUrusan = '2'");
			context.put("listRekodUrusan", listRekodUrusan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "3");
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getPenguatkuasaan")
	public String getPenguatkuasaan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
		    mp = new MyPersistence();
		     
		    DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);
			
			List<DevPenguatkuasaan> listPenguatkuasaan = mp.list("SELECT x FROM DevPenguatkuasaan x WHERE x.hakmilik.id= '"
					+ get("idHakmilik") + "'");
			context.put("listPenguatkuasaan", listPenguatkuasaan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
		    mp = new MyPersistence();
		     
		    DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);
			
			List<DevDokumen> listDokumen = mp.list("SELECT x FROM DevDokumen x WHERE x.hakmilik.id= '"
					+ get("idHakmilik") + "' AND x.penguatkuasaan is null");
			context.put("listDokumen", listDokumen);
			context.put("selectJenisDokumen", dataUtil.getListJenisDokumen());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getLogSemakan")
	public String getLogSemakan() {
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		try {
		    mp = new MyPersistence();
		     
		    DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			context.put("r", hakmilik);
			
			List<DevSemakan> listSemakan = mp.list("SELECT x FROM DevSemakan x WHERE x.hakmilik.id= '"
					+ get("idHakmilik") + "'");
			context.put("listSemakan", listSemakan);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "6");
		return getPath() + "/entry_page.vm";
	}
	/****************************************** END SENARAI TAB *******************************************/

	/****************************************** START HAKMILIK ********************************************/
	@Command("saveKemaskiniHakmilik")
	public String saveKemaskiniHakmilik() throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String statusInfo = "";
		String info = "";
		try {
			mp = new MyPersistence();
			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			if (hakmilik != null) {
				mp.begin();
				hakmilik.setNoFail(getParam("noFail"));
				hakmilik.setKementerian((Kementerian) mp.find(Kementerian.class, getParam("idKementerian")));
				hakmilik.setAgensi((Agensi) mp.find(Agensi.class, getParam("idAgensi")));
				if (getParam("statusDaftar") != "") {
					hakmilik.setStatusDaftar(getParam("statusDaftar"));
				} else {
					hakmilik.setStatusDaftar("D");
				}
				hakmilik.setNegeri((Negeri) mp.find(Negeri.class, getParam("idNegeri")));
				hakmilik.setDaerah((Daerah) mp.find(Daerah.class, getParam("idDaerah")));
				hakmilik.setMukim((Mukim) mp.find(Mukim.class, getParam("idMukim")));
				hakmilik.setLokasi(getParam("lokasi"));

				hakmilik.setPeganganHakmilik(getParam("peganganHakmilik"));
				hakmilik.setJenisHakmilik((JenisHakmilik) mp.find(JenisHakmilik.class, getParam("idJenisHakmilik")));
				//hakmilik.setNoHakmilik(getParam("noHakmilik"));
				hakmilik.setLot((Lot) mp.find(Lot.class, getParam("idLot")));
				hakmilik.setNoLot(getParam("noLot"));
				hakmilik.setJenisLuas((Luas) mp.find(Luas.class, getParam("idLuas")));
				hakmilik.setLuas(getParam("luas"));
				hakmilik.setNoWarta(getParam("noWarta"));
				hakmilik.setTarikhWarta(getDate("tarikhWarta"));

				hakmilik.setKategoriTanah((KategoriTanah) mp.find(KategoriTanah.class, getParam("idKategoriTanah")));
				hakmilik.setSubKategoriTanah((SubKategoriTanah) mp.find(SubKategoriTanah.class, getParam("idSubKategoriTanah")));
				hakmilik.setSyarat(getParam("syarat"));
				hakmilik.setSekatan(getParam("sekatan"));
				hakmilik.setKegunaanTanah(getParam("kegunaanTanah"));
				hakmilik.setCatatan(getParam("catatan"));
				hakmilik.setHakmilikAsal(getParam("hakmilikAsal"));
				hakmilik.setHakmilikBerikut(getParam("hakmilikBerikut"));

				hakmilik.setNoPelan(getParam("noPelan"));
				hakmilik.setNoSyit(getParam("noSyit"));
				hakmilik.setNoPu(getParam("noPu"));
				hakmilik.setTarikhDaftar(getDate("tarikhDaftar"));
				hakmilik.setTarafHakmilik(getParam("tarafHakmilik"));
				hakmilik.setTarikhLuput(getDate("tarikhLuput"));
				if (!"".equals(getParam("cukai")))
					hakmilik.setCukai(Double.parseDouble(Util.RemoveComma(getParam("cukai"))));
				if (!"".equals(getParam("cukaiTerkini")))
					hakmilik.setCukaiTerkini(Double.parseDouble(Util.RemoveComma(getParam("cukaiTerkini"))));

				hakmilik.setIdKemaskini((Users) mp.find(Users.class, userId));
				hakmilik.setTarikhKemaskini(new Date());
				mp.commit();
				info = "MAKLUMAT BERJAYA DIKEMASKINI";
				statusInfo = "Y";
			}
		} catch (Exception ex) {
			info = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
			statusInfo = "T";
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		context.put("info", info);
		context.put("statusInfo", statusInfo);
		return getMaklumatHakmilik();
	}

	@Command("deleteHakmilik")
	public String deleteHakmilik() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String statusInfo = "";
		try {
			mp = new MyPersistence();
			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			if (hakmilik != null) {
				mp.begin();
				hakmilik.setFlagAktif("T");
				hakmilik.setIdKemaskini((Users) mp.find(Users.class, userId));
				hakmilik.setTarikhKemaskini(new Date());
				mp.commit();
				statusInfo = "success";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			statusInfo = "error";
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		context.put("statusInfo", statusInfo);
		return getMaklumatHakmilik();
	}
	/******************************************** END HAKMILIK ********************************************/

	/******************************************* START CADANGAN PEMBANGUNAN *******************************************/
	@Command("addCadanganPembangunan")
	public String addCadanganPembangunan() {
		context.remove("cadanganPembangunan");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("doPaparCadanganPembangunan")
	public String doPaparCadanganPembangunan() {
		try {
			mp = new MyPersistence();	
			DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
			if (cadanganPembangunan != null) {
				context.put("cadanganPembangunan", cadanganPembangunan);
			} else {
				context.remove("cadanganPembangunan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "1");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("getSkopCadanganPembangunan")
	public String getSkopCadanganPembangunan() {
		try {
			mp = new MyPersistence();	
			DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
			if (cadanganPembangunan != null) {
				context.put("cadanganPembangunan", cadanganPembangunan);
				
				List<DevSkop> listSkopCadanganPembangunan = mp.list("select x from DevSkop x where x.cadangan.id = '" + cadanganPembangunan.getId() + "'");
				context.put("listSkopCadanganPembangunan", listSkopCadanganPembangunan);
			} else {
				context.remove("cadanganPembangunan");
				context.remove("listSkopCadanganPembangunan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "2");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("getEOT")
	public String getEOT() {
		try {
			mp = new MyPersistence();	
			DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
			if (cadanganPembangunan != null) {
				context.put("cadanganPembangunan", cadanganPembangunan);
				
				List<DevEOT> listEOTCadanganPembangunan = mp.list("select x from DevEOT x where x.cadangan.id = '" + cadanganPembangunan.getId() + "'");
				context.put("listEOTCadanganPembangunan", listEOTCadanganPembangunan);
			} else {
				context.remove("cadanganPembangunan");
				context.remove("listEOTCadanganPembangunan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "3");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("doSaveCadanganPembangunan")
	public String doSaveCadanganPembangunan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		String selectedSubTab = getParam("selectedSubTab");
		boolean addCadanganPembangunan = false;
		try {
			mp = new MyPersistence();
			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			if (hakmilik != null) {
				mp.begin();
				DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
				if (cadanganPembangunan == null) {
					cadanganPembangunan = new DevCadangan();
					cadanganPembangunan.setHakmilik(hakmilik);
					cadanganPembangunan.setStatusCadangan(selectedSubTab);
					cadanganPembangunan.setIdMasuk((Users) mp.find(Users.class, userId));
					addCadanganPembangunan = true;
				} else {
					cadanganPembangunan.setIdKemaskini((Users) mp.find(Users.class, userId));
					cadanganPembangunan.setTarikhKemaskini(new Date());
				}
				cadanganPembangunan.setNamaProjek(getParam("namaProjek"));
				cadanganPembangunan.setNoRujukan(getParam("noRujukan"));
				cadanganPembangunan.setKeterangan(getParam("keterangan"));
				cadanganPembangunan.setKontraktor(getParam("kontraktor"));
				if (getParam("hargaKontrak") != "") {
					cadanganPembangunan.setHargaKontrak(Double.valueOf(Util.RemoveComma(getParam("hargaKontrak"))));
				} else {
					cadanganPembangunan.setHargaKontrak(0D);
				}
				cadanganPembangunan.setTarikhMilikTapak(getDate("tarikhMilikTapak"));
				
				//MAKLUMAT PELAKSANAAN PROJEK
				cadanganPembangunan.setTempohPelaksanaan(getParam("tempohPelaksanaan"));
				cadanganPembangunan.setTarikhSiapAsal(getDate("tarikhSiapAsal"));	
				cadanganPembangunan.setTarikhSiapSemasa(getDate("tarikhSiapSemasa"));
				cadanganPembangunan.setTarikhSiapSebenar(getDate("tarikhSiapSebenar"));
				cadanganPembangunan.setTempohTanggungan(getParam("tempohTanggungan"));
				cadanganPembangunan.setCatatanPelaksanaan(getParam("catatanPelaksanaan"));
				
				if (addCadanganPembangunan)
					mp.persist(cadanganPembangunan);
				
				mp.commit();
				context.put("cadanganPembangunan", cadanganPembangunan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "1");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("doHapusCadanganPembangunan")
	public String doHapusCadanganPembangunan() {
		String selectedSubTab = getParam("selectedSubTab");
		try {
			mp = new MyPersistence();	
			DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
			if (cadanganPembangunan != null) {
				mp.begin();
				cadanganPembangunan.setFlagAktif("T");
				cadanganPembangunan.setIdKemaskini((Users) mp.find(Users.class, userId));
				cadanganPembangunan.setTarikhKemaskini(new Date());
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		if ("3".equals(selectedSubTab)) {
			return getSelesaiPembangunan();
		} else if ("2".equals(selectedSubTab)) {
			return getDalamPembangunan();
		} else {
			return getCadanganPembangunan();
		}
	}
	
	@Command("addSkop")
	public String addSkop() {
		context.remove("skop");
		return getPath() + "/cadanganPembangunan/popupMaklumatSkop.vm";
	}

	@Command("doPaparSkop")
	public String doPaparSkop() {

		try {
			mp = new MyPersistence();	
			DevSkop skop = (DevSkop) mp.find(DevSkop.class, getParam("idSkop"));
			if (skop != null) {
				context.put("skop", skop);
			} else {
				context.remove("skop");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/cadanganPembangunan/popupMaklumatSkop.vm";
	}
	
	@Command("doSaveSkop")
	public String doSaveSkop() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		boolean addSkop = false;
		try {
			mp = new MyPersistence();
			DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
			if (cadanganPembangunan != null) {
				mp.begin();
				DevSkop skop = (DevSkop) mp.find(DevSkop.class, getParam("idSkop"));
				if (skop == null) {
					skop = new DevSkop();
					skop.setCadangan(cadanganPembangunan);
					addSkop = true;
				}
				skop.setItem(getParam("item"));
				
				if (addSkop)
					mp.persist(skop);
				
				mp.commit();
				context.put("cadanganPembangunan", cadanganPembangunan);
				List<DevSkop> listSkopCadanganPembangunan = mp.list("select x from DevSkop x where x.cadangan.id = '" + cadanganPembangunan.getId() + "'");
				context.put("listSkopCadanganPembangunan", listSkopCadanganPembangunan);
			} else {
				context.remove("cadanganPembangunan");
				context.remove("listSkopCadanganPembangunan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "2");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("doHapusSkop")
	public String doHapusSkop() {
		try {
			mp = new MyPersistence();	
			DevSkop skop = (DevSkop) mp.find(DevSkop.class, getParam("idSkop"));
			if (skop != null) {
				DevCadangan cadanganPembangunan = skop.getCadangan();
				
				mp.begin();
				mp.remove(skop);
				mp.commit();
				
				context.put("cadanganPembangunan", cadanganPembangunan);
				List<DevSkop> listSkopCadanganPembangunan = mp.list("select x from DevSkop x where x.cadangan.id = '" + cadanganPembangunan.getId() + "'");
				context.put("listSkopCadanganPembangunan", listSkopCadanganPembangunan);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "2");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	@Command("addEOT")
	public String addEOT() {
		context.remove("eot");
		return getPath() + "/cadanganPembangunan/popupMaklumatEOT.vm";
	}

	@Command("doPaparEOT")
	public String doPaparEOT() {

		try {
			mp = new MyPersistence();	
			DevEOT eot = (DevEOT) mp.find(DevEOT.class, getParam("idEOT"));
			if (eot != null) {
				context.put("eot", eot);
			} else {
				context.remove("eot");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPath() + "/cadanganPembangunan/popupMaklumatEOT.vm";
	}
	
	@Command("doSaveEOT")
	public String doSaveEOT() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		boolean addEOT = false;
		try {
			mp = new MyPersistence();
			DevCadangan cadanganPembangunan = (DevCadangan) mp.find(DevCadangan.class, getParam("idCadanganPembangunan"));
			if (cadanganPembangunan != null) {
				mp.begin();
				DevEOT eot = (DevEOT) mp.find(DevEOT.class, getParam("idEOT"));
				if (eot == null) {
					eot = new DevEOT();
					eot.setCadangan(cadanganPembangunan);
					addEOT = true;
				}
				eot.setTarikhMohon(getDate("tarikhMohon"));
				eot.setTarikhEOTMohon(getDate("tarikhEOTMohon"));
				eot.setTempohEOTMohon(getParam("tempohEOTMohon"));
				eot.setCatatanMohon(getParam("catatanMohon"));
				eot.setTarikhEOTLulus(getDate("tarikhEOTLulus"));
				eot.setTempohEOTLulus(getParam("tempohEOTLulus"));
				eot.setCatatanLulus(getParam("catatanLulus"));
				
				if (addEOT)
					mp.persist(eot);
				
				mp.commit();
				context.put("cadanganPembangunan", cadanganPembangunan);
				List<DevEOT> listEOTCadanganPembangunan = mp.list("select x from DevEOT x where x.cadangan.id = '" + cadanganPembangunan.getId() + "'");
				context.put("listEOTCadanganPembangunan", listEOTCadanganPembangunan);
				
				updateTarikhSiapCadanganPembangunan(cadanganPembangunan, mp);
			} else {
				context.remove("cadanganPembangunan");
				context.remove("listEOTCadanganPembangunan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "3");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	
	private void updateTarikhSiapCadanganPembangunan(DevCadangan cadanganPembangunan, MyPersistence mp) {
		try {
			if (cadanganPembangunan != null) {
				mp.begin();
				DevEOT eot = (DevEOT) mp.get("select x from DevEOT x where x.cadangan.id = '" + cadanganPembangunan.getId() + "' order by x.tarikhEOTLulus desc");
				if (eot != null) {
					cadanganPembangunan.setTarikhSiapSemasa(eot.getTarikhEOTLulus());
					cadanganPembangunan.setTarikhSiapSebenar(eot.getTarikhEOTLulus());
				} else {
					cadanganPembangunan.setTarikhSiapSemasa(cadanganPembangunan.getTarikhSiapAsal());
					cadanganPembangunan.setTarikhSiapSebenar(cadanganPembangunan.getTarikhSiapAsal());
				}
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	@Command("doHapusEOT")
	public String doHapusEOT() {
		try {
			mp = new MyPersistence();	
			DevEOT eot = (DevEOT) mp.find(DevEOT.class, getParam("idEOT"));
			if (eot != null) {
				DevCadangan cadanganPembangunan = eot.getCadangan();
				mp.begin();
				mp.remove(eot);
				mp.commit();
				
				context.put("cadanganPembangunan", cadanganPembangunan);
				List<DevEOT> listEOTCadanganPembangunan = mp.list("select x from DevEOT x where x.cadangan.id = '" + cadanganPembangunan.getId() + "'");
				context.put("listEOTCadanganPembangunan", listEOTCadanganPembangunan);
				
				updateTarikhSiapCadanganPembangunan(cadanganPembangunan, mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubSubTab", "3");
		return getPath() + "/cadanganPembangunan/start.vm";
	}
	/******************************************** END CADANGAN PEMBANGUNAN ********************************************/
	
	/******************************************* START REKOD URUSAN *******************************************/
	@Command("addUrusan")
	public String addUrusan() {
		context.put("selectUrusanJKPTG", dataUtil.getListUrusanJKPTG());
		context.remove("rekod");
		context.put("selectedSubTab", getParam("selectedSubTab"));
		return getPath() + "/rekodUrusan/popupMaklumatUrusan.vm";
	}
	
	@Command("doPaparUrusan")
	public String doPaparUrusan() {
		context.put("selectUrusanJKPTG", dataUtil.getListUrusanJKPTG());

		try {
			mp = new MyPersistence();	
			DevRekodUrusan urusan = (DevRekodUrusan) mp.find(DevRekodUrusan.class, getParam("idUrusan"));
			if (urusan != null) {
				context.put("rekod", urusan);
			} else {
				context.remove("rekod");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubTab", getParam("selectedSubTab"));
		return getPath() + "/rekodUrusan/popupMaklumatUrusan.vm";
	}
	
	@Command("doSaveRekodUrusan")
	public String doSaveRekodUrusan() {
		String selectedSubTab = getParam("selectedSubTab");
		boolean addRekodUrusan = false;
		try {
			mp = new MyPersistence();
			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			if (hakmilik != null) {
				mp.begin();
				DevRekodUrusan urusan = (DevRekodUrusan) mp.find(DevRekodUrusan.class, getParam("idUrusan"));
				if (urusan == null) {
					urusan = new DevRekodUrusan();
					urusan.setHakmilik(hakmilik);
					urusan.setFlagUrusan(selectedSubTab);
					addRekodUrusan = true;
				}
				urusan.setNoFail(getParam("noFail"));
				urusan.setNoRujukan(getParam("noRujukan"));
				urusan.setKeterangan(getParam("keterangan"));
				
				if (getParam("idUrusanJKPTG") != "")
					urusan.setUrusanJKPTG((UrusanJKPTG) mp.find(UrusanJKPTG.class, getParam("idUrusanJKPTG")));
				if (getParam("tarikhMohon") != "")
					urusan.setTarikhMohon(getDate("tarikhMohon"));
				if (getParam("tarikhMula") != "")
					urusan.setTarikhMula(getDate("tarikhMula"));
				if (getParam("tarikhAkhir") != "")
					urusan.setTarikhAkhir(getDate("tarikhAkhir"));
				
				if (getParam("tarikhTerimaMaklumbalas") != "")
					urusan.setTarikhTerimaMaklumbalas(getDate("tarikhTerimaMaklumbalas"));
				if (getParam("nilaian") != "") {
					urusan.setNilaian(Double.valueOf(Util.RemoveComma(getParam("nilaian"))));
				} else {
					urusan.setNilaian(0D);
				}					
				if (getParam("catatanMaklumbalas") != "")
					urusan.setCatatanMaklumbalas(getParam("catatanMaklumbalas"));
				
				if (addRekodUrusan)
					mp.persist(urusan);
				
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		if ("2".equals(selectedSubTab)) {
			return getRekodUrusanJPPH();
		} else {
			return getRekodUrusan();
		}
	}
	
	@Command("doHapusUrusan")
	public String doHapusUrusan() {
		String selectedSubTab = getParam("selectedSubTab");
		try {
			mp = new MyPersistence();	
			DevRekodUrusan urusan = (DevRekodUrusan) mp.find(DevRekodUrusan.class, getParam("idUrusan"));
			if (urusan != null) {
				mp.begin();
				mp.remove(urusan);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		if ("2".equals(selectedSubTab)) {
			return getRekodUrusanJPPH();
		} else {
			return getRekodUrusan();
		}
	}
	/******************************************** END REKOD URUSAN ********************************************/
	
	/******************************************* START PENGUATKUASAAN *******************************************/
	@Command("addPenguatkuasaan")
	public String addPenguatkuasaan() {
		context.remove("penguatkuasaan");
		return getPath() + "/penguatkuasaan/start.vm";
	}
	
	@Command("doPaparMaklumatPenguatkuasaan")
	public String doPaparMaklumatPenguatkuasaan() {
		try {
			mp = new MyPersistence();	
			DevPenguatkuasaan penguatkuasaan = (DevPenguatkuasaan) mp.find(DevPenguatkuasaan.class, getParam("idMaklumatPenguatkuasaan"));
			if (penguatkuasaan != null) {
				context.put("penguatkuasaan", penguatkuasaan);
			} else {
				context.remove("penguatkuasaan");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubTab", "1");
		return getPath() + "/penguatkuasaan/start.vm";
	}
	
	@Command("doSaveMaklumatPenguatkuasaan")
	public String doSaveMaklumatPenguatkuasaan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		boolean addPenguatkuasaan = false;
		try {
			mp = new MyPersistence();
			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			if (hakmilik != null) {
				mp.begin();
				DevPenguatkuasaan penguatkuasaan = (DevPenguatkuasaan) mp.find(DevPenguatkuasaan.class, getParam("idMaklumatPenguatkuasaan"));
				if (penguatkuasaan == null) {
					penguatkuasaan = new DevPenguatkuasaan();
					penguatkuasaan.setHakmilik(hakmilik);
					penguatkuasaan.setTarikhMasuk(new Date());
					penguatkuasaan.setLaporanOleh((Users) mp.find(Users.class, userId));
					addPenguatkuasaan = true;
				} else {
					penguatkuasaan.setTarikhKemaskini(new Date());
					penguatkuasaan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				}
				penguatkuasaan.setTajuk(getParam("tajuk"));
				penguatkuasaan.setNoRujukan(getParam("noRujukan"));
				penguatkuasaan.setKeterangan(getParam("keterangan"));
				penguatkuasaan.setTarikhAduan(getDate("tarikhAduan"));
				penguatkuasaan.setTarikhSiasatan(getDate("tarikhSiasatan"));
				penguatkuasaan.setTarikhPenguatkuasaan(getDate("tarikhPenguatkuasaan"));
				penguatkuasaan.setTarikhLaporan(getDate("tarikhLaporan"));
				penguatkuasaan.setTujuanLaporan(getParam("tujuanLaporan"));
				penguatkuasaan.setLokasiTanah(getParam("lokasiTanah"));
				penguatkuasaan.setJalanHubungan(getParam("jalanHubungan"));
				penguatkuasaan.setKawasanBerhampiran(getParam("kawasanBerhampiran"));
				penguatkuasaan.setJarakDariBandar(getParam("jarakDariBandar"));
				penguatkuasaan.setKeadaanMukaBumi(getParam("keadaanMukaBumi"));
				penguatkuasaan.setButiranAtasTanah(getParam("butiranAtasTanah"));
				penguatkuasaan.setKemudahanAsas(getParam("kemudahanAsas"));
				penguatkuasaan.setUtara(getParam("utara"));
				penguatkuasaan.setTimur(getParam("timur"));
				penguatkuasaan.setSelatan(getParam("selatan"));
				penguatkuasaan.setBarat(getParam("barat"));
				penguatkuasaan.setLaporanTerkiniAtasTanah(getParam("laporanTerkiniAtasTanah"));
				penguatkuasaan.setPengambilanTanah(getParam("pengambilanTanah"));
				penguatkuasaan.setPelanGambar(getParam("pelanGambar"));
				penguatkuasaan.setUlasan(getParam("ulasan"));
				penguatkuasaan.setSyor(getParam("syor"));
				
				if (addPenguatkuasaan)
					mp.persist(penguatkuasaan);
				
				mp.commit();
				context.put("penguatkuasaan", penguatkuasaan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubTab", "1");
		return getPath() + "/penguatkuasaan/start.vm";
	}
	
	@Command("getDokumenSokonganPenguatkuasaan")
	public String getDokumenSokonganPenguatkuasaan() {
		try {
			mp = new MyPersistence();	
			DevPenguatkuasaan penguatkuasaan = (DevPenguatkuasaan) mp.find(DevPenguatkuasaan.class, getParam("idMaklumatPenguatkuasaan"));
			if (penguatkuasaan != null) {
				context.put("penguatkuasaan", penguatkuasaan);
				
				List<DevDokumen> listDokumen = mp.list("SELECT x FROM DevDokumen x WHERE x.penguatkuasaan.id= '"
						+ penguatkuasaan.getId() + "' AND x.hakmilik is null");
				context.put("listDokumen", listDokumen);
				context.put("selectJenisDokumen", dataUtil.getListJenisDokumen());
			} else {
				context.remove("penguatkuasaan");
				context.remove("listDokumen");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectedSubTab", "2");
		return getPath() + "/penguatkuasaan/start.vm";
	}
	
	@Command("doHapusMaklumatPenguatkuasaan")
	public String doHapusMaklumatPenguatkuasaan() {
		try {
			mp = new MyPersistence();	
			DevPenguatkuasaan penguatkuasaan = (DevPenguatkuasaan) mp.find(DevPenguatkuasaan.class, getParam("idMaklumatPenguatkuasaan"));
			if (penguatkuasaan != null) {
				mp.begin();
				List<DevDokumen> listDokumenPenguatkuasaan = mp.list("select x from DevDokumen x where x.penguatkuasaan.id = '" + penguatkuasaan.getId() + "'");
				
				if (listDokumenPenguatkuasaan.size() > 0) {
					for (DevDokumen dokumen : listDokumenPenguatkuasaan) {
						Util.deleteFile(dokumen.getPhotofilename());
						Util.deleteFile(dokumen.getThumbfilename());
						mp.remove(dokumen);
					}
				}				
				mp.remove(penguatkuasaan);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getPenguatkuasaan();
	}
	
	@Command("uploadDocPenguatkuasaan")
	public String uploadDocPenguatkuasaan() throws Exception {
		String idMaklumatPenguatkuasaan = get("idMaklumatPenguatkuasaan");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		DevDokumen dokumen = new DevDokumen();
		String uploadDir = "pembangunan/hakmilik/penguatkuasaan/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			String imgName = uploadDir + idMaklumatPenguatkuasaan + "_" + dokumen.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar"
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
				simpanDokumenPenguatkuasaan(idMaklumatPenguatkuasaan, imgName, avatarName, tajukDokumen,
						idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/penguatkuasaan/uploadDoc.vm";
	}

	public void simpanDokumenPenguatkuasaan(String idMaklumatPenguatkuasaan, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen,
			String keteranganDokumen, DevDokumen dokumen) throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			dokumen.setPenguatkuasaan((DevPenguatkuasaan) mp.find(DevPenguatkuasaan.class, idMaklumatPenguatkuasaan));
			dokumen.setPhotofilename(imgName);
			dokumen.setThumbfilename(avatarName);
			dokumen.setTajuk(tajukDokumen);
			dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
			dokumen.setKeterangan(keteranganDokumen);
			mp.persist(dokumen);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}
	
	@Command("deleteDokumenPenguatkuasaan")
	public String deleteDokumenPenguatkuasaan() throws Exception {
		String idDokumen = get("idDokumen");
		try {
			mp = new MyPersistence();
			DevDokumen dokumen = (DevDokumen) mp.find(DevDokumen.class, idDokumen);

			if (dokumen != null) {
				mp.begin();
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.remove(dokumen);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getDokumenSokonganPenguatkuasaan();
	}
	/******************************************** END PENGUATKUASAAN ********************************************/
	
	/******************************************* START DOKUMEN SOKONGAN *******************************************/
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idHakmilik = get("idHakmilik");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		DevDokumen dokumen = new DevDokumen();
		String uploadDir = "pembangunan/hakmilik/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";
			String fileName = item.getName();
			String imgName = uploadDir + idHakmilik + "_" + dokumen.getId()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_avatar"
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
				simpanDokumen(idHakmilik, imgName, avatarName, tajukDokumen,
						idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumen(String idHakmilik, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen,
			String keteranganDokumen, DevDokumen dokumen) throws Exception {
		try {
			mp = new MyPersistence();
			mp.begin();
			dokumen.setHakmilik((DevHakmilik) mp.find(DevHakmilik.class, idHakmilik));
			dokumen.setPhotofilename(imgName);
			dokumen.setThumbfilename(avatarName);
			dokumen.setTajuk(tajukDokumen);
			dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
			dokumen.setKeterangan(keteranganDokumen);
			mp.persist(dokumen);
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
	}

	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String idDokumen = get("idDokumen");
		try {
			mp = new MyPersistence();
			DevDokumen dokumen = (DevDokumen) mp.find(DevDokumen.class, idDokumen);

			if (dokumen != null) {
				mp.begin();
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.remove(dokumen);
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		return getDokumenSokongan();
	}

	@Command("refreshListDokumen")
	public String refreshListDokumen() throws Exception {
		return getDokumenSokongan();
	}
	/******************************************* END DOKUMEN SOKONGAN *******************************************/
	
	/******************************************* START LOG SEMAKAN *******************************************/
	@Command("addSemakan")
	public String addSemakan() {
		context.remove("semakan");
		context.put("selectPenyemak", dataUtil.getListPenyemakPembangunan());
		return getPath() + "/logSemakan/start.vm";
	}
	
	@Command("doPaparMaklumatSemakan")
	public String doPaparMaklumatSemakan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		DevSemakan semakan = null;
		List<DevLogSemakan> listLogSemakan = null;
		try {
			mp = new MyPersistence();
			semakan = (DevSemakan) mp.find(DevSemakan.class, getParam("idMaklumatSemakan"));
			if (semakan != null) {
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' order by x.tarikh asc");
			}		
			context.put("selectPenyemak", dataUtil.getListPenyemakPembangunan());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("semakan", semakan);
		context.put("listLogSemakan", listLogSemakan);
		return getPath() + "/logSemakan/start.vm";
	}
	
	@Command("doHantarMaklumatSemakan")
	public String doHantarMaklumatSemakan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		DevSemakan semakan = null;
		List<DevLogSemakan> listLogSemakan = null;
		try {
			mp = new MyPersistence();
			DevHakmilik hakmilik = (DevHakmilik) mp.find(DevHakmilik.class, getParam("idHakmilik"));
			if (hakmilik != null) {
				mp.begin();
				semakan = new DevSemakan();
				semakan.setHakmilik(hakmilik);
				semakan.setPerkara(getParam("perkara"));
				semakan.setKeterangan(getParam("keterangan"));
				semakan.setPenyedia((Users) mp.find(Users.class, userId));
				semakan.setTarikhPenyediaan(new Date());
				semakan.setStatus("B");
				semakan.setDaftarOleh((Users) mp.find(Users.class, userId));
				semakan.setTarikhMasuk(new Date());
				mp.persist(semakan);
				
				DevLogSemakan logSemakan = new DevLogSemakan();
				logSemakan.setSemakan(semakan);
				logSemakan.setPetugas((Users) mp.find(Users.class, userId));
				logSemakan.setPegawai((Users) mp.find(Users.class, getParam("idPenyemak")));
				logSemakan.setCatatan(semakan.getKeterangan());
				logSemakan.setTarikh(new Date());
				logSemakan.setFlagAktif("Y");
				mp.persist(logSemakan);
				
				mp.commit();
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' order by x.tarikh asc");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("semakan", semakan);
		context.put("listLogSemakan", listLogSemakan);
		return getPath() + "/logSemakan/start.vm";
	}
	
	@Command("doHantarPindaanSemakan")
	public String doHantarPindaanSemakan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		DevSemakan semakan = null;
		List<DevLogSemakan> listLogSemakan = null;
		try {
			mp = new MyPersistence();
			semakan = (DevSemakan) mp.find(DevSemakan.class, getParam("idSemakan"));
			if (semakan != null) {
				mp.begin();				
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' and x.flagAktif = 'Y' order by x.tarikh asc");
				for (DevLogSemakan logSemakan : listLogSemakan) {
					logSemakan.setFlagAktif("T");
				}
				
				DevLogSemakan newLogSemakan = new DevLogSemakan();
				newLogSemakan.setSemakan(semakan);
				newLogSemakan.setPetugas((Users) mp.find(Users.class, userId));
				newLogSemakan.setPegawai(semakan.getPenyedia());
				newLogSemakan.setCatatan(getParam("catatan"));
				newLogSemakan.setTarikh(new Date());
				newLogSemakan.setFlagAktif("Y");
				mp.persist(newLogSemakan);
				
				semakan.setStatus("P");
				semakan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				semakan.setTarikhKemaskini(new Date());
				
				mp.commit();
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' order by x.tarikh asc");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("semakan", semakan);
		context.put("listLogSemakan", listLogSemakan);
		return getPath() + "/logSemakan/start.vm";
	}
	
	@Command("doSelesaiSemakan")
	public String doSelesaiSemakan() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		DevSemakan semakan = null;
		List<DevLogSemakan> listLogSemakan = null;
		try {
			mp = new MyPersistence();
			semakan = (DevSemakan) mp.find(DevSemakan.class, getParam("idSemakan"));
			if (semakan != null) {
				mp.begin();				
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' and x.flagAktif = 'Y' order by x.tarikh asc");
				for (DevLogSemakan logSemakan : listLogSemakan) {
					logSemakan.setFlagAktif("T");
				}
				
				DevLogSemakan newLogSemakan = new DevLogSemakan();
				newLogSemakan.setSemakan(semakan);
				newLogSemakan.setPetugas((Users) mp.find(Users.class, userId));
				newLogSemakan.setCatatan(getParam("catatan"));
				newLogSemakan.setTarikh(new Date());
				newLogSemakan.setFlagAktif("Y");
				mp.persist(newLogSemakan);
				
				semakan.setStatus("S");
				semakan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				semakan.setTarikhKemaskini(new Date());
				
				mp.commit();
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' order by x.tarikh asc");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("semakan", semakan);
		context.put("listLogSemakan", listLogSemakan);
		return getPath() + "/logSemakan/start.vm";
	}
	
	@Command("doHantarSemakanSemula")
	public String doHantarSemakanSemula() {
		userId = (String) request.getSession().getAttribute("_portal_login");
		DevSemakan semakan = null;
		List<DevLogSemakan> listLogSemakan = null;
		try {
			mp = new MyPersistence();
			semakan = (DevSemakan) mp.find(DevSemakan.class, getParam("idSemakan"));
			if (semakan != null) {
				mp.begin();				
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' and x.flagAktif = 'Y' order by x.tarikh asc");
				for (DevLogSemakan logSemakan : listLogSemakan) {
					logSemakan.setFlagAktif("T");
				}
				
				DevLogSemakan newLogSemakan = new DevLogSemakan();
				newLogSemakan.setSemakan(semakan);
				newLogSemakan.setPetugas((Users) mp.find(Users.class, userId));
				newLogSemakan.setPegawai((Users) mp.find(Users.class, getParam("idPenyemak")));
				newLogSemakan.setCatatan(getParam("catatan"));
				newLogSemakan.setTarikh(new Date());
				newLogSemakan.setFlagAktif("Y");
				mp.persist(newLogSemakan);
				
				semakan.setStatus("B");
				semakan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				semakan.setTarikhKemaskini(new Date());
				
				mp.commit();
				
				listLogSemakan = mp.list("select x from DevLogSemakan x where x.semakan.id = '" + semakan.getId() + "' order by x.tarikh asc");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("semakan", semakan);
		context.put("listLogSemakan", listLogSemakan);
		return getPath() + "/logSemakan/start.vm";
	}
	/******************************************* END LOG SEMAKAN *******************************************/

	/******************************************* START DROPDOWN LIST *******************************************/
	@Command("findNegeri")
	public String findNegeri() throws Exception {

		String idZon = "0";
		if (get("findZon").trim().length() > 0)
			idZon = get("findZon");
		List<Negeri> list = dataUtil.getListNegeri(idZon);
		context.put("selectNegeri", list);

		return getPath() + "/findNegeri.vm";
	}

	@Command("findDaerah")
	public String findDaerah() throws Exception {

		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);

		return getPath() + "/findDaerah.vm";
	}

	@Command("findMukim")
	public String findMukim() throws Exception {

		String idDaerah = "0";
		if (get("findDaerah").trim().length() > 0)
			idDaerah = get("findDaerah");

		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/findMukim.vm";
	}

	@Command("findAgensi")
	public String findAgensi() throws Exception {

		String idKementerian = "0";
		if (get("findKementerian").trim().length() > 0)
			idKementerian = get("findKementerian");

		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/findAgensi.vm";
	}

	/** SELECT DAERAH **/
	@Command("selectDaerah")
	public String selectDaerah() throws Exception {

		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0) {
			idNegeri = get("idNegeri");
		}
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);

		return getPath() + "/selectDaerah.vm";
	}

	/** SELECT MUKIM **/
	@Command("selectMukim")
	public String selectMukim() throws Exception {

		String idDaerah = "0";
		if (get("idDaerah").trim().length() > 0) {
			idDaerah = get("idDaerah");
		}
		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/selectMukim.vm";
	}

	/** SELECT AGENSI **/
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {

		String idKementerian = "0";

		if (get("idKementerian").trim().length() > 0) {
			idKementerian = get("idKementerian");
		}
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/selectAgensi.vm";
	}

	/** SELECT SUBKATEGORITANAH **/
	@Command("selectSubKategoriTanah")
	public String selectSubKategoriTanah() throws Exception {

		String idKategoriTanah = "0";

		if (get("idKategoriTanah").trim().length() > 0) {
			idKategoriTanah = get("idKategoriTanah");
		}
		List<SubKategoriTanah> list = dataUtil.getListSubkategori(idKategoriTanah);
		context.put("selectSubKategoriTanah", list);

		return getPath() + "/selectSubKategoriTanah.vm";
	}
	/******************************************* END DROPDOWN LIST *******************************************/
}
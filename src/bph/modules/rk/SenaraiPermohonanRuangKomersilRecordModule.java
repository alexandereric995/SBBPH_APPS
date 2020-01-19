package bph.modules.rk;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kod.Bandar;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Seksyen;
import bph.entities.kod.Status;
import bph.entities.kod.Warganegara;
import bph.entities.rk.RkAkaun;
import bph.entities.rk.RkDokumen;
import bph.entities.rk.RkFail;
import bph.entities.rk.RkIndividu;
import bph.entities.rk.RkMaklumatLain;
import bph.entities.rk.RkMesyuaratPermohonan;
import bph.entities.rk.RkPemohon;
import bph.entities.rk.RkPerjanjian;
import bph.entities.rk.RkPermohonan;
import bph.entities.rk.RkRuangKomersil;
import bph.entities.rk.RkSST;
import bph.entities.rk.RkSeqPermohonan;
import bph.entities.rk.RkSyarikat;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanRuangKomersilRecordModule extends LebahRecordTemplateModule<RkPermohonan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;	
	
	private static String kodHasilDeposit = "79503"; // DEPOSIT PELBAGAI

	@Override
	public Class getIdType() {
		return String.class;
	}	
	
	@Override
	public Class<RkPermohonan> getPersistenceClass() {
		return RkPermohonan.class;
	}
	
	@Override
	public String getPath() {
		return "bph/modules/rk/permohonanRuangKomersil";
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
		context.put("selectJenisKegunaanRuang", dataUtil.getListJenisKegunaanRuang());
		context.put("selectWargaNegara", dataUtil.getListWarganegara());
		context.put("selectStatusPermohonan", dataUtil.getListStatus("12"));
		
		context.put("util", util);
		
		boolean showSeksyen = true;
		context.put("showSeksyen", showSeksyen);	
		context.remove("idSeksyen");

		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
			
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}

	private void addfilter() {
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");		
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void save(RkPermohonan r) throws Exception {
		userId = (String) request.getSession().getAttribute("_portal_login");
		
		r.setIdJenisPermohonan("1"); //BAHARU
		r.setTarikhPermohonan(getDate("tarikhPermohonan"));
		r.setNoRujukanPermohonan(getParam("noRujukanPermohonan"));
		r.setTarikhTerimaPermohonan(getDate("tarikhTerimaPermohonan"));
		r.setCatatan(getParam("catatan"));
		
		r.setIdJenisSewa(getParam("idJenisSewa"));
		r.setKadarSewaJPPH(Double.valueOf(Util.RemoveComma(getParam("kadarSewaJPPH"))));
		r.setHargaTawaranSewa(Double.valueOf(Util.RemoveComma(getParam("hargaTawaranSewa"))));
		r.setTarikhMulaOperasi(getDate("tarikhMulaOperasi"));
		r.setTempoh(getParamAsInteger("tempoh"));
		r.setTarikhTamatOperasi(getDate("tarikhTamatOperasi"));
		r.setStatus(db.find(Status.class, "1433140179898"));
		r.setDaftarOleh(db.find(Users.class, userId));
	}

	@Override
	public void afterSave(RkPermohonan r) {
		boolean addIndividu = false;
		boolean addSyarikat = false;
		try {
			db = new DbPersistence();
			db.begin();
			
			RkIndividu individu = (RkIndividu) db.find(RkIndividu.class, getParam("noPengenalan"));
			if (individu == null) {
				individu = new RkIndividu();
				individu.setId(getParam("noPengenalan"));
				addIndividu = true;
			}
			individu.setNama(getParam("nama"));
			individu.setJawatan(getParam("jawatan"));
			individu.setWarganegara((Warganegara) db.find(Warganegara.class, getParam("idWarganegara")));
			individu.setAlamat1(getParam("alamat1"));
			individu.setAlamat2(getParam("alamat2"));
			individu.setAlamat3(getParam("alamat3"));
			individu.setPoskod(getParam("poskod"));
			individu.setBandar((Bandar) db.find(Bandar.class, getParam("idBandar")));
			individu.setAlamat1Surat(getParam("alamat1Surat"));
			individu.setAlamat2Surat(getParam("alamat2Surat"));
			individu.setAlamat3Surat(getParam("alamat3Surat"));
			individu.setPoskodSurat(getParam("poskodSurat"));
			individu.setBandarSurat((Bandar) db.find(Bandar.class, getParam("idBandarSurat")));
			individu.setNoTelefon(getParam("noTelefon"));
			individu.setNoTelefonBimbit(getParam("noTelefonBimbit"));
			individu.setNoFaks(getParam("noFaks"));
			individu.setEmel(getParam("emel"));
			if (addIndividu) {
				db.persist(individu);
			}
			
			RkSyarikat syarikat = (RkSyarikat) db.find(RkSyarikat.class, getParam("noPendaftaran").trim());
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
			syarikat.setBandar((Bandar) db.find(Bandar.class, getParam("idBandarSyarikat")));
			if (addSyarikat) {
				db.persist(syarikat);
			}
			
			RkPemohon pemohon = new RkPemohon();
			pemohon.setIndividu(individu);
			pemohon.setSyarikat(syarikat);
			db.persist(pemohon);
			
			RkFail fail = new RkFail();
			RkRuangKomersil ruangKomersil = db.find(RkRuangKomersil.class, getParam("idRuang"));
			fail.setRuang(ruangKomersil);
			fail.setPemohon(pemohon);
			db.persist(fail);
			
			RkPermohonan permohonan = (RkPermohonan) db.find(RkPermohonan.class, r.getId());
			if (permohonan != null) {
				permohonan.setFail(fail);
				permohonan.setNoPermohonan(generateNoPermohonan(db, ruangKomersil));
			}			
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		context.put("allowUpdatePermohonan", "Y");
		context.put("r", r);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "1");
	}
	
	private String generateNoPermohonan(DbPersistence db, RkRuangKomersil ruangKomersil) {
		String noPermohonan = "";
		String kodSeksyen = "";
		int bil = 1;
		try {
			
			Calendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			String year = String.valueOf(cal.get(Calendar.YEAR));
			String idSeksyen = ruangKomersil.getSeksyen().getId();
			String id = year + idSeksyen;
			
			RkSeqPermohonan seq = (RkSeqPermohonan) db.find(RkSeqPermohonan.class, id);
			if(seq != null){
				db.pesismisticLock(seq);
				bil = seq.getBil() + 1;
				seq.setBil(bil);
				seq = (RkSeqPermohonan) db.merge(seq);
			} else {
				seq = new RkSeqPermohonan();
				seq.setId(id);
				seq.setSeksyen(db.find(Seksyen.class, idSeksyen));
				seq.setTahun(cal.get(Calendar.YEAR));
				seq.setBil(bil);
				db.persist(seq);
				db.flush();
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

	@Override
	public void getRelatedData(RkPermohonan r) {
		try {
			mp = new MyPersistence();			
			context.put("allowUpdatePermohonan", getFlagAllowUpdatePermohonan(r, mp));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", r);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "1");
	}

	private String getFlagAllowUpdatePermohonan(RkPermohonan permohonan, MyPersistence mp) {
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		String allowUpdate = "T";
		if (permohonan != null) {
			if (permohonan.getStatus() != null) {
				if ("1433140179898".equals(permohonan.getStatus().getId())) { //BARU
					allowUpdate = "Y";
				} else if ("22803475845603".equals(permohonan.getStatus().getId())) { //MESYUARAT
					if ("(RK) Urusetia".equals(userRole)) {
						allowUpdate = "Y";
					}					
				}
			}
		}		
		return allowUpdate;
	}
	
	private String getFlagAllowUpdatePerjanjian(RkPermohonan permohonan, MyPersistence mp) {
		String allowUpdate = "Y";
		try {
			
			RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.permohonan.id = '" + permohonan.getId() + "'");
			if (perjanjian != null) {
				if ("Y".equals(perjanjian.getFlagAktif())) {
					allowUpdate = "T";
				}
			}	
			
			// TOLAK || SELESAI || BATAL
			if ("1434079231886".equals(permohonan.getStatus().getId()) || "22803475845632".equals(permohonan.getStatus().getId()) 
					|| "22803475845641".equals(permohonan.getStatus().getId())) {
					allowUpdate = "T";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		return allowUpdate;
	}

	@Override
	public boolean delete(RkPermohonan r) throws Exception {
		boolean allowDelete = false;
		try {
			if ("1433140179898".equals(r.getStatus().getId())) {
				if ("1".equals(r.getIdJenisPermohonan())) {
					RkFail fail = r.getFail();
					if (fail != null) {
						db.begin();
						if (fail.getPemohon() != null)
							db.remove(fail.getPemohon());
						db.remove(fail);
						db.commit();						
					}
				}
				allowDelete = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allowDelete;
	}	

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		
		m.put("noPermohonan", get("findNoFail"));
		m.put("fail.pemohon.individu.id", get("findNoPengenalan"));
		m.put("fail.pemohon.syarikat.id", get("findNoPendaftaran"));
		m.put("fail.pemohon.individu.nama", get("findNamaPemohon"));
		m.put("fail.pemohon.syarikat.nama", get("findNamaSyarikat"));
		m.put("idJenisPermohonan", new OperatorEqualTo(get("findJenisPermohonan")));
		m.put("status.id", new OperatorEqualTo(get("findStatusPermohonan")));
		
		m.put("fail.ruang.idRuang", get("findIdRuang"));
		m.put("fail.ruang.namaRuang", get("findNamaRuang"));
		m.put("fail.ruang.jenisKegunaanRuang.id", new OperatorEqualTo(get("findJenisKegunaanRuang")));		
		m.put("fail.ruang.lokasi", get("findLokasi"));
		m.put("fail.ruang.bandar.negeri.id", new OperatorEqualTo(get("findNegeri")));
		m.put("fail.ruang.bandar.id", new OperatorEqualTo(get("findBandar")));
		
		return m;
	}
	
	/** START TAB **/
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);			
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatRuang")
	public String getMaklumatRuang(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);			
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatIndividu")
	public String getMaklumatIndividu(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		List<Bandar> listBandar = null;
		List<Bandar> listBandarSurat = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);			
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
				
				if (permohonan.getFail() != null) {
					if (permohonan.getFail().getPemohon() != null) {
						if (permohonan.getFail().getPemohon().getIndividu() != null) {
							if (permohonan.getFail().getPemohon().getIndividu().getBandar() != null) {
								if (permohonan.getFail().getPemohon().getIndividu().getBandar().getNegeri() != null) {
									listBandar = dataUtil.getListBandar(permohonan.getFail().getPemohon().getIndividu().getBandar().getNegeri().getId());
								}
							}
							if (permohonan.getFail().getPemohon().getIndividu().getBandarSurat() != null) {
								if (permohonan.getFail().getPemohon().getIndividu().getBandarSurat().getNegeri() != null) {
									listBandarSurat = dataUtil.getListBandar(permohonan.getFail().getPemohon().getIndividu().getBandarSurat().getNegeri().getId());
								}
							}
						}
					}
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectBandar", listBandar);
		context.put("selectBandarSurat", listBandarSurat);
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatSyarikat")
	public String getMaklumatSyarikat(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);						
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
				
				if (permohonan.getFail() != null) {
					if (permohonan.getFail().getPemohon() != null) {
						if (permohonan.getFail().getPemohon().getSyarikat() != null) {
							if (permohonan.getFail().getPemohon().getSyarikat().getBandar() != null) {
								if (permohonan.getFail().getPemohon().getSyarikat().getBandar().getNegeri() != null) {
									listBandar = dataUtil.getListBandar(permohonan.getFail().getPemohon().getSyarikat().getBandar().getNegeri().getId());
								}
							}							
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("selectBandar", listBandar);
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatLain")
	public String getMaklumatLain(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		RkMaklumatLain maklumatLain = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);						
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
				
				maklumatLain = (RkMaklumatLain) mp.get("select x from RkMaklumatLain x where x.permohonan.id = '" + idPermohonan + "'");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("maklumatLain", maklumatLain);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "5");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		List<RkDokumen> listDokumen = null;
		try {
			mp = new MyPersistence();			
			context.put("selectJenisDokumen", dataUtil.getListJenisDokumen());
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);						
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
				
				listDokumen = mp.list("select x from RkDokumen x where x.permohonan.id= '" + idPermohonan + "'");
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("listDokumen", listDokumen);
		context.put("r", permohonan);
		context.put("selectedTab", "1");	
		context.put("selectedSubTab", "6");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatKertasPertimbangan")
	public String getMaklumatKertasPertimbangan(){
		String idPermohonan = getParam("idPermohonan");
		String allowUpdatePermohonan = "T";
		RkPermohonan permohonan = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);						
			if (permohonan != null) {
				allowUpdatePermohonan = getFlagAllowUpdatePermohonan(permohonan, mp);
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePermohonan", allowUpdatePermohonan);
		context.put("r", permohonan);
		context.put("selectedTab", "2");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatMesyuarat")
	public String getMaklumatMesyuarat(){
		String idPermohonan = getParam("idPermohonan");
		RkPermohonan permohonan = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);						

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("r", permohonan);
		context.put("selectedTab", "3");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatSST")
	public String getMaklumatSST(){
		String idPermohonan = getParam("idPermohonan");
		RkPermohonan permohonan = null;
		RkSST sst = null;
		RkAkaun akaun = null;
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);	
			if (permohonan != null) {
				sst = (RkSST) mp.get("select x from RkSST x where x.permohonan.id = '" + idPermohonan + "'");
				
				akaun = (RkAkaun) mp.get("select x from RkAkaun x where x.kodHasil.id = '" + kodHasilDeposit + "' and x.flagAktif = 'Y' and x.permohonan.id = '" + permohonan.getId() + "'");
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("akaun", akaun);
		context.put("sst", sst);
		context.put("r", permohonan);
		context.put("selectedTab", "4");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPerjanjian")
	public String getMaklumatPerjanjian(){
		String idPermohonan = getParam("idPermohonan");
		RkPermohonan permohonan = null;
		RkSST sst = null;
		String allowUpdatePerjanjian = "T";
		try {
			mp = new MyPersistence();
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);	
			if (permohonan != null) {
				sst = (RkSST) mp.get("select x from RkSST x where x.permohonan.id = '" + idPermohonan + "'");
				allowUpdatePerjanjian = getFlagAllowUpdatePerjanjian(permohonan, mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.remove("existNoFail");
		context.put("allowUpdatePerjanjian", allowUpdatePerjanjian);
		context.put("sst", sst);
		context.put("r", permohonan);
		context.put("selectedTab", "5");	
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START KEMASKINI REKOD **/
	@Command("doKemaskiniMaklumatPermohonan")
	public String doKemaskiniMaklumatPermohonan(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				permohonan.setTarikhPermohonan(getDate("tarikhPermohonan"));
				permohonan.setNoRujukanPermohonan(getParam("noRujukanPermohonan"));
				permohonan.setTarikhTerimaPermohonan(getDate("tarikhTerimaPermohonan"));
				permohonan.setCatatan(getParam("catatan"));
				
				permohonan.setIdJenisSewa(getParam("idJenisSewa"));
				permohonan.setKadarSewaJPPH(Double.valueOf(Util.RemoveComma(getParam("kadarSewaJPPH"))));
				permohonan.setHargaTawaranSewa(Double.valueOf(Util.RemoveComma(getParam("hargaTawaranSewa"))));
				permohonan.setTarikhMulaOperasi(getDate("tarikhMulaOperasi"));
				permohonan.setTempoh(getParamAsInteger("tempoh"));
				permohonan.setTarikhTamatOperasi(getDate("tarikhTamatOperasi"));
				
				permohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				permohonan.setTarikhKemaskini(new Date());
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPermohonan();
	}
	
	@Command("doKemaskiniMaklumatRuang")
	public String doKemaskiniMaklumatRuang(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				if (permohonan.getFail() != null) {
					RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, getParam("idRuang"));
					if (ruang != null) {
						mp.begin();
						permohonan.getFail().setRuang(ruang);
						mp.commit();
					}					
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatRuang();
	}
	
	@Command("doKemaskiniMaklumatIndividu")
	public String doKemaskiniMaklumatIndividu(){
		boolean addIndividu = false;
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();	
			
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {	
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
				
				if (permohonan.getFail() != null) {
					if (permohonan.getFail().getPemohon() != null) {						
						RkIndividu currentIndividu = permohonan.getFail().getPemohon().getIndividu();
						if (currentIndividu != null) {
							if (!currentIndividu.getId().equals(individu.getId())) {
								RkPemohon checkPemohonLain = (RkPemohon) mp.get("select x from RkPemohon x where x.id != '" + permohonan.getFail().getPemohon().getId() + "' and x.individu.id = '" + currentIndividu.getId() + "'");
								if (checkPemohonLain == null) {
									mp.remove(currentIndividu);
								}
							}
						}						
						permohonan.getFail().getPemohon().setIndividu(individu);						
					}
				}
				mp.commit();
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
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
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
				
				if (permohonan.getFail() != null) {
					if (permohonan.getFail().getPemohon() != null) {
						RkSyarikat currentSyarikat = permohonan.getFail().getPemohon().getSyarikat();
						if (!currentSyarikat.getId().equals(syarikat.getId())) {
							RkPemohon checkPemohonLain = (RkPemohon) mp.get("select x from RkPemohon x where x.id != '" + permohonan.getFail().getPemohon().getId() + "' and x.syarikat.id = '" + currentSyarikat.getId() + "'");
							if (checkPemohonLain == null) {
								mp.remove(currentSyarikat);
							}
						}
						permohonan.getFail().getPemohon().setSyarikat(syarikat);
					}
				}
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatSyarikat();
	}
	
	@Command("doKemaskiniMaklumatLain")
	public String doKemaskiniMaklumatLain(){
		boolean addMaklumatLain = false;
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				RkMaklumatLain maklumatLain = (RkMaklumatLain) mp.get("select x from RkMaklumatLain x where x.permohonan.id = '" + permohonan.getId() + "'");
				if (maklumatLain == null) {
					maklumatLain = new RkMaklumatLain();
					maklumatLain.setPermohonan(permohonan);
					addMaklumatLain = true;
				}
				maklumatLain.setTempohPengalaman(getParam("tempohPengalaman"));
				maklumatLain.setBilPengurus(getParamAsInteger("bilPengurus"));
				maklumatLain.setBilPenyelia(getParamAsInteger("bilPenyelia"));
				maklumatLain.setBilPelayan(getParamAsInteger("bilPelayan"));
				maklumatLain.setBilPekerjaAm(getParamAsInteger("bilPekerjaAm"));
				maklumatLain.setFlagPekerjaAsing(getParam("flagPekerjaAsing"));
				maklumatLain.setFlagSewaPihakLain(getParam("flagSewaPihakLain"));
				maklumatLain.setSebabDisewakan(getParam("sebabDisewakan"));
				if (getParam("anggaranSewaan") != null && !"".equals(getParam("anggaranSewaan"))) {
					maklumatLain.setAnggaranSewaan(Double.valueOf(Util.RemoveComma(getParam("anggaranSewaan"))));
				} else {
					maklumatLain.setAnggaranSewaan(0D);
				}
					
				if (addMaklumatLain) {
					mp.persist(maklumatLain);
				}
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatLain();
	}
	
	@Command("uploadDoc")
	public String uploadPhoto() throws Exception {
		String idPermohonan = get("idPermohonan");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		RkDokumen dokumen = new RkDokumen();
		String uploadDir = "rk/permohonan/dokumenSokongan/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists()) {
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
			String imgName = uploadDir + idPermohonan + "_" + dokumen.getId() + fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf(".")) + "_avatar" + imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName,
						ResourceBundle.getBundle("dbconnection").getString("folder") + imgName, 600, 560, 100);
				lebah.repository.Thumbnail.create(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName,
						ResourceBundle.getBundle("dbconnection").getString("folder") + avatarName, 150, 90, 100);
			}

			if (!imgName.equals("")) {
				simpanDokumen(idPermohonan, imgName, avatarName, tajukDokumen, idJenisDokumen, keteranganDokumen, dokumen);
			}
		}
		context.put("idPermohonan", idPermohonan);
		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumen(String idPermohonan, String imgName, String avatarName, String tajukDokumen, String idJenisDokumen, String keteranganDokumen, RkDokumen dokumen) throws Exception {

		try {
			mp = new MyPersistence();
			mp.begin();
			dokumen.setPermohonan((RkPermohonan) mp.find(RkPermohonan.class, idPermohonan));
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
			RkDokumen dokumen = (RkDokumen) mp.find(RkDokumen.class, idDokumen);
			if (dokumen != null) {
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.begin();
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
	
	@Command("doKemaskiniMaklumatKertasPertimbangan")
	public String doKemaskiniMaklumatKertasPertimbangan(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				permohonan.setUlasanKertasPertimbangan(getParam("ulasanKertasPertimbangan"));
				permohonan.setSyorKertasPertimbangan(getParam("syorKertasPertimbangan"));
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatKertasPertimbangan();
	}
	
	@Command("doHantarKeMesyuarat")
	public String doHantarKeMesyuarat() throws Exception{	
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		RkPermohonan permohonan = null;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				permohonan.setUlasanKertasPertimbangan(getParam("ulasanKertasPertimbangan"));
				permohonan.setSyorKertasPertimbangan(getParam("syorKertasPertimbangan"));
				permohonan.setStatus((Status) mp.find(Status.class, "22803475845603")); //MESYUARAT
				permohonan.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				permohonan.setTarikhKemaskini(new Date());
				mp.commit();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getMaklumatKertasPertimbangan();
	}
	
	@Command("doChangeMaklumatSST")
	public String doChangeMaklumatSST() throws Exception{	
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			sst = (RkSST) mp.find(RkSST.class, idSST);
			if (sst == null) {
				sst = new RkSST();
			}
			sst.setTarikhMulaSST(getDate("tarikhMulaSST"));
			sst.setTarikhTamatSST(Util.getNextWorkingDate(getDate("tarikhMulaSST"), 14));
			sst.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
			sst.setNoRujukanSST(getParam("noRujukanSST"));
			sst.setTarikhMula(getDate("tarikhMula"));
			sst.setTempoh(getParamAsInteger("tempoh"));
			if (!"".equals(getParam("tarikhMula")) && !"".equals(getParam("tempoh")) && !"0".equals(getParam("tempoh")) && !"".equals(getParam("idJenisSewa"))){
				Calendar calTamat = new GregorianCalendar();
				calTamat.setTime(getDate("tarikhMula"));
				if ("H".equals(getParam("idJenisSewa"))) {
					calTamat.add(Calendar.DATE, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					sst.setTarikhTamat(calTamat.getTime());
				} else if ("B".equals(getParam("idJenisSewa"))) {
					calTamat.add(Calendar.MONTH, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					sst.setTarikhTamat(calTamat.getTime());
				} else if ("T".equals(getParam("idJenisSewa"))) {
					calTamat.add(Calendar.YEAR, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					sst.setTarikhTamat(calTamat.getTime());
				} else {
					sst.setTarikhTamat(getDate("tarikhTamat"));
				}
			} else {
				sst.setTarikhTamat(getDate("tarikhTamat"));
			}
			if (getParam("kadarSewa") != null && getParam("kadarSewa").length() > 0) 
				sst.setKadarSewa(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarSewa")))));
			if (getParam("deposit") != null && getParam("deposit").length() > 0) 
				sst.setDeposit(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("deposit")))));
			sst.setFlagCajIWK(getParam("flagCajIWK"));
			if (getParam("kadarBayaranIWK") != null && getParam("kadarBayaranIWK").length() > 0) 
				sst.setKadarBayaranIWK(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarBayaranIWK")))));
			sst.setIdJenisSewa(getParam("idJenisSewa"));
			sst.setCatatan(getParam("catatan"));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("sst", sst);
		context.put("r", permohonan);
		context.put("selectedTab", "4");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("doKemaskiniMaklumatSST")
	public String doKemaskiniMaklumatSST() throws Exception{	
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		boolean addSST = false;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst == null) {
					sst = new RkSST();
					sst.setFail(permohonan.getFail());
					sst.setPermohonan(permohonan);
					addSST = true;
				}
				sst.setNoRujukanSST(getParam("noRujukanSST"));
				sst.setTarikhMulaSST(getDate("tarikhMulaSST"));
				sst.setTarikhTamatSST(getDate("tarikhTamatSST"));
				sst.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));				
				sst.setIdJenisSewa(getParam("idJenisSewa"));
				sst.setTarikhMula(getDate("tarikhMula"));
				sst.setTempoh(getParamAsInteger("tempoh"));
				sst.setTarikhTamat(getDate("tarikhTamat"));
				sst.setKadarSewa(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarSewa")))));
				sst.setDeposit(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("deposit")))));
				sst.setFlagCajIWK(getParam("flagCajIWK"));
				if (getParam("kadarBayaranIWK") != null && getParam("kadarBayaranIWK").length() > 0) 
					sst.setKadarBayaranIWK(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarBayaranIWK")))));
				sst.setCatatan(getParam("catatan"));
				
				if (addSST) {
					sst.setDaftarOleh((Users) mp.find(Users.class, userId));
					mp.persist(sst);
				} else {
					sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					sst.setTarikhKemaskini(new Date());
				}
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getMaklumatSST();
	}
	
	@Command("doAktifMaklumatSST")
	public String doAktifMaklumatSST() throws Exception{	
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		boolean addSST = false;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst == null) {
					sst = new RkSST();
					sst.setFail(permohonan.getFail());
					sst.setPermohonan(permohonan);
					addSST = true;
				}
				sst.setNoRujukanSST(getParam("noRujukanSST"));
				sst.setTarikhMulaSST(getDate("tarikhMulaSST"));
				sst.setTarikhTamatSST(getDate("tarikhTamatSST"));
				sst.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));				
				sst.setIdJenisSewa(getParam("idJenisSewa"));
				sst.setTarikhMula(getDate("tarikhMula"));
				sst.setTempoh(getParamAsInteger("tempoh"));
				sst.setTarikhTamat(getDate("tarikhTamat"));
				sst.setKadarSewa(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarSewa")))));
				sst.setDeposit(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("deposit")))));
				sst.setFlagCajIWK(getParam("flagCajIWK"));
				if (getParam("kadarBayaranIWK") != null && getParam("kadarBayaranIWK").length() > 0) 
					sst.setKadarBayaranIWK(Double.valueOf(Util.RemoveComma(Util.removeNonNumeric(getParam("kadarBayaranIWK")))));
				sst.setCatatan(getParam("catatan"));
				
				if (addSST) {
					sst.setDaftarOleh((Users) mp.find(Users.class, userId));
					mp.persist(sst);
				} else {
					sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					sst.setTarikhKemaskini(new Date());
				}
				
				permohonan.setStatus((Status) mp.find(Status.class, "511108229258439")); //MAKLUMBALAS SST			
				mp.commit();
				
				//CREATE REKOD DEPOSIT
				if (sst.getDeposit() > 0D) {
					UtilRk.daftarUsersBagiPenyewaRK(permohonan.getFail(), mp);
					UtilRk.generateDepositPermohonan(sst, userId, mp);
				}	
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getMaklumatSST();
	}
	
	@Command("doKemaskiniAkuanTerimaSST")
	public String doKemaskiniAkuanTerimaSST() throws Exception{	
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {				
				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst != null) {
					mp.begin();
					sst.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
					sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					sst.setTarikhKemaskini(new Date());
					mp.commit();
				}
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getMaklumatSST();
	}
	
	@Command("doPenyediaanPerjanjian")
	public String doPenyediaanPerjanjian() throws Exception{	
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst != null) {					
					sst.setTarikhAkuanTerimaSST(getDate("tarikhAkuanTerimaSST"));
					sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					sst.setTarikhKemaskini(new Date());					
				}
				
				permohonan.setStatus((Status) mp.find(Status.class, "511108229258451")); //PENYEDIAAN PERJANJIAN
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getMaklumatPerjanjian();
	}
	
	@Command("doSemakNoFail")
	public String doSemakNoFail(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String noFail = getParam("noFail").trim().toUpperCase();
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		String allowUpdatePerjanjian = "T";
		try {
			mp = new MyPersistence();			
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				RkFail fail = (RkFail) mp.get("select x from RkFail x where x.flagAktifPerjanjian = 'Y' and x.id != '" + permohonan.getFail().getId() + "' and x.noFail = '" + noFail + "'");
				if (fail != null) {
					context.put("existNoFail", "Y");
				} else {
					context.remove("existNoFail");
					permohonan.getFail().setNoFail(noFail);				
				}

				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst != null) {
					sst.setNoRujukan(getParam("noRujukan"));			
				}
				allowUpdatePerjanjian = getFlagAllowUpdatePerjanjian(permohonan, mp);
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("allowUpdatePerjanjian", allowUpdatePerjanjian);
		context.put("sst", sst);
		context.put("r", permohonan);
		context.put("selectedTab", "5");	
		return getPath() + "/entry_page.vm";
	}
	
	@Command("doKemaskiniMaklumatPerjanjian")
	public String doKemaskiniMaklumatPerjanjian(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst != null) {
					sst.setNoRujukan(getParam("noRujukan"));
					sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					sst.setTarikhKemaskini(new Date());					
				}
				permohonan.getFail().setNoFail(getParam("noFail"));				
				mp.commit();
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getMaklumatPerjanjian();
	}
	
	@Command("doAktifPerjanjian")
	public String doAktifPerjanjian(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");	
		String idSST = getParam("idSST");	
		RkPermohonan permohonan = null;
		RkSST sst = null;
		try {
			mp = new MyPersistence();
			
			permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				sst = (RkSST) mp.find(RkSST.class, idSST);
				if (sst != null) {
					sst.setNoRujukan(getParam("noRujukan"));
					sst.setKemaskiniOleh((Users) mp.find(Users.class, userId));
					sst.setTarikhKemaskini(new Date());		
					
					//CREATE REKOD PERJANJIAN
					generateRekodPerjanjian(sst, mp);
					
				}
				permohonan.getFail().setNoFail(getParam("noFail"));
				permohonan.getFail().getRuang().setFlagSewa("Y");
				permohonan.getFail().setFlagAktifPerjanjian("Y");				
				permohonan.setStatus((Status) mp.find(Status.class, "22803475845632")); //SELESAI
				mp.commit();
				
				ServletContext servletContext = getServletContext();
				UtilRk.kemaskiniRekodPerjanjianDanAkaun(permohonan.getFail(), true, true, mp, servletContext);
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPerjanjian();
	}
	
	private void generateRekodPerjanjian(RkSST sst, MyPersistence mp) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		boolean addPerjanjian = false;
		try {
			RkPerjanjian perjanjian = (RkPerjanjian) mp.get("select x from RkPerjanjian x where x.permohonan.id = '" + sst.getPermohonan().getId() + "'");
			if (perjanjian == null) {
				perjanjian = new RkPerjanjian();
				addPerjanjian = true;
			}
			perjanjian.setFail(sst.getPermohonan().getFail());
			perjanjian.setPermohonan(sst.getPermohonan());
			perjanjian.setNoRujukanSST(sst.getNoRujukanSST());
			perjanjian.setTarikhMulaSST(sst.getTarikhMulaSST());
			perjanjian.setTarikhTamatSST(sst.getTarikhTamatSST());
			perjanjian.setTarikhAkuanTerimaSST(sst.getTarikhAkuanTerimaSST());
			perjanjian.setNoRujukan(sst.getNoRujukan());
			perjanjian.setTarikhMula(sst.getTarikhMula());
			perjanjian.setTempoh(sst.getTempoh());
			perjanjian.setTarikhTamat(sst.getTarikhTamat());
			perjanjian.setKadarSewa(sst.getKadarSewa());
			perjanjian.setDeposit(sst.getDeposit());
			perjanjian.setFlagCajIWK(sst.getFlagCajIWK());
			perjanjian.setKadarBayaranIWK(sst.getKadarBayaranIWK());
			perjanjian.setIdJenisSewa(sst.getIdJenisSewa());
			perjanjian.setIdJenisPerjanjian(sst.getIdJenisPerjanjian());
			perjanjian.setFlagAktif("Y");
			perjanjian.setFlagPerjanjianSemasa("T");
			perjanjian.setFlagKutipanData("T");
			perjanjian.setIdStatusPerjanjian("1");
			
			if (addPerjanjian) {
				perjanjian.setDaftarOleh((Users) mp.find(Users.class, userId));
				mp.persist(perjanjian);
			} else {
				perjanjian.setKemaskiniOleh((Users) mp.find(Users.class, userId));
				perjanjian.setTarikhKemaskini(new Date());
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	/** END KEMASKINI REKOD **/
	
	@Command("getSenaraiRuang")
	public String getSenaraiRuang(){
		String idSeksyen = getParam("idSeksyen");
		String sql = "";
		try {
			mp = new MyPersistence();
			sql = "select x from RkRuangKomersil x where x.flagSewa = 'T' and x.flagAktif = 'Y'";
			if (!"".equals(idSeksyen)) {
				sql = sql + " and x.seksyen.id = '" + idSeksyen + "'";
			}
			List<RkRuangKomersil> listRuang = mp.list(sql);
			context.put("listRuang", listRuang);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatRuang/popupSenaraiRuang.vm";
	}
	
	@Command("getPilihanRuang")
	public String getPilihanRuang() throws Exception{		
		String strRadioRuang = get("radioRuang");		
		try {
			mp = new MyPersistence();
			
			RkRuangKomersil ruang = (RkRuangKomersil) mp.find(RkRuangKomersil.class, strRadioRuang);
			context.put("ruang", ruang);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatRuang/maklumatRuang.vm";
	}
	
	@Command("doChangeJenisSewaan")
	public String doChangeJenisSewaan() throws Exception{		
		try {
			
			RkPermohonan permohonan = new RkPermohonan();
			permohonan.setTarikhPermohonan(getDate("tarikhPermohonan"));
			permohonan.setTarikhTerimaPermohonan(getDate("tarikhTerimaPermohonan"));
			permohonan.setIdJenisSewa(getParam("idJenisSewa"));
			if (!"".equals(getParam("kadarSewaJPPH"))) {
				permohonan.setKadarSewaJPPH(Double.valueOf(Util.RemoveComma(getParam("kadarSewaJPPH"))));
			}
			if (!"".equals(getParam("hargaTawaranSewa"))) {
				permohonan.setHargaTawaranSewa(Double.valueOf(Util.RemoveComma(getParam("hargaTawaranSewa"))));
			}			
			permohonan.setTarikhMulaOperasi(getDate("tarikhMulaOperasi"));
			permohonan.setTempoh(getParamAsInteger("tempoh"));
			if (!"".equals(getParam("tarikhMulaOperasi")) && !"".equals(getParam("tempoh")) && !"0".equals(getParam("tempoh")) && !"".equals(getParam("idJenisSewa"))){
				Calendar calTamat = new GregorianCalendar();
				calTamat.setTime(getDate("tarikhMulaOperasi"));
				if ("H".equals(getParam("idJenisSewa"))) {
					calTamat.add(Calendar.DATE, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					permohonan.setTarikhTamatOperasi(calTamat.getTime());
				} else if ("B".equals(getParam("idJenisSewa"))) {
					calTamat.add(Calendar.MONTH, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					permohonan.setTarikhTamatOperasi(calTamat.getTime());
				} else if ("T".equals(getParam("idJenisSewa"))) {
					calTamat.add(Calendar.YEAR, getParamAsInteger("tempoh"));
					calTamat.add(Calendar.DATE, -1);
					permohonan.setTarikhTamatOperasi(calTamat.getTime());
				} else {
					permohonan.setTarikhTamatOperasi(getDate("tarikhTamatOperasi"));
				}
			} else {
				permohonan.setTarikhTamatOperasi(getDate("tarikhTamatOperasi"));
			}

			context.put("r", permohonan);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatSewaan.vm";
	}
	
	@Command("getSenaraiIndividu")
	public String getSenaraiIndividu() throws Exception {
		try {
			mp = new MyPersistence();
			
			List<RkIndividu> listIndividu = mp.list("select x from RkIndividu x order by x.nama asc");
			context.put("listIndividu", listIndividu);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatIndividu/popupSenaraiIndividu.vm";
	}
	
	@Command("getPilihanIndividu")
	public String getPilihanIndividu() throws Exception {
		String strRadioIndividu = getParam("radioIndividu");
		
		List<Bandar> listBandar = null;
		List<Bandar> listBandarSurat = null;
		
		try {
			mp = new MyPersistence();
			
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
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		return getPath() + "/maklumatIndividu/maklumatIndividu.vm";
	}
	
	@Command("getMaklumatIndividuBerdaftar")
	public String getMaklumatIndividuBerdaftar() throws Exception{		
		String noPengenalan = get("noPengenalan").replace("-", "").trim();	
		List<Bandar> listBandar = null;
		List<Bandar> listBandarSurat = null;
		try {
			mp = new MyPersistence();
			
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
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatIndividu/maklumatIndividu.vm";
	}
	
	@Command("getSenaraiSyarikat")
	public String getSenaraiSyarikat() throws Exception {
		try {
			mp = new MyPersistence();
			
			List<RkSyarikat> listSyarikat = mp.list("select x from RkSyarikat x order by x.nama asc");
			context.put("listSyarikat", listSyarikat);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatSyarikat/popupSenaraiSyarikat.vm";
	}
	
	@Command("getPilihanSyarikat")
	public String getPilihanSyarikat() throws Exception {
		String strRadioSyarikat = getParam("radioSyarikat");
		
		List<Bandar> listBandar = null;
		
		try {
			mp = new MyPersistence();
			
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
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		return getPath() + "/maklumatSyarikat/maklumatSyarikat.vm";
	}
	
	@Command("doSimpanBatalPermohonan")
	public String doSimpanBatalPermohonan(){
		userId = (String) request.getSession().getAttribute("_portal_login");
		String idPermohonan = getParam("idPermohonan");
		try {
			mp = new MyPersistence();
			RkPermohonan permohonan = (RkPermohonan) mp.find(RkPermohonan.class, idPermohonan);
			if (permohonan != null) {
				mp.begin();
				
				//MESYUARAT
				if ("22803475845603".equals(permohonan.getStatus().getId())) {
					List<RkMesyuaratPermohonan> listMesyuaratPermohonan = mp.list("select x from RkMesyuaratPermohonan x where x.mesyuarat.status = 'B' and x.permohonan.id = '" + permohonan.getId() + "'");
					for (RkMesyuaratPermohonan mesyuaratPermohonan : listMesyuaratPermohonan) {
						mp.remove(mesyuaratPermohonan);
					}
				}
				//MAKLUMBALAS SST
				if ("511108229258439".equals(permohonan.getStatus().getId())) {
					KewDeposit deposit = (KewDeposit) mp.get("select x from KewDeposit x where x.flagBayar = 'T' and x.idLejar = '" + permohonan.getId() + "'");
					if (deposit != null) {
						deposit.setFlagBayar("BTL");
					}
				}
				permohonan.setCatatanBatal(getParam("catatanBatal"));
				permohonan.setStatus((Status) mp.find(Status.class, "22803475845641")); // BATAL
				permohonan.setBatalOleh((Users) mp.find(Users.class, userId));
				permohonan.setTarikhBatal(new Date());
				mp.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getMaklumatPermohonan();
	}
	
	@Command("popupBatalPermohonan")
	public String popupBatalPermohonan() throws Exception {

		return getPath() + "/maklumatBatalPermohonan/popupBatalPermohonan.vm";
	}
	
	@Command("getMaklumatSyarikatBerdaftar")
	public String getMaklumatSyarikatBerdaftar() throws Exception{		
		String noPendaftaran = getParam("noPendaftaran").trim().toUpperCase();;	
		List<Bandar> listBandar = null;
		try {
			mp = new MyPersistence();
			
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
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/maklumatSyarikat/maklumatSyarikat.vm";
	}
	
	/** START DROP DOWN LIST **/
	@Command("findBandar")
	public String findBandar() throws Exception {

		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0) {
			idNegeri = get("findNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/findBandar.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {

		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0) {
			idNegeri = get("idNegeri");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/maklumatIndividu/selectBandar.vm";
	}
	
	@Command("selectBandarSurat")
	public String selectBandarSurat() throws Exception {

		String idNegeri = "0";
		if (get("idNegeriSurat").trim().length() > 0) {
			idNegeri = get("idNegeriSurat");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandarSurat", list);

		return getPath() + "/maklumatIndividu/selectBandarSurat.vm";
	}
	
	@Command("selectBandarSyarikat")
	public String selectBandarSyarikat() throws Exception {

		String idNegeri = "0";
		if (get("idNegeriSyarikat").trim().length() > 0) {
			idNegeri = get("idNegeriSyarikat");
		}
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandarSyarikat", list);

		return getPath() + "/maklumatSyarikat/selectBandar.vm";
	}
	/** END DROP DOWN LIST **/
}

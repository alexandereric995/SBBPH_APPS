package bph.modules.bgs;

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
import lebah.template.OperatorEqualTo;
import lebah.template.UID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import portal.module.entity.Users;
import bph.entities.bgs.BgsDokumen;
import bph.entities.bgs.BgsKeperluanPejabat;
import bph.entities.bgs.BgsKertasPertimbangan;
import bph.entities.bgs.BgsKuiri;
import bph.entities.bgs.BgsMasalah;
import bph.entities.bgs.BgsPemohon;
import bph.entities.bgs.BgsPerjawatan;
import bph.entities.bgs.BgsPermohonan;
import bph.entities.bgs.BgsSeqPermohonan;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Daerah;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.Mukim;
import bph.entities.kod.Status;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevPremis;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPermohonanBGSRecordModule extends LebahRecordTemplateModule<BgsPermohonan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyPersistence mp;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<BgsPermohonan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return BgsPermohonan.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/bgs/permohonanBGS";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");		
		context.put("userRole", userRole);
		
		//LIST DROPDOWN
		context.put("selectKementerian", dataUtil.getListKementerian());
		context.put("selectNegeri", dataUtil.getListNegeri());		
				
		defaultButtonOption();
		addfilter(userId, userRole);
				
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
		context.remove("popupFlagStatusInfo");	
		context.remove("popupStatusInfo");	
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}
	
	private void addfilter(String userId, String userRole) {
		BgsPemohon pemohon = null;
		if ("(BGS) Pemohon".equals(userRole)) {
			try {
				mp = new MyPersistence();
				pemohon = (BgsPemohon) mp.find(BgsPemohon.class, userId);
				if (pemohon != null) {
					context.put("pemohon", pemohon);
					this.addFilter("pemohon.id = '" + pemohon.getId() + "'");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (mp != null) { mp.close(); }
			}
		}
		
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}
	
	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(BgsPermohonan r) throws Exception {
		// TODO Auto-generated method stub
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		String status = "";
		DevBangunan bangunan=null;
		Agensi agensi=null;
		Bandar bandar=null;
		Status statusSemasa=null;
		Users pemohon=null;
		
		if(userRole.equalsIgnoreCase("(BGS) Pemohon")){
			status = "1428384412943"; // DRAF PERMOHONAN
		} else {
			status = "1423568441671"; //PERMOHONAN BARU
			r.setTarikhPermohonan(new Date());
		}
		
		try {
			mp = new MyPersistence();
			bangunan=(DevBangunan)mp.find(DevBangunan.class, getParam("idBangunan"));
			agensi=(Agensi)mp.find(Agensi.class, getParam("idAgensi"));
			bandar=(Bandar)mp.find(Bandar.class, getParam("idBandar"));
			statusSemasa=(Status)mp.find(Status.class, status);
			pemohon=(Users)mp.find(Users.class, userId);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
		
		r.setTarikhSurat(getDate("tarikhSurat"));
		r.setBangunan(bangunan);
		r.setAgensi(agensi);
		r.setBandar(bandar);
		r.setStatus(statusSemasa);
		r.setPemohon(pemohon);
		r.setJabatan(getParam("jabatan"));
		r.setAlamat1(getParam("alamat1"));
		r.setAlamat2(getParam("alamat2"));
		r.setAlamat3(getParam("alamat3"));
		r.setPoskod(getParam("poskod"));
		r.setNoTelefon(getParam("noTelefon"));
		r.setNoFaks(getParam("noFaks"));
		r.setNamaPegawai(getParam("namaPegawai"));
		r.setEmelPegawai(getParam("emelPegawai"));
		r.setJumlahKeluasanAkanDatang("0");
		r.setJumlahKeluasanSemasa("0");
		r.setJumlahPerjawatanAkanDatang("0");
		r.setJumlahPerjawatanSemasa("0");
	}
	
	@Override
	public void afterSave(BgsPermohonan r) {
		BgsPermohonan permohonan = null;
		int counter = 0;
		//GENERATE NO PERMOHONAN
		String tahun = Util.getDateTime(new Date(), "yyyy");
		String idZon = "00";
		if (r.getBangunan() != null) {
			if (r.getBangunan().getPremis() != null) {
				if (r.getBangunan().getPremis().getMukim() != null) {
					if (r.getBangunan().getPremis().getMukim().getDaerah() != null) {
						if (r.getBangunan().getPremis().getMukim().getDaerah().getNegeri() != null) {
							if (r.getBangunan().getPremis().getMukim().getDaerah().getNegeri().getZon() != null) {
								idZon = r.getBangunan().getPremis().getMukim().getDaerah().getNegeri().getZon().getId();
							}
						}
					}
				}
			}
		}

		String id = idZon + String.valueOf(tahun);
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			BgsSeqPermohonan seq = (BgsSeqPermohonan) mp.find(BgsSeqPermohonan.class, id);
			if(seq != null){
				mp.pesismisticLock(seq);
				counter = seq.getBilPermohonan() + 1;
				seq.setBilPermohonan(counter);
				seq = (BgsSeqPermohonan) mp.merge(seq);
			} else {
				counter = 1;
				seq = new BgsSeqPermohonan();
				seq.setId(id);
				seq.setIdZon(idZon);
				seq.setTahun(Integer.valueOf(tahun));
				seq.setBilPermohonan(counter);
				mp.persist(seq);
				mp.flush();
			}
			
			String formatserial = new DecimalFormat("000").format(counter);
			String noPermohonan = "BGS/" + idZon + "/" + tahun + "/" + formatserial;
			
			permohonan = (BgsPermohonan) mp.find(BgsPermohonan.class, r.getId());
			if (permohonan != null) {
				permohonan.setNoPermohonan(noPermohonan);
				
//				//ADD KEPERLUAN PEJABAT DEFAULT
//				addDefaulKeperluanPejabat("Bilik Mesyuarat - Gred A (40 orang)", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Mesyuarat - Gred B (20 orang)", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Mesyuarat - Gred B (10 orang)", permohonan, mp);				
//				addDefaulKeperluanPejabat("Bilik Fail - Fail Sulit", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Fail - Terbuka / Am", permohonan, mp);				
//				addDefaulKeperluanPejabat("Bilik Dokumentasi", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Bacaan", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Komputer / Taip", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Menunggu", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Minum / Pantri", permohonan, mp);
//				addDefaulKeperluanPejabat("Surau", permohonan, mp);
//				addDefaulKeperluanPejabat("Setor Alatulis / Lain-lain", permohonan, mp);
//				addDefaulKeperluanPejabat("Kaunter", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Kebal", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Temuduga / Rundingan", permohonan, mp);
//				addDefaulKeperluanPejabat("Bilik Pendaftaran", permohonan, mp);
			}
			mp.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("r", permohonan);
		context.put("selectedTab", 1);
	}
	
	private void addDefaulKeperluanPejabat(String perkara, BgsPermohonan permohonan, MyPersistence mp) {
		BgsKeperluanPejabat keperluanPejabat = new BgsKeperluanPejabat();
		keperluanPejabat.setPermohonan(permohonan);
		keperluanPejabat.setPerkara(perkara);
		keperluanPejabat.setLuasSemasa("0");
		keperluanPejabat.setLuasAkanDatang("0");
		mp.persist(keperluanPejabat);
	}

	@Override
	public void getRelatedData(BgsPermohonan r) {
		// TODO Auto-generated method stub
		
		if (r.getBangunan() != null && r.getBangunan().getPremis() != null) {
			context.put("selectBangunan", dataUtil.getListBangunanBGS(r.getBangunan().getPremis().getId()));
			if (r.getBangunan().getPremis().getMukim() != null) {
				context.put("selectPremis", dataUtil.getListPremisBGS(r.getBangunan().getPremis().getMukim().getId()));
				if (r.getBangunan().getPremis().getMukim().getDaerah() != null) {
					context.put("selectMukimBangunan", dataUtil.getListMukim(r.getBangunan().getPremis().getMukim().getDaerah().getId()));	
					if (r.getBangunan().getPremis().getMukim().getDaerah().getNegeri() != null) {
						context.put("selectDaerahBangunan", dataUtil.getListDaerah(r.getBangunan().getPremis().getMukim().getDaerah().getNegeri().getId()));
					}					
				}				
			}			
		}		
		if (r.getAgensi() != null && r.getAgensi().getKementerian() != null)
			context.put("selectAgensi", dataUtil.getListAgensi(r.getAgensi().getKementerian().getId()));
		if (r.getBandar() != null && r.getBandar().getNegeri() != null)
			context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
		
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", 1);
	}

	@Override
	public boolean delete(BgsPermohonan r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("noPermohonan", getParam("findNoPermohonan"));
		m.put("noFail", getParam("findNoFail"));	
		m.put("jabatan", getParam("findJabatan"));
		m.put("agensi.kementerian.id", new OperatorEqualTo(getParam("findKementerian")));
		m.put("agensi.id", new OperatorEqualTo(getParam("findAgensi")));		
		m.put("bangunan.premis.mukim.daerah.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		m.put("bangunan.premis.mukim.daerah.id", new OperatorEqualTo(getParam("findDaerah")));
		m.put("bangunan.premis.mukim.id", new OperatorEqualTo(getParam("findMukim")));
		m.put("bangunan.premis.id", new OperatorEqualTo(getParam("findPremis")));
		m.put("bangunan.id", new OperatorEqualTo(getParam("findBangunan")));		
		return m;
	}
	
	/** START TAB **/
	@Command("getMaklumatPermohonan")
	public String getMaklumatPermohonan() {
		BgsPermohonan r = null;
		try {
			mp = new MyPersistence();
			
			r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			if (r != null) {
				
				if (r.getBangunan() != null && r.getBangunan().getPremis() != null) {
					context.put("selectBangunan", dataUtil.getListBangunanBGS(r.getBangunan().getPremis().getId()));
					if (r.getBangunan().getPremis().getMukim() != null) {
						context.put("selectPremis", dataUtil.getListPremisBGS(r.getBangunan().getPremis().getMukim().getId()));
						if (r.getBangunan().getPremis().getMukim().getDaerah() != null) {
							context.put("selectMukimBangunan", dataUtil.getListMukim(r.getBangunan().getPremis().getMukim().getDaerah().getId()));	
							if (r.getBangunan().getPremis().getMukim().getDaerah().getNegeri() != null) {
								context.put("selectDaerahBangunan", dataUtil.getListDaerah(r.getBangunan().getPremis().getMukim().getDaerah().getNegeri().getId()));
							}					
						}				
					}			
				}		
				if (r.getAgensi() != null && r.getAgensi().getKementerian() != null)
					context.put("selectAgensi", dataUtil.getListAgensi(r.getAgensi().getKementerian().getId()));
				if (r.getBandar() != null && r.getBandar().getNegeri() != null)
					context.put("selectBandar", dataUtil.getListBandar(r.getBandar().getNegeri().getId()));
			}			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPejabat")
	public String getMaklumatPejabat() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				List<BgsMasalah> listMasalah = mp.list("select x from BgsMasalah x where x.permohonan.id = '" + r.getId() + "'");
				context.put("listMasalah", listMasalah);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}		
			
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getMaklumatPerjawatan")
	public String getMaklumatPerjawatan() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				List<BgsPerjawatan> listPerjawatan = mp.list("select x from BgsPerjawatan x where x.permohonan.id = '" + r.getId() + "'");
				context.put("listPerjawatan", listPerjawatan);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getKeperluanPejabat")
	public String getKeperluanPejabat() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				List<BgsKeperluanPejabat> listKeperluanPejabat = mp.list("select x from BgsKeperluanPejabat x where x.permohonan.id = '" + r.getId() + "'");
				context.put("listKeperluanPejabat", listKeperluanPejabat);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", "4");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getDokumenSokongan")
	public String getDokumenSokongan() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				List<JenisDokumen> list = dataUtil.getListJenisDokumen();
				context.put("selectJenisDokumen", list);

				List<BgsDokumen> listDokumen = mp.list("SELECT x FROM BgsDokumen x WHERE x.permohonan.id= '" + r.getId() + "'");
				context.put("listDokumen", listDokumen);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", "5");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSenaraiSemak")
	public String getSenaraiSemak() {
		String checkPermohonan = "Y";
		String checkMaklumatPejabat = "T";
		String checkMasalah = "T";
		String checkPerjawatan = "T";
		String checkKeperluanPejabat = "T";
		String ulasanPengerusi = "T";
		String ulasanPengurus = "T";
		String kuiriBaru = "T";
		String jawapanKuiri = "T";
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				if (r.getJenisPerkhidmatan() != null && !r.getJenisPerkhidmatan().equals("")) {
					checkMaklumatPejabat = "Y";
				}
				List<BgsMasalah> listMasalah = mp.list("select x from BgsMasalah x where x.permohonan.id = '" + r.getId() + "'");
				if (listMasalah.size() > 0) {
					checkMasalah = "Y";
				}
				List<BgsPerjawatan> listPerjawatan = mp.list("select x from BgsPerjawatan x where x.permohonan.id = '" + r.getId() + "'");
				if (listPerjawatan.size() > 0) {
					checkPerjawatan = "Y";
				}
				List<BgsKeperluanPejabat> listKeperluanPejabat = mp.list("select x from BgsKeperluanPejabat x where x.permohonan.id = '" + r.getId() + "'");
				if (listKeperluanPejabat.size() > 0) {
					checkKeperluanPejabat = "Y";
				}
				if (r.getDokumenPengerusi() != null && !r.getDokumenPengerusi().equals("")) {
					ulasanPengerusi = "Y";
				}
				if (r.getDokumenPengurus() != null && !r.getDokumenPengurus().equals("")) {
					ulasanPengurus = "Y";
				}
				
				List<BgsKuiri> listKuiri = mp.list("select x from BgsKuiri x where x.permohonan.id = '" + r.getId() + "' order by x.tarikhKuiri desc");
				context.put("listKuiri", listKuiri);
				for (int i = 0; i < listKuiri.size(); i++) {
					if (listKuiri.get(i).getFlagJawapan().equals("T")) {
						kuiriBaru = "Y";
						if (!listKuiri.get(i).getJawapan().equals("")) {
							jawapanKuiri = "Y";
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		context.put("checkPermohonan", checkPermohonan);
		context.put("checkMaklumatPejabat", checkMaklumatPejabat);
		context.put("checkMasalah", checkMasalah);
		context.put("checkPerjawatan", checkPerjawatan);
		context.put("checkKeperluanPejabat", checkKeperluanPejabat);
		context.put("ulasanPengerusi", ulasanPengerusi);
		context.put("ulasanPengurus", ulasanPengurus);
		context.put("kuiriBaru", kuiriBaru);
		context.put("jawapanKuiri", jawapanKuiri);
		context.put("selectedUpperTab", "1");
		context.put("selectedTab", "6");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getKertasPertimbangan")
	public String getKertasPertimbangan() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.get("select x from BgsKertasPertimbangan x where x.permohonan.id = '" + r.getId() + "'");
				context.put("kertasPertimbangan", kertasPertimbangan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
				
		context.put("selectedUpperTab", "2");
		context.put("selectedTab", "1");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getSemakanKertasPertimbangan")
	public String getSemakanKertasPertimbangan() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
			if (r != null) {
				BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.get("select x from BgsKertasPertimbangan x where x.permohonan.id = '" + r.getId() + "'");
				context.put("kertasPertimbangan", kertasPertimbangan);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedUpperTab", "2");
		context.put("selectedTab", "2");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getKeputusanSUB")
	public String getKeputusanSUB() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedUpperTab", "3");
		return getPath() + "/entry_page.vm";
	}
	
	@Command("getKeputusanAkhir")
	public String getKeputusanAkhir() {
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));
			context.put("r", r);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("selectedTab", "7");
		return getPath() + "/entry_page.vm";
	}
	/** END TAB **/
	
	/** START MAKLUMAT PERMOHONAN **/
	@Command("saveMaklumatPermohonan")
	public String saveMaklumatPermohonan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		try {
			mp = new MyPersistence();
			
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			
			if (r != null) {
				mp.begin();
				r.setTarikhSurat(getDate("tarikhSurat"));
				r.setBangunan((DevBangunan) mp.find(DevBangunan.class, getParam("idBangunan")));			
				r.setAgensi((Agensi) mp.find(Agensi.class, getParam("idAgensi")));
				r.setJabatan(getParam("jabatan"));
				r.setAlamat1(getParam("alamat1"));
				r.setAlamat2(getParam("alamat2"));
				r.setAlamat3(getParam("alamat3"));
				r.setPoskod(getParam("poskod"));
				r.setBandar((Bandar) mp.find(Bandar.class, getParam("idBandar")));
				r.setNoTelefon(getParam("noTelefon"));
				r.setNoFaks(getParam("noFaks"));
				r.setNamaPegawai(getParam("namaPegawai"));
				r.setEmelPegawai(getParam("emelPegawai"));
				mp.commit();
				
				flagStatusInfo = "Y";
				statusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
			}
			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getMaklumatPermohonan();
	}
	
	@Command("hantarPermohonan")
	public String hantarPermohonan() throws Exception {
		
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		try {
			mp = new MyPersistence();
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			if (r != null) {
				mp.begin();
				r.setPemohon((Users) mp.find(Users.class, userId));
				r.setTarikhPermohonan(new Date());
				r.setStatus((Status) mp.find(Status.class, "1423568441671")); // PERMOHONAN BARU
				
				List<BgsKuiri> listKuiri = mp.list("select x from BgsKuiri x where x.permohonan.id = '" + r.getId() + "' and x.flagJawapan = 'T'");
				for (int i = 0; i < listKuiri.size(); i++) {
					listKuiri.get(i).setTarikhJawapan(new Date());
					listKuiri.get(i).setFlagJawapan("Y");
				}
				
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "PERMOHONAN TELAH BERJAYA DIHANTAR";
			}			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DIHANTAR";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSenaraiSemak();
	}
	
	@SuppressWarnings("deprecation")
	@Command("daftarFail")
	public String daftarFail() throws Exception {
		
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		String tujuanKP, lokasiDipohon, keluasanDipohon, namaJabatan, pejabatSediaada = "";
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			if (r != null) {
				mp.begin();
				
				r.setNoFail(generateNoFail(r,mp)); //TODO change to actual no fail		
				r.setTarikhDaftarFail(new Date());
				r.setPendaftar((Users) mp.find(Users.class, userId));
				r.setStatus((Status) mp.find(Status.class, "1423568441688")); // PENYEDIAAN KERTAS PERTIMBANGAN
				
				tujuanKP = "PERMOHONAN RUANG PEJABAT OLEH " + r.getJabatan() + " DI " + r.getBangunan().getNamaBangunan() + ", " + r.getBangunan().getPremis().getNamaPremis();
				lokasiDipohon = StringUtils.capitaliseAllWords(r.getBangunan().getNamaBangunan().toLowerCase())  + ", " + StringUtils.capitaliseAllWords(r.getBangunan().getPremis().getNamaPremis().toLowerCase());
				keluasanDipohon = r.getJumlahKeluasanAkanDatang() + " meter persegi.";
				namaJabatan = StringUtils.capitaliseAllWords(r.getJabatan().toLowerCase());
				pejabatSediaada = StringUtils.capitaliseAllWords(r.getJabatan().trim().toLowerCase()) + " " + StringUtils.capitaliseAllWords(r.getAlamat1().trim().toLowerCase())	
						+ " " + StringUtils.capitaliseAllWords(r.getAlamat2().trim().toLowerCase())
						+ " " + StringUtils.capitaliseAllWords(r.getAlamat3().trim().toLowerCase())
						+ " " + r.getPoskod().trim() + " " + StringUtils.capitaliseAllWords(r.getBandar().getKeterangan().trim().toLowerCase())
						+ " " + StringUtils.capitaliseAllWords(r.getBandar().getNegeri().getKeterangan().trim().toLowerCase());
				
				BgsKertasPertimbangan kertasPertimbangan = new BgsKertasPertimbangan();
				kertasPertimbangan.setPermohonan(r);				
				kertasPertimbangan.setTujuan(tujuanKP);
				kertasPertimbangan.setLokasiRuangDipohon(lokasiDipohon);
				kertasPertimbangan.setKeluasanRuangDipohon(keluasanDipohon);
				kertasPertimbangan.setNamaJabatan(namaJabatan);
				kertasPertimbangan.setBilKakitangan(r.getJumlahPerjawatanAkanDatang());
				kertasPertimbangan.setPejabatSediaada(pejabatSediaada);
				kertasPertimbangan.setCadanganRuang(lokasiDipohon);
				List<BgsMasalah> listMasalah = mp.list("select x from BgsMasalah x where x.permohonan.id = '" + r.getId() + "' order by x.id asc");
				for (int i = 0; i <listMasalah.size(); i++) {
					if (i == 0){
						kertasPertimbangan.setAlasan1(StringUtils.capitalise(listMasalah.get(i).getMasalah().trim().toLowerCase()));
					}
					if (i == 1){
						kertasPertimbangan.setAlasan2(StringUtils.capitalise(listMasalah.get(i).getMasalah().trim().toLowerCase()));
					}
					if (i == 2){
						kertasPertimbangan.setAlasan3(StringUtils.capitalise(listMasalah.get(i).getMasalah().trim().toLowerCase()));
						break;
					}
				}
				mp.persist(kertasPertimbangan);
				
				List<BgsKuiri> listKuiri = mp.list("select x from BgsKuiri x where x.permohonan.id = '" + r.getId() + "' and x.flagJawapan = 'T'");
				for (int i = 0; i < listKuiri.size(); i++) {
					mp.remove(listKuiri.get(i));
				}
				
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "FAIL TELAH BERJAYA DIDAFTAR";
			}
			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "FAIL TIDAK BERJAYA DIDAFTAR";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSenaraiSemak();
	}
	
	private String generateNoFail(BgsPermohonan r, MyPersistence mp) {
		String noFail = "";
		int counter = 0;
		//GENERATE NO PERMOHONAN
		String tahun = Util.getDateTime(new Date(), "yyyy");
		String idZon = "00";
		if (r.getBangunan() != null) {
			if (r.getBangunan().getPremis() != null) {
				if (r.getBangunan().getPremis().getMukim() != null) {
					if (r.getBangunan().getPremis().getMukim().getDaerah() != null) {
						if (r.getBangunan().getPremis().getMukim().getDaerah().getNegeri() != null) {
							if (r.getBangunan().getPremis().getMukim().getDaerah().getNegeri().getZon() != null) {
								idZon = r.getBangunan().getPremis().getMukim().getDaerah().getNegeri().getZon().getId();
							}
						}
					}
				}
			}
		}

		String id = idZon + String.valueOf(tahun);
		
		BgsSeqPermohonan seq = (BgsSeqPermohonan) mp.find(BgsSeqPermohonan.class, id);
		if(seq != null){
			mp.pesismisticLock(seq);
			counter = seq.getBilFail() + 1;
			seq.setBilPermohonan(counter);
			seq = (BgsSeqPermohonan) mp.merge(seq);
		} else {
			counter = 1;
			seq = new BgsSeqPermohonan();
			seq.setId(id);
			seq.setIdZon(idZon);
			seq.setTahun(Integer.valueOf(tahun));
			seq.setBilFail(counter);
			mp.persist(seq);
			mp.flush();
		}
		
		String formatserial = new DecimalFormat("000").format(counter);
		noFail = "BPH/RP/200-" + idZon + "/" + tahun + "/" + formatserial;
		
		return noFail;
	}
	
	@Command("hantarKuiri")
	public String hantarKuiri() throws Exception {		
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		try {
			mp = new MyPersistence();
			
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			if (r != null) {
				mp.begin();
				r.setStatus((Status) mp.find(Status.class, "1423568441685")); // KUIRI DARI BPH
				
				List<BgsKuiri> listKuiri = mp.list("select x from BgsKuiri x where x.permohonan.id = '" + r.getId() + "' and x.flagJawapan = 'T'");
				for (int i = 0; i < listKuiri.size(); i++) {
					listKuiri.get(i).setTarikhKuiri(new Date());
				}
				
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KUIRI TELAH BERJAYA DIHANTAR";
			}			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KUIRI TIDAK BERJAYA DIHANTAR";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);		
		return getSenaraiSemak();
	}
	/** END MAKLUMAT PERMOHONAN **/
	
	/** START MAKLUMAT PEJABAT **/
	@Command("saveMaklumatPejabat")
	public String saveMaklumatPejabat() throws Exception {
		
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		try {
			mp = new MyPersistence();
			
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			
			if (r != null) {
				mp.begin();
				r.setJenisPerkhidmatan(getParam("jenisPerkhidmatan"));
				r.setStatusRuangPejabat(getParam("statusRuangPejabat"));
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
			}
			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
				
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getMaklumatPejabat();
	}
	
	@Command("editMasalah")
	public String editMasalah() {
		context.remove("rekod");
		
		try {
			mp = new MyPersistence();
			BgsMasalah masalah = (BgsMasalah) mp.find(BgsMasalah.class, get("idMasalah"));
			context.put("rekod", masalah);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
		
		return getPath()  + "/maklumatPejabat/popupMasalahPejabat.vm";
	}
	
	@Command("saveIMasalahPejabat")
	public String saveIMasalahPejabat() throws ParseException {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			BgsPermohonan permohonan = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));	
			BgsMasalah masalah = new BgsMasalah();		
			masalah.setMasalah(getParam("iMasalah"));
			masalah.setPermohonan(permohonan);
			mp.persist(masalah);
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DISIMPAN";
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		
		return getMaklumatPejabat();
	}
	
	@Command("kemaskiniMasalahPejabat")
	public String kemaskiniMasalahPejabat() throws ParseException {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			BgsMasalah masalah = (BgsMasalah) mp.find(BgsMasalah.class, get("idMasalah"));	
			masalah.setMasalah(getParam("masalah"));
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
			
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		
		return getMaklumatPejabat();
	}
	
	@SuppressWarnings("unchecked")
	@Command("deleteMasalahPejabat")
	public String deleteMasalahPejabat() {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			BgsMasalah masalah = (BgsMasalah) mp.find(BgsMasalah.class, get("idMasalah"));
			if (masalah != null) mp.remove(masalah);
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DIHAPUS";
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		
		return getMaklumatPejabat();
	}
	/** END MAKLUMAT PEJABAT **/
	
	/** START MAKLUMAT PERJAWATAN **/	
	@Command("editPerjawatan")
	public String editPerjawatan() {
		context.remove("rekod");
		
		try {
			mp = new MyPersistence();
			BgsPerjawatan perjawatan = (BgsPerjawatan) mp.find(BgsPerjawatan.class, get("idPerjawatan"));
			context.put("rekod", perjawatan);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
		
		return getPath()  + "/maklumatPerjawatan/popupMaklumatPerjawatan.vm";
	}
	
	@Command("saveIPerjawatan")
	public String saveIPerjawatan() throws ParseException {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			BgsPermohonan permohonan = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));	
			
			BgsPerjawatan perjawatan = new BgsPerjawatan();		
			perjawatan.setJawatan(getParam("iJawatan"));
			perjawatan.setGred(getParam("iGred"));
			perjawatan.setBilanganSemasa(getParam("iBilanganSemasa"));
			perjawatan.setLuasSemasa(getParam("iLuasSemasa"));
			perjawatan.setBilanganAkanDatang(getParam("iBilanganAkanDatang"));
			perjawatan.setLuasAkanDatang(getParam("iLuasAkanDatang"));
			perjawatan.setPermohonan(permohonan);
			mp.persist(perjawatan);
			
			updateJumlahPerjawatan(permohonan, mp);
			updateJumlahKeluasan(permohonan, mp);
			
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DISIMPAN";
			
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
				
		return getMaklumatPerjawatan();
	}

	@Command("kemaskiniPerjawatan")
	public String kemaskiniPerjawatan() throws ParseException {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";

		try {
			mp = new MyPersistence();
			mp.begin();
			
			BgsPerjawatan perjawatan = (BgsPerjawatan) mp.find(BgsPerjawatan.class, get("idPerjawatan"));	
			if (perjawatan != null) {
				perjawatan.setJawatan(getParam("jawatan"));
				perjawatan.setGred(getParam("gred"));
				perjawatan.setBilanganSemasa(getParam("bilanganSemasa"));
				perjawatan.setLuasSemasa(getParam("luasSemasa"));
				perjawatan.setBilanganAkanDatang(getParam("bilanganAkanDatang"));
				perjawatan.setLuasAkanDatang(getParam("luasAkanDatang"));
			}		
			
			updateJumlahPerjawatan(perjawatan.getPermohonan(), mp);
			updateJumlahKeluasan(perjawatan.getPermohonan(), mp);
			
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
		} catch (Exception ex) {
			context.put("popupFlagStatusInfo", popupFlagStatusInfo);
			context.put("popupStatusInfo", popupStatusInfo);
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		return getMaklumatPerjawatan();
	}
	
	@SuppressWarnings("unchecked")
	@Command("deletePerjawatan")
	public String deletePerjawatan() {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			BgsPerjawatan perjawatan = (BgsPerjawatan) mp.find(BgsPerjawatan.class, get("idPerjawatan"));	
			if (perjawatan != null) {
				mp.remove(perjawatan);
			}
			
			updateJumlahPerjawatan(perjawatan.getPermohonan(), mp);
			updateJumlahKeluasan(perjawatan.getPermohonan(), mp);
			
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DIHAPUS";
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		
		return getMaklumatPerjawatan();
	}
	
	private void updateJumlahPerjawatan(BgsPermohonan permohonan, MyPersistence mp) {
		int jumlahPerjawatanSemasa = 0;
		int jumlahPerjawatanAkanDatang = 0;
		
		List<BgsPerjawatan> list = mp.list("select x from BgsPerjawatan x where x.permohonan.id = '" + permohonan.getId() + "'");
		if (list != null){
			for (int i = 0; i < list.size(); i++){
				jumlahPerjawatanSemasa = jumlahPerjawatanSemasa + Integer.parseInt(list.get(i).getBilanganSemasa());
				jumlahPerjawatanAkanDatang = jumlahPerjawatanAkanDatang + Integer.parseInt(list.get(i).getBilanganAkanDatang());
			}
		}
		
		permohonan.setJumlahPerjawatanSemasa(String.valueOf(jumlahPerjawatanSemasa));
		permohonan.setJumlahPerjawatanAkanDatang(String.valueOf(jumlahPerjawatanAkanDatang));
	}
	
	private void updateJumlahKeluasan(BgsPermohonan permohonan, MyPersistence mp) {
		// TODO Auto-generated method stub
		double jumlahKeluasanSemasa = 0D;
		double jumlahKeluasanAkanDatang = 0D;
		
		List<BgsPerjawatan> list = mp.list("select x from BgsPerjawatan x where x.permohonan.id = '" + permohonan.getId() + "'");
		if (list != null){
			for (int i = 0; i < list.size(); i++){
				jumlahKeluasanSemasa = jumlahKeluasanSemasa + Double.valueOf(list.get(i).getLuasSemasa());
				jumlahKeluasanAkanDatang = jumlahKeluasanAkanDatang + Double.valueOf(list.get(i).getLuasAkanDatang());
			}
		}
		
		List<BgsKeperluanPejabat> listKeperluanPejabat = mp.list("select x from BgsKeperluanPejabat x where x.permohonan.id = '" + permohonan.getId() + "'");
		if (listKeperluanPejabat != null){
			for (int j = 0; j < listKeperluanPejabat.size(); j++){
				jumlahKeluasanSemasa = jumlahKeluasanSemasa + Double.valueOf(listKeperluanPejabat.get(j).getLuasSemasa());
				jumlahKeluasanAkanDatang = jumlahKeluasanAkanDatang + Double.valueOf(listKeperluanPejabat.get(j).getLuasAkanDatang());
			}
		}

		permohonan.setJumlahKeluasanSemasa(String.valueOf(jumlahKeluasanSemasa));
		permohonan.setJumlahKeluasanAkanDatang(String.valueOf(jumlahKeluasanAkanDatang));
	}
	/** END MAKLUMAT PERJAWATAN **/
	
	/** START KEPERLUAN PEJABAT **/	
	@Command("editKeperluanPejabat")
	public String editKeperluanPejabat() {
		context.remove("rekod");
		
		try {
			mp = new MyPersistence();
			BgsKeperluanPejabat keperluanPejabat = (BgsKeperluanPejabat) mp.find(BgsKeperluanPejabat.class, get("idKeperluanPejabat"));
			context.put("rekod", keperluanPejabat);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		context.remove("flagStatusInfo");	
		context.remove("statusInfo");	
		
		return getPath()  + "/keperluanPejabat/popupMaklumatKeperluanPejabat.vm";
	}
	
	@Command("saveIKeperluanPejabat")
	public String saveIKeperluanPejabat() throws ParseException {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			BgsPermohonan permohonan = (BgsPermohonan) mp.find(BgsPermohonan.class, get("idPermohonan"));				
			BgsKeperluanPejabat keperluanPejabat = new BgsKeperluanPejabat();		
			keperluanPejabat.setPerkara(getParam("iPerkara"));
			keperluanPejabat.setLuasSemasa(getParam("iLuasSemasa"));
			keperluanPejabat.setLuasAkanDatang(getParam("iLuasAkanDatang"));
			keperluanPejabat.setPermohonan(permohonan);
			mp.persist(keperluanPejabat);
			
			updateJumlahKeluasan(permohonan, mp);
			
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DISIMPAN";
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		
		return getKeperluanPejabat();
	}
	
	@Command("kemaskiniKeperluanPejabat")
	public String kemaskiniKeperluanPejabat() throws ParseException {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";

		try {
			mp = new MyPersistence();
			mp.begin();
			BgsKeperluanPejabat keperluanPejabat = (BgsKeperluanPejabat) mp.find(BgsKeperluanPejabat.class, get("idKeperluanPejabat"));
			if (keperluanPejabat != null) {
				keperluanPejabat.setPerkara(getParam("perkara"));
				keperluanPejabat.setLuasSemasa(getParam("luasSemasa"));
				keperluanPejabat.setLuasAkanDatang(getParam("luasAkanDatang"));
			}
			updateJumlahKeluasan(keperluanPejabat.getPermohonan(), mp);
			mp.commit();
			popupFlagStatusInfo = "Y";
			popupStatusInfo = "MAKLUMAT BERJAYA DIKEMASKINI";
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		return getKeperluanPejabat();
	}
	
	@SuppressWarnings("unchecked")
	@Command("deleteKeperluanPejabat")
	public String deleteKeperluanPejabat() {
		String popupFlagStatusInfo = "";		
		String popupStatusInfo = "";
		
		try {
			mp = new MyPersistence();
			
			BgsKeperluanPejabat keperluanPejabat = (BgsKeperluanPejabat) mp.find(BgsKeperluanPejabat.class, get("idKeperluanPejabat"));	
			if (keperluanPejabat != null) {
				mp.begin();
				mp.remove(keperluanPejabat);
				mp.commit();
				popupFlagStatusInfo = "Y";
				popupStatusInfo = "MAKLUMAT BERJAYA DIHAPUS";
				
				updateJumlahKeluasan(keperluanPejabat.getPermohonan(), mp);
			}
		} catch (Exception ex) {
			popupFlagStatusInfo = "T";
			popupStatusInfo = "MAKLUMAT TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("popupFlagStatusInfo", popupFlagStatusInfo);
		context.put("popupStatusInfo", popupStatusInfo);
		
		return getKeperluanPejabat();
	}
	/** END MAKLUMAT PEJABAT **/
	
	/** START DOKUMEN SOKONGAN **/
	@Command("uploadDoc")
	public String uploadDoc() throws Exception {
		String idPermohonan = get("idPermohonan");
		String tajukDokumen = get("tajukDokumen");
		String idJenisDokumen = get("idJenisDokumen");
		String keteranganDokumen = get("keteranganDokumen");
		BgsDokumen dokumen = new BgsDokumen();
		String uploadDir = "bgs/permohonanRuangPejabat/dokumenSokongan/";
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
			String imgName = uploadDir + idPermohonan + "_" + dokumen.getId()
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
				simpanDokumen(idPermohonan, imgName, avatarName, tajukDokumen,
						idJenisDokumen, keteranganDokumen, dokumen);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumen(String idPermohonan, String imgName,
			String avatarName, String tajukDokumen, String idJenisDokumen,
			String keteranganDokumen, BgsDokumen dokumen) throws Exception {
		String flagStatusInfo = "";		
		String statusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			
			dokumen.setPermohonan((BgsPermohonan) mp.find(BgsPermohonan.class, idPermohonan));
			dokumen.setPhotofilename(imgName);
			dokumen.setThumbfilename(avatarName);
			dokumen.setTajuk(tajukDokumen);
			dokumen.setJenisDokumen((JenisDokumen) mp.find(JenisDokumen.class, idJenisDokumen));
			dokumen.setKeterangan(keteranganDokumen);
			mp.persist(dokumen);
			
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "DOKUMEN  BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
	}

	@Command("deleteDokumen")
	public String deleteDokumen() throws Exception {
		String flagStatusInfo = "";		
		String statusInfo = "";
		String idDokumen = get("idDokumen");
		
		try {
			mp = new MyPersistence();
			BgsDokumen dokumen = (BgsDokumen) mp.find(BgsDokumen.class, idDokumen);

			if (dokumen != null) {
				Util.deleteFile(dokumen.getPhotofilename());
				Util.deleteFile(dokumen.getThumbfilename());
				mp.begin();
				mp.remove(dokumen);
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "DOKUMEN BERJAYA DIHAPUS";
			}
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getDokumenSokongan();
	}

	@Command("refreshList")
	public String refreshList() throws Exception {

		return getDokumenSokongan();
	}
	/** END DOKUMEN SOKONGAN **/
	
	/** START DOKUMEN PENGERUSI **/
	@Command("uploadDocPengerusi")
	public String uploadDocPengerusi() throws Exception {
		String idPermohonan = get("idPermohonan");
		String uploadDir = "bgs/permohonanRuangPejabat/dokumenSokongan/";
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
			String imgName = uploadDir + idPermohonan + "_" + UID.getUID()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			if (!imgName.equals("")) {
				simpanDokumenPengerusi(idPermohonan, imgName);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumenPengerusi(String idPermohonan, String imgName) throws Exception {
		String flagStatusInfo = "";		
		String statusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			BgsPermohonan permohonan =  (BgsPermohonan) mp.find(BgsPermohonan.class, idPermohonan);
            if (permohonan != null) {
            	permohonan.setDokumenPengerusi(imgName);
            }			
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "DOKUMEN  BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
	}

	@Command("deleteDokumenPengerusi")
	public String deleteDokumenPengerusi() throws Exception {
		String flagStatusInfo = "";		
		String statusInfo = "";
		String idPermohonan = get("idPermohonan");
		
		try {
			mp = new MyPersistence();
			BgsPermohonan permohonan =  (BgsPermohonan) mp.find(BgsPermohonan.class, idPermohonan);

			if (permohonan != null) {
				Util.deleteFile(permohonan.getDokumenPengerusi());
				mp.begin();
				permohonan.setDokumenPengerusi(null);
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "DOKUMEN BERJAYA DIHAPUS";
			}
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getDokumenSokongan();
	}
	/** END DOKUMEN PENGERUSI **/
    
    /** START DOKUMEN PENGURUS **/
	@Command("uploadDocPengurus")
	public String uploadDocPengurus() throws Exception {
		String idPermohonan = get("idPermohonan");
		String uploadDir = "bgs/permohonanRuangPejabat/dokumenSokongan/";
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
			String imgName = uploadDir + idPermohonan + "_" + UID.getUID()
					+ fileName.substring(fileName.lastIndexOf("."));

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));

			if (!imgName.equals("")) {
				simpanDokumenPengurus(idPermohonan, imgName);
			}
		}

		return getPath() + "/dokumenSokongan/uploadDoc.vm";
	}

	public void simpanDokumenPengurus(String idPermohonan, String imgName) throws Exception {
		String flagStatusInfo = "";		
		String statusInfo = "";
		
		try {
			mp = new MyPersistence();
			mp.begin();
			BgsPermohonan permohonan =  (BgsPermohonan) mp.find(BgsPermohonan.class, idPermohonan);
            if (permohonan != null) {
            	permohonan.setDokumenPengurus(imgName);
            }			
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "DOKUMEN  BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
	}

	@Command("deleteDokumenPengurus")
	public String deleteDokumenPengurus() throws Exception {
		String flagStatusInfo = "";		
		String statusInfo = "";
		String idPermohonan = get("idPermohonan");
		
		try {
			mp = new MyPersistence();
			BgsPermohonan permohonan =  (BgsPermohonan) mp.find(BgsPermohonan.class, idPermohonan);

			if (permohonan != null) {
				Util.deleteFile(permohonan.getDokumenPengurus());
				mp.begin();
				permohonan.setDokumenPengurus(null);
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "DOKUMEN BERJAYA DIHAPUS";
			}
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "DOKUMEN TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getDokumenSokongan();
	}
	/** END DOKUMEN PENGURUS **/
	
	/** START MAKLUMAT KUIRI **/
	@Command("saveIKuiri")
	public String saveIKuiri() throws ParseException {
		String flagStatusInfo = "";		
		String statusInfo = "";

		try {
			mp = new MyPersistence();
			mp.begin();
			BgsPermohonan permohonan = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			BgsKuiri kuiri = new BgsKuiri();
			kuiri.setPermohonan(permohonan);
			kuiri.setKuiri(getParam("iKuiri"));
			kuiri.setFlagJawapan("T");
			mp.persist(kuiri);
			mp.commit();
			flagStatusInfo = "Y";
			statusInfo = "KUIRI BERJAYA DISIMPAN";
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KUIRI TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getSenaraiSemak();
	}
	
	@SuppressWarnings("unchecked")
	@Command("deleteKuiri")
	public String deleteKuiri() {
		String flagStatusInfo = "";		
		String statusInfo = "";
		
		try {
			mp = new MyPersistence();
			
			BgsKuiri kuiri = (BgsKuiri) mp.find(BgsKuiri.class, get("idKuiri"));	
			if (kuiri != null) {
				mp.begin();
				mp.remove(kuiri);
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KUIRI BERJAYA DIHAPUS";
			}
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KUIRI TIDAK BERJAYA DIHAPUS";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getSenaraiSemak();
	}
	
	@Command("saveIJawapan")
	public String saveIJawapan() throws ParseException {
		String flagStatusInfo = "";		
		String statusInfo = "";

		try {
			mp = new MyPersistence();
			
			BgsKuiri kuiri = (BgsKuiri) mp.find(BgsKuiri.class, getParam("idKuiri"));
			if (kuiri != null) {
				if (!getParam("iJawapan" + kuiri.getId()).equals("")) {
					mp.begin();
					kuiri.setJawapan(getParam("iJawapan" + kuiri.getId()));
					mp.commit();
					flagStatusInfo = "Y";
					statusInfo = "JAWAPAN KUIRI BERJAYA DISIMPAN";
				}				
			}				
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "JAWAPAN KUIRI TIDAK BERJAYA DISIMPAN";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		return getSenaraiSemak();
	}
	/** END MAKLUMAT KUIRI **/
	
	/** START KERTAS PERTIMBANGAN **/
	@Command("simpanKertasPertimbangan")
	public String simpanKertasPertimbangan() throws Exception {
		
		String statusInfo = "";	
		String flagStatusInfo = "";		
		
		try {
			mp = new MyPersistence();
			
			BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.find(BgsKertasPertimbangan.class, getParam("idKertasPertimbangan"));
			if (kertasPertimbangan != null) {
				mp.begin();
				
				kertasPertimbangan.setTujuan(getParam("tujuan"));
				kertasPertimbangan.setLokasiRuangDipohon(getParam("lokasiRuangDipohon"));
				kertasPertimbangan.setKeluasanRuangDipohon(getParam("keluasanRuangDipohon"));
				kertasPertimbangan.setLampiranA(getParam("lampiranA"));
				kertasPertimbangan.setNamaJabatan(getParam("namaJabatan"));
				kertasPertimbangan.setBilKakitangan(getParam("bilKakitangan"));
				kertasPertimbangan.setPejabatSediaada(getParam("pejabatSediaada"));
				kertasPertimbangan.setCadanganRuang(getParam("cadanganRuang"));
				kertasPertimbangan.setAlasan1(getParam("alasan1"));
				kertasPertimbangan.setAlasan2(getParam("alasan2"));
				kertasPertimbangan.setAlasan3(getParam("alasan3"));
				kertasPertimbangan.setLampiranB(getParam("lampiranB"));
				kertasPertimbangan.setNamaPengerusiBangunan(getParam("namaPengerusiBangunan"));
				kertasPertimbangan.setUlasanPengerusiBangunan(getParam("ulasanPengerusiBangunan"));
				kertasPertimbangan.setLampiranC(getParam("lampiranC"));
				kertasPertimbangan.setNamaPengurusBangunan(getParam("namaPengurusBangunan"));
				kertasPertimbangan.setUlasanPengurusBangunan(getParam("ulasanPengurusBangunan"));
				kertasPertimbangan.setLampiranD(getParam("lampiranD"));
				
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KERTAS PERTIMBANGAN BERJAYA DIKEMASKINI";
			}
			
		} catch (Exception ex) {
			flagStatusInfo = "T";
			statusInfo = "KERTAS PERTIMBANGAN TIDAK BERJAYA DIKEMASKINI";
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getKertasPertimbangan();
	}

	@Command("hantarSemakan")
	public String hantarSemakan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		
		try {
			mp = new MyPersistence();
			Users penyedia = (Users) mp.find(Users.class, userId);
			BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.find(BgsKertasPertimbangan.class, getParam("idKertasPertimbangan"));
			if (kertasPertimbangan != null) {
				mp.begin();
				kertasPertimbangan.setPenyedia(penyedia);
				if (kertasPertimbangan.getTarikhPenyediaan() == null) {
					kertasPertimbangan.setTarikhPenyediaan(new Date());
				}				
				kertasPertimbangan.setUlasanPenyedia(getParam("ulasanPenyedia"));
				
				kertasPertimbangan.setUlasanPenyemak(null);
				kertasPertimbangan.setTarikhSemakan(null);
				kertasPertimbangan.setPenyemak(null);
				kertasPertimbangan.setFlagKeputusanSemakan(null);
				kertasPertimbangan.setUlasanPengesah(null);
				kertasPertimbangan.setTarikhPengesahan(null);
				kertasPertimbangan.setPengesah(null);
				kertasPertimbangan.setFlagKeputusanPengesahan(null);
				
				kertasPertimbangan.getPermohonan().setStatus((Status) mp.find(Status.class, "1423568441682")); // SEMAKAN KERTAS PERTIMBANGAN
							
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KERTAS PERTIMBANGAN TELAH DIHANTAR UNTUK SEMAKAN";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "KERTAS PERTIMBANGAN TIDAK BERJAYA DIHANTAR UNTUK SEMAKAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKertasPertimbangan();
	}
	
	@Command("simpanPindaanPenyemak")
	public String simpanPindaanPenyemak() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		
		try {
			mp = new MyPersistence();
			Users penyemak = (Users) mp.find(Users.class, userId);
			BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.find(BgsKertasPertimbangan.class, getParam("idKertasPertimbangan"));
			if (kertasPertimbangan != null) {
				mp.begin();
				kertasPertimbangan.setPenyemak(penyemak);
				kertasPertimbangan.setTarikhSemakan(new Date());				
				kertasPertimbangan.setUlasanPenyemak(getParam("ulasanPenyemak"));
				kertasPertimbangan.setFlagKeputusanSemakan("T");
				
				kertasPertimbangan.setUlasanPenyedia(null);
				
				kertasPertimbangan.getPermohonan().setStatus((Status) mp.find(Status.class, "1423568441694")); // PINDAAN KERTAS PERTIMBANGAN
							
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KERTAS PERTIMBANGAN TELAH DIHANTAR UNTUK PINDAAN";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "KERTAS PERTIMBANGAN TIDAK BERJAYA DIHANTAR UNTUK PINDAAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKertasPertimbangan();
	}
	
	@Command("hantarPengesahan")
	public String hantarPengesahan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		
		try {
			mp = new MyPersistence();
			Users penyemak = (Users) mp.find(Users.class, userId);
			BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.find(BgsKertasPertimbangan.class, getParam("idKertasPertimbangan"));
			if (kertasPertimbangan != null) {
				mp.begin();
				kertasPertimbangan.setPenyemak(penyemak);
				kertasPertimbangan.setTarikhSemakan(new Date());				
				kertasPertimbangan.setUlasanPenyemak(getParam("ulasanPenyemak"));
				kertasPertimbangan.setFlagKeputusanSemakan("Y");
				
				kertasPertimbangan.getPermohonan().setStatus((Status) mp.find(Status.class, "1423822397722")); // PENGESAHAN KERTAS PERTIMBANGAN
							
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KERTAS PERTIMBANGAN TELAH DIHANTAR UNTUK PENGESAHAN";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "KERTAS PERTIMBANGAN TIDAK BERJAYA DIHANTAR UNTUK PENGESAHAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKertasPertimbangan();
	}
	
	@Command("simpanPindaanPengesah")
	public String simpanPindaanPengesah() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		
		try {
			mp = new MyPersistence();
			Users pengesah = (Users) mp.find(Users.class, userId);
			BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.find(BgsKertasPertimbangan.class, getParam("idKertasPertimbangan"));
			if (kertasPertimbangan != null) {
				mp.begin();
				kertasPertimbangan.setPengesah(pengesah);
				kertasPertimbangan.setTarikhPengesahan(new Date());				
				kertasPertimbangan.setUlasanPengesah(getParam("ulasanPengesah"));
				kertasPertimbangan.setFlagKeputusanPengesahan("T");
				
				kertasPertimbangan.setUlasanPenyedia(null);
				
				kertasPertimbangan.getPermohonan().setStatus((Status) mp.find(Status.class, "1423568441694")); // PINDAAN KERTAS PERTIMBANGAN
							
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KERTAS PERTIMBANGAN TELAH DIHANTAR UNTUK PINDAAN";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "KERTAS PERTIMBANGAN TIDAK BERJAYA DIHANTAR UNTUK PINDAAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKertasPertimbangan();
	}
	
	@Command("simpanPengesahan")
	public String simpanPengesahan() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";				
		
		try {
			mp = new MyPersistence();
			Users pengesah = (Users) mp.find(Users.class, userId);
			BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.find(BgsKertasPertimbangan.class, getParam("idKertasPertimbangan"));
			if (kertasPertimbangan != null) {
				mp.begin();
				kertasPertimbangan.setPengesah(pengesah);
				kertasPertimbangan.setTarikhPengesahan(new Date());				
				kertasPertimbangan.setUlasanPengesah(getParam("ulasanPengesah"));
				kertasPertimbangan.setFlagKeputusanPengesahan("Y");
				
				kertasPertimbangan.getPermohonan().setStatus((Status) mp.find(Status.class, "1423568441691")); // KEPUTUSAN SUB
							
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "KERTAS PERTIMBANGAN TELAH DISAHKAN";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "KERTAS PERTIMBANGAN TIDAK BERJAYA DISAHKAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getSemakanKertasPertimbangan();
	}
	/** END KERTAS PERTIMBANGAN **/
	
	/** START KEPUTUSAN SUB **/
	@Command("simpanKeputusanSUB")
	public String simpanKeputusanSUB() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		try {
			mp = new MyPersistence();
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			if (r != null) {
				mp.begin();
				r.setTarikhKeputusan(getDate("tarikhKeputusan"));
				r.setFlagKeputusan(getParam("idKeputusan"));
				r.setCatatanKeputusan(getParam("catatanKeputusan"));
				
				if ("L".equals(getParam("idKeputusan"))) {
					BgsKertasPertimbangan kertasPertimbangan = (BgsKertasPertimbangan) mp.get("select x from BgsKertasPertimbangan x where x.permohonan.id = '" + r.getId() + "'");
					if (kertasPertimbangan != null) {
						r.setRuangDiluluskan(kertasPertimbangan.getCadanganRuang());
						r.setLuasDiluluskan(r.getJumlahKeluasanAkanDatang());
					}
				} else {
					r.setRuangDiluluskan(null);
					r.setLuasDiluluskan(null);
				}
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "MAKLUMAT KEPUTUSAN BERJAYA DISIMPAN";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "MAKLUMAT KEPUTUSAN TIDAK BERJAYA DISIMPAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getKeputusanSUB();
	}
	
	@Command("simpanSelesai")
	public String simpanSelesai() throws Exception {
		String statusInfo = "";	
		String flagStatusInfo = "";	
		
		try {
			mp = new MyPersistence();
			BgsPermohonan r = (BgsPermohonan) mp.find(BgsPermohonan.class, getParam("idPermohonan"));
			if (r != null) {
				mp.begin();
				
				if (r.getFlagKeputusan().equals("L")) {
					r.setStatus((Status) mp.find(Status.class, "1423568441697")); // LULUS
				} else {
					r.setStatus((Status) mp.find(Status.class, "1423568441700")); // TOLAK
				}
				mp.commit();
				flagStatusInfo = "Y";
				statusInfo = "PERMOHONAN SELESAI";
			}
		} catch (Exception e) {
			flagStatusInfo = "T";
			statusInfo = "PERMOHONAN TIDAK BERJAYA DISELESAIKAN";
		}
		
		context.put("flagStatusInfo", flagStatusInfo);
		context.put("statusInfo", statusInfo);
		
		return getKeputusanSUB();
	}
	/** END KEPUTUSAN SUB **/
	
	/** START DROP DOWN **/
	@Command("findAgensi")
	public String findAgensi() throws Exception {
		String idKementerian = "0";
		if (get("findKementerian").trim().length() > 0)
			idKementerian = get("findKementerian");
		
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/findAgensi.vm";
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
	
	@Command("findPremis")
	public String findPremis() throws Exception {
		String idMukim = "0";
		if (get("findMukim").trim().length() > 0)
			idMukim = get("findMukim");
		
		List<DevPremis> list = dataUtil.getListPremisBGS(idMukim);
		context.put("selectPremis", list);

		return getPath() + "/findPremis.vm";
	}
	
	@Command("findBangunan")
	public String findBangunan() throws Exception {
		String idPremis = "0";
		if (get("findPremis").trim().length() > 0)
			idPremis = get("findPremis");
		
		List<DevBangunan> list = dataUtil.getListBangunanBGS(idPremis);
		context.put("selectBangunan", list);

		return getPath() + "/findBangunan.vm";
	}
	
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		String idKementerian = "0";
		if (get("idKementerian").trim().length() > 0)
			idKementerian = get("idKementerian");
		
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);

		return getPath() + "/selectAgensi.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0)
			idNegeri = get("idNegeri");
		
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);

		return getPath() + "/selectBandar.vm";
	}
	
	@Command("selectDaerahBangunan")
	public String selectDaerahBangunan() throws Exception {
		String idNegeriBangunan = "0";
		if (get("idNegeriBangunan").trim().length() > 0)
			idNegeriBangunan = get("idNegeriBangunan");
		
		List<Daerah> list = dataUtil.getListDaerah(idNegeriBangunan);
		context.put("selectDaerahBangunan", list);

		return getPath() + "/selectDaerahBangunan.vm";
	}
	
	@Command("selectMukimBangunan")
	public String selectMukimBangunan() throws Exception {
		String idDaerahBangunan = "0";
		if (get("idDaerahBangunan").trim().length() > 0)
			idDaerahBangunan = get("idDaerahBangunan");
		
		List<Mukim> list = dataUtil.getListMukim(idDaerahBangunan);
		context.put("selectMukimBangunan", list);

		return getPath() + "/selectMukimBangunan.vm";
	}
	
	@Command("selectPremis")
	public String selectPremis() throws Exception {
		String idMukimBangunan = "0";
		if (get("idMukimBangunan").trim().length() > 0)
			idMukimBangunan = get("idMukimBangunan");
		
		List<DevPremis> list = dataUtil.getListPremisBGS(idMukimBangunan);
		context.put("selectPremis", list);

		return getPath() + "/selectPremis.vm";
	}
	
	@Command("selectBangunan")
	public String selectBangunan() throws Exception {
		String idPremis = "0";
		if (get("idPremis").trim().length() > 0)
			idPremis = get("idPremis");
		
		List<DevBangunan> list = dataUtil.getListBangunanBGS(idPremis);
		context.put("selectBangunan", list);

		return getPath() + "/selectBangunan.vm";
	}
	/** END DROP DOWN **/
}

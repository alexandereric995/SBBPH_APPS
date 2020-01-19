package bph.utils;

import java.util.List;
import java.util.Vector;

import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.Daerah;
import bph.entities.kod.Gelaran;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisBangunan;
import bph.entities.kod.JenisBayaran;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodJenisPerolehan;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.entities.kod.Status;
import bph.entities.pembangunan.DevAras;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevRuang;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppUnit;

public class HTML {
	
	static Logger myLogger = Logger.getLogger(HTML.class);
	private static DataUtil dataUtil;
	protected static DbPersistence dbp;
	
//	public static String SelectKategoriBayaranBil(String selectName, String selectedValue,
//			String disability, String jsFunction) throws Exception {
//		StringBuffer sb = new StringBuffer("");
//		try {
//			sb.append("<select name='" + selectName + "'");
//			if (disability != null)
//				sb.append(disability);
//			if (jsFunction != null)
//				sb.append(jsFunction);
//			sb.append(" > ");
//			
//			sb.append("<option value=>Sila Pilih Kategori</option>\n");
//			String s = "";
//			
//			for (KategoriBayaranBilEnum kategoriBayaran : KategoriBayaranBilEnum.values()){
//				if (kategoriBayaran.toString().equalsIgnoreCase(selectedValue)) {
//					s = "selected";
//				} else {
//					s = "";
//				}
//				
//				sb.append("<option " + s + " value='"+kategoriBayaran+"'> "+kategoriBayaran.getCatValue()+" </option>\n");
//			}
//			
//			sb.append("</select>");
//			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			throw ex;
//		}
//
//		return sb.toString();
//	}
	
	
	/** KEWANGAN - KOD HASIL*/
	@SuppressWarnings("rawtypes")
	public static String SelectKodHasil(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			Vector v = DB.getRujKodHasil();
			KodHasil f = null;
			String s = "";
			for (int i = 0; i < v.size(); i++) {
				f = (KodHasil) v.get(i);
				if (f.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + f.getId() + ">"
						+ f.getKod() + " - " + f.getKeterangan()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return sb.toString();

	}
	
	/** KEWANGAN - JENIS BAYARAN */
	@SuppressWarnings("rawtypes")
	public static String SelectJenisBayaran(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			Vector v = DB.getRujJenisBayaran();
			JenisBayaran f = null;
			String s = "";
			for (int i = 0; i < v.size(); i++) {
				f = (JenisBayaran) v.get(i);
				if (f.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + f.getId() + ">"
						+ f.getId() + " - " + f.getKeterangan()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	
	/** KEWANGAN - JENIS PEROLEHAN */
	@SuppressWarnings("rawtypes")
	public static String SelectJenisPerolehan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			Vector v = DB.getRujJenisPerolehan();
			KodJenisPerolehan f = null;
			String s = "";
			for (int i = 0; i < v.size(); i++) {
				f = (KodJenisPerolehan) v.get(i);
				if (f.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + f.getId() + ">"
						+ f.getKod() + " - " + f.getKeterangan()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String SelectPegawaiRp(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			Vector v = DB.getPegawaiRp();
			Users f = null;
			String s = "";
			for (int i = 0; i < v.size(); i++) {
				f = (Users) v.get(i);
				if (f.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + f.getId() + ">"
						+ f.getUserName().toUpperCase() +""+ (f.getJawatan()!=null?" - "+f.getJawatan().getKeterangan().toUpperCase():"")
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String SelectPegawaiSemakRp(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			Vector v = DB.getPegawaiSemakRp();
			Users f = null;
			String s = "";
			for (int i = 0; i < v.size(); i++) {
				f = (Users) v.get(i);
				if (f.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + f.getId() + ">"
						+ f.getUserName().toUpperCase() +""+ (f.getJawatan()!=null?" - "+f.getJawatan().getKeterangan():"")
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String SelectPegawaiLulusRp(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			Vector v = DB.getPegawaiLulusRp();
			Users f = null;
			String s = "";
			for (int i = 0; i < v.size(); i++) {
				f = (Users) v.get(i);
				if (f.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + f.getId() + ">"
						+ f.getUserName().toUpperCase() +""+ (f.getJawatan()!=null?" - "+f.getJawatan().getKeterangan():"")
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectNegeri(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Negeri> f = dataUtil.getListNegeri();
			Negeri obj = new Negeri();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectDaerah(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Daerah> f = dataUtil.getListDaerah();
			Daerah obj = new Daerah();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectDaerahByNegeri(String idNegeri, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Daerah> f = dataUtil.getListDaerah(idNegeri);
			Daerah obj = new Daerah();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectMukim(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Mukim> f = dataUtil.getListMukim();
			Mukim obj = new Mukim();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectMukimByDaerah(String idDaerah,String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Mukim> f = dataUtil.getListMukim(idDaerah);
			Mukim obj = new Mukim();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectBangunan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<DevBangunan> f = dataUtil.getListBangunan();
			DevBangunan obj = new DevBangunan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getNamaBangunan()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectBangunanByMukim(String idMukim, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<DevBangunan> f = dataUtil.getListBangunan(idMukim);
			DevBangunan obj = new DevBangunan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getNamaBangunan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectAras(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<DevAras> f = dataUtil.getListAras();
			DevAras obj = new DevAras();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKodDAK().toUpperCase() +" - "+obj.getNamaAras().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectArasByBangunan(String idBangunan, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<DevAras> f = dataUtil.getListAras(idBangunan);
			DevAras obj = new DevAras();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKodDAK().toUpperCase() +" - "+obj.getNamaAras().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectRuang(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<DevRuang> f = dataUtil.getListRuang();
			DevRuang obj = new DevRuang();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKodDAK().toUpperCase() +" - "+obj.getNamaRuang().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectRuangByAras(String idAras, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<DevRuang> f = dataUtil.getListRuang(idAras);
			DevRuang obj = new DevRuang();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKodDAK().toUpperCase() +" - "+obj.getNamaRuang().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJenisDokumen(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<JenisDokumen> f = dataUtil.getListJenisDokumen();
			JenisDokumen obj = new JenisDokumen();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectIcPemohonRpp(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListPemohonRpp();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getNoKP()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectPemohonRpp(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListPemohonRpp();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getUserName().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJenisPeranginan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<JenisBangunan> f = dataUtil.getListJenisPeranginan();
			JenisBangunan obj = new JenisBangunan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getId().toUpperCase() +" - "+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJenisPerkhidmatan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<JenisPerkhidmatan> f = dataUtil.getListJenisPerkhidmatan();
			JenisPerkhidmatan obj = new JenisPerkhidmatan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectKelasPeranginan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<KelasKuarters> f = dataUtil.getListKelasPeranginan();
			KelasKuarters obj = new KelasKuarters();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getGredMula().toUpperCase() +" - "+obj.getGredAkhir().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectPeranginanByJenisPeranginan(String jenisPeranginan, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<RppPeranginan> f = dataUtil.getListPeranginanRpp(jenisPeranginan);
			RppPeranginan obj = new RppPeranginan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getNamaPeranginan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJenisUnitByPeranginan(String peranginan, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<JenisUnitRPP> f = dataUtil.getListJenisUnitByPeranginan(peranginan);
			JenisUnitRPP obj = new JenisUnitRPP();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return sb.toString();
	}
	
	public static String SelectGelaran(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Gelaran> f = dataUtil.getListGelaran();
			Gelaran obj = new Gelaran();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJawatan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Jawatan> f = dataUtil.getListJawatan();
			Jawatan obj = new Jawatan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectStatusPermohonan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Status> f = dataUtil.getListStatusPermohonan();
			Status obj = new Status();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectStatusHistory(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Status> f = dataUtil.getListStatusHistory();
			Status obj = new Status();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectStatusPengurusanBilik(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Status> f = dataUtil.getListStatusPengurusanBilik();
			Status obj = new Status();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectPenolongJurutera(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListPenolongJurutera();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getUserName().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectPenolongJuruteraKanan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListPenolongJuruteraKanan();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getUserName().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJurutera(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListJurutera();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getUserName().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectModBayaran(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<CaraBayar> f = dataUtil.getCaraBayar();
			CaraBayar obj = new CaraBayar();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectPegawaiBertugasDeposit(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListPenyediaDeposit();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getUserName().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectPegawaiBertugasSubsidiari(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Users> f = dataUtil.getListPenyediaSubsidiari();
			Users obj = new Users();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getUserName().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectUnitByJenisUnit(String jenisUnit, String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<RppUnit> f = dataUtil.getListUnitByJenisUnit(jenisUnit);
			RppUnit obj = new RppUnit();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getNamaUnit().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return sb.toString();
	}
	
	public static String SelectPeranginan(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<RppPeranginan> f = dataUtil.getListPeranginanRpp();
			RppPeranginan obj = new RppPeranginan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getNamaPeranginan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectStatusNoShow(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<Status> f = dataUtil.getListStatusNowShow();
			Status obj = new Status();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
	public static String SelectJenisPeranginanNoRT(String selectName, String selectedValue, String disability, String jsFunction) throws Exception {
		dataUtil = DataUtil.getInstance(dbp);
		StringBuffer sb = new StringBuffer("");
		try {
			sb.append("<select name='" + selectName + "'");
			if (disability != null)
				sb.append(disability);
			if (jsFunction != null)
				sb.append(" "+jsFunction);
			sb.append(" > ");
			sb.append("<option value=>SILA PILIH</option>\n");
			List<JenisBangunan> f = dataUtil.getListJenisPeranginanNoRT();
			JenisBangunan obj = new JenisBangunan();
			String s = "";
			for (int i = 0; i < f.size(); i++) {
				obj = f.get(i);
				if (obj.getId().equals(selectedValue)) {
					s = "selected";
				} else {
					s = "";
				}
				sb.append("<option " + s + " value=" + obj.getId() + ">"
						+ obj.getId().toUpperCase() +" - "+ obj.getKeterangan().toUpperCase()
						+ "</option>\n");
			}
			sb.append("</select>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return sb.toString();
	}
	
}// close class


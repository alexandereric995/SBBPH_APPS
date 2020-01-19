package bph.portal;

import java.util.List;

import org.apache.log4j.Logger;

import bph.entities.kod.Bahagian;
import bph.entities.kod.Seksyen;
import bph.entities.portal.CmsAkrab;
import bph.entities.portal.CmsAkrabAktiviti;
import bph.entities.portal.CmsAkrabGambar;
import bph.entities.portal.CmsBantuan;
import bph.entities.portal.CmsDirektori;
import bph.entities.portal.CmsGaleri;
import bph.entities.portal.CmsHubungiKami;
import bph.entities.portal.CmsInformasi;
import bph.entities.portal.CmsKeharta;
import bph.entities.portal.CmsKehartaAktiviti;
import bph.entities.portal.CmsKehartaGambarAktiviti;
import bph.entities.portal.CmsMakluman;
import bph.entities.portal.CmsOperatorUnit;
import bph.entities.portal.CmsPautan;
import bph.entities.portal.CmsPengumuman;
import bph.entities.portal.CmsPenyelenggaraanKuarters;
import bph.entities.portal.CmsProfilKorporat;
import bph.entities.portal.CmsPuspanita;
import bph.entities.portal.CmsPuspanitaAktiviti;
import bph.entities.portal.CmsPuspanitaGambarAktiviti;
import bph.entities.portal.CmsRujukan;
import bph.entities.portal.CmsSlideshow;
import bph.entities.portal.CmsSubBantuan;
import bph.entities.portal.CmsSubGaleri;
import bph.entities.portal.CmsSubInformasi;
import bph.entities.portal.CmsSubProfilKorporat;
import bph.entities.portal.CmsSubRujukan;
import bph.entities.portal.CmsWargaBph;
import db.persistence.MyPersistence;

public class PortalUtil {

	static Logger myLogger = Logger.getLogger("PortalUtil");
	
	/****************************** START FUNCTION PENGUMUMAN ********************************/
	public List<CmsPengumuman> getListPengumuman(MyPersistence mp) {
		List<CmsPengumuman> list = null;
		list = mp.list("SELECT x FROM CmsPengumuman x WHERE x.flagAktif = 'Y' ORDER BY x.turutan ASC");
		return list;
	}
	/****************************** END FUNCTION PENGUMUMAN ********************************/
	
	/****************************** START FUNCTION MAKLUMAN ********************************/
	public List<CmsMakluman> getListMakluman(MyPersistence mp) {
		List<CmsMakluman> list = null;
		list = mp.list("SELECT x FROM CmsMakluman x WHERE x.flagAktif = 'Y' ORDER BY x.turutan ASC");
		return list;
	}
	/****************************** END FUNCTION MAKLUMAN ********************************/
	
	/****************************** START FUNCTION PROFILE KORPORAT ********************************/
	public List<CmsProfilKorporat> getListProfilKorporat(MyPersistence mp) {
		List<CmsProfilKorporat> list = null;
		list = mp.list("SELECT x FROM CmsProfilKorporat x WHERE x.flagAktif = 'Y' ORDER BY x.turutan ASC");
		return list;
	}	
	
	public CmsProfilKorporat getProfilKorporat(String idProfilKorporat, MyPersistence mp) {
		CmsProfilKorporat profilKorporat = null;
		profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, idProfilKorporat);
		return profilKorporat;
	}
	
	public List<CmsSubProfilKorporat> getListSubProfilKorporat(String idProfilKorporat, MyPersistence mp) {
		List<CmsSubProfilKorporat> list = null;
		CmsProfilKorporat profilKorporat = (CmsProfilKorporat) mp.find(CmsProfilKorporat.class, idProfilKorporat);
		if (profilKorporat != null) {
			list = mp.list("SELECT x FROM CmsSubProfilKorporat x WHERE x.flagAktif = 'Y' and x.profilKorporat.id = '" + profilKorporat.getId() + "' ORDER BY x.id ASC");
		}
		return list;
	}
	/****************************** END FUNCTION PROFILE KORPORAT ********************************/
	
	/****************************** START FUNCTION RUJUKAN ******************/
	public List<CmsRujukan> getListRujukan(MyPersistence mp) {
		List<CmsRujukan> list = null;
		list = mp.list("SELECT x FROM CmsRujukan x WHERE x.flagAktif = 'Y' ORDER BY x.turutan ASC");
		return list;
	}
	
	public CmsRujukan getRujukan(String idRujukan, MyPersistence mp) {
		CmsRujukan rujukan = null;
		rujukan = (CmsRujukan) mp.find(CmsRujukan.class, idRujukan);
		return rujukan;
	}
	
	public List<CmsSubRujukan> getListSubRujukan(String idRujukan, MyPersistence mp) {
		List<CmsSubRujukan> list = null;
		CmsRujukan rujukan = (CmsRujukan) mp.find(CmsRujukan.class, idRujukan);
		if (rujukan != null) {
			list = mp.list("SELECT x FROM CmsSubRujukan x WHERE x.flagAktif = 'Y' and x.rujukan.id = '" + rujukan.getId() + "' ORDER BY x.id ASC");
		}
		return list;
	}
	/****************************** END FUNCTION MENU RUJUKAN *********************/	
	
	/****************************** START FUNCTION BANTUAN ************************/
	public List<CmsBantuan> getListBantuan(MyPersistence mp) {
		List<CmsBantuan> list = null;
		list = mp.list("SELECT x FROM CmsBantuan x WHERE x.flagAktif = 'Y' ORDER BY x.turutan ASC");
		return list;
	}
	
	public CmsBantuan getBantuan(String idBantuan, MyPersistence mp) {
		CmsBantuan bantuan = null;
		bantuan = (CmsBantuan) mp.find(CmsBantuan.class, idBantuan);
		return bantuan;
	}
	
	public List<CmsSubBantuan> getListSubBantuan(String idBantuan, MyPersistence mp) {
		List<CmsSubBantuan> list = null;
		CmsBantuan bantuan = (CmsBantuan) mp.find(CmsBantuan.class, idBantuan);
		if (bantuan != null) {
			list = mp.list("SELECT x FROM CmsSubBantuan x WHERE x.flagAktif = 'Y' and x.bantuan.id = '" + bantuan.getId() + "' ORDER BY x.id ASC");
		}
		return list;
	}
	/****************************** END FUNCTION BANTUAN ********************************/
	
	/************************************ START LIST SLIDES SHOW *************************************/
	public List<CmsSlideshow> getListSlideShow(MyPersistence mp) {
		List<CmsSlideshow> list = null;
		list = mp.list("SELECT x FROM CmsSlideshow x WHERE x.flagAktif = 'Y' AND x.fileName is not null ORDER BY x.turutan ASC");
		return list;
	}
	/************************************ END LIST SLIDES SHOW *************************************/
	
	/****************************** START INFORMASI ********************************/
	public List<CmsInformasi> getListInformasi(MyPersistence mp) {
		List<CmsInformasi> list = null;
		list = mp.list("SELECT x FROM CmsInformasi x WHERE x.flagAktif = 'Y' ORDER BY x.id ASC");
		return list;
	}
	
	public CmsInformasi getInformasi(String idInformasi, MyPersistence mp) {
		CmsInformasi informasi = null;
		informasi = (CmsInformasi) mp.find(CmsInformasi.class, idInformasi);
		return informasi;
	}
	
	public List<CmsSubInformasi> getListSubInformasi(String idInformasi, MyPersistence mp) {
		List<CmsSubInformasi> list = null;
		CmsInformasi informasi = (CmsInformasi) mp.find(CmsInformasi.class, idInformasi);
		if (informasi != null) {
			list = mp.list("SELECT x FROM CmsSubInformasi x WHERE x.flagAktif = 'Y' and x.informasi.id = '" + informasi.getId() + "' ORDER BY x.id ASC");
		}
		return list;
	}	
	/****************************** END INFORMASI ********************************/
	
	/****************************** START FUNCTION PAUTAN ********************************/
	public List<CmsPautan> getListPautan1(MyPersistence mp) {
		List<CmsPautan> list = null;
		list = mp.list("SELECT x FROM CmsPautan x WHERE x.flagAktif = 'Y' ORDER BY x.id ASC");
		list = list.subList(0, list.size() / 2);
		return list;
	}
	
	public List<CmsPautan> getListPautan2(MyPersistence mp) {
		List<CmsPautan> list = null;
		list = mp.list("SELECT x FROM CmsPautan x WHERE x.flagAktif = 'Y' ORDER BY x.id ASC");
		list = list.subList(list.size() / 2, list.size());
		return list;
	}
	/****************************** END FUNCTION PAUTAN ********************************/
	
	/****************************** START FUNCTION HUBUNGI KAMI ********************************/
	public CmsHubungiKami getHubungiKami(MyPersistence mp) {
		CmsHubungiKami hubungiKami = null;
		hubungiKami = (CmsHubungiKami) mp.get("SELECT x FROM CmsHubungiKami x WHERE x.flagUtama ='Y' ORDER BY x.id ASC");
		return hubungiKami;
	}
	
	public List<CmsHubungiKami> getListHubungiKami(MyPersistence mp) {
		List<CmsHubungiKami> list = null;
		list = mp.list("SELECT x FROM CmsHubungiKami x WHERE x.flagUtama = 'T' ORDER BY x.id ASC");
		return list;
	}
	/****************************** END FUNCTION HUBUNGI KAMI ********************************/
	
	/****************************** START FUNCTION WARGA BPH ********************************/
	public List<CmsWargaBph> getListWargaBph(MyPersistence mp) {
		List<CmsWargaBph> list = null;
		list = mp.list("SELECT x FROM CmsWargaBph x WHERE x.flagAktif = 'Y' ORDER BY x.id ASC");
		return list;
	}
	/****************************** START FUNCTION WARGA BPH ********************************/
	
	/***************************** START FUNCTION DIREKTORI ********************************/
	public List<Bahagian> getListBahagian1(MyPersistence mp) {
		List<Bahagian> list = null;
		list = mp.list("select x from Bahagian x order by x.id asc");
		list = list.subList(0, list.size() / 2);
		return list;
	}
	
	public List<Bahagian> getListBahagian2(MyPersistence mp) {
		List<Bahagian> list = null;
		list = mp.list("select x from Bahagian x order by x.id asc");
		list = list.subList(list.size() / 2, list.size());
		return list;
	}
	
	public Bahagian getBahagian(String idBahagian, MyPersistence mp) {
		Bahagian bahagian = null;
		bahagian = (Bahagian) mp.find(Bahagian.class, idBahagian);
		return bahagian;
	}
	
	public List<CmsDirektori> getListKetuaBahagian(String idBahagian, MyPersistence mp) {
		List<CmsDirektori> list = null;
		list = mp.list("select x from CmsDirektori x WHERE x.bahagian.id = '" + idBahagian + "' and x.seksyen is null and x.flagAktif = 'Y' order by x.turutan asc");
		return list;
	}
	
	public List<Seksyen> getListUnit1(String idBahagian, MyPersistence mp) {
		List<Seksyen> list = null;
		list = mp.list("select x from Seksyen x WHERE x.bahagian.id = '" + idBahagian + "' order by x.id asc");
		list = list.subList(0, list.size() / 2);
		return list;
	}
	
	public List<Seksyen> getListUnit2(String idBahagian, MyPersistence mp) {
		List<Seksyen> list = null;
		list = mp.list("select x from Seksyen x WHERE x.bahagian.id = '" + idBahagian + "' order by x.id asc");
		list = list.subList(list.size() / 2, list.size());
		return list;
	}
	
	public Seksyen getUnit(String idUnit, MyPersistence mp) {
		Seksyen seksyen = null;
		seksyen = (Seksyen) mp.find(Seksyen.class, idUnit);
		return seksyen;
	}
	
	public List<CmsDirektori> getListKetuaUnit(String idUnit, MyPersistence mp) {
		List<CmsDirektori> list = null;
		list = mp.list("select x from CmsDirektori x WHERE x.seksyen.id = '" + idUnit + "' and x.flagKetua = 'Y' and x.flagAktif = 'Y' order by x.turutan asc");
		return list;
	}
	
	public List<CmsDirektori> getListStafUnit(String idUnit, MyPersistence mp) {
		List<CmsDirektori> list = null;
		list = mp.list("select x from CmsDirektori x WHERE x.seksyen.id = '" + idUnit + "' and x.flagKetua = 'T' and x.flagAktif = 'Y' order by x.turutan asc");
		return list;
	}
	/***************************** END FUNCTION DIREKTORI********************************/
	
	/****************************** START FUNCTION GALERI ********************************/
	public List<CmsGaleri> getListGaleriGambar(MyPersistence mp) {
		List<CmsGaleri> list = null;
		list = mp.list("SELECT x FROM CmsGaleri x WHERE x.kategori = '01' and x.flagAktif = 'Y' ORDER BY x.id ASC");
		return list;
	}
	
	public List<CmsSubGaleri> getListSubGaleriGambar(String idGaleri, MyPersistence mp) {
		List<CmsSubGaleri> list = null;
		CmsGaleri galeri = (CmsGaleri) mp.find(CmsGaleri.class, idGaleri);
		if (galeri != null) {
			list = mp.list("SELECT x FROM CmsSubGaleri x WHERE x.galeri.id = '" + galeri.getId() + "' ORDER BY x.id ASC");
		}
		return list;
	}
	
	public List<CmsGaleri> getListGaleriVideo(MyPersistence mp) {
		List<CmsGaleri> list = null;
		list = mp.list("SELECT x FROM CmsGaleri x WHERE x.kategori = '02' and x.flagAktif = 'Y' AND x.fileName is not null ORDER BY x.id ASC");
		return list;
	}
	/****************************** END FUNCTION GALERI ********************************/	
		
	/****************************** START FUNCTION OPERATOR UNIT ********************************/	
	public List<CmsOperatorUnit> getListOperatorUnit(MyPersistence mp) {
		List<CmsOperatorUnit> list = null;
		list = mp.list("SELECT x FROM CmsOperatorUnit x WHERE x.flagAktif = 'Y' ORDER BY x.id ASC");
		return list;
	}
	
	/****************************** END FUNCTION OPERATOR UNIT ********************************/	
	
	/****************************** START FUNCTION KEHARTA ********************************/	
	public List<CmsKeharta> getListKeharta(MyPersistence mp) {
		List<CmsKeharta> list = null;
		list = mp.list("SELECT x FROM CmsKeharta x WHERE x.flagAktif = 'Y' ORDER BY x.tahun DESC");
		return list;
	}	
	
	public CmsKeharta getKeharta(String idKeharta, MyPersistence mp) {
		CmsKeharta keharta = null;
		keharta = (CmsKeharta) mp.find(CmsKeharta.class, idKeharta);
		return keharta;
	}
	
	public List<CmsKehartaAktiviti> getListAktivitiKeharta(String idKeharta, MyPersistence mp) {
		List<CmsKehartaAktiviti> list = null;
		list = mp.list("SELECT x FROM CmsKehartaAktiviti x WHERE x.flagAktif = 'Y' AND x.keharta.id = '" + idKeharta + "' ORDER BY x.tarikhAktiviti ASC");
		return list;
	}	
	
	public CmsKehartaAktiviti getAktivitiKeharta(String idAktiviti, MyPersistence mp) {
		CmsKehartaAktiviti aktiviti = null;
		aktiviti = (CmsKehartaAktiviti) mp.find(CmsKehartaAktiviti.class, idAktiviti);
		return aktiviti;
	}
	
	public List<CmsKehartaGambarAktiviti> getListGambarAktivitiKeharta(String idAktiviti, MyPersistence mp) {
		List<CmsKehartaGambarAktiviti> list = null;
		list = mp.list("SELECT x FROM CmsKehartaGambarAktiviti x WHERE x.aktiviti.id = '" + idAktiviti + "' ORDER BY x.id ASC");
		return list;
	}	
	/****************************** END FUNCTION KEHARTA ********************************/
	
	/****************************** START FUNCTION PUSPANITA ********************************/	
	public List<CmsPuspanita> getListPuspanita(MyPersistence mp) {
		List<CmsPuspanita> list = null;
		list = mp.list("SELECT x FROM CmsPuspanita x WHERE x.flagAktif = 'Y' ORDER BY x.tahun DESC");
		return list;
	}	
	
	public CmsPuspanita getPuspanita(String idPuspanita, MyPersistence mp) {
		CmsPuspanita puspanita = null;
		puspanita = (CmsPuspanita) mp.find(CmsPuspanita.class, idPuspanita);
		return puspanita;
	}
	
	public List<CmsPuspanitaAktiviti> getListAktivitiPuspanita(String idPuspanita, MyPersistence mp) {
		List<CmsPuspanitaAktiviti> list = null;
		list = mp.list("SELECT x FROM CmsPuspanitaAktiviti x WHERE x.flagAktif = 'Y' AND x.puspanita.id = '" + idPuspanita + "' ORDER BY x.tarikhAktiviti ASC");
		return list;
	}	
	
	public CmsPuspanitaAktiviti getAktivitiPuspanita(String idAktiviti, MyPersistence mp) {
		CmsPuspanitaAktiviti aktiviti = null;
		aktiviti = (CmsPuspanitaAktiviti) mp.find(CmsPuspanitaAktiviti.class, idAktiviti);
		return aktiviti;
	}
	
	public List<CmsPuspanitaGambarAktiviti> getListGambarAktivitiPuspanita(String idAktiviti, MyPersistence mp) {
		List<CmsPuspanitaGambarAktiviti> list = null;
		list = mp.list("SELECT x FROM CmsPuspanitaGambarAktiviti x WHERE x.aktiviti.id = '" + idAktiviti + "' ORDER BY x.id ASC");
		return list;
	}	
	/****************************** END FUNCTION PUSPANITA ********************************/
	
	/****************************** START FUNCTION AKRAB ADDBY zulfazdliabuas@gmail.com Date 14/6/2017 ********************************/	
	public List<CmsAkrab> getListAkrab(MyPersistence mp) {
		List<CmsAkrab> list = null;
		list = mp.list("SELECT x FROM CmsAkrab x WHERE x.flagAktif = 'Y' ORDER BY x.turutan ASC");
		return list;
	}	
	
	public CmsAkrab getAkrab(String idAkrab, MyPersistence mp) {
		CmsAkrab akrab = null;
		akrab = (CmsAkrab) mp.find(CmsAkrab.class, idAkrab);
		return akrab;
	}
	
	public List<CmsAkrabAktiviti> getListAktivitiAkrab(String idAkrab, MyPersistence mp) {
		List<CmsAkrabAktiviti> list = null;
		list = mp.list("SELECT x FROM CmsAkrabAktiviti x WHERE x.flagAktif = 'Y' AND x.akrab.id = '" + idAkrab + "' ORDER BY x.tarikhAktiviti ASC");
		return list;
	}	
	
	public CmsAkrabAktiviti getAktivitiAkrab(String idAktiviti, MyPersistence mp) {
		CmsAkrabAktiviti aktiviti = null;
		aktiviti = (CmsAkrabAktiviti) mp.find(CmsAkrabAktiviti.class, idAktiviti);
		return aktiviti;
	}
	
	public List<CmsAkrabGambar> getListGambarAkrab(String idAktiviti, MyPersistence mp) {
		List<CmsAkrabGambar> list = null;
		list = mp.list("SELECT x FROM CmsAkrabGambar x WHERE x.aktiviti.id = '" + idAktiviti + "' ORDER BY x.id ASC");
		return list;
	}	
	/****************************** END FUNCTION AKRAB ADDBY zulfazdliabuas@gmail.com Date 14/6/2017 ********************************/

	/****************************** START FUNCTION PENYELENGGARAAN KUARTERS ********************************/	
	public List<CmsPenyelenggaraanKuarters> getListPenyelenggaraanKuarters(MyPersistence mp) {
		List<CmsPenyelenggaraanKuarters> list = null;
		list = mp.list("SELECT x FROM CmsPenyelenggaraanKuarters x WHERE x.flagAktif = 'Y' ORDER BY x.id ASC");
		return list;
	}	
	
	public CmsPenyelenggaraanKuarters getPenyelenggaraanKuarters(String idKuarters, MyPersistence mp) {
		CmsPenyelenggaraanKuarters kuarters = null;
		kuarters = (CmsPenyelenggaraanKuarters) mp.find(CmsPenyelenggaraanKuarters.class, idKuarters);
		return kuarters;
	}	
}
/****************************** END FUNCTION PENYELENGGARAAN KUARTERS ********************************/	
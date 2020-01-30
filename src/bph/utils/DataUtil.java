package bph.utils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.template.DbPersistence;
import portal.module.entity.CSS;
import portal.module.entity.Module;
import portal.module.entity.Role;
import portal.module.entity.Users;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kod.Agama;
import bph.entities.kod.Agensi;
import bph.entities.kod.AktivitiRpp;
import bph.entities.kod.BadanBerkanun;
import bph.entities.kod.Bahagian;
import bph.entities.kod.Bandar;
import bph.entities.kod.Bangsa;
import bph.entities.kod.Bank;
import bph.entities.kod.BankFPX;
import bph.entities.kod.BlokUtk;
import bph.entities.kod.Bulan;
import bph.entities.kod.CaraBayar;
import bph.entities.kod.CawanganJANM;
import bph.entities.kod.Daerah;
import bph.entities.kod.Etnik;
import bph.entities.kod.Fasa;
import bph.entities.kod.Gelaran;
import bph.entities.kod.GelaranDalamSurat;
import bph.entities.kod.GredJawatan;
import bph.entities.kod.GredKontraktor;
import bph.entities.kod.GredPerkhidmatan;
import bph.entities.kod.JabatanPembayarJANM;
import bph.entities.kod.Jantina;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JawatanAPMM;
import bph.entities.kod.JawatanATM;
import bph.entities.kod.JawatanPDRM;
import bph.entities.kod.JenisAduan;
import bph.entities.kod.JenisBangunan;
import bph.entities.kod.JenisDokumen;
import bph.entities.kod.JenisHakmilik;
import bph.entities.kod.JenisJaminan;
import bph.entities.kod.JenisKediaman;
import bph.entities.kod.JenisKegunaanKuarters;
import bph.entities.kod.JenisKegunaanRuang;
import bph.entities.kod.JenisKenderaanUtk;
import bph.entities.kod.JenisKontrak;
import bph.entities.kod.JenisKuartersUtk;
import bph.entities.kod.JenisOperasiUtk;
import bph.entities.kod.JenisPelanggaranSyaratUtk;
import bph.entities.kod.JenisPengenalan;
import bph.entities.kod.JenisPenolakan;
import bph.entities.kod.JenisPerkhidmatan;
import bph.entities.kod.JenisPermohonanJRP;
import bph.entities.kod.JenisPertukaran;
import bph.entities.kod.JenisRayuan;
import bph.entities.kod.JenisUnitRPP;
import bph.entities.kod.KaedahPerolehan;
import bph.entities.kod.KategoriBidangKontraktor;
import bph.entities.kod.KategoriBil;
import bph.entities.kod.KategoriKontrak;
import bph.entities.kod.KategoriPengguna;
import bph.entities.kod.KategoriTanah;
import bph.entities.kod.KawasanUtk;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.KelasPerkhidmatan;
import bph.entities.kod.Kementerian;
import bph.entities.kod.KesalahanBerat;
import bph.entities.kod.KesalahanHilangKelayakan;
import bph.entities.kod.KodBil;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodJenisPerolehan;
import bph.entities.kod.KodJuruwang;
import bph.entities.kod.KodMesin;
import bph.entities.kod.KodPusatTerima;
import bph.entities.kod.KondisiKuarters;
import bph.entities.kod.LantikanKontrak;
import bph.entities.kod.Lokasi;
import bph.entities.kod.LokasiDibenar;
import bph.entities.kod.LokasiKuarters;
import bph.entities.kod.LokasiPermohonan;
import bph.entities.kod.Lot;
import bph.entities.kod.Luas;
import bph.entities.kod.Mukim;
import bph.entities.kod.Negeri;
import bph.entities.kod.Pegawai;
import bph.entities.kod.PengkhususanBidangKontraktor;
import bph.entities.kod.SebabBertukar;
import bph.entities.kod.SebabHilangKelayakanUtk;
import bph.entities.kod.SebabPenolakan;
import bph.entities.kod.Seksyen;
import bph.entities.kod.Status;
import bph.entities.kod.StatusBonKontrak;
import bph.entities.kod.StatusHakmilik;
import bph.entities.kod.StatusKuarters;
import bph.entities.kod.StatusPekerjaan;
import bph.entities.kod.StatusPerkahwinan;
import bph.entities.kod.StatusPerkhidmatan;
import bph.entities.kod.SubKategoriTanah;
import bph.entities.kod.SumberAduan;
import bph.entities.kod.Urusan;
import bph.entities.kod.UrusanJKPTG;
import bph.entities.kod.Warganegara;
import bph.entities.kod.Zon;
import bph.entities.kod.ZonUtk;
import bph.entities.pembangunan.DevAras;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevHakmilik;
import bph.entities.pembangunan.DevPremis;
import bph.entities.pembangunan.DevRuang;
import bph.entities.pro.ProSenaraiUnit;
import bph.entities.qtr.KuaKuarters;
import bph.entities.rk.RkRuangKomersil;
import bph.entities.rpp.RppKemudahan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppSebabMohonRT;
import bph.entities.rpp.RppTetapanBarangDeposit;
import bph.entities.rpp.RppTetapanCajTambahan;
import bph.entities.rpp.RppUnit;
import bph.entities.senggara.MtnIndenKerja;
import bph.entities.senggara.MtnKontraktor;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
//import bph.entities.kod.BlokUtk;
//import bph.entities.kod.JenisKenderaanUtk;

public class DataUtil {

	private DbPersistence db;
	private static DataUtil instance;

	private DataUtil(DbPersistence db) {
		this.db = db;
	}

	public static DataUtil getInstance(DbPersistence db) {
		if (instance == null) {
			instance = new DataUtil(db);
		}
		return instance;
	}
	
	public List<Role> getListRole() {
		List<Role> list = db.list("select x from Role x where x.name not in ('anon') order by x.name asc");
		return list;
	}
	
	public List<Role> getListRoleAwam() {
		List<Role> list = db.list("select x from Role x where x.kategori = '0' order by x.name asc");
		return list;
	}
	
	public List<Role> getListRoleDalaman() {
		List<Role> list = db.list("select x from Role x where x.kategori = '1' order by x.name asc");
		return list;
	}
	
	public List<Role> getListRoleAgensi() {
		List<Role> list = db.list("select x from Role x where x.kategori = '2' order by x.name asc");
		return list;
	}
	
	public List<CSS> getListCss() {
		List<CSS> list = db.list("select x from CSS x order by x.name asc");
		return list;
	}

	public List<Kementerian> getListKementerian() {
		List<Kementerian> list = db
				.list("select x from Kementerian x where x.flagAktif = 'Y' order by x.keterangan asc");
		return list;
	}

	public List<BadanBerkanun> getListBadanBerkanun() {
		List<BadanBerkanun> list = db
				.list("select x from BadanBerkanun x order by x.keterangan asc");
		return list;
	}

	public List<Luas> getListLuas() {
		List<Luas> list = db
				.list("select x from Luas x order by x.keterangan asc");
		return list;
	}

	/** 
	 * Edit by peje
	 * nak letak luar negara di bawah list dalam senarai negeri.
	 * @return
	 */
	public List<Negeri> getListNegeri() {
		List<Negeri> list = db
				.list("select x from Negeri x where x.id not in (98, 99) order by x.keterangan asc");
		Negeri negeri = db.find(Negeri.class, "98");
		list.add(negeri);
		negeri = db.find(Negeri.class, "99");
		list.add(negeri);
		return list;
	}
	
	public List<LokasiPermohonan> getList6Negeri() {
		List<LokasiPermohonan> list = db
				.list("select x from LokasiPermohonan x order by x.lokasi asc");
		return list;
	}

	public List<Negeri> getListNegeri(String idZon) {
		List<Negeri> list = db.list("select x from Negeri x where x.zon.id = '"
				+ idZon + "' order by x.keterangan asc");
		return list;
	}

	public List<Zon> getListZon() {
		List<Zon> list = db
				.list("select x from Zon x order by x.keterangan asc");
		return list;
	}

	public List<KodBil> getListKodBilByKategoriBil() {
		List<KodBil> list = db
				.list("select x from KodBil x order by x.keterangan asc");
		return list;
	}

	public List<DevHakmilik> getListTanah() {
		List<DevHakmilik> list = db
				.list("select x from DevHakmilik x order by x.noHakmilik asc");
		return list;
	}

	public List<Daerah> getListDaerah() {
		List<Daerah> list = db
				.list("select x from Daerah x order by x.keterangan asc");
		return list;
	}

	public List<Daerah> getListDaerah(String idNegeri) {
		List<Daerah> list = db
				.list("select x from Daerah x where x.negeri.id = '" + idNegeri
						+ "' order by x.keterangan asc");
		return list;
	}

	public List<Mukim> getListMukim() {
		List<Mukim> list = db
				.list("select x from Mukim x order by x.keterangan asc");
		return list;
	}

	public List<Mukim> getListMukim(String idDaerah) {
		List<Mukim> list = db
				.list("select x from Mukim x where x.daerah.id = '" + idDaerah
						+ "' order by x.keterangan asc");
		return list;
	}

	public List<JenisHakmilik> getListJenisHakMilik() {
		List<JenisHakmilik> list = db
				.list("select x from JenisHakmilik x order by x.keterangan asc");
		return list;
	}

	public List<StatusHakmilik> getListStatusHakMilik() {
		List<StatusHakmilik> list = db
				.list("select x from StatusHakMilik x order by x.keterangan asc");
		return list;
	}
	
	public List<Bahagian> getListBahagian() {
		List<Bahagian> list = db
				.list("select x from Bahagian x WHERE x.flagAktif ='Y'  order by x.keterangan asc");
		return list;
	}

	public List<Seksyen> getListSeksyen() {
		List<Seksyen> list = db
				.list("select x from Seksyen x order by x.keterangan asc");
		return list;
	}
	
	public List<ProSenaraiUnit> getListProSenaraiUnit() {
		List<ProSenaraiUnit> list = db
				.list("select x from ProSenaraiUnit x order by x.keterangan asc");
		return list;
	}
	
	public List<Seksyen> getListSeksyen(String idBahagian) {
		List<Seksyen> list = db
				.list("select x from Seksyen x where x.bahagian.id = '" + idBahagian + "' order by x.keterangan asc");
		return list;
	}

	public List<Gelaran> getListGelaran() {
		List<Gelaran> list = db
				.list("select x from Gelaran x order by x.keterangan asc");
		return list;
	}
	
		public List<Gelaran> getListGelaranPeribadi() {
		List<Gelaran> list = db
				.list("select x from Gelaran x where x.id in ('001','002','003','004','005','007','013','016','018','021','022','023','027','028','029','031','034','037','040','041','044','045','046','047','049','050','051','052','053','055','059','060','061','067','069','070','071','072','073','074','075','076','077','078','079','080','081','082','083','084','085','086','088','091','094','095','096','100','101','103','104','105','106','107','111','114','116','119','122','123','127','129','130','131','132','134','135')");
		return list;
	}

	public List<GelaranDalamSurat> getListGelaranDalamSurat() {
		List<GelaranDalamSurat> list = db
				.list("select x from GelaranDalamSurat x order by x.keterangan asc");
		return list;
	}

	public List<Warganegara> getListWarganegara() {
		List<Warganegara> list = db
				.list("select x from Warganegara x order by x.keterangan asc");
		return list;
	}

	public List<Bangsa> getListBangsa() {
		List<Bangsa> list = db
				.list("select x from Bangsa x order by x.turutan asc");
		return list;
	}

	public List<Etnik> getListEtnik() {
		List<Etnik> list = db.list("select x from Etnik x order by x.id asc");
		return list;
	}

	public List<Jantina> getListJantina() {
		List<Jantina> list = db
				.list("select x from Jantina x order by x.keterangan asc");
		return list;
	}

	public List<Agama> getListAgama() {
		List<Agama> list = db.list("select x from Agama x order by x.id asc");
		return list;
	}

	public List<Bandar> getListBandar() {
		List<Bandar> list = db
				.list("select x from Bandar x order by x.keterangan asc");
		return list;
	}

	public List<Bandar> getListBandar(String idNegeri) {
		List<Bandar> list = db
				.list("select x from Bandar x WHERE x.negeri.id = '" + idNegeri
						+ "' order by x.keterangan asc");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Bandar> getListBandarKuarters(String idNegeri) {
		List<Bandar> list = null;
		if(idNegeri.equalsIgnoreCase("14")){
			list = db.list("select x from Bandar x WHERE x.negeri.id = '" + idNegeri
					+ "'and x.id in ('1401','1404','1416') order by x.keterangan asc");
		}else{
			list = db.list("select x from Bandar x WHERE x.negeri.id = '" + idNegeri
							+ "' order by x.keterangan asc");
		}
		return list;
	}

	public List<JenisPengenalan> getListJenisPengenalan() {
		List<JenisPengenalan> list = db
				.list("select x from JenisPengenalan x order by x.keterangan asc");
		return list;
	}

	public List<JenisPengenalan> getListFewJenisPengenalan() {
		List<JenisPengenalan> list = db
				.list("select x from JenisPengenalan x where x.id in ('T','I') order by x.keterangan asc");
		return list;
	}

	public List<JenisPerkhidmatan> getListJenisPerkhidmatan() {
		List<JenisPerkhidmatan> list = db
				.list("select x from JenisPerkhidmatan x order by x.id asc");
		return list;
	}

	public List<Agensi> getListAgensi() {
		List<Agensi> list = db
				.list("select x from Agensi x order by x.keterangan asc");
		return list;
	}

	public List<JenisBangunan> getListJenisBangunan() {
		List<JenisBangunan> list = db
				.list("select x from JenisBangunan x order by x.keterangan asc");
		return list;
	}

	public List<Agensi> getListAgensi(String idKementerian) {
		List<Agensi> list = db
				.list("select x from Agensi x where x.kementerian.id = '"
						+ idKementerian + "' order by x.keterangan asc");
		return list;
	}
	
	public List<SubKategoriTanah> getListSubkategori() {
		List<SubKategoriTanah> list = db
				.list("select x from SubKategoriTanah x order by x.keterangan asc");
		return list;
	}

	public List<SubKategoriTanah> getListSubkategori(String idKategori) {
		List<SubKategoriTanah> list = db
				.list("select x from SubKategoriTanah x where x.kategoriTanah.id = '"
						+ idKategori + "' order by x.keterangan asc");
		return list;
	}

	public List<KategoriTanah> getListKategori() {
		List<KategoriTanah> list = db
				.list("select x from Kategori x order by x.keterangan asc");
		return list;
	}

	public List<Lot> getListLot() {
		List<Lot> list = db
				.list("select x from Lot x order by x.keterangan asc");
		return list;
	}

	public List<Bank> getListBank() {
		List<Bank> list = db
				.list("select x from Bank x order by x.keterangan asc");
		return list;
	}
	
	public List<BankFPX> getListBankFPX() {
		List<BankFPX> list = db
				.list("select x from BankFPX x where x.isActive = 'Y' order by x.displayName asc");
		return list;
	}
	
	public List<KodMesin> getListMesin() {
		List<KodMesin> list = db.list("select x from KodMesin x order by x.kodMesin asc");
		return list;
	}

	public List<CaraBayar> getListCaraBayar() {
		List<CaraBayar> list = db
				.list("select x from CaraBayar x order by x.keterangan asc");
		return list;
	}

	public List<KodPusatTerima> getListPusatTerima() {
		List<KodPusatTerima> list = db
				.list("select x from KodPusatTerima x order by x.kodPusatTerima asc");
		return list;
	}

	public List<KodHasil> getListKodHasil() {
		List<KodHasil> list = db
				.list("select x from KodHasil x order by x.kod asc");
		return list;
	}

	public List<KategoriTanah> getListKategoriTanah() {
		List<KategoriTanah> list = db
				.list("select x from KategoriTanah x order by x.keterangan asc");
		return list;
	}

	public List<Urusan> getListUrusan() {
		List<Urusan> list = db
				.list("select x from Urusan x order by x.keterangan asc");
		return list;
	}

	public List<Module> getListModule(String moduleGroup) {
		List<Module> list = db
				.list("SELECT x FROM Module x WHERE x.moduleGroup LIKE '%"
						+ moduleGroup
						+ "%' ORDER BY x.moduleTitle, x.moduleGroup ASC");
		return list;
	}

	public List<Status> getListStatus() {
		List<Status> list = db
				.list("select x from Status x order by x.keterangan asc");
		return list;
	}
	
	public List<Status> getListStatus(String idSeksyen) {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '" + idSeksyen + "' order by x.turutan asc");
		return list;
	}
	
	public List<Status> getListStatusModulSenggara() {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '03' order by x.turutan asc");
		return list;
	}

	public List<KelasPerkhidmatan> getListKelasPerkhidmatan() {
		List<KelasPerkhidmatan> list = db
				.list("select x from KelasPerkhidmatan x order by x.id asc");
		return list;
	}

	public List<UrusanJKPTG> getListUrusanJKPTG() {
		List<UrusanJKPTG> list = db
				.list("select x from UrusanJKPTG x where x.id in ('931','457','906','882','417','2000','2001') order by x.keterangan asc");
		return list;
	}

	public List<StatusPerkahwinan> getListStatusPerkahwinan() {
		List<StatusPerkahwinan> list = db
				.list("select x from StatusPerkahwinan x order by x.keterangan asc");
		return list;
	}

	public List<Jawatan> getListJawatan() {
		List<Jawatan> list = db
				.list("select x from Jawatan x order by x.keterangan asc");
		return list;
	}

	public List<Jawatan> getListJawatan(String kelas) {
		List<Jawatan> list = db
				.list("select x from Jawatan x where x.kelas = '" + kelas + "' order by x.keterangan asc");
		return list;
	}
	
	public List<KelasKuarters> getListKelasPeranginan() {
		List<KelasKuarters> list = db
				.list("select x from KelasKuarters x where x.flagAktif = 'Y' order by x.id asc");
		return list;
	}

	public List<SebabBertukar> getListSebabBertukar() {
		List<SebabBertukar> list = db
				.list("select x from SebabBertukar x order by x.id asc");
		return list;
	}

	public List<SebabHilangKelayakanUtk> getListSebabHilangKelayakanUtk() {
		List<SebabHilangKelayakanUtk> list = db
				.list("select x from SebabHilangKelayakanUtk x order by x.id asc");
		return list;
	}
	
	public List<LokasiPermohonan> getListLokasiPermohonan() {
		List<LokasiPermohonan> list = db
				.list("select x from LokasiPermohonan x where x.id in ('01','02','03','04','06') order by x.lokasi asc");
		return list;
	}

	public List<JenisKediaman> getListJenisKediaman() {
		List<JenisKediaman> list = db
				.list("select x from JenisKediaman x order by x.keterangan asc");
		return list;
	}

	public List<LokasiDibenar> getListLokasiDibenar() {
		List<LokasiDibenar> list = db
				.list("select x from LokasiDibenar x order by x.keterangan asc");
		return list;
	}

	public List<KondisiKuarters> getListKondisiKuarters() {
		List<KondisiKuarters> list = db
				.list("select x from KondisiKuarters x order by x.keterangan asc");
		return list;
	}

	public List<LokasiKuarters> getListLokasiKuarters() {
		List<LokasiKuarters> list = db
				.list("select x from LokasiKuarters X order by x.keterangan asc");
		return list;
	}
	
	public List<LokasiKuarters> getListLokasiKuarters(String idNegeri) {
		List<LokasiKuarters> list = db
				.list("select x from LokasiKuarters x WHERE x.lokasi.bandar.negeri.id ='"+idNegeri+"' order by x.keterangan asc");
		return list;
	}
	
	public List<LokasiKuarters> getListLokasiKuartersByLokasiPermohonan(String idLokasiPermohonan) {
		List<LokasiKuarters> list = db
				.list("select x from LokasiKuarters x WHERE x.lokasi.id ='" + idLokasiPermohonan + "' order by x.keterangan asc");
		return list;
	}

	public List<StatusPerkhidmatan> getListStatusPerkhidmatan() {
		List<StatusPerkhidmatan> list = db
				.list("select x from StatusPerkhidmatan x order by x.id asc");
		return list;
	}

	public List<JenisPertukaran> getListJenisPertukaran() {
		List<JenisPertukaran> list = db
				.list("select x from JenisPertukaran x order by x.id asc");
		return list;
	}

	public List<KesalahanHilangKelayakan> getListKesalahanHilangKelayakan() {
		List<KesalahanHilangKelayakan> list = db
				.list("select x from KesalahanHilangKelayakan x order by x.id asc");
		return list;
	}

	public List<KesalahanBerat> getListKesalahanBerat() {
		List<KesalahanBerat> list = db
				.list("select x from KesalahanBerat x order by x.id asc");
		return list;
	}

	public List<Lokasi> getListLokasi() {
		List<Lokasi> list = db.list("select x from Lokasi x order by x.id asc");
		return list;
	}

	public List<Pegawai> getListPegawai() {
		List<Pegawai> list = db
				.list("select x from Pegawai x order by x.id asc");
		return list;
	}

	public List<Status> getListStatusPermohonanRuangPejabat() {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '06' order by x.turutan asc");
		return list;
	}

	public List<DevBangunan> getListBangunan() {
		List<DevBangunan> list = db
				.list("select x from DevBangunan x order by x.id asc");
		return list;
	}

	public List<DevBangunan> getListBangunan(String idMukim) {
		List<DevBangunan> list = db
				.list("select x from DevBangunan x where x.premis.mukim.id = '"
						+ idMukim + "' order by x.id asc");
		return list;
	}

	public List<DevAras> getListAras() {
		List<DevAras> list = db
				.list("select x from DevAras x order by x.id asc");
		return list;
	}

	public List<DevAras> getListAras(String idBangunan) {
		List<DevAras> list = db
				.list("select x from DevAras x where x.bangunan.id = '"
						+ idBangunan + "' order by x.id asc");
		return list;
	}

	public List<DevRuang> getListRuang() {
		List<DevRuang> list = db
				.list("select x from DevRuang x order by x.id asc");
		return list;
	}

	public List<DevRuang> getListRuang(String idAras) {
		List<DevRuang> list = db
				.list("select x from DevRuang x where x.aras.id = '" + idAras
						+ "' order by x.id asc");
		return list;
	}

	public List<JenisDokumen> getListJenisDokumen() {
		List<JenisDokumen> list = db
				.list("select x from JenisDokumen x order by x.keterangan asc");
		return list;
	}

	public List<JenisDokumen> getListJenisDokumenPermohonanJRP() {
		List<JenisDokumen> list = db
				.list("select x from JenisDokumen x where x.id in ('01','02','03','04','05','06','07','08','09','10','11') order by x.id asc");
		return list;
	}

	public List<JenisPermohonanJRP> getListJenisPermohonanJRP() {
		List<JenisPermohonanJRP> list = db
				.list("select x from JenisPermohonanJRP x order by x.id asc");
		return list;
	}

	public List<JenisPermohonanJRP> getListJenisPermohonanBaruJRP() {
		List<JenisPermohonanJRP> list = db
				.list("select x from JenisPermohonanJRP x where x.id in ('JRP1','JRP2') order by x.id asc");
		return list;
	}

	public List<JenisPermohonanJRP> getListJenisPermohonanLanjutanJRP() {
		List<JenisPermohonanJRP> list = db
				.list("select x from JenisPermohonanJRP x where x.id = 'JRP7' order by x.id asc");
		return list;
	}

	public List<JenisPermohonanJRP> getListJenisPermohonanTambahanJRP() {
		List<JenisPermohonanJRP> list = db
				.list("select x from JenisPermohonanJRP x where x.id in ('JRP3','JRP4') order by x.id asc");
		return list;
	}

	public List<JenisPermohonanJRP> getListJenisPermohonanPindahJRP() {
		List<JenisPermohonanJRP> list = db
				.list("select x from JenisPermohonanJRP x where x.id in ('JRP5','JRP6') order by x.id asc");
		return list;
	}

	public List<Status> getListStatusJawatankuasaRuangPejabat() {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '09' and x.id not in ('1424860634502') order by x.turutan asc");
		return list;
	}

	public List<Users> getListPenyediaJRP() throws Exception {
		Db db1 = null;
		String sql = "";
		List<Users> list = db
				.list("select x from Users x where x.role.name = '(JRP) Penyedia' order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id = '(JRP) Penyedia'";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}

		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public List<Users> getListPemohonRpp() {
		List<Users> list = db
				.list("select x from Users x where x.noKP is not null order by x.noKP asc");
		return list;
	}

	public List<RppPeranginan> getListPeranginanRpp() {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x order by x.jenisPeranginan.id , x.namaPeranginan asc ");
		return list;
	}

	public List<RppPeranginan> getListPeranginanRpp(String jenisPeranginan) {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.jenisPeranginan.id = '"
						+ jenisPeranginan + "' and x.id not in ('11','12','13') order by x.namaPeranginan");
		return list;
	}

	public List<RppPeranginan> getListPeranginanRppLondon(String jenisPeranginan) {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.jenisPeranginan.id = '"
						+ jenisPeranginan + "' and x.kodLokasi='LN' order by x.namaPeranginan");
		return list;
	}
	
//	public List<RppPeranginan> getListPeranginanKelompok() {
//		List<RppPeranginan> list = db
//				.list("select x from RppPeranginan x where x.jenisPeranginan.id in ('RPP','RT') and x.id not in ('3','14','17','18','19','21','22','25') order by x.namaPeranginan");
//		return list;
//	}
	
	public List<RppPeranginan> getListPeranginanKelompok() {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.flagKelompok = 'Y' order by x.namaPeranginan");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<JenisUnitRPP> getListJenisUnitByPeranginan(String peranginan) {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.id = '"+ peranginan+ "' "
						+ " and x.id in (select y.jenisUnit.id from RppUnit y where COALESCE(y.status,'') <> 'RESERVED' ) "
						+ " order by x.keterangan asc");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<JenisUnitRPP> getListAllJenisUnitByPeranginan(String peranginan) {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.id = '"+ peranginan+ "' order by x.keterangan asc");
		return list;
	}

	public List<Status> getListStatusPermohonan() {
		List<Status> list = db.list("select x from Status x where x.id in ('1425259713412','1430809277096','1430809277102','1425259713415') order by x.keterangan asc");
		return list;
	}
	
	//bella
	public List<Status> getListStatusPermohonanLaporanPenggunaan() {
		List<Status> list = db.list("select x from Status x where x.id in ('1435512646303','1425259713412','1430809277096','1430809277102','1425259713415') order by x.keterangan desc");
		return list;
	}

	public List<Status> getListStatusRppBayaranBalik() {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '10' and x.id in ('1425259713427','1425259713430','1425259713433') order by x.turutan asc");
		return list;
	}

	public List<Status> getListStatusHistory() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1430809277099','1425259713418','1425259713424','1425259713430','1425259713421','1433083787409','1435093978588','1435512646303','1688545253001455') order by x.keterangan asc");
		return list;
	}

	public List<JenisBangunan> getListJenisPeranginan() {
		List<JenisBangunan> list = db
				.list("select x from JenisBangunan x where x.id in ('RP','RPP','RT') order by x.keterangan asc");
		return list;
	}

	public List<JenisBangunan> getListJenisPeranginanLondon() {
		List<JenisBangunan> list = db
				.list("select x from JenisBangunan x where x.id in ('RP') order by x.keterangan asc");
		return list;
	}
	
	public List<RppPermohonan> getListNoTempahanRpp() {
		List<RppPermohonan> list = db
				.list("select x from RppPermohonan x where x.noTempahan is not null order by x.noTempahan asc");
		return list;
	}

	public List<RppPermohonan> getListNoTempahanRpp(String idpermohonan) {
		List<RppPermohonan> list = db
				.list("select x from RppPermohonan x where x.id = '"
						+ idpermohonan
						+ "' and x.noTempahan is not null order by x.noTempahan asc");
		return list;
	}

	public List<Status> getListStatusPengurusanBilik() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1425259713415','1425259713421','1425259713424') order by x.keterangan asc");
		return list;
	}

	public List<JawatanAPMM> getListJawatanAPMM() {
		List<JawatanAPMM> list = db
				.list("select x from JawatanAPMM x order by x.id asc");
		return list;
	}

	public List<JawatanPDRM> getListJawtanPDRM() {
		List<JawatanPDRM> list = db
				.list("select x from JawatanPDRM x order by x.id asc");
		return list;
	}

	public List<JawatanATM> getListJawatanATM() {
		List<JawatanATM> list = db
				.list("select x from JawatanATM x order by x.id asc");
		return list;
	}

	public List<GredJawatan> getListGredJawatan() {
		List<GredJawatan> list = db
				.list("select x from GredJawatan x order by x.keterangan asc");
		return list;
	}

	public List<GredPerkhidmatan> getListGredPerkhidmatan() {
		List<GredPerkhidmatan> list = db
				.list("select x from GredPerkhidmatan x where x.flagAktif = 'Y' order by x.id asc");
		return list;
	}

	public List<Status> getListStatusKuarters() {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '02' order by x.turutan asc");
		return list;
	}

	public List<StatusKuarters> getListStatusKuartersBaru() {
		List<StatusKuarters> list = db
				//.list("select x from StatusKuarters x order by x.keterangan asc");
		.list("select x from StatusKuarters x where x.flagAktif = '1' order by x.id asc");
		return list;
	}
	
	public List<StatusKuarters> getListDetailKuarters(String idStatus) {
		if (idStatus.equalsIgnoreCase("AKTIF")){
			idStatus="1";
		}else{
			idStatus="0";
		}
		List<StatusKuarters> list = db
				.list("select x from StatusKuarters x where x.flagAktif = '" + idStatus
						+ "' order by x.id asc");
		return list;
	}

	public List<Users> getListPenolongJurutera() {
		List<Users> list = db
				.list("select x from Users x order by x.userName asc");
		return list;
	}

	public List<Users> getListPenolongJuruteraKanan() {
		List<Users> list = db
				.list("select x from Users x order by x.userName asc");
		return list;
	}

	public List<Users> getListJurutera() {
		List<Users> list = db
				.list("select x from Users x order by x.userName asc");
		return list;
	}

	public List<CaraBayar> getCaraBayar() {
		List<CaraBayar> list = db
				.list("select x from CaraBayar x where x.id not in ('N') order by x.keterangan asc");
		return list;
	}
	
	public List<CaraBayar> getCaraBayarKaunter() {
		List<CaraBayar> list = db
				.list("select x from CaraBayar x where x.id not in ('N','MIGS','FPX') order by x.keterangan asc");
		return list;
	}

	// addbyzul
	public List<KodBil> getListJenisBil() {
		List<KodBil> list = db
				.list("select x from KodBil x order by x.keterangan asc");
		return list;
	}

	// addbyzul
	public List<KodBil> getListKodBilByKategoriBil(String idKategoriBil) {
		List<KodBil> list = db
				.list("select x from KodBil x where x.kategori.id = '"
						+ idKategoriBil + "' order by x.keterangan asc");
		return list;
	}

	// addbyzul
	public List<KategoriBil> getListKategoriBil() {
		List<KategoriBil> list = db
				.list("select x from KategoriBil x order by x.keterangan asc");
		return list;
	}

	// addbyzul
	public List<UtilDewan> getListDewan() {
		List<UtilDewan> list = db.list("select x from UtilDewan x order by x.nama asc");
		return list;
	}
	
	//add by rozai 5/11/2015--asingkan dewan yang tiada gelanggang
	public List<UtilDewan> getListDewanGelanggangPenyedia() {
		List<UtilDewan> list = db
			.list("select x from UtilDewan x where x.lokasi <> 'PRESINT 9' order by x.seq asc");
		return list;
	}
	
	//add by rozai 15/01/2018--asingkan dewan yang tiada gelanggang
		public List<UtilDewan> getListDewanGelanggang() {
			List<UtilDewan> list = db
				.list("select x from UtilDewan x where x.flagAktif ='Y' order by x.seq asc");
			return list;
		}
	
	//add by rozai 15/01/2018--asingkan dewan yang tiada gelanggang dan asingkan futsal
		public List<UtilDewan> getListDewanTanpaGelanggangFutsal() {
			List<UtilDewan> list = db
				.list("select x from UtilDewan x where x.lokasi <> 'PRESINT 9' and x.nama like '%DEWAN%' order by x.seq asc");
			return list;
		}
	
	//add by rozai 5/11/2015--asingkan futsal dari list dewan
	public List<UtilDewan> getListDewanSahaja() {
		List<UtilDewan> list = db
				.list("select x from UtilDewan x where x.nama like '%DEWAN%' order by x.seq asc");
		return list;
	}
	
	//add by rozai 17/7/2017--listkan gelanggang ikut cawangan
	public List<UtilDewan> getListDewanGelanggang(String idCawangan) {
		List<UtilDewan> list = db
			.list("select x from UtilDewan x where x.lokasi <> 'PRESINT 9' and x.kodCawangan.id = '"+idCawangan+"' order by x.seq asc");
		return list;
	}
	
	//add by rozai 17/7/2017--listkan dewan ikut cawangan
	public List<UtilDewan> getListDewanSahaja(String idCawangan) {
		List<UtilDewan> list = null;
		if (idCawangan != null) {
			list = db
					.list("select x from UtilDewan x where x.nama like '%DEWAN%' and x.kodCawangan.id in ("+idCawangan+") order by x.seq asc");
		} else {
			list = db
					.list("select x from UtilDewan x where x.nama like '%DEWAN%' and x.kodCawangan is null order by x.seq asc");
		}		
		return list;
	}

	// Start add byzul - for module kontrak kontraktor
	public List<KaedahPerolehan> getListKaedahPerolehan() {
		List<KaedahPerolehan> list = db
				.list("select x from KaedahPerolehan x order by x.keterangan asc");
		return list;
	}
	
	public List<LantikanKontrak> getListLantikanKontrak(){
		List<LantikanKontrak> list = db.list("select x from LantikanKontrak x order by x.keterangan asc");
		return list;
	}
	
	public List<StatusBonKontrak> getListStatusBonKontrak(){
		List<StatusBonKontrak> list = db.list("select x from StatusBonKontrak x order by x.keterangan asc");
		return list;
	}
	
	public List<KategoriKontrak> getListKategoriKontrak() {
		List<KategoriKontrak> list = db.list("select x from KategoriKontrak x order by x.keterangan asc");
		return list;
	}
	// Edn add byzul - for module kontrak kontraktor

	// add by rozai
	public List<JenisJaminan> getListJenisJaminan() {
		List<JenisJaminan> list = db
				.list("select x from JenisJaminan x order by x.keterangan asc");
		return list;
	}

	public List<JenisKontrak> getListJenisKontrak() {
		List<JenisKontrak> list = db
				.list("select x from JenisKontrak x order by x.keterangan asc");
		return list;
	}

	public List<Users> getListPenyediaDeposit() {
		Db db1 = null;
		String sql = "";
		List<Users> list = db.list("select x from Users x where x.role.name = '(DEPOSIT) Penyedia' order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id = '(DEPOSIT) Penyedia' order by u.user_name asc";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public List<Users> getListPenyediaSubsidiari() {
		
		Db db1 = null;
		String sql = "";
		List<Users> list = db.list("select x from Users x where x.role.name = '(SUBSIDIARI) Penyedia' order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id = '(SUBSIDIARI) Penyedia' order by u.user_name asc";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public List<MtnIndenKerja> getListIndenByLokasi(String lokasi) {
		@SuppressWarnings("unchecked")
		List<MtnIndenKerja> list = db
				.list("select x from MtnIndenKerja x where x.lokasi.id = '"
						+ lokasi + "' order by x.noInden asc");
		return list;
	}

	// get list kontraktor yang ada terlibat dengan inden kerja
	public List<MtnKontraktor> getListKontraktorFromInden() {
		@SuppressWarnings("unchecked")
		List<MtnKontraktor> list = db
				.list("select x from MtnKontraktor x where x.noPendaftaran in (select y.kontraktor.noPendaftaran from MtnIndenKerja y) order by x.namaKontraktor asc");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<DevPremis> getListPremis(String idBandar) {
		List<DevPremis> list = db
				.list("select x from DevPremis x WHERE x.bandar.id = '"
						+ idBandar + "' order by x.namaPremis asc");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Status> getListStatusKeputusanSenaraiHitam() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1428990717389','1428990717392') order by x.keterangan asc");
		return list;
	}

	public List<RppUnit> getListUnitByJenisUnit(String jenisUnit) {
		List<RppUnit> list = db
				.list("select x from RppUnit x where x.jenisUnit.id = '"
						+ jenisUnit + "' order by x.namaUnit asc");
		return list;
	}

	public List<RppKemudahan> getListKemudahan(String idPeranginan) {
		List<RppKemudahan> list = db
				.list("select x from RppKemudahan x where x.peranginan.id = '"
						+ idPeranginan + "' order by x.nama asc");
		return list;
	}

	public List<RppPermohonan> getListPermohonanCheckin(String idPeranginan) {
		List<RppPermohonan> list = db
				.list("select x from RppPermohonan x where x.rppPeranginan.id = '"
						+ idPeranginan
						+ "' and x.status.id = '1425259713421' order by x.noTempahan asc");
		return list;
	}

	public List<GredKontraktor> getListGredKontraktor() {
		List<GredKontraktor> list = db
				.list("select x from GredKontraktor x order by x.id asc");
		return list;
	}

	public List<KategoriBidangKontraktor> getListKategoriBidangKontraktor() {
		List<KategoriBidangKontraktor> list = db
				.list("select x from KategoriBidangKontraktor x order by x.id asc");
		return list;
	}

	public List<PengkhususanBidangKontraktor> getListPengkhususanBidangKontraktor(
			String idKategori) {
		List<PengkhususanBidangKontraktor> list = db
				.list("select x from PengkhususanBidangKontraktor x WHERE x.kategori.id = '"
						+ idKategori + "' order by x.id asc");
		return list;
	}

	public List<AktivitiRpp> getListAktivitiRpp() {
		List<AktivitiRpp> list = db
				.list("select x from AktivitiRpp x where x.id not in ('05') order by x.id asc");
		return list;
	}

	public List<RkRuangKomersil> getListRuangKomersil() {
		List<RkRuangKomersil> list = db
				.list("select x from RkRuangKomersil x order by x.namaRuang asc");
		return list;
	}

	public List<UtilGelanggang> getListGelanggang(String idDewan) {
		List<UtilGelanggang> list = db
				.list("select x from UtilGelanggang x WHERE x.dewan.id = '"
						+ idDewan + "' order by x.nama asc");
		return list;
	}

	public List<KuaKuarters> getListJalanKuarters(String idLokasi) {
		List<KuaKuarters> list = db
				.list("SELECT x FROM KuaKuarters x WHERE x.alamat1 IS NOT NULL AND x.alamat1 <> '' AND x.lokasi.id = '"
						+ idLokasi
						+ "' GROUP BY x.lokasi.id, x.alamat1 ORDER BY x.lokasi.id, x.alamat1 ASC");
		return list;
	}

	public List<StatusPekerjaan> getListStatusPekerjaan() {
		List<StatusPekerjaan> list = db
				.list("select x from StatusPekerjaan x order by x.id asc");
		return list;
	}

	public List<KuaKuarters> getListKapasitiKuarters() {
		List<KuaKuarters> list = db
				.list("SELECT x FROM KuaKuarters x WHERE x.kapasiti <> 0 GROUP BY x.kapasiti ORDER BY x.kapasiti ASC");
		return list;
	}

	/**
	 * Changes by @Mohd Faizal
	 * 01/01/2020
	 * Add kuarters dalam pembaikan untuk ditawarkan
	 */
	public List<KuaKuarters> getListKuartersTawaran(String idLokasi, String kelas1) {
		List<KuaKuarters> list = db
				.list("select x from KuaKuarters x where x.statusKuarters.id in ('03', '02') and x.lokasi.lokasi.id = '"
						+ idLokasi
						+ "' and x.kelas.id = '"
						+ kelas1
						+ "' and x.flagAgihan = '0' order by x.kelas.id desc");
		return list;
	}
	
	/**
	 * Changes by @Mohd Faizal
	 * 01/01/2020
	 * Add kuarters dalam pembaikan untuk ditawarkan
	 */
	public List<KuaKuarters> getListKuartersKLTawaran(String idLokasi) {
		List<KuaKuarters> list = db
				.list("select x from KuaKuarters x where x.statusKuarters.id in ('03', '02') and x.lokasi.lokasi.id = '"
						+ idLokasi
						+ "' and x.flagAgihan = '0' order by x.kelas.id desc");
		return list;
	}
	
	public List<KuaKuarters> getListKuartersBerkongsi(String idLokasi, String kelas1) {
		List<KuaKuarters> list = db
				.list("select x from KuaKuarters x where x.statusKuarters.id = '03' and x.lokasi.lokasi.id = '"
						+ idLokasi
						+ "' and x.kelas.id = '"
						+ kelas1
						+ "' and x.kapasitiSemasa > 0"
						+ " and x.flagAgihan = '0' order by x.kelas.id desc");
		return list;
	}

	public List<KelasKuarters> getListKelasKuarters() {
		List<KelasKuarters> list = db.list("select x from KelasKuarters x where x.flagAktif = 'Y' order by x.id asc");
		return list;
	}
	
	/**
	 * Changes by @Mohd Faizal
	 * 01/01/2020
	 * Add kuarters dalam pembaikan untuk ditawarkan
	 */
	public List<KuaKuarters> getListKuartersTawaran(String idLokasi, String kelas1,
			String kelas2) {
		List<KuaKuarters> list = db
				.list("select x from KuaKuarters x where x.statusKuarters.id in ('03', '02') and x.lokasi.lokasi.id = '"
						+ idLokasi
						+ "' and x.kelas.id in ('"
						+ kelas1
						+ "', '"
						+ kelas2
						+ "') and x.flagAgihan = 0 order by x.kelas.id desc");
		return list;
	}

	public List<KuaKuarters> getListKuarters(String idLokasi,
			String kategoriPenghuni, String kelas1) {
		List<KuaKuarters> list = db
				.list("select x from KuaKuarters x where x.lokasi.lokasi.id = '"
						+ idLokasi
						+ "' and x.kategoriPenghuni = '"
						+ kategoriPenghuni
						+ "' and x.kekosongan = 0 and x.kondisiKuarters.id in ('03','04') and x.kelas.id = '"
						+ kelas1
						+ "' and x.flagAgihan = 0 order by x.kelas.id asc, x.kapasitiSemasa desc, x.kapasiti desc");
		// System.out.println("SELECT STATEMENT " + kelas1 + " | " +
		// kategoriPenghuni +
		// " ::: select x from KuaKuarters x where x.lokasi.lokasi.id = '" +
		// idLokasi + "' and x.kategoriPenghuni = '" + kategoriPenghuni +
		// "' and x.kekosongan = 0 and x.kondisiKuarters.id in ('03','04') and x.kelas.id = '"
		// + kelas1 + "' and x.flagAgihan = 0 order by x.kelas.id asc");
		if (kategoriPenghuni == "BUJANG")
			list = db
					.list("select x from KuaKuarters x where x.lokasi.lokasi.id = '"
							+ idLokasi
							+ "' and x.kategoriPenghuni = '"
							+ kategoriPenghuni
							+ "' and ( x.kekosongan = 0 or x.kekosongan = 1 ) and x.kondisiKuarters.id in ('03','04') and x.kapasiti > x.kapasitiSemasa and x.kelas.id = '"
							+ kelas1
							+ "' and x.flagAgihan = 0 order by x.kelas.id asc, x.kapasitiSemasa desc, x.kapasiti desc");
		// System.out.println("SELECT STATEMENT " + kelas1 + " | " +
		// kategoriPenghuni +
		// " ::: select x from KuaKuarters x where x.lokasi.lokasi.id = '" +
		// idLokasi + "' and x.kategoriPenghuni = '" + kategoriPenghuni +
		// "' and ( x.kekosongan = 0 or x.kekosongan = 1 ) and x.kondisiKuarters.id in ('03','04') and x.kapasiti > x.kapasitiSemasa and x.kelas.id = '"
		// + kelas1 + "' and x.flagAgihan = 0 order by x.kelas.id asc");
		return list;
	}

	public List<KuaKuarters> getListKuarters(String idLokasi,
			String kategoriPenghuni, String kelas1, String kelas2) {
		List<KuaKuarters> list = db
				.list("select x from KuaKuarters x where x.lokasi.lokasi.id = '"
						+ idLokasi
						+ "' and x.kategoriPenghuni = '"
						+ kategoriPenghuni
						+ "' and x.kekosongan = 0 and x.kondisiKuarters.id in ('03','04') and x.kelas.id in ('"
						+ kelas1
						+ "', '"
						+ kelas2
						+ "') and x.flagAgihan = 0 order by x.kelas.id asc, x.kapasitiSemasa desc, x.kapasiti desc");
		// System.out.println("SELECT STATEMENT " + kelas1 + " & " + kelas2 +
		// " | " + kategoriPenghuni +
		// " ::: select x from KuaKuarters x where x.lokasi.lokasi.id = '" +
		// idLokasi + "' and x.kategoriPenghuni = '" + kategoriPenghuni +
		// "' and x.kekosongan = 0 and x.kondisiKuarters.id in ('03','04') and x.kelas.id in ('"
		// + kelas1 + "', '" + kelas2 +
		// "') and x.flagAgihan = 0 order by x.kelas.id asc");
		if (kategoriPenghuni == "BUJANG")
			list = db
					.list("select x from KuaKuarters x where x.lokasi.lokasi.id = '"
							+ idLokasi
							+ "' and x.kategoriPenghuni = '"
							+ kategoriPenghuni
							+ "' and ( x.kekosongan = 0 or x.kekosongan = 1 ) and x.kondisiKuarters.id in ('03','04') and x.kapasiti > x.kapasitiSemasa and x.kelas.id in ('"
							+ kelas1
							+ "', '"
							+ kelas2
							+ "') and x.flagAgihan = 0 order by x.kelas.id asc, x.kapasitiSemasa desc, x.kapasiti desc");
		// System.out.println("SELECT STATEMENT " + kelas1 + " & " + kelas2 +
		// " | " + kategoriPenghuni +
		// " ::: select x from KuaKuarters x where x.lokasi.lokasi.id = '" +
		// idLokasi + "' and x.kategoriPenghuni = '" + kategoriPenghuni +
		// "' and ( x.kekosongan = 0 or x.kekosongan = 1 ) and x.kondisiKuarters.id in ('03','04') and x.kapasiti > x.kapasitiSemasa and x.kelas.id in ('"
		// + kelas1 + "', '" + kelas2 +
		// "') and x.flagAgihan = 0 order by x.kelas.id asc");
		return list;
	}

	public String getKelasDowngrade(String kelas) {
		String kelasDowngrade = "";

		if ("A".equals(kelas)) {
			kelasDowngrade = "";
		} else if ("B".equals(kelas)) {
			kelasDowngrade = "";
		} /*
		 * else if ("C".equals(kelas)) { kelasDowngrade = "C"; }
		 */else if ("D".equals(kelas)) {
			kelasDowngrade = "F";
		} /*
		 * else if ("D1".equals(kelas)) { kelasDowngrade = "F"; } else if
		 * ("D2".equals(kelas)) { kelasDowngrade = "F"; }
		 */else if ("E".equals(kelas)) {
			kelasDowngrade = "F";
		} else  {
			kelasDowngrade = "";
		}
		
		/*els
		
		 * else if ("F".equals(kelas)) { kelasDowngrade = "G"; } else if
		 * ("G".equals(kelas)) { kelasDowngrade = "H"; } else if
		 * ("H".equals(kelas)) { kelasDowngrade = "I"; }
		 */

		return kelasDowngrade;
	}

	public String getKategoriPenghuni(String idPemohon) {
		String kategoriPenghuni = "";

		Users u = db.find(Users.class, idPemohon);

		if ("01".equals(u.getStatusPerkahwinan().getId())) {
			kategoriPenghuni = "BUJANG";
		} else if ("02".equals(u.getStatusPerkahwinan().getId())) {
			kategoriPenghuni = "BERKELUARGA";
		} else if ("03".equals(u.getStatusPerkahwinan().getId())) {
			if (u.getBilAnak() > 0) {
				kategoriPenghuni = "BERKELUARGA";
			} else {
				kategoriPenghuni = "BUJANG";
			}
		} else if ("04".equals(u.getStatusPerkahwinan().getId())) {
			if (u.getBilAnak() > 0) {
				kategoriPenghuni = "BERKELUARGA";
			} else {
				kategoriPenghuni = "BUJANG";
			}
		}

		return kategoriPenghuni;
	}

	public List<JenisPenolakan> getListJenisPenolakan() {
		List<JenisPenolakan> list = db
				.list("select x from JenisPenolakan x order by x.keterangan asc");
		return list;
	}

	public List<SebabPenolakan> getListSebabPenolakan() {
		List<SebabPenolakan> list = db
				.list("select x from SebabPenolakan x order by x.keterangan asc");
		return list;
	}

	public List<JenisKegunaanRuang> getListJenisKegunaanRuang() {
		List<JenisKegunaanRuang> list = db.list("select x from JenisKegunaanRuang x where x.id not in(10) order by x.keterangan asc");
		JenisKegunaanRuang jenisKegunaanRuang = db.find(JenisKegunaanRuang.class, "10");
		if (jenisKegunaanRuang != null) {
			list.add(jenisKegunaanRuang);
		}
		return list;
	}

	public List<RppTetapanCajTambahan> getListCajTambahan() {
		List<RppTetapanCajTambahan> list = db
				.list("select x from RppTetapanCajTambahan x order by x.keterangan asc");
		return list;
	}

	public List<Status> getListStatusNowShow() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1425259713415','1433083787409') order by x.keterangan asc");
		return list;
	}

	public List<JenisDokumen> getListJenisDokumenRppKelompok() {
		List<JenisDokumen> list = db
				.list("select x from JenisDokumen x where x.id in ('01','03','12','11') order by x.id asc");
		return list;
	}

	public List<JenisBangunan> getListJenisPeranginanNoRT() {
		List<JenisBangunan> list = db
				.list("select x from JenisBangunan x where x.id in ('RP','RPP') order by x.keterangan asc");
		return list;
	}

	public List<JenisKegunaanKuarters> getListJenisKegunaanKuarters() {
		List<JenisKegunaanKuarters> list = db
				.list("select x from JenisKegunaanKuarters x where x.flagAktif = 1 order by x.id asc");
		return list;
	}

	public List<JenisRayuan> getListJenisRayuan() {
		List<JenisRayuan> list = db
				.list("select x from JenisRayuan x order by x.id asc");
		return list;
	}

	public List<RppTetapanBarangDeposit> getListBarangDeposit() {
		List<RppTetapanBarangDeposit> list = db
				.list("select x from RppTetapanBarangDeposit x where x.id <> '999' order by x.nama asc");
		return list;
	}

	public List<Bulan> getListBulan() {
		List<Bulan> list = db.list("select x from Bulan x order by x.id * 1 asc ");
		return list;
	}

	public List<RppPeranginan> getListPeranginanByGred(String gred) {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.jenisPeranginan.id in ('RPP','RP') and x.id not in ('11','12','13') order by x.jenisPeranginan.id , x.namaPeranginan asc");
		return list;
	}

	public List<RppPeranginan> getListPeranginanRTByGred(String gred) {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.jenisPeranginan.id in ('RT') and x.id != '17' order by x.namaPeranginan asc");
		return list;
	}

	public List<JenisUnitRPP> getListJenisUnitByGred(String gred) {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.jenisPeranginan.id in ('RPP','RP') order by x.keterangan asc");
		return list;
	}

	public List<JenisUnitRPP> getListJenisUnitRTByGred(String gred) {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.jenisPeranginan.id in ('RT') order by x.keterangan asc");
		return list;
	}

	
	
	//-------------------------------------------START MODULE PENGUATKUASA (NAZIRAN) ---------------------------------
	public List<ZonUtk> getListZonUtk() {
		List<ZonUtk> list = db.list("select x from ZonUtk x");
		return list;
	}

	public List<JenisKuartersUtk> getListJenisKuartersUtk() {
		List<JenisKuartersUtk> list = db
				.list("select x from JenisKuartersUtk x");
		return list;
	}

	public List<KawasanUtk> getListKawasanUtk() {
		List<KawasanUtk> list = db.list("select x from KawasanUtk x");
		return list;
	}

	public List<JenisOperasiUtk> getListJenisOperasiUtk() {
		List<JenisOperasiUtk> list = db.list("select x from JenisOperasiUtk x");
		return list;
	}

	public List<JenisPelanggaranSyaratUtk> getListJenisPelanggaranSyaratUtk() {
		List<JenisPelanggaranSyaratUtk> list = db
				.list("select x from JenisPelanggaranSyaratUtk x");
		return list;
	}

	public List<JenisPelanggaranSyaratUtk> getListJenisPelanggaranSyaratUtk(
			String idKes) {
		List<JenisPelanggaranSyaratUtk> list = db
				.list("select x from JenisPelanggaranSyaratUtk x where x.flagKes='"
						+ idKes + "'");
		return list;
	}
	
	public List<BlokUtk> getListBlokUtk(){
		List<BlokUtk> list = db.list("select x from BlokUtk x");
		return list;
	}
	
	public List<JenisKenderaanUtk> getListJenisKenderaan(){
		List<JenisKenderaanUtk> list = db.list("select x from JenisKenderaanUtk x");
		return list;
	}
	
		public List<Fasa> getListFasaUtk() {
			List<Fasa> list = db.list("select x from Fasa x order by x.id asc");
			return list;
		}
	
	//-------------------------------------------END MODULE PENGUATKUASA (NAZIRAN) ---------------------------------

	public List<JenisUnitRPP> getListJenisUnitByPeranginanDanGred(String idrp,
			String gred) {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.id = '"
						+ idrp
						+ "' AND x.id not in ('31','35') order by x.keterangan asc");
		return list;
	}

	public List<SumberAduan> getListSumberAduan() {
		List<SumberAduan> list = db
				.list("select x from SumberAduan x order by x.keterangan asc");
		return list;
	}

	public List<JenisAduan> getListJenisAduan() {
		List<JenisAduan> list = db
				.list("select x from JenisAduan x order by x.turutan asc");
		return list;
	}

	public List<Status> getListStatusAduan() {
		List<Status> list = db
				.list("select x from Status x where x.seksyen.id = '13' order by x.keterangan asc");
		return list;
	}

	public List<Users> getListPenyediaSeksyen(String idSeksyen) {
		Db db1 = null;
		String sql = "";
		List<Users> list = db
				.list("select x from Users x where x.role.name like '%Penyedia' and x.seksyen.id is not null and x.seksyen.id = '"
						+ idSeksyen + "' group by x.id order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id like '%Penyedia' and u.id_seksyen is not null and u.id_seksyen = '"
					+ idSeksyen + "' group by u.user_login";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}

		} catch (Exception e) {
			System.out.println("error dataUtil.getListPenyediaSeksyen "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public List<Fasa> getListFasa() {
		List<Fasa> list = db.list("select x from Fasa x order by x.keterangan asc");
		return list;
	}

	public List<CaraBayar> getCaraBayarInDailyTransaction(String tarikhPenyataPemungut, String kodPusatTerima) {
		List<CaraBayar> list = null;
		List<KewBayaranResit> listResit = db.list("select x.id from KewBayaranResit x where x.tarikhBayaran = '" + tarikhPenyataPemungut + "' and COALESCE(x.flagVoid,'T') = 'T' and x.kodJuruwang.id is not null and x.kodPusatTerima = '" + kodPusatTerima + "'");
//		List<CaraBayar> list = db.list("select distinct x.modBayaran from KewResitKaedahBayaran x where x.resit.tarikhBayaran = '"+ tarikhPenyataPemungut + "' ");
		if(listResit.size() > 0){
			list = db.list("select distinct x.modBayaran from KewResitKaedahBayaran x where x.resit.id in (select y.id from KewBayaranResit y where y.tarikhBayaran = '" + tarikhPenyataPemungut + "' and COALESCE(y.flagVoid,'T') = 'T' and y.kodJuruwang.id is not null and y.kodPusatTerima = '" + kodPusatTerima + "')");
		}
		return list;
	}

	public List<Users> getListJuruwang() {
		Db db1 = null;
		String sql = "";
		/*List<Users> list = db.list("select x from Users x where (x.role.name = '(HASIL) Juruwang' OR x.role.name = '(HASIL) Penyelia' ) "
						+ // " and x.seksyen.id is not null and x.seksyen.id = '08' "+ 
						" group by x.id order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select distinct u.user_login from users u, user_role ur "
					+ " where u.user_login = ur.user_id "
					+ " and (ur.role_id = '(HASIL) Juruwang' OR ur.role_id = '(HASIL) Penyelia') "
					+ " group by u.user_login order by u.user_name";
			// sql =
			// "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id like '%HASIL%' "
			// + " and u.id_seksyen is not null and u.id_seksyen = '08' "
			// + " group by u.user_login ";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					if (!list.contains(users)) {
						list.add(users);
					}
				}
			}} catch (Exception e) {
			System.out.println("error dataUtil.getListJuruwang " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}*/
		List<Users> list = db.list("select x from Users x where x.id in (select y.juruwang.id from KodJuruwang y) order by x.userName");
			
		return list;
	}

	public List<RppPeranginan> getListPeranginanRppLondon() {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.id in ('11','12','13') order by x.namaPeranginan asc");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<DevPremis> getListPremisBGS(String idMukim) {
		List<DevPremis> list = db.list("select x from DevPremis x WHERE x.mukim.id = '" + idMukim 
				+ "' and x.id in (select y.aras.bangunan.premis.id from BgsAras y) order by x.namaPremis asc");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<DevBangunan> getListBangunanBGS(String idPremis) {
		List<DevBangunan> list = db.list("select x from DevBangunan x WHERE x.premis.id = '" + idPremis
				+ "' and x.premis.id in (select y.aras.bangunan.premis.id from BgsAras y) order by x.namaBangunan asc");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<DevAras> getListArasBGS(String idBangunan) {
		List<DevAras> list = db.list("select x from DevAras x WHERE x.bangunan.id = '" + idBangunan
				+ "' and x.bangunan.premis.id in (select y.aras.bangunan.premis.id from BgsAras y) order by x.namaAras asc");
		return list;
	}

	public List<Users> getListPenjawatAwam() {
		Db db1 = null;
		String sql = "";
		List<Users> list = db
				.list("select x from Users x where x.role.name = '(Awam) Penjawat Awam' order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id = '(Awam) Penjawat Awam' ";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}

		} catch (Exception e) {
			System.out.println("error getting getListPenjawatAwam : "
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}

	public List<KodJuruwang> getListJuruwangReport() {
		List<KodJuruwang> list = db
				.list("select x from KodJuruwang x where x.jawatan = 'JURUWANG' order by x.juruwang.userName");
		return list;
	}
	
	public List<KodJuruwang> getListJuruwangReportByCawangan(String kodPusatTerima) {
		List<KodJuruwang> list = db
				.list("select x from KodJuruwang x where x.jawatan = 'JURUWANG' and x.kodPusatTerima = '" + kodPusatTerima + "' order by x.juruwang.userName");
		return list;
	}
	
	public List<KodPusatTerima> getListPusatTerimaReport() {
		List<KodPusatTerima> list = db
				.list("select x from KodPusatTerima x order by x.kodPusatTerima asc");
		return list;
	}
	
	public List<KodPusatTerima> getListPusatTerimaReportByCawangan(String kodPusatTerima) {
		List<KodPusatTerima> list = db
				.list("select x from KodPusatTerima x where x.kodPusatTerima = '" + kodPusatTerima + "' order by x.kodPusatTerima asc");
		return list;
	}
	
	public List<KodPusatTerima> getListPusatTerimaReportByCawangan(List senaraiPusatTerima) {
		String kodPusatTerima=null;
		kodPusatTerima="(";
		for (int i=0; i < senaraiPusatTerima.size(); i++)
		{
			if(i<senaraiPusatTerima.size()-1){
				kodPusatTerima=kodPusatTerima+"'"+senaraiPusatTerima.get(i).toString()+"',";
			}
			else{
				kodPusatTerima=kodPusatTerima+"'"+senaraiPusatTerima.get(i).toString()+"')";
			}
		}
		List<KodPusatTerima> list = db
				.list("select x from KodPusatTerima x where x.kodPusatTerima in " + kodPusatTerima + " order by x.kodPusatTerima asc");
		return list;
	}

	public List<KodJuruwang> getListPusatTerimaReportByIndividu(String noIC) {
		List<KodJuruwang> list = db
				.list("select x.pusatTerima.kodPusatTerima from KodJuruwang x where x.juruwang.id = '" + noIC + "' and x.jawatan='PENYELIA' order by x.kodPusatTerima asc");
		return list;
	}
	
	public List<KodJuruwang> getListPusatTerimaReportByIndividuJuruwang(String noIC) {
		List<KodJuruwang> list = db
				.list("select x.pusatTerima.kodPusatTerima from KodJuruwang x where x.juruwang.id = '" + noIC + "' and x.jawatan='JURUWANG' order by x.kodPusatTerima asc");
		return list;
	}
	
	public List<RppPeranginan> getListPeranginanRppHaveDeposit() {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.id not in ('3','14','11','12','13') order by x.namaPeranginan asc");
		return list;
	}
	
	public List<Status> getListStatusAgihanDeposit() {
		List<Status> list = db.list("select x from Status x where x.id in ('1436464445665','1436464445668') order by x.keterangan asc");
		return list;
	}
	
	public List<Status> getListStatusLondon() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1425259713412','1430809277099','1430809277102') order by x.keterangan asc");
		return list;
	}
	
	public List<Status> getListStatusKelompok() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1425259713412','1425259713415','1433097397170') order by x.keterangan asc");
		return list;
	}
	
	public List<Status> getListStatusPulanganDeposit() {
		List<Status> list = db
				.list("select x from Status x where x.id in ('1436464445665','1436464445668','1436464445673','1438023402951','1438023402975','1438023402980') order by x.keterangan asc");
		return list;
	}
	
	public List<RppSebabMohonRT> getListRppSebabMohonRT() {
		List<RppSebabMohonRT> list = db
				.list("select x from RppSebabMohonRT x order by x.keterangan asc");
		return list;
	}
	
	public List<KategoriPengguna> getListKategoriPenggunaOnline() {
		List<KategoriPengguna> list = db
				.list("select x from KategoriPengguna x where x.flagPengguna = 'O' and x.id not in ('07') order by x.id asc");
		return list;
	}
	
	public List<RppPeranginan> getListRppByOperator(String listPeranginan) {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.id in (" + listPeranginan + ") "
						+ " and x.id not in ('1','2','4','5','6','7','8','9','10','11','12','13','15','16','17','18','19','20','21','22','25','1439720205023') "
						+ "order by x.namaPeranginan asc");
		return list;
	}
	
	public List<RppPeranginan> getListRppByPenyelia(String listPeranginan) {
		List<RppPeranginan> list = null;
		if(listPeranginan != null){
			list = db.list("select x from RppPeranginan x where x.id in "+listPeranginan+"  order by x.namaPeranginan asc");
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<JenisUnitRPP> getListJenisUnitByRpp(String peranginan) {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.id = '"+ peranginan+ "' order by x.keterangan asc");
		return list;
	}
	
	public List<RppPeranginan> getListRppByOperatorLondon(String listPeranginan) {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.id in (" + listPeranginan + ") "
						+ " and x.id not in ('1','2','3','4','5','6','7','8','9','10','14','15','16','17','18','19','20','21','22','25','1439720205023') "
						+ "order by x.namaPeranginan asc");
		return list;
	}
	
	public List<JenisUnitRPP> getListJenisUnitLondon() {
		List<JenisUnitRPP> list = db
				.list("select x from JenisUnitRPP x where x.peranginan.id in ('11','12','13') order by x.peranginan.namaPeranginan asc ");
		return list;
	}
	
	public List<JenisUnitRPP> getListJenisUnit() {
		List<JenisUnitRPP> list = db.list("select x from JenisUnitRPP x order by x.keterangan asc");
		return list;
	}
	
	public List<RppUnit> getListUnit() {
		List<RppUnit> list = db.list("select x from RppUnit x order by x.namaUnit asc");
		return list;
	}
	
	public List<RppPeranginan> getListPeranginanExceptRppLondon() {
		List<RppPeranginan> list = db
				.list("select x from RppPeranginan x where x.id not in ('11','12','13') order by x.namaPeranginan asc");
		return list;
	}
	
	public List<Users> getListJuruwangKewangan() {
		Db db1 = null;
		String sql = "";
		Users users = null;
		ArrayList<Users> list = new ArrayList<Users>();
		
		try {
			db1 = new Db();
			sql = "select distinct a.user_login "+
					" from users a, user_role b "+
					" where a.user_login = b.user_id "+
					" and ((a.user_role = '(HASIL) Juruwang' OR a.user_role = '(HASIL) Penyelia') OR (b.role_id = '(HASIL) Juruwang' OR b.role_id = '(HASIL) Penyelia')) "+
					" order by a.user_name ";
					//" and a.user_login not in (select c.id_juruwang from ruj_kod_juruwang c where c.flag_juruwang = 'KEWANGAN' ) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null && !list.contains(users) ) {
					list.add(users);
				}
			}

		} catch (Exception e) {
			System.out.println("error dataUtil.getListJuruwangKewangan "+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null){
				db1.close();
			}
		}
		return list;
	}
	
	public List<Users> getListJuruwangPenyeliaIr() {
		Db db1 = null;
		String sql = "";
		Users users = null;
		ArrayList<Users> list = new ArrayList<Users>();
		
		try {
			db1 = new Db();
			sql = "select distinct a.user_login "+
					" from users a, user_role b "+
					" where a.user_login = b.user_id "+
					" and (a.user_role = '(RPP) Penyelia' OR b.role_id = '(RPP) Penyelia') "+
					" order by a.user_name ";
					//" and a.user_login not in (select c.id_juruwang from ruj_kod_juruwang c where c.flag_juruwang = 'IR' ) ";
			
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null && !list.contains(users) ) {
					list.add(users);
				}
			}

		} catch (Exception e) {
			System.out.println("error dataUtil.getListJuruwangPenyeliaIr "+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (db1 != null){
				db1.close();
			}
		}
		return list;
	}
	
	public List<KodJenisPerolehan> getListKodJenisPerolehan() {
		List<KodJenisPerolehan> list = db.list("select x from KodJenisPerolehan x order by x.keterangan asc");
		return list;
	}
	
	public List<Status> getListStatusPerolehan() {
		List<Status> list = db.list("select x from Status x where x.id in ('1422934789302','1422941888314','1422943424799','1422943424802','1422943424808','1422943424811','15027197149467845') order by x.keterangan asc");
		return list;
	}
	
	public List<Status> getListStatusSubsidari() {
		List<Status> list = db.list("select x from Status x where x.id in ('1436510785697','1436510785718','1436510785721','1436510785725','1436510785729','1438023402951','1438023402975') order by x.keterangan asc");
		return list;
	}
	
	public List<CawanganJANM> getListCawanganJANM() {
		List<CawanganJANM> list = db.list("select x from CawanganJANM x order by x.keterangan asc");
		return list;
	}
	
	public List<JabatanPembayarJANM> getListJabatanPembayarJANM() {
		List<JabatanPembayarJANM> list = db.list("select x from JabatanPembayarJANM x order by x.keterangan asc");
		return list;
	}
	
	public List<Users> getListPenyediaKuarters(){
		Db db1 = null;
		String sql = "";
		List<Users> list = db.list("select x from Users x where x.role.name = '(QTR) Penyedia' order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id = '(QTR) Penyedia'";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}
	
	public List<Users> getListPenyemakPembangunan(){
		Db db1 = null;
		String sql = "";
		List<Users> list = db.list("select x from Users x where x.role.name = '(TNH) Penyemak' order by x.userName asc");
		Users users = null;

		try {
			db1 = new Db();
			sql = "select u.user_login from users u, user_role ur where u.user_login = ur.user_id and ur.role_id = '(TNH) Penyemak'";
			ResultSet rs = db1.getStatement().executeQuery(sql);
			while (rs.next()) {
				users = db.find(Users.class, rs.getString("user_login"));
				if (users != null) {
					list.add(users);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (db1 != null)
				db1.close();
		}
		return list;
	}
}

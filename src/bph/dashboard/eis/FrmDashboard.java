package bph.dashboard.eis;

import java.util.List;

import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.pembangunan.DevHakmilik;
import bph.entities.pembangunan.DevPremis;
import bph.entities.qtr.KuaKuarters;
import bph.entities.qtr.KuaPermohonan;
import bph.entities.rpp.RppPeranginan;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilPermohonan;
import bph.entities.utk.UtkKesalahan;
import bph.utils.DataUtil;

public class FrmDashboard extends LebahModule {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {

		String portal_role = (String) request.getSession().getAttribute(
				"_portal_role");
		context.put("portal_role", portal_role);

		lebah.util.Util lebahUtil = new lebah.util.Util();
		context.put("lebahUtil", lebahUtil);

		context.put("path", getPath());
		
		//tanah bangunan
		//context.put("jumHakMilikTanah", NumberFormat.getNumberInstance(Locale.US).format(getJumHakMilikTanah()));
		//context.put("jumDPA", NumberFormat.getNumberInstance(Locale.US).format(getJumDaftarPremis()));
		//context.put("jumDAK", NumberFormat.getNumberInstance(Locale.US).format(getJumlahMohonDewan()));
		
		//naziran
		//context.put("jumKesalahanBerat", NumberFormat.getNumberInstance(Locale.US).format(getJumlahKesalahanBerat()));
		//context.put("jumKesalahanRingan", NumberFormat.getNumberInstance(Locale.US).format(getJumlahKesalahanRingan()));
		
		//kuarters
		//context.put("jumKuartersKosong", NumberFormat.getNumberInstance(Locale.US).format(getJumlahKuartersKosong()));
		//context.put("jumKuartersPenghuni", NumberFormat.getNumberInstance(Locale.US).format(getJumlahKuartersPenghuni()));
		//context.put("jumMohonKuarters", NumberFormat.getNumberInstance(Locale.US).format(getJumlahPermohonanKuarters()));
		//context.put("jumKuartersPenyelenggaraan", NumberFormat.getNumberInstance(Locale.US).format(getJumlahKuartersPenyelenggaraan()));
		
		//dewan gelanggang
		//context.put("jumDewanAktif", NumberFormat.getNumberInstance(Locale.US).format(getJumlahDewanAktif()));
		//context.put("jumGelanggangAktif", NumberFormat.getNumberInstance(Locale.US).format(getJumlahGelanggangAktif()));
		//context.put("jumMohonDewan", NumberFormat.getNumberInstance(Locale.US).format(getJumlahMohonDewan()));
		//context.put("jumMohonGelanggang", NumberFormat.getNumberInstance(Locale.US).format(getJumlahMohonGelanggang()));
		
		//rpp rumah peranginan
		//context.put("jumRppAktif", NumberFormat.getNumberInstance(Locale.US).format(getJumlahRppAktif()));
		//context.put("jumMohonRPP", NumberFormat.getNumberInstance(Locale.US).format(getJumlahMohonDewan()));
		
		//jrp
		//context.put("jumJrpMohonLulus", NumberFormat.getNumberInstance(Locale.US).format(getJumlahJrpLulus()));
		//context.put("jumJrpMohonGagal", NumberFormat.getNumberInstance(Locale.US).format(getJumlahJrpGagal()));
		//context.put("jumJrpMohonTangguh", NumberFormat.getNumberInstance(Locale.US).format(getJumlahJrpTangguh()));
		//context.put("jumJrpMohonBatal", NumberFormat.getNumberInstance(Locale.US).format(getJumlahJrpBatal()));
		//context.put("jumJrpMohonLulus", "0");
		//context.put("jumJrpMohonGagal", "0");
		//context.put("jumJrpMohonTangguh", "0");
		//context.put("jumJrpMohonBatal", "0");
		
		// KEWANGAN
		//context.put("jumKutipanHarianBphKeseluruhan", NumberFormat.getNumberInstance(Locale.US).format(getJumlahKutipanHarianKeseluruhan()));
		
		// SEMENTARA KOSONGKAN
		//context.put("kosong", "0");
		
		return getPath() + "/start.vm";
	}

	private String getPath() {
		
		String portal_role = (String) request.getSession().getAttribute("_portal_role");
		String path="";
		
		if(portal_role.equals("(EIS) Pengguna")){
			path= "bph/dashboard/eis/pengguna";
		}
		
		if(portal_role.equals("(EIS) Kewangan")){
			path= "bph/dashboard/eis/kewangan";
		}
		
		if(portal_role.equals("(EIS) Kuarters")){
			path= "bph/dashboard/eis/kuarters";
		}
		
		if(portal_role.equals("(EIS) Pembangunan")){
			path= "bph/dashboard/eis/pembangunan";
		}
		
		if(portal_role.equals("(EIS) Ruang Pejabat")){
			path= "bph/dashboard/eis/ruang";
		}
		
		if(portal_role.equals("(EIS) Rumah Peranginan")){
			path= "bph/dashboard/eis/peranginan";
		}
		
		return path;
	}

	@SuppressWarnings("unchecked")
	public int getJumHakMilikTanah() {
		int x = 0;
		List<DevHakmilik> kp = null;
		kp = db.list("SELECT p FROM DevHakmilik p"
				+ " WHERE p.id is not null");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumDaftarPremis() {
		int x = 0;
		List<DevPremis> kp = null;
		kp = db.list("SELECT p FROM DevPremis p"
				+ " WHERE p.id is not null");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahKesalahanBerat() {
		int x = 0;
		List<UtkKesalahan> kp = null;
		kp = db.list("SELECT p FROM UtkKesalahan p"
				+ " WHERE p.operasi.jenisPelanggaranSyarat.flagKes='2'");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahKesalahanRingan() {
		int x = 0;
		List<UtkKesalahan> kp = null;
		kp = db.list("SELECT p FROM UtkKesalahan p"
				+ " WHERE p.operasi.jenisPelanggaranSyarat.flagKes='1'");
		x = kp.size();
		return x;	
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahKuartersKosong() {
		int x = 0;
		List<KuaKuarters> kp = null;
		kp = db.list("SELECT p FROM KuaKuarters p"
				+ " WHERE p.kekosongan = '0'");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahKuartersPenghuni() {
		int x = 0;
		List<KuaKuarters> kp = null;
		kp = db.list("SELECT p FROM KuaKuarters p"
				+ " WHERE p.kekosongan = '1'");
		x = kp.size();
		return x;
	}

	@SuppressWarnings("unchecked")
	public int getJumlahPermohonanKuarters() {
		int x = 0;
		List<KuaPermohonan> kp = null;
		kp = db.list("SELECT p FROM KuaPermohonan p"
				+ " WHERE p.status.id = '1419483289678'");
		x = kp.size();
		return x;
	}
	

	@SuppressWarnings("unchecked")
	public int getJumlahKuartersPenyelenggaraan() {
		int x = 0;
		List<KuaKuarters> kp = null;
		kp = db.list("SELECT p FROM KuaKuarters p"
				+ " WHERE p.statusKuarters.id = '02'");
		x = kp.size();
		return x;
	}
	
	
	@SuppressWarnings("unchecked")
	public int getJumlahDewanAktif() {
		int x = 0;
		List<UtilDewan> kp = null;
		kp = db.list("SELECT p FROM UtilDewan p"
				+ " WHERE p.status = '1'");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahGelanggangAktif() {
		int x = 0;
		List<UtilGelanggang> kp = null;
		kp = db.list("SELECT p FROM UtilGelanggang p"
				+ " WHERE p.status = '1'");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahMohonDewan() {
		int x = 0;
		List<UtilPermohonan> kp = null;
		kp = db.list("SELECT p FROM UtilPermohonan p"
				+ " WHERE p.statusAktif = '1'"
				+ " AND p.gelanggang.id is null");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahMohonGelanggang() {
		int x = 0;
		List<UtilPermohonan> kp = null;
		kp = db.list("SELECT p FROM UtilPermohonan p"
				+ " WHERE p.statusAktif = '1'"
				+ " AND p.gelanggang.id is not null");
		x = kp.size();
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public int getJumlahRppAktif() {
		int x = 0;
		List<RppPeranginan> kp = null;
		kp = db.list("SELECT p FROM RppPeranginan p"
				+ " WHERE p.id is not null");
		x = kp.size();
		return x;
	}
	
	// add bymatjuju
	@SuppressWarnings("unchecked")
	public double getJumlahKutipanHarianKeseluruhan() {
		double x = 0;
		List<KewBayaranResit> kbr = null;
		kbr = db.list("select p"
				+ " from KewBayaranResit p"
				+ " where p.flagVoid = 'T'");
		for (int y = 0; y < kbr.size(); y++){
			x = x + kbr.get(y).getJumlahAmaunBayaran();
		}
		return x;
	}
	
}

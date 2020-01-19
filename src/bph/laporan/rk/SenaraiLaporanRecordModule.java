package bph.laporan.rk;

import java.util.Date;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiLaporanRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {			
		context.put("path", getPath());	
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/laporan/rk";
	}	
	
	@Command("kembali")
	public String kembali() {		
		context.put("path", getPath());		
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/senaraiLaporan.vm";
	}
	
	@Command("laporanSenaraiRuangKomersil")
	public String laporanSenaraiRuangKomersil() {
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectJenisKegunaanRuang", dataUtil.getListJenisKegunaanRuang());
		
		context.put("path", getPath());		
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/laporanSenaraiRuangKomersil.vm";
	}
	
	@Command("laporanSenaraiPenyewaanAktif")
	public String laporanSenaraiPenyewaanAktif() {	
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		context.put("path", getPath());		
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/laporanSenaraiPenyewaanAktif.vm";
	}
	
	@Command("laporanSenaraiPenyewaanAkanTamat")
	public String laporanSenaraiPenyewaanAkanTamat() {		
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		context.put("path", getPath());	
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/laporanSenaraiPenyewaanAkanTamat.vm";
	}
	
	@Command("laporanSenaraiPenyewaanTamat")
	public String laporanSenaraiPenyewaanTamat() {	
		
		context.put("selectNegeri", dataUtil.getListNegeri());
		
		context.put("path", getPath());	
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/laporanSenaraiPenyewaanTamat.vm";
	}	
	
	@Command("laporanKutipanBulanan")
	public String laporanKutipanBulanan() {	
	
		context.put("path", getPath());	
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/laporanKutipanBulanan.vm";
	}
	
	@Command("laporanSenaraiABT")
	public String laporanSenaraiABT() {	
		
		context.put("tarikhHingga", new Date());
		
		context.put("path", getPath());	
		context.put("command", command);
		context.put("userRole", request.getSession().getAttribute("_portal_role"));
		context.put("util", new Util());
		return getPath() + "/laporanSenaraiABT.vm";
	}
}
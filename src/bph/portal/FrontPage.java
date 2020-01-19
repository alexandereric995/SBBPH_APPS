package bph.portal;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class FrontPage extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8689874309992596529L;
	
	@Override
	public String start() {
		// TODO Auto-generated method stub
		context.put("path", getPath() + "/frontPage");
		
		return getPath() + "/frontPage/start.vm";
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/portal";
	}
	
	@Command("getMain")
	public String getMain() {
		context.put("path", getPath() + "/frontPage");
		
		return getPath() + "/frontPage/mainFrame.vm";
	}
	
	@Command("getHome")
	public String getHome() {
		context.put("path", getPath() + "/frontPage");
		
		return getPath() + "/frontPage/main-body.vm";
	}
	
	@Command("getSepintasLalu")
	public String getSepintasLalu() {
		return getPath() + "/aboutUs/sepintasLalu/start.vm";
	}
	
	@Command("getSejarah")
	public String getSejarah() {
		return getPath() + "/aboutUs/sejarah/start.vm";
	}
	
	@Command("getPiagam")
	public String getPiagam() {
		return getPath() + "/aboutUs/piagamPelanggan/piagam/start.vm";
	}
	
	@Command("getPencapaianPiagam")
	public String getPencapaianPiagam() {
		return getPath() + "/aboutUs/piagamPelanggan/pencapaianPiagam/start.vm";
	}
	
	@Command("getCarta")
	public String getCarta() {
		return getPath() + "/organization/carta/start.vm";
	}
	
	@Command("getDirektori")
	public String getDirektori() {
		return getPath() + "/organization/direktori/start.vm";
	}
	
	@Command("getFaqRumahPeranginan")
	public String getFaqRumahPeranginan() {
		return getPath() + "/faq/pengurusan/rumahPeranginan.vm";
	}
	
	@Command("getFaqKuarters")
	public String getFaqKuarters() {
		return getPath() + "/faq/pengurusan/kuarters.vm";
	}
	
	@Command("getFaqRuangPejabat")
	public String getFaqRuangPejabat() {
		return getPath() + "/faq/pengurusan/ruangPejabat.vm";
	}
	
	@Command("getFaqPembangunan")
	public String getFaqPembangunan() {
		return getPath() + "/faq/pembangunan.vm";
	}
	
	@Command("getFaqPerhubunganAwam")
	public String getFaqPerhubunganAwam() {
		return getPath() + "/faq/perhubunganAwam.vm";
	}
	
	@Command("getFaqPenguatkuasaan")
	public String getFaqPenguatkuasaan() {
		return getPath() + "/faq/penguatkuasaan.vm";
	}
	
	@Command("getFaqPembentungan")
	public String getFaqPembentungan() {
		return getPath() + "/faq/utiliti/pembentungan.vm";
	}
	
	@Command("getFaqKebocoranPaip")
	public String getFaqKebocoranPaip() {
		return getPath() + "/faq/utiliti/kebocoranPaip.vm";
	}
	
	@Command("getHubungiKami")
	public String getHubungiKami() {
		return getPath() + "/contactUs/start.vm";
	}
}
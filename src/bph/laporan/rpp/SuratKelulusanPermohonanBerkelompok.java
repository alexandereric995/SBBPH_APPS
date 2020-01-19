package bph.laporan.rpp;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import bph.entities.rpp.RppKelompokSenaraiAktiviti;
import bph.entities.rpp.RppPermohonan;
import bph.laporan.ReportServlet;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SuratKelulusanPermohonanBerkelompok extends ReportServlet{
	
	static Logger myLog = Logger.getLogger(SuratKelulusanPermohonanBerkelompok.class);
	public SuratKelulusanPermohonanBerkelompok() {
		
		
		super.setFolderName("rpp");
		super.setReportName("SuratKelulusanPermohonanBerkelompok");
		
	}

	@SuppressWarnings("rawtypes")
	public void doProcessing(HttpServletRequest request,
			HttpServletResponse response, ServletContext context, Map parameters)
			throws Exception {
		String idPermohonan = (String) parameters.get("idRppPermohonan");
		String flagSewaBot = "T";
		String namaPeranginan = "";
		String noTelefonPeranginan = "";
		String senaraiAktiviti = "";
		Double totalHargaSewaBot = 0D;
		RppPermohonan permohonan = null;
		try {
			MyPersistence mp = new MyPersistence();
			if (idPermohonan != null && idPermohonan != "") {
				permohonan = (RppPermohonan) mp.find(RppPermohonan.class, idPermohonan);
				if (permohonan != null) {
					if (permohonan.getRppPeranginan().getId().equals("4")) // 4 = TASIK KENYIR
						flagSewaBot = "Y";
					namaPeranginan = Util.capitalizedFirstCharacter(permohonan.getRppPeranginan().getNamaPeranginan());
					noTelefonPeranginan = permohonan.getRppPeranginan().getNoTelefon();
					
					//CALCULATE TOTAL HARGA BOT
					int bilBotDewasa = ((permohonan.getBilDewasa()!=null?permohonan.getBilDewasa():0) + (permohonan.getBilTambahanDewasa()!=null?permohonan.getBilTambahanDewasa():0));
					int bilBotKanakKanak = ((permohonan.getBilKanakKanak()!=null?permohonan.getBilKanakKanak():0) + (permohonan.getBilTambahanKanakKanak()!=null?permohonan.getBilTambahanKanakKanak():0));
					int totalHead = (bilBotDewasa + bilBotKanakKanak);
					
					Double totalHargaDewasa = (bilBotDewasa * 10.00);
					Double totalHargaKanakKanak =  (bilBotKanakKanak * 5.00);
					totalHargaSewaBot = (totalHargaDewasa + totalHargaKanakKanak);
					
					if(totalHead <= 3){
						totalHargaSewaBot = 30.00;
					}
					
					List<RppKelompokSenaraiAktiviti> listAktiviti = mp.list("select x from RppKelompokSenaraiAktiviti x where x.permohonan.id = '" + permohonan.getId() + "' order by x.aktiviti.id asc");
					for (int i = 0; i < listAktiviti.size(); i++) {
						RppKelompokSenaraiAktiviti aktiviti = listAktiviti.get(i);
						if (listAktiviti.size() > 1) {
							if (senaraiAktiviti.equals("")) {
								if (aktiviti.getAktiviti().getId().equals("06")) {
									senaraiAktiviti = Util.capitalizedFirstCharacter(aktiviti.getKeteranganAktivitiLain());
								} else {
									senaraiAktiviti = Util.capitalizedFirstCharacter(aktiviti.getAktiviti().getKeterangan());
								}
							} else {
								if (i == (listAktiviti.size() - 1)) {
									if (aktiviti.getAktiviti().getId().equals("06")) {
										senaraiAktiviti = senaraiAktiviti + " dan " + Util.capitalizedFirstCharacter(aktiviti.getKeteranganAktivitiLain());
									} else {
										senaraiAktiviti = senaraiAktiviti + " dan " + Util.capitalizedFirstCharacter(aktiviti.getAktiviti().getKeterangan());
									}
								} else {
									if (aktiviti.getAktiviti().getId().equals("06")) {
										senaraiAktiviti = senaraiAktiviti + " ," + Util.capitalizedFirstCharacter(aktiviti.getKeteranganAktivitiLain());
									} else {
										senaraiAktiviti = senaraiAktiviti + " ," + Util.capitalizedFirstCharacter(aktiviti.getAktiviti().getKeterangan());
									}
								}
							}
						} else {
							if (aktiviti.getAktiviti().getId().equals("06")) {
								senaraiAktiviti = Util.capitalizedFirstCharacter(aktiviti.getKeteranganAktivitiLain());
							} else {
								senaraiAktiviti = Util.capitalizedFirstCharacter(aktiviti.getAktiviti().getKeterangan());
							}
						}
					}
				}				
			}			
		} catch (Exception ex){
			ex.printStackTrace();
		} 
		parameters.put("flagSewaBot", flagSewaBot);
		parameters.put("namaPeranginan", namaPeranginan);
		parameters.put("senaraiAktiviti", senaraiAktiviti);
		parameters.put("noTelefonPeranginan", noTelefonPeranginan);
		parameters.put("totalHargaSewaBot", totalHargaSewaBot);
	}	
}

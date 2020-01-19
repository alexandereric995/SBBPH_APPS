package bph.mail.mailer;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.xml.ws.BindingProvider;

import lebah.mail.MailerDaemon;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.mail.EmailProperty;
import bph.mail.EmailSender;
import bph.mail.GetAttachment;
import bph.utils.Util;

public class RkMailer {
	
	private boolean enableMail = true;
	private static RkMailer instance;
	
	private RkMailer() {		
	}
	
	public static RkMailer get() {
		if ( instance == null ) instance = new RkMailer();
		return instance;
	}
	
	private void sendMail(String to, String subject, String body) {
		//IF SERVER BUKAN LIVE - REDIRECT EMEL KE DEFAULT EMEL 
		if (!ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")) {
			to = "mohdfaizal_hassan@hla-group.com";
		}
		
		if ( enableMail ) {
			if (to != null && to.trim().length() > 0) {
				MailerDaemon.getInstance().addMessage(to, subject, body);
			} else {
				System.out.println("RECEPIENT IS NULL - " + subject);
			}			
		}
	}
	
	public void emelNotisTuntutanBayaran(String idInvois, String idSyarikat, String namaSyarikat, String emel, String keteranganBayaran, String noFail, String noInvois, String tarikhInvois, 
			String tarikhAkhirBayaran, double sewaSemasa, double tunggakanSewa, double jumlahKenaBayar, String bulanSemasa, 
			String bulanTunggakanDari, String bulanTunggakanHingga, int abt, ServletContext context) throws Exception {
		
		if (!ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")) {
			emel = "mohdfaizal_hassan@hla-group.com";
		}
		
		String to = emel;
		String subject = "SBBPH :: NOTIS TUNTUTAN BAYARAN SEWA BAGI RUANG KOMERSIAL BAHAGIAN PENGURUSAN HARTANAH (BPH)";
	
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td colspan='3' valign='top'><strong>Salam Negaraku Sehati Sejiwa.</strong></td></tr>"
				+ "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
		        + "<tr><td colspan='3' valign='top'>YBhg. Dato'/Datin/Tuan/Puan/Encik/Cik,</td></tr>"
		        + "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
				+ "<tr><td colspan='3' valign='top'><strong>NOTIS TUNTUTAN BAYARAN SEWA BAGI RUANG KOMERSIAL BAHAGIAN PENGURUSAN HARTANAH (BPH)</strong></td></tr>"
		        + "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
		        + "<tr><td colspan='3' valign='top'>Dengan segala hormatnya merujuk kepada perkara di atas.</td></tr>"
		        + "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
		        + "<tr><td colspan='3' valign='top'>2. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sukacita bersama-sama ini dikemukakan <strong>NOTIS TUNTUTAN " + keteranganBayaran + "</strong> seperti maklumat di bawah :</td></tr>"
		        + "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>ID Penyewa</td><td width='60%' valign='top'><strong>: " + idSyarikat + "</strong></td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Nama Penyewa</td><td width='60%' valign='top'><strong>: " + namaSyarikat + "</strong></td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>No. Rujukan Fail</td><td width='60%' valign='top'><strong>: " + noFail + "</strong></td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>No. Invois</td><td width='60%' valign='top'><strong>: " + noInvois + "</strong></td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Tarikh Invois</td><td width='60%' valign='top'><strong>: " + tarikhInvois + "</strong></td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Tarikh Akhir Bayaran</td><td width='60%' valign='top'><strong>: " + tarikhAkhirBayaran + "</strong></td></tr>"
		        + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Sewa Bulan Semasa ( " + bulanSemasa + " )</td><td width='60%' valign='top'><strong>: RM " + Util.formatDecimal(sewaSemasa) + "</strong></td></tr>";
		
		if (tunggakanSewa > 0D) {
			if (abt == 1) {
				body = body + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Tunggakan Sewa ( " + bulanTunggakanDari + " )</td><td width='60%' valign='top'><strong>: RM " + Util.formatDecimal(tunggakanSewa) + "</strong></td></tr>";
			} else {
				body = body + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Tunggakan Sewa ( " + bulanTunggakanDari + " - " + bulanTunggakanHingga + " )</td><td width='60%' valign='top'><strong>: RM " + Util.formatDecimal(tunggakanSewa) + "</strong></td></tr>";
			}
		} else if (tunggakanSewa < 0D) {
			body = body + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Lebihan Bayaran </td><td width='60%' valign='top'><strong>: RM " + Util.formatDecimal(tunggakanSewa) + "</strong></td></tr>";
		}
        
		body = body + "<tr><td width='5%' valign='top'>&nbsp;</td><td width='35%' valign='top'>Jumlah Kena Bayar</td><td width='60%' valign='top'><strong>: RM " + Util.formatDecimal(jumlahKenaBayar) + "</strong></td></tr>"
        + "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
        + "<tr><td colspan='3'>3. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Sila jelaskan bayaran sebelum <strong>" + tarikhAkhirBayaran + "</strong> bagi mengelakkan tunggakan.</td></tr>"
		+ "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
		+ "<tr><td colspan='3' valign='top'>Sekian. Terima kasih.</td></tr>"
		+ "<tr><td colspan='3' valign='top'>&nbsp;</td></tr>"
		+ "</table>";		
		
		// ATTACHMENT
		GetAttachment report = new GetAttachment();	
		String baseDir = "";
		String reportDir = "";
		if (context != null) {
			baseDir = context.getRealPath("/img/");
			reportDir = context.getRealPath("/reports/");
		} else {
			String path = ResourceBundle.getBundle("dbconnection").getString("CONTEXT_PATH");
			baseDir = path + "\\img";
			reportDir = path + "\\reports";
		}
		
		String folderName = "rk";
		String fileName = "InvoisSewaBulanan";
		    	
		final Map<String, Object> myMap = new HashMap<String,Object>();
		myMap.put("BaseDir", baseDir);  	
		myMap.put("ReportDir", reportDir);  	
		myMap.put("ID_INVOIS", idInvois);		
		byte[] attachmentBytes = report.getReportBytes(folderName, fileName, context, myMap);
		String namaAttachment = "Notis Tuntutan Bayaran";
		
		String emailType = ResourceBundle.getBundle("dbconnection").getString("emel");
		if ("gmail".equals(emailType)) {
			EmailProperty pro = EmailProperty.getInstance();
			EmailSender email = EmailSender.getInstance();
			email.FROM = pro.getFrom();
			
			if (emel != "" && emel.contains(",")) {
				email.MULTIPLE_RECIEPIENT = emel.split(",");
			} else if (emel != "") {
				email.MULTIPLE_RECIEPIENT = new String[] { emel };
			} else {
				email.MULTIPLE_RECIEPIENT = new String[0];
			}
			
			email.SUBJECT = subject;
			email.MESSAGE = body;
			email.ATTACHMENT_BYTES = new String[1];
			email.ATTACHMENT_BYTES[0] = new String(attachmentBytes, "ISO-8859-1");;
			email.ATTACHMENT_BYTES_NAME = new String[1];
			email.ATTACHMENT_BYTES_NAME[0] = namaAttachment;//letak nama file bersesuaian
			email.sendEmail();
			
		} else {
			BPHServicesImplService service = new BPHServicesImplService();
		      BPHServices bphService = (BPHServices)service.getPort(BPHServices.class);

		      String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");

		      BindingProvider p = (BindingProvider)bphService;
		      p.getRequestContext().put("javax.xml.ws.service.endpoint.address", 
		        WS_URL);

		      bphService.sendEmailWithSingleAttachment(to, "", subject, body, namaAttachment, attachmentBytes);
		}
	}
		
	private String generateFooter(){
		String f = "";
		
		f = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
			+ " <tr><td>&nbsp;</td></tr>"
			+ " <tr><td><font color='#FF0000'>Nota : Emel ini dijana oleh sistem dan tidak perlu dibalas.</font></td></tr>"
			+ " </table>";
		
		return f;
	}
}

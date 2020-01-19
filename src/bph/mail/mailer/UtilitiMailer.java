package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;
import bph.entities.utiliti.UtilPermohonan;
import bph.utils.Util;

public class UtilitiMailer {
	
	private boolean enableMail = true;
	private static UtilitiMailer instance;
	
	private UtilitiMailer() {
		
	}
	
	public static UtilitiMailer get() {
		if ( instance == null ) instance = new UtilitiMailer();
		return instance;
	}
	
	private void sendMail(String to, String subject, String body) {
		//IF SERVER BUKAN LIVE - REDIRECT EMEL KE DEFAULT EMEL 
		if (!ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")) {
			to = "sbbph.bph@gmail.com";
		}
		
		if ( enableMail ) {
			if (to != null && to.trim().length() > 0) {
				MailerDaemon.getInstance().addMessage(to, subject, body);
			} else {
				System.out.println("RECEPIENT IS NULL - " + subject);
			}			
		}
	}
	
	private void sendMailCC(String to, String cc, String subject, String body) {
		//IF SERVER BUKAN LIVE - REDIRECT EMEL KE DEFAULT EMEL 
		if (!ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")) {
			to = "sbbph.bph@gmail.com";
		}
		
		if ( enableMail ) {
			if (to != null && to.trim().length() > 0) {
				MailerDaemon.getInstance().addMessage(to, cc,subject, body + generateFooter());
			} else {
				System.out.println("RECEPIENT IS NULL - " + subject);
			}			
		}
	}
	
	/** NOTIFIKASI SILA BUAT PEMBAYARAN */
	public void notifikasiPembayaranTempahanDewan(String emelto, UtilPermohonan permohonan) {
		String to = emelto;
		String subject = "SBBPH :: NOTIFIKASI PEMBAYARAN TEMPAHAN BAGI " + permohonan.getDewan().getNama();
		
		String noRuj = "";
		if( permohonan != null ){
			noRuj = permohonan.getIdTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Pembayaran bagi tempahan berikut masih <b>BELUM</b> dijelaskan.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : " + noRuj.toUpperCase() + "</td></tr>"
				+ "<tr><td>NAMA DEWAN : " + permohonan.getDewan().getNama().toUpperCase() + "</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : " + Util.getDateTime(permohonan.getTarikhPermohonan(),"dd-MM-yyyy") + "</td></tr>"
				+ "<tr><td>TARIKH TEMPAHAN : " + Util.getDateTime(permohonan.getTarikhMula(),"dd-MM-yyyy") + "</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>SILA JELASKAN PEMBAYARAN DENGAN SEGERA BAGI MENGELAKKAN PERMOHONAN TERBATAL.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMail(to,subject, body);
	}
	
	/** NOTIFIKASI PEMBERITAHUAN TEMPAHAN TELAH DIBATALKAN OLEH SISTEM */
	public void notifikasiPembatalanTempahanDewanTiadaKelulusan(String emelto, UtilPermohonan permohonan) {
		String to = emelto;
		String subject = "SBBPH :: PEMBATALAN TEMPAHAN OLEH SISTEM BAGI " + permohonan.getDewan().getNama();
		
		String noRuj = "";
		if( permohonan != null ){
			noRuj = permohonan.getIdTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa tempahan berikut <b>TELAH DIBATALKAN</b> atas sebab tiada kelulusan dari pegawai.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : " + noRuj.toUpperCase() + "</td></tr>"
				+ "<tr><td>NAMA DEWAN : " + permohonan.getDewan().getNama().toUpperCase() + "</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : " + Util.getDateTime(permohonan.getTarikhPermohonan(),"dd-MM-yyyy") + "</td></tr>"
				+ "<tr><td>TARIKH TEMPAHAN : " + Util.getDateTime(permohonan.getTarikhMula(),"dd-MM-yyyy") + "</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMail(to,subject, body);
	}
	
	/** NOTIFIKASI PEMBERITAHUAN TEMPAHAN TELAH DIBATALKAN OLEH SISTEM */
	public void notifikasiPembatalanTempahanDewan(String emelto, UtilPermohonan permohonan) {
		String to = emelto;
		String subject = "SBBPH :: PEMBATALAN TEMPAHAN OLEH SISTEM BAGI " + permohonan.getDewan().getNama();
		
		String noRuj = "";
		if( permohonan != null ){
			noRuj = permohonan.getIdTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa tempahan berikut <b>TELAH DIBATALKAN</b> atas sebab bayaran tempahan tidak dijelaskan pada tempoh yang diberikan.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : " + noRuj.toUpperCase() + "</td></tr>"
				+ "<tr><td>NAMA DEWAN : " + permohonan.getDewan().getNama().toUpperCase() + "</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : " + Util.getDateTime(permohonan.getTarikhPermohonan(),"dd-MM-yyyy") + "</td></tr>"
				+ "<tr><td>TARIKH TEMPAHAN : " + Util.getDateTime(permohonan.getTarikhMula(),"dd-MM-yyyy") + "</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMail(to,subject, body);
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

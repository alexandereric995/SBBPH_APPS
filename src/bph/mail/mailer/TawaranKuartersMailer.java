package bph.mail.mailer;

import java.util.Date;
import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;
import bph.utils.Util;

public class TawaranKuartersMailer {

	private boolean enableMail = true;
	private static TawaranKuartersMailer instance;
	private Util util;

	private TawaranKuartersMailer() {

	}

	public static TawaranKuartersMailer get() {
		if (instance == null)
			instance = new TawaranKuartersMailer();
		return instance;
	}

	private void sendMail(String to, String subject, String body) {
		//IF SERVER BUKAN LIVE - REDIRECT EMEL KE DEFAULT EMEL 
		if (!ResourceBundle.getBundle("dbconnection").getString("SERVER_DEFINITION").equals("LIVE")) {
			to = "sbbph.bph@gmail.com";
		}
		
		if (enableMail) {
			if (to != null && to.trim().length() > 0) {
				MailerDaemon.getInstance().addMessage(to, subject, body + generateFooter());
			} else {
				System.out.println("RECEPIENT IS NULL - " + subject);
			}					
		}
	}

	@SuppressWarnings("static-access")
	public void emelNotifikasiTawaran(String emel, Date tarikhDaftar) {
		String to = emel;
		String subject = "";

		subject = "SBBPH :: PERMOHONAN KUARTERS TELAH DILULUSKAN";

		String body = "<table width='75%' border='0' cellspacing='0' cellpadding='5'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>PER : <u style='font-weight: bold'>PERMOHONAN KUARTERS TUAN/PUAN TELAH DILULUSKAN</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dengan hormatnya saya merujuk kepada perkara diatas.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dimaklumkan permohonan tuan/puan bertarikh <strong>"
				+ util.getDateTime(tarikhDaftar, "dd MMMM yyyy")
				+ "</strong> telah diluluskan.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Tuan/puan diminta untuk hadir ke <strong><br/>Aras 4, Bahagian Pengurusan Hartanah,<br/>Jabatan Perdana Menteri<br/></strong>bagi mendapatkan dokumen berkaitan untuk dilengkapkan oleh pihak tuan/puan. Tuan/puan juga dikehendaki membawa segala dokumen-dokumen asal semasa urusan dilakukan.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sekian, terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td><strong>&quot;BERKHIDMAT UNTUK NEGARA&quot;</strong></i></td></tr>"
				+ "<tr><td><strong>&quot;BERILTIZAM PENUHI HASRAT&quot;</strong></i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>" + "</table>";

		sendMail(to, subject, body);
	}

	private String generateFooter() {
		String f = "";

		f = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ " <tr><td>&nbsp;</td></tr>"
				+ " <tr><td><font color='#FF0000'>Nota : Emel ini dijana oleh sistem dan tidak perlu dibalas.</font></td></tr>"
				+ " </table>";

		return f;
	}
}

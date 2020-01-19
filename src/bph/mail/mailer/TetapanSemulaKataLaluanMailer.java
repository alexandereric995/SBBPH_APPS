package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;

public class TetapanSemulaKataLaluanMailer {
	
	private boolean enableMail = true;
	private static TetapanSemulaKataLaluanMailer instance;
	
	private TetapanSemulaKataLaluanMailer() {
		
	}
	
	public static TetapanSemulaKataLaluanMailer get() {
		if ( instance == null ) instance = new TetapanSemulaKataLaluanMailer();
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
	
	
	public void hantarPassword(String emel, String idNo, String pass) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Tetapan Semula Kata Laluan";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Akaun anda telah ditetapkan semula.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sila gunakan maklumat dibawah untuk log masuk ke Portal SBBPH.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Log Pengguna : " + idNo + "</td></tr>"
				+ "<tr><td>Kata Laluan Sementara (<i>Dijana oleh sistem</i>) : " + pass + "</td></tr>"			
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sila login melalui <a href=http://www.bph.gov.my>www.bph.gov.my</a> untuk kemaskini kata laluan anda.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
	}
	
	public void hantarSemakan(String emel) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Tetapan Semula Kata Laluan";
	
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
		+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
		+ "<tr><td>&nbsp;</td></tr>"
		+ "<tr><td>Akaun anda masih dalam semakan. Katalaluan akan dihantar melalui e-mel setelah semakan maklumat perjawatan awam / persaraan dilakukan.</td></tr>"
		+ "<tr><td>Semakan ini dibuat dalam tempoh waktu bekerja (Isnin hingga Jumaat) sahaja.</td></tr>"
		+ "<tr><td>Sila semak emel tuan/puan dari masa ke semasa untuk mendapatkan maklumat akaun pendaftaran tuan/puan.</td></tr>"
		+ "<tr><td>Sebarang pertanyaan,sila emel kepada sbbph@bph.gov.my.</td></tr>"
		+ "<tr><td>&nbsp;</td></tr>"
		+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
		+ "<tr><td>&nbsp;</td></tr>"
		+ "</table>";		
		
		sendMail(to, subject, body);
	}
	
	public void hantarPasswordBadanBerkanun(String emel, String idNo, String pass) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Tetapan Semula Kata Laluan";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Akaun anda telah ditetapkan semula.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sila gunakan maklumat dibawah untuk log masuk ke Portal SBBPH.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Log Pengguna : " + idNo + "</td></tr>"
				+ "<tr><td>Kata Laluan Sementara (<i>Dijana oleh sistem</i>) : " + pass + "</td></tr>"			
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sila login melalui <a href=http://www.bph.gov.my>www.bph.gov.my</a> untuk kemaskini kata laluan anda.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Walaubagaimanapun sebarang urusan permohonan tidak dibenarkan setelah semakan surat pengesahan jawatan / gred perkhidmatan dilakukan.</td></tr>"
				+ "<tr><td>Semakan ini dibuat dalam tempoh waktu bekerja (Isnin hingga Jumaat) sahaja.</td></tr>"
				+ "<tr><td>Sebarang pertanyaan,sila emel kepada sbbph@bph.gov.my.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
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

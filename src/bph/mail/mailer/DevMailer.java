package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;

public class DevMailer {
	
	private boolean enableMail = true;
	private static DevMailer instance;
	
	private DevMailer() {
		
	}
	
	public static DevMailer get() {
		if ( instance == null ) instance = new DevMailer();
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
	
	public void hantarSemakan(String emel, String from) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Tugasan Baharu";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
			+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Untuk makluman, satu tugasan baharu telah dihantar oleh " + from + ".</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Sila log masuk ke Portal SBBPH untuk maklumat lanjut.</td></tr>"			
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Sekian, terima kasih.</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
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

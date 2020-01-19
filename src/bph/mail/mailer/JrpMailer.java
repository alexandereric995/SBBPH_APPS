package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;

public class JrpMailer {
	
	private boolean enableMail = true;
	private static JrpMailer instance;
	
	private JrpMailer() {
		
	}
	
	public static JrpMailer get() {
		if ( instance == null ) instance = new JrpMailer();
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
	
	public void hantarKuiri(String emel, String ulasan, String namaPenyedia, String noTelPenyedia) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Permohonan Ruang Pejabat";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
			+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Tuan/Puan,</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Dimaklumkan bahawa permohonan tidak dapat diproses "
			+ "pada masa ini kerana terdapat maklumat yang perlu dikemukakan/dilengkapkan seperti di bawah :</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>" + ulasan + "</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Sila pihak tuan/puan kemukakan semula permohonan yang telah dilengkapkan kepada BPH selaku Urus Setia "
			+ "Jawatankuasa Ruang Pejabat untuk dibawa kepada Pertimbangan Mesyuarat JRP akan datang. Sebarang pertanyaan lanjut, "
			+ "tuan/puan boleh menghubungi Desk Officer berikut :</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>" + namaPenyedia + "</td></tr>"
			+ "<tr><td>" + noTelPenyedia + "</td></tr>"
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

package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;

public class TemujanjiKuartersMailer {
	
	private boolean enableMail = true;
	private static TemujanjiKuartersMailer instance;
	
	private TemujanjiKuartersMailer() {
		
	}
	
	public static TemujanjiKuartersMailer get() {
		if ( instance == null ) instance = new TemujanjiKuartersMailer();
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
		
	public void hantarNotifikasiDaftarTemujanji(String emel, String tarikh, String masa) {
		String to = emel;
		String subject = "";	
		subject = "SBBPH :: Pemakluman Temujanji";	
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dimaklumkan disini bahawa permohonan slot temujanji anda telah berjaya direkodkan seperti berikut:</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Tarikh temujanji	: "+tarikh+"</td></tr>"
				+ "<tr><td>Masa temujanji	: "+masa+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td><b><u>Peringatan</u></b></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>1. Sila bawa bersama dokumen-dokumen berikut semasa urusan temujanji</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td><b>&nbsp;&nbsp;&nbsp;&nbsp;i.Slip gaji terkini;</b></td></tr>"
				+ "<tr><td><b>&nbsp;&nbsp;&nbsp;&nbsp;ii.Borang BPH1/2015 (Pin.2017);</b></td></tr>"
				+ "<tr><td><b>&nbsp;&nbsp;&nbsp;&nbsp;iii.Salinan Kenyataan Perkhidmatan (RS) dari mula masuk kuarters hingga yang terkini;</b></td></tr>"
				+ "<tr><td><b>&nbsp;&nbsp;&nbsp;&nbsp;iv.Sijil tutup TNB(Salinan asal);</b> dan</td></tr>"
				+ "<tr><td><b>&nbsp;&nbsp;&nbsp;&nbsp;v.Sijil tutup SYABAS (Salinan asal).</b></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>Sijil keluar kuarters tidak akan dikeluarkan jika <b>perkara (i)-(v)</b> tidak lengkap.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>2. Sila pastikan kehadiran anda pada waktu yang ditetapkan. Jika ingin membuat pembatalan temujanji, ianya mestilah dilakukan selewat-lewatnya 3 hari sebelum tarikh temujanji diatas.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>3. Temujanji akan terbatal sekiranya penghuni/wakil gagal hadir pada tarikh dan masa yang ditetapkan.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sekian. Terima kasih.</td></tr>"
				+ "</table>";
		
		
		
		sendMail(to, subject, body);
	}
	
	public void hantarNotifikasiUnitKuartersDaftarTemujanji(String nama,String alamat,String noIC,String noTel,String emelKuarters,String tarikh,String masa) {
		String to = emelKuarters;
		String subject = "";	
		subject = "[PERHATIAN] Pemakluman Rekod Temujanji";	
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dimaklumkan bahawa permohonan temujanji telah berjaya direkodkan seperti berikut:</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Tarikh Temujanji	: "+tarikh+"</td></tr>"
				+ "<tr><td>Masa Temujanji	: "+masa+"</td></tr>"
				+ "<tr><td>Nama				: "+nama+"</td></tr>"
				+ "<tr><td>MyID				: "+noIC+"</td></tr>"
				+ "<tr><td>Alamat			: "+alamat+"</td></tr>"
				+ "<tr><td>No.telefon		: "+noTel+"</td></tr>"
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

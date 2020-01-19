package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;
import lebah.util.Util;
import portal.module.entity.Users;

public class DaftarAkaunBaruMailer {
	
	private boolean enableMail = true;
	private static DaftarAkaunBaruMailer instance;
	
	private DaftarAkaunBaruMailer() {
		
	}
	
	public static DaftarAkaunBaruMailer get() {
		if ( instance == null ) instance = new DaftarAkaunBaruMailer();
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
	
	public void daftarBaru(String emel) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
	
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
		+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
		+ "<tr><td>&nbsp;</td></tr>"
		+ "<tr><td>Akaun telah berjaya didaftarkan. Katalaluan akan dihantar melalui e-mel setelah semakan maklumat perjawatan awam / persaraan dilakukan.</td></tr>"
		+ "<tr><td>Semakan ini dibuat dalam tempoh waktu bekerja (Isnin hingga Jumaat) sahaja.</td></tr>"
		+ "<tr><td>Sila semak emel tuan/puan dari masa ke semasa untuk mendapatkan maklumat akaun pendaftaran tuan/puan.</td></tr>"
		+ "<tr><td>Sebarang pertanyaan,sila emel kepada sbbph@bph.gov.my.</td></tr>"
		+ "<tr><td>&nbsp;</td></tr>"
		+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
		+ "<tr><td>&nbsp;</td></tr>"
		+ "</table>";		
		
		sendMail(to, subject, body);
	}
		
	public void hantarPassword(String emel, String idNo, String pass) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Akaun telah berjaya didaftarkan.</td></tr>"
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
	
	public void hantarPasswordBadanBerkanun(String emel, String idNo, String pass) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Akaun telah berjaya didaftarkan.</td></tr>"
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
	
	public void kelulusanPengesahanPengguna(Users pengguna, String emel) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pengesahan Maklumat Perjawatan / Perkhidmatan";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sukacita dimaklumkan bahawa maklumat perjawatan / perkhidmatan telah disahkan.</td></tr>"
				+ "<tr><td>Tarikh luput pengesahan ini adalah pada <b>" + Util.getDateTime(pengguna.getTarikhLuputPengesahan(), "dd-MM-yyyy") + "</b>.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Catatan Pengesahan : " + pengguna.getCatatanPengesahan() + "</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
	}
	
	public void penolakanPengesahanPengguna(Users pengguna, String emel) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pengesahan Maklumat Perjawatan / Perkhidmatan";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dukacita dimaklumkan bahawa pengesahan maklumat perjawatan / perkhidmatan telah ditolak.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Catatan Pengesahan : " + pengguna.getCatatanPengesahan() + "</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sila login melalui <a href=http://www.bph.gov.my>www.bph.gov.my</a> untuk membuat pengemaskinian maklumat.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
	}
	
	public void selesaiSemakanPenjawatAwamPesaraAwam(String emel, String idNo, String pass) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Semakan maklumat perjawatan awam / pesaraan telah selesai dilakukan.</td></tr>"
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
	
	public void gagalSemakanPenjawatAwam(String emel) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dukacita dimaklumkan bahawa setelah semakan maklumat perjawatan awam dengan sistem HRMIS, anda tidak direkodkan sebagai penjawat awam.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sehubungan itu sila semak maklumat perjawatan awam anda dengan pentadbir sistem HRMIS di jabatan / agensi anda berkerja.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Pendaftaran semula boleh dilakukan di <a href=http://www.bph.gov.my>www.bph.gov.my</a> setelah semakan dengan sistem HRMIS selesai.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
	}
	
	public void gagalSemakanPesaraAwam(String emel) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dukacita dimaklumkan bahawa setelah semakan maklumat persaraan awam dengan sistem PESARA, anda tidak direkodkan sebagai pesara awam.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Pendaftaran semula boleh dilakukan di <a href=http://www.bph.gov.my>www.bph.gov.my</a>.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
	}
	
	public void tempohPengesahanLuput(String emel, Users pengguna) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: TEMPOH SAH AKAUN TELAH LUPUT";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Dukacita dimaklumkan bahawa tempoh sah akaun pengguna bagi ID " + pengguna.getId() + " (" + pengguna.getUserName().toUpperCase() + ")" + " telah luput pada " + Util.getDateTime(pengguna.getTarikhLuputPengesahan(),"dd-MM-yyyy") + ".</td></tr>"
				+ "<tr><td>Sehubungan dengan itu, sila log masuk ke Portal SBBPH untuk mengemaskini maklumat pengesahan jawatan / gred perkhidmatan.</u></td></tr>"
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
	
	public void hantarPasswordPemohonBGSJRP(String emel, String idNo, String pass) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Akaun telah berjaya didaftarkan.</td></tr>"
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
	
	public void hantarSuccessRolePemohonBGSJRP(String emel, String idNo, String modul) {
		String to = emel;
		String subject = "";
		
		subject = "SBBPH :: Pendaftaran Baru";
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Peranan Pengguna sebagai Pemohon " + modul + " telah berjaya didaftarkan.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sila gunakan maklumat dibawah untuk log masuk ke Portal SBBPH.</u></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Log Pengguna : " + idNo + "</td></tr>"
				+ "<tr><td>Kata Laluan adalah katalaluan semasa akaun.</td></tr>"			
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sila login melalui <a href=http://www.bph.gov.my>www.bph.gov.my</a>.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "</table>";
				
		sendMail(to, subject, body);
	}
}

package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppRekodTempahanLondon;
import bph.utils.Util;

public class RppMailer {
	
	private boolean enableMail = true;
	private static RppMailer instance;
	
	private RppMailer() {
		
	}
	
	public static RppMailer get() {
		if ( instance == null ) instance = new RppMailer();
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
	
	/** NOTIFIKASI PERMOHONAN BARU KE OPERATOR PD / LANGKAWI */
	public void notifikasiPermohonanBaruOperator(String emelto, String emelcc, RppPermohonan r) {
		String to = emelto;
		String subject = "SBBPH :: TEMPAHAN "+r.getRppPeranginan().getNamaPeranginan();
		
		String noRuj = "";
		String nama = "";
		String nokp = "";
		if( r!=null ){
			noRuj = r.getNoTempahan();
			nama = r.getPemohon()!=null?r.getPemohon().getUserName():"";
			nokp = r.getPemohon()!=null?r.getPemohon().getNoKP():"";
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa tempahan baru telah dibuat.<br/> Berikut adalah maklumat pemohon :</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. RUJUKAN : "+noRuj.toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH MOHON : "+Util.getDateTime(r.getTarikhPermohonan(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH MASUK : "+Util.getDateTime(r.getTarikhMasukRpp(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>TARIKH KELUAR : "+Util.getDateTime(r.getTarikhKeluarRpp(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>BIL. PENGINAP : "+r.getBilDewasa()+" Dewasa dan "+r.getBilKanakKanak()+" Kanak - Kanak</td></tr>"
				+ "<tr><td><hr/></td></tr>"
				+ "<tr><td>NAMA : "+nama.toUpperCase()+"</td></tr>"
				+ "<tr><td>NO.KAD PENGENALAN : "+nokp+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMailCC(to, emelcc, subject, body);

	}
	
	
	/**NOTIFIKASI EMAIL BILA SLIP PEMBAYARAN DI UPLOAD**/
	public void notifikasiOperatorUploadSlipBayaran(String emel, String emelcc, RppPermohonan r) {
		String to = emel;
		String subject = "SBBPH :: Notifikasi Bayaran Telah Dibuat";
		
		String noRuj = "";
		String nama = "";
		String nokp = "";
		if( r!=null ){
			noRuj = r.getNoTempahan();
			nama = r.getPemohon()!=null?r.getPemohon().getUserName():"";
			nokp = r.getPemohon()!=null?r.getPemohon().getNoKP():"";
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
			+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Sistem memaklumkan bahawa slip pembayaran telah dimuat naik oleh pemohon.<br/> Berikut adalah maklumat pemohon :</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>NO. RUJUKAN : "+noRuj.toUpperCase()+"</td></tr>"
			+ "<tr><td>TARIKH MOHON : "+Util.getDateTime(r.getTarikhPermohonan(),"dd-MM-yyyy")+"</td></tr>"
			+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
			+ "<tr><td>TARIKH MASUK : "+Util.getDateTime(r.getTarikhMasukRpp(),"dd-MM-yyyy")+"</td></tr>"
			+ "<tr><td>TARIKH KELUAR : "+Util.getDateTime(r.getTarikhKeluarRpp(),"dd-MM-yyyy")+"</td></tr>"
			+ "<tr><td>BIL. PENGINAP : "+r.getBilDewasa()+" Dewasa dan "+r.getBilKanakKanak()+" Kanak - Kanak</td></tr>"
			+ "<tr><td><hr/></td></tr>"
			+ "<tr><td>NAMA : "+nama.toUpperCase()+"</td></tr>"
			+ "<tr><td>NO.KAD PENGENALAN : "+nokp+"</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "</table>";
				
		sendMailCC(to, emelcc, subject, body);

	}
	
	/** NOTIFIKASI PERMOHONAN LONDON DILULUSKAN / TIDAK LULUS */
	public void notifikasiStatusPermohonanLondon(String emelto, String emelcc, RppRekodTempahanLondon r,String flag) {
		String to = emelto;
		String subject = "SBBPH :: STATUS KELULUSAN TEMPAHAN "+r.getJenisUnitRpp().getPeranginan().getNamaPeranginan();
		
		String nama = "";
		String nokp = "";
		if( r!=null ){
			nama = r.getPemohon()!=null?r.getPemohon().getUserName():"";
			nokp = r.getPemohon()!=null?r.getPemohon().getNoKP():"";
		}
		
		String wording = "";
		if(flag.equalsIgnoreCase("LULUS")){
			wording = "Sistem memaklumkan bahawa tempahan tuan/puan <b>TELAH DILULUSKAN</b>.<br/> Berikut adalah maklumat tempahan :";
		}else if(flag.equalsIgnoreCase("TIDAK")){
			wording = "Sistem memaklumkan bahawa tempahan tuan/puan <b>TIDAK DILULUSKAN</b>.<br/> Berikut adalah maklumat tempahan :";
		}else if(flag.equalsIgnoreCase("BATAL")){
			wording = "Sistem memaklumkan bahawa tempahan tuan/puan <b>TELAH DIBATALKAN</b>.<br/> Berikut adalah maklumat tempahan :";
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>"+wording+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TARIKH MOHON : "+Util.getDateTime(r.getTarikhDaftarRekod(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH MASUK : "+Util.getDateTime(r.getTarikhMasukRpp(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>TARIKH KELUAR : "+Util.getDateTime(r.getTarikhKeluarRpp(),"dd-MM-yyyy")+"</td></tr>";
		
		if(flag.equalsIgnoreCase("LULUS")){
			body += "<tr><td>KADAR SEWA (RM) : "+Util.formatDecimal(r.getDebit())+"</td></tr>";
		}
		
		body +=	"<tr><td><hr/></td></tr>"
				+ "<tr><td>NAMA : "+nama.toUpperCase()+"</td></tr>"
				+ "<tr><td>NO.KAD PENGENALAN : "+nokp+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMailCC(to, emelcc, subject, body);
	}
	
	
	/** NOTIFIKASI PERMOHONAN LONDON BARU KEPADA PMO */
	public void notifikasiPermohonanBaru(String emelto, String emelcc, RppRekodTempahanLondon r) {
		String to = emelto;
		String subject = "SBBPH :: PERMOHONAN BARU TEMPAHAN "+r.getJenisUnitRpp().getPeranginan().getNamaPeranginan();
		
		String nama = "";
		String nokp = "";
		if( r!=null ){
			nama = r.getPemohon()!=null?r.getPemohon().getUserName():"";
			nokp = r.getPemohon()!=null?r.getPemohon().getNoKP():"";
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa permohonan tempahan baru telah dibuat.<br/> Berikut adalah maklumat tempahan :</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TARIKH MOHON : "+Util.getDateTime(r.getTarikhDaftarRekod(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH MASUK : "+Util.getDateTime(r.getTarikhMasukRpp(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>TARIKH KELUAR : "+Util.getDateTime(r.getTarikhKeluarRpp(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td><hr/></td></tr>"
				+ "<tr><td>NAMA : "+nama.toUpperCase()+"</td></tr>"
				+ "<tr><td>NO.KAD PENGENALAN : "+nokp+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMailCC(to, emelcc, subject, body);
	}
	
	
	/** NOTIFIKASI SILA BUAT PEMBAYARAN */
	public void notifikasiPembayaranTempahan(String emelto, RppPermohonan r) {
		String to = emelto;
		String subject = "SBBPH :: NOTIFIKASI PEMBAYARAN TEMPAHAN BAGI "+r.getRppPeranginan().getNamaPeranginan();
		
		String noRuj = "";
		if( r!=null ){
			noRuj = r.getNoTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Pembayaran bagi tempahan berikut masih <b>BELUM</b> dijelaskan.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : "+noRuj.toUpperCase()+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : "+Util.getDateTime(r.getTarikhPermohonan(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>TARIKH AKHIR BAYARAN : "+Util.getDateTime(r.getTarikhAkhirBayaran(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>SILA JELASKAN PEMBAYARAN DENGAN SEGERA BAGI MENGELAKKAN PERMOHONAN TERBATAL.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMail(to,subject, body);
	}
	
	
	/** NOTIFIKASI PEMBERITAHUAN TEMPAHAN TELAH DIBATALKAN OLEH SISTEM */
	public void notifikasiPembatalanTempahan(String emelto, RppPermohonan r) {
		String to = emelto;
		String subject = "SBBPH :: PEMBATALAN TEMPAHAN OLEH SISTEM BAGI "+r.getRppPeranginan().getNamaPeranginan();
		
		String noRuj = "";
		if( r!=null ){
			noRuj = r.getNoTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa tempahan berikut <b>TELAH DIBATALKAN</b> atas sebab bayaran tempahan tidak dijelaskan pada tempoh yang diberikan.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : "+noRuj.toUpperCase()+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : "+Util.getDateTime(r.getTarikhPermohonan(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>TARIKH AKHIR BAYARAN : "+Util.getDateTime(r.getTarikhAkhirBayaran(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMail(to,subject, body);
	}
	
	/** NOTIFIKASI PEMBERITAHUAN TEMPAHAN TELAH DIBATALKAN OLEH PEMOHON */
	public void notifikasiPembatalanTempahanOlehPemohon(String emelto, RppPermohonan r) {
		String to = emelto;
		String subject = "SBBPH :: PEMBATALAN TEMPAHAN OLEH " + r.getPemohon().getUserName() + " (" + r.getPemohon().getId() + ")" + " BAGI " + r.getRppPeranginan().getNamaPeranginan();
		
		String noRuj = "";
		if( r!=null ){
			noRuj = r.getNoTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa tempahan berikut <b>TELAH DIBATALKAN</b>.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : "+noRuj.toUpperCase()+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : "+Util.getDateTime(r.getTarikhPermohonan(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>***<i>&nbsp;Sekian. Terima kasih.</i></td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "</table>";
				
		sendMail(to,subject, body);
	}
	
	/** NOTIFIKASI PERMOHONAN BARU */
	public void notifikasiTempahanBaru(String emelto, RppPermohonan r) {
		String to = emelto;
		String subject = "SBBPH :: TEMPAHAN PERMOHONAN BAGI "+r.getRppPeranginan().getNamaPeranginan()+" TELAH BERJAYA DIDAFTAR";
		
		String noRuj = "";
		if( r!=null ){
			noRuj = r.getNoTempahan();
		}
		
		String body = "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>TUAN / PUAN,</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>Sistem memaklumkan bahawa tempahan berikut telah berjaya didaftar kedalam sistem.</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>NO. TEMPAHAN : "+noRuj.toUpperCase()+"</td></tr>"
				+ "<tr><td>JENIS UNIT : "+r.getJenisUnitRpp().getKeterangan().toUpperCase()+"</td></tr>"
				+ "<tr><td>TARIKH PERMOHONAN : "+Util.getDateTime(r.getTarikhPermohonan(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>TARIKH AKHIR BAYARAN : "+Util.getDateTime(r.getTarikhAkhirBayaran(),"dd-MM-yyyy")+"</td></tr>"
				+ "<tr><td>&nbsp;</td></tr>"
				+ "<tr><td>SILA JELASKAN PEMBAYARAN DENGAN SEGERA BAGI MENGELAKKAN TEMPAHAN DIBATALKAN.</td></tr>"
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

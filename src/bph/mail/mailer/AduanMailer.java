package bph.mail.mailer;

import java.util.ResourceBundle;

import lebah.mail.MailerDaemon;
import bph.entities.pro.ProAduan;
import bph.utils.Util;

public class AduanMailer {
	
	private boolean enableMail = true;
	private static AduanMailer instance;
	
	private AduanMailer() {
		
	}
	
	public static AduanMailer get() {
		if ( instance == null ) instance = new AduanMailer();
		return instance;
	}
	
	private void sendMailPortal(String to, String subject, String body) {
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
	
	private void sendMail(String to, String cc, String subject, String body, String nama, String email) {
		if ( enableMail ) {
			if (to != null && to.trim().length() > 0) {
				MailerDaemon.getInstance().addMessage(to, cc, subject, body + generateFooter(nama, email));
			} else {
				System.out.println("RECEPIENT IS NULL - " + subject);
			}			
		}
	}
	
	public void daftarAduanBaru(ProAduan aduan) {
		
		String to = aduan.getEmel();		
		String subject = generateSubject(aduan);
		String body = generateHeader(aduan) + getMaklumatAduan(aduan);
				
		sendMailPortal(to, subject, body);
	}
	
	public void terimaAduan(ProAduan aduan, String nama, String email, String to, String cc) {
				
		String subject = generateSubject(aduan);
		String body = generateHeader(aduan) + getMaklumbalasUrusetia(aduan) + getMaklumatAduan(aduan);
				
		sendMail(to, cc, subject, body, nama, email);
	}
	
	public void maklumbalasUnit(ProAduan aduan, String nama, String email, String to, String cc) {
			
		String subject = generateSubject(aduan);
		String body = generateHeader(aduan) + getMaklumbalasUnit(aduan) + getMaklumatAduan(aduan);
				
		sendMail(to, cc, subject, body, nama, email);
	}
	
	public void selesaiAduan(ProAduan aduan,String nama, String email, String to, String cc) {
				
		String subject = generateSubject(aduan);
		String body = generateHeader(aduan) + getMaklumbalasSelesai(aduan) + getMaklumatAduan(aduan);
				
		sendMail(to, cc, subject, body, nama, email);
	}
	
	public void hantarEmailKeFm(ProAduan aduan, String nama, String email, String to, String cc){
				
		String subject = generateSubject(aduan);
		String body = generateHeader(aduan) + getKeteranganTeknikal(aduan) + getMaklumatAduan(aduan);

		sendMail(to, cc, subject, body, nama, email);
	}
	
	private String generateSubject(ProAduan aduan){
		
		String subject = "";
		subject = aduan.getTajuk() + " No. Tiket : " + aduan.getNoAduan();			
		
		return subject;
	}
	
	private String generateHeader(ProAduan aduan){
		
		String header = "";
		header = "<table width='100%' border='0'>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4'><font color='#FF0000'>Nota : Emel ini dijana oleh sistem dan tidak perlu dibalas.</font></td>" 
				+"</tr>"
				+"<br />"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4'><b>Salam 1Malaysia,</b></td>"								 
				+"</tr>"
				+"<br />"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4'>Tuan/Puan,</td>"
				+"</tr>"
				+"<br />"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4'>Dengan segala hormatnya saya diarah merujuk kepada perkara tersebut di atas.</td>"								 
				+"</tr>"
				+"<br />"
				+"</table>";			
		
		return header;
	}
	
	private String getMaklumatAduan(ProAduan aduan){
		
		String tajuk = "Tiada Maklumat"; 
		String namaPengadu = "Tiada Maklumat";
		String ic = "Tiada Maklumat";
	    String noTel = "Tiada Maklumat";
	    String alamat = "";
	    String butiran = "Tiada Maklumat";
	    String status = "Tiada Maklumat";
	    String emailPengadu = "Tiada Maklumat";
	    String jenisAduan = "Tiada Maklumat";
		
	    if (aduan.getTajuk() != null && !"".equals(aduan.getTajuk())) {
	    	 tajuk = aduan.getTajuk();
	    }
	    
	    if (aduan.getNama() != null && !"".equals(aduan.getNama())) {
	    	namaPengadu = aduan.getNama();
	    }
	    
	    if (aduan.getNoPengenalan() != null && !"".equals(aduan.getNoPengenalan())) {
	    	ic = aduan.getNoPengenalan();
	    }
	    
	    alamat = (aduan.getAlamat1() != null && !"".equals(aduan.getAlamat1()) ? aduan.getAlamat1() + ", " : "")
	    		+ (aduan.getAlamat2() != null && !"".equals(aduan.getAlamat2()) ? aduan.getAlamat2() + ", " : "")
	    		+ (aduan.getAlamat3() != null && !"".equals(aduan.getAlamat3()) ? aduan.getAlamat3() + ", " : "")
	    		+ (aduan.getPoskod() != null && !"".equals(aduan.getPoskod()) ? aduan.getPoskod() + " " : "");
	    if (aduan.getBandar() != null) {
	    	alamat = alamat + aduan.getBandar().getKeterangan() + ", ";
	    	if (aduan.getBandar().getNegeri() != null) {
	    		alamat = alamat + aduan.getBandar().getNegeri().getKeterangan();
	    	}
	    }
	    
	    if (aduan.getNoTelefon() != null && !"".equals(aduan.getNoTelefon())) {
	    	noTel = aduan.getNoTelefon();
	    } 
	    
	    if (aduan.getButiran() != null && !"".equals(aduan.getButiran())) {
	    	butiran = aduan.getButiran();
	    } 
	    
	    if (aduan.getStatus() != null) {
	    	status = aduan.getStatus().getKeterangan();
	    }
	    
	    if (aduan.getEmel() != null && !"".equals(aduan.getEmel())) {
	    	emailPengadu = aduan.getEmel();
	    }

	    if (aduan.getJenisAduan() != null) {
	    	jenisAduan = aduan.getJenisAduan().getKeterangan();
	    }
	    
	    String content = "";
		content = "<table width='100%' border='0'>"
				+"<tr>"
				+"<td >&nbsp;</td>"
				+"<td  colspan='4'>Berikut adalah maklumat aduan yang telah didaftarkan.</td>"								 
				+"</tr>"
				+"<br />"
				+"<tr>"
				+"<td >&nbsp;</td>"							 
				+"<td  colspan='4'><b><u>" + tajuk + "</u></b><br><br></td>"
				+"</tr>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>No. Aduan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + aduan.getNoAduan() + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Tarikh Aduan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + Util.getDateTime(aduan.getTarikhAduan(), "dd-MM-yyyy") + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Nama Pengadu</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + namaPengadu + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>No. Pengenalan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + ic + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"								  
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Alamat</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + alamat + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"								  
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>No. Telefon</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + noTel + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"								  
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Emel</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + emailPengadu + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"								  
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Jenis Aduan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + jenisAduan + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"	
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Tajuk Aduan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + tajuk + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Butiran Aduan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + butiran + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td width='20%' valign='top' ><b>Status Aduan</b></td>"
				+"<td width='1%'  valign='top'>:</td>"
				+"<td width='64%'  valign='top'>" + status + "</td>"
				+"<td width='10%'></td>"
				+"</tr>"
				+"</table><br><br>";
					
		return content;				
	}
	
	private String getMaklumbalasUrusetia(ProAduan aduan){
		
		String content = "";		
		content = "<table width='100%' border='0'>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4' >" + aduan.getUlasanUrusetia() + "</td>"
				+"</tr>"
				+"<br />"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>" 
				+"<td colspan='4' >Sekian, terima kasih.<br></td>"
				+"</tr>"
				+"</table><br /><br />";
		
		return content;				
	}
	
	private String getMaklumbalasUnit(ProAduan aduan){
		
		String content = "";		
		content = "<table width='100%' border='0'>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4' >" + aduan.getUlasanUnit() + "</td>"
				+"</tr>"
				+"<br />"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>" 
				+"<td colspan='4' >Sekian, terima kasih.<br></td>"
				+"</tr>"
				+"</table><br /><br />";
		
		return content;				
	}
	
	private String getMaklumbalasSelesai(ProAduan aduan){
		
		String content = "";		
		content = "<table width='100%' border='0'>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4' >" + aduan.getUlasanSelesai() + "</td>"
				+"</tr>"
				+"</table><br><br>";
		
		return content;				
	}
	
	private String getKeteranganTeknikal(ProAduan aduan){
		
		String content = "";		
		content = "<table width='100%' border='0'>"
				+"<tr>"
				+"<td width='5%'>&nbsp;</td>"
				+"<td colspan='4' >" + aduan.getKeteranganTeknikal() + "</td>"
				+"</tr>"
				+"</table><br><br>";
		
		return content;				
	}	
	
	private String generateFooter(String nama, String email){
		
		String footer = "";	
		footer = "<br />"
				+ "<table width='100%' border='0' cellspacing='2' cellpadding='2'>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'><b>BERKHIDMAT UNTUK NEGARA</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'><b>BERILTIZAM PENUHI HASRAT</b></td>"
				+ "</tr>"
				+ "<br />"
				+ "<br />"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'><b>" + nama + "</b></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'>Bahagian Pengurusan Hartanah</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'>Jabatan Perdana Menteri</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'>Aras 7, Blok B3, Kompleks JPM</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'>62502 PUTRAJAYA</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'>No.Tel : 03-88800522/03-88800537; Fax : 03-88883031</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4'>Email :" + email + "</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td width='5%'>&nbsp;</td>"
				+ "<td colspan='4' style='color:'4CB849';'><b>Save a tree..please do not print this e-mail unless absolutely necessary.</b></td>"
				+ "</tr>"
				+ "</table>";
		
		return footer;
	}
}

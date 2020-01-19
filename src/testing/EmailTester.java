package testing;

import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;

public class EmailTester extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7306081640329430338L;
	String EMAIL_TYPE;
	@Override
	public String start() {
		// TODO Auto-generated method stub
		EMAIL_TYPE = ResourceBundle.getBundle("dbconnection").getString("emel");
		String info = "Mail Server [ "+EMAIL_TYPE+"]";
		context.put("EMAIL_TYPE",EMAIL_TYPE);
		context.put("info",info);
		return getPath() + "/start.vm";
	}
	
	public String getPath() {
		return "bph/testing/emailTester";
	}
	
	@Command("testEmail")
	public String testEmail() {
		StringBuffer out=new StringBuffer();
		 
		try {
			//
			//EmailProperty pro = EmailProperty.getInstance();
			//EMAIL_TYPE = (String)context.get("EMAIL_TYPE");
			EMAIL_TYPE = ResourceBundle.getBundle("dbconnection").getString("emel");
			
			if ( "gmail".equals(EMAIL_TYPE) ) {
				//out.append("Checking gmail ...");
				//LOCAL
				out.append("MailerDaemon:sendEmail GMAIL Sending Email to " + getParam("emel"));
				lebah.mail.GmailSMTP.sendEmail("sbbph.bph@gmail.com","sbbph12345",getParam("emel"), "SBBPH :: PENGUJIAN EMEL",this.emelContent()+this.generateFooter());
			} else {
				//out.append("Checking webservice ...");	
				//BPH WEB SERVICE
				org.tempuri.crsservice.JpnManager jpnManagerService = new org.tempuri.crsservice.JpnManager();
				BPHServicesImplService service = new BPHServicesImplService();
				BPHServices bphService = service.getPort(BPHServices.class);
				//OVERIDE ENDPOINT
				String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
				BindingProvider p=(BindingProvider)bphService;
				p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);
				//bphService.sendEmail(getParam("emel"),getParam("cc"), "SBBPH :: PENGUJIAN EMEL",this.emelContent()+this.generateFooter());
				out.append(
						bphService.sendEmailWithResult(getParam("emel"),getParam("cc"), "SBBPH :: PENGUJIAN EMEL",this.emelContent()+this.generateFooter())
				);
				
			}
			
			//TestingMailer.get().emelTesting(getParam("emel"), getParam("cc"));
			context.put("info", "Emel telah dihantar, sila semak emel anda");
		} catch (Exception e) {
			context.put("error", "ERROR : "+e.getMessage());
		}
		context.put("out-result",out.toString());
		return getPath() + "/test_email.vm";
	}
	
	////
	
	public String emelContent() {
		
		String body = "<table width='75%' border='0' cellspacing='0' cellpadding='5'>"
			+ "<tr><td>Assalamualaikum dan Salam Sejahtera.</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>PER : <u style='font-weight: bold'>PENGUJIAN EMEL</u></td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "<tr><td>Emel ini adalah sebagai pengujian.</td></tr>"
			+ "<tr><td>&nbsp;</td></tr>"
			+ "</table>";
				
		return body;
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

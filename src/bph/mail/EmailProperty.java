package bph.mail;

import java.util.ResourceBundle;

public class EmailProperty {
	private String host;
	private String port;
	private String from;
	private String pass;
	private final String BASENAME = "mail";
	private ResourceBundle rb = null;
	private volatile static EmailProperty singleton =null;
	
	private EmailProperty(){
		rb = ResourceBundle.getBundle(BASENAME);
	}
	
	public String getHost() {
		if ("gmail".equals(ResourceBundle.getBundle("dbconnection").getString("emel"))) {
			host = rb.getString("SMTP_HOST_GMAIL");
		} else {
			host = rb.getString("SMTP_HOST_1GOVUC");
		}		
		return host;
	}
	
	public String getPort() {
		if ("gmail".equals(ResourceBundle.getBundle("dbconnection").getString("emel"))) {
			port = rb.getString("SMTP_PORT_GMAIL");
		} else {
			port = rb.getString("SMTP_PORT_1GOVUC");
		}
		return port;
	}
	
	public String getFrom() {
		if ("gmail".equals(ResourceBundle.getBundle("dbconnection").getString("emel"))) {
			from = rb.getString("SMTP_FROM_GMAIL");
		} else {
			from = rb.getString("SMTP_FROM_1GOVUC");
		}
		return from;
	}	
	
	public String getPass() {
		if ("gmail".equals(ResourceBundle.getBundle("dbconnection").getString("emel"))) {
			pass = rb.getString("SMTP_PASS_GMAIL");
		} else {
			pass = rb.getString("SMTP_PASS_1GOVUC");
		}
		return pass;
	}
	
	public static EmailProperty getInstance(){
		if(singleton == null){
			synchronized(EmailProperty.class){
				if(singleton == null){
					singleton = new EmailProperty(); 
				}
			}
		}
		return singleton;
	}	
}

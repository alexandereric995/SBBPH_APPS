package bph.mail;

import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.istack.internal.ByteArrayDataSource;

public class EmailSender {

	private static EmailSender singleton;
	private static EmailProperty pro = EmailProperty.getInstance();
	private static final String EMAIL_HOST = pro.getHost();
	private static final int SMTP_PORT = Integer.parseInt(pro.getPort());
	public String FROM = pro.getFrom();
	public String PASS = pro.getPass();

	private Properties props = null;
	private Session mailSession = null;
	private MimeMessage msg = null;
	private InternetAddress multipleTo[];
	private InternetAddress multipleCC[];

	public String RECIEPIENT = null;
	public String MULTIPLE_RECIEPIENT[];
	public String TO_CC[];
	public String SUBJECT = null;
	public String MESSAGE = null;
	public String ATTACHMENT[];
	public String ATTACHMENT_BYTES[];
	public String ATTACHMENT_BYTES_NAME[];
	private File fileAttachments[];
	private byte[] fileAttachments_bytes[];

	private EmailSender() {
		String emailType = ResourceBundle.getBundle("dbconnection").getString("emel");
		getMailProperties(emailType);
		getMailSession(emailType);
	}

	public static synchronized EmailSender getInstance() {
		singleton = new EmailSender();
		return singleton;
	}

	private Properties getMailProperties(String emailType) {
		props = new Properties();
		if ("gmail".equals(emailType)) {
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.host", EMAIL_HOST);
			props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));
		} else {
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.ehlo", "false");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.host", EMAIL_HOST);
			props.setProperty("mail.smtp.port", String.valueOf(SMTP_PORT));			
		}
		return props;
	}

	private Session getMailSession(String emailType) {
		if ("gmail".equals(emailType)) {
			mailSession = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(FROM, PASS);
						}
					});
		} else {
			mailSession = Session.getDefaultInstance(props);
		}
		return mailSession;
	}

	private MimeMessage setMessage() throws Exception {
		msg = new MimeMessage(mailSession);
		if (MESSAGE == null)
			throw new Exception("EMAIL DONT HAVE ANY MESSAGE");
		if (SUBJECT == null)
			throw new Exception("EMAIL DONT HAVE ANY SUBJECT");
		if (RECIEPIENT == null && MULTIPLE_RECIEPIENT.length <= 0)
			throw new Exception("EMAIL DONT HAVE ANY RECIEPIENT");

		if (MULTIPLE_RECIEPIENT != null && MULTIPLE_RECIEPIENT.length > 0) {
			multipleTo = new InternetAddress[MULTIPLE_RECIEPIENT.length];
			for (int i = 0; i < MULTIPLE_RECIEPIENT.length; i++) {
				String to = MULTIPLE_RECIEPIENT[i];
				if (to != null && !to.equals(""))
					multipleTo[i] = new InternetAddress(MULTIPLE_RECIEPIENT[i]);
			}
		}

		if (TO_CC != null && TO_CC.length > 0) {
			multipleCC = new InternetAddress[TO_CC.length];
			for (int i = 0; i < TO_CC.length; i++) {
				String cc = TO_CC[i];
				if (cc != null && !cc.equals("")) {
					multipleCC[i] = new InternetAddress(TO_CC[i]);
				}
			}
		}
		if (ATTACHMENT != null && ATTACHMENT.length > 0) {
			int totalAttachment = ATTACHMENT.length;

			fileAttachments = new File[totalAttachment];
			for (int i = 0; i < totalAttachment; i++) {
				fileAttachments[i] = new File(ATTACHMENT[i]);
			}
		}

		msg.setSubject(SUBJECT);
		Multipart mp = new MimeMultipart();

		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(MESSAGE, "text/html; charset=utf-8");
		if (fileAttachments != null && fileAttachments.length > 0) {
			for (int i = 0; i < fileAttachments.length; i++) {
				MimeBodyPart attachPart = new MimeBodyPart();
				FileDataSource source = new FileDataSource(fileAttachments[i]);
				attachPart.setDataHandler(new DataHandler(source));
				attachPart.setFileName(fileAttachments[i].getName());
				mp.addBodyPart(attachPart);
			}
		}

		// open razman add new feature : attachment in bytes
		if (ATTACHMENT_BYTES != null && ATTACHMENT_BYTES.length > 0) {
			for (int i = 0; i < ATTACHMENT_BYTES.length; i++) {
				MimeBodyPart attachPart = new MimeBodyPart();

				if (ATTACHMENT_BYTES_NAME != null
						&& ATTACHMENT_BYTES_NAME.length > 0
						&& ATTACHMENT_BYTES_NAME[i] != null) {
					attachPart.setFileName(ATTACHMENT_BYTES_NAME[i]);
				} else {
					attachPart.setFileName("Lampiran" + (i + 1));
				}

				attachPart.setDataHandler(new DataHandler(
						new ByteArrayDataSource(ATTACHMENT_BYTES[i]
								.getBytes("ISO-8859-1"), "application/pdf")));
				mp.addBodyPart(attachPart);
			}
		}
		// close razman add new feature : attachment in bytes

		mp.addBodyPart(textPart);

		msg.setContent(mp);
		msg.setFrom(new InternetAddress(FROM));
		if (multipleTo != null && multipleTo.length > 0) {
			msg.addRecipients(Message.RecipientType.TO, multipleTo);
		} else {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					RECIEPIENT));
		}

		if (multipleCC != null && multipleCC.length > 0) {
			msg.addRecipients(Message.RecipientType.CC, multipleCC);
		}
		return msg;
	}

	public void sendEmail() {
		try {
			setMessage();
			Transport transport = mailSession.getTransport();
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String sendEmail_returnEM() {
		String error = "";
		try {
			setMessage();
			Transport transport = mailSession.getTransport();
			transport.connect();
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			error = e.toString();
		}
		return error;
	}
}

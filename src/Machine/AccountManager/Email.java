package Machine.AccountManager;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataSource;

public class Email
{
	private String emailUsername, password;
	private String recipient,subject,content,filePath,name;
	private final String from = "764people@gmail.com";
	private boolean sendStatement;
	private String tempPass;

	public Email()
	{
		this.emailUsername = from;
		this.password = "srilaprabhupada";
	}
	public Email(String tempPassword)
	{
		this();
		if (tempPassword != null)
			this.tempPass = tempPassword;
		setContent();
		setSubject();
	}

	public Email( boolean sendStatement)
	{
		this();
		this.sendStatement = sendStatement;
		if(this.sendStatement){
			this.subject = "\t\tBANK OF AMERICAN\n\t\tSTATEMENT";
			this.content = "Dear Customer,\n\t Your statement is attached.\n";
		}
	}

	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
	}

	public void setFilePath(String path)
	{
		filePath = path;
	}
	public void setFileName(String name)
	{
		this.name = name;
	}
	private void setSubject()
	{
		this.subject = "Password Reset";
	}
	private void setContent()
	{
		this.content = "PLEASE DO NOT REPLY TO THIS ADDRESS AS IT IS NOT MONITORED\n" +
				"\nDear Customer, \n" +
				"\tYour temporary password is: " + tempPass +
				"\nPlease go back and click on reset password." +
				"\nTEMPORARY CODE EXPIRES IN 15 MINUTES.";
	}

	public void setUsernameRequestContent(String user)
	{
		this.content = "PLEASE DO NOT REPLY TO THIS ADDRESS AS IT IS NOT MONITORED\n" +
				"\nYour username is: " + user + "" +
				"\nYou can now reset your password";
	}

	public void sendEmail() throws RuntimeException
	{
		email();
	}
	private void email()
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(emailUsername, password);
					}
				});

		try {
			if(!this.sendStatement) {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipient));
				message.setSubject(subject);
				message.setText(content);
				System.out.println("Sending message...");
				long start = System.currentTimeMillis();
				Transport.send(message);
				System.out.println(System.currentTimeMillis() - start);
				System.out.println("Message sent to : " + recipient);
			}
			else{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(this.recipient));
				message.setSubject(this.subject);

				MimeBodyPart messageBodyPart;
				MimeBodyPart emailText = new MimeBodyPart();
				emailText.setText(this.content);

				Multipart multipart = new MimeMultipart();

				messageBodyPart = new MimeBodyPart();
				String file = filePath;
				String fileName = name;
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);

				multipart.addBodyPart(emailText);
				multipart.addBodyPart(messageBodyPart);

				message.setContent(multipart);

				System.out.println("Sending message...");
				long start = System.currentTimeMillis();
				Transport.send(message);
				System.out.println("Done in: " + (System.currentTimeMillis() - start) + "ms");
			}
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
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
	private boolean chooseFile = false;

	public Email()
	{
		this.emailUsername = from;
		this.password = "srilaprabhupada";
	}

	public void setRecipient(String recipient) { this.recipient = recipient;}
	public void setChooseFile(boolean val)
	{
		chooseFile = val;
	}
	public void setFilePath(String path)
	{
		filePath = path;
	}
	public void setFileName(String name)
	{
		this.name = name;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	public void setContent(String content)
	{
		this.content = content;
	}

	public void sendEmail()
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
			if(!chooseFile){
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(recipient));
				message.setSubject(subject);
				message.setText(content);
				System.out.println("Sending message...");
				long start = System.currentTimeMillis();
				Transport.send(message);
				System.out.println(System.currentTimeMillis()-start);
				System.out.println("Message sent to : " + recipient);


			}else {
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
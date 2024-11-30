package com.mkyong;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootApplication
public class Application implements CommandLineRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private Environment env;

	// @Autowired
	// private JavaMailSenderImpl javaMailSender;

	@Autowired
	@Qualifier("primarySender")
	JavaMailSender primarySender;

	@Autowired
	@Qualifier("secondarySender")
	JavaMailSender secondarySender;

	private final String fromPrimary = "notifiche-fclt@hostingtt.it";
	private final String fromSecondary = "davcic@libero.it";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public static String[] tornaArrayString(String sArray, String div) {
		StringTokenizer st = new StringTokenizer(sArray,div);
		String[] vet = new String[st.countTokens()];
		int conta = 0;
		while (st.hasMoreTokens()) {
			vet[conta] = st.nextToken().toString();
			conta++;
		}
		return vet;
	}

	@Override
	public void run(String... args) {

		LOGGER.info("Sending Email..");

		String fcltMailPrimaryPassword = (String) env.getProperty("fclt.mail.primary.password");
		LOGGER.info("fclt.mail.primary.password " + fcltMailPrimaryPassword);

		String fcltMailSecondaryPassword = (String) env.getProperty("fclt.mail.secondary.password");
		LOGGER.info("fclt.mail.secondary.password " + fcltMailSecondaryPassword);

		try {

			sendEmailPrimary();
			sendEmailInlineSecondary();

			sendEmailWithAttachmentPrimary();
			sendEmailWithAttachmentSecondary();

			sendEmailSecondary();
			sendEmailInlineSecondary();

			// sendEmailLibero(fcltMailSecondaryPassword);
			// sendEmail();
			// sendEmailWithAttachment();
			// sendEmailInline();

			// Properties p = new Properties();
			// p.setProperty("from", from);
			// p.setProperty("to", "davide.cremona@gmail.com");
			//
			// MailClient client = new MailClient(javaMailSender);
			//
			// String fromS = (String) p.getProperty("from");
			// String toS = (String) p.getProperty("to");
			//
			// String[] to = null;
			// if (toS != null && !toS.equals("")) {
			// to = tornaArrayString(toS, ";");
			// }
			//
			// String subject = "Test Ogg";
			//
			// String cid = ContentIdGenerator.getContentId();
			//
			// String message = "<html><head>" + "<title>This is not usually" +
			// "displayed</title>" + "</head>\n" + "<body><div><b>Hi
			// there!</b></div>"
			// + "<div>Sending HTML in email is so <i>cool!</i> </div>\n" +
			// "<div>And" + "here's an image: <img src=\"cid:" + cid + "\"
			// /></div>\n" + "<div>I hope" + "you like it!</div></body></html>";
			//
			// String[] cc = null;
			// String[] bcc = null;
			// String[] att = new String[] { "/app/data/TOMORI.png" };
			//
			// client.sendMail(fromS, to, cc, bcc, subject, message,
			// "text/html", "1", att);
			//
			// File file = new File("/app/data/TOMORI.png");
			// InputStream targetStream = new FileInputStream(file);
			//
			// Map<String, InputStream> images = new HashMap<String,
			// InputStream>();
			// images.put(cid, targetStream);
			// client.sendMail2(fromS, to, cc, bcc, subject, message,
			// "text/html", "1", images);

		} catch (MessagingException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}

		System.out.println("Done");
	}

	void sendEmailLibero(String springMailPassword) throws Exception {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.libero.it");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session mailSession = Session.getInstance(props, null);

		mailSession.setDebug(true); // Enable the debug mode

		Message msg = new MimeMessage(mailSession);

		msg.setFrom(new InternetAddress("davcic@libero.it"));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("davide.cremona@gmail.com"));

		msg.setSentDate(new Date());
		msg.setSubject("Hello World!");

		msg.setText("Hello from my first e-mail sent with JavaMail");

		Transport transport = mailSession.getTransport("smtps");
		/* criptare la password */
		transport.connect("smtp.libero.it", "davcic@libero.it", springMailPassword);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
	}

	void sendEmailPrimary() throws Exception {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(fromPrimary);
		msg.setTo("davide.cremona@gmail.com", "davis_cremona@hotmail.com", "davcic@libero.it");
		msg.setSubject("Testing from Spring Boot sendEmailPrimary");
		msg.setText("Hello World \n Spring Boot Email sendEmailPrimary");
		primarySender.send(msg);
	}

	void sendEmailSecondary() throws Exception {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(fromSecondary);
		msg.setTo("davide.cremona@gmail.com", "davis_cremona@hotmail.com");
		msg.setSubject("Testing from Spring Boot sendEmailSecondary");
		msg.setText("Hello World \n Spring Boot Email sendEmailSecondary");
		secondarySender.send(msg);
	}

	void sendEmailInlinePrimary() throws Exception {

		MimeMessage msg = primarySender.createMimeMessage();
		msg.setFrom(fromPrimary);
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setTo("davide.cremona@gmail.com");
		helper.setFrom(fromPrimary, "notifiche-fclt");
		// use the true flag to indicate the text included is HTML
		helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);
		msg.setSubject("Testing primarySender");
		// let's include the infamous windows Sample file (this time copied to
		// c:/)
		FileSystemResource res = new FileSystemResource(new File("/app/data/TOMORI.png"));
		helper.addInline("identifier1234", res);
		primarySender.send(msg);

	}

	void sendEmailInlineSecondary() throws Exception {
		MimeMessage msg = secondarySender.createMimeMessage();
		msg.setFrom(fromSecondary);
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setTo("davide.cremona@gmail.com");
		helper.setFrom(fromSecondary, "notifiche-fclt");
		// use the true flag to indicate the text included is HTML
		helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);
		// let's include the infamous windows Sample file (this time copied to
		msg.setSubject("Testing secondarySender");
		FileSystemResource res = new FileSystemResource(new File("/app/data/TOMORI.png"));
		helper.addInline("identifier1234", res);
		secondarySender.send(msg);
	}

	void sendEmailWithAttachmentPrimary() throws Exception {

		MimeMessage msg = primarySender.createMimeMessage();
		msg.setFrom(fromPrimary);

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setTo("davide.cremona@gmail.com");
		helper.setFrom(fromPrimary, "notifiche-fclt");

		helper.setSubject("Testing from Spring Boot primarySender");

		// default = text/plain
		// helper.setText("Check attachment for image!");

		// true = text/html
		helper.setText("<h1>Check attachment for image!</h1>", true);

		// FileSystemResource file = new FileSystemResource(new
		// File("classpath:android.png"));

		// Resource resource = new ClassPathResource("android.png");
		// InputStream input = resource.getInputStream();

		// ResourceUtils.getFile("classpath:android.png");

		helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

		primarySender.send(msg);

	}

	void sendEmailWithAttachmentSecondary() throws Exception {

		MimeMessage msg = secondarySender.createMimeMessage();
		msg.setFrom(fromSecondary);

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setTo("davide.cremona@gmail.com");
		helper.setFrom(fromSecondary, "notifiche-fclt");

		helper.setSubject("Testing from Spring Boot secondarySender");

		// default = text/plain
		// helper.setText("Check attachment for image!");

		// true = text/html
		helper.setText("<h1>Check attachment for image!</h1>", true);

		// FileSystemResource file = new FileSystemResource(new
		// File("classpath:android.png"));

		// Resource resource = new ClassPathResource("android.png");
		// InputStream input = resource.getInputStream();

		// ResourceUtils.getFile("classpath:android.png");

		helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

		secondarySender.send(msg);

	}

}

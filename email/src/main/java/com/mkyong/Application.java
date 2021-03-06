package com.mkyong;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	// https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/integration.html#mail
	@Autowired
	private JavaMailSender javaMailSender;

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

		System.out.println("Sending Email..");

		String springMailPassword = (String) env.getProperty("spring.mail.password");
		System.out.println("springMailPassword " + springMailPassword);

		try {
			
			// sendEmail();
			// sendEmailWithAttachment();
			// sendEmailInline();

			Properties p = new Properties();
			p.setProperty("from", "notifiche-fclt@hostingtt.it");
			p.setProperty("to", "davide.cremona@gmail.com");

			MailClient client = new MailClient(javaMailSender);

			String fromS = (String) p.getProperty("from");
			String toS = (String) p.getProperty("to");

			String[] to = null;
			if (toS != null && !toS.equals("")) {
				to = tornaArrayString(toS, ";");
			}

			String subject = "Test Ogg";

			String cid = ContentIdGenerator.getContentId();

			String message = "<html><head>" + "<title>This is not usually" + "displayed</title>" + "</head>\n" + "<body><div><b>Hi there!</b></div>" 
			+ "<div>Sending HTML in email is so <i>cool!</i> </div>\n" + "<div>And" + "here's an image: <img src=\"cid:" + cid + "\" /></div>\n" + "<div>I hope" + "you like it!</div></body></html>";
			
			String[] cc = null;
			String[] bcc = null;
			String[] att = new String[] { "/app/data/TOMORI.png" };

			client.sendMail(fromS, to, cc, bcc, subject, message, "text/html", "1", att);

			File file = new File("/app/data/TOMORI.png");
			InputStream targetStream = new FileInputStream(file);

			Map<String, InputStream> images = new HashMap<String, InputStream>();
			images.put(cid, targetStream);
			client.sendMail2(fromS, to, cc, bcc, subject, message, "text/html", "1", images);

		} catch (MessagingException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}

		System.out.println("Done");
	}

	void sendEmail() {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("notifiche-fclt@hostingtt.it");

		msg.setTo("davide.cremona@gmail.com", "davis_cremona@hotmail.com", "davcic@libero.it");

		msg.setSubject("Testing from Spring Boot");
		msg.setText("Hello World \n Spring Boot Email");

		javaMailSender.send(msg);

	}

	void sendEmailWithAttachment() throws MessagingException, IOException {

		MimeMessage msg = javaMailSender.createMimeMessage();
		msg.setFrom("notifiche-fclt@hostingtt.it");

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		helper.setTo("davide.cremona@gmail.com");

		helper.setSubject("Testing from Spring Boot");

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

		javaMailSender.send(msg);

	}
	
	void sendEmailInline() throws MessagingException, IOException {

		MimeMessage msg = javaMailSender.createMimeMessage();
		msg.setFrom("notifiche-fclt@hostingtt.it");
		
		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo("davide.cremona@gmail.com");

		// use the true flag to indicate the text included is HTML
		helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);

		// let's include the infamous windows Sample file (this time copied to c:/)
		FileSystemResource res = new FileSystemResource(new File("/app/data/TOMORI.png"));
		helper.addInline("identifier1234", res);

		javaMailSender.send(msg);

	}

}

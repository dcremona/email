package com.mkyong;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@SpringBootApplication
public class Application implements CommandLineRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	// https://docs.spring.io/spring/docs/5.1.6.RELEASE/spring-framework-reference/integration.html#mail
	@Autowired
	private JavaMailSender javaMailSender;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		// SpringApplicationBuilder builder = new
		// SpringApplicationBuilder(Application.class);
		// builder.headless(false);
		// ConfigurableApplicationContext context = builder.run(args);
		// LOGGER.info(context.getDisplayName());

		// LOGGER.info("Simple log statement with inputs {}, {} and {}", 1, 2,
		// 3);
		// String fonts[] =
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		// LOGGER.info("fonts " + fonts.length);
		// for (String s : fonts) {
		// LOGGER.info(s);
		// }
	}

	@Override
	public void run(String... args) {

		System.out.println("Sending Email..");

		try {
			sendEmail();
			sendEmailWithAttachment();
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
}

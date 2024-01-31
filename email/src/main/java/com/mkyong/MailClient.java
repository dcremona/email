package com.mkyong;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailClient{

	private static final Logger log = LoggerFactory.getLogger(MailClient.class);

	private JavaMailSenderImpl javaMailSender;

	public MailClient(JavaMailSenderImpl jMS) {
		javaMailSender = jMS;
	}

	public void sendMail(String from, String[] to, String[] cc, String[] bcc,
			String subject, String messageBody, String typeMessage,
			String priority, String[] attachments)
			throws IOException, MessagingException, AddressException {

		log.info("****************************************");
		log.info("INIZIO ESECUZIONE sendMail              ");
		log.info("****************************************");

		log.info(" from: " + from);
		log.info(" subject: " + subject);
		log.info(" message: " + messageBody);

		if (attachments != null) {

			MimeMessage msg = javaMailSender.createMimeMessage();
			msg.setFrom(from);

			MimeMessageHelper helper = new MimeMessageHelper(msg,true);
			helper.setFrom(from, "notifiche-fclt");
			helper.setTo(to);
			if (cc != null) {
				helper.setCc(cc);
			}
			if (bcc != null) {
				helper.setBcc(bcc);
			}

			helper.setSubject(subject);
			if (typeMessage.equals("text/html")) {
				helper.setText(messageBody, true);
			} else {
				msg.setText(messageBody);
			}

			for (int i = 0; i <= attachments.length - 1; i++) {
				File file = new File(attachments[i]);
				helper.addAttachment(file.getName(), file);
			}

			javaMailSender.send(msg);

		} else {

			if (typeMessage.equals("text/html")) {

				MimeMessage msg = javaMailSender.createMimeMessage();
				msg.setFrom(from);
				MimeMessageHelper helper = new MimeMessageHelper(msg,true);
				helper.setTo(to);
				helper.setFrom(from, "notifiche-fclt");
				if (cc != null) {
					helper.setCc(cc);
				}
				if (bcc != null) {
					helper.setBcc(bcc);
				}
				helper.setSubject(subject);
				helper.setText(messageBody, true);
				javaMailSender.send(msg);

			} else {

				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setFrom(from);
				msg.setTo(to);
				if (cc != null) {
					msg.setCc(cc);
				}
				if (bcc != null) {
					msg.setBcc(bcc);
				}
				msg.setSubject(subject);
				msg.setText(messageBody);
				javaMailSender.send(msg);
			}
		}

		log.info("****************************************");
		log.info("FINE ESECUZIONE sendMail              ");
		log.info("****************************************");
	}

	public void sendMail2(String from, String[] to, String[] cc, String[] bcc,
			String subject, String messageBody, String typeMessage,
			String priority, Map<String, InputStream> images)
			throws IOException, MessagingException, AddressException {

		log.info("****************************************");
		log.info("INIZIO ESECUZIONE sendMail              ");
		log.info("****************************************");

		log.info(" from: " + from);
		log.info(" subject: " + subject);
		log.info(" message: " + messageBody);

		if (images != null) {

			MimeMessage msg = javaMailSender.createMimeMessage();
			msg.setFrom(from);

			MimeMessageHelper helper = new MimeMessageHelper(msg,true);
			helper.setTo(to);
			helper.setFrom(from, "notifiche-fclt");
			if (cc != null) {
				helper.setCc(cc);
			}
			if (bcc != null) {
				helper.setBcc(bcc);
			}

			helper.setSubject(subject);
			if (typeMessage.equals("text/html")) {
				helper.setText(messageBody, true);
			} else {
				msg.setText(messageBody);
			}

			Iterator<?> it = images.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();

				InputStream inputStream = (InputStream) pairs.getValue();
				File somethingFile = File.createTempFile("test", ".png");
				try {
					FileUtils.copyInputStreamToFile(inputStream, somethingFile);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
				helper.addInline((String) pairs.getKey(), somethingFile);
			}

			javaMailSender.send(msg);

		} else {

			if (typeMessage.equals("text/html")) {

				MimeMessage msg = javaMailSender.createMimeMessage();
				msg.setFrom(from);
				MimeMessageHelper helper = new MimeMessageHelper(msg,true);
				helper.setTo(to);
				helper.setFrom(from, "notifiche-fclt");
				if (cc != null) {
					helper.setCc(cc);
				}
				if (bcc != null) {
					helper.setBcc(bcc);
				}
				helper.setSubject(subject);
				helper.setText(messageBody, true);
				javaMailSender.send(msg);

			} else {

				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setFrom(from);
				msg.setTo(to);
				if (cc != null) {
					msg.setCc(cc);
				}
				if (bcc != null) {
					msg.setBcc(bcc);
				}
				msg.setSubject(subject);
				msg.setText(messageBody);
				javaMailSender.send(msg);

			}
		}

		log.info("****************************************");
		log.info("FINE ESECUZIONE sendMail              ");
		log.info("****************************************");

	}

}
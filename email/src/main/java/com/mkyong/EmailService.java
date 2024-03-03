package com.mkyong;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

	private JavaMailSender primarySender;
	private JavaMailSender secondarySender;

	public EmailService(
			@Qualifier("primarySender") JavaMailSender primarySender,
			@Qualifier("secondarySender") JavaMailSender secondarySender) {
		this.primarySender = primarySender;
		this.secondarySender = secondarySender;
	}

	public void sendPrimaryEmail(String from, String to, String subject,
			String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		primarySender.send(message);

	}

	public void sendSecondaryEmail(String from, String to, String subject,
			String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		secondarySender.send(message);

	}
}

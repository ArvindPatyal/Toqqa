package com.toqqa.util;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.toqqa.bo.EmailBo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailUtility {
	@Autowired
	private EmailProps emailProps;

	@Autowired
	private JavaMailSender sender;

	@Async
	public void processEmail(EmailBo emailBo) throws Exception {
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			String html = emailBo.getMailContent();
			helper.setTo(emailBo.getMailTo());
			helper.setText(html, true);
			helper.setSubject(emailBo.getMailSubject());
			helper.setFrom(emailProps.getUsername());
			sender.send(message);
			log.info("email sent to " + emailBo.getMailTo());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}

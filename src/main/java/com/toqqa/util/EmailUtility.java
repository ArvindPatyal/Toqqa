package com.toqqa.util;

import com.toqqa.bo.EmailBo;
import com.toqqa.dto.ResetTokenEmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class EmailUtility {
    @Autowired
    private EmailProps emailProps;

    @Autowired
    private JavaMailSender sender;

    @Async
    public void orderEmail(EmailBo emailBo) throws Exception {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            String html = emailBo.getMailContent();
            helper.setText(html, true);
            helper.setSubject(emailBo.getMailSubject());
            helper.setTo(emailBo.getMailTo());
            helper.setFrom(emailProps.getUsername());
            sender.send(message);
            log.info("email sent to " + emailBo.getMailTo());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Async
    public void feedbackEmail(EmailBo emailBo) throws Exception {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(emailProps.getAdminEmail());
            helper.setFrom(emailProps.getUsername());
            helper.setSubject(Constants.FEEDBACK_CONSTANT);
            String html = emailBo.getMailContent();
            helper.setText(html, true);
            sender.send(message);
            log.info("email sent to " + emailProps.getAdminEmail());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Async
    public void resetToken(ResetTokenEmailDto resetTokenEmailDto) throws Exception {
        log.info("Invoked :: EmailServiceImpl :: forgotPassword()");
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(resetTokenEmailDto.getMailTo());
            helper.setFrom(resetTokenEmailDto.getFrom());
            helper.setSubject(resetTokenEmailDto.getMailSubject());
            String html = resetTokenEmailDto.getMailContent();
            helper.setText(html, true);
            sender.send(message);
            log.info("reset password Email sent");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

package com.toqqa.service.impls;

import com.toqqa.bo.EmailBo;
import com.toqqa.domain.User;
import com.toqqa.dto.EmailRequestDto;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.EmailService;
import com.toqqa.util.Constants;
import com.toqqa.util.EmailProps;
import com.toqqa.util.EmailUtility;
import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service("emailService")
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailUtility emailUtil;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Autowired
    private EmailProps emailProps;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void sendEmail(EmailRequestDto emailRequestDto) {
        log.info("Invoked :: EmailServiceImpl :: sendOrderEmail()");
        User user = this.authenticationService.currentUser();
        EmailBo emailBo = new EmailBo();
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("title", emailRequestDto.getSubject());
        model.put("description", emailRequestDto.getBody());
        model.put("name", user.getFirstName() + " " + user.getLastName());
        model.put("email", user.getEmail() != null ? user.getEmail() : "");
        model.put("phoneNumber", user.getPhone());

        if (emailRequestDto.isOrder() == false) {
            try {
                freemarkerConfiguration.getTemplate("feedbackEmail.ftlh").process(model, writer);
                emailBo.setMailContent(writer.getBuffer().toString());
                this.emailUtil.feedbackEmail(emailBo);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        if (emailRequestDto.isOrder() == true) {
           emailBo.setMailTo(user.getEmail() != null ? user.getEmail() : "");
            emailBo.setMailSubject(Constants.ORDER_EMAIL_CONSTANT);
            try {
                freemarkerConfiguration.getTemplate("orderEmail.ftlh").process(model, writer);

                emailBo.setMailContent(writer.getBuffer().toString());
                this.emailUtil.orderEmail(emailBo);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}

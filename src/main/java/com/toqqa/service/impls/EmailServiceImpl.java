package com.toqqa.service.impls;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toqqa.bo.EmailBo;
import com.toqqa.domain.OrderInfo;
import com.toqqa.service.EmailService;
import com.toqqa.util.EmailUtility;

import freemarker.template.Configuration;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailUtility emailUtil;

	@Autowired
	private Configuration freemarkerConfiguration;

	@Override
	public Boolean sendEmail(EmailBo emailBo) throws Exception {

		return true;
	}

	@Override
	public void sendOrderEmail(OrderInfo orderInfo) {
		EmailBo bo = new EmailBo();

		StringWriter writer = new StringWriter();
		Map<String, Object> model = new HashMap<>();

		model.put("name", orderInfo.getFirstName());

		bo.setMailTo(orderInfo.getEmail());

		bo.setMailSubject("welcome to freemarker email ");

		try {
			freemarkerConfiguration.getTemplate("orderEmail.ftlh").process(model, writer);

			bo.setMailContent(writer.getBuffer().toString());
			this.emailUtil.processEmail(bo);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}

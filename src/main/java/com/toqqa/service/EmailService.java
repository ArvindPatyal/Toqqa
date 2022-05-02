package com.toqqa.service;

import com.toqqa.bo.EmailBo;
import com.toqqa.domain.OrderInfo;

public interface EmailService {

	public Boolean sendEmail(EmailBo emailBo) throws Exception;

	void sendOrderEmail(OrderInfo orderInfo);

}

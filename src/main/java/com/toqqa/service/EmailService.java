package com.toqqa.service;

import com.toqqa.bo.EmailBo;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.User;
import com.toqqa.dto.EmailRequestDto;

public interface EmailService {

    void sendEmail(EmailRequestDto emailRequestDto);
}

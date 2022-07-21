package com.toqqa.service;

import com.toqqa.dto.EmailRequestDto;
import com.toqqa.dto.ResetTokenEmailDto;

public interface EmailService {

    void sendEmail(EmailRequestDto emailRequestDto);

    void resetToken(ResetTokenEmailDto resetTokenEmailDto);
}

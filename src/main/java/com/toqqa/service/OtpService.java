package com.toqqa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toqqa.bo.SendOtpResponseBo;
import com.toqqa.bo.VerifyOtpResponseBo;
import com.toqqa.constants.CountryCodes;
import com.toqqa.dto.OtpDto;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceCreateUpdateException;
import com.toqqa.exception.UserAlreadyExists;
import com.toqqa.payload.Response;
import com.toqqa.util.TemplateId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class OtpService {

    @Value("${otp.AuthKey}")
    private String authKey;
    @Value("${otp.expiryTime}")
    private String expiryTime;
    @Value("${otp.email.template.id}")
    private String emailTemplateId;
    @Value("${otp.company.name}")
    private String companyName;
    @Value("${otp.send.base.url}")
    private String baseUrl;
    @Value("${otp.auth.base.url}")
    private String authUrl;


    private final UserService userService;

    @Autowired
    public OtpService(@Lazy UserService userService) {
        this.userService = userService;
    }

    public Response sendOtp(OtpDto otpDto) {
        if (userService.isUserExists(otpDto.getMobileNumber(), otpDto.getMobileNumber())) {
            throw new UserAlreadyExists("User Already exists with this phone Number OR Email!!!");
        }
        String smsTemplateId = this.templateIdByCountryCode(otpDto.getCountryCode());
        String url = null;
//        if (otpDto.getEmail() == null) {
        url = baseUrl +
                "?authkey=" + authKey +
                "&mobile=" + otpDto.getMobileNumber() +
                "&country_code=" + otpDto.getCountryCode() +
                "&sid=" + smsTemplateId +
                "&company=" + companyName +
                "&time=" + expiryTime;
      /*  } else if (otpDto.getMobileNumber() == null) {
            url = baseUrl +
                    "?authkey=" + authKey +
                    "&email=" + otpDto.getEmail() +
                    "&mid=" + emailTemplateId +
                    "&company=" + companyName +
                    "&time=" + expiryTime;
        } else {
            url = baseUrl +
                    "?authkey=" + authKey +
                    "&mobile=" + otpDto.getMobileNumber() +
                    "&country_code=" + otpDto.getCountryCode() +
                    "&sid=" + smsTemplateId +
                    "&email=" + otpDto.getEmail() +
                    "&mid=" + emailTemplateId +
                    "&company=" + companyName +
                    "&time=" + expiryTime;
        }*/
        return new Response(executeOtp(url), "OTP sent successfully");
    }

    private String templateIdByCountryCode(String countryCode) {
        CountryCodes countryCodes = CountryCodes.valueOf(countryCode);
        if (countryCodes.equals(null)) {
            throw new BadRequestException("Enter a valid country code");
        }
        if (countryCodes == CountryCodes.INDIA) {
            return TemplateId.INDIA;
        }
        if (countryCodes == CountryCodes.PAKISTAN) {
            return TemplateId.PAKISTAN;

        }
        throw new BadRequestException("Invalid country code");
    }


    private SendOtpResponseBo executeOtp(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
            } else {
                throw new ResourceCreateUpdateException("OTP sending failed");
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);
            in.close();
            return new ObjectMapper().readValue(response.toString(), SendOtpResponseBo.class);
        } catch (Exception e) {
            throw new ResourceCreateUpdateException("Error occurred while making call to send OTP");
        }
    }


    public VerifyOtpResponseBo verifyOtp(String otp, String loginId) {
        try {
            String url = authUrl + "?authkey=" + authKey + "&channel=sms&otp=" + otp + "&logid=" + loginId;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
            } else {
                throw new ResourceCreateUpdateException("OTP verification failed");
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            inputStream));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);
            in.close();
            return new ObjectMapper().readValue(response.toString(), VerifyOtpResponseBo.class);

        } catch (IOException e) {
            throw new ResourceCreateUpdateException("Error occurred while making call to verify OTP");
        }
    }
}

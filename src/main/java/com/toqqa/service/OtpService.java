package com.toqqa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toqqa.bo.OtpResponseBo;
import com.toqqa.dto.OtpDto;
import com.toqqa.exception.ResourceCreateUpdateException;
import com.toqqa.payload.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${otp.sms.template.id}")
    private String smsTemplateId;
    @Value("${otp.email.template.id}")
    private String emailTemplateId;
    @Value("${otp.company.name}")
    private String companyName;
    @Value("${otp.base.url}")
    private String baseUrl;

    public Response sendOtp(OtpDto otpDto) throws IOException {
        String url = null;
        if (otpDto.getEmail() == null) {
            url = baseUrl +
                    "?authkey=" + authKey +
                    "&mobile=" + otpDto.getMobileNumber() +
                    "&country_code=" + otpDto.getCountryCode() +
                    "&sid=" + smsTemplateId +
                    "&company=" + companyName +
                    "&time=" + expiryTime;
        } else if (otpDto.getMobileNumber() == null) {
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
        }
        return new Response(executeOtp(url), "OTP sent successfully");
    }


    private OtpResponseBo executeOtp(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        int responseCode = connection.getResponseCode();
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = connection.getInputStream();
        } else {
            throw new ResourceCreateUpdateException("OTP sending failed");
        }
        inputStream.getClass();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String currentLine;
        while ((currentLine = in.readLine()) != null)
            response.append(currentLine);
        in.close();
        return new ObjectMapper().readValue(response.toString(), OtpResponseBo.class);
    }


    public String verifyOtp(String otp, String loginId) throws IOException {
        String url = baseUrl + "?authkey=" + authKey + "&channel=sms&otp=" + otp + "&logid=" + loginId;
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
        return response.toString();
    }
}

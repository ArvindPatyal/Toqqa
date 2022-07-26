package com.toqqa.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.mail.Message;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpResponseBo {
    @JsonProperty(value = "LogID")
    private String logId;
    @JsonProperty(value = "Message")
    private String message;
}

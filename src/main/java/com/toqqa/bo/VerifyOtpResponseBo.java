package com.toqqa.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpResponseBo {
    @JsonProperty(value = "status")
    private boolean status;
    @JsonProperty(value = "message")
    private String message;
}

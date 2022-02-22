package com.toqqa.payload;

import com.toqqa.bo.UserBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private JwtAuthenticationResponse header;
    private UserBo userData;
}

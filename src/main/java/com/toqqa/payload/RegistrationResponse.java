package com.toqqa.payload;

import com.toqqa.bo.UserBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
	private UserBo user;
}

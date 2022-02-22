package com.toqqa.payload;

import com.toqqa.bo.SmeBo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmeRegistrationResponse extends RegistrationResponse {
	private SmeBo sme;
}

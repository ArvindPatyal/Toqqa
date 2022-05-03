package com.toqqa.bo;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailBo {

	private String mailTo;

	private String mailSubject;

	private String mailContent;

	private Map<String, Object> model;

}

package com.toqqa.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class EmailRequestDto {

    private  String subject;

    private String body;

    private Map <String, Object> data = new HashMap<>();

    private  boolean isOrder;


}

package com.toqqa.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NearbySmeRespDto {
	private String id;
	private String nameOfBusiness;
	private String businessAddress;
	private Double latitude;
	private Double longitude;

}

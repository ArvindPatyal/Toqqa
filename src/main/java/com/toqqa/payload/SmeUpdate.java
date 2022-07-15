package com.toqqa.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SmeUpdate {


    @NotNull
    private String nameOfBusiness;

    @NotNull
    private String businessAddress;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String country;


    private MultipartFile businessLogo;

    @NotNull
    private List<String> businessCategory;


    private List<String> businessSubCategory;

    private String description;

    @NotNull
    private String typeOfBusiness;

    @NotNull
    private Boolean deliverToCustomer;

    private Double deliveryRadius;

    @NotNull
    private Double deliveryCharge;


    private Long startTimeOfDelivery;

    private Long endTimeOfDelivery;

    private MultipartFile regDoc;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}

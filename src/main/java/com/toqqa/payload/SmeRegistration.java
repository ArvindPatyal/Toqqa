package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SmeRegistration {
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

    @NotNull
    private MultipartFile businessLogo;

    @NotNull
    private List<String> businessCategory;

    @NotNull
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

    @NotNull
    private Boolean isRegisteredWithGovt;

    //	@NotNull
    private MultipartFile regDoc;

    @NotNull
    private MultipartFile idProof;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;


}

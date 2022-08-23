package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SmeRegistration {
    @NotNull
    @Size(max = 255)
    private String nameOfBusiness;

    @NotNull
    @Size(max = 255)
    private String businessAddress;

    @NotNull
    @Size(max = 255)
    private String city;

    @NotNull
    @Size(max = 255)
    private String state;

    @NotNull
    @Size(max = 255)
    private String country;

    @NotNull
    private MultipartFile businessLogo;

    @NotNull
    private List<String> businessCategory;

    @NotNull
    private List<String> businessSubCategory;

    @Size(max = 500)
    private String description;

    @NotNull
    @Size(max = 255)
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

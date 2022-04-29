package com.toqqa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sme")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sme {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String nameOfBusiness;
    private String businessAddress;
    private String city;
    private String state;
    private String country;
    private String businessLogo;

    @ManyToMany
    @JoinTable(name = "category_sme", joinColumns = @JoinColumn(name = "sme_id"), inverseJoinColumns = @JoinColumn(name = "cat_id"))
    private List<Category> businessCatagory;

    @ManyToMany
    @JoinTable(name = "sub_category_sme", joinColumns = @JoinColumn(name = "sme_id"), inverseJoinColumns = @JoinColumn(name = "subCat_id"))
    private List<SubCategory> businessSubCatagory;

    @Column(length = 500)
    private String description;

    private Boolean isDeleted;
    private String typeOfBusiness;
    private Boolean isDeliverToCustomer;
    private Double deliveryRadius;
    private Double deliveryCharges;
    private Boolean isRegisterWithGovt;
    private Date startTimeOfDelivery;
    private Date endTimeOfDelivery;
    private String regDoc;
    private String idProof;
    private String userId;

}
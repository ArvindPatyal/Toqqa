package com.toqqa.bo;

import com.toqqa.domain.Advertisement;
import lombok.Data;

import java.util.Date;

@Data
public class AdvertisementBo {

    private String id;

    private String description;

    private Date createdDate;

    private Date modificationDate;

    private Integer clicks;

    private String banner;

    private ProductBo product;

    public AdvertisementBo(Advertisement advertisement) {

        this.id = advertisement.getId();
        this.description = advertisement.getDescription();
        this.createdDate = advertisement.getCreatedDate();
        this.modificationDate = advertisement.getModificationDate();
        this.clicks = advertisement.getClicks();
        this.banner = advertisement.getBanner();
    }

    public AdvertisementBo(Advertisement advertisement, ProductBo product) {

        this.id = advertisement.getId();
        this.description = advertisement.getDescription();
        this.createdDate = advertisement.getCreatedDate();
        this.modificationDate = advertisement.getModificationDate();
        this.clicks = advertisement.getClicks();
        this.banner = advertisement.getBanner();
        this.product = product;

    }

}

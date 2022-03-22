package com.toqqa.bo;

import java.util.Date;

import com.toqqa.domain.Advertisement;

import lombok.Data;

@Data
public class AdvertisementBo {

	private String id;

	private String description;

	private Date createdDate;

	private Date modificationDate;

	private Integer clicks;

	private String banner;

	private String productId;

	public AdvertisementBo(Advertisement advertisement) {

		this.id = advertisement.getId();
		this.description = advertisement.getDescription();
		this.createdDate = advertisement.getCreatedDate();
		this.modificationDate = advertisement.getModificationDate();
		this.clicks = advertisement.getClicks();
		this.banner = advertisement.getBanner();
		this.productId = advertisement.getProduct().getId();

	}

}

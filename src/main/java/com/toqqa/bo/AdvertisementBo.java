package com.toqqa.bo;

import com.toqqa.domain.Advertisement;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementBo {

	private String id;

	private String description;

	private Date createdDate;

	private Date modificationDate;

	private Integer clicks;

	private String banner;

	private ProductBo product;

	private Boolean isActive;
	
//	private Integer queueNumber;

	public AdvertisementBo(Advertisement advertisement) {

		this.id = advertisement.getId();
		this.description = advertisement.getDescription();
		this.createdDate = advertisement.getCreatedDate();
		this.modificationDate = advertisement.getModificationDate();
		this.clicks = advertisement.getClicks();
		this.banner = advertisement.getBanner();
		this.isActive = advertisement.getIsActive();
	}

	public AdvertisementBo(Advertisement advertisement, ProductBo product) {

		this.id = advertisement.getId();
		this.description = advertisement.getDescription();
		this.createdDate = advertisement.getCreatedDate();
		this.modificationDate = advertisement.getModificationDate();
		this.clicks = advertisement.getClicks();
		this.banner = advertisement.getBanner();
		this.product = product;
		this.isActive = advertisement.getIsActive();
	}

}

package com.toqqa.service;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.AdvertisementPayload;
import com.toqqa.payload.AdvertisementUpdate;
import com.toqqa.payload.ListResponseWithCount;

public interface AdvertisementService {

	AdvertisementBo createAds(AdvertisementPayload advertisementPayload);

	AdvertisementBo updateAd(AdvertisementUpdate advertisementUpdate);

	AdvertisementBo fetchAd(String id);

	void deleteAd(String id);

	ListResponseWithCount<AdvertisementBo> fetchAdvertisementList(PaginationBo paginationbo);

	AdvertisementBo updateClick(String id);

	AdvertisementBo updateAdsStatus(String id);

}

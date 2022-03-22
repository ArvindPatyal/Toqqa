package com.toqqa.service.impls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.Advertisement;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AdvertisementPayload;
import com.toqqa.payload.AdvertisementUpdate;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.repository.AdvertisementRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.AdvertisementService;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.StorageService;
import com.toqqa.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {

	@Autowired
	private AdvertisementRepository advertisementRepo;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ProductRepository productRepo;

	@Value("${pageSize}")
	private Integer pageSize;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private Helper helper;

	@Override
	public AdvertisementBo createAds(AdvertisementPayload advertisementPayload) {

		log.info("Inside create Advertisement");
		User user = this.authenticationService.currentUser();
		Advertisement ads = new Advertisement();
		ads.setIsDeleted(false);
		ads.setIsActive(true);
		ads.setDescription(advertisementPayload.getDescription());
		ads.setUser(user);
		ads.setProduct(this.productRepo.findById(advertisementPayload.getProductId()).get());
		ads.setClicks(0);
		ads.setCreatedDate(new Date());
		ads.setModificationDate(new Date());

		try {
			ads.setBanner(this.storageService
					.uploadFileAsync(advertisementPayload.getBanner(), user.getId(), FolderConstants.BANNER.getValue())
					.get());

		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}
		this.updateOldAdsStatus(user);
		ads = this.advertisementRepo.saveAndFlush(ads);
		ads = this.advertisementRepo.saveAndFlush(ads);
		AdvertisementBo bo = new AdvertisementBo(ads);
		bo.setBanner(this.prepareResource(ads.getBanner()));
		return bo;

	}

	private void updateOldAdsStatus(User user) {

		log.info("Inside advertisement update ads Status");
		List<Advertisement> ads = this.advertisementRepo.findByUserAndIsActive(user, true);
		ads.forEach(ad -> {
			ad.setIsActive(false);
			this.advertisementRepo.saveAndFlush(ad);
		});
	}

	@Override
	public AdvertisementBo updateAd(AdvertisementUpdate advertisementUpdate) {

		log.info("Inside Update Advertisement");
		User user = this.authenticationService.currentUser();
		Advertisement ads = this.advertisementRepo.findById(advertisementUpdate.getId()).get();
		ads.setModificationDate(new Date());
		ads.setDescription(advertisementUpdate.getDescription());
		ads.setProduct(this.productRepo.findById(advertisementUpdate.getProductId()).get());
		try {
			ads.setBanner(this.storageService
					.uploadFileAsync(advertisementUpdate.getBanner(), user.getId(), FolderConstants.BANNER.getValue())
					.get());

		} catch (InterruptedException | ExecutionException e) {

			e.printStackTrace();
		}

		ads = this.advertisementRepo.saveAndFlush(ads);
		AdvertisementBo bo = new AdvertisementBo(ads);
		bo.setBanner(this.prepareResource(ads.getBanner()));
		return bo;
	}

	private String prepareResource(String location) {
		if (this.helper.notNullAndBlank(location)) {
			return this.storageService.generatePresignedUrl(location);
		}
		return "";
	}

	public void deleteAd(String id) {
		Advertisement ad = this.advertisementRepo.findById(id).get();
		ad.setIsDeleted(true);
		this.advertisementRepo.saveAndFlush(ad);
	}

	@Override
	public AdvertisementBo fetchAd(String id) {
		log.info("Inside fetch advertisement");
		Optional<Advertisement> advertisement = this.advertisementRepo.findById(id);
		if (advertisement.isPresent()) {
			return new AdvertisementBo(advertisement.get());
		}
		throw new BadRequestException("no advertisement found with id= " + id);
	}

	@Override
	public ListResponseWithCount<AdvertisementBo> fetchAdvertisementList(PaginationBo paginationBo) {
		User user = this.authenticationService.currentUser();
		Page<Advertisement> allAdvertisements = null;
		if (this.authenticationService.isAdmin()) {
			allAdvertisements = this.advertisementRepo.findAll(PageRequest.of(paginationBo.getPageNumber(), pageSize));
		} else {
			allAdvertisements = this.advertisementRepo
					.findByUser(PageRequest.of(paginationBo.getPageNumber(), pageSize), user);
		}
		List<AdvertisementBo> bos = new ArrayList<AdvertisementBo>();
		allAdvertisements.forEach(advertisement -> bos.add(new AdvertisementBo(advertisement)));
		return new ListResponseWithCount<AdvertisementBo>(bos, "", allAdvertisements.getTotalElements(),
				paginationBo.getPageNumber(), allAdvertisements.getTotalPages());
	}

	@Override
	public AdvertisementBo updateClick(String id) {
		log.info("Inside updateClicks advertisement");
		Advertisement ads = this.advertisementRepo.getById(id);
		ads.setClicks(ads.getClicks() + 1);
		ads.setModificationDate(new Date());
		ads = this.advertisementRepo.saveAndFlush(ads);
		return new AdvertisementBo(ads);

	}

	@Override
	public AdvertisementBo updateAdsStatus(String id) {
		log.info("Inside update Ads Status");
		User user = this.authenticationService.currentUser();
		Advertisement ads = this.advertisementRepo.getById(id);
		ads.setIsActive(ads.getIsActive());
		ads.setModificationDate(new Date());
		this.updateOldAdsStatus(user);
		ads = this.advertisementRepo.saveAndFlush(ads);
		return new AdvertisementBo(ads);
	}
}

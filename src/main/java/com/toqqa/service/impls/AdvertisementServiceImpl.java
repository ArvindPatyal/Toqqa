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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.Advertisement;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.AdvertisementPayload;
import com.toqqa.payload.AdvertisementUpdate;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ToggleAdStatus;
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
		if (!this.authenticationService.isSME()) {
			throw new AccessDeniedException("user is not an sme");
		}
		User user = this.authenticationService.currentUser();
		Advertisement ads = new Advertisement();
		ads.setIsDeleted(false);
		ads.setIsActive(false);
		ads.setDescription(advertisementPayload.getDescription());
		ads.setUser(user);
		ads.setProduct(this.productRepo.findById(advertisementPayload.getProductId()).get());
		ads.setClicks(0);
		ads.setQueueDate(new Date());
		try {
			ads.setBanner(this.storageService
					.uploadFileAsync(advertisementPayload.getBanner(), user.getId(), FolderConstants.BANNER.getValue())
					.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	//	this.updateOldAdsStatus(user);
		ads = this.advertisementRepo.saveAndFlush(ads);

		ProductBo productBo = new ProductBo(ads.getProduct(),
				this.helper.prepareAttachments(ads.getProduct().getAttachments()));
		productBo.setBanner(this.helper.prepareResource(productBo.getBanner()));
		AdvertisementBo bo = new AdvertisementBo(ads, productBo);
		bo.setBanner(this.helper.prepareResource(ads.getBanner()));
		return bo;

	}


	/*private ProductBo prepareProduct(AdvertisementBo bo){
		log.info("inside prepare product");
		bo.getProduct().getImages().forEach(image -> {
			image = this.helper.prepareResource(image);
			bo.getProduct().getImages().add(image);
		});
		bo.getProduct().setBanner(this.helper.prepareResource(bo.getProduct().getBanner()));
		return bo.getProduct();
	}*/

	private void updateOldAdsStatus(User user) {
		log.info("Inside advertisement update ads Status");
		List<Advertisement> ads = this.advertisementRepo.findByUserAndIsActiveAndIsDeleted(user, true, false);
		ads.forEach(ad -> {
			ad.setIsActive(false);
			this.advertisementRepo.saveAndFlush(ad);
		});
	}

	@Override
	public AdvertisementBo updateAd(AdvertisementUpdate advertisementUpdate) {
		log.info("Inside Update Advertisement");
		if (!this.authenticationService.isSME()) {
			throw new AccessDeniedException("user is not an sme");
		}
		User user = this.authenticationService.currentUser();
		Advertisement ads = this.advertisementRepo.findById(advertisementUpdate.getId()).get();
		ads.setDescription(advertisementUpdate.getDescription());
		ads.setProduct(this.productRepo.findById(advertisementUpdate.getProductId()).get());				
		if(advertisementUpdate.getBanner()!=null& !advertisementUpdate.getBanner().isEmpty()) {
		try {
			ads.setBanner(this.storageService
					.uploadFileAsync(advertisementUpdate.getBanner(), user.getId(), FolderConstants.BANNER.getValue())
					.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}}
		ads = this.advertisementRepo.saveAndFlush(ads);
		ProductBo productBo = new ProductBo(ads.getProduct(),
				this.helper.prepareAttachments(ads.getProduct().getAttachments()));
		productBo.setBanner(this.helper.prepareResource(productBo.getBanner()));
		AdvertisementBo bo = new AdvertisementBo(ads, productBo);
		bo.setBanner(this.helper.prepareResource(ads.getBanner()));
		return bo;
	}

	public void deleteAd(String id) {
		log.info("inside delete ad");
		Advertisement ad = this.advertisementRepo.findById(id).get();
		ad.setIsDeleted(true);
		ad.setIsActive(false);
		this.advertisementRepo.saveAndFlush(ad);
	}

	@Override
	public AdvertisementBo fetchAd(String id) {
		log.info("Inside fetch advertisement");
		Optional<Advertisement> advertisement = this.advertisementRepo.findById(id);
		if (advertisement.isPresent()) {
			ProductBo productBo = new ProductBo(advertisement.get().getProduct(),
					this.helper.prepareAttachments(advertisement.get().getProduct().getAttachments()));
			productBo.setBanner(this.helper.prepareResource(productBo.getBanner()));
			AdvertisementBo bo = new AdvertisementBo(advertisement.get(), productBo);
			bo.setBanner(this.helper.prepareResource(advertisement.get().getBanner()));
			return bo;
		}
		throw new BadRequestException("no advertisement found with id= " + id);
	}

	@Override
	public ListResponseWithCount<AdvertisementBo> fetchAdvertisementList(PaginationBo paginationBo) {
		log.info("inside fetch advertisementList ");
		User user = this.authenticationService.currentUser();
		Page<Advertisement> allAdvertisements = null;
		if (this.authenticationService.isAdmin()) {
			allAdvertisements = this.advertisementRepo
					.findByIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize), false);
		} else {
			allAdvertisements = this.advertisementRepo
					.findByUserAndIsDeleted(PageRequest.of(paginationBo.getPageNumber(), pageSize), user, false);
		}
		List<AdvertisementBo> bos = new ArrayList<AdvertisementBo>();
		allAdvertisements.forEach(advertisement -> {
			ProductBo productBo = new ProductBo(advertisement.getProduct(),
					this.helper.prepareAttachments(advertisement.getProduct().getAttachments()));
			productBo.setBanner(this.helper.prepareResource(productBo.getBanner()));
			AdvertisementBo bo = new AdvertisementBo(advertisement, productBo);
			bo.setBanner(this.helper.prepareResource(bo.getBanner()));
			bos.add(bo);
		});
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
	public AdvertisementBo updateAdsStatus(ToggleAdStatus status) {
		log.info("Inside update Ads Status");
		User user = this.authenticationService.currentUser();
		Optional<Advertisement> ad = this.advertisementRepo.findById(status.getAdId());
		if (ad.isPresent()) {
			Advertisement ads = ad.get();
			ads.setIsActive(status.getStatus());			
			ads = this.advertisementRepo.saveAndFlush(ads);
			this.updateOldAdsStatus(user);
			ProductBo productBo = new ProductBo(ads.getProduct(),
					this.helper.prepareAttachments(ads.getProduct().getAttachments()));
			productBo.setBanner(this.helper.prepareResource(productBo.getBanner()));
			AdvertisementBo bo = new AdvertisementBo(ads, productBo);
			bo.setBanner(this.helper.prepareResource(bo.getBanner()));
			return bo;
		}
		throw new BadRequestException("invalid ad id " + status.getAdId());
	}

	@Override
	public List<AdvertisementBo> fetchTopActiveAdds() {
		
		List<Advertisement> ads = this.advertisementRepo.findTop10ByOrderByQueueDateAsc();
		
		List<AdvertisementBo> adds = new ArrayList<AdvertisementBo>();
		
		ads.forEach(ad ->
		{
			ProductBo productBo = new ProductBo(ad.getProduct(),
					this.helper.prepareAttachments(ad.getProduct().getAttachments()));
			productBo.setBanner(this.helper.prepareResource(productBo.getBanner()));
			AdvertisementBo bo = new AdvertisementBo(ad, productBo);
			bo.setBanner(this.helper.prepareResource(bo.getBanner()));
			adds.add(bo);
		});
		
		return adds;
	}
	
	@Scheduled(fixedRate = 14400000)
	private void resetAdds()
	{
		List<Advertisement> ads = this.advertisementRepo.findTop10ByOrderByQueueDateAsc();
		Advertisement ad = ads.get(0);
		ad.setQueueDate(new Date());
		this.advertisementRepo.saveAndFlush(ad);
	}
}

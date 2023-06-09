package com.toqqa.service.impls;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.domain.Advertisement;
import com.toqqa.domain.Product;
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
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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
        log.info("Invoked :: AdvertisementServiceImpl :: createAds()");
        if (!this.authenticationService.isSME()) {
            throw new AccessDeniedException("user is not an sme");
        }
        User user = this.authenticationService.currentUser();
        Advertisement ads = new Advertisement();
        ads.setIsDeleted(false);
        ads.setIsActive(false);
        ads.setDescription(advertisementPayload.getDescription());
        ads.setUser(user);
        Product product = this.productRepo.findById(advertisementPayload.getProductId()).orElseThrow(() -> new BadRequestException("Enter a valid product ID"));
        if (this.userAdvertisementList(user, product.getId())) {
            if (product.getIsDeleted()) {
                throw new BadRequestException(Constants.ADVERTISEMENT_NOT_CREATED);
            }
            throw new BadRequestException(Constants.ADVERTISEMENT_ALREADY_PRESENT + " " + product.getId());
        }
        ads.setProduct(product);
        ads.setClicks(0);
        ads.setQueueDate(new Date());
        try {
            ads.setBanner(this.storageService
                    .uploadFileAsync(advertisementPayload.getBanner(), user.getId(), FolderConstants.BANNER.getValue())
                    .get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // this.updateOldAdsStatus(user);
        ads = this.advertisementRepo.saveAndFlush(ads);

        ProductBo productBo = new ProductBo(ads.getProduct(),
                this.helper.prepareProductAttachments(ads.getProduct().getAttachments()));
        productBo.setBanner(this.helper.prepareAttachmentResource(ads.getProduct().getBanner()));
        AdvertisementBo bo = new AdvertisementBo(ads, productBo);
        bo.setBanner(this.helper.prepareResource(ads.getBanner()));
        return bo;

    }

    private Boolean userAdvertisementList(User user, String id) {
        List<Advertisement> ads = this.advertisementRepo.findByUserAndIsDeleted(user, false);
        return ads.stream().anyMatch(ad -> ad.getProduct().getId().equals(id));
    }

    /*
     * private ProductBo prepareProduct(AdvertisementBo bo){
     * log.info("inside prepare product"); bo.getProduct().getImages().forEach(image
     * -> { image = this.helper.prepareResource(image);
     * bo.getProduct().getImages().add(image); });
     * bo.getProduct().setBanner(this.helper.prepareResource(bo.getProduct().
     * getBanner())); return bo.getProduct(); }
     */

    public void updateOldAdsStatus(User user) {
        log.info("Invoked :: AdvertisementServiceImpl :: updateOldAdsStatus()");
        List<Advertisement> ads = this.advertisementRepo.findByUserAndIsActiveAndIsDeleted(user, true, false);
        ads.forEach(ad -> {
            ad.setIsActive(false);
            this.advertisementRepo.saveAndFlush(ad);
        });
    }

    @Override
    public AdvertisementBo updateAd(AdvertisementUpdate advertisementUpdate) {
        log.info("Invoked :: AdvertisementServiceImpl :: updateAd()");
        if (!this.authenticationService.isSME()) {
            throw new AccessDeniedException("user is not an sme");
        }
        User user = this.authenticationService.currentUser();
        Advertisement ads = this.advertisementRepo.findById(advertisementUpdate.getId()).get();
        ads.setDescription(advertisementUpdate.getDescription());
        ads.setProduct(this.productRepo.findById(advertisementUpdate.getProductId()).get());
        if (advertisementUpdate.getBanner() != null) {
            try {
                ads.setBanner(this.storageService
                        .uploadFileAsync(advertisementUpdate.getBanner(), user.getId(), FolderConstants.BANNER.getValue())
                        .get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        ads = this.advertisementRepo.saveAndFlush(ads);
        ProductBo productBo = new ProductBo(ads.getProduct(),
                this.helper.prepareProductAttachments(ads.getProduct().getAttachments()));
        productBo.setBanner(this.helper.prepareAttachmentResource(ads.getProduct().getBanner()));
        AdvertisementBo bo = new AdvertisementBo(ads, productBo);
        bo.setBanner(this.helper.prepareResource(ads.getBanner()));
        return bo;
    }

    public void deleteAd(String id) {
        log.info("Invoked :: AdvertisementServiceImpl :: deleteAd()");
        Advertisement ad = this.advertisementRepo.findById(id).get();
        ad.setIsDeleted(true);
        ad.setIsActive(false);
        this.advertisementRepo.saveAndFlush(ad);
    }

    @Override
    public AdvertisementBo fetchAd(String id) {
        log.info("Invoked :: AdvertisementServiceImpl :: fetchAd()");
        Optional<Advertisement> advertisement = this.advertisementRepo.findById(id);
        if (advertisement.isPresent()) {
            ProductBo productBo = new ProductBo(advertisement.get().getProduct(),
                    this.helper.prepareProductAttachments(advertisement.get().getProduct().getAttachments()));
            productBo.setBanner(this.helper.prepareAttachmentResource(advertisement.get().getProduct().getBanner()));
            AdvertisementBo bo = new AdvertisementBo(advertisement.get(), productBo);
            bo.setBanner(this.helper.prepareResource(advertisement.get().getBanner()));
            return bo;
        }
        throw new BadRequestException("no advertisement found with id= " + id);
    }

    @Override
    public ListResponseWithCount<AdvertisementBo> fetchAdvertisementList(PaginationBo paginationBo) {
        log.info("Invoked :: AdvertisementServiceImpl :: fetchAdvertisementList()");
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
                    this.helper.prepareProductAttachments(advertisement.getProduct().getAttachments()));
            productBo.setBanner(this.helper.prepareAttachmentResource(advertisement.getProduct().getBanner()));
            AdvertisementBo bo = new AdvertisementBo(advertisement, productBo);
            bo.setBanner(this.helper.prepareResource(bo.getBanner()));
            bos.add(bo);
        });
        return new ListResponseWithCount<AdvertisementBo>(bos, "",
                allAdvertisements.getTotalElements(), paginationBo.getPageNumber(), allAdvertisements.getTotalPages());
    }

    @Override
    public AdvertisementBo updateClick(String id) {
        log.info("Invoked :: AdvertisementServiceImpl :: updateClick()");
        Advertisement ads = this.advertisementRepo.getById(id);
        ads.setClicks(ads.getClicks() + 1);
        ads.setModificationDate(new Date());
        ads = this.advertisementRepo.saveAndFlush(ads);
        return new AdvertisementBo(ads);
    }

    @Override
    public AdvertisementBo updateAdsStatus(ToggleAdStatus status) {
        log.info("Invoked :: AdvertisementServiceImpl :: updateAdsStatus()");
        User user = this.authenticationService.currentUser();
        Optional<Advertisement> ad = this.advertisementRepo.findById(status.getAdId());
        if (ad.isPresent()) {
            if (status.getStatus()) {
                this.updateOldAdsStatus(user);
            }
            Advertisement ads = ad.get();
            ads.setIsActive(status.getStatus());
            ads = this.advertisementRepo.saveAndFlush(ads);
            ProductBo productBo = new ProductBo(ads.getProduct(),
                    this.helper.prepareProductAttachments(ads.getProduct().getAttachments()));
            productBo.setBanner(this.helper.prepareAttachmentResource(ads.getProduct().getBanner()));
            AdvertisementBo bo = new AdvertisementBo(ads, productBo);
            bo.setBanner(this.helper.prepareResource(bo.getBanner()));
            return bo;
        }
        throw new BadRequestException("invalid ad id " + status.getAdId());
    }

    @Override
    public List<AdvertisementBo> fetchTopActiveAdds() {
        log.info("Invoked :: AdvertisementServiceImpl :: fetchTopActiveAdds()");
        List<Advertisement> ads = this.advertisementRepo.findTop10ByIsActiveOrderByQueueDateAsc(true);
        List<AdvertisementBo> adds = new ArrayList<AdvertisementBo>();

        ads.forEach(ad -> {
            ProductBo productBo = new ProductBo(ad.getProduct(),
                    this.helper.prepareProductAttachments(ad.getProduct().getAttachments()));
            productBo.setBanner(this.helper.prepareAttachmentResource(ad.getProduct().getBanner()));
            AdvertisementBo bo = new AdvertisementBo(ad, productBo);
            bo.setBanner(this.helper.prepareResource(bo.getBanner()));
            adds.add(bo);
        });

        return adds;
    }

    @Scheduled(fixedRate = 14400000, initialDelay = 14400000)
    private void resetAdds() {
        log.info("Invoked :: AdvertisementServiceImpl :: resetAdds()");
        List<Advertisement> ads = this.advertisementRepo.findTop10ByIsActiveOrderByQueueDateAsc(true);
        if (this.helper.notNullAndHavingData(ads)) {
            Advertisement ad = ads.get(0);
            ad.setQueueDate(new Date());
            this.advertisementRepo.saveAndFlush(ad);
        }
    }

	/*@Override
	// @Scheduled(fixedRate = 14400000)
	public List<AdvertisementBo> alotQueueNumber(List<AdvertisementBo> bos) {

		List<Advertisement> listAds = advertisementRepo.findByIsActiveOrderByQueueDateAsc(true);

		List<Advertisement> topListad = advertisementRepo.findTop10ByIsActiveOrderByQueueDateAsc(true);

		listAds.removeIf(x -> topListad.contains(x));

		List<AdvertisementBo> adsBo = new ArrayList<>();

		List<AdvertisementBo> adsbos = new ArrayList<>();

		for (Advertisement ads : listAds) {

			ProductBo productBo = new ProductBo(ads.getProduct(),
					this.helper.prepareProductAttachments(ads.getProduct().getAttachments()));
			productBo.setBanner(this.helper.prepareAttachmentResource(ads.getProduct().getBanner()));
			AdvertisementBo adbo = new AdvertisementBo(ads, productBo);
			adbo.setQueueNumber(listAds.indexOf(ads) + 1);
			adsBo.add(adbo);

			for (AdvertisementBo adbs : bos) {
				if (ads.getId().equals(adbs.getId())) {
					adbs.setQueueNumber(adbo.getQueueNumber());
					adsbos.add(adbs);
				}

			}
		}

		return adsbos;
	}*/
}

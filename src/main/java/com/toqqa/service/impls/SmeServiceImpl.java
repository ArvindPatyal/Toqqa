package com.toqqa.service.impls;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.bo.ProductCategoryBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.FolderConstants;
import com.toqqa.constants.OrderStatus;
import com.toqqa.constants.RoleConstants;
import com.toqqa.constants.VerificationStatusConstants;
import com.toqqa.domain.*;
import com.toqqa.dto.NearbySmeRespDto;
import com.toqqa.dto.SmeStatsResponseDto;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.InvalidAccessException;
import com.toqqa.exception.ResourceNotFoundException;
import com.toqqa.payload.*;
import com.toqqa.repository.*;
import com.toqqa.service.*;
import com.toqqa.util.Constants;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class SmeServiceImpl implements SmeService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private SmeRepository smeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private StorageService storageService;

    @Autowired
    private Helper helper;

    @Value("${pageSize}")
    private Integer pageSize;

    @Autowired
    @Lazy
    private FavouriteService favouriteService;

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DeliveryAddressServiceImpl deliveryAddressService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private VerificationStatusService statusService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SmeBo smeRegistration(SmeRegistration smeRegistration, String userId, boolean isNewUser) {
        log.info("Invoked :: SmeServiceImpl :: smeRegistration()");
        if (!alreadySme(userId)) {
            try {
                Sme sme = new Sme();
                sme.setNameOfBusiness(smeRegistration.getNameOfBusiness());
                sme.setBusinessAddress(smeRegistration.getBusinessAddress());
                sme.setState(smeRegistration.getState());
                sme.setCountry(smeRegistration.getCountry());
                sme.setTypeOfBusiness(smeRegistration.getTypeOfBusiness());
                sme.setDeliveryRadius(smeRegistration.getDeliveryRadius());
                sme.setDeliveryCharges(smeRegistration.getDeliveryCharge());
                sme.setIsDeleted(false);
                sme.setDescription(smeRegistration.getDescription());
                sme.setCity(smeRegistration.getCity());
                sme.setIsDeliverToCustomer(smeRegistration.getDeliverToCustomer());
                sme.setIsRegisterWithGovt(smeRegistration.getIsRegisteredWithGovt());
                sme.setLatitude(smeRegistration.getLatitude());
                sme.setLongitude(smeRegistration.getLongitude());

                if (smeRegistration.getStartTimeOfDelivery() != null
                        && smeRegistration.getEndTimeOfDelivery() != null) {
                    sme.setStartTimeOfDelivery(new Date(smeRegistration.getStartTimeOfDelivery()));
                    sme.setEndTimeOfDelivery(new Date(smeRegistration.getEndTimeOfDelivery()));
                }

                sme.setUserId(userId);
                sme.setBusinessCatagory(this.categoryRepository.findAllById(smeRegistration.getBusinessCategory()));
                sme.setBusinessSubCatagory(
                        this.subcategoryRepository.findAllById(smeRegistration.getBusinessSubCategory()));
                User user = this.userRepo.findById(userId).get();
                List<Role> roles = new ArrayList<Role>();
                roles.addAll(user.getRoles());
                roles.add(this.roleRepo.findByRole(RoleConstants.SME.getValue()));
                user.setRoles(roles);
                user = this.userRepo.saveAndFlush(user);

                try {
                    if (smeRegistration.getIdProof() != null && !smeRegistration.getIdProof().isEmpty()) {
                        sme.setIdProof(this.storageService.uploadFileAsync(smeRegistration.getIdProof(), userId,
                                FolderConstants.DOCUMENTS.getValue()).get());
                    }
                    if (smeRegistration.getBusinessLogo() != null && !smeRegistration.getBusinessLogo().isEmpty()) {
                        sme.setBusinessLogo(this.storageService.uploadFileAsync(smeRegistration.getBusinessLogo(),
                                userId, FolderConstants.LOGO.getValue()).get());
                    }
                    if (smeRegistration.getRegDoc() != null && !smeRegistration.getRegDoc().isEmpty()) {
                        sme.setRegDoc(this.storageService.uploadFileAsync(smeRegistration.getRegDoc(), userId,
                                FolderConstants.DOCUMENTS.getValue()).get());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                sme = this.smeRepo.saveAndFlush(sme);
                SmeBo bo = new SmeBo(sme);
                bo.setRegDoc(this.prepareResource(sme.getRegDoc()));
                bo.setIdProof(this.prepareResource(sme.getIdProof()));
                bo.setBusinessLogo(this.prepareResource(sme.getBusinessLogo()));

                VerificationStatus status = new VerificationStatus();
                status.setStatus(VerificationStatusConstants.PENDING);
                status.setUser(user);
                status.setRole(RoleConstants.SME.getValue());
                this.statusService.createVerificationStatus(status);

                return bo;
            } catch (Exception e) {
                log.error("unable to create sme", e);
                if (isNewUser == true) {
                    this.userRepo.deleteById(userId);
                }

            }
        }
        throw new BadRequestException("user already sme");
    }


    private Boolean alreadySme(String id) {
        log.info("Invoked :: SmeServiceImpl :: alreadySme()");
        User user = this.userRepo.findById(id).get();
        return user.getRoles().stream().anyMatch(role -> role.getRole().equals(RoleConstants.SME.getValue()));
    }

    private String prepareResource(String location) {
        if (this.helper.notNullAndBlank(location)) {
            return this.storageService.generatePresignedUrl(location);
        }
        return "";
    }

    @Override
    public SmeBo smeUpdate(SmeUpdate smeUpdate) {
        log.info("Invoked :: SmeServiceImpl :: smeUpdate()");
        User user = this.authenticationService.currentUser();
        Sme sme = this.smeRepo.findByUserId(user.getId());
        if (sme != null) {
            sme.setNameOfBusiness(smeUpdate.getNameOfBusiness());
            sme.setBusinessAddress(smeUpdate.getBusinessAddress());
            sme.setState(smeUpdate.getState());
            sme.setCountry(smeUpdate.getCountry());
            sme.setTypeOfBusiness(smeUpdate.getTypeOfBusiness());
            sme.setDeliveryRadius(smeUpdate.getDeliveryRadius());
            sme.setDeliveryCharges(smeUpdate.getDeliveryCharge());
            sme.setDescription(smeUpdate.getDescription());
            sme.setCity(smeUpdate.getCity());
            sme.setIsDeliverToCustomer(smeUpdate.getDeliverToCustomer());
            sme.setLatitude(smeUpdate.getLatitude());
            sme.setLongitude(smeUpdate.getLongitude());
            if (smeUpdate.getStartTimeOfDelivery() != null && smeUpdate.getEndTimeOfDelivery() != null) {
                sme.setStartTimeOfDelivery(new Date(smeUpdate.getStartTimeOfDelivery()));
                sme.setEndTimeOfDelivery(new Date(smeUpdate.getEndTimeOfDelivery()));
            }
            sme.setBusinessCatagory(this.categoryRepository.findAllById(smeUpdate.getBusinessCategory()));
            if (smeUpdate.getBusinessSubCategory() != null) {
                sme.setBusinessSubCatagory(this.subcategoryRepository.findAllById(smeUpdate.getBusinessSubCategory()));
            }
            try {
                if (smeUpdate.getBusinessLogo() != null && !smeUpdate.getBusinessLogo().isEmpty()) {
                    sme.setBusinessLogo(this.storageService.uploadFileAsync(smeUpdate.getBusinessLogo(),
                            sme.getUserId(), FolderConstants.LOGO.getValue()).get());
                }
                if (sme.getRegDoc() == null) {
                    if (smeUpdate.getRegDoc() != null && !smeUpdate.getRegDoc().isEmpty()) {
                        sme.setRegDoc(this.storageService.uploadFileAsync(smeUpdate.getRegDoc(), sme.getUserId(),
                                FolderConstants.DOCUMENTS.getValue()).get());
                    }

                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            sme = this.smeRepo.saveAndFlush(sme);
            SmeBo bo = new SmeBo(sme);
            bo.setRegDoc(this.prepareResource(sme.getRegDoc()));
            bo.setIdProof(this.prepareResource(sme.getIdProof()));
            bo.setBusinessLogo(this.prepareResource(sme.getBusinessLogo()));
            return bo;
        }
        throw new BadRequestException("User is not a Sme");

    }

    @Override
    public SmeBo fetchSme(String id) {
        log.info("Invoked :: SmeServiceImpl :: fetchSme()");
        Sme sme = this.smeRepo.findByUserId(id);
        if (sme != null) {
            SmeBo bo = this.toSmeBo(sme);
            bo.setRegDoc(this.prepareResource(sme.getRegDoc()));
            bo.setIdProof(this.prepareResource(sme.getIdProof()));
            bo.setIsFavSme(this.favouriteService.isFavSme(bo,
                    this.favouriteRepository.findByUser(this.authenticationService.currentUser())));
            return bo;
        }
        throw new BadRequestException("no user found with id= " + id);
    }

//    @Override
//    public ListResponseWithCount<SmeBo> fetchSmeList(PaginationBo bo){
//        Page<Sme> smes = this.smeRepo.findByIsDeleted(PageRequest.of(bo.getPageNumber(), pageSize), false);
//        List<SmeBo> smeBoList= new ArrayList<>();
//        smes.get().forEach(sme -> {
//            SmeBo smeBo = new SmeBo(sme);
//            smeBo.setBusinessLogo(this.helper.prepareResource(smeBo.getBusinessLogo()));
//            smeBo.setIsFavSme(this.favouriteService.isFavSme(smeBo,this.favouriteRepository.findByUser(this.authenticationService.currentUser())));
//            smeBoList.add(smeBo);
//        });
//        return new ListResponseWithCount<>(smeBoList,"",smes.getTotalElements(),bo.getPageNumber(),smes.getTotalPages());
//    }

    @Override
    public ListResponse<SmeBo> fetchSmeListWithoutPagination() {
        List<Sme> smeObjList = getAllSme(Boolean.FALSE);
        List<SmeBo> smeBoList = new ArrayList<>();
        smeObjList.forEach(sme -> {
            SmeBo smeBo = this.toSmeBo(sme);
            smeBo.setIsFavSme(this.favouriteService.isFavSme(smeBo,
                    this.favouriteRepository.findByUser(this.authenticationService.currentUser())));
            smeBoList.add(smeBo);
        });
        return new ListResponse<>(smeBoList, "");
    }

    @Override
    public ListResponseWithCount fetchProductsList(ProductRequestFilter productRequestFilter) {
        log.info("Inside get sme Product List");
        String userId = this.authenticationService.currentUser().getId();
        productRequestFilter.setSmeUserId(userId);
        return this.fetchProducts(productRequestFilter);
    }

    @Override
    public ListResponseWithCount fetchProducts(ProductRequestFilter productRequestFilter) {
        log.info("Inside get product list");
        Page<Product> products = null;
//        productRequestFilter.setUserId(this.authenticationService.currentUser().getId());
        if (!this.helper.notNullAndHavingData(productRequestFilter.getProductCategoryIds())) {
            products = this.productRepository.findByUser_IdAndIsDeleted(
                    PageRequest.of(productRequestFilter.getPageNumber(), pageSize), productRequestFilter.getSmeUserId(),
                    productRequestFilter.getIsInActive());
            List<ProductBo> productList = new ArrayList<>();
            products.forEach(product -> productList.add(this.productService.toProductBo(product)));
            return new ListResponseWithCount(productList, "", products.getTotalElements(),
                    productRequestFilter.getPageNumber(), products.getTotalPages());
        } else {
            products = this.productRepository.findByProductCategories_IdInAndIsDeletedAndUser_Id(
                    PageRequest.of(productRequestFilter.getPageNumber(), pageSize),
                    productRequestFilter.getProductCategoryIds(), productRequestFilter.getIsInActive(),
                    productRequestFilter.getSmeUserId());
            List<ProductBo> productBos = new ArrayList<>();
            products.forEach(product -> {
                productBos.add(this.productService.toProductBo(product));
            });
            return new ListResponseWithCount(productBos, "", products.getTotalElements(),
                    (productRequestFilter.getPageNumber()), (products.getTotalPages()));
        }

    }

    @Override
    public List<Sme> getAllSme(Boolean isDeleted) {
        return smeRepo.findAll(isDeleted);
    }

    @Override
    public Sme getSmeByUserId(String userId) {
        return smeRepo.findByUserId(userId);
    }

    @Override
    public List<NearbySmeRespDto> getNearbySme() {
        log.info("Invoked :: SmeServiceImpl :: getNearbySme()");

        Optional<DeliveryAddress> deliveryAddObj = deliveryAddressService
                .getCurrentDelAddress(authenticationService.currentUser());

        List<NearbySmeRespDto> dtoList = new ArrayList<NearbySmeRespDto>(0);

        if (deliveryAddObj.isPresent()) {
            for (Sme smeObj : getAllSme(Boolean.FALSE)) {
                if (deliveryAddObj.get().getLatitude() != null && deliveryAddObj.get().getLongitude() != null
                        && smeObj.getLatitude() != null && smeObj.getLongitude() != null) {

                    int distance = computeDistance(deliveryAddObj.get().getLatitude(),
                            deliveryAddObj.get().getLongitude(), smeObj.getLatitude(), smeObj.getLongitude());

                    if (distance <= Constants.MIN_DISTANCE) {
                        List<Integer> integers = new ArrayList<>();
                        smeObj.getSellerRatings().forEach(sellerRating -> integers.add(sellerRating.getSellerRating()));
                        OptionalDouble average = integers.stream().mapToDouble(value -> value).average();
                        dtoList.add(new NearbySmeRespDto(smeObj.getUserId(), smeObj.getNameOfBusiness(),
                                smeObj.getBusinessAddress(), smeObj.getLatitude(), smeObj.getLongitude(),
                                bindLogoUrl(smeObj.getBusinessLogo()),
                                average.isPresent() ? average.getAsDouble() : 0.0));
                    }
                }
            }
        } else {
            throw new ResourceNotFoundException(Constants.ERR_NO_CURRENT_ADDRESS);
        }

        return dtoList;
    }

    private String bindLogoUrl(String logoString) {
        FileBo urlObject = helper.prepareAttachmentResource(logoString);
        String imageUrl = Constants.MSG_NO_BUSINESS_LOGO;
        if (urlObject != null) {
            imageUrl = urlObject.getImageUrl();

        }
        return imageUrl;
    }

    private int computeDistance(Double userLat, Double userLong, Double smeLat, Double smeLong) {
        log.info("Invoked :: SmeServiceImpl :: computeDistance()");

        double theta = userLong - smeLong;
        double distance = Math.sin(helper.deg2rad(userLat)) * Math.sin(helper.deg2rad(smeLat))
                + Math.cos(helper.deg2rad(userLat)) * Math.cos(helper.deg2rad(smeLat))
                * Math.cos(helper.deg2rad(theta));
        distance = Math.acos(distance);
        distance = helper.rad2deg(distance);
        distance = distance * 60 * 1.1515;
        distance = distance * 1.609344;
        return (int) distance;
    }

    @SuppressWarnings("unused")
    @Override
    public SmeStatsResponseDto getOverallStatsByDate(LocalDate startDate, LocalDate endDate) {
        log.info("Invoked :: SmeServiceImpl :: getOverallStats() :: with params :: startDate=" + startDate
                + " :: endDate=" + endDate);

        Sme smeUser = getSmeByUserId(authenticationService.currentUser().getId());
        SmeStatsResponseDto responseObj = null;

        if (smeUser == null) {
            throw new InvalidAccessException(Constants.ERR_USER_NOT_SME);

        } else {

            Optional<Integer> totalOrderAmount = orderInfoService.getDeliveredOrderCountBySmeAndDate(smeUser.getId(),
                    startDate, endDate);

            Optional<Integer> totalOrderQty = orderItemService.getDeleviredQtyCountBySmeAndDate(smeUser.getId(),
                    startDate, endDate);
            Optional<Integer> totalOrderDelivered = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.DELIVERED.name(), startDate, endDate);
            Optional<Integer> totalOrderCancelled = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.CANCELLED.name(), startDate, endDate);
            Optional<Integer> totalOrderPlaced = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.PLACED.name(), startDate, endDate);
            Optional<Integer> totalOrderReceived = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.RECEIVED.name(), startDate, endDate);
            Optional<Integer> totalOrderConfirmed = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.CONFIRMED.name(), startDate, endDate);
            Optional<Integer> totalOrderOut = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.OUT_FOR_DELIVERY.name(), startDate, endDate);
            Optional<Integer> totalOrderDispatch = orderInfoService.getOrderCountBySmeAndDateAndStatus(smeUser.getId(),
                    OrderStatus.READY_FOR_DISPATCH.name(), startDate, endDate);

            responseObj = new SmeStatsResponseDto(totalOrderAmount.isPresent() ? totalOrderAmount.get() : 0,
                    totalOrderQty.isPresent() ? totalOrderQty.get() : 0,
                    totalOrderDelivered.isPresent() ? totalOrderDelivered.get() : 0,
                    totalOrderCancelled.isPresent() ? totalOrderCancelled.get() : 0,
                    totalOrderPlaced.isPresent() ? totalOrderPlaced.get() : 0,
                    totalOrderReceived.isPresent() ? totalOrderReceived.get() : 0,
                    totalOrderConfirmed.isPresent() ? totalOrderConfirmed.get() : 0,
                    totalOrderOut.isPresent() ? totalOrderOut.get() : 0,
                    totalOrderDispatch.isPresent() ? totalOrderDispatch.get() : 0);
        }

        return responseObj;
    }

    @Override
    public ListResponse smeProductCategories() {
        log.info("Invoked :: SmeServiceImpl :::smeProductCategories()");
        User user = this.authenticationService.currentUser();
        Map<String, String> productCategories = new HashMap<>();
        this.productRepository.findByUser(user).forEach
                (product -> product.getProductCategories().forEach
                        (productCategory -> productCategories.put(productCategory.getId(), productCategory.getProductCategory())));
        List<ProductCategoryBo> productCategoryBos = new ArrayList<>();
        productCategories.forEach((id, name) ->
                productCategoryBos.add(new ProductCategoryBo(id, name)));
        return new ListResponse(productCategoryBos, "categories returned");
    }

    @Override
    public SmeBo becomeASme(SmeRegistration smeRegistration) {
        log.info("Invoked :: SmeServiceImpl :: becomeASme()");
        User user = this.authenticationService.currentUser();
        return this.smeRegistration(smeRegistration, user.getId(), false);
    }

    @Override
    public SmeBo toSmeBo(Sme sme) {
        SmeBo smeBo = null;
        if (sme != null) {
            smeBo = new SmeBo(sme);
            smeBo.setBusinessLogo(this.helper.prepareResource(smeBo.getBusinessLogo()));
            if (!sme.getSellerRatings().isEmpty()) {
                List<Integer> ratings = new ArrayList<>();
                List<String> reviews = new ArrayList<>();
                sme.getSellerRatings().forEach(sellerRating -> {
                    ratings.add(sellerRating.getSellerRating());
                    reviews.add(sellerRating.getReviewComment());
                });
                OptionalDouble average = ratings.stream().mapToDouble(value -> value).average();
                reviews.removeAll(Collections.singletonList(null));
                smeBo.setTotalReviews(reviews.size());
                smeBo.setAverageRating(average.isPresent() ? average.getAsDouble() : 0.0);
            }
        }
        return smeBo;
    }
}



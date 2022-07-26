package com.toqqa.service.impls;

import com.toqqa.bo.RatingReviewBo;
import com.toqqa.bo.RatingStatsBo;
import com.toqqa.bo.SellerRatingBo;
import com.toqqa.constants.OrderStatus;
import com.toqqa.domain.OrderInfo;
import com.toqqa.domain.SellerRating;
import com.toqqa.domain.Sme;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.payload.SellerRatingPayload;
import com.toqqa.payload.SellerRatings;
import com.toqqa.repository.OrderInfoRepository;
import com.toqqa.repository.SellerRatingRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.SellerRatingService;
import com.toqqa.service.UserService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SellerRatingServiceImpl implements SellerRatingService {
    @Autowired
    private SmeRepository smeRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private SellerRatingRepository sellerRatingRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private Helper helper;

    @Override
    public Response rateSeller(SellerRatingPayload sellerRatingPayload) {
        log.info("Invoked :: SellerRatingServiceImpl :: rateSeller()");
        User user = this.authenticationService.currentUser();
        if (this.sellerRatingRepository.findBySmeIdAndUser_Id(sellerRatingPayload.getSmeId(), user.getId()) == null) {
            List<Sme> smeList = new ArrayList<>();
            List<OrderInfo> orderInfos = this.orderInfoRepository.findByUser_IdAndOrderStatus(user.getId(), OrderStatus.DELIVERED);
            if (orderInfos==null) {
                throw new BadRequestException("you have not ordered anything yet or order is yet to be delivered");
            }

            orderInfos.forEach(orderInfo -> smeList.add(orderInfo.getSme()));
            Optional<Sme> optionalSme = smeList.stream().filter(s -> s.getId().equals(sellerRatingPayload.getSmeId())).findFirst();
            if (!optionalSme.isPresent()) {
                throw new BadRequestException("Enter a valid smeId!!!!");
            }
            Sme sme = optionalSme.get();
            SellerRating sellerRating = new SellerRating();
            sellerRating.setSellerRating(sellerRatingPayload.getStars());
            sellerRating.setSme(sme);
            if (sellerRatingPayload.getReviewComment() != null) {
                sellerRating.setReviewComment(sellerRatingPayload.getReviewComment());
            }
            sellerRating.setReviewComment(sellerRating.getReviewComment());
            sellerRating.setUser(user);
            sellerRating = this.sellerRatingRepository.saveAndFlush(sellerRating);
            this.pushNotificationService.sendNotificationToSmeForRating(this.userService.getById(sme.getUserId()));
            return new Response(new SellerRatingBo(sellerRating), "Seller rated Successfully");
        } else {
            throw new BadRequestException("Already reviewed this Seller");
        }
    }

    @Override
    public Response sellerRatings(SellerRatings getSellerRatings) {
        log.info("Invoked :: SellerRatingServiceImpl :: sellerRatings()");
        Optional<Sme> optionalSme = this.smeRepository.findById(getSellerRatings.getSellerId());
        if (!optionalSme.isPresent()) {
            throw new BadRequestException("Enter a valid product id");
        }
        Sme sme = optionalSme.get();
        List<SellerRating> sellerRatings = sme.getSellerRatings();
        if (!sellerRatings.isEmpty()) {
            RatingStatsBo ratingStatsBo = new RatingStatsBo();
            List<RatingReviewBo> ratingReviewBos = new ArrayList<>();
            Integer totalRatings = sellerRatings.size();
            List<Integer> ratings = new ArrayList<>();
            sellerRatings.forEach(sellerRating -> {
                ratings.add(sellerRating.getSellerRating());
                if (sellerRating.getUser().getProfilePicture() != null) {
                    sellerRating.getUser().setProfilePicture(this.helper.prepareResource(sellerRating.getUser().getProfilePicture()));
                }
                ratingReviewBos.add(new RatingReviewBo(
                        sellerRating.getId(),
                        sellerRating.getUser().getFirstName(),
                        sellerRating.getUser().getProfilePicture(),
                        sellerRating.getSellerRating(),
                        sellerRating.getReviewComment(),
                        sellerRating.getDateOfRatingCreation(),
                        sellerRating.getDateOfRatingUpdation(),
                        sellerRating.getUser().getId()
                ));
            });
            ratingReviewBos.removeIf(ratingReviewBo -> ratingReviewBo.getReviewComment() == null);
            ratingReviewBos.sort(Comparator.comparing(RatingReviewBo::getDateOfRatingUpdate).reversed());
            ratingStatsBo.setTotalReviewComments(ratingReviewBos.size());


            Optional<RatingReviewBo> optionalRatingReviewBo = ratingReviewBos.stream().filter(ratingReviewBo1 -> ratingReviewBo1.getUserId().equals(getSellerRatings.getUserId())).findFirst();
            if (optionalRatingReviewBo.isPresent()) {
                RatingReviewBo ratingReviewBo = optionalRatingReviewBo.get();
                ratingReviewBos.remove(ratingReviewBo);
                ratingReviewBos.add(0, ratingReviewBo);
            }

            OptionalDouble average = ratings.stream().mapToDouble(value -> value).average();
            ratingStatsBo.setAverageRating(average.isPresent() ? average.getAsDouble() : 0.0);

            ratingStatsBo.setFiveStars(Collections.frequency(ratings, 5));
            ratingStatsBo.setFourStars(Collections.frequency(ratings, 4));
            ratingStatsBo.setThreeStars(Collections.frequency(ratings, 3));
            ratingStatsBo.setTwoStars(Collections.frequency(ratings, 2));
            ratingStatsBo.setOneStar(Collections.frequency(ratings, 1));

            ratingStatsBo.setTotalRatings(totalRatings);
            ratingStatsBo.setRatingReviewBos(ratingReviewBos);
            return new Response(ratingStatsBo, "ratings returned");
        }
        return new Response("", "NO Ratings found for this Seller");
    }

    @Override
    public Response deleteSellerRating(String ratingId) {
        log.info("Invoked :: SellerRatingServiceImpl :: deleteSellerRating()");
        User user = this.authenticationService.currentUser();
        SellerRating sellerRating = this.sellerRatingRepository.findByIdAndUser_Id(ratingId, user.getId());
        if (sellerRating == null) {
            throw new BadRequestException("Rating " + ratingId + " not found associated with this user");
        }
        this.sellerRatingRepository.deleteById(ratingId);
        return new Response("", "Rating deleted SuccessFully");
    }

    @Override
    public Response updateSellerRating(RatingUpdatePayload ratingUpdatePayload) {
        log.info("Invoked :: SellerRatingServiceImpl :: updateSellerRating()");
        User user = this.authenticationService.currentUser();
        SellerRating sellerRating = this.sellerRatingRepository.findByIdAndUser_Id(ratingUpdatePayload.getRatingId(), user.getId());
        if (sellerRating == null) {
            throw new BadRequestException("Rating " + ratingUpdatePayload.getRatingId() + " not found associated with this user");
        }
        sellerRating.setSellerRating(ratingUpdatePayload.getRatingStars());
        if (ratingUpdatePayload.getReviewComment() != null) {
            sellerRating.setReviewComment(ratingUpdatePayload.getReviewComment());
        }
        sellerRating = this.sellerRatingRepository.saveAndFlush(sellerRating);
        return new Response(sellerRating, "Rating updated successfully");
    }
}


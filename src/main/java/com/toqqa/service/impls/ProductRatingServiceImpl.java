package com.toqqa.service.impls;

import com.toqqa.bo.ProductRatingBo;
import com.toqqa.bo.RatingReviewBo;
import com.toqqa.bo.RatingStatsBo;
import com.toqqa.constants.OrderStatus;
import com.toqqa.domain.*;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.ProductRatingPayload;
import com.toqqa.payload.RatingUpdatePayload;
import com.toqqa.payload.Response;
import com.toqqa.repository.OrderInfoRepository;
import com.toqqa.repository.ProductRatingRepository;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.ProductRatingService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductRatingServiceImpl implements ProductRatingService {
    @Autowired
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    @Lazy
    private AuthenticationService authenticationService;
    @Autowired
    private ProductRatingRepository productRatingRepository;

    @Autowired
    private Helper helper;

    @Override
    public Response rateProduct(ProductRatingPayload productRatingPayload) {
        log.info("Invoked :: ProductRatingServiceImpl :: rateProduct()");
        User user = this.authenticationService.currentUser();
        if (this.productRatingRepository.findByProductIdAndUser(productRatingPayload.getProductId(), user) == null) {
            List<OrderItem> orderItems = new ArrayList<>();
            List<OrderInfo> orderInfos = this.orderInfoRepository.findByUser_IdAndOrderStatus(user.getId(), OrderStatus.DELIVERED);
            if (orderInfos.isEmpty()) {
                throw new BadRequestException("you have not ordered anything yet or order is yet to be delivered");
            }

            orderInfos.forEach(orderInfo -> orderItems.addAll(orderInfo.getOrderItems()));
            Optional<OrderItem> optionalOrderItem = orderItems.stream().filter(
                    item -> item.getProduct().getId().equals(productRatingPayload.getProductId())).findFirst();
            if (!optionalOrderItem.isPresent()) {
                throw new BadRequestException("You have not purchased this product yet!!!!");
            }
            Product product = optionalOrderItem.get().getProduct();
            ProductRating productRating = new ProductRating();
            productRating.setStars(productRatingPayload.getStars());
            if (productRatingPayload.getReviewComment() != null) {
                productRating.setReviewComment(productRatingPayload.getReviewComment());
            }
            productRating.setProduct(product);
            productRating.setUser(user);
            productRating = this.productRatingRepository.saveAndFlush(productRating);
            return new Response(new ProductRatingBo(productRating), "Product rated Successfully");
        } else {
            throw new BadRequestException("Already reviewed this product");
        }
    }

    @Override
    public Response productRatings(String productId) {
        log.info("Invoked :: ProductRatingServiceImpl :: ratingDetails()");
        Optional<Product> optionalProduct = this.productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new BadRequestException("Enter a valid product id");
        }
        Product product = optionalProduct.get();
        List<ProductRating> productRatings = product.getProductRatings();
        if (!productRatings.isEmpty()) {
            RatingStatsBo ratingStatsBo = new RatingStatsBo();
            List<RatingReviewBo> ratingReviewBos = new ArrayList<>();
            Integer totalRatings = productRatings.size();
            List<Integer> ratings = new ArrayList<>();

            productRatings.forEach(productRating -> {
                ratings.add(productRating.getStars());
                if (productRating.getUser().getProfilePicture() != null) {
                    productRating.getUser().setProfilePicture(this.helper.prepareResource(productRating.getUser().getProfilePicture()));
                }
                ratingReviewBos.add(new RatingReviewBo(
                        productRating.getId(),
                        productRating.getUser().getFirstName(),
                        productRating.getUser().getProfilePicture(),
                        productRating.getStars(),
                        productRating.getReviewComment(),
                        productRating.getDateOfRatingCreation(),
                        productRating.getDateOfRatingUpdation()
                ));
            });
            Double sumOfRatings = Double.valueOf(ratings.stream().mapToInt(Integer::intValue).sum());
            ratingReviewBos.removeIf(ratingReviewBo -> ratingReviewBo.getReviewComment() == null);
            ratingStatsBo.setTotalReviewComments(ratingReviewBos.size());
            ratingStatsBo.setAverageRating(sumOfRatings / totalRatings);
            ratingStatsBo.setFiveStars(Collections.frequency(ratings, 5));
            ratingStatsBo.setFourStars(Collections.frequency(ratings, 4));
            ratingStatsBo.setThreeStars(Collections.frequency(ratings, 3));
            ratingStatsBo.setTwoStars(Collections.frequency(ratings, 2));
            ratingStatsBo.setOneStar(Collections.frequency(ratings, 1));
            ratingStatsBo.setTotalRatings(totalRatings);
            ratingStatsBo.setRatingReviewBos(ratingReviewBos);
            return new Response(ratingStatsBo, "ratings returned");
        }
        return new Response("", "NO Ratings found for this product");
    }

    @Override
    public Response deleteProductRating(String ratingId) {
        log.info("Invoked :: ProductRatingServiceImpl :: deleteProductRating()");
        User user = this.authenticationService.currentUser();
        ProductRating productRating = this.productRatingRepository.findByIdAndUser_Id(ratingId, user.getId());
        if (productRating == null) {
            throw new BadRequestException("Rating " + ratingId + " not found associated with this user");
        }
        this.productRatingRepository.deleteById(ratingId);
        return new Response("", "Rating deleted SuccessFully");
    }

    @Override
    public Response updateProductRating(RatingUpdatePayload ratingUpdatePayload) {
        log.info("Invoked :: ProductRatingServiceImpl :: updateProductRating()");
        User user = this.authenticationService.currentUser();
        ProductRating productRating = this.productRatingRepository.findByIdAndUser_Id(ratingUpdatePayload.getRatingId(), user.getId());
        if (productRating == null) {
            throw new BadRequestException("Rating " + ratingUpdatePayload.getRatingId() + " not found associated with this user");
        }
        productRating.setStars(ratingUpdatePayload.getRatingStars());
        if (ratingUpdatePayload.getReviewComment() != null) {
            productRating.setReviewComment(ratingUpdatePayload.getReviewComment());
        }
        productRating = this.productRatingRepository.saveAndFlush(productRating);

        return new Response(new ProductRatingBo(productRating), "Rating updated successfully");
    }
}

package com.toqqa.bo;

import com.toqqa.domain.Product;
import com.toqqa.domain.ProductRating;
import com.toqqa.domain.SellerRating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatingBo {
    private String ratingId;
    private Integer rating;
    private String ratingComment;
    private Date ratingCreationDate;
    private Date ratingUpdationDate;

    public ProductRatingBo(ProductRating productRating){
        this.ratingId= productRating.getId();
        this.rating= productRating.getStars();
        this.ratingComment= productRating.getReviewComment();
        this.ratingCreationDate=productRating.getDateOfRatingCreation();
        this.ratingUpdationDate=productRating.getDateOfRatingUpdation();

    }

}

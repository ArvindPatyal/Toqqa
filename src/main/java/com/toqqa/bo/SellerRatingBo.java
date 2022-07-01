package com.toqqa.bo;

import com.toqqa.domain.SellerRating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerRatingBo {
    private String ratingId;
    private Integer rating;
    private String ratingComment;
    private Date ratingCreationDate;
    private Date ratingUpdationDate;

    public SellerRatingBo(SellerRating sellerRating) {
        this.ratingId = sellerRating.getId();
        this.rating = sellerRating.getSellerRating();
        this.ratingComment = sellerRating.getReviewComment();
        this.ratingCreationDate = sellerRating.getDateOfRatingCreation();
        this.ratingUpdationDate = sellerRating.getDateOfRatingUpdation();

    }

}

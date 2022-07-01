package com.toqqa.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingReviewBo {
    private String ratingId;
    private String nameOfCustomer;
    private String profilePic;
    private Integer starsGiven;
    private String reviewComment;
    private Date dateOfRating;
    private Date dateOfRatingUpdate;
    private String userId;
}

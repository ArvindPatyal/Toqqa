package com.toqqa.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingUpdatePayload {
    @NotNull
    @NotBlank
    private String ratingId;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer ratingStars;
    @Length(min = 0, max = 1000)
    private String reviewComment;
}

package com.toqqa.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatingPayload {

    @NotNull
    @NotBlank
    private String productId;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer stars;
    @Length(min = 0, max = 1000)
    private String reviewComment;
}

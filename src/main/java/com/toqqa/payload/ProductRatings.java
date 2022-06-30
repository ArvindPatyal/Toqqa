package com.toqqa.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRatings {
    @NotNull
    @NotEmpty
    private String productId;
    @NotNull
    @NotEmpty
    private String userId;
}

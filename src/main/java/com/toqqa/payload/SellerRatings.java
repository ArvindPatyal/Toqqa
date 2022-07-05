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
public class SellerRatings {
    @NotNull
    @NotEmpty
    private String sellerId;
    @NotNull
    @NotEmpty
    private String userId;
}
package com.toqqa.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ToggleAdStatus {

    @NotNull
    private Boolean status;
    @NotNull
    @NotEmpty
    private String adId;
}

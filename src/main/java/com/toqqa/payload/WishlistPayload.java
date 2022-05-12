package com.toqqa.payload;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class WishlistPayload {

    @NotEmpty
    private List<WishlistItemPayload> items;

}

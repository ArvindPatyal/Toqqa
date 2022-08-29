package com.toqqa.payload;

import com.toqqa.bo.PaginationBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductRequest extends PaginationBo {

    private List<String> productCategoryIds;
    private Boolean showBulkProducts = false;

}

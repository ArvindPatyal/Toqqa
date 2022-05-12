package com.toqqa.bo;

import com.toqqa.domain.ProductCategory;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryBo {
	
	private String id;
	
    private String productCategory;

    public ProductCategoryBo(ProductCategory productcategory){
        this.id=productcategory.getId();
        this.productCategory=productcategory.getProductCategory();
    }

}

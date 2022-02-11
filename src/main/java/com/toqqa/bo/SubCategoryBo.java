package com.toqqa.bo;

import com.toqqa.domain.SubCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SubCategoryBo {
    private String id;
    private String subCategory;
    public SubCategoryBo(SubCategory subCategory)
    {
        this.subCategory=subCategory.getSubcategory();
        this.id=subCategory.getId();
    }

}

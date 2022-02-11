package com.toqqa.bo;

import com.toqqa.domain.Category;
import lombok.Data;

@Data
public class CategoryBo {
    private String id;
    private String category;

    public CategoryBo(Category category){
        this.id=category.getId();
        this.category=category.getCategory();
    }
}

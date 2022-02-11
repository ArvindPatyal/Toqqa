package com.toqqa.service.impls;

import com.toqqa.bo.CategoryBo;
import com.toqqa.domain.Category;
import com.toqqa.repository.CategoryRepository;
import com.toqqa.service.BusinessCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BusinessCategoryServiceImpl implements BusinessCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryBo> getCategories() {
        List<Category>categories=this.categoryRepository.findAll();
        List<CategoryBo> bo= new ArrayList<>();
        categories.forEach(category -> bo.add(new CategoryBo(category)));
        return bo;
    }
}

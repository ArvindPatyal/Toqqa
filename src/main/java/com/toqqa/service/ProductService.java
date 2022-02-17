package com.toqqa.service;

import com.toqqa.bo.ProductBo;
import com.toqqa.payload.AddProduct;

public interface ProductService {
	
	ProductBo addProduct(AddProduct addProduct);
}

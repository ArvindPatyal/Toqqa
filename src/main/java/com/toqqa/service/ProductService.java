package com.toqqa.service;

import com.toqqa.bo.ProductBo;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.ListProductRequest;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ToggleStatus;
import com.toqqa.payload.UpdateProduct;

public interface ProductService {

	ProductBo addProduct(AddProduct addProduct);

	ProductBo updateProduct(UpdateProduct updateProduct);

	ProductBo fetchProduct(String id);

	ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationbo);

	void deleteProduct(String id);

	ProductBo updateProductStatus(ToggleStatus toggleStatus);

}

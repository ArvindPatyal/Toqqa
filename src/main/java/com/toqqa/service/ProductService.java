package com.toqqa.service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.FileUpload;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.payload.UpdateProduct;

public interface ProductService {

	ProductBo addProduct(AddProduct addProduct);

	ProductBo updateProduct(UpdateProduct updateProduct);

	ProductBo fetchProduct(String id);
  
	ListResponseWithCount<ProductBo> fetchProductList(PaginationBo paginationbo);
	
	void deleteProduct(String id);
	
	void deleteAttachment(String id);
	
	Response<String> updateProductImage(FileUpload file);

}

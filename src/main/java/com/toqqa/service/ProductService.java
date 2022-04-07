package com.toqqa.service;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.payload.*;
import com.toqqa.payload.ListResponseWithCount;

public interface ProductService {

	ProductBo addProduct(AddProduct addProduct);

	ProductBo updateProduct(UpdateProduct updateProduct);

	ProductBo fetchProduct(String id);

	ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationbo);

	void deleteProduct(String id);
	
	Boolean deleteAttachment(String id);

	ListResponse<FileBo> updateProductImage(FileUpload file);

	ProductBo updateProductStatus(ToggleStatus toggleStatus);

}

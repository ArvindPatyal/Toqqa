package com.toqqa.service;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.dto.UpdateSequenceNumberDTO;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.FileUpload;
import com.toqqa.payload.ListProductRequest;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ToggleStatus;
import com.toqqa.payload.UpdateProduct;

public interface ProductService {

	ProductBo addProduct(AddProduct addProduct);

	ProductBo updateProduct(UpdateProduct updateProduct);

	ProductBo fetchProduct(String id);

	ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationbo);

	void deleteProduct(String id);

	Boolean deleteAttachment(String id);

	ListResponse<FileBo> updateProductImage(FileUpload file);

	ProductBo updateProductStatus(ToggleStatus toggleStatus);

	ListResponse productList();

	ListResponseWithCount<ProductBo> searchProducts(PaginationBo paginationbo);

	ProductBo toProductBo(Product product);

	Boolean updateSequenceNumber(UpdateSequenceNumberDTO dto);

}

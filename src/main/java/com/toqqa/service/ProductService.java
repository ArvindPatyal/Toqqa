package com.toqqa.service;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.payload.*;

public interface ProductService {

	ProductBo addProduct(AddProduct addProduct);

	ProductBo updateProduct(UpdateProduct updateProduct);

	ProductBo fetchProduct(String id);

	ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationbo);

	void deleteProduct(String id);

	Boolean deleteAttachment(String id);

	ListResponse<FileBo> updateProductImage(FileUpload file);

	ProductBo updateProductStatus(ToggleStatus toggleStatus);

	ListResponseWithCount smeProductListFilter(ProductRequestFilter ProductRequestFilter);

	ListResponse productList();

	ListResponseWithCount<ProductBo> fetchProducts(PaginationBo paginationbo);

	//List<ProductBo> searchProducts(String query);
}

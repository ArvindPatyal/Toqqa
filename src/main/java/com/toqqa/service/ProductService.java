package com.toqqa.service;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.dto.UpdateSequenceNumberDTO;
import com.toqqa.payload.*;

public interface ProductService {

    ProductBo addProduct(AddProduct addProduct);

    ProductBo updateProduct(UpdateProduct updateProduct);

    ProductBo fetchProduct(String id);

    ListResponseWithCount<ProductBo> fetchProductList(ListProductRequest paginationbo);

    Boolean deleteAttachment(String id);

    ListResponse<FileBo> updateProductImage(FileUpload file);

    ProductBo updateProductStatus(ToggleStatus toggleStatus);

    ListResponse productList();

    //ListResponseWithCount<ProductBo> searchProducts(PaginationBo paginationbo);

    ProductBo toProductBo(Product product);

    Boolean updateSequenceNumber(UpdateSequenceNumberDTO dto);

}

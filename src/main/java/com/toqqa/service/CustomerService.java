package com.toqqa.service;

import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;

public interface CustomerService {

    ListResponseWithCount productList(CustomerProductRequest req);

    ListResponseWithCount smeProductList(ProductRequestFilter productRequestFilter);
}

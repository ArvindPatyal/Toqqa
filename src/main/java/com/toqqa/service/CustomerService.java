package com.toqqa.service;

import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponseWithCount;

public interface CustomerService {

	ListResponseWithCount productList(CustomerProductRequest req);
}

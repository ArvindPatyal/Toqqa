package com.toqqa.service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;

public interface CustomerService {

    ListResponseWithCount productList(PaginationBo bo);
}

package com.toqqa.service;

import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Sme;
import com.toqqa.dto.NearbySmeRespDto;
import com.toqqa.payload.*;

import java.util.List;


public interface SmeService {
    SmeBo smeRegistration(SmeRegistration smeRegistration, String userId);

    SmeBo smeUpdate(SmeUpdate smeUpdate);

    SmeBo fetchSme(String id);

    // ListResponseWithCount<SmeBo> fetchSmeList(PaginationBo bo);
    ListResponse<SmeBo> fetchSmeListWithoutPagination();

    ListResponseWithCount fetchProductsList(ProductRequestFilter productRequestFilter);

    ListResponseWithCount fetchProducts(ProductRequestFilter productRequestFilter);

    List<Sme> getAllSme(Boolean isDeleted);

    List<NearbySmeRespDto> getNearbySme();

}

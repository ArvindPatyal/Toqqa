package com.toqqa.service;

import java.time.LocalDate;
import java.util.List;
import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Sme;
import com.toqqa.dto.NearbySmeRespDto;
import com.toqqa.dto.SmeStatsResponseDto;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.payload.SmeRegistration;
import com.toqqa.payload.SmeUpdate;


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
        
    Sme getSmeByUserId(String userId);
    
    SmeStatsResponseDto getOverallStatsByDate(LocalDate startDate, LocalDate endDate);

}

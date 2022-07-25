package com.toqqa.service;

import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Sme;
import com.toqqa.dto.NearbySmeRespDto;
import com.toqqa.dto.SmeStatsResponseDto;
import com.toqqa.payload.*;

import java.time.LocalDate;
import java.util.List;


public interface SmeService {
    SmeBo smeRegistration(SmeRegistration smeRegistration, String userId, boolean isNewUser);

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

    ListResponse smeProductCategories();

    SmeBo becomeASme(SmeRegistration smeRegistration);

    SmeBo toSmeBo(Sme sme);
}

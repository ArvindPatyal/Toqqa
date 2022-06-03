package com.toqqa.service;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.constants.OrderConstants;
import com.toqqa.domain.Sme;
import com.toqqa.dto.NearbySmeRespDto;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.OrderStatusUpdatePayload;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeRegistration;
import com.toqqa.payload.SmeUpdate;
import com.toqqa.payload.ToggleOrdersStatus;
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

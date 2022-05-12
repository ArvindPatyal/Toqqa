package com.toqqa.service;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.SmeRegistration;
import com.toqqa.payload.SmeUpdate;

public interface SmeService {
	SmeBo smeRegistration(SmeRegistration smeRegistration, String userId);
	
	SmeBo smeUpdate(SmeUpdate smeUpdate);
	
	SmeBo fetchSme(String id);

    ListResponseWithCount<SmeBo> fetchSmeList(PaginationBo bo);
}

package com.toqqa.service;

import com.toqqa.bo.SmeBo;
import com.toqqa.payload.SmeRegistration;

public interface SmeService {
	SmeBo smeRegistration(SmeRegistration smeRegistration,String userId);

}

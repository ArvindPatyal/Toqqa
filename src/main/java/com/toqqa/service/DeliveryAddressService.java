package com.toqqa.service;

import java.util.List;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;

public interface DeliveryAddressService {

	DeliveryAddressBo createAddress(DeliveryAddressPayload addressPayload);

	DeliveryAddressBo updateAddress(DeliveryAddressUpdate addresstUpdate);

	List<DeliveryAddressBo> fetchAddress(String id);

	void deleteAddress(String id);

}

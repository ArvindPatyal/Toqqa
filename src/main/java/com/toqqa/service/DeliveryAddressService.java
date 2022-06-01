package com.toqqa.service;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;
import com.toqqa.payload.Response;

public interface DeliveryAddressService {

	DeliveryAddressBo createAddress(DeliveryAddressPayload addressPayload);

	DeliveryAddressBo updateAddress(DeliveryAddressUpdate addressUpdate);

	DeliveryAddressBo fetchAddress(String id);

	void deleteAddress(String id);

	Response fetchAddressList();

	Response currentOrder(String addressId);
}

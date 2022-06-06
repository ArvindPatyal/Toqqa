package com.toqqa.service;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.domain.DeliveryAddress;
import com.toqqa.domain.User;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;
import com.toqqa.payload.Response;

import java.util.Optional;

public interface DeliveryAddressService {

    DeliveryAddressBo createAddress(DeliveryAddressPayload addressPayload);

    void create(User user);

    DeliveryAddressBo updateAddress(DeliveryAddressUpdate addressUpdate);

    DeliveryAddressBo fetchAddress(String id);

    void deleteAddress(String id);

    Response fetchAddressList();


    Response currentAddress(String addressId);

    Optional<DeliveryAddress> getCurrentDelAddress(User user);
}

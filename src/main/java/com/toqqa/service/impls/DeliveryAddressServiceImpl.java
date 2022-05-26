package com.toqqa.service.impls;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.domain.DeliveryAddress;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;
import com.toqqa.payload.Response;
import com.toqqa.repository.DeliveryAddressRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.DeliveryAddressService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    @Autowired
    private Helper helper;
    @Autowired
    private DeliveryAddressRepository addressRepo;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public DeliveryAddressBo createAddress(DeliveryAddressPayload addressPayload) {

        log.info("Inside Add address");

        DeliveryAddress deliveryAddress = new DeliveryAddress();

        deliveryAddress.setUser(this.authenticationService.currentUser());
        deliveryAddress.setCity(addressPayload.getCity());
        deliveryAddress.setAddress(addressPayload.getAddress());
        deliveryAddress.setPostCode(addressPayload.getPostCode());
        deliveryAddress.setState(addressPayload.getState());
        deliveryAddress.setCountry(addressPayload.getCountry());
        deliveryAddress.setLatitude(addressPayload.getLatitude());
        deliveryAddress.setLongitude(addressPayload.getLongitude());

        if (this.helper.notNullAndBlank(addressPayload.getPhoneNumber())) {
            if (this.helper.isValidNumber(addressPayload.getPhoneNumber())) {
                deliveryAddress.setPhoneNumber(addressPayload.getPhoneNumber());
            } else {
                throw new BadRequestException("Enter a valid phone number");
            }
        } else throw new BadRequestException("Enter a valid phone number");
        deliveryAddress = this.addressRepo.saveAndFlush(deliveryAddress);
        return new DeliveryAddressBo(deliveryAddress);


    }

    @Override
    public DeliveryAddressBo updateAddress(DeliveryAddressUpdate addresstUpdate) {

        log.info("inside update address");

        DeliveryAddress deliveryAddress = this.addressRepo.findById(addresstUpdate.getDeliveryAddressId()).get();
        deliveryAddress.setCity(addresstUpdate.getCity());
        deliveryAddress.setAddress(addresstUpdate.getAddress());
        deliveryAddress.setPostCode(addresstUpdate.getPostCode());
        deliveryAddress.setState(addresstUpdate.getState());
        deliveryAddress.setCountry(addresstUpdate.getCountry());
        deliveryAddress.setLongitude(addresstUpdate.getLongitude());
        deliveryAddress.setLatitude(addresstUpdate.getLatitude());
        if (this.helper.notNullAndBlank(addresstUpdate.getPhoneNumber())) {
            if (this.helper.isValidNumber(addresstUpdate.getPhoneNumber())) {
                deliveryAddress.setPhoneNumber(addresstUpdate.getPhoneNumber());
            } else {
                throw new BadRequestException("Enter a valid phone number");
            }
        } else throw new BadRequestException("Enter a valid phone number");
        deliveryAddress = this.addressRepo.saveAndFlush(deliveryAddress);
        return new DeliveryAddressBo(deliveryAddress);
    }

    @Override
    public DeliveryAddressBo fetchAddress(String id) {
        log.info("Inside fetch Address");
        Optional<DeliveryAddress> address = this.addressRepo.findById(id);
        if (address.isPresent()) {
            return new DeliveryAddressBo(address.get());
        } else {
            throw new BadRequestException("No address found with  " + id);
        }
    }

    @Override
    public void deleteAddress(String id) {

        log.info("inside delete address");

        Optional<DeliveryAddress> da = this.addressRepo.findById(id);

        if (da.isPresent()) {

            this.addressRepo.deleteById(id);
        } else {
            throw new BadRequestException("invalid address id: " + id);
        }
    }

    @Override
    public Response fetchAddressList() {
        log.info("Inside Service fetchAddress List");
        User user = this.authenticationService.currentUser();
        List<DeliveryAddress> deliveryAddresses = this.addressRepo.findByUser_Id(user.getId());
        if (!(this.helper.notNullAndHavingData(deliveryAddresses))) {
            return new Response("", "No added addresses found for this user");
        } else {
            List<DeliveryAddressBo> deliveryAddressBos = new ArrayList<>();

            deliveryAddresses.forEach(deliveryAddress -> {
                DeliveryAddressBo deliveryAddressBo = new DeliveryAddressBo(deliveryAddress);
                deliveryAddressBos.add(deliveryAddressBo);
            });
            return new Response(deliveryAddressBos, "addresses fetched");
        }
    }

}

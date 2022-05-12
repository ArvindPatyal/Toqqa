package com.toqqa.service.impls;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.domain.DeliveryAddress;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;
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
    public List<DeliveryAddressBo> fetchAddress(String id) {
        log.info("Inside fetch Address");

        List<DeliveryAddress> address = this.addressRepo.findByUser_Id(id);
        List<DeliveryAddressBo> addressBo = new ArrayList<>();

        address.forEach(a -> {

            DeliveryAddressBo dabo = new DeliveryAddressBo(a);

            dabo.setCity(a.getCity());
            dabo.setCountry(a.getCountry());
            dabo.setAddress(a.getAddress());
            dabo.setPostCode(a.getPostCode());
            dabo.setState(a.getState());
            dabo.setPhoneNumber(a.getPhoneNumber());
            addressBo.add(dabo);

        });
        return addressBo;
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

}

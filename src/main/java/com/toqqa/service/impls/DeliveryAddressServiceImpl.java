package com.toqqa.service.impls;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.domain.DeliveryAddress;
import com.toqqa.domain.User;
import com.toqqa.exception.BadRequestException;
import com.toqqa.exception.ResourceNotFoundException;
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
    private DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public DeliveryAddressBo createAddress(DeliveryAddressPayload addressPayload) {

        log.info("Inside Add address");
        User user = this.authenticationService.currentUser();
        List<DeliveryAddress> deliveryAddresses = this.deliveryAddressRepository.findByUser_Id(user.getId());
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        if (deliveryAddresses.isEmpty()) {
            deliveryAddress.setIsCurrentAddress(true);
        } else {
            deliveryAddress.setIsCurrentAddress(false);
        }
        deliveryAddress.setUser(user);
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
        deliveryAddress = this.deliveryAddressRepository.saveAndFlush(deliveryAddress);
        return new DeliveryAddressBo(deliveryAddress);


    }

    @Override
    public DeliveryAddressBo updateAddress(DeliveryAddressUpdate addressUpdate) {

        log.info("inside update address");

        DeliveryAddress deliveryAddress = this.deliveryAddressRepository.findById(addressUpdate.getDeliveryAddressId()).get();
        deliveryAddress.setCity(addressUpdate.getCity());
        deliveryAddress.setAddress(addressUpdate.getAddress());
        deliveryAddress.setPostCode(addressUpdate.getPostCode());
        deliveryAddress.setState(addressUpdate.getState());
        deliveryAddress.setCountry(addressUpdate.getCountry());
        deliveryAddress.setLongitude(addressUpdate.getLongitude());
        deliveryAddress.setLatitude(addressUpdate.getLatitude());
        if (this.helper.notNullAndBlank(addressUpdate.getPhoneNumber())) {
            if (this.helper.isValidNumber(addressUpdate.getPhoneNumber())) {
                deliveryAddress.setPhoneNumber(addressUpdate.getPhoneNumber());
            } else {
                throw new BadRequestException("Enter a valid phone number");
            }
        } else throw new BadRequestException("Enter a valid phone number");
        deliveryAddress = this.deliveryAddressRepository.saveAndFlush(deliveryAddress);
        return new DeliveryAddressBo(deliveryAddress);
    }

    @Override
    public DeliveryAddressBo fetchAddress(String id) {
        log.info("Inside fetch Address");
        Optional<DeliveryAddress> address = this.deliveryAddressRepository.findById(id);
        if (address.isPresent()) {
            return new DeliveryAddressBo(address.get());
        } else {
            throw new BadRequestException("No address found with  " + id);
        }
    }

    @Override
    public void deleteAddress(String id) {

        log.info("inside delete address");

        Optional<DeliveryAddress> da = this.deliveryAddressRepository.findById(id);

        if (da.isPresent()) {

            this.deliveryAddressRepository.deleteById(id);
        } else {
            throw new BadRequestException("invalid address id: " + id);
        }
    }

    @Override
    public Response fetchAddressList() {
        log.info("Inside Service fetchAddress List");
        User user = this.authenticationService.currentUser();
        List<DeliveryAddress> deliveryAddresses = this.deliveryAddressRepository.findByUser_Id(user.getId());
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

    @Override
    public Response currentOrder(String addressId) {
        log.info("");
        String userId = this.authenticationService.currentUser().getId();
        List<DeliveryAddress> addressList = new ArrayList<>();
        List<DeliveryAddress> deliveryAddresses = this.deliveryAddressRepository.findByUser_Id(userId);
        Boolean isExists = deliveryAddresses.stream().anyMatch(deliveryAddress -> deliveryAddress.getId().equals(addressId));
        if (!isExists) {
            throw new ResourceNotFoundException("Address Not found with id " + addressId);
        }
        deliveryAddresses.forEach(deliveryAddress -> {

            if (deliveryAddress.getId().equals(addressId)) {
                deliveryAddress.setIsCurrentAddress(true);
            } else {
                deliveryAddress.setIsCurrentAddress(false);
            }
            addressList.add(deliveryAddress);
        });
        this.deliveryAddressRepository.saveAllAndFlush(addressList);
        return new Response(true, "current Address updated");
    }

}

package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.model.User;
import com.ecommerce.Ecom.payload.AddressDTO;
import com.ecommerce.Ecom.service.AddressService;
import com.ecommerce.Ecom.util.AuthUtil;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO>createAddress(@Valid @RequestBody AddressDTO addressDTO){
    User user=authUtil.loggedInUser();

    AddressDTO savedAddressDto=addressService.createAddress(addressDTO,user);

    return new ResponseEntity<AddressDTO>(savedAddressDto, HttpStatus.CREATED);
    }


    @GetMapping("/addresses")
    public ResponseEntity <List<AddressDTO>>getAddress(){
        List<AddressDTO> addressList=addressService.getAddress();

        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }


    @GetMapping("/addresses/{addressId}")
    public ResponseEntity <AddressDTO>getAddress(@PathVariable Long addressId){
        User user=authUtil.loggedInUser();

        AddressDTO  addressDTO=addressService.getAddressesById(addressId);

        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }


    @GetMapping("/users/addresses")
    public ResponseEntity <List<AddressDTO>>getAddressByUser(){
        User user=authUtil.loggedInUser();
        List<AddressDTO> addressList=addressService.getUserAddresses(user);

        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity <AddressDTO>updateAddressById(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO){


        User user=authUtil.loggedInUser();
        AddressDTO updatedAddress=addressService.updateAddress(addressId,addressDTO);

        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity <String>updateAddressById(@PathVariable Long addressId){

        String status=addressService.deleteAddress(addressId);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}

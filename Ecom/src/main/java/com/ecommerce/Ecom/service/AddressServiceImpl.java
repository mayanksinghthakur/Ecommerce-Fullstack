package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.exceptions.ResourceNotFoundException;
import com.ecommerce.Ecom.model.Address;
import com.ecommerce.Ecom.model.User;
import com.ecommerce.Ecom.payload.AddressDTO;
import com.ecommerce.Ecom.repositories.AddressRepository;
import com.ecommerce.Ecom.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        //mapping addressDto to address class
        Address address=modelMapper.map(addressDTO,Address.class);

        //getting list of addresses
        List<Address>addressList=user.getAddresses();
        addressList.add(address);

        //setting user with address list
        user.setAddresses(addressList);

        //setting list of user in the address
        address.setUser(user);

        //saving the address in the database
        Address savedAddress=addressRepository.save(address);


        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddress() {

        List<Address>addresses=addressRepository.findAll();
        List<AddressDTO>addressDTOS= addresses.stream().map(address ->
            modelMapper.map(address,AddressDTO.class)
        ).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address= addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {

        List<Address>addresses=user.getAddresses();


        return addresses.stream().map(address->modelMapper.map(address,AddressDTO.class)).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId,AddressDTO addressDTO) {

        Address address=addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        address.setCity(addressDTO.getCity());
        address.setPincode(addressDTO.getPincode());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setStreet(addressDTO.getStreet());
        address.setBuildingName(addressDTO.getBuildingName());

        Address updatedAddress=addressRepository.save(address);

        //because user has the list<address> so we need to update there also
        User user=address.getUser();

//removing old address
        user.getAddresses().removeIf(addres->addres.getAddressId().equals(addressId));


        user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {

        Address addressFromDataBase=addressRepository.findById((addressId)).orElseThrow(()->new ResourceNotFoundException("Address","address",addressId));

        User user=addressFromDataBase.getUser();
        addressRepository.deleteById(addressId);

        user.getAddresses().removeIf(addres->addres.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressFromDataBase);

        return   "Address deleted with addressId :"+addressId;
    }
}

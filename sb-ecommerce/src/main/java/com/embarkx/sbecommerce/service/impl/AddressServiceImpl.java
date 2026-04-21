package com.embarkx.sbecommerce.service.impl;

import com.embarkx.sbecommerce.model.Address;
import com.embarkx.sbecommerce.model.User;
import com.embarkx.sbecommerce.payload.request.AddressDTO;
import com.embarkx.sbecommerce.repository.AddressRepository;
import com.embarkx.sbecommerce.repository.UserRepository;
import com.embarkx.sbecommerce.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address with id " + addressId + " not found"));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address with id " + addressId + " not found"));
        addressFromDB.setCity(addressDTO.getCity());
        addressFromDB.setPincode(addressDTO.getPincode());
        addressFromDB.setState(addressDTO.getState());
        addressFromDB.setCountry(addressDTO.getCountry());
        addressFromDB.setBuildingName(addressDTO.getBuildingName());
        addressFromDB.setStreet(addressDTO.getStreet());

        Address updatedAddress = addressRepository.save(addressFromDB);
        User user = updatedAddress.getUser();
        user.getAddresses().removeIf(a -> a.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address with id " + addressId + " not found"));

        User user = addressFromDB.getUser();
        user.getAddresses().removeIf(a -> a.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressFromDB);
        return "Address deleted successfully with addressId : " + addressId;
    }
}

package com.embarkx.sbecommerce.service;

import com.embarkx.sbecommerce.model.User;
import com.embarkx.sbecommerce.payload.request.AddressDTO;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);
}

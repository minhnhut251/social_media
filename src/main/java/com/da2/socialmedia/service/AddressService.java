package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.AddressEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<AddressEntity> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }
}
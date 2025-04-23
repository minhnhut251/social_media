package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.AddressEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;  // Thêm import này

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

    public AddressEntity saveAddress(User user, Map<String, String> addressData) {
        AddressEntity address = new AddressEntity();
        address.setUser(user);

        // Thiết lập các trường dữ liệu từ addressData theo đúng entity
        address.setRecipientName(addressData.get("fullName"));
        address.setPhone(addressData.get("phone"));
        address.setProvince(addressData.get("province"));
        address.setDistrict(addressData.get("district"));
        address.setWard(addressData.get("ward"));

        // Sử dụng addressDetail thay vì addressLine1 và addressLine2
        String addressDetail = addressData.get("addressLine1");
        if (addressData.containsKey("addressLine2") && addressData.get("addressLine2") != null
                && !addressData.get("addressLine2").isEmpty()) {
            addressDetail += ", " + addressData.get("addressLine2");
        }
        address.setAddressDetail(addressDetail);

        // Đặt địa chỉ mặc định nếu được chỉ định
        if (addressData.containsKey("isDefault") && "true".equals(addressData.get("isDefault"))) {
            address.setDefault(true);
        } else {
            address.setDefault(false);
        }

        return addressRepository.save(address);
    }
}
package com.address.web.controller;

import com.address.web.entity.Address;
import com.address.web.repository.AddressRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/address")
public class AddressController {


    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Get Address by given country
     *
     * @return @{@link com.address.web.entity.Address}
     */
    @GetMapping(value = "country/{country}")
    public ResponseEntity<?> getAddressByCountry(@PathVariable(value = "country", required = true) String country){
        List<Address> addresses = addressRepository.findByCountry(country);
        if (!CollectionUtils.isEmpty(addresses)){
            return ResponseEntity.ok(addresses);
        }
        return ResponseEntity.ok(Collections.EMPTY_LIST);
    }
}

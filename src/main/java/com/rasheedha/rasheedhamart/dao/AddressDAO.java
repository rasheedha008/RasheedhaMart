package com.rasheedha.rasheedhamart.dao;

import com.rasheedha.rasheedhamart.model.Address;
import java.util.List;
import java.util.Optional;

public interface AddressDAO {
    Address create(Address address);
    Address update(Address address);
    boolean delete(Long id, Long userId);
    Optional<Address> findById(Long id);
    List<Address> findByUserId(Long userId);
    boolean setDefaultAddress(Long addressId, Long userId);
}

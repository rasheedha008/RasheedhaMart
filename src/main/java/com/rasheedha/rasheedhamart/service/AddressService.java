package com.rasheedha.rasheedhamart.service;

import com.rasheedha.rasheedhamart.dao.AddressDAO;
import com.rasheedha.rasheedhamart.exception.ResourceNotFoundException;
import com.rasheedha.rasheedhamart.exception.ValidationException;
import com.rasheedha.rasheedhamart.model.Address;
import com.rasheedha.rasheedhamart.util.ValidationUtil;
import com.rasheedha.rasheedhamart.util.XSSUtil;

import java.util.List;

public class AddressService {

    private final AddressDAO addressDAO;

    public AddressService(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public Address addAddress(Long userId, Address address) {
        ValidationUtil.validateNotBlank(address.getFullName(), "Full Name");
        ValidationUtil.validateNotBlank(address.getPhoneNumber(), "Phone Number");
        ValidationUtil.validateNotBlank(address.getAddressLine1(), "Address Line 1");
        ValidationUtil.validateNotBlank(address.getCity(), "City");
        ValidationUtil.validateNotBlank(address.getState(), "State");
        ValidationUtil.validateNotBlank(address.getPostalCode(), "Postal Code");

        address.setUserId(userId);
        address.setFullName(XSSUtil.escapeHtml(address.getFullName()));
        address.setAddressLine1(XSSUtil.escapeHtml(address.getAddressLine1()));
        address.setAddressLine2(XSSUtil.escapeHtml(address.getAddressLine2()));
        address.setCity(XSSUtil.escapeHtml(address.getCity()));
        address.setState(XSSUtil.escapeHtml(address.getState()));
        address.setPostalCode(XSSUtil.escapeHtml(address.getPostalCode()));

        return addressDAO.create(address);
    }

    public Address updateAddress(Long userId, Address address) {
        if (address.getId() == null) {
            throw new ValidationException("Address ID is required for update.");
        }

        Address existing = addressDAO.findById(address.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Address not found."));

        if (!existing.getUserId().equals(userId)) {
            throw new ValidationException("Access denied. Address belongs to another user.");
        }

        ValidationUtil.validateNotBlank(address.getFullName(), "Full Name");
        ValidationUtil.validateNotBlank(address.getPhoneNumber(), "Phone Number");
        ValidationUtil.validateNotBlank(address.getAddressLine1(), "Address Line 1");

        address.setUserId(userId);
        address.setFullName(XSSUtil.escapeHtml(address.getFullName()));
        address.setAddressLine1(XSSUtil.escapeHtml(address.getAddressLine1()));

        return addressDAO.update(address);
    }

    public boolean deleteAddress(Long userId, Long addressId) {
        return addressDAO.delete(addressId, userId);
    }

    public List<Address> getUserAddresses(Long userId) {
        return addressDAO.findByUserId(userId);
    }

    public boolean setDefaultAddress(Long userId, Long addressId) {
        return addressDAO.setDefaultAddress(addressId, userId);
    }
}

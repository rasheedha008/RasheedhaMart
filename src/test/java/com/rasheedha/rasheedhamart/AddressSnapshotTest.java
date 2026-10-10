package com.rasheedha.rasheedhamart;

import com.rasheedha.rasheedhamart.model.Address;
import com.rasheedha.rasheedhamart.model.AddressSnapshot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressSnapshotTest {

    @Test
    public void testAddressSnapshotImmutability() {
        Address address = new Address(1L, 10L, "John Doe", "9876543210", "123 Anna Salai", "Apt 4B", "Chennai", "Tamil Nadu", "600002", "India", true, null);
        
        AddressSnapshot snapshot = AddressSnapshot.fromAddress(100L, address);

        assertEquals(100L, snapshot.getOrderId());
        assertEquals("John Doe", snapshot.getFullName());
        assertEquals("9876543210", snapshot.getPhoneNumber());
        assertEquals("123 Anna Salai", snapshot.getAddressLine1());
        assertEquals("Chennai", snapshot.getCity());
        assertEquals("India", snapshot.getCountry());

        // Modify original address object
        address.setFullName("John Updated");
        address.setAddressLine1("456 New Salai");

        // Assert snapshot remains unchanged and immutable
        assertEquals("John Doe", snapshot.getFullName());
        assertEquals("123 Anna Salai", snapshot.getAddressLine1());
    }
}

package com.smartcart.testing;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class StoreUnitTesting {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @Test
    void givenComplexStoreData_whenRegisterStore_thenReturnSavedStoreWithPendingStatus() {
        // Arrange
        Address address = new Address("Av. Prolongación San Juan 456", "San Juan de Miraflores", -12.156, -76.983);
        List<OperatingHour> hours = List.of(new OperatingHour("Monday", "08:00", "22:00"));

        Store storeInput = new Store("M-999", "Metro - Monterrico", "20100435671", address, hours);
        Store storeSaved = new Store(101L, "M-999", "Metro - Monterrico", "20100435671", address, hours, "PENDING_VERIFICATION");

        when(storeRepository.save(any(Store.class))).thenReturn(storeSaved);

        // Act
        Store result = storeService.registerStore(storeInput);

        // Assert
        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("PENDING_VERIFICATION", result.getStatus());
        assertEquals("San Juan de Miraflores", result.getAddress().getDistrict());
        verify(storeRepository, times(1)).save(storeInput);
    }
}

package com.customer.transacation.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.customer.transacation.repository.CustomerTranscationRepository;
import com.customer.transacation.service.impl.ValidationServiceImpl;
import com.customer.transaction.errors.CustomerTransactionException;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceImplTest {

	@Mock
	private CustomerTranscationRepository customerTransactionRepository;

	@InjectMocks
	private ValidationServiceImpl validationServiceImpl;

	public ValidationServiceImplTest() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateCustomerIdIsPresent_InvalidCustomer() {
        Long invalidCustomerId = 1L;
        when(customerTransactionRepository.findCustomerIdByCustomerId(invalidCustomerId)).thenReturn(null);
        Exception exception = assertThrows(CustomerTransactionException.class, () -> {
            validationServiceImpl.validateCustomerIdIsPresent(invalidCustomerId);
        });
        assertEquals("CustomerId not found: " + invalidCustomerId, exception.getMessage());
    }
}

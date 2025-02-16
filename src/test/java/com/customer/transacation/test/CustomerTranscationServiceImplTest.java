package com.customer.transacation.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.customer.transacation.domain.CustomerTransaction;
import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.repository.CustomerTranscationRepository;
import com.customer.transacation.service.impl.CustomerTranscationServiceImpl;
import com.customer.transacation.service.impl.ValidationServiceImpl;


@ExtendWith(MockitoExtension.class)
public class CustomerTranscationServiceImplTest {

    @Mock
    private CustomerTranscationRepository customerTranscationRepository;

    @InjectMocks
    private CustomerTranscationServiceImpl customerTranscationService;

    private List<CustomerTransaction> mockTransactions;
 
    @Mock
    private ValidationServiceImpl validationServiceImpl;

    @BeforeEach
    void setUp() {
        mockTransactions = new ArrayList<>();

        CustomerTransaction transaction1 = new CustomerTransaction();
        transaction1.setId(1L);
        transaction1.setCustomerId(1L);
        transaction1.setAmountSpent(120L);
        transaction1.setTransactionDate(LocalDate.of(2024, 1, 10)); // January - 90 points

        CustomerTransaction transaction2 = new CustomerTransaction();
        transaction2.setId(2L);
        transaction2.setCustomerId(1L);
        transaction2.setAmountSpent(80L);
        transaction2.setTransactionDate(LocalDate.of(2024, 2, 15)); // February - 30 points

        CustomerTransaction transaction3 = new CustomerTransaction();
        transaction3.setId(3L);
        transaction3.setCustomerId(1L);
        transaction3.setAmountSpent(40L);
        transaction3.setTransactionDate(LocalDate.of(2024, 3, 20)); // March - 0 points

        mockTransactions.add(transaction1);
        mockTransactions.add(transaction2);
        mockTransactions.add(transaction3);
    }
    @Test
    void testCalculateRewardPoints() {

        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        RewardResponseDTO response = customerTranscationService.calculateRewardPoints(1L);

        assertEquals(1L, response.getCustomerId());
        assertEquals(90, response.getMonthlyPoints().get("JANUARY"));
        assertEquals(30, response.getMonthlyPoints().get("FEBRUARY"));
        assertEquals(0, response.getMonthlyPoints().getOrDefault("MARCH", 0));
        assertEquals(120, response.getTotalPoints());
    }
    
    @Test
    void testGetMonthlyRewardPoints() {
        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        Map<String, Integer> response = customerTranscationService.getMonthlyRewardPoints(1L);

        assertEquals(90, response.get("JANUARY"));
        assertEquals(30, response.get("FEBRUARY"));
        assertEquals(0, response.getOrDefault("MARCH", 0));
    }
    
   

}
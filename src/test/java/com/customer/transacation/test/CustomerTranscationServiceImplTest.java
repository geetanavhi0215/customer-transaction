package com.customer.transacation.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.dto.TransactionDetails;
import com.customer.transacation.entity.CustomerTransaction;
import com.customer.transacation.repository.CustomerTransactionRepository;
import com.customer.transacation.service.impl.CustomerTransactionServiceImpl;
import com.customer.transaction.errors.CustomerTransactionException;

@ExtendWith(MockitoExtension.class)
public class CustomerTranscationServiceImplTest {

    @Mock
    private CustomerTransactionRepository customerTranscationRepository;

    @InjectMocks
    private CustomerTransactionServiceImpl customerTranscationService;

    private List<CustomerTransaction> mockTransactions;

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
        when(customerTranscationRepository.existsById(1L)).thenReturn(true);
        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        RewardResponseDTO response = customerTranscationService.calculateRewardPoints(1L);

        assertEquals(1L, response.getCustomerId());
        assertEquals(120, response.getTotalPoints());
    }

    @Test
    void testGetMonthlyRewardPoints() {
        when(customerTranscationRepository.existsById(1L)).thenReturn(true);
        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        Map<String, Integer> response = customerTranscationService.getMonthlyRewardPoints(1L);

        assertEquals(90, response.get("JANUARY"));
        assertEquals(30, response.get("FEBRUARY"));
        assertEquals(0, response.getOrDefault("MARCH", 0));
    }
    @Test
    void testGetRewardPointsForMonth_ValidCustomerAndMonth() {
        String targetMonth = "JANUARY";

        when(customerTranscationRepository.existsById(1L)).thenReturn(true); // Valid customer
        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        Map<String, Object> rewardPointsForMonth = customerTranscationService.getRewardPointsForMonth(1L, targetMonth);

        assertEquals(targetMonth, rewardPointsForMonth.get("month"));
        assertEquals(90, rewardPointsForMonth.get("totalPoints")); 
        List<TransactionDetails> transactions = (List<TransactionDetails>) rewardPointsForMonth.get("transactions");
        assertEquals(1, transactions.size());
        assertEquals(120, transactions.get(0).getAmountSpent().longValue());
        assertEquals(90, transactions.get(0).getRewardPoints());
    }

    @Test
    void testGetRewardPointsForMonth_NoTransactionsForMonth() {
        String targetMonth = "APRIL";

        when(customerTranscationRepository.existsById(1L)).thenReturn(true); // Valid customer
        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        CustomerTransactionException exception = assertThrows(
            CustomerTransactionException.class,
            () -> customerTranscationService.getRewardPointsForMonth(1L, targetMonth)
        );

        assertEquals("No transactions found for the specified month.", exception.getMessage());
    }

    @Test
    void testGetRewardPointsForMonth_InvalidCustomer() {
        String targetMonth = "JANUARY";

        when(customerTranscationRepository.existsById(1L)).thenReturn(false); // Invalid customer

        CustomerTransactionException exception = assertThrows(
            CustomerTransactionException.class,
            () -> customerTranscationService.getRewardPointsForMonth(1L, targetMonth)
        );

        assertEquals("CustomerId not found: 1", exception.getMessage());
    }
    @Test
    void testGetRewardPointsForMonth_InvalidTransactionAmount() {
        String targetMonth = "JANUARY";

        when(customerTranscationRepository.existsById(1L)).thenReturn(true);
        List<CustomerTransaction> mockTransactions = new ArrayList<>();
        CustomerTransaction invalidTransaction = new CustomerTransaction();
        invalidTransaction.setId(1L);
        invalidTransaction.setCustomerId(1L);
        invalidTransaction.setTransactionDate(LocalDate.of(2025, 1, 10));
        invalidTransaction.setAmountSpent(-50L); // Invalid amount
        mockTransactions.add(invalidTransaction);
        when(customerTranscationRepository.getTransactions()).thenReturn(mockTransactions);

        CustomerTransactionException exception = assertThrows(
            CustomerTransactionException.class,
            () -> customerTranscationService.getRewardPointsForMonth(1L, targetMonth)
        );

        assertEquals("Invalid transaction amount", exception.getMessage());
    }

    @Test
    void testCalculatePoints() {
        // Testing amounts below the reward threshold
        assertEquals(0, customerTranscationService.calculatePoints(49));

        // Testing amounts between $50 and $100
        assertEquals(1, customerTranscationService.calculatePoints(51));

        // Testing amounts above $100
        assertEquals(90, customerTranscationService.calculatePoints(120));

        // Testing boundary conditions at exactly $100
        assertEquals(50, customerTranscationService.calculatePoints(100));

        // Testing zero amount
        assertEquals(0, customerTranscationService.calculatePoints(0));

        // Testing negative amount to ensure exception is thrown
        CustomerTransactionException exception = assertThrows(
            CustomerTransactionException.class,
            () -> customerTranscationService.calculatePoints(-10)
        );
        assertEquals("Invalid transaction amount", exception.getMessage());
    }

}

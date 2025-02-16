package com.customer.transacation.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.service.CustomerTransactionService;
import com.customer.transacation.web.rest.CustomerTransactionController;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerTransactionController.class)
public class CustomerTranscationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerTransactionService customerTranscationService;

    @Test
    void testGetRewardPoints() throws Exception {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("JANUARY", 90);
        monthlyPoints.put("FEBRUARY", 30);

        RewardResponseDTO rewardResponseDTO = new RewardResponseDTO();
        rewardResponseDTO.setCustomerId(1L);
        rewardResponseDTO.setMonthlyPoints(monthlyPoints);
        rewardResponseDTO.setTotalPoints(120);

        when(customerTranscationService.calculateRewardPoints(1L)).thenReturn(rewardResponseDTO);

        mockMvc.perform(get("/api/customer-transactions/{customerId}",1L))
                .andExpect(status().isOk());
    }
    @Test
    void testGetMonthlyRewardPoints() throws Exception {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("JANUARY", 90);
        monthlyPoints.put("FEBRUARY", 30);

        when(customerTranscationService.getMonthlyRewardPoints(1L)).thenReturn(monthlyPoints);

        mockMvc.perform(get("/api/customer-transactions/{customerId}/monthly", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.JANUARY").value(90))
            .andExpect(jsonPath("$.FEBRUARY").value(30));
    }
}

package com.customer.transacation.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.customer.transacation.dto.RewardResponseDTO;
import com.customer.transacation.service.CustomerTranscationService;
import com.customer.transacation.web.rest.CustomerTranscationResource;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerTranscationResource.class)
public class CustomerTranscationResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerTranscationService customerTranscationService;

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

        mockMvc.perform(get("/api/customer-transacation/{customerId}",1))
                .andExpect(status().isOk());
    }
}

package com.customer.transacation.dto;

import java.util.List;
import java.util.Map;


import lombok.Data;
import lombok.Generated;

@Data
@Generated
public class RewardResponseDTO {
    
    private Long customerId;
    private Integer totalPoints;
    private List<MonthlyRewardDetails> monthlyRewardDetails;

    
}

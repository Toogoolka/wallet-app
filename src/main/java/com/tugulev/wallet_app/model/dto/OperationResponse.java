package com.tugulev.wallet_app.model.dto;


import com.tugulev.wallet_app.model.enums.OperationType;
import com.tugulev.wallet_app.model.enums.StatusOperation;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponse {
    private int StatusCode;
    private UUID walletId;
    private OperationType operationType;
    private StatusOperation statusOperation;
    private BigDecimal amount;
    private String error;
}

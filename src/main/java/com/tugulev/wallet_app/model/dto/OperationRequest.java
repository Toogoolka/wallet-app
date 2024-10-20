package com.tugulev.wallet_app.model.dto;

import com.tugulev.wallet_app.model.enums.OperationType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OperationRequest {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}

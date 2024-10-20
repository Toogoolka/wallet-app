package com.tugulev.wallet_app.service;

import com.tugulev.wallet_app.exception.ValidRequestException;
import com.tugulev.wallet_app.model.dto.OperationRequest;
import com.tugulev.wallet_app.model.dto.OperationResponse;
import com.tugulev.wallet_app.model.enums.OperationType;
import com.tugulev.wallet_app.model.enums.StatusOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class ResponseService {
        public ResponseEntity<OperationResponse> acceptResponseHandler(OperationType operationType,
                                                                       UUID walletId, BigDecimal amount) {
            return ResponseEntity.ok(OperationResponse.builder()
                    .StatusCode(200)
                    .operationType(operationType)
                    .statusOperation(StatusOperation.ACCEPTED)
                    .walletId(walletId)
                    .amount(amount)
                    .build());
        }

        public ResponseEntity<OperationResponse> errorResponseHandler(OperationType operationType,
                                                                       UUID walletId, BigDecimal amount,
                                                                       String messageError) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(OperationResponse.builder()
                            .StatusCode(400)
                            .operationType(operationType)
                            .statusOperation(StatusOperation.NOT_ACCEPTED)
                            .walletId(walletId)
                            .amount(amount)
                            .error(messageError)
                            .build());
        }

    public void validateJson(OperationRequest request) {
        if (request.getOperationType() == null) {
            log.error("Operation type is null");
            throw new ValidRequestException("Operation type not nullable");
        }

        try {
            OperationType.valueOf(request.getOperationType().name());
        } catch (RuntimeException e) {
            log.error("ERROR: OperationType is not supported: {}", request.getOperationType());
            throw new ValidRequestException("Your JSON is not allowed, please use the \"Operation Type\" " +
                    "as in the official document to API");
        }
        if (request.getWalletId() == null) {
            log.error("ERROR: WalletId is null");
                throw new ValidRequestException("Wallet Id can not be null");
        }
        if ((request.getAmount() == null) || (request.getAmount().compareTo(BigDecimal.ZERO) < 0)) {
            log.error("ERROR: Amount is {}", request.getAmount());
            throw new ValidRequestException("Amount can not be null and 0");
        }
    }
}

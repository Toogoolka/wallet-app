package com.tugulev.wallet_app.controller;

import com.tugulev.wallet_app.model.dto.OperationRequest;
import com.tugulev.wallet_app.model.dto.OperationResponse;
import com.tugulev.wallet_app.service.ResponseService;
import com.tugulev.wallet_app.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final ResponseService responseService;

    @PostMapping(value = "/wallet",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationResponse> walletOperation(@RequestBody OperationRequest request) {
        try {
            responseService.validateJson(request);
            walletService.operationHandler(request.getWalletId(), request.getOperationType(),
                    request.getAmount());
            return responseService.acceptResponseHandler(request.getOperationType(),
                    request.getWalletId(), request.getAmount());
        } catch (RuntimeException e) {
            return responseService.errorResponseHandler(request.getOperationType(),
                    request.getWalletId(), request.getAmount(), e.getMessage());
        }
    }


    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<?> getBalance(@PathVariable UUID walletId) {
        try {
            return ResponseEntity.ok(walletService.getBalanceByWalletId(walletId));
        } catch (RuntimeException e) {
            return handleErrorResponse(e);
        }
    }

    private ResponseEntity<?> handleErrorResponse(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}

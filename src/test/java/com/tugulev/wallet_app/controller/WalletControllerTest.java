package com.tugulev.wallet_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugulev.wallet_app.exception.WalletNotFoundException;
import com.tugulev.wallet_app.model.dto.OperationRequest;
import com.tugulev.wallet_app.model.enums.OperationType;
import com.tugulev.wallet_app.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class WalletControllerTest {
    private final static UUID WALLET_ID = UUID.fromString("1ea7b8a8-1eba-4288-a0ed-285b62b1d349");
    private final static UUID NOT_VALID_WALLET_ID = UUID.fromString("1ea7b8a8-1eba-4288-a0ed-295b62b1d349");
    private final static String GET_URI = "http://loclahost:8080/api/v1/wallets/";
    private final static String POST_URI = "http://loclahost:8080/api/v1/wallet";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalletService walletService;
    @Test
    void walletOperationTest() throws Exception {
        OperationRequest request = new OperationRequest();
        request.setWalletId(WALLET_ID);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(new BigDecimal(1000.00));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOperationRequestObject = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(POST_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOperationRequestObject))
                .andExpectAll(status().isOk());

        //операция списания
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(new BigDecimal(500.00));
        jsonOperationRequestObject = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(POST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOperationRequestObject))
                .andExpectAll(status().isOk());
    }



    @Test
    void getBalanceAcceptTest() throws Exception {
        BigDecimal balance = new BigDecimal("2000.00");


        when(walletService.getBalanceByWalletId(WALLET_ID)).thenReturn(balance);
        mockMvc.perform(get(GET_URI + WALLET_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(balance.toString()));
    }

    @Test
    void getBalanceErrorTest() throws Exception {
        when(walletService.getBalanceByWalletId(NOT_VALID_WALLET_ID))
                .thenThrow(new WalletNotFoundException());
        mockMvc.perform(get(GET_URI + NOT_VALID_WALLET_ID.toString()))
                .andExpect(status().isBadRequest());
    }

}

package com.tugulev.wallet_app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String message) {
        super(message);
    }
}

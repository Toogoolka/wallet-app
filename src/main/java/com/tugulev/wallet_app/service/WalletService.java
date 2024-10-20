package com.tugulev.wallet_app.service;

import com.tugulev.wallet_app.dao.WalletRepository;
import com.tugulev.wallet_app.exception.InsufficientFundsException;
import com.tugulev.wallet_app.exception.WalletNotFoundException;
import com.tugulev.wallet_app.model.entity.Wallet;
import com.tugulev.wallet_app.model.enums.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;

    /*Использую синхронную обработку запроса так как операции по изменению баланса должны быть согласованы.
    Пробовал при разработке этой логики проинициализировать пул из 10 потоков с помощью экзекьютора,
    а всю логику операции над запросом обрабатывать в статичном методе runAsync от CompletableFuture,
    над методом использовал @Async. Что привело к потерявшимся исключениям,
    баланс кошелька оставался согласованным, но ответ запрос клиенту доходил как "успешный". */
    public void operationHandler(UUID walletId, OperationType operationType,
                                                    BigDecimal amount) throws RuntimeException {
    log.info("Handler get started process for Wallet #{}", walletId);
        //Ищу кошелёк в базе
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        //Обрабатываю операцию, полученную в контроллере

        switch (operationType) {
            case DEPOSIT -> {
                wallet.setBalance(wallet.getBalance().add(amount));
                break;
            }
            case WITHDRAW -> {
                if (wallet.getBalance().compareTo(amount) < 0) {
                    log.error("ERROR: Insufficient funds in the Wallet #{}",walletId);
                    throw new InsufficientFundsException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
                break;
            }
        }
        walletRepository.save(wallet);
        log.info("Process for Wallet #{} successful", walletId);
    }


    public BigDecimal getBalanceByWalletId(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }
}

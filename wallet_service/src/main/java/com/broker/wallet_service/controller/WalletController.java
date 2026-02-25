package com.broker.wallet_service.controller;

import com.broker.wallet_service.DTOS.DepositRequest;
import com.broker.wallet_service.DTOS.WalletSumaryResponse;
import com.broker.wallet_service.repository.WalletRepository;
import com.broker.wallet_service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/me")
    public WalletSumaryResponse getWalletSummary(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        WalletSumaryResponse response = walletService.getWalletSummary(userId);

        return response;
    }


    @PostMapping("/NewDeposit")
    public ResponseEntity<BigDecimal> deposit(
            @RequestBody DepositRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        BigDecimal amount = request.getAmount();

        // Validación básica
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // Llamada al service para hacer el depósito
        BigDecimal updatedWallet = walletService.depositCash(userId, amount);

        return ResponseEntity.ok(updatedWallet);
    }
}

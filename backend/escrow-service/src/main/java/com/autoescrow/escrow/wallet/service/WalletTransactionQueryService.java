package com.autoescrow.escrow.wallet.service;

import java.util.List;

import com.autoescrow.escrow.wallet.dto.WalletTransactionDTO;

public interface WalletTransactionQueryService {

    List<WalletTransactionDTO> getMyTransactions(String userEmail);

    List<WalletTransactionDTO> getTransactionsByUser(String userEmail);
}

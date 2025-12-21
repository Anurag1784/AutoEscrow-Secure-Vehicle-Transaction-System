package com.autoescrow.escrow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescrow.escrow.entity.EscrowTransaction;

public interface EscrowTransactionRepository
extends JpaRepository<EscrowTransaction, Long> {

List<EscrowTransaction> findByBuyerEmail(String buyerEmail);
List<EscrowTransaction> findBySellerEmail(String sellerEmail);
}

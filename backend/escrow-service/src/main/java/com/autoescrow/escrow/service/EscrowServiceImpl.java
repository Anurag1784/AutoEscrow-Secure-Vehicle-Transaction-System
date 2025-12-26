package com.autoescrow.escrow.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescrow.escrow.audit.AdminAuditService;
import com.autoescrow.escrow.client.VehicleClient;
import com.autoescrow.escrow.dto.VehicleResponse;
import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.exception.EscrowNotFoundException;
import com.autoescrow.escrow.exception.InvalidEscrowStateException;
import com.autoescrow.escrow.exception.UnauthorizedActionException;
import com.autoescrow.escrow.payment.enums.PaymentType;
import com.autoescrow.escrow.payment.service.PaymentRecordService;
import com.autoescrow.escrow.repository.EscrowTransactionRepository;
import com.autoescrow.escrow.security.ServiceSecurityUtil;
import com.autoescrow.escrow.state.EscrowStateMachine;
import com.autoescrow.escrow.state.EscrowStatus;
import com.autoescrow.escrow.wallet.service.WalletService;

@Service
@Transactional
public class EscrowServiceImpl implements EscrowService {

    private final EscrowTransactionRepository repository;
    private final AdminAuditService auditService;
    private final VehicleClient vehicleClient;
    private final WalletService walletService;
    private final PaymentRecordService paymentRecordService;

    public EscrowServiceImpl(
            EscrowTransactionRepository repository,
            AdminAuditService auditService,
            VehicleClient vehicleClient,
            WalletService walletService,
            PaymentRecordService paymentRecordService) {

        this.repository = repository;
        this.auditService = auditService;
        this.vehicleClient = vehicleClient;
        this.walletService = walletService;
        this.paymentRecordService = paymentRecordService;
    }

    // ===============================
    // CREATE ESCROW (BUYER)
    // ===============================
    @Override
    public EscrowTransaction createEscrow(
            String buyerEmail,
            String sellerEmail,
            Long vehicleId,
            BigDecimal amount) {

        ServiceSecurityUtil.requireRole("BUYER");

        validateVehicleForEscrow(vehicleId);

        walletService.createWalletIfNotExists(buyerEmail);
        walletService.lockFunds(buyerEmail, amount);

        EscrowTransaction escrow = new EscrowTransaction();
        escrow.setBuyerEmail(buyerEmail);
        escrow.setSellerEmail(sellerEmail);
        escrow.setVehicleId(vehicleId);
        escrow.setAmount(amount);
        escrow.setCreatedAt(LocalDateTime.now());
        escrow.setSellerConfirmDeadline(LocalDateTime.now().plusHours(24));
        escrow.setStatus(EscrowStatus.FUNDS_DEPOSITED);
        escrow.setSellerConfirmed(false);
        escrow.setBuyerConfirmed(false);

        EscrowTransaction saved = repository.save(escrow);

        paymentRecordService.recordPayment(
                saved.getEscrowId(),
                buyerEmail,
                "ESCROW",
                amount,
                PaymentType.PAYMENT_LOCKED
        );

        return saved;
    }

    // ===============================
    // SELLER CONFIRM
    // ===============================
    @Override
    public EscrowTransaction sellerConfirm(Long escrowId, String sellerEmail) {

        ServiceSecurityUtil.requireRole("SELLER");

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new EscrowNotFoundException("Escrow not found"));

        if (!escrow.getSellerEmail().equals(sellerEmail)) {
            throw new UnauthorizedActionException("Unauthorized seller");
        }

        EscrowStateMachine.validateTransition(
                escrow.getStatus(),
                EscrowStatus.SELLER_CONFIRMED
        );

        escrow.setSellerConfirmed(true);
        escrow.setStatus(EscrowStatus.SELLER_CONFIRMED);
        escrow.setUpdatedAt(LocalDateTime.now());

        return repository.save(escrow);
    }

    // ===============================
    // BUYER CONFIRM  ✅ FINAL BUSINESS STEP
    // ===============================
    @Override
    public EscrowTransaction buyerConfirm(Long escrowId, String buyerEmail) {

        ServiceSecurityUtil.requireRole("BUYER");

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new EscrowNotFoundException("Escrow not found"));

        if (!escrow.getBuyerEmail().equals(buyerEmail)) {
            throw new UnauthorizedActionException("Unauthorized buyer");
        }

        if (!escrow.isSellerConfirmed()) {
            throw new InvalidEscrowStateException("Seller not confirmed yet");
        }

        // ✅ Ensure seller wallet exists
        walletService.createWalletIfNotExists(escrow.getSellerEmail());

        // Release locked funds
        walletService.releaseLockedToSeller(
                escrow.getBuyerEmail(),
                escrow.getSellerEmail(),
                escrow.getAmount()
        );

        escrow.setBuyerConfirmed(true);
        escrow.setStatus(EscrowStatus.COMPLETED);
        escrow.setCompletedAt(LocalDateTime.now());

        // 🔥 NEW STEP: MARK VEHICLE AS SOLD
        vehicleClient.updateVehicleStatus(
                escrow.getVehicleId(),
                Map.of("status", "SOLD")
        );

        paymentRecordService.recordPayment(
                escrow.getEscrowId(),
                escrow.getBuyerEmail(),
                escrow.getSellerEmail(),
                escrow.getAmount(),
                PaymentType.PAYMENT_RELEASED
        );

        return repository.save(escrow);
    }

    // ===============================
    // GET ESCROW BY ID
    // ===============================
    @Override
    public EscrowTransaction getEscrowById(Long escrowId) {
        return repository.findById(escrowId)
                .orElseThrow(() -> new EscrowNotFoundException("Escrow not found"));
    }

    // ===============================
    // BUYER CANCEL
    // ===============================
    @Override
    public EscrowTransaction cancelEscrow(Long escrowId, String buyerEmail) {

        ServiceSecurityUtil.requireRole("BUYER");

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new EscrowNotFoundException("Escrow not found"));

        walletService.refundLockedToBuyer(
                escrow.getBuyerEmail(),
                escrow.getAmount()
        );

        escrow.setStatus(EscrowStatus.REFUNDED);
        escrow.setCompletedAt(LocalDateTime.now());

        return repository.save(escrow);
    }

    // ===============================
    // ADMIN FORCE CANCEL
    // ===============================
    @Override
    public EscrowTransaction adminForceCancel(Long escrowId) {

        ServiceSecurityUtil.requireRole("ADMIN");

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new EscrowNotFoundException("Escrow not found"));

        escrow.setStatus(EscrowStatus.CANCELLED);
        escrow.setUpdatedAt(LocalDateTime.now());

        auditService.logAction(
                ServiceSecurityUtil.getCurrentUser(),
                "FORCE_CANCEL",
                escrowId,
                "Admin cancelled escrow"
        );

        return repository.save(escrow);
    }

    // ===============================
    // ADMIN FORCE REFUND
    // ===============================
    @Override
    public EscrowTransaction adminForceRefund(Long escrowId) {

        ServiceSecurityUtil.requireRole("ADMIN");

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new EscrowNotFoundException("Escrow not found"));

        walletService.refundLockedToBuyer(
                escrow.getBuyerEmail(),
                escrow.getAmount()
        );

        escrow.setStatus(EscrowStatus.REFUNDED);
        escrow.setCompletedAt(LocalDateTime.now());

        auditService.logAction(
                ServiceSecurityUtil.getCurrentUser(),
                "FORCE_REFUND",
                escrowId,
                "Admin refunded escrow"
        );

        return repository.save(escrow);
    }

    // ===============================
    // ADMIN VIEW ALL
    // ===============================
    @Override
    public List<EscrowTransaction> getAllEscrows() {
        ServiceSecurityUtil.requireRole("ADMIN");
        return repository.findAll();
    }

    // ===============================
    // VEHICLE VALIDATION
    // ===============================
    private void validateVehicleForEscrow(Long vehicleId) {

        VehicleResponse vehicle = vehicleClient.getVehicleById(vehicleId);

        if (vehicle == null) {
            throw new IllegalStateException("Vehicle not found");
        }

        if (!"ACTIVE".equalsIgnoreCase(vehicle.getStatus())) {
            throw new IllegalStateException("Vehicle is not available for escrow");
        }
    }
}

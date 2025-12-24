package com.autoescrow.escrow.exception;

public class EscrowNotFoundException extends RuntimeException {
    public EscrowNotFoundException(String message) {
        super(message);
    }
}

package com.autoescrow.escrow.exception;

public class InvalidEscrowStateException extends RuntimeException {
    public InvalidEscrowStateException(String message) {
        super(message);
    }
}

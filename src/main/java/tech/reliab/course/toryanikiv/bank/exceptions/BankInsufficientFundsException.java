package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class BankInsufficientFundsException extends RuntimeException {
    public BankInsufficientFundsException(UUID bankUUID, String action) {
        super("Bank(" + bankUUID.toString() + "): insufficient funds for action \"" + action + "\".");
    }
}

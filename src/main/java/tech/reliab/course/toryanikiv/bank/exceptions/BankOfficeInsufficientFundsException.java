package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class BankOfficeInsufficientFundsException extends RuntimeException {
    public BankOfficeInsufficientFundsException(UUID bankOfficeUUID, String action) {
        super("BankOffice(" + bankOfficeUUID.toString() + "): insufficient funds for action \"" + action + "\".");
    }
}

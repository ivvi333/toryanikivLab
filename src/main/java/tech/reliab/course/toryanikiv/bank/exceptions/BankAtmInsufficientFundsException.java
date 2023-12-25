package tech.reliab.course.toryanikiv.bank.exceptions;

import tech.reliab.course.toryanikiv.bank.entity.BankAtm;

import java.util.UUID;

public class BankAtmInsufficientFundsException extends RuntimeException {
    public BankAtmInsufficientFundsException(UUID bankAtmUUID, String action) {
        super("BankAtm(" + bankAtmUUID.toString() + "): insufficient funds for action \"" + action + "\".");
    }
}

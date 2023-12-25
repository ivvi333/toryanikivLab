package tech.reliab.course.toryanikiv.bank.exceptions;

import tech.reliab.course.toryanikiv.bank.entity.BankAtm;

import java.util.UUID;

public class BankAtmStatusException extends RuntimeException {
    public BankAtmStatusException(UUID bankAtmUUID, BankAtm.BankAtmStatus status) {
        super("BankAtm(" + bankAtmUUID.toString() + "): ATM already has the status \"" + status.toString() + "\".");
    }
}

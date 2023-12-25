package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class BankOfficeNoAtmSpaceException extends RuntimeException {
    public BankOfficeNoAtmSpaceException(UUID bankOfficeUUID) {
        super("BankOffice(" + bankOfficeUUID.toString() + "): no space for new ATM.");
    }
}

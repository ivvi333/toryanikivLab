package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class BankOfficeBankIsNullException extends RuntimeException {
    public BankOfficeBankIsNullException(UUID bankOfficeUUID) {
        super("BankOffice(" + bankOfficeUUID.toString() + "): bank is null.");
    }
}

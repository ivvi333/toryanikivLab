package tech.reliab.course.toryanikiv.bank.exceptions;

public class CreditNoBankException extends RuntimeException {
    public CreditNoBankException() {
        super("Bank available for this credit terms/conditions not found.");
    }
}

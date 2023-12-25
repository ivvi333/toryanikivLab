package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class PaymentAccountInsufficientFundsException extends RuntimeException {
    public PaymentAccountInsufficientFundsException(UUID paymentAccountUUID, String action) {
        super("PaymentAccount(" + paymentAccountUUID.toString() + "): insufficient funds for action \"" + action + "\".");
    }
}

package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class UserNoPaymentAccountException extends RuntimeException {
    public UserNoPaymentAccountException(UUID userUUID) {
        super("User(" + userUUID.toString() + "): payment account not found in user's payment accounts.");
    }
}

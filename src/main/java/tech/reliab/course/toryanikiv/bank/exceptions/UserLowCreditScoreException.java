package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class UserLowCreditScoreException extends RuntimeException {
    public UserLowCreditScoreException(UUID userUUID) {
        super("User(" + userUUID.toString() + "): credit not available, low credit score.");
    }
}

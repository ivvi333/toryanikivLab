package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CreditAccountService {
    UUID openCreditAccount(@NonNull User user, @NonNull Bank bank, @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                           @NonNull LocalDate creditOpeningDate, int creditDurationInMonths, @NonNull BigDecimal creditAmount);
    boolean closeCreditAccount(@NonNull CreditAccount creditAccount, @NonNull User user, @NonNull LocalDate currDate);
}

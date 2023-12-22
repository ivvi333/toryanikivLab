package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.Employee;
import tech.reliab.course.toryanikiv.bank.entity.PaymentAccount;
import tech.reliab.course.toryanikiv.bank.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CreditAccountService {
    boolean openCreditAccount(@NonNull User user, @NonNull Bank bank, @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                    @NonNull LocalDate creditOpeningDate, @NonNull int creditDurationInMonths, @NonNull BigDecimal creditAmount);
    boolean closeCreditAccount(@NonNull User user, @NonNull LocalDate currDate);
}

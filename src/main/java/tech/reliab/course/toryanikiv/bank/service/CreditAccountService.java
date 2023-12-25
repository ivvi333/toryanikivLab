package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.exceptions.BankInsufficientFundsException;
import tech.reliab.course.toryanikiv.bank.exceptions.EmployeeCreditIssueException;
import tech.reliab.course.toryanikiv.bank.exceptions.UserNoPaymentAccountException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CreditAccountService {
    UUID openCreditAccount(@NonNull User user, @NonNull Bank bank, @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                           @NonNull LocalDate creditOpeningDate, int creditDurationInMonths, @NonNull BigDecimal creditAmount)
            throws UserNoPaymentAccountException, BankInsufficientFundsException, EmployeeCreditIssueException;
    UUID openCreditAccount(@NonNull User user, @NonNull LocalDate creditOpeningDate, int creditDurationInMonths, @NonNull BigDecimal creditAmount,
                           @NonNull PaymentAccountService paymentAccountService);
    boolean closeCreditAccount(@NonNull CreditAccount creditAccount, @NonNull User user, @NonNull LocalDate currDate);
}

package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.exceptions.*;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentAccountService {
    UUID openPaymentAccount(@NonNull User user, @NonNull Bank bank);
    boolean closePaymentAccount(@NonNull PaymentAccount paymentAccount, @NonNull User user, @NonNull Bank bank)
            throws UserNoPaymentAccountException;
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm)
            throws BankAtmInsufficientFundsException;
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice)
            throws BankOfficeInsufficientFundsException;
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm)
            throws PaymentAccountInsufficientFundsException;
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice)
            throws PaymentAccountInsufficientFundsException;
}

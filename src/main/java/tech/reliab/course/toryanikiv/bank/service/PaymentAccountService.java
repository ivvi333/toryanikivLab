package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentAccountService {
    UUID openPaymentAccount(@NonNull User user, @NonNull Bank bank);
    boolean closePaymentAccount(@NonNull PaymentAccount paymentAccount, @NonNull User user, @NonNull Bank bank);
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm);
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice);
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm);
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice);
}

package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.math.BigDecimal;

public interface PaymentAccountService {
    boolean openPaymentAccount(@NonNull User user, @NonNull Bank bank);
    boolean closePaymentAccount(@NonNull User user);
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm);
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice);
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm);
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice);
}

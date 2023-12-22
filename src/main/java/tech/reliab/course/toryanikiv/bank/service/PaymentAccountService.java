package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankAtmDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentAccountService {
    UUID openPaymentAccount(@NonNull UserDao userDao, @NonNull User user, @NonNull BankDao bankDao, @NonNull Bank bank);
    boolean closePaymentAccount(@NonNull PaymentAccount paymentAccount, @NonNull UserDao userDao, @NonNull User user, @NonNull BankDao bankDao, @NonNull Bank bank);
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm);
    boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice);
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm);
    boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice);
}

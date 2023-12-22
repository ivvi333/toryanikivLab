package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface CreditAccountService {
    UUID openCreditAccount(@NonNull UserDao userDao, @NonNull User user, @NonNull BankDao bankDao, @NonNull Bank bank,
                           @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                           @NonNull LocalDate creditOpeningDate, @NonNull int creditDurationInMonths, @NonNull BigDecimal creditAmount);
    boolean closeCreditAccount(@NonNull CreditAccount creditAccount, @NonNull UserDao userDao, @NonNull User user, @NonNull LocalDate currDate);
}

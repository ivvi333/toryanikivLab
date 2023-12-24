package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.CreditAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.PaymentAccountDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.CreditAccount;
import tech.reliab.course.toryanikiv.bank.entity.PaymentAccount;
import tech.reliab.course.toryanikiv.bank.entity.User;

import java.io.IOException;

public interface UserService {
    boolean changeJob(@NonNull User user, @NonNull String job);
    void printPaymentAccounts(@NonNull User user);
    void printCreditAccounts(@NonNull User user);
    boolean saveUserInfo(@NonNull User user, @NonNull String targetDirPath) throws IOException;

    /** If you have changed the bank directly in JSON file */
    boolean changePaymentAccountBank(@NonNull String paymentAccountsDirPath,
                                     @NonNull PaymentAccountDao paymentAccountDao, @NonNull PaymentAccount paymentAccount,
                                     @NonNull BankDao bankDao) throws IOException;

    /** If you haven't yet changed the bank directly in JSON file (this method will change it in the JSON file) */
    boolean changePaymentAccountBank(@NonNull String paymentAccountsDirPath,
                                     @NonNull PaymentAccountDao paymentAccountDao, @NonNull PaymentAccount paymentAccount,
                                     @NonNull BankDao bankDao, @NonNull Bank bank) throws IOException;

    /** If you have changed the bank directly in JSON file */
    boolean changeCreditAccountBank(@NonNull String creditAccountsDirPath,
                                    @NonNull CreditAccountDao creditAccountDao, @NonNull CreditAccount creditAccount,
                                    @NonNull BankDao bankDao) throws IOException;

    /** If you haven't yet changed the bank directly in JSON file (this method will change it in the JSON file) */
    boolean changeCreditAccountBank(@NonNull String creditAccountsDirPath,
                                    @NonNull CreditAccountDao creditAccountDao, @NonNull CreditAccount creditAccount,
                                    @NonNull BankDao bankDao, @NonNull Bank bank) throws IOException;
}

package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.exceptions.BankInsufficientFundsException;

import java.math.BigDecimal;

public interface BankService {
    boolean addOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice, @NonNull BigDecimal initialTotalMoney)
            throws BankInsufficientFundsException;
    boolean deleteOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice);
    void printBankAtms(@NonNull Bank bank);
    void printBankOffices(@NonNull Bank bank);
    void printBankEmployees(@NonNull Bank bank);
    void printBankPaymentAccounts(@NonNull Bank bank);
    void printBankCreditAccounts(@NonNull Bank bank);
}

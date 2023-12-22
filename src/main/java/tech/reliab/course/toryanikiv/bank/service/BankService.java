package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;

public interface BankService {
    boolean addOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice);
    boolean deleteOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice);
    void printBankAtms(@NonNull Bank bank);
    void printBankOffices(@NonNull Bank bank);
    void printBankEmployees(@NonNull Bank bank);
    void printBankClients(@NonNull Bank bank);
}

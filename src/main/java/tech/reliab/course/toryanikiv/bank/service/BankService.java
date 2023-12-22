package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;

public interface BankService {
    boolean addOffice(@NonNull Bank bank, @NonNull BankOffice bankOffice);
    boolean deleteOffice(@NonNull Bank bank, @NonNull BankOffice bankOffice);
}

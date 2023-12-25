package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.exceptions.BankInsufficientFundsException;
import tech.reliab.course.toryanikiv.bank.service.BankOfficeService;
import tech.reliab.course.toryanikiv.bank.service.BankService;

import java.math.BigDecimal;

public class BankServiceImpl implements BankService {
    private final BankDao bankDao;

    public BankServiceImpl(@NonNull BankDao bankDao) {
        this.bankDao = bankDao;
    }

    @Override
    public boolean addOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice, @NonNull BigDecimal initialTotalMoney) {
        if (bank.getTotalMoney().compareTo(initialTotalMoney) < 0) {
            throw new BankInsufficientFundsException(bank.getUuid(), "addOffice");
        }

        bankOffice.setBank(bank);
        bankOffice.setAtmPlaceable(true);
        bankOffice.setTotalMoney(initialTotalMoney);

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankOfficeDao, bankDao);
        if (!bankOfficeService.open(bankOffice)) {
            return false;
        }

        bank.getBankOffices().add(bankOffice);
        bank.setTotalMoney(bank.getTotalMoney().subtract(initialTotalMoney));

        bankDao.update(bank);
        bankOfficeDao.update(bankOffice);

        return true;
    }

    @Override
    public boolean deleteOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice) {
        bankOffice.setBank(null);
        bankOffice.setAtmPlaceable(false);

        bank.setTotalMoney(bank.getTotalMoney().add(bankOffice.getTotalMoney()));
        bankOffice.setTotalMoney(BigDecimal.ZERO);

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankOfficeDao, bankDao);
        if (!bankOfficeService.close(bankOffice)) {
            return false;
        }

        bank.getBankOffices().remove(bankOffice);

        bankDao.update(bank);
        bankOfficeDao.update(bankOffice);

        return true;
    }

    @Override
    public void printBankAtms(@NonNull Bank bank) {
        bank.getBankOffices().forEach(bankOffice -> bankOffice.getBankAtms().forEach(System.out::println));
    }

    @Override
    public void printBankOffices(@NonNull Bank bank) {
        bank.getBankOffices().forEach(System.out::println);
    }

    @Override
    public void printBankEmployees(@NonNull Bank bank) {
        bank.getBankOffices().forEach(bankOffice -> bankOffice.getEmployees().forEach(System.out::println));
    }

    @Override
    public void printBankPaymentAccounts(@NonNull Bank bank) {
        bank.getPaymentAccounts().forEach(System.out::println);
    }

    @Override
    public void printBankCreditAccounts(@NonNull Bank bank) {
        bank.getCreditAccounts().forEach(System.out::println);
    }
}

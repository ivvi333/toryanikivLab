package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.service.BankOfficeService;
import tech.reliab.course.toryanikiv.bank.service.BankService;

import java.math.BigDecimal;

public class BankServiceImpl implements BankService {
    private final BankDao bankDao;

    public BankServiceImpl(@NonNull BankDao bankDao) {
        this.bankDao = bankDao;
    }

    @Override
    public boolean addOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice) {
        bankOffice.setBank(bank);
        bankOffice.setAtmPlaceable(true);
        bankOffice.setTotalMoney(bank.getTotalMoney());

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankOfficeDao);
        if (!bankOfficeService.open(bankOffice)) {
            return false;
        }

        bank.getBankOffices().add(bankOffice);

        bankDao.update(bank);

        return true;
    }

    @Override
    public boolean deleteOffice(@NonNull Bank bank, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice) {
        bankOffice.setBank(null);
        bankOffice.setAtmPlaceable(false);
        bankOffice.setTotalMoney(BigDecimal.ZERO);

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankOfficeDao);
        if (!bankOfficeService.close(bankOffice)) {
            return false;
        }

        bank.getBankOffices().remove(bankOffice);

        bankDao.update(bank);

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
    public void printBankClients(@NonNull Bank bank) {
        bank.getClients().forEach(System.out::println);
    }
}

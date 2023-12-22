package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.service.BankOfficeService;
import tech.reliab.course.toryanikiv.bank.service.BankService;

import java.math.BigDecimal;

public class BankServiceImpl implements BankService {
    @Override
    public boolean addOffice(@NonNull Bank bank, @NonNull BankOffice bankOffice) {
        bankOffice.setBank(bank);
        bankOffice.setAtmPlaceable(true);
        bankOffice.setTotalMoney(bank.getTotalMoney());

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl();
        if (!bankOfficeService.open(bankOffice)) {
            return false;
        }

        bank.setOfficeCount(bank.getOfficeCount() + 1);

        return true;
    }

    @Override
    public boolean deleteOffice(@NonNull Bank bank, @NonNull BankOffice bankOffice) {
        bankOffice.setBank(null);
        bankOffice.setAtmPlaceable(false);
        bankOffice.setTotalMoney(BigDecimal.ZERO);

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl();
        if (!bankOfficeService.close(bankOffice)) {
            return false;
        }

        bank.setOfficeCount(bank.getOfficeCount() - 1);

        return true;
    }
}

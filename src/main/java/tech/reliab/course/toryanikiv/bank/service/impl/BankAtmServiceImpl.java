package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankAtmDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.service.BankAtmService;

import java.math.BigDecimal;

public class BankAtmServiceImpl implements BankAtmService {
    private final BankAtmDao bankAtmDao;
    private final BankDao bankDao;
    private final BankOfficeDao bankOfficeDao;

    public BankAtmServiceImpl(@NonNull BankAtmDao bankAtmDao, BankDao bankDao, BankOfficeDao bankOfficeDao) {
        this.bankAtmDao = bankAtmDao;
        this.bankDao = bankDao;
        this.bankOfficeDao = bankOfficeDao;
    }

    @Override
    public boolean closeForMaintenance(@NonNull BankAtm bankAtm) {
        if (bankAtm.getStatus() == BankAtm.BankAtmStatus.OUT_OF_SERVICE) {
            return false;
        }

        bankAtm.setDepositAvailable(false);
        bankAtm.setWithdrawAvailable(false);
        bankAtm.setStatus(BankAtm.BankAtmStatus.OUT_OF_SERVICE);

        bankAtmDao.update(bankAtm);
        bankOfficeDao.update(bankAtm.getBankOffice());
        bankDao.update(bankAtm.getBank());

        return true;
    }

    @Override
    public boolean openAfterMaintenance(@NonNull BankAtm bankAtm) {
        if (bankAtm.getStatus() != BankAtm.BankAtmStatus.OUT_OF_SERVICE) {
            return false;
        }

        bankAtm.setDepositAvailable(true);

        if (bankAtm.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankAtm.setWithdrawAvailable(false);
            bankAtm.setStatus(BankAtm.BankAtmStatus.OUT_OF_CASH);
        }
        else {
            bankAtm.setWithdrawAvailable(true);
            bankAtm.setStatus(BankAtm.BankAtmStatus.FUNCTIONING);
        }

        bankAtmDao.update(bankAtm);
        bankOfficeDao.update(bankAtm.getBankOffice());
        bankDao.update(bankAtm.getBank());

        return true;
    }
}

package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankAtmDao;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.service.BankAtmService;

public class BankAtmServiceImpl implements BankAtmService {
    private final BankAtmDao bankAtmDao;

    public BankAtmServiceImpl(@NonNull BankAtmDao bankAtmDao) {
        this.bankAtmDao = bankAtmDao;
    }

    @Override
    public boolean closeForMaintenance(@NonNull BankAtm bankAtm) {
        bankAtm.setDepositAvailable(false);
        bankAtm.setWithdrawAvailable(false);
        bankAtm.setStatus(BankAtm.BankAtmStatus.OUT_OF_SERVICE);

        bankAtmDao.update(bankAtm);

        return true;
    }

    @Override
    public boolean openAfterMaintenance(@NonNull BankAtm bankAtm) {
        bankAtm.setDepositAvailable(true);
        bankAtm.setWithdrawAvailable(true);
        bankAtm.setStatus(BankAtm.BankAtmStatus.FUNCTIONING);

        bankAtmDao.update(bankAtm);

        return true;
    }
}

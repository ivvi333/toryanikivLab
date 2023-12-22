package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.*;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.PaymentAccountService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PaymentAccountServiceImpl implements PaymentAccountService {
    private final PaymentAccountDao paymentAccountDao;

    public PaymentAccountServiceImpl(PaymentAccountDao paymentAccountDao) {
        this.paymentAccountDao = paymentAccountDao;
    }

    @Override
    public UUID openPaymentAccount(@NonNull UserDao userDao, @NonNull User user, @NonNull BankDao bankDao, @NonNull Bank bank) {
        PaymentAccount paymentAccount = new PaymentAccount(user, bank);

        user.getPaymentAccounts().put(paymentAccount.getUuid(), paymentAccount);
        user.getBankNames().add(bank.getName());

        bank.getClients().add(user);

        userDao.update(user);
        bankDao.update(bank);

        return paymentAccountDao.save(paymentAccount);
    }

    @Override
    public boolean closePaymentAccount(@NonNull PaymentAccount paymentAccount, @NonNull UserDao userDao, @NonNull User user, @NonNull BankDao bankDao, @NonNull Bank bank) {
        if (!user.getPaymentAccounts().containsValue(paymentAccount)) {
            return false;
        }

        paymentAccountDao.delete(paymentAccount);

        user.getPaymentAccounts().remove(paymentAccount.getUuid());
        boolean isBankInUse = user.getPaymentAccounts().entrySet().stream()
                .anyMatch(o -> Objects.equals(o.getValue().getBank().getName(), paymentAccount.getBank().getName()));
        if (!isBankInUse) {
            user.getBankNames().remove(paymentAccount.getBank().getName());

            bank.getClients().remove(user);
            bankDao.update(bank);
        }

        userDao.update(user);

        return true;
    }

    @Override
    public boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm) {
        if (!bankAtm.isDepositAvailable()) {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().add(value));
        paymentAccountDao.update(paymentAccount);

        bankAtm.setTotalMoney(bankAtm.getTotalMoney().add(value));
        bankAtmDao.update(bankAtm);

        return true;
    }

    @Override
    public boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice) {
        if (!bankOffice.isDepositAvailable()) {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().add(value));
        paymentAccountDao.update(paymentAccount);

        bankOffice.setTotalMoney(bankOffice.getTotalMoney().add(value));
        bankOfficeDao.update(bankOffice);

        return true;
    }

    @Override
    public boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm) {
        if (!bankAtm.isWithdrawAvailable() || bankAtm.getTotalMoney().compareTo(value) < 0
            || paymentAccount.getBalance().compareTo(value) < 0)
        {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(value));
        paymentAccountDao.update(paymentAccount);

        bankAtm.setTotalMoney(bankAtm.getTotalMoney().subtract(value));
        if (bankAtm.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankAtm.setStatus(BankAtm.BankAtmStatus.OUT_OF_CASH);
            bankAtm.setWithdrawAvailable(false);
        }
        bankAtmDao.update(bankAtm);

        return true;
    }

    @Override
    public boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOfficeDao bankOfficeDao, @NonNull BankOffice bankOffice) {
        if (!bankOffice.isWithdrawAvailable() || bankOffice.getTotalMoney().compareTo(value) < 0
                || paymentAccount.getBalance().compareTo(value) < 0)
        {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(value));
        paymentAccountDao.update(paymentAccount);

        bankOffice.setTotalMoney(bankOffice.getTotalMoney().subtract(value));
        if (bankOffice.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankOffice.setWithdrawAvailable(false);
        }
        bankOfficeDao.update(bankOffice);

        return true;
    }
}

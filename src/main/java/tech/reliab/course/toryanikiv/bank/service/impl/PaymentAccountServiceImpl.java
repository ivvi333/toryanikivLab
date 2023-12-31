package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.*;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.exceptions.BankAtmInsufficientFundsException;
import tech.reliab.course.toryanikiv.bank.exceptions.BankOfficeInsufficientFundsException;
import tech.reliab.course.toryanikiv.bank.exceptions.PaymentAccountInsufficientFundsException;
import tech.reliab.course.toryanikiv.bank.exceptions.UserNoPaymentAccountException;
import tech.reliab.course.toryanikiv.bank.service.PaymentAccountService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class PaymentAccountServiceImpl implements PaymentAccountService {
    private final PaymentAccountDao paymentAccountDao;
    private final BankDao bankDao;
    private final UserDao userDao;
    private final BankOfficeDao bankOfficeDao;
    private final BankAtmDao bankAtmDao;

    public PaymentAccountServiceImpl(PaymentAccountDao paymentAccountDao, BankDao bankDao, UserDao userDao, BankOfficeDao bankOfficeDao, BankAtmDao bankAtmDao) {
        this.paymentAccountDao = paymentAccountDao;
        this.bankDao = bankDao;
        this.userDao = userDao;
        this.bankOfficeDao = bankOfficeDao;
        this.bankAtmDao = bankAtmDao;
    }

    @Override
    public UUID openPaymentAccount(@NonNull User user, @NonNull Bank bank) {
        PaymentAccount paymentAccount = new PaymentAccount(user, bank);

        user.getPaymentAccounts().put(paymentAccount.getUuid(), paymentAccount);
        user.getBankNames().add(bank.getName());

        bank.getPaymentAccounts().add(paymentAccount);

        userDao.update(user);
        bankDao.update(bank);

        return paymentAccountDao.save(paymentAccount);
    }

    @Override
    public boolean closePaymentAccount(@NonNull PaymentAccount paymentAccount, @NonNull User user, @NonNull Bank bank) {
        if (!user.getPaymentAccounts().containsValue(paymentAccount)) {
            throw new UserNoPaymentAccountException(user.getUuid());
        }

        paymentAccountDao.delete(paymentAccount);

        user.getPaymentAccounts().remove(paymentAccount.getUuid());
        boolean isBankInUse = user.getPaymentAccounts().entrySet().stream()
                .anyMatch(o -> Objects.equals(o.getValue().getBank().getName(), paymentAccount.getBank().getName()));
        if (!isBankInUse) {
            user.getBankNames().remove(paymentAccount.getBank().getName());
        }

        bank.getPaymentAccounts().remove(paymentAccount);

        userDao.update(user);
        bankDao.update(bank);

        return true;
    }

    @Override
    public boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm) {
        if (!bankAtm.isWithdrawAvailable()) {
            return false;
        }
        else if (bankAtm.getTotalMoney().compareTo(value) < 0) {
            throw new BankAtmInsufficientFundsException(bankAtm.getUuid(), "PaymentAccountService.deposit");
        }

        paymentAccount.setBalance(paymentAccount.getBalance().add(value));

        bankAtm.setTotalMoney(bankAtm.getTotalMoney().subtract(value));
        if (bankAtm.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankAtm.setStatus(BankAtm.BankAtmStatus.OUT_OF_CASH);
            bankAtm.setWithdrawAvailable(false);
        }

        paymentAccountDao.update(paymentAccount);
        userDao.update(paymentAccount.getUser());
        bankAtmDao.update(bankAtm);
        bankOfficeDao.update(bankAtm.getBankOffice());
        bankDao.update(paymentAccount.getBank());

        return true;
    }

    @Override
    public boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice) {
        if (!bankOffice.isWithdrawAvailable()) {
            return false;
        }
        else if (bankOffice.getTotalMoney().compareTo(value) < 0) {
            throw new BankOfficeInsufficientFundsException(bankOffice.getUuid(), "PaymentAccountService.deposit");
        }

        paymentAccount.setBalance(paymentAccount.getBalance().add(value));

        bankOffice.setTotalMoney(bankOffice.getTotalMoney().subtract(value));
        if (bankOffice.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankOffice.setWithdrawAvailable(false);
        }

        paymentAccountDao.update(paymentAccount);
        userDao.update(paymentAccount.getUser());
        bankAtmDao.getAll().filter(bankAtm -> bankOffice.equals(bankAtm.getBankOffice())).forEach(bankAtmDao::update);
        bankOfficeDao.update(bankOffice);
        bankDao.update(paymentAccount.getBank());

        return true;
    }

    @Override
    public boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm) {
        if (!bankAtm.isDepositAvailable())
        {
            return false;
        }
        else if (paymentAccount.getBalance().compareTo(value) < 0) {
            throw new PaymentAccountInsufficientFundsException(paymentAccount.getUuid(), "withdraw");
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(value));

        bankAtm.setTotalMoney(bankAtm.getTotalMoney().add(value));
        if (bankAtm.getStatus() == BankAtm.BankAtmStatus.OUT_OF_CASH) {
            bankAtm.setWithdrawAvailable(true);
            bankAtm.setStatus(BankAtm.BankAtmStatus.FUNCTIONING);
        }

        paymentAccountDao.update(paymentAccount);
        userDao.update(paymentAccount.getUser());
        bankAtmDao.update(bankAtm);
        bankOfficeDao.update(bankAtm.getBankOffice());
        bankDao.update(paymentAccount.getBank());

        return true;
    }

    @Override
    public boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice) {
        if (!bankOffice.isDepositAvailable()) {
            return false;
        }
        else if (paymentAccount.getBalance().compareTo(value) < 0) {
            throw new PaymentAccountInsufficientFundsException(paymentAccount.getUuid(), "withdraw");
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(value));

        bankOffice.setTotalMoney(bankOffice.getTotalMoney().add(value));
        if (!bankOffice.isWithdrawAvailable()) {
            bankOffice.setWithdrawAvailable(true);
        }

        paymentAccountDao.update(paymentAccount);
        userDao.update(paymentAccount.getUser());
        bankAtmDao.getAll().filter(bankAtm -> bankOffice.equals(bankAtm.getBankOffice())).forEach(bankAtmDao::update);
        bankOfficeDao.update(bankOffice);
        bankDao.update(paymentAccount.getBank());

        return true;
    }
}

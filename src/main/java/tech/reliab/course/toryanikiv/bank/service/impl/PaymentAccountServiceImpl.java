package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.PaymentAccountService;

import java.math.BigDecimal;

public class PaymentAccountServiceImpl implements PaymentAccountService {
    @Override
    public boolean openPaymentAccount(@NonNull User user, @NonNull Bank bank) {
        if (user.getPaymentAccount() != null) {
            return false;
        }

        PaymentAccount paymentAccount = new PaymentAccount(user, bank);

        user.setPaymentAccount(paymentAccount);
        user.setBank(bank);

        bank.setClientCount(bank.getClientCount() + 1);

        return true;
    }

    @Override
    public boolean closePaymentAccount(@NonNull User user) {
        if (user.getPaymentAccount() == null) {
            return false;
        }

        user.setPaymentAccount(null);
        user.getBank().setClientCount(user.getBank().getClientCount() - 1);
        user.setBank(null);

        return true;
    }

    @Override
    public boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm) {
        if (!bankAtm.isDepositAvailable()) {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().add(value));

        bankAtm.setTotalMoney(bankAtm.getTotalMoney().add(value));
        bankAtm.getBank().setTotalMoney(bankAtm.getBank().getTotalMoney().add(value));

        return true;
    }

    @Override
    public boolean deposit(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice) {
        if (!bankOffice.isDepositAvailable()) {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().add(value));

        bankOffice.setTotalMoney(bankOffice.getTotalMoney().add(value));
        bankOffice.getBank().setTotalMoney(bankOffice.getBank().getTotalMoney().add(value));

        return true;
    }

    @Override
    public boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankAtm bankAtm) {
        if (!bankAtm.isWithdrawAvailable() || bankAtm.getTotalMoney().compareTo(value) < 0
            || paymentAccount.getBalance().compareTo(value) < 0)
        {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(value));

        bankAtm.setTotalMoney(bankAtm.getTotalMoney().subtract(value));
        if (bankAtm.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankAtm.setStatus(BankAtm.BankAtmStatus.OUT_OF_CASH);
            bankAtm.setWithdrawAvailable(false);
        }
        bankAtm.getBank().setTotalMoney(bankAtm.getBank().getTotalMoney().subtract(value));

        return true;
    }

    @Override
    public boolean withdraw(@NonNull PaymentAccount paymentAccount, @NonNull BigDecimal value, @NonNull BankOffice bankOffice) {
        if (!bankOffice.isWithdrawAvailable() || bankOffice.getTotalMoney().compareTo(value) < 0
                || paymentAccount.getBalance().compareTo(value) < 0)
        {
            return false;
        }

        paymentAccount.setBalance(paymentAccount.getBalance().subtract(value));

        bankOffice.setTotalMoney(bankOffice.getTotalMoney().subtract(value));
        if (bankOffice.getTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
            bankOffice.setWithdrawAvailable(false);
        }
        bankOffice.getBank().setTotalMoney(bankOffice.getBank().getTotalMoney().subtract(value));

        return true;
    }
}

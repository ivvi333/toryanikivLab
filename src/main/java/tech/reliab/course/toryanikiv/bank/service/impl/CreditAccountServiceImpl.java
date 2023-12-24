package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.CreditAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.PaymentAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.CreditAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class CreditAccountServiceImpl implements CreditAccountService {
    private final CreditAccountDao creditAccountDao;
    private final PaymentAccountDao paymentAccountDao;
    private final BankDao bankDao;
    private final UserDao userDao;

    public CreditAccountServiceImpl(CreditAccountDao creditAccountDao, PaymentAccountDao paymentAccountDao, BankDao bankDao, UserDao userDao) {
        this.creditAccountDao = creditAccountDao;
        this.paymentAccountDao = paymentAccountDao;
        this.bankDao = bankDao;
        this.userDao = userDao;
    }

    @Override
    public UUID openCreditAccount(@NonNull User user, @NonNull Bank bank, @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                                  @NonNull LocalDate creditOpeningDate, int creditDurationInMonths, @NonNull BigDecimal creditAmount)
    {
        if (!user.getPaymentAccounts().containsValue(paymentAccount)
                || bank.getTotalMoney().compareTo(creditAmount) < 0
                || !creditAssistant.isCanIssueCredit())
        {
            return null;
        }

        CreditAccount creditAccount = new CreditAccount(user, bank, creditAssistant, paymentAccount, creditOpeningDate, creditDurationInMonths, creditAmount);

        bank.setTotalMoney(bank.getTotalMoney().subtract(creditAmount));

        user.getCreditAccounts().put(creditAccount.getUuid(), creditAccount);
        user.setCreditScore(user.getCreditScore() - 10f);

        bank.getCreditAccounts().add(creditAccount);

        userDao.update(user);
        bankDao.update(bank);

        return creditAccountDao.save(creditAccount);
    }

    @Override
    public boolean closeCreditAccount(@NonNull CreditAccount creditAccount, @NonNull User user, @NonNull LocalDate currDate) {
        if (!user.getCreditAccounts().containsValue(creditAccount) ||
                user.getCreditAccounts().get(creditAccount.getUuid()).getCreditClosingDate() != currDate)
        {
            return false;
        }

        creditAccountDao.delete(creditAccount);

        user.getCreditAccounts().remove(creditAccount.getUuid());
        user.setCreditScore(user.getCreditScore() + 10f);

        creditAccount.getBank().getCreditAccounts().remove(creditAccount);

        userDao.update(user);
        bankDao.update(creditAccount.getBank());

        return true;
    }
}

package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.CreditAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.CreditAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreditAccountServiceImpl implements CreditAccountService {
    private final CreditAccountDao creditAccountDao;

    public CreditAccountServiceImpl(CreditAccountDao creditAccountDao) {
        this.creditAccountDao = creditAccountDao;
    }

    @Override
    public UUID openCreditAccount(@NonNull UserDao userDao, @NonNull User user, @NonNull BankDao bankDao, @NonNull Bank bank,
                                  @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                                  @NonNull LocalDate creditOpeningDate, @NonNull int creditDurationInMonths, @NonNull BigDecimal creditAmount)
    {
        if (!user.getPaymentAccounts().containsValue(paymentAccount)
                || bank.getTotalMoney().compareTo(creditAmount) < 0
                || !creditAssistant.isCanIssueCredit())
        {
            return null;
        }

        CreditAccount creditAccount = new CreditAccount(user, bank, creditAssistant, paymentAccount, creditOpeningDate, creditDurationInMonths, creditAmount);

        bank.setTotalMoney(bank.getTotalMoney().subtract(creditAmount));
        bankDao.update(bank);

        user.getCreditAccounts().put(creditAccount.getUuid(), creditAccount);
        user.setCreditScore(user.getCreditScore() - 10f);
        userDao.update(user);

        return creditAccountDao.save(creditAccount);
    }

    @Override
    public boolean closeCreditAccount(@NonNull CreditAccount creditAccount, @NonNull UserDao userDao, @NonNull User user, @NonNull LocalDate currDate) {
        if (!user.getCreditAccounts().containsValue(creditAccount) ||
                user.getCreditAccounts().get(creditAccount.getUuid()).getCreditClosingDate() != currDate)
        {
            return false;
        }

        creditAccountDao.delete(creditAccount);

        user.getCreditAccounts().remove(creditAccount.getUuid());
        user.setCreditScore(user.getCreditScore() + 10f);
        userDao.update(user);

        return true;
    }
}

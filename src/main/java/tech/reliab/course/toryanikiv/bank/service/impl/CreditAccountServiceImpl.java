package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.CreditAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.PaymentAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.exceptions.*;
import tech.reliab.course.toryanikiv.bank.service.CreditAccountService;
import tech.reliab.course.toryanikiv.bank.service.PaymentAccountService;
import tech.reliab.course.toryanikiv.bank.service.utils.impl.BankComparatorImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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
        if (!user.getPaymentAccounts().containsValue(paymentAccount)) {
            throw new UserNoPaymentAccountException(user.getUuid());
        }
        else if (bank.getTotalMoney().compareTo(creditAmount) < 0) {
            throw new BankInsufficientFundsException(bank.getUuid(), "CreditAccountService.openCreditAccount");
        }
        else if (!creditAssistant.isCanIssueCredit()) {
            throw new EmployeeCreditIssueException(creditAssistant.getBankOffice().getUuid());
        }

        CreditAccount creditAccount = new CreditAccount(user, bank, creditAssistant, paymentAccount, creditOpeningDate, creditDurationInMonths, creditAmount);

        bank.setTotalMoney(bank.getTotalMoney().subtract(creditAmount));

        creditAccount.getPaymentAccount().setBalance(creditAccount.getPaymentAccount().getBalance().add(creditAmount));

        user.getCreditAccounts().put(creditAccount.getUuid(), creditAccount);
        user.setCreditScore(user.getCreditScore() - 10f);

        bank.getCreditAccounts().add(creditAccount);

        paymentAccountDao.update(creditAccount.getPaymentAccount());
        userDao.update(user);
        bankDao.update(bank);

        return creditAccountDao.save(creditAccount);
    }

    @Override
    public UUID openCreditAccount(@NonNull User user, @NonNull LocalDate creditOpeningDate, int creditDurationInMonths, @NonNull BigDecimal creditAmount,
                                  @NonNull PaymentAccountService paymentAccountService)
    {
        CreditAccount creditAccount = null;

        ArrayList<Bank> banks = new ArrayList<>(bankDao.getAll().toList());
        banks.sort(new BankComparatorImpl());
        Collections.reverse(banks);

        if (user.getCreditScore() < 300) {
            banks = new ArrayList<>(banks.stream().filter(bank -> bank.getRating() <= 50).toList());
        }
        if (banks.isEmpty()) {
            throw new UserLowCreditScoreException(user.getUuid());
        }

        for (Bank bank : banks) {
            bank.setBankOffices(new ArrayList<>(bank.getBankOffices().stream()
                    .filter(BankOffice::isCreditAvailable)
                    .toList()
            ));

            for (BankOffice bankOffice : bank.getBankOffices()) {
                bankOffice.setEmployees(new ArrayList<>(bankOffice.getEmployees().stream()
                        .filter(Employee::isCanIssueCredit)
                        .toList()
                ));
            }
            bank.setBankOffices(new ArrayList<>(bank.getBankOffices().stream()
                    .filter(bankOffice -> !bankOffice.getEmployees().isEmpty())
                    .toList()
            ));
        }
        banks = new ArrayList<>(banks.stream().filter(bank -> !bank.getBankOffices().isEmpty()).toList());
        if (banks.isEmpty()) {
            throw new CreditNoBankException();
        }
        Bank bank = banks.get(0);

        ArrayList<PaymentAccount> paymentAccounts = new ArrayList<>(user.getPaymentAccounts().values().stream()
                .filter(paymentAccount -> paymentAccount.getBank().getUuid() == bank.getUuid())
                .toList()
        );
        PaymentAccount paymentAccount = null;
        if (paymentAccounts.isEmpty()) {
            UUID paymentAccountUUID = paymentAccountService.openPaymentAccount(user, bank);
            paymentAccount = paymentAccountDao.getByUUID(paymentAccountUUID).get();
        }
        else {
            paymentAccount = paymentAccounts.get(0);
        }

        return openCreditAccount(user, bank, bank.getBankOffices().get(0).getEmployees().get(0), paymentAccount,
                creditOpeningDate, creditDurationInMonths, creditAmount);
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

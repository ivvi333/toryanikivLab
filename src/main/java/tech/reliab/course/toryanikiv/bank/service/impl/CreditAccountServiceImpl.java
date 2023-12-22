package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.CreditAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditAccountServiceImpl implements CreditAccountService {
    @Override
    public boolean openCreditAccount(@NonNull User user, @NonNull Bank bank, @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount, @NonNull LocalDate creditOpeningDate, @NonNull int creditDurationInMonths, @NonNull BigDecimal creditAmount) {
        if (user.getCreditAccount() != null || user.getPaymentAccount() != paymentAccount || !creditAssistant.isCanIssueCredit()) {
            return false;
        }

        CreditAccount creditAccount = new CreditAccount(user, bank, creditAssistant, paymentAccount, creditOpeningDate, creditDurationInMonths, creditAmount);

        user.setCreditAccount(creditAccount);

        return true;
    }

    @Override
    public boolean closeCreditAccount(@NonNull User user, @NonNull LocalDate currDate) {
        if (user.getCreditAccount() == null || user.getCreditAccount().getCreditClosingDate() != currDate) {
            return false;
        }

        user.setCreditAccount(null);
        user.setCreditScore(user.getCreditScore() + 10f);

        return true;
    }
}

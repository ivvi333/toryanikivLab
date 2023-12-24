package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uuid",
        scope = CreditAccount.class)
public class CreditAccount extends Account {
    private LocalDate creditOpeningDate;
    private LocalDate creditClosingDate;
    private int creditDurationInMonths;
    private BigDecimal creditAmount;
    private BigDecimal monthlyPayment;
    private float interestRate;
    private Employee creditAssistant;
    private PaymentAccount paymentAccount;

    @JsonCreator
    public CreditAccount(@NonNull User user, @NonNull Bank bank,
                         @NonNull Employee creditAssistant, @NonNull PaymentAccount paymentAccount,
                         @NonNull LocalDate creditOpeningDate, @NonNull int creditDurationInMonths,
                         @NonNull BigDecimal creditAmount)
    {
        super(user, bank);
        this.creditAssistant = creditAssistant;
        this.paymentAccount = paymentAccount;
        this.creditOpeningDate = creditOpeningDate;
        this.creditDurationInMonths = creditDurationInMonths;
        this.creditClosingDate = creditOpeningDate.plusMonths(creditDurationInMonths);
        this.creditAmount = creditAmount.setScale(2, RoundingMode.DOWN);
        this.interestRate = bank.getInterestRate();
        float mIR = this.interestRate / 1200.0f;
        this.monthlyPayment = creditAmount.multiply(BigDecimal.valueOf(mIR + mIR / (Math.pow(1.0f + mIR, creditDurationInMonths) - 1.0f)));
    }

    @Override
    public String toString() {
        return String.format("CreditAccount(uuid=%s, userUUID=%s, bankUUID=%s, creditOpeningDate=%s, creditClosingDate=%s, " +
                        "creditDurationInMonths=%d, creditAmount=%s, monthlyPayment=%s, interestRate=%f, " +
                        "creditAssistantUUID=%s, paymentAccountUUID=%s)",
                uuid.toString(), user.getUuid().toString(), bank.getUuid().toString(), creditOpeningDate.toString(), creditClosingDate.toString(),
                creditDurationInMonths, creditAmount.toString(), monthlyPayment.toString(), interestRate,
                creditAssistant.getUuid().toString(), paymentAccount.getUuid().toString()
        );
    }
}

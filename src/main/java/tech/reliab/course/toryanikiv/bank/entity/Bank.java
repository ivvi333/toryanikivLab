package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uuid",
        scope = Bank.class)
public class Bank {
    @Setter(AccessLevel.NONE) private UUID uuid;
    private String name;
    private ArrayList<BankOffice> bankOffices;
    private ArrayList<PaymentAccount> paymentAccounts;
    private ArrayList<CreditAccount> creditAccounts;
    private int rating;
    private BigDecimal totalMoney;
    private float interestRate;

    @JsonCreator
    public Bank(@NonNull @JsonProperty("name") String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.bankOffices = new ArrayList<>();
        this.paymentAccounts = new ArrayList<>();
        this.creditAccounts = new ArrayList<>();
        this.rating = ThreadLocalRandom.current().nextInt(0, 101);
        this.totalMoney = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(100000.0, 1000000.0)).setScale(2, RoundingMode.DOWN);
        this.interestRate = ThreadLocalRandom.current().nextFloat(1.0F, 21.5F - 1.5F * (((int) this.rating - 1) / 10 + 1));
    }

    @Override
    public String toString() {
        return String.format("Bank(uuid=%s, name=%s, officeCount=%d, paymentAccountCount=%d, creditAccountCount=%d, " +
                        "rating=%d, totalMoney=%s, interestRate=%f)",
                uuid.toString(), name, bankOffices.size(), paymentAccounts.size(), creditAccounts.size(),
                rating, totalMoney.toString(), interestRate
        );
    }
}

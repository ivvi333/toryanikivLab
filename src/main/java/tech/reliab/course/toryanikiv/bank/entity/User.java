package tech.reliab.course.toryanikiv.bank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class User {
    @Setter(AccessLevel.NONE) private UUID uuid;
    private String fullName;
    private LocalDate dateOfBirth;
    private String job;
    private float monthlyIncome;
    private HashSet<String> bankNames;
    private HashMap<UUID, CreditAccount> creditAccounts;
    private HashMap<UUID, PaymentAccount> paymentAccounts;
    private float creditScore;

    public User(@NonNull String fullName, @NonNull LocalDate dateOfBirth, @NonNull String job) {
        this.uuid = UUID.randomUUID();
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.job = job;
        this.monthlyIncome = ThreadLocalRandom.current().nextFloat(0, 10000.0f);
        this.bankNames = new HashSet<>();
        this.creditAccounts = new HashMap<>();
        this.paymentAccounts = new HashMap<>();
        this.creditScore = (((int) this.monthlyIncome) / 1000) * 100.0f + 100.0f;
    }

    @Override
    public String toString() {
        return String.format("User(uuid=%s, fullName=%s, dateOfBirth=%s, job=%s, monthlyIncome=%f, " +
                        "bankNames=%s, creditAccountCount=%d, paymentAccountCount=%d, creditScore=%f)",
                uuid.toString(), fullName, dateOfBirth.toString(), job, monthlyIncome,
                bankNames.toString(), creditAccounts.size(), paymentAccounts.size(), creditScore
        );
    }
}

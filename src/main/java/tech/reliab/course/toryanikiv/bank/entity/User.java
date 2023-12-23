package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import tech.reliab.course.toryanikiv.bank.deserializer.CreditAccountHashMapDeserializer;
import tech.reliab.course.toryanikiv.bank.deserializer.PaymentAccountHashMapDeserializer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uuid",
        scope = User.class)
public class User {
    @Setter(AccessLevel.NONE) private UUID uuid;
    private String fullName;
    private LocalDate dateOfBirth;
    private String job;
    private float monthlyIncome;
    private HashSet<String> bankNames;
    @JsonDeserialize(using = CreditAccountHashMapDeserializer.class)
    private HashMap<UUID, CreditAccount> creditAccounts;
    @JsonDeserialize(using = PaymentAccountHashMapDeserializer.class)
    private HashMap<UUID, PaymentAccount> paymentAccounts;
    private float creditScore;

    @JsonCreator
    public User(@NonNull @JsonProperty("fullName") String fullName, @NonNull @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
                @NonNull @JsonProperty("job") String job) {
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

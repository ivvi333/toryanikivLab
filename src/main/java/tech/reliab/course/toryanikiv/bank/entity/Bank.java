package tech.reliab.course.toryanikiv.bank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class Bank {
    @Setter(AccessLevel.NONE) private UUID uuid;
    private String name;
    private int officeCount;
    private int atmCount;
    private int employeeCount;
    private int clientCount;
    private int rating;
    private BigDecimal totalMoney;
    private float interestRate;

    public Bank(@NonNull String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.officeCount = 0;
        this.atmCount = 0;
        this.employeeCount = 0;
        this.clientCount = 0;
        this.rating = ThreadLocalRandom.current().nextInt(0, 101);
        this.totalMoney = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10000.0, 1000000.0)).setScale(2, RoundingMode.DOWN);
        this.interestRate = ThreadLocalRandom.current().nextFloat(1.0F, 21.5F - 1.5F * (((int) this.rating - 1) / 10 + 1));
    }

    @Override
    public String toString() {
        return String.format("Bank(uuid=%s, name=%s, officeCount=%d, atmCount=%d, employeeCount=%d, " +
                        "clientCount=%d, rating=%d, totalMoney=%s, interestRate=%f)",
                uuid.toString(), name, officeCount, atmCount, employeeCount,
                clientCount, rating, totalMoney.toString(), interestRate
        );
    }
}

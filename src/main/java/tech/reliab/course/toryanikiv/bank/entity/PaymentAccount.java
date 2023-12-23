package tech.reliab.course.toryanikiv.bank.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@Setter
public class PaymentAccount extends Account {
    private BigDecimal balance;

    public PaymentAccount(@NonNull User user, @NonNull Bank bank) {
        super(user, bank);
        this.balance = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return String.format("PaymentAccount(uuid=%s, userUUID=%s, bankUUID=%s, balance=%s)",
                uuid.toString(), user.getUuid().toString(), bank.getUuid().toString(), balance.toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentAccount that = (PaymentAccount) o;
        return Objects.equals(uuid, that.uuid)
                && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, balance);
    }
}

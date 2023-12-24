package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uuid",
        scope = BankAtm.class)
public class BankAtm {
    public enum BankAtmStatus {
        FUNCTIONING,
        OUT_OF_SERVICE,
        OUT_OF_CASH
    }

    @Setter(AccessLevel.NONE) private UUID uuid;
    private String name;
    private String address;
    private BankAtmStatus status;
    private BankOffice bankOffice;
    private Employee operator;
    private boolean isWithdrawAvailable;
    private boolean isDepositAvailable;
    private BigDecimal totalMoney;
    private BigDecimal maintenanceCost;

    public BankAtm(@NonNull String name, @NonNull BigDecimal maintenanceCost) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.address = "";
        this.status = BankAtmStatus.OUT_OF_SERVICE;
        this.bankOffice = null;
        this.operator = null;
        this.isWithdrawAvailable = false;
        this.isDepositAvailable = false;
        this.totalMoney = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.DOWN);
        this.maintenanceCost = maintenanceCost.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return String.format("BankAtm(uuid=%s, name=%s, address=%s, status=%s, " +
                        "bankUUID=%s, bankOfficeUUID=%s, operatorUUID=%s, " +
                        "isWithdrawAvailable=%b, isDepositAvailable=%b, totalMoney=%s, maintenanceCost=%s)",
                uuid.toString(), name, address, status.toString(),
                bankOffice.getBank().getUuid().toString(), bankOffice.getUuid().toString(), operator == null ? "" : operator.getUuid().toString(),
                isWithdrawAvailable, isDepositAvailable, totalMoney.toString(), maintenanceCost.toString()
        );
    }
}

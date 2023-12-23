package tech.reliab.course.toryanikiv.bank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public class BankOffice {
    @Setter(AccessLevel.NONE) private UUID uuid;
    private String name;
    private String address;
    private Bank bank;
    private boolean isOpen;
    private boolean isAtmPlaceable;
    private ArrayList<BankAtm> bankAtms;
    private ArrayList<Employee> employees;
    private boolean isCreditAvailable;
    private boolean isWithdrawAvailable;
    private boolean isDepositAvailable;
    private BigDecimal totalMoney;
    private BigDecimal leaseCost;

    public BankOffice(@NonNull String name, @NonNull String address, @NonNull BigDecimal leaseCost) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.address = address;
        this.bank = null;
        this.isOpen = false;
        this.isAtmPlaceable = false;
        this.bankAtms = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.isCreditAvailable = false;
        this.isWithdrawAvailable = false;
        this.isDepositAvailable = false;
        this.totalMoney = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.DOWN);
        this.leaseCost = leaseCost.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return String.format("BankOffice(uuid=%s, name=%s, address=%s, bankUUID=%s, isOpen=%b, isAtmPlaceable=%b, atmCount=%d," +
                        "employeeCount=%d, isCreditAvailable=%b, isWithdrawAvailable=%b, isDepositAvailable=%b, totalMoney=%s, leaseCost=%s)",
                uuid.toString(), name, address, bank == null ? "" : bank.getUuid().toString(), isOpen, isAtmPlaceable, bankAtms.size(),
                employees.size(), isCreditAvailable, isWithdrawAvailable, isDepositAvailable, totalMoney.toString(), leaseCost.toString()
        );
    }
}


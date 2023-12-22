package tech.reliab.course.toryanikiv.bank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Employee {
    public enum EmployeeOccupation {
        BANKER,
        TELLER,
        ANALYST,
        ASSISTANT,
        MANAGER
    }

    @Setter(AccessLevel.NONE) private UUID uuid;
    private String fullName;
    private LocalDate dateOfBirth;
    private EmployeeOccupation occupation;
    private Bank bank;
    private boolean isWorkingRemotely;
    private BankOffice bankOffice;
    private boolean canIssueCredit;
    private BigDecimal salary;

    public Employee(@NonNull String fullName, @NonNull LocalDate dateOfBirth) {
        this.uuid = UUID.randomUUID();
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.occupation = null;
        this.bank = null;
        this.isWorkingRemotely = false;
        this.bankOffice = null;
        this.canIssueCredit = false;
        this.salary = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return String.format("Employee(uuid=%s, fullName=%s, dateOfBirth=%s, occupation=%s, bank=%s, " +
                        "isWorkingRemotely=%b, bankOffice=%s, canIssueCredit=%b, salary=%s)",
                uuid.toString(), fullName, dateOfBirth.toString(), occupation == null ? "" : occupation.toString(), bank == null ? "" : bank.toString(),
                isWorkingRemotely, bankOffice == null ? "" : bankOffice.toString(), canIssueCredit, salary.toString()
        );
    }
}

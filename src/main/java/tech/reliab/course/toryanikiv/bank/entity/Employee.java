package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.*;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uuid",
        scope = Employee.class)
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
    private boolean isWorkingRemotely;
    private BankOffice bankOffice;
    private boolean canIssueCredit;
    private BigDecimal salary;

    public Employee(@NonNull String fullName, @NonNull LocalDate dateOfBirth) {
        this.uuid = UUID.randomUUID();
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.occupation = null;
        this.isWorkingRemotely = false;
        this.bankOffice = null;
        this.canIssueCredit = false;
        this.salary = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return String.format("Employee(uuid=%s, fullName=%s, dateOfBirth=%s, occupation=%s, " +
                        "bankUUID=%s, isWorkingRemotely=%b, bankOfficeUUID=%s, " +
                        "canIssueCredit=%b, salary=%s)",
                uuid.toString(), fullName, dateOfBirth.toString(), occupation == null ? "" : occupation.toString(),
                bankOffice.getBank().getUuid().toString(), isWorkingRemotely, bankOffice.getUuid().toString(),
                canIssueCredit, salary.toString()
        );
    }
}

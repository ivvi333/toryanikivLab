package tech.reliab.course.toryanikiv.bank.exceptions;

import java.util.UUID;

public class EmployeeCreditIssueException extends RuntimeException {
    public EmployeeCreditIssueException(UUID employeeUUID) {
        super("Employee(" + employeeUUID + "): employee can't issue credits.");
    }
}

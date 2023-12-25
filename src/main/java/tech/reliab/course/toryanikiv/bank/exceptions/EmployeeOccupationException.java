package tech.reliab.course.toryanikiv.bank.exceptions;

import tech.reliab.course.toryanikiv.bank.entity.Employee;

import java.util.UUID;

public class EmployeeOccupationException extends RuntimeException {
    public EmployeeOccupationException(UUID employeeUUID, Employee.EmployeeOccupation occupation) {
        super("Employee(" + employeeUUID.toString() + "): employee already has the occupation \"" + occupation.toString() + "\".");
    }
}

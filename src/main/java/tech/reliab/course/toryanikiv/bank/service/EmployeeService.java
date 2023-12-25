package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.Employee;
import tech.reliab.course.toryanikiv.bank.exceptions.EmployeeOccupationException;

public interface EmployeeService {
    void changeOccupation(@NonNull Employee employee, @NonNull Employee.EmployeeOccupation employeeOccupation) throws EmployeeOccupationException;
    boolean startWorkingRemotely(@NonNull Employee employee);
    boolean startWorkingInOffice(@NonNull Employee employee);
}

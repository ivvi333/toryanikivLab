package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.Employee;

public interface EmployeeService {
    boolean changeOccupation(@NonNull Employee employee, @NonNull Employee.EmployeeOccupation employeeOccupation);
    boolean startWorkingRemotely(@NonNull Employee employee);
    boolean startWorkingInOffice(@NonNull Employee employee);
}

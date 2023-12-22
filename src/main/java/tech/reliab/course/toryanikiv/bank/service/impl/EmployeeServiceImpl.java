package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.EmployeeDao;
import tech.reliab.course.toryanikiv.bank.entity.Employee;
import tech.reliab.course.toryanikiv.bank.service.EmployeeService;

import java.math.BigDecimal;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public boolean changeOccupation(@NonNull Employee employee, Employee.@NonNull EmployeeOccupation employeeOccupation) {
        if (employee.getOccupation() == employeeOccupation) {
            return false;
        }

        switch (employeeOccupation) {
            case BANKER -> employee.setSalary(BigDecimal.valueOf(5000));
            case MANAGER, ANALYST -> employee.setSalary(BigDecimal.valueOf(1000));
            case TELLER, ASSISTANT -> employee.setSalary(BigDecimal.valueOf(500));
        }
        employee.setOccupation(employeeOccupation);
        if (employeeOccupation == Employee.EmployeeOccupation.ASSISTANT) {
            employee.setCanIssueCredit(true);
        }

        employeeDao.update(employee);

        return true;
    }

    @Override
    public boolean startWorkingRemotely(@NonNull Employee employee) {
        if (employee.isWorkingRemotely()) {
            return false;
        }

        employee.setWorkingRemotely(true);

        employeeDao.update(employee);

        return true;
    }

    @Override
    public boolean startWorkingInOffice(@NonNull Employee employee) {
        if (!employee.isWorkingRemotely()) {
            return false;
        }

        employee.setWorkingRemotely(false);

        employeeDao.update(employee);

        return true;
    }
}

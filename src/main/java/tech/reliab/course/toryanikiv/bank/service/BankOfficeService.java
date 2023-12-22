package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankAtmDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.EmployeeDao;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.entity.Employee;

public interface BankOfficeService {
    boolean addAtm(@NonNull BankOffice bankOffice, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm, @NonNull Employee operator);
    boolean deleteAtm(@NonNull BankOffice bankOffice, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm);
    boolean addEmployee(@NonNull BankOffice bankOffice, @NonNull EmployeeDao employeeDao, @NonNull Employee employee, @NonNull Employee.EmployeeOccupation employeeOccupation);
    boolean deleteEmployee(@NonNull BankOffice bankOffice, @NonNull EmployeeDao employeeDao, @NonNull Employee employee);
    boolean close(@NonNull BankOffice bankOffice);
    boolean open(@NonNull BankOffice bankOffice);
}

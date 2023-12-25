package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankAtmDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.EmployeeDao;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.entity.Employee;
import tech.reliab.course.toryanikiv.bank.exceptions.*;

import java.math.BigDecimal;

public interface BankOfficeService {
    void addAtm(@NonNull BankOffice bankOffice, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm,
                   @NonNull Employee operator, @NonNull BigDecimal initialTotalMoney)
            throws BankOfficeNoAtmSpaceException, BankOfficeBankIsNullException, BankInsufficientFundsException;
    void deleteAtm(@NonNull BankOffice bankOffice, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm)
            throws BankOfficeBankIsNullException;
    void addEmployee(@NonNull BankOffice bankOffice, @NonNull EmployeeDao employeeDao,
                        @NonNull Employee employee, @NonNull Employee.EmployeeOccupation employeeOccupation)
            throws BankOfficeBankIsNullException;
    void deleteEmployee(@NonNull BankOffice bankOffice, @NonNull EmployeeDao employeeDao, @NonNull Employee employee)
            throws BankOfficeBankIsNullException;
    boolean close(@NonNull BankOffice bankOffice);
    boolean open(@NonNull BankOffice bankOffice);
}

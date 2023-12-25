package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankAtmDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankOfficeDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.EmployeeDao;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.entity.Employee;
import tech.reliab.course.toryanikiv.bank.exceptions.BankAtmStatusException;
import tech.reliab.course.toryanikiv.bank.exceptions.BankInsufficientFundsException;
import tech.reliab.course.toryanikiv.bank.exceptions.BankOfficeBankIsNullException;
import tech.reliab.course.toryanikiv.bank.exceptions.BankOfficeNoAtmSpaceException;
import tech.reliab.course.toryanikiv.bank.service.BankAtmService;
import tech.reliab.course.toryanikiv.bank.service.BankOfficeService;
import tech.reliab.course.toryanikiv.bank.service.EmployeeService;

import java.math.BigDecimal;

public class BankOfficeServiceImpl implements BankOfficeService {
    private final BankOfficeDao bankOfficeDao;
    private final BankDao bankDao;

    public BankOfficeServiceImpl(@NonNull BankOfficeDao bankOfficeDao, BankDao bankDao) {
        this.bankOfficeDao = bankOfficeDao;
        this.bankDao = bankDao;
    }

    @Override
    public void addAtm(@NonNull BankOffice bankOffice, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm,
                          @NonNull Employee operator, @NonNull BigDecimal initialTotalMoney)
    {
        if (!bankOffice.isAtmPlaceable()) {
            throw new BankOfficeNoAtmSpaceException(bankOffice.getUuid());
        }
        else if (bankOffice.getBank() == null) {
            throw new BankOfficeBankIsNullException(bankOffice.getUuid());
        }
        else if (bankOffice.getBank().getTotalMoney().compareTo(initialTotalMoney) < 0) {
            throw new BankInsufficientFundsException(bankOffice.getBank().getUuid(), "addAtm");
        }

        bankAtm.setAddress(bankOffice.getAddress());
        bankAtm.setBankOffice(bankOffice);
        bankAtm.setOperator(operator);
        bankAtm.setTotalMoney(initialTotalMoney);

        BankAtmService bankAtmService = new BankAtmServiceImpl(bankAtmDao, bankDao, bankOfficeDao);
        bankAtmService.openAfterMaintenance(bankAtm);

        bankOffice.getBankAtms().add(bankAtm);
        bankOffice.getBank().setTotalMoney(bankOffice.getBank().getTotalMoney().subtract(initialTotalMoney));

        bankOfficeDao.update(bankOffice);
        bankDao.update(bankOffice.getBank());
        bankAtmDao.update(bankAtm);
    }

    @Override
    public void deleteAtm(@NonNull BankOffice bankOffice, @NonNull BankAtmDao bankAtmDao, @NonNull BankAtm bankAtm) {
        if (bankOffice.getBank() == null) {
            throw new BankOfficeBankIsNullException(bankOffice.getUuid());
        }

        bankAtm.setAddress("");
        bankAtm.setBankOffice(null);
        bankAtm.setOperator(null);

        bankOffice.getBank().setTotalMoney(bankOffice.getBank().getTotalMoney().add(bankAtm.getTotalMoney()));
        bankAtm.setTotalMoney(BigDecimal.ZERO);

        BankAtmService bankAtmService = new BankAtmServiceImpl(bankAtmDao, bankDao, bankOfficeDao);
        bankAtmService.closeForMaintenance(bankAtm);

        bankOffice.getBankAtms().remove(bankAtm);

        bankOfficeDao.update(bankOffice);
        bankDao.update(bankOffice.getBank());
        bankAtmDao.update(bankAtm);
    }

    @Override
    public void addEmployee(@NonNull BankOffice bankOffice, @NonNull EmployeeDao employeeDao, @NonNull Employee employee, Employee.@NonNull EmployeeOccupation employeeOccupation) {
        if (bankOffice.getBank() == null) {
            throw new BankOfficeBankIsNullException(bankOffice.getUuid());
        }

        employee.setBankOffice(bankOffice);

        EmployeeService employeeService = new EmployeeServiceImpl(employeeDao, bankDao, bankOfficeDao);
        employeeService.changeOccupation(employee, employeeOccupation);

        bankOffice.getEmployees().add(employee);

        bankOfficeDao.update(bankOffice);
        bankDao.update(bankOffice.getBank());
        employeeDao.update(employee);
    }

    @Override
    public void deleteEmployee(@NonNull BankOffice bankOffice, @NonNull EmployeeDao employeeDao, @NonNull Employee employee) {
        if (bankOffice.getBank() == null) {
            throw new BankOfficeBankIsNullException(bankOffice.getUuid());
        }

        employee.setBankOffice(null);
        employee.setWorkingRemotely(false);
        employee.setCanIssueCredit(false);
        employee.setOccupation(null);
        employee.setSalary(BigDecimal.ZERO);

        bankOffice.getEmployees().remove(employee);

        bankOfficeDao.update(bankOffice);
        bankDao.update(bankOffice.getBank());
        employeeDao.update(employee);
    }

    @Override
    public boolean close(@NonNull BankOffice bankOffice) {
        bankOffice.setCreditAvailable(false);
        bankOffice.setDepositAvailable(false);
        bankOffice.setWithdrawAvailable(false);
        bankOffice.setOpen(false);

        bankOfficeDao.update(bankOffice);
        bankDao.update(bankOffice.getBank());

        return true;
    }

    @Override
    public boolean open(@NonNull BankOffice bankOffice) {
        bankOffice.setCreditAvailable(true);
        bankOffice.setDepositAvailable(true);
        bankOffice.setWithdrawAvailable(true);
        bankOffice.setOpen(true);

        bankOfficeDao.update(bankOffice);
        bankDao.update(bankOffice.getBank());

        return true;
    }
}

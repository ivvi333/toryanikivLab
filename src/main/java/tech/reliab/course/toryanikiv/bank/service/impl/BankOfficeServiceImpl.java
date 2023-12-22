package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;
import tech.reliab.course.toryanikiv.bank.entity.Employee;
import tech.reliab.course.toryanikiv.bank.service.BankAtmService;
import tech.reliab.course.toryanikiv.bank.service.BankOfficeService;
import tech.reliab.course.toryanikiv.bank.service.EmployeeService;

import java.math.BigDecimal;

public class BankOfficeServiceImpl implements BankOfficeService {
    @Override
    public boolean addAtm(@NonNull BankOffice bankOffice, @NonNull BankAtm bankAtm, @NonNull Employee operator) {
        if (!bankOffice.isAtmPlaceable() || bankOffice.getBank() == null) {
            return false;
        }

        bankAtm.setAddress(bankOffice.getAddress());
        bankAtm.setBank(bankOffice.getBank());
        bankAtm.setBankOffice(bankOffice);
        bankAtm.setOperator(operator);
        bankAtm.setTotalMoney(bankOffice.getTotalMoney());

        BankAtmService bankAtmService = new BankAtmServiceImpl();
        if (!bankAtmService.openAfterMaintenance(bankAtm)) {
            return false;
        }

        bankOffice.setAtmCount(bankOffice.getAtmCount() + 1);
        bankOffice.getBank().setAtmCount(bankOffice.getAtmCount());

        return true;
    }

    @Override
    public boolean deleteAtm(@NonNull BankOffice bankOffice, @NonNull BankAtm bankAtm) {
        bankAtm.setAddress("");
        bankAtm.setBank(null);
        bankAtm.setBankOffice(null);
        bankAtm.setOperator(null);
        bankAtm.setTotalMoney(BigDecimal.ZERO);

        BankAtmService bankAtmService = new BankAtmServiceImpl();
        if (!bankAtmService.closeForMaintenance(bankAtm)) {
            return false;
        }

        bankOffice.setAtmCount(bankOffice.getAtmCount() - 1);
        bankOffice.getBank().setAtmCount(bankOffice.getAtmCount());

        return true;
    }

    @Override
    public boolean addEmployee(@NonNull BankOffice bankOffice, @NonNull Employee employee, Employee.@NonNull EmployeeOccupation employeeOccupation) {
        if (bankOffice.getBank() == null) {
            return false;
        }

        employee.setBank(bankOffice.getBank());
        employee.setBankOffice(bankOffice);

        EmployeeService employeeService = new EmployeeServiceImpl();
        if (!employeeService.changeOccupation(employee, employeeOccupation)) {
            return false;
        }

        bankOffice.getBank().setEmployeeCount(bankOffice.getBank().getEmployeeCount() + 1);

        return true;
    }

    @Override
    public boolean deleteEmployee(@NonNull BankOffice bankOffice, @NonNull Employee employee) {
        if (bankOffice.getBank() == null) {
            return false;
        }

        employee.setBank(null);
        employee.setBankOffice(null);
        employee.setWorkingRemotely(false);
        employee.setCanIssueCredit(false);
        employee.setOccupation(null);
        employee.setSalary(BigDecimal.ZERO);

        bankOffice.getBank().setEmployeeCount(bankOffice.getBank().getEmployeeCount() - 1);

        return true;
    }

    @Override
    public boolean close(@NonNull BankOffice bankOffice) {
        bankOffice.setCreditAvailable(false);
        bankOffice.setDepositAvailable(false);
        bankOffice.setWithdrawAvailable(false);
        bankOffice.setOpen(false);

        return true;
    }

    @Override
    public boolean open(@NonNull BankOffice bankOffice) {
        bankOffice.setCreditAvailable(true);
        bankOffice.setDepositAvailable(true);
        bankOffice.setWithdrawAvailable(true);
        bankOffice.setOpen(true);

        return true;
    }
}

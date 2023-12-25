package tech.reliab.course.toryanikiv.bank.service.utils.impl;

import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.service.utils.BankComparator;

public class BankComparatorImpl implements BankComparator {
    @Override
    public int compare(Bank bank1, Bank bank2) {
        int atmCount1, atmCount2;
        int employeeCount1, employeeCount2;

        atmCount1 = bank1.getBankOffices().stream().mapToInt(bankOffice -> bankOffice.getBankAtms().size()).sum();
        employeeCount1 = bank1.getBankOffices().stream().mapToInt(bankOffice -> bankOffice.getEmployees().size()).sum();
        atmCount2 = bank2.getBankOffices().stream().mapToInt(bankOffice -> bankOffice.getBankAtms().size()).sum();
        employeeCount2 = bank2.getBankOffices().stream().mapToInt(bankOffice -> bankOffice.getEmployees().size()).sum();

        double bankCriterion1 = (bank1.getBankOffices().size() + atmCount1 + employeeCount1) / bank1.getInterestRate();
        double bankCriterion2 = (bank2.getBankOffices().size() + atmCount2 + employeeCount2) / bank2.getInterestRate();
        return Double.compare(bankCriterion1, bankCriterion2);
    }
}

package tech.reliab.course.toryanikiv.bank;

import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.BankOfficeService;
import tech.reliab.course.toryanikiv.bank.service.BankService;
import tech.reliab.course.toryanikiv.bank.service.CreditAccountService;
import tech.reliab.course.toryanikiv.bank.service.PaymentAccountService;
import tech.reliab.course.toryanikiv.bank.service.impl.BankOfficeServiceImpl;
import tech.reliab.course.toryanikiv.bank.service.impl.BankServiceImpl;
import tech.reliab.course.toryanikiv.bank.service.impl.CreditAccountServiceImpl;
import tech.reliab.course.toryanikiv.bank.service.impl.PaymentAccountServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank("Test bank");
        BankAtm bankAtm = new BankAtm("Test bank ATM", BigDecimal.valueOf(1000));
        BankOffice bankOffice = new BankOffice("Test bank office", "Test address", BigDecimal.valueOf(5000));
        Employee employee = new Employee("Test Test Test", LocalDate.of(2002, 7, 9));
        User user = new User("Testing Testing Testing", LocalDate.of(2001, 6, 8), "Tester");

        BankService bankService = new BankServiceImpl();
        bankService.addOffice(bank, bankOffice);

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl();
        bankOfficeService.addEmployee(bankOffice, employee, Employee.EmployeeOccupation.ASSISTANT);
        bankOfficeService.addAtm(bankOffice, bankAtm, employee);

        PaymentAccountService paymentAccountService = new PaymentAccountServiceImpl();
        paymentAccountService.openPaymentAccount(user, bank);

        CreditAccountService creditAccountService = new CreditAccountServiceImpl();
        creditAccountService.openCreditAccount(user, bank, employee, user.getPaymentAccount(),
                LocalDate.of(2024, 1, 1), 2, BigDecimal.valueOf(1000));

        System.out.println(bank);
        System.out.println(bankAtm);
        System.out.println(bankOffice);
        System.out.println(employee);
        System.out.println(user);
        System.out.println(user.getPaymentAccount());
        System.out.println(user.getCreditAccount());
    }
}
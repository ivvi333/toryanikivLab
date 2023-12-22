package tech.reliab.course.toryanikiv.bank;

import tech.reliab.course.toryanikiv.bank.dal.impl.*;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.*;
import tech.reliab.course.toryanikiv.bank.service.impl.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        BankDao bankDao = new BankDao();
        BankAtmDao bankAtmDao = new BankAtmDao();
        BankOfficeDao bankOfficeDao = new BankOfficeDao();
        EmployeeDao employeeDao = new EmployeeDao();
        UserDao userDao = new UserDao();
        PaymentAccountDao paymentAccountDao = new PaymentAccountDao();
        CreditAccountDao creditAccountDao = new CreditAccountDao();

        BankService bankService = new BankServiceImpl(bankDao);
        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankOfficeDao);
        PaymentAccountService paymentAccountService = new PaymentAccountServiceImpl(paymentAccountDao);
        CreditAccountService creditAccountService = new CreditAccountServiceImpl(creditAccountDao);

        final int bankCount = 5, officeCount = 3, personCount = 5, accountCount = 2;

        for (int bankID = 0; bankID < bankCount; bankID++) {
            Bank bank = new Bank(String.format("Test bank %d", bankID));
            bankDao.save(bank);

            for (int officeID = bankID * officeCount; officeID < bankID * officeCount + officeCount; officeID++) {
                BankOffice bankOffice = new BankOffice(String.format("Test bank office %d", officeID), String.format("Test st. %d", officeID), BigDecimal.valueOf(5000));
                bankOfficeDao.save(bankOffice);
                bankService.addOffice(bank, bankOfficeDao, bankOffice);

                Employee banker = new Employee(String.format("Banker %d", officeID),
                        LocalDate.now().minusYears(30).plusDays(officeID));
                employeeDao.save(banker);
                bankOfficeService.addEmployee(bankOffice, employeeDao, banker, Employee.EmployeeOccupation.BANKER);

                Employee teller = new Employee(String.format("Teller %d", officeID),
                        LocalDate.now().minusYears(30).plusMonths(1).plusDays(officeID));
                employeeDao.save(teller);
                bankOfficeService.addEmployee(bankOffice, employeeDao, teller, Employee.EmployeeOccupation.TELLER);

                Employee analyst = new Employee(String.format("Analyst %d", officeID),
                        LocalDate.now().minusYears(30).plusMonths(2).plusDays(officeID));
                employeeDao.save(analyst);
                bankOfficeService.addEmployee(bankOffice, employeeDao, analyst, Employee.EmployeeOccupation.ANALYST);

                Employee assistant = new Employee(String.format("Assistant %d", officeID),
                        LocalDate.now().minusYears(30).plusMonths(3).plusDays(officeID));
                employeeDao.save(assistant);
                bankOfficeService.addEmployee(bankOffice, employeeDao, assistant, Employee.EmployeeOccupation.ASSISTANT);

                Employee manager = new Employee(String.format("Manager %d", officeID),
                        LocalDate.now().minusYears(30).plusMonths(4).plusDays(officeID));
                employeeDao.save(manager);
                bankOfficeService.addEmployee(bankOffice, employeeDao, manager, Employee.EmployeeOccupation.MANAGER);

                BankAtm bankAtm = new BankAtm(String.format("Test bank ATM %d", officeID), BigDecimal.valueOf(1000));
                bankAtmDao.save(bankAtm);
                bankOfficeService.addAtm(bankOffice, bankAtmDao, bankAtm, teller);

                for (int personID = officeID * personCount; personID < officeID * personCount + personCount; personID++) {
                    User user = new User(String.format("User %d", personID),
                            LocalDate.now().minusYears(25).plusMonths(6).plusDays(personID), String.format("Job %d", personID));
                    userDao.save(user);

                    for (int accountID = personID * accountCount; accountID < personID * accountCount + accountCount; accountID++) {
                        UUID paymentAccountUUID = paymentAccountService.openPaymentAccount(userDao, user, bankDao, bank);
                        creditAccountService.openCreditAccount(userDao, user, bankDao, bank, assistant,
                                paymentAccountDao.getByUUID(paymentAccountUUID).get(),
                                LocalDate.now(), 4, BigDecimal.valueOf(1000));
                    }
                }
            }
        }

        bankDao.getAll().forEach(System.out::println);
        bankAtmDao.getAll().forEach(System.out::println);
        bankOfficeDao.getAll().forEach(System.out::println);
        employeeDao.getAll().forEach(System.out::println);
        userDao.getAll().forEach(System.out::println);
    }
}
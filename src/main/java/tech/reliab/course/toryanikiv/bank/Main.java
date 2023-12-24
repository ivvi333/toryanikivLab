package tech.reliab.course.toryanikiv.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import tech.reliab.course.toryanikiv.bank.dal.impl.*;
import tech.reliab.course.toryanikiv.bank.deserializer.*;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.*;
import tech.reliab.course.toryanikiv.bank.service.impl.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
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
        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankOfficeDao, bankDao);
        PaymentAccountService paymentAccountService = new PaymentAccountServiceImpl(paymentAccountDao, bankDao, userDao, bankOfficeDao, bankAtmDao);
        CreditAccountService creditAccountService = new CreditAccountServiceImpl(creditAccountDao, paymentAccountDao, bankDao, userDao);
        UserService userService = new UserServiceImpl(userDao);

        Bank bank = new Bank("Test bank");
        bankDao.save(bank);

        Bank newBank = new Bank("New bank");
        bankDao.save(newBank);

        BankOffice bankOffice = new BankOffice("Test office", "Test addr", BigDecimal.valueOf(1000));
        bankOfficeDao.save(bankOffice);
        bankService.addOffice(bank, bankOfficeDao, bankOffice, BigDecimal.valueOf(5000));

        UUID employeeUUID = UUID.randomUUID();
        Employee employee = new Employee("Test name", LocalDate.now().minusYears(30));
        employeeDao.save(employee);
        bankOfficeService.addEmployee(bankOffice, employeeDao, employee, Employee.EmployeeOccupation.ASSISTANT);

        UUID employee2UUID = UUID.randomUUID();
        Employee employee2 = new Employee("Test name2", LocalDate.now().minusYears(30));
        employeeDao.save(employee2);
        bankOfficeService.addEmployee(bankOffice, employeeDao, employee2, Employee.EmployeeOccupation.TELLER);

        UUID bankAtmUUID = UUID.randomUUID();
        BankAtm bankAtm = new BankAtm("Test atm", BigDecimal.valueOf(100));
        bankAtmDao.save(bankAtm);
        bankOfficeService.addAtm(bankOffice, bankAtmDao, bankAtm, employee2, BigDecimal.valueOf(1000));

        UUID userUUID = UUID.randomUUID();
        User user = new User("Test user name", LocalDate.now().minusYears(25), "Tester");
        userDao.save(user);

        UUID paymentAccountUUID = paymentAccountService.openPaymentAccount(user, bank);
        paymentAccountService.openPaymentAccount(user, bank);
        UUID creditAccountUUID = creditAccountService.openCreditAccount(user, bank, employee, paymentAccountDao.getByUUID(paymentAccountUUID).get(),
                LocalDate.now().minusDays(5), 1, BigDecimal.valueOf(1000));

//        System.out.println(user);

        try {
            userService.saveUserInfo(user, "/home/ivvi/Desktop");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            userService.changePaymentAccountBank("/home/ivvi/Desktop/User/PaymentAccounts",
                    paymentAccountDao, paymentAccountDao.getByUUID(paymentAccountUUID).get(), bankDao, newBank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(newBank);
    }
}
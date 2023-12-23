package tech.reliab.course.toryanikiv.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import tech.reliab.course.toryanikiv.bank.dal.impl.*;
import tech.reliab.course.toryanikiv.bank.deserializer.*;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.*;
import tech.reliab.course.toryanikiv.bank.service.impl.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
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

        Bank bank = new Bank("Test bank");
        bankDao.save(bank);

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

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addDeserializer(Bank.class, new BankDeserializer());
        simpleModule.addDeserializer(BankOffice.class, new BankOfficeDeserializer());
        simpleModule.addDeserializer(BankAtm.class, new BankAtmDeserializer());
        simpleModule.addDeserializer(Employee.class, new EmployeeDeserializer());
        simpleModule.addDeserializer(User.class, new UserDeserializer());
        simpleModule.addDeserializer(PaymentAccount.class, new PaymentAccountDeserializer());
        simpleModule.addDeserializer(CreditAccount.class, new CreditAccountDeserializer());

        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(simpleModule);

        var json = mapper.writeValueAsString(paymentAccountDao.getByUUID(paymentAccountUUID).get());
        System.out.println(json);

        paymentAccountDao.delete(paymentAccountDao.getByUUID(paymentAccountUUID).get());

        PaymentAccount oldPaymentAccount = mapper.readValue(json, PaymentAccount.class);

        paymentAccountDao.save(oldPaymentAccount);

        paymentAccountDao.getAll().forEach(System.out::println);
    }
}
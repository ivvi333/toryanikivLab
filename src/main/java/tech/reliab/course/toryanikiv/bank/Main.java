package tech.reliab.course.toryanikiv.bank;

import tech.reliab.course.toryanikiv.bank.dal.impl.*;
import tech.reliab.course.toryanikiv.bank.entity.*;
import tech.reliab.course.toryanikiv.bank.service.*;
import tech.reliab.course.toryanikiv.bank.service.impl.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
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

        final int bankCount = 5, officeCount = 3, personCount = 5, accountCount = 2;

        for (int bankID = 0; bankID < bankCount; bankID++) {
            Bank bank = new Bank(String.format("Test bank %d", bankID));
            bankDao.save(bank);

            for (int officeID = bankID * officeCount; officeID < bankID * officeCount + officeCount; officeID++) {
                BankOffice bankOffice = new BankOffice(String.format("Test bank office %d", officeID), String.format("Test st. %d", officeID), BigDecimal.valueOf(5000));
                bankOfficeDao.save(bankOffice);
                bankService.addOffice(bank, bankOfficeDao, bankOffice, BigDecimal.valueOf(2000));

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
                bankOfficeService.addAtm(bankOffice, bankAtmDao, bankAtm, teller, BigDecimal.valueOf(1000));

                for (int personID = officeID * personCount; personID < officeID * personCount + personCount; personID++) {
                    User user = new User(String.format("User %d", personID),
                            LocalDate.now().minusYears(25).plusMonths(6).plusDays(personID), String.format("Job %d", personID));
                    userDao.save(user);

                    for (int accountID = personID * accountCount; accountID < personID * accountCount + accountCount; accountID++) {
                        UUID paymentAccountUUID = paymentAccountService.openPaymentAccount(user, bank);
                        creditAccountService.openCreditAccount(user, bank, assistant,
                                paymentAccountDao.getByUUID(paymentAccountUUID).get(),
                                LocalDate.now(), 4, BigDecimal.valueOf(1000));
                    }
                }
            }
        }

        Scanner scanner = new Scanner(System.in);

        scannerLoop: while (true) {
            System.out.println("\nChoose entities to work with:");
            System.out.println("1 - banks");
            System.out.println("2 - bank offices");
            System.out.println("3 - bank ATMs");
            System.out.println("4 - employees");
            System.out.println("5 - users");
            System.out.println("6 - payment accounts");
            System.out.println("7 - credit accounts");
            System.out.println("0 - quit");

            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all banks");
                        System.out.println("2 - print bank by UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 2);

                    if (action == 1) {
                        bankDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (bankDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(bankDao.getByUUID(uuid).get());
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 2:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all bank offices");
                        System.out.println("2 - print bank office by UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 2);

                    if (action == 1) {
                        bankOfficeDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (bankOfficeDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(bankOfficeDao.getByUUID(uuid).get());
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 3:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all bank ATMs");
                        System.out.println("2 - print bank ATM by UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 2);

                    if (action == 1) {
                        bankAtmDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (bankAtmDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(bankAtmDao.getByUUID(uuid).get());
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 4:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all employees");
                        System.out.println("2 - print employee by UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 2);

                    if (action == 1) {
                        employeeDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (employeeDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(employeeDao.getByUUID(uuid).get());
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 5:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all users");
                        System.out.println("2 - print user by UUID");
                        System.out.println("3 - save user info and accounts to JSON by UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 3);

                    if (action == 1) {
                        userDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (userDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(userDao.getByUUID(uuid).get());
                        }
                    }
                    else if (action == 3) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (userDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println("\nEnter the full path to the target directory:");
                            String targetDirPath = scanner.nextLine();
                            userService.saveUserInfo(userDao.getByUUID(uuid).get(), targetDirPath);
                            System.out.println("\nUser info and accounts are successfully saved!");
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 6:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all payment accounts");
                        System.out.println("2 - print payment account by UUID");
                        System.out.println("3 - print all payment accounts by user UUID");
                        System.out.println("4 - change payment account bank by account UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 4);

                    if (action == 1) {
                        paymentAccountDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (paymentAccountDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(paymentAccountDao.getByUUID(uuid).get());
                        }
                    }
                    else if (action == 3) {
                        System.out.println("\nEnter the user UUID:");
                        UUID userUUID = UUID.fromString(scanner.nextLine());
                        if (userDao.getByUUID(userUUID).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            paymentAccountDao
                                    .getAll()
                                    .filter(paymentAccount -> userUUID.equals(paymentAccount.getUser().getUuid()))
                                    .forEach(System.out::println);
                        }
                    }
                    else if (action == 4) {
                        System.out.println("\nEnter the account UUID:");
                        UUID accountUUID = UUID.fromString(scanner.nextLine());
                        if (paymentAccountDao.getByUUID(accountUUID).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            String ynAction;
                            do {
                                System.out.println("Have you saved user's info and accounts to JSON? (Y/N):");
                                ynAction = scanner.nextLine();
                            } while (!ynAction.equalsIgnoreCase("Y") && !ynAction.equalsIgnoreCase("N"));

                            if (ynAction.equalsIgnoreCase("Y")) {
                                System.out.println("\nEnter the full path to the PaymentAccounts directory:");
                                String paymentAccountsDirPath = scanner.nextLine();

                                do {
                                    System.out.println("Have you changed the account's bank directly in JSON? (Y/N):");
                                    ynAction = scanner.nextLine();
                                } while (!ynAction.equalsIgnoreCase("Y") && !ynAction.equalsIgnoreCase("N"));

                                if (ynAction.equalsIgnoreCase("Y")) {
                                    userService.changePaymentAccountBank(paymentAccountsDirPath,
                                            paymentAccountDao, paymentAccountDao.getByUUID(accountUUID).get(), bankDao);
                                    System.out.println("Payment account bank is successfully changed!");
                                }
                                else {
                                    System.out.println("\nEnter the bank UUID:");
                                    UUID bankUUID = UUID.fromString(scanner.nextLine());
                                    if (bankDao.getByUUID(bankUUID).isEmpty()) {
                                        System.out.println("Not found!");
                                    }
                                    else {
                                        userService.changePaymentAccountBank(paymentAccountsDirPath,
                                                paymentAccountDao, paymentAccountDao.getByUUID(accountUUID).get(), bankDao, bankDao.getByUUID(bankUUID).get());
                                        System.out.println("Payment account bank is successfully changed!");
                                    }
                                }
                            }
                            else {
                                System.out.println("\nEnter the bank UUID:");
                                UUID bankUUID = UUID.fromString(scanner.nextLine());
                                if (bankDao.getByUUID(bankUUID).isEmpty()) {
                                    System.out.println("Not found!");
                                }
                                else {
                                    userService.changePaymentAccountBank("",
                                            paymentAccountDao, paymentAccountDao.getByUUID(accountUUID).get(), bankDao, bankDao.getByUUID(bankUUID).get());
                                    System.out.println("Payment account bank is successfully changed!");
                                }
                            }
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 7:
                    do {
                        System.out.println("\nChoose type of action:");
                        System.out.println("1 - print all credit accounts");
                        System.out.println("2 - print credit account by UUID");
                        System.out.println("3 - print all credit accounts by user UUID");
                        System.out.println("4 - change credit account bank by account UUID");
                        System.out.println("0 - quit");
                        action = scanner.nextInt();
                        scanner.nextLine();
                    } while (action < 0 || action > 4);

                    if (action == 1) {
                        creditAccountDao.getAll().forEach(System.out::println);
                    }
                    else if (action == 2) {
                        System.out.println("\nEnter the UUID:");
                        UUID uuid = UUID.fromString(scanner.nextLine());
                        if (creditAccountDao.getByUUID(uuid).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            System.out.println(creditAccountDao.getByUUID(uuid).get());
                        }
                    }
                    else if (action == 3) {
                        System.out.println("\nEnter the user UUID:");
                        UUID userUUID = UUID.fromString(scanner.nextLine());
                        if (userDao.getByUUID(userUUID).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            creditAccountDao
                                    .getAll()
                                    .filter(creditAccount -> userUUID.equals(creditAccount.getUser().getUuid()))
                                    .forEach(System.out::println);
                        }
                    }
                    else if (action == 4) {
                        System.out.println("\nEnter the account UUID:");
                        UUID accountUUID = UUID.fromString(scanner.nextLine());
                        if (creditAccountDao.getByUUID(accountUUID).isEmpty()) {
                            System.out.println("Not found!");
                        }
                        else {
                            String ynAction;
                            do {
                                System.out.println("Have you saved user's info and accounts to JSON? (Y/N):");
                                ynAction = scanner.nextLine();
                            } while (!ynAction.equalsIgnoreCase("Y") && !ynAction.equalsIgnoreCase("N"));

                            if (ynAction.equalsIgnoreCase("Y")) {
                                System.out.println("\nEnter the full path to the CreditAccounts directory:");
                                String creditAccountsDirPath = scanner.nextLine();

                                do {
                                    System.out.println("Have you changed the account's bank directly in JSON? (Y/N):");
                                    ynAction = scanner.nextLine();
                                } while (!ynAction.equalsIgnoreCase("Y") && !ynAction.equalsIgnoreCase("N"));

                                if (ynAction.equalsIgnoreCase("Y")) {
                                    userService.changeCreditAccountBank(creditAccountsDirPath,
                                            creditAccountDao, creditAccountDao.getByUUID(accountUUID).get(), bankDao);
                                    System.out.println("Credit account bank is successfully changed!");
                                }
                                else {
                                    System.out.println("\nEnter the bank UUID:");
                                    UUID bankUUID = UUID.fromString(scanner.nextLine());
                                    if (bankDao.getByUUID(bankUUID).isEmpty()) {
                                        System.out.println("Not found!");
                                    }
                                    else {
                                        userService.changeCreditAccountBank(creditAccountsDirPath,
                                                creditAccountDao, creditAccountDao.getByUUID(accountUUID).get(), bankDao, bankDao.getByUUID(bankUUID).get());
                                        System.out.println("Credit account bank is successfully changed!");
                                    }
                                }
                            }
                            else {
                                System.out.println("\nEnter the bank UUID:");
                                UUID bankUUID = UUID.fromString(scanner.nextLine());
                                if (bankDao.getByUUID(bankUUID).isEmpty()) {
                                    System.out.println("Not found!");
                                }
                                else {
                                    userService.changeCreditAccountBank("",
                                            creditAccountDao, creditAccountDao.getByUUID(accountUUID).get(), bankDao, bankDao.getByUUID(bankUUID).get());
                                    System.out.println("Credit account bank is successfully changed!");
                                }
                            }
                        }
                    }
                    else {
                        break scannerLoop;
                    }
                    break;
                case 0:
                    break scannerLoop;
                default:
                    System.out.println("Unknown action");
                    break;
            }
        }
    }
}
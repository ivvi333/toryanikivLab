package tech.reliab.course.toryanikiv.bank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.CreditAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.PaymentAccountDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.deserializer.CreditAccountDeserializer;
import tech.reliab.course.toryanikiv.bank.deserializer.PaymentAccountDeserializer;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.CreditAccount;
import tech.reliab.course.toryanikiv.bank.entity.PaymentAccount;
import tech.reliab.course.toryanikiv.bank.entity.User;
import tech.reliab.course.toryanikiv.bank.service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean changeJob(@NonNull User user, @NonNull String job) {
        user.setJob(job);
        user.setMonthlyIncome(ThreadLocalRandom.current().nextFloat(0, 10000.0f));
        user.setCreditScore(((int) user.getMonthlyIncome() / 1000) * 100.0f + 100.0f);

        userDao.update(user);

        return true;
    }

    @Override
    public void printPaymentAccounts(@NonNull User user) {
        user.getPaymentAccounts().forEach((uuid, paymentAccount) -> System.out.println(paymentAccount));
    }

    @Override
    public void printCreditAccounts(@NonNull User user) {
        user.getCreditAccounts().forEach((uuid, creditAccount) -> System.out.println(creditAccount));
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @Override
    public boolean saveUserInfo(@NonNull User user, @NonNull String targetDirPath) throws IOException {
        String userDirPath = targetDirPath + File.separator + "User";
        File userDir = new File(userDirPath);
        if (userDir.exists()) {
            if (!deleteDirectory(userDir)) {
                return false;
            }
        }
        if (!userDir.mkdirs()) {
            return false;
        }
        File paymentAccountDir = new File(userDirPath, "PaymentAccounts");
        paymentAccountDir.mkdir();
        File creditAccountDir = new File(userDirPath, "CreditAccounts");
        creditAccountDir.mkdir();

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JavaTimeModule());

        mapper.writeValue(new File(userDirPath, "User-" + user.getUuid().toString() + ".json"), user);
        user.getPaymentAccounts()
                .forEach((uuid, paymentAccount) -> {
                    try {
                        mapper.writeValue(
                            new File(paymentAccountDir, "PaymentAccount-" + paymentAccount.getUuid().toString() + ".json"),
                                paymentAccount);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        user.getCreditAccounts()
                .forEach((uuid, creditAccount) -> {
                    try {
                        mapper.writeValue(
                                new File(creditAccountDir, "CreditAccount-" + creditAccount.getUuid().toString() + ".json"),
                                creditAccount);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return true;
    }

    @Override
    public boolean changePaymentAccountBank(@NonNull String paymentAccountsDirPath,
                                            @NonNull PaymentAccountDao paymentAccountDao, @NonNull PaymentAccount paymentAccount,
                                            @NonNull BankDao bankDao)
            throws IOException
    {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(PaymentAccount.class, new PaymentAccountDeserializer(paymentAccountDao, bankDao));
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(simpleModule);

        PaymentAccount paymentAccountFromJSON = mapper.readValue(
                new File(paymentAccountsDirPath, "PaymentAccount-" + paymentAccount.getUuid().toString() + ".json"),
                PaymentAccount.class);

        if (paymentAccountFromJSON.getBank() != paymentAccount.getBank()) {
            paymentAccount.getBank().getPaymentAccounts().remove(paymentAccount);
            paymentAccountFromJSON.getBank().getPaymentAccounts().add(paymentAccountFromJSON);
            paymentAccountFromJSON.getUser().getBankNames().add(paymentAccountFromJSON.getBank().getName());

            paymentAccountDao.update(paymentAccountFromJSON);
            userDao.update(paymentAccountFromJSON.getUser());
            bankDao.update(paymentAccountFromJSON.getBank());
        }

        return true;
    }

    @Override
    public boolean changePaymentAccountBank(@NonNull String paymentAccountsDirPath,
                                            @NonNull PaymentAccountDao paymentAccountDao, @NonNull PaymentAccount paymentAccount,
                                            @NonNull BankDao bankDao, @NonNull Bank bank)
            throws IOException
    {
        paymentAccount.getBank().getPaymentAccounts().remove(paymentAccount);
        paymentAccount.setBank(bank);
        bank.getPaymentAccounts().add(paymentAccount);
        paymentAccount.getUser().getBankNames().add(bank.getName());

        paymentAccountDao.update(paymentAccount);
        userDao.update(paymentAccount.getUser());
        bankDao.update(bank);

        if (!paymentAccountsDirPath.isBlank()) {
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            try {
                mapper.writeValue(
                        new File(paymentAccountsDirPath, "PaymentAccount-" + paymentAccount.getUuid().toString() + ".json"),
                        paymentAccount);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    @Override
    public boolean changeCreditAccountBank(@NonNull String creditAccountsDirPath,
                                           @NonNull CreditAccountDao creditAccountDao, @NonNull CreditAccount creditAccount,
                                           @NonNull BankDao bankDao)
            throws IOException
    {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(CreditAccount.class, new CreditAccountDeserializer(creditAccountDao, bankDao));
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(simpleModule);

        CreditAccount creditAccountFromJSON = mapper.readValue(
                new File(creditAccountsDirPath, "CreditAccount-" + creditAccount.getUuid().toString() + ".json"),
                CreditAccount.class);

        if (creditAccountFromJSON.getBank() != creditAccountFromJSON.getBank()) {
            creditAccount.getBank().getPaymentAccounts().remove(creditAccount);
            creditAccountFromJSON.getBank().getCreditAccounts().add(creditAccountFromJSON);
            creditAccountFromJSON.getUser().getBankNames().add(creditAccountFromJSON.getBank().getName());

            creditAccountDao.update(creditAccountFromJSON);
            userDao.update(creditAccountFromJSON.getUser());
            bankDao.update(creditAccountFromJSON.getBank());
        }

        return true;
    }

    @Override
    public boolean changeCreditAccountBank(@NonNull String creditAccountsDirPath,
                                           @NonNull CreditAccountDao creditAccountDao, @NonNull CreditAccount creditAccount,
                                           @NonNull BankDao bankDao, @NonNull Bank bank)
            throws IOException
    {
        creditAccount.getBank().getCreditAccounts().remove(creditAccount);
        creditAccount.setBank(bank);
        bank.getCreditAccounts().add(creditAccount);
        creditAccount.getUser().getBankNames().add(bank.getName());

        creditAccountDao.update(creditAccount);
        userDao.update(creditAccount.getUser());
        bankDao.update(bank);

        if (!creditAccountsDirPath.isBlank()) {
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            try {
                mapper.writeValue(
                        new File(creditAccountsDirPath, "CreditAccount-" + creditAccount.getUuid().toString() + ".json"),
                        creditAccount);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}

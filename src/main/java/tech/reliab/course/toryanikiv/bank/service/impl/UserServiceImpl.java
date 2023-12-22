package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.dal.impl.UserDao;
import tech.reliab.course.toryanikiv.bank.entity.User;
import tech.reliab.course.toryanikiv.bank.service.UserService;

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
}

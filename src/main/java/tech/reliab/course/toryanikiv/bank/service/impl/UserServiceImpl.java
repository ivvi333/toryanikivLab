package tech.reliab.course.toryanikiv.bank.service.impl;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.User;
import tech.reliab.course.toryanikiv.bank.service.UserService;

import java.util.concurrent.ThreadLocalRandom;

public class UserServiceImpl implements UserService {
    @Override
    public boolean changeJob(@NonNull User user, @NonNull String job) {
        user.setJob(job);
        user.setMonthlyIncome(ThreadLocalRandom.current().nextFloat(0, 10000.0f));
        user.setCreditScore((user.getMonthlyIncome() % 1000.0f) * 100.0f + 100.0f);

        return true;
    }
}

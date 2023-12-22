package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.User;

public interface UserService {
    boolean changeJob(@NonNull User user, @NonNull String job);
}

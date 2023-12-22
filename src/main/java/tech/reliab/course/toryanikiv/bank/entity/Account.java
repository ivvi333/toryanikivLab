package tech.reliab.course.toryanikiv.bank.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Account {
    @Setter(AccessLevel.NONE) protected UUID uuid;
    protected User user;
    protected Bank bank;

    protected Account(@NonNull User user, @NonNull Bank bank) {
        this.uuid = UUID.randomUUID();
        this.user = user;
        this.bank = bank;
    }
}

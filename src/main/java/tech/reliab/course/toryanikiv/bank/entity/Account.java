package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uuid",
        scope = Account.class)
public class Account {
    @Setter(AccessLevel.NONE) protected UUID uuid;
    @JsonIdentityReference(alwaysAsId = true) protected User user;
    @JsonIdentityReference(alwaysAsId = true) protected Bank bank;

    protected Account(@NonNull User user, @NonNull Bank bank) {
        this.uuid = UUID.randomUUID();
        this.user = user;
        this.bank = bank;
    }
}

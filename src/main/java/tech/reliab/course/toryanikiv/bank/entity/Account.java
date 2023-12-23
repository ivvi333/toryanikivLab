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
    protected User user;
    protected Bank bank;

    @JsonCreator
    protected Account(@NonNull @JsonProperty("user") User user, @NonNull @JsonProperty("bank") Bank bank) {
        this.uuid = UUID.randomUUID();
        this.user = user;
        this.bank = bank;
    }
}

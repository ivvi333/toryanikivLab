package tech.reliab.course.toryanikiv.bank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Account {
    @Setter(AccessLevel.NONE) @JsonIgnore protected UUID uuid;
    @JsonBackReference(value = "account-user") protected User user;
    @JsonBackReference(value = "account-bank") protected Bank bank;

    @JsonCreator
    protected Account(@NonNull @JsonProperty("user") User user, @NonNull @JsonProperty("bank") Bank bank) {
        this.uuid = UUID.randomUUID();
        this.user = user;
        this.bank = bank;
    }
}

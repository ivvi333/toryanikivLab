package tech.reliab.course.toryanikiv.bank.dal;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Dao<T> {
    Stream<T> getAll();
    Optional<T> getByUUID(UUID uuid);
    UUID save(T t);
    void update(T t);
    void delete(T t);
}

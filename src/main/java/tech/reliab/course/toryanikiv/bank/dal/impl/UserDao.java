package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.User;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class UserDao implements Dao<User> {
    private final HashMap<UUID, User> userHashMap;

    public UserDao() {
        userHashMap = new HashMap<>();
    }

    @Override
    public Stream<User> getAll() {
        return userHashMap.values().stream();
    }

    @Override
    public Optional<User> getByUUID(UUID uuid) {
        return Optional.ofNullable(userHashMap.get(uuid));
    }

    @Override
    public UUID save(User user) {
        userHashMap.put(user.getUuid(), user);
        return user.getUuid();
    }

    @Override
    public void update(User user) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        userHashMap.put(user.getUuid(), user);
    }

    @Override
    public void delete(User user) {
        userHashMap.remove(user.getUuid());
    }
}

package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.CreditAccount;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class CreditAccountDao implements Dao<CreditAccount> {
    private final HashMap<UUID, CreditAccount> creditAccountHashMap;

    public CreditAccountDao() {
        creditAccountHashMap = new HashMap<>();
    }

    @Override
    public Stream<CreditAccount> getAll() {
        return creditAccountHashMap.values().stream();
    }

    @Override
    public Optional<CreditAccount> getByUUID(UUID uuid) {
        return Optional.ofNullable(creditAccountHashMap.get(uuid));
    }

    @Override
    public UUID save(CreditAccount creditAccount) {
        creditAccountHashMap.put(creditAccount.getUuid(), creditAccount);
        return creditAccount.getUuid();
    }

    @Override
    public void update(CreditAccount creditAccount) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        creditAccountHashMap.put(creditAccount.getUuid(), creditAccount);
    }

    @Override
    public void delete(CreditAccount creditAccount) {
        creditAccountHashMap.remove(creditAccount.getUuid());
    }
}

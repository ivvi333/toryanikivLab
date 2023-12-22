package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class BankDao implements Dao<Bank> {
    private final HashMap<UUID, Bank> bankHashMap;

    public BankDao() {
        bankHashMap = new HashMap<>();
    }

    @Override
    public Stream<Bank> getAll() {
        return bankHashMap.values().stream();
    }

    @Override
    public Optional<Bank> getByUUID(UUID uuid) {
        return Optional.ofNullable(bankHashMap.get(uuid));
    }

    @Override
    public UUID save(Bank bank) {
        bankHashMap.put(bank.getUuid(), bank);
        return bank.getUuid();
    }

    @Override
    public void update(Bank bank) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        bankHashMap.put(bank.getUuid(), bank);
    }

    @Override
    public void delete(Bank bank) {
        bankHashMap.remove(bank.getUuid());
    }
}

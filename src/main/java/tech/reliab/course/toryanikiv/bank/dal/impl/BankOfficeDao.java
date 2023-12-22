package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.BankOffice;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class BankOfficeDao implements Dao<BankOffice> {
    private final HashMap<UUID, BankOffice> bankOfficeHashMap;

    public BankOfficeDao() {
        bankOfficeHashMap = new HashMap<>();
    }

    @Override
    public Stream<BankOffice> getAll() {
        return bankOfficeHashMap.values().stream();
    }

    @Override
    public Optional<BankOffice> getByUUID(UUID uuid) {
        return Optional.ofNullable(bankOfficeHashMap.get(uuid));
    }

    @Override
    public UUID save(BankOffice bankOffice) {
        bankOfficeHashMap.put(bankOffice.getUuid(), bankOffice);
        return bankOffice.getUuid();
    }

    @Override
    public void update(BankOffice bankOffice) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        bankOfficeHashMap.put(bankOffice.getUuid(), bankOffice);
    }

    @Override
    public void delete(BankOffice bankOffice) {
        bankOfficeHashMap.remove(bankOffice.getUuid());
    }
}

package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class BankAtmDao implements Dao<BankAtm> {
    private final HashMap<UUID, BankAtm> bankAtmHashMap;

    public BankAtmDao() {
        bankAtmHashMap = new HashMap<>();
    }

    @Override
    public Stream<BankAtm> getAll() {
        return bankAtmHashMap.values().stream();
    }

    @Override
    public Optional<BankAtm> getByUUID(UUID uuid) {
        return Optional.ofNullable(bankAtmHashMap.get(uuid));
    }

    @Override
    public UUID save(BankAtm bankAtm) {
        bankAtmHashMap.put(bankAtm.getUuid(), bankAtm);
        return bankAtm.getUuid();
    }

    @Override
    public void update(BankAtm bankAtm) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        bankAtmHashMap.put(bankAtm.getUuid(), bankAtm);
    }

    @Override
    public void delete(BankAtm bankAtm) {
        bankAtmHashMap.remove(bankAtm.getUuid());
    }
}

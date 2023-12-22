package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.PaymentAccount;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class PaymentAccountDao implements Dao<PaymentAccount> {
    private final HashMap<UUID, PaymentAccount> paymentAccountHashMap;

    public PaymentAccountDao() {
        paymentAccountHashMap = new HashMap<>();
    }

    @Override
    public Stream<PaymentAccount> getAll() {
        return paymentAccountHashMap.values().stream();
    }

    @Override
    public Optional<PaymentAccount> getByUUID(UUID uuid) {
        return Optional.ofNullable(paymentAccountHashMap.get(uuid));
    }

    @Override
    public UUID save(PaymentAccount paymentAccount) {
        paymentAccountHashMap.put(paymentAccount.getUuid(), paymentAccount);
        return paymentAccount.getUuid();
    }

    @Override
    public void update(PaymentAccount paymentAccount) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        paymentAccountHashMap.put(paymentAccount.getUuid(), paymentAccount);
    }

    @Override
    public void delete(PaymentAccount paymentAccount) {
        paymentAccountHashMap.remove(paymentAccount.getUuid());
    }
}

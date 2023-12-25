package tech.reliab.course.toryanikiv.bank.service.utils;

import tech.reliab.course.toryanikiv.bank.entity.Bank;

import java.util.Comparator;

public interface BankComparator extends Comparator<Bank> {
    @Override
    int compare(Bank bank1, Bank bank2);
}

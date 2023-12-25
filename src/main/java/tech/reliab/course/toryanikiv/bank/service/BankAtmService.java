package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;
import tech.reliab.course.toryanikiv.bank.exceptions.BankAtmStatusException;

public interface BankAtmService {
    void closeForMaintenance(@NonNull BankAtm bankAtm) throws BankAtmStatusException;
    void openAfterMaintenance(@NonNull BankAtm bankAtm) throws BankAtmStatusException;
}

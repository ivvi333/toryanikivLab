package tech.reliab.course.toryanikiv.bank.service;

import lombok.NonNull;
import tech.reliab.course.toryanikiv.bank.entity.BankAtm;

public interface BankAtmService {
    boolean closeForMaintenance(@NonNull BankAtm bankAtm);
    boolean openAfterMaintenance(@NonNull BankAtm bankAtm);
}

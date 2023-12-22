package tech.reliab.course.toryanikiv.bank.dal.impl;

import tech.reliab.course.toryanikiv.bank.dal.Dao;
import tech.reliab.course.toryanikiv.bank.entity.Employee;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class EmployeeDao implements Dao<Employee> {
    private final HashMap<UUID, Employee> employeeHashMap;

    public EmployeeDao() {
        employeeHashMap = new HashMap<>();
    }

    @Override
    public Stream<Employee> getAll() {
        return employeeHashMap.values().stream();
    }

    @Override
    public Optional<Employee> getByUUID(UUID uuid) {
        return Optional.ofNullable(employeeHashMap.get(uuid));
    }

    @Override
    public UUID save(Employee employee) {
        employeeHashMap.put(employee.getUuid(), employee);
        return employee.getUuid();
    }

    @Override
    public void update(Employee employee) {
        // Т.к. HashMap сможет перезаписать элемент с таким же ключом
        employeeHashMap.put(employee.getUuid(), employee);
    }

    @Override
    public void delete(Employee employee) {
        employeeHashMap.remove(employee.getUuid());
    }
}

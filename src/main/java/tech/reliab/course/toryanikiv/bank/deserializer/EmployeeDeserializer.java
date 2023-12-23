package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDeserializer extends JsonDeserializer<Employee> {
    @Override
    public Employee deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            String fullName = node.get("fullName").asText();
            LocalDate dateOfBirth = mapper.treeToValue(node.get("dateOfBirth"), LocalDate.class);

            Employee employee = new Employee(fullName, dateOfBirth);

            Employee.EmployeeOccupation occupation = Employee.EmployeeOccupation.valueOf(node.get("occupation").asText());
            boolean isWorkingRemotely = node.get("isWorkingRemotely").booleanValue();
            BankOffice bankOffice = mapper.treeToValue(node.get("bankOffice"), BankOffice.class);
            boolean canIssueCredit = node.get("canIssueCredit").booleanValue();
            BigDecimal salary = new BigDecimal(node.get("salary").asText());

            employee.setOccupation(occupation);
            employee.setWorkingRemotely(isWorkingRemotely);
            employee.setBankOffice(bankOffice);
            employee.setCanIssueCredit(canIssueCredit);
            employee.setSalary(salary);

            return employee;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid Employee.");
        }
    }
}

package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankOfficeDeserializer extends JsonDeserializer<BankOffice> {
    @Override
    public BankOffice deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            String name = node.get("name").asText();
            String address = node.get("address").asText();
            BigDecimal leaseCost = new BigDecimal(node.get("leaseCost").asText());

            BankOffice bankOffice = new BankOffice(name, address, leaseCost);

            Bank bank = mapper.treeToValue(node.get("bank"), Bank.class);
            boolean isOpen = node.get("isOpen").booleanValue();
            boolean isAtmPlaceable = node.get("isAtmPlaceable").booleanValue();
            ArrayList<BankAtm> bankAtms = mapper.readValue(node.get("bankAtms").toString(), new TypeReference<ArrayList<BankAtm>>() {});
            ArrayList<Employee> employees = mapper.readValue(node.get("employees").toString(), new TypeReference<ArrayList<Employee>>() {});
            boolean isCreditAvailable = node.get("isCreditAvailable").booleanValue();
            boolean isWithdrawAvailable = node.get("isWithdrawAvailable").booleanValue();
            boolean isDepositAvailable = node.get("isDepositAvailable").booleanValue();
            BigDecimal totalMoney = new BigDecimal(node.get("totalMoney").asText());

            bankOffice.setBank(bank);
            bankOffice.setOpen(isOpen);
            bankOffice.setAtmPlaceable(isAtmPlaceable);
            bankOffice.setBankAtms(bankAtms);
            bankOffice.setEmployees(employees);
            bankOffice.setCreditAvailable(isCreditAvailable);
            bankOffice.setWithdrawAvailable(isWithdrawAvailable);
            bankOffice.setDepositAvailable(isDepositAvailable);
            bankOffice.setTotalMoney(totalMoney);

            return bankOffice;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid BankOffice.");
        }
    }
}

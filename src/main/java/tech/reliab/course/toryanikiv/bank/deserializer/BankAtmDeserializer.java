package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;
import java.math.BigDecimal;

public class BankAtmDeserializer extends JsonDeserializer<BankAtm> {
    @Override
    public BankAtm deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            String name = node.get("name").asText();
            BigDecimal maintenanceCost = new BigDecimal(node.get("maintenanceCost").asText());

            BankAtm bankAtm = new BankAtm(name, maintenanceCost);

            String address = node.get("address").asText();
            BankAtm.BankAtmStatus status = BankAtm.BankAtmStatus.valueOf(node.get("status").asText());
            BankOffice bankOffice = mapper.treeToValue(node.get("bankOffice"), BankOffice.class);
            Employee operator = mapper.treeToValue(node.get("operator"), Employee.class);
            boolean isWithdrawAvailable = node.get("isWithdrawAvailable").booleanValue();
            boolean isDepositAvailable = node.get("isDepositAvailable").booleanValue();
            BigDecimal totalMoney = new BigDecimal(node.get("totalMoney").asText());

            bankAtm.setAddress(address);
            bankAtm.setStatus(status);
            bankAtm.setBankOffice(bankOffice);
            bankAtm.setOperator(operator);
            bankAtm.setWithdrawAvailable(isWithdrawAvailable);
            bankAtm.setDepositAvailable(isDepositAvailable);
            bankAtm.setTotalMoney(totalMoney);

            return bankAtm;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid BankAtm.");
        }
    }
}

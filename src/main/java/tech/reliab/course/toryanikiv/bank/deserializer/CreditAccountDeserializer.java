package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CreditAccountDeserializer extends JsonDeserializer<CreditAccount> {
    @Override
    public CreditAccount deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            User user = mapper.treeToValue(node.get("user"), User.class);
            Bank bank = mapper.treeToValue(node.get("bank"), Bank.class);

            LocalDate creditOpeningDate = mapper.treeToValue(node.get("creditOpeningDate"), LocalDate.class);
            int creditDurationInMonths = node.get("creditDurationInMonths").asInt();
            BigDecimal creditAmount = new BigDecimal(node.get("creditAmount").asText());
            Employee creditAssistant = mapper.treeToValue(node.get("creditAssistant"), Employee.class);
            PaymentAccount paymentAccount = mapper.treeToValue(node.get("paymentAccount"), PaymentAccount.class);

            return new CreditAccount(user, bank, creditAssistant, paymentAccount, creditOpeningDate, creditDurationInMonths, creditAmount);
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid CreditAccount.");
        }
    }
}

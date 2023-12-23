package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankDeserializer extends JsonDeserializer<Bank> {
    @Override
    public Bank deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            String name = node.get("name").asText();

            Bank bank = new Bank(name);

            ArrayList<BankOffice> bankOffices = mapper.readValue(node.get("bankOffices").toString(), new TypeReference<ArrayList<BankOffice>>() {});
            ArrayList<PaymentAccount> paymentAccounts = mapper.readValue(node.get("paymentAccounts").toString(), new TypeReference<ArrayList<PaymentAccount>>() {});
            ArrayList<CreditAccount> creditAccounts = mapper.readValue(node.get("creditAccounts").toString(), new TypeReference<ArrayList<CreditAccount>>() {});
            int rating = node.get("rating").asInt();
            BigDecimal totalMoney = new BigDecimal(node.get("totalMoney").asText());
            float interestRate = node.get("interestRate").floatValue();

            bank.setBankOffices(bankOffices);
            bank.setPaymentAccounts(paymentAccounts);
            bank.setCreditAccounts(creditAccounts);
            bank.setRating(rating);
            bank.setTotalMoney(totalMoney);
            bank.setInterestRate(interestRate);

            return bank;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid Bank.");
        }
    }
}

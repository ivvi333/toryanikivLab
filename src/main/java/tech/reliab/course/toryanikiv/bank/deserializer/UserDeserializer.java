package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class UserDeserializer extends JsonDeserializer<User> {
    @Override
    public User deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            String fullName = node.get("fullName").asText();
            LocalDate dateOfBirth = mapper.treeToValue(node.get("dateOfBirth"), LocalDate.class);
            String job = node.get("job").asText();

            User user = new User(fullName, dateOfBirth, job);

            float monthlyIncome = node.get("monthlyIncome").floatValue();
            HashSet<String> bankNames = new HashSet<>(Arrays.asList(node.get("bankNames").asText().split(",")));
            HashMap<UUID, CreditAccount> creditAccounts = mapper.treeToValue(node.get("creditAccounts"), new TypeReference<HashMap<UUID, CreditAccount>>(){});
            HashMap<UUID, PaymentAccount> paymentAccounts = mapper.treeToValue(node.get("paymentAccounts"), new TypeReference<HashMap<UUID, PaymentAccount>>(){});
            float creditScore = node.get("creditScore").floatValue();

            user.setMonthlyIncome(monthlyIncome);
            user.setBankNames(bankNames);
            user.setCreditAccounts(creditAccounts);
            user.setPaymentAccounts(paymentAccounts);
            user.setCreditScore(creditScore);

            return user;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid User.");
        }
    }
}

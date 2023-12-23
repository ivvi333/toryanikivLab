package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.entity.*;

import java.io.IOException;

public class PaymentAccountDeserializer extends JsonDeserializer<PaymentAccount> {
    @Override
    public PaymentAccount deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            User user = mapper.treeToValue(node.get("user"), User.class);
            Bank bank = mapper.treeToValue(node.get("bank"), Bank.class);

            return new PaymentAccount(user, bank);
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid CreditAccount.");
        }
    }
}

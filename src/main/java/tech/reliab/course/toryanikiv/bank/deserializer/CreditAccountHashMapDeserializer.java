package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.reliab.course.toryanikiv.bank.entity.CreditAccount;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CreditAccountHashMapDeserializer extends JsonDeserializer<HashMap<UUID, CreditAccount>> {
    @Override
    public HashMap<UUID, CreditAccount> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            HashMap<UUID, CreditAccount> result = new HashMap<UUID, CreditAccount>();
            while (jp.nextToken() == JsonToken.FIELD_NAME) {
                String fieldName = jp.getCurrentName();
                JsonNode node = mapper.readTree(jp);
                UUID key = UUID.fromString(fieldName);
                CreditAccount value = mapper.treeToValue(node, CreditAccount.class);
                result.put(key, value);
            }

            return result;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid HashMap<UUID, CreditAccount>.");
        }
    }
}

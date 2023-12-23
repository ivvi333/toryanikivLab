package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.reliab.course.toryanikiv.bank.entity.PaymentAccount;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PaymentAccountHashMapDeserializer extends JsonDeserializer<HashMap<UUID, PaymentAccount>> {
    @Override
    public HashMap<UUID, PaymentAccount> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            return mapper.readValue(jp, new TypeReference<HashMap<UUID, PaymentAccount>>() {
            });
        }
        else {
            mapper.readTree(jp);
            return new HashMap<UUID, PaymentAccount>();
        }
    }
}

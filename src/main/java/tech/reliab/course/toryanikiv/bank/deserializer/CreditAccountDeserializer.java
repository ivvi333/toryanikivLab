package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.CreditAccountDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.CreditAccount;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class CreditAccountDeserializer extends JsonDeserializer<CreditAccount> {
    private final CreditAccountDao creditAccountDao;
    private final BankDao bankDao;

    public CreditAccountDeserializer(CreditAccountDao creditAccountDao, BankDao bankDao) {
        this.creditAccountDao = creditAccountDao;
        this.bankDao = bankDao;
    }

    @Override
    public CreditAccount deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            Optional<CreditAccount> creditAccountOrNull = creditAccountDao.getByUUID(UUID.fromString(node.get("uuid").asText()));
            if (creditAccountOrNull.isEmpty()) {
                throw new IllegalArgumentException("Input JSON does not contain existing CreditAccount.");
            }
            CreditAccount creditAccount = creditAccountOrNull.get();

            Optional<Bank> bankOptional = bankDao.getByUUID(UUID.fromString(node.get("bank").asText()));
            if (bankOptional.isEmpty()) {
                throw new IllegalArgumentException("Input JSON does not contain existing Bank.");
            }
            Bank bank = bankOptional.get();

            creditAccount.setBank(bank);

            return creditAccount;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid CreditAccount.");
        }
    }
}

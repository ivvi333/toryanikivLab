package tech.reliab.course.toryanikiv.bank.deserializer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import tech.reliab.course.toryanikiv.bank.dal.impl.BankDao;
import tech.reliab.course.toryanikiv.bank.dal.impl.PaymentAccountDao;
import tech.reliab.course.toryanikiv.bank.entity.Bank;
import tech.reliab.course.toryanikiv.bank.entity.PaymentAccount;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class PaymentAccountDeserializer extends JsonDeserializer<PaymentAccount> {
    private final PaymentAccountDao paymentAccountDao;
    private final BankDao bankDao;

    public PaymentAccountDeserializer(PaymentAccountDao paymentAccountDao, BankDao bankDao) {
        this.paymentAccountDao = paymentAccountDao;
        this.bankDao = bankDao;
    }

    @Override
    public PaymentAccount deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            JsonNode node = mapper.readTree(jp);

            Optional<PaymentAccount> paymentAccountOptional = paymentAccountDao.getByUUID(UUID.fromString(node.get("uuid").asText()));
            if (paymentAccountOptional.isEmpty()) {
                throw new IllegalArgumentException("Input JSON does not contain existing PaymentAccount.");
            }
            PaymentAccount paymentAccount = paymentAccountOptional.get();

            Optional<Bank> bankOptional = bankDao.getByUUID(UUID.fromString(node.get("bank").asText()));
            if (bankOptional.isEmpty()) {
                throw new IllegalArgumentException("Input JSON does not contain existing Bank.");
            }
            Bank bank = bankOptional.get();

            paymentAccount.setBank(bank);

            return paymentAccount;
        }
        else {
            throw new IllegalArgumentException("Input JSON is not a valid PaymentAccount.");
        }
    }
}

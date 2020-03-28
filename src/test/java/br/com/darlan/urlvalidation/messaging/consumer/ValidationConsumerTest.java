package br.com.darlan.urlvalidation.messaging.consumer;

import br.com.darlan.urlvalidation.messaging.message.ValidationInMessage;
import br.com.darlan.urlvalidation.messaging.message.ValidationOutMessage;
import br.com.darlan.urlvalidation.messaging.producer.ValidationProducer;
import br.com.darlan.urlvalidation.model.Whitelist;
import br.com.darlan.urlvalidation.repository.WhitelistRepository;
import br.com.darlan.urlvalidation.service.ValidationService;
import br.com.darlan.urlvalidation.service.impl.ValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValidationConsumerTest {

    @Mock
    private WhitelistRepository whitelistRepository;
    @Mock
    private ValidationProducer validationProducer;
    private ValidationConsumer validationConsumer;

    @Before
    public void setUp() {
        final ValidationService validationService = new ValidationServiceImpl(whitelistRepository);
        this.validationConsumer = new ValidationConsumer(validationProducer, validationService);

        final Whitelist whitelistG = Whitelist.builder()
                .regex("http://.*")
            .build();

        final Whitelist whitelistP = Whitelist.builder()
                .regex("http://darlan\\.com")
            .build();

        when(whitelistRepository.findByType(Whitelist.Type.GLOBAL))
                .thenReturn(Collections.singletonList(whitelistG));
        when(whitelistRepository.findByClientAndType("client1", Whitelist.Type.PRIVATE))
                .thenReturn(Collections.singletonList(whitelistP));
    }

    @Test
    public void givenNullClientAndValidUrlShouldSendMatchAndGlobal() {
        final ValidationInMessage inMessage = ValidationInMessage.builder()
                .url("http://google.com")
                .correlationId(123)
            .build();
        validationConsumer.consume(inMessage);
        final ValidationOutMessage outMessage = ValidationOutMessage.builder()
                .match(true)
                .regex("http://.*")
                .correlationId(123)
            .build();
        verify(validationProducer).send(outMessage);
    }

    @Test
    public void givenNullClientAndInvalidUrlShouldSendNotMatch(){
        final ValidationInMessage inMessage = ValidationInMessage.builder()
                .url("ayreon")
                .correlationId(123)
            .build();
        validationConsumer.consume(inMessage);
        final ValidationOutMessage outMessage = ValidationOutMessage.builder()
                .match(false)
                .correlationId(123)
            .build();
        verify(validationProducer).send(outMessage);
    }

    @Test
    public void givenAClientAndValidUrlShouldSendMatchGlobal(){
        final ValidationInMessage inMessage = ValidationInMessage.builder()
                .url("http://arroz.com")
                .correlationId(123)
            .build();
        validationConsumer.consume(inMessage);
        final ValidationOutMessage outMessage = ValidationOutMessage.builder()
                .match(true)
                .regex("http://.*")
                .correlationId(123)
            .build();
        verify(validationProducer).send(outMessage);
    }

    @Test
    public void givenAClientAndValidUrlShouldSendMatchPrivate(){
        final ValidationInMessage inMessage = ValidationInMessage.builder()
                .client("client1")
                .url("http://darlan.com")
                .correlationId(123)
            .build();
        validationConsumer.consume(inMessage);
        final ValidationOutMessage outMessage = ValidationOutMessage.builder()
                .match(true)
                .regex("http://darlan\\.com")
                .correlationId(123)
            .build();
        verify(validationProducer).send(outMessage);
    }

    @Test
    public void givenAClientAndInvalidUrlShouldSendNotMatch(){
        final ValidationInMessage inMessage = ValidationInMessage.builder()
                .client("client1")
                .url("dreams")
                .correlationId(123)
            .build();
        validationConsumer.consume(inMessage);
        final ValidationOutMessage outMessage = ValidationOutMessage.builder()
                .match(false)
                .correlationId(123)
            .build();
        verify(validationProducer).send(outMessage);
    }
}
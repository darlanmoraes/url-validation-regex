package br.com.darlan.urlvalidation.messaging.consumer;

import br.com.darlan.urlvalidation.model.UrlValidation;
import br.com.darlan.urlvalidation.messaging.message.ValidationInMessage;
import br.com.darlan.urlvalidation.messaging.message.ValidationOutMessage;
import br.com.darlan.urlvalidation.messaging.producer.ValidationProducer;
import br.com.darlan.urlvalidation.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Slf4j
@Component
public class ValidationConsumer {

    private final ValidationProducer validationProducer;
    private final ValidationService validationService;

    @Autowired
    public ValidationConsumer(final ValidationProducer validationProducer,
                              final ValidationService validationService) {
        this.validationProducer = validationProducer;
        this.validationService = validationService;
    }

    @RabbitListener(queues = "${validation.queue}")
    public void consume(@Valid @Payload final ValidationInMessage messageIn) {
        log.info("Received messageIn from queue VALIDATION_QUEUE. {}", messageIn);
        final UrlValidation validation = this.validationService.validateUrl(messageIn.getClient(),
                messageIn.getUrl(), messageIn.getCorrelationId());
        final ValidationOutMessage messageOut = ValidationOutMessage.from(validation);
        this.validationProducer.send(messageOut);
        log.info("Sent message to exchange RESPONSE_EXCHANGE. {}", messageOut);
    }
}

package br.com.darlan.urlvalidation.messaging.consumer;

import br.com.darlan.urlvalidation.messaging.message.InsertionInMessage;
import br.com.darlan.urlvalidation.service.WhitelistService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Log4j2
@Component
public class InsertionConsumer {

    private final WhitelistService whitelistService;

    @Autowired
    public InsertionConsumer(final WhitelistService whitelistService) {
        this.whitelistService = whitelistService;
    }

    @Async
    @RabbitListener(queues = "${insertion.queue}")
    public void consume(@Valid @Payload final InsertionInMessage messageIn) {
        log.info("Received message from queue INSERTION_QUEUE. {}", messageIn);
        this.whitelistService.saveWhitelist(messageIn.getClient(), messageIn.getRegex());
    }
}

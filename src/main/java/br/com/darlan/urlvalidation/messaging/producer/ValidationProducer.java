package br.com.darlan.urlvalidation.messaging.producer;

import br.com.darlan.urlvalidation.messaging.message.ValidationOutMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ValidationProducer {

    @Value("${response.exchange}")
    private String responseExchange;

    @Value("${response.routing.key}")
    private String responseRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ValidationProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(final ValidationOutMessage message){
        this.rabbitTemplate.convertAndSend(responseExchange, responseRoutingKey, message);
    }
}

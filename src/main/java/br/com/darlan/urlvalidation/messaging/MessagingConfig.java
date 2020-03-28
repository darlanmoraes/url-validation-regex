package br.com.darlan.urlvalidation.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.validation.SmartValidator;

@Configuration
public class MessagingConfig implements RabbitListenerConfigurer {

    @Autowired
    private SmartValidator validator;

    @Value("${insertion.queue}")
    private String insertionQueue;

    @Value("${validation.queue}")
    private String validationQueue;

    @Value("${dead-letter.queue}")
    private String deadLetterQueue;

    @Value("${response.exchange}")
    private String responseExchange;

    @Value("${response.routing.key}")
    private String responseRoutingKey;

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        final DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setValidator(this.validator);
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue insertionQueue(){
        return QueueBuilder.durable(insertionQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", deadLetterQueue)
            .build();
    }

    @Bean
    public Queue validationQueue(){
        return QueueBuilder.durable(validationQueue)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", deadLetterQueue)
            .build();
    }

    @Bean
    public Queue deadLetterQueue(){
        return QueueBuilder.durable(deadLetterQueue).build();
    }

    @Bean
    public TopicExchange responseExchange(){
        return new TopicExchange(responseExchange);
    }

    @Bean
    public Binding createBinding(final TopicExchange topicExchange){
        return BindingBuilder.bind(topicExchange).to(topicExchange).with(responseRoutingKey);
    }

}

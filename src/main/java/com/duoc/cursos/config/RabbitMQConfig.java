package com.duoc.cursos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INSCRIPCION_QUEUE = "inscripciones.queue";
    public static final String INSCRIPCION_ROUTING_KEY = "inscripciones.routingkey";

    public static final String INSCRIPCION_ERROR_QUEUE = "inscripciones.error.queue";
    public static final String INSCRIPCION_ERROR_ROUTING_KEY = "inscripciones.error.routingkey";

    public static final String INSCRIPCION_EXCHANGE = "inscripciones.exchange";

    @Bean
    Queue inscripcionQueue() {
        return new Queue(INSCRIPCION_QUEUE, true, false, false, null);
    }

    @Bean
    Queue inscripcionErrorQueue() {
        return new Queue(INSCRIPCION_ERROR_QUEUE, true, false, false, null);
    }

    @Bean
    DirectExchange inscripcionExchange() {
        return new DirectExchange(INSCRIPCION_EXCHANGE);
    }

    @Bean
    Binding inscripcionBinding(Queue inscripcionQueue, DirectExchange inscripcionExchange) {
        return BindingBuilder.bind(inscripcionQueue)
                .to(inscripcionExchange)
                .with(INSCRIPCION_ROUTING_KEY);
    }

    @Bean
    Binding inscripcionErrorBinding(Queue inscripcionErrorQueue, DirectExchange inscripcionExchange) {
        return BindingBuilder.bind(inscripcionErrorQueue)
                .to(inscripcionExchange)
                .with(INSCRIPCION_ERROR_ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}

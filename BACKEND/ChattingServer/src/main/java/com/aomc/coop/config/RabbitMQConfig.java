package com.aomc.coop.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@EnableRabbit
public class RabbitMQConfig { //monitoring -> http://localhost:15672

    public static final String QUEUE_NAME = "queue";

    public static final String RECEIVE_QUEUE_NAME = "receieve_queue";

    private static final String EXCHANGE = RECEIVE_QUEUE_NAME + "-exchange";


    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(RECEIVE_QUEUE_NAME); //default
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(RECEIVE_QUEUE_NAME); //default
//        container.setMessageListener(listenerAdapter);
//        container.setMessageConverter(jsonMessageConverter());
        return container;
    }

    @Bean
    public Queue queue() {
        return new Queue(RECEIVE_QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RECEIVE_QUEUE_NAME);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ??
//    @Bean
//    public BaseMesage baseMesage() {
//        return new BaseMesage();
//    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
//        factory.setUsername("rabbitmq");
//        factory.setPassword("rabbitmq");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }


}

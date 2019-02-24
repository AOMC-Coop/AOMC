package com.aomc.coop.config;



import org.springframework.amqp.core.*;
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

    public static final String CHANNEL_Direct_QUEUE_NAME = "channel_direct_queue";

    public static final String CHANNEL_Topic_QUEUE_NAME = "channel_topic_queue";

//    public static final String User_Direct_QUEUE_NAME = "user_direct_queue";
//
//    public static final String User_Topic_QUEUE_NAME = "user_topic_queue";

    public static final String EXCHANGE = QUEUE_NAME + "-exchange";

    public static final String MessageRoutingKey = "charge.message.route";
    public static final String ChannelRoutingKey = "charge.channel.route";
//    public static final String UserRoutingKey = "charge.user.route";



    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        template.setRoutingKey(QUEUE_NAME);
//        template.setRoutingKey(CHANNEL_Direct_QUEUE_NAME);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames(QUEUE_NAME);
//        container.setQueueNames(CHANNEL_Direct_QUEUE_NAME);
//        container.setMessageListener(listenerAdapter);
//        container.setMessageConverter(jsonMessageConverter());
        return container;
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue channel_queue() {
        return new Queue(CHANNEL_Direct_QUEUE_NAME, true);
    }

//    @Bean
//    public Queue user_queue() {
//        return new Queue(User_Direct_QUEUE_NAME, true);
//    }


    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }


    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MessageRoutingKey);
    }

    @Bean
    public Binding channel_binding(Queue channel_queue, DirectExchange exchange) {
        return BindingBuilder.bind(channel_queue).to(exchange).with(ChannelRoutingKey);
    }

//    @Bean
//    public Binding user_binding(Queue user_queue, DirectExchange exchange) {
//        return BindingBuilder.bind(user_queue).to(exchange).with(UserRoutingKey);
//    }


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

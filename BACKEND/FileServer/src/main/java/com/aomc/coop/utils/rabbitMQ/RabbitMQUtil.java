package com.aomc.coop.utils.rabbitMQ;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.dto.MessageRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQUtil {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQUtil(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    public MessageRequest sendRabbitMQ(MessageRequest messageRequest) {
        System.out.println("요청이 왔습니다" + messageRequest);
//        System.out.println("channelIdx는 " + channelIdx);

        Map<String, MessageRequest> map = new HashMap<>();
        map.put("msg", messageRequest);
//        map.put("channelIdx", ms);

        //큐에보냄
        if(messageRequest != null) {
            System.out.println("rabbitMQ send");
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, map);
        }

        return messageRequest;
    }

//    public void receiveRabbitMQ(Message message) {
//        System.out.println("rabbitMQ receive = " + message);
//        //SendTo("/topic/message")
//        this.simpMessagingTemplate.convertAndSend("/topic/message", message);
//    }
}

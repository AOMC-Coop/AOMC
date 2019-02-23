package com.aomc.coop.utils.rabbitMQ;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.ChannelInvite;
import com.aomc.coop.model.Message;
import com.aomc.coop.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQUtil.class);

    public Message sendRabbitMQ(Message message) {
        System.out.println("요청이 왔습니다" + message);

        Map<String, Message> map = new HashMap<>();
        map.put("msg", message);

        if(message != null) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.QUEUE_NAME, map);
            logger.debug("rabbitMQ send");
        }

        return message;
    }

    public ChannelInvite channel_sendRabbitMQ(ChannelInvite channelInvite) {
        System.out.println("요청이 왔습니다" + channelInvite);

        if(channelInvite != null) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CHANNEL_QUEUE_NAME, channelInvite);
            logger.info("rabbitMQ send" + RabbitMQConfig.CHANNEL_QUEUE_NAME);
        }

        return channelInvite;
    }

    public void receiveRabbitMQ(Message message) {
        logger.info("rabbitMQ receive = " + message);
        this.simpMessagingTemplate.convertAndSend("/topic/message/" + message.getChannel_idx(), message);
    }

    public void channel_receiveRabbitMQ(ChannelInvite channelInvite) {
        logger.info("rabbitMQ receive = " + channelInvite);
        //list만큼 돌기
        for(int i=0; i<channelInvite.getToInvite().size(); i++) {
            this.simpMessagingTemplate.convertAndSend("/topic/chatInvite/" + channelInvite.getToInvite().get(i).getIdx(), channelInvite);
            logger.info("sendTo => " + channelInvite.getToInvite().get(i).getIdx());
        }
    }
}

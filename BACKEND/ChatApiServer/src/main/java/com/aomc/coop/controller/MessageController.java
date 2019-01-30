package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

//방 아이디에 따라 메세지 나누기
@Slf4j
@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @MessageMapping("/chat/{channelIdx}")
    @SendTo("/topic/message")
    public Message broadcasting(@DestinationVariable(value = "channelIdx") int channelIdx, Message msg) throws Exception{
        System.out.println("요청이 왔습니다" + msg);
//        System.out.println("channelIdx는 " + channelIdx);

        Map<String, Message> map = new HashMap<>();
        map.put("msg", msg);
//        map.put("channelIdx", ms);

        //큐에보냄
        if(msg != null) {
            System.out.println("rabbitMQ send");
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, map);
        }

        return  msg;
    }
}


//<html>
//<head>
//<title>Chat WebSocket</title>
//<script src="./js/sockjs-0.3.4.js"></script>
//<script src="./js/stomp.js"></script>
//<script type="text/javascript">
//        var stompClient = null;
//
//        function setConnected(connected) {
//        document.getElementById('connect').disabled = connected;
//        document.getElementById('disconnect').disabled = !connected;
//        document.getElementById('conversationDiv').style.visibility
//        = connected ? 'visible' : 'hidden';
//        document.getElementById('response').innerHTML = '';
//        }
//
//        function connect() {
//        var socket = new SockJS('/spring-mvc-java/chat');
//        stompClient = Stomp.over(socket);
//        stompClient.connect({}, function(frame) {
//        setConnected(true);
//        console.log('Connected: ' + frame);
//        stompClient.subscribe('/topic/messages', function(messageOutput) {
//        showMessageOutput(JSON.parse(messageOutput.body));
//        });
//        });
//        }
//
//        function disconnect() {
//        if(stompClient != null) {
//        stompClient.disconnect();
//        }
//        setConnected(false);
//        console.log("Disconnected");
//        }
//
//        function sendMessage() {
//        var from = document.getElementById('from').value;
//        var text = document.getElementById('text').value;
//        stompClient.send("/app/chat", {},
//        JSON.stringify({'from':from, 'text':text}));
//        }
//
//        function showMessageOutput(messageOutput) {
//        var response = document.getElementById('response');
//        var p = document.createElement('p');
//        p.style.wordWrap = 'break-word';
//        p.appendChild(document.createTextNode(messageOutput.from + ": "
//        + messageOutput.text + " (" + messageOutput.time + ")"));
//        response.appendChild(p);
//        }
//</script>
//</head>
//<body onload="disconnect()">
//<div>
//<div>
//<input type="text" id="from" placeholder="Choose a nickname"/>
//</div>
//<br />
//<div>
//<button id="connect" onclick="connect();">Connect</button>
//<button id="disconnect" disabled="disabled" onclick="disconnect();">
//        Disconnect
//</button>
//</div>
//<br />
//<div id="conversationDiv">
//<input type="text" id="text" placeholder="Write a message..."/>
//<button id="sendMessage" onclick="sendMessage();">Send</button>
//<p id="response"></p>
//</div>
//</div>
//
//</body>
//</html>
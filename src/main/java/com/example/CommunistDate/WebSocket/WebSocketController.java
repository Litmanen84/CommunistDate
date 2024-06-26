package com.example.CommunistDate.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {


    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/test")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        LOGGER.info("Start to process message {}", message);
        Thread.sleep(1000); 
        return message;
    }

}
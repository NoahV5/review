package com.victor.test;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Victor
 * @date 2018/04/04
 */
@Component
public class HelloSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(){
        rabbitTemplate.convertAndSend("queue_test","hello,rabbitMQ");
    }
}

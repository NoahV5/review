package com.victor.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * @author Victor
 * @date 2018/04/02
 */
@Component
public class RabbitmqService {

    private final static String QUEUE_NAME = "HELLO";

    public static void main(String[] args) throws Exception{
        //创建链接连接RabbitMQ
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("119.29.84.121");
        factory.setVirtualHost("/");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //指定一个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发送消息
        String message = "Hello World!";
        //往队列发送一条消息
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("发送消息: "+message);
        channel.close();
        connection.close();
    }
}

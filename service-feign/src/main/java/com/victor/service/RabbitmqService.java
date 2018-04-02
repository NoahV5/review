package com.victor.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author Victor
 * @date 2018/04/02
 */
public class RabbitmqService {

    private final static String QUEUE_NAME = "HELLO";
    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        factory.setVirtualHost("/");
        factory.setHost("119.29.84.121");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //创建队列消费者
        QueueingConsumer consumer  = new QueueingConsumer(channel);
        //指定消费队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
        while (true){
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }
}

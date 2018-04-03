package com.victor.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * @author Victor
 * @date 2018/04/03
 */
public class EmitLog {

    private final  static String EXCHANGE_NAME = "EX_LOG";

    public static void main(String[] args)throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        factory.setVirtualHost("/");
        factory.setHost("119.29.84.121");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");//声明转发器和类型
        String log = new Date().toString()+" :log something";
        //往转发器上发送消息
        channel.basicPublish(EXCHANGE_NAME,"",null,log.getBytes());
        System.out.println("发送消息: "+log);
        channel.close();
        connection.close();
    }
}

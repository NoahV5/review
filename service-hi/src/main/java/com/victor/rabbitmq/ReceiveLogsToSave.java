package com.victor.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Victor
 * @date 2018/04/03
 */
public class ReceiveLogsToSave {

    private final static String EXCHANGE_NAME = "EX_LOG";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        factory.setVirtualHost("/");
        factory.setHost("119.29.84.121");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //创建一个非持久,唯一的且自动删除的列队
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //指定接受者,第二个参数为自动应答,无需手动应答
        channel.basicConsume(queueName,true,consumer);
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String (delivery.getBody());
            print2File(message);
        }
    }

    private static void print2File(String message) {
        try {
            String dir = ReceiveLogsToSave.class.getClassLoader().getResource("").getPath();
            String logFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(dir,logFileName+".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write(message.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){

        }
    }
}

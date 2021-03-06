package com.victor.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @author Victor
 * @date 2018/04/02
 */
public class NewTask {

    //队列名称
    private final static String QUEUE_NAME = "WORKQUEUE_TEST";

    public static void main(String[] args) throws Exception{
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("119.29.84.121");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        boolean durable  = true; //消息是否持久化
        //声明队列
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        //发送10条消息，依次在消息后面附加1-10个点
        for (int i = 0; i < 10; i++)
        {
            String dots = "";
            for (int j = 0; j <= i; j++)
            {
                dots += ".";
            }
            String message = "helloworld" + dots+dots.length();
            //需要标识我们的信息为持久化的。通过设置MessageProperties（implements BasicProperties）值为PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        //关闭频道和资源
        channel.close();
        connection.close();
    }
}

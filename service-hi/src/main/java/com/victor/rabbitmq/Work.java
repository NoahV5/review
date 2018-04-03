package com.victor.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author Victor
 * @date 2018/04/02
 */
public class Work {

    //队列的名称
    private final static String QUEUE_NAME = "WORKQUEUE_TEST";
    public static void main(String[] args) throws Exception{
        //区分不同工作进程的输出
        int hashCode = Work.class.hashCode();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("119.29.84.121");
        factory.setVirtualHost("/");
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        Connection connection = factory.newConnection();
        //声明队列
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //设置最大服务转发消息数量
        int prefetchCount = 1;
        channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        boolean ask = false;//打开应答机制
        //指定消费队列
        channel.basicConsume(QUEUE_NAME,ask,consumer);
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(hashCode + " [x] Received '" + message + "'");
            doWork(message);
            System.out.println(hashCode + " [x] Done");
        }
    }

    /**
     * 每个点耗时1s
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException
    {
        for (char ch : task.toCharArray())
        {
            if (ch == '.')
                Thread.sleep(1000);
        }
    }
}

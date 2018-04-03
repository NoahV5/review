package com.victor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Victor
 * @date 2018/04/02
 */
@Configuration
public class AmqpConfig {

    @Value("${spring.rabbitmq.host}")
    public String host;
    @Value("${spring.rabbitmq.username}")
    public String username;
    @Value("${spring.rabbitmq.password}")
    public String password;
    @Value("${spring.rabbitmq.virtual-host}")
    public String virtualHost;

    public final static String EXANAGE_NAME = "EXANGE_TEST";
    public final static String ROUTE_KEY1 = "ROUTE_KEY_1";
    public final static String ROUTE_KEY2 = "ROUTE_KEY_2";



    @Bean
    public ConnectionFactory createConnection(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPassword(password);
        factory.setUsername(username);
        factory.setVirtualHost(virtualHost);
        factory.setPublisherConfirms(true);//必须设置,消息才能回调
        return factory;
    }

    /**
     * 1,定义direct exchange,绑定queueTest
     * 2,durable = "true",rabbitmq重启的时候不需要创建新的转发器
     * 3,direct转发器相对来说比较简单,匹配规则为,如果路由键匹配,消息就会投送到相对较的队列
     *   fanout转发器中没有路由键的说法,他会把消息发送到绑定的在该转发器上面的队列中
     *   topic转发器采用模糊匹配路由器的原则进行转发消息
     *   key:queue在该direct-exchange中的key值,当消息发送给direct-exchange中指定key设置值时,消息会转发给queue参数指定
     */

    @Bean
    public DirectExchange getDirectChange(){

        DirectExchange exchange = new DirectExchange(AmqpConfig.EXANAGE_NAME,true,false);
        return exchange;

    }

    /**
     * 定义queue_one
     *
     */

    @Bean
    public Queue createQueue(){
        /**
         *   durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
         auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
         exclusive  表示该消息队列是否只在当前connection生效,默认是false*/
        Queue queue = new Queue("queue_one",true,false,false);
        return queue;
    }

    /**
     * 讲队列绑定咋转发器上
     */

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(createQueue()).to(getDirectChange()).with(AmqpConfig.ROUTE_KEY1);
    }

    /**
     * 定义queue_two
     *
     */

    @Bean
    public Queue createQueueTwo(){
        /**
         *   durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
         auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
         exclusive  表示该消息队列是否只在当前connection生效,默认是false*/
        Queue queue = new Queue("queue_two",true,false,false);
        return queue;
    }

    /**
     * 讲队列绑定咋转发器上
     */

    @Bean
    public Binding bindingQueueTwo(){
        return BindingBuilder.bind(createQueue()).to(getDirectChange()).with(AmqpConfig.ROUTE_KEY2);
    }

    /**
     * queue listener 观察 监听模式,当有消息到达时会监听所在队列的监听对象
     */
    @Bean
    public SimpleMessageListenerContainer createListener(){
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(this.createConnection());
        listenerContainer.addQueues(createQueueTwo());
        listenerContainer.setExposeListenerChannel(true);
        listenerContainer.setMaxConcurrentConsumers(1);
        listenerContainer.setConcurrentConsumers(1);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式手动确认
        listenerContainer.setMessageListener(messageConsumer());
        return listenerContainer;
    }

    /**
     * 定义消费者
     * @return
     */
    @Bean
    public Object messageConsumer() {
        return new MessageConsumer2();
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(){
        RabbitTemplate template = new RabbitTemplate(createConnection());
        /**若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
         * 每个rabbitTemplate只能有一个confirm-callback和return-callback*/

        template.setConfirmCallback(msgSendConfirmCallBack());
        //template.setReturnCallback(msgSendReturnCallback());
        /**
         * 使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，可针对每次请求的消息去确定’mandatory’的boolean值，
         * 只能在提供’return -callback’时使用，与mandatory互斥*/
        //  template.setMandatory(true);
        return template;
    }
    /**
     消息确认机制
     Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)在channel为事务时，
     不可引入确认模式；同样channel为确认模式下，不可使用事务。
     * @date:2017/8/31
     * @className:ConnectionFactory
     * @author:Administrator
     * @description:
     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack(){
        return new MsgSendConfirmCallBack();
    }
}

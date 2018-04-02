package com.victor.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Victor
 * @date 2018/04/02
 */
@Configuration
public class AmqpConfig {

    @Bean
    public ConnectionFactory getConnection(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("119.29.84.121:15672");
        factory.setPassword("rabbitmq");
        factory.setUsername("rabbitmq");
        factory.setVirtualHost("/");
        factory.setPublisherConfirms(true);//必须设置,消息才能回调
        return factory;
    }

    /**
     * 必须是prototype类型
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)//在每次注入的时候回自动创建一个新的bean实例
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(getConnection());
        return template;
    }
}

package com.victor.test;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

/**
 * @author Victor
 * @date 2018/04/04
 */
@Configuration
public class SendConf {

    public Queue queue(){
        return new Queue("queue_test");
    }
}

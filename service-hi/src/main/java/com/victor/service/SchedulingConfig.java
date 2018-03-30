package com.victor.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Victor
 * @date 2018/03/20
 */
@EnableScheduling
@Configuration
public class SchedulingConfig {

    @Scheduled(cron = "0/5 * * * * ?")
    public void scheduled(){
        System.out.print(System.currentTimeMillis());
    }
}

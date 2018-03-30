package com.victor.service;

import org.springframework.stereotype.Component;

/**
 * @author Victor
 * @date 2018/03/12
 */
@Component
public class HiServiceHystrix implements IFeignService{

    public String sayHi(){
        return "服务调用异常!";
    }
}

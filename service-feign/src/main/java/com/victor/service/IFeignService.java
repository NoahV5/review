package com.victor.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Victor
 * @date 2018/03/12
 */
@FeignClient(value = "service-hi",fallback = HiServiceHystrix.class)
public interface IFeignService {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "hiError")
    String sayHi();

}

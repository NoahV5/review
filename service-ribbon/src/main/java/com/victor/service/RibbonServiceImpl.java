package com.victor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Victor
 * @date 2018/03/12
 */
@Service
public class RibbonServiceImpl implements RibbonService {



    @Autowired
    RestTemplate restTemplate;

    public String sayHi() {
        return restTemplate.getForObject("http://SERVICE-HI/hi",String.class);
    }
}

package com.victor.controller;

import com.victor.service.IFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Victor
 * @date 2018/03/12
 */
@RestController
public class FeignController {

    @Autowired
    private IFeignService feignService;

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    @ResponseBody
    public String sayHi(){
        return feignService.sayHi();
    }
}

package com.victor;

import com.victor.config.AmqpConfig;
import com.victor.service.HiService;
import com.victor.service.RabbitmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@SpringBootApplication
@EnableEurekaClient
@Controller
@EnableScheduling
public class ServiceHiApplication {

	@Autowired
	private HiService hiService;

	@Autowired
	private RabbitmqService rabbitmqService;

	public static void main(String[] args) {
		SpringApplication.run(ServiceHiApplication.class, args);
	}

	@RequestMapping(value = "/hi",method = RequestMethod.GET)
	@ResponseBody
	public String sayHi(){
		return hiService.sayHi();
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@RequestMapping("/send")
	public String send3() throws UnsupportedEncodingException {
		String uuid = UUID.randomUUID().toString();
		CorrelationData correlationId = new CorrelationData(uuid);
		rabbitTemplate.convertAndSend(AmqpConfig.EXANAGE_NAME, AmqpConfig.ROUTE_KEY2, "哈哈", correlationId);
		return uuid;
	}

}

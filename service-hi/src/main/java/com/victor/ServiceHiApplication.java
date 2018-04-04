package com.victor;

import com.victor.config.AmqpConfig;
import com.victor.service.HiService;
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
	private RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ServiceHiApplication.class, args);
	}

	@RequestMapping(value = "/hi",method = RequestMethod.GET)
	@ResponseBody
	public String sayHi(){
		return hiService.sayHi();
	}


	@RequestMapping("/send")
	public String send3() throws UnsupportedEncodingException {
		String uuid = UUID.randomUUID().toString();
		CorrelationData correlationId = new CorrelationData(uuid);

		rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, AmqpConfig.ROUTINGKEY2, "哈哈", correlationId);
		return uuid;
	}
}

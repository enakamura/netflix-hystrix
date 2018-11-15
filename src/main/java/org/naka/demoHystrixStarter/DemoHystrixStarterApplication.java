package org.naka.demoHystrixStarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@SpringBootApplication
@RestController
@EnableHystrix
public class DemoHystrixStarterApplication {

	@Value("${url}")
	private String url;
	
	@Value("${apiKey}")
	private String apiKey;
	
	@Autowired
	RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoHystrixStarterApplication.class, args);
	}
	
	@GetMapping("/testeHystrix/{city}")
	@HystrixCommand(fallbackMethod="fallbackWeather", commandProperties= {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")})
	public Object testeHystrix(@PathVariable("city") String city) {
		return restTemplate.getForEntity(url, String.class, city, apiKey);
	}
	
	@SuppressWarnings({"unused"})
	private Object fallbackWeather(String city) {
		return new ResponseEntity<Object>(Status.builder().status("fallback method").build(),HttpStatus.I_AM_A_TEAPOT);
	}

	
}

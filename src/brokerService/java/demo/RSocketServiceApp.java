///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.springframework.boot:spring-boot-dependencies:2.6.6@pom
//DEPS org.springframework.boot:spring-boot-starter-webflux
//DEPS org.springframework.boot:spring-boot-starter-actuator
//DEPS com.alibaba.rsocket:alibaba-rsocket-spring-boot-starter:1.1.3
//SOURCES GreetingService.java
//FILES ../../resources/application.properties

package demo;

import com.alibaba.rsocket.RSocketService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RSocketServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(RSocketServiceApp.class, args);
    }

    @RSocketService(serviceInterface = GreetingService.class)
    @Service
    public static class GreetingServiceImpl implements GreetingService {
        @Override
        public Mono<String> hello(String name) {
            return Mono.just("Hello " + name + "!");
        }
    }
}

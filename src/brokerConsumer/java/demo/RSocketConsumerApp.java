///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.springframework.boot:spring-boot-dependencies:2.6.6@pom
//DEPS org.springframework.boot:spring-boot-starter-webflux
//DEPS org.springframework.boot:spring-boot-starter-actuator
//DEPS com.alibaba.rsocket:alibaba-rsocket-spring-boot-starter:1.1.3
//SOURCES GreetingService.java
//FILES ../../resources/application.properties

package demo;

import com.alibaba.rsocket.invocation.RSocketRemoteServiceBuilder;
import com.alibaba.rsocket.metadata.RSocketMimeType;
import com.alibaba.rsocket.upstream.UpstreamManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RSocketConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(RSocketConsumerApp.class, args);
    }

    @Bean
    public GreetingService greetingService(UpstreamManager upstreamManager) {
        return RSocketRemoteServiceBuilder
                .client(GreetingService.class)
                .upstreamManager(upstreamManager)
                .acceptEncodingType(RSocketMimeType.Json)
                .build();
    }

    @RestController
    public static class PortalController {
        @Autowired
        GreetingService greetingService;

        @RequestMapping("/")
        public Mono<String> index() {
            return greetingService.hello("World");
        }
    }

}

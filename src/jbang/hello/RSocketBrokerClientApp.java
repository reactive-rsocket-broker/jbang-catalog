//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.slf4j:slf4j-simple:1.7.30
//DEPS org.projectlombok:lombok:1.18.18
//DEPS com.alibaba.rsocket:alibaba-rsocket-core:1.0.1

import com.alibaba.rsocket.client.RSocketBrokerClient;
import com.alibaba.rsocket.client.RSocketBrokerConnector;
import com.alibaba.rsocket.encoding.JsonUtils;
import com.alibaba.rsocket.metadata.RSocketMimeType;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * jbang RSocket Broker Client
 *
 * @author leijuan
 */
public class RSocketBrokerClientApp {
    private static final char[] jwtToken = "".toCharArray();
    private static final List<String> brokers = Collections.singletonList("tcp://127.0.0.1:9999");

    public static void main(String[] args) throws Exception {
        RSocketBrokerClient brokerClient = RSocketBrokerConnector
                .create()
                .appName("MockApp")
                .dataMimeType(RSocketMimeType.Json)
                .jwtToken(jwtToken)
                //.service("com.alibaba.service.DemoMockService", DemoMockService.class, (DemoMockService) id -> Mono.just("Hello " + id))
                .brokers(brokers)
                .connect();
        //waiting for broker created
        Thread.sleep(1000);
        UserService userService = userService(brokerClient);
        User user = userService.findById(1).block();
        System.out.println(JsonUtils.toJsonText(user));
        brokerClient.dispose();
    }

    public static UserService userService(RSocketBrokerClient brokerClient) {
        return brokerClient.buildService(UserService.class, "com.alibaba.user.UserService");
    }

    public interface UserService {
        Mono<User> findById(Integer id);
    }

    @Data
    public static class User implements Serializable {
        private Integer id;
        private String nick;
        private String email;
        private String phone;
    }
}

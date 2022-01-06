package demo;

import reactor.core.publisher.Mono;

public interface GreetingService {
    Mono<String> hello(String name);
}

package com.example.demoproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SpringBootApplication
@Controller
public class DemoProducerApplication {

    @Autowired
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(DemoProducerApplication.class, args);
    }

    @Bean
    public Supplier<Movie> supplier() {
        return () -> {
            Collections.shuffle(MOVIES);
            return MOVIES.iterator().next();
        };
    }

    private static final List<Movie> MOVIES = new LinkedList<>(List.of(
            new Movie("Hello Kitty versus Chthulhu", 4),
            new Movie("Spongebob Squarepants visits North Korea", 5),
            new Movie("Expert Tax Evasion with Bob the Builder", 2)
    ));

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delegateToSupplier(@RequestBody String body) {
        System.out.println("Sending " + body);
        streamBridge.send("supplier-out-0", body);
    }
}

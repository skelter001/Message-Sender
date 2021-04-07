package com.xaghoul.demo;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.repository.MessageTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(MessageTemplateRepository repository) {
        return args -> {
            repository.save(MessageTemplate.builder()
                    .name("template1")
                    .recipients(Arrays.asList(new URL("https://httpbin.org/post"),
                            new URL("https://postman-echo.com/post")))
                    .template("Jetbrains Internship in teamName team for termLong term.")
                    //.substitutionWords(new HashMap<>())
                    .build());
            repository.save(MessageTemplate.builder()
                    .name("template2")
                    .build());
            repository.save(MessageTemplate.builder()
                    .name("$$$")
                    .template("Today is $year$ year")
                    .recipients(Collections.singletonList(new URL("https://httpbin.org/post")))
                    .build());
        };
    }
}

package com.xaghoul.demo;

import com.xaghoul.demo.model.MessageTemplate;
import com.xaghoul.demo.repository.MessageTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
                    .substitutionValue("A-Team")
                    .build());
            repository.save(MessageTemplate.builder()
                    .name("template2")
                    .build());

        };
    }
}

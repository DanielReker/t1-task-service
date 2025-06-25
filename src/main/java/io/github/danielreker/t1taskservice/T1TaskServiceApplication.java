package io.github.danielreker.t1taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class T1TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1TaskServiceApplication.class, args);
    }

}

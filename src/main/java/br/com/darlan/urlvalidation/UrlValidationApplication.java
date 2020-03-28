package br.com.darlan.urlvalidation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@EnableRabbit
@EnableCaching
@EnableAspectJAutoProxy
@SpringBootApplication
public class UrlValidationApplication {

    public static void main(final String[] args) {
        SpringApplication.run(UrlValidationApplication.class, args);
    }

}

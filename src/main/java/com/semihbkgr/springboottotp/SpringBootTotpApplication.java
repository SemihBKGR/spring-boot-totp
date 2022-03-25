package com.semihbkgr.springboottotp;

import com.github.javafaker.Faker;
import com.semihbkgr.springboottotp.user.User;
import com.semihbkgr.springboottotp.user.UserDetail;
import com.semihbkgr.springboottotp.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@Slf4j
@SpringBootApplication
public class SpringBootTotpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTotpApplication.class, args);
    }

    @Bean
    public CommandLineRunner initUser(UserRepository repository) {
        return (String... args) -> {
            var faker = Faker.instance();
            log.info("-".repeat(50));
            IntStream.range(0, 10)
                    .mapToObj(i -> {
                        var username = faker.name().firstName().toLowerCase();
                        var password = "{noop}" + faker.internet().password();
                        var user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        var userDetails = new UserDetail();
                        userDetails.setFirstname(username);
                        userDetails.setLastname(faker.name().lastName());
                        userDetails.setEmail(faker.internet().emailAddress());
                        userDetails.setTwofaEnabled(false);
                        user.setUserDetail(userDetails);
                        return user;
                    }).forEach(user -> {
                        repository.save(user);
                        log.info("Username: {}, Password: {}", user.getUsername(), user.getPassword());
                    });
            log.info("-".repeat(50));
        };
    }

}

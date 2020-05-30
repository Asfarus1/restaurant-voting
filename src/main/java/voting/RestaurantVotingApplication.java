package voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootApplication
public class RestaurantVotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }

//    @Bean
//    @Scope("prototype")
//    LocalDateTime now() {
//        return LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0, 0));
////        return LocalDateTime.now();
//    }
}

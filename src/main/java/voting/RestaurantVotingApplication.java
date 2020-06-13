package voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

@SpringBootApplication
public class RestaurantVotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }

    @Autowired
    public void addFormatters(@Qualifier("defaultConversionService") FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
}

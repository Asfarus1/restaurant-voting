package voting.web;

import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import voting.repository.LunchRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;

@AutoConfigureMockMvc
@ContextConfiguration(classes = UserRoleControllerTest.Config.class)
class UserRoleControllerTest {
    @MockBean
    private LunchRepository lunchRepository;
    @MockBean
    private Supplier<LocalDateTime> timeSupplier;

    @Test
    @WithUserDetails()
    void haveLunchBefore11() {
        Mockito.when(timeSupplier.get())
                .thenReturn(LocalDateTime.of(LocalDate.now(), LocalTime.of(10,0,0)));
    }

    @Test
    void haveLunchAfter11() {
    }

    @Test
    void getCurrentUser() {
    }

    @Configuration
    static class Config {

        @Bean
        public LunchRepository lunchRepository() {
            return null;
        }

        @Bean
        public Supplier<LocalDateTime> timeSupplier() {
            return null;
        }

        @Bean
        public UserRoleController userRoleController(LunchRepository lunchRepository,
                                                     Supplier<LocalDateTime> timeSupplier) {
            return new UserRoleController(lunchRepository) {
                @Setter
                private LocalDateTime currentDateTime;

                @Override
                protected LocalDateTime now() {
                    return timeSupplier.get();
                }
            };
        }
    }
}
package voting.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import voting.repository.LunchRepository;
import voting.security.SecurityUtilBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserRoleControllerTest {
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.parse("2020-05-05T00:00:00");

    @Value("${end-hour-for-choose-lunch}")
    private int endHourForChooseLunch;

    @Value("${spring.data.rest.base-path}")
    protected String restRoot;

    @MockBean
    private LunchRepository lunchRepository;

    @MockBean
    private SecurityUtilBean securityUtilBean;

    @SpyBean
    private UserRoleController controller;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        when(securityUtilBean.getUserId()).thenReturn(2L);
    }

    @Test
    @WithMockUser
    void haveLunchBefore11() throws Exception {
        LocalDateTime beforeEndTime = LOCAL_DATE_TIME.plusHours(endHourForChooseLunch - 1);
        when(controller.now()).thenReturn(beforeEndTime);

        mockMvc.perform(put(restRoot + "/restaurants/44/have-lunch"))
                .andDo(print())
                .andExpect(status().isAccepted());

        Mockito.verify(lunchRepository, Mockito.times(1))
                .haveLunchIn(44L, LOCAL_DATE_TIME.toLocalDate(), 2L);
    }

    @Test
    @WithMockUser
    void haveLunchAfter11() throws Exception {
        LocalDateTime endTime = LOCAL_DATE_TIME.plusHours(endHourForChooseLunch);
        when(controller.now()).thenReturn(endTime);

        mockMvc.perform(put(restRoot + "/restaurants/44/have-lunch"))
                .andDo(print())
                .andExpect(status().isForbidden());

        Mockito.verify(lunchRepository, never())
                .haveLunchIn(any(), any(), any());
    }

    @Test
    @WithMockUser
    void forwardCurrentUser() throws Exception {
        mockMvc.perform(get(restRoot + "/account"))
                .andExpect(forwardedUrl(restRoot + "/users/2/"));

        mockMvc.perform(get(restRoot + "/account/"))
                .andExpect(forwardedUrl(restRoot + "/users/2/"));

        mockMvc.perform(get(restRoot + "/account/lunches"))
                .andExpect(forwardedUrl(restRoot + "/users/2/lunches"));
    }
}
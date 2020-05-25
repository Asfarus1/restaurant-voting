package voting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import voting.web.dto.RefreshTokenRequest;
import voting.web.dto.TokenResponse;

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    private static final String USER_ACCESS_TOKEN = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwia" +
            "WF0IjoxNTkwNDM0MDAwLCJleHAiOjMzOTA0MzQwMDB9.o-JP6-t1wt_1k5gE_PzL9HKGCSSEMwgD4p3-gNp7T_4";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "authService")
    void getAccessToken() throws Exception {
        checkToken(mockMvc.perform(post("/auth/create_token")));
    }

    @Test
    @Sql(value = "classpath:add-refresh-token.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:delete-refresh-token.sql", executionPhase = AFTER_TEST_METHOD)
    void refreshAccessToken() throws Exception {
        String body = objectMapper.writeValueAsString(getRefreshTokenRequest());
        checkToken(mockMvc.perform(post("/auth/refresh_token")
                .contentType(APPLICATION_JSON)
                .content(body)));
    }

    @Test
    @Sql(value = "classpath:add-refresh-token.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:delete-refresh-token.sql", executionPhase = AFTER_TEST_METHOD)
    void refreshTokenWorksOnce() throws Exception {
        String body = objectMapper.writeValueAsString(getRefreshTokenRequest());
        mockMvc.perform(post("/auth/refresh_token")
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/refresh_token")
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(value = "classpath:add-refresh-token.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:delete-refresh-token.sql", executionPhase = AFTER_TEST_METHOD)
    @WithUserDetails(userDetailsServiceBeanName = "authService")
    void logout() throws Exception {
        mockMvc.perform(post("/auth/logout")).andExpect(status().isAccepted());

        mockMvc.perform(post("/auth/refresh_token")
                .content(objectMapper.writeValueAsString(getRefreshTokenRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accessUsingBasic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/").with(httpBasic("user", "u")))
                .andExpect(status().isOk());
    }

    @Test
    void accessUsingToken() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/")
                .header(HttpHeaders.AUTHORIZATION, USER_ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:add-expired-refresh-token.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(value = "classpath:delete-refresh-token.sql", executionPhase = AFTER_TEST_METHOD)
    void refreshTokenExpired() throws Exception {
        String body = objectMapper.writeValueAsString(getRefreshTokenRequest());
        mockMvc.perform(post("/auth/refresh_token")
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError());
    }

    private RefreshTokenRequest getRefreshTokenRequest() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("0ba2a955-fc68-4f5d-87ad-688702ad7269");
        refreshTokenRequest.setUsername("user");
        return refreshTokenRequest;
    }

    private void checkToken(ResultActions actions) throws Exception {
        String body = actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokenResponse = objectMapper.readValue(body, TokenResponse.class);

        Assertions.assertThat(tokenResponse.getRefreshToken()).isNotBlank();
        Assertions.assertThat(tokenResponse.getRefreshToken().length()).isEqualTo(36);
        Assertions.assertThat(tokenResponse.getAccessToken()).isNotBlank();
        Assertions.assertThat(tokenResponse.getRefreshExpired()).isAfter(new Date());
        Assertions.assertThat(tokenResponse.getAccessExpired()).isAfter(new Date());
    }
}
package voting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import voting.TestData;
import voting.repository.RefreshTokenRepository;
import voting.repository.UserRepository;
import voting.security.JwtTokenProvider;
import voting.security.exceptions.TokenAuthenticationException;
import voting.web.dto.RefreshTokenRequest;
import voting.web.dto.SignUpRequest;
import voting.web.dto.TokenResponse;

import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Value("${spring.data.rest.base-path}")
    protected String restRoot;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        when(tokenProvider.generateToken(anyString())).thenReturn(TestData.USER_ACCESS_TOKEN);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "authService")
    void getAccessToken() throws Exception {
        checkToken(mockMvc.perform(post("/auth/create_token")));
        verify(refreshTokenRepository, times(1))
                .add(eq(TestData.USER_ID), anyString(), any(Date.class));
    }

    @Test
    void refreshAccessToken() throws Exception {
        when(refreshTokenRepository.update(
                eq(TestData.USER_ID), eq(TestData.USER_REFRESH_TOKEN), anyString(), any(Date.class)))
                .thenReturn(true);

        String body = objectMapper.writeValueAsString(getRefreshTokenRequest());
        checkToken(mockMvc.perform(post("/auth/refresh_token")
                .contentType(APPLICATION_JSON)
                .content(body)));

        verify(refreshTokenRepository, times(1))
                .update(eq(TestData.USER_ID), eq(TestData.USER_REFRESH_TOKEN), anyString(), any(Date.class));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "authService")
    void logout() throws Exception {
        mockMvc.perform(post("/auth/logout")).andExpect(status().isAccepted());
        verify(refreshTokenRepository, times(1)).deleteAllByUserId(TestData.USER_ID);
    }

    @Test
    void accessUsingBasic() throws Exception {
        mockMvc.perform(get(restRoot))
                .andExpect(status().isForbidden());

        mockMvc.perform(get(restRoot).with(httpBasic(TestData.USERNAME, TestData.PASSWORD)))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessUsingToken() throws Exception {
        when(tokenProvider.isSignedWith(TestData.USER_ACCESS_TOKEN)).thenReturn(true);
        when(tokenProvider.validateAndGetUsername(TestData.USER_ACCESS_TOKEN)).thenReturn(TestData.USERNAME);

        mockMvc.perform(get(restRoot))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get(restRoot)
                .header(HttpHeaders.AUTHORIZATION, TestData.USER_ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void refreshTokenExpired() throws Exception {
        when(tokenProvider.validateAndGetUsername(anyString()))
                .thenThrow(TokenAuthenticationException.class);

        String body = objectMapper.writeValueAsString(getRefreshTokenRequest());

        mockMvc.perform(post("/auth/refresh_token")
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DirtiesContext
    void signUpTest() throws Exception {
        String username = "test_user";
        String password = "test_pass";
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName(username);
        signUpRequest.setPassword(password);

        checkToken(mockMvc.perform(post("/auth/sign_up")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(APPLICATION_JSON)));

        Assertions.assertThat(userRepository.findByUsername(username)).isNotEmpty();

        mockMvc.perform(post("/auth/create_token")
                .with(httpBasic(username, password)))
                .andExpect(status().isOk());
    }

    private RefreshTokenRequest getRefreshTokenRequest() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken(TestData.USER_REFRESH_TOKEN);
        refreshTokenRequest.setUsername(TestData.USERNAME);
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
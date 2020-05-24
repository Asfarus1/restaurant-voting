package voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import voting.web.dto.TokenResponse;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "authService")
    void getAccessToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/create_token/"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
//        ResponseEntity<TokenResponse> response = template.withBasicAuth("user", "u")
//                .postForEntity(url, null, TokenResponse.class);
//        System.out.println(response);
    }

    @Test
    void refreshAccessToken() {
    }

    @Test
    void logout() {
    }

//    private String getBasicAuthHeader(String username, String password){
}
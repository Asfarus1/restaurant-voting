package voting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LunchApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //<user>
    @Test
    @WithMockUser
    void userGetAll() throws Exception {
        mockMvc.perform(get(getCollectionUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(content().string(containsString("page")));
    }

    @Test
    @WithMockUser
    void userGetOne() throws Exception {
        mockMvc.perform(get(getItemUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    @WithMockUser
    void userPost() throws Exception {
        mockMvc.perform(post(getCollectionUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser
    void userPut() throws Exception {
        mockMvc.perform(put(getItemUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser
    void userDelete() throws Exception {
        mockMvc.perform(delete(getItemUrl())
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
    //</user>

    //<admin>
    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminGetAll() throws Exception {
        mockMvc.perform(get(getCollectionUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(content().string(containsString("page")));
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminGetOne() throws Exception {
        mockMvc.perform(get(getItemUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    @DirtiesContext
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminPost() throws Exception {
        mockMvc.perform(post(getCollectionUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminPut() throws Exception {
        mockMvc.perform(put(getItemUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminDelete() throws Exception {
        mockMvc.perform(delete(getItemUrl())
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
    //</admin>

    private Map<String, Object> getNewItem() {
        return
                Map.of("date", LocalDate.now(),
                        "user", "/users/2",
                        "restaurant", "/restaurants/42");
    }

    private String getCollectionUrl(){
        return "/lunches";
    }

    private String getItemUrl(){
        return "/lunches/110";
    }
}

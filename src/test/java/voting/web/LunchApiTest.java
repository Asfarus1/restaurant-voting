package voting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class LunchApiTest extends AbstractRestApiPermissionsTest {

    @Override
    protected Map<String, Object> getNewItem() {
        return
                Map.of("date", LocalDate.now(),
                        "user", "/users/2",
                        "restaurant", "/restaurants/42");
    }

    @Override
    protected String getCollectionUrl(){
        return "/lunches";
    }

    @Override
    protected String getItemUrl(){
        return "/lunches/110";
    }

    //<user>
    @Test
    @WithMockUser
    void userPost() throws Exception {
        mockMvc.perform(post(restRoot +  getCollectionUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser
    void userPut() throws Exception {
        mockMvc.perform(put(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser
    void userDelete() throws Exception {
        mockMvc.perform(delete(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
    //</user>

    //<admin>
    @Test
    @DirtiesContext
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminPost() throws Exception {
        mockMvc.perform(post(restRoot + getCollectionUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminPut() throws Exception {
        mockMvc.perform(put(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminDelete() throws Exception {
        mockMvc.perform(delete(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
    //</admin>
}

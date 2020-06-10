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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractRestApiPermissionsTest {

    @Value("${spring.data.rest.base-path}")
    protected String restRoot;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected abstract Object getNewItem();

    protected abstract String getCollectionUrl();

    protected abstract String getItemUrl();

    //<user>
    @Test
    @WithMockUser
    void userGetAll() throws Exception {
        mockMvc.perform(get(restRoot + getCollectionUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(content().string(containsString("page")));
    }

    @Test
    @WithMockUser
    void userGetOne() throws Exception {
        mockMvc.perform(get(restRoot + getItemUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    @WithMockUser
    void userPost() throws Exception {
        mockMvc.perform(post(restRoot + getCollectionUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void userPut() throws Exception {
        mockMvc.perform(put(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void userDelete() throws Exception {
        mockMvc.perform(delete(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    //</user>

    //<admin>
    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminGetAll() throws Exception {
        mockMvc.perform(get(restRoot + getCollectionUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(content().string(containsString("page")));
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminGetOne() throws Exception {
        mockMvc.perform(get(restRoot + getItemUrl()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    @DirtiesContext
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminPost() throws Exception {
        mockMvc.perform(post(restRoot + getCollectionUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DirtiesContext
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminPut() throws Exception {
        mockMvc.perform(put(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getNewItem())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    @WithMockUser(value = "admin", roles = "ADMIN")
    void adminDelete() throws Exception {
        mockMvc.perform(delete(restRoot + getItemUrl())
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
    //</admin>
}

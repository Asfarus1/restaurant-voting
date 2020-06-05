package voting.web.dto;

import lombok.Data;

@Data
public class TokenRequest {
    private String username;
    private String password;
}

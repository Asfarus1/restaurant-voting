package voting.web.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
    private String username;
}

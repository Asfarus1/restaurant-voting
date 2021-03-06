package voting.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RefreshTokenRequest {
    @NotEmpty
    private String refreshToken;
    @NotEmpty
    private String username;
}

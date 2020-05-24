package voting.web.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private Date accessExpired;
    private Date refreshExpired;
}

package voting.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignUpRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
}

package voting.web;

import org.springframework.boot.test.context.TestConfiguration;
import voting.security.JwtTokenProvider;

@TestConfiguration
public class TestConfig {
    JwtTokenProvider JwtTokenProvider(){
        return new JwtTokenProvider(1800000,"test_secret");
    }
}

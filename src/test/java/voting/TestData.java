package voting;

import voting.domain.Role;
import voting.domain.User;

public class TestData {
    public static final long USER_ID = 2L;
    public static final User USER = new User("user", "{noop}u", Role.ROLE_USER) {
        {
            setId(USER_ID);
        }
    };
    public static final String USER_REFRESH_TOKEN = "0ba2a955-fc68-4f5d-87ad-688702ad7269";
    public static final String USER_ACCESS_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwia" +
            "WF0IjoxNTkwNDM0MDAwLCJleHAiOjMzOTA0MzQwMDB9.o-JP6-t1wt_1k5gE_PzL9HKGCSSEMwgD4p3-gNp7T_4";
    public static final String USERNAME = "user";
    public static final String PASSWORD = "u";
}

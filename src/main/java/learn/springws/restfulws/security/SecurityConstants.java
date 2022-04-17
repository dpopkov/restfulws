package learn.springws.restfulws.security;

import learn.springws.restfulws.RestfulwsApplicationContext;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 10 * 24 * 60 * 60 * 1000;    // 10 days in ms
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String H2_CONSOLE_URL = "/h2-console/**";

    public static String getTokenSecret() {
        AppProperties appProperties = RestfulwsApplicationContext.getBean(AppProperties.class);
        return appProperties.getTokenSecret();
    }
}

package learn.springws.restfulws.security;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 10 * 24 * 60 * 60 * 1000;    // 10 days in ms
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_SECRET = "a7slxo29oxnA7b";
    public static final String SIGN_UP_URL = "/users";
}

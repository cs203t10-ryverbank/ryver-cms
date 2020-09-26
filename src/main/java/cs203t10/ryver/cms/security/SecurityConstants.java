package cs203t10.ryver.cms.security;

public class SecurityConstants {
    public static final String SECRET = "RyverAPISecretKey";
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "auth";
}


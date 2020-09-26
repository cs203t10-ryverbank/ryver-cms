package cs203t10.ryver.cms.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cs203t10.ryver.cms.user.User;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static cs203t10.ryver.cms.security.SecurityConstants.AUTHORITIES_KEY;
import static cs203t10.ryver.cms.security.SecurityConstants.EXPIRATION_TIME;
import static cs203t10.ryver.cms.security.SecurityConstants.HEADER_STRING;
import static cs203t10.ryver.cms.security.SecurityConstants.SECRET;
import static cs203t10.ryver.cms.security.SecurityConstants.TOKEN_PREFIX;

/**
 * Handle all authentication attempts on the `/login` route.
 *
 * {@link UsernamePasswordAuthenticationFilter} registers itself on the
 * `/login` route. Any request made to the `/login` endpoint will activate
 * this authentication filter.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            return authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        // Serialize all granted authorities into a comma-separated string.
        final String authorities = authResult.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        String token = JWT.create()
            .withSubject(((User) authResult.getPrincipal()).getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .withClaim(AUTHORITIES_KEY, authorities)
            .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }

}

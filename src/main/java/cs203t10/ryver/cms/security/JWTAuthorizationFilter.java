package cs203t10.ryver.cms.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static cs203t10.ryver.cms.security.SecurityConstants.AUTHORITIES_KEY;
import static cs203t10.ryver.cms.security.SecurityConstants.HEADER_STRING;
import static cs203t10.ryver.cms.security.SecurityConstants.SECRET;
import static cs203t10.ryver.cms.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        // If no JWT in the Authorization header, then skip this filter.
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = getAuthentication(request);

        // If the JWT is valid, set the security context.
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }

    /**
     * Verify the JWT of a request.
     * @return An authentication token if the JWT is valid, or null if it is not.
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) {
            return null;
        }
        DecodedJWT jwt = JWT.require(HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));

        // Extract the username (subject) from the JWT.
        final String user = jwt.getSubject();
        if (user == null) {
            return null;
        }

        // Extract the authorities from the JWT.
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(jwt.getClaim(AUTHORITIES_KEY).asString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

}

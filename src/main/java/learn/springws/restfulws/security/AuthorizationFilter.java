package learn.springws.restfulws.security;

import io.jsonwebtoken.Jwts;
import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.trace("doFilterInternal(..)");
        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    /**
     * Extracts from the http request and validates the JWT token.
     * @param request http request
     * @return the authentication object which represents a successfully authenticated user's email or null otherwise
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (token != null && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            token = token.substring(SecurityConstants.TOKEN_PREFIX.length());
            String emailAsUsername = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if (emailAsUsername != null) {
                // todo: it would be better not to get authorities from the database on every authorization (m.b. use JWT)
                Optional<UserEntity> byEmail = userRepository.findByEmail(emailAsUsername);
                UserPrincipal userPrincipal = new UserPrincipal(byEmail.orElseThrow());
                return new UsernamePasswordAuthenticationToken(emailAsUsername, null,
                        userPrincipal.getAuthorities());
            }
        }
        return null;
    }
}

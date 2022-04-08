package learn.springws.restfulws.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import learn.springws.restfulws.RestfulwsApplicationContext;
import learn.springws.restfulws.rest.model.request.UserLoginRequestModel;
import learn.springws.restfulws.service.UserService;
import learn.springws.restfulws.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.trace("attemptAuthentication(..)");
        try {
            UserLoginRequestModel credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequestModel.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        log.trace("successfulAuthentication(..)");
        String emailAsUserName = ((User) authResult.getPrincipal()).getUsername();
        String token = Jwts.builder()
                .setSubject(emailAsUserName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        UserService userService = RestfulwsApplicationContext.getBean(UserServiceImpl.class);
        String publicUserId = userService.getUserByEmail(emailAsUserName).getUserId();
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
        response.addHeader("UserID", publicUserId);
    }
}

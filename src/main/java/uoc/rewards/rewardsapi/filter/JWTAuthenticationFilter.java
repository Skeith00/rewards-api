package uoc.rewards.rewardsapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uoc.rewards.rewardsapi.model.dto.request.OrganistationLogin;
import uoc.rewards.rewardsapi.security.jwt.JWTWritter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private AuthenticationManager authenticationManager;
    private JWTWritter jwtWritter;
    private ObjectMapper objectMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTWritter jwtWritter, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtWritter = jwtWritter;
        this.objectMapper = objectMapper;
        this.setAuthenticationFailureHandler(new UnauthorizedHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            OrganistationLogin creds = objectMapper
                    .readValue(req.getInputStream(), OrganistationLogin.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        //res.setHeader("Access-Control-Allow-Headers", "Authorization");
        res.setHeader("Access-Control-Expose-Headers", "Authorization");
        String token = jwtWritter.generateToken(((User) auth.getPrincipal()).getUsername());
        res.setHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
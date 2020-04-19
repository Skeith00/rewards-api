package uoc.rewards.rewardsapi.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uoc.rewards.rewardsapi.security.jwt.JWTReader;
import uoc.rewards.rewardsapi.security.jwt.model.JWT;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final static Logger LOG = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private JWTReader jwtReader;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTReader jwtReader) {
        super(authenticationManager);
        this.jwtReader = jwtReader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOG.info("Starting a transaction for req : {}", request.getRequestURI());

        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // parse the token.
        JWT decodedToken = jwtReader.decode(header.replace(TOKEN_PREFIX, ""), JWT.class);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(decodedToken.getSub(), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomHttpServletRequest customRequest = new CustomHttpServletRequest(request);
        customRequest.addHeader("organisation", "1");
        filterChain.doFilter(customRequest, response);
    }
}

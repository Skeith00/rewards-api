package uoc.rewards.rewardsapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uoc.rewards.rewardsapi.common.exception.ForbiddenException;
import uoc.rewards.rewardsapi.security.jwt.JWTReader;
import uoc.rewards.rewardsapi.security.jwt.model.JWT;
import uoc.rewards.rewardsapi.util.MessageUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final static Logger LOG = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private JWTReader jwtReader;
    private ObjectMapper objectMapper;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTReader jwtReader, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.jwtReader = jwtReader;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOG.info("Starting a transaction for req : {}", request.getRequestURI());
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // parse the token.
            JWT decodedToken = jwtReader.decode(header.replace(TOKEN_PREFIX, ""), JWT.class);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(decodedToken.getSub(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomHttpServletRequest customRequest = new CustomHttpServletRequest(request);
            customRequest.addHeader("organisation", decodedToken.getSub());
            filterChain.doFilter(customRequest, response);
        } catch (ForbiddenException e) {
            notAuthorized(response, e.getMessage());
        }
    }

    private void notAuthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        Map<String, Object> errorAttributes = MessageUtils.getErrorAttributes(HttpStatus.FORBIDDEN, message);
        OutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, errorAttributes);
        out.flush();
    }
}

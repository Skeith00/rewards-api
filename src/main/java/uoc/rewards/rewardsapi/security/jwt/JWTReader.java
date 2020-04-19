package uoc.rewards.rewardsapi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import uoc.rewards.rewardsapi.common.exception.BadJWTException;
import uoc.rewards.rewardsapi.common.exception.ExpiredJWTException;

import java.io.IOException;

/**
 * JWT Reader combines many JWT Verifiers and helps decode tokens into pojo objects safely
 */
public class JWTReader {

    private static final String JWT_INVALID = "JWT is not valid";

    private JWTVerifier verifier;
    private ObjectMapper mapper;

    public JWTReader(ObjectMapper mapper, JWTVerifier verifier) {
        this.mapper = mapper;
        this.verifier = verifier;
    }

    public <T extends uoc.rewards.rewardsapi.security.jwt.model.JWT> T decode(String token, Class<T> type) {
        if (verifier == null) {
            throw new IllegalStateException("JWTReader doesn't have any verifiers registered!");
        }

        try {
            verify(token);
            DecodedJWT decodedJWT = JWT.decode(token);
            return mapper.readerFor(type).readValue(Base64.decodeBase64(decodedJWT.getPayload()));

        } catch (IOException e) {
            throw new BadJWTException("Unexpected JWT payload");
        }
    }

    // Verifying signature in token
    private void verify(String jwt) {
        try {
            verifier.verify(jwt);
        } catch (TokenExpiredException e) {
            throw new ExpiredJWTException("JWT is expired");
        } catch (InvalidClaimException e) {
            throw new BadJWTException(JWT_INVALID);
        } catch (Exception e) {
            throw new BadJWTException(JWT_INVALID);
        }
    }
}

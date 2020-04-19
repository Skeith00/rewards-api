package uoc.rewards.rewardsapi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import uoc.rewards.rewardsapi.common.exception.ServiceException;
import uoc.rewards.rewardsapi.config.properties.JWTProperties;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT Reader combines many JWT Verifiers and helps decode tokens into pojo objects safely
 */
public class JWTWritter {

    private static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(15);

    private JWTProperties jwtProperties;
    private Algorithm algorithm;

    public JWTWritter(JWTProperties jwtProperties, Algorithm algorithm) {
        this.jwtProperties = jwtProperties;
        this.algorithm = algorithm;
    }

    public String generateToken(String subject) {
        try {

            return JWT.create()
                    .withSubject(subject)
                    .withIssuer(jwtProperties.getIssuer())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);

        } catch (Exception e) {
            throw new ServiceException(String.format("Error creating JWT for Org. %s. Reason: %s", subject, e.getMessage()));
        }
    }
}

package uoc.rewards.rewardsapi.config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uoc.rewards.rewardsapi.config.properties.JWTProperties;
import uoc.rewards.rewardsapi.security.jwt.JWTReader;
import uoc.rewards.rewardsapi.security.jwt.JWTWritter;
import uoc.rewards.rewardsapi.util.JWTPemUtils;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * JWT Configuration class holds bean factory methods for creating JWTReaders and JWTWriters.
 */
@Configuration
public class JWTConfig {

    @Bean
    public Algorithm algorithm() throws IOException {
        return Algorithm.RSA256(
            (RSAPublicKey) JWTPemUtils.readPublicKeyFromFile(getClass().getClassLoader().getResourceAsStream("certificates/public.pem").readAllBytes()),
            (RSAPrivateKey) JWTPemUtils.readPrivateKeyFromFile(getClass().getClassLoader().getResourceAsStream("certificates/privatepkcs8.pem").readAllBytes())
        );
    }


    @Bean
    public JWTReader jwtReader(ObjectMapper mapper, Algorithm algorithm) {
        return new JWTReader(
                mapper,
                JWT.require(algorithm).build());
    }

    @Bean
    public JWTWritter jwtWritter(JWTProperties jwtProperties, Algorithm algorithm) {
        return new JWTWritter(jwtProperties, algorithm);
    }
}

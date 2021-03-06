package uoc.rewards.rewardsapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uoc.rewards.rewardsapi.filter.JWTAuthenticationFilter;
import uoc.rewards.rewardsapi.filter.JWTAuthorizationFilter;
import uoc.rewards.rewardsapi.security.jwt.JWTReader;
import uoc.rewards.rewardsapi.security.jwt.JWTWritter;
import uoc.rewards.rewardsapi.service.UserDetailsServiceImpl;

@EnableWebSecurity
public class FilterConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private JWTReader jwtReader;
    private JWTWritter jwtWritter;
    private ObjectMapper mapper;

    public FilterConfig(UserDetailsServiceImpl userDetailsService, JWTReader jwtReader, JWTWritter jwtWritter, ObjectMapper mapper) {
        this.userDetailsService = userDetailsService;
        this.jwtReader = jwtReader;
        this.jwtWritter = jwtWritter;
        this.mapper = mapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtWritter, mapper))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtReader, mapper))
             // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}

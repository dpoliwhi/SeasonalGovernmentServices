package ru.didenko.smartconsulting.seasonalservices.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

/**
 * Class to configure security, filter and to divide the possibilities of each role
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class JwtSecurityConfig implements WebMvcConfigurer {

    private final JwtTokenFilter jwtTokenFilter;

    public JwtSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs", "/swagger-resources",
            "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui.html",
            "/swagger-ui.html/**", "/webjars/**",
            "/v3/api-docs/**", "/swagger-ui/**",
            "/css/**", "/img/**", "/js/**", "/encode/*",

            "/rest/user/auth", "/rest/user/create",
            "/rest/services/list-allowed",
    };

    private static final String[] USER_ACCESS = {
            "/rest/user/update/*", "/rest/user/get-one/*",
            "/rest/services/get-one/*",
            "/rest/application/create-application",
            "/rest/application/get-confirmed-applications-of-user/*",
            "/rest/application/get-all-applications-of-user/*",
            "/rest/application/get-one/*"
    };

    private static final String[] MANAGER_ACCESS = {
            "/rest/user/user-applications", "/rest/user/list", "/rest/user/delete/*",
            "/rest/services/create", "/rest/services/update/*",
            "/rest/services/list", "/rest/services/delete/*",
            "/rest/application/list",
            "/rest/application/get-not-confirmed-applications",
            "/rest/application/get-all-confirmed-applications",
            "/rest/application/get-applications-to-one-service/*"
    };

    private static final String[] ADMIN_ACCESS = {"/rest/**"};

    /**
     * Main filter called for every request, checks all mappings and divide it for user roles
     */
    @Bean
    public SecurityFilterChain filterChainJwt(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        ))
                .and().authorizeRequests()
                .antMatchers(USER_ACCESS).hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers(MANAGER_ACCESS).hasAnyRole("MANAGER", "ADMIN")
                .antMatchers(ADMIN_ACCESS).hasRole("ADMIN")
                .and()
                //JWT Token VALID or NOT
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
